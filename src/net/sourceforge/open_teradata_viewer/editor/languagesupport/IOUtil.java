/*
 * Open Teradata Viewer ( editor language support )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * I/O related utility methods.
 *
 * @author D. Campione
 * 
 */
public class IOUtil {

    private static Map<String, String> DEFAULT_ENV;

    /** Private constructor to prevent instantiation. */
    private IOUtil() {
    }

    /**
     * Gets the environment of the current process. Works with Java 1.4 as well
     * as 1.5+.
     *
     * @return A mapping of environment variable names to values.
     */
    private static Map<String, String> getDefaultEnvMap() {
        // If we've already created it..
        if (DEFAULT_ENV != null) {
            return DEFAULT_ENV;
        }

        // In Java 5+, we can just get the environment directly
        try {
            DEFAULT_ENV = System.getenv();
        } catch (SecurityException e) { // In an applet perhaps?
            DEFAULT_ENV = Collections.emptyMap();
        }

        return DEFAULT_ENV;
    }

    /**
     * Returns the value of an environment variable. This method is here so we
     * don't get an exception when calling <tt>System.getenv()</tt> in Java 1.4
     * (which we don't support anyway).
     *
     * @param var The environment variable.
     * @return The value of the variable, or <code>null</code> if it is not
     *         defined.
     */
    public static String getEnvSafely(String var) {
        String value = null;
        try {
            value = System.getenv(var);
        } catch (SecurityException e) { // In an applet perhaps?
            // Swallow
        }
        return value;
    }

    /**
     * Returns the environment of the current process, with some variables
     * possibly added/overwritten. This method works even with Java 1.4.
     *
     * @param toAdd The environment variables to add/overwrite in the returned
     *        array. This array should have an even length, with even indices
     *        containing variable names and odd indices containing the variable
     *        values.
     * @return The environment variables. This array's entries will be of the
     *         form "<code>name=value</code>", so it can be passed directly into
     *         <code>Runtime.exec()</code>.
     */
    public static String[] getEnvironmentSafely(String[] toAdd) {
        Map<String, String> env = getDefaultEnvMap();

        // Put any vars they want to explicitly specify
        if (toAdd != null) {
            Map<String, String> temp = new HashMap<String, String>(env);
            for (int i = 0; i < toAdd.length; i += 2) {
                temp.put(toAdd[i], toAdd[i + 1]);
            }
            env = temp;
        }

        // Create an array of "name=value" items, like Runtime.exec() wants
        int count = env.size();
        String[] vars = new String[count];
        int i = 0;
        for (Map.Entry<String, String> entry : env.entrySet()) {
            vars[i++] = entry.getKey() + "=" + entry.getValue();
        }

        return vars;
    }

    /**
     * Runs a process, possibly capturing its stdout and/or stderr.
     *
     * @param p The process.
     * @param stdout A buffer in which to put stdout, or <code>null</code> if
     *        you don't want to keep it.
     * @param stderr A buffer in which to keep stderr, or <code>null</code> if
     *        you don't want to keep it.
     * @return The return code of the process.
     * @throws IOException If an IO error occurs.
     */
    public static int waitForProcess(Process p, StringBuffer stdout,
            StringBuffer stderr) throws IOException {
        InputStream in = p.getInputStream();
        InputStream err = p.getErrorStream();
        Thread t1 = new Thread(new OutputCollector(in, stdout));
        Thread t2 = new Thread(new OutputCollector(err, stderr));
        t1.start();
        t2.start();
        int rc = -1;

        try {
            rc = p.waitFor();
            t1.join();
            t2.join();
        } catch (InterruptedException ie) {
            p.destroy();
        } finally {
            in.close();
            err.close();
        }

        return rc;
    }
}