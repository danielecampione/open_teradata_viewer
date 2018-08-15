/*
 * Open Teradata Viewer ( editor search )
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

package net.sourceforge.open_teradata_viewer.editor.search;

/**
 * Utility methods for this package.
 *
 * @author D. Campione
 * 
 */
class SearchUtil {

    /**
     * Formats an error message from a find/replace button enable result for use
     * in a tool tip. This assumes the error (if any) came from a
     * <code>PatternSyntaxException</code>.
     *
     * @param res The result.
     * @return The tool tip, or <code>null</code> if no error message was
     *         specified in <code>res</code>.
     */
    public static final String getToolTip(FindReplaceButtonsEnableResult res) {
        String tooltip = res.getError();
        if (tooltip != null && tooltip.indexOf('\n') > -1) {
            tooltip = tooltip.replaceFirst("\\\n", "</b><br><pre>");
            tooltip = "<html><b>" + tooltip;
        }
        return tooltip;
    }

}