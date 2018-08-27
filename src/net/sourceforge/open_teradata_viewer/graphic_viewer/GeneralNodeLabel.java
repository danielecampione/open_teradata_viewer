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

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GeneralNodeLabel extends GraphicViewerText {

    private static final long serialVersionUID = 6345566824806969549L;

    public GeneralNodeLabel() {
    }

    public GeneralNodeLabel(String s, GeneralNode generalnode) {
        super(s);
        initialize(s, generalnode);
    }

    public void initialize(String s, GeneralNode generalnode) {
        setSelectable(false);
        setDraggable(false);
        setResizable(false);
        setVisible(true);
        setEditable(true);
        setEditOnSingleClick(true);
        setTransparent(true);
        setAlignment(2);
        setTopLeft(generalnode.getLeft(), generalnode.getTop());
        generalnode.addObjectAtTail(this);
    }

    public GeneralNode getGeneralNode() {
        return (GeneralNode) getParent();
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphic_viewer.GeneralNodeLabel",
                    domelement);
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, IDomElement domelement,
            IDomElement domelement1) {
        if (domelement1 != null)
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        return domelement.getNextSibling();
    }
}