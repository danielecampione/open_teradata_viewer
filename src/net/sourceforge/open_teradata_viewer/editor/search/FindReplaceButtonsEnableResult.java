/*
 * Open Teradata Viewer ( editor search )
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

package net.sourceforge.open_teradata_viewer.editor.search;

/**
 * Returns the result of whether the "action" buttons such as "Find" and
 * "Replace" should be enabled.
 *
 * @author D. Campione
 * 
 */
// NOTE: This class is public to enable applications to create custom search
// dialogs that extend AbstractSearchDialog, such as a FindInFilesDialog
public class FindReplaceButtonsEnableResult {

    private boolean enable;
    private String error;

    public FindReplaceButtonsEnableResult(boolean enable, String error) {
        this.enable = enable;
        this.error = error;
    }

    public boolean getEnable() {
        return enable;
    }

    public String getError() {
        return error;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}