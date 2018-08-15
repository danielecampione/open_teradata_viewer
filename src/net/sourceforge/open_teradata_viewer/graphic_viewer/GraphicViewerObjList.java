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

import java.io.Serializable;

/**
 * 
 * 
 * @author D. Campione
 *
 */
class GraphicViewerObjList implements Serializable {

    private static final long serialVersionUID = 872776455730424153L;

    private GraphicViewerListPosition myHead = null;
    private GraphicViewerListPosition myTail = null;
    private int myNumObjects = 0;
    private boolean myOwner = false;

    public GraphicViewerObjList() {
    }

    public GraphicViewerObjList(boolean flag) {
        myOwner = flag;
    }

    public int getNumObjects() {
        return myNumObjects;
    }

    public boolean isEmpty() {
        return myHead == null;
    }

    public GraphicViewerListPosition addObjectAtHead(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null) {
            return null;
        }
        GraphicViewerListPosition graphicviewerlistposition = new GraphicViewerListPosition(
                graphicviewerobject, null, myHead);
        if (myHead != null) {
            myHead.prev = graphicviewerlistposition;
        }
        myHead = graphicviewerlistposition;
        if (myTail == null) {
            myTail = myHead;
        }
        myNumObjects++;
        if (myOwner) {
            graphicviewerobject.setCurrentListPosition(myHead);
        }
        return myHead;
    }

    public GraphicViewerListPosition addObjectAtTail(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null) {
            return null;
        }
        GraphicViewerListPosition graphicviewerlistposition = new GraphicViewerListPosition(
                graphicviewerobject, myTail, null);
        if (myTail != null) {
            myTail.next = graphicviewerlistposition;
        }
        myTail = graphicviewerlistposition;
        if (myHead == null) {
            myHead = myTail;
        }
        myNumObjects++;
        if (myOwner) {
            graphicviewerobject.setCurrentListPosition(myTail);
        }
        return myTail;
    }

    public GraphicViewerListPosition insertObjectBefore(
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerlistposition == null || graphicviewerobject == null) {
            return null;
        }
        GraphicViewerListPosition graphicviewerlistposition1 = new GraphicViewerListPosition(
                graphicviewerobject, graphicviewerlistposition.prev,
                graphicviewerlistposition);
        if (graphicviewerlistposition.prev != null) {
            graphicviewerlistposition.prev.next = graphicviewerlistposition1;
        } else {
            myHead = graphicviewerlistposition1;
        }
        graphicviewerlistposition.prev = graphicviewerlistposition1;
        myNumObjects++;
        if (myOwner) {
            graphicviewerobject
                    .setCurrentListPosition(graphicviewerlistposition1);
        }
        return graphicviewerlistposition1;
    }

    public GraphicViewerListPosition insertObjectAfter(
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerlistposition == null || graphicviewerobject == null) {
            return null;
        }
        GraphicViewerListPosition graphicviewerlistposition1 = new GraphicViewerListPosition(
                graphicviewerobject, graphicviewerlistposition,
                graphicviewerlistposition.next);
        if (graphicviewerlistposition.next != null) {
            graphicviewerlistposition.next.prev = graphicviewerlistposition1;
        } else {
            myTail = graphicviewerlistposition1;
        }
        graphicviewerlistposition.next = graphicviewerlistposition1;
        myNumObjects++;
        if (myOwner) {
            graphicviewerobject
                    .setCurrentListPosition(graphicviewerlistposition1);
        }
        return graphicviewerlistposition1;
    }

    public void removeObject(GraphicViewerObject graphicviewerobject) {
        GraphicViewerListPosition graphicviewerlistposition = findObject(graphicviewerobject);
        removeObjectAtPos(graphicviewerlistposition);
    }

    public GraphicViewerObject removeObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        if (graphicviewerlistposition == null) {
            return null;
        }
        GraphicViewerObject graphicviewerobject = graphicviewerlistposition.obj;
        if (graphicviewerlistposition == myHead) {
            myHead = graphicviewerlistposition.next;
        }
        if (graphicviewerlistposition == myTail) {
            myTail = graphicviewerlistposition.prev;
        }
        if (graphicviewerlistposition.prev != null) {
            graphicviewerlistposition.prev.next = graphicviewerlistposition.next;
        }
        if (graphicviewerlistposition.next != null) {
            graphicviewerlistposition.next.prev = graphicviewerlistposition.prev;
        }
        graphicviewerlistposition.prev = null;
        graphicviewerlistposition.next = null;
        if (myOwner) {
            graphicviewerobject.setCurrentListPosition(null);
        }
        myNumObjects--;
        return graphicviewerobject;
    }

    public GraphicViewerListPosition getFirstObjectPos() {
        return myHead;
    }

    public GraphicViewerListPosition getLastObjectPos() {
        return myTail;
    }

    public GraphicViewerListPosition getNextObjectPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        if (graphicviewerlistposition == null) {
            return null;
        } else {
            return graphicviewerlistposition.next;
        }
    }

    public GraphicViewerListPosition getPrevObjectPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        if (graphicviewerlistposition == null) {
            return null;
        } else {
            return graphicviewerlistposition.prev;
        }
    }

    public GraphicViewerObject getObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        if (graphicviewerlistposition == null) {
            return null;
        } else {
            return graphicviewerlistposition.obj;
        }
    }

    public GraphicViewerListPosition findObject(
            GraphicViewerObject graphicviewerobject) {
        if (myOwner) {
            GraphicViewerListPosition graphicviewerlistposition = graphicviewerobject
                    .getCurrentListPosition();
            if (graphicviewerlistposition != null) {
                return graphicviewerlistposition;
            }
        }
        for (GraphicViewerListPosition graphicviewerlistposition1 = myHead; graphicviewerlistposition1 != null; graphicviewerlistposition1 = graphicviewerlistposition1.next) {
            if (graphicviewerlistposition1.obj == graphicviewerobject) {
                return graphicviewerlistposition1;
            }
        }

        return null;
    }
}