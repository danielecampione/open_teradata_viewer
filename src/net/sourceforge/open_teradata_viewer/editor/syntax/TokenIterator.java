/*
 * Open Teradata Viewer ( editor syntax )
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

package net.sourceforge.open_teradata_viewer.editor.syntax;

import java.util.Iterator;

/**
 * Allows you to iterate through all paintable tokens in an
 * <code>SyntaxDocument</code>.
 *
 * @author D. Campione
 * 
 */
class TokenIterator implements Iterator<IToken> {

    private SyntaxDocument doc;
    private int curLine;
    private IToken token;

    /**
     * Ctor.
     *
     * @param doc The document whose tokens we should iterate over.
     */
    public TokenIterator(SyntaxDocument doc) {
        this.doc = doc;
        loadTokenListForCurLine();
        int lineCount = getLineCount();
        while ((token == null || !token.isPaintable())
                && curLine < lineCount - 1) {
            curLine++;
            loadTokenListForCurLine();
        }
    }

    private int getLineCount() {
        return doc.getDefaultRootElement().getElementCount();
    }

    /**
     * Returns whether any more paintable tokens are in the document.
     *
     * @return Whether there are any more paintable tokens.
     * @see #next()
     */
    @Override
    public boolean hasNext() {
        return token != null;
    }

    private void loadTokenListForCurLine() {
        token = doc.getTokenListForLine(curLine);
        if (token != null && !token.isPaintable()) {
            // Common end of document scenario
            token = null;
        }
    }

    /**
     * @return The next paintable token in the document.
     * @see #hasNext()
     */
    @Override
    public IToken next() {
        IToken t = token;
        boolean tIsCloned = false;
        int lineCount = getLineCount();

        // Get the next token, going to the next line if necessary
        if (token != null && token.isPaintable()) {
            token = token.getNextToken();
        } else if (curLine < lineCount - 1) {
            t = new TokenImpl(t); // Clone t since tokens are pooled
            tIsCloned = true;
            curLine++;
            loadTokenListForCurLine();
        } else if (token != null && !token.isPaintable()) {
            // Ends with a non-paintable token
            token = null;
        }

        while ((token == null || !token.isPaintable())
                && curLine < lineCount - 1) {
            if (!tIsCloned) {
                t = new TokenImpl(t); // Clone t since tokens are pooled
                tIsCloned = true;
            }
            curLine++;
            loadTokenListForCurLine();
        }
        if (token != null && !token.isPaintable() && curLine == lineCount - 1) {
            token = null;
        }

        return t;
    }

    /**
     * Always throws {@link UnsupportedOperationException}, as
     * <code>IToken</code> removal is not supported.
     *
     * @throws UnsupportedOperationException always.
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}