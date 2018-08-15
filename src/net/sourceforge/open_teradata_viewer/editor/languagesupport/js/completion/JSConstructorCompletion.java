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

import javax.swing.Icon;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.MethodInfo;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.IconFactory;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class JSConstructorCompletion extends JSFunctionCompletion {

    public JSConstructorCompletion(ICompletionProvider provider,
            MethodInfo method) {
        super(provider, method);
    }

    @Override
    public Icon getIcon() {
        return IconFactory.getIcon(IconFactory.FUNCTION_ICON);
    }
}