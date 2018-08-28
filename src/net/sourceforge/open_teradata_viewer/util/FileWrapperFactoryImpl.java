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

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * Implementation of factory that produces FileWrappers which mimic the
 * interface of java.io.File. All of the public constructors of java.io.File can
 * be accessed using the analogous factory methods given here which accept and
 * return FileWrappers instead of Files. The original java.io.File constructor
 * javadoc comments are used here to help choose the right factory method.
 * 
 * @author D. Campione
 * 
 */
public class FileWrapperFactoryImpl implements IFileWrapperFactory {

    /**
     * @see net.sourceforge.open_teradata_viewer.util.IFileWrapperFactory#create(net.sourceforge.open_teradata_viewer.util.FileWrapperImpl)
     */
    public IFileWrapper create(FileWrapperImpl impl) {
        return new FileWrapperImpl(impl);
    }

    /**
     * @see net.sourceforge.open_teradata_viewer.util.IFileWrapperFactory#create(java.lang.String)
     */
    public IFileWrapper create(String pathname) {
        return new FileWrapperImpl(pathname);
    }

    /**
     * @see net.sourceforge.open_teradata_viewer.util.IFileWrapperFactory#create(java.lang.String, java.lang.String)
     */
    public IFileWrapper create(String parent, String child) {
        return new FileWrapperImpl(parent, child);
    }

    /**
     * @see net.sourceforge.open_teradata_viewer.util.IFileWrapperFactory#create(net.sourceforge.open_teradata_viewer.util.IFileWrapper, java.lang.String)
     */
    public IFileWrapper create(IFileWrapper parent, String child) {
        return new FileWrapperImpl(parent, child);
    }

    /**
     * @see net.sourceforge.open_teradata_viewer.util.IFileWrapperFactory#create(java.net.URI)
     */
    public IFileWrapper create(URI uri) {
        return new FileWrapperImpl(uri);
    }

    /**
     * @see net.sourceforge.open_teradata_viewer.util.IFileWrapperFactory#create(java.io.File)
     */
    public IFileWrapper create(File f) {
        return new FileWrapperImpl(f);
    }

    public FileWrapperImpl createTempFile(String prefix, String suffix,
            IFileWrapper directory) throws IOException {
        return FileWrapperImpl.createTempFile(prefix, suffix,
                (FileWrapperImpl) directory);
    }

    /**
     * @see net.sourceforge.open_teradata_viewer.util.IFileWrapperFactory#createTempFile(java.lang.String, java.lang.String)
     */
    @Override
    public IFileWrapper createTempFile(String prefix, String suffix)
            throws IOException {
        return FileWrapperImpl.createTempFile(prefix, suffix);
    }
}