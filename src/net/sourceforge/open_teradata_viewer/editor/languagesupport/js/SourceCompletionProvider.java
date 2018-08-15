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

import java.awt.Cursor;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.Segment;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.DefaultCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.ShorthandCompletionCache;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.JarManager;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.buildpath.ISourceLocation;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.JavaScriptHelper.ParseText;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.CodeBlock;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.JavaScriptVariableDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.TypeDeclarationOptions;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.VariableResolver;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.jsType.JavaScriptType;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.jsType.JavaScriptTypesFactory;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.parser.JavaScriptParser;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.TypeDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.TypeDeclarationFactory;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.ecma.TypeDeclarations;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.completion.IJSCompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.completion.IJSCompletionUI;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.completion.JSVariableCompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.engine.JavaScriptEngine;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.engine.JavaScriptEngineFactory;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.resolver.JavaScriptResolver;
import sun.org.mozilla.javascript.internal.ast.AstNode;
import sun.org.mozilla.javascript.internal.ast.AstRoot;

/**
 * Completion provider for JavaScript source code (not comments or strings).
 * 
 * @author D. Campione
 * 
 */
public class SourceCompletionProvider extends DefaultCompletionProvider {

    private JavaScriptCompletionProvider parent;
    private JarManager jarManager;
    private int dot;
    private JavaScriptEngine engine;
    private JavaScriptTypesFactory javaScriptTypesFactory;

    private VariableResolver variableResolver;

    private PreProcesssingScripts preProcessing;

    // Shorthand completions (templates and comments)
    private ShorthandCompletionCache shorthandCache;

    private boolean xmlSupported;
    // The base class for the completions
    private String self;

    private TypeDeclarationOptions typeDeclarationOptions;

    public SourceCompletionProvider(boolean xmlSupported) {
        this(null, xmlSupported);
    }

    public SourceCompletionProvider(String javaScriptEngine,
            boolean xmlSupported) {
        variableResolver = new VariableResolver();
        this.xmlSupported = xmlSupported;
        setParameterizedCompletionParams('(', ", ", ')');
        setAutoActivationRules(false, "."); // Default - only activate after '.'
        engine = JavaScriptEngineFactory.Instance().getEngineFromCache(
                javaScriptEngine);
        javaScriptTypesFactory = engine.getJavaScriptTypesFactory(this);
        // Set default for self to Global
        setSelf(TypeDeclarations.ECMA_GLOBAL);
    }

    /**
     * Adds simple shorthand completions relevant to JavaScript from the short
     * hand template.
     *
     * @param set The set to add to.
     * @see ShorthandCompletionCache
     */
    private void addShorthandCompletions(Set set) {
        if (shorthandCache != null) {
            set.addAll(shorthandCache.getShorthandCompletions());
        }
    }

    /** Set template completion cache for source completion provider. */
    public void setShorthandCache(ShorthandCompletionCache shorthandCache) {
        this.shorthandCache = shorthandCache;
    }

    private String lastCompletionsAtText = null;
    private List<ICompletion> lastParameterizedCompletionsAt = null;

    /** {@inheritDoc} */
    @Override
    public List<ICompletion> getCompletionsAt(JTextComponent tc, Point p) {
        int offset = tc.viewToModel(p);
        if (offset < 0 || offset >= tc.getDocument().getLength()) {
            lastCompletionsAtText = null;
            return lastParameterizedCompletionsAt = null;
        }

        Segment s = new Segment();
        Document doc = tc.getDocument();
        Element root = doc.getDefaultRootElement();
        int line = root.getElementIndex(offset);
        Element elem = root.getElement(line);
        int start = elem.getStartOffset();
        int end = elem.getEndOffset() - 1;

        try {
            doc.getText(start, end - start, s);

            // Get the valid chars before the specified offset
            int startOffs = s.offset + (offset - start) - 1;
            while (startOffs >= s.offset
                    && Character.isLetterOrDigit(s.array[startOffs])) {
                startOffs--;
            }

            // Get the valid chars at and after the specified offset
            int endOffs = s.offset + (offset - start);
            while (endOffs < s.offset + s.count
                    && Character.isLetterOrDigit(s.array[endOffs])) {
                endOffs++;
            }

            int len = endOffs - startOffs - 1;
            if (len <= 0) {
                return lastParameterizedCompletionsAt = null;
            }
            String text = new String(s.array, startOffs + 1, len);

            if (text.equals(lastCompletionsAtText)) {
                return lastParameterizedCompletionsAt;
            }

            // Get a list of all Completions matching the text
            AstRoot ast = this.parent.getASTRoot();
            Set<IJSCompletionUI> set = new HashSet<IJSCompletionUI>();
            CodeBlock block = iterateAstRoot(ast, set, text,
                    tc.getCaretPosition(), typeDeclarationOptions);
            recursivelyAddLocalVars(set, block, dot, null, false, false);
            lastCompletionsAtText = text;
            return lastParameterizedCompletionsAt = new ArrayList<ICompletion>(
                    set);

        } catch (BadLocationException ble) {
            ExceptionDialog.hideException(ble); // Never happens
        }

        lastCompletionsAtText = null;
        return lastParameterizedCompletionsAt = null;
    }

