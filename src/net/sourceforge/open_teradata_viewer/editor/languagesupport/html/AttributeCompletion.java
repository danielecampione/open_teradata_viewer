/*
 * Open Teradata Viewer ( editor language support html )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.html;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.AbstractCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.IParameterizedCompletion.Parameter;

/**
 * A completion for an HTML attribute.
 *
 * @author D. Campione
 * 
 */
public class AttributeCompletion extends AbstractCompletion {

    private Parameter param;

    public AttributeCompletion(ICompletionProvider provider, Parameter param) {
        super(provider);
        this.param = param;
    }

    @Override
    public String getSummary() {
        return param.getDescription();
    }

    @Override
    public String getReplacementText() {
        return param.getName();
    }
}