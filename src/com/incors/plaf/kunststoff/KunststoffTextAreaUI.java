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
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextAreaUI;

/**
 * 
 * 
 * @author Timo Haberkern
 * 
 */
public class KunststoffTextAreaUI extends BasicTextAreaUI {

    protected JComponent myComponent;

    public KunststoffTextAreaUI(JComponent c) {
        super();
        myComponent = c;
    }

    public static ComponentUI createUI(JComponent c) {
        return new KunststoffTextAreaUI(c);
    }

    protected void paintBackground(Graphics g) {
        super.paintBackground(g);
        Rectangle editorRect = getVisibleEditorRect();

        // paint upper gradient
        Color colorReflection = KunststoffLookAndFeel
                .getTextComponentGradientColorReflection();
        if (colorReflection != null) {
            Color colorReflectionFaded = KunststoffUtilities
                    .getTranslucentColor(colorReflection, 0);
            Rectangle rect = new Rectangle(editorRect.x, editorRect.y,
                    editorRect.width, editorRect.height / 2);
            KunststoffUtilities.drawGradient(g, colorReflection,
                    colorReflectionFaded, rect, true);
        }

        // paint lower gradient
        Color colorShadow = KunststoffLookAndFeel
                .getTextComponentGradientColorShadow();
        if (colorShadow != null) {
            Color colorShadowFaded = KunststoffUtilities.getTranslucentColor(
                    colorShadow, 0);
            Rectangle rect = new Rectangle(editorRect.x, editorRect.y
                    + editorRect.height / 2, editorRect.width,
                    editorRect.height / 2);
            KunststoffUtilities.drawGradient(g, colorShadowFaded, colorShadow,
                    rect, true);
        }
    }
}