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

import javax.swing.Icon;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.BasicCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.TemplateCompletion;

/**
 * A generator that returns completions for CSS colors.
 * 
 * @author D. Campione
 * 
 */
class ColorCompletionGenerator implements ICompletionGenerator {

    private List<ICompletion> defaults;

    private static final String FUNC_ICON_KEY = "css_propertyvalue_function";
    private static final String ICON_KEY = "css_propertyvalue_identifier";

    private static final Pattern DIGITS = Pattern.compile("\\d*");

    public ColorCompletionGenerator(ICompletionProvider provider) {
        defaults = createDefaults(provider);
    }

    private static final List<ICompletion> createDefaults(
            ICompletionProvider provider) {
        List<ICompletion> completions = new ArrayList<ICompletion>();

        // CSS2 colors
        completions.add(new ColorCompletion(provider, "black"));
        completions.add(new ColorCompletion(provider, "silver"));
        completions.add(new ColorCompletion(provider, "gray"));
        completions.add(new ColorCompletion(provider, "white"));
        completions.add(new ColorCompletion(provider, "maroon"));
        completions.add(new ColorCompletion(provider, "red"));
        completions.add(new ColorCompletion(provider, "purple"));
        completions.add(new ColorCompletion(provider, "fuchsia"));
        completions.add(new ColorCompletion(provider, "green"));
        completions.add(new ColorCompletion(provider, "lime"));
        completions.add(new ColorCompletion(provider, "olive"));
        completions.add(new ColorCompletion(provider, "yellow"));
        completions.add(new ColorCompletion(provider, "navy"));
        completions.add(new ColorCompletion(provider, "blue"));
        completions.add(new ColorCompletion(provider, "teal"));
        completions.add(new ColorCompletion(provider, "aqua"));
        completions.add(new ColorCompletion(provider, "orange"));

        completions.add(new ColorCompletion(provider, "currentColor"));
        completions.add(new ColorCompletion(provider, "transparent"));
        completions.add(new ColorTemplateCompletion(provider, "#",
                "#${rgb}${cursor}", "#RGB"));
        completions.add(new ColorTemplateCompletion(provider, "#",
                "#${rrggbb}${cursor}", "#RRGGBB"));
        completions.add(new ColorTemplateCompletion(provider, "rgb",
                "rgb(${red}, ${green}, ${blue})${cursor}", "rgb(r, g, b)"));
        completions.add(new ColorTemplateCompletion(provider, "rgba",
                "rgba(${red}, ${green}, ${blue}, ${alpha})${cursor}",
                "rgba(r, g, b, a)"));
        completions.add(new ColorTemplateCompletion(provider, "hsl",
                "hsl(${hue}, ${saturation}, ${brightness})${cursor}",
                "hsl(h, s, b)"));
        completions
                .add(new ColorTemplateCompletion(
                        provider,
                        "hsla",
                        "hsla(${hue}, ${saturation}, ${brightness}, ${alpha})${cursor}",
                        "hsla(h, s, b, a)"));

        return completions;
    }

    /** {@inheritDoc} */
    @Override
    public List<ICompletion> generate(ICompletionProvider provider, String input) {
        List<ICompletion> completions = new ArrayList<ICompletion>(defaults);

        if (DIGITS.matcher(input).matches()) {
            completions.add(new ColorCompletion(provider, input + "s"));
            completions.add(new ColorCompletion(provider, input + "ms"));
        }

        return completions;
    }

    /**
     * A completion for an RGB color.
     * 
     * @author D. Campione
     * 
     */
    private static class ColorTemplateCompletion extends TemplateCompletion {

        public ColorTemplateCompletion(ICompletionProvider provider,
                String input, String template, String desc) {
            super(provider, input, desc, template, desc, null);
            boolean function = template.indexOf('(') > -1;
            setIcon(IconFactory.get().getIcon(
                    function ? FUNC_ICON_KEY : ICON_KEY));

        }

    }

    /**
     * The type of completion returned by this generator.
     * 
     * @author D. Campione
     * 
     */
    private static class ColorCompletion extends BasicCompletion {

        public ColorCompletion(ICompletionProvider provider, String value) {
            super(provider, value);
        }

        @Override
        public Icon getIcon() {
            return IconFactory.get().getIcon(ICON_KEY);
        }
    }
}