    /** {@inheritDoc} */
    @Override
    protected List<ICompletion> getCompletionsImpl(JTextComponent comp) {
        comp.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            // Reset local variables.
            // They maybe needed outside this method so keep them in memory and
            // clear them at the start
            variableResolver.resetLocalVariables();

            completions.clear();

            dot = comp.getCaretPosition();

            AstRoot astRoot = parent.getASTRoot();

            if (astRoot == null) {
                return completions; // Empty
            }

            Set<IJSCompletionUI> set = new TreeSet<IJSCompletionUI>();

            // Cut down the list to just those matching what we've typed.
            // Note: getAlreadyEnteredText() never returns null
            String text = getAlreadyEnteredText(comp);

            if (supportsPreProcessingScripts()) {
                variableResolver.resetPreProcessingVariables(false);
            }

            if (text == null) {
                return completions; // Empty
            }

            // Remove anything right of comma (if applicable) as this causes
            // Rhino Ast Compile errors and is not required
            // text = JavaScriptHelper.trimFromLastParam(text.trim());

            boolean noDotInText = text.indexOf('.') == -1;

            // Need to populate completions to work out all variables available
            CodeBlock block = iterateAstRoot(astRoot, set, text, dot,
                    typeDeclarationOptions);

            boolean isNew = false;
            if (noDotInText) {
                // Don't add shorthand completions if they're typing something
                // qualified; only add shorthand completions if the user has
                // started typing something in (Eclipse behaviour)
                if (text.length() > 0) {
                    addShorthandCompletions(set);
                }

                if (text.length() > 0) { // Try to convert text by removing
                                         // any if, while etc..
                    ParseText pt = JavaScriptHelper.parseEnteredText(text);
                    text = pt.text;
                    isNew = pt.isNew;

                    if (isNew) {
                        return handleNewFilter(set, text);
                    } else {
                        // Load classes and move on
                        loadECMAClasses(set, "");
                    }
                }

                // Load global object
                parseTextAndResolve(set, "this." + text);
                recursivelyAddLocalVars(set, block, dot, null, false, false);
            } else {
                parseTextAndResolve(set, text);
            }

            if (noDotInText && supportsPreProcessingScripts() && !isNew) {
                set.addAll(preProcessing.getCompletions());
            }

            return resolveCompletions(text, set);
        } finally {
            comp.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        }
    }

    private List<ICompletion> handleNewFilter(Set<IJSCompletionUI> set,
            String text) {
        set.clear(); // Reset as just interested in the just load the
                     // constructors
        loadECMAClasses(set, text);
        return resolveCompletions(text, set);
    }

    private List<ICompletion> resolveCompletions(String text,
            Set<IJSCompletionUI> set) {
        completions.addAll(set);

        // Do a sort of all of our completions to put into case insensitive
        // order and we're good to go
        Collections.sort(completions, comparator);

        // Only match based on stuff after the final '.', since that's what is
        // displayed for all of our completions
        text = text.substring(text.lastIndexOf('.') + 1);

        int start = Collections.binarySearch(completions, text, comparator);
        if (start < 0) {
            start = -(start + 1);
        } else {
            // There might be multiple entries with the same input text
            while (start > 0
                    && comparator.compare(completions.get(start - 1), text) == 0) {
                start--;
            }
        }

        int end = Collections.binarySearch(completions, text + '{', comparator);
        end = -(end + 1);

        return completions.subList(start, end);
    }

    /**
     * Load ECMA JavaScript class completions.
     * 
     * @param set Completion set.
     */
    private void loadECMAClasses(Set<IJSCompletionUI> set, String text) {
        // All the constructors
        List<JavaScriptType> list = engine.getJavaScriptTypesFactory(this)
                .getECMAObjectTypes(this);

        for (JavaScriptType type : list) {
            // Iterate through the constructors
            if (text.length() == 0) {
                if (type.getClassTypeCompletion() != null) {
                    set.add(type.getClassTypeCompletion());
                }
            } else {
                if (type.getType().getJSName().startsWith(text)) {
                    for (IJSCompletion jsc : type.getConstructorCompletions()
                            .values()) {
                        set.add(jsc);
                    }
                }
            }
        }
    }

    /**
     * returns the Base class for the source completion provider. This is
     * represented by a class name or ECMA lookup name, e.g set to 'Global' for
     * server side or 'Window' for client JavaScript support.
     * 
     * @return Base class for the completion provider.
     */
    public String getSelf() {
        return self;
    }

    /** Parse text and add completions to set. */
    private void parseTextAndResolve(Set set, String text) {
        // Compile the entered text and resolve the variables/function
        JavaScriptResolver compiler = engine.getJavaScriptResolver(this);
        try {
            JavaScriptType type = compiler.compileText(text);
            boolean resolved = populateCompletionsFromType(type, set);
            if (!resolved) {
                type = compiler.compileText("this." + text);
                populateCompletionsFromType(type, set);
            }
        } catch (IOException io) {
            ExceptionDialog.hideException(io);
        }
    }

    /**
     * Populate Set of completions if JavaScriptType is not null and return
     * true, otherwise return false.
     */
    private boolean populateCompletionsFromType(JavaScriptType type, Set set) {
        if (type != null) {
            javaScriptTypesFactory.populateCompletionsForType(type, set);
            return true;
        }
        return false;
    }

    @Override
    public String getAlreadyEnteredText(JTextComponent comp) {
        String text = super.getAlreadyEnteredText(comp);
        if (text != null) {
            int charIndex = JavaScriptHelper
                    .findIndexOfFirstOpeningBracket(text);
            text = text.substring(charIndex, text.length());
            int sqIndex = JavaScriptHelper
                    .findIndexOfFirstOpeningSquareBracket(text);
            text = text.substring(sqIndex).trim();
            if (charIndex > 0 || sqIndex > 0) {
                text = JavaScriptHelper.trimFromLastParam(text);
                Logger.log("SourceCompletionProvider:getAlreadyEnteredText()::afterTrim "
                        + text);
            }
        }

        return text;
    }

    /**
     * Iterates through AstRoot to extract all code blocks, functions, variables
     * etc.., e.g functions, if statements, variables.
     * 
     * @param root AstRoot to iterate.
     * @param set Add completions to set (functions only).
     * @param entered Already entered text.
     * @param dot Position in code.
     * @param preProcessingMode Flag to state whether the parsing is before the
     *        STA parsing.
     */
    protected CodeBlock iterateAstRoot(AstRoot root, Set<IJSCompletionUI> set,
            String entered, int dot, TypeDeclarationOptions options) {
        JavaScriptParser parser = engine.getParser(this, dot, options);
        return parser.convertAstNodeToCodeBlock(root, set, entered);
    }

    /** Convenience method to call variable resolver. */
    public TypeDeclaration resolveTypeDeclation(String name) {
        return variableResolver.resolveType(name, dot);
    }

    /**
     * Convenience method to call variable resolver.
     * 
     * @return JavaScript variable declaration.
     */
    public JavaScriptVariableDeclaration findDeclaration(String name) {
        return variableResolver.findDeclaration(name, dot);
    }

    /**
     * Convenience method to call variable resolver for non local variables, i.e
     * does NOT try to resolve name to any local variables (just pre-processed
     * or system).
     * 
     * @return JavaScript variable declaration.
     */
    public JavaScriptVariableDeclaration findNonLocalDeclaration(String name) {
        return variableResolver.findNonLocalDeclaration(name, dot);
    }

    /**
     * Get the source of the node and try to resolve function node:
     * 
     * @return a.toString().getCharAt(1); returns String TypeDeclaration.
     */
    public TypeDeclaration resolveTypeFromFunctionNode(AstNode functionNode) {
        String functionText = functionNode.toSource();

        // Resolve the TypeDeclaration and set on the variable
        return resolveTypeDeclation(functionText);
    }

    void setParent(JavaScriptCompletionProvider parent) {
        this.parent = parent;
    }

    public void setJavaScriptTypesFactory(JavaScriptTypesFactory factory) {
        this.javaScriptTypesFactory = factory;
    }

    public JavaScriptTypesFactory getJavaScriptTypesFactory() {
        return javaScriptTypesFactory;
    }

    /** Iterate though the CodeBlock and extract all variables within scope. */
    protected void recursivelyAddLocalVars(Set<IJSCompletionUI> completions,
            CodeBlock block, int dot, String text, boolean findMatch,
            boolean isPreprocessing) {

        if (!block.contains(dot)) {
            return;
        }

        // Add local variables declared in this code block
        for (int i = 0; i < block.getVariableDeclarationCount(); i++) {
            JavaScriptVariableDeclaration dec = block.getVariableDeclaration(i);
            int decOffs = dec.getOffset();
            if (dot <= decOffs) {
                if (!findMatch || dec.getName().equals(text)) {
                    JSVariableCompletion completion = new JSVariableCompletion(
                            this, dec, !isPreprocessing);
                    // Check whether the variable exists and replace as the
                    // scope may be local
                    if (completions.contains(completion)) {
                        completions.remove(completion);
                    }
                    completions.add(completion);
                }
            } else {
                break;
            }
        }

        // Add any local variables declared in a child code block
        for (int i = 0; i < block.getChildCodeBlockCount(); i++) {
            CodeBlock child = block.getChildCodeBlock(i);
            if (child.contains(dot)) {
                recursivelyAddLocalVars(completions, child, dot, text,
                        findMatch, isPreprocessing);
            }
        }
    }

    @Override
    protected boolean isValidChar(char ch) {
        return Character.isJavaIdentifierPart(ch) || ch == ',' || ch == '.'
                || ch == getParameterListStart() || ch == getParameterListEnd()
                || ch == ' ' || ch == '"' || ch == '[' || ch == ']';
    }

    /** The jar manager is used to parse the JS API for function completions. */
    public void setJarManager(JarManager jarManager) {
        this.jarManager = jarManager;
    }

    public JarManager getJarManager() {
        return jarManager;
    }

    public VariableResolver getVariableResolver() {
        return variableResolver;
    }

    public JavaScriptLanguageSupport getLanguageSupport() {
        return parent.getLanguageSupport();
    }

    public void setPreProcessingScripts(PreProcesssingScripts preProcessing) {
        this.preProcessing = preProcessing;
    }

    public PreProcesssingScripts getPreProcessingScripts() {
        return preProcessing;
    }

    private boolean supportsPreProcessingScripts() {
        return preProcessing != null;
    }

    public JavaScriptEngine getJavaScriptEngine() {
        return engine;
    }

    public void setJavaScriptEngine(JavaScriptEngine engine) {
        this.engine = engine;
    }

    public ISourceLocation getSourceLocForClass(String className) {
        return jarManager.getSourceLocForClass(className);
    }

    public boolean isXMLSupported() {
        return xmlSupported;
    }

    public void setXMLSupported(boolean xmlSupported) {
        this.xmlSupported = xmlSupported;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public void parseDocument(int dot) {
        AstRoot ast = this.parent.getASTRoot();
        Set<IJSCompletionUI> set = new HashSet<IJSCompletionUI>();
        variableResolver.resetLocalVariables();
        iterateAstRoot(ast, set, "", dot, typeDeclarationOptions);
    }

    public TypeDeclarationFactory getTypesFactory() {
        return engine.getTypesFactory();
    }

    /** Set type declaration options for parser. */
    public void setTypeDeclationOptions(
            TypeDeclarationOptions typeDeclarationOptions) {
        this.typeDeclarationOptions = typeDeclarationOptions;
    }

    public void debugCodeBlock(CodeBlock block, int tab) {
        System.out.println();
        tab++;
        if (block != null) {
            for (int i = 0; i < tab; i++) {
                System.out.print("\t");
            }
            System.out.print("Start: " + block.getStartOffset() + " end:"
                    + block.getEndOffset());
            for (int ii = 0; ii < block.getVariableDeclarationCount(); ii++) {
                JavaScriptVariableDeclaration vd = block
                        .getVariableDeclaration(ii);
                System.out.print(" " + vd.getName() + " ");
            }
            for (int i = 0; i < block.getChildCodeBlockCount(); i++) {
                debugCodeBlock(block.getChildCodeBlock(i), tab);
            }
        }
    }
}