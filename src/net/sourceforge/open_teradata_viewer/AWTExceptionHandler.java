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

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Catches uncaught exceptions thrown in the EDT. This is "magic" that works in
 * at least 1.6 in ORACLE JVMs.
 * 
 * To use this class, call <code>AWTExceptionHandler.register()</code>.
 *
 * @author D. Campione
 * 
 */
public class AWTExceptionHandler {

    private static Logger logger;
    private FileHandler fileHandler;

    public AWTExceptionHandler() {
        if (logger != null) {
            logger = Logger.getLogger("net.sourceforge.open_teradata_viewer");
            try {
                fileHandler = new FileHandler(
                        "%h/uncaughtOpenTeradataViewerAwtExceptions.log", true);
                logger.addHandler(fileHandler);
            } catch (IOException ioe) {
                ExceptionDialog.hideException(ioe);
            }
        }
    }

    /**
     * Callback for whenever an uncaught Throwable is thrown on the EDT.
     *
     * @param t the uncaught Throwable.
     */
    public void handle(Throwable t) {
        try {
            ExceptionDialog.hideException(t);
            if (logger != null) {
                logger.log(Level.SEVERE, "Uncaught exception in EDT", t);
            }
        } catch (Throwable t2) {
            // Don't let the exception get thrown out, will cause infinite
            // looping
        }
    }

    /** Call this method to register this exception handler with the EDT. */
    public static void register() {
        System.setProperty("sun.awt.exception.handler",
                AWTExceptionHandler.class.getName());
    }

    /**
     * Cleans up this exception handler. This should be called when the
     * application shuts down.
     */
    public static void shutdown() {
        if (logger != null) {
            // Manually close FileHandlers to remove .lck files
            Handler[] handlers = logger.getHandlers();
            for (int i = 0; i < handlers.length; i++) {
                handlers[i].close();
            }
            logger = null;
        }
    }
}