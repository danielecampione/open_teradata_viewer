/*
 * Open Teradata Viewer ( editor language support c )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.c;

import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JList;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.CompletionCellRenderer;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.EmptyIcon;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.FunctionCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.VariableCompletion;

/**
 * The cell renderer used for the C programming language.
 *
 * @author D. Campione
 * 
 */
class CCellRenderer extends CompletionCellRenderer {

    private static final long serialVersionUID = -4835472752365224494L;

    private Icon variableIcon;
    private Icon functionIcon;
    private Icon emptyIcon;

    /** Ctor. */
    public CCellRenderer() {
        emptyIcon = new EmptyIcon(16); // Should be done first
        variableIcon = getIcon("/icons/var.png");
        functionIcon = getIcon("/icons/function.png");
    }

    /**
     * Returns an icon.
     *
     * @param resource The icon to retrieve. This should either be a file or a
     *        resource loadable by the current ClassLoader.
     * @return The icon.
     */
    private Icon getIcon(String resource) {
        URL url = getClass().getResource(resource); // Should never be null
        return url != null ? new ImageIcon(url) : emptyIcon;
    }

    /** {@inheritDoc} */
    @Override
    protected void prepareForOtherCompletion(JList list, ICompletion c,
            int index, boolean selected, boolean hasFocus) {
        super.prepareForOtherCompletion(list, c, index, selected, hasFocus);
        setIcon(emptyIcon);
    }

    /** {@inheritDoc} */
    @Override
    protected void prepareForVariableCompletion(JList list,
            VariableCompletion vc, int index, boolean selected, boolean hasFocus) {
        super.prepareForVariableCompletion(list, vc, index, selected, hasFocus);
        setIcon(variableIcon);
    }

    /** {@inheritDoc} */
    @Override
    protected void prepareForFunctionCompletion(JList list,
            FunctionCompletion fc, int index, boolean selected, boolean hasFocus) {
        super.prepareForFunctionCompletion(list, fc, index, selected, hasFocus);
        setIcon(functionIcon);
    }
}