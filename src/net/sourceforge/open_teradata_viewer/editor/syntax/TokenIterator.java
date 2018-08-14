/*
 * Open Teradata Viewer ( editor syntax )
 * Copyright (C) 2013, D. Campione
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
        token = doc.getTokenListForLine(0);
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

    /**
     * @return The next paintable token in the document.
     * @see #hasNext()
     */
    @Override
    public IToken next() {
        IToken t = token;
        if (token != null) {
            if (token.isPaintable()) {
                token = token.getNextToken();
            } else if (curLine < getLineCount() - 1) {
                do {
                    token = doc.getTokenListForLine(++curLine);
                } while (token != null && !token.isPaintable());
            } else {
                token = null;
            }
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