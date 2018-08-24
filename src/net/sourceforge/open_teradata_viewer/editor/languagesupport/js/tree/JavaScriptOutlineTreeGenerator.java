/*
 * Open Teradata Viewer ( editor language support js tree )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.BadLocationException;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.IconFactory;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;
import sun.org.mozilla.javascript.internal.Token;
import sun.org.mozilla.javascript.internal.ast.Assignment;
import sun.org.mozilla.javascript.internal.ast.AstNode;
import sun.org.mozilla.javascript.internal.ast.AstRoot;
import sun.org.mozilla.javascript.internal.ast.ExpressionStatement;
import sun.org.mozilla.javascript.internal.ast.FunctionNode;
import sun.org.mozilla.javascript.internal.ast.Name;
import sun.org.mozilla.javascript.internal.ast.NodeVisitor;
import sun.org.mozilla.javascript.internal.ast.PropertyGet;
import sun.org.mozilla.javascript.internal.ast.VariableDeclaration;
import sun.org.mozilla.javascript.internal.ast.VariableInitializer;

/**
 * Generates the root node for a {@link JavaScriptOutlineTree} based on a Rhino
 * AST of {@link SyntaxTextArea} code.
 *
 * @author D. Campione
 * 
 */
class JavaScriptOutlineTreeGenerator implements NodeVisitor {

    private JavaScriptTreeNode root;
    private SyntaxTextArea textArea;

    private JavaScriptTreeNode curScopeTreeNode;

    private Map<String, List<JavaScriptTreeNode>> prototypeAdditions = null;

    public JavaScriptOutlineTreeGenerator(SyntaxTextArea textArea, AstRoot ast) {
        this.textArea = textArea;
        root = new JavaScriptTreeNode(null);
        if (ast != null) {
            ast.visit(this);
        }
    }

    private void addPrototypeAdditionsToRoot() {
        if (prototypeAdditions != null) {
            root.refresh();

            for (Map.Entry<String, List<JavaScriptTreeNode>> entry : prototypeAdditions
                    .entrySet()) {
                String clazz = entry.getKey();
                for (int i = 0; i < root.getChildCount(); i++) {
                    JavaScriptTreeNode childNode = (JavaScriptTreeNode) root
                            .getChildAt(i);
                    if (childNode.getText(true).startsWith(clazz + "(")) {
                        for (JavaScriptTreeNode memberNode : entry.getValue()) {
                            childNode.add(memberNode);
                        }
                        childNode.setIcon(IconFactory
                                .getIcon(IconFactory.DEFAULT_CLASS_ICON));
                        break;
                    }
                }
            }
        }
    }

