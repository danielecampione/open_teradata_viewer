/*
 * Open Teradata Viewer ( editor language support java )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.swing.text.BadLocationException;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.CodeBlock;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.CompilationUnit;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.FormalParameter;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.IMember;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.ITypeDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.LocalVariable;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.Method;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.IJavaLexicalToken;
import net.sourceforge.open_teradata_viewer.editor.syntax.ILinkGenerator;
import net.sourceforge.open_teradata_viewer.editor.syntax.ILinkGeneratorResult;
import net.sourceforge.open_teradata_viewer.editor.syntax.IToken;
import net.sourceforge.open_teradata_viewer.editor.syntax.SelectRegionLinkGeneratorResult;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxUtilities;
import net.sourceforge.open_teradata_viewer.editor.syntax.TokenImpl;

/**
 * Checks for hyperlink-able tokens under the mouse position when Ctrl is
 * pressed (Cmd on OS X). Currently this class only checks for accessible
 * members in the current file only (e.g. no members in super classes, no other
 * classes on the classpath, etc..). So naturally, there is a lot of room for
 * improvement. IDE-style applications, for example, would want to check for
 * members in super-classes, and open their source on click events.
 * 
 * @author D. Campione
 * 
 */
class JavaLinkGenerator implements ILinkGenerator {

    private JavaLanguageSupport jls;

    JavaLinkGenerator(JavaLanguageSupport jls) {
        this.jls = jls;
    }

    /**
     * Checks if the token at the specified offset is possibly a "click-able"
     * region.
     *
     * @param textArea The text area.
     * @param offs The offset, presumably at the mouse position.
     * @return A result object.
     */
    private IsLinkableCheckResult checkForLinkableToken(
            SyntaxTextArea textArea, int offs) {
        IsLinkableCheckResult result = null;

        if (offs >= 0) {
            try {
                int line = textArea.getLineOfOffset(offs);
                IToken first = textArea.getTokenListForLine(line);
                IToken prev = null;

                for (IToken t = first; t != null && t.isPaintable(); t = t
                        .getNextToken()) {
                    if (t.containsPosition(offs)) {
                        // STA's tokens are pooled and re-used, so we must
                        // defensively make a copy of the one we want to keep
                        IToken token = new TokenImpl(t);
                        boolean isMethod = false;

                        if (prev == null) {
                            prev = SyntaxUtilities.getPreviousImportantToken(
                                    textArea, line - 1);
                        }
                        if (prev != null && prev.isSingleChar('.')) {
                            // Not a field or method defined in this class
                            break;
                        }

                        IToken next = SyntaxUtilities.getNextImportantToken(
                                t.getNextToken(), textArea, line);
                        if (next != null
                                && next.isSingleChar(
                                        IJavaLexicalToken.SEPARATOR, '(')) {
                            isMethod = true;
                        }

                        result = new IsLinkableCheckResult(token, isMethod);
                        break;
                    } else if (!t.isCommentOrWhitespace()) {
                        prev = t;
                    }
                }
            } catch (BadLocationException ble) {
                ExceptionDialog.hideException(ble); // Never happens
            }
        }

        return result;
    }

    /** {@inheritDoc} */
    @Override
    public ILinkGeneratorResult isLinkAtOffset(SyntaxTextArea textArea, int offs) {
        int start = -1;
        int end = -1;

        IsLinkableCheckResult result = checkForLinkableToken(textArea, offs);
        if (result != null) {
            JavaParser parser = jls.getParser(textArea);
            CompilationUnit cu = parser.getCompilationUnit();
            IToken t = result.token;
            boolean method = result.method;

            if (cu != null) {
                ITypeDeclaration td = cu
                        .getDeepestTypeDeclarationAtOffset(offs);
                boolean staticFieldsOnly = false;
                boolean deepestTypeDec = true;
                boolean deepestContainingMemberStatic = false;
                while (td != null && start == -1) {
                    // First, check for a local variable in methods/static blocks
                    if (!method && deepestTypeDec) {
                        Iterator<IMember> i = td.getMemberIterator();
                        while (i.hasNext()) {
                            Method m = null;
                            IMember member = i.next();
                            CodeBlock block = null;

                            // Check if a method or static block contains offs
                            if (member instanceof Method) {
                                m = (Method) member;
                                if (m.getBodyContainsOffset(offs)
                                        && m.getBody() != null) {
                                    deepestContainingMemberStatic = m
                                            .isStatic();
                                    block = m
                                            .getBody()
                                            .getDeepestCodeBlockContaining(offs);
                                }
                            } else if (member instanceof CodeBlock) {
                                block = (CodeBlock) member;
                                deepestContainingMemberStatic = block
                                        .isStatic();
                                block = block
                                        .getDeepestCodeBlockContaining(offs);
                            }

                            // If so, scan its locals
                            if (block != null) {
                                String varName = t.getLexeme();
                                // Local variables first, in reverse order
                                List<LocalVariable> locals = block
                                        .getLocalVarsBefore(offs);
                                Collections.reverse(locals);
                                for (LocalVariable local : locals) {
                                    if (varName.equals(local.getName())) {
                                        start = local.getNameStartOffset();
                                        end = local.getNameEndOffset();
                                    }
                                }
                                // Then arguments, if any
                                if (start == -1 && m != null) {
                                    for (int j = 0; j < m.getParameterCount(); j++) {
                                        FormalParameter p = m.getParameter(j);
                                        if (varName.equals(p.getName())) {
                                            start = p.getNameStartOffset();
                                            end = p.getNameEndOffset();
                                        }
                                    }
                                }
                                break; // No other code block will contain offs
                            }
                        }
                    }

                    // If no local var match, check fields or methods
                    if (start == -1) {
                        String varName = t.getLexeme();
                        Iterator<? extends IMember> i = method ? td
                                .getMethodIterator() : td.getFieldIterator();
                        while (i.hasNext()) {
                            IMember member = i.next();
                            if (((!deepestContainingMemberStatic && !staticFieldsOnly) || member
                                    .isStatic())
                                    && varName.equals(member.getName())) {
                                start = member.getNameStartOffset();
                                end = member.getNameEndOffset();
                                break;
                            }
                        }
                    }

                    // If still no match found, check parent type
                    if (start == -1) {
                        staticFieldsOnly |= td.isStatic();
                        td = td.getParentType();
                        // Don't check for local vars in parent type methods
                        deepestTypeDec = false;
                    }
                }
            }

            if (start > -1) {
                return new SelectRegionLinkGeneratorResult(textArea,
                        t.getOffset(), start, end);
            }
        }

        return null;
    }

    /**
     * The result of checking whether a region of code under the mouse is
     * <em>possibly</em> link-able.
     * 
     * @author D. Campione
     * 
     */
    private static class IsLinkableCheckResult {

        /** The token under the mouse position. */
        private IToken token;

        /**
         * Whether the token is a method invocation (as opposed to a local
         * variable or field).
         */
        private boolean method;

        private IsLinkableCheckResult(IToken token, boolean method) {
            this.token = token;
            this.method = method;
        }
    }
}