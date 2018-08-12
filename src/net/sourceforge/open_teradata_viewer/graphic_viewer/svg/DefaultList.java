/*
 * Open Teradata Viewer ( graphic viewer svg )
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

package net.sourceforge.open_teradata_viewer.graphic_viewer.svg;


import net.sourceforge.open_teradata_viewer.graphic_viewer.IDomList;
import net.sourceforge.open_teradata_viewer.graphic_viewer.IDomNode;

import org.w3c.dom.NodeList;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class DefaultList implements IDomList {

    public DefaultList(NodeList nodelist) {
        a = nodelist;
    }

    public NodeList getNodeList() {
        return a;
    }

    public int getLength() {
        return a.getLength();
    }

    public IDomNode item(int i) {
        org.w3c.dom.Node node = a.item(i);
        return new AbstractNode(node);
    }

    private NodeList a;
}