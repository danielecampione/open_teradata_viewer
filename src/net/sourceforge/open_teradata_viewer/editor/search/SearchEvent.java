/*
 * Open Teradata Viewer ( editor search )
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

package net.sourceforge.open_teradata_viewer.editor.search;

import java.util.EventObject;

import net.sourceforge.open_teradata_viewer.editor.SearchContext;

/**
 * The event fired whenever a user wants to search for or replace text in a Find
 * or Replace dialog/tool bar.
 *
 * @author D. Campione
 * 
 */
public class SearchEvent extends EventObject {

    private static final long serialVersionUID = 5087727953995181235L;

    private SearchContext context;
    private Type type;

    public SearchEvent(Object source, Type type, SearchContext context) {
        super(source);
        this.type = type;
        this.context = context;
    }

    public Type getType() {
        return type;
    }

    public SearchContext getSearchContext() {
        return context;
    }

    /**
     * Types of search events.
     * 
     * @author D. Campione
     * 
     */
    public static enum Type {

        /** The event fired when the text to "mark all" has changed. */
        MARK_ALL,

        /** The event fired when the user wants to find text in the editor. */
        FIND,

        /**
         * The event fired when the user wants to replace text in the editor.
         */
        REPLACE,

        /**
         * The event fired when the user wants to replace all instances of
         * specific text with new text in the editor.
         */
        REPLACE_ALL;

    }
}