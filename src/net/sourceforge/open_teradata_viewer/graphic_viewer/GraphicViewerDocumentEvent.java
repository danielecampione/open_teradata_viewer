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

    public static final int UPDATE_ALL = 100;
    public static final int STARTED_TRANSACTION = 104;
    public static final int FINISHED_TRANSACTION = 105;
    public static final int ABORTED_TRANSACTION = 106;
    public static final int STARTING_UNDO = 107;
    public static final int FINISHED_UNDO = 108;
    public static final int STARTING_REDO = 109;
    public static final int FINISHED_REDO = 110;
    static final int FIRST_STATE_CHANGED_HINT = 200;
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
    private int myHint;
    private int myFlags;
    private Object myObj;
    private Object myPreviousValue;
    private int myPreviousValueInt;
    private transient Rectangle myTempRectangle = null;

    public GraphicViewerDocumentEvent(
            GraphicViewerDocument graphicviewerdocument, int i, int j,
            Object obj) {
        super(graphicviewerdocument);
        myHint = i;
        myFlags = j;
        myObj = obj;
        myPreviousValue = null;
        myPreviousValueInt = 0;
    }

    public GraphicViewerDocumentEvent(
            GraphicViewerDocument graphicviewerdocument, int i, int j,
            Object obj, int k, Object obj1) {
        super(graphicviewerdocument);
        myHint = i;
        myFlags = j;
        myObj = obj;
        myPreviousValue = obj1;
        myPreviousValueInt = k;
    }

    public int getHint() {
        return myHint & 0xffff7fff;
    }

    void setHint(int i) {
        myHint = i;
    }

    public int getFlags() {
        return myFlags;
    }

    void setFlags(int i) {
        myFlags = i;
    }

    public GraphicViewerObject getGraphicViewerObject() {
        if (myObj instanceof GraphicViewerObject) {
            return (GraphicViewerObject) myObj;
        } else {
            return null;
        }
    }

    public Object getObject() {
        return myObj;
    }

    void setObject(Object obj) {
        myObj = obj;
    }

    public Object getPreviousValue() {
        return myPreviousValue;
    }

    public int getPreviousValueInt() {
        return myPreviousValueInt;
    }

    public boolean isBeforeChanging() {
        return (myHint & 0x8000) != 0;
    }

    void setPreviousValue(Object obj) {
        myPreviousValue = obj;
    }

    void setPreviousValueInt(int i) {
        myPreviousValueInt = i;
    }

    Rectangle getTempRectangle() {
        if (myTempRectangle == null) {
            myTempRectangle = new Rectangle(0, 0, 0, 0);
        }
        return myTempRectangle;
    }
}