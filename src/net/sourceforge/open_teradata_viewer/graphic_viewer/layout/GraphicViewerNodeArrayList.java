/*
 * Open Teradata Viewer ( graphic viewer layout )
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

package net.sourceforge.open_teradata_viewer.graphic_viewer.layout;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class GraphicViewerNodeArrayList {

    private GraphicViewerTreeNetworkNode[] myData;
    private int myDataCount;

    public int getCount() {
        return myDataCount;
    }

    public boolean Contains(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
        if (myData == null) {
            return false;
        }
        for (int i = 0; i < myData.length; i++) {
            if (myData[i] == paramGraphicViewerTreeNetworkNode) {
                return true;
            }
        }
        return false;
    }

    public int IndexOf(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
        for (int i = 0; i < myData.length; i++) {
            if (myData[i] == paramGraphicViewerTreeNetworkNode) {
                return i;
            }
        }
        return -1;
    }

    public GraphicViewerTreeNetworkNode get(int paramInt) {
        if (myData == null) {
            return null;
        }
        return myData[paramInt];
    }

    public int Add(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
        int i = this.myDataCount;
        myDataCount += 1;
        if (myData == null) {
            myData = new GraphicViewerTreeNetworkNode[10];
        }
        if (myDataCount > myData.length) {
            GraphicViewerTreeNetworkNode[] arrayOfGraphicViewerTreeNetworkNode = new GraphicViewerTreeNetworkNode[this.myDataCount * 2];
            System.arraycopy(myData, 0, arrayOfGraphicViewerTreeNetworkNode, 0,
                    myData.length);
            myData = arrayOfGraphicViewerTreeNetworkNode;
        }
        myData[i] = paramGraphicViewerTreeNetworkNode;
        return i;
    }

    public void RemoveAt(int paramInt) {
        if ((paramInt < 0) || (paramInt >= myDataCount) || (myData == null)) {
            return;
        }
        if (myDataCount > paramInt + 1) {
            System.arraycopy(myData, paramInt + 1, myData, paramInt,
                    myDataCount - paramInt - 1);
        }
        myDataCount -= 1;
        myData[myDataCount] = null;
    }

    public GraphicViewerTreeNetworkNode[] ToArray() {
        GraphicViewerTreeNetworkNode[] arrayOfGraphicViewerTreeNetworkNode = new GraphicViewerTreeNetworkNode[myDataCount];
        if (myData == null) {
            return arrayOfGraphicViewerTreeNetworkNode;
        }
        System.arraycopy(myData, 0, arrayOfGraphicViewerTreeNetworkNode, 0,
                myDataCount);
        return arrayOfGraphicViewerTreeNetworkNode;
    }
}