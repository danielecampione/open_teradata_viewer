/*
 * Open Teradata Viewer ( editor syntax )
 * Copyright (C) 2012, D. Campione
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The location of a local file.
 *
 * @author D. Campione
 * 
 */
class LocalFileLocation extends FileLocation {

    /** The file. This may or may not actually exist. */
    private File file;

    /**
     * Ctor.
     *
     * @param file The local file.
     */
    public LocalFileLocation(File file) {
        try {
            // Useful on Windows and OS X
            this.file = file.getCanonicalFile();
        } catch (IOException ioe) {
            this.file = file;
        }
    }

    /**
     * Returns the last time this file was modified, or {@link
     * TextEditorPane#LAST_MODIFIED_UNKNOWN} if this value cannot be computed
     * (such as for a remote file).
     *
     * @return The last time this file was modified.
     */
    protected long getActualLastModified() {
        return file.lastModified();
    }

    /**
     * Returns the full path to the file.
     *
     * @return The full path to the file.
     * @see #getFileName()
     */
    public String getFileFullPath() {
        return file.getAbsolutePath();
    }

    /**
     * Returns the name of the file.
     *
     * @return The name of the file.
     * @see #getFileFullPath()
     */
    public String getFileName() {
        return file.getName();
    }

    /**
     * Opens an input stream for reading from this file.
     *
     * @return The input stream.
     * @throws IOException If the file does not exist, or some other IO error
     *                     occurs.
     */
    protected InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    /**
     * Opens an output stream for writing this file.
     *
     * @return An output stream.
     * @throws IOException If an IO error occurs.
     */
    protected OutputStream getOutputStream() throws IOException {
        return new FileOutputStream(file);
    }

    /**
     * @return Whether this file location is a local file.
     * @see #isLocalAndExists()
     */
    public boolean isLocal() {
        return true;
    }

    /**
     * @return Whether this file location is a local file and already exists
     * @see #isLocal()
     */
    public boolean isLocalAndExists() {
        return file.exists();
    }
}