/*
 * Open Teradata Viewer ( editor autocomplete )
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

package net.sourceforge.open_teradata_viewer.editor.autocomplete;

import java.util.List;

import javax.swing.text.JTextComponent;

/**
 * Provides completions for a {@link IParameterizedCompletion}'s parameters.
 * So, for example, if the user code-completes a function or method, if
 * a <code>IParameterChoicesProvider</code> is installed, it can return possible
 * completions for the parameters to that function or method.
 *
 * @author D. Campione
 * 
 */
public interface IParameterChoicesProvider {

    /**
     * Returns a list of choices for a specific parameter.
     *
     * @param tc The text component.
     * @param param The currently focused parameter.
     * @return The list of parameters. This may be <code>null</code> for "no
     *         parameters," but might also be an empty list.
     */
    public List<ICompletion> getParameterChoices(JTextComponent tc,
            IParameterizedCompletion.Parameter param);

}