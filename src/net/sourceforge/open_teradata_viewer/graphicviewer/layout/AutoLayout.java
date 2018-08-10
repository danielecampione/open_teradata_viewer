/*
 * Open Teradata Viewer ( graphic viewer layout )
 * Copyright (C) 2012, D. Campione
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

package net.sourceforge.open_teradata_viewer.graphicviewer.layout;

import net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerDocument;
import net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerSelection;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public abstract class AutoLayout {

    public AutoLayout() {
        a = null;
        _fldif = null;
        a(null, null);
    }

    public AutoLayout(GraphicViewerDocument graphicViewerDocument) {
        a = null;
        _fldif = null;
        a(graphicViewerDocument, new Network(graphicViewerDocument));
    }

    public AutoLayout(GraphicViewerSelection graphicViewerSelection) {
        a = null;
        _fldif = null;
        a(graphicViewerSelection.getView().getDocument(), new Network(
                graphicViewerSelection));
    }

    public AutoLayout(GraphicViewerDocument graphicViewerDocument,
            Network graphicViewerNetwork) {
        a = null;
        _fldif = null;
        a(graphicViewerDocument, graphicViewerNetwork);
    }

    private void a(GraphicViewerDocument GraphicViewerDocument,
            Network graphicViewerNetwork) {
        a = GraphicViewerDocument;
        _fldif = graphicViewerNetwork;
        return;
    }

    public abstract void performLayout();

    public void progressUpdate(double d) {
    }

    public GraphicViewerDocument getDocument() {
        return a;
    }

    public void setDocument(GraphicViewerDocument graphicViewerDocument) {
        a = graphicViewerDocument;
        if (getNetwork() == null)
            setNetwork(new Network(graphicViewerDocument));
    }

    public Network getNetwork() {
        return _fldif;
    }

    public void setNetwork(Network graphicViewerNetwork) {
        _fldif = graphicViewerNetwork;
    }

    private GraphicViewerDocument a;
    private Network _fldif;
}