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
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.ColorModel;

/**
 * 
 * 
 * @author Christoph Wilhelms
 * 
 */
public class FastGradientPaint implements Paint {

    int startColor, endColor;
    boolean isVertical;

    public FastGradientPaint(Color sc, Color ec, boolean isV) {
        startColor = sc.getRGB();
        endColor = ec.getRGB();
        isVertical = isV;
    }

    public synchronized PaintContext createContext(ColorModel cm, Rectangle r,
            Rectangle2D r2d, AffineTransform xform, RenderingHints hints) {
        return new FastGradientPaintContext(cm, r, startColor, endColor,
                isVertical);
    }

    public int getTransparency() {
        return ((((startColor & endColor) >> 24) & 0xFF) == 0xFF)
                ? OPAQUE
                : TRANSLUCENT;
    }
}
