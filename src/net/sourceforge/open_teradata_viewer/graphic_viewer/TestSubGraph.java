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

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class TestSubGraph extends GraphicViewerSubGraph {

    private static final long serialVersionUID = 4183215077598003075L;

    public GraphicViewerPort getInput() {
        return myInput;
    }
    public GraphicViewerPort getOutput() {
        return myOutput;
    }

    private GraphicViewerPort myInput = null;
    private GraphicViewerPort myOutput = null;

    /** Default constructor, just for copying. */
    public TestSubGraph() {
    }

    public TestSubGraph(String s) {
        super(s);
        init();
    }

    /** Create an input port and an output port, each a green triangle. */
    public void init() {
        setInitializing(true);
        setLabelSpot(GraphicViewerObject.TopLeft);
        setCollapsedLabelSpot(GraphicViewerObject.BottomCenter);
        setInsets(new Insets(10, 10, 10, 10));
        setCollapsedInsets(new Insets(10, 10, 10, 10));

        myInput = new GraphicViewerPort();
        myInput.setSize(7, 7);
        myInput.setBrush(GraphicViewerBrush.green);
        myInput.setStyle(GraphicViewerPort.StyleTriangle);
        myInput.setValidSource(false);
        addObjectAtTail(myInput);

        myOutput = new GraphicViewerPort();
        myOutput.setSize(7, 7);
        myOutput.setBrush(GraphicViewerBrush.green);
        myOutput.setStyle(GraphicViewerPort.StyleTriangle);
        myOutput.setValidDestination(false);
        addObjectAtTail(myOutput);

        setInitializing(false);
        layoutChildren(null);
    }

    protected void copyChildren(GraphicViewerArea newarea,
            IGraphicViewerCopyEnvironment env) {
        super.copyChildren(newarea, env);
        TestSubGraph newobj = (TestSubGraph) newarea;
        newobj.myInput = (GraphicViewerPort) env.get(myInput);
        newobj.myOutput = (GraphicViewerPort) env.get(myOutput);
    }

    public GraphicViewerObject removeObjectAtPos(GraphicViewerListPosition pos) {
        GraphicViewerObject child = super.removeObjectAtPos(pos);
        if (child == myInput) {
            myInput = null;
        } else if (child == myOutput) {
            myOutput = null;
        }
        return child;
    }

    /**
     * The input port is positioned inside the center left point of the
     * subgraph; the output port is position inside the center right spot.
     */
    public void layoutPort() {
        Rectangle b = computeBorder();
        if (getInput() != null) {
            getInput().setSpotLocation(CenterLeft,
                    new Point(b.x, b.y + b.height / 2));
        }
        if (getOutput() != null) {
            getOutput().setSpotLocation(CenterRight,
                    new Point(b.x + b.width, b.y + b.height / 2));
        }
    }
}