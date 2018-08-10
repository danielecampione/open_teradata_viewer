/*
 * Open Teradata Viewer ( graphic viewer )
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

package net.sourceforge.open_teradata_viewer.graphicviewer;

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
            GraphicViewerCopyEnvironment,
            Serializable {

    private static final long serialVersionUID = -9041495972527243790L;

    @SuppressWarnings("rawtypes")
    public GraphicViewerCopyMap() {
        _fldif = new HashMap();
        a = new Vector();
    }

    public void clear() {
        _fldif.clear();
    }

    public boolean containsKey(Object obj) {
        if (obj == null)
            return false;
        else
            return _fldif.containsKey(obj);
    }

    public boolean containsValue(Object obj) {
        return _fldif.containsValue(obj);
    }

    @SuppressWarnings("rawtypes")
    public Set entrySet() {
        return _fldif.entrySet();
    }

    public boolean equals(Object obj) {
        if (obj instanceof GraphicViewerCopyMap) {
            GraphicViewerCopyMap graphicviewercopymap = (GraphicViewerCopyMap) obj;
            return _fldif.equals(graphicviewercopymap._fldif)
                    && a.equals(graphicviewercopymap.a);
        } else {
            return false;
        }
    }

    public Object get(Object obj) {
        if (obj == null)
            return null;
        else
            return _fldif.get(obj);
    }

    public int hashCode() {
        return _fldif.hashCode();
    }

    public boolean isEmpty() {
        return _fldif.isEmpty();
    }

    @SuppressWarnings("rawtypes")
    public Set keySet() {
        return _fldif.keySet();
    }

    @SuppressWarnings("unchecked")
    public Object put(Object obj, Object obj1) {
        if (obj == null)
            return null;
        else
            return _fldif.put(obj, obj1);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void putAll(Map map) {
        _fldif.putAll(map);
    }

    public Object remove(Object obj) {
        if (obj == null)
            return null;
        else
            return _fldif.remove(obj);
    }

    public int size() {
        return _fldif.size();
    }

    @SuppressWarnings("rawtypes")
    public Collection values() {
        return _fldif.values();
    }

    public GraphicViewerObject copy(GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject == null)
            return null;
        GraphicViewerObject graphicviewerobject1 = (GraphicViewerObject) _fldif
                .get(graphicviewerobject);
        if (graphicviewerobject1 == null)
            graphicviewerobject1 = graphicviewerobject.copyObject(this);
        return graphicviewerobject1;
    }

    @SuppressWarnings("rawtypes")
    public void finishDelayedCopies() {
        Vector vector = getDelayeds();
        if (vector == null)
            return;
        for (int i = 0; i < vector.size(); i++) {
            Object obj = vector.get(i);
            if (!(obj instanceof GraphicViewerObject))
                continue;
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
        a.clear();
    }

    public boolean isEmptyDelayeds() {
        return a.isEmpty();
    }

    public int sizeDelayeds() {
        return a.size();
    }

    public boolean isDelayed(Object obj) {
        return a.contains(obj);
    }

    @SuppressWarnings("unchecked")
    public void delay(Object obj) {
        a.add(obj);
    }

    public void removeDelayed(Object obj) {
        a.remove(obj);
    }

    @SuppressWarnings("rawtypes")
    public Vector getDelayeds() {
        return a;
    }

    @SuppressWarnings("rawtypes")
    private HashMap _fldif;
    @SuppressWarnings("rawtypes")
    private Vector a;
}