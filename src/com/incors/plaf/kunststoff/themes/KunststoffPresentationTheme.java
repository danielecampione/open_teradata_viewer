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

package com.incors.plaf.kunststoff.themes;

import java.awt.Font;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

/**
 * 
 * 
 * @author Christoph Wilhelms
 * 
 */
public class KunststoffPresentationTheme
        extends
            com.incors.plaf.kunststoff.KunststoffTheme {
    // primary colors
    private final ColorUIResource primary1 = new ColorUIResource(22, 22, 54);
    private final ColorUIResource primary2 = new ColorUIResource(110, 110, 130);
    private final ColorUIResource primary3 = new ColorUIResource(150, 150, 170);

    // secondary colors
    private final ColorUIResource secondary1 = new ColorUIResource(100, 100,
            100);
    private final ColorUIResource secondary2 = new ColorUIResource(130, 130,
            130);
    private final ColorUIResource secondary3 = new ColorUIResource(180, 180,
            180);
    //  private final ColorUIResource secondary3 = new ColorUIResource(224, 224, 224);

    // fonts
    private FontUIResource controlFont;
    private FontUIResource menuFont;
    private FontUIResource windowTitleFont;
    private FontUIResource monospacedFont;
    private FontUIResource textFont;

    /**
     * Crates this Theme
     */
    public KunststoffPresentationTheme() {
        menuFont = new FontUIResource("Tahoma", Font.PLAIN, 17);
        controlFont = new FontUIResource("Tahoma", Font.PLAIN, 16);
        textFont = new FontUIResource("Tahoma", Font.PLAIN, 14);
        windowTitleFont = new FontUIResource("Tahoma", Font.BOLD, 17);
        monospacedFont = new FontUIResource("Monospaced", Font.PLAIN, 15);
    }

    public String getName() {
        return "Presentation";
    }

    /**
     * The Font of Labels in many cases
     */
    public FontUIResource getControlTextFont() {
        return controlFont;
    }

    /**
     * The Font of Menus and MenuItems
     */
    public FontUIResource getMenuTextFont() {
        return menuFont;
    }

    /**
     * The Font of Nodes in JTrees
     */
    public FontUIResource getSystemTextFont() {
        return controlFont;
    }

    /**
     * The Font in TextFields, EditorPanes, etc.
     */
    public FontUIResource getUserTextFont() {
        return textFont;
    }

    /**
     * The Font of the Title of JInternalFrames
     */
    public FontUIResource getWindowTitleFont() {
        return windowTitleFont;
    }

    protected ColorUIResource getPrimary1() {
        return primary1;
    }

    protected ColorUIResource getPrimary2() {
        return primary2;
    }

    protected ColorUIResource getPrimary3() {
        return primary3;
    }

    protected ColorUIResource getSecondary1() {
        return secondary1;
    }

    protected ColorUIResource getSecondary2() {
        return secondary2;
    }

    protected ColorUIResource getSecondary3() {
        return secondary3;
    }

    public void addCustomEntriesToTable(UIDefaults table) {
        super.addCustomEntriesToTable(table);
        UIManager.getDefaults().put("PasswordField.font", monospacedFont);
        UIManager.getDefaults().put("TextArea.font", monospacedFont);
        UIManager.getDefaults().put("TextPane.font", monospacedFont);
        UIManager.getDefaults().put("EditorPane.font", monospacedFont);
    }
}
