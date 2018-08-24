/*
 * Open Teradata Viewer ( editor language support html )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.html;

import javax.swing.Icon;
import javax.swing.JList;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.CompletionCellRenderer;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.FunctionCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.MarkupTagCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.VariableCompletion;

/**
 * The cell renderer used for HTML.
 *
 * @author D. Campione
 * 
 */
public class HtmlCellRenderer extends CompletionCellRenderer {

    private static final long serialVersionUID = 393588532673451572L;

    private Icon tagIcon;
    private Icon attrIcon;

    /** Ctor. */
    public HtmlCellRenderer() {
        tagIcon = getIcon("tag.png");
        attrIcon = getIcon("attribute.png");
    }

    /** {@inheritDoc} */
    @Override
    protected void prepareForFunctionCompletion(JList list,
            FunctionCompletion fc, int index, boolean selected, boolean hasFocus) {
        super.prepareForFunctionCompletion(list, fc, index, selected, hasFocus);
        setIcon(getEmptyIcon());
    }

    /** {@inheritDoc} */
    @Override
    protected void prepareForMarkupTagCompletion(JList list,
            MarkupTagCompletion c, int index, boolean selected, boolean hasFocus) {
        super.prepareForMarkupTagCompletion(list, c, index, selected, hasFocus);
        setIcon(tagIcon);
    }

    /** {@inheritDoc} */
    @Override
    protected void prepareForOtherCompletion(JList list, ICompletion c,
            int index, boolean selected, boolean hasFocus) {
        super.prepareForOtherCompletion(list, c, index, selected, hasFocus);
        if (c instanceof AttributeCompletion) {
            setIcon(attrIcon);
        } else {
            setIcon(getEmptyIcon());
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void prepareForVariableCompletion(JList list,
            VariableCompletion vc, int index, boolean selected, boolean hasFocus) {
        super.prepareForVariableCompletion(list, vc, index, selected, hasFocus);
        setIcon(getEmptyIcon());
    }
}