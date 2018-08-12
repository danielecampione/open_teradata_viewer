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
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JScrollBar;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerPalette extends GraphicViewerView {

    private static final long serialVersionUID = -4083183720807413194L;

    public GraphicViewerPalette() {
        bu = -1;
        bv = true;
        bt = new ArrayList<GraphicViewerObject>();
        setOrientation(1);
        setHidingDisabledScrollbars(true);
        getDocument().setModifiable(false);
        setGridOrigin(new Point(5, 5));
    }

    public int getOrientation() {
        return bu;
    }

    public void setOrientation(int i) {
        int j = bu;
        if (j != i && (i == 1 || i == 0)) {
            bu = i;
            firePropertyChange("orientation", j, i);
            if (bu == 1) {
                if (getVerticalScrollBar() == null) {
                    JScrollBar jscrollbar = new JScrollBar(1);
                    jscrollbar.setSize(jscrollbar.getPreferredSize());
                    jscrollbar.setUnitIncrement(getGridHeight());
                    setVerticalScrollBar(jscrollbar);
                }
                setHorizontalScrollBar(null);
            } else {
                if (getHorizontalScrollBar() == null) {
                    JScrollBar jscrollbar1 = new JScrollBar(0);
                    jscrollbar1.setSize(jscrollbar1.getPreferredSize());
                    jscrollbar1.setUnitIncrement(getGridWidth());
                    setHorizontalScrollBar(jscrollbar1);
                }
                setVerticalScrollBar(null);
            }
            invalidate();
            layoutItems();
        }
    }

    public void setSingleRowCol(boolean flag) {
        boolean flag1 = bv;
        if (flag1 != flag) {
            bv = flag;
            firePropertyChange("singleRowCol", flag1, flag);
            layoutItems();
        }
    }

    public boolean getSingleRowCol() {
        return bv;
    }

    public void setShowSampleItems(boolean flag) {
        if (flag) {
            if (flag & (bt.size() == 0))
                d();
        } else {
            GraphicViewerObject graphicviewerobject;
            for (Iterator<GraphicViewerObject> iterator = bt.iterator(); iterator
                    .hasNext(); getDocument().removeObject(graphicviewerobject))
                graphicviewerobject = (GraphicViewerObject) iterator.next();

            bt.clear();
        }
    }

    public boolean isShowSampleItems() {
        return bt.size() > 0;
    }

    public void layoutItems() {
        boolean flag = getOrientation() == 1;
        getDocument().setSuspendUpdates(true);
        int i = getExtentSize().width;
        int j = getExtentSize().height;
        int k = getGridWidth();
        int l = getGridHeight();
        int i1 = getGridOrigin().x;
        int j1 = getGridOrigin().y;
        int k1 = i1;
        int l1 = j1;
        boolean flag1 = getSingleRowCol();
        int i2 = getGridSpot();
        GraphicViewerListPosition graphicviewerlistposition = getDocument()
                .getFirstObjectPos();
        do {
            if (graphicviewerlistposition == null)
                break;
            GraphicViewerObject graphicviewerobject = getDocument()
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getDocument().getNextObjectPosAtTop(
                    graphicviewerlistposition);
            graphicviewerobject.setSpotLocation(i2, k1, l1);
            if (flag) {
                k1 += Math.max(
                        k,
                        (int) Math.ceil((double) graphicviewerobject.getWidth()
                                / (double) k)
                                * k);
                if (flag1 || k1 + graphicviewerobject.getWidth() > i) {
                    k1 = i1;
                    l1 += Math.max(
                            l,
                            (int) Math.ceil((double) graphicviewerobject
                                    .getHeight() / (double) l)
                                    * l);
                }
            } else {
                l1 += Math.max(
                        l,
                        (int) Math.ceil((double) graphicviewerobject
                                .getHeight() / (double) l)
                                * l);
                if (flag1 || l1 + graphicviewerobject.getHeight() > j) {
                    l1 = j1;
                    k1 += Math.max(
                            k,
                            (int) Math.ceil((double) graphicviewerobject
                                    .getWidth() / (double) k)
                                    * k);
                }
            }
        } while (true);
        Dimension dimension = getPrintDocumentSize();
        getDocument().setDocumentSize(dimension);
        Point point = getPrintDocumentTopLeft();
        getDocument().setDocumentTopLeft(point);
        getDocument().setSuspendUpdates(false);
    }

    public void documentChanged(
            GraphicViewerDocumentEvent graphicviewerdocumentevent) {
        super.documentChanged(graphicviewerdocumentevent);
        if (graphicviewerdocumentevent.isBeforeChanging())
            return;
        if (graphicviewerdocumentevent.getHint() == 202
                || graphicviewerdocumentevent.getHint() == 204)
            layoutItems();
    }

    public Dimension getPreferredSize() {
        return new Dimension(150, 200);
    }

    public Dimension getMinimumSize() {
        return new Dimension(50, 100);
    }

    public void addNotify() {
        super.addNotify();
        setDropEnabled(false);
        layoutItems();
    }

    public void doLayout() {
        super.doLayout();
        layoutItems();
    }

    private void _mthnew(GraphicViewerObject graphicviewerobject) {
        getDocument().addObjectAtTail(graphicviewerobject);
        bt.add(graphicviewerobject);
    }

    private void d() {
        GraphicViewerBasicNode graphicviewerbasicnode = new GraphicViewerBasicNode(
                "GraphicViewerBasicNode");
        graphicviewerbasicnode.setBrush(GraphicViewerBrush.orange);
        _mthnew(graphicviewerbasicnode);
        GraphicViewerIconicNode graphicviewericonicnode = new GraphicViewerIconicNode(
                "GraphicViewerIconicNode");
        GraphicViewerImage graphicviewerimage = new GraphicViewerImage(
                new Rectangle(0, 0, 50, 50));
        graphicviewerimage.loadImage(
                (GraphicViewerPalette.class).getResource("document.png"), true);
        graphicviewericonicnode.setIcon(graphicviewerimage);
        _mthnew(graphicviewericonicnode);
        GraphicViewerTextNode graphicviewertextnode = new GraphicViewerTextNode(
                "GraphicViewerTextNode");
        _mthnew(graphicviewertextnode);
        layoutItems();
    }

    public void onGridChange(int i) {
        super.onGridChange(i);
        if (i == 0 || i == 6)
            layoutItems();
    }

    public GraphicViewerObject pickObject(Point point, boolean flag) {
        return null;
    }

    public void autoscroll(Point point) {
    }

    public static final int OrientationVertical = 1;
    public static final int OrientationHorizontal = 0;
    private int bu;
    private boolean bv;

    private ArrayList<GraphicViewerObject> bt;
}