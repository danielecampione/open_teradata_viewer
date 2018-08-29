/*
 * Open Teradata Viewer ( util )
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

package net.sourceforge.open_teradata_viewer.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Obligatory IO utilities.
 *
 * @author D. Campione
 * 
 */
public final class IOUtil {

    /** Private constructor to prevent instantiation. */
    private IOUtil() {
    }

    /**
     * Reads all text from an input stream. The stream will be closed when
     * this method returns.
     *
     * @param in The input stream to read from. Will be closed on return.
     * @return The text read from the stream.
     * @throws IOException If an IO error occurs.
     */
    public static String readFully(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        String line = null;
        try {
            while ((line = r.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } finally {
            r.close();
        }
        return sb.toString();
    }
}