/*
 * Open Teradata Viewer ( graphic viewer layout )
 * Copyright (C) 2015, D. Campione
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

import java.util.Comparator;

import net.sourceforge.open_teradata_viewer.graphic_viewer.IGraphicViewerLabeledPart;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
class GraphicViewerAlphaComparer implements Comparator {

    public int compare(Object paramObject1, Object paramObject2) {
        if ((paramObject1 == null) || (paramObject2 == null)) {
            return 0;
        }
        if ((!(paramObject1 instanceof GraphicViewerTreeNetworkNode))
                || (!(paramObject2 instanceof GraphicViewerTreeNetworkNode))) {
            return 0;
        }
        GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode1 = (GraphicViewerTreeNetworkNode) paramObject1;
        GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode2 = (GraphicViewerTreeNetworkNode) paramObject2;
        if (localGraphicViewerTreeNetworkNode1 != null) {
            if (localGraphicViewerTreeNetworkNode2 != null) {
                IGraphicViewerLabeledPart localGraphicViewerLabeledPart1 = null;
                IGraphicViewerLabeledPart localGraphicViewerLabeledPart2 = null;
                if ((localGraphicViewerTreeNetworkNode1
                        .getGraphicViewerObject() instanceof IGraphicViewerLabeledPart)) {
                    localGraphicViewerLabeledPart1 = (IGraphicViewerLabeledPart) localGraphicViewerTreeNetworkNode1
                            .getGraphicViewerObject();
                }
                if ((localGraphicViewerTreeNetworkNode2
                        .getGraphicViewerObject() instanceof IGraphicViewerLabeledPart)) {
                    localGraphicViewerLabeledPart2 = (IGraphicViewerLabeledPart) localGraphicViewerTreeNetworkNode2
                            .getGraphicViewerObject();
                }
                if (localGraphicViewerLabeledPart1 != null) {
                    if (localGraphicViewerLabeledPart2 != null) {
                        return localGraphicViewerLabeledPart1.getText()
                                .compareTo(
                                        localGraphicViewerLabeledPart2
                                                .getText());
                    }
                    return 1;
                }
                if (localGraphicViewerLabeledPart2 != null) {
                    return -1;
                }
                return 0;
            }
            return 1;
        }
        if (localGraphicViewerTreeNetworkNode2 != null) {
            return -1;
        }
        return 0;
    }
}