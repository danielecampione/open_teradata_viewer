/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

import net.sourceforge.open_teradata_viewer.util.Utilities;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class Drivers {

    private static boolean initialized = false;

    private Drivers() {
    }

    public static void initialize() throws Exception {
        if (!initialized) {
            addAllJarsToClasspath(".");
            try {
                loadCustomDrivers();
            } catch (ClassNotFoundException cnfe) {
                throw cnfe;
            }
            initialized = true;
        }
    }

    static URL[] retrieveAllJars(String path) throws MalformedURLException {
        File[] files = new File(Utilities.conformizePath(path))
                .listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String fileName) {
                        Vector<String> filesToIgnore = new Vector<String>(1, 1);
                        String currentFileInClassPath = null;
                        StringTokenizer classesPaths = new StringTokenizer(
                                Utilities.conformizePath(System
                                        .getProperty("java.class.path")), ";");
                        while (classesPaths.hasMoreTokens()) {
                            StringTokenizer pathTokenizer = new StringTokenizer(
                                    classesPaths.nextToken(), "\\");
                            while (pathTokenizer.hasMoreTokens()) {
                                currentFileInClassPath = pathTokenizer
                                        .nextToken();
                            }
                            if (fileName
                                    .equalsIgnoreCase(currentFileInClassPath)) {
                                filesToIgnore.add(currentFileInClassPath);
                            }
                        }

                        return !filesToIgnore.contains(fileName)
                                && fileName.toLowerCase().endsWith(".jar")
                                || fileName.toLowerCase().endsWith(".zip");
                    }
                });

        URL[] urls = new URL[files.length];
        for (int i = 0; i < urls.length; i++) {
            urls[i] = files[i].toURI().toURL();
        }
        return urls;
    }

    static void addAllJarsToClasspath(String path) throws Exception {
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        Method addURL = URLClassLoader.class.getDeclaredMethod("addURL",
                URL.class);
        addURL.setAccessible(true);
        URL[] urls = retrieveAllJars(path);
        for (URL url1 : urls) {
            addURL.invoke(systemClassLoader, url1);
        }
    }

    private static void loadCustomDrivers() throws Exception {
        String drivers = Config.getDrivers();
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(drivers);
        scanner.useDelimiter("[\\s,;]+");
        while (scanner.hasNext()) {
            String driver = scanner.next();
            if (stringBuilder.indexOf(driver) == -1) {
                try {
                    Class.forName(driver);
                    stringBuilder.append(driver);
                    stringBuilder.append("\n");
                } catch (ClassNotFoundException cnfe) {
                    initialized = false;
                    scanner.close();
                    throw cnfe;
                }
            }
        }
        Config.saveDrivers(stringBuilder.toString());
        scanner.close();
    }

    public static int editDrivers() throws Exception {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(2, 2, 2, 2);
        c.gridy++;
        c.gridwidth = 3;
        panel.add(
                new JLabel(String.format(
                        "The JDBC driver jar files are located in \"%s\".",
                        new File(".").getCanonicalPath())), c);
        c.gridy++;
        panel.add(new JLabel(" "), c);
        c.gridy++;
        panel.add(new JLabel(" "), c);
        c.gridy++;
        panel.add(new JSeparator(), c);
        c.gridy++;
        panel.add(new JLabel(" "), c);
        c.gridy++;
        panel.add(new JLabel("Currently loaded drivers:"), c);
        c.gridy++;
        panel.add(new JLabel(" "), c);
        c.gridwidth = 1;
        Enumeration<Driver> loadedDrivers = DriverManager.getDrivers();
        while (loadedDrivers.hasMoreElements()) {
            Driver loadedDriver = loadedDrivers.nextElement();
            c.gridy++;
            c.gridx = 0;
            panel.add(new JLabel(loadedDriver.getClass().getName()), c);
            c.gridx++;
            panel.add(
                    new JLabel(String.format("v%d.%d",
                            loadedDriver.getMajorVersion(),
                            loadedDriver.getMinorVersion())), c);
            c.gridx++;
            try {
                URI uri = loadedDriver.getClass().getProtectionDomain()
                        .getCodeSource().getLocation().toURI();
                String path = new File(uri).getName();
                panel.add(new JLabel(path), c);
            } catch (Exception e) {
                panel.add(new JLabel(), c);
            }
        }
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy++;
        panel.add(new JLabel(" "), c);
        c.gridy++;
        panel.add(new JSeparator(), c);
        c.gridy++;
        panel.add(new JLabel(" "), c);
        c.gridy++;
        panel.add(new JLabel(
                "Add any driver that couldn't automatically be loaded to the list below,"
                        + " separated by a comma or a new line."), c);
        c.gridy++;
        panel.add(new JLabel(" "), c);
        c.gridy++;
        String drivers = Config.getDrivers();
        JTextArea driverfield = new JTextArea(drivers, 4, 0);
        panel.add(new JScrollPane(driverfield), c);
        int response = Dialog.show("Drivers", panel, Dialog.PLAIN_MESSAGE,
                Dialog.OK_CANCEL_OPTION);
        if (Dialog.OK_OPTION == response) {
            Config.saveDrivers(driverfield.getText());
            try {
                initialized = false;
                initialize();
            } catch (ClassNotFoundException cnfe) {
                // Show the error only in the text area
                ApplicationFrame
                        .getInstance()
                        .getConsole()
                        .println("Driver NOT found.",
                                ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);

            }
        }
        return response;
    }

    public static void setInitialized(boolean initialized) {
        Drivers.initialized = initialized;
    }

    public static boolean hasBeenInitialized() {
        return initialized;
    }
}