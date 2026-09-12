/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.sourceforge.open_teradata_viewer.util.StringUtil;
import net.sourceforge.open_teradata_viewer.util.RadianceUtil;
import net.sourceforge.open_teradata_viewer.util.SwingUtil;
import net.sourceforge.open_teradata_viewer.util.UIUtil;
import net.sourceforge.open_teradata_viewer.util.Utilities;

/**
 * Entry point for the application.
 * 
 * @author <A HREF="mailto:nonametekno@gmail.com">D. Campione</A>
 *
 */
public class Main {

    public static final String APPLICATION_NAME = "Open Teradata Viewer";

    /** Prints a usage statement. */
    private static void printUsage() {
        System.err.println("Usage: java net.sourceforge.open_teradata_viewer.Main");
    }

    public static void main(final String[] args) {
        // Start of the anti-blur trick for hi-dpi
        // Declare the app as DPI-aware to avoid the OS's blurry scaling
        System.setProperty("sun.java2d.dpiaware", "true");
        // Let the JRE's own graphics pipeline apply per-monitor HiDPI
        // scaling where supported, instead of leaving everything at 1x and
        // relying only on our own manual font/size scaling
        // (SwingUtil.DPI_SCALE) to compensate
        System.setProperty("sun.java2d.uiScale.enabled", "true");
        // Force text antialiasing to have sharp and defined fonts
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        // End of the anti-blur makeup

        // Nashorn (used for JavaScript macros, see RunMacroAction) prints
        // "Warning: Nashorn engine is planned to be removed from a future
        // JDK release" to stderr the first time it's instantiated, on any
        // JDK 11+. This is the officially documented way to silence it
        // (JDK 11 release notes), and only affects that one console
        // message - Nashorn itself keeps working exactly the same
        System.setProperty("nashorn.args", "--no-deprecation-warning");

        // Check if the used JDK is supported
        if (!Utilities.isJDK11OrAbove()) {
            System.err.println(
                    "Java 11 or higher is required to run this application.\n" + "The program will be terminated.");
            System.exit(-1);
        }

        if (args.length > 0) {
            printUsage();
            System.exit(-2);
        }

        // Setting this property makes the menu appear on top of the screen on
        // Apple Mac OS X systems. It is ignored by all other Java
        // implementations
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        // Make Metal not use bold fonts
        UIManager.put("swing.boldMetal", Boolean.FALSE);

        // Catch any uncaught Throwables on the EDT and log them
        AWTExceptionHandler.register();

        // Initialize language manager before any UI components
        net.sourceforge.open_teradata_viewer.i18n.LanguageManager.getInstance();

        SwingUtilities.invokeLater(() -> {
            String lafName = UIManager.getSystemLookAndFeelClassName();

            try {
                String startupLookAndFeelProperty = "startup_lookandfeel_class";
                String strStartupLookAndFeelClassName = Config.getSetting(startupLookAndFeelProperty);
                if (StringUtil.isEmpty(strStartupLookAndFeelClassName)) {
                    Config.saveSetting(startupLookAndFeelProperty, lafName);
                } else {
                    lafName = strStartupLookAndFeelClassName;
                }
            } catch (Exception e) {
                ExceptionDialog.hideException(e);
            }

            String rootDir = Utilities.getRootDir();
            ThirdPartyLookAndFeelManager lafManager = new ThirdPartyLookAndFeelManager(rootDir + File.separator);

            try {
                installACompatibleLaf(lafManager, lafName);
            } catch (ClassNotFoundException cnfe) {
                ExceptionDialog.hideException(cnfe);
                ThirdPartyLookAndFeelManager.restoreSystemLookAndFeel();
            } catch (UnsupportedLookAndFeelException ulafe) {
                ExceptionDialog.hideException(ulafe);
                ThirdPartyLookAndFeelManager.restoreSystemLookAndFeel();
            } catch (IllegalAccessException iae) {
                ExceptionDialog.hideException(iae);
                ThirdPartyLookAndFeelManager.restoreSystemLookAndFeel();
            } catch (InstantiationException ie) {
                ExceptionDialog.hideException(ie);
                ThirdPartyLookAndFeelManager.restoreSystemLookAndFeel();
            } catch (IllegalStateException ise) {
                ExceptionDialog.hideException(ise);
                ThirdPartyLookAndFeelManager.restoreSystemLookAndFeel();
            } catch (NoClassDefFoundError ncdfe) { // For example, the JGoodies Looks library is unavailable
                // NOTE: this used to show a blocking dialog and then call
                // System.exit(), forcing the user to restart the app (and,
                // since the broken LAF's class name was still the one
                // saved to config at that point, restart into the very
                // same failure again and again, until they went and
                // edited open_teradata_viewer_lookandfeels.xml by hand).
                // restoreSystemLookAndFeel() below already fixes the saved
                // setting for the *next* restart - there's no reason not
                // to just let this one carry on with whatever LAF is
                // already active (the JVM's own built-in default, since
                // the failed LAF never got installed), exactly like every
                // other case caught here.
                ExceptionDialog.hideException(ncdfe);
                ThirdPartyLookAndFeelManager.restoreSystemLookAndFeel();
            } catch (RuntimeException re) {
                throw re;
            } catch (Throwable t) {
                // NOTE: a LookAndFeel that is no longer compatible with the
                // running JVM (e.g. an old Substance/Insubstantial skin on
                // Java 9+) typically fails with some flavor of LinkageError
                // not explicitly named above (NoSuchMethodError,
                // ExceptionInInitializerError, etc.), which used to end up
                // here without ever correcting the saved LAF setting -
                // meaning every subsequent restart tried, and failed on,
                // the very same broken LAF again. Restoring the system
                // default here too, exactly like every other case above,
                // makes recovery automatic instead of requiring a manual
                // edit of open_teradata_viewer_lookandfeels.xml.
                ExceptionDialog.hideException(t);
                ThirdPartyLookAndFeelManager.restoreSystemLookAndFeel();
            }
            UIManager.put("TextPane.font", new Font(Font.MONOSPACED, Font.PLAIN, 13));
            UIManager.put("TextArea.font", new Font(Font.MONOSPACED, Font.PLAIN, 13));

            // Some Look & Feels define a noticeably smaller default font for
            // labels, trees, buttons, toggle buttons, text fields, combo
            // boxes, lists, checkboxes and radio buttons than for
            // menu items - most visible now that Java 11 renders every one
            // of them at its true, correctly DPI-scaled size (a DPI-unaware
            // Java 8 runtime rendered everything uniformly undersized,
            // which masked the difference). Bring them all up to the menu
            // item font's size for visual consistency, without touching
            // anything if a given LAF doesn't have this discrepancy in the
            // first place
            Font menuItemFont = UIManager.getFont("MenuItem.font");
            if (menuItemFont != null) {
                String[] keysToMatchMenuItemFont = { "Label.font", "Tree.font", "Button.font", "TextField.font",
                        "ComboBox.font", "List.font", "ToggleButton.font", "CheckBox.font", "RadioButton.font",
                        "Table.font", "TableHeader.font" };
                for (String key : keysToMatchMenuItemFont) {
                    Font font = UIManager.getFont(key);
                    if (font != null && font.getSize() < menuItemFont.getSize()) {
                        UIManager.put(key, font.deriveFont((float) menuItemFont.getSize()));
                    }
                }
            }

            // Allow Radiance to paint window titles, etc.. We don't allow
            // Metal (for example) to do this, because setting these
            // properties to "true", then toggling to a LAF that doesn't
            // support this property, such as Windows, causes the
            // OS-supplied frame to not appear (as of JVM 6u20)
            lafName = UIManager.getLookAndFeel().getClass().getCanonicalName();
            if (RadianceUtil.isARadianceLookAndFeel(lafName)) {
                JFrame.setDefaultLookAndFeelDecorated(true);
                JDialog.setDefaultLookAndFeelDecorated(true);
            }

            // The default speed of Radiance animations is too slow
            // (200ms), looks bad moving through JMenuItems quickly
            if (RadianceUtil.isRadianceInstalled()) {
                try {
                    RadianceUtil.setAnimationSpeed(100);
                } catch (Exception e) {
                    ExceptionDialog.hideException(e);
                }
            }

            SwingUtil.scaleAllFonts();

            Toolkit.getDefaultToolkit().setDynamicLayout(true);
            ApplicationFrame applicationFrame = new ApplicationFrame();
            applicationFrame.initLookAndFeelManager(lafManager);
            applicationFrame.drawIt();
        });
    }

