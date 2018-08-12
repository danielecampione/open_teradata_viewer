/*
 * Open Teradata Viewer ( graphic viewer )
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

package net.sourceforge.open_teradata_viewer.graphic_viewer;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GeneralNodePortLabel extends GraphicViewerText {

    private static final long serialVersionUID = -9161703208116856106L;

    public GeneralNodePortLabel() {
        myPort = null;
    }

    public GeneralNodePortLabel(String s, GeneralNodePort generalnodeport) {
        super(s);
        myPort = null;
        initialize(s, generalnodeport);
    }

    public void initialize(String s, GeneralNodePort generalnodeport) {
        setSelectable(false);
        setDraggable(false);
        setResizable(false);
        setVisible(true);
        setEditable(true);
        setEditOnSingleClick(true);
        setTransparent(true);
        setFontSize(getFontSize() - 1);
        GraphicViewerArea graphicviewerarea = generalnodeport.getParent();
        setTopLeft(graphicviewerarea.getLeft(), graphicviewerarea.getTop());
        graphicviewerarea.addObjectAtTail(this);
        generalnodeport.setLabel(this);
    }

    public void setText(String s) {
        if (!getText().equals(s)) {
            super.setText(s);
            if (getPort() != null)
                getPort().setName(getText());
        }
    }

    public void SVGUpdateReference(String s, Object obj) {
        super.SVGUpdateReference(s, obj);
        if (s.equals("port"))
            myPort = (GeneralNodePort) obj;
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphic_viewer.GeneralNodePortLabel",
                    domelement);
            if (myPort != null)
                domdoc.registerReferencingNode(domelement1, "port", myPort);
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, IDomElement domelement,
            IDomElement domelement1) {
        if (domelement1 != null) {
            String s = domelement1.getAttribute("port");
            domdoc.registerReferencingObject(this, "port", s);
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        }
        return domelement.getNextSibling();
    }

    public GeneralNodePort getPort() {
        return myPort;
    }

    void setPort(GeneralNodePort generalnodeport) {
        myPort = generalnodeport;
    }

    private GeneralNodePort myPort;
}