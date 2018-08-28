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

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerCopyMap
        implements
            IGraphicViewerCopyEnvironment,
            Serializable {

    private static final long serialVersionUID = -9041495972527243790L;

    private HashMap myMap = new HashMap();
    private Vector myDelayeds = new Vector();

    public void clear() {
        myMap.clear();
    }

    public boolean containsKey(Object obj) {
        if (obj == null) {
            return false;
        } else {
            return myMap.containsKey(obj);
        }
    }

    public boolean containsValue(Object obj) {
        return myMap.containsValue(obj);
    }

    public Set entrySet() {
        return myMap.entrySet();
    }

    public boolean equals(Object obj) {
        if (obj instanceof GraphicViewerCopyMap) {
            GraphicViewerCopyMap graphicviewercopymap = (GraphicViewerCopyMap) obj;
            return myMap.equals(graphicviewercopymap.myMap)
                    && myDelayeds.equals(graphicviewercopymap.myDelayeds);
        } else {
            return false;
        }
    }

    public Object get(Object obj) {
        if (obj == null) {
            return null;
        } else {
            return myMap.get(obj);
        }
    }

    public int hashCode() {
        return myMap.hashCode();
    }

    public boolean isEmpty() {
        return myMap.isEmpty();
    }

    public Set keySet() {
        return myMap.keySet();
    }

    public Object put(Object obj, Object obj1) {
        if (obj == null) {
            return null;
        } else {
            return myMap.put(obj, obj1);
        }
    }

    public void putAll(Map map) {
        myMap.putAll(map);
    }

    public Object remove(Object obj) {
        if (obj == null) {
            return null;
        } else {
            return myMap.remove(obj);
        }
    }

    public int size() {
        return myMap.size();
    }

    public Collection values() {
        return myMap.values();
    }

    public GraphicViewerObject copy(GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null) {
            return null;
        }
        GraphicViewerObject graphicviewerobject1 = (GraphicViewerObject) myMap
                .get(graphicviewerobject);
        if (graphicviewerobject1 == null) {
            graphicviewerobject1 = graphicviewerobject.copyObject(this);
        }
        return graphicviewerobject1;
    }

    public void finishDelayedCopies() {
        Vector vector = getDelayeds();
        if (vector == null) {
            return;
        }
        for (int i = 0; i < vector.size(); i++) {
            Object obj = vector.get(i);
            if (!(obj instanceof GraphicViewerObject)) {
                continue;
            }
            GraphicViewerObject graphicviewerobject = (GraphicViewerObject) obj;
            GraphicViewerObject graphicviewerobject1 = (GraphicViewerObject) get(graphicviewerobject);
            if (graphicviewerobject1 instanceof GraphicViewerObject) {
                GraphicViewerObject graphicviewerobject2 = (GraphicViewerObject) graphicviewerobject1;
                graphicviewerobject.copyObjectDelayed(this,
                        graphicviewerobject2);
            }
        }

    }

    public GraphicViewerObject copyComplete(
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null) {
            return null;
        } else {
            GraphicViewerObject graphicviewerobject1 = copy(graphicviewerobject);
            finishDelayedCopies();
            return graphicviewerobject1;
        }
    }

    public void clearDelayeds() {
        myDelayeds.clear();
    }

    public boolean isEmptyDelayeds() {
        return myDelayeds.isEmpty();
    }

    public int sizeDelayeds() {
        return myDelayeds.size();
    }

    public boolean isDelayed(Object obj) {
        return myDelayeds.contains(obj);
    }

    public void delay(Object obj) {
        myDelayeds.add(obj);
    }

    public void removeDelayed(Object obj) {
        myDelayeds.remove(obj);
    }

    public Vector getDelayeds() {
        return myDelayeds;
    }
}