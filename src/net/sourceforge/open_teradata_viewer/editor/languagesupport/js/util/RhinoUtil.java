/*
 * Open Teradata Viewer ( editor language support util )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.util;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.PropertyGet;
import org.mozilla.javascript.ast.StringLiteral;

/**
 * Utility methods for walking ASTs from Rhino.
 *
 * @author D. Campione
 *
 */
public class RhinoUtil {

    /** Private constructor to prevent instantiation. */
    private RhinoUtil() {
    }

    /**
     * Iterates through a function's parameters and returns a string
     * representation of them, suitable for presentation as part of the method's
     * signature.
     *
     * @param fn The function node.
     * @return The string representation of the function's arguments.
     */
    public static final String getFunctionArgsString(FunctionNode fn) {
        StringBuilder sb = new StringBuilder("(");
        int paramCount = fn.getParamCount();
        if (paramCount > 0) {
            List<AstNode> fnParams = fn.getParams();
            for (int i = 0; i < paramCount; i++) {
                String paramName = null;
                AstNode paramNode = fnParams.get(i);
                switch (paramNode.getType()) {
                case Token.NAME:
                    paramName = ((Name) paramNode).getIdentifier();
                    break;
                default:
                    System.out.println("Unhandled class for param: "
                            + paramNode.getClass());
                    paramName = "?";
                    break;
                }
                sb.append(paramName);
                if (i < paramCount - 1) {
                    sb.append(", ");
                }
            }
        }
        sb.append(')');
        return sb.toString();
    }

    /**
     * Property keys in object literals can be identifiers or string literals.
     * This method takes an AST node that was the key of an
     * <code>ObjectProperty</code> and returns its value, no matter what the
     * concrete AST node's type.
     *
     * @param propKeyNode The AST node for the property key.
     * @return The property key's value.
     */
    public static final String getPropertyName(AstNode propKeyNode) {
        return (propKeyNode instanceof Name) ? ((Name) propKeyNode)
                .getIdentifier() : ((StringLiteral) propKeyNode).getValue();
    }

    public static final String getPrototypeClazz(List<AstNode> nodes) {
        return getPrototypeClazz(nodes, -1);
    }

    public static final String getPrototypeClazz(List<AstNode> nodes, int depth) {
        if (depth < 0) {
            depth = nodes.size();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            // I *think* these should always be Names but to be safe and prevent
            // ClassCastExceptions..
            sb.append(nodes.get(i).toSource());
            if (i < depth - 1) {
                sb.append('.');
            }
        }
        return sb.toString();
    }

    /**
     * Returns whether an AST node is a <code>Name</code> with the specified
     * value.
     *
     * @param node The AST node.
     * @param value The expected value.
     * @return Whether the AST node is a <code>Name</code> with the specified
     *         value.
     */
    private static final boolean isName(AstNode node, String value) {
        return node instanceof Name
                && value.equals(((Name) node).getIdentifier());
    }

    public static final boolean isPrototypeNameNode(AstNode node) {
        return node instanceof Name
                && "prototype".equals(((Name) node).getIdentifier());
    }

    public static final boolean isPrototypePropertyGet(PropertyGet pg) {
        return pg != null && pg.getLeft() instanceof Name
                && isPrototypeNameNode(pg.getRight());
    }

    /**
     * Returns whether a <code>PropertyGet</code> is a simple one, referencing
     * an object's value 1 level deep. For example, <code>Object.create</code>.
     *
     * @param pg The <code>PropertyGet</code>.
     * @param expectedObj The expected object value.
     * @param expectedField The expected string value.
     * @return Whether the object is what was expected.
     */
    public static final boolean isSimplePropertyGet(PropertyGet pg,
            String expectedObj, String expectedField) {
        return pg != null && isName(pg.getLeft(), expectedObj)
                && isName(pg.getRight(), expectedField);
    }

    public static final List<AstNode> toList(AstNode... nodes) {
        List<AstNode> list = new ArrayList<AstNode>();
        for (AstNode node : nodes) {
            list.add(node);
        }
        return list;
    }
}