/*
 * Open Teradata Viewer ( editor autocomplete )
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

package net.sourceforge.open_teradata_viewer.editor.autocomplete;

import java.util.EventObject;

/**
 * An event fired by an instance of {@link AutoCompletion}. This can be used by
 * applications that wish to be notified of the auto-complete popup window
 * showing and hiding.
 *
 * @author D. Campione
 * 
 */
public class AutoCompletionEvent extends EventObject {

    private static final long serialVersionUID = -6775605041689083535L;

    /** The type of this event. */
    private Type type;

    /**
     * Ctor.
     *
     * @param source The <coded>AutoCompletion</code> instance that fired this
     *        event.
     * @param type The event type.
     */
    public AutoCompletionEvent(AutoCompletion source, Type type) {
        super(source);
        this.type = type;
    }

    /**
     * Returns the source <code>AutoCompletion</code> instance. This is just
     * shorthand for <code>return (AutoCompletion)getSource();</code>.
     *
     * @return The source <code>AutoCompletion</code> instance.
     */
    public AutoCompletion getAutoCompletion() {
        return (AutoCompletion) getSource();
    }

    /** @return The type of this event. */
    public Type getEventType() {
        return type;
    }

    /**
     * Enumeration of the various types of this event.
     * 
     * @author D. Campione
     * 
     */
    public static enum Type {
        POPUP_SHOWN, POPUP_HIDDEN
    }
}