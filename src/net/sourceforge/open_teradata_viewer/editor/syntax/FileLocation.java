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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Holds the location of a local or remote file. This provides a common way to
 * read, write, and check properties of both local and remote files.
 *
 * @author D. Campione
 * 
 */
public abstract class FileLocation {

    /**
     * Creates a {@link FileLocation} instance for the specified local file.
     *
     * @param fileFullPath The full path to a local file.
     * @return The file's location.
     */
    public static FileLocation create(String fileFullPath) {
        return new FileFileLocation(new File(fileFullPath));
    }

    /**
     * Creates a {@link FileLocation} instance for the specified local file.
     *
     * @param file A local file.
     * @return The file's location.
     */
    public static FileLocation create(File file) {
        return new FileFileLocation(file);
    }

    /**
     * Creates a {@link FileLocation} instance for the specified file.
     *
     * @param url The URL of a file.
     * @return The file's location.
     */
    public static FileLocation create(URL url) {
        if ("file".equalsIgnoreCase(url.getProtocol())) {
            return new FileFileLocation(new File(url.getPath()));
        }
        return new URLFileLocation(url);
    }

    /**
     * Returns the last time this file was modified, or {@link
     * TextEditorPane#LAST_MODIFIED_UNKNOWN} if this value cannot be computed
     * (such as for a remote file).
     *
     * @return The last time this file was modified.
     */
    protected abstract long getActualLastModified();

    /**
     * Returns the full path to the file. This will be stripped of sensitive
     * information such as passwords for remote files.
     *
     * @return The full path to the file.
     * @see #getFileName()
     */
    public abstract String getFileFullPath();

    /**
     * Returns the name of the file.
     *
     * @return The name of the file.
     * @see #getFileFullPath()
     */
    public abstract String getFileName();

    /**
     * Opens an input stream for reading from this file.
     *
     * @return The input stream.
     * @throws IOException If the file does not exist, or some other IO error
     *         occurs.
     */
    protected abstract InputStream getInputStream() throws IOException;

    /**
     * Opens an output stream for writing this file.
     *
     * @return An output stream.
     * @throws IOException If an IO error occurs.
     */
    protected abstract OutputStream getOutputStream() throws IOException;

    /**
     * Returns whether this file location is a local file.
     *
     * @return Whether this is a local file.
     * @see #isLocalAndExists()
     */
    public abstract boolean isLocal();

    /**
     * Returns whether this file location is a local file that already
     * exists.
     *
     * @return Whether this file is local and actually exists.
     * @see #isLocal()
     */
    public abstract boolean isLocalAndExists();

    /**
     * Returns whether this file location is a remote location.
     *
     * @return Whether this is a remote file location.
     */
    public boolean isRemote() {
        return !isLocal();
    }
}