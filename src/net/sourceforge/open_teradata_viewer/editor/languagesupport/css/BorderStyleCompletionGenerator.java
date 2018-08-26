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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;

/**
 * A generator that returns completions for border styles.
 * 
 * @author D. Campione
 * 
 */
class BorderStyleCompletionGenerator implements ICompletionGenerator {

    private static final String ICON_KEY = "css_propertyvalue_identifier";

    /** {@inheritDoc} */
    @Override
    public List<ICompletion> generate(ICompletionProvider provider, String input) {
        List<ICompletion> completions = new ArrayList<ICompletion>();

        completions.add(new BorderStyleCompletion(provider, "none"));
        completions.add(new BorderStyleCompletion(provider, "hidden"));
        completions.add(new BorderStyleCompletion(provider, "dotted"));
        completions.add(new BorderStyleCompletion(provider, "dashed"));
        completions.add(new BorderStyleCompletion(provider, "solid"));
        completions.add(new BorderStyleCompletion(provider, "double"));
        completions.add(new BorderStyleCompletion(provider, "groove"));
        completions.add(new BorderStyleCompletion(provider, "ridge"));
        completions.add(new BorderStyleCompletion(provider, "inset"));
        completions.add(new BorderStyleCompletion(provider, "outset"));

        return completions;
    }

    /**
     * The type of completion returned by this generator.
     * 
     * @author D. Campione
     * 
     */
    private static class BorderStyleCompletion extends BasicCssCompletion {

        public BorderStyleCompletion(ICompletionProvider provider, String value) {
            super(provider, value, ICON_KEY);
        }

    }
}