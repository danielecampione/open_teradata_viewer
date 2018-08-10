/*
 * Open Teradata Viewer ( graphic viewer layout )
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

package net.sourceforge.open_teradata_viewer.graphicviewer.layout;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class LayeredDigraphAutoLayoutNodeData implements AutoLayoutNodeData {

    public LayeredDigraphAutoLayoutNodeData() {
    }

    public int layer;
    public int column;
    public int index;
    public boolean valid;
    public int discover;
    public int finish;
    public int component;
}