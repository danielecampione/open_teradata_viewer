package com.incors.plaf.kunststoff;

/*
 * This code was developed by INCORS GmbH (www.incors.com).
 * It is published under the terms of the GNU Lesser General Public License.
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.plaf.metal.MetalCheckBoxIcon;

public class KunststoffCheckBoxIcon extends MetalCheckBoxIcon {

    private static final long serialVersionUID = 542119613930659052L;

    public void paintIcon(Component c, Graphics g, int x, int y) {
        super.paintIcon(c, g, x, y);

        int controlSize = getControlSize();

        // paint upper gradient
        Color colorReflection = KunststoffLookAndFeel
                .getComponentGradientColorReflection();
        if (colorReflection != null) {
            Color colorReflectionFaded = KunststoffUtilities
                    .getTranslucentColor(colorReflection, 0);
            Rectangle rect = new Rectangle(x, y, controlSize, controlSize / 2);
            KunststoffUtilities.drawGradient(g, colorReflection,
                    colorReflectionFaded, rect, true);
        }
        // paint lower gradient
        Color colorShadow = KunststoffLookAndFeel
                .getComponentGradientColorShadow();
        if (colorShadow != null) {
            Color colorShadowFaded = KunststoffUtilities.getTranslucentColor(
                    colorShadow, 0);
            Rectangle rect = new Rectangle(x, x + controlSize / 2, controlSize,
                    controlSize / 2);
            KunststoffUtilities.drawGradient(g, colorShadowFaded, colorShadow,
                    rect, true);
        }

    }
}