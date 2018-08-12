/*
 * Open Teradata Viewer ( util )
 * Copyright (C) 2013, D. Campione
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

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * Utilities for translucent windows in Java 6 update 10 and newer.
 *
 * @author D. Campione
 * 
 */
public abstract class TranslucencyUtil {

    /** The singleton instance. */
    private static TranslucencyUtil instance;

    /**
     * Returns the singleton instance of this class.
     *
     * @return The singleton instance, or <code>null</code> if something went
     *         wrong.
     */
    public static TranslucencyUtil get() {
        if (instance == null) {
            String ver = System.getProperty("java.specification.version");

            try {
                if ("1.6".equals(ver)) {
                    Class clazz = Class
                            .forName("net.sourceforge.open_teradata_viewer.util.Java6TranslucencyUtil");
                    instance = (TranslucencyUtil) clazz.newInstance();
                } else { // Java 1.7 +
                    Class clazz = Class
                            .forName("net.sourceforge.open_teradata_viewer.util.Java7TranslucencyUtil");
                    instance = (TranslucencyUtil) clazz.newInstance();
                }
            } catch (RuntimeException re) {
                throw re;
            } catch (Exception e) {
                ExceptionDialog.hideException(e);
            }

            // Something bad happened during our reflection
            if (instance == null) {
                instance = new DummyTranslucencyUtil();
            }
        }

        return instance;
    }

    /**
     * Returns the opacity of a (fully, not per-pixel, translucent) window.
     *
     * @param w The window.
     * @return The opacity of the window. A value of <code>1.0f</code> means the
     *         window is fully opaque.
     * @see #setOpacity(Window, float)
     */
    public abstract float getOpacity(Window w);

    /**
     * Returns whether translucency is supported by this JVM.
     *
     * @param perPixel Whether to check for per-pixel translucency (vs. just
     *        translucency of an entire window, which is cheaper).
     * @return Whether translucency is supported.
     */
    public abstract boolean isTranslucencySupported(boolean perPixel);

    /**
     * Toggles the opacity of an entire window (i.e., non-per-pixel opacity).
     *
     * @param w The window to modify.
     * @param value The opacity.
     * @return Whether the operation was successful.
     * @see #getOpacity(Window)
     * @see #setOpaque(Window, boolean)
     */
    public abstract boolean setOpacity(Window w, float value);

    /**
     * Toggles whether a window is fully opaque (e.g., toggles per-pixel
     * translucency). To set the translucency of an entire window, on a
     * non-per-pixel basis (which is cheaper), use {@link #setOpacity(Window,
     * float)}.
     *
     * @param w The window to modify.
     * @param opaque Whether the window should be fully opaque (versus per-pixel
     *        translucent).
     * @return Whether the operation was successful.
     * @see #setOpacity(Window, float)
     */
    public abstract boolean setOpaque(Window w, boolean opaque);
}