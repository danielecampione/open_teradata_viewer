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
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalToggleButtonUI;

/**
 * 
 * 
 * @author <A HREF="mailto:jens@jensn.de">Jens Niemeyer</A>
 * 
 */
public class KunststoffToggleButtonUI extends MetalToggleButtonUI {

    private final static KunststoffToggleButtonUI buttonUI = new KunststoffToggleButtonUI();

    public static ComponentUI createUI(JComponent c) {
        return buttonUI;
    }

    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);

        if (!c.isOpaque()) {
            return; // we only draw the gradients if the component is opaque
        }

        Component parent = c.getParent();

        if (parent instanceof JToolBar) {
            int orientation = ((JToolBar) parent).getOrientation();
            Point loc = c.getLocation();
            Rectangle rectReflection;
            Rectangle rectShadow;
            Color colorReflection = KunststoffLookAndFeel
                    .getComponentGradientColorReflection();
            Color colorShadow = KunststoffLookAndFeel
                    .getComponentGradientColorShadow();

            if (orientation == SwingConstants.HORIZONTAL) {
                // paint upper gradient
                if (colorReflection != null) {
                    Color colorReflectionFaded = KunststoffUtilities
                            .getTranslucentColor(colorReflection, 0);
                    rectReflection = new Rectangle(0, -loc.y,
                            parent.getWidth(), parent.getHeight() / 2);
                    KunststoffUtilities.drawGradient(g, colorReflection,
                            colorReflectionFaded, rectReflection, true);
                }
                // paint lower gradient
                if (colorShadow != null) {
                    Color colorShadowFaded = KunststoffUtilities
                            .getTranslucentColor(colorShadow, 0);
                    rectShadow = new Rectangle(0, parent.getHeight() / 2
                            - loc.y, parent.getWidth(), parent.getHeight() / 2);
                    KunststoffUtilities.drawGradient(g, colorShadowFaded,
                            colorShadow, rectShadow, true);
                }
            } else { // if tool bar orientation is vertical
                // paint left gradient
                if (colorReflection != null) {
                    Color colorReflectionFaded = KunststoffUtilities
                            .getTranslucentColor(colorReflection, 0);
                    rectReflection = new Rectangle(0, 0, parent.getWidth() / 2,
                            parent.getHeight());
                    KunststoffUtilities.drawGradient(g, colorReflection,
                            colorReflectionFaded, rectReflection, false);
                }
                // paint right gradient
                if (colorShadow != null) {
                    Color colorShadowFaded = KunststoffUtilities
                            .getTranslucentColor(colorShadow, 0);
                    rectShadow = new Rectangle(parent.getWidth() / 2 - loc.x,
                            0, parent.getWidth(), parent.getHeight());
                    KunststoffUtilities.drawGradient(g, colorShadowFaded,
                            colorShadow, rectShadow, false);
                }
            }
        } else { // if not in tool bar
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
        }
    }

}