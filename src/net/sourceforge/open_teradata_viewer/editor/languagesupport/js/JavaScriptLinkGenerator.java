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

import javax.swing.text.BadLocationException;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.JavaScriptDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.VariableResolver;
import net.sourceforge.open_teradata_viewer.editor.syntax.ILinkGenerator;
import net.sourceforge.open_teradata_viewer.editor.syntax.ILinkGeneratorResult;
import net.sourceforge.open_teradata_viewer.editor.syntax.IToken;
import net.sourceforge.open_teradata_viewer.editor.syntax.SelectRegionLinkGeneratorResult;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxUtilities;
import net.sourceforge.open_teradata_viewer.editor.syntax.TokenImpl;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class JavaScriptLinkGenerator implements ILinkGenerator {

    private JavaScriptLanguageSupport language;
    private boolean findLocal;
    private boolean findPreprocessed;
    private boolean findSystem;

    public JavaScriptLinkGenerator(JavaScriptLanguageSupport language) {
        this.language = language;
        this.findLocal = true;
    }

    /** {@inheritDoc} */
    @Override
    public ILinkGeneratorResult isLinkAtOffset(SyntaxTextArea textArea, int offs) {
        JavaScriptDeclaration dec = null;
        IsLinkableCheckResult result = checkForLinkableToken(textArea, offs);
        if (result != null) {
            // Re-parse the document to resolve any variables local to the offs
            IToken t = result.token;
            boolean function = result.function;
            String name = t.getLexeme();
            if (name != null && name.length() > 0) {
                // Only re-parse the document if there is a character that could
                // potentially be a variable or function 
                if (name.length() > 1
                        || (name.length() == 1 && Character
                                .isJavaIdentifierPart(name.charAt(0)))) {
                    language.reparseDocument(offs);
                }
            }
            JavaScriptParser parser = language.getJavaScriptParser();
            VariableResolver variableResolver = parser
                    .getVariablesAndFunctions();

            if (variableResolver != null) {
                if (!function) { // Must be a variable
                    dec = variableResolver.findDeclaration(name, offs,
                            findLocal, findPreprocessed, findSystem);
                } else {
                    String lookup = getLookupNameForFunction(textArea, offs,
                            name);
                    // Lookup Function based on the name
                    dec = variableResolver.findFunctionDeclaration(lookup,
                            findLocal, findPreprocessed);
                    if (dec == null) {
                        dec = variableResolver
                                .findFunctionDeclarationByFunctionName(name,
                                        findLocal, findPreprocessed);
                    }
                }
            }

            if (dec != null) {
                return createSelectedRegionResult(textArea, t, dec);
            }
        }
        return null;
    }

    /**
     * @return LinkGeneratorResult based on the JavaScriptDeclaraton and the
     *         position.
     */
    protected ILinkGeneratorResult createSelectedRegionResult(
            SyntaxTextArea textArea, IToken t, JavaScriptDeclaration dec) {
        if (dec.getTypeDeclarationOptions() != null
                && !dec.getTypeDeclarationOptions().isSupportsLinks()) {
            return null;
        }
        return new SelectRegionLinkGeneratorResult(textArea, t.getOffset(),
                dec.getStartOffSet(), dec.getEndOffset());
    }

    /**
     * @param find Flag to state whether to look in the STA editing script for
     *        variable/function completions.
     */
    public void setFindLocal(boolean find) {
        this.findLocal = find;
    }

    /**
     * @param find Flag to state whether to look in the pre-processed scripts
     *        for variable/function completions.
     */
    public void setFindPreprocessed(boolean find) {
        this.findPreprocessed = find;
    }

    /**
     * @param find Flag to state whether to look in the system scripts for
     *        variable/function completions.
     */
    public void setFindSystem(boolean find) {
        this.findSystem = find;
    }

    /**
     * Convert the function Token to JavaScript variable resolver lookup name by
     * replacing any parameters with 'p' and stripping any whitespace between
     * the parameters:
     * e.g
     * Token may contain the function:
     * addTwoNumbers(num1, num2);
     * 
     * The return result will be:
     * addTwoNumbers(p,p);
     * 
     * @return Converted function name to variable resolver lookup name.
     */
    private String getLookupNameForFunction(SyntaxTextArea textArea, int offs,
            String name) {
        StringBuilder temp = new StringBuilder();
        if (offs >= 0) {
            try {
                int line = textArea.getLineOfOffset(offs);

                IToken first = wrapToken(textArea.getTokenListForLine(line));
                for (IToken t = first; t != null && t.isPaintable(); t = wrapToken(t
                        .getNextToken())) {
                    if (t.containsPosition(offs)) {
                        for (IToken tt = t; tt != null && tt.isPaintable(); tt = wrapToken(tt
                                .getNextToken())) {
                            temp.append(tt.getLexeme());
                            if (tt.isSingleChar(IToken.SEPARATOR, ')')) {
                                break;
                            }
                        }
                    }
                }
            } catch (BadLocationException ble) {
                ExceptionDialog.hideException(ble); // Never happens
            }
        }

        // Now replace all the variables with lookup 'p'
        String function = temp.toString().replaceAll("\\s", ""); // Remove all whitespace
        boolean params = false;
        int count = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < function.length(); i++) {
            char ch = function.charAt(i);

            if (ch == '(') {
                params = true;
                count = 0;
                sb.append(ch);
            } else if (ch == ')') {
                sb.append(ch);
                break;
            } else if (ch == ',') {
                count = 0;
                sb.append(ch);
                continue;
            } else if (params && count == 0) {
                sb.append('p');
                count++;
            } else if (!params) {
                sb.append(ch);
            }
        }

        return sb.toString();
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
                IToken first = wrapToken(textArea.getTokenListForLine(line));
                IToken prev = null;

                for (IToken t = first; t != null && t.isPaintable(); t = wrapToken(t
                        .getNextToken())) {
                    if (t.containsPosition(offs)) {
                        // STA's tokens are pooled and re-used, so we must
                        // defensively make a copy of the one we want to keep
                        IToken token = wrapToken(t);

                        boolean isFunction = false;

                        if (prev != null && prev.isSingleChar('.')) {
                            // Not a field or method defined in this
                            break;
                        }

                        IToken next = wrapToken(SyntaxUtilities
                                .getNextImportantToken(t.getNextToken(),
                                        textArea, line));
                        if (next != null
                                && next.isSingleChar(IToken.SEPARATOR, '(')) {
                            isFunction = true;
                        }

                        result = new IsLinkableCheckResult(token, isFunction);
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

    /**
     * Due to the Tokens being reused, this can cause problems when finding the
     * next token associated with a token. Wrap the tokens to stop this problem.
     * 
     * @param token Token to wrap.
     * @return Copy of the original token.
     */
    private IToken wrapToken(IToken token) {
        if (token != null) {
            return new TokenImpl(token);
        }
        return token;
    }

    /** @return JavaScriptLanguage support. */
    public JavaScriptLanguageSupport getLanguage() {
        return language;
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
         * Whether the token is a function invocation (as opposed to a local
         * variable or object).
         */
        private boolean function;

        private IsLinkableCheckResult(IToken token, boolean function) {
            this.token = token;
            this.function = function;
        }
    }
}