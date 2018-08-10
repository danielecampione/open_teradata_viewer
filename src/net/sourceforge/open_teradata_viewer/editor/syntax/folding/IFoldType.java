/*
 * Open Teradata Viewer ( editor syntax folding )
 * Copyright (C) 2012, D. Campione
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

package net.sourceforge.open_teradata_viewer.editor.syntax.folding;

/**
 * Constants representing the "type" of a folded region. Implementations of
 * {@link IFoldParser} can also define their own folded region types, provided
 * they give them values of at least {@link #FOLD_TYPE_USER_DEFINED_MIN}. This
 * allows you to identify and auto-fold specific regions of source code when
 * opening files; for example, a Java editor could identify all import
 * statements in a file as a foldable region, and give it a user-defined value
 * for fold type. Then, the UI could provide a means for the user to specify
 * that they always want the import region folded when opening a new file.<p>
 * 
 * The majority of the time, however, code editors won't need to be that fancy,
 * and can simply use the standard <code>CODE</code> and <code>COMMENT</code>
 * fold types.
 * 
 * @author D. Campione
 * @see Fold
 * 
 */
public interface IFoldType {

    /** Denotes a <code>Fold</code> as being a region of code. */
    public static final int CODE = 0;

    /** Denotes a <code>Fold</code> as being a multi-line comment. */
    public static final int COMMENT = 1;

    /**
     * Denotes a <code>Fold</code> as being a section of import statements
     * (Java), include statements (C), etc..
     */
    public static final int IMPORTS = 2;

    /**
     * Users building advanced editors such as IDE's, that want to allow their
     * users to auto-expand/collapse foldable regions of a specific type other
     * than comments, should define their custom fold types using values
     * <code>FOLD_TYPE_USER_DEFINED_MIN + <em>n</em></code>.  That way, if new
     * default fold types are added to this interface in the future, your code
     * won't suddenly break when upgrading to a new version of STA.
     */
    public static final int FOLD_TYPE_USER_DEFINED_MIN = 1000;
}