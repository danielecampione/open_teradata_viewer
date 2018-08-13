/*
 * Open Teradata Viewer ( editor autocomplete )
 * Copyright (C) 2013, D. Campione
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractListModel;

/**
 * A list model implementation that allows the bulk addition of elements. This
 * is the only feature missing from <code>DefaultListModel</code> that we need.
 *
 * @author D. Campione
 * 
 */
class CompletionListModel extends AbstractListModel {

    private static final long serialVersionUID = -6775318186968991795L;

    /** Container for items in this model. */
    private List<ICompletion> delegate;

    /** Ctor. */
    public CompletionListModel() {
        delegate = new ArrayList<ICompletion>();
    }

    /**
     * Removes all of the elements from this list. The list will be empty after
     * this call returns (unless it throws an exception).
     *
     * @see #setContents(Collection)
     */
    public void clear() {
        int end = delegate.size() - 1;
        delegate.clear();
        if (end >= 0) {
            fireIntervalRemoved(this, 0, end);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Object getElementAt(int index) {
        return delegate.get(index);
    }

    /** {@inheritDoc} */
    @Override
    public int getSize() {
        return delegate.size();
    }

    /**
     * Sets the contents of this model. All previous contents are removed.
     *
     * @param contents The new contents of this model.
     */
    public void setContents(Collection<ICompletion> contents) {
        clear();
        if (contents.size() > 0) {
            delegate.addAll(contents);
            fireIntervalAdded(this, 0, contents.size());
        }
    }
}