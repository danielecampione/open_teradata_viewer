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

import java.awt.Graphics2D;
import java.awt.Insets;
import java.util.ArrayList;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class MultiTextNode extends GraphicViewerNode {

    private static final long serialVersionUID = -2845126676363781592L;

    public MultiTextNode() {
        myVector = new ArrayList<Object>();
        myLeftPorts = new ArrayList<Object>();
        myRightPorts = new ArrayList<Object>();
        myTopPort = null;
        myBottomPort = null;
        myBack = null;
        myInsets = new Insets(1, 1, 1, 1);
        mySpacing = 0;
        myLinePen = null;
        myItemWidth = 150;
    }

    public void initialize() {
        setInitializing(true);
        setResizable(false);
        myBack = createBackground();
        addObjectAtHead(myBack);
        myTopPort = createEndPort(true);
        addObjectAtTail(myTopPort);
        myBottomPort = createEndPort(false);
        addObjectAtTail(myBottomPort);
        setInitializing(false);
        layoutChildren(null);
    }

    public GraphicViewerDrawable createBackground() {
        GraphicViewerRectangle graphicviewerrectangle = new GraphicViewerRectangle();
        graphicviewerrectangle.setBrush(null);
        graphicviewerrectangle.setSelectable(false);
        graphicviewerrectangle.setDraggable(false);
        graphicviewerrectangle.setResizable(false);
        return graphicviewerrectangle;
    }

    public GraphicViewerObject createEndPort(boolean flag) {
        GraphicViewerPort graphicviewerport = new GraphicViewerPort();
        graphicviewerport.setSize(5, 3);
        graphicviewerport.setStyle(0);
        if (flag) {
            graphicviewerport.setFromSpot(2);
            graphicviewerport.setToSpot(2);
        } else {
            graphicviewerport.setFromSpot(6);
            graphicviewerport.setToSpot(6);
        }
        return graphicviewerport;
    }

    public GraphicViewerObject createItem(int i, String s) {
        GraphicViewerText graphicviewertext = new GraphicViewerText(s);
        graphicviewertext.setTransparent(true);
        graphicviewertext.setMultiline(true);
        graphicviewertext.setClipping(true);
        graphicviewertext.setWrapping(true);
        graphicviewertext.setWrappingWidth(getItemWidth());
        return graphicviewertext;
    }

    public GraphicViewerObject createPort(int i, boolean flag) {
        GraphicViewerPort graphicviewerport = new GraphicViewerPort();
        graphicviewerport.setSize(3, 5);
        graphicviewerport.setStyle(0);
        if (flag) {
            graphicviewerport.setFromSpot(8);
            graphicviewerport.setToSpot(8);
        } else {
            graphicviewerport.setFromSpot(4);
            graphicviewerport.setToSpot(4);
        }
        return graphicviewerport;
    }

    protected void copyChildren(GraphicViewerArea graphicviewerarea,
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        MultiTextNode multitextnode = (MultiTextNode) graphicviewerarea;
        multitextnode.myInsets.top = myInsets.top;
        multitextnode.myInsets.left = myInsets.left;
        multitextnode.myInsets.bottom = myInsets.bottom;
        multitextnode.myInsets.right = myInsets.right;
        multitextnode.mySpacing = mySpacing;
        multitextnode.myLinePen = myLinePen;
        multitextnode.myItemWidth = myItemWidth;
        super.copyChildren(graphicviewerarea, graphicviewercopyenvironment);
        multitextnode.myBack = (GraphicViewerDrawable) graphicviewercopyenvironment
                .get(myBack);
        multitextnode.myTopPort = (GraphicViewerObject) graphicviewercopyenvironment
                .get(myTopPort);
        multitextnode.myBottomPort = (GraphicViewerObject) graphicviewercopyenvironment
                .get(myBottomPort);
        for (int i = 0; i < myVector.size(); i++) {
            GraphicViewerObject graphicviewerobject = (GraphicViewerObject) myVector
                    .get(i);
            GraphicViewerObject graphicviewerobject3 = (GraphicViewerObject) graphicviewercopyenvironment
                    .get(graphicviewerobject);
            multitextnode.myVector.add(graphicviewerobject3);
        }

        for (int j = 0; j < myLeftPorts.size(); j++) {
            GraphicViewerObject graphicviewerobject1 = (GraphicViewerObject) myLeftPorts
                    .get(j);
            GraphicViewerObject graphicviewerobject4 = (GraphicViewerObject) graphicviewercopyenvironment
                    .get(graphicviewerobject1);
            multitextnode.myLeftPorts.add(graphicviewerobject4);
        }

        for (int k = 0; k < myRightPorts.size(); k++) {
            GraphicViewerObject graphicviewerobject2 = (GraphicViewerObject) myRightPorts
                    .get(k);
            GraphicViewerObject graphicviewerobject5 = (GraphicViewerObject) graphicviewercopyenvironment
                    .get(graphicviewerobject2);
            multitextnode.myRightPorts.add(graphicviewerobject5);
        }

    }

    public GraphicViewerDrawable getBackground() {
        return myBack;
    }

    public void setBackground(GraphicViewerDrawable graphicviewerdrawable) {
        GraphicViewerDrawable graphicviewerdrawable1 = myBack;
        if (graphicviewerdrawable1 != graphicviewerdrawable) {
            if (graphicviewerdrawable1 != null) {
                if (graphicviewerdrawable != null)
                    graphicviewerdrawable
                            .setBoundingRect(graphicviewerdrawable1
                                    .getBoundingRect());
                removeObject(graphicviewerdrawable1);
            }
            myBack = graphicviewerdrawable;
            if (graphicviewerdrawable != null) {
                graphicviewerdrawable.setSelectable(false);
                graphicviewerdrawable.setDraggable(false);
                graphicviewerdrawable.setResizable(false);
                addObjectAtHead(graphicviewerdrawable);
            }
            update(0x12725, 0, graphicviewerdrawable1);
        }
    }

    public GraphicViewerPen getLinePen() {
        return myLinePen;
    }

    public void setLinePen(GraphicViewerPen graphicviewerpen) {
        GraphicViewerPen graphicviewerpen1 = myLinePen;
        if (graphicviewerpen1 != graphicviewerpen) {
            myLinePen = graphicviewerpen;
            layoutChildren(null);
            update(0x1272f, 0, graphicviewerpen1);
        }
    }

    public Insets getInsets() {
        return myInsets;
    }

    public void setInsets(Insets insets) {
        Insets insets1 = myInsets;
        if (!insets1.equals(insets)) {
            Insets insets2 = new Insets(insets1.top, insets1.left,
                    insets1.bottom, insets1.right);
            myInsets.top = insets.top;
            myInsets.left = insets.left;
            myInsets.bottom = insets.bottom;
            myInsets.right = insets.right;
            layoutChildren(null);
            update(0x12730, 0, insets2);
        }
    }

    public int getSpacing() {
        return mySpacing;
    }

    public void setSpacing(int i) {
        int j = mySpacing;
        if (j != i) {
            mySpacing = i;
            layoutChildren(null);
            update(0x12731, j, null);
        }
    }

    public GraphicViewerPen getPen() {
        return getBackground().getPen();
    }

    public void setPen(GraphicViewerPen graphicviewerpen) {
        getBackground().setPen(graphicviewerpen);
    }

    public GraphicViewerBrush getBrush() {
        return getBackground().getBrush();
    }

    public void setBrush(GraphicViewerBrush graphicviewerbrush) {
        getBackground().setBrush(graphicviewerbrush);
    }

    public GraphicViewerObject getTopPort() {
        return myTopPort;
    }

    public void setTopPort(GraphicViewerObject graphicviewerobject) {
        GraphicViewerObject graphicviewerobject1 = myTopPort;
        if (graphicviewerobject1 != graphicviewerobject) {
            if (graphicviewerobject1 != null)
                removeObject(graphicviewerobject1);
            myTopPort = graphicviewerobject;
            if (graphicviewerobject != null)
                addObjectAtTail(graphicviewerobject);
            update(0x12726, 0, graphicviewerobject1);
        }
    }

    public GraphicViewerObject getBottomPort() {
        return myBottomPort;
    }

    public void setBottomPort(GraphicViewerObject graphicviewerobject) {
        GraphicViewerObject graphicviewerobject1 = myBottomPort;
        if (graphicviewerobject1 != graphicviewerobject) {
            if (graphicviewerobject1 != null)
                removeObject(graphicviewerobject1);
            myBottomPort = graphicviewerobject;
            if (graphicviewerobject != null)
                addObjectAtTail(graphicviewerobject);
            update(0x12728, 0, graphicviewerobject1);
        }
    }

    public int getNumItems() {
        return myVector.size();
    }

    public GraphicViewerObject getItem(int i) {
        if (i < 0 || i >= myVector.size())
            return null;
        else
            return (GraphicViewerObject) myVector.get(i);
    }

    public void setItem(int i, GraphicViewerObject graphicviewerobject) {
        if (i < 0 || i >= getNumItems())
            return;
        GraphicViewerObject graphicviewerobject1 = getItem(i);
        if (graphicviewerobject1 == null)
            return;
        if (graphicviewerobject1 != graphicviewerobject) {
            removeObject(graphicviewerobject1);
            myVector.set(i, graphicviewerobject);
            if (graphicviewerobject != null) {
                addObjectAtTail(graphicviewerobject);
                graphicviewerobject.setSelectable(false);
                graphicviewerobject.setResizable(false);
                graphicviewerobject.setDraggable(false);
                layoutChildren(graphicviewerobject);
            }
            update(0x12734, i, graphicviewerobject1);
        }
    }

    public GraphicViewerObject getLeftPort(int i) {
        if (i < 0 || i >= myLeftPorts.size())
            return null;
        else
            return (GraphicViewerObject) myLeftPorts.get(i);
    }

    public void setLeftPort(int i, GraphicViewerObject graphicviewerobject) {
        GraphicViewerObject graphicviewerobject1 = getLeftPort(i);
        if (graphicviewerobject1 != graphicviewerobject) {
            if (graphicviewerobject1 != null) {
                if (graphicviewerobject != null)
                    graphicviewerobject.setBoundingRect(graphicviewerobject1
                            .getBoundingRect());
                removeObject(graphicviewerobject1);
            }
            myLeftPorts.set(i, graphicviewerobject);
            if (graphicviewerobject != null) {
                graphicviewerobject.setSelectable(false);
                graphicviewerobject.setDraggable(false);
                graphicviewerobject.setResizable(false);
                addObjectAtTail(graphicviewerobject);
            }
            update(0x12735, i, graphicviewerobject1);
        }
    }

    public GraphicViewerObject getRightPort(int i) {
        if (i < 0 || i >= myRightPorts.size())
            return null;
        else
            return (GraphicViewerObject) myRightPorts.get(i);
    }

    public void setRightPort(int i, GraphicViewerObject graphicviewerobject) {
        GraphicViewerObject graphicviewerobject1 = getRightPort(i);
        if (graphicviewerobject1 != graphicviewerobject) {
            if (graphicviewerobject1 != null) {
                if (graphicviewerobject != null)
                    graphicviewerobject.setBoundingRect(graphicviewerobject1
                            .getBoundingRect());
                removeObject(graphicviewerobject1);
            }
            myRightPorts.set(i, graphicviewerobject);
            if (graphicviewerobject != null) {
                graphicviewerobject.setSelectable(false);
                graphicviewerobject.setDraggable(false);
                graphicviewerobject.setResizable(false);
                addObjectAtTail(graphicviewerobject);
            }
            update(0x12736, i, graphicviewerobject1);
        }
    }

    public String getString(int i) {
        GraphicViewerObject graphicviewerobject = getItem(i);
        if (graphicviewerobject != null
                && (graphicviewerobject instanceof GraphicViewerText)) {
            GraphicViewerText graphicviewertext = (GraphicViewerText) graphicviewerobject;
            return graphicviewertext.getText();
        } else {
            return "";
        }
    }

    public void setString(int i, String s) {
        GraphicViewerObject graphicviewerobject = getItem(i);
        if (graphicviewerobject != null
                && (graphicviewerobject instanceof GraphicViewerText)) {
            GraphicViewerText graphicviewertext = (GraphicViewerText) graphicviewerobject;
            graphicviewertext.setText(s);
        }
    }

    public GraphicViewerObject addString(String s) {
        int i = getNumItems();
        insertItem(i, createItem(i, s), createPort(i, true),
                createPort(i, false));
        return getItem(i);
    }

    public final void addItem(GraphicViewerObject graphicviewerobject,
            GraphicViewerObject graphicviewerobject1,
            GraphicViewerObject graphicviewerobject2) {
        insertItem(getNumItems(), graphicviewerobject, graphicviewerobject1,
                graphicviewerobject2);
    }

    public void insertItem(int i, GraphicViewerObject graphicviewerobject,
            GraphicViewerObject graphicviewerobject1,
            GraphicViewerObject graphicviewerobject2) {
        if (i < 0 || i > getNumItems())
            return;
        GraphicViewerDrawable graphicviewerdrawable = getBackground();
        myVector.add(i, graphicviewerobject);
        if (graphicviewerobject != null) {
            graphicviewerobject.setTopLeft(graphicviewerdrawable.getLeft(),
                    graphicviewerdrawable.getTop());
            graphicviewerobject.setSelectable(false);
            graphicviewerobject.setDraggable(false);
            graphicviewerobject.setResizable(false);
            addObjectAtTail(graphicviewerobject);
        }
        myLeftPorts.add(i, graphicviewerobject1);
        if (graphicviewerobject1 != null) {
            graphicviewerobject1.setTopLeft(graphicviewerdrawable.getLeft(),
                    graphicviewerdrawable.getTop());
            graphicviewerobject1.setSelectable(false);
            graphicviewerobject1.setDraggable(false);
            graphicviewerobject1.setResizable(false);
            addObjectAtTail(graphicviewerobject1);
        }
        myRightPorts.add(i, graphicviewerobject2);
        if (graphicviewerobject2 != null) {
            graphicviewerobject2.setTopLeft(graphicviewerdrawable.getLeft(),
                    graphicviewerdrawable.getTop());
            graphicviewerobject2.setSelectable(false);
            graphicviewerobject2.setDraggable(false);
            graphicviewerobject2.setResizable(false);
            addObjectAtTail(graphicviewerobject2);
        }
        layoutChildren(null);
        GraphicViewerObject agraphicviewerobject[] = new GraphicViewerObject[3];
        agraphicviewerobject[0] = graphicviewerobject;
        agraphicviewerobject[1] = graphicviewerobject1;
        agraphicviewerobject[2] = graphicviewerobject2;
        update(0x12737, i, agraphicviewerobject);
    }

    public void removeItem(int i) {
        if (i < 0 || i >= getNumItems())
            return;
        GraphicViewerObject graphicviewerobject = getLeftPort(i);
        myLeftPorts.remove(i);
        if (graphicviewerobject != null)
            super.removeObject(graphicviewerobject);
        GraphicViewerObject graphicviewerobject1 = getRightPort(i);
        myRightPorts.remove(i);
        if (graphicviewerobject1 != null)
            super.removeObject(graphicviewerobject1);
        GraphicViewerObject graphicviewerobject2 = getItem(i);
        if (graphicviewerobject2 != null) {
            myVector.remove(i);
            super.removeObject(graphicviewerobject2);
        }
        layoutChildren(null);
        GraphicViewerObject agraphicviewerobject[] = new GraphicViewerObject[3];
        agraphicviewerobject[0] = graphicviewerobject2;
        agraphicviewerobject[1] = graphicviewerobject;
        agraphicviewerobject[2] = graphicviewerobject1;
        update(0x12738, i, agraphicviewerobject);
    }

    public void removeObject(GraphicViewerObject graphicviewerobject) {
        boolean flag = false;
        int i = getNumItems();
        int j = 0;
        do {
            if (j >= i)
                break;
            GraphicViewerObject graphicviewerobject1 = getItem(j);
            if (graphicviewerobject1 == graphicviewerobject) {
                removeItem(j);
                flag = true;
                break;
            }
            j++;
        } while (true);
        if (!flag)
            super.removeObject(graphicviewerobject);
    }

    public void layoutChildren(GraphicViewerObject graphicviewerobject) {
        if (isInitializing())
            return;
        Insets insets = getInsets();
        GraphicViewerDrawable graphicviewerdrawable = getBackground();
        if (graphicviewerdrawable == null)
            return;
        setInitializing(true);
        sendObjectToBack(graphicviewerdrawable);
        if (getTopPort() != null)
            getTopPort().getHeight();
        if (getBottomPort() != null)
            getBottomPort().getHeight();
        getMaxPortWidth(true);
        getMaxPortWidth(false);
        int i1 = getItemWidth();
        int j1 = graphicviewerdrawable.getLeft() + insets.left;
        int k1 = graphicviewerdrawable.getTop() + insets.top;
        int l1 = i1 + insets.left + insets.right;
        int i2 = 0;
        if (getLinePen() != null)
            i2 = getLinePen().getWidth();
        int j2 = Math.max(i2, getSpacing());
        layoutEndPorts();
        int k2 = 0;
        int l2 = getNumItems();
        for (int i3 = 0; i3 < l2; i3++) {
            int j3 = getItemHeight(i3);
            layoutItem(i3, j1, k1 + k2, l1, j3, i1);
            k2 += j3;
            k2 += j2;
        }

        if (l2 > 0)
            k2 -= j2;
        graphicviewerdrawable.setSize(l1, k2 + insets.top + insets.bottom);
        setInitializing(false);
    }

    public int getItemWidth() {
        return myItemWidth;
    }

    public void setItemWidth(int i) {
        int j = myItemWidth;
        if (j != i) {
            myItemWidth = i;
            for (int k = 0; k < getNumItems(); k++) {
                GraphicViewerObject graphicviewerobject = getItem(k);
                if (graphicviewerobject != null
                        && (graphicviewerobject instanceof GraphicViewerText)) {
                    GraphicViewerText graphicviewertext = (GraphicViewerText) graphicviewerobject;
                    graphicviewertext.setWrappingWidth(i);
                }
            }

            layoutChildren(null);
            update(0x12733, j, null);
        }
    }

    public int getMaxPortWidth(boolean flag) {
        int i = 0;
        ArrayList<?> arraylist = flag ? myLeftPorts : myRightPorts;
        for (int j = 0; j < arraylist.size(); j++) {
            GraphicViewerObject graphicviewerobject = (GraphicViewerObject) arraylist
                    .get(j);
            if (graphicviewerobject != null)
                i = Math.max(i, graphicviewerobject.getWidth());
        }

        return i;
    }

    protected int getItemHeight(int i) {
        int j = 0;
        GraphicViewerObject graphicviewerobject = getItem(i);
        if (graphicviewerobject != null)
            j = graphicviewerobject.getHeight();
        GraphicViewerObject graphicviewerobject1 = getLeftPort(i);
        if (graphicviewerobject1 != null)
            j = Math.max(j, graphicviewerobject1.getHeight());
        GraphicViewerObject graphicviewerobject2 = getRightPort(i);
        if (graphicviewerobject2 != null)
            j = Math.max(j, graphicviewerobject2.getHeight());
        return j;
    }

    protected void layoutEndPorts() {
        GraphicViewerDrawable graphicviewerdrawable = getBackground();
        if (graphicviewerdrawable == null)
            return;
        GraphicViewerObject graphicviewerobject = getTopPort();
        if (graphicviewerobject != null)
            graphicviewerobject.setSpotLocation(6, graphicviewerdrawable, 2);
        graphicviewerobject = getBottomPort();
        if (graphicviewerobject != null)
            graphicviewerobject.setSpotLocation(2, graphicviewerdrawable, 6);
    }

    protected void layoutItem(int i, int j, int k, int l, int i1, int j1) {
        GraphicViewerObject graphicviewerobject = getItem(i);
        if (graphicviewerobject != null) {
            graphicviewerobject.setTopLeft(j, (k + i1 / 2)
                    - graphicviewerobject.getHeight() / 2);
            graphicviewerobject.setWidth(j1);
        }
        GraphicViewerDrawable graphicviewerdrawable = getBackground();
        if (graphicviewerdrawable == null)
            return;
        int k1 = graphicviewerdrawable.getLeft();
        graphicviewerdrawable.getTop();
        int i2 = graphicviewerdrawable.getWidth();
        GraphicViewerObject graphicviewerobject1 = getLeftPort(i);
        if (graphicviewerobject1 != null) {
            graphicviewerobject1.setVisible(true);
            int j2 = k1 - graphicviewerobject1.getWidth();
            int k2 = (k + i1 / 2) - graphicviewerobject1.getHeight() / 2;
            graphicviewerobject1.setTopLeft(j2, k2);
        }
        GraphicViewerObject graphicviewerobject2 = getRightPort(i);
        if (graphicviewerobject2 != null) {
            graphicviewerobject2.setVisible(true);
            int l2 = k1 + i2;
            int i3 = (k + i1 / 2) - graphicviewerobject2.getHeight() / 2;
            graphicviewerobject2.setTopLeft(l2, i3);
        }
    }

    public void paint(Graphics2D graphics2d, GraphicViewerView graphicviewerview) {
        super.paint(graphics2d, graphicviewerview);
        int i = 0;
        if (getLinePen() != null)
            i = getLinePen().getWidth();
        if (i <= 0)
            return;
        GraphicViewerDrawable graphicviewerdrawable = getBackground();
        if (graphicviewerdrawable == null)
            return;
        int j = graphicviewerdrawable.getLeft();
        int k = graphicviewerdrawable.getTop();
        int l = graphicviewerdrawable.getWidth();
        graphicviewerdrawable.getHeight();
        Insets insets = getInsets();
        int k1 = k + insets.top;
        int i2 = 0;
        int j2 = Math.max(i, getSpacing());
        int k2 = getNumItems() - 1;
        for (int l2 = 0; l2 < k2; l2++) {
            int i3 = getItemHeight(l2);
            i2 += i3;
            GraphicViewerDrawable.drawLine(graphics2d, getLinePen(), j, k1 + i2
                    + j2 / 2, j + l, k1 + i2 + j2 / 2);
            i2 += j2;
        }

    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 75567 :
                graphicviewerdocumentchangededit.setNewValue(getLinePen());
                return;

            case 75568 :
                Insets insets = getInsets();
                graphicviewerdocumentchangededit.setNewValue(new Insets(
                        insets.top, insets.left, insets.bottom, insets.right));
                return;

            case 75569 :
                graphicviewerdocumentchangededit.setNewValueInt(getSpacing());
                return;

            case 75572 :
                graphicviewerdocumentchangededit
                        .setNewValue(getItem(graphicviewerdocumentchangededit
                                .getOldValueInt()));
                return;

            case 75571 :
                graphicviewerdocumentchangededit.setNewValueInt(getItemWidth());
                return;

            case 75557 :
                graphicviewerdocumentchangededit.setNewValue(getBackground());
                return;

            case 75558 :
                graphicviewerdocumentchangededit.setNewValue(getTopPort());
                return;

            case 75560 :
                graphicviewerdocumentchangededit.setNewValue(getBottomPort());
                return;

            case 75573 :
                graphicviewerdocumentchangededit
                        .setNewValue(getLeftPort(graphicviewerdocumentchangededit
                                .getOldValueInt()));
                return;

            case 75574 :
                graphicviewerdocumentchangededit
                        .setNewValue(getRightPort(graphicviewerdocumentchangededit
                                .getOldValueInt()));
                return;

            case 75575 :
                return;

            case 75576 :
                return;

            case 75559 :
            case 75561 :
            case 75562 :
            case 75563 :
            case 75564 :
            case 75565 :
            case 75566 :
            case 75570 :
            default :
                super.copyNewValueForRedo(graphicviewerdocumentchangededit);
                return;
        }
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 75567 :
                myLinePen = (GraphicViewerPen) graphicviewerdocumentchangededit
                        .getValue(flag);
                update();
                return;

            case 75568 :
                Insets insets = (Insets) graphicviewerdocumentchangededit
                        .getValue(flag);
                myInsets.top = insets.top;
                myInsets.left = insets.left;
                myInsets.bottom = insets.bottom;
                myInsets.right = insets.right;
                return;

            case 75569 :
                mySpacing = graphicviewerdocumentchangededit.getValueInt(flag);
                return;

            case 75572 :
                setItem(graphicviewerdocumentchangededit.getOldValueInt(),
                        (GraphicViewerObject) graphicviewerdocumentchangededit
                                .getValue(flag));
                return;

            case 75571 :
                myItemWidth = graphicviewerdocumentchangededit
                        .getValueInt(flag);
                return;

            case 75557 :
                setBackground((GraphicViewerDrawable) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 75558 :
                setTopPort((GraphicViewerPort) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 75560 :
                setBottomPort((GraphicViewerPort) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 75573 :
                setLeftPort(graphicviewerdocumentchangededit.getOldValueInt(),
                        (GraphicViewerObject) graphicviewerdocumentchangededit
                                .getValue(flag));
                return;

            case 75574 :
                setRightPort(graphicviewerdocumentchangededit.getOldValueInt(),
                        (GraphicViewerObject) graphicviewerdocumentchangededit
                                .getValue(flag));
                return;

            case 75575 :
                if (flag) {
                    removeItem(graphicviewerdocumentchangededit
                            .getOldValueInt());
                } else {
                    GraphicViewerObject agraphicviewerobject[] = (GraphicViewerObject[]) graphicviewerdocumentchangededit
                            .getOldValue();
                    insertItem(
                            graphicviewerdocumentchangededit.getOldValueInt(),
                            agraphicviewerobject[0], agraphicviewerobject[1],
                            agraphicviewerobject[2]);
                }
                return;

            case 75576 :
                if (flag) {
                    GraphicViewerObject agraphicviewerobject1[] = (GraphicViewerObject[]) graphicviewerdocumentchangededit
                            .getOldValue();
                    insertItem(
                            graphicviewerdocumentchangededit.getOldValueInt(),
                            agraphicviewerobject1[0], agraphicviewerobject1[1],
                            agraphicviewerobject1[2]);
                } else {
                    removeItem(graphicviewerdocumentchangededit
                            .getOldValueInt());
                }
                return;

            case 75559 :
            case 75561 :
            case 75562 :
            case 75563 :
            case 75564 :
            case 75565 :
            case 75566 :
            case 75570 :
            default :
                super.changeValue(graphicviewerdocumentchangededit, flag);
                return;
        }
    }

    public void SVGUpdateReference(String s, Object obj) {
        super.SVGUpdateReference(s, obj);
        if (s.equals("backdraw")) {
            myBack = (GraphicViewerDrawable) obj;
            layoutChildren(null);
        } else if (s.equals("topport"))
            myTopPort = (GraphicViewerObject) obj;
        else if (s.equals("bottomport"))
            myBottomPort = (GraphicViewerObject) obj;
        else if (s.equals("vectorelements"))
            myVector.add(obj);
        else if (s.equals("leftports"))
            myLeftPorts.add(obj);
        else if (s.equals("rightports"))
            myRightPorts.add(obj);
        else if (s.equals("linepen")) {
            myLinePen = (GraphicViewerPen) obj;
            layoutChildren(null);
        }
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.MultiTextNode",
                            domelement);
            domelement1.setAttribute("spacing", Integer.toString(mySpacing));
            domelement1
                    .setAttribute("itemwidth", Integer.toString(myItemWidth));
            domelement1.setAttribute("insetleft",
                    Integer.toString(myInsets.left));
            domelement1.setAttribute("insetright",
                    Integer.toString(myInsets.right));
            domelement1
                    .setAttribute("insettop", Integer.toString(myInsets.top));
            domelement1.setAttribute("insetbottom",
                    Integer.toString(myInsets.bottom));
            if (myBack != null)
                domdoc.registerReferencingNode(domelement1, "backdraw", myBack);
            if (myTopPort != null)
                domdoc.registerReferencingNode(domelement1, "topport",
                        myTopPort);
            if (myBottomPort != null)
                domdoc.registerReferencingNode(domelement1, "bottomport",
                        myBottomPort);
            for (int i = 0; i < myVector.size(); i++)
                domdoc.registerReferencingNode(domelement1, "vectorelements",
                        (GraphicViewerObject) myVector.get(i));

            for (int j = 0; j < myLeftPorts.size(); j++)
                domdoc.registerReferencingNode(domelement1, "leftports",
                        (GraphicViewerObject) myLeftPorts.get(j));

            for (int k = 0; k < myRightPorts.size(); k++)
                domdoc.registerReferencingNode(domelement1, "rightports",
                        (GraphicViewerObject) myRightPorts.get(k));

            if (myLinePen != null) {
                if (!domdoc.isRegisteredReference(myLinePen)) {
                    domelement1.setAttribute("embeddedlinepen", "true");
                    IDomElement domelement2 = domdoc.createElement("g");
                    domelement1.appendChild(domelement2);
                    myLinePen.SVGWriteObject(domdoc, domelement2);
                }
                domdoc.registerReferencingNode(domelement1, "linepen",
                        myLinePen);
            }
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument,
            IDomElement domelement, IDomElement domelement1) {
        if (domelement1 != null) {
            mySpacing = Integer.parseInt(domelement1.getAttribute("spacing"));
            myItemWidth = Integer.parseInt(domelement1
                    .getAttribute("itemwidth"));
            myInsets.left = Integer.parseInt(domelement1
                    .getAttribute("insetleft"));
            myInsets.right = Integer.parseInt(domelement1
                    .getAttribute("insetright"));
            myInsets.top = Integer.parseInt(domelement1
                    .getAttribute("insettop"));
            myInsets.bottom = Integer.parseInt(domelement1
                    .getAttribute("insetbottom"));
            String s = domelement1.getAttribute("backdraw");
            domdoc.registerReferencingObject(this, "backdraw", s);
            String s1 = domelement1.getAttribute("topport");
            domdoc.registerReferencingObject(this, "topport", s1);
            String s2 = domelement1.getAttribute("bottomport");
            domdoc.registerReferencingObject(this, "bottomport", s2);
            String s5;
            for (String s3 = domelement1.getAttribute("vectorelements"); s3
                    .length() > 0; domdoc.registerReferencingObject(this,
                    "vectorelements", s5)) {
                int i = s3.indexOf(" ");
                if (i == -1)
                    i = s3.length();
                s5 = s3.substring(0, i);
                if (i >= s3.length())
                    s3 = "";
                else
                    s3 = s3.substring(i + 1);
            }

            String s7;
            for (String s4 = domelement1.getAttribute("leftports"); s4.length() > 0; domdoc
                    .registerReferencingObject(this, "leftports", s7)) {
                int j = s4.indexOf(" ");
                if (j == -1)
                    j = s4.length();
                s7 = s4.substring(0, j);
                if (j >= s4.length())
                    s4 = "";
                else
                    s4 = s4.substring(j + 1);
            }

            String s9;
            for (String s6 = domelement1.getAttribute("rightports"); s6
                    .length() > 0; domdoc.registerReferencingObject(this,
                    "rightports", s9)) {
                int k = s6.indexOf(" ");
                if (k == -1)
                    k = s6.length();
                s9 = s6.substring(0, k);
                if (k >= s6.length())
                    s6 = "";
                else
                    s6 = s6.substring(k + 1);
            }

            if (domelement1.getAttribute("embeddedlinepen").equals("true"))
                domdoc.SVGTraverseChildren(graphicviewerdocument, domelement1,
                        null, false);
            String s8 = domelement1.getAttribute("linepen");
            domdoc.registerReferencingObject(this, "linepen", s8);
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        }
        return domelement.getNextSibling();
    }

    public static final int LinePenChanged = 0x1272f;
    public static final int InsetsChanged = 0x12730;
    public static final int SpacingChanged = 0x12731;
    public static final int ItemWidthChanged = 0x12733;
    public static final int ItemChanged = 0x12734;
    public static final int BackgroundChanged = 0x12725;
    public static final int TopPortChanged = 0x12726;
    public static final int BottomPortChanged = 0x12728;
    public static final int LeftPortChanged = 0x12735;
    public static final int RightPortChanged = 0x12736;
    public static final int ItemInsertedChanged = 0x12737;
    public static final int ItemRemovedChanged = 0x12738;
    private ArrayList<Object> myVector;
    private ArrayList<Object> myLeftPorts;
    private ArrayList<Object> myRightPorts;
    private GraphicViewerObject myTopPort;
    private GraphicViewerObject myBottomPort;
    private GraphicViewerDrawable myBack;
    private Insets myInsets;
    private int mySpacing;
    private GraphicViewerPen myLinePen;
    private int myItemWidth;
}