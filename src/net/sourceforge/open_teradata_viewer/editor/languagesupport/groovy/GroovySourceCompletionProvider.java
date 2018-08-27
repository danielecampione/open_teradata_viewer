/*
 * Open Teradata Viewer ( editor language support groovy )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.groovy;

import java.util.Collections;
import java.util.List;

import javax.swing.text.JTextComponent;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.BasicCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.DefaultCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.common.CodeBlock;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.common.TokenScanner;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.common.VariableDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.JarManager;
import net.sourceforge.open_teradata_viewer.editor.syntax.IToken;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * The completion provider used for Groovy source code.
 *
 * @author D. Campione
 * 
 */
public class GroovySourceCompletionProvider extends DefaultCompletionProvider {

    private static final char[] KEYWORD_DEF = { 'd', 'e', 'f' };

    /** Ctor. */
    public GroovySourceCompletionProvider() {
        this(null);
    }

    /**
     * Ctor.
     *
     * @param jarManager The jar manager for this provider.
     */
    public GroovySourceCompletionProvider(JarManager jarManager) {
        if (jarManager == null) {
            jarManager = new JarManager();
        }
        setParameterizedCompletionParams('(', ", ", ')');
        setAutoActivationRules(false, "."); // Default - only activate after '.'
    }

    private CodeBlock createAst(JTextComponent comp) {
        CodeBlock ast = new CodeBlock(0);

        SyntaxTextArea textArea = (SyntaxTextArea) comp;
        TokenScanner scanner = new TokenScanner(textArea);
        parseCodeBlock(scanner, ast);

        return ast;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    protected List<ICompletion> getCompletionsImpl(JTextComponent comp) {
        completions.clear();

        CodeBlock ast = createAst(comp);

        int dot = comp.getCaretPosition();
        recursivelyAddLocalVars(completions, ast, dot);

        Collections.sort(completions);

        // Cut down the list to just those matching what we've typed
        String text = getAlreadyEnteredText(comp);

        int start = Collections.binarySearch(completions, text, comparator);
        if (start < 0) {
            start = -(start + 1);
        } else {
            // There might be multiple entries with the same input text
            while (start > 0
                    && comparator.compare(completions.get(start - 1), text) == 0) {
                start--;
            }
        }

        int end = Collections.binarySearch(completions, text + '{', comparator);
        end = -(end + 1);

        return completions.subList(start, end);
    }

    /** {@inheritDoc} */
    @Override
    protected boolean isValidChar(char ch) {
        return Character.isJavaIdentifierPart(ch) || ch == '.';
    }

    private void parseCodeBlock(TokenScanner scanner, CodeBlock block) {
        IToken t = scanner.next();
        while (t != null) {
            if (t.isRightCurly()) {
                block.setEndOffset(t.getOffset());
                return;
            } else if (t.isLeftCurly()) {
                CodeBlock child = block.addChildCodeBlock(t.getOffset());
                parseCodeBlock(scanner, child);
            } else if (t.is(IToken.RESERVED_WORD, KEYWORD_DEF)) {
                t = scanner.next();
                if (t != null) {
                    VariableDeclaration varDec = new VariableDeclaration(
                            t.getLexeme(), t.getOffset());
                    block.addVariable(varDec);
                }
            }
            t = scanner.next();
        }
    }

    private void recursivelyAddLocalVars(List<ICompletion> completions,
            CodeBlock block, int dot) {
        if (!block.contains(dot)) {
            return;
        }

        // Add local variables declared in this code block
        for (int i = 0; i < block.getVariableDeclarationCount(); i++) {
            VariableDeclaration dec = block.getVariableDeclaration(i);
            int decOffs = dec.getOffset();
            if (decOffs < dot) {
                BasicCompletion c = new BasicCompletion(this, dec.getName());
                completions.add(c);
            } else {
                break;
            }
        }

        // Add any local variables declared in a child code block
        for (int i = 0; i < block.getChildCodeBlockCount(); i++) {
            CodeBlock child = block.getChildCodeBlock(i);
            if (child.contains(dot)) {
                recursivelyAddLocalVars(completions, child, dot);
                return; // No other child blocks can contain the dot
            }
        }
    }
}