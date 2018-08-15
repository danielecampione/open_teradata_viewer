/*
 * Open Teradata Viewer ( editor syntax )
 * Copyright (C) 2014, D. Campione
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

package net.sourceforge.open_teradata_viewer.editor.syntax;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * The location of a file at a (remote) URL.
 *
 * @author D. Campione
 * 
 */
class URLFileLocation extends FileLocation {

    /** URL of the remote file. */
    private URL url;

    /** A prettied-up full path of the URL (password removed, etc.). */
    private String fileFullPath;

    /**
     * A prettied-up filename (leading slash, and possibly "<code>%2F</code>",
     * removed).
     */
    private String fileName;

    /**
     * Ctor.
     *
     * @param url The URL of the file.
     */
    URLFileLocation(URL url) {
        this.url = url;
        fileFullPath = createFileFullPath();
        fileName = createFileName();
    }

    /**
     * Creates a "prettied-up" URL to use. This will be stripped of sensitive
     * information such as passwords.
     *
     * @return The full path to use.
     */
    private String createFileFullPath() {
        String fullPath = url.toString();
        fullPath = fullPath.replaceFirst("://([^:]+)(?:.+)@", "://$1@");
        return fullPath;
    }

    /**
     * Creates the "prettied-up" filename to use.
     *
     * @return The base name of the file of this URL.
     */
    private String createFileName() {
        String fileName = url.getPath();
        if (fileName.startsWith("/%2F/")) { // Absolute path
            fileName = fileName.substring(4);
        } else if (fileName.startsWith("/")) { // All others
            fileName = fileName.substring(1);
        }
        return fileName;
    }

    /**
     * Returns the last time this file was modified, or {@link
     * TextEditorPane#LAST_MODIFIED_UNKNOWN} if this value cannot be computed
     * (such as for a remote file).
     *
     * @return The last time this file was modified. This will always be {@link
     *         TextEditorPane#LAST_MODIFIED_UNKNOWN} for URL's.
     */
    @Override
    protected long getActualLastModified() {
        return TextEditorPane.LAST_MODIFIED_UNKNOWN;
    }

    /** {@inheritDoc} */
    @Override
    public String getFileFullPath() {
        return fileFullPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFileName() {
        return fileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected InputStream getInputStream() throws IOException {
        return url.openStream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected OutputStream getOutputStream() throws IOException {
        return url.openConnection().getOutputStream();
    }

    /**
     * Returns whether this file location is a local file.
     *
     * @return Whether this is a local file.
     * @see #isLocalAndExists()
     */
    @Override
    public boolean isLocal() {
        return "file".equalsIgnoreCase(url.getProtocol());
    }

    /**
     * Returns whether this file location is a local file and already exists. 
     * This method always returns <code>false</code> since we cannot check this
     * value easily.
     *
     * @return <code>false</code> always.
     * @see #isLocal()
     */
    @Override
    public boolean isLocalAndExists() {
        return false;
    }
}