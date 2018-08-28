/*
 * Open Teradata Viewer ( graphic viewer )
 * Copyright (C), D. Campione
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

    public static final int ChangedScrollBar = 1002;
    public static final int ChangedScrollBarValue = 1003;
    public static final int ChangedScrollBarVertical = 1004;
    private boolean myVertical = true;
    private int myValue = 0;
    private int myExtent = 10;
    private int myMinimum = 0;
    private int myMaximum = 100;
    private int myUnitIncrement = 1;
    private int myBlockIncrement = 9;
    private transient boolean myValueChanging = false;

    public GraphicViewerScrollBar() {
    }

    public GraphicViewerScrollBar(Rectangle rectangle, boolean flag) {
        super(rectangle);
        myVertical = flag;
    }

    public GraphicViewerScrollBar(Point point, Dimension dimension, boolean flag) {
        super(point, dimension);
        myVertical = flag;
    }

    public GraphicViewerObject copyObject(
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerScrollBar graphicviewerscrollbar = (GraphicViewerScrollBar) super
                .copyObject(graphicviewercopyenvironment);
        if (graphicviewerscrollbar != null) {
            graphicviewerscrollbar.myVertical = myVertical;
            graphicviewerscrollbar.myValue = myValue;
            graphicviewerscrollbar.myExtent = myExtent;
            graphicviewerscrollbar.myMinimum = myMinimum;
            graphicviewerscrollbar.myMaximum = myMaximum;
            graphicviewerscrollbar.myUnitIncrement = myUnitIncrement;
            graphicviewerscrollbar.myBlockIncrement = myBlockIncrement;
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
        return myVertical;
    }

    public void setVertical(boolean flag) {
        boolean flag1 = myVertical;
        if (flag1 != flag) {
            myVertical = flag;
            myValueChanging = true;
            GraphicViewerView graphicviewerview;
            for (Iterator<?> iterator = getIterator(); iterator.hasNext(); graphicviewerview
                    .getCanvas().validate()) {
                Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
                graphicviewerview = (GraphicViewerView) entry.getKey();
                JScrollBar jscrollbar = (JScrollBar) entry.getValue();
                jscrollbar.setOrientation(isVertical() ? 1 : 0);
            }

            myValueChanging = false;
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
            setValuesInternal(i, j, k, l, i1, j1);
            update(1002, 0, ai);
            GraphicViewerArea graphicviewerarea = getParent();
            if (graphicviewerarea != null) {
                graphicviewerarea.update(1003, k1, null);
            }
        }
    }

    private void setValuesInternal(int i, int j, int k, int l, int i1, int j1) {
        myValue = i;
        myExtent = j;
        myMinimum = k;
        myMaximum = l;
        myValueChanging = true;
        GraphicViewerView graphicviewerview;
        for (Iterator<?> iterator = getIterator(); iterator.hasNext(); graphicviewerview
                .getCanvas().validate()) {
            Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
            graphicviewerview = (GraphicViewerView) entry.getKey();
            JScrollBar jscrollbar = (JScrollBar) entry.getValue();
            jscrollbar.setValues(i, j, k, l);
            jscrollbar.setUnitIncrement(i1);
            jscrollbar.setBlockIncrement(j1);
        }

        myValueChanging = false;
    }

    public int getValue() {
        return myValue;
    }

    public void setValue(int i) {
        int j = getValue();
        if (j != i) {
            myValue = i;
            setValueInternal(i);
            update(1003, j, null);
            GraphicViewerArea graphicviewerarea = getParent();
            if (graphicviewerarea != null) {
                graphicviewerarea.update(1003, j, null);
            }
        }
    }

    private void setValueInternal(int i) {
        myValueChanging = true;
        Iterator<?> iterator = getIterator();
        do {
            if (!iterator.hasNext()) {
                break;
            }
            Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
            GraphicViewerView graphicviewerview = (GraphicViewerView) entry
                    .getKey();
            JScrollBar jscrollbar = (JScrollBar) entry.getValue();
            if (jscrollbar.getValue() != i) {
                jscrollbar.setValue(i);
                graphicviewerview.getCanvas().validate();
            }
        } while (true);
        myValueChanging = false;
    }

    public int getExtent() {
        return myExtent;
    }

    public int getMinimum() {
        return myMinimum;
    }

    public int getMaximum() {
        return myMaximum;
    }

    public int getUnitIncrement() {
        return myUnitIncrement;
    }

    public int getBlockIncrement() {
        return myBlockIncrement;
    }

    public void valueChanged(int i, GraphicViewerView graphicviewerview) {
        GraphicViewerDocument graphicviewerdocument = getDocument();
        if (graphicviewerdocument != null) {
            graphicviewerdocument.startTransaction();
        }
        setValue(i);
        if (graphicviewerdocument != null) {
            graphicviewerdocument.endTransaction(graphicviewerview
                    .getEditPresentationName(12));
        }
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
                setValuesInternal(ai[0], ai[1], ai[2], ai[3], ai[4], ai[5]);
                return;

            case 1003 :
                setValueInternal(graphicviewerdocumentchangededit
                        .getValueInt(flag));
                return;

            case 1004 :
                setVertical(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;
        }
        super.changeValue(graphicviewerdocumentchangededit, flag);
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerScrollBar",
                            domelement);
            domelement1.setAttribute("vertical", myVertical ? "true" : "false");
            domelement1.setAttribute("value", Integer.toString(myValue));
            domelement1.setAttribute("extent", Integer.toString(myExtent));
            domelement1.setAttribute("minimum", Integer.toString(myMinimum));
            domelement1.setAttribute("maximum", Integer.toString(myMaximum));
            domelement1.setAttribute("unitincrement",
                    Integer.toString(myUnitIncrement));
            domelement1.setAttribute("blockincrement",
                    Integer.toString(myBlockIncrement));
        }
        IDomElement domelement2 = domdoc.createElement("rect");
        SVGWriteAttributes(domelement2);
        domelement.appendChild(domelement2);
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument,
            IDomElement domelement, IDomElement domelement1) {
        if (domelement1 != null) {
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
            myVertical = domelement1.getAttribute("vertical").equals("true");
            myValue = Integer.parseInt(domelement1.getAttribute("value"));
            myExtent = Integer.parseInt(domelement1.getAttribute("extent"));
            myMinimum = Integer.parseInt(domelement1.getAttribute("minimum"));
            myMaximum = Integer.parseInt(domelement1.getAttribute("maximum"));
            myUnitIncrement = Integer.parseInt(domelement1
                    .getAttribute("unitincrement"));
            myBlockIncrement = Integer.parseInt(domelement1
                    .getAttribute("blockincrement"));
        }
        return domelement.getNextSibling();
    }

    public void SVGWriteAttributes(IDomElement domelement) {
        super.SVGWriteAttributes(domelement);
        domelement.setAttribute("x", Integer.toString(getTopLeft().x));
        domelement.setAttribute("y", Integer.toString(getTopLeft().y));
        domelement.setAttribute("width", Integer.toString(getWidth()));
        domelement.setAttribute("height", Integer.toString(getHeight()));
        domelement.setAttribute("style",
                "stroke:black;stroke-width:1;fill:none");
    }

    public void SVGReadAttributes(IDomElement domelement) {
        super.SVGReadAttributes(domelement);
        String s = domelement.getAttribute("x");
        String s1 = domelement.getAttribute("y");
        setTopLeft(new Point(Integer.parseInt(s), Integer.parseInt(s1)));
        String s2 = domelement.getAttribute("width");
        String s3 = domelement.getAttribute("height");
        setWidth(Integer.parseInt(s2));
        setHeight(Integer.parseInt(s3));
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static class JSBDTListener implements DropTargetListener {

        JComponent comp;
        GraphicViewerView view;

        JSBDTListener(JComponent jcomponent, GraphicViewerView graphicviewerview) {
            comp = jcomponent;
            view = graphicviewerview;
        }

        public void dragEnter(DropTargetDragEvent droptargetdragevent) {
            view.onDragEnter(makeDragEvent(droptargetdragevent));
        }

        public void dragOver(DropTargetDragEvent droptargetdragevent) {
            view.onDragOver(makeDragEvent(droptargetdragevent));
        }

        public void dropActionChanged(DropTargetDragEvent droptargetdragevent) {
            view.onDropActionChanged(makeDragEvent(droptargetdragevent));
        }

        public void dragExit(DropTargetEvent droptargetevent) {
            view.onDragExit(droptargetevent);
        }

        public void drop(DropTargetDropEvent droptargetdropevent) {
            view.onDrop(makeDropEvent(droptargetdropevent));
        }

        DropTargetDragEvent makeDragEvent(
                DropTargetDragEvent droptargetdragevent) {
            return new DropTargetDragEvent(
                    droptargetdragevent.getDropTargetContext(),
                    a(droptargetdragevent.getLocation()),
                    droptargetdragevent.getDropAction(),
                    droptargetdragevent.getSourceActions());
        }

        DropTargetDropEvent makeDropEvent(
                DropTargetDropEvent droptargetdropevent) {
            return new DropTargetDropEvent(
                    droptargetdropevent.getDropTargetContext(),
                    a(droptargetdropevent.getLocation()),
                    droptargetdropevent.getDropAction(),
                    droptargetdropevent.getSourceActions());
        }

        Point a(Point point) {
            Point point1 = comp.getLocationOnScreen();
            Point point2 = view.getCanvas().getLocationOnScreen();
            return new Point((point1.x - point2.x) + point.x,
                    (point1.y - point2.y) + point.y);
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static class JSBListener implements AdjustmentListener {

        GraphicViewerScrollBar bar;
        GraphicViewerView view;

        JSBListener(GraphicViewerScrollBar graphicviewerscrollbar,
                GraphicViewerView graphicviewerview) {
            bar = graphicviewerscrollbar;
            view = graphicviewerview;
        }

        public void adjustmentValueChanged(AdjustmentEvent adjustmentevent) {
            if (!bar.myValueChanging
                    && adjustmentevent.getAdjustmentType() == 5) {
                int i = adjustmentevent.getValue();
                bar.valueChanged(i, view);
            }
        }
    }
}