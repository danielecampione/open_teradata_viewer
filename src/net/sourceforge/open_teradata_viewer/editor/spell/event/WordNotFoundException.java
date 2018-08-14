/*
 * Open Teradata Viewer ( editor spell event )
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

package net.sourceforge.open_teradata_viewer.editor.spell.event;

/**
 * An exception to indicate that there not enough words as expected.
 * 
 * @author D. Campione
 * 
 */
public class WordNotFoundException extends RuntimeException {

    private static final long serialVersionUID = -5043183865809643733L;

    //~ Constructors ............................................................

    /** Creates a new WordNotFoundException object. */
    public WordNotFoundException() {
        super();
    }

    /**
     * Creates a new WordNotFoundException object.
     *
     * @param s A message.
     */
    public WordNotFoundException(String s) {
        super(s);
    }
}