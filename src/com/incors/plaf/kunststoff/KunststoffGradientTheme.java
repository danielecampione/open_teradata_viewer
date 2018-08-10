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

import javax.swing.plaf.ColorUIResource;

import com.incors.plaf.ColorUIResource2;

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
public class KunststoffGradientTheme implements GradientTheme {

    // gradient colors

    private final ColorUIResource componentGradientColorReflection = new ColorUIResource2(
            255, 255, 255, 96);

    private final ColorUIResource componentGradientColorShadow = new ColorUIResource2(
            0, 0, 0, 48);

    private final ColorUIResource textComponentGradientColorReflection = new ColorUIResource2(
            0, 0, 0, 32);

    private final ColorUIResource textComponentGradientColorShadow = null;

    private final int backgroundGradientShadow = 32;

    // methods

    public String getName() {
        return "Default Kunststoff Gradient Theme";
    }

    // methods for getting gradient colors

    public ColorUIResource getComponentGradientColorReflection() {
        return componentGradientColorReflection;
    }

    public ColorUIResource getComponentGradientColorShadow() {
        return componentGradientColorShadow;
    }

    public ColorUIResource getTextComponentGradientColorReflection() {
        return textComponentGradientColorReflection;
    }

    public ColorUIResource getTextComponentGradientColorShadow() {
        return textComponentGradientColorShadow;
    }

    public int getBackgroundGradientShadow() {
        return backgroundGradientShadow;
    }

}