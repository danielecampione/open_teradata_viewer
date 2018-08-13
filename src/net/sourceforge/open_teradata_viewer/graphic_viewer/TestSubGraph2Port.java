/*
 * Open Teradata Viewer ( graphic viewer )
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

package net.sourceforge.open_teradata_viewer.graphic_viewer;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class TestSubGraph2Port extends GraphicViewerPort {

    private static final long serialVersionUID = 8909835236580306597L;

    public TestSubGraph2Port() {
        setFromSpot(NoSpot);
        setToSpot(NoSpot);
        setStyle(GraphicViewerPort.StyleHidden);
    }

    /**
     * Make this port "hollow" by not allowing a pick inside the bounds of the
     * port except along an outer margin.
     */
    public GraphicViewerObject pick(Point p, boolean selectableOnly) {
        Rectangle r = getBoundingRect();
        int margin = 5;
        if (p.x > r.x + margin && p.x < r.x + r.width - margin
                && p.y > r.y + margin && p.y < r.y + r.height - margin) {
            return null;
        } else {
            return super.pick(p, selectableOnly);
        }
    }
}