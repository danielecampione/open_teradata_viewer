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

import java.awt.Point;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class DecoratedTextNode extends GraphicViewerTextNode {

    private static final long serialVersionUID = -7979415432735633257L;

    public DecoratedTextNode() {
        myImgName = "icons/star.gif";
    }

    public DecoratedTextNode(String s) {
        super(s);
        myImgName = "icons/star.gif";
        setTopPort(null);
        GraphicViewerImage graphicviewerimage = new GraphicViewerImage();
        graphicviewerimage.setSelectable(false);
        graphicviewerimage.loadImage(
                (DecoratedTextNode.class).getResource(myImgName), true);
        graphicviewerimage.setSize(32, 32);
        insertObjectAfter(findObject(getBackground()), graphicviewerimage);
        myDecoration = graphicviewerimage;
        layoutChildren(getDecoration());
    }

    public GraphicViewerObject copyObject(
            GraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        DecoratedTextNode decoratedtextnode = (DecoratedTextNode) super
                .copyObject(graphicviewercopyenvironment);
        decoratedtextnode.myDecoration = (GraphicViewerImage) graphicviewercopyenvironment
                .get(myDecoration);
        return decoratedtextnode;
    }

    public GraphicViewerObject removeObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        GraphicViewerObject graphicviewerobject = super
                .removeObjectAtPos(graphicviewerlistposition);
        if (graphicviewerobject == myDecoration)
            myDecoration = null;
        return graphicviewerobject;
    }

    public void layoutChildren(GraphicViewerObject graphicviewerobject) {
        super.layoutChildren(graphicviewerobject);
        if (!isInitializing() && getDecoration() != null
                && getBackground() != null)
            getDecoration().setSpotLocation(6, getBackground(), 2);
    }

    public boolean doMouseDblClick(int i, Point point, Point point1,
            GraphicViewerView graphicviewerview) {
        if (getDecoration() != null) {
            if (myImgName.equals("icons/star.gif"))
                myImgName = "icons/doc.gif";
            else
                myImgName = "icons/star.gif";
            getDecoration().loadImage(
                    (DecoratedTextNode.class).getResource(myImgName), true);
        }
        return true;
    }

    public GraphicViewerImage getDecoration() {
        return myDecoration;
    }

    private GraphicViewerImage myDecoration;
    private String myImgName;
}