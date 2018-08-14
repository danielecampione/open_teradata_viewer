/*
 * Open Teradata Viewer ( editor )
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

package net.sourceforge.open_teradata_viewer.editor;

import java.awt.Dimension;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

/**
 * A combo box whose width cannot go over a specified value. This class is
 * useful when you have a layout manager that adheres to the combo box's
 * preferred/maximum sizes (such as <code>SpringLayout</code>), and your combo
 * box contains a value longer than you'd like - the combo box is drawn too
 * large and the GUI looks ugly. With this class you can set a maximum width for
 * the combo box, and its height will never be affected.
 *
 * @author D. Campione
 * 
 */
public class MaxWidthComboBox extends JComboBox {

    private static final long serialVersionUID = 4371171720133689187L;

    /** The width of this combo box will never be greater than this value. */
    private int maxWidth;

    /**
     * Ctor.
     *
     * @param maxWidth The maximum width for this combo box.
     */
    public MaxWidthComboBox(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    /**
     * Ctor.
     *
     * @param model The model for this combo box.
     * @param maxWidth The maximum width for this combo box.
     */
    public MaxWidthComboBox(ComboBoxModel model, int maxWidth) {
        super(model);
        this.maxWidth = maxWidth;
    }

    /**
     * Overridden to ensure that the returned size has width no greater than the
     * specified maximum.
     *
     * @return The maximum size of this combo box.
     */
    @Override
    public Dimension getMaximumSize() {
        Dimension size = super.getMaximumSize();
        size.width = Math.min(size.width, maxWidth);
        return size;
    }

    /**
     * Overridden to ensure that the returned size has width no greater than the
     * specified maximum.
     *
     * @return The minimum size of this combo box.
     */
    @Override
    public Dimension getMinimumSize() {
        Dimension size = super.getMinimumSize();
        size.width = Math.min(size.width, maxWidth);
        return size;
    }

    /**
     * Overridden to ensure that the returned size has width no greater than the
     * specified maximum.
     *
     * @return The preferred size of this combo box.
     */
    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.width = Math.min(size.width, maxWidth);
        return size;
    }
}