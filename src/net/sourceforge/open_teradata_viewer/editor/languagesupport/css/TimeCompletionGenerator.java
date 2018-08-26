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
import java.util.regex.Pattern;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;

/**
 * A generator that returns completions for CSS percentage/length values.
 * 
 * @author D. Campione
 * 
 */
class TimeCompletionGenerator implements ICompletionGenerator {

    private static final String ICON_KEY = "css_propertyvalue_identifier"; /* time */

    private static final Pattern DIGITS = Pattern.compile("\\d*");

    /** {@inheritDoc} */
    @Override
    public List<ICompletion> generate(ICompletionProvider provider, String input) {
        List<ICompletion> completions = new ArrayList<ICompletion>();

        if (DIGITS.matcher(input).matches()) {
            completions.add(new TimeCompletion(provider, input + "s"));
            completions.add(new TimeCompletion(provider, input + "ms"));
        }

        return completions;
    }

    /**
     * The type of completion returned by this generator.
     * 
     * @author D. Campione
     * 
     */
    private static class TimeCompletion extends BasicCssCompletion {

        public TimeCompletion(ICompletionProvider provider, String value) {
            super(provider, value, ICON_KEY);
        }

    }
}