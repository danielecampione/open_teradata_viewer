/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.io.File;

import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

/**
 * File filter that accepts all files.
 *
 * @author D. Campione
 * 
 */
public class AcceptAllFileFilter extends FileFilter {

    public boolean accept(File f) {
        return true;
    }

    public String getDescription() {
        return UIManager.getString("FileChooser.acceptAllFileFilterText");
    }

    /**
     * Overridden to return the description of this file filter, that way we
     * render nicely in combo boxes.
     *
     * @return A string representation of this filter.
     */
    public String toString() {
        return getDescription();
    }
}