    private String getFunctionArgsString(FunctionNode fn) {
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

    public JavaScriptTreeNode getTreeRoot() {
        addPrototypeAdditionsToRoot();
        return root;
    }

    @Override
    public boolean visit(AstNode node) {
        if (node == null) {
            return false;
        }

        int nodeType = node.getType();
        switch (nodeType) {

        case Token.SCRIPT: // AstRoot
            curScopeTreeNode = root;
            return true;

        case Token.FUNCTION:
            FunctionNode fn = (FunctionNode) node;
            return visitFunction(fn);

        case Token.VAR:
            VariableDeclaration varDec = (VariableDeclaration) node;
            return visitVariableDeclaration(varDec);

        case Token.BLOCK:
            return true;

        case Token.EXPR_RESULT:
            ExpressionStatement exprStmt = (ExpressionStatement) node;
            return visitExpressionStatement(exprStmt);
        }

        return false; // Unhandled node type
    }

    private boolean visitExpressionStatement(ExpressionStatement exprStmt) {
        // NOTE: We currently only check for expressions of the form
        // "Foo.prototype.xyz = ..."

        AstNode expr = exprStmt.getExpression();

        // Check for "Foo.prototype.xyz = ..."
        if (expr instanceof Assignment) {
            Assignment assignment = (Assignment) expr;
            AstNode left = assignment.getLeft();

            if (left instanceof PropertyGet) {
                PropertyGet pg = (PropertyGet) left;
                if (pg.getLeft() instanceof PropertyGet) {
                    PropertyGet pg2 = (PropertyGet) pg.getLeft();
                    if (pg2.getLeft() instanceof Name
                            && pg2.getRight() instanceof Name) {
                        Name temp = (Name) pg2.getRight();
                        if (temp.getIdentifier().equals("prototype")) {
                            String clazz = ((Name) pg2.getLeft())
                                    .getIdentifier();
                            String member = ((Name) pg.getRight())
                                    .getIdentifier();

                            JavaScriptTreeNode tn = new JavaScriptTreeNode(
                                    pg.getRight());
                            try {
                                int offs = pg.getRight().getAbsolutePosition();
                                tn.setOffset(textArea.getDocument()
                                        .createPosition(offs));
                            } catch (BadLocationException ble) { // Never happens
                                ExceptionDialog.hideException(ble);
                            }

                            boolean isFunction = assignment.getRight() instanceof FunctionNode;
                            String text = member;
                            if (isFunction) {
                                FunctionNode func = (FunctionNode) assignment
                                        .getRight();
                                text += getFunctionArgsString(func);
                            }

                            tn.setText(text);
                            tn.setIcon(IconFactory
                                    .getIcon(IconFactory.DEFAULT_FUNCTION_ICON));
                            tn.setSortPriority(JavaScriptOutlineTree.PRIORITY_FUNCTION);
                            if (prototypeAdditions == null) {
                                prototypeAdditions = new HashMap<String, List<JavaScriptTreeNode>>();
                            }
                            List<JavaScriptTreeNode> list = prototypeAdditions
                                    .get(clazz);
                            if (list == null) {
                                list = new ArrayList<JavaScriptTreeNode>();
                                prototypeAdditions.put(clazz, list);
                            }
                            list.add(tn);

                            if (isFunction) {
                                JavaScriptTreeNode prevScopeTreeNode = curScopeTreeNode;
                                curScopeTreeNode = tn;
                                FunctionNode func = (FunctionNode) assignment
                                        .getRight();
                                func.getBody().visit(this);
                                curScopeTreeNode = prevScopeTreeNode;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    private boolean visitFunction(FunctionNode fn) {
        Name funcName = fn.getFunctionName();

        // Happens with certain syntax errors, such as
        // "function function foo() {"
        if (funcName != null) {
            String text = fn.getName() + getFunctionArgsString(fn);

            JavaScriptTreeNode tn = new JavaScriptTreeNode(funcName);
            try {
                int offs = funcName.getAbsolutePosition();
                tn.setOffset(textArea.getDocument().createPosition(offs));
            } catch (BadLocationException ble) { // Never happens
                ExceptionDialog.hideException(ble);
            }
            tn.setText(text);
            tn.setIcon(IconFactory.getIcon(IconFactory.DEFAULT_FUNCTION_ICON));
            tn.setSortPriority(JavaScriptOutlineTree.PRIORITY_FUNCTION);

            curScopeTreeNode.add(tn);

            curScopeTreeNode = tn;
            fn.getBody().visit(this);
            curScopeTreeNode = (JavaScriptTreeNode) curScopeTreeNode
                    .getParent();
        }

        // Never visit children; we do this manually so we know when scope ends
        return false;
    }

    private boolean visitVariableDeclaration(VariableDeclaration varDec) {
        List<VariableInitializer> vars = varDec.getVariables();
        for (VariableInitializer var : vars) {
            Name varNameNode = null;
            String varName = null;
            AstNode target = var.getTarget();
            switch (target.getType()) {
            case Token.NAME:
                varNameNode = (Name) target;
                varName = varNameNode.getIdentifier();
                break;
            default:
                System.out.println("... Unknown var target type: "
                        + target.getClass());
                varName = "?";
                break;
            }

            boolean isFunction = var.getInitializer() instanceof FunctionNode;
            JavaScriptTreeNode tn = new JavaScriptTreeNode(varNameNode);
            try {
                int offs = varNameNode.getAbsolutePosition();
                tn.setOffset(textArea.getDocument().createPosition(offs));
            } catch (BadLocationException ble) { // Never happens
                ExceptionDialog.hideException(ble);
            }
            if (isFunction) {
                FunctionNode func = (FunctionNode) var.getInitializer();
                tn.setText(varName + getFunctionArgsString(func));
                tn.setIcon(IconFactory.getIcon(IconFactory.DEFAULT_CLASS_ICON));
                tn.setSortPriority(JavaScriptOutlineTree.PRIORITY_FUNCTION);
                curScopeTreeNode.add(tn);

                curScopeTreeNode = tn;
                func.getBody().visit(this);
                curScopeTreeNode = (JavaScriptTreeNode) curScopeTreeNode
                        .getParent();
            } else {
                tn.setText(varName);
                tn.setIcon(IconFactory.getIcon(IconFactory.LOCAL_VARIABLE_ICON));
                tn.setSortPriority(JavaScriptOutlineTree.PRIORITY_VARIABLE);
                curScopeTreeNode.add(tn);
            }
        }

        return false;
    }
}