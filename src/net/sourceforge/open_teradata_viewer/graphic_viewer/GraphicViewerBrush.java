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

    public GraphicViewerBrush() {
        a = null;
        _flddo = 0;
        _fldif = null;
        a = null;
    }

    public GraphicViewerBrush(int i, Color color) {
        a = null;
        _flddo = i;
        _fldif = color;
        a = color;
    }

    public GraphicViewerBrush(Paint paint) {
        a = null;
        a = paint;
        if (paint instanceof Color) {
            _flddo = 65535;
            _fldif = (Color) paint;
        } else {
            _flddo = 65534;
            _fldif = null;
        }
    }

    public static GraphicViewerBrush makeStockBrush(Color color) {
        if (color.equals(ColorBlack))
            return black;
        if (color.equals(ColorDarkGray))
            return darkGray;
        if (color.equals(ColorGray))
            return gray;
        if (color.equals(ColorLightGray))
            return lightGray;
        if (color.equals(ColorWhite))
            return white;
        if (color.equals(ColorRed))
            return red;
        if (color.equals(ColorMagenta))
            return magenta;
        if (color.equals(ColorYellow))
            return yellow;
        if (color.equals(ColorGreen))
            return green;
        if (color.equals(ColorCyan))
            return cyan;
        if (color.equals(ColorBlue))
            return blue;
        if (color.equals(ColorOrange))
            return orange;
        if (color.equals(ColorPink))
            return pink;
        else
            return new GraphicViewerBrush(65535, color);
    }

    public static GraphicViewerBrush make(int i, Color color) {
        if (i == 65535)
            return makeStockBrush(color);
        else
            return Null;
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerBrush",
                            domelement);
            a(domelement1);
            domdoc.registerObject(this, domelement1);
        }
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, IDomElement domelement,
            IDomElement domelement1) {
        if (domelement1 != null) {
            _mthif(domelement1);
            String s = domelement1.getAttribute("id");
            if (s.length() > 0)
                domdoc.registerTag(s, this);
        }
        return domelement.getNextSibling();
    }

    public void SVGWriteAttributes(IDomElement domelement) {
        String s = "fill:none;";
        if (getStyle() == 0)
            s = "fill:none;";
        else if (getStyle() == 65535) {
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
            if (!domnode.isElement())
                continue;
            IDomElement domelement1 = domnode.elementCast();
            String s2 = domelement1.getAttribute("style");
            if (s1.length() == 0)
                s1 = domelement1.getSubAttribute(s2, "fill");
        }

        Color color = null;
        if (s1.length() > 0) {
            if (s1.equalsIgnoreCase("black"))
                color = ColorBlack;
            else if (s1.equalsIgnoreCase("darkGray"))
                color = ColorDarkGray;
            else if (s1.equalsIgnoreCase("gray"))
                color = ColorGray;
            else if (s1.equalsIgnoreCase("lightGray"))
                color = ColorLightGray;
            else if (s1.equalsIgnoreCase("white"))
                color = ColorWhite;
            else if (s1.equalsIgnoreCase("red"))
                color = ColorRed;
            else if (s1.equalsIgnoreCase("magenta"))
                color = ColorMagenta;
            else if (s1.equalsIgnoreCase("yellow"))
                color = ColorYellow;
            else if (s1.equalsIgnoreCase("green"))
                color = ColorGreen;
            else if (s1.equalsIgnoreCase("cyan"))
                color = ColorCyan;
            else if (s1.equalsIgnoreCase("blue"))
                color = ColorBlue;
            else if (s1.equalsIgnoreCase("orange"))
                color = ColorOrange;
            else if (s1.equalsIgnoreCase("pink"))
                color = ColorPink;
            else if (s1.startsWith("rgb")) {
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
                _flddo = 65535;
                _fldif = color;
            }
        }
    }

    private void a(IDomElement domelement) {
        domelement.setAttribute("brushstyle", Integer.toString(_flddo));
        if (_fldif != null) {
            String s = "black";
            if (_fldif == ColorBlack)
                s = "black";
            else if (_fldif == ColorDarkGray)
                s = "darkGray";
            else if (_fldif == ColorGray)
                s = "gray";
            else if (_fldif == ColorLightGray)
                s = "lightGray";
            else if (_fldif == ColorWhite)
                s = "white";
            else if (_fldif == ColorRed)
                s = "red";
            else if (_fldif == ColorMagenta)
                s = "magenta";
            else if (_fldif == ColorYellow)
                s = "yellow";
            else if (_fldif == ColorGreen)
                s = "green";
            else if (_fldif == ColorCyan)
                s = "cyan";
            else if (_fldif == ColorBlue)
                s = "blue";
            else if (_fldif == ColorOrange)
                s = "orange";
            else if (_fldif == ColorPink) {
                s = "pink";
            } else {
                int i = _fldif.getRed();
                int j = _fldif.getGreen();
                int k = _fldif.getBlue();
                int l = _fldif.getAlpha();
                s = "rgbalpha(" + Integer.toString(i) + ","
                        + Integer.toString(j) + "," + Integer.toString(k) + ","
                        + Integer.toString(l) + ")";
            }
            domelement.setAttribute("brushcolor", s);
        }
    }

    private void _mthif(IDomElement domelement) {
        String s = domelement.getAttribute("brushstyle");
        _flddo = Integer.parseInt(s);
        String s1 = domelement.getAttribute("brushcolor");
        if (s1.length() > 0)
            if (s1.equals("black"))
                _fldif = ColorBlack;
            else if (s1.equals("darkGray"))
                _fldif = ColorDarkGray;
            else if (s1.equals("gray"))
                _fldif = ColorGray;
            else if (s1.equals("lightGray"))
                _fldif = ColorLightGray;
            else if (s1.equals("white"))
                _fldif = ColorWhite;
            else if (s1.equals("red"))
                _fldif = ColorRed;
            else if (s1.equals("magenta"))
                _fldif = ColorMagenta;
            else if (s1.equals("yellow"))
                _fldif = ColorYellow;
            else if (s1.equals("green"))
                _fldif = ColorGreen;
            else if (s1.equals("cyan"))
                _fldif = ColorCyan;
            else if (s1.equals("blue"))
                _fldif = ColorBlue;
            else if (s1.equals("orange"))
                _fldif = ColorOrange;
            else if (s1.equals("pink"))
                _fldif = ColorPink;
            else if (s1.startsWith("rgbalpha")) {
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
                _fldif = new Color(Integer.parseInt(s2), Integer.parseInt(s3),
                        Integer.parseInt(s4), Integer.parseInt(s5));
            }
    }

    public int getStyle() {
        return _flddo;
    }

    public Paint getPaint() {
        if (a == null)
            switch (getStyle()) {
                case 0 : // '\0'
                default :
                    a = null;
                    break;

                case 65535 :
                    a = _fldif;
                    break;
            }
        return a;
    }

    public Color getColor() {
        return _fldif;
    }

    public static final Color ColorBlack;
    public static final Color ColorDarkGray;
    public static final Color ColorGray;
    public static final Color ColorLightGray;
    public static final Color ColorWhite;
    public static final Color ColorRed;
    public static final Color ColorMagenta;
    public static final Color ColorYellow;
    public static final Color ColorGreen;
    public static final Color ColorCyan;
    public static final Color ColorBlue;
    public static final Color ColorOrange;
    public static final Color ColorPink;
    public static final int NONE = 0;
    public static final int SOLID = 65535;
    public static final int CUSTOM = 65534;
    public static final GraphicViewerBrush black;
    public static final GraphicViewerBrush darkGray;
    public static final GraphicViewerBrush gray;
    public static final GraphicViewerBrush lightGray;
    public static final GraphicViewerBrush white;
    public static final GraphicViewerBrush red;
    public static final GraphicViewerBrush magenta;
    public static final GraphicViewerBrush yellow;
    public static final GraphicViewerBrush green;
    public static final GraphicViewerBrush cyan;
    public static final GraphicViewerBrush blue;
    public static final GraphicViewerBrush orange;
    public static final GraphicViewerBrush pink;
    public static final GraphicViewerBrush Null = new GraphicViewerBrush(0,
            null);
    private int _flddo;
    private Color _fldif;
    private transient Paint a;

    static {
        ColorBlack = Color.black;
        ColorDarkGray = Color.darkGray;
        ColorGray = Color.gray;
        ColorLightGray = Color.lightGray;
        ColorWhite = Color.white;
        ColorRed = Color.red;
        ColorMagenta = Color.magenta;
        ColorYellow = Color.yellow;
        ColorGreen = Color.green;
        ColorCyan = Color.cyan;
        ColorBlue = Color.blue;
        ColorOrange = Color.orange;
        ColorPink = Color.pink;
        black = new GraphicViewerBrush(65535, ColorBlack);
        darkGray = new GraphicViewerBrush(65535, ColorDarkGray);
        gray = new GraphicViewerBrush(65535, ColorGray);
        lightGray = new GraphicViewerBrush(65535, ColorLightGray);
        white = new GraphicViewerBrush(65535, ColorWhite);
        red = new GraphicViewerBrush(65535, ColorRed);
        magenta = new GraphicViewerBrush(65535, ColorMagenta);
        yellow = new GraphicViewerBrush(65535, ColorYellow);
        green = new GraphicViewerBrush(65535, ColorGreen);
        cyan = new GraphicViewerBrush(65535, ColorCyan);
        blue = new GraphicViewerBrush(65535, ColorBlue);
        orange = new GraphicViewerBrush(65535, ColorOrange);
        pink = new GraphicViewerBrush(65535, ColorPink);
    }
}