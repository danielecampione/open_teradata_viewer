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

import java.lang.reflect.Method;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;

/**
 * Utility methods for interfacing with Radiance Theming (formerly known as
 * Substance, and - in the community-maintained fork OTV previously used -
 * "Insubstantial") in applications that only require Java 9 or later.
 * <p>
 * Substance/Insubstantial stopped being maintained years ago; its original
 * author continued the project under the new name "Radiance", with a
 * completely different package namespace ({@code org.pushingpixels.
 * radiance.theming.api} instead of {@code org.pushingpixels.substance.
 * api}) and, as of its current generation, a completely different color
 * model (Material-Design-3-style color tokens instead of named colors such
 * as "LightColor" or "DarkColor"). Because of that second change, the old
 * {@code getSubstanceColor(String)} method - which used to read one named
 * color off the active skin's color scheme - has no direct equivalent
 * anymore and was removed rather than ported to something that would not
 * actually behave the same way; it had no callers in OTV's own code
 * anyway.
 *
 * @author D. Campione
 * 
 */
public class RadianceUtil {

    /**
     * Package for Radiance Theming's animation manager. NOTE: this class
     * lives under Theming's "internal" package (unlike the old Substance/
     * Insubstantial "laf-widget" jar, where the equivalent class was public
     * API) - it isn't part of Radiance's officially supported API surface,
     * but it is still a plain public class with a public no-arg accessor
     * and public getter/setter, reachable via reflection exactly like
     * before.
     */
    private static final String THEMING_INTERNAL_PKG = "org.pushingpixels.radiance.theming.internal.";

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
            String managerClassName = THEMING_INTERNAL_PKG + "AnimationConfigurationManager";
            Class managerClazz = Class.forName(managerClassName, true, cl);
            Method m = managerClazz.getMethod("getInstance", null);
            Object manager = m.invoke(null, null);
            m = managerClazz.getMethod("getTimelineDuration", null);
            Long millis = (Long) m.invoke(manager, null);
            speed = millis.longValue();
        }

        return speed;
    }

    /** @return Whether the currently installed LookAndFeel is a Radiance one. */
    public static boolean isRadianceInstalled() {
        return isARadianceLookAndFeel(UIManager.getLookAndFeel());
    }

    /**
     * Returns whether a given LookAndFeel is a Radiance LookAndFeel.
     *
     * @param laf The LookAndFeel.
     * @return Whether it is a Radiance LookAndFeel.
     * @see #isARadianceLookAndFeel(String)
     * @see #isRadianceInstalled()
     */
    public static boolean isARadianceLookAndFeel(LookAndFeel laf) {
        return isARadianceLookAndFeel(laf.getClass().getName());
    }

    /**
     * Returns whether a given LookAndFeel is a Radiance LookAndFeel.
     *
     * @param lafName The LookAndFeel's class name.
     * @return Whether it is a Radiance LookAndFeel.
     * @see #isARadianceLookAndFeel(LookAndFeel)
     * @see #isRadianceInstalled()
     */
    public static boolean isARadianceLookAndFeel(String lafName) {
        // Radiance's own skin-based LookAndFeel classes are all named
        // "Radiance<SkinName>LookAndFeel" (e.g. RadianceBusinessLookAndFeel),
        // exactly mirroring the old "Substance<SkinName>LookAndFeel"
        // pattern this check used to look for.
        return lafName.indexOf(".Radiance") > -1;
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
            String managerClassName = THEMING_INTERNAL_PKG + "AnimationConfigurationManager";
            Class managerClazz = Class.forName(managerClassName, true, cl);
            Method m = managerClazz.getMethod("getInstance", null);
            Object manager = m.invoke(null, null);
            m = managerClazz.getMethod("setTimelineDuration", new Class[]{long.class});
            m.invoke(manager, new Object[]{new Long(millis)});
        }
    }
}
