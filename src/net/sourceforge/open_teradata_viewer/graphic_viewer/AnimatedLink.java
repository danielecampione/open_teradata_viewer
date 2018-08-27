/*
 * Open Teradata Viewer ( graphic viewer )
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

package net.sourceforge.open_teradata_viewer.graphic_viewer;

import java.awt.Graphics2D;
import java.awt.Point;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class AnimatedLink extends GraphicViewerLink {

    private static final long serialVersionUID = 3430579971715994686L;

    transient private int mySeg = 0;
    transient private float myDist = 0;

    public AnimatedLink() {
    }

    public AnimatedLink(GraphicViewerPort from, GraphicViewerPort to) {
        super(from, to);
    }

    public void paint(Graphics2D g, GraphicViewerView view) {
        super.paint(g, view);
        GraphicViewerStroke s = this;
        if (mySeg >= s.getNumPoints() - 1) {
            mySeg = 0;
        }
        Point a = s.getPoint(mySeg);
        Point b = s.getPoint(mySeg + 1);
        double len = Math.sqrt((b.x - a.x) * (b.x - a.x) + (b.y - a.y)
                * (b.y - a.y));
        int x = b.x;
        int y = b.y;
        if (myDist >= len) {
            mySeg++;
            myDist = 0;
        } else if (len >= 1) {
            x = (int) (a.x + (b.x - a.x) * myDist / len);
            y = (int) (a.y + (b.y - a.y) * myDist / len);
        }
        GraphicViewerDrawable.drawEllipse(g, null, GraphicViewerBrush.red,
                x - 3, y - 3, 7, 7);
    }

    public void step() {
        myDist += 3;
        update();
    }
}