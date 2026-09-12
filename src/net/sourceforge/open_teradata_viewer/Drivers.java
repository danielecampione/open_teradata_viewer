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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import net.sourceforge.open_teradata_viewer.util.Utilities;
import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class Drivers {

    private static boolean initialized = false;

    /**
     * Class loader for any jars found by {@link #addAllJarsToClasspath(
     * String)} (JDBC driver or plugin jars dropped into the application's
     * working directory), used by {@link #loadCustomDrivers()} to load
     * driver classes that are not visible from the application's own
     * class loader. {@code null} until the first jar is found.
     *
     * @see DriverShim
     */
    private static URLClassLoader externalJarsClassLoader;

    private Drivers() {
    }

    /**
     * Returns the class loader that should be used to resolve anything
     * that might live in a jar dropped into the application's working
     * directory at runtime - not just JDBC drivers, but e.g. the Groovy
     * engine jars ({@code groovy-*.jar}, {@code groovy-jsr223-*.jar})
     * that {@code RunMacroAction} looks up via {@code
     * javax.script.ScriptEngineManager}.
     * <p>
     * On Java 8, such a jar used to be injected directly into {@link
     * ClassLoader#getSystemClassLoader()} (see the javadoc on {@link
     * #addAllJarsToClasspath(String)}), so simply using {@code
     * getClass().getClassLoader()} anywhere in the application was
     * enough to see it. Since that jar now instead lives in a separate,
     * dedicated {@link #externalJarsClassLoader}, any other code that
     * needs to resolve something from it - such as {@code
     * ServiceLoader}-based lookups like {@code ScriptEngineManager}'s -
     * must go through this method instead of using its own class's
     * class loader directly.
     *
     * @return {@link #externalJarsClassLoader} if at least one jar/zip
     *         was found under the working directory, or the application's
     *         own class loader otherwise (nothing extra to see, so any
     *         class loader in the application works just as well)
     */
    public static ClassLoader getRuntimeClassLoader() {
        return externalJarsClassLoader != null ? externalJarsClassLoader : Drivers.class.getClassLoader();
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
        File[] files = new File(Utilities.normalizePath(path)).listFiles((dir, fileName) -> {
            Vector<String> filesToIgnore = new Vector<>(1, 1);
            String currentFileInClassPath = null;
            StringTokenizer classesPaths = new StringTokenizer(
                    Utilities.normalizePath(System.getProperty("java.class.path")), ";");
            while (classesPaths.hasMoreTokens()) {
                StringTokenizer pathTokenizer = new StringTokenizer(classesPaths.nextToken(), "\\");
                while (pathTokenizer.hasMoreTokens()) {
                    currentFileInClassPath = pathTokenizer.nextToken();
                }
                if (fileName.equalsIgnoreCase(currentFileInClassPath)) {
                    filesToIgnore.add(currentFileInClassPath);
                }
            }

            // Locale-independent: an uppercase ".ZIP" extension must still
            // be recognized regardless of the active UI language.
            return !filesToIgnore.contains(fileName) && fileName.toLowerCase(Locale.ENGLISH).endsWith(".jar")
                    || fileName.toLowerCase(Locale.ENGLISH).endsWith(".zip");
        });

        URL[] urls = new URL[files.length];
        for (int i = 0; i < urls.length; i++) {
            urls[i] = files[i].toURI().toURL();
        }
        return urls;
    }

    /**
     * Makes every jar/zip found under {@code path} loadable, so that
     * {@link #loadCustomDrivers()} can subsequently resolve driver
     * classes that live in one of them.
     * <p>
     * On Java 8, this used to reflectively call {@code URLClassLoader
     * #addURL(URL)} on {@link ClassLoader#getSystemClassLoader()}, which
     * on that JVM was itself a {@code URLClassLoader}. As of Java 9, the
     * system class loader is an internal JDK class that no longer
     * extends {@code URLClassLoader}, so that call now fails with an
     * {@code IllegalArgumentException} ("object is not an instance of
     * declaring class") - previously masked by the no-op {@code catch}
     * in {@link ApplicationFrame#installPlugins()}, which silently
     * swallowed it, so driver/plugin jars dropped into the application's
     * working directory stopped being picked up at all, with no visible
     * error. A dedicated {@link URLClassLoader} is created instead; see
     * {@link DriverShim} for how {@link #loadCustomDrivers()} then makes
     * classes loaded through it usable with {@link DriverManager}.
     */
    static void addAllJarsToClasspath(String path) throws Exception {
        URL[] urls = retrieveAllJars(path);
        if (urls.length > 0) {
            externalJarsClassLoader = new URLClassLoader(urls, Drivers.class.getClassLoader());
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
                    loadAndRegisterDriver(driver);
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

    /**
     * Loads the given JDBC driver class and ensures it is registered
     * with {@link DriverManager}.
     * <p>
     * Drivers already visible from the application's own class loader
     * (e.g. {@code terajdbc4.jar}, always on OTV's build path) are
     * loaded exactly as on Java 8: {@code Class.forName} alone triggers
     * the driver's own JDBC 4 static initializer, which self-registers
     * it. Only when that fails is {@link #externalJarsClassLoader}
     * consulted, for a driver that exists solely in a jar dropped into
     * the application's working directory at runtime; such a class is
     * then wrapped in a {@link DriverShim} rather than registered as-is
     * - see that class' javadoc for why that wrapping is required.
     *
     * @param driverClassName the fully-qualified name of the driver
     *                        class
     */
    private static void loadAndRegisterDriver(String driverClassName) throws Exception {
        try {
            Class.forName(driverClassName);
            return;
        } catch (ClassNotFoundException cnfe) {
            if (externalJarsClassLoader == null) {
                throw notFoundException(driverClassName, cnfe);
            }
        }

        // Skip re-registering a shim for a driver that a previous call
        // already loaded and wrapped (e.g. the "Edit drivers" dialog
        // was confirmed more than once in this session).
        Enumeration<Driver> registeredDrivers = DriverManager.getDrivers();
        while (registeredDrivers.hasMoreElements()) {
            Driver registeredDriver = registeredDrivers.nextElement();
            if (registeredDriver instanceof DriverShim
                    && ((DriverShim) registeredDriver).getDelegateClassName().equals(driverClassName)) {
                return;
            }
        }

        try {
            Class<?> driverClass = Class.forName(driverClassName, true, externalJarsClassLoader);
            Driver driverInstance = (Driver) driverClass.getDeclaredConstructor().newInstance();
            DriverManager.registerDriver(new DriverShim(driverInstance));
        } catch (ClassNotFoundException cnfe) {
            throw notFoundException(driverClassName, cnfe);
        }
    }

    /**
     * Builds a {@link ClassNotFoundException} whose message tells the
     * user exactly where OTV looked for the given driver's jar - "class
     * not found" alone gives no clue that the fix is simply to drop the
     * jar directly into that folder (not a subfolder, and not {@code
     * lib/}), which is very easy to get wrong when adding a driver such
     * as Oracle's or MySQL's for the first time.
     */
    private static ClassNotFoundException notFoundException(String driverClassName, ClassNotFoundException cause) {
        String scannedDir = Utilities.normalizePath(".");
        return new ClassNotFoundException("Driver class \"" + driverClassName
                + "\" not found. Make sure its jar file is placed directly inside \"" + scannedDir
                + "\" (the application's current working directory - not a subfolder), then try again.", cause);
    }

    /**
     * Wraps a {@link Driver} instance loaded through {@link
     * #externalJarsClassLoader} so that {@link DriverManager} will
     * actually use it.
     * <p>
     * {@code DriverManager#getConnection} only considers a registered
     * driver whose class the <em>caller's</em> class loader - the
     * application's regular one, for every caller in OTV - can itself
     * resolve to that exact same {@code Class} object ({@code
     * DriverManager#isDriverAllowed}). A class private to a separate
     * {@code externalJarsClassLoader} never satisfies that, no matter
     * how it was registered, so the raw driver instance would silently
     * never be picked for a connection. This shim class, however, is
     * itself part of OTV and loaded by the application's own class
     * loader like everything else, so it always passes that check; it
     * simply delegates every call to the real driver it wraps. This is
     * the standard workaround for this well-known
     * {@code DriverManager} limitation.
     */
    private static final class DriverShim implements java.sql.Driver {

        private final Driver delegate;

        DriverShim(Driver delegate) {
            this.delegate = delegate;
        }

        /** @return the fully-qualified class name of the wrapped driver. */
        String getDelegateClassName() {
            return delegate.getClass().getName();
        }

        /** @return the jar/directory the wrapped driver was loaded from. */
        URL getDelegateCodeSourceLocation() {
            return delegate.getClass().getProtectionDomain().getCodeSource().getLocation();
        }

        @Override
        public java.sql.Connection connect(String url, java.util.Properties info) throws java.sql.SQLException {
            return delegate.connect(url, info);
        }

        @Override
        public boolean acceptsURL(String url) throws java.sql.SQLException {
            return delegate.acceptsURL(url);
        }

        @Override
        public java.sql.DriverPropertyInfo[] getPropertyInfo(String url, java.util.Properties info)
                throws java.sql.SQLException {
            return delegate.getPropertyInfo(url, info);
        }

        @Override
        public int getMajorVersion() {
            return delegate.getMajorVersion();
        }

        @Override
        public int getMinorVersion() {
            return delegate.getMinorVersion();
        }

        @Override
        public boolean jdbcCompliant() {
            return delegate.jdbcCompliant();
        }

        @Override
        public java.util.logging.Logger getParentLogger() throws java.sql.SQLFeatureNotSupportedException {
            return delegate.getParentLogger();
        }
    }

    public static int editDrivers() throws Exception {
        // Ensure all UI components are created on EDT
        final AtomicReference<JPanel> panelRef = new AtomicReference<>();
        final AtomicReference<JTextArea> driverfieldRef = new AtomicReference<>();
        final AtomicInteger responseRef = new AtomicInteger();
        
        try {
            SwingUtilities.invokeAndWait(() -> {
                try {
                    JPanel panel = new JPanel(new GridBagLayout());
                    GridBagConstraints c = new GridBagConstraints();
                    c.anchor = GridBagConstraints.WEST;
                    c.fill = GridBagConstraints.BOTH;
                    c.insets = new Insets(2, 2, 2, 2);
                    c.gridy++;
                    c.gridwidth = 3;
                    LanguageManager langManager = LanguageManager.getInstance();
                    
                    panel.add(new JLabel(
                            langManager.getString("drivers.location", new File(".").getCanonicalPath())),
                            c);
                    c.gridy++;
                    panel.add(new JLabel(" "), c);
                    c.gridy++;
                    panel.add(new JLabel(" "), c);
                    c.gridy++;
                    panel.add(new JSeparator(), c);
                    c.gridy++;
                    panel.add(new JLabel(" "), c);
                    c.gridy++;
                    panel.add(new JLabel(langManager.getString("drivers.loaded")), c);
                    c.gridy++;
                    panel.add(new JLabel(" "), c);
                    c.gridwidth = 1;
                    Enumeration<Driver> loadedDrivers = DriverManager.getDrivers();
                    while (loadedDrivers.hasMoreElements()) {
                        Driver loadedDriver = loadedDrivers.nextElement();
                        // Unwrap a DriverShim purely for display, so this
                        // dialog keeps showing the real driver's class
                        // name and jar, exactly as before that wrapping
                        // was introduced.
                        boolean isShim = loadedDriver instanceof DriverShim;
                        String driverClassName = isShim
                                ? ((DriverShim) loadedDriver).getDelegateClassName()
                                : loadedDriver.getClass().getName();
                        c.gridy++;
                        c.gridx = 0;
                        panel.add(new JLabel(driverClassName), c);
                        c.gridx++;
                        panel.add(
                                new JLabel(String.format("v%d.%d", loadedDriver.getMajorVersion(), loadedDriver.getMinorVersion())),
                                c);
                        c.gridx++;
                        try {
                            URL location = isShim
                                    ? ((DriverShim) loadedDriver).getDelegateCodeSourceLocation()
                                    : loadedDriver.getClass().getProtectionDomain().getCodeSource().getLocation();
                            String path = new File(location.toURI()).getName();
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
                    panel.add(new JLabel(langManager.getString("drivers.add_manual")), c);
                    c.gridy++;
                    panel.add(new JLabel(" "), c);
                    c.gridy++;
                    String drivers = Config.getDrivers();
                    JTextArea driverfield = new JTextArea(drivers, 4, 0);
                    panel.add(new JScrollPane(driverfield), c);
                    
                    panelRef.set(panel);
                    driverfieldRef.set(driverfield);
                    
                    int response = Dialog.show(langManager.getString("dialog.drivers"), panel, Dialog.PLAIN_MESSAGE, Dialog.OK_CANCEL_OPTION);
                    responseRef.set(response);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
        int response = responseRef.get();
        if (Dialog.OK_OPTION == response) {
            Config.saveDrivers(driverfieldRef.get().getText());
            try {
                initialized = false;
                initialize();
            } catch (ClassNotFoundException cnfe) {
                // Show the detailed message (now including where OTV
                // looked for the jar) instead of a generic one.
                ApplicationFrame.getInstance().getConsole().println(cnfe.getMessage(),
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