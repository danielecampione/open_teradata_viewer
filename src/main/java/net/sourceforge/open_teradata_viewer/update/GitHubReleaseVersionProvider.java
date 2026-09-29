/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C), D. Campione
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.sourceforge.open_teradata_viewer.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import net.sourceforge.open_teradata_viewer.Config;

/**
 * {@link IUpdateVersionProvider} implementation that determines the latest
 * published version of the application by querying the "latest release" of
 * the project's official GitHub repository through the public,
 * unauthenticated GitHub REST API.
 * <p>
 * This replaces the previous approach of downloading and parsing a plain
 * text file (<code>changes.txt</code>) hosted on SourceForge, which had
 * become unreliable over time (intermittent <code>403 Forbidden</code>
 * responses requiring a spoofed browser <code>User-Agent</code>, etc.).
 * <p>
 * The JSON response is parsed with the <a
 * href="https://github.com/stleary/JSON-java">org.json</a> reference
 * library (a single, dependency-free jar): this makes the extraction of
 * <code>tag_name</code> and <code>published_at</code> a real JSON parse,
 * correct regardless of field order, surrounding whitespace, or
 * additional/nested fields GitHub might add to the response in the future.
 *
 * @author D. Campione
 * 
 */
public class GitHubReleaseVersionProvider implements IUpdateVersionProvider {

    private static final int CONNECT_TIMEOUT_MILLIS = 10000;
    private static final int READ_TIMEOUT_MILLIS = 10000;
    private static final int MAX_ERROR_BODY_LENGTH = 300;

    @Override
    public String getLatestVersionLabel(Proxy proxy) throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(Config.GITHUB_LATEST_RELEASE_API_URL);
            connection = (HttpURLConnection) url.openConnection(proxy);

            // The GitHub API rejects requests that do not provide a
            // User-Agent header
            connection.setRequestProperty("User-Agent",
                    "open_teradata_viewer-update-checker");
            connection.setRequestProperty("Accept",
                    "application/vnd.github+json");
            connection.setConnectTimeout(CONNECT_TIMEOUT_MILLIS);
            connection.setReadTimeout(READ_TIMEOUT_MILLIS);

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException(
                        "The GitHub API returned HTTP status " + responseCode
                                + " while checking for the latest release"
                                + describe(readErrorBody(connection)));
            }

            String body = readBody(connection);

            try {
                JSONObject release = new JSONObject(body);
                String tagName = release.getString("tag_name");
                String publishedAt = release.getString("published_at");
                return toVersionLabel(tagName, publishedAt);
            } catch (JSONException je) {
                throw new IOException("Unexpected response format returned "
                        + "by the GitHub API while checking for the latest "
                        + "release", je);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * Builds a version label matching the format used by the locally
     * embedded <code>changes.txt</code> resource, e.g.
     * <code>"0.3c (07/07/2026)"</code>, so that the date-based comparison
     * performed by
     * {@link net.sourceforge.open_teradata_viewer.UpdateChecker} keeps
     * working unchanged.
     */
    private static String toVersionLabel(String tagName, String publishedAt)
            throws IOException {
        try {
            Date publishedDate = Date.from(Instant.parse(publishedAt));
            String formattedDate = new SimpleDateFormat("dd/MM/yyyy")
                    .format(publishedDate);
            return tagName + " (" + formattedDate + ")";
        } catch (DateTimeParseException dtpe) {
            throw new IOException("Unable to parse the release date '"
                    + publishedAt + "' returned by the GitHub API", dtpe);
        }
    }

    private static String readBody(HttpURLConnection connection)
            throws IOException {
        StringBuilder body = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(),
                        StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
        }
        return body.toString();
    }

    private static String readErrorBody(HttpURLConnection connection) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getErrorStream(),
                        StandardCharsets.UTF_8))) {
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null
                    && body.length() < MAX_ERROR_BODY_LENGTH) {
                body.append(line);
            }
            return body.toString();
        } catch (Exception e) {
            return "";
        }
    }

    private static String describe(String errorBody) {
        return errorBody.isEmpty() ? "" : (" (" + errorBody + ")");
    }
}
