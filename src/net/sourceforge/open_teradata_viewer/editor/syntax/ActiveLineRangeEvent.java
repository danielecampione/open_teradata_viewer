/*
 * Open Teradata Viewer ( editor syntax )
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

package net.sourceforge.open_teradata_viewer.editor.syntax;

import java.util.EventObject;

/**
 * The event fired by {@link SyntaxTextArea}s when the active line range
 * changes.
 *
 * @author D. Campione
 * 
 */
public class ActiveLineRangeEvent extends EventObject {

    private static final long serialVersionUID = 4026499208139043731L;

    private int min;
    private int max;

    /**
     * Ctor.
     *
     * @param source The text area.
     * @param min The first line in the active line range, or <code>-1</code> if
     *            the line range is being cleared.
     * @param max The last line in the active line range, or <code>-1</code> if
     *            the line range is being cleared.
     */
    public ActiveLineRangeEvent(SyntaxTextArea source, int min, int max) {
        super(source);
        this.min = min;
        this.max = max;
    }

    /**
     * Returns the last line in the active line range.
     *
     * @return The last line, or <code>-1</code> if the range is being cleared.
     * @see #getMin()
     */
    public int getMax() {
        return max;
    }

    /**
     * Returns the first line in the active line range.
     *
     * @return The first line, or <code>-1</code> if the range is being cleared.
     * @see #getMax()
     */
    public int getMin() {
        return min;
    }
}