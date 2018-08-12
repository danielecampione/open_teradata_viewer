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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerPort extends GraphicViewerDrawable
        implements
            IGraphicViewerIdentifiablePart {

    private static final long serialVersionUID = -6212048868696697211L;

    public GraphicViewerPort() {
        dx = -1;
        dw = 2;
        dr = null;
        dB = 4;
        dA = 8;
        ds = new GraphicViewerObjList();
        dz = 10;
        _mthint(2);
    }

    public GraphicViewerPort(Rectangle rectangle) {
        super(rectangle);
        dx = -1;
        dw = 2;
        dr = null;
        dB = 4;
        dA = 8;
        ds = new GraphicViewerObjList();
        dz = 10;
        _mthint(2);
    }

    public GraphicViewerPort(Point point, Dimension dimension) {
        super(point, dimension);
        dx = -1;
        dw = 2;
        dr = null;
        dB = 4;
        dA = 8;
        ds = new GraphicViewerObjList();
        dz = 10;
        _mthint(2);
    }

    public GraphicViewerPort(Rectangle rectangle,
            GraphicViewerObject graphicviewerobject) {
        super(rectangle);
        dx = -1;
        dw = 2;
        dr = null;
        dB = 4;
        dA = 8;
        ds = new GraphicViewerObjList();
        dz = 10;
        _mthint(1);
        if (graphicviewerobject.getLayer() != null)
            graphicviewerobject.getLayer().removeObject(
                    graphicviewerobject.getTopLevelObject());
        else if (graphicviewerobject.getView() != null)
            graphicviewerobject.getView().removeObject(
                    graphicviewerobject.getTopLevelObject());
        graphicviewerobject.setSelectable(false);
        graphicviewerobject.setDraggable(false);
        graphicviewerobject.setResizable(false);
        dr = graphicviewerobject;
    }

    private final void _mthint(int i) {
        dw = i;
        _mthfor(g() & -5 & 0xffffffef | 0x4000 | 0x2000 | 0x40000);
        setBrush(GraphicViewerBrush.black);
    }

    public GraphicViewerObject copyObject(
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerPort graphicviewerport = (GraphicViewerPort) super
                .copyObject(graphicviewercopyenvironment);
        if (graphicviewerport != null) {
            graphicviewerport.dx = dx;
            graphicviewerport._mthfor(graphicviewerport.g() & 0xffff7fff);
            graphicviewerport.dw = dw;
            graphicviewerport.dB = dB;
            graphicviewerport.dA = dA;
            graphicviewerport.dz = dz;
            graphicviewerport.dr = dr;
            if (dr != null)
                graphicviewercopyenvironment.delay(this);
        }
        return graphicviewerport;
    }

    public void copyObjectDelayed(
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment,
            GraphicViewerObject graphicviewerobject) {
        super.copyObjectDelayed(graphicviewercopyenvironment,
                graphicviewerobject);
        GraphicViewerPort graphicviewerport = (GraphicViewerPort) graphicviewerobject;
        GraphicViewerObject graphicviewerobject1 = (GraphicViewerObject) graphicviewercopyenvironment
                .get(dr);
        if (graphicviewerobject1 != null)
            graphicviewerport.dr = graphicviewerobject1;
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerPort",
                            domelement);
            domelement1.setAttribute("partid", Integer.toString(dx));
            domelement1.setAttribute("portstyle", Integer.toString(dw));
            domelement1.setAttribute("fromlinkspot", Integer.toString(dB));
            domelement1.setAttribute("tolinkspot", Integer.toString(dA));
            domelement1.setAttribute("endsegmentlength", Integer.toString(dz));
            if (dr != null) {
                if (!domdoc.isRegisteredReference(dr)) {
                    domelement1.setAttribute("embeddedportobject", "true");
                    IDomElement domelement2 = domdoc.createElement("g");
                    domelement1.appendChild(domelement2);
                    dr.SVGWriteObject(domdoc, domelement2);
                }
                domdoc.registerReferencingNode(domelement1, "portobject", dr);
            }
        }
        if (domdoc.SVGOutputEnabled()) {
            Rectangle rectangle = getBoundingRect();
            int ai[] = new int[4];
            int ai1[] = new int[4];
            switch (dw) {
                case 0 : // '\0'
                    break;

                case 3 : // '\003'
                    switch (getToSpot()) {
                        case 1 : // '\001'
                            ai[0] = rectangle.x + rectangle.width / 2;
                            ai1[0] = rectangle.y;
                            ai[1] = rectangle.x + rectangle.width;
                            ai1[1] = rectangle.y + rectangle.height;
                            ai[2] = rectangle.x;
                            ai1[2] = rectangle.y + rectangle.height / 2;
                            break;

                        case 2 : // '\002'
                            ai[0] = rectangle.x + rectangle.width;
                            ai1[0] = rectangle.y;
                            ai[1] = rectangle.x + rectangle.width / 2;
                            ai1[1] = rectangle.y + rectangle.height;
                            ai[2] = rectangle.x;
                            ai1[2] = rectangle.y;
                            break;

                        case 3 : // '\003'
                            ai[0] = rectangle.x + rectangle.width;
                            ai1[0] = rectangle.y + rectangle.height / 2;
                            ai[1] = rectangle.x;
                            ai1[1] = rectangle.y + rectangle.height;
                            ai[2] = rectangle.x + rectangle.width / 2;
                            ai1[2] = rectangle.y;
                            break;

                        case 4 : // '\004'
                            ai[0] = rectangle.x + rectangle.width;
                            ai1[0] = rectangle.y + rectangle.height;
                            ai[1] = rectangle.x;
                            ai1[1] = rectangle.y + rectangle.height / 2;
                            ai[2] = rectangle.x + rectangle.width;
                            ai1[2] = rectangle.y;
                            break;

                        case 5 : // '\005'
                            ai[0] = rectangle.x + rectangle.width / 2;
                            ai1[0] = rectangle.y + rectangle.height;
                            ai[1] = rectangle.x;
                            ai1[1] = rectangle.y;
                            ai[2] = rectangle.x + rectangle.width;
                            ai1[2] = rectangle.y + rectangle.height / 2;
                            break;

                        case 6 : // '\006'
                            ai[0] = rectangle.x;
                            ai1[0] = rectangle.y + rectangle.height;
                            ai[1] = rectangle.x + rectangle.width / 2;
                            ai1[1] = rectangle.y;
                            ai[2] = rectangle.x + rectangle.width;
                            ai1[2] = rectangle.y + rectangle.height;
                            break;

                        case 7 : // '\007'
                            ai[0] = rectangle.x;
                            ai1[0] = rectangle.y + rectangle.height / 2;
                            ai[1] = rectangle.x + rectangle.width;
                            ai1[1] = rectangle.y;
                            ai[2] = rectangle.x + rectangle.width / 2;
                            ai1[2] = rectangle.y + rectangle.height;
                            break;

                        case 0 : // '\0'
                        case 8 : // '\b'
                        default :
                            ai[0] = rectangle.x;
                            ai1[0] = rectangle.y;
                            ai[1] = rectangle.x + rectangle.width;
                            ai1[1] = rectangle.y + rectangle.height / 2;
                            ai[2] = rectangle.x;
                            ai1[2] = rectangle.y + rectangle.height;
                            break;
                    }
                    IDomElement domelement3 = domdoc.createElement("path");
                    String s = "M " + Integer.toString(ai[0]) + " "
                            + Integer.toString(ai1[0]);
                    s = s + " L " + Integer.toString(ai[1]) + " "
                            + Integer.toString(ai1[1]);
                    s = s + " L " + Integer.toString(ai[2]) + " "
                            + Integer.toString(ai1[2]);
                    s = s + " Z";
                    domelement3.setAttribute("d", s);
                    SVGWriteAttributes(domelement3);
                    domelement.appendChild(domelement3);
                    break;

                case 4 : // '\004'
                    IDomElement domelement4 = domdoc.createElement("rect");
                    domelement4
                            .setAttribute("x", Integer.toString(rectangle.x));
                    domelement4
                            .setAttribute("y", Integer.toString(rectangle.y));
                    domelement4.setAttribute("width",
                            Integer.toString(rectangle.width));
                    domelement4.setAttribute("height",
                            Integer.toString(rectangle.height));
                    SVGWriteAttributes(domelement4);
                    domelement.appendChild(domelement4);
                    break;

                case 5 : // '\005'
                    ai[0] = rectangle.x + rectangle.width / 2;
                    ai1[0] = rectangle.y;
                    ai[1] = rectangle.x + rectangle.width;
                    ai1[1] = rectangle.y + rectangle.height / 2;
                    ai[2] = ai[0];
                    ai1[2] = rectangle.y + rectangle.height;
                    ai[3] = rectangle.x;
                    ai1[3] = ai1[1];
                    IDomElement domelement5 = domdoc.createElement("path");
                    String s1 = "M " + Integer.toString(ai[0]) + " "
                            + Integer.toString(ai1[0]);
                    s1 = s1 + " L " + Integer.toString(ai[1]) + " "
                            + Integer.toString(ai1[1]);
                    s1 = s1 + " L " + Integer.toString(ai[2]) + " "
                            + Integer.toString(ai1[2]);
                    s1 = s1 + " L " + Integer.toString(ai[3]) + " "
                            + Integer.toString(ai1[3]);
                    s1 = s1 + " Z";
                    domelement5.setAttribute("d", s1);
                    SVGWriteAttributes(domelement5);
                    domelement.appendChild(domelement5);
                    break;

                case 1 : // '\001'
                case 2 : // '\002'
                default :
                    int i = getTopLeft().x + getWidth() / 2;
                    int j = getTopLeft().y + getHeight() / 2;
                    int k = getWidth() / 2;
                    int l = getHeight() / 2;
                    IDomElement domelement6 = domdoc.createElement("ellipse");
                    domelement6.setAttribute("rx", Integer.toString(k));
                    domelement6.setAttribute("ry", Integer.toString(l));
                    domelement6.setAttribute("cx", Integer.toString(i));
                    domelement6.setAttribute("cy", Integer.toString(j));
                    SVGWriteAttributes(domelement6);
                    domelement.appendChild(domelement6);
                    break;
            }
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument,
            IDomElement domelement, IDomElement domelement1) {
        if (domelement1 != null) {
            String s = domelement1.getAttribute("partid");
            if (s.length() > 0)
                dx = Integer.parseInt(s);
            String s1 = domelement1.getAttribute("portstyle");
            if (s1.length() > 0)
                setStyle(Integer.parseInt(s1));
            if (domelement1.getAttribute("embeddedportobject").equals("true"))
                domdoc.SVGTraverseChildren(graphicviewerdocument, domelement1,
                        null, false);
            String s2 = domelement1.getAttribute("portobject");
            domdoc.registerReferencingObject(this, "portobject", s2);
            String s3 = domelement1.getAttribute("fromlinkspot");
            if (s3.length() > 0)
                dB = Integer.parseInt(s3);
            String s4 = domelement1.getAttribute("tolinkspot");
            if (s4.length() > 0)
                dA = Integer.parseInt(s4);
            String s5 = domelement1.getAttribute("endsegmentlength");
            if (s5.length() > 0)
                dz = Integer.parseInt(s5);
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
            return domelement.getNextSibling();
        } else {
            return domelement.getNextSibling();
        }
    }

    public void SVGUpdateReference(String s, Object obj) {
        super.SVGUpdateReference(s, obj);
        if (s.equals("portobject"))
            setPortObject((GraphicViewerObject) obj);
    }

    public void paint(Graphics2D graphics2d, GraphicViewerView graphicviewerview) {
        if (paintGreek(graphics2d, graphicviewerview))
            return;
        Rectangle rectangle = getBoundingRect();
        switch (getStyle()) {
            case 0 : // '\0'
                return;

            case 1 : // '\001'
                GraphicViewerObject graphicviewerobject = getPortObject();
                if (graphicviewerobject != null
                        && graphicviewerobject.getLayer() == null
                        && graphicviewerobject.getView() == null) {
                    graphicviewerobject.setBoundingRect(rectangle);
                    graphicviewerobject.paint(graphics2d, graphicviewerview);
                }
                break;

            case 3 : // '\003'
                int ai[] = graphicviewerview._mthdo(3);
                int ai2[] = graphicviewerview._mthif(3);
                switch (getToSpot()) {
                    case 1 : // '\001'
                        ai[0] = rectangle.x + rectangle.width / 2;
                        ai2[0] = rectangle.y;
                        ai[1] = rectangle.x + rectangle.width;
                        ai2[1] = rectangle.y + rectangle.height;
                        ai[2] = rectangle.x;
                        ai2[2] = rectangle.y + rectangle.height / 2;
                        break;

                    case 2 : // '\002'
                        ai[0] = rectangle.x + rectangle.width;
                        ai2[0] = rectangle.y;
                        ai[1] = rectangle.x + rectangle.width / 2;
                        ai2[1] = rectangle.y + rectangle.height;
                        ai[2] = rectangle.x;
                        ai2[2] = rectangle.y;
                        break;

                    case 3 : // '\003'
                        ai[0] = rectangle.x + rectangle.width;
                        ai2[0] = rectangle.y + rectangle.height / 2;
                        ai[1] = rectangle.x;
                        ai2[1] = rectangle.y + rectangle.height;
                        ai[2] = rectangle.x + rectangle.width / 2;
                        ai2[2] = rectangle.y;
                        break;

                    case 4 : // '\004'
                        ai[0] = rectangle.x + rectangle.width;
                        ai2[0] = rectangle.y + rectangle.height;
                        ai[1] = rectangle.x;
                        ai2[1] = rectangle.y + rectangle.height / 2;
                        ai[2] = rectangle.x + rectangle.width;
                        ai2[2] = rectangle.y;
                        break;

                    case 5 : // '\005'
                        ai[0] = rectangle.x + rectangle.width / 2;
                        ai2[0] = rectangle.y + rectangle.height;
                        ai[1] = rectangle.x;
                        ai2[1] = rectangle.y;
                        ai[2] = rectangle.x + rectangle.width;
                        ai2[2] = rectangle.y + rectangle.height / 2;
                        break;

                    case 6 : // '\006'
                        ai[0] = rectangle.x;
                        ai2[0] = rectangle.y + rectangle.height;
                        ai[1] = rectangle.x + rectangle.width / 2;
                        ai2[1] = rectangle.y;
                        ai[2] = rectangle.x + rectangle.width;
                        ai2[2] = rectangle.y + rectangle.height;
                        break;

                    case 7 : // '\007'
                        ai[0] = rectangle.x;
                        ai2[0] = rectangle.y + rectangle.height / 2;
                        ai[1] = rectangle.x + rectangle.width;
                        ai2[1] = rectangle.y;
                        ai[2] = rectangle.x + rectangle.width / 2;
                        ai2[2] = rectangle.y + rectangle.height;
                        break;

                    case 0 : // '\0'
                    case 8 : // '\b'
                    default :
                        ai[0] = rectangle.x;
                        ai2[0] = rectangle.y;
                        ai[1] = rectangle.x + rectangle.width;
                        ai2[1] = rectangle.y + rectangle.height / 2;
                        ai[2] = rectangle.x;
                        ai2[2] = rectangle.y + rectangle.height;
                        break;
                }
                drawPolygon(graphics2d, ai, ai2, 3);
                break;

            case 4 : // '\004'
                drawRect(graphics2d, rectangle.x, rectangle.y, rectangle.width,
                        rectangle.height);
                break;

            case 5 : // '\005'
                int ai1[] = graphicviewerview._mthdo(4);
                int ai3[] = graphicviewerview._mthif(4);
                ai1[0] = rectangle.x + rectangle.width / 2;
                ai3[0] = rectangle.y;
                ai1[1] = rectangle.x + rectangle.width;
                ai3[1] = rectangle.y + rectangle.height / 2;
                ai1[2] = ai1[0];
                ai3[2] = rectangle.y + rectangle.height;
                ai1[3] = rectangle.x;
                ai3[3] = ai3[1];
                drawPolygon(graphics2d, ai1, ai3, 4);
                break;

            case 2 : // '\002'
            default :
                drawEllipse(graphics2d, rectangle.x, rectangle.y,
                        rectangle.width, rectangle.height);
                break;
        }
    }

    public boolean paintGreek(Graphics2D graphics2d,
            GraphicViewerView graphicviewerview) {
        double d = graphicviewerview.getScale();
        double d1 = getDefaultPaintNothingScale();
        double d2 = getDefaultPaintGreekScale();
        if (graphicviewerview.isPrinting()) {
            d1 /= 3D;
            d2 /= 3D;
        }
        if (d <= d1)
            return true;
        if (d <= d2) {
            if (getStyle() != 0) {
                Rectangle rectangle = getBoundingRect();
                drawRect(graphics2d, rectangle.x, rectangle.y, rectangle.width,
                        rectangle.height);
            }
            return true;
        } else {
            return false;
        }
    }

    public void expandRectByPenWidth(Rectangle rectangle) {
        if (getStyle() == 0)
            return;
        GraphicViewerObject graphicviewerobject = getPortObject();
        if (graphicviewerobject != null && graphicviewerobject != this
                && getStyle() == 1 && graphicviewerobject.getLayer() == null
                && graphicviewerobject.getView() == null
                && (g() & 0x20000) == 0) {
            _mthfor(g() | 0x20000);
            graphicviewerobject.expandRectByPenWidth(rectangle);
            _mthfor(g() & 0xfffdffff);
        } else {
            super.expandRectByPenWidth(rectangle);
        }
    }

    public boolean getNearestIntersectionPoint(int i, int j, int k, int l,
            Point point) {
        GraphicViewerObject graphicviewerobject = getPortObject();
        if (graphicviewerobject != null
                && graphicviewerobject != this
                && getStyle() != 1
                && (g() & 0x20000) == 0
                && (graphicviewerobject.getLayer() != null
                        || graphicviewerobject.getView() != null || GraphicViewerObject
                        .findCommonParent(this, graphicviewerobject) != null)) {
            _mthfor(g() | 0x20000);
            boolean flag = graphicviewerobject.getNearestIntersectionPoint(i,
                    j, k, l, point);
            _mthfor(g() & 0xfffdffff);
            return flag;
        } else {
            return super.getNearestIntersectionPoint(i, j, k, l, point);
        }
    }

    public int getPartID() {
        return dx;
    }

    public void setPartID(int i) {
        int j = dx;
        if (j != i) {
            dx = i;
            update(312, j, null);
        }
    }

    public int getStyle() {
        return dw;
    }

    public void setStyle(int i) {
        _mthbyte(i, false);
    }

    private void _mthbyte(int i, boolean flag) {
        int j = dw;
        if (j != i) {
            dw = i;
            update(301, j, null);
            if (!flag)
                portChange(301, j, null);
        }
    }

    public GraphicViewerObject getPortObject() {
        return dr;
    }

    public void setPortObject(GraphicViewerObject graphicviewerobject) {
        _mthfor(graphicviewerobject, false);
    }

    private void _mthfor(GraphicViewerObject graphicviewerobject, boolean flag) {
        GraphicViewerObject graphicviewerobject1 = dr;
        if (graphicviewerobject1 != graphicviewerobject) {
            dr = graphicviewerobject;
            update(302, 0, graphicviewerobject1);
            if (!flag)
                portChange(302, 0, graphicviewerobject1);
        }
    }

    public int getNumLinks() {
        return ds.getNumObjects();
    }

    public int getNumFromLinks() {
        int i = 0;
        GraphicViewerListPosition graphicviewerlistposition = getFirstLinkPos();
        do {
            if (graphicviewerlistposition == null)
                break;
            GraphicViewerLink graphicviewerlink = getLinkAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextLinkPos(graphicviewerlistposition);
            if (graphicviewerlink.getFromPort() == this
                    && graphicviewerlink.getToPort().getDocument() != null)
                i++;
        } while (true);
        return i;
    }

    public int getNumToLinks() {
        int i = 0;
        GraphicViewerListPosition graphicviewerlistposition = getFirstLinkPos();
        do {
            if (graphicviewerlistposition == null)
                break;
            GraphicViewerLink graphicviewerlink = getLinkAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextLinkPos(graphicviewerlistposition);
            if (graphicviewerlink.getToPort() == this)
                i++;
        } while (true);
        return i;
    }

    public boolean hasNoLinks() {
        return ds.isEmpty();
    }

    public GraphicViewerListPosition getFirstLinkPos() {
        return ds.getFirstObjectPos();
    }

    public GraphicViewerLink getLinkAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        return (GraphicViewerLink) ds.getObjectAtPos(graphicviewerlistposition);
    }

    public GraphicViewerListPosition getNextLinkPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        return ds.getNextObjectPos(graphicviewerlistposition);
    }

    public void removeAllLinks() {
        _mthbyte(((GraphicViewerObject) (null)));
    }

    private void _mthbyte(GraphicViewerObject graphicviewerobject) {
        ArrayList<GraphicViewerLink> arraylist = null;
        GraphicViewerListPosition graphicviewerlistposition = getFirstLinkPos();
        do {
            if (graphicviewerlistposition == null)
                break;
            GraphicViewerLink graphicviewerlink = getLinkAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextLinkPos(graphicviewerlistposition);
            if (graphicviewerobject == null
                    || !graphicviewerlink.isChildOf(graphicviewerobject)) {
                if (arraylist == null)
                    arraylist = new ArrayList<GraphicViewerLink>();
                arraylist.add(graphicviewerlink);
            }
        } while (true);
        if (arraylist != null) {
            for (int i = 0; i < arraylist.size(); i++) {
                GraphicViewerLink graphicviewerlink1 = (GraphicViewerLink) arraylist
                        .get(i);
                graphicviewerlink1.unlink();
            }

        }
    }

    public Point getLinkPoint(int i, Point point) {
        Rectangle rectangle = getBoundingRect();
        if (point == null)
            point = new Point(0, 0);
        int j = rectangle.x;
        int k = rectangle.x + rectangle.width / 2;
        int l = rectangle.x + rectangle.width;
        int i1 = rectangle.y;
        int j1 = rectangle.y + rectangle.height / 2;
        int k1 = rectangle.y + rectangle.height;
        switch (i) {
            case 0 : // '\0'
            default :
                point.x = k;
                point.y = j1;
                break;

            case 1 : // '\001'
                point.x = j;
                point.y = i1;
                break;

            case 2 : // '\002'
                point.x = k;
                point.y = i1;
                break;

            case 3 : // '\003'
                point.x = l;
                point.y = i1;
                break;

            case 4 : // '\004'
                point.x = l;
                point.y = j1;
                break;

            case 5 : // '\005'
                point.x = l;
                point.y = k1;
                break;

            case 6 : // '\006'
                point.x = k;
                point.y = k1;
                break;

            case 7 : // '\007'
                point.x = j;
                point.y = k1;
                break;

            case 8 : // '\b'
                point.x = j;
                point.y = j1;
                break;
        }
        return point;
    }

    public final Point getLinkPoint(int i) {
        return getLinkPoint(i, null);
    }

    public Point getLinkPointFromPoint(int i, int j, Point point) {
        if (point == null)
            point = new Point(0, 0);
        Object obj = getPortObject();
        if (obj == null
                || ((GraphicViewerObject) (obj)).getLayer() == null
                && ((GraphicViewerObject) (obj)).getView() == null
                && GraphicViewerObject.findCommonParent(this,
                        ((GraphicViewerObject) (obj))) == null)
            obj = this;
        point.x = i;
        point.y = j;
        if (((GraphicViewerObject) (obj)).isPointInObj(point)) {
            Rectangle rectangle = ((GraphicViewerObject) (obj))
                    .getBoundingRect();
            point.x = rectangle.x + rectangle.width / 2;
            point.y = rectangle.y + rectangle.height / 2;
            return point;
        }
        Rectangle rectangle1 = getBoundingRect();
        if (getNearestIntersectionPoint(i, j, rectangle1.x + rectangle1.width
                / 2, rectangle1.y + rectangle1.height / 2, point)) {
            return point;
        } else {
            Rectangle rectangle2 = ((GraphicViewerObject) (obj))
                    .getBoundingRect();
            point.x = rectangle2.x + rectangle2.width / 2;
            point.y = rectangle2.y + rectangle2.height / 2;
            return point;
        }
    }

    public Point getFromLinkPoint(GraphicViewerLink graphicviewerlink,
            Point point) {
        if (getFromSpot() != -1)
            return getLinkPoint(getFromSpot(), point);
        if (point == null)
            point = new Point(0, 0);
        if (graphicviewerlink == null || graphicviewerlink.getToPort() == null) {
            point.x = getLeft() + getWidth() / 2;
            point.y = getTop() + getHeight() / 2;
            return point;
        }
        if (graphicviewerlink.getNumPoints() > (graphicviewerlink
                .isOrthogonal() ? 6 : 2)) {
            Point point1 = graphicviewerlink.getPoint(1);
            point.x = point1.x;
            point.y = point1.y;
            if (graphicviewerlink.isOrthogonal())
                point = _mthif(point);
        } else {
            GraphicViewerPort graphicviewerport = graphicviewerlink.getToPort();
            point.x = graphicviewerport.getLeft()
                    + graphicviewerport.getWidth() / 2;
            point.y = graphicviewerport.getTop()
                    + graphicviewerport.getHeight() / 2;
            if (graphicviewerlink.isOrthogonal())
                point = _mthif(point);
        }
        return getLinkPointFromPoint(point.x, point.y, point);
    }

    private Point _mthif(Point point) {
        int i = getLeft() + getWidth() / 2;
        int j = getTop() + getHeight() / 2;
        if (Math.abs(point.x - i) >= Math.abs(point.y - j)) {
            if (point.x >= i)
                point.x += 0x1869f;
            else
                point.x -= 0x1869f;
            point.y = j;
        } else {
            if (point.y >= j)
                point.y += 0x1869f;
            else
                point.y -= 0x1869f;
            point.x = i;
        }
        return point;
    }

    public final Point getFromLinkPoint(Point point) {
        return getLinkPoint(getFromSpot(), point);
    }

    public final Point getFromLinkPoint() {
        return getLinkPoint(getFromSpot(), null);
    }

    public Point getToLinkPoint(GraphicViewerLink graphicviewerlink, Point point) {
        if (getToSpot() != -1)
            return getLinkPoint(getToSpot(), point);
        if (point == null)
            point = new Point(0, 0);
        if (graphicviewerlink == null
                || graphicviewerlink.getFromPort() == null) {
            point.x = getLeft() + getWidth() / 2;
            point.y = getTop() + getHeight() / 2;
            return point;
        }
        if (graphicviewerlink.getNumPoints() > (graphicviewerlink
                .isOrthogonal() ? 6 : 2)) {
            Point point1 = graphicviewerlink.getPoint(graphicviewerlink
                    .getNumPoints() - 2);
            point.x = point1.x;
            point.y = point1.y;
            if (graphicviewerlink.isOrthogonal())
                point = _mthif(point);
        } else {
            GraphicViewerPort graphicviewerport = graphicviewerlink
                    .getFromPort();
            point.x = graphicviewerport.getLeft()
                    + graphicviewerport.getWidth() / 2;
            point.y = graphicviewerport.getTop()
                    + graphicviewerport.getHeight() / 2;
            if (graphicviewerlink.isOrthogonal())
                point = _mthif(point);
        }
        return getLinkPointFromPoint(point.x, point.y, point);
    }

    public final Point getToLinkPoint(Point point) {
        return getToLinkPoint(null, point);
    }

    public final Point getToLinkPoint() {
        return getLinkPoint(getToSpot(), null);
    }

    public int getFromSpot() {
        return dB;
    }

    public void setFromSpot(int i) {
        _mthtry(i, false);
    }

    private void _mthtry(int i, boolean flag) {
        int j = dB;
        if (j != i) {
            dB = i;
            update(305, j, null);
            if (!flag)
                portChange(305, j, null);
        }
    }

    public int getToSpot() {
        return dA;
    }

    public void setToSpot(int i) {
        _mthint(i, false);
    }

    private void _mthint(int i, boolean flag) {
        int j = dA;
        if (j != i) {
            dA = i;
            update(306, j, null);
            if (!flag)
                portChange(306, j, null);
        }
    }

    public double getLinkDir(int i) {
        switch (i) {
            case 0 : // '\0'
            default :
                return -1D;

            case 1 : // '\001'
                return 3.9269908169872414D;

            case 2 : // '\002'
                return 4.7123889803846897D;

            case 3 : // '\003'
                return 5.497787143782138D;

            case 4 : // '\004'
                return 0.0D;

            case 5 : // '\005'
                return 0.78539816339744828D;

            case 6 : // '\006'
                return 1.5707963267948966D;

            case 7 : // '\007'
                return 2.3561944901923448D;

            case 8 : // '\b'
                return 3.1415926535897931D;
        }
    }

    public double getFromLinkDir() {
        return getLinkDir(getFromSpot());
    }

    public double getToLinkDir() {
        return getLinkDir(getToSpot());
    }

    public double getFromLinkDir(GraphicViewerLink graphicviewerlink) {
        int i = getFromSpot();
        if (i != -1 && i != 0)
            return getFromLinkDir();
        if (graphicviewerlink == null || graphicviewerlink.getToPort() == null)
            return 0.0D;
        GraphicViewerPort graphicviewerport = graphicviewerlink.getToPort();
        int j = graphicviewerport.getLeft() + graphicviewerport.getWidth() / 2;
        int k = graphicviewerport.getTop() + graphicviewerport.getHeight() / 2;
        int l = getLeft() + getWidth() / 2;
        int i1 = getTop() + getHeight() / 2;
        if (Math.abs(j - l) > Math.abs(k - i1))
            return j < l ? 3.1415926535897931D : 0.0D;
        return k < i1 ? 4.7123889803846897D : 1.5707963267948966D;
    }

    public double getToLinkDir(GraphicViewerLink graphicviewerlink) {
        int i = getToSpot();
        if (i != -1 && i != 0)
            return getToLinkDir();
        if (graphicviewerlink == null
                || graphicviewerlink.getFromPort() == null)
            return 0.0D;
        GraphicViewerPort graphicviewerport = graphicviewerlink.getFromPort();
        int j = graphicviewerport.getLeft() + graphicviewerport.getWidth() / 2;
        int k = graphicviewerport.getTop() + graphicviewerport.getHeight() / 2;
        int l = getLeft() + getWidth() / 2;
        int i1 = getTop() + getHeight() / 2;
        if (Math.abs(j - l) > Math.abs(k - i1))
            return j < l ? 3.1415926535897931D : 0.0D;
        return k < i1 ? 4.7123889803846897D : 1.5707963267948966D;
    }

    public boolean validLink(GraphicViewerPort graphicviewerport) {
        return canLinkFrom()
                && graphicviewerport != null
                && graphicviewerport.canLinkTo()
                && (isValidSelfNode() && graphicviewerport.isValidSelfNode() || !isInSameNode(graphicviewerport))
                && (isValidDuplicateLinks()
                        && graphicviewerport.isValidDuplicateLinks() || !isLinked(graphicviewerport))
                && _mthdo(graphicviewerport);
    }

    public boolean isValidLink() {
        return (g() & 0x8000) != 0;
    }

    private boolean _mthdo(GraphicViewerPort graphicviewerport) {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument == null)
            return true;
        int i = graphicviewerdocument.getValidCycle();
        switch (i) {
            case 0 : // '\0'
            default :
                return true;

            case 1 : // '\001'
                return !GraphicViewerDocument.makesDirectedCycle(
                        getParentGraphicViewerNode(),
                        graphicviewerport.getParentGraphicViewerNode());

            case 2 : // '\002'
                return !GraphicViewerDocument.makesDirectedCycleFast(
                        getParentGraphicViewerNode(),
                        graphicviewerport.getParentGraphicViewerNode());

            case 3 : // '\003'
                return !GraphicViewerDocument.makesUndirectedCycle(
                        getParentGraphicViewerNode(),
                        graphicviewerport.getParentGraphicViewerNode());

            case 4 : // '\004'
                return graphicviewerport.getNumToLinks() == 0
                        && !GraphicViewerDocument.makesDirectedCycleFast(
                                getParentGraphicViewerNode(),
                                graphicviewerport.getParentGraphicViewerNode());

            case 5 : // '\005'
                return getNumFromLinks() == 0
                        && !GraphicViewerDocument.makesDirectedCycleFast(
                                getParentGraphicViewerNode(),
                                graphicviewerport.getParentGraphicViewerNode());
        }
    }

    public boolean canLinkFrom() {
        if (!isValidSource())
            return false;
        return canView();
    }

    public boolean canLinkTo() {
        if (!isValidDestination())
            return false;
        return canView();
    }

    public boolean isValidSource() {
        return (g() & 0x4000) != 0;
    }

    public void setValidSource(boolean flag) {
        boolean flag1 = (g() & 0x4000) != 0;
        if (flag1 != flag) {
            if (flag)
                _mthfor(g() | 0x4000);
            else
                _mthfor(g() & 0xffffbfff);
            update(303, flag1 ? 1 : 0, null);
        }
    }

    public boolean isValidDestination() {
        return (g() & 0x2000) != 0;
    }

    public void setValidDestination(boolean flag) {
        boolean flag1 = (g() & 0x2000) != 0;
        if (flag1 != flag) {
            if (flag)
                _mthfor(g() | 0x2000);
            else
                _mthfor(g() & 0xffffdfff);
            update(304, flag1 ? 1 : 0, null);
        }
    }

    public boolean isValidSelfNode() {
        return (g() & 0x1000) != 0;
    }

    public void setValidSelfNode(boolean flag) {
        boolean flag1 = (g() & 0x1000) != 0;
        if (flag1 != flag) {
            if (flag)
                _mthfor(g() | 0x1000);
            else
                _mthfor(g() & 0xffffefff);
            update(310, flag1 ? 1 : 0, null);
        }
    }

    public static boolean isInSameNode(GraphicViewerPort graphicviewerport,
            GraphicViewerPort graphicviewerport1) {
        if (graphicviewerport == null || graphicviewerport1 == null)
            return false;
        if (graphicviewerport == graphicviewerport1) {
            return true;
        } else {
            GraphicViewerObject graphicviewerobject = graphicviewerport
                    .getParentNode();
            GraphicViewerObject graphicviewerobject1 = graphicviewerport1
                    .getParentNode();
            return graphicviewerobject != null
                    && graphicviewerobject == graphicviewerobject1;
        }
    }

    public boolean isInSameNode(GraphicViewerPort graphicviewerport) {
        return isInSameNode(this, graphicviewerport);
    }

    public boolean isValidDuplicateLinks() {
        return (g() & 0x10000) != 0;
    }

    public void setValidDuplicateLinks(boolean flag) {
        boolean flag1 = (g() & 0x10000) != 0;
        if (flag1 != flag) {
            if (flag)
                _mthfor(g() | 0x10000);
            else
                _mthfor(g() & 0xfffeffff);
            update(311, flag1 ? 1 : 0, null);
        }
    }

    public static boolean isLinked(GraphicViewerPort graphicviewerport,
            GraphicViewerPort graphicviewerport1) {
        if (graphicviewerport == null || graphicviewerport1 == null)
            return false;
        for (GraphicViewerListPosition graphicviewerlistposition = graphicviewerport
                .getFirstLinkPos(); graphicviewerlistposition != null;) {
            GraphicViewerLink graphicviewerlink = graphicviewerport
                    .getLinkAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerport
                    .getNextLinkPos(graphicviewerlistposition);
            if (graphicviewerlink.getFromPort() == graphicviewerport
                    && graphicviewerlink.getToPort() == graphicviewerport1)
                return true;
        }

        return false;
    }

    public boolean isLinked(GraphicViewerPort graphicviewerport) {
        return isLinked(this, graphicviewerport);
    }

    void _mthbyte(boolean flag) {
        if (flag)
            _mthfor(g() | 0x8000);
        else
            _mthfor(g() & 0xffff7fff);
    }

    void a(GraphicViewerLink graphicviewerlink) {
        ds.addObjectAtTail(graphicviewerlink);
        update(307, 0, graphicviewerlink);
        linkChange();
    }

    void _mthif(GraphicViewerLink graphicviewerlink) {
        GraphicViewerListPosition graphicviewerlistposition = ds
                .findObject(graphicviewerlink);
        if (graphicviewerlistposition != null) {
            ds.removeObjectAtPos(graphicviewerlistposition);
            update(308, 0, graphicviewerlink);
            linkChange();
        }
    }

    protected void geometryChange(Rectangle rectangle) {
        super.geometryChange(rectangle);
        portChange(1, 0, rectangle);
    }

    public void portChange(int i, int j, Object obj) {
        for (GraphicViewerListPosition graphicviewerlistposition = getFirstLinkPos(); graphicviewerlistposition != null;) {
            GraphicViewerLink graphicviewerlink = getLinkAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getNextLinkPos(graphicviewerlistposition);
            graphicviewerlink.portChange(this, i, j, obj);
        }

    }

    public void linkChange() {
    }

    protected void ownerChange(
            IGraphicViewerObjectCollection graphicviewerobjectcollection,
            IGraphicViewerObjectCollection graphicviewerobjectcollection1,
            GraphicViewerObject graphicviewerobject) {
        super.ownerChange(graphicviewerobjectcollection,
                graphicviewerobjectcollection1, graphicviewerobject);
        if (graphicviewerobjectcollection1 == null && u() && !t())
            _mthbyte(graphicviewerobject);
    }

    void _mthcase(boolean flag) {
        int i = g();
        if (flag)
            i |= 0x80000;
        else
            i &= 0xfff7ffff;
        _mthfor(i);
    }

    boolean t() {
        return (g() & 0x80000) != 0;
    }

    boolean u() {
        return (g() & 0x40000) != 0;
    }

    void _mthtry(boolean flag) {
        boolean flag1 = (g() & 0x40000) != 0;
        if (flag1 != flag) {
            if (flag)
                _mthfor(g() | 0x40000);
            else
                _mthfor(g() & 0xfffbffff);
            update(313, flag1 ? 1 : 0, null);
        }
    }

    public boolean doUncapturedMouseMove(int i, Point point, Point point1,
            GraphicViewerView graphicviewerview) {
        if (getLayer() != null && !getLayer().isModifiable())
            return false;
        if (!isValidSource() && !isValidDestination()) {
            return false;
        } else {
            graphicviewerview.a(12);
            return true;
        }
    }

    public int getEndSegmentLength() {
        return dz;
    }

    public void setEndSegmentLength(int i) {
        _mthnew(i, false);
    }

    private void _mthnew(int i, boolean flag) {
        int j = dz;
        if (j != i) {
            dz = i;
            update(309, j, null);
            if (!flag)
                portChange(309, j, null);
        }
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 301 :
                graphicviewerdocumentchangededit.setNewValueInt(getStyle());
                return;

            case 302 :
                graphicviewerdocumentchangededit.setNewValue(getPortObject());
                return;

            case 303 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isValidSource());
                return;

            case 304 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isValidDestination());
                return;

            case 305 :
                graphicviewerdocumentchangededit.setNewValueInt(getFromSpot());
                return;

            case 306 :
                graphicviewerdocumentchangededit.setNewValueInt(getToSpot());
                return;

            case 307 :
                return;

            case 308 :
                return;

            case 309 :
                graphicviewerdocumentchangededit
                        .setNewValueInt(getEndSegmentLength());
                return;

            case 312 :
                graphicviewerdocumentchangededit.setNewValueInt(getPartID());
                return;

            case 313 :
                graphicviewerdocumentchangededit.setNewValueBoolean(u());
                return;

            case 310 :
            case 311 :
            default :
                super.copyNewValueForRedo(graphicviewerdocumentchangededit);
                return;
        }
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 301 :
                _mthbyte(graphicviewerdocumentchangededit.getValueInt(flag),
                        true);
                return;

            case 302 :
                _mthfor((GraphicViewerObject) graphicviewerdocumentchangededit
                        .getValue(flag),
                        true);
                return;

            case 303 :
                setValidSource(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 304 :
                setValidDestination(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;

            case 305 :
                _mthtry(graphicviewerdocumentchangededit.getValueInt(flag),
                        true);
                return;

            case 306 :
                _mthint(graphicviewerdocumentchangededit.getValueInt(flag),
                        true);
                return;

            case 307 :
                GraphicViewerLink graphicviewerlink = (GraphicViewerLink) graphicviewerdocumentchangededit
                        .getOldValue();
                if (flag)
                    _mthif(graphicviewerlink);
                else
                    a(graphicviewerlink);
                return;

            case 308 :
                GraphicViewerLink graphicviewerlink1 = (GraphicViewerLink) graphicviewerdocumentchangededit
                        .getOldValue();
                if (flag)
                    a(graphicviewerlink1);
                else
                    _mthif(graphicviewerlink1);
                return;

            case 309 :
                _mthnew(graphicviewerdocumentchangededit.getValueInt(flag),
                        true);
                return;

            case 312 :
                setPartID(graphicviewerdocumentchangededit.getValueInt(flag));
                return;

            case 313 :
                _mthtry(graphicviewerdocumentchangededit.getValueBoolean(flag));
                return;

            case 310 :
            case 311 :
            default :
                super.changeValue(graphicviewerdocumentchangededit, flag);
                return;
        }
    }

    public static double getDefaultPaintNothingScale() {
        return dG;
    }

    public static void setDefaultPaintNothingScale(double d) {
        dG = d;
    }

    public static double getDefaultPaintGreekScale() {
        return dI;
    }

    public static void setDefaultPaintGreekScale(double d) {
        dI = d;
    }

    public static final int StyleHidden = 0;
    public static final int StyleObject = 1;
    public static final int StyleEllipse = 2;
    public static final int StyleTriangle = 3;
    public static final int StyleRectangle = 4;
    public static final int StyleDiamond = 5;
    public static final int ChangedStyle = 301;
    public static final int ChangedObject = 302;
    public static final int ChangedValidSource = 303;
    public static final int ChangedValidDestination = 304;
    public static final int ChangedFromSpot = 305;
    public static final int ChangedToSpot = 306;
    public static final int ChangedAddedLink = 307;
    public static final int ChangedRemovedLink = 308;
    public static final int ChangedEndSegmentLength = 309;
    public static final int ChangedValidSelfNode = 310;
    public static final int ChangedValidDuplicateLinks = 311;
    public static final int ChangedPartID = 312;
    static final int dv = 313;
    private static double dG = 0.14999999999999999D;
    private static double dI = 0.25D;
    private int dx;
    private int dw;
    private GraphicViewerObject dr;
    private int dB;
    private int dA;
    private GraphicViewerObjList ds;
    private int dz;
}