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

    public TestSubGraph() {
        myInput = null;
        myOutput = null;
    }

    public TestSubGraph(String s) {
        super(s);
        myInput = null;
        myOutput = null;
        init();
    }

    public void init() {
        setInitializing(true);
        setLabelSpot(1);
        setCollapsedLabelSpot(6);
        setInsets(new Insets(10, 10, 10, 10));
        setCollapsedInsets(new Insets(10, 10, 10, 10));
        myInput = new GraphicViewerPort();
        myInput.setSize(7, 7);
        myInput.setBrush(GraphicViewerBrush.green);
        myInput.setStyle(3);
        myInput.setValidSource(false);
        addObjectAtTail(myInput);
        myOutput = new GraphicViewerPort();
        myOutput.setSize(7, 7);
        myOutput.setBrush(GraphicViewerBrush.green);
        myOutput.setStyle(3);
        myOutput.setValidDestination(false);
        addObjectAtTail(myOutput);
        setInitializing(false);
        layoutChildren(null);
    }

    protected void copyChildren(GraphicViewerArea graphicviewerarea,
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        super.copyChildren(graphicviewerarea, graphicviewercopyenvironment);
        TestSubGraph testsubgraph = (TestSubGraph) graphicviewerarea;
        testsubgraph.myInput = (GraphicViewerPort) graphicviewercopyenvironment
                .get(myInput);
        testsubgraph.myOutput = (GraphicViewerPort) graphicviewercopyenvironment
                .get(myOutput);
    }

    public GraphicViewerObject removeObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        GraphicViewerObject graphicviewerobject = super
                .removeObjectAtPos(graphicviewerlistposition);
        if (graphicviewerobject == myInput)
            myInput = null;
        else if (graphicviewerobject == myOutput)
            myOutput = null;
        return graphicviewerobject;
    }

    public void layoutPort() {
        Rectangle rectangle = computeBorder();
        if (getInput() != null)
            getInput().setSpotLocation(8,
                    new Point(rectangle.x, rectangle.y + rectangle.height / 2));
        if (getOutput() != null)
            getOutput().setSpotLocation(
                    4,
                    new Point(rectangle.x + rectangle.width, rectangle.y
                            + rectangle.height / 2));
    }

    public GraphicViewerPort getInput() {
        return myInput;
    }

    public GraphicViewerPort getOutput() {
        return myOutput;
    }

    private GraphicViewerPort myInput;
    private GraphicViewerPort myOutput;
}