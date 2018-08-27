/*
 * Open Teradata Viewer ( editor autocomplete )
 * Copyright (C) 2015, D. Campione
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

package net.sourceforge.open_teradata_viewer.editor.autocomplete;

import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.lang.reflect.Method;
import java.net.URI;
import java.security.AccessControlException;

import net.sourceforge.open_teradata_viewer.editor.syntax.PopupWindowDecorator;

/**
 * Utility methods for the auto-complete framework.
 *
 * @author D. Campione
 * 
 */
public class Util {

    /**
     * If this system property is <code>true</code>, then even the "main" two
     * auto-complete windows will allow window decorations via {@link
     * PopupWindowDecorator}. If this property is undefined or
     * <code>false</code>, they won't honor such decorations. This is due to
     * certain performance issues with translucent windows (used for drop
     * shadows), even as of Java 7u2.
     */
    public static final String PROPERTY_ALLOW_DECORATED_AUTOCOMPLETE_WINDOWS = "net.sourceforge.open_teradata_viewer.editor.autocomplete.allowDecoratedAutoCompleteWindows";

    private static boolean desktopCreationAttempted;
    private static Object desktop;
    private static final Object LOCK_DESKTOP_CREATION = new Object();

    /**
     * Attempts to open a Web browser to the specified URI.
     *
     * @param uri The URI to open. If this is <code>null</code>, nothing happens
     *        and this method returns <code>false</code>.
     * @return Whether the operation was successful. This will be
     *         <code>false</code> on JRE's older than 1.6.
     */
    public static boolean browse(URI uri) {
        boolean success = false;

        if (uri != null) {
            Object desktop = getDesktop();
            if (desktop != null) {
                try {
                    Method m = desktop.getClass().getDeclaredMethod("browse",
                            new Class[] { URI.class });
                    m.invoke(desktop, new Object[] { uri });
                    success = true;
                } catch (RuntimeException re) {
                    throw re;
                } catch (Exception e) {
                    // Ignore, just return "false" below
                }
            }
        }

        return success;
    }

    /**
     * Returns the singleton <code>java.awt.Desktop</code> instance, or
     * <code>null</code> if it is unsupported on this platform (or the JRE is
     * older than 1.6).
     *
     * @return The desktop, as an {@link Object}.
     */
    private static Object getDesktop() {
        synchronized (LOCK_DESKTOP_CREATION) {
            if (!desktopCreationAttempted) {
                desktopCreationAttempted = true;

                try {
                    Class<?> desktopClazz = Class.forName("java.awt.Desktop");
                    Method m = desktopClazz
                            .getDeclaredMethod("isDesktopSupported");

                    boolean supported = ((Boolean) m.invoke(null))
                            .booleanValue();
                    if (supported) {
                        m = desktopClazz.getDeclaredMethod("getDesktop");
                        desktop = m.invoke(null);
                    }
                } catch (RuntimeException re) {
                    throw re;
                } catch (Exception e) {
                    // Ignore; keeps desktop as null
                }
            }
        }

        return desktop;
    }

    /**
     * Returns a hex string for the specified color, suitable for HTML.
     *
     * @param c The color.
     * @return The string representation, in the form "<code>#rrggbb</code>", or
     *         <code>null</code> if <code>c</code> is <code>null</code>.
     */
    public static String getHexString(Color c) {
        if (c == null) {
            return null;
        }

        // Don't assume 0xff alpha
        //return "#" + Integer.toHexString(c.getRGB()&0xffffff).substring(2);

        StringBuilder sb = new StringBuilder("#");
        int r = c.getRed();
        if (r < 16) {
            sb.append('0');
        }
        sb.append(Integer.toHexString(r));
        int g = c.getGreen();
        if (g < 16) {
            sb.append('0');
        }
        sb.append(Integer.toHexString(g));
        int b = c.getBlue();
        if (b < 16) {
            sb.append('0');
        }
        sb.append(Integer.toHexString(b));

        return sb.toString();
    }

    /**
     * Returns the screen coordinates for the monitor that contains the
     * specified point. This is useful for setups with multiple monitors, to
     * ensure that popup windows are positioned properly.
     *
     * @param x The x-coordinate, in screen coordinates.
     * @param y The y-coordinate, in screen coordinates.
     * @return The bounds of the monitor that contains the specified point.
     */
    public static Rectangle getScreenBoundsForPoint(int x, int y) {
        GraphicsEnvironment env = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = env.getScreenDevices();
        for (int i = 0; i < devices.length; i++) {
            GraphicsConfiguration config = devices[i].getDefaultConfiguration();
            Rectangle gcBounds = config.getBounds();
            if (gcBounds.contains(x, y)) {
                return gcBounds;
            }
        }
        // If point is outside all monitors, default to default monitor
        return env.getMaximumWindowBounds();
    }

    /**
     * Give apps a chance to decorate us with drop shadows, etc.. Since very
     * scrolly things such as lists (of e.g. completions) are *very* slow when
     * in per-pixel translucent windows, even as of Java 7u2, we force the user
     * to specify an extra option for the two "main" auto-complete windows.
     *
     * @return Whether to allow decorating the main auto-complete windows.
     * @see #PROPERTY_ALLOW_DECORATED_AUTOCOMPLETE_WINDOWS
     */
    public static boolean getShouldAllowDecoratingMainAutoCompleteWindows() {
        try {
            return Boolean
                    .getBoolean(PROPERTY_ALLOW_DECORATED_AUTOCOMPLETE_WINDOWS);
        } catch (AccessControlException ace) { // We're in an applet
            return false;
        }
    }

    /**
     * Returns whether <code>str</code> starts with <code>start</code>,
     * ignoring case.
     *
     * @param str The string to check.
     * @param start The prefix to check for.
     * @return Whether <code>str</code> starts with <code>start</code>,
     *         ignoring case.
     */
    public static boolean startsWithIgnoreCase(String str, String start) {
        int startLen = start.length();
        if (str.length() >= startLen) {
            for (int i = 0; i < startLen; i++) {
                char c1 = str.charAt(i);
                char c2 = start.charAt(i);
                if (Character.toLowerCase(c1) != Character.toLowerCase(c2)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}