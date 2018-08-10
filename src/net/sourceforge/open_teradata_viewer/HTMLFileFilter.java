/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import net.sourceforge.open_teradata_viewer.util.Utilities;

/**
 * A file filter for <code>JFileChooser</code>s that filters everything except
 * HTML files.
 *
 * @author D. Campione
 * 
 */
public class HTMLFileFilter extends FileFilter {

    /**
     * Accept all directories and all *.html/*.htm files.
     *
     * @param f The file to check.
     * @return Whether the file passes this filter.
     */
    public boolean accept(File f) {
        // Accept all directories
        if (f.isDirectory()) {
            return true;
        }
        String extension = Utilities.getExtension(f.getName());
        return extension != null
                && (extension.equalsIgnoreCase("htm") || extension
                        .equalsIgnoreCase("html"));
    }

    // The description of this filter
    public String getDescription() {
        return "HTML files (*.htm, *.html)";
    }
}