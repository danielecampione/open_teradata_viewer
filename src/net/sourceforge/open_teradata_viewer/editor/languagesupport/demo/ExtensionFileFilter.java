/*
 * Open Teradata Viewer ( editor language support demo )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.demo;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * A file filter for opening files with a specific extension.
 *
 * @author D. Campione
 *
 */
class ExtensionFileFilter extends FileFilter {

    private String desc;
    private String ext;

    /**
     * Ctor.
     *
     * @param desc A description of the file type.
     * @param ext The extension of the file type.
     */
    public ExtensionFileFilter(String desc, String ext) {
        this.desc = desc;
        this.ext = ext;
    }

    /** {@inheritDoc} */
    @Override
    public boolean accept(File f) {
        return f.isDirectory() || f.getName().endsWith(ext);
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription() {
        return desc + " (*." + ext + ")";
    }
}