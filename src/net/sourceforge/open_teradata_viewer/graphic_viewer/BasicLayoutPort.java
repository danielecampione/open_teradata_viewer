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

/**
 * To get the link arrowheads' point at the edge of the ellipse, we need to
 * override how the link point is computed, rather than depending on the
 * built-in mechanism specifying a spot.
 * 
 * @author D. Campione
 *
 */
public class BasicLayoutPort extends GraphicViewerPort {

    private static final long serialVersionUID = 4166518283906407331L;

    public GraphicViewerEllipse myEllipse = null;

    public BasicLayoutPort() {
        super();
        setSelectable(false);
        setDraggable(false);
        setStyle(StyleEllipse); // Black circle/ellipse
        // Use custom link spots for both links coming in and going out
        setFromSpot(GraphicViewerObject.NoSpot);
        setToSpot(GraphicViewerObject.NoSpot);
    }

    /** Return a point on the edge of the ellipse. */
    public Point getLinkPointFromPoint(int x, int y, Point p) {
        if (p == null) {
            p = new Point();
        }
        p.x = x;
        p.y = y;

        Point center = getSpotLocation(GraphicViewerObject.Center);
        double x1 = (double) x;
        double y1 = (double) y;
        double x2 = center.x;
        double y2 = center.y;

        center = myEllipse.getSpotLocation(GraphicViewerObject.Center);
        double U = center.x;
        double V = center.y;

        double P = myEllipse.getWidth();
        double Q = myEllipse.getHeight();

        double A = (4.0 / (P * P)) * (x2 - x1) * (x2 - x1) + (4.0 / (Q * Q))
                * (y2 - y1) * (y2 - y1);
        double B = (8.0 / (P * P)) * (x2 - x1) * (x1 - U) + (8.0 / (Q * Q))
                * (y2 - y1) * (y1 - V);
        double C = (4.0 / (P * P)) * (x1 - U) * (x1 - U) + (4.0 / (Q * Q))
                * (y1 - V) * (y1 - V) - 1.0;
        double D = B * B - 4.0 * A * C;

        if (D < 0) {
            return p;
        }

        double T1 = (-1.0 * B - Math.sqrt(D)) / (2.0 * A);
        double T2 = (-1.0 * B + Math.sqrt(D)) / (2.0 * A);

        double t1 = Math.min(T1, T2);
        double t2 = Math.max(T1, T2);

        if ((0.0 <= t1) && (t1 <= 1.0)) {
            p.x = (int) Math.round(x1 + t1 * (x2 - x1));
            p.y = (int) Math.round(y1 + t1 * (y2 - y1));
            return p;
        } else if ((0.0 <= t2) && (t2 <= 1.0)) {
            p.x = (int) Math.round(x1 + t2 * (x2 - x1));
            p.y = (int) Math.round(y1 + t2 * (y2 - y1));
            return p;
        }

        return p;
    }
}