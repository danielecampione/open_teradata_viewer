/*
 * Open Teradata Viewer ( util )
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

package net.sourceforge.open_teradata_viewer.util;

import java.awt.Window;

/**
 * Returned when a system does not support translucency.
 *
 * @author D. Campione
 * 
 */
class DummyTranslucencyUtil extends TranslucencyUtil {

    /** {@inheritDoc} */
    public float getOpacity(Window w) {
        return 1f;
    }

    /** {@inheritDoc} */
    public boolean isTranslucencySupported(boolean perPixel) {
        return false;
    }

    /** {@inheritDoc} */
    public boolean setOpacity(Window w, float value) {
        return false;
    }

    /** {@inheritDoc} */
    public boolean setOpaque(Window w, boolean opaque) {
        return false;
    }
}