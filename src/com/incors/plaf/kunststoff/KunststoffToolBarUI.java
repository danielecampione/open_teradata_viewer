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

package com.incors.plaf.kunststoff;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalToolBarUI;

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
public class KunststoffToolBarUI extends MetalToolBarUI {

    private final static KunststoffToolBarUI toolBarUI = new KunststoffToolBarUI();

    public static ComponentUI createUI(JComponent c) {
        return toolBarUI;
    }

    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);

        int orientation = SwingConstants.HORIZONTAL;

        if (c instanceof JToolBar) {
            orientation = ((JToolBar) c).getOrientation();
        }

        if (orientation == SwingConstants.HORIZONTAL) {

            // paint upper (reflection) gradient
            Color colorReflection = KunststoffLookAndFeel
                    .getComponentGradientColorReflection();
            if (colorReflection != null) {
                Color colorReflectionFaded = KunststoffUtilities
                        .getTranslucentColor(colorReflection, 0);
                Rectangle rect = new Rectangle(0, 0, c.getWidth(),
                        c.getHeight() / 2);
                KunststoffUtilities.drawGradient(g, colorReflection,
                        colorReflectionFaded, rect, true);
            }
            // paint lower (shadow) gradient
            Color colorShadow = KunststoffLookAndFeel
                    .getComponentGradientColorShadow();
            if (colorShadow != null) {
                Color colorShadowFaded = KunststoffUtilities
                        .getTranslucentColor(colorShadow, 0);
                Rectangle rect = new Rectangle(0, c.getHeight() / 2,
                        c.getWidth(), c.getHeight() / 2);
                KunststoffUtilities.drawGradient(g, colorShadowFaded,
                        colorShadow, rect, true);
            }

        } else { // is orientation is vertical

            // paint left (reflection) gradient
            Color colorReflection = KunststoffLookAndFeel
                    .getComponentGradientColorReflection();
            if (colorReflection != null) {
                Color colorReflectionFaded = KunststoffUtilities
                        .getTranslucentColor(colorReflection, 0);
                Rectangle rect = new Rectangle(0, 0, c.getWidth() / 2,
                        c.getHeight());
                KunststoffUtilities.drawGradient(g, colorReflection,
                        colorReflectionFaded, rect, false);
            }
            // paint right (shadow) gradient
            Color colorShadow = KunststoffLookAndFeel
                    .getComponentGradientColorShadow();
            if (colorShadow != null) {
                Color colorShadowFaded = KunststoffUtilities
                        .getTranslucentColor(colorShadow, 0);
                Rectangle rect = new Rectangle(c.getWidth() / 2, 0,
                        c.getWidth() / 2, c.getHeight());
                KunststoffUtilities.drawGradient(g, colorShadowFaded,
                        colorShadow, rect, false);
            }
        }

    }

}
