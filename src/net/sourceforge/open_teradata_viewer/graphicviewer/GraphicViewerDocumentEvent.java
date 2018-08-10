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

import java.awt.Rectangle;
import java.util.EventObject;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerDocumentEvent extends EventObject {

    private static final long serialVersionUID = -4184535562126698115L;

    public GraphicViewerDocumentEvent(
            GraphicViewerDocument graphicviewerdocument, int i, int j,
            Object obj) {
        super(graphicviewerdocument);
        _fldif = null;
        _fldint = i;
        _fldnew = j;
        a = obj;
        _fldtry = null;
        _flddo = 0;
    }

    public GraphicViewerDocumentEvent(
            GraphicViewerDocument graphicviewerdocument, int i, int j,
            Object obj, int k, Object obj1) {
        super(graphicviewerdocument);
        _fldif = null;
        _fldint = i;
        _fldnew = j;
        a = obj;
        _fldtry = obj1;
        _flddo = k;
    }

    public int getHint() {
        return _fldint & 0xffff7fff;
    }

    void _mthif(int i) {
        _fldint = i;
    }

    public int getFlags() {
        return _fldnew;
    }

    void a(int i) {
        _fldnew = i;
    }

    public GraphicViewerObject getGraphicViewerObject() {
        if (a instanceof GraphicViewerObject)
            return (GraphicViewerObject) a;
        else
            return null;
    }

    public Object getObject() {
        return a;
    }

    void a(Object obj) {
        a = obj;
    }

    public Object getPreviousValue() {
        return _fldtry;
    }

    public int getPreviousValueInt() {
        return _flddo;
    }

    public boolean isBeforeChanging() {
        return (_fldint & 0x8000) != 0;
    }

    void _mthif(Object obj) {
        _fldtry = obj;
    }

    void _mthdo(int i) {
        _flddo = i;
    }

    Rectangle a() {
        if (_fldif == null)
            _fldif = new Rectangle(0, 0, 0, 0);
        return _fldif;
    }

    public static final int UPDATE_ALL = 100;
    public static final int STARTED_TRANSACTION = 104;
    public static final int FINISHED_TRANSACTION = 105;
    public static final int ABORTED_TRANSACTION = 106;
    public static final int STARTING_UNDO = 107;
    public static final int FINISHED_UNDO = 108;
    public static final int STARTING_REDO = 109;
    public static final int FINISHED_REDO = 110;
    static final int _fldfor = 200;
    public static final int INSERTED = 202;
    public static final int CHANGED = 203;
    public static final int REMOVED = 204;
    public static final int SIZE_CHANGED = 205;
    public static final int PAPER_COLOR_CHANGED = 206;
    public static final int MODIFIABLE_CHANGED = 208;
    public static final int TOPLEFT_CHANGED = 209;
    public static final int LAYER_INSERTED = 210;
    public static final int LAYER_REMOVED = 211;
    public static final int LAYER_MOVED = 212;
    public static final int LAYER_VISIBLE_CHANGED = 213;
    public static final int LAYER_MODIFIABLE_CHANGED = 214;
    public static final int LAYER_TRANSPARENCY_CHANGED = 215;
    public static final int LAYER_IDENTIFIER_CHANGED = 217;
    public static final int ARRANGED = 218;
    public static final int MAINTAINS_PARTID_CHANGED = 219;
    public static final int VALID_CYCLE_CHANGED = 220;
    public static final int BEFORE_CHANGING = 32768;
    public static final int LAST = 65535;
    private int _fldint;
    private int _fldnew;
    private Object a;
    private Object _fldtry;
    private int _flddo;
    private transient Rectangle _fldif;
}