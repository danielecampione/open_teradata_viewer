/*
 * Open Teradata Viewer ( editor language support css )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.css;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;

/**
 * A generator that returns completions for common font names (not the
 * standardized generic fonts defined in the CSS spec).
 *
 * @author D. Campione
 *
 */
class CommonFontCompletionGenerator implements ICompletionGenerator {

    private static final String ICON_KEY = "css_propertyvalue_identifier"; /* font */

    /** {@inheritDoc} */
    @Override
    public List<ICompletion> generate(ICompletionProvider provider, String input) {
        List<ICompletion> completions = new ArrayList<ICompletion>();

        completions.add(new FontFamilyCompletion(provider, "Georgia"));
        completions.add(new FontFamilyCompletion(provider,
                "\"Times New Roman\""));
        completions.add(new FontFamilyCompletion(provider, "Arial"));
        completions.add(new FontFamilyCompletion(provider, "Helvetica"));
        completions.add(new FontFamilyCompletion(provider, "Impact"));
        completions.add(new FontFamilyCompletion(provider,
                "\"Lucida Sans Unicode\""));
        completions.add(new FontFamilyCompletion(provider, "Tahoma"));
        completions.add(new FontFamilyCompletion(provider, "Verdana"));
        completions.add(new FontFamilyCompletion(provider, "Geneva"));
        completions.add(new FontFamilyCompletion(provider, "\"Courier New\""));
        completions.add(new FontFamilyCompletion(provider, "Courier"));
        completions
                .add(new FontFamilyCompletion(provider, "\"Lucida Console\""));
        completions.add(new FontFamilyCompletion(provider, "Menlo"));
        completions.add(new FontFamilyCompletion(provider, "Monaco"));
        completions.add(new FontFamilyCompletion(provider, "Consolas"));

        return completions;
    }

    /**
     * The type of completion returned by this generator.
     *
     * @author D. Campione
     *
     */
    private static class FontFamilyCompletion extends BasicCssCompletion {

        public FontFamilyCompletion(ICompletionProvider provider, String value) {
            super(provider, value, ICON_KEY);
        }

    }
}