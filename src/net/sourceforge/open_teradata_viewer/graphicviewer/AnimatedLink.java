/*
 * Open Teradata Viewer ( graphic viewer )
 * Copyright (C) 2011, D. Campione
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

    private transient int mySeg = 0;
    private transient float myDist = 0.0F;

    public AnimatedLink() {
    }

    public AnimatedLink(GraphicViewerPort paramGraphicViewerPort1,
            GraphicViewerPort paramGraphicViewerPort2) {
        super(paramGraphicViewerPort1, paramGraphicViewerPort2);
    }
    public void paint(Graphics2D paramGraphics2D,
            GraphicViewerView paramGraphicViewerView) {
        super.paint(paramGraphics2D, paramGraphicViewerView);
        AnimatedLink localAnimatedLink = this;
        if (this.mySeg >= localAnimatedLink.getNumPoints() - 1)
            this.mySeg = 0;
        Point localPoint1 = localAnimatedLink.getPoint(this.mySeg);
        Point localPoint2 = localAnimatedLink.getPoint(this.mySeg + 1);
        double d = Math.sqrt((localPoint2.x - localPoint1.x)
                * (localPoint2.x - localPoint1.x)
                + (localPoint2.y - localPoint1.y)
                * (localPoint2.y - localPoint1.y));
        int i = localPoint2.x;
        int j = localPoint2.y;
        if (this.myDist >= d) {
            this.mySeg += 1;
            this.myDist = 0.0F;
        } else if (d >= 1.0D) {
            i = (int) (localPoint1.x + (localPoint2.x - localPoint1.x)
                    * this.myDist / d);
            j = (int) (localPoint1.y + (localPoint2.y - localPoint1.y)
                    * this.myDist / d);
        }
        GraphicViewerDrawable.drawEllipse(paramGraphics2D, null,
                GraphicViewerBrush.red, i - 3, j - 3, 7, 7);
    }

    public void step() {
        this.myDist += 3.0F;
        update();
    }
}
