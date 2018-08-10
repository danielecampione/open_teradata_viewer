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
import java.awt.Rectangle;

import javax.swing.plaf.metal.MetalCheckBoxIcon;

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