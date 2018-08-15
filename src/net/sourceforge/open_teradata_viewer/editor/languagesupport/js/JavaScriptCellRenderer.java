/*
 * Open Teradata Viewer ( editor language support js )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js;

import javax.swing.Icon;
import javax.swing.JList;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.CompletionCellRenderer;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.EmptyIcon;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.FunctionCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.TemplateCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.VariableCompletion;

/**
 * The cell renderer used for JavaScript completion choices.
 * 
 * @author D. Campione
 * 
 */
public class JavaScriptCellRenderer extends CompletionCellRenderer {

    private static final long serialVersionUID = 4701950133516668348L;

    private Icon emptyIcon;

    /** Ctor. */
    public JavaScriptCellRenderer() {
        emptyIcon = new EmptyIcon(16);
    }

    /** {@inheritDoc} */
    @Override
    protected void prepareForOtherCompletion(JList list, ICompletion c,
            int index, boolean selected, boolean hasFocus) {
        super.prepareForOtherCompletion(list, c, index, selected, hasFocus);
        setIconWithDefault(c, emptyIcon);
    }

    /** {@inheritDoc} */
    @Override
    protected void prepareForTemplateCompletion(JList list,
            TemplateCompletion tc, int index, boolean selected, boolean hasFocus) {
        super.prepareForTemplateCompletion(list, tc, index, selected, hasFocus);
        setIconWithDefault(tc, IconFactory.getIcon(IconFactory.TEMPLATE_ICON));
    }

    /** {@inheritDoc} */
    @Override
    protected void prepareForVariableCompletion(JList list,
            VariableCompletion vc, int index, boolean selected, boolean hasFocus) {
        super.prepareForVariableCompletion(list, vc, index, selected, hasFocus);
        setIconWithDefault(vc,
                IconFactory.getIcon(IconFactory.LOCAL_VARIABLE_ICON));
    }

    /** {@inheritDoc} */
    @Override
    protected void prepareForFunctionCompletion(JList list,
            FunctionCompletion fc, int index, boolean selected, boolean hasFocus) {
        super.prepareForFunctionCompletion(list, fc, index, selected, hasFocus);
        setIconWithDefault(fc,
                IconFactory.getIcon(IconFactory.DEFAULT_FUNCTION_ICON));
    }
}