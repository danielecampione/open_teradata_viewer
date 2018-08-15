/*
 * Open Teradata Viewer ( editor language support js completion )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.completion;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public interface IJSCompletionUI extends ICompletion {

    static final int LOCAL_VARIABLE_RELEVANCE = 9;
    static final int GLOBAL_VARIABLE_RELEVANCE = 8;
    static final int DEFAULT_VARIABLE_RELEVANCE = 7;
    static final int STATIC_FIELD_RELEVANCE = 6;
    static final int BEAN_METHOD_RELEVANCE = 5;
    static final int DEFAULT_FUNCTION_RELEVANCE = 4;
    static final int GLOBAL_FUNCTION_RELEVANCE = 3;
    static final int DEFAULT_CLASS_RELEVANCE = 2;
    static final int BASIC_COMPLETION_RELEVANCE = 1;
    static final int TEMPLATE_RELEVANCE = 0;

}