/*
 * Open Teradata Viewer ( graphic viewer )
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

package net.sourceforge.open_teradata_viewer.graphicviewer;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class SimpleNodeLabel extends GraphicViewerText {

    private static final long serialVersionUID = 8232384774478384492L;

    public SimpleNodeLabel() {
    }

    public SimpleNodeLabel(String s, GraphicViewerArea graphicviewerarea) {
        super(s);
        initialize(s, graphicviewerarea);
    }

    public void initialize(String s, GraphicViewerArea graphicviewerarea) {
        setSelectable(false);
        setDraggable(false);
        setResizable(false);
        setVisible(true);
        setEditable(true);
        setEditOnSingleClick(true);
        setTransparent(true);
        setAlignment(2);
        setTopLeft(graphicviewerarea.getLeft(), graphicviewerarea.getTop());
        graphicviewerarea.addObjectAtTail(this);
    }

    public void SVGWriteObject(DomDoc domdoc, DomElement domelement) {
        @SuppressWarnings("unused")
        DomElement domelement1;
        if (domdoc.GraphicViewerXMLOutputEnabled())
            domelement1 = domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphicviewer.SimpleNodeLabel",
                    domelement);
        super.SVGWriteObject(domdoc, domelement);
    }

    public DomNode SVGReadObject(DomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, DomElement domelement,
            DomElement domelement1) {
        if (domelement1 != null)
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingElement());
        return domelement.getNextSibling();
    }
}