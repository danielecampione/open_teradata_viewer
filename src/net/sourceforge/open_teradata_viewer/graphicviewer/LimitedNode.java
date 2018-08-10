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

package net.sourceforge.open_teradata_viewer.graphicviewer;

import java.awt.Point;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class LimitedNode extends GraphicViewerBasicNode {

    private static final long serialVersionUID = -6234051730240201109L;

    public LimitedNode() {
    }

    public LimitedNode(String s) {
        super(s);
    }

    public Point computeMove(int i, int j, int k, int l, Point point) {
        if (point == null)
            point = new Point(0, 0);
        if (k < 50)
            point.x = 50;
        else if (k > 500)
            point.x = 500;
        else
            point.x = k;
        point.y = l;
        return point;
    }
}