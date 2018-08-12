/*
 * Open Teradata Viewer ( graphic viewer layout )
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
public class RandomAutoLayout extends AutoLayout {

    public RandomAutoLayout() {
    }

    public RandomAutoLayout(GraphicViewerDocument graphicViewerDocument) {
        super(graphicViewerDocument);
        l = 100;
        o = graphicViewerDocument.getDocumentSize().width;
        k = graphicViewerDocument.getDocumentSize().height;
        n = 100;
        m = new Random();
    }

    public RandomAutoLayout(GraphicViewerDocument graphicViewerDocument,
            Network graphicViewerNetwork) {
        super(graphicViewerDocument, graphicViewerNetwork);
        l = 100;
        o = graphicViewerDocument.getDocumentSize().width;
        k = graphicViewerDocument.getDocumentSize().height;
        n = 100;
        m = new Random();
    }

    public RandomAutoLayout(GraphicViewerDocument graphicViewerDocument, int i,
            int j, int i1, int j1) {
        super(graphicViewerDocument);
        l = i;
        o = j;
        k = i1;
        n = j1;
        m = new Random();
    }

    public RandomAutoLayout(GraphicViewerDocument graphicViewerDocument,
            Network graphicViewerNetwork, int i, int j, int i1, int j1) {
        super(graphicViewerDocument, graphicViewerNetwork);
        l = i;
        o = j;
        k = i1;
        n = j1;
        m = new Random();
    }

    public void performLayout() {
        if (getNetwork() == null)
            return;
        Iterator<Object> iterator = getNetwork().getNodeIterator();
        do {
            if (!iterator.hasNext())
                break;
            NetworkNode graphicViewerNetworkNode = (NetworkNode) iterator
                    .next();
            if (!isFixed(graphicViewerNetworkNode)) {
                Point point = new Point(0, 0);
                int i = Math.abs(m.nextInt());
                int j = Math.abs(m.nextInt());
                point.x = l + i % ((o - l) + 1);
                point.y = k + j % ((n - k) + 1);
                graphicViewerNetworkNode.setCenter(point);
                graphicViewerNetworkNode.commitPosition();
            }
        } while (true);
    }

    protected boolean isFixed(NetworkNode graphicViewerNetworkNode) {
        return false;
    }

    private Random m;
    private int l;
    private int o;
    private int k;
    private int n;
}