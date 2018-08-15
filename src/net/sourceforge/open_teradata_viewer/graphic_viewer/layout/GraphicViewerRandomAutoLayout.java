/*
 * Open Teradata Viewer ( graphic viewer layout )
 * Copyright (C) 2014, D. Campione
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

package net.sourceforge.open_teradata_viewer.graphic_viewer.layout;

import java.awt.Point;
import java.util.Iterator;
import java.util.Random;

import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerDocument;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerRandomAutoLayout extends GraphicViewerAutoLayout {

    private Random r;
    private int minx;
    private int maxx;
    private int miny;
    private int maxy;

    public GraphicViewerRandomAutoLayout() {
    }

    public GraphicViewerRandomAutoLayout(
            GraphicViewerDocument graphicViewerDocument) {
        super(graphicViewerDocument);
        minx = 100;
        maxx = graphicViewerDocument.getDocumentSize().width;
        miny = graphicViewerDocument.getDocumentSize().height;
        maxy = 100;
        r = new Random();
    }

    public GraphicViewerRandomAutoLayout(
            GraphicViewerDocument graphicViewerDocument,
            GraphicViewerNetwork graphicViewerNetwork) {
        super(graphicViewerDocument, graphicViewerNetwork);
        minx = 100;
        maxx = graphicViewerDocument.getDocumentSize().width;
        miny = graphicViewerDocument.getDocumentSize().height;
        maxy = 100;
        r = new Random();
    }

    public GraphicViewerRandomAutoLayout(
            GraphicViewerDocument graphicViewerDocument, int i, int j, int i1,
            int j1) {
        super(graphicViewerDocument);
        minx = i;
        maxx = j;
        miny = i1;
        maxy = j1;
        r = new Random();
    }

    public GraphicViewerRandomAutoLayout(
            GraphicViewerDocument graphicViewerDocument,
            GraphicViewerNetwork graphicViewerNetwork, int i, int j, int i1,
            int j1) {
        super(graphicViewerDocument, graphicViewerNetwork);
        minx = i;
        maxx = j;
        miny = i1;
        maxy = j1;
        r = new Random();
    }

    public void performLayout() {
        if (getNetwork() == null) {
            return;
        }
        Iterator iterator = getNetwork().getNodeIterator();
        do {
            if (!iterator.hasNext()) {
                break;
            }
            GraphicViewerNetworkNode graphicViewerNetworkNode = (GraphicViewerNetworkNode) iterator
                    .next();
            if (!isFixed(graphicViewerNetworkNode)) {
                Point point = new Point(0, 0);
                int i = Math.abs(r.nextInt());
                int j = Math.abs(r.nextInt());
                point.x = minx + i % ((maxx - minx) + 1);
                point.y = miny + j % ((maxy - miny) + 1);
                graphicViewerNetworkNode.setCenter(point);
                graphicViewerNetworkNode.commitPosition();
            }
        } while (true);
    }

    protected boolean isFixed(GraphicViewerNetworkNode graphicViewerNetworkNode) {
        return false;
    }
}