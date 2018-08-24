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

import javax.swing.Icon;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ShorthandCompletion;

/**
 * A completion for a CSS property name.
 *
 * @author D. Campione
 * 
 */
class PropertyCompletion extends ShorthandCompletion {

    private String iconKey;

    public PropertyCompletion(ICompletionProvider provider, String property,
            String iconKey) {
        super(provider, property, property + ": ");
        this.iconKey = iconKey;
    }

    @Override
    public Icon getIcon() {
        return IconFactory.get().getIcon(iconKey);
    }
}