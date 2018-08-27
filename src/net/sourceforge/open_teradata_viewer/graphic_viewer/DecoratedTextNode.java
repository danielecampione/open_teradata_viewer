/*
 * Open Teradata Viewer ( graphic viewer )
 * Copyright (C) 2015, D. Campione
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

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class DecoratedTextNode extends GraphicViewerTextNode {

    private static final long serialVersionUID = -7979415432735633257L;

    private GraphicViewerImage myDecoration;
    private String myImgName = "star.gif";

    public DecoratedTextNode() {
    }

    public DecoratedTextNode(String s) {
        super(s);
        setTopPort(null);
        GraphicViewerImage img = new GraphicViewerImage();
        img.setSelectable(false);
        img.loadImage(GraphicViewer.class.getResource(myImgName), true);
        img.setSize(32, 32);
        insertObjectAfter(findObject(getBackground()), img);
        myDecoration = img;
        layoutChildren(getDecoration());
    }

    public GraphicViewerObject copyObject(IGraphicViewerCopyEnvironment env) {
        DecoratedTextNode newobj = (DecoratedTextNode) super.copyObject(env);
        newobj.myDecoration = (GraphicViewerImage) env.get(myDecoration);
        return newobj;
    }

    public GraphicViewerObject removeObjectAtPos(GraphicViewerListPosition pos) {
        GraphicViewerObject obj = super.removeObjectAtPos(pos);
        if (obj == myDecoration) {
            myDecoration = null;
        }
        return obj;
    }

    public void layoutChildren(GraphicViewerObject child) {
        super.layoutChildren(child);
        if (!isInitializing() && getDecoration() != null
                && getBackground() != null) {
            getDecoration().setSpotLocation(BottomCenter, getBackground(),
                    TopCenter);
        }
    }

    public boolean doMouseDblClick(int modifiers, Point dc, Point vc,
            GraphicViewerView view) {
        if (getDecoration() != null) {
            if (myImgName.equals("star.gif")) {
                myImgName = "doc.gif";
            } else {
                myImgName = "star.gif";
            }
            getDecoration().loadImage(
                    GraphicViewer.class.getResource(myImgName), true);
        }
        return true;
    }

    public GraphicViewerImage getDecoration() {
        return myDecoration;
    }
}