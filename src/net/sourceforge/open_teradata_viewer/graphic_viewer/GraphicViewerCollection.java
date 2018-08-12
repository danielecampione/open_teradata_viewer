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

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerCollection
        implements
            IGraphicViewerObjectCollection,
            Serializable {

    private static final long serialVersionUID = 3589279540531798916L;

    public GraphicViewerCollection() {
        by = new GraphicViewerObjList();
    }

    public GraphicViewerCollection(
            IGraphicViewerObjectSimpleCollection graphicviewerobjectsimplecollection) {
        by = new GraphicViewerObjList();
        addCollection(graphicviewerobjectsimplecollection);
    }

    public GraphicViewerCollection(GraphicViewerObject agraphicviewerobject[]) {
        by = new GraphicViewerObjList();
        addArray(agraphicviewerobject);
    }

    public int getNumObjects() {
        return by.getNumObjects();
    }

    public boolean isEmpty() {
        return by.isEmpty();
    }

    public GraphicViewerListPosition getFirstObjectPos() {
        return by.getFirstObjectPos();
    }

    public GraphicViewerObject getObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        return by.getObjectAtPos(graphicviewerlistposition);
    }

    public GraphicViewerListPosition getNextObjectPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        return by.getNextObjectPos(graphicviewerlistposition);
    }

    public GraphicViewerListPosition getNextObjectPosAtTop(
            GraphicViewerListPosition graphicviewerlistposition) {
        return by.getNextObjectPos(graphicviewerlistposition);
    }

    public GraphicViewerListPosition getLastObjectPos() {
        return by.getLastObjectPos();
    }

    public GraphicViewerListPosition getPrevObjectPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        return by.getPrevObjectPos(graphicviewerlistposition);
    }

    public GraphicViewerListPosition findObject(
            GraphicViewerObject graphicviewerobject) {
        return by.findObject(graphicviewerobject);
    }

    public GraphicViewerListPosition addObjectAtHead(
            GraphicViewerObject graphicviewerobject) {
        return by.addObjectAtHead(graphicviewerobject);
    }

    public GraphicViewerListPosition addObjectAtTail(
            GraphicViewerObject graphicviewerobject) {
        return by.addObjectAtTail(graphicviewerobject);
    }

    public GraphicViewerListPosition insertObjectBefore(
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerObject graphicviewerobject) {
        return by.insertObjectBefore(graphicviewerlistposition,
                graphicviewerobject);
    }

    public GraphicViewerListPosition insertObjectAfter(
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerObject graphicviewerobject) {
        return by.insertObjectAfter(graphicviewerlistposition,
                graphicviewerobject);
    }

    public void bringObjectToFront(GraphicViewerObject graphicviewerobject) {
        if (getLastObject() == graphicviewerobject) {
            return;
        } else {
            by.removeObject(graphicviewerobject);
            by.addObjectAtTail(graphicviewerobject);
            return;
        }
    }

    public void sendObjectToBack(GraphicViewerObject graphicviewerobject) {
        if (getFirstObject() == graphicviewerobject) {
            return;
        } else {
            by.removeObject(graphicviewerobject);
            by.addObjectAtHead(graphicviewerobject);
            return;
        }
    }

    public void removeObject(GraphicViewerObject graphicviewerobject) {
        by.removeObject(graphicviewerobject);
    }

    public GraphicViewerObject removeObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        return by.removeObjectAtPos(graphicviewerlistposition);
    }

    public GraphicViewerObject pickObject(Point point, boolean flag) {
        for (GraphicViewerListPosition graphicviewerlistposition = by
                .getLastObjectPos(); graphicviewerlistposition != null; graphicviewerlistposition = by
                .getPrevObjectPos(graphicviewerlistposition)) {
            GraphicViewerObject graphicviewerobject = by
                    .getObjectAtPos(graphicviewerlistposition);
            GraphicViewerObject graphicviewerobject1 = graphicviewerobject
                    .pick(point, flag);
            if (graphicviewerobject1 != null)
                return graphicviewerobject1;
        }

        return null;
    }

    public GraphicViewerObject getFirstObject() {
        GraphicViewerListPosition graphicviewerlistposition = by
                .getFirstObjectPos();
        if (graphicviewerlistposition != null)
            return by.getObjectAtPos(graphicviewerlistposition);
        else
            return null;
    }

    public GraphicViewerObject getLastObject() {
        GraphicViewerListPosition graphicviewerlistposition = by
                .getLastObjectPos();
        if (graphicviewerlistposition != null)
            return by.getObjectAtPos(graphicviewerlistposition);
        else
            return null;
    }

    public GraphicViewerObject[] toArray() {
        int i = by.getNumObjects();
        GraphicViewerObject agraphicviewerobject[] = new GraphicViewerObject[i];
        int j = 0;
        for (GraphicViewerListPosition graphicviewerlistposition = by
                .getFirstObjectPos(); graphicviewerlistposition != null;) {
            GraphicViewerObject graphicviewerobject = by
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = by
                    .getNextObjectPos(graphicviewerlistposition);
            agraphicviewerobject[j++] = graphicviewerobject;
        }

        return agraphicviewerobject;
    }

    public void addArray(GraphicViewerObject agraphicviewerobject[]) {
        for (int i = 0; i < agraphicviewerobject.length; i++)
            by.addObjectAtTail(agraphicviewerobject[i]);

    }

    public void addCollection(ArrayList<?> arraylist) {
        for (int i = 0; i < arraylist.size(); i++)
            by.addObjectAtTail((GraphicViewerObject) arraylist.get(i));

    }

    public void addCollection(
            IGraphicViewerObjectSimpleCollection graphicviewerobjectsimplecollection) {
        for (GraphicViewerListPosition graphicviewerlistposition = graphicviewerobjectsimplecollection
                .getFirstObjectPos(); graphicviewerlistposition != null;) {
            GraphicViewerObject graphicviewerobject = graphicviewerobjectsimplecollection
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerobjectsimplecollection
                    .getNextObjectPos(graphicviewerlistposition);
            by.addObjectAtTail(graphicviewerobject);
        }

    }

    private GraphicViewerObjList by;
}