/*
 * Open Teradata Viewer ( editor autocomplete )
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

package net.sourceforge.open_teradata_viewer.editor.autocomplete;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * List cell renderer that delegates to a {@link ICompletionProvider}'s
 * renderer, if it has one. If it doesn't, it calls into a fallback renderer. If
 * a fallback renderer isn't specified, it simply renders <code>(({@link
 * ICompletion})value).toString()</code>.
 *
 * @author D. Campione
 * 
 */
class DelegatingCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = -7700527166002302157L;

    /**
     * The renderer to fall back on if one isn't specified by a provider. This
     * is usually <tt>this</tt>.
     */
    private ListCellRenderer fallback;

    /**
     * Returns the fallback cell renderer.
     *
     * @return The fallback cell renderer.
     * @see #setFallbackCellRenderer(ListCellRenderer)
     */
    public ListCellRenderer getFallbackCellRenderer() {
        return fallback;
    }

    /** {@inheritDoc} */
    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean selected, boolean hasFocus) {
        ICompletion c = (ICompletion) value;
        ICompletionProvider p = c.getProvider();
        ListCellRenderer r = p.getListCellRenderer();
        if (r != null) {
            return r.getListCellRendererComponent(list, value, index, selected,
                    hasFocus);
        }
        if (fallback == null) {
            return super.getListCellRendererComponent(list, value, index,
                    selected, hasFocus);
        }
        return fallback.getListCellRendererComponent(list, value, index,
                selected, hasFocus);
    }

    /**
     * Sets the fallback cell renderer.
     *
     * @param fallback The fallback cell renderer. If this is <code>null</code>,
     *        <tt>this</tt> will be used.
     * @see #getFallbackCellRenderer()
     */
    public void setFallbackCellRenderer(ListCellRenderer fallback) {
        this.fallback = fallback;
    }

    /** {@inheritDoc} */
    @Override
    public void updateUI() {
        super.updateUI();
        if ((fallback instanceof JComponent) && fallback != this) {
            ((JComponent) fallback).updateUI();
        }
    }
}