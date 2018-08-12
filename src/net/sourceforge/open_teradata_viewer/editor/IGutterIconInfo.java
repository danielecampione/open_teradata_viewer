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

import javax.swing.Icon;

/**
 * Information about an icon displayed in a {@link Gutter}. Instances of this
 * class are returned by {@link Gutter#addLineTrackingIcon(int, Icon)} and
 * {@link Gutter#addOffsetTrackingIcon(int, Icon)}. They can later be used in
 * calls to {@link Gutter#removeTrackingIcon(IGutterIconInfo)} to be
 * individually removed.
 *
 * @author D. Campione
 * @see Gutter
 * 
 */
public interface IGutterIconInfo {

    /** @return The icon being rendered. */
    public Icon getIcon();

    /**
     * Returns the offset that is being tracked. The line of this offset is
     * where the icon is rendered. This offset may change as the user types to
     * track the new location of the marked offset.
     *
     * @return The offset being tracked.
     */
    public int getMarkedOffset();

    /** @return The tool tip to display when the mouse hovers over this icon. */
    public String getToolTip();
}