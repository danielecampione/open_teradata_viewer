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
        _mthfor(g() & 0xfffffbff);
    }

    public static GraphicViewerSubGraphBase findParentSubGraph(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null)
            return null;
        for (GraphicViewerArea graphicviewerarea = graphicviewerobject
                .getParent(); graphicviewerarea != null; graphicviewerarea = graphicviewerarea
                .getParent())
            if (graphicviewerarea instanceof GraphicViewerSubGraphBase)
                return (GraphicViewerSubGraphBase) graphicviewerarea;

        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
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
                .getParent());
        GraphicViewerSubGraphBase graphicviewersubgraphbase2 = null;
        if (obj instanceof GraphicViewerSubGraphBase)
            graphicviewersubgraphbase2 = (GraphicViewerSubGraphBase) obj;
        if (graphicviewerobject.getParent() != graphicviewersubgraphbase2
                || graphicviewerobject.getLayer() == null)
            if (graphicviewerobject.getParent() == null
                    && graphicviewerobject.getLayer() == null) {
                if (graphicviewersubgraphbase2 != null) {
                    if (flag)
                        graphicviewersubgraphbase2
                                .addObjectAtHead(graphicviewerobject);
                    else
                        graphicviewersubgraphbase2
                                .addObjectAtTail(graphicviewerobject);
                } else {
                    graphicviewerlayer.addObjectAtTail(graphicviewerobject);
                }
            } else {
                ArrayList arraylist = new ArrayList();
                arraylist.add(graphicviewerobject);
                if (graphicviewersubgraphbase2 != null)
                    graphicviewersubgraphbase2.addCollection(arraylist, false,
                            null);
                else
                    graphicviewerlayer.addCollection(arraylist, false, null);
            }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void reparentAllLinksToSubGraphs(
            GraphicViewerObjectSimpleCollection graphicviewerobjectsimplecollection,
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

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void reparentAllLinksToSubGraphs(ArrayList arraylist,
            boolean flag, GraphicViewerLayer graphicviewerlayer) {
        ArrayList arraylist1 = new ArrayList();
        label0 : for (int i = 0; i < arraylist.size(); i++) {
            GraphicViewerObject graphicviewerobject = (GraphicViewerObject) arraylist
                    .get(i);
            if (graphicviewerobject instanceof GraphicViewerNode) {
                GraphicViewerNode graphicviewernode = (GraphicViewerNode) graphicviewerobject;
                arraylist1.clear();
                ArrayList arraylist2 = graphicviewernode.findAll(6, arraylist1);
                int j = 0;
                do {
                    if (j >= arraylist2.size())
                        continue label0;
                    GraphicViewerLink graphicviewerlink2 = (GraphicViewerLink) arraylist2
                            .get(j);
                    if (graphicviewerlink2 != null
                            && graphicviewerlink2.getFromPort() != null
                            && graphicviewerlink2.getToPort() != null)
                        reparentToCommonSubGraph(graphicviewerlink2,
                                graphicviewerlink2.getFromPort(),
                                graphicviewerlink2.getToPort(), flag,
                                graphicviewerlayer);
                    j++;
                } while (true);
            }
            if (graphicviewerobject instanceof GraphicViewerPort) {
                GraphicViewerPort graphicviewerport = (GraphicViewerPort) graphicviewerobject;
                arraylist1.clear();
                for (GraphicViewerListPosition graphicviewerlistposition = graphicviewerport
                        .getFirstLinkPos(); graphicviewerlistposition != null;) {
                    GraphicViewerLink graphicviewerlink1 = graphicviewerport
                            .getLinkAtPos(graphicviewerlistposition);
                    graphicviewerlistposition = graphicviewerport
                            .getNextLinkPos(graphicviewerlistposition);
                    arraylist1.add(graphicviewerlink1);
                }

                int k = 0;
                do {
                    if (k >= arraylist1.size())
                        continue label0;
                    GraphicViewerLink graphicviewerlink3 = (GraphicViewerLink) arraylist1
                            .get(k);
                    if (graphicviewerlink3 != null
                            && graphicviewerlink3.getFromPort() != null
                            && graphicviewerlink3.getToPort() != null)
                        reparentToCommonSubGraph(graphicviewerlink3,
                                graphicviewerlink3.getFromPort(),
                                graphicviewerlink3.getToPort(), flag,
                                graphicviewerlayer);
                    k++;
                } while (true);
            }
            if (!(graphicviewerobject instanceof GraphicViewerLink))
                continue;
            GraphicViewerLink graphicviewerlink = (GraphicViewerLink) graphicviewerobject;
            if (graphicviewerlink != null
                    && graphicviewerlink.getFromPort() != null
                    && graphicviewerlink.getToPort() != null)
                reparentToCommonSubGraph(graphicviewerlink,
                        graphicviewerlink.getFromPort(),
                        graphicviewerlink.getToPort(), flag, graphicviewerlayer);
        }

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public ArrayList pickObjects(Point point, boolean flag,
            ArrayList arraylist, int i) {
        if (arraylist == null)
            arraylist = new ArrayList();
        if (arraylist.size() >= i)
            return arraylist;
        if (!getBoundingRect().contains(point.x, point.y))
            return arraylist;
        if (!isVisible())
            return arraylist;
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
                    if (!arraylist.contains(graphicviewerobject1))
                        arraylist.add(graphicviewerobject1);
                    if (arraylist.size() >= i)
                        return arraylist;
                }
            }
        }

        if (isPickableBackground() && (!flag || isSelectable())
                && !arraylist.contains(this))
            arraylist.add(this);
        return arraylist;
    }

    public void SVGWriteObject(DomDoc domdoc, DomElement domelement) {
        @SuppressWarnings("unused")
        DomElement domelement1;
        if (domdoc.GraphicViewerXMLOutputEnabled())
            domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerSubGraphBase",
                            domelement);
        super.SVGWriteObject(domdoc, domelement);
    }

    public DomNode SVGReadObject(DomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, DomElement domelement,
            DomElement domelement1) {
        if (domelement1 != null)
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        return domelement.getNextSibling();
    }
}