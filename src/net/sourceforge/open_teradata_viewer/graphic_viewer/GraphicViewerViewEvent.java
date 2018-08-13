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
import java.util.EventObject;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerViewEvent extends EventObject {

    private static final long serialVersionUID = 5363609477078330074L;

    public static final int UPDATE_ALL = 1;
    public static final int INSERTED = 2;
    public static final int CHANGED = 3;
    public static final int REMOVED = 4;
    public static final int SELECTION_GAINED = 20;
    public static final int SELECTION_LOST = 21;
    public static final int CLICKED = 22;
    public static final int DOUBLE_CLICKED = 23;
    public static final int BACKGROUND_CLICKED = 24;
    public static final int BACKGROUND_DOUBLE_CLICKED = 25;
    public static final int SELECTION_MOVED = 26;
    public static final int SELECTION_COPIED = 27;
    public static final int SELECTION_DELETING = 28;
    public static final int SELECTION_DELETED = 29;
    public static final int OBJECT_RESIZED = 30;
    public static final int LINK_CREATED = 31;
    public static final int LINK_RELINKED = 32;
    public static final int OBJECT_EDITED = 33;
    public static final int CLIPBOARD_PASTED = 34;
    public static final int EXTERNAL_OBJECTS_DROPPED = 35;
    public static final int CLIPBOARD_COPIED = 36;
    public static final int SELECTION_STARTING = 37;
    public static final int SELECTION_FINISHED = 38;
    public static final int POSITION_CHANGED = 107;
    public static final int SCALE_CHANGED = 108;
    public static final int SELECTION_COLOR_CHANGED = 109;
    public static final int BACKGROUND_IMAGE_CHANGED = 112;
    public static final int HIDING_DISABLED_SCROLLBARS_CHANGED = 113;
    public static final int INCLUDING_NEGATIVE_COORDS_CHANGED = 114;
    public static final int KEY_ENABLED_CHANGED = 115;
    public static final int MOUSE_ENABLED_CHANGED = 116;
    public static final int DRAG_ENABLED_CHANGED = 117;
    public static final int DROP_ENABLED_CHANGED = 118;
    public static final int DEFAULT_CURSOR_CHANGED = 119;
    public static final int DRAGS_REALTIME_CHANGED = 120;
    public static final int INTERNAL_MOUSE_ACTIONS_CHANGED = 121;
    public static final int DRAGS_SELECTION_IMAGE_CHANGED = 122;
    public static final int AUTOSCROLL_INSETS_CHANGED = 123;
    public static final int GRID_CHANGED = 124;
    public static final int LAST = 65535;
    private int myHint;
    private int myFlags;
    private Object myObj;
    private Point myVC;
    private Point myDC;
    private int myModifiers;
    private boolean myConsumed;

    public GraphicViewerViewEvent(GraphicViewerView paramGraphicViewerView,
            int paramInt1, int paramInt2, Object paramObject,
            Point paramPoint1, Point paramPoint2, int paramInt3) {
        super(paramGraphicViewerView);
        myObj = paramObject;
        myHint = paramInt1;
        myFlags = paramInt2;
        myVC = paramPoint1;
        myDC = paramPoint2;
        myModifiers = paramInt3;
    }

    public int getHint() {
        return myHint;
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

    public boolean isConsumed() {
        return myConsumed;
    }

    void setConsumed(boolean flag) {
        myConsumed = flag;
    }

    public void consume() {
        setConsumed(true);
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

    public Point getPointViewCoords() {
        return myVC;
    }

    void setPointViewCoords(Point point) {
        myVC = point;
    }

    public Point getPointDocCoords() {
        return myDC;
    }

    void setPointDocCoords(Point point) {
        myDC = point;
    }

    public int getModifiers() {
        return myModifiers;
    }

    void setModifiers(int i) {
        myModifiers = i;
    }
}