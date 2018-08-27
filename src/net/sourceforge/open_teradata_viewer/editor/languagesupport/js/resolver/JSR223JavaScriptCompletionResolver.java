/*
 * Open Teradata Viewer ( editor language support js resolver )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.resolver;

import java.io.IOException;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.ClassFile;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.JavaScriptHelper;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.Logger;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.SourceCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.TypeDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.completion.JSMethodData;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.PropertyGet;

/**
 *
 *
 * @author D. Campione
 *
 */
public class JSR223JavaScriptCompletionResolver extends
        JavaScriptCompletionResolver {

    /**
     * RhinoCompletionProvider constructor; resolves Rhino specific types.
     * Used to resolve Static class, e.g java.lag.String methods and fields.
     */
    public JSR223JavaScriptCompletionResolver(SourceCompletionProvider provider) {
        super(provider);
    }

    /**
     * Try to resolve standard JavaScript type. If null, then look for static
     * class.
     */
    @Override
    protected TypeDeclaration resolveNativeType(AstNode node) {
        TypeDeclaration dec = super.resolveNativeType(node);
        if (dec == null) {
            dec = testJavaStaticType(node);
        }

        return dec;
    }

    @Override
    public String getLookupText(JSMethodData methodData, String name) {
        StringBuilder sb = new StringBuilder(name);
        sb.append('(');
        int count = methodData.getParameterCount();
        String[] parameterTypes = methodData.getMethodInfo()
                .getParameterTypes();
        for (int i = 0; i < count; i++) {
            String paramName = methodData.getParameterType(parameterTypes, i,
                    provider);
            sb.append(paramName);
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
                AstNode paramNode = call.getArguments().get(i);
                JavaScriptResolver resolver = provider.getJavaScriptEngine()
                        .getJavaScriptResolver(provider);
                Logger.log("PARAM: "
                        + JavaScriptHelper.convertNodeToSource(paramNode));
                try {
                    TypeDeclaration type = resolver
                            .resolveParamNode(JavaScriptHelper
                                    .convertNodeToSource(paramNode));
                    String resolved = type != null ? type.getQualifiedName()
                            : "any";
                    sb.append(resolved);
                    if (i < count - 1) {
                        sb.append(",");
                    }
                } catch (IOException io) {
                    ExceptionDialog.hideException(io);
                }
            }
            sb.append(")");
            return sb.toString();
        }
        return null;
    }

    /**
     * Try to resolve the Token.NAME AstNode and return a TypeDeclaration.
     *
     * @param node Node to resolve.
     * @return TypeDeclaration if the name can be resolved as a Java Class else
     *         null.
     */
    @Override
    protected TypeDeclaration findJavaStaticType(AstNode node) {
        // Check parent is of type property get
        String testName = null;
        if (node.getParent() != null
                && node.getParent().getType() == Token.GETPROP) { // Ast Parser

            String name = node.toSource();
            try {
                String longName = node.getParent().toSource();

                if (longName.indexOf('[') == -1 && longName.indexOf(']') == -1
                        && longName.indexOf('(') == -1
                        && longName.indexOf(')') == -1) {

                    // Trim the text to the short name
                    int index = longName.lastIndexOf(name);
                    if (index > -1) {
                        testName = longName.substring(0, index + name.length());
                    }
                }
            } catch (Exception e) {
                Logger.log(e.getMessage());
            }
        } else {
            testName = node.toSource();
        }

        if (testName != null) {
            TypeDeclaration dec = JavaScriptHelper.getTypeDeclaration(testName,
                    provider);

            if (dec == null) {
                dec = JavaScriptHelper.createNewTypeDeclaration(testName);
            }

            ClassFile cf = provider.getJavaScriptTypesFactory().getClassFile(
                    provider.getJarManager(), dec);
            if (cf != null) {
                TypeDeclaration returnDec = provider
                        .getJavaScriptTypesFactory().createNewTypeDeclaration(
                                cf, true, false);
                return returnDec;
            }
        }
        return null;
    }
}