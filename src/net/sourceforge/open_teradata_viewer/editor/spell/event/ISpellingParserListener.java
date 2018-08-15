/*
 * Open Teradata Viewer ( editor spell event )
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

package net.sourceforge.open_teradata_viewer.editor.spell.event;

import java.util.EventListener;

import net.sourceforge.open_teradata_viewer.editor.spell.SpellingParser;

/**
 * Listens for events from a {@link SpellingParser}. A listener of this type
 * will receive notification when:
 * 
 * <ul>
 *    <li>A word is added to the user's dictionary.</li>
 *    <li>A word will be ignored for the rest of the JVM session.</li>
 * </ul>
 *
 * @author D. Campione
 * 
 */
public interface ISpellingParserListener extends EventListener {

    /**
     * Called when an event occurs in the spelling parser.
     *
     * @param e The event.
     */
    public void spellingParserEvent(SpellingParserEvent e);

}