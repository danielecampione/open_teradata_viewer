/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.io.File;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public interface IXFileDialogs {

    File saveAs(Object action, String title, String extension, String fileType,
            File defaultFile);

    File saveAs(Object action, String title);

    File saveAsDirectory(Object action, String title, File defaultDirectory);

    File open(Object action, String title, String extension, String fileType,
            String current);

    File openXML(Object action, String title);

    File openDirectory(Object action, String string, File defaultDirectory);

    File openFileOrDirectory(Object action, String title, File defaultDirectory);
}