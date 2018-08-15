/*
 * Open Teradata Viewer ( editor language support js resolver )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.resolver;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.ClassFile;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.JavaScriptHelper;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.JavaScriptParser;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.Logger;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.SourceCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.JavaScriptFunctionDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.jsType.JavaScriptType;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.TypeDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.completion.IJSCompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.completion.JSMethodData;
import sun.org.mozilla.javascript.internal.CompilerEnvirons;
import sun.org.mozilla.javascript.internal.Parser;
import sun.org.mozilla.javascript.internal.Token;
import sun.org.mozilla.javascript.internal.ast.AstNode;
import sun.org.mozilla.javascript.internal.ast.AstRoot;
import sun.org.mozilla.javascript.internal.ast.ExpressionStatement;
import sun.org.mozilla.javascript.internal.ast.FunctionCall;
import sun.org.mozilla.javascript.internal.ast.Name;
import sun.org.mozilla.javascript.internal.ast.NodeVisitor;
import sun.org.mozilla.javascript.internal.ast.PropertyGet;

/**
 * Compiles the entered text using Rhino and tries to resolve the JavaScriptType
 * from the AstRoot e.g var a = ""; "" -> String JavaScriptType var b =
 * a.toString() a.toString -> String JavaScriptType, etc..
 * 
 * Note, will resolve any type added to JavaScriptTypesFactory.
 * 
 * @author D. Campione
 * 
 */
public class JavaScriptCompletionResolver extends JavaScriptResolver {

    protected JavaScriptType lastJavaScriptType;
    protected String lastLookupName = null;

    /** Standard ECMA JavaScript resolver. */
    public JavaScriptCompletionResolver(SourceCompletionProvider provider) {
        super(provider);
    }

    /**
     * Compiles Text and resolves the type.
     * e.g 
     * "Hello World".length; // Resolve as a Number
     * 
     * @param text Text to compile and resolve.
     */
    @Override
    public JavaScriptType compileText(String text) throws IOException {
        CompilerEnvirons env = JavaScriptParser.createCompilerEnvironment(
                new JavaScriptParser.JSErrorReporter(),
                provider.getLanguageSupport());

        String parseText = JavaScriptHelper.removeLastDotFromText(text);

        int charIndex = JavaScriptHelper
                .findIndexOfFirstOpeningBracket(parseText);
        env.setRecoverFromErrors(true);
        Parser parser = new Parser(env);
        StringReader r = new StringReader(parseText);
        AstRoot root = parser.parse(r, null, 0);
        CompilerNodeVisitor visitor = new CompilerNodeVisitor(charIndex == 0);
        root.visitAll(visitor);
        return lastJavaScriptType;
    }

    /**
     * Resolve node type to TypeDeclaration. Called instead of
     * #compileText(String text) when document is already parsed.
     * 
     * @param node AstNode to resolve.
     * @return TypeDeclaration for node or null if not found.
     */
    @Override
    public TypeDeclaration resolveParamNode(String text) throws IOException {
        if (text != null) {
            CompilerEnvirons env = JavaScriptParser.createCompilerEnvironment(
                    new JavaScriptParser.JSErrorReporter(),
                    provider.getLanguageSupport());

            int charIndex = JavaScriptHelper
                    .findIndexOfFirstOpeningBracket(text);
            env.setRecoverFromErrors(true);
            Parser parser = new Parser(env);
            StringReader r = new StringReader(text);
            AstRoot root = parser.parse(r, null, 0);
            CompilerNodeVisitor visitor = new CompilerNodeVisitor(
                    charIndex == 0);
            root.visitAll(visitor);
        }

        return lastJavaScriptType != null ? lastJavaScriptType.getType()
                : provider.getTypesFactory().getDefaultTypeDeclaration();
    }

    /**
     * Resolve node type to TypeDeclaration. Called instead of
     * #compileText(String text) when document is already parsed.
     * 
     * @param node AstNode to resolve.
     * @return TypeDeclaration for node or null if not found.
     */
    @Override
    public TypeDeclaration resolveNode(AstNode node) {
        if (node == null) {
            return provider.getTypesFactory().getDefaultTypeDeclaration();
        }
        CompilerNodeVisitor visitor = new CompilerNodeVisitor(true);
        node.visit(visitor);
        return lastJavaScriptType != null ? lastJavaScriptType.getType()
                : provider.getTypesFactory().getDefaultTypeDeclaration();
    }

