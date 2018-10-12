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
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.sourceforge.open_teradata_viewer.util.StringUtil;
import net.sourceforge.open_teradata_viewer.util.SubstanceUtil;
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
        // Check if the used JDK is supported
        if (!Utilities.isJDK18OrAbove()) {
            System.err.println("The installed JDK version is NOT supported.\n" + "The program will be terminated.");
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

        // Swing stuff should always be done on the EDT..
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
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
                    ExceptionDialog.hideException(ncdfe);
                    ThirdPartyLookAndFeelManager.restoreSystemLookAndFeel();
                    String message = "The Look And Feel can't be installed.\n" + "Please restart the application.";
                    String title = "Look And Feel";
                    UISupport.getDialogs().showInfoMessage(message, title);
                    System.exit(-3);
                } catch (RuntimeException re) {
                    throw re;
                } catch (Throwable t) {
                    ExceptionDialog.hideException(t);
                }
                UIManager.put("TextArea.font", new Font(Font.MONOSPACED, Font.PLAIN, 12));

                // Allow Substance to paint window titles, etc.. We don't allow
                // Metal (for example) to do this, because setting these
                // properties to "true", then toggling to a LAF that doesn't
                // support this property, such as Windows, causes the
                // OS-supplied frame to not appear (as of JVM 6u20)
                lafName = UIManager.getLookAndFeel().getClass().getCanonicalName();
                if (SubstanceUtil.isASubstanceLookAndFeel(lafName)) {
                    JFrame.setDefaultLookAndFeelDecorated(true);
                    JDialog.setDefaultLookAndFeelDecorated(true);
                }

                // The default speed of Substance animations is too slow
                // (200ms), looks bad moving through JMenuItems quickly
                if (SubstanceUtil.isSubstanceInstalled()) {
                    try {
                        SubstanceUtil.setAnimationSpeed(100);
                    } catch (Exception e) {
                        ExceptionDialog.hideException(e);
                    }
                }

                setDefaultFontSize(14);

                Toolkit.getDefaultToolkit().setDynamicLayout(true);
                ApplicationFrame applicationFrame = new ApplicationFrame();
                applicationFrame.initLookAndFeelManager(lafManager);
                applicationFrame.drawIt();
            }
        });
    }

    /**
     * The method checks if the minimum Java version required for the selected
     * LAF is still compatible (the installed JVM can be changed or the minimum
     * Java version specified for the current LAF can be downgraded from the
     * last startup) and, if not, it temporary installs the default LAF.
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
            // the LAF. Substance is so high-maintenance
            UIManager.getLookAndFeelDefaults().put("ClassLoader", cl);
            Class<?> clazz = null;
            try {
                clazz = cl.loadClass(lafName);
            } catch (UnsupportedClassVersionError ucve) {
                // A LookAndFeel requiring Java X or later, but we're
                // now restarting with a Java version earlier than X
                lafName = UIManager.getSystemLookAndFeelClassName();
                clazz = cl.loadClass(lafName);
            }
            LookAndFeel laf = (LookAndFeel) clazz.newInstance();
            UIManager.setLookAndFeel(laf);
            UIManager.getLookAndFeelDefaults().put("ClassLoader", cl);
            UIUtil.installOsSpecificLafTweaks();
        }
    }

    public static void setDefaultFontSize(int size) {
        Set<Object> keySet = UIManager.getLookAndFeelDefaults().keySet();
        Object[] keys = keySet.toArray(new Object[keySet.size()]);

        for (Object key : keys) {
            if (key != null && key.toString().toLowerCase().contains("font")) {
                Font font = UIManager.getDefaults().getFont(key);
                if (font != null) {
                    font = font.deriveFont((float) size);
                    UIManager.put(key, font);
                }
            }
        }
    }
}