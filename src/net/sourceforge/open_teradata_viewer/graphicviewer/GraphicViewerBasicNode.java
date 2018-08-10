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

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerBasicNode extends GraphicViewerNode {

    private static final long serialVersionUID = 6233692699831637870L;

    public GraphicViewerBasicNode() {
        dc = null;
        dh = null;
        dg = null;
        df = 2;
        de = new Insets(5, 10, 5, 10);
        r();
    }

    public GraphicViewerBasicNode(String s) {
        dc = null;
        dh = null;
        dg = null;
        df = 2;
        de = new Insets(5, 10, 5, 10);
        r();
        _mthdo(s);
    }

    private void r() {
        _mthfor(g() & 0xffffffef | 0x10000);
    }

    private void _mthdo(String s) {
        setInitializing(true);
        dg = createPort();
        dc = createDrawable(dg);
        dh = createLabel(s);
        addObjectAtHead(dc);
        addObjectAtTail(dg);
        addObjectAtTail(dh);
        if (dg != null)
            dg.setPortObject(dc);
        setInitializing(false);
        layoutChildren(null);
    }

    public GraphicViewerPort createPort() {
        GraphicViewerPort graphicviewerport = new GraphicViewerPort();
        if (getLabelSpot() == 0)
            graphicviewerport.setStyle(0);
        else
            graphicviewerport.setStyle(2);
        graphicviewerport.setFromSpot(-1);
        graphicviewerport.setToSpot(-1);
        graphicviewerport.setSize(db);
        return graphicviewerport;
    }

    public GraphicViewerDrawable createDrawable(
            GraphicViewerPort graphicviewerport) {
        GraphicViewerEllipse graphicviewerellipse = new GraphicViewerEllipse();
        Dimension dimension = da;
        graphicviewerellipse.setSize(graphicviewerport.getWidth() + 2
                * dimension.width, graphicviewerport.getHeight() + 2
                * dimension.height);
        graphicviewerellipse.setSelectable(false);
        graphicviewerellipse.setResizable(false);
        graphicviewerellipse.setBrush(GraphicViewerBrush.white);
        return graphicviewerellipse;
    }

    public GraphicViewerText createLabel(String s) {
        if (s != null) {
            GraphicViewerText graphicviewertext = new GraphicViewerText(s);
            graphicviewertext.setSelectable(false);
            graphicviewertext.setDraggable(false);
            graphicviewertext.setEditable(false);
            graphicviewertext.setEditOnSingleClick(true);
            graphicviewertext.setTransparent(true);
            return graphicviewertext;
        } else {
            return null;
        }
    }

    protected void copyChildren(GraphicViewerArea graphicviewerarea,
            GraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerBasicNode graphicviewerbasicnode = (GraphicViewerBasicNode) graphicviewerarea;
        graphicviewerbasicnode.df = df;
        graphicviewerbasicnode.de = new Insets(de.top, de.left, de.bottom,
                de.right);
        super.copyChildren(graphicviewerarea, graphicviewercopyenvironment);
        graphicviewerbasicnode.dc = (GraphicViewerDrawable) graphicviewercopyenvironment
                .get(dc);
        graphicviewerbasicnode.dg = (GraphicViewerPort) graphicviewercopyenvironment
                .get(dg);
        graphicviewerbasicnode.dh = (GraphicViewerText) graphicviewercopyenvironment
                .get(dh);
    }

    public GraphicViewerObject removeObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        GraphicViewerObject graphicviewerobject = super
                .removeObjectAtPos(graphicviewerlistposition);
        if (graphicviewerobject == dc)
            dc = null;
        else if (graphicviewerobject == dh)
            dh = null;
        else if (graphicviewerobject == dg)
            dg = null;
        return graphicviewerobject;
    }

    public Point getLocation(Point point) {
        if (getDrawable() != null)
            return getDrawable().getSpotLocation(0, point);
        else
            return getSpotLocation(0, point);
    }

    public void setLocation(int i, int j) {
        if (getDrawable() != null) {
            Rectangle rectangle = getDrawable().getBoundingRect();
            int k = rectangle.x + rectangle.width / 2;
            int i1 = rectangle.y + rectangle.height / 2;
            if (i != k || j != i1) {
                int k1 = getLeft();
                int l1 = getTop();
                setTopLeft(i - (k - k1), j - (i1 - l1));
            }
        } else {
            Rectangle rectangle1 = getBoundingRect();
            int l = rectangle1.x + rectangle1.width / 2;
            int j1 = rectangle1.y + rectangle1.height / 2;
            if (i != l || j != j1)
                setTopLeft(i - rectangle1.width / 2, j - rectangle1.height / 2);
        }
    }

    public void layoutChildren(GraphicViewerObject graphicviewerobject) {
        if (isInitializing())
            return;
        GraphicViewerDrawable graphicviewerdrawable = getDrawable();
        if (graphicviewerdrawable == null)
            return;
        GraphicViewerText graphicviewertext = getLabel();
        if (graphicviewertext != null) {
            int i = getLabelSpot();
            if (i == 0) {
                int j = graphicviewerdrawable.getLeft()
                        + graphicviewerdrawable.getWidth() / 2;
                int k = graphicviewerdrawable.getTop()
                        + graphicviewerdrawable.getHeight() / 2;
                Insets insets = getInsets();
                if (isAutoResize()) {
                    int l = graphicviewertext.getWidth() + insets.left
                            + insets.right;
                    int j1 = graphicviewertext.getHeight() + insets.top
                            + insets.bottom;
                    graphicviewerdrawable.setBoundingRect(
                            j - graphicviewertext.getWidth() / 2 - insets.left,
                            k - graphicviewertext.getHeight() / 2 - insets.top,
                            l, j1);
                } else {
                    int i1 = Math.max(graphicviewerdrawable.getWidth()
                            - (insets.left + insets.right), 0);
                    int k1 = Math.max(graphicviewerdrawable.getHeight()
                            - (insets.top + insets.bottom), 0);
                    graphicviewertext.setWidth(i1);
                    graphicviewertext.setWrappingWidth(i1);
                    graphicviewertext.C();
                    int l1 = Math.min(graphicviewertext.getHeight(), k1);
                    int i2 = graphicviewerdrawable.getLeft() + insets.left;
                    int j2 = graphicviewerdrawable.getTop() + insets.top
                            + (k1 - l1) / 2;
                    graphicviewertext.setBoundingRect(i2, j2, i1, l1);
                }
                graphicviewertext.setAlignment(0);
                graphicviewertext.setSpotLocation(0, j, k);
                if (getPort() != null)
                    getPort().setBoundingRect(
                            graphicviewerdrawable.getBoundingRect());
            } else {
                graphicviewertext.setAlignment(spotOpposite(i));
                graphicviewertext.setSpotLocation(spotOpposite(i),
                        graphicviewerdrawable, i);
            }
        }
        if (getPort() != null)
            getPort().setSpotLocation(0, graphicviewerdrawable, 0);
    }

    public int getLabelSpot() {
        return df;
    }

    public void setLabelSpot(int i) {
        _mthfor(i, false);
    }

    private void _mthfor(int i, boolean flag) {
        int j = df;
        if (j != i) {
            df = i;
            update(2101, j, null);
            if (!flag)
                labelSpotChanged(j);
        }
    }

    public void labelSpotChanged(int i) {
        GraphicViewerPort graphicviewerport = getPort();
        if (graphicviewerport != null)
            if (getLabelSpot() == 0) {
                graphicviewerport.setStyle(0);
                setResizable(false);
            } else if (i == 0) {
                graphicviewerport.setStyle(2);
                GraphicViewerDrawable graphicviewerdrawable = getDrawable();
                int j = graphicviewerdrawable.getLeft()
                        + graphicviewerdrawable.getWidth() / 2;
                int k = graphicviewerdrawable.getTop()
                        + graphicviewerdrawable.getHeight() / 2;
                Rectangle rectangle = new Rectangle(j - db.width / 2, k
                        - db.height / 2, db.width, db.height);
                graphicviewerdrawable.setBoundingRect(j - rectangle.width / 2
                        - da.width, k - rectangle.height / 2 - da.height,
                        rectangle.width + 2 * da.width, rectangle.height + 2
                                * da.height);
                graphicviewerport.setBoundingRect(rectangle);
            }
        layoutChildren(getLabel());
    }

    public Insets getInsets() {
        return de;
    }

    public void setInsets(Insets insets) {
        a(insets, false);
    }

    private void a(Insets insets, boolean flag) {
        Insets insets1 = de;
        if (!insets1.equals(insets)) {
            Insets insets2 = new Insets(insets1.top, insets1.left,
                    insets1.bottom, insets1.right);
            de.top = insets.top;
            de.left = insets.left;
            de.bottom = insets.bottom;
            de.right = insets.right;
            update(2105, 0, insets2);
            if (!flag)
                layoutChildren(null);
        }
    }

    public void setAutoResize(boolean flag) {
        _mthdo(flag, false);
    }

    private void _mthdo(boolean flag, boolean flag1) {
        boolean flag2 = (g() & 0x10000) != 0;
        if (flag2 != flag) {
            if (flag)
                _mthfor(g() | 0x10000);
            else
                _mthfor(g() & 0xfffeffff);
            update(2106, flag2 ? 1 : 0, null);
            if (!flag1)
                onAutoResizeChanged(flag2);
        }
    }

    public boolean isAutoResize() {
        return (g() & 0x10000) != 0;
    }

    public void onAutoResizeChanged(boolean flag) {
        GraphicViewerText graphicviewertext = getLabel();
        if (graphicviewertext != null) {
            graphicviewertext.setMultiline(flag);
            graphicviewertext.setWrapping(flag);
            graphicviewertext.setClipping(flag);
        }
    }

    public GraphicViewerPen getPen() {
        return getDrawable().getPen();
    }

    public void setPen(GraphicViewerPen graphicviewerpen) {
        getDrawable().setPen(graphicviewerpen);
    }

    public GraphicViewerBrush getBrush() {
        return getDrawable().getBrush();
    }

    public void setBrush(GraphicViewerBrush graphicviewerbrush) {
        getDrawable().setBrush(graphicviewerbrush);
    }

    public String getText() {
        if (getLabel() != null)
            return getLabel().getText();
        else
            return "";
    }

    public void setText(String s) {
        if (s == null)
            removeObject(dh);
        else if (getLabel() == null)
            setLabel(createLabel(s));
        else
            getLabel().setText(s);
    }

    public GraphicViewerDrawable getDrawable() {
        return dc;
    }

    public void setDrawable(GraphicViewerDrawable graphicviewerdrawable) {
        GraphicViewerDrawable graphicviewerdrawable1 = dc;
        if (graphicviewerdrawable1 != graphicviewerdrawable) {
            a(graphicviewerdrawable1, graphicviewerdrawable);
            if (graphicviewerdrawable1 != null)
                removeObject(graphicviewerdrawable1);
            dc = graphicviewerdrawable;
            if (graphicviewerdrawable != null) {
                if (graphicviewerdrawable1 == null) {
                    graphicviewerdrawable.setSelectable(false);
                    graphicviewerdrawable.setResizable(false);
                }
                addObjectAtHead(graphicviewerdrawable);
            }
            update(2102, 0, graphicviewerdrawable1);
            if (getPort() != null
                    && getPort().getPortObject() == graphicviewerdrawable1)
                getPort().setPortObject(graphicviewerdrawable);
        }
    }

    public GraphicViewerText getLabel() {
        return dh;
    }

    public void setLabel(GraphicViewerText graphicviewertext) {
        GraphicViewerText graphicviewertext1 = dh;
        if (graphicviewertext1 != graphicviewertext) {
            if (graphicviewertext1 != null)
                removeObject(graphicviewertext1);
            dh = graphicviewertext;
            if (graphicviewertext != null)
                addObjectAtTail(graphicviewertext);
            update(2103, 0, graphicviewertext1);
        }
    }

    public GraphicViewerPort getPort() {
        return dg;
    }

    public void setPort(GraphicViewerPort graphicviewerport) {
        GraphicViewerPort graphicviewerport1 = dg;
        if (graphicviewerport1 != graphicviewerport) {
            if (graphicviewerport1 != null)
                removeObject(graphicviewerport1);
            dg = graphicviewerport;
            if (graphicviewerport != null)
                addObjectAtTail(graphicviewerport);
            update(2104, 0, graphicviewerport1);
            if (graphicviewerport != null
                    && graphicviewerport.getPortObject() == null)
                graphicviewerport.setPortObject(getDrawable());
        }
    }

    public void SVGUpdateReference(String s, Object obj) {
        super.SVGUpdateReference(s, obj);
        if (s.equals("drawable"))
            dc = (GraphicViewerDrawable) obj;
        else if (s.equals("label"))
            dh = (GraphicViewerText) obj;
        else if (s.equals("port"))
            dg = (GraphicViewerPort) obj;
    }

    public void SVGWriteObject(DomDoc domdoc, DomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            DomElement domelement1 = domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerBasicNode",
                    domelement);
            domelement1.setAttribute("labelspot", Integer.toString(df));
            domelement1.setAttribute("insets_top", Integer.toString(de.top));
            domelement1
                    .setAttribute("insets_right", Integer.toString(de.right));
            domelement1.setAttribute("insets_bottom",
                    Integer.toString(de.bottom));
            domelement1.setAttribute("insets_left", Integer.toString(de.left));
            if (dc != null)
                domdoc.registerReferencingNode(domelement1, "drawable", dc);
            if (dh != null)
                domdoc.registerReferencingNode(domelement1, "label", dh);
            if (dg != null)
                domdoc.registerReferencingNode(domelement1, "port", dg);
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public DomNode SVGReadObject(DomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, DomElement domelement,
            DomElement domelement1) {
        if (domelement1 != null) {
            df = Integer.parseInt(domelement1.getAttribute("labelspot"));
            String s = domelement1.getAttribute("insets_top");
            if (s.length() > 0)
                de.top = Integer.parseInt(s);
            String s1 = domelement1.getAttribute("insets_right");
            if (s1.length() > 0)
                de.right = Integer.parseInt(s1);
            String s2 = domelement1.getAttribute("insets_bottom");
            if (s2.length() > 0)
                de.bottom = Integer.parseInt(s2);
            String s3 = domelement1.getAttribute("insets_left");
            if (s3.length() > 0)
                de.left = Integer.parseInt(s3);
            String s4 = domelement1.getAttribute("drawable");
            domdoc.registerReferencingObject(this, "drawable", s4);
            String s5 = domelement1.getAttribute("label");
            domdoc.registerReferencingObject(this, "label", s5);
            String s6 = domelement1.getAttribute("port");
            domdoc.registerReferencingObject(this, "port", s6);
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        }
        return domelement.getNextSibling();
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 2101 :
                graphicviewerdocumentchangededit.setNewValueInt(getLabelSpot());
                return;

            case 2102 :
                graphicviewerdocumentchangededit.setNewValue(getDrawable());
                return;

            case 2103 :
                graphicviewerdocumentchangededit.setNewValue(getLabel());
                return;

            case 2104 :
                graphicviewerdocumentchangededit.setNewValue(getPort());
                return;

            case 2105 :
                Insets insets = getInsets();
                graphicviewerdocumentchangededit.setNewValue(new Insets(
                        insets.top, insets.left, insets.bottom, insets.right));
                return;

            case 2106 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isAutoResize());
                return;
        }
        super.copyNewValueForRedo(graphicviewerdocumentchangededit);
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 2101 :
                _mthfor(graphicviewerdocumentchangededit.getValueInt(flag),
                        true);
                return;

            case 2102 :
                setDrawable((GraphicViewerDrawable) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2103 :
                setLabel((GraphicViewerText) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2104 :
                setPort((GraphicViewerPort) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2105 :
                a((Insets) graphicviewerdocumentchangededit.getValue(flag),
                        true);
                return;

            case 2106 :
                _mthdo(graphicviewerdocumentchangededit.getValueBoolean(flag),
                        true);
                return;
        }
        super.changeValue(graphicviewerdocumentchangededit, flag);
    }

    private static Dimension db = new Dimension(7, 7);
    private static Dimension da = new Dimension(7, 7);
    public static final int ChangedLabelSpot = 2101;
    public static final int ChangedDrawable = 2102;
    public static final int ChangedLabel = 2103;
    public static final int ChangedPort = 2104;
    public static final int ChangedInsets = 2105;
    public static final int ChangedAutoResize = 2106;
    @SuppressWarnings("unused")
    private static final int dd = 0x10000;
    private GraphicViewerDrawable dc;
    private GraphicViewerText dh;
    private GraphicViewerPort dg;
    private int df;
    private Insets de;

}