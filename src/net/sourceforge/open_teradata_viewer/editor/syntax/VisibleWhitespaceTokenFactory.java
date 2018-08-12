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

/**
 * Token factory that generates tokens that display special symbols for the
 * whitespace characters space and tab.<p>
 *
 * NOTE: This class should only be used by {@link ITokenMaker}; nobody else
 * needs it.
 *
 * @author D. Campione
 * 
 */
class VisibleWhitespaceTokenFactory extends DefaultTokenFactory {

    /** Ctor. */
    public VisibleWhitespaceTokenFactory() {
        this(DEFAULT_START_SIZE, DEFAULT_INCREMENT);
    }

    /**
     * Ctor.
     *
     * @param size The initial number of tokens in this factory.
     * @param increment How many tokens to increment by when the stack gets
     *                  empty.
     */
    public VisibleWhitespaceTokenFactory(int size, int increment) {
        super(size, increment);
    }

    /**
     * Creates a token for use internally by this token factory. This method
     * should NOT be called externally; only by this class and possibly
     * subclasses.
     *
     * @return A token to add to this token factory's internal stack.
     */
    protected Token createInternalUseOnlyToken() {
        return new VisibleWhitespaceToken();
    }
}