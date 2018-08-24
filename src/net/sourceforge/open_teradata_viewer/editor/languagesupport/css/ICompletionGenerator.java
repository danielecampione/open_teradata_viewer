/*
 * Open Teradata Viewer ( editor language support css )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.css;

import java.util.List;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public interface ICompletionGenerator {

    /**
     * Generates a list of completions based on the current user input.
     *
     * @param provider The completion provider querying for completions.
     * @param input The current user input.
     * @return The list of completions. This may be <code>null</code> if no
     *         completions are appropriate.
     */
    List<ICompletion> generate(ICompletionProvider provider, String input);

}