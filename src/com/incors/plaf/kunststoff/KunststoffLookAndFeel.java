/*
 * Open Teradata Viewer ( look and feel )
 * Copyright (C) 2011, D. Campione
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

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;

/**
 * The main class for the Kunststoff Look&Feel.
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
public class KunststoffLookAndFeel extends MetalLookAndFeel {

    private static final long serialVersionUID = -2022166877246168939L;
    private static GradientTheme gradientTheme;
    private static boolean isInstalled = false;
    private static boolean themeHasBeenSet = false; // Thanks to Jonas Kilian for
                                                    // fixing the themes-bug

    public KunststoffLookAndFeel() {
        // the next line was removed by Jens Niemeyer, jens@jensn.de, because it would
        // cause a crash when using Sun Web Start
        // super();

        // install with the UIManager, if not done yet.
        if (!isInstalled) {
            UIManager.installLookAndFeel(new UIManager.LookAndFeelInfo(
                    "Kunststoff",
                    "com.incors.plaf.kunststoff.KunststoffLookAndFeel"));
            isInstalled = true;
        }
    }

    public String getID() {
        return "Kunststoff";
    }

    public String getName() {
        return "Kunststoff";
    }

    public String getDescription() {
        return "Look and Feel giving a plastic effect. Developed by INCORS GmbH, 2001"
                + "and contributors. Published under the Lesser GNU Public Licence.";
    }

    public boolean isNativeLookAndFeel() {
        return false;
    }

    public boolean isSupportedLookAndFeel() {
        return true;
    }

    protected void initClassDefaults(UIDefaults table) {
        super.initClassDefaults(table);
        putDefault(table, "ButtonUI");
        putDefault(table, "ToggleButtonUI");
        putDefault(table, "ComboBoxUI");
        putDefault(table, "TabbedPaneUI");
        putDefault(table, "TextFieldUI");
        putDefault(table, "PasswordFieldUI");
        putDefault(table, "ListUI");
        putDefault(table, "TreeUI");
        putDefault(table, "ToolBarUI");
        putDefault(table, "MenuBarUI");
        putDefault(table, "MenuUI");
        putDefault(table, "ScrollBarUI");
        putDefault(table, "ProgressBarUI");
        putDefault(table, "TableHeaderUI");
        putDefault(table, "InternalFrameUI");
        // if you want a check box icon with gradients, just remove the comment from
        // the following lines. We prefer the standard icon.
        /*
        putDefault(table, "CheckBoxUI");
        try {
          String className = "com.incors.plaf.kunststoff.KunststoffCheckBoxIcon";
          table.put("CheckBox.icon", className);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
        */
    }

    protected void putDefault(UIDefaults table, String uiKey) {
        try {
            String className = "com.incors.plaf.kunststoff.Kunststoff" + uiKey;
            table.put(uiKey, className);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void createDefaultTheme() {
        if (!themeHasBeenSet) {
            setCurrentTheme(new KunststoffTheme());
        }
        if (gradientTheme == null) {
            gradientTheme = new KunststoffGradientTheme();
        }
    }

    /**
     * Sets the theme that defines the colors for gradients.
     */
    public static void setCurrentGradientTheme(GradientTheme theme) {
        if (theme == null) {
            throw new NullPointerException(
                    "Gradient Theme cannot have null value");
        }
        gradientTheme = theme;
    }

    /**
     * Sets the current color theme. This works exactly as with the MetalLookAndFeel.
     * Note that for customizing the gradients the method setCurrentGradientTheme()
     * must be used.
     */
    public static void setCurrentTheme(MetalTheme theme) {
        MetalLookAndFeel.setCurrentTheme(theme);
        themeHasBeenSet = true;
    }

    protected void initSystemColorDefaults(UIDefaults table) {
        super.initSystemColorDefaults(table);
        // we made the color a bit darker because the were complaints about the color
        // being very difficult to see
        table.put("textHighlight", KunststoffUtilities
                .getTranslucentColorUIResource(getTextHighlightColor(), 128));
    }

    protected void initComponentDefaults(UIDefaults table) {
        super.initComponentDefaults(table);
        table.put("SplitPane.dividerSize", new Integer(8)); // will result in only one row of bumps
    }

    // ******** getter methods for the gradient colors *********

    /**
     * Returns the reflection color for a standard component (such as JButton).
     */
    public static ColorUIResource getComponentGradientColorReflection() {
        return gradientTheme.getComponentGradientColorReflection();
    }

    /**
     * Returns the shadow color for a standard component (such as JButton).
     */
    public static ColorUIResource getComponentGradientColorShadow() {
        return gradientTheme.getComponentGradientColorShadow();
    }

    /**
     * Returns the reflection color for a text component (such as JTextField).
     */
    public static ColorUIResource getTextComponentGradientColorReflection() {
        return gradientTheme.getTextComponentGradientColorReflection();
    }

    /**
     * Returns the reflection color for a text component (such as JTextField).
     */
    public static ColorUIResource getTextComponentGradientColorShadow() {
        return gradientTheme.getTextComponentGradientColorShadow();
    }

    /**
     * Returns the background shadow color for JList. In future we might also
     * use this color for the background of JTree.
     */
    public static int getBackgroundGradientShadow() {
        return gradientTheme.getBackgroundGradientShadow();
    }

}