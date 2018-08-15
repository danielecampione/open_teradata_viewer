/*
 * Open Teradata Viewer ( editor language support js )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.List;

import javax.swing.text.Element;

import net.sourceforge.open_teradata_viewer.DocumentReader;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.VariableResolver;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxDocument;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.AbstractParser;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.DefaultParseResult;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.DefaultParserNotice;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.IParseResult;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.IParserNotice;
import sun.org.mozilla.javascript.internal.CompilerEnvirons;
import sun.org.mozilla.javascript.internal.ErrorReporter;
import sun.org.mozilla.javascript.internal.EvaluatorException;
import sun.org.mozilla.javascript.internal.Parser;
import sun.org.mozilla.javascript.internal.RhinoException;
import sun.org.mozilla.javascript.internal.ast.AstRoot;
import sun.org.mozilla.javascript.internal.ast.ErrorCollector;
import sun.org.mozilla.javascript.internal.ast.ParseProblem;

/**
 * Parses JavaScript code in an <code>SyntaxTextArea</code>.<p>
 * 
 * Like all STA <tt>Parser</tt>s, a <tt>JavaScriptParser</tt> instance is
 * notified when the STA's text content changes. After a small delay, it will
 * parse the content as JS code, building an AST and looking for any errors.
 * When parsing is complete, a property change event of type
 * {@link #PROPERTY_AST} is fired. Listeners can check the new value of the
 * property for the <code>AstRoot</code> built that represents the source code
 * in the text area.<p>
 * 
 * This parser cannot be shared amongst multiple instances of
 * <code>SyntaxTextArea</code>.<p>
 * 
 * @author D. Campione
 * 
 */
public class JavaScriptParser extends AbstractParser {

    /**
     * The property change event that's fired when the document is re-parsed.
     * Applications can listen for this property change and update themselves
     * accordingly. The "new" value of this property will be an instance of
     * <code>org.mozilla.javascript.ast.AstRoot</code>.
     */
    public static final String PROPERTY_AST = "AST";

    private AstRoot astRoot;
    private JavaScriptLanguageSupport langSupport;
    private PropertyChangeSupport support;
    private DefaultParseResult result;
    private VariableResolver variableResolver;

    /** Ctor. */
    public JavaScriptParser(JavaScriptLanguageSupport langSupport,
            SyntaxTextArea textArea) {
        this.langSupport = langSupport;
        support = new PropertyChangeSupport(this);
        result = new DefaultParseResult(this);
    }

    /**
     * Registers a property change listener on this parser. You'll probably want
     * to listen for changes to {@link #PROPERTY_AST}.
     *
     * @param prop The property to listen for changes in.
     * @param l The listener to add.
     * @see #removePropertyChangeListener(String, PropertyChangeListener)
     */
    public void addPropertyChangeListener(String prop, PropertyChangeListener l) {
        support.addPropertyChangeListener(prop, l);
    }

    /**
     * Creates options for Rhino based off of the user's preferences.
     *
     * @param errorHandler The container for errors found while parsing.
     * @return The properties for the JS compiler to use.
     */
    public static CompilerEnvirons createCompilerEnvironment(
            ErrorReporter errorHandler, JavaScriptLanguageSupport langSupport) {
        CompilerEnvirons env = new CompilerEnvirons();
        env.setErrorReporter(errorHandler);
        env.setIdeMode(true);
        env.setRecordingComments(true);
        env.setRecordingLocalJsDocComments(true);
        env.setRecoverFromErrors(true);
        if (langSupport != null) {
            env.setXmlAvailable(langSupport.isXmlAvailable());
            env.setStrictMode(langSupport.isStrictMode());
            int version = langSupport.getLanguageVersion();
            if (version > 0) {
                Logger.log("[JavaScriptParser]: JS language version set to: "
                        + version);
                env.setLanguageVersion(version);
            }
        }
        return env;
    }

    /**
     * Returns the AST or <code>null</code> if the editor's content has not yet
     * been parsed.
     * 
     * @return The AST or <code>null</code>.
     */
    public AstRoot getAstRoot() {
        return astRoot;
    }

    /** {@inheritDoc} */
    @Override
    public IParseResult parse(SyntaxDocument doc, String style) {
        astRoot = null;
        result.clearNotices();
        // Always spell check all lines, for now
        Element root = doc.getDefaultRootElement();
        int lineCount = root.getElementCount();
        result.setParsedLines(0, lineCount - 1);

        DocumentReader r = new DocumentReader(doc);
        ErrorCollector errorHandler = new ErrorCollector();
        CompilerEnvirons env = createCompilerEnvironment(errorHandler,
                langSupport);
        long start = System.currentTimeMillis();
        try {
            Parser parser = new Parser(env);
            astRoot = parser.parse(r, null, 0);
            long time = System.currentTimeMillis() - start;
            result.setParseTime(time);
        } catch (IOException ioe) { // Never happens
            result.setError(ioe);
            ExceptionDialog.hideException(ioe);
        } catch (RhinoException re) {
            // Shouldn't happen since we're passing an ErrorCollector in
            int line = re.lineNumber();
            Element elem = root.getElement(line);
            int offs = elem.getStartOffset();
            int len = elem.getEndOffset() - offs - 1;
            String msg = re.details();
            result.addNotice(new DefaultParserNotice(this, msg, line, offs, len));
        } catch (Exception e) {
            result.setError(e); // catch all
        }

        r.close();

        // Get any parser errors
        List<ParseProblem> errors = errorHandler.getErrors();
        if (errors != null && errors.size() > 0) {
            for (ParseProblem problem : errors) {
                int offs = problem.getFileOffset();
                int len = problem.getLength();
                int line = root.getElementIndex(offs);
                String desc = problem.getMessage();
                DefaultParserNotice notice = new DefaultParserNotice(this,
                        desc, line, offs, len);
                if (problem.getType() == ParseProblem.Type.Warning) {
                    notice.setLevel(IParserNotice.WARNING);
                }
                result.addNotice(notice);
            }
        }

        support.firePropertyChange(PROPERTY_AST, null, astRoot);
        return result;
    }

    public void setVariablesAndFunctions(VariableResolver variableResolver) {
        this.variableResolver = variableResolver;
    }

    public VariableResolver getVariablesAndFunctions() {
        return variableResolver;
    }

    /**
     * Removes a property change listener from this parser.
     *
     * @param prop The property that was being listened to.
     * @param l The listener to remove.
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     */
    public void removePropertyChangeListener(String prop,
            PropertyChangeListener l) {
        support.removePropertyChangeListener(prop, l);
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    public static class JSErrorReporter implements ErrorReporter {

        @Override
        public void error(String message, String sourceName, int line,
                String lineSource, int lineOffset) {
        }

        @Override
        public EvaluatorException runtimeError(String message,
                String sourceName, int line, String lineSource, int lineOffset) {
            return null;
        }

        @Override
        public void warning(String message, String sourceName, int line,
                String lineSource, int lineOffset) {
        }
    }
}