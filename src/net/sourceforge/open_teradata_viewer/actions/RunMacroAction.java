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

package net.sourceforge.open_teradata_viewer.actions;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URI;
import java.text.MessageFormat;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ApplicationMenuBar;
import net.sourceforge.open_teradata_viewer.Drivers;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.Main;
import net.sourceforge.open_teradata_viewer.editor.macros.Macro;
import net.sourceforge.open_teradata_viewer.editor.macros.MacroManager;

/**
 *
 *
 * @author D. Campione
 *
 */
public class RunMacroAction extends CustomAction {

    private static final long serialVersionUID = -6087118542339474647L;

    /** The macro to run. */
    private Macro macro;

    /** The cached bindings instance. */
    private Bindings bindings;

    /**
     * The script engine for JavaScript, shared across all instances of this
     * action.
     */
    private static ScriptEngine jsEngine;

    /**
     * The script engine for Groovy, shared across all instances of this
     * action.
     */
    private static ScriptEngine groovyEngine;

    public RunMacroAction(Macro macro) {
        super(macro.getName(), null, KeyStroke.getKeyStroke(macro.getAccelerator()), null);
        this.macro = macro;

        setEnabled(true);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        handleSubmit(macro);
    }

    /**
     * Returns the directory OTV is actually running from (i.e. the
     * directory containing the application's jar file, or the output/
     * "bin" directory when running unpacked from within an IDE). This is
     * where OTV expects optional dependency jars, such as the embeddable
     * Groovy jar, to be placed.
     * <p>
     * This is deliberately <b>not</b> based on the JVM's current working
     * directory ("user.dir"): on Linux desktop environments (e.g. Debian
     * 13 "trixie"), GUI application launchers commonly start the JVM with
     * the current working directory set to the user's home directory
     * rather than to the application's installation directory. Relying on
     * "user.dir" therefore previously caused the Groovy jar lookup to fail
     * even when the jar was correctly placed alongside the application.
     *
     * @return The application's installation directory, or the JVM's
     *         current working directory as a fallback if the former
     *         cannot be determined.
     */
    private File getApplicationInstallDirectory() {
        try {
            URI codeSourceUri = RunMacroAction.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            // When running from a jar, this location is the jar file
            // itself, so its parent is the installation directory. When
            // running unpacked (e.g. from within an IDE), this location is
            // already the output directory, whose parent is the project's
            // root directory - which is also where dependency jars are
            // expected to be placed during development
            return new File(codeSourceUri).getParentFile();
        } catch (Exception e) {
            return new File(System.getProperty("user.dir"));
        }
    }

