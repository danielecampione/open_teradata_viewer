/*
 * Open Teradata Viewer ( editor language support java jc parser )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.parser;

import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.AbstractTypeDeclarationNode;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.CodeBlock;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.CompilationUnit;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.EnumBody;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.EnumDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.Field;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.FormalParameter;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.ITypeDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.ITypeDeclarationContainer;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.ImportDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.LocalVariable;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.Method;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.NormalClassDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.NormalInterfaceDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.Package;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.Annotation;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.Modifiers;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.Type;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.TypeArgument;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.TypeParameter;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.IJavaLexicalToken;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.ITokenTypes;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.Scanner;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.notices.ParserNotice;

/**
 * Generates an abstract syntax tree for a Java source file.
 * 
 * @author D. Campione
 * 
 */
public class ASTFactory implements ITokenTypes {

    private static final boolean DEBUG = false;

    /** Whether the next member (or class, interface or enum) is deprecated. */
    private boolean nextMemberDeprecated;

    public ASTFactory() {
    }

    private boolean checkDeprecated() {
        boolean deprecated = nextMemberDeprecated;
        nextMemberDeprecated = false;
        return deprecated;
    }

    /**
     * Checks whether a local variable's name collides with a local variable
     * defined earlier. Note that this method assumes that it is called
     * immediately whenever a variable is parsed, thus any other variables
     * declared in a code block were declared before the one being checked.
     *
     * @param cu The compilation unit.
     * @param lVar The just-scanned local variable.
     * @param block The code block the variable is in.
     * @param m The method the (possibly nested) code block <code>block</code>
     *        is in or <code>null</code> for none.
     */
    private void checkForDuplicateLocalVarNames(CompilationUnit cu,
            IJavaLexicalToken lVar, CodeBlock block, Method m) {
        String name = lVar.getLexeme();
        boolean found = false;

        // See if a local variable defined previously in this block has the same
        // name
        for (int i = 0; i < block.getLocalVarCount(); i++) {
            LocalVariable otherLocal = block.getLocalVar(i);
            if (name.equals(otherLocal.getName())) {
                cu.addParserNotice(lVar, "Duplicate local variable: " + name);
                found = true;
                break;
            }
        }

        // If not..
        if (!found) {
            // If this was a nested code block, check previously-defined
            // variables in the parent block
            if (block.getParent() != null) {
                checkForDuplicateLocalVarNames(cu, lVar, block.getParent(), m);
            }

            // If this was the highest-level code block, if we're in the body of
            // a method, check the method's parameters
            else if (m != null) {
                for (int i = 0; i < m.getParameterCount(); i++) {
                    FormalParameter param = m.getParameter(i);
                    if (name.equals(param.getName())) {
                        cu.addParserNotice(lVar, "Duplicate local variable: "
                                + name);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Assumes <tt>t</tt> is the actual '<tt>@foobar</tt>' annotation token.
     *
     * @throws IOException
     */
    private Annotation _getAnnotation(CompilationUnit cu, Scanner s)
            throws IOException {
        s.yylexNonNull(ANNOTATION_START, "Annotation expected");
        Type type = _getType(cu, s);

        if ("Deprecated".equals(type.toString())) {
            nextMemberDeprecated = true;
        }

        if (s.yyPeekCheckType() == SEPARATOR_LPAREN) {
            s.yylex();
            s.eatThroughNextSkippingBlocks(SEPARATOR_RPAREN);
        }

        Annotation a = new Annotation(type);
        return a;
    }

    private CodeBlock _getBlock(CompilationUnit cu, CodeBlock parent, Method m,
            Scanner s, boolean isStatic) throws IOException {
        return _getBlock(cu, parent, m, s, isStatic, 1);
    }

    /**
     * Parses a block of code. This should not be called.
     *
     * @param parent The parent code block or <code>null</code> if none (i.e.
     *        this is the body of a method, a static initializer block, etc..).
     * @param m The method containing this block or <code>null</code> if this
     *        block is not part of a method.
     * @param s The scanner.
     * @param isStatic Whether this is a static code block.
     * @param depth The nested depth of this code block.
     */
    private CodeBlock _getBlock(CompilationUnit cu, CodeBlock parent, Method m,
            Scanner s, boolean isStatic, int depth) throws IOException {
        log("Entering _getBlock() (" + depth + ")");

        IJavaLexicalToken t = s.yylexNonNull(SEPARATOR_LBRACE, "'{' expected");
        CodeBlock block = new CodeBlock(isStatic, s.createOffset(t.getOffset()));
        block.setParent(parent);
        boolean atStatementStart = true;

        OUTER: while (true) {
            // Don't bail if they have unmatched parens (for example), just
            // return the current status of the block
            //t = s.yylexNonNull("Unexpected end of input");
            if ((t = s.yylex()) == null) {
                log("Exiting _getBlock() - eos (" + depth + ")");
                block.setDeclarationEndOffset(s.createOffset(s.getOffset()));
                return block;
            }

            int type = t.getType();
            boolean isFinal = false;

            switch (type) {
            case SEPARATOR_LBRACE:
                s.yyPushback(t);
                CodeBlock child = _getBlock(cu, block, m, s, isStatic,
                        depth + 1);
                block.add(child);
                atStatementStart = true;
                break;

            case SEPARATOR_RBRACE:
                block.setDeclarationEndOffset(s.createOffset(t.getOffset()));
                break OUTER;

            case KEYWORD_TRY:
                t = s.yyPeekNonNull(SEPARATOR_LBRACE, SEPARATOR_LPAREN,
                        "'{' or '(' expected");
                if (t.getType() == SEPARATOR_LPAREN) { // Auto-closeable stuff
                    s.eatParenPairs();
                }
                s.yyPeekNonNull(SEPARATOR_LBRACE, "'{' expected");
                CodeBlock tryBlock = _getBlock(cu, block, m, s, isStatic,
                        depth + 1);
                block.add(tryBlock);
                while (s.yyPeekCheckType() == KEYWORD_CATCH
                        && s.yyPeekCheckType(2) == SEPARATOR_LPAREN) {
                    s.yylex(); // catch
                    s.yylex(); // lparen
                    Type exType = null;
                    IJavaLexicalToken var = null;
                    boolean multiCatch = false;
                    do {
                        isFinal = false;
                        IJavaLexicalToken temp = s.yyPeekNonNull(IDENTIFIER,
                                KEYWORD_FINAL, "Throwable type expected");
                        if (temp.isType(KEYWORD_FINAL)) {
                            isFinal = true;
                            s.yylex();
                        }
                        s.yyPeekNonNull(IDENTIFIER,
                                "Variable declarator expected");
                        exType = _getType(cu, s); // Not good for multi-catch!
                        var = s.yylexNonNull(IDENTIFIER, OPERATOR_BITWISE_OR,
                                "Variable declarator expected");
                        multiCatch |= var.isType(OPERATOR_BITWISE_OR);
                    } while (var.isType(OPERATOR_BITWISE_OR));
                    s.yylexNonNull(SEPARATOR_RPAREN, "')' expected");
                    s.yyPeekNonNull(SEPARATOR_LBRACE, "'{' expected");
                    CodeBlock catchBlock = _getBlock(cu, block, m, s, false,
                            depth);
                    int offs = var.getOffset(); // Not actually in block
                    if (multiCatch) {
                        exType = new Type("java");
                        exType.addIdentifier("lang", null);
                        exType.addIdentifier("Throwable", null);
                    }
                    LocalVariable localVar = new LocalVariable(s, isFinal,
                            exType, offs, var.getLexeme());
                    checkForDuplicateLocalVarNames(cu, var, block, m);
                    catchBlock.addLocalVariable(localVar);
                    block.add(catchBlock);
                }
                break;

            case KEYWORD_FOR:
                // Fall through
            case KEYWORD_WHILE:
                int nextType = s.yyPeekCheckType();
                while (nextType != -1 && nextType != SEPARATOR_LPAREN) {
                    t = s.yylex(); // Grab the (unexpected) token
                    if (t != null) { // Should always be true
                        ParserNotice pn = new ParserNotice(t,
                                "Unexpected token");
                        cu.addParserNotice(pn);
                    }
                    nextType = s.yyPeekCheckType();
                }
                if (nextType == SEPARATOR_LPAREN) {
                    s.eatParenPairs();
                }
                nextType = s.yyPeekCheckType();
                if (nextType == SEPARATOR_LBRACE) {
                    child = _getBlock(cu, block, m, s, isStatic, depth + 1);
                    block.add(child);
                    atStatementStart = true;
                }
                break;

            case KEYWORD_FINAL:
                isFinal = true;
                t = s.yylexNonNull("Unexpected end of file");
                // Fall through

            default:
                if (t.isType(SEPARATOR_SEMICOLON)) {
                    atStatementStart = true;
                    break;
                } else if (atStatementStart
                        && (t.isBasicType() || (t.isIdentifier()))) {
                    s.yyPushback(t);
                    Type varType = null;
                    try {
                        varType = _getType(cu, s, true);
                    } catch (IOException ioe) { // Not a var declaration
                        s.eatUntilNext(SEPARATOR_SEMICOLON, SEPARATOR_LBRACE,
                                SEPARATOR_RBRACE);
                        // Only needed if ended on ';' or '}' but..
                        atStatementStart = true;
                        break;
                    }
                    if (s.yyPeekCheckType() == IDENTIFIER) {
                        while ((t = s.yylexNonNull(
                                IDENTIFIER,
                                "Variable name expected (type=="
                                        + varType.toString() + ")")) != null) {
                            int arrayDepth = s.skipBracketPairs();
                            varType.incrementBracketPairCount(arrayDepth);
                            String varDec = varType.toString() + " "
                                    + t.getLexeme();
                            log(">>> Variable -- " + varDec + " (line "
                                    + t.getLine() + ")");
                            int offs = t.getOffset();
                            String name = t.getLexeme();
                            LocalVariable lVar = new LocalVariable(s, isFinal,
                                    varType, offs, name);
                            checkForDuplicateLocalVarNames(cu, t, block, m);
                            block.addLocalVariable(lVar);
                            nextType = s.yyPeekCheckType();
                            // A "valid" nextType would be '=', ',' or ';'.
                            // If it's an '=', skip past the assignment
                            if (nextType == OPERATOR_EQUALS) {
                                IJavaLexicalToken temp = s
                                        .eatThroughNextSkippingBlocksAndStuffInParens(
                                                SEPARATOR_COMMA,
                                                SEPARATOR_SEMICOLON);
                                if (temp != null) {
                                    s.yyPushback(temp);
                                }
                                nextType = s.yyPeekCheckType();
                            }
                            // If next is a comma, loop to read the next local
                            // var. Otherwise, whether or not it's valid, eat
                            // until the end of the statement
                            if (nextType != SEPARATOR_COMMA) {
                                s.eatThroughNextSkippingBlocks(SEPARATOR_SEMICOLON);
                                break;
                            }
                            s.yylex(); // Eat the comma (does nothing if EOS)
                        }
                    }
                } else {
                    atStatementStart = false;
                }
                break;
            }
        }

        log("Exiting _getBlock() (" + depth + ")");
        return block;
    }

    private void _getClassBody(CompilationUnit cu, Scanner s,
            NormalClassDeclaration classDec) throws IOException {
        log("Entering _getClassBody");

        IJavaLexicalToken t = s.yylexNonNull(SEPARATOR_LBRACE, "'{' expected");
        classDec.setBodyStartOffset(s.createOffset(t.getOffset()));

        t = s.yylexNonNull("ClassBody expected");

        while (t.getType() != SEPARATOR_RBRACE) {

            switch (t.getType()) {

            case SEPARATOR_SEMICOLON:
                break; // Do nothing

            case KEYWORD_STATIC:
                IJavaLexicalToken t2 = s
                        .yyPeekNonNull("'{' or modifier expected");
                if (t2.isType(SEPARATOR_LBRACE)) {
                    CodeBlock block = _getBlock(cu, null, null, s, true);
                    classDec.addMember(block);
                    break;
                } else { // Not "static {" => must be a member
                    s.yyPushback(t); // Put back "static"
                    Modifiers modList = _getModifierList(cu, s);
                    _getMemberDecl(cu, s, classDec, modList);
                }
                break;

            case SEPARATOR_LBRACE:
                s.yyPushback(t);
                CodeBlock block = _getBlock(cu, null, null, s, false);
                classDec.addMember(block);
                break;

            default:
                s.yyPushback(t);
                Modifiers modList = _getModifierList(cu, s);
                _getMemberDecl(cu, s, classDec, modList);
                break;

            }

            try {
                t = s.yylexNonNull("'}' expected (one)");
                classDec.setBodyEndOffset(s.createOffset(t.getOffset()));
            } catch (IOException ioe) {
                classDec.setBodyEndOffset(s.createOffset(s.getOffset()));
                int line = s.getLine();
                int col = s.getColumn();
                ParserNotice pn = new ParserNotice(line, col, 1,
                        "'}' expected (two)");
                cu.addParserNotice(pn);
                break; // No more content in file
            }
        }

        log("Exiting _getClassBody");
    }

    private ITypeDeclaration _getClassOrInterfaceDeclaration(
            CompilationUnit cu, Scanner s, ITypeDeclarationContainer addTo,
            Modifiers modList) throws IOException {
        log("Entering _getClassOrInterfaceDeclaration");
        IJavaLexicalToken t = s
                .yyPeekNonNull("class, enum, interface or @interface expected");

        if (modList == null) { // Not yet read in
            modList = _getModifierList(cu, s);
        }
        t = s.yylexNonNull("class, enum, interface or @interface expected");

        ITypeDeclaration td = null;

        switch (t.getType()) {

        case KEYWORD_CLASS:
            td = _getNormalClassDeclaration(cu, s, addTo);
            break;

        case KEYWORD_ENUM:
            td = _getEnumDeclaration(cu, s, addTo);
            break;

        case KEYWORD_INTERFACE:
            td = _getNormalInterfaceDeclaration(cu, s, addTo);
            break;

        case ANNOTATION_START:
            throw new IOException("AnnotationTypeDeclaration not implemented");

        default:
            ParserNotice notice = new ParserNotice(t,
                    "class, interface or enum expected");
            cu.addParserNotice(notice);
            // Assume we're a class to get more problems
            td = _getNormalClassDeclaration(cu, s, addTo);
            break;

        }

        ((AbstractTypeDeclarationNode) td).setModifiers(modList);
        ((AbstractTypeDeclarationNode) td).setDeprecated(checkDeprecated());

        log("Exiting _getClassOrInterfaceDeclaration");
        return td;
    }

    /**
     * Reads tokens for a Java source file from the specified lexer and returns
     * the structure of the source as an AST.
     * 
     * @param scanner The scanner to read from.
     * @return The root node of the AST.
     */
    public CompilationUnit getCompilationUnit(String name, Scanner scanner)
            throws IOException {
        CompilationUnit cu = new CompilationUnit(name);

        try {
            // Get annotations
            List<Annotation> initialAnnotations = null; // Usually none
            while (scanner.yyPeekCheckType() == ANNOTATION_START) {
                if (initialAnnotations == null) {
                    initialAnnotations = new ArrayList<Annotation>(1);
                }
                initialAnnotations.add(_getAnnotation(cu, scanner));
            }

            // Get possible "package" line
            IJavaLexicalToken t = scanner.yylex();
            if (t == null) {
                return cu;
            }
            if (t.isType(KEYWORD_PACKAGE)) {
                t = scanner.yyPeekNonNull("Identifier expected");
                int offs = t.getOffset();
                String qualifiedID = getQualifiedIdentifier(scanner);
                Package pkg = new Package(scanner, offs, qualifiedID);
                if (initialAnnotations != null) {
                    initialAnnotations = null;
                }
                cu.setPackage(pkg);
                scanner.yylexNonNull(SEPARATOR_SEMICOLON, "Semicolon expected");
                t = scanner.yylex();
            }

            // Go through any import statements
            OUTER: while (t != null && t.isType(KEYWORD_IMPORT)) {
                boolean isStatic = false;
                StringBuilder buf = new StringBuilder();
                t = scanner.yylexNonNull("Incomplete import statement");
                IJavaLexicalToken temp = null;
                int offs = 0;

                if (t.isType(KEYWORD_STATIC)) {
                    isStatic = true;
                    t = scanner.yylexNonNull("Incomplete import statement");
                }

                if (!t.isIdentifier()) {
                    cu.addParserNotice(t,
                            "Expected identifier, found: \"" + t.getLexeme()
                                    + "\"");
                    scanner.eatThroughNextSkippingBlocks(SEPARATOR_SEMICOLON);
                    // We expect "t" to be the semicolon below
                    t = scanner.getMostRecentToken();
                } else {
                    offs = t.getOffset();
                    buf.append(t.getLexeme());
                    temp = scanner.yylexNonNull(SEPARATOR_DOT,
                            SEPARATOR_SEMICOLON, "'.' or ';' expected");
                    while (temp.isType(SEPARATOR_DOT)) {
                        temp = scanner.yylexNonNull(IDENTIFIER, OPERATOR_TIMES,
                                "Identifier or '*' expected");
                        if (temp.isIdentifier()) {
                            buf.append('.').append(temp.getLexeme());
                        } else {//if (temp.getLexeme().equals("*")) {
                            buf.append(".*");
                            temp = scanner.yylex(); // We're bailing, so scan here
                            break;
                        }
                        temp = scanner.yylexNonNull(KEYWORD_IMPORT,
                                SEPARATOR_DOT, SEPARATOR_SEMICOLON,
                                "'.' or ';' expected");
                        if (temp.isType(KEYWORD_IMPORT)) {
                            cu.addParserNotice(temp, "';' expected");
                            t = temp;
                            continue OUTER;
                        }
                    }
                    t = temp;
                }

                if (temp == null || !t.isType(SEPARATOR_SEMICOLON)) {
                    throw new IOException("Semicolon expected, found " + t);
                }

                ImportDeclaration id = new ImportDeclaration(scanner, offs,
                        buf.toString(), isStatic);
                cu.addImportDeclaration(id);
                t = scanner.yylex();

            }

            // Class files aren't required to have ITypeDeclarations
            if (t == null) {
                return cu;
            }

            scanner.yyPushback(t);
            //ITypeDeclaration td = null;
            while ((/*td = */_getTypeDeclaration(cu, scanner)) != null) {
                if (initialAnnotations != null) {
                    //	td.addAnnotations(initialAnnotations);
                    initialAnnotations = null;
                }
                //			cu.addTypeDeclaration(td);
                // Done when the type declarations are created
            }
        } catch (IOException ioe) {
            if (!(ioe instanceof EOFException)) { // Not just "end of file"
                ApplicationFrame
                        .getInstance()
                        .getConsole()
                        .println(ioe.getMessage(),
                                ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            }
            ParserNotice notice = null;
            IJavaLexicalToken lastTokenLexed = scanner.getMostRecentToken();
            if (lastTokenLexed == null) {
                notice = new ParserNotice(0, 0, 5, ioe.getMessage());
            } else {
                notice = new ParserNotice(lastTokenLexed, ioe.getMessage());
            }
            cu.addParserNotice(notice);
            //throw ioe;
        }

        return cu;
    }

    private EnumBody _getEnumBody(CompilationUnit cu, Scanner s,
            EnumDeclaration enumDec) throws IOException {
        CodeBlock block = _getBlock(cu, null, null, s, false);
        enumDec.setBodyEndOffset(s.createOffset(block.getNameEndOffset()));
        return null;
    }

    private EnumDeclaration _getEnumDeclaration(CompilationUnit cu, Scanner s,
            ITypeDeclarationContainer addTo) throws IOException {
        IJavaLexicalToken t = s.yylexNonNull(IDENTIFIER, "Identifier expected");
        String enumName = t.getLexeme();
        EnumDeclaration enumDec = new EnumDeclaration(s, t.getOffset(),
                enumName);
        enumDec.setPackage(cu.getPackage());
        addTo.addTypeDeclaration(enumDec);

        t = s.yylexNonNull("implements or '{' expected");

        if (t.isType(KEYWORD_IMPLEMENTS)) {
            List<Type> implemented = new ArrayList<Type>(1); // Usually small
            do {
                implemented.add(_getType(cu, s));
                t = s.yylex();
            } while (t != null && t.isType(SEPARATOR_COMMA));
            if (t != null) {
                s.yyPushback(t);
            }
        } else if (t.isType(SEPARATOR_LBRACE)) {
            s.yyPushback(t);
        }

        _getEnumBody(cu, s, enumDec);

        return enumDec;
    }

    private List<FormalParameter> _getFormalParameters(CompilationUnit cu,
            List<IJavaLexicalToken> tokenList) throws IOException {
        List<FormalParameter> list = new ArrayList<FormalParameter>(0);

        Scanner s = new Scanner(tokenList);
        IJavaLexicalToken t = s.yylex();
        if (t == null) { // No parameters
            return list;
        }

        while (true) {
            boolean isFinal = false;
            if (t.isType(KEYWORD_FINAL)) {
                isFinal = true;
                t = s.yylexNonNull("Type expected");
            }
            List<Annotation> annotations = null;
            s.yyPushback(t);
            Type type = _getType(cu, s);
            IJavaLexicalToken temp = s.yylexNonNull("Argument name expected");
            boolean elipsis = false;
            if (temp.isType(ELIPSIS)) {
                elipsis = true;
                temp = s.yylexNonNull(IDENTIFIER, "Argument name expected");
            }
            type.incrementBracketPairCount(s.skipBracketPairs());
            int offs = temp.getOffset();
            String name = temp.getLexeme();
            FormalParameter param = new FormalParameter(s, isFinal, type, offs,
                    name, annotations);
            list.add(param);
            if (elipsis) {
                break; // Must be last parameter
            }
            t = s.yylex();
            if (t == null) {
                break;
            } else if (t.getType() != SEPARATOR_COMMA) {
                throw new IOException("Comma expected");
            }
            t = s.yylexNonNull("Parameter or ')' expected");
        }

        return list;
    }

    private void _getInterfaceBody(CompilationUnit cu, Scanner s,
            NormalInterfaceDeclaration iDec) throws IOException {
        log("Entering _getInterfaceBody");

        IJavaLexicalToken t = s.yylexNonNull(SEPARATOR_LBRACE, "'{' expected");
        iDec.setBodyStartOffset(s.createOffset(t.getOffset()));

        t = s.yylexNonNull("InterfaceBody expected");

        while (t.getType() != SEPARATOR_RBRACE) {
            switch (t.getType()) {
            case SEPARATOR_SEMICOLON:
                break; // Do nothing

            case SEPARATOR_LBRACE:
                s.yyPushback(t);
                _getBlock(cu, null, null, s, false);
                break;

            default:
                s.yyPushback(t);
                Modifiers modList = _getModifierList(cu, s);
                _getInterfaceMemberDecl(cu, s, iDec, modList);
                break;
            }

            try {
                t = s.yylexNonNull("'}' expected (one)");
                iDec.setBodyEndOffset(s.createOffset(t.getOffset()));
            } catch (IOException ioe) {
                iDec.setBodyEndOffset(s.createOffset(s.getOffset()));
                int line = s.getLine();
                int col = s.getColumn();
                ParserNotice pn = new ParserNotice(line, col, 1,
                        "'}' expected (two)");
                cu.addParserNotice(pn);
            }
        }

        log("Exiting _getInterfaceBody");
    }

    /*
     * InterfaceMemberDecl:
     *    InterfaceMethodOrFieldDecl
     *    InterfaceGenericMethodDecl
     *    void Identifier VoidInterfaceMethodDeclaratorRest
     *    InterfaceDeclaration
     *    ClassDeclaration
     */
    private void _getInterfaceMemberDecl(CompilationUnit cu, Scanner s,
            NormalInterfaceDeclaration iDec, Modifiers modList)
            throws IOException {
        log("Entering _getInterfaceMemberDecl");

        List<IJavaLexicalToken> tokenList = new ArrayList<IJavaLexicalToken>(1);
        List<IJavaLexicalToken> methodNameAndTypeTokenList = null;
        List<IJavaLexicalToken> methodParamsList = null;
        int bracketPairCount;
        boolean methodDecl = false;
        boolean blockDecl = false;
        boolean varDecl = false;
        IJavaLexicalToken t;

        OUTER: while (true) {
            t = s.yylexNonNull("Unexpected end of input");

            switch (t.getType()) {
            case SEPARATOR_LPAREN:
                methodNameAndTypeTokenList = tokenList;
                methodParamsList = new ArrayList<IJavaLexicalToken>(1);
                methodDecl = true;
                break OUTER;
            case SEPARATOR_LBRACE:
                blockDecl = true;
                break OUTER;
            case OPERATOR_EQUALS:
                varDecl = true;
                // Can be "= 4;", "= new foo();" or even "= new foo() { ... };"
                s.eatThroughNextSkippingBlocks(SEPARATOR_SEMICOLON);
                break OUTER;
            case SEPARATOR_SEMICOLON:
                varDecl = true;
                break OUTER;
            default:
                tokenList.add(t);
                break;
            }
        }

        if (varDecl) {
            log("*** Variable declaration:");
            Scanner tempScanner = new Scanner(tokenList);
            Type type = _getType(cu, tempScanner);
            IJavaLexicalToken fieldNameToken = tempScanner.yylexNonNull(
                    IDENTIFIER, "Identifier (field name) expected");
            bracketPairCount = tempScanner.skipBracketPairs();
            type.incrementBracketPairCount(bracketPairCount);
            Field field = new Field(s, modList, type, fieldNameToken);
            field.setDeprecated(checkDeprecated());
            field.setDocComment(s.getLastDocComment());
            log(field.toString());
            iDec.addMember(field);
        } else if (methodDecl) {
            log("*** Method declaration:");
            Scanner tempScanner = new Scanner(methodNameAndTypeTokenList);
            Type type = null;
            if (methodNameAndTypeTokenList.size() > 1) { // InterfaceMethodOrFieldDecl or InterfaceGenericMethodDecl
                if (tempScanner.yyPeekCheckType() == OPERATOR_LT) { // InterfaceGenericMethodDecl
                    _getTypeParameters(cu, tempScanner);
                    type = _getType(cu, tempScanner);
                } else { // InterfaceMethodOrFieldDecl (really just an InterfaceMethod)
                    type = _getType(cu, tempScanner);
                }
            }
            IJavaLexicalToken methodNameToken = tempScanner.yylexNonNull(
                    IDENTIFIER, "Identifier (method name) expected");
            while (true) {
                t = s.yylexNonNull("Unexpected end of input");
                if (t.isType(SEPARATOR_RPAREN)) {
                    break;
                }
                methodParamsList.add(t);
            }
            List<FormalParameter> formalParams = _getFormalParameters(cu,
                    methodParamsList);
            if (s.yyPeekCheckType() == SEPARATOR_LBRACKET) {
                if (type == null) {
                    throw new IOException(
                            "Constructors cannot return array types");
                }
                type.incrementBracketPairCount(s.skipBracketPairs());
            }
            List<String> thrownTypeNames = getThrownTypeNames(cu, s);
            t = s.yylexNonNull("'{' or ';' expected");
            if (t.getType() != SEPARATOR_SEMICOLON) {
                throw new IOException("';' expected");
            }
            Method m = new Method(s, modList, type, methodNameToken,
                    formalParams, thrownTypeNames);
            m.setDeprecated(checkDeprecated());
            m.setDocComment(s.getLastDocComment());
            iDec.addMember(m);
        } else if (blockDecl) {
            if (tokenList.size() < 2) {
                for (int i = tokenList.size() - 1; i >= 0; i--) {
                    s.yyPushback(tokenList.get(i));
                }
                CodeBlock block = _getBlock(cu, null, null, s, false);
                iDec.addMember(block);
            } else {
                s.yyPushback(t); // The '{' token
                for (int i = tokenList.size() - 1; i >= 0; i--) {
                    s.yyPushback(tokenList.get(i));
                }
                /*ITypeDeclaration type = */_getClassOrInterfaceDeclaration(
                        cu, s, iDec, modList);
            }
        }

        log("Exiting _getInterfaceMemberDecl");
    }

    /*
     * MemberDecl:
     *    GenericMethodOrConstructorDecl
     *    MethodOrFieldDecl
     *    void Identifier VoidMethodDeclaratorRest
     *    Identifier ConstructorDeclaratorRest
     *    InterfaceDeclaration
     *    ClassDeclaration
     */
    private void _getMemberDecl(CompilationUnit cu, Scanner s,
            NormalClassDeclaration classDec, Modifiers modList)
            throws IOException {
        log("Entering _getMemberDecl");

        List<IJavaLexicalToken> tokenList = new ArrayList<IJavaLexicalToken>(1);
        List<IJavaLexicalToken> methodNameAndTypeTokenList = null;
        List<IJavaLexicalToken> methodParamsList = null;
        int bracketPairCount;
        boolean methodDecl = false;
        boolean blockDecl = false;
        boolean varDecl = false;
        IJavaLexicalToken t;

        OUTER: while (true) {
            t = s.yylexNonNull("Unexpected end of input");

            switch (t.getType()) {
            case SEPARATOR_LPAREN:
                methodNameAndTypeTokenList = tokenList;
                methodParamsList = new ArrayList<IJavaLexicalToken>(1);
                methodDecl = true;
                break OUTER;
            case SEPARATOR_LBRACE:
                blockDecl = true;
                break OUTER;
            case OPERATOR_EQUALS:
                varDecl = true;
                // Can be "= 4;", "= new foo();" or even "= new foo() { ... };"
                s.eatThroughNextSkippingBlocks(SEPARATOR_SEMICOLON);
                break OUTER;
            case SEPARATOR_SEMICOLON:
                varDecl = true;
                break OUTER;
            default:
                tokenList.add(t);
                break;
            }
        }

        if (varDecl) {
            log("*** Variable declaration:");
            Scanner tempScanner = new Scanner(tokenList);
            Type type = _getType(cu, tempScanner);
            IJavaLexicalToken fieldNameToken = tempScanner.yylexNonNull(
                    IDENTIFIER, "Identifier (field name) expected");
            bracketPairCount = tempScanner.skipBracketPairs();
            type.incrementBracketPairCount(bracketPairCount);
            Field field = new Field(s, modList, type, fieldNameToken);
            field.setDeprecated(checkDeprecated());
            field.setDocComment(s.getLastDocComment());
            log(field.toString());
            classDec.addMember(field);
        } else if (methodDecl) {
            log("*** Method declaration:");
            CodeBlock block = null; // Method body
            Scanner tempScanner = new Scanner(methodNameAndTypeTokenList);
            Type type = null;
            if (methodNameAndTypeTokenList.size() > 1) { // GenericMethodOrConstructorDecl or method (not a regular constructor)
                if (tempScanner.yyPeekCheckType() == OPERATOR_LT) { // GenericMethodOrConstructorDecl
                    _getTypeParameters(cu, tempScanner);
                    if (tempScanner.yyPeekCheckType(2) == -1) { // GenericConstructor
                        // Do nothing; we're good to keep going
                    } else { // A generic method declaration
                        type = _getType(cu, tempScanner);
                    }
                } else {
                    type = _getType(cu, tempScanner); // Method (not a constructor)
                }
            }
            IJavaLexicalToken methodNameToken = tempScanner.yylexNonNull(
                    IDENTIFIER, "Identifier (method name) expected");
            while (true) {
                t = s.yylexNonNull("Unexpected end of input");
                if (t.isType(SEPARATOR_RPAREN)) {
                    break;
                }
                methodParamsList.add(t);
            }
            List<FormalParameter> formalParams = _getFormalParameters(cu,
                    methodParamsList);
            if (s.yyPeekCheckType() == SEPARATOR_LBRACKET) {
                if (type == null) {
                    throw new IOException(
                            "Constructors cannot return array types");
                }
                type.incrementBracketPairCount(s.skipBracketPairs());
            }
            List<String> thrownTypeNames = getThrownTypeNames(cu, s);
            Method m = new Method(s, modList, type, methodNameToken,
                    formalParams, thrownTypeNames);
            m.setDeprecated(checkDeprecated());
            m.setDocComment(s.getLastDocComment());
            classDec.addMember(m);
            t = s.yylexNonNull("'{' or ';' expected");
            if (t.isType(SEPARATOR_SEMICOLON)) {
                // Just a method declaration (such as in an interface)
            } else if (t.isType(SEPARATOR_LBRACE)) {
                s.yyPushback(t);
                block = _getBlock(cu, null, m, s, false);
            } else {
                throw new IOException("'{' or ';' expected");
            }
            m.setBody(block);
        } else if (blockDecl) {
            // Could be a code block (static or not) or an inner class, enum or
            // interface..
            nextMemberDeprecated = false;
            if (tokenList.size() < 2) {
                for (int i = tokenList.size() - 1; i >= 0; i--) {
                    s.yyPushback(tokenList.get(i));
                }
                CodeBlock block = _getBlock(cu, null, null, s, false);
                classDec.addMember(block);
            } else {
                s.yyPushback(t); // The '{' token
                for (int i = tokenList.size() - 1; i >= 0; i--) {
                    s.yyPushback(tokenList.get(i));
                }
                /*ITypeDeclaration type = */_getClassOrInterfaceDeclaration(
                        cu, s, classDec, modList);
            }
        }

        log("Exiting _getMemberDecl (next== " + s.yyPeek() + ")");
    }

    private Modifiers _getModifierList(CompilationUnit cu, Scanner s)
            throws IOException {
        Modifiers modList = null;
        IJavaLexicalToken t = s.yylexNonNull("Unexpected end of input");

        while (true) {
            int modifier = isModifier(t);
            if (modifier != -1) {
                if (modList == null) {
                    modList = new Modifiers();
                }
                if (!modList.addModifier(modifier)) {
                    cu.addParserNotice(t, "Duplicate modifier");
                }
            } else if (t.isType(ANNOTATION_START)) {
                IJavaLexicalToken next = s.yyPeekNonNull("Annotation expected");
                s.yyPushback(t); // Put '@' back
                if (next.isType(KEYWORD_INTERFACE)) {
                    return modList;
                }
                if (modList == null) {
                    modList = new Modifiers();
                }
                modList.addAnnotation(_getAnnotation(cu, s));
            } else {
                s.yyPushback(t);
                return modList;
            }

            t = s.yylexNonNull("Unexpected end of input");
        }
    }

    private NormalClassDeclaration _getNormalClassDeclaration(
            CompilationUnit cu, Scanner s, ITypeDeclarationContainer addTo)
            throws IOException {
        log("Entering _getNormalClassDeclaration");
        String className = null;

        IJavaLexicalToken t = s.yylexNonNull("Identifier expected");
        if (t.isType(IDENTIFIER)) {
            className = t.getLexeme();
        } else {
            className = "Unknown";
            cu.addParserNotice(new ParserNotice(t, "Class name expected"));
            s.eatUntilNext(KEYWORD_EXTENDS, KEYWORD_IMPLEMENTS,
                    SEPARATOR_LBRACE);
        }

        NormalClassDeclaration classDec = new NormalClassDeclaration(s,
                t.getOffset(), className);
        classDec.setPackage(cu.getPackage());
        addTo.addTypeDeclaration(classDec);

        t = s.yylexNonNull("TypeParameters, extends, implements or '{' expected");
        if (t.isType(OPERATOR_LT)) {
            s.yyPushback(t);
            List<TypeParameter> typeParams = _getTypeParameters(cu, s);
            classDec.setTypeParameters(typeParams);
            t = s.yylexNonNull("extends, implements or '{' expected");
        }

        if (t.isType(KEYWORD_EXTENDS)) {
            classDec.setExtendedType(_getType(cu, s));
            t = s.yylexNonNull("implements or '{' expected");
        }

        if (t.isType(KEYWORD_IMPLEMENTS)) {
            do {
                classDec.addImplemented(_getType(cu, s));
                t = s.yylex();
            } while (t != null && t.isType(SEPARATOR_COMMA));
            if (t != null) {
                s.yyPushback(t);
            }
        } else if (t.isType(SEPARATOR_LBRACE)) {
            s.yyPushback(t);
        }

        _getClassBody(cu, s, classDec);

        log("Exiting _getNormalClassDeclaration");
        return classDec;
    }

    private NormalInterfaceDeclaration _getNormalInterfaceDeclaration(
            CompilationUnit cu, Scanner s, ITypeDeclarationContainer addTo)
            throws IOException {
        String iName = null;

        IJavaLexicalToken t = s.yylexNonNull("Identifier expected");
        if (t.isType(IDENTIFIER)) {
            iName = t.getLexeme();
        } else {
            iName = "Unknown";
            cu.addParserNotice(new ParserNotice(t, "Interface name expected"));
            s.eatUntilNext(KEYWORD_EXTENDS, SEPARATOR_LBRACE);
        }

        NormalInterfaceDeclaration iDec = new NormalInterfaceDeclaration(s,
                t.getOffset(), iName);
        iDec.setPackage(cu.getPackage());
        addTo.addTypeDeclaration(iDec);

        t = s.yylexNonNull("TypeParameters, extends or '{' expected");
        if (t.isType(OPERATOR_LT)) {
            s.yyPushback(t);
            _getTypeParameters(cu, s);
            t = s.yylexNonNull("Interface body expected");
        }

        if (t.isType(KEYWORD_EXTENDS)) {
            do {
                iDec.addExtended(_getType(cu, s));
                t = s.yylex();
            } while (t != null && t.isType(SEPARATOR_COMMA));
            if (t != null) {
                s.yyPushback(t);
            }
        } else if (t.isType(SEPARATOR_LBRACE)) {
            s.yyPushback(t);
        }

        _getInterfaceBody(cu, s, iDec);

        return iDec;
    }

    private String getQualifiedIdentifier(Scanner scanner) throws IOException {
        IJavaLexicalToken t = null;
        StringBuilder sb = new StringBuilder();

        while ((t = scanner.yylex()).isIdentifier()) {
            sb.append(t.getLexeme());
            t = scanner.yylex();
            if (t.isType(SEPARATOR_DOT)) {
                sb.append('.');
            } else {
                break;
            }
        }

        // QualifiedIdentifier has ended
        scanner.yyPushback(t);

        return sb.toString();
    }

    private List<String> getThrownTypeNames(CompilationUnit cu, Scanner s)
            throws IOException {
        if (s.yyPeekCheckType() != KEYWORD_THROWS) {
            return null;
        }
        s.yylex();

        List<String> list = new ArrayList<String>(1); // Usually small

        list.add(getQualifiedIdentifier(s));
        while (s.yyPeekCheckType() == SEPARATOR_COMMA) {
            s.yylex();
            list.add(getQualifiedIdentifier(s));
        }

        return list;
    }

    // For "backwards compatibility", don't know if "false" is usually correct
    // or not
    private Type _getType(CompilationUnit cu, Scanner s) throws IOException {
        return _getType(cu, s, false);
    }

    private Type _getType(CompilationUnit cu, Scanner s,
            boolean pushbackOnUnexpected) throws IOException {
        log("Entering _getType()");
        Type type = new Type();

        IJavaLexicalToken t = s.yylexNonNull("Type expected");

        if (t.isType(KEYWORD_VOID)) {
            type.addIdentifier(t.getLexeme(), null);
            log("Exiting _getType(): " + type.toString());
            return type;
        } else if (t.isBasicType()) {
            int arrayDepth = s.skipBracketPairs();
            type.addIdentifier(t.getLexeme(), null);
            type.setBracketPairCount(arrayDepth);
            log("Exiting _getType(): " + type.toString());
            return type;
        }

        OUTER: while (true) {
            switch (t.getType()) {
            case IDENTIFIER:
                List<TypeArgument> typeArgs = null;
                if (s.yyPeekCheckType() == OPERATOR_LT) {
                    typeArgs = _getTypeArguments(cu, s);
                }
                type.addIdentifier(t.getLexeme(), typeArgs);
                t = s.yylexNonNull("Unexpected end of input");
                if (t.isType(SEPARATOR_DOT)) {
                    t = s.yylexNonNull("Unexpected end of input");
                    continue;
                } else if (t.isType(SEPARATOR_LBRACKET)) {
                    s.yyPushback(t);
                    type.setBracketPairCount(s.skipBracketPairs());
                    break OUTER;
                } else {
                    s.yyPushback(t);
                    break OUTER;
                }
            default:
                if (pushbackOnUnexpected) {
                    s.yyPushback(t);
                }
                throw new IOException("Expected identifier, found: " + t);
            }
        }

        log("Exiting _getType(): " + type.toString());
        return type;
    }

    private TypeArgument _getTypeArgument(CompilationUnit cu, Scanner s)
            throws IOException {
        log("Entering _getTypeArgument()");

        TypeArgument typeArg = null;

        IJavaLexicalToken t = s.yyPeekNonNull("Type or '?' expected");

        if (t.isType(OPERATOR_QUESTION)) {
            s.yylex(); // Pop the '?' off the stream
            t = s.yyPeek();
            if (t.getType() != OPERATOR_GT) {
                t = s.yylexNonNull(SEPARATOR_COMMA, KEYWORD_EXTENDS,
                        KEYWORD_SUPER, "',', super or extends expected");
                switch (t.getType()) {
                case SEPARATOR_COMMA:
                    typeArg = new TypeArgument(null, 0, null);
                    s.yyPushback(t);
                    break;
                case KEYWORD_EXTENDS:
                    Type otherType = _getType(cu, s);
                    typeArg = new TypeArgument(null, TypeArgument.EXTENDS,
                            otherType);
                    break;
                default: // KEYWORD_SUPER:
                    otherType = _getType(cu, s);
                    typeArg = new TypeArgument(null, TypeArgument.SUPER,
                            otherType);
                    break;
                }
            } else {
                typeArg = new TypeArgument(null, 0, null);
            }
        } else {
            Type type = _getType(cu, s);
            typeArg = new TypeArgument(type);
        }

        log("Exiting _getTypeArgument() : " + typeArg);
        return typeArg;
    }

    private List<TypeArgument> _getTypeArguments(CompilationUnit cu, Scanner s)
            throws IOException {
        s.increaseTypeArgumentsLevel();
        log("Entering _getTypeArguments() (" + s.getTypeArgumentsLevel() + ")");

        s.markResetPosition();
        s.yylexNonNull(OPERATOR_LT, "'<' expected");

        List<TypeArgument> typeArgs = new ArrayList<TypeArgument>(1);

        IJavaLexicalToken t = null;
        do {
            typeArgs.add(_getTypeArgument(cu, s));
            t = s.yylexNonNull("',' or '>' expected");
            if (t.getType() != SEPARATOR_COMMA && t.getType() != OPERATOR_GT) {
                // Assume we're in a code block and are simply at the (much more
                // common) case of e.g. "if (i < 7) ..."
                s.resetToLastMarkedPosition();
                log("Exiting _getTypeArguments() (" + s.getTypeArgumentsLevel()
                        + ") - NOT TYPE ARGUMENTS (" + t.getLexeme() + ")");
                s.decreaseTypeArgumentsLevel();
                return null;
            }
        } while (t.isType(SEPARATOR_COMMA));

        log("Exiting _getTypeArguments() (" + s.getTypeArgumentsLevel() + ")");
        s.decreaseTypeArgumentsLevel();

        s.clearResetPosition();
        return typeArgs;
    }

    private ITypeDeclaration _getTypeDeclaration(CompilationUnit cu, Scanner s)
            throws IOException {
        /*
         * ITypeDeclaration:
         *    ClassOrInterfaceDeclaration
         *    ';'
         */

        IJavaLexicalToken t = s.yylex();
        if (t == null) {
            return null; // End of source file
        }

        // Skip any semicolons
        while (t.isType(SEPARATOR_SEMICOLON)) {
            t = s.yylex();
            if (t == null) {
                return null; // End of source file
            }
        }

        s.yyPushback(t); // Probably some modifier, e.g. "public"

        String docComment = s.getLastDocComment();
        ITypeDeclaration td = _getClassOrInterfaceDeclaration(cu, s, cu, null);
        td.setDocComment(docComment); // May be null
        return td;

    }

    private TypeParameter _getTypeParameter(CompilationUnit cu, Scanner s)
            throws IOException {
        log("Entering _getTypeParameter()");

        IJavaLexicalToken identifier = s.yylexNonNull(IDENTIFIER,
                "Identifier expected");
        TypeParameter typeParam = new TypeParameter(identifier);

        if (s.yyPeekCheckType() == KEYWORD_EXTENDS) {
            do {
                s.yylex(); // Pop off "extends" or "&"
                typeParam.addBound(_getType(cu, s));
            } while (s.yyPeekCheckType() == OPERATOR_BITWISE_AND);
        }

        log("Exiting _getTypeParameter(): " + typeParam.getName());
        return typeParam;
    }

    private List<TypeParameter> _getTypeParameters(CompilationUnit cu, Scanner s)
            throws IOException {
        s.increaseTypeArgumentsLevel();
        log("Entering _getTypeParameters() (" + s.getTypeArgumentsLevel() + ")");

        s.markResetPosition();
        IJavaLexicalToken t = s.yylexNonNull(OPERATOR_LT,
                "TypeParameters expected");

        List<TypeParameter> typeParams = new ArrayList<TypeParameter>(1);

        do {
            TypeParameter typeParam = _getTypeParameter(cu, s);
            typeParams.add(typeParam);
            t = s.yylexNonNull(SEPARATOR_COMMA, OPERATOR_GT,
                    "',' or '>' expected");
        } while (t.isType(SEPARATOR_COMMA));

        log("Exiting _getTypeParameters() (" + s.getTypeArgumentsLevel() + ")");
        s.decreaseTypeArgumentsLevel();

        return typeParams;
    }

    private int isModifier(IJavaLexicalToken t) {
        switch (t.getType()) {
        case KEYWORD_PUBLIC:
        case KEYWORD_PROTECTED:
        case KEYWORD_PRIVATE:
        case KEYWORD_STATIC:
        case KEYWORD_ABSTRACT:
        case KEYWORD_FINAL:
        case KEYWORD_NATIVE:
        case KEYWORD_SYNCHRONIZED:
        case KEYWORD_TRANSIENT:
        case KEYWORD_VOLATILE:
        case KEYWORD_STRICTFP:
            return t.getType();
        default:
            return -1;
        }
    }

    private static final void log(String msg) {
        if (DEBUG) {
            System.out.println(msg);
        }
    }
}