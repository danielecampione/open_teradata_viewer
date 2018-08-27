/*
 * Open Teradata Viewer ( editor spell event )
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

package net.sourceforge.open_teradata_viewer.editor.spell.event;

import java.util.EventListener;

/**
 * This is the event based listener interface.
 *
 * @author D. Campione
 * 
 */
public interface ISpellCheckListener extends EventListener {

    /**
     * Propagates the spelling errors to listeners.
     * 
     * @param event The event to handle.
     */
    public void spellingError(ISpellCheckEvent event);

}