/*
 * Open Teradata Viewer ( editor language support js ast parser )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.parser;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.JavaScriptHelper;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.SourceCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.CodeBlock;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.TypeDeclarationOptions;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.jsType.JavaScriptTypesFactory;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.jsType.RhinoJavaScriptTypesFactory;
import sun.org.mozilla.javascript.internal.Token;
import sun.org.mozilla.javascript.internal.ast.AstNode;
import sun.org.mozilla.javascript.internal.ast.AstRoot;

/**
 * Rhino specific JavaScriptAstParser.
 *  
 * Reads the importPackage and importClass from the parsed document and adds to
 * the RhinoJavaScriptTypesFactory.
 * 
 * @author D. Campione
 * 
 */
public class RhinoJavaScriptAstParser extends JavaScriptAstParser {

    public static final String PACKAGES = "Packages.";

    private LinkedHashSet<String> importClasses = new LinkedHashSet<String>();
    private LinkedHashSet<String> importPackages = new LinkedHashSet<String>();

    public RhinoJavaScriptAstParser(SourceCompletionProvider provider, int dot,
            TypeDeclarationOptions options) {
        super(provider, dot, options);
    }

    /**
     * Clear the importPackage and importClass cache.
     * 
     * @param provider SourceCompletionProvider.
     */
    public void clearImportCache(SourceCompletionProvider provider) {
        JavaScriptTypesFactory typesFactory = provider
                .getJavaScriptTypesFactory();
        if (typesFactory instanceof RhinoJavaScriptTypesFactory) {
            ((RhinoJavaScriptTypesFactory) typesFactory).clearImportCache();
        }
    }

    @Override
    public CodeBlock convertAstNodeToCodeBlock(AstRoot root, Set set,
            String entered) {
        try {
            return super.convertAstNodeToCodeBlock(root, set, entered);
        } finally {
            // Merge import packages
            mergeImportCache(importPackages, importClasses);
            // Clear, as these are now merged
            importClasses.clear();
            importPackages.clear();
        }
    }

    private void mergeImportCache(HashSet<String> packages,
            HashSet<String> classes) {
        JavaScriptTypesFactory typesFactory = provider
                .getJavaScriptTypesFactory();
        if (typesFactory instanceof RhinoJavaScriptTypesFactory) {
            ((RhinoJavaScriptTypesFactory) typesFactory).mergeImports(packages,
                    classes);
        }
    }

    /**
     * Overridden iterateNode to intercept Token.EXPR_RESULT and check for
     * importPackage and importClass named nodes.
     * If found, then process them and extract the imports and add them to
     * RhinoJavaScriptTypesFactory then return otherwise call
     * super.iterateNode().
     */
    @Override
    protected void iterateNode(AstNode child, Set set, String entered,
            CodeBlock block, int offset) {
        // Look for importPackage and importClass
        switch (child.getType()) {
        case Token.EXPR_RESULT:
            boolean importFound = processImportNode(child, set, entered, block,
                    offset);
            if (importFound) {
                return; // Already processed node
            }
            break;
        }

        super.iterateNode(child, set, entered, block, offset);
    }

    /**
     * Look for text importPackage and importClass and add to cache.
     * 
     * @param child AstNode to check. This will always be Token.EXPR_RESULT
     *        AstNode.
     * @param set Set to add completions.
     * @param entered Text entered by user if applicable.
     * @param block CodeBlock.
     * @param offset Position of AstNode within document.
     * @return true if either importPackage or importClass is found.
     */
    private boolean processImportNode(AstNode child, Set set, String entered,
            CodeBlock block, int offset) {
        String src = JavaScriptHelper.convertNodeToSource(child);
        if (src != null) {
            if (src.startsWith("importPackage")) {
                processImportPackage(src);
                return true;
            } else if (src.startsWith("importClass")) {
                processImportClass(src);
                return true;
            }
        }

        return false;
    }

    public static String removePackages(String src) {
        if (src.startsWith(PACKAGES)) {
            String pkg = src.substring(PACKAGES.length());
            if (pkg != null) {
                StringBuilder sb = new StringBuilder();
                // Remove any non java characters
                char[] chars = pkg.toCharArray();
                for (int i = 0; i < chars.length; i++) {
                    char ch = chars[i];
                    if (Character.isJavaIdentifierPart(ch) || ch == '.') {
                        sb.append(ch);
                    }
                }
                if (sb.length() > 0) {
                    return sb.toString();
                }
            }
        }
        return src;
    }

    /**
     * @param src String to extract name.
     * @return import statement from withing the ( and ),
     *         e.g importPackage(java.util)
     * 		       importClass(java.util.HashSet)
     *         Returns java.util or java.util.HashSet respectively.
     */
    private String extractNameFromSrc(String src) {
        int startIndex = src.indexOf("(");
        int endIndex = src.indexOf(")");
        if (startIndex != -1 && endIndex != -1) {
            return removePackages(src.substring(startIndex + 1, endIndex));
        }
        return removePackages(src);
    }

    /**
     * Adds package name to RhinoJavaScriptTypesFactory.
     * 
     * @param src Source text to extract the package.
     */
    private void processImportPackage(String src) {
        String pkg = extractNameFromSrc(src);
        importPackages.add(pkg);
    }

    /**
     * Adds class name to RhinoJavaScriptTypesFactory.
     * 
     * @param src Source text to extract the class name.
     */
    private void processImportClass(String src) {
        String cls = extractNameFromSrc(src);
        importClasses.add(cls);
    }
}