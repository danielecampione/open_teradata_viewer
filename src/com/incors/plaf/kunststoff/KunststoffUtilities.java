/*
 * Open Teradata Viewer ( look and feel )
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

package com.incors.plaf.kunststoff;

import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;

import com.incors.plaf.ColorUIResource2;
import com.incors.plaf.FastGradientPaint;

/**
 * Collection of methods often used in the Kunststoff Look&Feel
 * 
 * @author Aljoscha Rittner
 * @author C.J. Kent
 * @author Christian Peter
 * @author Christoph Wilhelms
 * @author Eric Georges
 * @author Gerald Bauer
 * @author Ingo Kegel
 * @author Jamie LaScolea
 * @author <A HREF="mailto:jens@jensn.de">Jens Niemeyer</A>
 * @author Jerason Banes
 * @author Jim Wissner
 * @author Johannes Ernst
 * @author Jonas Kilian
 * @author <A HREF="mailto:julien@izforge.com">Julien Ponge</A>
 * @author Karsten Lentzsch
 * @author Matthew Philips
 * @author Romain Guy
 * @author Sebastian Ferreyra
 * @author Steve Varghese
 * @author Taoufik Romdhane
 * @author Timo Haberkern
 * 
 */
public class KunststoffUtilities {

    /**
     * Convenience method to create a translucent <code>Color</color>.
     */
    public static Color getTranslucentColor(Color color, int alpha) {
        if (color == null) {
            return null;
        } else if (alpha == 255) {
            return color;
        } else {
            return new Color(color.getRed(), color.getGreen(), color.getBlue(),
                    alpha);
        }
    }

    /**
     * Convenience method to create a translucent <code>ColorUIResource</code>.
     */
    public static Color getTranslucentColorUIResource(Color color, int alpha) {
        if (color == null) {
            return null;
        } else if (alpha == 255) {
            return color;
        } else {
            return new ColorUIResource2(color.getRed(), color.getGreen(),
                    color.getBlue(), alpha);
        }
    }

    /**
     * Convenience method to draw a gradient on the specified rectangle
     */
    public static void drawGradient(Graphics g, Color color1, Color color2,
            Rectangle rect, boolean isVertical) {
        Graphics2D g2D = (Graphics2D) g;
        Paint gradient = new FastGradientPaint(color1, color2, isVertical);
        g2D.setPaint(gradient);
        g2D.fill(rect);
    }

    /**
     * Convenience method to draw a gradient. The first rectangle defines the drawing region,
     * the second rectangle defines the size of the gradient.
     */
    public static void drawGradient(Graphics g, Color color1, Color color2,
            Rectangle rect, Rectangle rect2, boolean isVertical) {
        // We are currently not using the FastGradientPaint to render this gradient, because we have to decide how
        // we can use FastGradientPaint if rect and rect2 are different.
        if (isVertical) {
            Graphics2D g2D = (Graphics2D) g;
            GradientPaint gradient = new GradientPaint(0f, (float) rect.getY(),
                    color1, 0f, (float) (rect.getHeight() + rect.getY()),
                    color2);
            g2D.setPaint(gradient);
            g2D.fill(rect);
        } else {
            Graphics2D g2D = (Graphics2D) g;
            GradientPaint gradient = new GradientPaint((float) rect.getX(), 0f,
                    color1, (float) (rect.getWidth() + rect.getX()), 0f, color2);
            g2D.setPaint(gradient);
            g2D.fill(rect);
        }
    }

    /**
     * Returns true if the display uses 24- or 32-bit color depth (= true color)
     */
    public static boolean isToolkitTrueColor(Component c) {
        int pixelsize = c.getToolkit().getColorModel().getPixelSize();
        return pixelsize >= 24;
    }
}
