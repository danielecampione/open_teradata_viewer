/*
 * Open Teradata Viewer ( graphic viewer )
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

    private GraphicViewerObjList myObjects = new GraphicViewerObjList();

    public GraphicViewerCollection() {
    }

    public GraphicViewerCollection(
            IGraphicViewerObjectSimpleCollection graphicviewerobjectsimplecollection) {
        addCollection(graphicviewerobjectsimplecollection);
    }

    public GraphicViewerCollection(GraphicViewerObject agraphicviewerobject[]) {
        addArray(agraphicviewerobject);
    }

    public int getNumObjects() {
        return myObjects.getNumObjects();
    }

    public boolean isEmpty() {
        return myObjects.isEmpty();
    }

    public GraphicViewerListPosition getFirstObjectPos() {
        return myObjects.getFirstObjectPos();
    }

    public GraphicViewerObject getObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        return myObjects.getObjectAtPos(graphicviewerlistposition);
    }

    public GraphicViewerListPosition getNextObjectPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        return myObjects.getNextObjectPos(graphicviewerlistposition);
    }

    public GraphicViewerListPosition getNextObjectPosAtTop(
            GraphicViewerListPosition graphicviewerlistposition) {
        return myObjects.getNextObjectPos(graphicviewerlistposition);
    }

    public GraphicViewerListPosition getLastObjectPos() {
        return myObjects.getLastObjectPos();
    }

    public GraphicViewerListPosition getPrevObjectPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        return myObjects.getPrevObjectPos(graphicviewerlistposition);
    }

    public GraphicViewerListPosition findObject(
            GraphicViewerObject graphicviewerobject) {
        return myObjects.findObject(graphicviewerobject);
    }

    public GraphicViewerListPosition addObjectAtHead(
            GraphicViewerObject graphicviewerobject) {
        return myObjects.addObjectAtHead(graphicviewerobject);
    }

    public GraphicViewerListPosition addObjectAtTail(
            GraphicViewerObject graphicviewerobject) {
        return myObjects.addObjectAtTail(graphicviewerobject);
    }

    public GraphicViewerListPosition insertObjectBefore(
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerObject graphicviewerobject) {
        return myObjects.insertObjectBefore(graphicviewerlistposition,
                graphicviewerobject);
    }

    public GraphicViewerListPosition insertObjectAfter(
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerObject graphicviewerobject) {
        return myObjects.insertObjectAfter(graphicviewerlistposition,
                graphicviewerobject);
    }

    public void bringObjectToFront(GraphicViewerObject graphicviewerobject) {
        if (getLastObject() == graphicviewerobject) {
            return;
        } else {
            myObjects.removeObject(graphicviewerobject);
            myObjects.addObjectAtTail(graphicviewerobject);
            return;
        }
    }

    public void sendObjectToBack(GraphicViewerObject graphicviewerobject) {
        if (getFirstObject() == graphicviewerobject) {
            return;
        } else {
            myObjects.removeObject(graphicviewerobject);
            myObjects.addObjectAtHead(graphicviewerobject);
            return;
        }
    }

    public void removeObject(GraphicViewerObject graphicviewerobject) {
        myObjects.removeObject(graphicviewerobject);
    }

    public GraphicViewerObject removeObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        return myObjects.removeObjectAtPos(graphicviewerlistposition);
    }

    public GraphicViewerObject pickObject(Point point, boolean flag) {
        for (GraphicViewerListPosition graphicviewerlistposition = myObjects
                .getLastObjectPos(); graphicviewerlistposition != null; graphicviewerlistposition = myObjects
                .getPrevObjectPos(graphicviewerlistposition)) {
            GraphicViewerObject graphicviewerobject = myObjects
                    .getObjectAtPos(graphicviewerlistposition);
            GraphicViewerObject graphicviewerobject1 = graphicviewerobject
                    .pick(point, flag);
            if (graphicviewerobject1 != null) {
                return graphicviewerobject1;
            }
        }

        return null;
    }

    public GraphicViewerObject getFirstObject() {
        GraphicViewerListPosition graphicviewerlistposition = myObjects
                .getFirstObjectPos();
        if (graphicviewerlistposition != null) {
            return myObjects.getObjectAtPos(graphicviewerlistposition);
        } else {
            return null;
        }
    }

    public GraphicViewerObject getLastObject() {
        GraphicViewerListPosition graphicviewerlistposition = myObjects
                .getLastObjectPos();
        if (graphicviewerlistposition != null) {
            return myObjects.getObjectAtPos(graphicviewerlistposition);
        } else {
            return null;
        }
    }

    public GraphicViewerObject[] toArray() {
        int i = myObjects.getNumObjects();
        GraphicViewerObject agraphicviewerobject[] = new GraphicViewerObject[i];
        int j = 0;
        for (GraphicViewerListPosition graphicviewerlistposition = myObjects
                .getFirstObjectPos(); graphicviewerlistposition != null;) {
            GraphicViewerObject graphicviewerobject = myObjects
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = myObjects
                    .getNextObjectPos(graphicviewerlistposition);
            agraphicviewerobject[j++] = graphicviewerobject;
        }

        return agraphicviewerobject;
    }

    public void addArray(GraphicViewerObject agraphicviewerobject[]) {
        for (int i = 0; i < agraphicviewerobject.length; i++) {
            myObjects.addObjectAtTail(agraphicviewerobject[i]);
        }
    }

    public void addCollection(ArrayList arraylist) {
        for (int i = 0; i < arraylist.size(); i++) {
            myObjects.addObjectAtTail((GraphicViewerObject) arraylist.get(i));
        }
    }

    public void addCollection(
            IGraphicViewerObjectSimpleCollection graphicviewerobjectsimplecollection) {
        for (GraphicViewerListPosition graphicviewerlistposition = graphicviewerobjectsimplecollection
                .getFirstObjectPos(); graphicviewerlistposition != null;) {
            GraphicViewerObject graphicviewerobject = graphicviewerobjectsimplecollection
                    .getObjectAtPos(graphicviewerlistposition);
            graphicviewerlistposition = graphicviewerobjectsimplecollection
                    .getNextObjectPos(graphicviewerlistposition);
            myObjects.addObjectAtTail(graphicviewerobject);
        }
    }
}