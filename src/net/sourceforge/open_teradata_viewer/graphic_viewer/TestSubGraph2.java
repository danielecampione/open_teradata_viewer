/*
 * Open Teradata Viewer ( graphic viewer )
 * Copyright (C), D. Campione
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

    /** Default constructor, just for copying. */
    public TestSubGraph2() {
        init();
    }

    public TestSubGraph2(String s) {
        super(s);
        init();
    }

    public void init() {
        setCollapsedLabelSpot(GraphicViewerObject.BottomCenter);
        setInsets(new Insets(10, 10, 10, 10));
        setCollapsedInsets(new Insets(10, 10, 10, 10));
        setBorderPen(GraphicViewerPen.black);
    }

    // Position the Handle at the top-left corner of the border, not just inside
    // the margins
    protected void layoutHandle() {
        if (!isExpanded()) {
            return;
        }
        GraphicViewerSubGraphHandle h = getHandle();
        if (h != null) {
            Rectangle r = computeBorder();
            h.setTopLeft(r.x, r.y);
        }
    }

    /**
     * This subgraph has a "hollow" port representing the whole subgraph.
     * <p>
     * Users will be able to starting dragging a new link along the margins of
     * the subgraph.
     */
    protected GraphicViewerPort createPort() {
        return new TestSubGraph2Port();
    }

    /**
     * When the subgraph is collapsed and there is a collapsed object, the port
     * has the same bounds as the collapsed object; otherwise, the port has the
     * same bounds as the whole subgraph.
     */
    public void layoutPort() {
        GraphicViewerPort p = getPort();
        if (p != null && p.isVisible()) {
            if (!isExpanded() && getCollapsedObject() != null
                    && getCollapsedObject().isVisible()) {
                p.setBoundingRect(getCollapsedObject().getBoundingRect());
            } else {
                Rectangle b = computeBorder();
                p.setBoundingRect(b);
            }
        }
    }
}