    /**
     * Resolve node type to TypeDeclaration.
     * N.B called from <code>CompilerNodeVisitor.visit()</code>.
     * 
     * @param node AstNode to resolve.
     * @return TypeDeclaration for node or null if not found.
     */
    @Override
    protected TypeDeclaration resolveNativeType(AstNode node) {
        return JavaScriptHelper.tokenToNativeTypeDeclaration(node, provider);
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    private class CompilerNodeVisitor implements NodeVisitor {

        private boolean ignoreParams;
        private HashSet<AstNode> paramNodes = new HashSet<AstNode>();

        private CompilerNodeVisitor(boolean ignoreParams) {
            this.ignoreParams = ignoreParams;
        }

        @Override
        public boolean visit(AstNode node) {
            Logger.log(JavaScriptHelper.convertNodeToSource(node));
            Logger.log(node.shortName());

            if (!validNode(node)) {
                // Invalid node found, set last completion invalid and stop
                // processing
                lastJavaScriptType = null;
                return false;
            }

            if (ignore(node, ignoreParams)) {
                return true;
            }

            JavaScriptType jsType = null;
            TypeDeclaration dec = null;
            // Only resolve native type if last type is null; otherwise it can
            // be assumed that this is part of multi depth - e.g
            // "".length.toString()
            if (lastJavaScriptType == null) {
                dec = resolveNativeType(node);
                if (dec == null && node.getType() == Token.NAME) {
                    lastJavaScriptType = null;
                    return false;
                }
            } else {
                dec = resolveTypeFromLastJavaScriptType(node);
            }

            if (dec != null) {
                // Lookup JavaScript completions type
                jsType = provider.getJavaScriptTypesFactory().getCachedType(
                        dec, provider.getJarManager(), provider,
                        JavaScriptHelper.convertNodeToSource(node));

                if (jsType != null) {
                    lastJavaScriptType = jsType;
                    // Stop here
                    return false;
                }
            } else if (lastJavaScriptType != null) {
                if (node.getType() == Token.NAME) {
                    // Lookup from source name
                    jsType = lookupFromName(node, lastJavaScriptType);
                    if (jsType == null) {
                        // Lookup name through the functions of
                        // lastJavaScriptType
                        jsType = lookupFunctionCompletion(node,
                                lastJavaScriptType);
                    }
                    lastJavaScriptType = jsType;
                }
            } else if (node instanceof FunctionCall) {
                FunctionCall fn = (FunctionCall) node;
                String lookupText = createLookupString(fn);
                JavaScriptFunctionDeclaration funcDec = provider
                        .getVariableResolver().findFunctionDeclaration(
                                lookupText);
                if (funcDec != null) {
                    jsType = provider.getJavaScriptTypesFactory()
                            .getCachedType(funcDec.getTypeDeclaration(),
                                    provider.getJarManager(), provider,
                                    JavaScriptHelper.convertNodeToSource(node));
                    if (jsType != null) {
                        lastJavaScriptType = jsType;
                        // Stop here
                        return false;
                    }
                }
            }

            return true;
        }

        private boolean validNode(AstNode node) {
            switch (node.getType()) {
            case Token.NAME:
                return ((Name) node).getIdentifier() != null
                        && ((Name) node).getIdentifier().length() > 0;
            }
            return true;
        }

        private String createLookupString(FunctionCall fn) {
            StringBuilder sb = new StringBuilder();
            String name = "";
            switch (fn.getTarget().getType()) {
            case Token.NAME:
                name = ((Name) fn.getTarget()).getIdentifier();
                break;
            }
            sb.append(name);
            sb.append("(");
            Iterator<AstNode> i = fn.getArguments().iterator();
            while (i.hasNext()) {
                i.next();
                sb.append("p");
                if (i.hasNext()) {
                    sb.append(",");
                }
            }
            sb.append(")");
            return sb.toString();
        }

        /**
         * Test node to check whether to ignore resolving, this is for
         * parameters.
         * 
         * @param node Node to test.
         * @return true to ignore.
         */
        private boolean ignore(AstNode node, boolean ignoreParams) {
            switch (node.getType()) {
            // Ignore errors, e.g if statement - if(a. // No closing brace
            case Token.EXPR_VOID:
            case Token.EXPR_RESULT:
                return ((ExpressionStatement) node).getExpression().getType() == Token.ERROR;
            case Token.ERROR:
            case Token.GETPROP:
            case Token.SCRIPT:
                return true;
            default: {
                if (isParameter(node)) {
                    collectAllNodes(node); // Everything within this node is a
                                           // parameter
                    return ignoreParams;
                }
                break;
            }
            }

            return false;
        }

        /** Get all nodes within AstNode and add to an ArrayList. */
        private void collectAllNodes(AstNode node) {
            if (node.getType() == Token.CALL) {
                // Collect all argument nodes
                FunctionCall call = (FunctionCall) node;
                Iterator<AstNode> args = call.getArguments().iterator();
                while (args.hasNext()) {
                    AstNode arg = args.next();
                    VisitorAll all = new VisitorAll();
                    arg.visit(all);
                    paramNodes.addAll(all.getAllNodes());
                }
            }
        }

        /**
         * Check the function that a name may belong to contains this actual
         * parameter.
         * 
         * @param node Node to check.
         * @return true if the function contains the parameter.
         */
        private boolean isParameter(AstNode node) {
            if (paramNodes.contains(node)) {
                return true;
            }
            // Get all params from this function too
            FunctionCall fc = JavaScriptHelper.findFunctionCallFromNode(node);
            if (fc != null && !(node == fc)) {
                collectAllNodes(fc);
                if (paramNodes.contains(node)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Lookup the name of the node within the last JavaScript type. e.g var a =
     * 1; var b = a.MAX_VALUE; looks up MAX_VALUE within NumberLiteral a where a
     * is resolve before as a JavaScript Number.
     */
    protected JavaScriptType lookupFromName(AstNode node,
            JavaScriptType lastJavaScriptType) {
        JavaScriptType javaScriptType = null;
        if (lastJavaScriptType != null) {
            String lookupText = null;
            switch (node.getType()) {
            case Token.NAME:
                lookupText = ((Name) node).getIdentifier();
                break;
            }
            if (lookupText == null) {
                // Just try the source
                lookupText = node.toSource();
            }
            javaScriptType = lookupJavaScriptType(lastJavaScriptType,
                    lookupText);
        }
        return javaScriptType;
    }

    /**
     * Lookup the function name of the node within the last JavaScript type. e.g
     * var a = ""; var b = a.toString(); looks up toString() within
     * StringLiteral a where a is resolve before as a JavaScript String.
     */
    protected JavaScriptType lookupFunctionCompletion(AstNode node,
            JavaScriptType lastJavaScriptType) {
        JavaScriptType javaScriptType = null;
        if (lastJavaScriptType != null) {

            String lookupText = JavaScriptHelper.getFunctionNameLookup(node,
                    provider);
            javaScriptType = lookupJavaScriptType(lastJavaScriptType,
                    lookupText);

        }
        // Return last type
        return javaScriptType;
    }

    @Override
    public String getLookupText(JSMethodData method, String name) {
        StringBuilder sb = new StringBuilder(name);
        sb.append('(');
        int count = method.getParameterCount();
        for (int i = 0; i < count; i++) {
            sb.append("p");
            if (i < count - 1) {
                sb.append(",");
            }
        }
        sb.append(')');
        return sb.toString();
    }

    @Override
    public String getFunctionNameLookup(FunctionCall call,
            SourceCompletionProvider provider) {
        if (call != null) {
            StringBuilder sb = new StringBuilder();
            if (call.getTarget() instanceof PropertyGet) {
                PropertyGet get = (PropertyGet) call.getTarget();
                sb.append(get.getProperty().getIdentifier());
            }
            sb.append("(");
            int count = call.getArguments().size();
            for (int i = 0; i < count; i++) {
                sb.append("p");
                if (i < count - 1) {
                    sb.append(",");
                }
            }
            sb.append(")");
            return sb.toString();
        }
        return null;
    }

    private JavaScriptType lookupJavaScriptType(
            JavaScriptType lastJavaScriptType, String lookupText) {
        JavaScriptType javaScriptType = null;
        if (lookupText != null && !lookupText.equals(lastLookupName)) {
            // Look up IJSCompletion
            IJSCompletion completion = lastJavaScriptType.getCompletion(
                    lookupText, provider);
            if (completion != null) {
                String type = completion.getType(true);
                if (type != null) {
                    TypeDeclaration newType = provider.getTypesFactory()
                            .getTypeDeclaration(type);
                    if (newType != null) {
                        javaScriptType = provider.getJavaScriptTypesFactory()
                                .getCachedType(newType,
                                        provider.getJarManager(), provider,
                                        lookupText);
                    } else {
                        javaScriptType = createNewTypeDeclaration(provider,
                                type, lookupText);
                    }
                }
            }
        }
        lastLookupName = lookupText;
        return javaScriptType;
    }

    /**
     * Creates a new JavaScriptType based on the String type.
     * 
     * @param provider SourceCompletionProvider.
     * @param type Type of JavaScript type to create, e.g java.sql.Connection.
     * @param text Text entered from the user to resolve the node. This will be
     *        null if resolveNode(AstNode node) is called.
     */
    private JavaScriptType createNewTypeDeclaration(
            SourceCompletionProvider provider, String type, String text) {
        if (provider.getJavaScriptTypesFactory() != null) {
            ClassFile cf = provider.getJarManager().getClassEntry(type);
            TypeDeclaration newType = null;
            if (cf != null) {
                newType = provider.getJavaScriptTypesFactory()
                        .createNewTypeDeclaration(cf, false);
                return provider.getJavaScriptTypesFactory().getCachedType(
                        newType, provider.getJarManager(), provider, text);
            }
        }
        return null;
    }

    /**
     * Method called if the lastJavaScriptType is not null. i.e has gone through
     * one iteration at least.
     * Resolves TypeDeclaration for parts of a variable past the first part. e.g
     * "".toString() // Resolve toString()
     * In some circumstances this is useful to resolve this. e.g for Custom
     * Object completions.
     * 
     * @param node Node to resolve.
     * @return Type Declaration.
     * 
     */
    protected TypeDeclaration resolveTypeFromLastJavaScriptType(AstNode node) {
        return null;
    }

    /**
     * Visit all nodes in the AstNode tree and all to a single list
     * 
     * @author D. Campione
     * 
     */
    private class VisitorAll implements NodeVisitor {

        private ArrayList<AstNode> all = new ArrayList<AstNode>();

        @Override
        public boolean visit(AstNode node) {
            all.add(node);
            return true;
        }

        public ArrayList<AstNode> getAllNodes() {
            return all;
        }
    }
}