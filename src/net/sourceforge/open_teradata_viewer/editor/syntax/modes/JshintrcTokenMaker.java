/*
 * Open Teradata Viewer ( editor syntax modes )
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

package net.sourceforge.open_teradata_viewer.editor.syntax.modes;

/**
 * Scanner for .jshintrc files. This is equivalent to JSON with C-style
 * end-of-line comments.
 *
 * @author D. Campione
 *
 */
public class JshintrcTokenMaker extends JsonTokenMaker {

    /** Ctor; overridden to enable highlighting of EOL comments. */
    public JshintrcTokenMaker() {
        setHighlightEolComments(true);
    }

}