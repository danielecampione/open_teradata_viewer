/*
 * Open Teradata Viewer ( editor syntax )
 * Copyright (C) 2012, D. Campione
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

package net.sourceforge.open_teradata_viewer.editor.syntax;

import java.util.EventListener;

/**
 * Listens for "active line range" events from an <code>SyntaxTextArea</code>.
 * If a text area contains some semantic knowledge of the programming language
 * being edited, it may broadcast {@link ActiveLineRangeEvent}s whenever the
 * caret moves into a new "block" of code. Listeners can listen for these events
 * and respond accordingly.<p>
 *
 * @author D. Campione
 * 
 */
public interface IActiveLineRangeListener extends EventListener {

    /**
     * Called whenever the "active line range" changes.
     *
     * @param e Information about the line range change.  If there is no longer
     *          an "active line range," the "minimum" and "maximum" line values
     *          should both be <code>-1</code>.
     */
    public void activeLineRangeChanged(ActiveLineRangeEvent e);

}