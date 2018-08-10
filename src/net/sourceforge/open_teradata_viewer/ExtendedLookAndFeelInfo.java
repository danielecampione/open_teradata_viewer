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

import javax.swing.UIManager;

/**
 * Information about a 3rd party Look and Feel in a JAR file.
 *
 * @author D. Campione
 * 
 */
public class ExtendedLookAndFeelInfo extends UIManager.LookAndFeelInfo {

    /**
     * Ctor.
     *
     * @param name The name of the Look and Feel.
     * @param className The name of the main class of the Look and Feel.
     */
    public ExtendedLookAndFeelInfo(String name, String className) {
        super(name, className);
    }
}