/*
 * Open Teradata Viewer ( editor autocomplete )
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

package net.sourceforge.open_teradata_viewer.editor.autocomplete;

import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;

import javax.swing.Icon;

/**
 * A standard icon that doesn't paint anything. This can be used when some
 * <code>ICompletion</code>s have icons and others don't, to visually align the
 * text of all completions.
 *
 * @author D. Campione
 * 
 */
public class EmptyIcon implements Icon, Serializable {

    private static final long serialVersionUID = -1325513067897195554L;

    private int size;

    public EmptyIcon(int size) {
        this.size = size;
    }

    public int getIconHeight() {
        return size;
    }

    public int getIconWidth() {
        return size;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
    }
}