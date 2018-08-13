/*
 * Open Teradata Viewer ( editor syntax )
 * Copyright (C) 2013, D. Campione
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
 */
class FileFileLocation extends FileLocation {

    /** The file. This may or may not actually exist. */
    private File file;

    /**
     * Ctor.
     *
     * @param file The local file.
     */
    public FileFileLocation(File file) {
        try {
            // Useful on Windows and OS X
            this.file = file.getCanonicalFile();
        } catch (IOException ioe) {
            this.file = file;
        }
    }

    /** {@inheritDoc} */
    @Override
    protected long getActualLastModified() {
        return file.lastModified();
    }

    /**
     * Returns the full path to the file.
     *
     * @return The full path to the file.
     * @see #getFileName()
     */
    @Override
    public String getFileFullPath() {
        return file.getAbsolutePath();
    }

    /** {@inheritDoc} */
    @Override
    public String getFileName() {
        return file.getName();
    }

    /** {@inheritDoc} */
    @Override
    protected InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }

    /** {@inheritDoc} */
    @Override
    protected OutputStream getOutputStream() throws IOException {
        return new FileOutputStream(file);
    }

    /**
     * Always returns <code>true</code>.
     *
     * @return <code>true</code> always.
     * @see #isLocalAndExists()
     */
    @Override
    public boolean isLocal() {
        return true;
    }

    /**
     * Since file locations of this type are guaranteed to be local, this
     * method returns whether the file exists.
     *
     * @return Whether this local file actually exists.
     * @see #isLocal()
     */
    @Override
    public boolean isLocalAndExists() {
        return file.exists();
    }
}