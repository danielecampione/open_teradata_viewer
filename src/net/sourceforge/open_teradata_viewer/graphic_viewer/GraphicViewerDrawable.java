/*
 * Open Teradata Viewer ( graphic viewer )
 * Copyright (C) 2015, D. Campione
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
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public abstract class GraphicViewerDrawable extends GraphicViewerObject {

    private static final long serialVersionUID = 5440647169946678126L;

    private GraphicViewerPen myCurrentPen = GraphicViewerPen.black;
    private GraphicViewerBrush myCurrentBrush = null;

    public GraphicViewerDrawable() {
    }

    public GraphicViewerDrawable(Rectangle rectangle) {
        super(rectangle);
    }

    public GraphicViewerDrawable(Point point, Dimension dimension) {
        super(point, dimension);
    }

    public GraphicViewerObject copyObject(
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerDrawable graphicviewerdrawable = (GraphicViewerDrawable) super
                .copyObject(graphicviewercopyenvironment);
        if (graphicviewerdrawable != null) {
            graphicviewerdrawable.myCurrentPen = myCurrentPen;
            graphicviewerdrawable.myCurrentBrush = myCurrentBrush;
        }
        return graphicviewerdrawable;
    }

    public void setPen(GraphicViewerPen graphicviewerpen) {
        GraphicViewerPen graphicviewerpen1 = myCurrentPen;
        if (graphicviewerpen1 != graphicviewerpen) {
            update();
            myCurrentPen = graphicviewerpen;
            update(11, 0, graphicviewerpen1);
        }
    }

    public GraphicViewerPen getPen() {
        return myCurrentPen;
    }

    public void setBrush(GraphicViewerBrush graphicviewerbrush) {
        GraphicViewerBrush graphicviewerbrush1 = myCurrentBrush;
        if (graphicviewerbrush1 != graphicviewerbrush) {
            myCurrentBrush = graphicviewerbrush;
            update(12, 0, graphicviewerbrush1);
        }
    }

    public GraphicViewerBrush getBrush() {
        return myCurrentBrush;
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 11 : // '\013'
                graphicviewerdocumentchangededit.setNewValue(getPen());
                return;

            case 12 : // '\f'
                graphicviewerdocumentchangededit.setNewValue(getBrush());
                return;
        }
        super.copyNewValueForRedo(graphicviewerdocumentchangededit);
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 11 : // '\013'
                setPen((GraphicViewerPen) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 12 : // '\f'
                setBrush((GraphicViewerBrush) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;
        }
        super.changeValue(graphicviewerdocumentchangededit, flag);
    }

    public void expandRectByPenWidth(Rectangle rectangle) {
        if (getPen() != null) {
            int i = getPen().getWidth();
            rectangle.x -= i;
            rectangle.y -= i;
            rectangle.width += i * 2;
            rectangle.height += i * 2;
        }
    }

    public boolean getNearestIntersectionPoint(int i, int j, int k, int l,
            Point point) {
        Rectangle rectangle = getBoundingRect();
        int i1 = 1;
        if (getPen() != null) {
            i1 = getPen().getWidth();
        }
        return GraphicViewerRectangle.getNearestIntersectionPoint(rectangle.x
                - i1, rectangle.y - i1, rectangle.width + 2 * i1,
                rectangle.height + 2 * i1, i, j, k, l, point);
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerDrawable",
                            domelement);
            if (getBrush() != null) {
                if (!domdoc.isRegisteredReference(getBrush())) {
                    domelement1.setAttribute("embeddedpenbrush", "true");
                    IDomElement domelement2 = domdoc.createElement("g");
                    domelement1.appendChild(domelement2);
                    getBrush().SVGWriteObject(domdoc, domelement2);
                }
                domdoc.registerReferencingNode(domelement1, "drawablebrush",
                        getBrush());
            }
            if (getPen() != null) {
                if (!domdoc.isRegisteredReference(getPen())) {
                    domelement1.setAttribute("embeddedpenbrush", "true");
                    IDomElement domelement3 = domdoc.createElement("g");
                    domelement1.appendChild(domelement3);
                    getPen().SVGWriteObject(domdoc, domelement3);
                }
                domdoc.registerReferencingNode(domelement1, "drawablepen",
                        getPen());
            }
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument,
            IDomElement domelement, IDomElement domelement1) {
        if (domelement1 != null) {
            if (domelement1.getAttribute("embeddedpenbrush").equals("true")) {
                domdoc.SVGTraverseChildren(graphicviewerdocument, domelement1,
                        null, false);
            }
            String s = domelement1.getAttribute("drawablepen");
            domdoc.registerReferencingObject(this, "drawablepen", s);
            String s1 = domelement1.getAttribute("drawablebrush");
            if (s1.length() > 0) {
                domdoc.registerReferencingObject(this, "drawablebrush", s1);
            } else {
                setBrush(null);
            }
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        }
        return domelement.getNextSibling();
    }

    public void SVGWriteAttributes(IDomElement domelement) {
        GraphicViewerPen graphicviewerpen = getPen();
        if (graphicviewerpen != null) {
            graphicviewerpen.SVGWriteAttributes(domelement);
        }
        GraphicViewerBrush graphicviewerbrush = getBrush();
        if (graphicviewerbrush != null) {
            graphicviewerbrush.SVGWriteAttributes(domelement);
        } else {
            String s = domelement.getAttribute("style");
            s = s + "fill:none;";
            domelement.setAttribute("style", s);
        }
    }

    public void SVGReadAttributes(IDomElement domelement) {
        setPen(new GraphicViewerPen());
        getPen().SVGReadAttributes(domelement);
        setBrush(new GraphicViewerBrush());
        getBrush().SVGReadAttributes(domelement);
    }

    public void SVGUpdateReference(String s, Object obj) {
        super.SVGUpdateReference(s, obj);
        if (s.equals("drawablepen")) {
            setPen((GraphicViewerPen) obj);
        } else if (s.equals("drawablebrush")) {
            setBrush((GraphicViewerBrush) obj);
        }
    }

    public static void drawLine(Graphics2D graphics2d,
            GraphicViewerPen graphicviewerpen, int i, int j, int k, int l) {
        if (graphicviewerpen != null && graphicviewerpen.getStyle() != 0) {
            java.awt.Stroke stroke = graphicviewerpen.getStroke();
            if (stroke == null) {
                return;
            }
            graphics2d.setStroke(stroke);
            graphics2d.setColor(graphicviewerpen.getColor());
            graphics2d.drawLine(i, j, k, l);
        }
    }

    public void drawLine(Graphics2D graphics2d, int i, int j, int k, int l) {
        drawLine(graphics2d, getPen(), i, j, k, l);
    }

    public static void drawEllipse(Graphics2D graphics2d,
            GraphicViewerPen graphicviewerpen,
            GraphicViewerBrush graphicviewerbrush, int i, int j, int k, int l) {
        if (graphicviewerbrush != null) {
            java.awt.Paint paint = graphicviewerbrush.getPaint();
            if (paint != null) {
                graphics2d.setPaint(paint);
                graphics2d.fillOval(i, j, k, l);
            }
        }
        if (graphicviewerpen != null) {
            java.awt.Stroke stroke = graphicviewerpen.getStroke();
            if (stroke != null) {
                graphics2d.setStroke(stroke);
                graphics2d.setColor(graphicviewerpen.getColor());
                graphics2d.drawOval(i, j, k, l);
            }
        }
    }

    public void drawEllipse(Graphics2D graphics2d, int i, int j, int k, int l) {
        drawEllipse(graphics2d, getPen(), getBrush(), i, j, k, l);
    }

    public void drawEllipse(Graphics2D graphics2d, Rectangle rectangle) {
        drawEllipse(graphics2d, getPen(), getBrush(), rectangle.x, rectangle.y,
                rectangle.width, rectangle.height);
    }

    public static void drawRect(Graphics2D graphics2d,
            GraphicViewerPen graphicviewerpen,
            GraphicViewerBrush graphicviewerbrush, int i, int j, int k, int l) {
        if (graphicviewerbrush != null) {
            java.awt.Paint paint = graphicviewerbrush.getPaint();
            if (paint != null) {
                graphics2d.setPaint(paint);
                graphics2d.fillRect(i, j, k, l);
            }
        }
        if (graphicviewerpen != null) {
            java.awt.Stroke stroke = graphicviewerpen.getStroke();
            if (stroke != null) {
                graphics2d.setStroke(stroke);
                graphics2d.setColor(graphicviewerpen.getColor());
                graphics2d.drawRect(i, j, k, l);
            }
        }
    }

    public void drawRect(Graphics2D graphics2d, int i, int j, int k, int l) {
        drawRect(graphics2d, getPen(), getBrush(), i, j, k, l);
    }

    public void drawRect(Graphics2D graphics2d, Rectangle rectangle) {
        drawRect(graphics2d, getPen(), getBrush(), rectangle.x, rectangle.y,
                rectangle.width, rectangle.height);
    }

    public static void drawRoundRect(Graphics2D graphics2d,
            GraphicViewerPen graphicviewerpen,
            GraphicViewerBrush graphicviewerbrush, int i, int j, int k, int l,
            int i1, int j1) {
        if (graphicviewerbrush != null) {
            java.awt.Paint paint = graphicviewerbrush.getPaint();
            if (paint != null) {
                graphics2d.setPaint(paint);
                graphics2d.fillRoundRect(i, j, k, l, i1, j1);
            }
        }
        if (graphicviewerpen != null && graphicviewerpen.getStyle() != 0) {
            java.awt.Stroke stroke = graphicviewerpen.getStroke();
            if (stroke != null) {
                graphics2d.setStroke(stroke);
                graphics2d.setColor(graphicviewerpen.getColor());
                graphics2d.drawRoundRect(i, j, k, l, i1, j1);
            }
        }
    }

    public void drawRoundRect(Graphics2D graphics2d, int i, int j, int k,
            int l, int i1, int j1) {
        drawRoundRect(graphics2d, getPen(), getBrush(), i, j, k, l, i1, j1);
    }

    public static void draw3DRect(Graphics2D graphics2d,
            GraphicViewerPen graphicviewerpen,
            GraphicViewerBrush graphicviewerbrush, int i, int j, int k, int l,
            boolean flag) {
        if (graphicviewerbrush != null) {
            java.awt.Paint paint = graphicviewerbrush.getPaint();
            if (paint != null) {
                graphics2d.setPaint(paint);
                graphics2d.fill3DRect(i, j, k, l, flag);
            }
        }
        if (graphicviewerpen != null && graphicviewerpen.getStyle() == 65535) {
            java.awt.Stroke stroke = graphicviewerpen.getStroke();
            if (stroke != null) {
                graphics2d.setStroke(stroke);
                graphics2d.setColor(graphicviewerpen.getColor());
                graphics2d.draw3DRect(i, j, k, l, flag);
            }
        }
    }

    public void draw3DRect(Graphics2D graphics2d, int i, int j, int k, int l,
            boolean flag) {
        draw3DRect(graphics2d, getPen(), getBrush(), i, j, k, l, flag);
    }

    public static void drawPolygon(Graphics2D graphics2d,
            GraphicViewerPen graphicviewerpen,
            GraphicViewerBrush graphicviewerbrush, int ai[], int ai1[], int i) {
        if (graphicviewerbrush != null) {
            java.awt.Paint paint = graphicviewerbrush.getPaint();
            if (paint != null) {
                graphics2d.setPaint(paint);
                graphics2d.fillPolygon(ai, ai1, i);
            }
        }
        if (graphicviewerpen != null) {
            java.awt.Stroke stroke = graphicviewerpen.getStroke();
            if (stroke != null) {
                graphics2d.setStroke(stroke);
                graphics2d.setColor(graphicviewerpen.getColor());
                graphics2d.drawPolygon(ai, ai1, i);
            }
        }
    }

    public void drawPolygon(Graphics2D graphics2d, int ai[], int ai1[], int i) {
        drawPolygon(graphics2d, getPen(), getBrush(), ai, ai1, i);
    }

    public static void drawPath(Graphics2D graphics2d,
            GraphicViewerPen graphicviewerpen,
            GraphicViewerBrush graphicviewerbrush, GeneralPath generalpath) {
        if (graphicviewerbrush != null) {
            java.awt.Paint paint = graphicviewerbrush.getPaint();
            if (paint != null) {
                graphics2d.setPaint(paint);
                graphics2d.fill(generalpath);
            }
        }
        if (graphicviewerpen != null) {
            java.awt.Stroke stroke = graphicviewerpen.getStroke();
            if (stroke != null) {
                graphics2d.setStroke(stroke);
                graphics2d.setColor(graphicviewerpen.getColor());
                graphics2d.draw(generalpath);
            }
        }
    }

    public static void SVGAddPathArgs(IDomElement domelement,
            GeneralPath generalpath) {
        String s = "";
        for (PathIterator pathiterator = generalpath.getPathIterator(null); !pathiterator
                .isDone(); pathiterator.next()) {
            double ad[] = new double[6];
            switch (pathiterator.currentSegment(ad)) {
                case 0 : // '\0'
                    Point point = new Point((int) ad[0], (int) ad[1]);
                    s = s + " M";
                    s = s + " " + Integer.toString(point.x);
                    s = s + " " + Integer.toString(point.y);
                    break;

                case 1 : // '\001'
                    Point point1 = new Point((int) ad[0], (int) ad[1]);
                    s = s + " L ";
                    s = s + " " + Integer.toString(point1.x);
                    s = s + " " + Integer.toString(point1.y);
                    break;

                case 2 : // '\002'
                    Point point2 = new Point((int) ad[0], (int) ad[1]);
                    Point point4 = new Point((int) ad[2], (int) ad[3]);
                    s = s + " Q ";
                    s = s + " " + Integer.toString(point2.x);
                    s = s + " " + Integer.toString(point2.y);
                    s = s + " " + Integer.toString(point4.x);
                    s = s + " " + Integer.toString(point4.y);
                    break;

                case 3 : // '\003'
                    Point point3 = new Point((int) ad[0], (int) ad[1]);
                    Point point5 = new Point((int) ad[2], (int) ad[3]);
                    Point point6 = new Point((int) ad[4], (int) ad[5]);
                    s = s + " C ";
                    s = s + " " + Integer.toString(point3.x);
                    s = s + " " + Integer.toString(point3.y);
                    s = s + " " + Integer.toString(point5.x);
                    s = s + " " + Integer.toString(point5.y);
                    s = s + " " + Integer.toString(point6.x);
                    s = s + " " + Integer.toString(point6.y);
                    break;
            }
            domelement.setAttribute("d", s);
        }

    }
}