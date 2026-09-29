/*
 * Open Teradata Viewer ( graphic viewer layout )
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

package net.sourceforge.open_teradata_viewer.graphic_viewer.layout;

import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerDocument;
import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerSelection;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public abstract class GraphicViewerAutoLayout {

    private GraphicViewerDocument myGoDoc = null;
    private GraphicViewerNetwork myGoNetwork = null;

    public GraphicViewerAutoLayout() {
        init(null, null);
    }

    public GraphicViewerAutoLayout(GraphicViewerDocument graphicViewerDocument) {
        init(graphicViewerDocument, new GraphicViewerNetwork(
                graphicViewerDocument));
    }

    public GraphicViewerAutoLayout(GraphicViewerSelection graphicViewerSelection) {
        init(graphicViewerSelection.getView().getDocument(),
                new GraphicViewerNetwork(graphicViewerSelection));
    }

    public GraphicViewerAutoLayout(GraphicViewerDocument graphicViewerDocument,
            GraphicViewerNetwork graphicViewerNetwork) {
        init(graphicViewerDocument, graphicViewerNetwork);
    }

    private void init(GraphicViewerDocument GraphicViewerDocument,
            GraphicViewerNetwork graphicViewerNetwork) {
        myGoDoc = GraphicViewerDocument;
        myGoNetwork = graphicViewerNetwork;
    }

    public abstract void performLayout();

    public void progressUpdate(double d) {
    }

    public GraphicViewerDocument getDocument() {
        return myGoDoc;
    }

    public void setDocument(GraphicViewerDocument graphicViewerDocument) {
        myGoDoc = graphicViewerDocument;
        if (getNetwork() == null) {
            setNetwork(new GraphicViewerNetwork(graphicViewerDocument));
        }
    }

    public GraphicViewerNetwork getNetwork() {
        return myGoNetwork;
    }

    public void setNetwork(GraphicViewerNetwork graphicViewerNetwork) {
        myGoNetwork = graphicViewerNetwork;
    }
}