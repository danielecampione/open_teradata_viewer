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

import javax.swing.JInternalFrame;
import javax.swing.plaf.metal.MetalInternalFrameTitlePane;

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
public class KunststoffInternalFrameTitlePane
        extends
            MetalInternalFrameTitlePane {

    private static final long serialVersionUID = -2426327024518552916L;

    public KunststoffInternalFrameTitlePane(JInternalFrame frame) {
        super(frame);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // paint reflection
        Color colorReflection = KunststoffLookAndFeel
                .getComponentGradientColorReflection();
        Color colorReflectionFaded = KunststoffUtilities.getTranslucentColor(
                colorReflection, 0);
        Rectangle rectReflection = new Rectangle(0, 1, this.getWidth(),
                this.getHeight() / 2);;
        KunststoffUtilities.drawGradient(g, colorReflection,
                colorReflectionFaded, rectReflection, true);

        // paint shadow
        Color colorShadow = KunststoffLookAndFeel
                .getComponentGradientColorShadow();
        Color colorShadowFaded = KunststoffUtilities.getTranslucentColor(
                colorShadow, 0);
        Rectangle rectShadow = new Rectangle(0, this.getHeight() / 2,
                this.getWidth(), this.getHeight() / 2 + 1);
        KunststoffUtilities.drawGradient(g, colorShadowFaded, colorShadow,
                rectShadow, true);
    }
}
