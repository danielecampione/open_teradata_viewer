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

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerHandle extends GraphicViewerRectangle {

    private static final long serialVersionUID = 6887336048602576402L;

    private static int myDefaultHandleWidth = 5;
    private static int myDefaultHandleHeight = 5;
    private int myCursorType = 0;
    private int myHandleType = 0;
    private GraphicViewerObject myHandleFor = null;

    public GraphicViewerHandle() {
        init(0, 0);
    }

    public GraphicViewerHandle(Rectangle rectangle, int i) {
        super(rectangle);
        init(i, 0);
    }

    public GraphicViewerHandle(Rectangle rectangle, int i, int j) {
        super(rectangle);
        init(i, j);
    }

    private final void init(int i, int j) {
        setInternalFlags(getInternalFlags() & 0xffffffeb);
        myCursorType = i;
        myHandleType = j;
        setBrush(GraphicViewerBrush.black);
    }

    public GraphicViewerObject copyObject(
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        return null;
    }

    public boolean doUncapturedMouseMove(int i, Point point, Point point1,
            GraphicViewerView graphicviewerview) {
        GraphicViewerObject graphicviewerobject = getHandleFor();
        if (graphicviewerobject != null
                && graphicviewerobject.getLayer() != null
                && !graphicviewerobject.getLayer().isModifiable()) {
            return false;
        }
        if (getCursorType() != 0) {
            graphicviewerview.setCursorType(getCursorType());
            return true;
        } else {
            return false;
        }
    }

    public Cursor getCursor() {
        return Cursor.getPredefinedCursor(getCursorType());
    }

    protected void gainedSelection(GraphicViewerSelection graphicviewerselection) {
    }

    protected void lostSelection(GraphicViewerSelection graphicviewerselection) {
    }

    public GraphicViewerObject redirectSelection() {
        return null;
    }

    public GraphicViewerObject getPartner() {
        return myHandleFor;
    }

    public void setPartner(GraphicViewerObject graphicviewerobject) {
        myHandleFor = graphicviewerobject;
    }

    public final void setHandleFor(GraphicViewerObject graphicviewerobject) {
        setPartner(graphicviewerobject);
    }

    public final GraphicViewerObject getHandleFor() {
        return getPartner();
    }

    public void setHandleType(int i) {
        myHandleType = i;
        int j = getCursorType();
        int k = j;
        switch (myHandleType) {
            case 1 : // '\001'
                k = 6;
                break;

            case 2 : // '\002'
                k = 8;
                break;

            case 3 : // '\003'
                k = 7;
                break;

            case 8 : // '\b'
                k = 10;
                break;

            case 4 : // '\004'
                k = 11;
                break;

            case 7 : // '\007'
                k = 4;
                break;

            case 6 : // '\006'
                k = 9;
                break;

            case 5 : // '\005'
                k = 5;
                break;

            case 91 : // '['
                k = 12;
                break;

            case 92 : // '\\'
                k = 12;
                break;
        }
        if (k != j) {
            setCursorType(k);
        }
    }

    public int getHandleType() {
        return myHandleType;
    }

    public void setCursorType(int i) {
        myCursorType = i;
    }

    public int getCursorType() {
        return myCursorType;
    }

    public static int getDefaultHandleWidth() {
        return myDefaultHandleWidth;
    }

    public static void setDefaultHandleWidth(int i) {
        myDefaultHandleWidth = i;
    }

    public static int getDefaultHandleHeight() {
        return myDefaultHandleHeight;
    }

    public static void setDefaultHandleHeight(int i) {
        myDefaultHandleHeight = i;
    }
}