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
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.plaf.metal.MetalScrollButton;

/**
 * 
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
public class KunststoffScrollButton extends MetalScrollButton {

    private static final long serialVersionUID = 2611825589859393117L;

    public KunststoffScrollButton(int direction, int width, boolean freeStanding) {
        super(direction, width, freeStanding);
    }

    /**
     * Calls the super classes paint(Graphics g) method and then paints two gradients.
     * The direction of the gradients depends on the direction of the scrollbar.
     */
    public void paint(Graphics g) {
        super.paint(g);
        int width = getWidth();
        int height = getHeight();
        Rectangle rectReflection;
        Rectangle rectShadow;
        boolean isVertical = (getDirection() == EAST || getDirection() == WEST);
        if (isVertical) {
            rectReflection = new Rectangle(1, 1, width, height / 2);
            rectShadow = new Rectangle(1, height / 2, width, height / 2 + 1);
        } else {
            rectReflection = new Rectangle(1, 1, width / 2, height);
            rectShadow = new Rectangle(width / 2, 1, width / 2 + 1, height);
        }

        // paint reflection gradient
        Color colorReflection = KunststoffLookAndFeel
                .getComponentGradientColorReflection();
        if (colorReflection != null) {
            Color colorReflectionFaded = KunststoffUtilities
                    .getTranslucentColor(colorReflection, 0);
            KunststoffUtilities.drawGradient(g, colorReflection,
                    colorReflectionFaded, rectReflection, isVertical);
        }

        // paint shadow gradient
        Color colorShadow = KunststoffLookAndFeel
                .getComponentGradientColorShadow();
        if (colorShadow != null) {
            Color colorShadowFaded = KunststoffUtilities.getTranslucentColor(
                    colorShadow, 0);
            KunststoffUtilities.drawGradient(g, colorShadowFaded, colorShadow,
                    rectShadow, isVertical);
        }

    }

}