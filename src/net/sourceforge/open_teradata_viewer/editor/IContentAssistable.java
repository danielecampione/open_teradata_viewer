/*
 * Open Teradata Viewer ( editor )
 * Copyright (C) 2015, D. Campione
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

/**
 * A component (such as a text field) that supports content assist.
 * Implementations will fire a property change event of type
 * {@link #ASSISTANCE_IMAGE} when content assist is enabled or disabled.
 *
 * @author D. Campione
 * 
 */
public interface IContentAssistable {

    /**
     * Property event fired when the image to use when the component is focused
     * changes. This will either be <code>null</code> for "no image," or a
     * <code>java.awt.Image</code>.
     */
    public static final String ASSISTANCE_IMAGE = "AssistanceImage";

}