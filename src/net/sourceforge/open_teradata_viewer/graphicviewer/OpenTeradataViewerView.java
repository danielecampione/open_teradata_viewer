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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.print.PageFormat;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.border.TitledBorder;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class OpenTeradataViewerView extends GraphicViewerView implements MouseWheelListener {

    private static final long serialVersionUID = 5523588950202336233L;

    OpenTeradataViewerView() {
        myPopupMenu = new JPopupMenu();
        myMouseUpDocPoint = new Point(0, 0);
        myGhost = new GraphicViewerRectangle(new Point(0, 0), new Dimension());
        setIncludingNegativeCoords(true);
        setHidingDisabledScrollbars(true);
        addMouseWheelListener(this);
    }

    public void mouseWheelMoved(MouseWheelEvent mousewheelevent) {
        int i = mousewheelevent.getWheelRotation();
        if (mousewheelevent.isControlDown()) {
            Point point = getViewPosition();
            Point point1 = viewToDocCoords(mousewheelevent.getPoint());
            setScale(getScale() * (1.0D + (double) i / 20D));
            Point point2 = viewToDocCoords(mousewheelevent.getPoint());
            setViewPosition((point.x + point1.x) - point2.x,
                    (point.y + point1.y) - point2.y);
            return;
        }
        JScrollBar jscrollbar;
        if (mousewheelevent.isShiftDown())
            jscrollbar = getHorizontalScrollBar();
        else
            jscrollbar = getVerticalScrollBar();
        if (jscrollbar == null)
            return;
        byte byte0 = ((byte) (i <= 0 ? -1 : 1));
        int j;
        if (byte0 == 1)
            j = jscrollbar.getMinimum();
        else
            j = jscrollbar.getMaximum();
        int k;
        if (mousewheelevent.getScrollType() == 1)
            k = jscrollbar.getBlockIncrement(byte0);
        else
            k = jscrollbar.getUnitIncrement(byte0);
        int l = jscrollbar.getValue();
        int i1 = l + byte0 * k;
        if (byte0 == 1)
            i1 = Math.max(i1, j);
        else
            i1 = Math.min(i1, j);
        jscrollbar.setValue(i1);
    }

    public void doCancelMouse() {
        myMouseUpDocPoint.x = -9999;
        myMouseUpDocPoint.y = -9999;
        if (getState() == 101)
            cancelDrawingStroke();
        else
            super.doCancelMouse();
    }

    public void onKeyEvent(KeyEvent keyevent) {
        int i = keyevent.getKeyCode();
        if (i == 27)
            doCancelMouse();
        else if (i == 10 && getState() == 101) {
            GraphicViewerStroke graphicviewerstroke = (GraphicViewerStroke) getCurrentObject();
            if (graphicviewerstroke != null
                    && graphicviewerstroke.getNumPoints() > 2)
                finishDrawingStroke();
            else
                doCancelMouse();
        } else {
            super.onKeyEvent(keyevent);
        }
    }

    public boolean doMouseDown(int i, Point point, Point point1) {
        if (getState() == 101) {
            addPointToDrawingStroke(i, point, point1);
            return true;
        }
        GraphicViewerObject graphicviewerobject = pickDocObject(point, true);
        if (graphicviewerobject != null) {
            GraphicViewerObject graphicviewerobject1 = graphicviewerobject;
            if (!graphicviewerobject1.isDraggable()
                    && graphicviewerobject1.getParent() != null
                    && (graphicviewerobject1.getParent() instanceof ListArea)
                    && graphicviewerobject1.getParent().getParent() != null
                    && (graphicviewerobject1.getParent().getParent() instanceof RecordNode)) {
                RecordNode recordnode = (RecordNode) graphicviewerobject1
                        .getParent().getParent();
                int j = recordnode.findItem(graphicviewerobject1);
                if (j >= 0) {
                    GraphicViewerPort graphicviewerport = recordnode
                            .getLeftPort(j);
                    if (graphicviewerport == null)
                        graphicviewerport = recordnode.getRightPort(j);
                    if (graphicviewerport != null
                            && startNewLink(graphicviewerport, point))
                        return true;
                }
            }
        }
        return super.doMouseDown(i, point, point1);
    }

    public boolean doMouseMove(int i, Point point, Point point1) {
        if (getState() == 101) {
            followPointerForDrawingStroke(i, point, point1);
            return true;
        } else {
            return super.doMouseMove(i, point, point1);
        }
    }

    public boolean doMouseUp(int i, Point point, Point point1) {
        myMouseUpDocPoint.x = point.x;
        myMouseUpDocPoint.y = point.y;
        if (getState() == 101)
            return false;
        if ((i & 4) != 0) {
            GraphicViewerObject graphicviewerobject = pickDocObject(point, true);
            if (graphicviewerobject != null) {
                if ((i & 2) != 0) {
                    GraphicViewerObject graphicviewerobject1 = pickDocObject(
                            point, false);
                    if (graphicviewerobject1 != null)
                        graphicviewerobject = graphicviewerobject1;
                } else {
                    selectObject(graphicviewerobject);
                }
                if ((graphicviewerobject instanceof GraphicViewerLink)
                        && (i & 2) == 0) {
                    GraphicViewerLink graphicviewerlink = (GraphicViewerLink) graphicviewerobject;
                    JPopupMenu jpopupmenu = myPopupMenu;
                    jpopupmenu.removeAll();
                    AppAction appaction = new AppAction("Insert Point",
                            getFrame()) {

                        private static final long serialVersionUID = 8010588837895034337L;

                        public void actionPerformed(ActionEvent actionevent) {
                            insertPointIntoLink();
                        }

                    };
                    jpopupmenu.add(appaction);
                    if (graphicviewerlink.getNumPoints() > (graphicviewerlink
                            .isOrthogonal() ? 6 : 2)) {
                        AppAction appaction1 = new AppAction("Remove Segment",
                                getFrame()) {

                            private static final long serialVersionUID = -4892627324797251312L;

                            public void actionPerformed(ActionEvent actionevent) {
                                removeSegmentFromLink();
                            }

                        };
                        jpopupmenu.add(appaction1);
                    }
                    jpopupmenu.show(this, point1.x, point1.y);
                } else {
                    doCancelMouse();
                    GraphicViewer sc = (GraphicViewer) getFrame();
                    sc.callDialog(graphicviewerobject);
                }
                return true;
            }
        }
        return super.doMouseUp(i, point, point1);
    }

    public boolean startNewLink(GraphicViewerPort graphicviewerport, Point point) {
        if (graphicviewerport.isSelectable())
            return false;
        else
            return super.startNewLink(graphicviewerport, point);
    }

    public void startDrawingStroke() {
        setState(101);
        getDocument().startTransaction();
        setCurrentObject(null);
        setCursor(Cursor.getPredefinedCursor(1));
    }

    void cancelDrawingStroke() {
        if (!hasFocus())
            return;
        if (getCurrentObject() != null) {
            removeObject(getCurrentObject());
            setCurrentObject(null);
        }
        setCursor(getDefaultCursor());
        getDocument().endTransaction(false);
        setState(0);
    }

    void addPointToDrawingStroke(int i, Point point, Point point1) {
        GraphicViewerStroke graphicviewerstroke = (GraphicViewerStroke) getCurrentObject();
        if (graphicviewerstroke == null) {
            graphicviewerstroke = new GraphicViewerStroke();
            graphicviewerstroke.addPoint(point);
            addObjectAtTail(graphicviewerstroke);
            setCurrentObject(graphicviewerstroke);
        }
        graphicviewerstroke.addPoint(point);
    }

    void followPointerForDrawingStroke(int i, Point point, Point point1) {
        GraphicViewerStroke graphicviewerstroke = (GraphicViewerStroke) getCurrentObject();
        if (graphicviewerstroke != null) {
            int j = graphicviewerstroke.getNumPoints();
            graphicviewerstroke.setPoint(j - 1, point);
        }
    }

    void finishDrawingStroke() {
        GraphicViewerStroke graphicviewerstroke = (GraphicViewerStroke) getCurrentObject();
        if (graphicviewerstroke != null) {
            removeObject(graphicviewerstroke);
            graphicviewerstroke
                    .removePoint(graphicviewerstroke.getNumPoints() - 1);
            getDocument().getDefaultLayer()
                    .addObjectAtTail(graphicviewerstroke);
            selectObject(graphicviewerstroke);
            setCurrentObject(null);
            setCursor(getDefaultCursor());
            getDocument().endTransaction("created stroke");
            setState(0);
        }
    }

    void insertPointIntoLink() {
        if (getSelection().getPrimarySelection() instanceof GraphicViewerLink) {
            GraphicViewerLink graphicviewerlink = (GraphicViewerLink) getSelection()
                    .getPrimarySelection();
            int i = graphicviewerlink.getSegmentNearPoint(myMouseUpDocPoint);
            if (graphicviewerlink.getNumPoints() > 3)
                if (i < 1)
                    i = 1;
                else if (i >= graphicviewerlink.getNumPoints() - 2)
                    i = graphicviewerlink.getNumPoints() - 3;
            Point point = graphicviewerlink.getPoint(i);
            Point point1 = graphicviewerlink.getPoint(i + 1);
            Point point2 = new Point((point.x + point1.x) / 2,
                    (point.y + point1.y) / 2);
            getDocument().startTransaction();
            graphicviewerlink.insertPoint(i + 1, point2);
            if (graphicviewerlink.isOrthogonal())
                graphicviewerlink.insertPoint(i + 1, point2);
            getSelection().toggleSelection(graphicviewerlink);
            selectObject(graphicviewerlink);
            getDocument().endTransaction("inserted point into link stroke");
        }
    }

    void removeSegmentFromLink() {
        if (getSelection().getPrimarySelection() instanceof GraphicViewerLink) {
            GraphicViewerLink graphicviewerlink = (GraphicViewerLink) getSelection()
                    .getPrimarySelection();
            int i = graphicviewerlink.getSegmentNearPoint(myMouseUpDocPoint);
            getDocument().startTransaction();
            if (graphicviewerlink.isOrthogonal()) {
                i = Math.max(i, 2);
                i = Math.min(i, graphicviewerlink.getNumPoints() - 5);
                Point point = graphicviewerlink.getPoint(i);
                Point point1 = graphicviewerlink.getPoint(i + 1);
                graphicviewerlink.removePoint(i);
                graphicviewerlink.removePoint(i);
                Point point2 = new Point(graphicviewerlink.getPoint(i).x,
                        graphicviewerlink.getPoint(i).y);
                if (point.x == point1.x)
                    point2.y = point.y;
                else
                    point2.x = point.x;
                graphicviewerlink.setPoint(i, point2);
            } else {
                i = Math.max(i, 1);
                i = Math.min(i, graphicviewerlink.getNumPoints() - 2);
                graphicviewerlink.removePoint(i);
            }
            getSelection().toggleSelection(graphicviewerlink);
            selectObject(graphicviewerlink);
            getDocument().endTransaction("removed segment from link stroke");
        }
    }

    public boolean doMouseDblClick(int i, Point point, Point point1) {
        boolean flag = super.doMouseDblClick(i, point, point1);
        if (!flag && pickDocObject(point, false) == null) {
            getDocument().startTransaction();
            LimitedNode limitednode = new LimitedNode("limited");
            limitednode.setLocation(point);
            getDocument().addObjectAtTail(limitednode);
            getDocument().endTransaction("added LimitedNode in background");
        }
        return flag;
    }

    public void doBackgroundClick(int i, Point point, Point point1) {
        if ((i & 4) != 0) {
            GraphicViewer sc = (GraphicViewer) getFrame();
            sc.gridAction();
        } else {
            super.doBackgroundClick(i, point, point1);
        }
    }

    public void dragOver(DropTargetDragEvent droptargetdragevent) {
        super.dragOver(droptargetdragevent);
        if (droptargetdragevent.getDropAction() != 0) {
            if (myGhost.getView() != this) {
                myGhost.setSize(50, 50);
                addObjectAtTail(myGhost);
            }
            myGhost.setTopLeft(viewToDocCoords(droptargetdragevent
                    .getLocation()));
        }
    }

    public void dragExit(DropTargetEvent droptargetevent) {
        if (myGhost.getView() == this)
            removeObject(myGhost);
        super.dragExit(droptargetevent);
    }

    public boolean isDropFlavorAcceptable(
            DropTargetDragEvent droptargetdragevent) {
        return super.isDropFlavorAcceptable(droptargetdragevent)
                || droptargetdragevent
                        .isDataFlavorSupported(DataFlavor.stringFlavor);
    }

    @SuppressWarnings("rawtypes")
    public void drop(DropTargetDropEvent droptargetdropevent) {
        GraphicViewerCopyEnvironment graphicviewercopyenvironment = getDocument()
                .createDefaultCopyEnvironment();
        getDocument().startTransaction();
        if (doDrop(droptargetdropevent, graphicviewercopyenvironment)) {
            Iterator iterator = graphicviewercopyenvironment.values()
                    .iterator();
            while (iterator.hasNext()) {
                Object o = iterator.next();
                if (o instanceof GraphicViewerObject) {
                    GraphicViewerObject obj = (GraphicViewerObject) o;
                    if (obj.isTopLevel()) {
                        obj.setTopLeft(obj.getLeft() + 1, obj.getTop() + 1);
                    }
                }
            }
            fireUpdate(GraphicViewerViewEvent.EXTERNAL_OBJECTS_DROPPED, 0, null);
            getDocument().endTransaction("Drop");
            return;
        }
        try {
            if (droptargetdropevent
                    .isDataFlavorSupported(DataFlavor.stringFlavor)) {
                Transferable transferable = droptargetdropevent
                        .getTransferable();
                Object obj1 = transferable
                        .getTransferData(DataFlavor.stringFlavor);
                droptargetdropevent.acceptDrop(droptargetdropevent
                        .getDropAction());
                String s = (String) obj1;
                Vector vector = AppAction.allActions();
                int i = 0;
                do {
                    if (i >= vector.size())
                        break;
                    AppAction appaction = (AppAction) vector.elementAt(i);
                    if (appaction.toString().equals(s)) {
                        Point point = viewToDocCoords(droptargetdropevent
                                .getLocation());
                        GraphicViewer sc = (GraphicViewer) getFrame();
                        sc.setDefaultLocation(point);
                        appaction.actionPerformed(null);
                        break;
                    }
                    i++;
                } while (true);
                completeDrop(droptargetdropevent, true);
                fireUpdate(35, 0, null);
                getDocument().endTransaction("Dropped Action");
                return;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            droptargetdropevent.rejectDrop();
        }
        getDocument().endTransaction(false);
    }

    public void updateBorder() {
        TitledBorder titledborder = (TitledBorder) getBorder();
        String s = "Zoom: ";
        int i = (int) (getScale() * 100D);
        s = s + String.valueOf(i);
        s = s + "%";
        titledborder.setTitle(s);
        Insets insets = getInsets();
        paintImmediately(0, 0, getWidth(), insets.top);
    }

    public boolean makeLabeledLinkForPort(GraphicViewerPort graphicviewerport) {
        if (graphicviewerport.getParent() instanceof GraphicViewerBasicNode)
            return true;
        GraphicViewerArea graphicviewerarea = graphicviewerport.getParent();
        return graphicviewerarea != null
                && (graphicviewerarea.getFlags() & 0x10000) != 0;
    }

    public void newLink(GraphicViewerPort graphicviewerport,
            GraphicViewerPort graphicviewerport1) {
        Object obj = null;
        if (makeLabeledLinkForPort(graphicviewerport)
                && makeLabeledLinkForPort(graphicviewerport1)) {
            GraphicViewerLabeledLink graphicviewerlabeledlink = new GraphicViewerLabeledLink(
                    graphicviewerport, graphicviewerport1);
            obj = graphicviewerlabeledlink;
            if (!(graphicviewerport.getParent() instanceof GraphicViewerBasicNode)) {
                GraphicViewerLinkLabel graphicviewerlinklabel = new GraphicViewerLinkLabel(
                        "from");
                graphicviewerlinklabel.setAlignment(1);
                graphicviewerlinklabel.setSelectable(true);
                graphicviewerlinklabel.setEditOnSingleClick(true);
                graphicviewerlabeledlink.setFromLabel(graphicviewerlinklabel);
            }
            GraphicViewerLinkLabel graphicviewerlinklabel1 = new GraphicViewerLinkLabel(
                    "middle");
            graphicviewerlinklabel1.setAlignment(2);
            graphicviewerlinklabel1.setSelectable(true);
            graphicviewerlinklabel1.setEditable(true);
            graphicviewerlinklabel1.setEditOnSingleClick(true);
            graphicviewerlabeledlink.setMidLabel(graphicviewerlinklabel1);
            if (!(graphicviewerport1.getParent() instanceof GraphicViewerBasicNode)) {
                GraphicViewerLinkLabel graphicviewerlinklabel2 = new GraphicViewerLinkLabel(
                        "to");
                graphicviewerlinklabel2.setAlignment(3);
                graphicviewerlinklabel2.setEditOnSingleClick(true);
                graphicviewerlabeledlink.setToLabel(graphicviewerlinklabel2);
            }
        } else {
            obj = new GraphicViewerLink(graphicviewerport, graphicviewerport1);
        }
        GraphicViewerSubGraphBase.reparentToCommonSubGraph(
                ((GraphicViewerObject) (obj)), graphicviewerport,
                graphicviewerport1, true, graphicviewerport.getDocument()
                        .getLinksLayer());
        fireUpdate(31, 0, obj);
        graphicviewerport.getDocument().endTransaction("new link");
    }

    public java.awt.geom.Rectangle2D.Double getPrintPageRect(
            Graphics2D graphics2d, PageFormat pageformat) {
        return new java.awt.geom.Rectangle2D.Double(pageformat.getImageableX(),
                pageformat.getImageableY(), pageformat.getImageableWidth(),
                pageformat.getImageableHeight() - 20D);
    }

    public double getPrintScale(Graphics2D graphics2d, PageFormat pageformat) {
        return getScale();
    }

    public void printDecoration(Graphics2D graphics2d, PageFormat pageformat,
            int i, int j) {
        super.printDecoration(graphics2d, pageformat, i, j);
        String s = Integer.toString(i);
        s = s + ", ";
        s = s + Integer.toString(j);
        java.awt.Paint paint = graphics2d.getPaint();
        graphics2d.setPaint(Color.black);
        Font font = graphics2d.getFont();
        graphics2d.setFont(new Font(GraphicViewerText.getDefaultFontFaceName(),
                0, 10));
        graphics2d.drawString(s, (int) (pageformat.getImageableX() + pageformat
                .getImageableWidth() / 2D),
                (int) ((pageformat.getImageableY() + pageformat
                        .getImageableHeight()) - 10D));
        graphics2d.setPaint(paint);
        graphics2d.setFont(font);
    }

    public String getToolTipText(MouseEvent mouseevent) {
        if (!isMouseEnabled())
            return null;
        Point point = mouseevent.getPoint();
        Point point1 = viewToDocCoords(point);
        GraphicViewerObject graphicviewerobject = pickDocObject(point1, false);
        if (graphicviewerobject != null) {
            Object obj = graphicviewerobject.getLayer().getIdentifier();
            if (obj != null)
                obj = obj.toString();
            if (obj != null)
                return (String) obj;
        }
        return super.getToolTipText(mouseevent);
    }

    public static final int MouseStateDrawingStroke = 101;
    private JPopupMenu myPopupMenu;
    private Point myMouseUpDocPoint;
    protected GraphicViewerRectangle myGhost;
}