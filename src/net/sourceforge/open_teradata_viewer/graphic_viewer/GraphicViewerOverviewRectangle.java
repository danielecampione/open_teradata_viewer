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
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerOverviewRectangle extends GraphicViewerRectangle
        implements
            IGraphicViewerViewListener,
            ComponentListener {

    private static final long serialVersionUID = -1244601428380489567L;

    private boolean myChanging = false;

    public GraphicViewerOverviewRectangle() {
    }

    public GraphicViewerOverviewRectangle(Point point, Dimension dimension) {
        super(point, dimension);
        setPen(GraphicViewerPen.make(65535, 8, new Color(0, 128, 128)));
        setResizable(false);
    }

    private GraphicViewerView getObserved() {
        if (getView() instanceof GraphicViewerOverview) {
            GraphicViewerOverview graphicvieweroverview = (GraphicViewerOverview) getView();
            return graphicvieweroverview.getObserved();
        } else {
            return null;
        }
    }

    public void updateRectFromView() {
        GraphicViewerView graphicviewerview = getObserved();
        if (graphicviewerview == null) {
            return;
        }
        if (myChanging) {
            return;
        } else {
            myChanging = true;
            setBoundingRect(graphicviewerview.getViewPosition(),
                    graphicviewerview.getExtentSize());
            getView().scrollRectToVisible(getBoundingRect());
            myChanging = false;
            return;
        }
    }

    public void setBoundingRect(int i, int j, int k, int l) {
        if (getView() == null) {
            return;
        }
        GraphicViewerView graphicviewerview = getObserved();
        if (graphicviewerview != null) {
            Point point = graphicviewerview.getDocumentTopLeft();
            Dimension dimension = graphicviewerview.getDocumentSize();
            if (i + k > point.x + dimension.width) {
                i = (point.x + dimension.width) - k;
            }
            if (i < point.x) {
                i = point.x;
            }
            if (j + l > point.y + dimension.height) {
                j = (point.y + dimension.height) - l;
            }
            if (j < point.y) {
                j = point.y;
            }
        }
        if (!getView().isIncludingNegativeCoords()) {
            if (i < 0) {
                i = 0;
            }
            if (j < 0) {
                j = 0;
            }
        }
        super.setBoundingRect(i, j, k, l);
    }

    protected void geometryChange(Rectangle rectangle) {
        if (getView() == null) {
            return;
        }
        GraphicViewerView graphicviewerview = getObserved();
        if (graphicviewerview == null) {
            return;
        }
        if (myChanging) {
            return;
        } else {
            myChanging = true;
            graphicviewerview.setViewPosition(getTopLeft());
            myChanging = false;
            return;
        }
    }

    protected void gainedSelection(GraphicViewerSelection graphicviewerselection) {
    }

    protected void lostSelection(GraphicViewerSelection graphicviewerselection) {
    }

    public void viewChanged(GraphicViewerViewEvent graphicviewerviewevent) {
        switch (graphicviewerviewevent.getHint()) {
            case 1 : // '\001'
                GraphicViewerView graphicviewerview = getObserved();
                if (graphicviewerview != null
                        && graphicviewerview.getDocument() != graphicviewerviewevent
                                .getObject()) {
                    if (graphicviewerviewevent.getObject() instanceof GraphicViewerDocument) {
                        GraphicViewerDocument graphicviewerdocument = (GraphicViewerDocument) graphicviewerviewevent
                                .getObject();
                        graphicviewerdocument
                                .removeDocumentListener((GraphicViewerView) graphicviewerviewevent
                                        .getSource());
                    }
                    graphicviewerview.getDocument().addDocumentListener(
                            (GraphicViewerView) graphicviewerviewevent
                                    .getSource());
                }
                updateRectFromView();
                break;

            case 107 : // 'k'
            case 108 : // 'l'
                updateRectFromView();
                break;
        }
    }

    public void componentHidden(ComponentEvent componentevent) {
    }

    public void componentMoved(ComponentEvent componentevent) {
    }

    public void componentShown(ComponentEvent componentevent) {
    }

    public void componentResized(ComponentEvent componentevent) {
        updateRectFromView();
    }
}