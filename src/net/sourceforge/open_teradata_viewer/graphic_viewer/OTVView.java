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

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.border.TitledBorder;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * 
 * 
 * @author D. Campione
 *
 */
// This class also includes support for MouseWheel events, which is only
// available when using J2SDK/JRE 1.4 or later
public class OTVView extends GraphicViewerView implements MouseWheelListener {

    private static final long serialVersionUID = 5523588950202336233L;

    OTVView() {
        super();
        setIncludingNegativeCoords(true);
        setHidingDisabledScrollbars(true);
        addMouseWheelListener(this);
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        int clicks = e.getWheelRotation();

        if (e.isControlDown()) {
            Point oldpos = getViewPosition();
            Point oldpt = viewToDocCoords(e.getPoint());
            setScale(getScale() * (1 + ((double) clicks) / 20));
            Point focus = viewToDocCoords(e.getPoint());
            setViewPosition(oldpos.x + oldpt.x - focus.x, oldpos.y + oldpt.y
                    - focus.y);
            return;
        }

        JScrollBar bar;
        if (e.isShiftDown()) {
            bar = getHorizontalScrollBar();
        } else {
            bar = getVerticalScrollBar();
        }
        if (bar == null) {
            return;
        }

        int direction = clicks > 0 ? 1 : -1;
        int bound;
        if (direction == 1) {
            bound = bar.getMinimum();
        } else {
            bound = bar.getMaximum();
        }
        int step;
        if (e.getScrollType() == MouseWheelEvent.WHEEL_BLOCK_SCROLL) {
            step = bar.getBlockIncrement(direction);
        } else {
            step = bar.getUnitIncrement(direction);
        }

        int oldv = bar.getValue();
        int newv = oldv + direction * step;
        if (direction == 1) {
            newv = Math.max(newv, bound);
        } else {
            newv = Math.min(newv, bound);
        }
        bar.setValue(newv);
    }

    // New kinds of "modes"
    public static final int MouseStateDrawingStroke = MouseStateLast + 1;

    public void doCancelMouse() {
        myMouseUpDocPoint.x = -9999;
        myMouseUpDocPoint.y = -9999;
        if (getState() == MouseStateDrawingStroke) {
            cancelDrawingStroke();
        } else {
            super.doCancelMouse();
        }
    }

    public void onKeyEvent(KeyEvent evt) {
        int t = evt.getKeyCode();
        if (t == KeyEvent.VK_ESCAPE) {
            doCancelMouse();
        } else if (t == KeyEvent.VK_ENTER
                && getState() == MouseStateDrawingStroke) {
            GraphicViewerStroke myStroke = (GraphicViewerStroke) getCurrentObject();
            if (myStroke != null && myStroke.getNumPoints() > 2) {
                // Stroke always includes a point where the mouse is, which
                // isn't supposed to be included in the finished
                // GraphicViewerStroke
                finishDrawingStroke();
            } else {
                doCancelMouse();
            }
        } else {
            super.onKeyEvent(evt);
        }
    }

    // This implements additional mouse behaviors on mouse down:
    // - start or continue creating a stroke with a user-defined path
    // - if on a record node field that is not Draggable, then we start drawing
    //   a link from that field's port
    public boolean doMouseDown(int modifiers, Point dc, Point vc) {
        if (getState() == MouseStateDrawingStroke) {
            addPointToDrawingStroke(modifiers, dc, vc);
            return true;
        }
        GraphicViewerObject obj = pickDocObject(dc, true);
        if (obj != null) {
            GraphicViewerObject field = obj;
            if (!field.isDraggable() && field.getParent() != null
                    && field.getParent() instanceof ListArea
                    && field.getParent().getParent() != null
                    && field.getParent().getParent() instanceof RecordNode) {
                RecordNode node = (RecordNode) field.getParent().getParent();
                int idx = node.findItem(field);
                if (idx >= 0) {
                    GraphicViewerPort port = node.getLeftPort(idx);
                    if (port == null) {
                        port = node.getRightPort(idx);
                    }
                    if (port != null && startNewLink(port, dc)) {
                        return true;
                    }
                }
            }
        }
        // Otherwise implement the default behavior
        return super.doMouseDown(modifiers, dc, vc);
    }

