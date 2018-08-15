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

/**
 * Passed to {@link IExternalURLHandler}s as a way for them to display a summary
 * for a new completion in response to a link event.
 *
 * @author D. Campione
 * @see IExternalURLHandler
 */
public interface IDescWindowCallback {

    /**
     * Callback allowing a new code completion's description to be displayed in
     * the description window.
     *
     * @param completion The new completion.
     * @param anchor The anchor to scroll to, or <code>null</code> if none.
     */
    public void showSummaryFor(ICompletion completion, String anchor);

}