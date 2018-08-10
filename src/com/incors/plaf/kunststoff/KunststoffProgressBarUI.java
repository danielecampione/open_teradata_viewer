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
import javax.swing.JProgressBar;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;

/**
 * 
 * 
 * @author <A HREF="mailto:julien@izforge.com">Julien Ponge</A>
 * 
 */
public class KunststoffProgressBarUI extends BasicProgressBarUI {

    // Creates the UI
    public static ComponentUI createUI(JComponent x) {
        return new KunststoffProgressBarUI();
    }

    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);

        JProgressBar prog = (JProgressBar) c;
        if (prog.getOrientation() == JProgressBar.HORIZONTAL) {

            // paint upper gradient
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

            // paint lower gradient
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

        } else { // if progress bar is vertical

            // paint left gradient
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

            // paint right gradient
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