/*
 * Open Teradata Viewer ( editor )
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

package net.sourceforge.open_teradata_viewer.editor;

/**
 * Provides various ways to render a caret such as {@link ConfigurableCaret}..
 *
 * Currently supported renderings include:
 * 
 * <ol>
 *    <li>As a vertical line (like <code>DefaultCaret</code>)</li>
 *    <li>As a slightly thicker vertical line (like Eclipse)</li>
 *    <li>As an underline</li>
 *    <li>As a "block caret"</li>
 *    <li>As a rectangle around the current character</li>
 * </li>
 * 
 * @author D. Campione
 * 
 */
public enum CaretStyle {

    VERTICAL_LINE_STYLE,

    UNDERLINE_STYLE,

    BLOCK_STYLE,

    BLOCK_BORDER_STYLE,

    THICK_VERTICAL_LINE_STYLE;
}