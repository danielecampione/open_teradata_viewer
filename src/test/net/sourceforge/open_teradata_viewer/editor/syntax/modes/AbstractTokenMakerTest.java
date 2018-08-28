/*
 * Open Teradata Viewer ( editor syntax modes )
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

package test.net.sourceforge.open_teradata_viewer.editor.syntax.modes;

import javax.swing.text.Segment;

/**
 * Utility classes for unit tests for <code>ITokenMaker</code> implementations.
 *
 * @author D. Campione
 *
 */
class AbstractTokenMakerTest {

    /**
     * Creates a <code>Segment</code> from a <code>String</code>.
     *
     * @param code The string representing some code.
     * @return The code, as a <code>Segment</code>.
     */
    protected Segment createSegment(String code) {
        return new Segment(code.toCharArray(), 0, code.length());
    }

}