    // This implements additional mouse behaviors on mouse down:
    // - when creating a link with a user-defined path, have the end follow the
    //   mouse pointer between clicks
    public boolean doMouseMove(int modifiers, Point dc, Point vc) {
        if (getState() == MouseStateDrawingStroke) {
            followPointerForDrawingStroke(modifiers, dc, vc);
            return true;
        }
        return super.doMouseMove(modifiers, dc, vc);
    }

    private JPopupMenu myPopupMenu = new JPopupMenu();
    private Point myMouseUpDocPoint = new Point(0, 0);

    // This implements additional mouse behaviors on mouse up:
    // - mouse up must be ignored when the user is specifying stroke points
    // - if it's a right button mouse down bring up a properties dialog for the
    //   object
    public boolean doMouseUp(int modifiers, Point dc, Point vc) {
        myMouseUpDocPoint.x = dc.x;
        myMouseUpDocPoint.y = dc.y;

        if (getState() == MouseStateDrawingStroke) {
            // Don't need to do anything
            return false;
        }
        if ((modifiers & InputEvent.BUTTON3_MASK) != 0) {
            GraphicViewerObject obj = pickDocObject(dc, true);
            if (obj != null) {
                // The right-mouse-button was used normally OBJ is a selectable
                // object; if the Control key was also held down, we try to
                // operate on the immediate object at the point, even if not
                // selectable
                if ((modifiers & InputEvent.CTRL_MASK) != 0) {
                    GraphicViewerObject o = pickDocObject(dc, false);
                    if (o != null) {
                        obj = o;
                    }
                } else {
                    selectObject(obj);
                }
                if (obj instanceof GraphicViewerLink
                        && (modifiers & InputEvent.CTRL_MASK) == 0) {
                    GraphicViewerLink link = (GraphicViewerLink) obj;
                    JPopupMenu popup = myPopupMenu;
                    popup.removeAll();
                    AppAction insertPointAction = new AppAction("Insert Point",
                            getFrame()) {
                        public void actionPerformed(ActionEvent e) {
                            insertPointIntoLink();
                        }
                    };
                    popup.add(insertPointAction);
                    if (link.getNumPoints() > (link.isOrthogonal() ? 6 : 2)) {
                        AppAction removeSegmentAction = new AppAction(
                                "Remove Segment", getFrame()) {
                            public void actionPerformed(ActionEvent e) {
                                removeSegmentFromLink();
                            }
                        };
                        popup.add(removeSegmentAction);
                    }
                    popup.show(this, vc.x, vc.y);
                } else {
                    doCancelMouse();
                    GraphicViewer app = (GraphicViewer) getFrame();
                    app.callDialog(obj);
                }
                return true;
            }
        }
        // Otherwise implement the default behavior
        return super.doMouseUp(modifiers, dc, vc);
    }

    // Don't start drawing a new link if the port is selectable
    public boolean startNewLink(GraphicViewerPort port, Point dc) {
        if (port.isSelectable()) {
            return false;
        }
        return super.startNewLink(port, dc);
    }

