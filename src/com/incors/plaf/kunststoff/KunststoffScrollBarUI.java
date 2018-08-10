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

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalScrollBarUI;

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
public class KunststoffScrollBarUI extends MetalScrollBarUI {

    public static ComponentUI createUI(JComponent c) {
        return new KunststoffScrollBarUI();
    }

    protected JButton createDecreaseButton(int orientation) {
        decreaseButton = new KunststoffScrollButton(orientation,
                scrollBarWidth, isFreeStanding);
        return decreaseButton;
    }

    protected JButton createIncreaseButton(int orientation) {
        increaseButton = new KunststoffScrollButton(orientation,
                scrollBarWidth, isFreeStanding);
        return increaseButton;
    }

    /**
     * Calls the super classes paint(Graphics g) method and then paints two gradients.
     * The direction of the gradients depends on the direction of the scrollbar.
     */
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        super.paintThumb(g, c, thumbBounds);

        // colors for the reflection gradient
        Color colorReflection = KunststoffLookAndFeel
                .getComponentGradientColorReflection();
        Color colorReflectionFaded = KunststoffUtilities.getTranslucentColor(
                colorReflection, 0);
        // colors for the shadow gradient
        Color colorShadow = KunststoffLookAndFeel
                .getComponentGradientColorShadow();
        Color colorShadowFaded = KunststoffUtilities.getTranslucentColor(
                colorShadow, 0);

        Rectangle rectReflection; // rectangle for the reflection gradient
        Rectangle rectShadow; // rectangle for the shadow gradient
        if (scrollbar.getOrientation() == JScrollBar.VERTICAL) {
            rectReflection = new Rectangle(thumbBounds.x + 1, thumbBounds.y,
                    thumbBounds.width / 2, thumbBounds.height);
            rectShadow = new Rectangle(thumbBounds.x + thumbBounds.width / 2,
                    thumbBounds.y, thumbBounds.width / 2 + 1,
                    thumbBounds.height);
        } else {
            rectReflection = new Rectangle(thumbBounds.x, thumbBounds.y + 1,
                    thumbBounds.width, thumbBounds.height / 2);
            rectShadow = new Rectangle(thumbBounds.x, thumbBounds.y
                    + thumbBounds.height / 2, thumbBounds.width,
                    thumbBounds.height / 2 + 1);
        }

        // the direction of the gradient is orthogonal to the direction of the scrollbar
        boolean isVertical = (scrollbar.getOrientation() == JScrollBar.HORIZONTAL);
        KunststoffUtilities.drawGradient(g, colorReflection,
                colorReflectionFaded, rectReflection, isVertical);
        KunststoffUtilities.drawGradient(g, colorShadowFaded, colorShadow,
                rectShadow, isVertical);
    }

}