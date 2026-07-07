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

    private File getGroovyJar() {
        return new File("groovy-all-3.0.0-alpha-1.jar");
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
                javax.swing.SwingUtilities.invokeAndWait(() -> {
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
        // Swing components (e.g. JPopupMenu) and Substance enforces EDT-only
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
        File groovyJar = getGroovyJar();
        if (groovyJar == null || !groovyJar.isFile()) {
            String message = "In order to run Groovy macros, place a copy of the embeddable\nGroovy jar in this location:\n\n{0}\n\nRestarting "
                    + Main.APPLICATION_NAME + " will also be required.";
            message = MessageFormat.format(message, ApplicationFrame.getInstance().getUserDirectory());
            final String finalMessage = message;
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    JOptionPane.showMessageDialog(ApplicationFrame.getInstance(),
                            finalMessage, "An error occured", JOptionPane.ERROR_MESSAGE);
                });
            } catch (Exception ex) {
                ExceptionDialog.hideException(ex);
            }
            return null;
        }

        if (groovyEngine == null) {
            groovyEngine = initScriptEngineImpl("Groovy");
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
            jsEngine = initScriptEngineImpl("JavaScript");
        }
        return jsEngine;
    }

    private ScriptEngine initScriptEngineImpl(String shortName) {
        ScriptEngine engine = null;

        try {
            ScriptEngineManager sem = new ScriptEngineManager(this.getClass().getClassLoader());
            engine = sem.getEngineByName(shortName);
            if (engine == null) {
                showLoadingEngineError(shortName);
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
            javax.swing.SwingUtilities.invokeAndWait(() -> {
                JOptionPane.showMessageDialog(ApplicationFrame.getInstance(),
                        message, "An error occured", JOptionPane.ERROR_MESSAGE);
            });
        } catch (Exception ex) {
            ExceptionDialog.hideException(ex);
        }
    }
}