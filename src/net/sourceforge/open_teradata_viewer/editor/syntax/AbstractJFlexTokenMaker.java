/*
 * Open Teradata Viewer ( editor syntax )
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

package net.sourceforge.open_teradata_viewer.editor.syntax;

import javax.swing.text.Segment;

/**
 * Base class for JFlex-generated token makers. This class attempts to factor
 * out all common code from these classes. Many methods <em>almost</em> could be
 * factored out into this class, but cannot because they reference JFlex
 * variables that we cannot access from this class.
 *
 * @author D. Campione
 * 
 */
public abstract class AbstractJFlexTokenMaker extends TokenMakerBase {

    protected Segment s;

    protected int start; // Just for states

    protected int offsetShift; // As parser always starts at 0, but our line doesn't

    /**
     * Declared here so we can define overloads that refer to this method.
     *
     * @param newState The new JFlex state to enter.
     */
    public abstract void yybegin(int newState);

    /**
     * Starts a new JFlex state and changes the current language index.
     *
     * @param state The new JFlex state to enter.
     * @param languageIndex The new language index.
     */
    protected void yybegin(int state, int languageIndex) {
        yybegin(state);
        setLanguageIndex(languageIndex);
    }
}