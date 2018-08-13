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

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerStroke extends GraphicViewerDrawable {

    private static final long serialVersionUID = 5287373060672811611L;

    public static final int ChangedAddPoint = 101;
    public static final int ChangedRemovePoint = 102;
    public static final int ChangedModifiedPoint = 103;
    public static final int ChangedArrowHeads = 104;
    public static final int ChangedArrowLength = 105;
    public static final int ChangedArrowShaftLength = 106;

    /** @deprecated */
    public static final int ChangedArrowAngle = 107;
    public static final int ChangedHighlight = 108;
    public static final int ChangedCubic = 109;
    public static final int ChangedAllPoints = 110;
    public static final int ChangedArrowWidth = 111;
    private static final int LINE_FUZZ = 3;
    private static final int NUM_HEADPOINTS = 4;
    private static final double DEFAULT_ARROW_LENGTH = 10.0D;
    private static final double DEFAULT_ARROW_SHAFT_LENGTH = 8.0D;
    private static final double DEFAULT_ARROW_WIDTH = 8.0D;
    private static final int flagStrokeArrowStart = 32768;
    private static final int flagStrokeArrowEnd = 16384;
    private static final int flagStrokeCubic = 4096;
    protected ArrayList myPoints = new ArrayList();
    private GraphicViewerPen myHighlightPen = null;
    private ArrowInfo myArrowInfo = null;
    private transient Point myTempPoint = null;
    private transient GeneralPath myPath = null;

    public GraphicViewerStroke() {
        setBrush(GraphicViewerBrush.black);
    }

    public GraphicViewerObject copyObject(
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerStroke graphicviewerstroke = (GraphicViewerStroke) super
                .copyObject(graphicviewercopyenvironment);
        if (graphicviewerstroke != null) {
            for (int i = 0; i < myPoints.size(); i++) {
                Point point = (Point) myPoints.get(i);
                graphicviewerstroke.myPoints
                        .add(i, new Point(point.x, point.y));
            }

            if (myArrowInfo != null) {
                graphicviewerstroke.myArrowInfo = new ArrowInfo();
                graphicviewerstroke.myArrowInfo.myLength = myArrowInfo.myLength;
                graphicviewerstroke.myArrowInfo.myShaftLength = myArrowInfo.myShaftLength;
                graphicviewerstroke.myArrowInfo.myWidth = myArrowInfo.myWidth;
            }
            graphicviewerstroke.myHighlightPen = myHighlightPen;
        }
        return graphicviewerstroke;
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerStroke",
                            domelement);
            GraphicViewerPen graphicviewerpen = getHighlight();
            if (graphicviewerpen != null) {
                if (!domdoc.isRegisteredReference(getHighlight())) {
                    domelement1.setAttribute("embeddedhighlightpen", "true");
                    IDomElement domelement3 = domdoc.createElement("g");
                    domelement1.appendChild(domelement3);
                    getHighlight().SVGWriteObject(domdoc, domelement3);
                }
                domdoc.registerReferencingNode(domelement1, "highlightpen",
                        getHighlight());
            }
            if (myArrowInfo != null) {
                myArrowInfo.SVGWriteGraphicViewerElementAttributes(domelement1);
            }
            String s1 = "";
            String s2 = "";
            for (int k = 0; k < myPoints.size(); k++) {
                Point point3 = (Point) myPoints.get(k);
                if (k > 0) {
                    s1 = s1 + " ";
                    s2 = s2 + " ";
                }
                s1 = s1 + Integer.toString(point3.x);
                s2 = s2 + Integer.toString(point3.y);
            }

            domelement1.setAttribute("xpoints", s1);
            domelement1.setAttribute("ypoints", s2);
        }
        if (domdoc.SVGOutputEnabled()) {
            IDomElement domelement2 = domdoc.createElement("path");
            String s = "";
            int i = myPoints.size();
            if (isCubic() && i >= 4) {
                Point point = (Point) myPoints.get(0);
                s = s + "M";
                s = s + " " + Integer.toString(point.x);
                s = s + " " + Integer.toString(point.y);
                for (int l = 1; l < myPoints.size(); l += 3) {
                    Point point4 = (Point) myPoints.get(l);
                    if (l + 6 > myPoints.size()) {
                        l = myPoints.size() - 3;
                    }
                    Point point5 = (Point) myPoints.get(l + 1);
                    Point point6 = (Point) myPoints.get(l + 2);
                    s = s + " C " + Integer.toString(point4.x);
                    s = s + " " + Integer.toString(point4.y);
                    s = s + " " + Integer.toString(point5.x);
                    s = s + " " + Integer.toString(point5.y);
                    s = s + " " + Integer.toString(point6.x);
                    s = s + " " + Integer.toString(point6.y);
                }

                domelement2.setAttribute("d", s);
            } else if (i > 0) {
                Point point1 = (Point) myPoints.get(0);
                s = s + "M";
                s = s + " " + Integer.toString(point1.x);
                s = s + " " + Integer.toString(point1.y);
                for (int i1 = 1; i1 < myPoints.size(); i1++) {
                    Point point2 = (Point) myPoints.get(i1);
                    s = s + " L " + Integer.toString(point2.x);
                    s = s + " " + Integer.toString(point2.y);
                }

                domelement2.setAttribute("d", s);
            }
            IDomElement domelement4 = null;
            IDomElement domelement5 = null;
            if (myArrowInfo != null) {
                int ai[] = myArrowInfo.getXs();
                int ai1[] = myArrowInfo.getYs();
                if (hasArrowAtEnd()) {
                    domelement4 = domdoc.createElement("polygon");
                    String s4 = "";
                    Point point7 = getArrowToAnchorPoint();
                    Point point9 = getArrowToEndPoint();
                    if (point7 != null && point9 != null) {
                        calculateFilledArrowhead(point7.x, point7.y, point9.x,
                                point9.y, 1, ai, ai1);
                        for (int l1 = 0; l1 < ai.length; l1++) {
                            s4 = s4 + " " + Integer.toString(ai[l1]) + " "
                                    + Integer.toString(ai1[l1]);
                        }

                        domelement4.setAttribute("points", s4);
                    }
                }
                if (hasArrowAtStart()) {
                    domelement5 = domdoc.createElement("polygon");
                    String s5 = "";
                    Point point8 = getArrowFromAnchorPoint();
                    Point point10 = getArrowFromEndPoint();
                    if (point8 != null && point10 != null) {
                        calculateFilledArrowhead(point8.x, point8.y, point10.x,
                                point10.y, 0, ai, ai1);
                        for (int i2 = 0; i2 < ai.length; i2++) {
                            s5 = s5 + " " + Integer.toString(ai[i2]) + " "
                                    + Integer.toString(ai1[i2]);
                        }

                        domelement5.setAttribute("points", s5);
                    }
                }
            }
            SVGWriteAttributes(domelement2);
            String s3 = domelement2.getAttribute("style");
            int j1 = s3.indexOf("fill:");
            if (j1 != -1) {
                int k1 = s3.indexOf(";", j1) + 1;
                String s6 = s3.substring(0, j1) + "fill:none;"
                        + s3.substring(k1);
                domelement2.setAttribute("style", s6);
            }
            domelement.appendChild(domelement2);
            if (domelement4 != null) {
                SVGWriteAttributes(domelement4);
                domelement.appendChild(domelement4);
            }
            if (domelement5 != null) {
                SVGWriteAttributes(domelement5);
                domelement.appendChild(domelement5);
            }
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument,
            IDomElement domelement, IDomElement domelement1) {
        if (domelement1 != null) {
            if (domelement1.getAttribute("embeddedhighlightpen").equals("true")) {
                domdoc.SVGTraverseChildren(graphicviewerdocument, domelement1,
                        null, false);
            }
            String s = domelement1.getAttribute("highlightpen");
            domdoc.registerReferencingObject(this, "highlightpen", s);
            if (domelement1.getAttribute("arrow_length").length() > 0) {
                if (myArrowInfo == null) {
                    myArrowInfo = new ArrowInfo();
                }
                myArrowInfo.SVGReadGraphicViewerElementAttributes(domelement1);
            }
            String s2 = domelement1.getAttribute("xpoints");
            String s7;
            String s8;
            for (String s4 = domelement1.getAttribute("ypoints"); s2.length() > 0
                    && s4.length() > 0; addPoint(Integer.parseInt(s7),
                    Integer.parseInt(s8))) {
                int i = s2.indexOf(" ");
                if (i == -1) {
                    i = s2.length();
                }
                s7 = s2.substring(0, i);
                if (i >= s2.length()) {
                    s2 = "";
                } else {
                    s2 = s2.substring(i + 1);
                }
                i = s4.indexOf(" ");
                if (i == -1) {
                    i = s4.length();
                }
                s8 = s4.substring(0, i);
                if (i >= s4.length()) {
                    s4 = "";
                } else {
                    s4 = s4.substring(i + 1);
                }
            }

            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
            setBoundingRectInvalid(true);
        } else if (domelement.getTagName().equalsIgnoreCase("line")) {
            String s1 = domelement.getAttribute("x1");
            String s3 = domelement.getAttribute("y1");
            String s5 = domelement.getAttribute("x2");
            String s6 = domelement.getAttribute("y2");
            addPoint((new Float(s1)).intValue(), (new Float(s3)).intValue());
            addPoint((new Float(s5)).intValue(), (new Float(s6)).intValue());
            SVGReadAttributes(domelement);
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement, null);
        }
        return domelement.getNextSibling();
    }

    public void SVGUpdateReference(String s, Object obj) {
        super.SVGUpdateReference(s, obj);
        if (s.equals("highlightpen")) {
            setHighlight((GraphicViewerPen) obj);
        }
    }

    public void paint(Graphics2D graphics2d, GraphicViewerView graphicviewerview) {
        GraphicViewerPen graphicviewerpen = getPen();
        if (graphicviewerpen == null || graphicviewerpen.getStyle() == 0) {
            return;
        }
        GraphicViewerPen graphicviewerpen1 = getHighlight();
        if (getNumPoints() == 2) {
            Point point = getPoint(0);
            int i = point.x;
            int k = point.y;
            if (hasArrowAtStart() && getArrowShaftLength() > 0.0D) {
                if (myArrowInfo == null) {
                    myArrowInfo = new ArrowInfo();
                }
                int ai[] = myArrowInfo.getXs();
                int ai1[] = myArrowInfo.getYs();
                Point point2 = getArrowFromAnchorPoint();
                Point point3 = getArrowFromEndPoint();
                if (point2 != null && point3 != null) {
                    calculateFilledArrowhead(point2.x, point2.y, point3.x,
                            point3.y, 0, ai, ai1);
                    i = ai[0];
                    k = ai1[0];
                }
            }
            Point point1 = getPoint(1);
            int l = point1.x;
            int i1 = point1.y;
            if (hasArrowAtEnd() && getArrowShaftLength() > 0.0D) {
                if (myArrowInfo == null) {
                    myArrowInfo = new ArrowInfo();
                }
                int ai2[] = myArrowInfo.getXs();
                int ai3[] = myArrowInfo.getYs();
                Point point4 = getArrowToAnchorPoint();
                Point point5 = getArrowToEndPoint();
                if (point4 != null && point5 != null) {
                    calculateFilledArrowhead(point4.x, point4.y, point5.x,
                            point5.y, 1, ai2, ai3);
                    l = ai2[0];
                    i1 = ai3[0];
                }
            }
            if (graphicviewerpen1 != null && graphicviewerpen1.getStyle() != 0) {
                drawLine(graphics2d, graphicviewerpen1, i, k, l, i1);
            }
            drawLine(graphics2d, graphicviewerpen, i, k, l, i1);
        } else {
            if (graphicviewerpen1 != null && graphicviewerpen1.getStyle() != 0) {
                drawPath(graphics2d, graphicviewerpen1, null,
                        getPath(graphicviewerview));
            }
            drawPath(graphics2d, graphicviewerpen, null,
                    getPath(graphicviewerview));
        }
        drawArrowHeads(graphics2d);
    }

    void makePath(GeneralPath paramGeneralPath,
            GraphicViewerView paramGraphicViewerView) {
        int i = getNumPoints();
        if ((isCubic()) && (i >= 4)) {
            Point localPoint1 = getPoint(0);
            int j = localPoint1.x;
            int k = localPoint1.y;
            if ((hasArrowAtStart()) && (getArrowShaftLength() > 0.0D)) {
                if (myArrowInfo == null) {
                    myArrowInfo = new ArrowInfo();
                }
                int[] arrayOfInt1 = myArrowInfo.getXs();
                int localObject1[] = myArrowInfo.getYs();
                Point localPoint2 = getArrowFromAnchorPoint();
                Point localPoint3 = getArrowFromEndPoint();
                if ((localPoint2 != null) && (localPoint3 != null)) {
                    calculateFilledArrowhead(localPoint2.x, localPoint2.y,
                            localPoint3.x, localPoint3.y, 0, arrayOfInt1,
                            (int[]) localObject1);
                    j = arrayOfInt1[0];
                    k = localObject1[0];
                }
            }
            paramGeneralPath.moveTo(j, k);
            for (int m = 3; m < i; m += 3) {
                Point localObject1 = getPoint(m - 2);
                if (m + 3 >= i) {
                    m = i - 1;
                }
                Point localPoint2 = getPoint(m - 1);
                Point localPoint3 = getPoint(m);
                int i3 = localPoint3.x;
                int i4 = localPoint3.y;
                if ((m == i - 1) && (hasArrowAtEnd())
                        && (getArrowShaftLength() > 0.0D)) {
                    if (myArrowInfo == null) {
                        myArrowInfo = new ArrowInfo();
                    }
                    int[] localObject2 = myArrowInfo.getXs();
                    int[] localObject3 = myArrowInfo.getYs();
                    Point localPoint4 = getArrowToAnchorPoint();
                    Point localPoint5 = getArrowToEndPoint();
                    if ((localPoint4 != null) && (localPoint5 != null)) {
                        calculateFilledArrowhead(localPoint4.x, localPoint4.y,
                                localPoint5.x, localPoint5.y, 1,
                                (int[]) localObject2, (int[]) localObject3);
                        i3 = localObject2[0];
                        i4 = localObject3[0];
                    }
                }
                paramGeneralPath.curveTo(((Point) localObject1).x,
                        ((Point) localObject1).y, localPoint2.x, localPoint2.y,
                        i3, i4);
            }
        } else if (i >= 2) {
            Point localPoint1 = getPoint(0);
            int j = localPoint1.x;
            int k = localPoint1.y;
            if ((hasArrowAtStart()) && (getArrowShaftLength() > 0.0D)) {
                if (myArrowInfo == null) {
                    myArrowInfo = new ArrowInfo();
                }
                int[] arrayOfInt2 = myArrowInfo.getXs();
                int[] localObject1 = myArrowInfo.getYs();
                Point localPoint2 = getArrowFromAnchorPoint();
                Point localPoint3 = getArrowFromEndPoint();
                if ((localPoint2 != null) && (localPoint3 != null)) {
                    calculateFilledArrowhead(localPoint2.x, localPoint2.y,
                            localPoint3.x, localPoint3.y, 0, arrayOfInt2,
                            (int[]) localObject1);
                    j = arrayOfInt2[0];
                    k = localObject1[0];
                }
            }
            paramGeneralPath.moveTo(j, k);
            for (int n = 1; n < i; n++) {
                Point localObject1 = getPoint(n);
                int i1 = ((Point) localObject1).x;
                int i2 = ((Point) localObject1).y;
                if ((n == i - 1) && (hasArrowAtEnd())
                        && (getArrowShaftLength() > 0.0D)) {
                    if (myArrowInfo == null) {
                        myArrowInfo = new ArrowInfo();
                    }
                    int[] arrayOfInt3 = myArrowInfo.getXs();
                    int[] arrayOfInt4 = myArrowInfo.getYs();
                    Point localObject2 = getArrowToAnchorPoint();
                    Point localObject3 = getArrowToEndPoint();
                    if ((localObject2 != null) && (localObject3 != null)) {
                        calculateFilledArrowhead(((Point) localObject2).x,
                                ((Point) localObject2).y,
                                ((Point) localObject3).x,
                                ((Point) localObject3).y, 1, arrayOfInt3,
                                arrayOfInt4);
                        i1 = arrayOfInt3[0];
                        i2 = arrayOfInt4[0];
                    }
                }
                paramGeneralPath.lineTo(i1, i2);
            }
        }
    }

    GeneralPath getPath(GraphicViewerView graphicviewerview) {
        if (myPath == null) {
            int i = getNumPoints();
            myPath = new GeneralPath(1, 2 * i);
        }
        if (myPath.getCurrentPoint() == null) {
            makePath(myPath, graphicviewerview);
        }
        return myPath;
    }

    void resetPath() {
        if (myPath != null) {
            myPath.reset();
        }
    }

    public boolean isCubic() {
        return (getInternalFlags() & 0x1000) != 0;
    }

    public void setCubic(boolean flag) {
        boolean flag1 = (getInternalFlags() & 0x1000) != 0;
        if (flag1 != flag) {
            if (flag) {
                setInternalFlags(getInternalFlags() | 0x1000);
            } else {
                setInternalFlags(getInternalFlags() & 0xffffefff);
            }
            resetPath();
            update(109, flag1 ? 1 : 0, null);
        }
    }

    public final int addPoint(Point point) {
        return addPoint(point.x, point.y);
    }

    public int addPoint(int i, int k) {
        return internalInsertPoint(myPoints.size(), i, k, false);
    }

    public final int insertPoint(int i, Point point) {
        return insertPoint(i, point.x, point.y);
    }

    public int insertPoint(int i, int k, int l) {
        return internalInsertPoint(i, k, l, false);
    }

    private int internalInsertPoint(int i, int k, int l, boolean flag) {
        Point point = new Point(k, l);
        int i1 = i;
        try {
            myPoints.add(i, point);
        } catch (Exception e) {
            myPoints.add(point);
            i1 = myPoints.size() - 1;
        }
        if (!flag) {
            setBoundingRectInvalid(true);
        }
        resetPath();
        update(101, i1, point);
        return i1;
    }

    public void removePoint(int i) {
        internalRemovePoint(i, false);
    }

    private void internalRemovePoint(int i, boolean flag) {
        if (i >= 0 && i < getNumPoints()) {
            Point point = null;
            try {
                point = (Point) myPoints.remove(i);
            } catch (Exception e) {
                ExceptionDialog.ignoreException(e);
            }
            if (!flag) {
                setBoundingRectInvalid(true);
            }
            resetPath();
            update(102, i, point);
        }
    }

    public Point getPoint(int i) {
        if (i >= 0 && i < getNumPoints()) {
            return (Point) myPoints.get(i);
        } else {
            return null;
        }
    }

    public int getPointX(int i) {
        Point point = getPoint(i);
        if (point != null) {
            return point.x;
        } else {
            return -1;
        }
    }

    public int getPointY(int i) {
        Point point = getPoint(i);
        if (point != null) {
            return point.y;
        } else {
            return -1;
        }
    }

    public final void setPoint(int i, Point point) {
        setPoint(i, point.x, point.y);
    }

    public void setPoint(int i, int k, int l) {
        internalSetPoint(i, k, l, false);
    }

    private void internalSetPoint(int i, int k, int l, boolean flag) {
        Point point = getPoint(i);
        if (point != null && (point.x != k || point.y != l)) {
            if (myTempPoint == null) {
                myTempPoint = new Point(point.x, point.y);
            } else {
                myTempPoint.x = point.x;
                myTempPoint.y = point.y;
            }
            point.x = k;
            point.y = l;
            if (!flag) {
                setBoundingRectInvalid(true);
            }
            resetPath();
            update(103, i, myTempPoint);
        }
    }

    public void removeAllPoints() {
        foredate(110);
        myPoints.clear();
        setBoundingRectInvalid(true);
        resetPath();
        update(110, 0, null);
    }

    public void setPoints(Vector vector) {
        foredate(110);
        myPoints.clear();
        for (int i = 0; i < vector.size(); i++) {
            Point point = (Point) vector.get(i);
            Point point1 = new Point(point.x, point.y);
            myPoints.add(point1);
        }

        setBoundingRectInvalid(true);
        resetPath();
        update(110, 0, null);
    }

    public void setPoints(ArrayList arraylist) {
        internalSetPoints(arraylist, false);
    }

    void internalSetPoints(ArrayList arraylist, boolean flag) {
        foredate(110);
        myPoints.clear();
        for (int i = 0; i < arraylist.size(); i++) {
            Point point = (Point) arraylist.get(i);
            Point point1 = new Point(point.x, point.y);
            myPoints.add(point1);
        }

        if (!flag) {
            setBoundingRectInvalid(true);
        }
        resetPath();
        update(110, 0, null);
    }

    public Vector copyPoints() {
        Vector vector = new Vector();
        int i = getNumPoints();
        for (int k = 0; k < i; k++) {
            Point point = getPoint(k);
            vector.add(k, new Point(point.x, point.y));
        }

        return vector;
    }

    public ArrayList copyPointsArray() {
        ArrayList arraylist = new ArrayList();
        int i = getNumPoints();
        for (int k = 0; k < i; k++) {
            Point point = getPoint(k);
            arraylist.add(k, new Point(point.x, point.y));
        }

        return arraylist;
    }

    public int getNumPoints() {
        return myPoints.size();
    }

    public Point getStartPoint() {
        return getPoint(0);
    }

    public Point getEndPoint() {
        return getPoint(getNumPoints() - 1);
    }

    public int getFirstPickPoint() {
        return 0;
    }

    public int getLastPickPoint() {
        return getNumPoints() - 1;
    }

    public Rectangle handleResize(Graphics2D graphics2d,
            GraphicViewerView graphicviewerview, Rectangle rectangle,
            Point point, int i, int k, int l, int i1) {
        if (i >= 100) {
            setPoint(i - 100, point);
        } else {
            return super.handleResize(graphics2d, graphicviewerview, rectangle,
                    point, i, k, l, i1);
        }
        return null;
    }

    protected void geometryChange(Rectangle rectangle) {
        if (rectangle.width == getWidth() && rectangle.height == getHeight()) {
            Rectangle rectangle1 = getBoundingRect();
            int i = rectangle1.x - rectangle.x;
            int k = rectangle1.y - rectangle.y;
            if (i == 0 && k == 0) {
                return;
            }
            foredate(110);
            setSuspendUpdates(true);
            for (int l = 0; l < getNumPoints(); l++) {
                Point point = getPoint(l);
                if (point != null) {
                    int i1 = point.x + i;
                    int k1 = point.y + k;
                    setPoint(l, i1, k1);
                }
            }

            setBoundingRectInvalid(false);
            setSuspendUpdates(false);
            resetPath();
            update(110, 0, null);
        } else {
            Rectangle rectangle2 = getBoundingRect();
            double d = 1.0D;
            if (rectangle.width != 0) {
                d = (double) rectangle2.width / (double) rectangle.width;
            }
            double d1 = 1.0D;
            if (rectangle.height != 0) {
                d1 = (double) rectangle2.height / (double) rectangle.height;
            }
            foredate(110);
            setSuspendUpdates(true);
            for (int j1 = 0; j1 < getNumPoints(); j1++) {
                Point point1 = getPoint(j1);
                if (point1 != null) {
                    int l1 = rectangle2.x
                            + (int) Math.rint((double) (point1.x - rectangle.x)
                                    * d);
                    int i2 = rectangle2.y
                            + (int) Math.rint((double) (point1.y - rectangle.y)
                                    * d1);
                    setPoint(j1, l1, i2);
                }
            }

            setBoundingRectInvalid(false);
            setSuspendUpdates(false);
            resetPath();
            update(110, 0, null);
        }
    }

    protected void gainedSelection(GraphicViewerSelection graphicviewerselection) {
        if (!isResizable()) {
            graphicviewerselection.createBoundingHandle(this);
            return;
        }
        int i = getLastPickPoint();
        for (int k = getFirstPickPoint(); k <= i; k++) {
            Point point = getPoint(k);
            if (point != null) {
                graphicviewerselection.createResizeHandle(this, point.x,
                        point.y, 100 + k, true);
            }
        }
    }

    protected Rectangle computeBoundingRect() {
        int i = getNumPoints();
        if (i == 0) {
            return null;
        }
        Point point = getPoint(0);
        int k = point.x;
        int l = point.y;
        int i1 = point.x;
        int j1 = point.y;
        if (isCubic() && i >= 4) {
            for (int l1 = 3; l1 < i; l1 += 3) {
                Point point2 = getPoint(l1 - 3);
                Point point3 = getPoint(l1 - 2);
                if (l1 + 3 >= i) {
                    l1 = i - 1;
                }
                Point point4 = getPoint(l1 - 1);
                Point point5 = getPoint(l1);
                Rectangle rectangle = BezierBounds(point2.x, point2.y,
                        point3.x, point3.y, point4.x, point4.y, point5.x,
                        point5.y);
                k = Math.min(k, rectangle.x);
                l = Math.min(l, rectangle.y);
                i1 = Math.max(i1, rectangle.x + rectangle.width);
                j1 = Math.max(j1, rectangle.y + rectangle.height);
            }
        } else {
            for (int k1 = 1; k1 < i; k1++) {
                Point point1 = getPoint(k1);
                k = Math.min(k, point1.x);
                l = Math.min(l, point1.y);
                i1 = Math.max(i1, point1.x);
                j1 = Math.max(j1, point1.y);
            }
        }
        return new Rectangle(k, l, i1 - k, j1 - l);
    }

    public void expandRectByPenWidth(Rectangle rectangle) {
        GraphicViewerPen graphicviewerpen = getPen();
        if (graphicviewerpen != null) {
            int i = 5 * graphicviewerpen.getWidth();
            if (getHighlight() != null) {
                i = Math.max(i, 5 * getHighlight().getWidth());
            }
            if (hasArrowAtEnd() || hasArrowAtStart()) {
                i = Math.max(i, (int) getArrowLength());
                i = Math.max(i, (int) getArrowWidth());
            }
            rectangle.x -= i;
            rectangle.y -= i;
            rectangle.width += i * 2;
            rectangle.height += i * 2;
        }
    }

    public boolean isPointInObj(Point point) {
        int i = getSegmentNearPoint(point);
        return i >= 0;
    }

    public int getSegmentNearPoint(Point point) {
        Rectangle rectangle = getBoundingRect();
        int i = getPen() == null ? 1 : getPen().getWidth();
        if (point.x < rectangle.x - i
                || point.x > rectangle.x + rectangle.width + i
                || point.y < rectangle.y - i
                || point.y > rectangle.y + rectangle.height + i) {
            return -1;
        }
        int k = getNumPoints();
        if (k <= 1) {
            return -1;
        }
        i += 3;
        if (isCubic() && k >= 4) {
            i *= Math.max(1, Math.max(rectangle.width, rectangle.height) / 100);
            for (int i1 = 3; i1 < k; i1 += 3) {
                int j1 = i1;
                Point point1 = getPoint(i1 - 3);
                Point point2 = getPoint(i1 - 2);
                if (i1 + 3 >= k) {
                    i1 = k - 1;
                }
                Point point4 = getPoint(i1 - 1);
                Point point6 = getPoint(i1);
                if (BezierContainsPoint(point1.x, point1.y, point2.x, point2.y,
                        point4.x, point4.y, point6.x, point6.y, i, point)) {
                    return j1;
                }
            }

        } else {
            for (int l = 0; l < k - 1; l++) {
                Point point3 = getPoint(l);
                Point point5 = getPoint(l + 1);
                if (LineContainsPoint(point3.x, point3.y, point5.x, point5.y,
                        i, point)) {
                    return l;
                }
            }
        }
        return -1;
    }

    static boolean LineContainsPoint(int paramInt1, int paramInt2,
            int paramInt3, int paramInt4, int paramInt5, Point paramPoint) {
        return LineContainsPoint(paramInt1, paramInt2, paramInt3, paramInt4,
                paramInt5, paramPoint.x, paramPoint.y);
    }

    static boolean LineContainsPoint(int paramInt1, int paramInt2,
            int paramInt3, int paramInt4, int paramInt5, int paramInt6,
            int paramInt7) {
        int j;
        int i;
        if (paramInt1 < paramInt3) {
            j = paramInt1;
            i = paramInt3;
        } else {
            j = paramInt3;
            i = paramInt1;
        }
        int m;
        int k;
        if (paramInt2 < paramInt4) {
            m = paramInt2;
            k = paramInt4;
        } else {
            m = paramInt4;
            k = paramInt2;
        }
        if ((paramInt1 == paramInt3) && (paramInt1 - paramInt5 <= paramInt6)
                && (paramInt6 <= paramInt1 + paramInt5) && (m <= paramInt7)
                && (paramInt7 <= k)) {
            return true;
        }
        if ((paramInt2 == paramInt4) && (paramInt2 - paramInt5 <= paramInt7)
                && (paramInt7 <= paramInt2 + paramInt5) && (j <= paramInt6)
                && (paramInt6 <= i)) {
            return true;
        }
        int n = i + paramInt5;
        int i1 = j - paramInt5;
        if ((i1 <= paramInt6) && (paramInt6 <= n)) {
            int i2 = k + paramInt5;
            int i3 = m - paramInt5;
            if ((i3 <= paramInt7) && (paramInt7 <= i2)) {
                double d1;
                double d2;
                if (n - i1 > i2 - i3) {
                    if (Math.abs(paramInt1 - paramInt3) > paramInt5) {
                        d1 = (paramInt4 - paramInt2) / (paramInt3 - paramInt1);
                        d2 = d1 * (paramInt6 - paramInt1) + paramInt2;
                        if ((d2 - paramInt5 <= paramInt7)
                                && (paramInt7 <= d2 + paramInt5)) {
                            return true;
                        }
                    } else {
                        return true;
                    }
                } else if (Math.abs(paramInt2 - paramInt4) > paramInt5) {
                    d1 = (paramInt3 - paramInt1) / (paramInt4 - paramInt2);
                    d2 = d1 * (paramInt7 - paramInt2) + paramInt1;
                    if ((d2 - paramInt5 <= paramInt6)
                            && (paramInt6 <= d2 + paramInt5)) {
                        return true;
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean BezierContainsPoint(int paramInt1, int paramInt2,
            int paramInt3, int paramInt4, int paramInt5, int paramInt6,
            int paramInt7, int paramInt8, int paramInt9, Point paramPoint) {
        if ((!LineContainsPoint(paramInt1, paramInt2, paramInt7, paramInt8,
                paramInt9, paramInt3, paramInt4))
                || (!LineContainsPoint(paramInt1, paramInt2, paramInt7,
                        paramInt8, paramInt9, paramInt5, paramInt6))) {
            int i = (paramInt1 + paramInt3) / 2;
            int j = (paramInt2 + paramInt4) / 2;
            int k = (paramInt3 + paramInt5) / 2;
            int m = (paramInt4 + paramInt6) / 2;
            int n = (paramInt5 + paramInt7) / 2;
            int i1 = (paramInt6 + paramInt8) / 2;
            int i2 = (i + k) / 2;
            int i3 = (j + m) / 2;
            int i4 = (k + n) / 2;
            int i5 = (m + i1) / 2;
            int i6 = (i2 + i4) / 2;
            int i7 = (i3 + i5) / 2;
            return (BezierContainsPoint(paramInt1, paramInt2, i, j, i2, i3, i6,
                    i7, paramInt9, paramPoint))
                    || (BezierContainsPoint(i6, i7, i4, i5, n, i1, paramInt7,
                            paramInt8, paramInt9, paramPoint));
        }
        return LineContainsPoint(paramInt1, paramInt2, paramInt7, paramInt8,
                paramInt9, paramPoint.x, paramPoint.y);
    }

    static void BezierMidPoint(int i, int k, int l, int i1, int j1, int k1,
            int l1, int i2, Point point, Point point1) {
        int l2 = (i + l) / 2;
        int i3 = (k + i1) / 2;
        int j3 = (l + j1) / 2;
        int k3 = (i1 + k1) / 2;
        int l3 = (j1 + l1) / 2;
        int i4 = (k1 + i2) / 2;
        point.x = (l2 + j3) / 2;
        point.y = (i3 + k3) / 2;
        point1.x = (j3 + l3) / 2;
        point1.y = (k3 + i4) / 2;
    }

    static Rectangle BezierBounds(int i, int k, int l, int i1, int j1, int k1,
            int l1, int i2) {
        int j2 = i;
        int k2 = k;
        int l2 = (i + l) / 2;
        int i3 = (k + i1) / 2;
        int j3 = (l + j1) / 2;
        int k3 = (i1 + k1) / 2;
        int l3 = (j1 + l1) / 2;
        int i4 = (k1 + i2) / 2;
        int j4 = l1;
        int k4 = i2;
        int l4 = j2;
        int i5 = k2;
        int j5 = (j2 + l2) / 2;
        int k5 = (k2 + i3) / 2;
        int l5 = (l2 + j3) / 2;
        int i6 = (i3 + k3) / 2;
        int j6 = (j3 + l3) / 2;
        int k6 = (k3 + i4) / 2;
        int l6 = (l3 + j4) / 2;
        int i7 = (i4 + k4) / 2;
        int j7 = j4;
        int k7 = k4;
        int l7 = l4;
        int i8 = l4;
        if (j5 < l7) {
            l7 = j5;
        } else if (j5 > i8) {
            i8 = j5;
        }
        if (l5 < l7) {
            l7 = l5;
        } else if (l5 > i8) {
            i8 = l5;
        }
        if (j6 < l7) {
            l7 = j6;
        } else if (j6 > i8) {
            i8 = j6;
        }
        if (l6 < l7) {
            l7 = l6;
        } else if (l6 > i8) {
            i8 = l6;
        }
        if (j7 < l7) {
            l7 = j7;
        } else if (j7 > i8) {
            i8 = j7;
        }
        int j8 = i5;
        int k8 = i5;
        if (k5 < j8) {
            j8 = k5;
        } else if (k5 > k8) {
            k8 = k5;
        }
        if (i6 < j8) {
            j8 = i6;
        } else if (i6 > k8) {
            k8 = i6;
        }
        if (k6 < j8) {
            j8 = k6;
        } else if (k6 > k8) {
            k8 = k6;
        }
        if (i7 < j8) {
            j8 = i7;
        } else if (i7 > k8) {
            k8 = i7;
        }
        if (k7 < j8) {
            j8 = k7;
        } else if (k7 > k8) {
            k8 = k7;
        }
        return new Rectangle(l7 - 10, j8 - 10, (i8 - l7) + 20, (k8 - j8) + 20);
    }

    static boolean BezierNearestIntersectionOnLine(int i, int k, int l, int i1,
            int j1, int k1, int l1, int i2, int j2, int k2, int l2, int i3,
            Point point) {
        int j3 = i;
        int k3 = k;
        int l3 = (i + l) / 2;
        int i4 = (k + i1) / 2;
        int j4 = (l + j1) / 2;
        int k4 = (i1 + k1) / 2;
        int l4 = (j1 + l1) / 2;
        int i5 = (k1 + i2) / 2;
        int j5 = l1;
        int k5 = i2;
        int l5 = j3;
        int i6 = k3;
        int j6 = (j3 + l3) / 2;
        int k6 = (k3 + i4) / 2;
        int l6 = (l3 + j4) / 2;
        int i7 = (i4 + k4) / 2;
        int j7 = (j4 + l4) / 2;
        int k7 = (k4 + i5) / 2;
        int l7 = (l4 + j5) / 2;
        int i8 = (i5 + k5) / 2;
        int j8 = j5;
        int k8 = k5;
        Point point1 = new Point(0, 0);
        double d = 1E+021D;
        if (getNearestIntersectionOnLine(l5, i6, j6, k6, j2, k2, l2, i3, point1)) {
            float f = (point1.x - j2) * (point1.x - j2) + (point1.y - k2)
                    * (point1.y - k2);
            if ((double) f < d) {
                d = f;
                point.x = point1.x;
                point.y = point1.y;
            }
        }
        if (getNearestIntersectionOnLine(j6, k6, l6, i7, j2, k2, l2, i3, point1)) {
            float f1 = (point1.x - j2) * (point1.x - j2) + (point1.y - k2)
                    * (point1.y - k2);
            if ((double) f1 < d) {
                d = f1;
                point.x = point1.x;
                point.y = point1.y;
            }
        }
        if (getNearestIntersectionOnLine(l6, i7, j7, k7, j2, k2, l2, i3, point1)) {
            float f2 = (point1.x - j2) * (point1.x - j2) + (point1.y - k2)
                    * (point1.y - k2);
            if ((double) f2 < d) {
                d = f2;
                point.x = point1.x;
                point.y = point1.y;
            }
        }
        if (getNearestIntersectionOnLine(j7, k7, l7, i8, j2, k2, l2, i3, point1)) {
            float f3 = (point1.x - j2) * (point1.x - j2) + (point1.y - k2)
                    * (point1.y - k2);
            if ((double) f3 < d) {
                d = f3;
                point.x = point1.x;
                point.y = point1.y;
            }
        }
        if (getNearestIntersectionOnLine(l7, i8, j8, k8, j2, k2, l2, i3, point1)) {
            float f4 = (point1.x - j2) * (point1.x - j2) + (point1.y - k2)
                    * (point1.y - k2);
            if ((double) f4 < d) {
                d = f4;
                point.x = point1.x;
                point.y = point1.y;
            }
        }
        return d < 1.0000000200408773E+021D;
    }

    public static boolean getNearestPointOnLine(Point point, Point point1,
            Point point2, Point point3) {
        return getNearestPointOnLine(point.x, point.y, point1.x, point1.y,
                point2.x, point2.y, point3);
    }

    public static boolean getNearestPointOnLine(int i, int k, int l, int i1,
            int j1, int k1, Point point) {
        if (i == l) {
            int l1;
            int j2;
            if (k < i1) {
                l1 = k;
                j2 = i1;
            } else {
                l1 = i1;
                j2 = k;
            }
            int l2 = k1;
            if (l2 < l1) {
                point.x = i;
                point.y = l1;
                return false;
            }
            if (l2 > j2) {
                point.x = i;
                point.y = j2;
                return false;
            } else {
                point.x = i;
                point.y = l2;
                return true;
            }
        }
        if (k == i1) {
            int i2;
            int k2;
            if (i < l) {
                i2 = i;
                k2 = l;
            } else {
                i2 = l;
                k2 = i;
            }
            int i3 = j1;
            if (i3 < i2) {
                point.x = i2;
                point.y = k;
                return false;
            }
            if (i3 > k2) {
                point.x = k2;
                point.y = k;
                return false;
            } else {
                point.x = i3;
                point.y = k;
                return true;
            }
        }
        double d = (l - i) * (l - i) + (i1 - k) * (i1 - k);
        double d1 = (double) ((i - j1) * (i - l) + (k - k1) * (k - i1)) / d;
        if (d1 < 0.0D) {
            point.x = i;
            point.y = k;
            return false;
        }
        if (d1 > 1.0D) {
            point.x = l;
            point.y = i1;
            return false;
        } else {
            double d2 = (double) i + d1 * (double) (l - i);
            double d3 = (double) k + d1 * (double) (i1 - k);
            point.x = (int) Math.rint(d2);
            point.y = (int) Math.rint(d3);
            return true;
        }
    }

    public static boolean getNearestIntersectionOnLine(int i, int k, int l,
            int i1, int j1, int k1, int l1, int i2, Point point) {
        if (j1 == l1) {
            if (i == l) {
                getNearestPointOnLine(i, k, l, i1, j1, k1, point);
                return false;
            } else {
                double d = (double) (i1 - k) / (double) (l - i);
                int j2 = (int) Math.rint(d * (double) (j1 - i) + (double) k);
                return getNearestPointOnLine(i, k, l, i1, j1, j2, point);
            }
        }
        double d1 = (double) (i2 - k1) / (double) (l1 - j1);
        if (i == l) {
            int k2 = (int) Math.rint(d1 * (double) (i - j1) + (double) k1);
            if (k2 < Math.min(k, i1)) {
                point.x = i;
                point.y = Math.min(k, i1);
                return false;
            }
            if (k2 > Math.max(k, i1)) {
                point.x = i;
                point.y = Math.max(k, i1);
                return false;
            } else {
                point.x = i;
                point.y = k2;
                return true;
            }
        }
        double d2 = (double) (i1 - k) / (double) (l - i);
        if (d1 == d2) {
            getNearestPointOnLine(i, k, l, i1, j1, k1, point);
            return false;
        }
        int l2 = (int) Math
                .rint((((d2 * (double) i - d1 * (double) j1) + (double) k1) - (double) k)
                        / (d2 - d1));
        if (d2 == 0.0D) {
            if (l2 < Math.min(i, l)) {
                point.x = Math.min(i, l);
                point.y = k;
                return false;
            }
            if (l2 > Math.max(i, l)) {
                point.x = Math.max(i, l);
                point.y = k;
                return false;
            } else {
                point.x = l2;
                point.y = k;
                return true;
            }
        } else {
            int i3 = (int) Math.rint(d2 * (double) (l2 - i) + (double) k);
            return getNearestPointOnLine(i, k, l, i1, l2, i3, point);
        }
    }

    public boolean getNearestIntersectionPoint(int i, int k, int l, int i1,
            Point point) {
        getBoundingRect();
        Point point1 = new Point(0, 0);
        double d = 1E+021D;
        int j1 = getNumPoints();
        if (isCubic() && j1 >= 4) {
            for (int l1 = 3; l1 < j1; l1 += 3) {
                Point point2 = getPoint(l1 - 3);
                Point point3 = getPoint(l1 - 2);
                if (l1 + 3 >= j1) {
                    l1 = j1 - 1;
                }
                Point point5 = getPoint(l1 - 1);
                Point point7 = getPoint(l1);
                if (BezierNearestIntersectionOnLine(point2.x, point2.y,
                        point3.x, point3.y, point5.x, point5.y, point7.x,
                        point7.y, i, k, l, i1, point1)) {
                    double d2 = (point1.x - i) * (point1.x - i)
                            + (point1.y - k) * (point1.y - k);
                    if (d2 < d) {
                        d = d2;
                        point.x = point1.x;
                        point.y = point1.y;
                    }
                }
            }
        } else {
            for (int k1 = 0; k1 < j1 - 1; k1++) {
                Point point4 = getPoint(k1);
                Point point6 = getPoint(k1 + 1);
                if (!getNearestIntersectionOnLine(point4.x, point4.y, point6.x,
                        point6.y, i, k, l, i1, point1)) {
                    continue;
                }
                double d1 = (point1.x - i) * (point1.x - i) + (point1.y - k)
                        * (point1.y - k);
                if (d1 < d) {
                    d = d1;
                    point.x = point1.x;
                    point.y = point1.y;
                }
            }

        }
        return d < 1E+021D;
    }

    public void setArrowHeads(boolean flag, boolean flag1) {
        boolean flag2 = (getInternalFlags() & 0x4000) != 0;
        boolean flag3 = (getInternalFlags() & 0x8000) != 0;
        if (flag2 != flag1 || flag3 != flag) {
            int i = 0;
            int k = 0;
            if (flag) {
                i |= 0x8000;
            } else {
                k |= 0x8000;
            }
            if (flag1) {
                i |= 0x4000;
            } else {
                k |= 0x4000;
            }
            setInternalFlags(getInternalFlags() & ~k | i);
            update(104, (flag2 ? 2 : 0) + (flag3 ? 1 : 0), null);
        }
    }

    public boolean hasArrowAtEnd() {
        return (getInternalFlags() & 0x4000) != 0;
    }

    public boolean hasArrowAtStart() {
        return (getInternalFlags() & 0x8000) != 0;
    }

    public Point getArrowToEndPoint() {
        return getEndPoint();
    }

    public Point getArrowToAnchorPoint() {
        return getPoint(getNumPoints() - 2);
    }

    public Point getArrowFromEndPoint() {
        return getStartPoint();
    }

    public Point getArrowFromAnchorPoint() {
        return getPoint(1);
    }

    public void setArrowLength(double d) {
        double d1 = getArrowLength();
        if (d1 != d) {
            if (myArrowInfo == null) {
                myArrowInfo = new ArrowInfo();
            }
            myArrowInfo.myLength = d;
            update(105, 0, new Double(d1));
        }
    }

    public double getArrowLength() {
        if (myArrowInfo != null) {
            return myArrowInfo.myLength;
        } else {
            return 10D;
        }
    }

    public void setArrowShaftLength(double d) {
        double d1 = getArrowShaftLength();
        if (d1 != d) {
            if (myArrowInfo == null) {
                myArrowInfo = new ArrowInfo();
            }
            myArrowInfo.myShaftLength = d;
            update(106, 0, new Double(d1));
        }
    }

    public double getArrowShaftLength() {
        if (myArrowInfo != null) {
            return myArrowInfo.myShaftLength;
        } else {
            return 8D;
        }
    }

    /** @deprecated */
    public void setArrowAngle(double d) {
        setArrowWidth(getArrowLength() * Math.tan(d / 2D) * 2D);
    }

    /** @deprecated */
    public double getArrowAngle() {
        double d = getArrowLength();
        if (d < 1.0D) {
            d = 1.0D;
        }
        return Math.atan(getArrowWidth() / (d * 2D)) * 2D;
    }

    public void setArrowWidth(double d) {
        double d1 = getArrowWidth();
        if (d1 != d && d >= 0.0D) {
            if (myArrowInfo == null) {
                myArrowInfo = new ArrowInfo();
            }
            myArrowInfo.myWidth = d;
            update(111, 0, new Double(d1));
        }
    }

    public double getArrowWidth() {
        if (myArrowInfo != null) {
            return myArrowInfo.myWidth;
        } else {
            return 8D;
        }
    }

    protected void drawArrowHeads(Graphics2D graphics2d) {
        boolean flag = hasArrowAtEnd();
        boolean flag1 = hasArrowAtStart();
        if (flag || flag1) {
            if (myArrowInfo == null) {
                myArrowInfo = new ArrowInfo();
            }
            int ai[] = myArrowInfo.getXs();
            int ai1[] = myArrowInfo.getYs();
            if (flag) {
                Point point = getArrowToAnchorPoint();
                Point point2 = getArrowToEndPoint();
                if (point != null && point2 != null) {
                    calculateFilledArrowhead(point.x, point.y, point2.x,
                            point2.y, 1, ai, ai1);
                    drawArrowhead(graphics2d, true, ai, ai1);
                }
            }
            if (flag1) {
                Point point1 = getArrowFromAnchorPoint();
                Point point3 = getArrowFromEndPoint();
                if (point1 != null && point3 != null) {
                    calculateFilledArrowhead(point1.x, point1.y, point3.x,
                            point3.y, 0, ai, ai1);
                    drawArrowhead(graphics2d, false, ai, ai1);
                }
            }
        }
    }

    protected void calculateFilledArrowhead(int i, int k, int l, int i1,
            int j1, int ai[], int ai1[]) {
        int k1 = l - i;
        int l1 = i1 - k;
        double d = Math.sqrt(k1 * k1 + l1 * l1);
        if (d <= 1.0D) {
            d = 1.0D;
        }
        double d1 = (double) k1 / d;
        double d2 = (double) l1 / d;
        double d3 = getArrowLength();
        double d4 = getArrowShaftLength();
        int i2 = (int) Math.round(getArrowWidth() / 2D);
        double d5 = -d4;
        double d6 = 0.0D;
        double d7 = -d3;
        double d8 = i2;
        double d9 = -d3;
        double d10 = -i2;
        ai[0] = l + (int) Math.round(d1 * d5 - d2 * d6);
        ai1[0] = i1 + (int) Math.round(d2 * d5 + d1 * d6);
        ai[1] = l + (int) Math.round(d1 * d7 - d2 * d8);
        ai1[1] = i1 + (int) Math.round(d2 * d7 + d1 * d8);
        ai[2] = l;
        ai1[2] = i1;
        ai[3] = l + (int) Math.round(d1 * d9 - d2 * d10);
        ai1[3] = i1 + (int) Math.round(d2 * d9 + d1 * d10);
    }

    protected void drawArrowhead(Graphics2D graphics2d, boolean flag, int ai[],
            int ai1[]) {
        GraphicViewerPen graphicviewerpen = getPen();
        if (graphicviewerpen.getStyle() != 65535
                || graphicviewerpen.getWidth() > 1) {
            graphicviewerpen = GraphicViewerPen.makeStockPen(graphicviewerpen
                    .getColor());
        }
        drawPolygon(graphics2d, graphicviewerpen, getBrush(), ai, ai1, 4);
    }

    public void setHighlight(GraphicViewerPen graphicviewerpen) {
        GraphicViewerPen graphicviewerpen1 = myHighlightPen;
        if (graphicviewerpen1 != graphicviewerpen) {
            myHighlightPen = graphicviewerpen;
            update(108, 0, graphicviewerpen1);
        }
    }

    public GraphicViewerPen getHighlight() {
        return myHighlightPen;
    }

    public void copyOldValueForUndo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 101 : // 'e'
                Point point = (Point) graphicviewerdocumentchangededit
                        .getOldValue();
                graphicviewerdocumentchangededit.setOldValue(new Point(point.x,
                        point.y));
                return;

            case 102 : // 'f'
                Point point1 = (Point) graphicviewerdocumentchangededit
                        .getOldValue();
                graphicviewerdocumentchangededit.setOldValue(new Point(
                        point1.x, point1.y));
                return;

            case 103 : // 'g'
                Point point2 = (Point) graphicviewerdocumentchangededit
                        .getOldValue();
                graphicviewerdocumentchangededit.setOldValue(new Point(
                        point2.x, point2.y));
                return;

            case 104 : // 'h'
            case 105 : // 'i'
            case 106 : // 'j'
            case 108 : // 'l'
            case 109 : // 'm'
            case 111 : // 'o'
                return;

            case 110 : // 'n'
                if (!graphicviewerdocumentchangededit.isBeforeChanging()) {
                    GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit1 = graphicviewerdocumentchangededit
                            .findBeforeChangingEdit();
                    if (graphicviewerdocumentchangededit1 != null) {
                        graphicviewerdocumentchangededit
                                .setOldValue(graphicviewerdocumentchangededit1
                                        .getNewValue());
                    }
                }
                return;

            case 107 : // 'k'
            default :
                super.copyOldValueForUndo(graphicviewerdocumentchangededit);
                return;
        }
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 101 : // 'e'
                Point point = getPoint(graphicviewerdocumentchangededit
                        .getOldValueInt());
                graphicviewerdocumentchangededit.setNewValue(new Point(point.x,
                        point.y));
                return;

            case 102 : // 'f'
                Point point1 = getPoint(graphicviewerdocumentchangededit
                        .getOldValueInt());
                graphicviewerdocumentchangededit.setNewValue(new Point(
                        point1.x, point1.y));
                return;

            case 103 : // 'g'
                Point point2 = getPoint(graphicviewerdocumentchangededit
                        .getOldValueInt());
                graphicviewerdocumentchangededit.setNewValue(new Point(
                        point2.x, point2.y));
                return;

            case 104 : // 'h'
                graphicviewerdocumentchangededit
                        .setNewValueInt((hasArrowAtEnd() ? 2 : 0)
                                + (hasArrowAtStart() ? 1 : 0));
                return;

            case 105 : // 'i'
                graphicviewerdocumentchangededit.setNewValue(new Double(
                        getArrowLength()));
                return;

            case 106 : // 'j'
                graphicviewerdocumentchangededit.setNewValue(new Double(
                        getArrowShaftLength()));
                return;

            case 111 : // 'o'
                graphicviewerdocumentchangededit.setNewValue(new Double(
                        getArrowWidth()));
                return;

            case 108 : // 'l'
                graphicviewerdocumentchangededit.setNewValue(getHighlight());
                return;

            case 109 : // 'm'
                graphicviewerdocumentchangededit.setNewValueBoolean(isCubic());
                return;

            case 110 : // 'n'
                ArrayList arraylist = copyPointsArray();
                graphicviewerdocumentchangededit.setNewValue(arraylist);
                return;

            case 107 : // 'k'
            default :
                super.copyNewValueForRedo(graphicviewerdocumentchangededit);
                return;
        }
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 101 : // 'e'
                if (flag) {
                    internalRemovePoint(
                            graphicviewerdocumentchangededit.getOldValueInt(),
                            true);
                } else {
                    Point point = (Point) graphicviewerdocumentchangededit
                            .getNewValue();
                    internalInsertPoint(
                            graphicviewerdocumentchangededit.getOldValueInt(),
                            point.x, point.y, true);
                }
                return;

            case 102 : // 'f'
                if (flag) {
                    Point point1 = (Point) graphicviewerdocumentchangededit
                            .getOldValue();
                    internalInsertPoint(
                            graphicviewerdocumentchangededit.getOldValueInt(),
                            point1.x, point1.y, true);
                } else {
                    internalRemovePoint(
                            graphicviewerdocumentchangededit.getOldValueInt(),
                            true);
                }
                return;

            case 103 : // 'g'
                if (flag) {
                    Point point2 = (Point) graphicviewerdocumentchangededit
                            .getOldValue();
                    internalSetPoint(
                            graphicviewerdocumentchangededit.getOldValueInt(),
                            point2.x, point2.y, true);
                } else {
                    Point point3 = (Point) graphicviewerdocumentchangededit
                            .getNewValue();
                    internalSetPoint(
                            graphicviewerdocumentchangededit.getOldValueInt(),
                            point3.x, point3.y, true);
                }
                return;

            case 104 : // 'h'
                int i = graphicviewerdocumentchangededit.getValueInt(flag);
                boolean flag1 = (i & 2) != 0;
                boolean flag2 = (i & 1) != 0;
                setArrowHeads(flag2, flag1);
                return;

            case 105 : // 'i'
                setArrowLength(graphicviewerdocumentchangededit
                        .getValueDouble(flag));
                return;

            case 106 : // 'j'
                setArrowShaftLength(graphicviewerdocumentchangededit
                        .getValueDouble(flag));
                return;

            case 111 : // 'o'
                setArrowWidth(graphicviewerdocumentchangededit
                        .getValueDouble(flag));
                return;

            case 108 : // 'l'
                setHighlight((GraphicViewerPen) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 109 : // 'm'
                setCubic(graphicviewerdocumentchangededit.getValueBoolean(flag));
                return;

            case 110 : // 'n'
                ArrayList arraylist = (ArrayList) graphicviewerdocumentchangededit
                        .getValue(flag);
                internalSetPoints(arraylist, true);
                return;

            case 107 : // 'k'
            default :
                super.changeValue(graphicviewerdocumentchangededit, flag);
                return;
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static class ArrowInfo implements Serializable {

        private static final long serialVersionUID = -9160998475192949070L;

        double myLength = 10.0D;
        double myShaftLength = 8.0D;
        double myWidth = 8.0D;
        transient int[] headx = null;
        transient int[] heady = null;

        int[] getXs() {
            if (headx == null) {
                headx = new int[4];
            }
            return headx;
        }

        int[] getYs() {
            if (heady == null) {
                heady = new int[4];
            }
            return heady;
        }

        public void SVGWriteGraphicViewerElementAttributes(
                IDomElement domelement) {
            domelement.setAttribute("arrow_length", Double.toString(myLength));
            domelement.setAttribute("arrow_shaftlength",
                    Double.toString(myShaftLength));
            domelement.setAttribute("arrow_width", Double.toString(myWidth));
        }

        public void SVGReadGraphicViewerElementAttributes(IDomElement domelement) {
            String s = domelement.getAttribute("arrow_length");
            myLength = Double.parseDouble(s);
            String s1 = domelement.getAttribute("arrow_shaftlength");
            myShaftLength = Double.parseDouble(s1);
            String s2 = domelement.getAttribute("arrow_angle");
            if (s2.length() > 0) {
                double d = Double.parseDouble(s2);
                myWidth = myLength * Math.tan(d / 2D) * 2D;
            } else {
                String s3 = domelement.getAttribute("arrow_width");
                myWidth = Double.parseDouble(s3);
            }
        }
    }
}