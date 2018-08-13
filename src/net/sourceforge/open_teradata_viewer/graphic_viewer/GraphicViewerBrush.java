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
import java.awt.Paint;
import java.io.Serializable;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerBrush
        implements
            Serializable,
            IGraphicViewerXMLSaveRestore {

    private static final long serialVersionUID = 3806827378087242926L;

    public static final Color ColorBlack = Color.black;
    public static final Color ColorDarkGray = Color.darkGray;
    public static final Color ColorGray = Color.gray;
    public static final Color ColorLightGray = Color.lightGray;
    public static final Color ColorWhite = Color.white;
    public static final Color ColorRed = Color.red;
    public static final Color ColorMagenta = Color.magenta;
    public static final Color ColorYellow = Color.yellow;
    public static final Color ColorGreen = Color.green;
    public static final Color ColorCyan = Color.cyan;
    public static final Color ColorBlue = Color.blue;
    public static final Color ColorOrange = Color.orange;
    public static final Color ColorPink = Color.pink;
    public static final int NONE = 0;
    public static final int SOLID = 65535;
    public static final int CUSTOM = 65534;
    public static final GraphicViewerBrush black = new GraphicViewerBrush(
            65535, ColorBlack);
    public static final GraphicViewerBrush darkGray = new GraphicViewerBrush(
            65535, ColorDarkGray);
    public static final GraphicViewerBrush gray = new GraphicViewerBrush(65535,
            ColorGray);
    public static final GraphicViewerBrush lightGray = new GraphicViewerBrush(
            65535, ColorLightGray);
    public static final GraphicViewerBrush white = new GraphicViewerBrush(
            65535, ColorWhite);
    public static final GraphicViewerBrush red = new GraphicViewerBrush(65535,
            ColorRed);
    public static final GraphicViewerBrush magenta = new GraphicViewerBrush(
            65535, ColorMagenta);
    public static final GraphicViewerBrush yellow = new GraphicViewerBrush(
            65535, ColorYellow);
    public static final GraphicViewerBrush green = new GraphicViewerBrush(
            65535, ColorGreen);
    public static final GraphicViewerBrush cyan = new GraphicViewerBrush(65535,
            ColorCyan);
    public static final GraphicViewerBrush blue = new GraphicViewerBrush(65535,
            ColorBlue);
    public static final GraphicViewerBrush orange = new GraphicViewerBrush(
            65535, ColorOrange);
    public static final GraphicViewerBrush pink = new GraphicViewerBrush(65535,
            ColorPink);
    public static final GraphicViewerBrush Null = new GraphicViewerBrush(0,
            null);
    private int myStyle;
    private Color myColor;
    private transient Paint myPaint = null;

    public GraphicViewerBrush() {
        myStyle = 0;
        myColor = null;
        myPaint = null;
    }

    public GraphicViewerBrush(int i, Color color) {
        myStyle = i;
        myColor = color;
        myPaint = color;
    }

    public GraphicViewerBrush(Paint paint) {
        myPaint = paint;
        if (paint instanceof Color) {
            myStyle = 65535;
            myColor = (Color) paint;
        } else {
            myStyle = 65534;
            myColor = null;
        }
    }

    public static GraphicViewerBrush makeStockBrush(Color color) {
        if (color.equals(ColorBlack)) {
            return black;
        }
        if (color.equals(ColorDarkGray)) {
            return darkGray;
        }
        if (color.equals(ColorGray)) {
            return gray;
        }
        if (color.equals(ColorLightGray)) {
            return lightGray;
        }
        if (color.equals(ColorWhite)) {
            return white;
        }
        if (color.equals(ColorRed)) {
            return red;
        }
        if (color.equals(ColorMagenta)) {
            return magenta;
        }
        if (color.equals(ColorYellow)) {
            return yellow;
        }
        if (color.equals(ColorGreen)) {
            return green;
        }
        if (color.equals(ColorCyan)) {
            return cyan;
        }
        if (color.equals(ColorBlue)) {
            return blue;
        }
        if (color.equals(ColorOrange)) {
            return orange;
        }
        if (color.equals(ColorPink)) {
            return pink;
        } else {
            return new GraphicViewerBrush(65535, color);
        }
    }

    public static GraphicViewerBrush make(int i, Color color) {
        if (i == 65535) {
            return makeStockBrush(color);
        } else {
            return Null;
        }
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerBrush",
                            domelement);
            SVGWriteGraphicViewerElementAttributes(domelement1);
            domdoc.registerObject(this, domelement1);
        }
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument,
            IDomElement domelement, IDomElement domelement1) {
        if (domelement1 != null) {
            SVGReadGraphicViewerElementAttributes(domelement1);
            String s = domelement1.getAttribute("id");
            if (s.length() > 0) {
                domdoc.registerTag(s, this);
            }
        }
        return domelement.getNextSibling();
    }

    public void SVGWriteAttributes(IDomElement domelement) {
        String s = "fill:none;";
        if (getStyle() == 0) {
            s = "fill:none;";
        } else if (getStyle() == 65535) {
            Color color = getColor();
            int i = color.getRed();
            int j = color.getGreen();
            int k = color.getBlue();
            s = "fill:rgb(" + Integer.toString(i) + "," + Integer.toString(j)
                    + "," + Integer.toString(k) + ");";
        }
        String s1 = domelement.getAttribute("style");
        s1 = s1 + s;
        domelement.setAttribute("style", s1);
    }

    public void SVGReadAttributes(IDomElement domelement) {
        String s = domelement.getAttribute("style");
        String s1 = domelement.getSubAttribute(s, "fill");
        for (IDomNode domnode = domelement.getParentNode(); domnode != null; domnode = domnode
                .getParentNode()) {
            if (!domnode.isElement()) {
                continue;
            }
            IDomElement domelement1 = domnode.elementCast();
            String s2 = domelement1.getAttribute("style");
            if (s1.length() == 0) {
                s1 = domelement1.getSubAttribute(s2, "fill");
            }
        }

        Color color = null;
        if (s1.length() > 0) {
            if (s1.equalsIgnoreCase("black")) {
                color = ColorBlack;
            } else if (s1.equalsIgnoreCase("darkGray")) {
                color = ColorDarkGray;
            } else if (s1.equalsIgnoreCase("gray")) {
                color = ColorGray;
            } else if (s1.equalsIgnoreCase("lightGray")) {
                color = ColorLightGray;
            } else if (s1.equalsIgnoreCase("white")) {
                color = ColorWhite;
            } else if (s1.equalsIgnoreCase("red")) {
                color = ColorRed;
            } else if (s1.equalsIgnoreCase("magenta")) {
                color = ColorMagenta;
            } else if (s1.equalsIgnoreCase("yellow")) {
                color = ColorYellow;
            } else if (s1.equalsIgnoreCase("green")) {
                color = ColorGreen;
            } else if (s1.equalsIgnoreCase("cyan")) {
                color = ColorCyan;
            } else if (s1.equalsIgnoreCase("blue")) {
                color = ColorBlue;
            } else if (s1.equalsIgnoreCase("orange")) {
                color = ColorOrange;
            } else if (s1.equalsIgnoreCase("pink")) {
                color = ColorPink;
            } else if (s1.startsWith("rgb")) {
                int i = s1.indexOf("(") + 1;
                int j = s1.indexOf(",", i);
                String s3 = s1.substring(i, j);
                i = j + 1;
                j = s1.indexOf(",", i);
                String s4 = s1.substring(i, j);
                i = j + 1;
                j = s1.indexOf(")", i);
                String s5 = s1.substring(i, j);
                color = new Color(Integer.parseInt(s3), Integer.parseInt(s4),
                        Integer.parseInt(s5));
            }
            if (color != null) {
                myStyle = 65535;
                myColor = color;
            }
        }
    }

    private void SVGWriteGraphicViewerElementAttributes(IDomElement domelement) {
        domelement.setAttribute("brushstyle", Integer.toString(myStyle));
        if (myColor != null) {
            String s = "black";
            if (myColor == ColorBlack) {
                s = "black";
            } else if (myColor == ColorDarkGray) {
                s = "darkGray";
            } else if (myColor == ColorGray) {
                s = "gray";
            } else if (myColor == ColorLightGray) {
                s = "lightGray";
            } else if (myColor == ColorWhite) {
                s = "white";
            } else if (myColor == ColorRed) {
                s = "red";
            } else if (myColor == ColorMagenta) {
                s = "magenta";
            } else if (myColor == ColorYellow) {
                s = "yellow";
            } else if (myColor == ColorGreen) {
                s = "green";
            } else if (myColor == ColorCyan) {
                s = "cyan";
            } else if (myColor == ColorBlue) {
                s = "blue";
            } else if (myColor == ColorOrange) {
                s = "orange";
            } else if (myColor == ColorPink) {
                s = "pink";
            } else {
                int i = myColor.getRed();
                int j = myColor.getGreen();
                int k = myColor.getBlue();
                int l = myColor.getAlpha();
                s = "rgbalpha(" + Integer.toString(i) + ","
                        + Integer.toString(j) + "," + Integer.toString(k) + ","
                        + Integer.toString(l) + ")";
            }
            domelement.setAttribute("brushcolor", s);
        }
    }

    private void SVGReadGraphicViewerElementAttributes(IDomElement domelement) {
        String s = domelement.getAttribute("brushstyle");
        myStyle = Integer.parseInt(s);
        String s1 = domelement.getAttribute("brushcolor");
        if (s1.length() > 0) {
            if (s1.equals("black")) {
                myColor = ColorBlack;
            } else if (s1.equals("darkGray")) {
                myColor = ColorDarkGray;
            } else if (s1.equals("gray")) {
                myColor = ColorGray;
            } else if (s1.equals("lightGray")) {
                myColor = ColorLightGray;
            } else if (s1.equals("white")) {
                myColor = ColorWhite;
            } else if (s1.equals("red")) {
                myColor = ColorRed;
            } else if (s1.equals("magenta")) {
                myColor = ColorMagenta;
            } else if (s1.equals("yellow")) {
                myColor = ColorYellow;
            } else if (s1.equals("green")) {
                myColor = ColorGreen;
            } else if (s1.equals("cyan")) {
                myColor = ColorCyan;
            } else if (s1.equals("blue")) {
                myColor = ColorBlue;
            } else if (s1.equals("orange")) {
                myColor = ColorOrange;
            } else if (s1.equals("pink")) {
                myColor = ColorPink;
            } else if (s1.startsWith("rgbalpha")) {
                int i = s1.indexOf("(") + 1;
                int j = s1.indexOf(",", i);
                String s2 = s1.substring(i, j);
                i = j + 1;
                j = s1.indexOf(",", i);
                String s3 = s1.substring(i, j);
                i = j + 1;
                j = s1.indexOf(",", i);
                String s4 = s1.substring(i, j);
                i = j + 1;
                j = s1.indexOf(")", i);
                String s5 = s1.substring(i, j);
                myColor = new Color(Integer.parseInt(s2), Integer.parseInt(s3),
                        Integer.parseInt(s4), Integer.parseInt(s5));
            }
        }
    }

    public int getStyle() {
        return myStyle;
    }

    public Paint getPaint() {
        if (myPaint == null) {
            switch (getStyle()) {
                case 0 : // '\0'
                default :
                    myPaint = null;
                    break;

                case 65535 :
                    myPaint = myColor;
                    break;
            }
        }
        return myPaint;
    }

    public Color getColor() {
        return myColor;
    }
}