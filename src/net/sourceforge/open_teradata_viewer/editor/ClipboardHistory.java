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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A history of text added to the clipboard.
 *
 * Listens for cuts and copies from instances of {@link TextArea}. This is used
 * for the "clipboard history" shortcut (Ctrl+Shift+V by default).<p>
 *
 * Note that this class does not listen for all events on the system clipboard,
 * because that functionality is pretty fragile. See <a
 * href="http://stackoverflow.com/questions/5484927/listen-to-clipboard-changes-check-ownership">
 * http://stackoverflow.com/questions/5484927/listen-to-clipboard-changes-check-ownership</a>
 * for more information.
 *
 * @author D. Campione
 *
 */
public class ClipboardHistory {

    private static ClipboardHistory INSTANCE;

    private List<String> history;
    private int maxSize;

    private static final int DEFAULT_MAX_SIZE = 12;

    private ClipboardHistory() {
        history = new ArrayList<String>();
        maxSize = DEFAULT_MAX_SIZE;
    }

    /**
     * Adds an entry to the clipboard history.
     *
     * @param str The text to add.
     * @see #getHistory()
     */
    public void add(String str) {
        int size = history.size();
        if (size == 0) {
            history.add(str);
        } else {
            int index = history.indexOf(str);
            if (index != size - 1) {
                if (index > -1) {
                    history.remove(index);
                }
                history.add(str);
            }
            trim();
        }
    }

    /**
     * Returns the singleton instance of this class, lazily creating it if
     * necessary.<p>
     *
     * This method should only be called on the EDT.
     *
     * @return The singleton instance of this class.
     */
    public static final ClipboardHistory get() {
        if (INSTANCE == null) {
            INSTANCE = new ClipboardHistory();
        }
        return INSTANCE;
    }

    /**
     * Returns the clipboard history, in most-recently-used order.
     *
     * @return The clipboard history.
     */
    public List<String> getHistory() {
        List<String> copy = new ArrayList<String>(this.history);
        Collections.reverse(copy);
        return copy;
    }

    /**
     * @return The maximum number of clipboard values remembered.
     * @see #setMaxSize(int)
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * Sets the maximum number of clipboard values remembered.
     *
     * @param maxSize The maximum number of clipboard values to remember.
     * @throws IllegalArgumentException If <code>maxSize</code> is not greater
     *         than zero.
     * @see #getMaxSize()
     */
    public void setMaxSize(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("Maximum size must be >= 0");
        }
        this.maxSize = maxSize;
        trim();
    }

    /**
     * Ensures the remembered set of strings is not larger than the maximum
     * allowed size.
     */
    private void trim() {
        while (history.size() > maxSize) {
            history.remove(0);
        }
    }
}