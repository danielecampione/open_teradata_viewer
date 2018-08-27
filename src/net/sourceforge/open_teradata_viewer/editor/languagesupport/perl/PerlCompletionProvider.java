/*
 * Open Teradata Viewer ( editor language support perl )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.perl;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.text.JTextComponent;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.BasicCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.DefaultCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.c.CCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.common.CodeBlock;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.common.TokenScanner;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.common.VariableDeclaration;
import net.sourceforge.open_teradata_viewer.editor.syntax.IToken;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * A completion provider for Perl. It provides:
 * 
 * <ul>
 *    <li>Auto-completion for standard Perl 5.10 functions (read from an XML
 *        file).</li>
 *    <li>Crude auto-completion for variables. Only variables in scope at the
 *        current caret position are suggested.</li>
 * </ul>
 *
 * To toggle whether parameter assistance wraps your parameters in parens, use
 * the {@link #setUseParensWithFunctions(boolean)} method.
 *
 * @author D. Campione
 * 
 */
public class PerlCompletionProvider extends CCompletionProvider {

    private boolean useParensWithFunctions;

    /** {@inheritDoc} */
    @Override
    protected void addShorthandCompletions(DefaultCompletionProvider codeCP) {
    }

    /**
     * Creates an "AST" for Perl code, representing code blocks and variables
     * inside of those blocks.
     *
     * @param textArea The text area.
     * @return A "code block" representing the entire Perl source file.
     */
    private CodeBlock createAst(SyntaxTextArea textArea) {
        CodeBlock ast = new CodeBlock(0);
        TokenScanner scanner = new TokenScanner(textArea);
        parseCodeBlock(scanner, ast);
        return ast;
    }

    /** {@inheritDoc} */
    @Override
    protected ICompletionProvider createCodeCompletionProvider() {
        DefaultCompletionProvider cp = new PerlCodeCompletionProvider(this);
        loadCodeCompletionsFromXml(cp);
        addShorthandCompletions(cp);
        return cp;
    }

    /** {@inheritDoc} */
    @Override
    protected ICompletionProvider createStringCompletionProvider() {
        DefaultCompletionProvider cp = new DefaultCompletionProvider();
        return cp;
    }

    /** {@inheritDoc} */
    @Override
    protected List<ICompletion> getCompletionsImpl(JTextComponent comp) {
        List<ICompletion> completions = super.getCompletionsImpl(comp);

        SortedSet<ICompletion> varCompletions = getVariableCompletions(comp);
        if (varCompletions != null) {
            completions.addAll(varCompletions);
            Collections.sort(completions);
        }

        return completions;
    }

    /**
     * Overridden to return the null char (meaning "no end character") if the
     * user doesn't want to use parens around their functions.
     *
     * @return The end character for parameters list, or the null char if none.
     * @see #getUseParensWithFunctions()
     */
    @Override
    public char getParameterListEnd() {
        return getUseParensWithFunctions() ? ')' : 0;
    }

    /**
     * Overridden to return the null char (meaning "no start character") if the
     * user doesn't want to use parens around their functions.
     *
     * @return The start character for parameters list, or the null char if
     *         none.
     * @see #getUseParensWithFunctions()
     */
    @Override
    public char getParameterListStart() {
        return getUseParensWithFunctions() ? '(' : ' ';
    }

    /**
     * Returns whether the user wants to use parens around parameters to
     * functions.
     *
     * @return Whether to use parens around parameters to functions.
     * @see #setUseParensWithFunctions(boolean)
     */
    public boolean getUseParensWithFunctions() {
        return useParensWithFunctions;
    }

