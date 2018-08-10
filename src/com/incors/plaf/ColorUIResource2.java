/*
 * Open Teradata Viewer ( look and feel )
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

package com.incors.plaf;

import java.awt.Color;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

import javax.swing.plaf.ColorUIResource;

/**
 * This class had to be created because <code>ColorUIResouce</code> does not allow
 * transparency. Hopefully one day support for transparency will be added to we
 * ColorUIResouce and we can get rid of this class. Wrapping a <code>Color</color>
 * object to make a pseudo subclass is very ugly.
 * 
 * @author Christoph Wilhelms
 * 
 */
public class ColorUIResource2 extends ColorUIResource {

    private static final long serialVersionUID = 4261040681968142273L;
    private Color myColor;

    // constructors

    public ColorUIResource2(Color c) {
        super(0, 0, 0);
        myColor = c;
    }

    public ColorUIResource2(int r, int g, int b) {
        super(0, 0, 0);
        myColor = new Color(r, g, b);
    }

    public ColorUIResource2(int r, int g, int b, int a) {
        super(0, 0, 0);
        myColor = new Color(r, g, b, a);
    }

    public ColorUIResource2(int rgb) {
        super(0, 0, 0);
        myColor = new Color(rgb);
    }

    public ColorUIResource2(int rgba, boolean hasalpha) {
        super(0, 0, 0);
        myColor = new Color(rgba, hasalpha);
    }

    public ColorUIResource2(float r, float g, float b) {
        super(0, 0, 0);
        myColor = new Color(r, g, b);
    }

    public ColorUIResource2(float r, float g, float b, float alpha) {
        super(0, 0, 0);
        myColor = new Color(r, g, b, alpha);
    }

    // non static methods

    public int getRed() {
        return myColor.getRed();
    }

    public int getGreen() {
        return myColor.getGreen();
    }

    public int getBlue() {
        return myColor.getBlue();
    }

    public int getAlpha() {
        return myColor.getAlpha();
    }

    public int getRGB() {
        return myColor.getRGB();
    }

    public Color brighter() {
        return myColor.brighter();
    }

    public Color darker() {
        return myColor.darker();
    }

    public int hashCode() {
        return myColor.hashCode();
    }

    public boolean equals(Object obj) {
        return myColor.equals(obj);
    }

    public String toString() {
        return myColor.toString();
    }

    public float[] getRGBComponents(float[] compArray) {
        return myColor.getRGBComponents(compArray);
    }

    public float[] getRGBColorComponents(float[] compArray) {
        return myColor.getRGBColorComponents(compArray);
    }

    public float[] getComponents(float[] compArray) {
        return myColor.getComponents(compArray);
    }

    public float[] getColorComponents(float[] compArray) {
        return myColor.getColorComponents(compArray);
    }

    public float[] getComponents(ColorSpace cspace, float[] compArray) {
        return myColor.getComponents(cspace, compArray);
    }

    public float[] getColorComponents(ColorSpace cspace, float[] compArray) {
        return myColor.getColorComponents(cspace, compArray);
    }

    public ColorSpace getColorSpace() {
        return myColor.getColorSpace();
    }

    public PaintContext createContext(ColorModel cm, Rectangle r,
            Rectangle2D r2d, AffineTransform xform, RenderingHints hints) {
        return myColor.createContext(cm, r, r2d, xform, hints);
    }

    public int getTransparency() {
        return myColor.getTransparency();
    }

}