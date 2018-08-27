/*
 * Open Teradata Viewer ( graphic viewer )
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

package net.sourceforge.open_teradata_viewer.graphic_viewer;

import java.awt.Dimension;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class SanKeyNode extends GraphicViewerIconicNode {

    private static final long serialVersionUID = -5452280417994883795L;

    public SanKeyNode() {
        super();
    }

    public SanKeyNode(String s) {
        super(s);
    }

    public GraphicViewerPort createPort() {
        SanKeyPort p = new SanKeyPort();
        p.setSize(new Dimension(7, 7));
        p.setPortObject(getIcon());
        return p;
    }
}