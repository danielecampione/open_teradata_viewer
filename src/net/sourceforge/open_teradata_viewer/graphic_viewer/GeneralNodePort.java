/*
 * Open Teradata Viewer ( graphic viewer )
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

package net.sourceforge.open_teradata_viewer.graphic_viewer;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GeneralNodePort extends GraphicViewerPort {

    private static final long serialVersionUID = 6034782618020815813L;

    public GeneralNodePort() {
        myLeftSide = true;
        myIndex = 0;
        myPortLabel = null;
        myName = "";
    }

    public GeneralNodePort(boolean flag, String s,
            GraphicViewerArea graphicviewerarea) {
        super(TriangleRect());
        myLeftSide = true;
        myIndex = 0;
        myPortLabel = null;
        myName = "";
        initialize(flag, s, graphicviewerarea);
    }

    public void initialize(boolean flag, String s,
            GraphicViewerArea graphicviewerarea) {
        setSelectable(false);
        setDraggable(false);
        setResizable(false);
        setVisible(true);
        setStyle(3);
        setPen(GraphicViewerPen.darkGray);
        setBrush(GraphicViewerBrush.lightGray);
        if (flag) {
            myLeftSide = true;
            setValidSource(false);
            setValidDestination(true);
            setToSpot(8);
        } else {
            myLeftSide = false;
            setValidSource(true);
            setValidDestination(false);
            setFromSpot(4);
        }
        setTopLeft(graphicviewerarea.getLeft(), graphicviewerarea.getTop());
        graphicviewerarea.addObjectAtTail(this);
        setName(s);
    }

    public final boolean isInput() {
        return isValidDestination();
    }

    public final boolean isOutput() {
        return isValidSource();
    }

    public boolean validLink(GraphicViewerPort graphicviewerport) {
        if (graphicviewerport.getParent() == null)
            return false;
        if (graphicviewerport instanceof GeneralNodePort)
            return super.validLink(graphicviewerport)
                    && getParent() != graphicviewerport.getParent()
                    && isOutput()
                    && (graphicviewerport instanceof GeneralNodePort)
                    && ((GeneralNodePort) graphicviewerport).isInput()
                    && !alreadyLinked(graphicviewerport);
        else
            return super.validLink(graphicviewerport)
                    && getParent() != graphicviewerport.getParent()
                    && isOutput();
    }

    public boolean alreadyLinked(GraphicViewerPort graphicviewerport) {
        for (GraphicViewerListPosition graphicviewerlistposition = getFirstLinkPos(); graphicviewerlistposition != null;) {
            GraphicViewerLink graphicviewerlink = getLinkAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextLinkPos(graphicviewerlistposition);
            if (graphicviewerlink.getFromPort() == this
                    && graphicviewerlink.getToPort() == graphicviewerport)
                return true;
        }

        return false;
    }

    public boolean isOnLeftSide() {
        return myLeftSide;
    }

    public int getIndex() {
        return myIndex;
    }

    void setSideIndex(boolean flag, int i) {
        myLeftSide = flag;
        myIndex = i;
    }

    public GeneralNodePortLabel getLabel() {
        return myPortLabel;
    }

    public void setLabel(GeneralNodePortLabel generalnodeportlabel) {
        GeneralNodePortLabel generalnodeportlabel1 = myPortLabel;
        if (generalnodeportlabel1 != generalnodeportlabel) {
            if (generalnodeportlabel1 != null) {
                if (getParent() != null)
                    getParent().removeObject(generalnodeportlabel1);
                generalnodeportlabel1.setPartner(null);
            }
            myPortLabel = generalnodeportlabel;
            if (generalnodeportlabel != null) {
                generalnodeportlabel.setPartner(this);
                if (isOnLeftSide())
                    generalnodeportlabel.setAlignment(3);
                else
                    generalnodeportlabel.setAlignment(1);
                if (getParent() != null)
                    getParent().addObjectAtTail(generalnodeportlabel);
            }
            layoutLabel();
            update(0x1271f, 0, generalnodeportlabel1);
        }
    }

    public void layoutLabel() {
        if (getLabel() != null)
            if (isOnLeftSide()) {
                getLabel().setSpotLocation(4, this, 8);
                getLabel().setLeft(getLabel().getLeft() - getLabelSpacing());
            } else {
                getLabel().setSpotLocation(8, this, 4);
                getLabel().setLeft(getLabel().getLeft() + getLabelSpacing());
            }
    }

    public int getLabelSpacing() {
        return 2;
    }

    public Point getLinkPoint(int i, Point point) {
        switch (i) {
            default :
                return super.getLinkPoint(i, point);

            case 4 : // '\004'
                Rectangle rectangle = getBoundingRect();
                if (point == null)
                    point = new Point(0, 0);
                point.x = rectangle.x + rectangle.width;
                point.y = rectangle.y + rectangle.height / 2;
                GeneralNodePortLabel generalnodeportlabel = getLabel();
                if (generalnodeportlabel != null
                        && generalnodeportlabel.isVisible())
                    point.x += generalnodeportlabel.getWidth()
                            + getLabelSpacing();
                break;

            case 8 : // '\b'
                Rectangle rectangle1 = getBoundingRect();
                if (point == null)
                    point = new Point(0, 0);
                point.x = rectangle1.x;
                point.y = rectangle1.y + rectangle1.height / 2;
                GeneralNodePortLabel generalnodeportlabel1 = getLabel();
                if (generalnodeportlabel1 != null
                        && generalnodeportlabel1.isVisible())
                    point.x -= generalnodeportlabel1.getWidth()
                            + getLabelSpacing();
                break;
        }
        return point;
    }

    public Point getLinkPointFromPoint(int i, int j, Point point) {
        if (isOnLeftSide())
            return getLinkPoint(8, point);
        else
            return getLinkPoint(4, point);
    }

    public String getName() {
        return myName;
    }

    public void setName(String s) {
        String s1 = myName;
        if (!s1.equals(s)) {
            myName = s;
            if (getLabel() != null)
                getLabel().setText(s);
            update(0x1271e, 0, s1);
        }
    }

    public GeneralNode getNode() {
        return (GeneralNode) getParent();
    }

    public String getToolTipText() {
        return getName();
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 75550 :
                graphicviewerdocumentchangededit.setNewValue(getName());
                return;

            case 75551 :
                graphicviewerdocumentchangededit.setNewValue(getLabel());
                return;
        }
        super.copyNewValueForRedo(graphicviewerdocumentchangededit);
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 75550 :
                setName((String) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 75551 :
                setLabel((GeneralNodePortLabel) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;
        }
        super.changeValue(graphicviewerdocumentchangededit, flag);
    }

    public void SVGUpdateReference(String s, Object obj) {
        super.SVGUpdateReference(s, obj);
        if (s.equals("portlabel"))
            myPortLabel = (GeneralNodePortLabel) obj;
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.GeneralNodePort",
                            domelement);
            domelement1.setAttribute("index", Integer.toString(myIndex));
            domelement1.setAttribute("leftside", myLeftSide ? "true" : "false");
            domelement1.setAttribute("name", myName);
            if (myPortLabel != null)
                domdoc.registerReferencingNode(domelement1, "portlabel",
                        myPortLabel);
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument,
            IDomElement domelement, IDomElement domelement1) {
        if (domelement1 != null) {
            myIndex = Integer.parseInt(domelement1.getAttribute("index"));
            myLeftSide = domelement1.getAttribute("leftside").equals("true");
            myName = domelement1.getAttribute("name");
            String s = domelement1.getAttribute("portlabel");
            domdoc.registerReferencingObject(this, "portlabel", s);
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        }
        return domelement.getNextSibling();
    }

    public static Rectangle TriangleRect() {
        return myTriangleRect;
    }

    public static final int NameChanged = 0x1271e;
    public static final int LabelChanged = 0x1271f;
    private static Rectangle myTriangleRect = new Rectangle(0, 0, 8, 8);
    private boolean myLeftSide;
    private int myIndex;
    private GeneralNodePortLabel myPortLabel;
    private String myName;
}