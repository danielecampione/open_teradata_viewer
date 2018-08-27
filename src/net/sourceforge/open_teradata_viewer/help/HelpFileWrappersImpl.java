/*
 * Open Teradata Viewer ( help )
 * Copyright (C) 2015, D. Campione
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

package net.sourceforge.open_teradata_viewer.help;

import net.sourceforge.open_teradata_viewer.util.FileWrapperFactoryImpl;
import net.sourceforge.open_teradata_viewer.util.IFileWrapper;
import net.sourceforge.open_teradata_viewer.util.IFileWrapperFactory;

/**
 * This class wraps access to HelpFiles presenting an interface that consists of
 * FileWrappers instead of Files.
 * 
 * @author D. Campione
 * 
 */
public class HelpFileWrappersImpl implements IHelpFileWrappers {

    private IFileWrapperFactory _fileWrapperFactory = new FileWrapperFactoryImpl();

    /**
     * @see net.sourceforge.open_teradata_viewer.util.IHelpFileWrappers#setFileWrapperFactory(net.sourceforge.open_teradata_viewer.util.IFileWrapperFactory)
     */
    public void setFileWrapperFactory(IFileWrapperFactory factory) {
        _fileWrapperFactory = factory;
    }

    private HelpFiles _helpFiles = new HelpFiles();

    /**
     * @see net.sourceforge.open_teradata_viewer.util.IHelpFileWrappers#setHelpFiles(net.sourceforge.open_teradata_viewer.help.HelpFiles)
     */
    public void setHelpFiles(HelpFiles files) {
        _helpFiles = files;
    }

    public HelpFileWrappersImpl() {
    }

    /**
     * @see net.sourceforge.IHelpFileWrappers.client.util.HelpFileWrappers#getQuickStartGuideFile()
     */
    public IFileWrapper getQuickStartGuideFile() {
        return _fileWrapperFactory.create(_helpFiles.getQuickStartGuideFile());
    }

    /**
     * @see net.sourceforge.open_teradata_viewer.util.IHelpFileWrappers#getFAQFile()
     */
    public IFileWrapper getFAQFile() {
        return _fileWrapperFactory.create(_helpFiles.getFAQFile());
    }

    /**
     * @see net.sourceforge.open_teradata_viewer.util.IHelpFileWrappers#getChangeLogFile()
     */
    public IFileWrapper getChangeLogFile() {
        return _fileWrapperFactory.create(_helpFiles.getChangeLogFile());
    }

    /**
     * @see net.sourceforge.open_teradata_viewer.util.IHelpFileWrappers#getLicenceFile()
     */
    public IFileWrapper getLicenceFile() {
        return _fileWrapperFactory.create(_helpFiles.getLicenceFile());
    }
}