/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2011, D. Campione
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
import java.util.Locale;

import javax.swing.filechooser.FileFilter;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ExtensionFileFilter extends FileFilter {
    private final String extension;
    private final String description;

    public ExtensionFileFilter(String extension, String description) {
        this.extension = extension.toLowerCase();
        this.description = description;
    }

    public boolean accept(File f) {
        return f.isDirectory()
                || "*".equals(extension)
                || f.getName().toLowerCase(Locale.getDefault())
                        .endsWith(extension);
    }

    public String getDescription() {
        return description;
    }
}
