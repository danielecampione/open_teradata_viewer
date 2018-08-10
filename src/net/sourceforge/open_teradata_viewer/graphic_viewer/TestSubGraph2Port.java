/*
 * Open Teradata Viewer ( graphic viewer )
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
        setFromSpot(-1);
        setToSpot(-1);
        setStyle(0);
    }

    public GraphicViewerObject pick(Point point, boolean flag) {
        Rectangle rectangle = getBoundingRect();
        byte byte0 = 5;
        if (point.x > rectangle.x + byte0
                && point.x < (rectangle.x + rectangle.width) - byte0
                && point.y > rectangle.y + byte0
                && point.y < (rectangle.y + rectangle.height) - byte0)
            return null;
        else
            return super.pick(point, flag);
    }
}