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

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableHeaderUI;

/**
 * 
 * 
 * @author <A HREF="mailto:jens@jensn.de">Jens Niemeyer</A>
 * 
 */
public class KunststoffTableHeaderUI extends BasicTableHeaderUI {

    public static ComponentUI createUI(JComponent h) {
        return new KunststoffTableHeaderUI();
    }

    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);

        if (!c.isOpaque()) {
            return; // we only draw the gradients if the component is opaque
        }

        // paint reflection
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

        // paint shadow
        Color colorShadow = KunststoffLookAndFeel
                .getComponentGradientColorShadow();
        if (colorShadow != null) {
            Color colorShadowFaded = KunststoffUtilities.getTranslucentColor(
                    colorShadow, 0);
            Rectangle rect = new Rectangle(0, c.getHeight() / 2, c.getWidth(),
                    c.getHeight() / 2);
            KunststoffUtilities.drawGradient(g, colorShadowFaded, colorShadow,
                    rect, true);
        }
    }
}
