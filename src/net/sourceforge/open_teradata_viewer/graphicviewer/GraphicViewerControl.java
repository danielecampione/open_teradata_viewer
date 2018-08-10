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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JComponent;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public abstract class GraphicViewerControl extends GraphicViewerObject {

    private static final long serialVersionUID = -4367533898300911419L;

    public GraphicViewerControl() {
        dT = null;
    }

    public GraphicViewerControl(Rectangle rectangle) {
        super(rectangle);
        dT = null;
    }

    public GraphicViewerControl(Point point, Dimension dimension) {
        super(point, dimension);
        dT = null;
    }

    @SuppressWarnings("rawtypes")
    protected void ownerChange(
            GraphicViewerObjectCollection graphicviewerobjectcollection,
            GraphicViewerObjectCollection graphicviewerobjectcollection1,
            GraphicViewerObject graphicviewerobject) {
        super.ownerChange(graphicviewerobjectcollection,
                graphicviewerobjectcollection1, graphicviewerobject);
        if (graphicviewerobjectcollection != null
                && graphicviewerobjectcollection1 == null
                && (graphicviewerobjectcollection instanceof GraphicViewerDocument)) {
            @SuppressWarnings("unused")
            GraphicViewerDocument graphicviewerdocument = (GraphicViewerDocument) graphicviewerobjectcollection;
            Iterator iterator = getIterator();
            do {
                if (!iterator.hasNext())
                    break;
                java.util.Map.Entry entry = (java.util.Map.Entry) iterator
                        .next();
                GraphicViewerView graphicviewerview2 = (GraphicViewerView) entry
                        .getKey();
                JComponent jcomponent2 = (JComponent) entry.getValue();
                if (graphicviewerview2 != null && jcomponent2 != null)
                    graphicviewerview2._mthif(this, jcomponent2);
            } while (true);
            x().clear();
        } else if (graphicviewerobjectcollection == null
                && graphicviewerobjectcollection1 != null
                && (graphicviewerobjectcollection1 instanceof GraphicViewerDocument)
                && isVisible()) {
            GraphicViewerDocument graphicviewerdocument1 = (GraphicViewerDocument) graphicviewerobjectcollection1;
            GraphicViewerDocumentListener agraphicviewerdocumentlistener[] = graphicviewerdocument1
                    .getDocumentListeners();
            for (int i = 0; i < agraphicviewerdocumentlistener.length; i++) {
                GraphicViewerDocumentListener graphicviewerdocumentlistener = agraphicviewerdocumentlistener[i];
                if (graphicviewerdocumentlistener instanceof GraphicViewerView) {
                    GraphicViewerView graphicviewerview3 = (GraphicViewerView) graphicviewerdocumentlistener;
                    @SuppressWarnings("unused")
                    JComponent jcomponent3;
                    if (graphicviewerview3 != null)
                        jcomponent3 = getComponent(graphicviewerview3);
                }
            }

        } else if (graphicviewerobjectcollection != null
                && graphicviewerobjectcollection1 == null
                && (graphicviewerobjectcollection instanceof GraphicViewerView)) {
            GraphicViewerView graphicviewerview = (GraphicViewerView) graphicviewerobjectcollection;
            if (x().get(graphicviewerview) != null) {
                JComponent jcomponent = (JComponent) x().remove(
                        graphicviewerview);
                if (jcomponent != null)
                    graphicviewerview._mthif(this, jcomponent);
            }
        } else if (graphicviewerobjectcollection == null
                && graphicviewerobjectcollection1 != null
                && (graphicviewerobjectcollection1 instanceof GraphicViewerView)
                && isVisible()) {
            GraphicViewerView graphicviewerview1 = (GraphicViewerView) graphicviewerobjectcollection1;
            @SuppressWarnings("unused")
            JComponent jcomponent1 = getComponent(graphicviewerview1);
        }
    }

    @SuppressWarnings("unchecked")
    public JComponent getComponent(GraphicViewerView graphicviewerview) {
        JComponent jcomponent = (JComponent) x().get(graphicviewerview);
        if (jcomponent == null) {
            jcomponent = createComponent(graphicviewerview);
            if (jcomponent != null) {
                x().put(graphicviewerview, jcomponent);
                graphicviewerview.a(this, jcomponent);
                jcomponent.setVisible(true);
                jcomponent.repaint();
                graphicviewerview.getCanvas().validate();
            }
        }
        return jcomponent;
    }

    public abstract JComponent createComponent(
            GraphicViewerView graphicviewerview);

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void setVisible(boolean flag) {
        if (flag != isVisible()) {
            Iterator iterator = getIterator();
            do {
                if (!iterator.hasNext())
                    break;
                java.util.Map.Entry entry = (java.util.Map.Entry) iterator
                        .next();
                GraphicViewerView graphicviewerview = (GraphicViewerView) entry
                        .getKey();
                JComponent jcomponent = (JComponent) entry.getValue();
                if (graphicviewerview != null)
                    if (flag && jcomponent == null)
                        getComponent(graphicviewerview);
                    else if (!flag && jcomponent != null) {
                        graphicviewerview._mthif(this, jcomponent);
                        x().put(graphicviewerview, null);
                    }
            } while (true);
        }
        super.setVisible(flag);
    }

    @SuppressWarnings("rawtypes")
    protected void geometryChange(Rectangle rectangle) {
        super.geometryChange(rectangle);
        Rectangle rectangle1 = new Rectangle(0, 0, 0, 0);
        Iterator iterator = getIterator();
        do {
            if (!iterator.hasNext())
                break;
            java.util.Map.Entry entry = (java.util.Map.Entry) iterator.next();
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
            Rectangle rectangle = graphicviewerview.c();
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

    @SuppressWarnings("rawtypes")
    public Iterator getIterator() {
        return x().entrySet().iterator();
    }

    @SuppressWarnings("rawtypes")
    HashMap x() {
        if (dT == null)
            dT = new HashMap();
        return dT;
    }

    @SuppressWarnings("rawtypes")
    private transient HashMap dT;
}