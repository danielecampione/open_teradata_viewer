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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.io.Serializable;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerPen
        implements
            Serializable,
            GraphicViewerXMLSaveRestore {
    private static final long serialVersionUID = 938141752348112607L;

    public GraphicViewerPen() {
        _fldtry = null;
        _fldnew = 65535;
        _fldfor = 1;
        _fldint = GraphicViewerBrush.ColorBlack;
        _fldtry = null;
    }

    public GraphicViewerPen(int i, int j, Color color) {
        _fldtry = null;
        _fldnew = i;
        _fldfor = i != 0 ? Math.max(j, 1) : 0;
        _fldint = color;
        _fldtry = null;
    }

    public GraphicViewerPen(Stroke stroke, Color color) {
        _fldtry = null;
        _fldnew = 65534;
        if (stroke != null && (stroke instanceof BasicStroke))
            _fldfor = (int) ((BasicStroke) stroke).getLineWidth();
        else
            _fldfor = 1;
        _fldint = color;
        _fldtry = stroke;
    }

    public static GraphicViewerPen makeStockPen(Color color) {
        if (color.equals(GraphicViewerBrush.ColorBlack))
            return black;
        if (color.equals(GraphicViewerBrush.ColorDarkGray))
            return darkGray;
        if (color.equals(GraphicViewerBrush.ColorGray))
            return gray;
        if (color.equals(GraphicViewerBrush.ColorLightGray))
            return lightGray;
        if (color.equals(GraphicViewerBrush.ColorWhite))
            return white;
        if (color.equals(GraphicViewerBrush.ColorRed))
            return red;
        if (color.equals(GraphicViewerBrush.ColorMagenta))
            return magenta;
        if (color.equals(GraphicViewerBrush.ColorYellow))
            return yellow;
        if (color.equals(GraphicViewerBrush.ColorGreen))
            return green;
        if (color.equals(GraphicViewerBrush.ColorCyan))
            return cyan;
        if (color.equals(GraphicViewerBrush.ColorBlue))
            return blue;
        if (color.equals(GraphicViewerBrush.ColorOrange))
            return orange;
        if (color.equals(GraphicViewerBrush.ColorPink))
            return pink;
        else
            return new GraphicViewerPen(65535, 1, color);
    }

    public static GraphicViewerPen make(int i, int j, Color color) {
        if (i == 0)
            return Null;
        if (i == 65535 && j == 1)
            return makeStockPen(color);
        else
            return new GraphicViewerPen(i, j, color);
    }

    public int getStyle() {
        return _fldnew;
    }

    public int getWidth() {
        return _fldfor;
    }

    public Color getColor() {
        return _fldint;
    }

    public Stroke getStroke() {
        if (_fldtry == null)
            switch (getStyle()) {
                case 0 : // '\0'
                default :
                    _fldtry = null;
                    break;

                case 65535 :
                    int i = getWidth();
                    _fldtry = new BasicStroke(i, 0, 0, 10F);
                    break;

                case 1 : // '\001'
                    float f = getWidth();
                    float af[] = {3F * f, f};
                    _fldtry = new BasicStroke(f, 0, 0, 10F, af, 0.0F);
                    break;

                case 2 : // '\002'
                    float f1 = getWidth();
                    float af1[] = {f1, f1};
                    _fldtry = new BasicStroke(f1, 0, 0, 10F, af1, 0.0F);
                    break;

                case 3 : // '\003'
                    float f2 = getWidth();
                    float af2[] = {3F * f2, f2, f2, f2};
                    _fldtry = new BasicStroke(f2, 0, 0, 10F, af2, 0.0F);
                    break;

                case 4 : // '\004'
                    float f3 = getWidth();
                    float af3[] = {3F * f3, f3, f3, f3, f3, f3};
                    _fldtry = new BasicStroke(f3, 0, 0, 10F, af3, 0.0F);
                    break;
            }
        return _fldtry;
    }

    public void SVGWriteObject(DomDoc domdoc, DomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            DomElement domelement1 = domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerPen",
                    domelement);
            _mthdo(domelement1);
            domdoc.registerObject(this, domelement1);
        }
    }

    public DomNode SVGReadObject(DomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, DomElement domelement,
            DomElement domelement1) {
        if (domelement1 != null) {
            _mthfor(domelement1);
            String s = domelement1.getAttribute("id");
            if (s.length() > 0)
                domdoc.registerTag(s, this);
        }
        return domelement.getNextSibling();
    }

    public void SVGWriteAttributes(DomElement domelement) {
        String s = "";
        if (getStyle() == 0) {
            s = "stroke:none;";
        } else {
            int i = getWidth();
            s = "stroke-width:" + Integer.toString(i) + ";";
            if (getStyle() == 1)
                s = s + "stroke-dasharray:" + Integer.toString(i * 3) + ","
                        + Integer.toString(i) + ";";
            else if (getStyle() == 2)
                s = s + "stroke-dasharray:" + Integer.toString(i) + ","
                        + Integer.toString(i) + ";";
            else if (getStyle() == 3)
                s = s + "stroke-dasharray:" + Integer.toString(i * 3) + ","
                        + Integer.toString(i) + "," + Integer.toString(i) + ","
                        + Integer.toString(i) + ";";
            else if (getStyle() == 4)
                s = s + "stroke-dasharray:" + Integer.toString(i * 3) + ","
                        + Integer.toString(i) + "," + Integer.toString(i) + ","
                        + Integer.toString(i) + "," + Integer.toString(i) + ","
                        + Integer.toString(i) + ";";
            Color color = getColor();
            if (color == Color.black) {
                s = s + "stroke:black;";
            } else {
                int j = color.getRed();
                int k = color.getGreen();
                int l = color.getBlue();
                s = s + "stroke:rgb(" + Integer.toString(j) + ","
                        + Integer.toString(k) + "," + Integer.toString(l)
                        + ");";
            }
        }
        String s1 = domelement.getAttribute("style");
        s1 = s1 + s;
        domelement.setAttribute("style", s1);
    }

    public void SVGReadAttributes(DomElement domelement) {
        String s = domelement.getAttribute("style");
        String s1 = domelement.getSubAttribute(s, "stroke-width");
        String s2 = domelement.getSubAttribute(s, "stroke");
        String s3 = domelement.getSubAttribute(s, "stroke-dasharray");
        for (DomNode domnode = domelement.getParentNode(); domnode != null; domnode = domnode
                .getParentNode()) {
            if (!domnode.isElement())
                continue;
            DomElement domelement1 = domnode.elementCast();
            String s4 = domelement1.getAttribute("style");
            if (s1.length() == 0)
                s1 = domelement1.getSubAttribute(s4, "stroke-width");
            if (s2.length() == 0)
                s2 = domelement1.getSubAttribute(s4, "stroke");
            if (s3.length() == 0)
                s3 = domelement1.getSubAttribute(s4, "stroke-dasharray");
        }

        int i = 1;
        if (s1.length() > 0)
            i = Integer.parseInt(s1);
        int j = 65535;
        Color color = GraphicViewerBrush.ColorBlack;
        if (s2.length() > 0)
            if (s2.equals("none"))
                j = 0;
            else if (s2.equalsIgnoreCase("black"))
                color = GraphicViewerBrush.ColorBlack;
            else if (s2.equalsIgnoreCase("darkGray"))
                color = GraphicViewerBrush.ColorDarkGray;
            else if (s2.equalsIgnoreCase("gray"))
                color = GraphicViewerBrush.ColorGray;
            else if (s2.equalsIgnoreCase("lightGray"))
                color = GraphicViewerBrush.ColorLightGray;
            else if (s2.equalsIgnoreCase("white"))
                color = GraphicViewerBrush.ColorWhite;
            else if (s2.equalsIgnoreCase("red"))
                color = GraphicViewerBrush.ColorRed;
            else if (s2.equalsIgnoreCase("magenta"))
                color = GraphicViewerBrush.ColorMagenta;
            else if (s2.equalsIgnoreCase("yellow"))
                color = GraphicViewerBrush.ColorYellow;
            else if (s2.equalsIgnoreCase("green"))
                color = GraphicViewerBrush.ColorGreen;
            else if (s2.equalsIgnoreCase("cyan"))
                color = GraphicViewerBrush.ColorCyan;
            else if (s2.equalsIgnoreCase("blue"))
                color = GraphicViewerBrush.ColorBlue;
            else if (s2.equalsIgnoreCase("orange"))
                color = GraphicViewerBrush.ColorOrange;
            else if (s2.equalsIgnoreCase("pink"))
                color = GraphicViewerBrush.ColorPink;
            else if (s2.startsWith("rgb")) {
                int k = s2.indexOf("(") + 1;
                int l = s2.indexOf(",", k);
                String s5 = s2.substring(k, l);
                k = l + 1;
                l = s2.indexOf(",", k);
                String s6 = s2.substring(k, l);
                k = l + 1;
                l = s2.indexOf(")", k);
                String s7 = s2.substring(k, l);
                color = new Color(Integer.parseInt(s5), Integer.parseInt(s6),
                        Integer.parseInt(s7));
            }
        _fldnew = j;
        _fldfor = i;
        _fldint = color;
    }

    private void _mthdo(DomElement domelement) {
        domelement.setAttribute("penstyle", Integer.toString(_fldnew));
        domelement.setAttribute("penwidth", Integer.toString(_fldfor));
        String s = "black";
        if (_fldint == GraphicViewerBrush.ColorBlack)
            s = "black";
        else if (_fldint == GraphicViewerBrush.ColorLightGray)
            s = "lightGray";
        else if (_fldint == GraphicViewerBrush.ColorDarkGray)
            s = "darkGray";
        else if (_fldint == GraphicViewerBrush.ColorGray)
            s = "gray";
        else if (_fldint == GraphicViewerBrush.ColorWhite)
            s = "white";
        else if (_fldint == GraphicViewerBrush.ColorRed)
            s = "red";
        else if (_fldint == GraphicViewerBrush.ColorMagenta)
            s = "magenta";
        else if (_fldint == GraphicViewerBrush.ColorYellow)
            s = "yellow";
        else if (_fldint == GraphicViewerBrush.ColorGreen)
            s = "green";
        else if (_fldint == GraphicViewerBrush.ColorCyan)
            s = "cyan";
        else if (_fldint == GraphicViewerBrush.ColorBlue)
            s = "blue";
        else if (_fldint == GraphicViewerBrush.ColorOrange)
            s = "orange";
        else if (_fldint == GraphicViewerBrush.ColorPink) {
            s = "pink";
        } else {
            int i = _fldint.getRed();
            int j = _fldint.getGreen();
            int k = _fldint.getBlue();
            int l = _fldint.getAlpha();
            s = "rgbalpha(" + Integer.toString(i) + "," + Integer.toString(j)
                    + "," + Integer.toString(k) + "," + Integer.toString(l)
                    + ")";
        }
        domelement.setAttribute("pencolor", s);
    }

    private void _mthfor(DomElement domelement) {
        String s = domelement.getAttribute("penstyle");
        _fldnew = Integer.parseInt(s);
        String s1 = domelement.getAttribute("penwidth");
        _fldfor = Integer.parseInt(s1);
        String s2 = domelement.getAttribute("pencolor");
        if (s2.equals("black"))
            _fldint = GraphicViewerBrush.ColorBlack;
        else if (s2.equals("darkGray"))
            _fldint = GraphicViewerBrush.ColorDarkGray;
        else if (s2.equals("gray"))
            _fldint = GraphicViewerBrush.ColorGray;
        else if (s2.equals("lightGray"))
            _fldint = GraphicViewerBrush.ColorLightGray;
        else if (s2.equals("white"))
            _fldint = GraphicViewerBrush.ColorWhite;
        else if (s2.equals("red"))
            _fldint = GraphicViewerBrush.ColorRed;
        else if (s2.equals("magenta"))
            _fldint = GraphicViewerBrush.ColorMagenta;
        else if (s2.equals("yellow"))
            _fldint = GraphicViewerBrush.ColorYellow;
        else if (s2.equals("green"))
            _fldint = GraphicViewerBrush.ColorGreen;
        else if (s2.equals("cyan"))
            _fldint = GraphicViewerBrush.ColorCyan;
        else if (s2.equals("blue"))
            _fldint = GraphicViewerBrush.ColorBlue;
        else if (s2.equals("orange"))
            _fldint = GraphicViewerBrush.ColorOrange;
        else if (s2.equals("pink"))
            _fldint = GraphicViewerBrush.ColorPink;
        else if (s2.startsWith("rgbalpha")) {
            int i = s2.indexOf("(") + 1;
            int j = s2.indexOf(",", i);
            String s3 = s2.substring(i, j);
            i = j + 1;
            j = s2.indexOf(",", i);
            String s4 = s2.substring(i, j);
            i = j + 1;
            j = s2.indexOf(",", i);
            String s5 = s2.substring(i, j);
            i = j + 1;
            j = s2.indexOf(")", i);
            String s6 = s2.substring(i, j);
            _fldint = new Color(Integer.parseInt(s3), Integer.parseInt(s4),
                    Integer.parseInt(s5), Integer.parseInt(s6));
        }
    }

    public static final int NONE = 0;
    public static final int SOLID = 65535;
    public static final int DASHED = 1;
    public static final int DOTTED = 2;
    public static final int DASHDOT = 3;
    public static final int DASHDOTDOT = 4;
    public static final int CUSTOM = 65534;
    public static final GraphicViewerPen black;
    public static final GraphicViewerPen darkGray;
    public static final GraphicViewerPen gray;
    public static final GraphicViewerPen lightGray;
    public static final GraphicViewerPen white;
    public static final GraphicViewerPen red;
    public static final GraphicViewerPen magenta;
    public static final GraphicViewerPen yellow;
    public static final GraphicViewerPen green;
    public static final GraphicViewerPen cyan;
    public static final GraphicViewerPen blue;
    public static final GraphicViewerPen orange;
    public static final GraphicViewerPen pink;
    public static final GraphicViewerPen Null;
    private int _fldnew;
    private int _fldfor;
    private Color _fldint;
    private transient Stroke _fldtry;

    static {
        black = new GraphicViewerPen(65535, 1, GraphicViewerBrush.ColorBlack);
        darkGray = new GraphicViewerPen(65535, 1,
                GraphicViewerBrush.ColorDarkGray);
        gray = new GraphicViewerPen(65535, 1, GraphicViewerBrush.ColorGray);
        lightGray = new GraphicViewerPen(65535, 1,
                GraphicViewerBrush.ColorLightGray);
        white = new GraphicViewerPen(65535, 1, GraphicViewerBrush.ColorWhite);
        red = new GraphicViewerPen(65535, 1, GraphicViewerBrush.ColorRed);
        magenta = new GraphicViewerPen(65535, 1,
                GraphicViewerBrush.ColorMagenta);
        yellow = new GraphicViewerPen(65535, 1, GraphicViewerBrush.ColorYellow);
        green = new GraphicViewerPen(65535, 1, GraphicViewerBrush.ColorGreen);
        cyan = new GraphicViewerPen(65535, 1, GraphicViewerBrush.ColorCyan);
        blue = new GraphicViewerPen(65535, 1, GraphicViewerBrush.ColorBlue);
        orange = new GraphicViewerPen(65535, 1, GraphicViewerBrush.ColorOrange);
        pink = new GraphicViewerPen(65535, 1, GraphicViewerBrush.ColorPink);
        Null = new GraphicViewerPen(0, 1, GraphicViewerBrush.ColorBlack);
    }
}