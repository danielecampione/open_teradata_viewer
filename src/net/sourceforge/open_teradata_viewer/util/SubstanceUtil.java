/*
 * Open Teradata Viewer ( util )
 * Copyright (C), D. Campione
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

import java.awt.Color;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;

/**
 * Utility methods for interfacing with Substance 7.x (Insubstantial) in
 * applications that only require Java 7 or later.
 *
 * @author D. Campione
 * 
 */
public class SubstanceUtil {

    public static final String FOREGROUND_COLOR = "ForegroundColor";

    public static final String ULTRA_LIGHT_COLOR = "UltraLightColor";

    public static final String EXTRA_LIGHT_COLOR = "ExtraLightColor";

    public static final String LIGHT_COLOR = "LightColor";

    public static final String MID_COLOR = "MidColor";

    public static final String DARK_COLOR = "DarkColor";

    public static final String ULTRA_DARK_COLOR = "UltraDarkColor";

    public static final String LINE_COLOR = "LineColor";

    public static final String SELECTION_BG_COLOR = "SelectionBackgroundColor";

    public static final String SELECTION_FG_COLOR = "SelectionForegroundColor";

    public static final String BACKGROUND_FILL_COLOR = "BackgroundFillColor";

    public static final String TEXT_BG_FILL_COLOR = "TextBackgroundFillColor";

    public static final String FOCUS_RING_COLOR = "FocusRingColor";

    /** Package for the Substance public API. */
    private static final String PKG = "org.pushingpixels.substance.api.";

    private static final String LAFWIDGET_PKG = "org.pushingpixels.lafwidget.";

    /**
     * Returns the length of time GUI animations take, in milliseconds.
     *
     * @return The length of time, in milliseconds.
     * @throws Exception If an error occurs.
     * @see #setAnimationSpeed(long)
     */
    public static long getAnimationSpeed() throws Exception {
        long speed = -1;

        ClassLoader cl = (ClassLoader) UIManager.get("ClassLoader");
        if (cl != null) {
            String managerClassName = LAFWIDGET_PKG
                    + "animation.AnimationConfigurationManager";
            Class managerClazz = Class.forName(managerClassName, true, cl);
            Method m = managerClazz.getMethod("getInstance", null);
            Object manager = m.invoke(null, null);
            m = managerClazz.getMethod("getTimelineDuration", null);
            Long millis = (Long) m.invoke(manager, null);
            speed = millis.longValue();
        }

        return speed;
    }

    /**
     * Returns a color from the currently active Substance skin.
     *
     * @param name The name of a Color, for example {@link #LIGHT_COLOR}.
     * @return The color, or <code>null</code> if no color by that name is
     *         defined.
     * @throws Exception If an error occurs.
     */
    public static Color getSubstanceColor(String name) throws Exception {
        Color color = null;
        name = Character.toUpperCase(name.charAt(0)) + name.substring(1);

        LookAndFeel laf = UIManager.getLookAndFeel();
        ClassLoader cl = (ClassLoader) UIManager.get("ClassLoader");
        if (cl != null) {
            Class clazz = Class.forName(PKG + "SubstanceLookAndFeel", true, cl);
            if (clazz.isInstance(laf)) {
                Class skinClazz = Class
                        .forName(PKG + "SubstanceSkin", true, cl);
                Method m = clazz.getDeclaredMethod("getCurrentSkin", null);
                Object skin = m.invoke(null, null);
                Class decAreaTypeClazz = Class.forName(PKG
                        + "DecorationAreaType", true, cl);
                Field decAreaTypeField = decAreaTypeClazz
                        .getDeclaredField("GENERAL");
                Object decAreaType = decAreaTypeField.get(null);
                m = skinClazz.getDeclaredMethod("getActiveColorScheme",
                        new Class[]{decAreaTypeClazz});
                Object colorScheme = m.invoke(skin, new Object[]{decAreaType});
                Class colorSchemeClazz = Class.forName(PKG
                        + "SubstanceColorScheme", true, cl);
                m = colorSchemeClazz.getMethod("get" + name, null);
                color = (Color) m.invoke(colorScheme, null);
            }
        }

        return color;
    }

    /** @return Whether the currently installed LookAndFeel is Substance. */
    public static boolean isSubstanceInstalled() {
        return isASubstanceLookAndFeel(UIManager.getLookAndFeel());
    }

    /**
     * Returns whether a given LookAndFeel is a Substance LookAndFeel.
     *
     * @param laf The LookAndFeel.
     * @return Whether it is a Substance LookAndFeel.
     * @see #isASubstanceLookAndFeel(String)
     * @see #isSubstanceInstalled()
     */
    public static boolean isASubstanceLookAndFeel(LookAndFeel laf) {
        return isASubstanceLookAndFeel(laf.getClass().getName());
    }

    /**
     * Returns whether a given LookAndFeel is a Substance LookAndFeel.
     *
     * @param lafName The LookAndFeel's class name.
     * @return Whether it is a Substance LookAndFeel.
     * @see #isASubstanceLookAndFeel(LookAndFeel)
     * @see #isSubstanceInstalled()
     */
    public static boolean isASubstanceLookAndFeel(String lafName) {
        return lafName.indexOf(".Substance") > -1;
    }

    /**
     * Configures the length of GUI animations, in milliseconds.
     *
     * @param millis The amount of time animations should take.
     * @throws Exception If an error occurs.
     * @see #getAnimationSpeed()
     */
    public static void setAnimationSpeed(long millis) throws Exception {
        ClassLoader cl = (ClassLoader) UIManager.get("ClassLoader");
        if (cl != null) {
            String managerClassName = LAFWIDGET_PKG
                    + "animation.AnimationConfigurationManager";
            Class managerClazz = Class.forName(managerClassName, true, cl);
            Method m = managerClazz.getMethod("getInstance", null);
            Object manager = m.invoke(null, null);
            m = managerClazz.getMethod("setTimelineDuration",
                    new Class[]{long.class});
            m.invoke(manager, new Object[]{new Long(millis)});
        }
    }
}