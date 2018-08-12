/*
 * Open Teradata Viewer ( editor syntax )
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

package net.sourceforge.open_teradata_viewer.editor.syntax;

import javax.swing.JWindow;

/**
 * A hook allowing hosting applications to decorate JWindows. For example, you
 * could use the <a href="http://jgoodies.com/">JGoodies</a> library to add drop
 * shadows to the windows. 
 *
 * @author D. Campione
 * 
 */
public abstract class PopupWindowDecorator {

    /** The singleton instance of this class. */
    private static PopupWindowDecorator decorator;

    /**
     * Callback called whenever an appropriate JWindow is created.
     * Implementations can decorate the window however they see fit.
     *
     * @param window The newly-created window.
     */
    public abstract void decorate(JWindow window);

    /**
     * Returns the singleton instance of this class. This should only be called
     * on the EDT.
     *
     * @return The singleton instance of this class, or <code>null</code> for
     *         none.
     * @see #set(PopupWindowDecorator)
     */
    public static PopupWindowDecorator get() {
        return decorator;
    }

    /**
     * Sets the singleton instance of this class. This should only be called
     * on the EDT.
     *
     * @param decorator The new instance of this class. This may be
     *                  <code>null</code>.
     * @see #get()
     */
    public static void set(PopupWindowDecorator decorator) {
        PopupWindowDecorator.decorator = decorator;
    }
}