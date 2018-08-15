/*
 * Open Teradata Viewer ( editor language support java )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JList;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;

/**
 * The renderer used for parameter completions (for methods) in Java.
 *
 * @author D. Campione
 * 
 */
public class JavaParamListCellRenderer extends JavaCellRenderer {

    private static final long serialVersionUID = -2210932551904976773L;

    public JavaParamListCellRenderer() {
        // Param completions don't display type info, etc.., because all
        // completions for a single parameter have the same type (or subclass
        // that type)
        setSimpleText(true);
    }

    /**
     * Returns the preferred size of a particular cell. Note that the parent
     * class {@link JavaCellRenderer} doesn't override this method, because it
     * doesn't use the cells to dictate the preferred size of the list, due to
     * the large number of completions it shows at a time.
     */
    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        d.width += 32; // Looks better when less scrunched
        return d;
    }

    /**
     * Returns the renderer.
     *
     * @param list The list of choices being rendered.
     * @param value The {@link ICompletion} being rendered.
     * @param index The index into <code>list</code> being rendered.
     * @param selected Whether the item is selected.
     * @param hasFocus Whether the item has focus.
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean selected, boolean hasFocus) {
        super.getListCellRendererComponent(list, value, index, selected,
                hasFocus);
        IJavaSourceCompletion ajsc = (IJavaSourceCompletion) value;
        setIcon(ajsc.getIcon());
        return this;
    }
}