    private void handleSubmit(Macro macro) {
        File file = new File(macro.getFile());
        if (!file.isFile()) {
            String text = "The script associated with this macro no longer exists:\n\n{0}\n\nDo you want to remove this macro?";
            text = MessageFormat.format(text, file.getAbsolutePath());
            String title = "An error occured";
            ApplicationFrame app = ApplicationFrame.getInstance();
            final String finalText = text;
            final String finalTitle = title;
            final int[] rc = {JOptionPane.NO_OPTION};
            try {
                SwingUtilities.invokeAndWait(() -> {
                    rc[0] = JOptionPane.showConfirmDialog(app, finalText, finalTitle,
                            JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                });
            } catch (Exception ex) {
                ExceptionDialog.hideException(ex);
                return;
            }
            if (rc[0] == JOptionPane.YES_OPTION) {
                MacroManager.get().removeMacro(macro);
                ApplicationMenuBar menuBar = app.getApplicationMenuBar();
                menuBar.refreshMacrosMenu();
            }
            return;
        }

        try (BufferedReader r = new BufferedReader(new FileReader(file))) {
            handleSubmit(file.getName(), r);
        } catch (Throwable t) {
            ExceptionDialog.showException(t);
        }
    }

    private void handleSubmit(String sourceName, BufferedReader r) throws Throwable {
        ApplicationFrame app = ApplicationFrame.getInstance();

        ScriptEngine engine = null;
        if (sourceName.endsWith(".js")) {
            engine = initJavaScriptEngine();
            if (engine == null) { // An error message was already displayed
                return;
            }
        } else if (sourceName.endsWith(".groovy")) {
            engine = initGroovyEngine();
            if (engine == null) { // An error message was already displayed
                return;
            }
        } else {
            ExceptionDialog.showException(new Exception("Bad macro type: " + sourceName));
            return;
        }

        // Create our bindings and cache them for later
        bindings = engine.createBindings();
        engine.setBindings(bindings, ScriptContext.ENGINE_SCOPE);

        // We always reset the value of "app" and "textArea", but
        // all other variables they've modified are persistent
        bindings.put("app", app);
        bindings.put("textArea", app.getTextComponent());

        // Script execution MUST happen on the EDT: the macro may create
        // Swing components (e.g. JPopupMenu) and Radiance enforces EDT-only
        // component creation.
        final ScriptEngine finalEngine = engine;
        final Throwable[] evalError = {null};
        SwingUtilities.invokeAndWait(() -> {
            try {
                finalEngine.eval(r);
            } catch (Throwable t) {
                evalError[0] = t;
            }
        });
        if (evalError[0] != null) {
            throw evalError[0];
        }
    }

    /**
     * Returns the Groovy engine, lazily creating it, if necessary.
     *
     * @return The script engine, or <code>null</code> if it cannot be created.
     */
    private ScriptEngine initGroovyEngine() {
        if (groovyEngine == null) {
            groovyEngine = initScriptEngineImpl("Groovy", this::showMissingGroovyJarsMessage);
        }
        return groovyEngine;
    }

    /**
     * Returns the JS engine, lazily creating it if necessary.
     *
     * @return The script engine, or <code>null</code> if it cannot be created.
     */
    private ScriptEngine initJavaScriptEngine() {
        if (jsEngine == null) {
            jsEngine = initScriptEngineImpl("JavaScript", null);
        }
        return jsEngine;
    }

    /**
     * Looks up and initializes the given script engine.
     *
     * @param shortName the engine's JSR 223 short name (e.g. "Groovy" or
     *                  "JavaScript")
     * @param onNotFound invoked instead of {@link #showLoadingEngineError(
     *                   String)} when the engine cannot be found, so a
     *                   caller can show a more specific, actionable
     *                   message; may be {@code null} to use the generic
     *                   one
     * @return The script engine, or <code>null</code> if it cannot be created.
     */
    private ScriptEngine initScriptEngineImpl(String shortName, Runnable onNotFound) {
        ScriptEngine engine = null;

        try {
            // NOTE: on Java 8, a jar dropped into the application's
            // working directory (e.g. groovy-3.0.25.jar and
            // groovy-jsr223-3.0.25.jar, providing the "Groovy" engine)
            // used to be injected straight into the system class loader,
            // so this.getClass().getClassLoader() already saw it. As of
            // Java 9, such jars instead live in a separate class loader
            // (see Drivers#addAllJarsToClasspath), invisible from here
            // unless resolved through Drivers.getRuntimeClassLoader()
            // instead - see that method's javadoc for the full story.
            ScriptEngineManager sem = new ScriptEngineManager(Drivers.getRuntimeClassLoader());
            engine = sem.getEngineByName(shortName);
            if (engine == null) {
                if (onNotFound != null) {
                    onNotFound.run();
                } else {
                    showLoadingEngineError(shortName);
                }
                return null;
            }

            // Write stdout and stderr to this console. Must wrap these in
            // PrintWriters for standard print() and println() methods to work
            ScriptContext context = engine.getContext();
            PrintWriter w = new PrintWriter(new OutputStreamWriter(System.out));
            context.setWriter(w);
            w = new PrintWriter(new OutputStreamWriter(System.err));
            context.setErrorWriter(w);
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
        }

        return engine;
    }

    /**
     * Displays an error dialog stating that an unknown error occurred
     * loading the scripting engine.
     *
     * @param engine The name of the engine we tried to load.
     */
    private void showLoadingEngineError(String engine) {
        String message = "Script engine not found: " + engine;
        try {
            SwingUtilities.invokeAndWait(() -> {
                JOptionPane.showMessageDialog(ApplicationFrame.getInstance(),
                        message, "An error occured", JOptionPane.ERROR_MESSAGE);
            });
        } catch (Exception ex) {
            ExceptionDialog.hideException(ex);
        }
    }

    /**
     * Displays an error dialog telling the user where to place Groovy's
     * engine jars, shown when no "Groovy" engine could be found at all.
     * <p>
     * Deliberately does not name a single specific jar file: Groovy no
     * longer ships as one "groovy-all" uber-jar (that packaging was
     * dropped after the 2.5.x line), so - unlike a hardcoded single file
     * name check, which broke the very first time a different jar/
     * version combination was used - this message, like the check it
     * follows from, stays correct regardless of which Groovy jars or
     * version the user drops in.
     */
    private void showMissingGroovyJarsMessage() {
        String message = "In order to run Groovy macros, place a copy of Groovy's engine jars\n"
                + "(e.g. groovy-<version>.jar and groovy-jsr223-<version>.jar) in this\nlocation:\n\n{0}\n\nRestarting "
                + Main.APPLICATION_NAME + " will also be required.";
        message = MessageFormat.format(message, getApplicationInstallDirectory().getAbsolutePath());
        final String finalMessage = message;
        try {
            SwingUtilities.invokeAndWait(() -> {
                JOptionPane.showMessageDialog(ApplicationFrame.getInstance(),
                        finalMessage, "An error occured", JOptionPane.ERROR_MESSAGE);
            });
        } catch (Exception ex) {
            ExceptionDialog.hideException(ex);
        }
    }
}