    public void startDrawingStroke() {
        setState(MouseStateDrawingStroke);
        getDocument().startTransaction();
        setCurrentObject(null);
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    void cancelDrawingStroke() {
        if (!hasFocus()) {
            return;
        }
        if (getCurrentObject() != null) {
            removeObject(getCurrentObject());
            setCurrentObject(null);
        }
        setCursor(getDefaultCursor());
        getDocument().endTransaction(false);
        setState(MouseStateNone);
    }

    void addPointToDrawingStroke(int modifiers, Point dc, Point vc) {
        // Keep adding points to the stroke representing the future link
        GraphicViewerStroke s = (GraphicViewerStroke) getCurrentObject();
        if (s == null) {
            // Create a new stroke starting at or near the first mouse down
            // point
            s = new GraphicViewerStroke();
            s.addPoint(dc);
            addObjectAtTail(s);
            setCurrentObject(s);
        }
        s.addPoint(dc);
    }

    void followPointerForDrawingStroke(int modifiers, Point dc, Point vc) {
        GraphicViewerStroke myStroke = (GraphicViewerStroke) getCurrentObject();
        if (myStroke != null) {
            int numpts = myStroke.getNumPoints();
            myStroke.setPoint(numpts - 1, dc);
        }
    }

    void finishDrawingStroke() {
        GraphicViewerStroke myStroke = (GraphicViewerStroke) getCurrentObject();
        if (myStroke != null) {
            // Get rid of the stroke from the view
            removeObject(myStroke);
            // Remove last point, which had been following the pointer
            myStroke.removePoint(myStroke.getNumPoints() - 1);
            getDocument().getDefaultLayer().addObjectAtTail(myStroke);
            selectObject(myStroke);
            setCurrentObject(null);
            setCursor(getDefaultCursor());
            getDocument().endTransaction("created stroke");
            setState(MouseStateNone);
        }
    }

    // Popup menu commands

    void insertPointIntoLink() {
        if (getSelection().getPrimarySelection() instanceof GraphicViewerLink) {
            GraphicViewerLink s = (GraphicViewerLink) getSelection()
                    .getPrimarySelection();
            int i = s.getSegmentNearPoint(myMouseUpDocPoint);
            if (s.getNumPoints() > 3) {
                if (i < 1) {
                    i = 1; // Don't add to first segment
                } else if (i >= s.getNumPoints() - 2) {
                    i = s.getNumPoints() - 3; // Don't add to last segment
                }
            }
            Point a = s.getPoint(i);
            Point b = s.getPoint(i + 1);
            Point closest = new Point((a.x + b.x) / 2, (a.y + b.y) / 2);
            getDocument().startTransaction();
            s.insertPoint(i + 1, closest);
            if (s.isOrthogonal()) {
                s.insertPoint(i + 1, closest);
            }
            getSelection().toggleSelection(s);
            selectObject(s);
            getDocument().endTransaction("inserted point into link stroke");
        }
    }

    void removeSegmentFromLink() {
        if (getSelection().getPrimarySelection() instanceof GraphicViewerLink) {
            GraphicViewerLink s = (GraphicViewerLink) getSelection()
                    .getPrimarySelection();
            int i = s.getSegmentNearPoint(myMouseUpDocPoint);
            getDocument().startTransaction();
            if (s.isOrthogonal()) { // Will have at least 7 points
                // Don't remove either first two or last two segments
                i = Math.max(i, 2);
                i = Math.min(i, s.getNumPoints() - 5);
                Point a = s.getPoint(i);
                Point b = s.getPoint(i + 1);
                s.removePoint(i);
                // To maintain orthogonality, gotta remove two points
                s.removePoint(i);
                // Now fix up following point to maintain orthogonality
                Point c = new Point(s.getPoint(i).x, s.getPoint(i).y);
                if (a.x == b.x) {
                    c.y = a.y;
                } else {
                    c.x = a.x;
                }
                s.setPoint(i, c);
            } else { // Will have at least 3 points
                i = Math.max(i, 1); // don't remove point 0
                i = Math.min(i, s.getNumPoints() - 2); // don't remove last point
                s.removePoint(i);
            }
            getSelection().toggleSelection(s);
            selectObject(s);
            getDocument().endTransaction("removed segment from link stroke");
        }
    }

    // Demonstrate how to detect a double-click in the background--i.e., not on
    // any object (for single-click in background, see doBackgroundClick
    // below..)
    public boolean doMouseDblClick(int modifiers, Point dc, Point vc) {
        boolean result = super.doMouseDblClick(modifiers, dc, vc);
        if (!result && pickDocObject(dc, false) == null) {
            getDocument().startTransaction();
            LimitedNode n = new LimitedNode("limited");
            n.setLocation(dc);
            getDocument().addObjectAtTail(n);
            getDocument().endTransaction("added LimitedNode in background");
        }
        return result;
    }

    // If a right button mouse click, bring up the grid properties dialog
    public void doBackgroundClick(int modifiers, Point dc, Point vc) {
        if ((modifiers & InputEvent.BUTTON3_MASK) != 0) {
            GraphicViewer app = (GraphicViewer) getFrame();
            app.gridAction();
        } else {
            super.doBackgroundClick(modifiers, dc, vc);
        }
    }

    // For this demo, provide visual feedback regarding where something will be
    // dropped from a different window, while dragging
    protected GraphicViewerRectangle myGhost = new GraphicViewerRectangle(
            new Point(0, 0), new Dimension());

    public void dragOver(DropTargetDragEvent e) {
        super.dragOver(e);
        if (e.getDropAction() != DnDConstants.ACTION_NONE) {
            if (myGhost.getView() != this) {
                // Set a default size for the ghost rectangle
                myGhost.setSize(50, 50);
                addObjectAtTail(myGhost);
            }
            myGhost.setTopLeft(viewToDocCoords(e.getLocation()));
        }
    }

    public void dragExit(DropTargetEvent e) {
        if (myGhost.getView() == this) {
            removeObject(myGhost);
        }
        super.dragExit(e);
    }

    // Support both dragging from the palette or other GraphicViewerView as well
    // as from the List of AppActions represented by their String names
    public boolean isDropFlavorAcceptable(DropTargetDragEvent e) {
        return super.isDropFlavorAcceptable(e)
                || e.isDataFlavorSupported(DataFlavor.stringFlavor);
    }

    // Let drag-and-drops from other windows add GraphicViewerObjects to the
    // GraphicViewerSubGraph where the drop occurs.
    //
    //public boolean doDrop(DropTargetDropEvent e, GraphicViewerCopyEnvironment copyenv) {
    //  Point viewCoord = e.getLocation();
    //  Point docCoord = viewToDocCoords(viewCoord);
    //  GraphicViewerSubGraph sg = pickSubGraph(docCoord);
    //  boolean result = super.doDrop(e, copyenv);
    //  if (result && sg != null) {
    //    sg.addCollection(getSelection(), true, getDocument().getDefaultLayer());
    //  }
    //  return result;
    //}

    //public GraphicViewerSubGraph pickSubGraph(Point p) {
    //  GraphicViewerObject obj = pickDocObject(p, true);
    //  if (obj == null) return null;
    //  if (obj instanceof GraphicViewerSubGraph) return (GraphicViewerSubGraph)obj;
    //  if (obj.getParentNode() != null && obj.getParentNode().getParent() instanceof GraphicViewerSubGraph)
    //    return (GraphicViewerSubGraph)obj.getParentNode().getParent();
    //  return null;
    //}

    // Implement drop behavior when coming from another component
    public void drop(DropTargetDropEvent e) {
        // Try the standard drop action.
        // For this module of the application, we'll modify the copied objects
        // to make them resizable. Since the default implementation of drop
        // doesn't make the copy environment accessible, we need to allocate it
        // here
        IGraphicViewerCopyEnvironment map = getDocument()
                .createDefaultCopyEnvironment();
        getDocument().startTransaction();
        if (doDrop(e, map)) {
            // If the default drop action succeeded, let's nudge the copied
            // top-level objects
            Iterator i = map.values().iterator();
            while (i.hasNext()) {
                Object o = i.next();
                if (o instanceof GraphicViewerObject) {
                    GraphicViewerObject obj = (GraphicViewerObject) o;
                    if (obj.isTopLevel()) {
                        obj.setTopLeft(obj.getLeft() + 1, obj.getTop() + 1);
                    }
                }
            }
            fireUpdate(GraphicViewerViewEvent.EXTERNAL_OBJECTS_DROPPED, 0, null);
            // Done with the document changes
            getDocument().endTransaction("Drop");
            return;
        }
        // Otherwise try a command action
        try {
            if (e.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                Transferable tr = e.getTransferable();
                Object data = tr.getTransferData(DataFlavor.stringFlavor);
                e.acceptDrop(e.getDropAction());
                // Execute command
                String actname = (String) data;
                Vector allActs = AppAction.allActions();
                // Find the appropriate command by name
                for (int i = 0; i < allActs.size(); i++) {
                    AppAction act = (AppAction) allActs.elementAt(i);
                    if (act.toString().equals(actname)) {
                        // Set default location to be mouse drop point
                        Point docloc = viewToDocCoords(e.getLocation());
                        GraphicViewer app = (GraphicViewer) getFrame();
                        app.setDefaultLocation(docloc);
                        // Execute the action
                        act.actionPerformed(null);
                        break;
                    }
                }
                completeDrop(e, true);
                fireUpdate(GraphicViewerViewEvent.EXTERNAL_OBJECTS_DROPPED, 0,
                        null);
                // Done with the document changes
                getDocument().endTransaction("Dropped Action");
                return;
            }
        } catch (Exception x) {
            ExceptionDialog.hideException(x);
        }
        // Can't do anything with the drop
        e.rejectDrop();
        // Abort the document transaction
        getDocument().endTransaction(false);
    }

    // Make use of the TitledBorder to show the current zoom scale and
    // (optionally) the number of objects selected
    public void updateBorder() {
        // Change border to reflect current scale
        TitledBorder b = (TitledBorder) getBorder();
        String msg = "GraphicViewer View: ";
        int scale = (int) (getScale() * 100);
        msg = msg + String.valueOf(scale);
        msg = msg + "%";
        // WARNING: turning on this code to display the number of selected
        // objects will slow down the display. In particular, selecting or
        // deselecting a lot of objects will take a long time updating
        // this counter display
        // msg = msg + ",  ";
        // int numsel = getSelection().getNumObjects();
        // msg = msg + numsel;
        // msg = msg + " selected";
        b.setTitle(msg);
        Insets insets = getInsets();
        paintImmediately(0, 0, getWidth(), insets.top);
    }

    // This is a helper function for deciding whether to create a labeled link
    // or not
    public boolean makeLabeledLinkForPort(GraphicViewerPort p) {
        if (p.getParent() instanceof GraphicViewerBasicNode) {
            return true;
        }
        GraphicViewerArea area = p.getParent();
        if (area != null
                && (area.getFlags() & GraphicViewer.flagMultiSpotNode) != 0) {
            return true;
        }
        return false;
    }

    // This method is called by GraphicViewerView when the user has performed
    // the gestures for creating a new link between two ports. We need to create
    // the link, add it to the document, and finish the transaction
    public void newLink(GraphicViewerPort from, GraphicViewerPort to) {
        GraphicViewerLink l = null;
        if (makeLabeledLinkForPort(from) && makeLabeledLinkForPort(to)) {
            GraphicViewerLabeledLink link = new GraphicViewerLabeledLink(from,
                    to);
            l = link;

            GraphicViewerLinkLabel text;
            if (!(from.getParent() instanceof GraphicViewerBasicNode)) {
                text = new GraphicViewerLinkLabel("from");
                text.setAlignment(GraphicViewerText.ALIGN_LEFT);
                text.setSelectable(true);
                text.setEditOnSingleClick(true);
                link.setFromLabel(text);
            }

            text = new GraphicViewerLinkLabel("middle");
            text.setAlignment(GraphicViewerText.ALIGN_CENTER);
            text.setSelectable(true);
            text.setEditable(true);
            text.setEditOnSingleClick(true);
            link.setMidLabel(text);

            if (!(to.getParent() instanceof GraphicViewerBasicNode)) {
                text = new GraphicViewerLinkLabel("to");
                text.setAlignment(GraphicViewerText.ALIGN_RIGHT);
                text.setEditOnSingleClick(true);
                link.setToLabel(text);
            }
        } else {
            l = new GraphicViewerLink(from, to);
        }

        GraphicViewerSubGraphBase.reparentToCommonSubGraph(l, from, to, true,
                from.getDocument().getLinksLayer());

        fireUpdate(GraphicViewerViewEvent.LINK_CREATED, 0, l);
        from.getDocument().endTransaction("new link");
    }

    // Printing support

    //protected PageFormat printShowPageDialog(PrinterJob pj) {
    //  PageFormat defaultpf = pj.validatePage(pj.defaultPage());
    //  defaultpf.setOrientation(PageFormat.LANDSCAPE);
    //  return defaultpf;
    //}

    public Rectangle2D.Double getPrintPageRect(Graphics2D g2, PageFormat pf) {
        // Leave some space at the bottom for a footer
        return new Rectangle2D.Double(pf.getImageableX(), pf.getImageableY(),
                pf.getImageableWidth(), pf.getImageableHeight() - 20);
    }

    public double getPrintScale(Graphics2D g2, PageFormat pf) {
        // Three different options:

        // (A) always print at standard scale
        /*
         return 1.0d;
        */

        // (B) print using view's currently selected scale
        return getScale();

        // (C) scale to fit printed page
        /*
         Rectangle2D.Double pageRect = getPrintPageRect(g2, pf);
         Dimension docSize = getPrintDocumentSize();
         // make sure it doesn't get scaled too much! (especially if no objects in document)
         docSize.width = Math.max(docSize.width, 50);
         docSize.height = Math.max(docSize.height, 50);
         double hratio = pageRect.width / docSize.width;
         double vratio = pageRect.height / docSize.height;
         return Math.min(hratio, vratio);
        */
    }

    public void printDecoration(Graphics2D g2, PageFormat pf, int hpnum,
            int vpnum) {
        // Draw corners around the getPrintPageRect area
        super.printDecoration(g2, pf, hpnum, vpnum);

        // Print the n,m page number in the footer
        String msg = Integer.toString(hpnum);
        msg += ", ";
        msg += Integer.toString(vpnum);

        Paint oldpaint = g2.getPaint();
        g2.setPaint(Color.black);
        Font oldfont = g2.getFont();
        g2.setFont(new Font(GraphicViewerText.getDefaultFontFaceName(),
                Font.PLAIN, 10));
        g2.drawString(msg,
                (int) (pf.getImageableX() + pf.getImageableWidth() / 2),
                (int) (pf.getImageableY() + pf.getImageableHeight() - 10));
        g2.setPaint(oldpaint);
        g2.setFont(oldfont);
    }

    // Provide tooltips indicating layer for all objects except those in the
    // middle layer, for which the default behavior applies--looking for
    // object-specific tooltip strings
    public String getToolTipText(MouseEvent evt) {
        if (!isMouseEnabled()) {
            return null;
        }

        Point p = evt.getPoint();
        Point dc = viewToDocCoords(p);

        GraphicViewerObject obj = pickDocObject(dc, false);

        if (obj != null) {
            Object id = obj.getLayer().getIdentifier();
            if (id != null) {
                id = id.toString();
            }
            if (id != null) {
                return (String) id;
            }
        }

        // Otherwise, just have the normal behavior
        return super.getToolTipText(evt);
    }

    /*
    // A gradient for the view, regardless of where the view is scrolled
    protected void paintPaperColor(Graphics2D g2, Rectangle r)
    {
      Point vp = getViewPosition();
      Dimension vs = getExtentSize();
      GradientPaint gp = new GradientPaint(vp.x, vp.y, Color.white, vp.x+vs.width, vp.y+vs.height, new Color(178, 223, 238));
      g2.setPaint(gp);
      g2.fillRect(r.x, r.y, r.width, r.height);
    }
    */
    /*
      // A gradient for the document, at a fixed document position
      GradientPaint myGP = new GradientPaint(100, 100, Color.white, 400, 400, new Color(178, 223, 238));
      protected void paintPaperColor(Graphics2D g2, Rectangle r)
      {
        g2.setPaint(myGP);
        g2.fillRect(r.x, r.y, r.width, r.height);
      }
    */
}