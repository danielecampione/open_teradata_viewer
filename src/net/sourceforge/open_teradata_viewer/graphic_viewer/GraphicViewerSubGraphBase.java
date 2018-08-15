/*
 * Open Teradata Viewer ( graphic viewer )
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

package net.sourceforge.open_teradata_viewer.graphic_viewer;

import java.awt.Point;
import java.util.ArrayList;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerSubGraphBase extends GraphicViewerNode {

    private static final long serialVersionUID = 305229284334723726L;

    public GraphicViewerSubGraphBase() {
        setInternalFlags(getInternalFlags() & 0xfffffbff);
    }

    public static GraphicViewerSubGraphBase findParentSubGraph(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null) {
            return null;
        }
        for (GraphicViewerArea graphicviewerarea = graphicviewerobject
                .getParent(); graphicviewerarea != null; graphicviewerarea = graphicviewerarea
                .getParent()) {
            if (graphicviewerarea instanceof GraphicViewerSubGraphBase) {
                return (GraphicViewerSubGraphBase) graphicviewerarea;
            }
        }

        return null;
    }

    public static void reparentToCommonSubGraph(
            GraphicViewerObject graphicviewerobject,
            GraphicViewerObject graphicviewerobject1,
            GraphicViewerObject graphicviewerobject2, boolean flag,
            GraphicViewerLayer graphicviewerlayer) {
        GraphicViewerSubGraphBase graphicviewersubgraphbase = findParentSubGraph(graphicviewerobject1);
        GraphicViewerSubGraphBase graphicviewersubgraphbase1 = findParentSubGraph(graphicviewerobject2);
        Object obj;
        for (obj = GraphicViewerObject.findCommonParent(
                graphicviewersubgraphbase, graphicviewersubgraphbase1); obj != null
                && !(obj instanceof GraphicViewerSubGraphBase); obj = ((GraphicViewerObject) (obj))
                .getParent()) {
            ;
        }
        GraphicViewerSubGraphBase graphicviewersubgraphbase2 = null;
        if (obj instanceof GraphicViewerSubGraphBase) {
            graphicviewersubgraphbase2 = (GraphicViewerSubGraphBase) obj;
        }
        if (graphicviewerobject.getParent() != graphicviewersubgraphbase2
                || graphicviewerobject.getLayer() == null) {
            if (graphicviewerobject.getParent() == null
                    && graphicviewerobject.getLayer() == null) {
                if (graphicviewersubgraphbase2 != null) {
                    if (flag) {
                        graphicviewersubgraphbase2
                                .addObjectAtHead(graphicviewerobject);
                    } else {
                        graphicviewersubgraphbase2
                                .addObjectAtTail(graphicviewerobject);
                    }
                } else {
                    graphicviewerlayer.addObjectAtTail(graphicviewerobject);
                }
            } else {
                ArrayList arraylist = new ArrayList();
                arraylist.add(graphicviewerobject);
                if (graphicviewersubgraphbase2 != null) {
                    graphicviewersubgraphbase2.addCollection(arraylist, false,
                            null);
                } else {
                    graphicviewerlayer.addCollection(arraylist, false, null);
                }
            }
        }
    }

    public static void reparentAllLinksToSubGraphs(
            IGraphicViewerObjectSimpleCollection graphicviewerobjectsimplecollection,
            boolean flag, GraphicViewerLayer graphicviewerlayer) {
        ArrayList arraylist = new ArrayList();
        for (GraphicViewerListPosition graphicviewerlistposition = graphicviewerobjectsimplecollection
                .getFirstObjectPos(); graphicviewerlistposition != null;) {
            GraphicViewerObject graphicviewerobject = graphicviewerobjectsimplecollection
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerobjectsimplecollection
                    .getNextObjectPosAtTop(graphicviewerlistposition);
            arraylist.add(graphicviewerobject);
        }

        reparentAllLinksToSubGraphs(arraylist, flag, graphicviewerlayer);
    }

    public static void reparentAllLinksToSubGraphs(ArrayList paramArrayList,
            boolean paramBoolean, GraphicViewerLayer paramGraphicViewerLayer) {
        ArrayList localArrayList = new ArrayList();
        for (int i = 0; i < paramArrayList.size(); i++) {
            GraphicViewerObject localGraphicViewerObject = (GraphicViewerObject) paramArrayList
                    .get(i);
            Object localObject1;
            Object localObject2;
            GraphicViewerLink localGraphicViewerLink2;
            if ((localGraphicViewerObject instanceof GraphicViewerNode)) {
                localObject1 = (GraphicViewerNode) localGraphicViewerObject;
                localArrayList.clear();
                localObject2 = ((GraphicViewerNode) localObject1).findAll(6,
                        localArrayList);
                for (int j = 0; j < ((ArrayList) localObject2).size(); j++) {
                    localGraphicViewerLink2 = (GraphicViewerLink) ((ArrayList) localObject2)
                            .get(j);
                    if ((localGraphicViewerLink2 != null)
                            && (localGraphicViewerLink2.getFromPort() != null)
                            && (localGraphicViewerLink2.getToPort() != null)) {
                        reparentToCommonSubGraph(localGraphicViewerLink2,
                                localGraphicViewerLink2.getFromPort(),
                                localGraphicViewerLink2.getToPort(),
                                paramBoolean, paramGraphicViewerLayer);
                    }
                }
            } else if ((localGraphicViewerObject instanceof GraphicViewerPort)) {
                localObject1 = (GraphicViewerPort) localGraphicViewerObject;
                localArrayList.clear();
                localObject2 = ((GraphicViewerPort) localObject1)
                        .getFirstLinkPos();
                while (localObject2 != null) {
                    GraphicViewerLink localGraphicViewerLink1 = ((GraphicViewerPort) localObject1)
                            .getLinkAtPos((GraphicViewerListPosition) localObject2);
                    localObject2 = ((GraphicViewerPort) localObject1)
                            .getNextLinkPos((GraphicViewerListPosition) localObject2);
                    localArrayList.add(localGraphicViewerLink1);
                }
                for (int k = 0; k < localArrayList.size(); k++) {
                    localGraphicViewerLink2 = (GraphicViewerLink) localArrayList
                            .get(k);
                    if ((localGraphicViewerLink2 != null)
                            && (localGraphicViewerLink2.getFromPort() != null)
                            && (localGraphicViewerLink2.getToPort() != null)) {
                        reparentToCommonSubGraph(localGraphicViewerLink2,
                                localGraphicViewerLink2.getFromPort(),
                                localGraphicViewerLink2.getToPort(),
                                paramBoolean, paramGraphicViewerLayer);
                    }
                }
            } else if ((localGraphicViewerObject instanceof GraphicViewerLink)) {
                localObject1 = (GraphicViewerLink) localGraphicViewerObject;
                if ((localObject1 != null)
                        && (((GraphicViewerLink) localObject1).getFromPort() != null)
                        && (((GraphicViewerLink) localObject1).getToPort() != null)) {
                    reparentToCommonSubGraph(
                            (GraphicViewerObject) localObject1,
                            ((GraphicViewerLink) localObject1).getFromPort(),
                            ((GraphicViewerLink) localObject1).getToPort(),
                            paramBoolean, paramGraphicViewerLayer);
                }
            }
        }
    }

    public ArrayList pickObjects(Point point, boolean flag,
            ArrayList arraylist, int i) {
        if (arraylist == null) {
            arraylist = new ArrayList();
        }
        if (arraylist.size() >= i) {
            return arraylist;
        }
        if (!getBoundingRect().contains(point.x, point.y)) {
            return arraylist;
        }
        if (!isVisible()) {
            return arraylist;
        }
        for (GraphicViewerListPosition graphicviewerlistposition = getLastObjectPos(); graphicviewerlistposition != null;) {
            GraphicViewerObject graphicviewerobject = getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = getPrevObjectPos(graphicviewerlistposition);
            if (graphicviewerobject instanceof GraphicViewerSubGraphBase) {
                GraphicViewerSubGraphBase graphicviewersubgraphbase = (GraphicViewerSubGraphBase) graphicviewerobject;
                graphicviewersubgraphbase
                        .pickObjects(point, flag, arraylist, i);
            } else {
                GraphicViewerObject graphicviewerobject1 = graphicviewerobject
                        .pick(point, flag);
                if (graphicviewerobject1 != null) {
                    if (!arraylist.contains(graphicviewerobject1)) {
                        arraylist.add(graphicviewerobject1);
                    }
                    if (arraylist.size() >= i) {
                        return arraylist;
                    }
                }
            }
        }

        if (isPickableBackground() && (!flag || isSelectable())
                && !arraylist.contains(this)) {
            arraylist.add(this);
        }
        return arraylist;
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerSubGraphBase",
                    domelement);
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument,
            IDomElement domelement, IDomElement domelement1) {
        if (domelement1 != null) {
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        }
        return domelement.getNextSibling();
    }
}