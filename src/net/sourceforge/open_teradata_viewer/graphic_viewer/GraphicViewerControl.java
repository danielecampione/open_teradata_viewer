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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JComponent;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public abstract class GraphicViewerControl extends GraphicViewerObject {

    private static final long serialVersionUID = -4367533898300911419L;

    private transient HashMap myMap = null;

    public GraphicViewerControl() {
    }

    public GraphicViewerControl(Rectangle rectangle) {
        super(rectangle);
    }

    public GraphicViewerControl(Point point, Dimension dimension) {
        super(point, dimension);
    }

    protected void ownerChange(
            IGraphicViewerObjectCollection graphicviewerobjectcollection,
            IGraphicViewerObjectCollection graphicviewerobjectcollection1,
            GraphicViewerObject graphicviewerobject) {
        super.ownerChange(graphicviewerobjectcollection,
                graphicviewerobjectcollection1, graphicviewerobject);
        if (graphicviewerobjectcollection != null
                && graphicviewerobjectcollection1 == null
                && (graphicviewerobjectcollection instanceof GraphicViewerDocument)) {
            Iterator iterator = getIterator();
            do {
                if (!iterator.hasNext()) {
                    break;
                }
                Entry entry = (Entry) iterator.next();
                GraphicViewerView graphicviewerview2 = (GraphicViewerView) entry
                        .getKey();
                JComponent jcomponent2 = (JComponent) entry.getValue();
                if (graphicviewerview2 != null && jcomponent2 != null) {
                    graphicviewerview2.removeControl(this, jcomponent2);
                }
            } while (true);
            getMap().clear();
        } else if (graphicviewerobjectcollection == null
                && graphicviewerobjectcollection1 != null
                && (graphicviewerobjectcollection1 instanceof GraphicViewerDocument)
                && isVisible()) {
            GraphicViewerDocument graphicviewerdocument1 = (GraphicViewerDocument) graphicviewerobjectcollection1;
            IGraphicViewerDocumentListener agraphicviewerdocumentlistener[] = graphicviewerdocument1
                    .getDocumentListeners();
            for (int i = 0; i < agraphicviewerdocumentlistener.length; i++) {
                IGraphicViewerDocumentListener graphicviewerdocumentlistener = agraphicviewerdocumentlistener[i];
                if (graphicviewerdocumentlistener instanceof GraphicViewerView) {
                    GraphicViewerView graphicviewerview3 = (GraphicViewerView) graphicviewerdocumentlistener;
                    if (graphicviewerview3 != null) {
                        getComponent(graphicviewerview3);
                    }
                }
            }

        } else if (graphicviewerobjectcollection != null
                && graphicviewerobjectcollection1 == null
                && (graphicviewerobjectcollection instanceof GraphicViewerView)) {
            GraphicViewerView graphicviewerview = (GraphicViewerView) graphicviewerobjectcollection;
            if (getMap().get(graphicviewerview) != null) {
                JComponent jcomponent = (JComponent) getMap().remove(
                        graphicviewerview);
                if (jcomponent != null) {
                    graphicviewerview.removeControl(this, jcomponent);
                }
            }
        } else if (graphicviewerobjectcollection == null
                && graphicviewerobjectcollection1 != null
                && (graphicviewerobjectcollection1 instanceof GraphicViewerView)
                && isVisible()) {
            GraphicViewerView graphicviewerview1 = (GraphicViewerView) graphicviewerobjectcollection1;
            getComponent(graphicviewerview1);
        }
    }

    public JComponent getComponent(GraphicViewerView graphicviewerview) {
        JComponent jcomponent = (JComponent) getMap().get(graphicviewerview);
        if (jcomponent == null) {
            jcomponent = createComponent(graphicviewerview);
            if (jcomponent != null) {
                getMap().put(graphicviewerview, jcomponent);
                graphicviewerview.addControl(this, jcomponent);
                jcomponent.setVisible(true);
                jcomponent.repaint();
                graphicviewerview.getCanvas().validate();
            }
        }
        return jcomponent;
    }

    public abstract JComponent createComponent(
            GraphicViewerView graphicviewerview);

    public void setVisible(boolean flag) {
        if (flag != isVisible()) {
            Iterator iterator = getIterator();
            do {
                if (!iterator.hasNext()) {
                    break;
                }
                Entry entry = (Entry) iterator.next();
                GraphicViewerView graphicviewerview = (GraphicViewerView) entry
                        .getKey();
                JComponent jcomponent = (JComponent) entry.getValue();
                if (graphicviewerview != null) {
                    if (flag && jcomponent == null) {
                        getComponent(graphicviewerview);
                    } else if (!flag && jcomponent != null) {
                        graphicviewerview.removeControl(this, jcomponent);
                        getMap().put(graphicviewerview, null);
                    }
                }
            } while (true);
        }
        super.setVisible(flag);
    }

    protected void geometryChange(Rectangle rectangle) {
        super.geometryChange(rectangle);
        Rectangle rectangle1 = new Rectangle(0, 0, 0, 0);
        Iterator iterator = getIterator();
        do {
            if (!iterator.hasNext()) {
                break;
            }
            Entry entry = (Entry) iterator.next();
            GraphicViewerView graphicviewerview = (GraphicViewerView) entry
                    .getKey();
            JComponent jcomponent = (JComponent) entry.getValue();
            if (graphicviewerview != null && jcomponent != null) {
                Rectangle rectangle2 = getBoundingRect();
                rectangle1.x = rectangle2.x;
                rectangle1.y = rectangle2.y;
                rectangle1.width = rectangle2.width;
                rectangle1.height = rectangle2.height;
                graphicviewerview.convertDocToView(rectangle1);
                jcomponent.setBounds(rectangle1);
                jcomponent.repaint();
                graphicviewerview.getCanvas().validate();
            }
        } while (true);
    }

    public void paint(Graphics2D graphics2d, GraphicViewerView graphicviewerview) {
        JComponent jcomponent = getComponent(graphicviewerview);
        if (jcomponent != null) {
            Rectangle rectangle = graphicviewerview.getTempRectangle();
            Rectangle rectangle1 = getBoundingRect();
            rectangle.x = rectangle1.x;
            rectangle.y = rectangle1.y;
            rectangle.width = rectangle1.width;
            rectangle.height = rectangle1.height;
            graphicviewerview.convertDocToView(rectangle);
            if (!jcomponent.getBounds().equals(rectangle)) {
                jcomponent.setBounds(rectangle);
                jcomponent.setVisible(true);
                graphicviewerview.getCanvas().validate();
            }
        }
    }

    public Iterator getIterator() {
        return getMap().entrySet().iterator();
    }

    HashMap getMap() {
        if (myMap == null) {
            myMap = new HashMap();
        }
        return myMap;
    }
}