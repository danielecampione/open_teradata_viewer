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

    public GraphicViewerHandle() {
        dN = 0;
        dO = 0;
        dL = null;
        a(0, 0);
    }

    public GraphicViewerHandle(Rectangle rectangle, int i) {
        super(rectangle);
        dN = 0;
        dO = 0;
        dL = null;
        a(i, 0);
    }

    public GraphicViewerHandle(Rectangle rectangle, int i, int j) {
        super(rectangle);
        dN = 0;
        dO = 0;
        dL = null;
        a(i, j);
    }

    private final void a(int i, int j) {
        _mthfor(g() & 0xffffffeb);
        dN = i;
        dO = j;
        setBrush(GraphicViewerBrush.black);
    }

    public GraphicViewerObject copyObject(
            GraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        return null;
    }

    public boolean doUncapturedMouseMove(int i, Point point, Point point1,
            GraphicViewerView graphicviewerview) {
        GraphicViewerObject graphicviewerobject = getHandleFor();
        if (graphicviewerobject != null
                && graphicviewerobject.getLayer() != null
                && !graphicviewerobject.getLayer().isModifiable())
            return false;
        if (getCursorType() != 0) {
            graphicviewerview.a(getCursorType());
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
        return dL;
    }

    public void setPartner(GraphicViewerObject graphicviewerobject) {
        dL = graphicviewerobject;
    }

    public final void setHandleFor(GraphicViewerObject graphicviewerobject) {
        setPartner(graphicviewerobject);
    }

    public final GraphicViewerObject getHandleFor() {
        return getPartner();
    }

    public void setHandleType(int i) {
        dO = i;
        int j = getCursorType();
        int k = j;
        switch (dO) {
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
        if (k != j)
            setCursorType(k);
    }

    public int getHandleType() {
        return dO;
    }

    public void setCursorType(int i) {
        dN = i;
    }

    public int getCursorType() {
        return dN;
    }

    public static int getDefaultHandleWidth() {
        return dM;
    }

    public static void setDefaultHandleWidth(int i) {
        dM = i;
    }

    public static int getDefaultHandleHeight() {
        return dP;
    }

    public static void setDefaultHandleHeight(int i) {
        dP = i;
    }

    private static int dM = 5;
    private static int dP = 5;
    private int dN;
    private int dO;
    private GraphicViewerObject dL;

}