    /**
     * Does a crude search for variables up to the caret position. This method
     * does not care whether the variables are in scope at the caret position.
     *
     * @param comp The text area.
     * @return The completions for variables, or <code>null</code> if there were
     *         none.
     */
    private SortedSet<ICompletion> getVariableCompletions(JTextComponent comp) {
        SyntaxTextArea textArea = (SyntaxTextArea) comp;
        int dot = textArea.getCaretPosition();
        SortedSet<ICompletion> varCompletions = new TreeSet<ICompletion>(
                comparator);

        ICompletionProvider p = getDefaultCompletionProvider();
        String text = p.getAlreadyEnteredText(comp);
        char firstChar = text.length() == 0 ? 0 : text.charAt(0);
        if (firstChar != '$' && firstChar != '@' && firstChar != '%') {
            System.out.println("DEBUG: No use matching variables, exiting");
            return null;
        }

        // Go through all code blocks in scope and look for variables declared
        // before the caret
        CodeBlock block = createAst(textArea);
        recursivelyAddLocalVars(varCompletions, block, dot, firstChar);

        // Get only those that match what's typed
        if (varCompletions.size() > 0) {
            ICompletion from = new BasicCompletion(p, text);
            ICompletion to = new BasicCompletion(p, text + '{');
            varCompletions = varCompletions.subSet(from, to);
        }

        return varCompletions;
    }

    private CaseInsensitiveComparator comparator = new CaseInsensitiveComparator();

    /**
     * A comparator that compares the input text of two {@link ICompletion}s
     * lexicographically, ignoring case.
     * 
     * @author D. Campione
     * 
     */
    private static class CaseInsensitiveComparator implements
            Comparator<ICompletion>, Serializable {

        private static final long serialVersionUID = 9116836916674377559L;

        @Override
        public int compare(ICompletion c1, ICompletion c2) {
            String s1 = c1.getInputText();
            String s2 = c2.getInputText();
            return String.CASE_INSENSITIVE_ORDER.compare(s1, s2);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected String getXmlResource() {
        return "res/perl5.xml";
    }

    /** Recursively adds code blocks, remembering variables in them. */
    private void parseCodeBlock(TokenScanner scanner, CodeBlock block) {
        IToken t = scanner.next();
        while (t != null) {
            if (t.isRightCurly()) {
                block.setEndOffset(t.getOffset());
                return;
            } else if (t.isLeftCurly()) {
                CodeBlock child = block.addChildCodeBlock(t.getOffset());
                parseCodeBlock(scanner, child);
            } else if (t.getType() == IToken.VARIABLE) {
                VariableDeclaration varDec = new VariableDeclaration(
                        t.getLexeme(), t.getOffset());
                block.addVariable(varDec);
            }
            t = scanner.next();
        }
    }

    /**
     * Recursively adds any local variables defined before the given caret
     * offset and in the given code block (and any nested children the caret is
     * in).
     *
     * @param completions The list to add to.
     * @param block The code block to search through.
     * @param dot The caret position.
     */
    private void recursivelyAddLocalVars(SortedSet<ICompletion> completions,
            CodeBlock block, int dot, int firstChar) {
        if (!block.contains(dot)) {
            return;
        }

        // Add local variables declared in this code block
        for (int i = 0; i < block.getVariableDeclarationCount(); i++) {
            VariableDeclaration dec = block.getVariableDeclaration(i);
            int decOffs = dec.getOffset();
            if (decOffs < dot) {
                String name = dec.getName();
                char ch = name.charAt(0);
                if (firstChar <= ch) { // '$' comes before '@'/'%' in ascii
                    if (firstChar < ch) { // Use first char they entered
                        name = firstChar + name.substring(1);
                    }
                    BasicCompletion c = new BasicCompletion(this, name);
                    completions.add(c);
                }
            } else { // A variable declared past the caret -> nothing more to add
                break;
            }
        }

        // Add any local variables declared in a child code block
        for (int i = 0; i < block.getChildCodeBlockCount(); i++) {
            CodeBlock child = block.getChildCodeBlock(i);
            if (child.contains(dot)) {
                recursivelyAddLocalVars(completions, child, dot, firstChar);
                return; // No other child blocks can contain the dot
            }
        }
    }

    /**
     * Sets whether the user wants to use parens around parameters to functions.
     *
     * @param use Whether to use parens around parameters to functions.
     * @see #getUseParensWithFunctions()
     */
    public void setUseParensWithFunctions(boolean use) {
        useParensWithFunctions = use;
    }
}