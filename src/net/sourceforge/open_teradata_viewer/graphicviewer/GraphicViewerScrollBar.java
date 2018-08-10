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

package net.sourceforge.open_teradata_viewer.graphicviewer;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.JScrollBar;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerScrollBar extends GraphicViewerControl {

    private static final long serialVersionUID = -8230008936429961796L;

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static class JSBDTListener implements DropTargetListener {

        public void dragEnter(DropTargetDragEvent droptargetdragevent) {
            a.onDragEnter(a(droptargetdragevent));
        }

        public void dragOver(DropTargetDragEvent droptargetdragevent) {
            a.onDragOver(a(droptargetdragevent));
        }

        public void dropActionChanged(DropTargetDragEvent droptargetdragevent) {
            a.onDropActionChanged(a(droptargetdragevent));
        }

        public void dragExit(DropTargetEvent droptargetevent) {
            a.onDragExit(droptargetevent);
        }

        public void drop(DropTargetDropEvent droptargetdropevent) {
            a.onDrop(a(droptargetdropevent));
        }

        DropTargetDragEvent a(DropTargetDragEvent droptargetdragevent) {
            return new DropTargetDragEvent(
                    droptargetdragevent.getDropTargetContext(),
                    a(droptargetdragevent.getLocation()),
                    droptargetdragevent.getDropAction(),
                    droptargetdragevent.getSourceActions());
        }

        DropTargetDropEvent a(DropTargetDropEvent droptargetdropevent) {
            return new DropTargetDropEvent(
                    droptargetdropevent.getDropTargetContext(),
                    a(droptargetdropevent.getLocation()),
                    droptargetdropevent.getDropAction(),
                    droptargetdropevent.getSourceActions());
        }

        Point a(Point point) {
            Point point1 = _fldif.getLocationOnScreen();
            Point point2 = a.getCanvas().getLocationOnScreen();
            return new Point((point1.x - point2.x) + point.x,
                    (point1.y - point2.y) + point.y);
        }

        JComponent _fldif;
        GraphicViewerView a;

        JSBDTListener(JComponent jcomponent, GraphicViewerView graphicviewerview) {
            _fldif = jcomponent;
            a = graphicviewerview;
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static class JSBListener implements AdjustmentListener {

        public void adjustmentValueChanged(AdjustmentEvent adjustmentevent) {
            if (!_fldif.d3 && adjustmentevent.getAdjustmentType() == 5) {
                int i = adjustmentevent.getValue();
                _fldif.valueChanged(i, a);
            }
        }

        GraphicViewerScrollBar _fldif;
        GraphicViewerView a;

        JSBListener(GraphicViewerScrollBar graphicviewerscrollbar,
                GraphicViewerView graphicviewerview) {
            _fldif = graphicviewerscrollbar;
            a = graphicviewerview;
        }
    }

    public GraphicViewerScrollBar() {
        d0 = true;
        d7 = 0;
        d1 = 10;
        d6 = 0;
        d4 = 100;
        d2 = 1;
        d5 = 9;
        d3 = false;
    }

    public GraphicViewerScrollBar(Rectangle rectangle, boolean flag) {
        super(rectangle);
        d0 = true;
        d7 = 0;
        d1 = 10;
        d6 = 0;
        d4 = 100;
        d2 = 1;
        d5 = 9;
        d3 = false;
        d0 = flag;
    }

    public GraphicViewerScrollBar(Point point, Dimension dimension, boolean flag) {
        super(point, dimension);
        d0 = true;
        d7 = 0;
        d1 = 10;
        d6 = 0;
        d4 = 100;
        d2 = 1;
        d5 = 9;
        d3 = false;
        d0 = flag;
    }

    public GraphicViewerObject copyObject(
            GraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerScrollBar graphicviewerscrollbar = (GraphicViewerScrollBar) super
                .copyObject(graphicviewercopyenvironment);
        if (graphicviewerscrollbar != null) {
            graphicviewerscrollbar.d0 = d0;
            graphicviewerscrollbar.d7 = d7;
            graphicviewerscrollbar.d1 = d1;
            graphicviewerscrollbar.d6 = d6;
            graphicviewerscrollbar.d4 = d4;
            graphicviewerscrollbar.d2 = d2;
            graphicviewerscrollbar.d5 = d5;
        }
        return graphicviewerscrollbar;
    }

    public JComponent createComponent(GraphicViewerView graphicviewerview) {
        JScrollBar jscrollbar = new JScrollBar(isVertical() ? 1 : 0);
        jscrollbar.setValues(getValue(), getExtent(), getMinimum(),
                getMaximum());
        jscrollbar.addAdjustmentListener(new JSBListener(this,
                graphicviewerview));
        new DropTarget(jscrollbar, new JSBDTListener(jscrollbar,
                graphicviewerview));
        return jscrollbar;
    }

    public boolean isVertical() {
        return d0;
    }

    @SuppressWarnings("rawtypes")
    public void setVertical(boolean flag) {
        boolean flag1 = d0;
        if (flag1 != flag) {
            d0 = flag;
            d3 = true;
            GraphicViewerView graphicviewerview;
            for (Iterator iterator = getIterator(); iterator.hasNext(); graphicviewerview
                    .getCanvas().validate()) {
                Entry entry = (Entry) iterator.next();
                graphicviewerview = (GraphicViewerView) entry.getKey();
                JScrollBar jscrollbar = (JScrollBar) entry.getValue();
                jscrollbar.setOrientation(isVertical() ? 1 : 0);
            }

            d3 = false;
            update(1004, flag1 ? 1 : 0, null);
        }
    }

    public void setValues(int i, int j, int k, int l, int i1, int j1) {
        if (getValue() != i || getExtent() != j || getMinimum() != k
                || getMaximum() != l || getUnitIncrement() != i1
                || getBlockIncrement() != j1) {
            int k1 = getValue();
            int ai[] = new int[6];
            ai[0] = k1;
            ai[1] = getExtent();
            ai[2] = getMinimum();
            ai[3] = getMaximum();
            ai[4] = getUnitIncrement();
            ai[5] = getBlockIncrement();
            a(i, j, k, l, i1, j1);
            update(1002, 0, ai);
            GraphicViewerArea graphicviewerarea = getParent();
            if (graphicviewerarea != null)
                graphicviewerarea.update(1003, k1, null);
        }
    }

    @SuppressWarnings("rawtypes")
    private void a(int i, int j, int k, int l, int i1, int j1) {
        d7 = i;
        d1 = j;
        d6 = k;
        d4 = l;
        d3 = true;
        GraphicViewerView graphicviewerview;
        for (Iterator iterator = getIterator(); iterator.hasNext(); graphicviewerview
                .getCanvas().validate()) {
            Entry entry = (Entry) iterator.next();
            graphicviewerview = (GraphicViewerView) entry.getKey();
            JScrollBar jscrollbar = (JScrollBar) entry.getValue();
            jscrollbar.setValues(i, j, k, l);
            jscrollbar.setUnitIncrement(i1);
            jscrollbar.setBlockIncrement(j1);
        }

        d3 = false;
    }

    public int getValue() {
        return d7;
    }

    public void setValue(int i) {
        int j = getValue();
        if (j != i) {
            d7 = i;
            _mthnew(i);
            update(1003, j, null);
            GraphicViewerArea graphicviewerarea = getParent();
            if (graphicviewerarea != null)
                graphicviewerarea.update(1003, j, null);
        }
    }

    @SuppressWarnings("rawtypes")
    private void _mthnew(int i) {
        d3 = true;
        Iterator iterator = getIterator();
        do {
            if (!iterator.hasNext())
                break;
            Entry entry = (Entry) iterator.next();
            GraphicViewerView graphicviewerview = (GraphicViewerView) entry
                    .getKey();
            JScrollBar jscrollbar = (JScrollBar) entry.getValue();
            if (jscrollbar.getValue() != i) {
                jscrollbar.setValue(i);
                graphicviewerview.getCanvas().validate();
            }
        } while (true);
        d3 = false;
    }

    public int getExtent() {
        return d1;
    }

    public int getMinimum() {
        return d6;
    }

    public int getMaximum() {
        return d4;
    }

    public int getUnitIncrement() {
        return d2;
    }

    public int getBlockIncrement() {
        return d5;
    }

    public void valueChanged(int i, GraphicViewerView graphicviewerview) {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null)
            graphicviewerdocument.startTransaction();
        setValue(i);
        if (graphicviewerdocument != null)
            graphicviewerdocument.endTransaction(graphicviewerview
                    .getEditPresentationName(12));
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 1002 :
                int ai[] = new int[6];
                ai[0] = getValue();
                ai[1] = getExtent();
                ai[2] = getMinimum();
                ai[3] = getMaximum();
                ai[4] = getUnitIncrement();
                ai[5] = getBlockIncrement();
                graphicviewerdocumentchangededit.setNewValue(ai);
                return;

            case 1003 :
                graphicviewerdocumentchangededit.setNewValueInt(getValue());
                return;

            case 1004 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isVertical());
                return;
        }
        super.copyNewValueForRedo(graphicviewerdocumentchangededit);
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 1002 :
                int ai[] = flag
                        ? (int[]) graphicviewerdocumentchangededit
                                .getOldValue()
                        : (int[]) graphicviewerdocumentchangededit
                                .getNewValue();
                a(ai[0], ai[1], ai[2], ai[3], ai[4], ai[5]);
                return;

            case 1003 :
                _mthnew(graphicviewerdocumentchangededit.getValueInt(flag));
                return;

            case 1004 :
                setVertical(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;
        }
        super.changeValue(graphicviewerdocumentchangededit, flag);
    }

    public void SVGWriteObject(DomDoc domdoc, DomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            DomElement domelement1 = domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerScrollBar",
                    domelement);
            domelement1.setAttribute("vertical", d0 ? "true" : "false");
            domelement1.setAttribute("value", Integer.toString(d7));
            domelement1.setAttribute("extent", Integer.toString(d1));
            domelement1.setAttribute("minimum", Integer.toString(d6));
            domelement1.setAttribute("maximum", Integer.toString(d4));
            domelement1.setAttribute("unitincrement", Integer.toString(d2));
            domelement1.setAttribute("blockincrement", Integer.toString(d5));
        }
        DomElement domelement2 = domdoc.createElement("rect");
        SVGWriteAttributes(domelement2);
        domelement.appendChild(domelement2);
        super.SVGWriteObject(domdoc, domelement);
    }

    public DomNode SVGReadObject(DomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, DomElement domelement,
            DomElement domelement1) {
        if (domelement1 != null) {
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
            d0 = domelement1.getAttribute("vertical").equals("true");
            d7 = Integer.parseInt(domelement1.getAttribute("value"));
            d1 = Integer.parseInt(domelement1.getAttribute("extent"));
            d6 = Integer.parseInt(domelement1.getAttribute("minimum"));
            d4 = Integer.parseInt(domelement1.getAttribute("maximum"));
            d2 = Integer.parseInt(domelement1.getAttribute("unitincrement"));
            d5 = Integer.parseInt(domelement1.getAttribute("blockincrement"));
        }
        return domelement.getNextSibling();
    }

    public void SVGWriteAttributes(DomElement domelement) {
        super.SVGWriteAttributes(domelement);
        domelement.setAttribute("x", Integer.toString(getTopLeft().x));
        domelement.setAttribute("y", Integer.toString(getTopLeft().y));
        domelement.setAttribute("width", Integer.toString(getWidth()));
        domelement.setAttribute("height", Integer.toString(getHeight()));
        domelement.setAttribute("style",
                "stroke:black;stroke-width:1;fill:none");
    }

    public void SVGReadAttributes(DomElement domelement) {
        super.SVGReadAttributes(domelement);
        String s = domelement.getAttribute("x");
        String s1 = domelement.getAttribute("y");
        setTopLeft(new Point(Integer.parseInt(s), Integer.parseInt(s1)));
        String s2 = domelement.getAttribute("width");
        String s3 = domelement.getAttribute("height");
        setWidth(Integer.parseInt(s2));
        setHeight(Integer.parseInt(s3));
    }

    public static final int ChangedScrollBar = 1002;
    public static final int ChangedScrollBarValue = 1003;
    public static final int ChangedScrollBarVertical = 1004;
    private boolean d0;
    private int d7;
    private int d1;
    private int d6;
    private int d4;
    private int d2;
    private int d5;
    private transient boolean d3;

}