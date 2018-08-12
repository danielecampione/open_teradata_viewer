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

import java.awt.Insets;
import java.awt.Rectangle;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class TestSubGraph2 extends GraphicViewerSubGraph {

    private static final long serialVersionUID = 336815877409875261L;

    public TestSubGraph2() {
        init();
    }

    public TestSubGraph2(String s) {
        super(s);
        init();
    }

    public void init() {
        setCollapsedLabelSpot(6);
        setInsets(new Insets(10, 10, 10, 10));
        setCollapsedInsets(new Insets(10, 10, 10, 10));
        setBorderPen(GraphicViewerPen.black);
    }

    protected void layoutHandle() {
        if (!isExpanded())
            return;
        GraphicViewerSubGraphHandle graphicviewersubgraphhandle = getHandle();
        if (graphicviewersubgraphhandle != null) {
            Rectangle rectangle = computeBorder();
            graphicviewersubgraphhandle.setTopLeft(rectangle.x, rectangle.y);
        }
    }

    protected GraphicViewerPort createPort() {
        return new TestSubGraph2Port();
    }

    public void layoutPort() {
        GraphicViewerPort graphicviewerport = getPort();
        if (graphicviewerport != null && graphicviewerport.isVisible())
            if (!isExpanded() && getCollapsedObject() != null
                    && getCollapsedObject().isVisible()) {
                graphicviewerport.setBoundingRect(getCollapsedObject()
                        .getBoundingRect());
            } else {
                Rectangle rectangle = computeBorder();
                graphicviewerport.setBoundingRect(rectangle);
            }
    }
}