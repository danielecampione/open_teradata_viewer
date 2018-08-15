/*
 * Open Teradata Viewer ( editor language support js )
 * Copyright (C) 2014, D. Campione
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.EmptyIcon;

/**
 * Holds icons used by JavaScript auto-completion.
 * 
 * @author D. Campione
 * 
 */
public class IconFactory {

    public static final String FUNCTION_ICON = "function";
    public static final String LOCAL_VARIABLE_ICON = "local_variable";
    public static final String TEMPLATE_ICON = "template";
    public static final String EMPTY_ICON = "empty";
    public static final String GLOBAL_VARIABLE_ICON = "global_variable";
    public static final String DEFAULT_FUNCTION_ICON = "default_function";
    public static final String PUBLIC_STATIC_FUNCTION_ICON = "public_static_function";
    public static final String STATIC_VAR_ICON = "static_var";
    public static final String DEFAULT_VARIABLE_ICON = "default_variable";
    public static final String DEFAULT_CLASS_ICON = "default_class";
    public static final String JSDOC_ITEM_ICON = "jsdoc_item";

    private Map<String, Icon> iconMap;

    private static final IconFactory INSTANCE = new IconFactory();

    private IconFactory() {
        iconMap = new HashMap<String, Icon>();

        iconMap.put(FUNCTION_ICON, loadIcon("/icons/methpub_obj.gif"));
        iconMap.put(PUBLIC_STATIC_FUNCTION_ICON,
                loadIcon("/icons/methpub_static.gif"));
        iconMap.put(LOCAL_VARIABLE_ICON,
                loadIcon("/icons/localvariable_obj.gif"));
        iconMap.put(GLOBAL_VARIABLE_ICON,
                loadIcon("/icons/field_public_obj.gif"));
        iconMap.put(TEMPLATE_ICON, loadIcon("/icons/template_obj.gif"));
        iconMap.put(DEFAULT_FUNCTION_ICON, loadIcon("/icons/methdef_obj.gif"));
        iconMap.put(STATIC_VAR_ICON, loadIcon("/icons/static_co.gif"));
        iconMap.put(DEFAULT_VARIABLE_ICON,
                loadIcon("/icons/field_default_obj.gif"));
        iconMap.put(DEFAULT_CLASS_ICON, loadIcon("/icons/class_obj.gif"));
        iconMap.put(JSDOC_ITEM_ICON, loadIcon("/icons/jdoc_tag_obj.gif"));
        iconMap.put(EMPTY_ICON, new EmptyIcon(16));
    }

    private Icon getIconImage(String name) {
        return iconMap.get(name);
    }

    public static Icon getIcon(String name) {
        return INSTANCE.getIconImage(name);
    }

    public static String getEmptyIcon() {
        return EMPTY_ICON;
    }

    /**
     * Loads an icon.
     * 
     * @param name The file name of the icon to load.
     * @return The icon.
     */
    private Icon loadIcon(String name) {
        URL res = IconFactory.class.getResource(name);
        if (res == null) { // Never happens
            // IllegalArgumentException is what would be thrown if res was null
            // anyway, we're just giving the actual arg name to make the message
            // more descriptive
            throw new IllegalArgumentException("icon not found: " + name);
        }
        return new ImageIcon(res);
    }
}