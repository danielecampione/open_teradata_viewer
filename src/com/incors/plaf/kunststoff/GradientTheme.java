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

import javax.swing.plaf.ColorUIResource;

/**
 * Interface that provides methods for getting the colors for all gradients
 * in the Kunststoff Look&Feel. This interface can be implemented by subclasses
 * of <code>javax.swing.plaf.metal.MetalTheme</code> to have a theme that provides
 * standard colors as well as gradient colors.
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
public interface GradientTheme {

    /**
     * Returns the upper gradient color for components like JButton, JMenuBar,
     * and JProgressBar.
     * Will return <code>null</code> if upper gradient should not be painted.
     */
    public ColorUIResource getComponentGradientColorReflection();

    /**
     * Returns the lower gradient color for components like JButton, JMenuBar,
     * and JProgressBar.
     * Will return <code>null</code> if lower gradient should not be painted.
     */
    public ColorUIResource getComponentGradientColorShadow();

    /**
     * Returns the upper gradient color for text components like JTextField and
     * JPasswordField.
     * Will return <code>null</code> if upper gradient should not be painted.
     */
    public ColorUIResource getTextComponentGradientColorReflection();

    /**
     * Returns the lower gradient color for text components like JTextField and
     * JPasswordField.
     * Will return <code>null</code> if lower gradient should not be painted.
     */
    public ColorUIResource getTextComponentGradientColorShadow();

    public int getBackgroundGradientShadow();

}