    /**
     * The method checks if the minimum Java version required for the selected LAF
     * is still compatible (the installed JVM can be changed or the minimum Java
     * version specified for the current LAF can be downgraded from the last
     * startup) and, if not, it temporary installs the default LAF.
     */
    private static void installACompatibleLaf(ThirdPartyLookAndFeelManager lafManager, String lafName)
            throws Throwable {
        boolean systemLaf = false;
        boolean compatibleThirdPartyLaf = false;
        UIManager.LookAndFeelInfo[] lafsInfo = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo lafInfo : lafsInfo) {
            if (lafName.trim().equals(lafInfo.getClassName().trim())) {
                systemLaf = true;
                break;
            }
        }
        if (!systemLaf) {
            ExtendedLookAndFeelInfo[] extendedLookAndFeelsInfo = lafManager.get3rdPartyLookAndFeelInfo();
            for (ExtendedLookAndFeelInfo extendedLookAndFeelInfo : extendedLookAndFeelsInfo) {
                if (lafName.trim().equals(extendedLookAndFeelInfo.getClassName().trim())) {
                    compatibleThirdPartyLaf = true;
                    break;
                }
            }
        }
        if (systemLaf || compatibleThirdPartyLaf) {
            ClassLoader cl = lafManager.getLAFClassLoader();
            // Set these properties before instantiating WebLookAndFeel
            if (WebLookAndFeelUtil.isWebLookAndFeel(lafName)) {
                WebLookAndFeelUtil.installWebLookAndFeelProperties(cl);
            }
            // Must set UIManager's ClassLoader before instantiating
            // the LAF. Radiance is so high-maintenance
            UIManager.getLookAndFeelDefaults().put("ClassLoader", cl);
            // NOTE: this used to load the LAF class and instantiate it
            // manually (cl.loadClass(...) + Constructor#newInstance()),
            // which - for any JDK built-in LAF living in a package
            // java.desktop does not export, such as WindowsLookAndFeel -
            // triggers an "illegal reflective access" warning on every
            // single startup (JDK-8136366: those packages were never
            // exported/opened to unnamed modules). UIManager.
            // setLookAndFeel(String) achieves the exact same result
            // without that warning: its own reflective instantiation
            // happens *inside* java.desktop itself, so accessing another
            // java.desktop-internal class from there is ordinary
            // intra-module access, never cross-module reflection.
            // Temporarily pointing the context class loader at the LAF
            // class loader makes UIManager.setLookAndFeel(String) - which
            // resolves the class via Thread.currentThread().
            // getContextClassLoader() - still find third-party LAF jars
            // (Radiance, Kunststoff, Liquid, ...) exactly as before.
            ClassLoader previousContextCl = Thread.currentThread().getContextClassLoader();
            Thread.currentThread().setContextClassLoader(cl);
            try {
                try {
                    UIManager.setLookAndFeel(lafName);
                } catch (UnsupportedClassVersionError ucve) {
                    // A LookAndFeel requiring Java X or later, but we're
                    // now restarting with a Java version earlier than X
                    lafName = UIManager.getSystemLookAndFeelClassName();
                    UIManager.setLookAndFeel(lafName);
                }
            } finally {
                Thread.currentThread().setContextClassLoader(previousContextCl);
            }
            UIManager.getLookAndFeelDefaults().put("ClassLoader", cl);
            UIUtil.installOsSpecificLafTweaks();
        }
    }
}