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
 * Constants that define the different programming languages understood by
 * <code>SyntaxTextArea</code>. These constants are the values you can pass to
 * {@link SyntaxTextArea#setSyntaxEditingStyle(String)} to get syntax
 * highlighting.<p>
 *
 * By default, all <code>SyntaxTextArea</code>s can render all of these
 * languages, but this can be changed (the list can be augmented or completely
 * overwritten) on a per-text area basis. What languages can be rendered is
 * actually managed by the {@link TokenMakerFactory} installed on the text
 * area's {@link SyntaxDocument}. By default, all <code>SyntaxDocument</code>s
 * have a factory installed capable of handling all of these languages.
 *
 * @author D. Campione
 * 
 */
public interface ISyntaxConstants {

    /** Style meaning don't syntax highlight anything. */
    public static final String SYNTAX_STYLE_NONE = "text/plain";

    /** Style for highlighting SQL. */
    public static final String SYNTAX_STYLE_SQL = "text/sql";

    /** Style for highlighting Clojure. */
    public static final String SYNTAX_STYLE_CLOJURE = "text/clojure";
}