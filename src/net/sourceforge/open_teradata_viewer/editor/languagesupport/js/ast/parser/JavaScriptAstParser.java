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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.IParameterizedCompletion.Parameter;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.JavaScriptHelper;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.Logger;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.SourceCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.CodeBlock;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.JavaScriptFunctionDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.JavaScriptVariableDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.TypeDeclarationOptions;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.ArrayTypeDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.TypeDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.ecma.TypeDeclarations;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.completion.IJSCompletionUI;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.completion.JavaScriptInScriptFunctionCompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.resolver.JavaScriptResolver;
import sun.org.mozilla.javascript.internal.Node;
import sun.org.mozilla.javascript.internal.Token;
import sun.org.mozilla.javascript.internal.ast.Assignment;
import sun.org.mozilla.javascript.internal.ast.AstNode;
import sun.org.mozilla.javascript.internal.ast.AstRoot;
import sun.org.mozilla.javascript.internal.ast.CatchClause;
import sun.org.mozilla.javascript.internal.ast.DoLoop;
import sun.org.mozilla.javascript.internal.ast.ExpressionStatement;
import sun.org.mozilla.javascript.internal.ast.ForInLoop;
import sun.org.mozilla.javascript.internal.ast.ForLoop;
import sun.org.mozilla.javascript.internal.ast.FunctionNode;
import sun.org.mozilla.javascript.internal.ast.IfStatement;
import sun.org.mozilla.javascript.internal.ast.InfixExpression;
import sun.org.mozilla.javascript.internal.ast.Name;
import sun.org.mozilla.javascript.internal.ast.NodeVisitor;
import sun.org.mozilla.javascript.internal.ast.ReturnStatement;
import sun.org.mozilla.javascript.internal.ast.SwitchCase;
import sun.org.mozilla.javascript.internal.ast.SwitchStatement;
import sun.org.mozilla.javascript.internal.ast.TryStatement;
import sun.org.mozilla.javascript.internal.ast.VariableDeclaration;
import sun.org.mozilla.javascript.internal.ast.VariableInitializer;
import sun.org.mozilla.javascript.internal.ast.WhileLoop;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class JavaScriptAstParser extends JavaScriptParser {

    private ArrayList<ProcessFunctionType> functions;

    public JavaScriptAstParser(SourceCompletionProvider provider, int dot,
            TypeDeclarationOptions options) {
        super(provider, dot, options);
        functions = new ArrayList<ProcessFunctionType>();
    }

    @Override
    public CodeBlock convertAstNodeToCodeBlock(AstRoot root, Set set,
            String entered) {
        functions.clear();
        CodeBlock block = new CodeBlock(0);
        addCodeBlock(root, set, entered, block, Integer.MAX_VALUE);
        setFunctionValues();
        provider.getLanguageSupport().getJavaScriptParser()
                .setVariablesAndFunctions(provider.getVariableResolver());
        return block;
    }

    private void setFunctionValues() {
        // Iterate through any found functions and set their types
        for (ProcessFunctionType type : functions) {
            type.dec.setTypeDeclaration(type.typeNode);
        }
    }

    /**
     * For each child of parent AstNode add a new code block and add completions
     * for each block of code.
     * 
     * @param parent AstNode to iterate children.
     * @param set Completions set to add to.
     * @param entered Text entered.
     * @param codeBlock Parent CodeBlock.
     * @param offset CodeBlock offset.
     */
    private void addCodeBlock(Node parent, Set set, String entered,
            CodeBlock codeBlock, int offset) {
        if (parent == null) {
            return;
        }

        Node child = parent.getFirstChild();

        while (child != null) {
            CodeBlock childBlock = codeBlock;
            if (child instanceof AstNode) {
                AstNode node = (AstNode) child;
                int start = node.getAbsolutePosition();
                childBlock = codeBlock.addChildCodeBlock(start);
                childBlock.setEndOffset(offset);
            }
            iterateNode((AstNode) child, set, entered, childBlock, offset);

            child = child.getNext();
        }
    }

    protected void iterateNode(AstNode child, Set set, String entered,
            CodeBlock block, int offset) {
        if (child == null) {
            return;
        }

        Logger.log(JavaScriptHelper.convertNodeToSource(child));
        Logger.log(child.shortName());

        if (JavaScriptHelper.isInfixOnly(child)) {
            // Will need to look into it
            processInfix(child, block, set, entered, offset);
        } else {
            switch (child.getType()) {
            case Token.FUNCTION:
                processFunctionNode(child, block, set, entered, offset);
                break;
            case Token.VAR:
                processVariableNode(child, block, set, entered, offset);
                break;
            case Token.FOR: {
                processForNode(child, block, set, entered, offset);
                break;
            }
            case Token.WHILE: {
                processWhileNode(child, block, set, entered, offset);
                break;
            }
            case Token.BLOCK: {
                addCodeBlock(child, set, entered, block, offset);
                break;
            }
            case Token.ASSIGN: {
                reassignVariable(child, block, offset);
                break;
            }
            case Token.EXPR_VOID: {
                processExpressionNode(child, block, set, entered, offset);
                break;
            }
            case Token.IF: {
                processIfThenElse(child, block, set, entered, offset);
                break;
            }
            case Token.TRY: {
                processTryCatchNode(child, block, set, entered, offset);
                break;
            }
            case Token.DO: {
                processDoNode(child, block, set, entered, offset);
                break;
            }
            case Token.SWITCH:
                processSwitchNode(child, block, set, entered, offset);
                break;
            case Token.CASE:
                processCaseNode(child, block, set, entered, offset);
                break;

            // Ignore
            case Token.BREAK:
            case Token.CONTINUE:
            case Token.CALL:
            case Token.EMPTY:
            case Token.NAME:
            case Token.CATCH:
            case Token.ERROR:
            case Token.RETURN:
            case Token.NEW:
            case Token.GETPROP:
                // XML support -- ignore these
            case Token.DEFAULTNAMESPACE:
            case Token.XMLATTR:
                break;
            case Token.EXPR_RESULT:
                processExpressionStatement(child, block, set, entered, offset);
                break;
            default:
                Logger.log("Unhandled: " + child.getClass() + " (\""
                        + child.toString() + "\":" + child.getLineno());
                break;
            }
        }
    }

    private void processExpressionStatement(Node child, CodeBlock block,
            Set set, String entered, int offset) {
        ExpressionStatement exp = (ExpressionStatement) child;

        AstNode expNode = exp.getExpression();
        iterateNode(expNode, set, entered, block, offset);
    }

    private void reassignVariable(AstNode assign, CodeBlock block,
            int locationOffSet) {
        Assignment assignNode = (Assignment) assign;
        // Maybe a variable
        AstNode leftNode = assignNode.getLeft();
        // Assign the variable to
        AstNode rightNode = assignNode.getRight();

        String name = leftNode.getType() == Token.NAME ? ((Name) leftNode)
                .getIdentifier() : null;
        if (name != null) {
            int start = assignNode.getAbsolutePosition();
            int offset = start + assignNode.getLength();
            // Check that the caret position is below the dot before looking for
            // the variable
            if (offset <= dot) {
                JavaScriptVariableDeclaration dec = provider
                        .getVariableResolver().findDeclaration(name, dot);
                if (dec != null
                        && (dec.getCodeBlock() == null || dec.getCodeBlock()
                                .contains(dot))) {
                    // Set reference to new type
                    dec.setTypeDeclaration(rightNode, isPreProcessing());
                } else {
                    // Assume we can add variable as we are trying to assign to
                    // name
                    addVariableToResolver(leftNode, rightNode, block,
                            locationOffSet);
                }
            }
        }
    }

    private void addVariableToResolver(AstNode name, AstNode target,
            CodeBlock block, int offset) {
        JavaScriptVariableDeclaration dec = extractVariableFromNode(name,
                block, offset, target);
        if (dec != null && target != null
                && JavaScriptHelper.canResolveVariable(name, target)) {
            dec.setTypeDeclaration(target);
        } else {
            if (dec != null) {
                dec.setTypeDeclaration(provider.getTypesFactory()
                        .getDefaultTypeDeclaration());
            }
        }
        if (dec != null) {
            if (canAddVariable(block)) {
                // Add declaration to resolver if one is found

                if (isPreProcessing()) {
                    block.setStartOffSet(0);
                    // Set the owner document
                    dec.setTypeDeclarationOptions(options);
                    provider.getVariableResolver()
                            .addPreProcessingVariable(dec);
                } else {
                    provider.getVariableResolver().addLocalVariable(dec);
                }
            }
        }
    }

    private void processCaseNode(Node child, CodeBlock block, Set set,
            String entered, int offset) {
        SwitchCase switchCase = (SwitchCase) child;
        List<AstNode> statements = switchCase.getStatements();
        int start = switchCase.getAbsolutePosition();
        offset = start + switchCase.getLength();
        if (canProcessNode(switchCase)) {
            block = block.addChildCodeBlock(start);
            block.setEndOffset(offset);
            if (statements != null) {
                for (AstNode node : statements) {
                    iterateNode(node, set, entered, block, offset);
                }
            }
        }
    }

    /** Extract local variables from switch node. */
    private void processSwitchNode(Node child, CodeBlock block, Set set,
            String entered, int offset) {
        SwitchStatement switchStatement = (SwitchStatement) child;
        if (canProcessNode(switchStatement)) {
            List<SwitchCase> cases = switchStatement.getCases();
            if (cases != null) {
                for (SwitchCase switchCase : cases) {
                    iterateNode(switchCase, set, entered, block, offset);
                }
            }
        }
    }

    /** Extract variables from try/catch node(s). */
    private void processTryCatchNode(Node child, CodeBlock block, Set set,
            String entered, int offset) {
        TryStatement tryStatement = (TryStatement) child;
        if (canProcessNode(tryStatement)) {
            offset = tryStatement.getTryBlock().getAbsolutePosition()
                    + tryStatement.getTryBlock().getLength();
            addCodeBlock(tryStatement.getTryBlock(), set, entered, block,
                    offset);
            // Iterate catch
            for (int i = 0; i < tryStatement.getCatchClauses().size(); i++) {
                CatchClause clause = tryStatement.getCatchClauses().get(i);
                if (canProcessNode(clause)) {
                    offset = clause.getAbsolutePosition() + clause.getLength();
                    CodeBlock catchBlock = block.getParent().addChildCodeBlock(
                            clause.getAbsolutePosition());
                    catchBlock.setEndOffset(offset);
                    AstNode target = clause.getVarName();

                    JavaScriptVariableDeclaration dec = extractVariableFromNode(
                            target, catchBlock, offset);
                    if (dec != null) {
                        dec.setTypeDeclaration(clause);
                    }

                    addCodeBlock(clause.getBody(), set, entered, catchBlock,
                            offset);
                }
            }
            // Now sort out finally block
            if (tryStatement.getFinallyBlock() != null) {
                AstNode finallyNode = tryStatement.getFinallyBlock();
                if (canProcessNode(finallyNode)) {
                    offset = finallyNode.getAbsolutePosition()
                            + finallyNode.getLength();
                    CodeBlock finallyBlock = block.getParent()
                            .addChildCodeBlock(
                                    tryStatement.getFinallyBlock()
                                            .getAbsolutePosition());
                    addCodeBlock(finallyNode, set, entered, finallyBlock,
                            offset);
                    finallyBlock.setEndOffset(offset);
                }
            }
        }
    }

    /** Extract variables from if/else node(s). */
    private void processIfThenElse(Node child, CodeBlock block, Set set,
            String entered, int offset) {
        IfStatement ifStatement = (IfStatement) child;
        if (canProcessNode(ifStatement)) {
            offset = ifStatement.getAbsolutePosition()
                    + ifStatement.getLength();
            addCodeBlock(ifStatement.getThenPart(), set, entered, block, offset);
            AstNode elseNode = ifStatement.getElsePart();
            if (elseNode != null) {
                int start = elseNode.getAbsolutePosition();
                CodeBlock childBlock = block.addChildCodeBlock(start);
                offset = start + elseNode.getLength();
                iterateNode(elseNode, set, entered, childBlock, offset);
                childBlock.setEndOffset(offset);
            }
        }
    }

    /** Extract completions from expression node. */
    private void processExpressionNode(Node child, CodeBlock block, Set set,
            String entered, int offset) {
        if (child instanceof ExpressionStatement) {
            ExpressionStatement expr = (ExpressionStatement) child;
            iterateNode(expr.getExpression(), set, entered, block, offset);
        }
    }

    /** Extract while loop from node and add new code block. */
    private void processWhileNode(Node child, CodeBlock block, Set set,
            String entered, int offset) {
        WhileLoop loop = (WhileLoop) child;
        if (canProcessNode(loop)) {
            offset = loop.getAbsolutePosition() + loop.getLength();
            addCodeBlock(loop.getBody(), set, entered, block, offset);
        }
    }

    /** Extract while loop from node and add new code block. */
    private void processDoNode(Node child, CodeBlock block, Set set,
            String entered, int offset) {
        DoLoop loop = (DoLoop) child;
        if (canProcessNode(loop)) {
            offset = loop.getAbsolutePosition() + loop.getLength();
            addCodeBlock(loop.getBody(), set, entered, block, offset);
        }
    }

    /** Extract variable from binary operator e.g <, >, = etc.. */
    private void processInfix(Node child, CodeBlock block, Set set,
            String entered, int offset) {
        InfixExpression epre = (InfixExpression) child;
        AstNode target = epre.getLeft();
        if (canProcessNode(target)) {
            extractVariableFromNode(target, block, offset);
            addCodeBlock(epre, set, entered, block, offset);
        }
    }

    /**
     * Add function to completions set and extract local variables to add to
     * code block.
     */
    private void processFunctionNode(Node child, CodeBlock block,
            Set<IJSCompletionUI> set, String entered, int offset) {
        FunctionNode fn = (FunctionNode) child;
        String jsdoc = fn.getJsDoc();
        TypeDeclaration returnType = getFunctionType(fn);
        JavaScriptInScriptFunctionCompletion fc = new JavaScriptInScriptFunctionCompletion(
                provider, fn.getName(), returnType);
        fc.setShortDescription(jsdoc);
        offset = fn.getAbsolutePosition() + fn.getLength();

        if (fn.getParamCount() > 0) {
            List<AstNode> fnParams = fn.getParams();
            List<Parameter> params = new ArrayList<Parameter>();
            for (int i = 0; i < fn.getParamCount(); i++) {
                String paramName = null;
                AstNode node = fnParams.get(i);
                switch (node.getType()) {
                case Token.NAME:
                    paramName = ((Name) node).getIdentifier();
                    break;
                default:
                    break;
                }
                Parameter param = new Parameter(null, paramName);
                params.add(param);

                if (!isPreProcessing() && canProcessNode(fn)) {
                    JavaScriptVariableDeclaration dec = extractVariableFromNode(
                            node, block, offset);
                    provider.getVariableResolver().addLocalVariable(dec);
                }
            }
            fc.setParams(params);
        }

        if (isPreProcessing()) {
            block.setStartOffSet(0);
        }

        if (isPreProcessing()) {
            JavaScriptFunctionDeclaration function = createJavaScriptFunction(
                    fc.getLookupName(), offset, block, returnType, fn);
            function.setTypeDeclarationOptions(options);
            provider.getVariableResolver().addPreProcessingFunction(function);
        } else {
            provider.getVariableResolver().addLocalFunction(
                    createJavaScriptFunction(fc.getLookupName(), offset, block,
                            returnType, fn));
        }

        // Get body
        addCodeBlock(fn.getBody(), set, entered, block, offset);
        if (entered.indexOf('.') == -1) {
            set.add(fc);
        }
    }

    private JavaScriptFunctionDeclaration createJavaScriptFunction(
            String lookupName, int offset, CodeBlock block,
            TypeDeclaration returnType, FunctionNode fn) {
        Name name = fn.getFunctionName();
        JavaScriptFunctionDeclaration function = new JavaScriptFunctionDeclaration(
                lookupName, offset, block, returnType);
        if (name != null) {
            int start = name.getAbsolutePosition();
            int end = start + name.getLength();
            function.setStartOffset(start);
            function.setEndOffset(end);
            function.setFunctionName(fn.getName());
        }
        return function;
    }

    private boolean canProcessNode(AstNode node) {
        int start = node.getAbsolutePosition();
        int offset = start + node.getLength();
        return dot >= start && dot < offset;
    }

    private TypeDeclaration getFunctionType(FunctionNode fn) {
        FunctionReturnVisitor visitor = new FunctionReturnVisitor();
        fn.visit(visitor);
        return visitor.getCommonReturnType();
    }

    /** Extract variable from node and add to code block. */
    private void processVariableNode(Node child, CodeBlock block,
            Set<IJSCompletionUI> set, String entered, int offset) {
        // Check block can resolve variable or is pre-processing variables
        if (block.contains(dot) || isPreProcessing()) {
            VariableDeclaration varDec = (VariableDeclaration) child;
            List<VariableInitializer> vars = varDec.getVariables();
            for (VariableInitializer var : vars) {
                extractVariableFromNode(var, block, offset);
            }
        }
    }

    /**
     * Extract code from Token.FOR and add completions, then parse body of for
     * loop.
     */
    private void processForNode(Node child, CodeBlock block, Set set,
            String entered, int offset) {
        if (child instanceof ForLoop) {
            ForLoop loop = (ForLoop) child;
            offset = loop.getAbsolutePosition() + loop.getLength();
            if (canProcessNode(loop)) {
                iterateNode(loop.getInitializer(), set, entered, block, offset);
                addCodeBlock(loop.getBody(), set, entered, block, offset);
            }
        } else if (child instanceof ForInLoop) {
            ForInLoop loop = (ForInLoop) child;
            offset = loop.getAbsolutePosition() + loop.getLength();
            if (canProcessNode(loop)) {
                AstNode iteratedObject = loop.getIteratedObject();
                AstNode iterator = loop.getIterator();
                if (iterator != null) {
                    if (iterator.getType() == Token.VAR) // Expected
                    {
                        VariableDeclaration vd = (VariableDeclaration) iterator;
                        List<VariableInitializer> variables = vd.getVariables();
                        if (variables.size() == 1) // Expected
                        {
                            VariableInitializer vi = variables.get(0);
                            if (loop.isForEach()) {
                                extractVariableForForEach(vi, block, offset,
                                        iteratedObject);
                            } else {
                                extractVariableForForIn(vi, block, offset,
                                        iteratedObject);
                            }
                        }
                    }
                }
                addCodeBlock(loop.getBody(), set, entered, block, offset);
            }
        }
    }

    /**
     * Extract the variable from the Variable initializer and set the Type.
     * 
     * @param initializer AstNode from which to extract the variable.
     * @param block Code block to add the variable too.
     * @param offset Position of the variable in code.
     */
    private void extractVariableFromNode(VariableInitializer initializer,
            CodeBlock block, int offset) {
        AstNode target = initializer.getTarget();

        if (target != null) {
            addVariableToResolver(target, initializer.getInitializer(), block,
                    offset);
        }
    }

    /**
     * Extract variable for each in loop. If the iteratedObject is an Array,
     * then resolve the variable to the array type otherwise set to iterator
     * object type.
     */
    private void extractVariableForForEach(VariableInitializer initializer,
            CodeBlock block, int offset, AstNode iteratedObject) {
        AstNode target = initializer.getTarget();
        if (target != null) {
            JavaScriptVariableDeclaration dec = extractVariableFromNode(target,
                    block, offset);
            if (dec != null
                    && iteratedObject != null
                    && JavaScriptHelper.canResolveVariable(target,
                            iteratedObject)) {

                // Resolve the iterated object
                JavaScriptResolver resolver = provider.getJavaScriptEngine()
                        .getJavaScriptResolver(provider);
                if (resolver != null) {
                    TypeDeclaration iteratorDec = resolver
                            .resolveNode(iteratedObject);
                    if (iteratorDec instanceof ArrayTypeDeclaration) {
                        // Set type to array type dec
                        dec.setTypeDeclaration(((ArrayTypeDeclaration) iteratorDec)
                                .getArrayType());
                    } else {
                        dec.setTypeDeclaration(iteratorDec);
                    }
                }

                if (canAddVariable(block)) {
                    provider.getVariableResolver().addLocalVariable(dec);
                }
            }
        }
    }

    /**
     * Extract variable for in loop. If the iteratedObject is an Array, then
     * assume the variable to be a number otherwise do not attempt to resolve.
     */
    private void extractVariableForForIn(VariableInitializer initializer,
            CodeBlock block, int offset, AstNode iteratedObject) {
        AstNode target = initializer.getTarget();
        if (target != null) {
            JavaScriptVariableDeclaration dec = extractVariableFromNode(target,
                    block, offset);
            if (dec != null
                    && iteratedObject != null
                    && JavaScriptHelper.canResolveVariable(target,
                            iteratedObject)) {
                // Resolve the iterated object
                JavaScriptResolver resolver = provider.getJavaScriptEngine()
                        .getJavaScriptResolver(provider);
                if (resolver != null) {
                    TypeDeclaration iteratorDec = resolver
                            .resolveNode(iteratedObject);
                    if (iteratorDec instanceof ArrayTypeDeclaration) {
                        // Always assume a number for arrays
                        dec.setTypeDeclaration(provider.getTypesFactory()
                                .getTypeDeclaration(
                                        TypeDeclarations.ECMA_NUMBER));
                    } else {
                        dec.setTypeDeclaration(provider.getTypesFactory()
                                .getDefaultTypeDeclaration());
                    }
                }

                if (canAddVariable(block)) {
                    provider.getVariableResolver().addLocalVariable(dec);
                }
            }
        }
    }

    private boolean canAddVariable(CodeBlock block) {
        if (!isPreProcessing()) {
            return true;
        }
        // Else check parent is base block
        CodeBlock parent = block.getParent();
        return parent != null && parent.getStartOffset() == 0;
    }

    /**
     * Extract the variable from the Rhino node and add to the CodeBlock.
     * 
     * @param node AstNode node from which to extract the variable.
     * @param block Code block to add the variable too.
     * @param offset Position of the variable in code.
     */
    private JavaScriptVariableDeclaration extractVariableFromNode(AstNode node,
            CodeBlock block, int offset) {
        return extractVariableFromNode(node, block, offset, null);
    }

    /**
     * Extract the variable from the Rhino node and add to the CodeBlock.
     * 
     * @param node AstNode node from which to extract the variable.
     * @param block Code block to add the variable too.
     * @param offset Position of the variable in code.
     * @param initializer Node associated with variable.
     */
    private JavaScriptVariableDeclaration extractVariableFromNode(AstNode node,
            CodeBlock block, int offset, AstNode initializer) {
        JavaScriptVariableDeclaration dec = null;
        // Check that the caret position is below the dot before looking for the
        // variable
        if (node != null) {
            switch (node.getType()) {
            case Token.NAME:
                Name name = (Name) node;
                dec = new JavaScriptVariableDeclaration(name.getIdentifier(),
                        offset, provider, block);
                dec.setStartOffset(name.getAbsolutePosition());
                dec.setEndOffset(name.getAbsolutePosition() + name.getLength());
                if (initializer != null && initializer.getType() == Token.CALL) {
                    // Set the type node later for functions as these sometimes
                    // cannot be resolved until after the entire document is
                    // parsed
                    ProcessFunctionType func = new ProcessFunctionType();
                    func.dec = dec;
                    func.typeNode = initializer;
                    functions.add(func);
                }
                if (initializer == null
                        || JavaScriptHelper.canResolveVariable(name,
                                initializer)) {
                    block.addVariable(dec);
                }
                break;
            default:
                Logger.log("... Unknown var target type: " + node.getClass());
                break;
            }
        }

        return dec;
    }

    public SourceCompletionProvider getProvider() {
        return provider;
    }

    public int getDot() {
        return dot;
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    private class FunctionReturnVisitor implements NodeVisitor {

        private ArrayList<ReturnStatement> returnStatements = new ArrayList<ReturnStatement>();

        @Override
        public boolean visit(AstNode node) {
            switch (node.getType()) {
            case Token.RETURN:
                returnStatements.add((ReturnStatement) node);
                break;
            }
            return true;
        }

        /**
         * Iterate through all the return types and check they are all the same,
         * otherwise return no type.
         */
        public TypeDeclaration getCommonReturnType() {
            TypeDeclaration commonType = null;
            for (ReturnStatement rs : returnStatements) {
                AstNode returnValue = rs.getReturnValue();
                // Resolve the node
                TypeDeclaration type = provider.getJavaScriptEngine()
                        .getJavaScriptResolver(provider)
                        .resolveNode(returnValue);
                if (commonType == null) {
                    commonType = type;
                } else {
                    if (!commonType.equals(type)) {
                        commonType = provider.getTypesFactory()
                                .getDefaultTypeDeclaration();
                        break; // Not matching
                    }
                }
            }
            return commonType;
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    static class ProcessFunctionType {

        AstNode typeNode;
        JavaScriptVariableDeclaration dec;

    }
}