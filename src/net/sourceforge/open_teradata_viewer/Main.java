/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.awt.Font;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * Entry point for the application
 * 
 * @author <A HREF="mailto:nonametekno@gmail.com">D. Campione</A>
 *
 */
public class Main {

    public static final String APPLICATION_NAME = "Open Teradata Viewer";

    /** Prints a usage statement. */
    private static void printUsage() {
        System.err
                .println("Usage: java net.sourceforge.open_teradata_viewer.Main");
    }

    public static void main(final String[] args) {
        // Check if the used JDK is supported
        if (!Tools.isJDK16OrAbove()) {
            System.err.println("The installed JDK version is NOT supported.\n"
                    + "The program will be terminated.");
            System.exit(-1);
        }

        if (args.length > 0) {
            printUsage();
            System.exit(-2);
        }

        JFrame.setDefaultLookAndFeelDecorated(false);
        JDialog.setDefaultLookAndFeelDecorated(false);
        UIManager.put("TextArea.font",
                new Font(Font.MONOSPACED, Font.PLAIN, 12));
        // Turn off metal's use of bold fonts
        // UIManager.put("swing.boldMetal", Boolean.FALSE);

        ApplicationFrame mainWindow = new ApplicationFrame();
        SplashScreen splashScreen = new SplashScreen(mainWindow, 0);
        mainWindow.drawIt(splashScreen);

        mainWindow.setVisible(true);
        splashScreen.setVisible(false);
        splashScreen.dispose();
    }
}
