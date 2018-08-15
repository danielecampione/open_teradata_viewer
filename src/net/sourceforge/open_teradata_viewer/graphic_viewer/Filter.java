/*
 * Open Teradata Viewer ( graphic viewer )
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

package net.sourceforge.open_teradata_viewer.graphic_viewer;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class Filter extends FileFilter {

    public Filter(String sExt, String sDescr) {
        super();
        myExtension = sExt.toLowerCase();
        myDescription = sDescr;
    }

    // Accept all directories and all .xsvg files.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        //String extension = java.Utils.getExtension(f);
        String sPath = f.getPath();
        sPath.toLowerCase();
        if (sPath.indexOf(".") != -1) {
            if (sPath.endsWith(myExtension)) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    // The description of this filter
    public String getDescription() {
        return myDescription;
    }

    public String getExtension() {
        return myExtension;
    }

    private String myExtension;
    private String myDescription;
}
