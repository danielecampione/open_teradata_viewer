/*
 * Open Teradata Viewer ( util )
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

package net.sourceforge.open_teradata_viewer;

import java.awt.Color;
import java.awt.Component;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.ButtonUI;

/**
 * Utility methods for applications looking to support WebLookAndFeel.
 * 
 * Utilities for using WebLookAndFeel. It's super nice, but like Substance, it
 * assumes you're building your application from the ground up with it, so we
 * have to tweak it somewhat to make it look OK with an application like Open
 * Teradata Viewer that uses "standard" Swing paradigms.<p>
 * 
 * Note that WebLookAndFeel is a little dangerous for applications to support,
 * since it uses lots of Sun-internal APIs and so may break at a moment's
 * notice.<p>
 * 
 * WebLookAndFeel also requires Java 6+, so everything is done by reflection.
 * Everything in this class works as of the WebLaF 1.4 preview release.
 *
 * @author D. Campione
 * 
 */
public class WebLookAndFeelUtil {

    private static final String LAF_CLASS_NAME = "com.alee.laf.WebLookAndFeel";
    private static final String BUTTON_UI_CLASS_NAME = "com.alee.laf.button.WebButtonUI";
    private static final String STYLE_CONSTANTS_CLASS = "com.alee.laf.StyleConstants";

    /**
     * Sets properties needed for toolbar buttons to look nice in
     * WebLookAndFeel.
     *
     * @param toolBar The toolbar to update.
     */
    public static void fixToolbarButtons(JToolBar toolBar) {
        try {
            for (int i = 0; i < toolBar.getComponentCount(); i++) {
                Component comp = toolBar.getComponent(i);
                System.out.println("--- " + comp);
                if (comp instanceof JButton) {
                    JButton button = (JButton) comp;
                    fixToolbarButtonImpl(button);
                }
            }
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
        }
    }

    private static final void fixToolbarButtonImpl(JButton button)
            throws Exception {
        ButtonUI ui = button.getUI();

        if (ui.getClass().getName().equals(BUTTON_UI_CLASS_NAME)) {
            Class clazz = ui.getClass();
            Class[] params = {boolean.class};
            Method m = clazz.getDeclaredMethod("setRolloverDecoratedOnly",
                    params);
            m.invoke(ui, new Object[]{Boolean.TRUE});
            params = new Class[]{int.class};
            m = clazz.getMethod("setRound", params);
            ClassLoader cl = clazz.getClassLoader();
            clazz = Class.forName(STYLE_CONSTANTS_CLASS, true, cl);
            Field smallRound = clazz.getField("smallRound");
            int value = smallRound.getInt(null);
            m.invoke(ui, new Object[]{new Integer(value)});
        }
    }

    public static final void installWebLookAndFeelProperties(ClassLoader cl) {
        // Don't override non-UIResource borders.
        // This is only in our custom build, based off of 1.6, for now
        String honorBorders = "WebLookAndFeel.honorUserBorders";
        System.setProperty(honorBorders, "true");

        try {
            // Decorating frames is disabled as for some reason it sets our
            // initial size to 0, which breaks STA's addNotify() method
            Class clazz = Class.forName(LAF_CLASS_NAME, true, cl);
            Class[] classes = {boolean.class};
            Method m = clazz.getDeclaredMethod("setDecorateDialogs", classes);
            m.invoke(null, new Object[]{Boolean.TRUE});

            clazz = Class.forName(STYLE_CONSTANTS_CLASS, true, cl);

            Field topBGColor = clazz.getDeclaredField("topBgColor");
            Field bottomBGColor = clazz.getDeclaredField("bottomBgColor");
            topBGColor.set(null, new Color(0xffefef));
            bottomBGColor.set(null, new Color(0xdfdfdf));
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
        }
    }

    public static final boolean isWebLookAndFeel(String laf) {
        return laf.equals(LAF_CLASS_NAME);
    }

    public static final boolean isWebLookAndFeelInstalled() {
        LookAndFeel laf = UIManager.getLookAndFeel();
        return isWebLookAndFeel(laf.getClass().getName());
    }
}