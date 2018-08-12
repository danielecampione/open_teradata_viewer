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

import java.awt.Color;
import java.awt.Rectangle;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class TestIconicNode extends GraphicViewerIconicNode {

    private static final long serialVersionUID = -662402734272827918L;

    public TestIconicNode() {
        myBack = null;
    }

    public TestIconicNode(String s) {
        super(s);
        myBack = null;
        GraphicViewerRectangle graphicviewerrectangle = new GraphicViewerRectangle();
        graphicviewerrectangle.setSelectable(false);
        graphicviewerrectangle.setVisible(false);
        graphicviewerrectangle.setBrush(GraphicViewerBrush
                .makeStockBrush(new Color(0, 0, 139)));
        graphicviewerrectangle.setPen(null);
        graphicviewerrectangle.setBoundingRect(getBoundingRect());
        addObjectAtHead(graphicviewerrectangle);
        myBack = graphicviewerrectangle;
    }

    public GraphicViewerObject copyObject(
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        TestIconicNode testiconicnode = (TestIconicNode) super
                .copyObject(graphicviewercopyenvironment);
        testiconicnode.myBack = (GraphicViewerDrawable) graphicviewercopyenvironment
                .get(myBack);
        return testiconicnode;
    }

    public void layoutChildren(GraphicViewerObject graphicviewerobject) {
        super.layoutChildren(graphicviewerobject);
        Rectangle rectangle = getIcon().getBoundingRect();
        Rectangle rectangle1 = new Rectangle(rectangle.x, rectangle.y,
                rectangle.width, rectangle.height);
        rectangle1 = rectangle1.union(getLabel().getBoundingRect());
        if (getBackground() != null) {
            getBackground().setBoundingRect(rectangle1);
            addObjectAtHead(getBackground());
        }
    }

    protected void gainedSelection(GraphicViewerSelection graphicviewerselection) {
        getBackground().setVisible(true);
        getLabel().setBold(true);
        getLabel().setTextColor(GraphicViewerBrush.ColorWhite);
    }

    protected void lostSelection(GraphicViewerSelection graphicviewerselection) {
        getBackground().setVisible(false);
        getLabel().setBold(false);
        getLabel().setTextColor(GraphicViewerBrush.ColorBlack);
    }

    public GraphicViewerDrawable getBackground() {
        return myBack;
    }

    private GraphicViewerDrawable myBack;
}