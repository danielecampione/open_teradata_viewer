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

import java.awt.Color;
import java.awt.Point;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class BasicLayoutNode extends GraphicViewerNode {

    private static final long serialVersionUID = 6588473479213385101L;

    static int myStdSize = 40;

    // Get access to the parts of the node
    public GraphicViewerObject getEllipse() {
        return myEllipse;
    }
    public GraphicViewerText getLabel() {
        return myLabel;
    }
    public BasicLayoutPort[] getPorts() {
        return myPorts;
    }

    // State
    protected GraphicViewerEllipse myEllipse = null;
    protected GraphicViewerText myLabel = null;
    protected BasicLayoutPort[] myPorts = null;
    protected int numPorts = 0;
    protected boolean horizontal = true;

    /**
     * A newly constructed BasicLayoutNode is not usable until you've called
     * initialize().
     */
    public BasicLayoutNode() {
        super();
    }

    protected void copyChildren(GraphicViewerArea newarea,
            IGraphicViewerCopyEnvironment env) {
        BasicLayoutNode newobj = (BasicLayoutNode) newarea;

        newobj.numPorts = numPorts;
        newobj.myPorts = new BasicLayoutPort[numPorts];

        super.copyChildren(newarea, env);

        newobj.myEllipse = (GraphicViewerEllipse) env.get(myEllipse);
        newobj.myLabel = (GraphicViewerText) env.get(myLabel);

        Point c = newobj.myEllipse.getSpotLocation(Center);
        for (int i = 0; i < numPorts; i++) {
            BasicLayoutPort op = myPorts[i];
            BasicLayoutPort np = (BasicLayoutPort) env.get(op);
            newobj.myPorts[i] = np;
            np.myEllipse = newobj.myEllipse;
        }
    }

    /**
     * Keep the parts of a BasicLayoutNode positioned relative to each other by
     * setting their locations using some of the standard spots of a
     * GraphicViewerObject.
     * <p>
     * By default the label will be positioned at the bottom of the node, above
     * the ellipse. To change this to be below the ellipse, at the bottom of the
     * node, change the myLabel.setSpotLocation() call.
     */
    public void layoutChildren() {
        if (myEllipse == null) {
            return;
        }
        if (myLabel != null) {
            // Put the label above the node
            myLabel.setSpotLocation(TopCenter, myEllipse, BottomCenter);
            // Put the label below the node
            //myLabel.setSpotLocation(TopCenter, myEllipse, BottomCenter);
        }
        if (myPorts != null) {
            Point c = myEllipse.getSpotLocation(Center);
            for (int i = 0; i < numPorts; i++) {
                if (horizontal) {
                    myPorts[i].setSpotLocation(Center, c.x - (getStdSize() / 4)
                            * (numPorts - 1) + (getStdSize() / 2) * i, c.y);
                } else {
                    myPorts[i].setSpotLocation(Center, c.x, c.y
                            - (getStdSize() / 4) * (numPorts - 1)
                            + (getStdSize() / 2) * i);
                }
            }
        }
    }

    /**
     * Specify the location of the node; the size is constant.
     * If labeltext is null, do not create a label.
     */
    public void initialize(Point loc, String labeltext, int ports, boolean horiz) {
        horizontal = horiz;
        numPorts = ports;
        if (numPorts <= 0) {
            numPorts = 1;
        }
        if (horizontal) {
            setSize((myStdSize / 2) * (numPorts + 1), myStdSize);
        } else {
            setSize(myStdSize, (myStdSize / 2) * (numPorts + 1));
        }

        // The user can move this node around
        setDraggable(true);
        // The user cannot resize this node
        setResizable(false);

        // Create the bigger circle/ellipse around and behind the port
        myEllipse = new GraphicViewerEllipse(getTopLeft(), getSize());
        myEllipse.setSelectable(false);
        myEllipse.setDraggable(false);

        // Can't setLocation until myEllipse exists
        myEllipse.setSpotLocation(GraphicViewerObject.Center, loc);

        // If there is a string, create a label with a transparent background
        // that is centered
        if (labeltext != null) {
            myLabel = new GraphicViewerText(labeltext);
            myLabel.setSelectable(false);
            myLabel.setDraggable(false);
            myLabel.setAlignment(GraphicViewerText.ALIGN_CENTER);
            myLabel.setTransparent(true);
        }

        // Create a Port, which knows how to make sure connected
        // GraphicViewerLinks have a reasonable end point
        myPorts = new BasicLayoutPort[numPorts];
        for (int i = 0; i < numPorts; i++) {
            myPorts[i] = new BasicLayoutPort();
            myPorts[i].myEllipse = myEllipse;
            myPorts[i].setSize(16, 16);
        }

        // Add all the children to the area
        addObjectAtHead(myEllipse);
        if (myLabel != null) {
            addObjectAtTail(myLabel);
        }
        for (int i = 0; i < numPorts; i++) {
            addObjectAtTail(myPorts[i]);
        }

        // Now position the label and port appropriately relative to the ellipse
        layoutChildren();
        setBrush(GraphicViewerBrush.white);
        setColor(Color.red);
    }

    // Convenience methods: control the ellipse's pen and brush.
    public GraphicViewerPen getPen() {
        return myEllipse.getPen();
    }

    public void setPen(GraphicViewerPen p) {
        myEllipse.setPen(p);
    }

    public GraphicViewerBrush getBrush() {
        return myEllipse.getBrush();
    }

    public void setBrush(GraphicViewerBrush b) {
        myEllipse.setBrush(b);
    }

    public Color getColor() {
        return getPen().getColor();
    }

    public void setColor(Color c) {
        setPen(GraphicViewerPen.makeStockPen(c));
    }

    public void colorChange() {
        Color c = getColor();
        if (c == Color.red) {
            setColor(Color.green);
        } else if (c == Color.green) {
            setColor(Color.blue);
        } else if (c == Color.blue) {
            setColor(Color.red);
        }
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public int getNumPorts() {
        return numPorts;
    }

    static int getStdSize() {
        return myStdSize;
    }

    static void setStdSize(int size) {
        myStdSize = size;
    }
}