/*
 * Open Teradata Viewer ( editor language support common )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.common;

import javax.swing.text.Element;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.ILanguageSupport;
import net.sourceforge.open_teradata_viewer.editor.syntax.IToken;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxDocument;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * Returns non-whitespace, non-comment tokens from a {@link SyntaxDocument}, one
 * at a time. This can be used by simplistic {@link ILanguageSupport}s to
 * "parse" for simple, easily-identifiable tokens, such as curly braces and
 * {@link IToken#VARIABLE}s. For example, to identify code blocks for languages
 * structured like C and Java, you can use this class in conjunction with {@link
 * CodeBlock} and {@link VariableDeclaration} to create an easily-parsable model
 * of your source code.
 *
 * @author D. Campione
 * 
 */
public class TokenScanner {

    private SyntaxDocument doc;
    private Element root;
    private IToken t;
    private int line;

    public TokenScanner(SyntaxTextArea textArea) {
        this((SyntaxDocument) textArea.getDocument());
    }

    public TokenScanner(SyntaxDocument doc) {
        this.doc = doc;
        root = doc.getDefaultRootElement();
        line = 0;
        t = null;
    }

    /**
     * Returns the document being parsed.
     *
     * @return The document.
     */
    public SyntaxDocument getDocument() {
        return doc;
    }

    /**
     * Returns the next non-whitespace, non-comment token in the text area.
     *
     * @return The next token, or <code>null</code> if we are at the end of its
     *         document.
     */
    public IToken next() {
        IToken next = nextRaw();
        while (next != null && (next.isWhitespace() || next.isComment())) {
            next = nextRaw();
        }
        return next;
    }

    /**
     * Returns the next token in the text area.
     *
     * @return The next token, or <code>null</code> if we are at the end of its
     *         document.
     */
    private IToken nextRaw() {
        if (t == null || !t.isPaintable()) {
            int lineCount = root.getElementCount();
            while (line < lineCount && (t == null || !t.isPaintable())) {
                t = doc.getTokenListForLine(line++);
            }
            if (line == lineCount) {
                return null;
            }
        }
        IToken next = t;
        t = t.getNextToken();
        return next;
    }
}