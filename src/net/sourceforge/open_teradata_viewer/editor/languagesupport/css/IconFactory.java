/*
 * Open Teradata Viewer ( editor language support css )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.css;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * The icons for CSS properties and values.
 *
 * @author D. Campione
 * 
 */
class IconFactory {

    private static IconFactory INSTANCE;

    private Map<String, Icon> iconMap;

    /** Private constructor to prevent instantiation. */
    private IconFactory() {
        iconMap = new HashMap<String, Icon>();
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return The singleton instance.
     */
    public static IconFactory get() {
        if (INSTANCE == null) {
            INSTANCE = new IconFactory();
        }
        return INSTANCE;
    }

    /**
     * Returns the icon requested.
     *
     * @param key The key for the icon.
     * @return The icon.
     */
    public Icon getIcon(String key) {
        Icon icon = iconMap.get(key);
        if (icon == null) {
            icon = loadIcon(key + ".gif");
            iconMap.put(key, icon);
        }
        return icon;
    }

    /**
     * Loads an icon by file name.
     *
     * @param name The icon file name.
     * @return The icon.
     */
    private Icon loadIcon(String name) {
        URL res = getClass().getResource("img/" + name);
        if (res == null) {
            // IllegalArgumentException is what would be thrown if res was null
            // anyway, we're just giving the actual arg name to make the message
            // more descriptive
            throw new IllegalArgumentException("icon not found: img/" + name);
        }
        return new ImageIcon(res);
    }
}