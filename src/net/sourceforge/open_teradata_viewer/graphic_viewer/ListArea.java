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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ListArea extends GraphicViewerArea {

    private static final long serialVersionUID = -6277318536848734193L;

    public ListArea() {
        myVector = new ArrayList<Object>();
        myIcons = new ArrayList<Object>();
        myLeftPorts = new ArrayList<Object>();
        myRightPorts = new ArrayList<Object>();
        myRect = null;
        myBar = null;
        myVertical = true;
        myScrollBarOnRight = true;
        myAlignment = 0;
        myIconAlignment = 0;
        myFirstItem = 0;
        myLastItem = -1;
        myInsets = new Insets(1, 4, 1, myBarSize + 4);
        mySpacing = 0;
        myLinePen = null;
        myIconSpacing = 1;
        myMaxItemSize = new Dimension(-1, -1);
    }

    public void initialize() {
        myRect = new ListAreaRect();
        myRect.setBrush(GraphicViewerBrush.lightGray);
        myRect.setSelectable(true);
        addObjectAtHead(myRect);
        myBar = new GraphicViewerScrollBar();
        myBar.setVertical(isVertical());
        myBar.setSelectable(false);
        addObjectAtTail(myBar);
    }

    protected void copyChildren(GraphicViewerArea graphicviewerarea,
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        ListArea listarea = (ListArea) graphicviewerarea;
        listarea.myVertical = myVertical;
        listarea.myScrollBarOnRight = myScrollBarOnRight;
        listarea.myAlignment = myAlignment;
        listarea.myIconAlignment = myIconAlignment;
        listarea.myFirstItem = myFirstItem;
        listarea.myLastItem = myLastItem;
        listarea.myInsets.top = myInsets.top;
        listarea.myInsets.left = myInsets.left;
        listarea.myInsets.bottom = myInsets.bottom;
        listarea.myInsets.right = myInsets.right;
        listarea.mySpacing = mySpacing;
        listarea.myLinePen = myLinePen;
        listarea.myIconSpacing = myIconSpacing;
        super.copyChildren(graphicviewerarea, graphicviewercopyenvironment);
        listarea.myRect = (GraphicViewerRectangle) graphicviewercopyenvironment
                .get(myRect);
        for (int i = 0; i < myVector.size(); i++) {
            GraphicViewerObject graphicviewerobject = (GraphicViewerObject) myVector
                    .get(i);
            GraphicViewerObject graphicviewerobject4 = (GraphicViewerObject) graphicviewercopyenvironment
                    .get(graphicviewerobject);
            listarea.myVector.add(graphicviewerobject4);
        }

        for (int j = 0; j < myIcons.size(); j++) {
            GraphicViewerObject graphicviewerobject1 = (GraphicViewerObject) myIcons
                    .get(j);
            GraphicViewerObject graphicviewerobject5 = (GraphicViewerObject) graphicviewercopyenvironment
                    .get(graphicviewerobject1);
            listarea.myIcons.add(graphicviewerobject5);
        }

        for (int k = 0; k < myLeftPorts.size(); k++) {
            GraphicViewerObject graphicviewerobject2 = (GraphicViewerObject) myLeftPorts
                    .get(k);
            GraphicViewerObject graphicviewerobject6 = (GraphicViewerObject) graphicviewercopyenvironment
                    .get(graphicviewerobject2);
            listarea.myLeftPorts.add(graphicviewerobject6);
        }

        for (int l = 0; l < myRightPorts.size(); l++) {
            GraphicViewerObject graphicviewerobject3 = (GraphicViewerObject) myRightPorts
                    .get(l);
            GraphicViewerObject graphicviewerobject7 = (GraphicViewerObject) graphicviewercopyenvironment
                    .get(graphicviewerobject3);
            listarea.myRightPorts.add(graphicviewerobject7);
        }

        listarea.myBar = (GraphicViewerScrollBar) graphicviewercopyenvironment
                .get(myBar);
    }

    public GraphicViewerRectangle getRect() {
        return myRect;
    }

    public GraphicViewerScrollBar getScrollBar() {
        return myBar;
    }

    public boolean isVertical() {
        return myVertical;
    }

    public void setVertical(boolean flag) {
        boolean flag1 = myVertical;
        if (flag1 != flag) {
            myVertical = flag;
            if (getScrollBar() != null)
                getScrollBar().setVertical(isVertical());
            Insets insets = getInsets();
            if (flag) {
                if (isScrollBarOnRight()) {
                    insets.right += myBarSize;
                    insets.bottom -= myBarSize;
                } else {
                    insets.left += myBarSize;
                    insets.top -= myBarSize;
                }
            } else if (isScrollBarOnRight()) {
                insets.right -= myBarSize;
                insets.bottom += myBarSize;
            } else {
                insets.left -= myBarSize;
                insets.top += myBarSize;
            }
            layoutChildren(null);
            update(0x1272d, flag1 ? 1 : 0, null);
        }
    }

    public boolean isScrollBarOnRight() {
        return myScrollBarOnRight;
    }

    public void setScrollBarOnRight(boolean flag) {
        boolean flag1 = myScrollBarOnRight;
        if (flag1 != flag) {
            myScrollBarOnRight = flag;
            Insets insets = getInsets();
            if (flag) {
                if (isVertical()) {
                    insets.right += myBarSize;
                    insets.left -= myBarSize;
                } else {
                    insets.bottom += myBarSize;
                    insets.top -= myBarSize;
                }
            } else if (isVertical()) {
                insets.right -= myBarSize;
                insets.left += myBarSize;
            } else {
                insets.bottom -= myBarSize;
                insets.top += myBarSize;
            }
            layoutChildren(null);
            update(0x1272e, flag1 ? 1 : 0, null);
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

    public int getAlignment() {
        return myAlignment;
    }

    public void setAlignment(int i) {
        int j = myAlignment;
        if (j != i) {
            myAlignment = i;
            layoutChildren(null);
            update(0x12732, j, null);
        }
    }

    public int getIconAlignment() {
        return myIconAlignment;
    }

    public void setIconAlignment(int i) {
        int j = myIconAlignment;
        if (j != i) {
            myIconAlignment = i;
            layoutChildren(null);
            update(0x1273a, j, null);
        }
    }

    public int getIconSpacing() {
        return myIconSpacing;
    }

    public void setIconSpacing(int i) {
        int j = myIconSpacing;
        if (j != i) {
            myIconSpacing = i;
            layoutChildren(null);
            update(0x1273b, j, null);
        }
    }

    public int getFirstVisibleIndex() {
        return myFirstItem;
    }

    public void setFirstVisibleIndex(int i) {
        int j = myFirstItem;
        if (i >= 0 && i <= getNumItems() && j != i) {
            myFirstItem = i;
            layoutChildren(null);
            update(0x12733, j, null);
        }
    }

    public int getLastVisibleIndex() {
        return myLastItem;
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
            adjustMaxItemSize(graphicviewerobject,
                    graphicviewerobject1.getWidth(),
                    graphicviewerobject1.getHeight(), getIcon(i));
            if (graphicviewerobject != null) {
                addObjectAtTail(graphicviewerobject);
                graphicviewerobject.setResizable(false);
                GraphicViewerRectangle graphicviewerrectangle = getRect();
                if (i < getFirstVisibleIndex()) {
                    graphicviewerobject.setVisible(false);
                    graphicviewerobject.setTopLeft(
                            graphicviewerrectangle.getLeft(),
                            graphicviewerrectangle.getTop());
                } else if (i <= getLastVisibleIndex()) {
                    layoutChildren(graphicviewerobject);
                } else {
                    graphicviewerobject.setVisible(false);
                    if (isVertical())
                        graphicviewerobject
                                .setTopLeft(
                                        graphicviewerrectangle.getLeft(),
                                        (graphicviewerrectangle.getTop() + graphicviewerrectangle
                                                .getHeight())
                                                - graphicviewerobject
                                                        .getHeight());
                    else
                        graphicviewerobject
                                .setTopLeft(
                                        (graphicviewerrectangle.getLeft() + graphicviewerrectangle
                                                .getWidth())
                                                - graphicviewerobject
                                                        .getWidth(),
                                        graphicviewerrectangle.getTop());
                }
            }
            update(0x12734, i, graphicviewerobject1);
        }
    }

    public int findItem(GraphicViewerObject graphicviewerobject) {
        for (int i = 0; i < getNumItems(); i++) {
            GraphicViewerObject graphicviewerobject1 = getItem(i);
            if (graphicviewerobject1 == graphicviewerobject)
                return i;
        }

        return -1;
    }

    public GraphicViewerObject getIcon(int i) {
        if (i < 0 || i >= myIcons.size())
            return null;
        else
            return (GraphicViewerObject) myIcons.get(i);
    }

    public void setIcon(int i, GraphicViewerObject graphicviewerobject) {
        GraphicViewerObject graphicviewerobject1 = getIcon(i);
        if (graphicviewerobject1 != graphicviewerobject) {
            if (graphicviewerobject1 != null) {
                if (graphicviewerobject != null)
                    graphicviewerobject.setBoundingRect(graphicviewerobject1
                            .getBoundingRect());
                removeObject(graphicviewerobject1);
            }
            myIcons.set(i, graphicviewerobject);
            if (graphicviewerobject != null) {
                graphicviewerobject.setResizable(false);
                addObjectAtTail(graphicviewerobject);
                if (graphicviewerobject1 != null)
                    adjustMaxItemSize(graphicviewerobject,
                            graphicviewerobject1.getWidth(),
                            graphicviewerobject1.getHeight(), getItem(i));
            }
            update(0x12739, i, graphicviewerobject1);
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

    public final void addItem(GraphicViewerObject graphicviewerobject,
            GraphicViewerObject graphicviewerobject1,
            GraphicViewerObject graphicviewerobject2,
            GraphicViewerObject graphicviewerobject3) {
        insertItem(getNumItems(), graphicviewerobject, graphicviewerobject1,
                graphicviewerobject2, graphicviewerobject3);
    }

    public void insertItem(int i, GraphicViewerObject graphicviewerobject,
            GraphicViewerObject graphicviewerobject1,
            GraphicViewerObject graphicviewerobject2,
            GraphicViewerObject graphicviewerobject3) {
        if (i < 0 || i > getNumItems())
            return;
        GraphicViewerRectangle graphicviewerrectangle = getRect();
        myVector.add(i, graphicviewerobject);
        if (graphicviewerobject != null) {
            graphicviewerobject.setTopLeft(graphicviewerrectangle.getLeft(),
                    graphicviewerrectangle.getTop());
            graphicviewerobject.setResizable(false);
            adjustMaxItemSize(graphicviewerobject, -1, -1, graphicviewerobject3);
            addObjectAtTail(graphicviewerobject);
        }
        myIcons.add(i, graphicviewerobject3);
        if (graphicviewerobject3 != null) {
            graphicviewerobject3.setTopLeft(graphicviewerrectangle.getLeft(),
                    graphicviewerrectangle.getTop());
            graphicviewerobject3.setResizable(false);
            addObjectAtTail(graphicviewerobject3);
        }
        myLeftPorts.add(i, graphicviewerobject1);
        if (graphicviewerobject1 != null) {
            graphicviewerobject1.setTopLeft(graphicviewerrectangle.getLeft(),
                    graphicviewerrectangle.getTop());
            graphicviewerobject1.setSelectable(false);
            graphicviewerobject1.setDraggable(false);
            graphicviewerobject1.setResizable(false);
            addObjectAtTail(graphicviewerobject1);
        }
        myRightPorts.add(i, graphicviewerobject2);
        if (graphicviewerobject2 != null) {
            graphicviewerobject2.setTopLeft(graphicviewerrectangle.getLeft(),
                    graphicviewerrectangle.getTop());
            graphicviewerobject2.setSelectable(false);
            graphicviewerobject2.setDraggable(false);
            graphicviewerobject2.setResizable(false);
            addObjectAtTail(graphicviewerobject2);
        }
        if (i < getFirstVisibleIndex()) {
            if (graphicviewerobject != null)
                graphicviewerobject.setVisible(false);
            updateScrollBar();
        } else if (i <= getLastVisibleIndex()) {
            layoutChildren(null);
        } else {
            if (graphicviewerobject != null) {
                graphicviewerobject.setVisible(false);
                if (isVertical())
                    graphicviewerobject
                            .setTopLeft(
                                    graphicviewerrectangle.getLeft(),
                                    (graphicviewerrectangle.getTop() + graphicviewerrectangle
                                            .getHeight())
                                            - graphicviewerobject.getHeight());
                else
                    graphicviewerobject
                            .setTopLeft(
                                    (graphicviewerrectangle.getLeft() + graphicviewerrectangle
                                            .getWidth())
                                            - graphicviewerobject.getWidth(),
                                    graphicviewerrectangle.getTop());
            }
            updateScrollBar();
        }
        GraphicViewerObject agraphicviewerobject[] = new GraphicViewerObject[4];
        agraphicviewerobject[0] = graphicviewerobject;
        agraphicviewerobject[1] = graphicviewerobject1;
        agraphicviewerobject[2] = graphicviewerobject2;
        agraphicviewerobject[3] = graphicviewerobject3;
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
        GraphicViewerObject graphicviewerobject2 = getIcon(i);
        myIcons.remove(i);
        if (graphicviewerobject2 != null)
            super.removeObject(graphicviewerobject2);
        GraphicViewerObject graphicviewerobject3 = getItem(i);
        if (graphicviewerobject3 != null) {
            myVector.remove(i);
            super.removeObject(graphicviewerobject3);
            adjustMaxItemSize(null, graphicviewerobject3.getWidth(),
                    graphicviewerobject3.getHeight(), null);
        }
        if (i <= getLastVisibleIndex())
            layoutChildren(null);
        else
            updateScrollBar();
        GraphicViewerObject agraphicviewerobject[] = new GraphicViewerObject[4];
        agraphicviewerobject[0] = graphicviewerobject3;
        agraphicviewerobject[1] = graphicviewerobject;
        agraphicviewerobject[2] = graphicviewerobject1;
        agraphicviewerobject[3] = graphicviewerobject2;
        update(0x12738, i, agraphicviewerobject);
    }

    public void removeObject(GraphicViewerObject graphicviewerobject) {
        boolean flag = false;
        int i = 0;
        do {
            if (i >= getNumItems())
                break;
            GraphicViewerObject graphicviewerobject1 = getItem(i);
            if (graphicviewerobject1 == graphicviewerobject) {
                removeItem(i);
                flag = true;
                break;
            }
            i++;
        } while (true);
        if (!flag)
            super.removeObject(graphicviewerobject);
    }

    public void layoutChildren(GraphicViewerObject graphicviewerobject) {
        if (isInitializing())
            return;
        setInitializing(true);
        Insets insets = getInsets();
        GraphicViewerRectangle graphicviewerrectangle = getRect();
        if (graphicviewerrectangle == null)
            return;
        sendObjectToBack(graphicviewerrectangle);
        myLastItem = getFirstVisibleIndex();
        int i = graphicviewerrectangle.getLeft() + insets.left;
        int j = graphicviewerrectangle.getTop() + insets.top;
        int k = graphicviewerrectangle.getWidth() - insets.left - insets.right;
        int l = graphicviewerrectangle.getHeight() - insets.top - insets.bottom;
        int i1 = 0;
        if (getLinePen() != null)
            i1 = getLinePen().getWidth();
        if (isVertical()) {
            int j1 = 0;
            for (int l1 = 0; l1 < getNumItems(); l1++) {
                GraphicViewerObject graphicviewerobject1 = getItem(l1);
                GraphicViewerObject graphicviewerobject3 = getIcon(l1);
                GraphicViewerObject graphicviewerobject5 = getLeftPort(l1);
                GraphicViewerObject graphicviewerobject7 = getRightPort(l1);
                if (l1 < getFirstVisibleIndex()) {
                    if (graphicviewerobject1 != null) {
                        graphicviewerobject1.setVisible(false);
                        graphicviewerobject1.setTopLeft(i, j);
                    }
                    if (graphicviewerobject3 != null) {
                        graphicviewerobject3.setVisible(false);
                        graphicviewerobject3.setTopLeft(i, j);
                    }
                    if (graphicviewerobject5 != null) {
                        graphicviewerobject5.setVisible(false);
                        graphicviewerobject5.setTopLeft(i, j);
                    }
                    if (graphicviewerobject7 != null) {
                        graphicviewerobject7.setVisible(false);
                        graphicviewerobject7.setTopLeft((i + k)
                                - graphicviewerobject7.getWidth(), j);
                    }
                    continue;
                }
                int j2 = getItemSize(l1);
                if (j1 + j2 <= l) {
                    layoutItem(l1, i, j + j1, k, j2);
                    j1 += j2;
                    j1 += Math.max(i1, getSpacing());
                    myLastItem = l1;
                    continue;
                }
                if (graphicviewerobject1 != null) {
                    graphicviewerobject1.setVisible(false);
                    graphicviewerobject1.setTopLeft(i, (j + l) - j2);
                }
                if (graphicviewerobject3 != null) {
                    graphicviewerobject3.setVisible(false);
                    graphicviewerobject3.setTopLeft(i, (j + l) - j2);
                }
                if (graphicviewerobject5 != null) {
                    graphicviewerobject5.setVisible(false);
                    graphicviewerobject5.setTopLeft(i, (j + l)
                            - graphicviewerobject5.getHeight());
                }
                if (graphicviewerobject7 != null) {
                    graphicviewerobject7.setVisible(false);
                    graphicviewerobject7.setTopLeft((i + k)
                            - graphicviewerobject7.getWidth(), (j + l)
                            - graphicviewerobject7.getHeight());
                }
                j1 = l + 1;
            }

            GraphicViewerScrollBar graphicviewerscrollbar = getScrollBar();
            if (graphicviewerscrollbar != null) {
                if (isScrollBarOnRight())
                    graphicviewerscrollbar
                            .setBoundingRect(
                                    (graphicviewerrectangle.getLeft() + graphicviewerrectangle
                                            .getWidth()) - myBarSize,
                                    graphicviewerrectangle.getTop(), myBarSize,
                                    graphicviewerrectangle.getHeight());
                else
                    graphicviewerscrollbar.setBoundingRect(
                            graphicviewerrectangle.getLeft(),
                            graphicviewerrectangle.getTop(), myBarSize,
                            graphicviewerrectangle.getHeight());
                updateScrollBar();
            }
        } else {
            int k1 = 0;
            for (int i2 = 0; i2 < getNumItems(); i2++) {
                GraphicViewerObject graphicviewerobject2 = getItem(i2);
                GraphicViewerObject graphicviewerobject4 = getIcon(i2);
                GraphicViewerObject graphicviewerobject6 = getLeftPort(i2);
                GraphicViewerObject graphicviewerobject8 = getRightPort(i2);
                if (i2 < getFirstVisibleIndex()) {
                    if (graphicviewerobject2 != null) {
                        graphicviewerobject2.setVisible(false);
                        graphicviewerobject2.setTopLeft(i, j);
                    }
                    if (graphicviewerobject4 != null) {
                        graphicviewerobject4.setVisible(false);
                        graphicviewerobject4.setTopLeft(i, j);
                    }
                    if (graphicviewerobject6 != null) {
                        graphicviewerobject6.setVisible(false);
                        graphicviewerobject6.setTopLeft(i, j);
                    }
                    if (graphicviewerobject8 != null) {
                        graphicviewerobject8.setVisible(false);
                        graphicviewerobject8.setTopLeft(i, (j + l)
                                - graphicviewerobject8.getHeight());
                    }
                    continue;
                }
                int k2 = getItemSize(i2);
                if (k1 + k2 <= k) {
                    layoutItem(i2, i + k1, j, k2, l);
                    k1 += k2;
                    k1 += Math.max(i1, getSpacing());
                    myLastItem = i2;
                    continue;
                }
                if (graphicviewerobject2 != null) {
                    graphicviewerobject2.setVisible(false);
                    graphicviewerobject2.setTopLeft((i + k) - k2, j);
                }
                if (graphicviewerobject4 != null) {
                    graphicviewerobject4.setVisible(false);
                    graphicviewerobject4.setTopLeft((i + k) - k2, j);
                }
                if (graphicviewerobject6 != null) {
                    graphicviewerobject6.setVisible(false);
                    graphicviewerobject6.setTopLeft((i + k)
                            - graphicviewerobject6.getWidth(), j);
                }
                if (graphicviewerobject8 != null) {
                    graphicviewerobject8.setVisible(false);
                    graphicviewerobject8.setTopLeft((i + k)
                            - graphicviewerobject8.getWidth(), (j + l)
                            - graphicviewerobject8.getHeight());
                }
                k1 = k + 1;
            }

            GraphicViewerScrollBar graphicviewerscrollbar1 = getScrollBar();
            if (graphicviewerscrollbar1 != null) {
                if (isScrollBarOnRight())
                    graphicviewerscrollbar1
                            .setBoundingRect(
                                    graphicviewerrectangle.getLeft(),
                                    (graphicviewerrectangle.getTop() + graphicviewerrectangle
                                            .getHeight()) - myBarSize,
                                    graphicviewerrectangle.getWidth(),
                                    myBarSize);
                else
                    graphicviewerscrollbar1.setBoundingRect(
                            graphicviewerrectangle.getLeft(),
                            graphicviewerrectangle.getTop(),
                            graphicviewerrectangle.getWidth(), myBarSize);
                updateScrollBar();
            }
        }
        setInitializing(false);
    }

    protected int getItemSize(int i) {
        int j = 0;
        GraphicViewerObject graphicviewerobject = getItem(i);
        if (graphicviewerobject != null)
            if (isVertical())
                j = graphicviewerobject.getHeight();
            else
                j = graphicviewerobject.getWidth();
        GraphicViewerObject graphicviewerobject1 = getIcon(i);
        if (graphicviewerobject1 != null)
            if (isVertical())
                j = Math.max(j, graphicviewerobject1.getHeight());
            else
                j = Math.max(j, graphicviewerobject1.getWidth());
        GraphicViewerObject graphicviewerobject2 = getLeftPort(i);
        if (graphicviewerobject2 != null)
            if (isVertical())
                j = Math.max(j, graphicviewerobject2.getHeight());
            else
                j = Math.max(j, graphicviewerobject2.getWidth());
        GraphicViewerObject graphicviewerobject3 = getRightPort(i);
        if (graphicviewerobject3 != null)
            if (isVertical())
                j = Math.max(j, graphicviewerobject3.getHeight());
            else
                j = Math.max(j, graphicviewerobject3.getWidth());
        return j;
    }

    protected void layoutItem(int i, int j, int k, int l, int i1) {
        GraphicViewerObject graphicviewerobject = getItem(i);
        GraphicViewerObject graphicviewerobject1 = getIcon(i);
        int j1 = 0;
        int k1 = 0;
        if (graphicviewerobject != null)
            if (isVertical())
                j1 = graphicviewerobject.getWidth();
            else
                j1 = graphicviewerobject.getHeight();
        if (graphicviewerobject1 != null)
            if (isVertical())
                k1 = graphicviewerobject1.getWidth();
            else
                k1 = graphicviewerobject1.getHeight();
        int l1 = 0;
        if (isVertical())
            l1 = l;
        else
            l1 = i1;
        int i2 = 0;
        int j2 = 0;
        int k2 = getIconSpacing();
        switch (getAlignment()) {
            case 0 : // '\0'
            default :
                i2 = 0;
                switch (getIconAlignment()) {
                    case 0 : // '\0'
                    default :
                        j2 = 0;
                        i2 = k1 + k2;
                        break;

                    case 1 : // '\001'
                        j2 = l1 / 2 - k1 / 2;
                        j2 = Math.max(j2, j1 + k2);
                        break;

                    case 2 : // '\002'
                        j2 = l1 - k1;
                        break;
                }
                break;

            case 1 : // '\001'
                i2 = l1 / 2 - j1 / 2;
                switch (getIconAlignment()) {
                    case 0 : // '\0'
                    default :
                        j2 = 0;
                        i2 = Math.max(i2 + k2, k1);
                        break;

                    case 1 : // '\001'
                        j2 = l1 / 2 - (k1 + j1 + k2) / 2;
                        i2 = j2 + k1 + k2;
                        break;

                    case 2 : // '\002'
                        j2 = l1 - k1;
                        i2 = Math.min(i2, l1 - k1 - j1 - k2);
                        break;
                }
                break;

            case 2 : // '\002'
                i2 = l1 - j1;
                switch (getIconAlignment()) {
                    case 0 : // '\0'
                    default :
                        j2 = 0;
                        break;

                    case 1 : // '\001'
                        j2 = l1 / 2 - k1 / 2;
                        j2 = Math.min(j2, l1 - j1 - k1 - k2);
                        break;

                    case 2 : // '\002'
                        j2 = l1 - k1;
                        i2 = j2 - j1 - k2;
                        break;
                }
                break;
        }
        if (graphicviewerobject != null) {
            graphicviewerobject.setVisible(true);
            if (isVertical())
                graphicviewerobject.setTopLeft(j + i2, (k + i1 / 2)
                        - graphicviewerobject.getHeight() / 2);
            else
                graphicviewerobject.setTopLeft((j + l / 2)
                        - graphicviewerobject.getWidth() / 2, k + i2);
        }
        if (graphicviewerobject1 != null) {
            graphicviewerobject1.setVisible(true);
            if (isVertical())
                graphicviewerobject1.setTopLeft(j + j2, (k + i1 / 2)
                        - graphicviewerobject1.getHeight() / 2);
            else
                graphicviewerobject1.setTopLeft((j + l / 2)
                        - graphicviewerobject1.getWidth() / 2, k + j2);
        }
        GraphicViewerRectangle graphicviewerrectangle = getRect();
        if (graphicviewerrectangle == null)
            return;
        int l2 = graphicviewerrectangle.getLeft();
        int i3 = graphicviewerrectangle.getTop();
        int j3 = graphicviewerrectangle.getWidth();
        int k3 = graphicviewerrectangle.getHeight();
        GraphicViewerObject graphicviewerobject2 = getLeftPort(i);
        if (graphicviewerobject2 != null) {
            graphicviewerobject2.setVisible(true);
            if (isVertical()) {
                int l3 = l2 - graphicviewerobject2.getWidth();
                int j4 = (k + i1 / 2) - graphicviewerobject2.getHeight() / 2;
                graphicviewerobject2.setTopLeft(l3, j4);
            } else {
                int i4 = (j + l / 2) - graphicviewerobject2.getWidth() / 2;
                int k4 = i3 - graphicviewerobject2.getHeight();
                graphicviewerobject2.setTopLeft(i4, k4);
            }
        }
        GraphicViewerObject graphicviewerobject3 = getRightPort(i);
        if (graphicviewerobject3 != null) {
            graphicviewerobject3.setVisible(true);
            if (isVertical()) {
                int l4 = l2 + j3;
                int j5 = (k + i1 / 2) - graphicviewerobject3.getHeight() / 2;
                graphicviewerobject3.setTopLeft(l4, j5);
            } else {
                int i5 = (j + l / 2) - graphicviewerobject3.getWidth() / 2;
                int k5 = i3 + k3;
                graphicviewerobject3.setTopLeft(i5, k5);
            }
        }
    }

    public void paint(Graphics2D graphics2d, GraphicViewerView graphicviewerview) {
        super.paint(graphics2d, graphicviewerview);
        int i = 0;
        if (getLinePen() != null)
            i = getLinePen().getWidth();
        if (i == 0)
            return;
        GraphicViewerRectangle graphicviewerrectangle = getRect();
        if (graphicviewerrectangle == null)
            return;
        Insets insets = getInsets();
        int j = graphicviewerrectangle.getLeft();
        int k = graphicviewerrectangle.getTop();
        int l = graphicviewerrectangle.getWidth();
        int i1 = graphicviewerrectangle.getHeight();
        int j1 = j + insets.left;
        int k1 = k + insets.top;
        int l1 = l - insets.left - insets.right;
        int i2 = i1 - insets.top - insets.bottom;
        int j2 = 0;
        if (isVertical())
            j2 = i2;
        else
            j2 = l1;
        int k2 = 0;
        for (int l2 = getFirstVisibleIndex(); l2 < getLastVisibleIndex(); l2++) {
            int i3 = getItemSize(l2);
            if (k2 + i3 > j2)
                continue;
            k2 += i3;
            int j3 = Math.max(i, getSpacing());
            if (k2 + j3 <= j2)
                if (isVertical())
                    GraphicViewerDrawable.drawLine(graphics2d, getLinePen(), j,
                            k1 + k2 + j3 / 2, j + l, k1 + k2 + j3 / 2);
                else
                    GraphicViewerDrawable.drawLine(graphics2d, getLinePen(), j1
                            + k2 + j3 / 2, k, j1 + k2 + j3 / 2, k + i1);
            k2 += j3;
        }

    }

    public void updateScrollBar() {
        GraphicViewerScrollBar graphicviewerscrollbar = getScrollBar();
        if (graphicviewerscrollbar != null) {
            graphicviewerscrollbar.setVertical(isVertical());
            if (getFirstVisibleIndex() == 0
                    && getLastVisibleIndex() == getNumItems() - 1) {
                graphicviewerscrollbar.setVisible(false);
            } else {
                graphicviewerscrollbar.setVisible(true);
                boolean flag = isInitializing();
                setInitializing(true);
                graphicviewerscrollbar.setValues(getFirstVisibleIndex(),
                        (getLastVisibleIndex() - getFirstVisibleIndex()) + 1,
                        0, getNumItems(), 1, Math.max(getLastVisibleIndex()
                                - getFirstVisibleIndex(), 1));
                setInitializing(flag);
            }
        }
    }

    public void update(int i, int j, Object obj) {
        if (i == 1003) {
            if (getScrollBar() != null && !isInitializing())
                setFirstVisibleIndex(getScrollBar().getValue());
        } else {
            super.update(i, j, obj);
        }
    }

    public void setVisible(boolean flag) {
        if (getScrollBar() != null)
            getScrollBar().setVisible(flag);
        super.setVisible(flag);
    }

    public GraphicViewerObject redirectSelection() {
        return getRect();
    }

    public Dimension getMinimumRectSize() {
        Dimension dimension = getMaxItemSize();
        Insets insets = getInsets();
        int i = dimension.width + insets.left + insets.right;
        int j = dimension.height + insets.top + insets.bottom;
        return new Dimension(i, j);
    }

    public Dimension getMinimumSize() {
        Dimension dimension = getMinimumRectSize();
        int i = getMaxPortWidth(true);
        int j = getMaxPortWidth(false);
        if (isVertical())
            return new Dimension(dimension.width + i + j, dimension.height);
        else
            return new Dimension(dimension.width, dimension.height + i + j);
    }

    public void setBoundingRect(int i, int j, int k, int l) {
        Dimension dimension = getMinimumSize();
        super.setBoundingRect(i, j, Math.max(k, dimension.width),
                Math.max(l, dimension.height));
    }

    protected void rescaleChildren(Rectangle rectangle) {
        if (getRect() != null) {
            Rectangle rectangle1 = getBoundingRect();
            int i = getMaxPortWidth(true);
            int j = getMaxPortWidth(false);
            if (isVertical())
                getRect().setBoundingRect(rectangle1.x + i, rectangle1.y,
                        rectangle1.width - i - j, rectangle1.height);
            else
                getRect().setBoundingRect(rectangle1.x, rectangle1.y + i,
                        rectangle1.width, rectangle1.height - i - j);
        }
    }

    protected boolean geometryChangeChild(
            GraphicViewerObject graphicviewerobject, Rectangle rectangle) {
        if (isInitializing())
            return false;
        int i = -1;
        if (graphicviewerobject != getRect()
                && (graphicviewerobject.getWidth() != rectangle.width || graphicviewerobject
                        .getHeight() != rectangle.height)
                && (i = findItem(graphicviewerobject)) >= 0) {
            adjustMaxItemSize(graphicviewerobject, rectangle.width,
                    rectangle.height, getIcon(i));
            return super.geometryChangeChild(graphicviewerobject, rectangle);
        }
        if (graphicviewerobject == getRect())
            return super.geometryChangeChild(graphicviewerobject, rectangle);
        else
            return false;
    }

    public void adjustMaxItemSize(GraphicViewerObject graphicviewerobject,
            int i, int j, GraphicViewerObject graphicviewerobject1) {
        int k = -1;
        int l = -1;
        if (graphicviewerobject != null) {
            k = graphicviewerobject.getWidth();
            l = graphicviewerobject.getHeight();
        }
        int i1 = i;
        int j1 = j;
        int k1 = k;
        int l1 = l;
        if (graphicviewerobject1 != null) {
            i1 += graphicviewerobject1.getWidth();
            j1 += graphicviewerobject1.getHeight();
            k1 += graphicviewerobject1.getWidth();
            l1 += graphicviewerobject1.getHeight();
            int i2 = getIconSpacing();
            if (isVertical()) {
                i1 += i2;
                k1 += i2;
            } else {
                j1 += i2;
                l1 += i2;
            }
        }
        Insets insets = getInsets();
        if (myMaxItemSize.width > -1)
            if (k > i && k1 > myMaxItemSize.width) {
                myMaxItemSize.width = k1;
                int j2 = k1 + insets.left + insets.right;
                if (getRect().getWidth() < j2)
                    getRect().setWidth(j2);
            } else if (k < i && i1 == myMaxItemSize.width)
                myMaxItemSize.width = -1;
        if (myMaxItemSize.height > -1)
            if (l > j && l1 > myMaxItemSize.height) {
                myMaxItemSize.height = l1;
                int k2 = l1 + insets.top + insets.bottom;
                if (getRect().getHeight() < k2)
                    getRect().setHeight(k2);
            } else if (l < j && j1 == myMaxItemSize.height)
                myMaxItemSize.height = -1;
    }

    public Dimension getMaxItemSize() {
        if (myMaxItemSize.width < 0 || myMaxItemSize.height < 0) {
            myMaxItemSize.width = -1;
            myMaxItemSize.height = -1;
            for (int i = 0; i < myVector.size(); i++) {
                GraphicViewerObject graphicviewerobject = (GraphicViewerObject) myVector
                        .get(i);
                int j = 0;
                int k = 0;
                if (graphicviewerobject != null) {
                    j = graphicviewerobject.getWidth();
                    k = graphicviewerobject.getHeight();
                    if (j > myMaxItemSize.width)
                        myMaxItemSize.width = j;
                    if (k > myMaxItemSize.height)
                        myMaxItemSize.height = k;
                }
                GraphicViewerObject graphicviewerobject1 = null;
                if (i < myIcons.size())
                    graphicviewerobject1 = (GraphicViewerObject) myIcons.get(i);
                int l = 0;
                int i1 = 0;
                if (graphicviewerobject1 != null) {
                    l = graphicviewerobject1.getWidth();
                    i1 = graphicviewerobject1.getHeight();
                    if (l > myMaxItemSize.width)
                        myMaxItemSize.width = l;
                    if (i1 > myMaxItemSize.height)
                        myMaxItemSize.height = i1;
                }
                if (graphicviewerobject == null || graphicviewerobject1 == null)
                    continue;
                if (isVertical()) {
                    int j1 = j + l + getIconSpacing();
                    if (j1 > myMaxItemSize.width)
                        myMaxItemSize.width = j1;
                    continue;
                }
                int k1 = k + i1 + getIconSpacing();
                if (k1 > myMaxItemSize.height)
                    myMaxItemSize.height = k1;
            }

        }
        return myMaxItemSize;
    }

    public int getMaxPortWidth(boolean flag) {
        int i = 0;
        ArrayList<?> arraylist = flag ? myLeftPorts : myRightPorts;
        for (int j = 0; j < arraylist.size(); j++) {
            GraphicViewerObject graphicviewerobject = (GraphicViewerObject) arraylist
                    .get(j);
            if (graphicviewerobject == null)
                continue;
            if (isVertical())
                i = Math.max(i, graphicviewerobject.getWidth());
            else
                i = Math.max(i, graphicviewerobject.getHeight());
        }

        return i;
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 75565 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isVertical());
                return;

            case 75566 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isScrollBarOnRight());
                return;

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

            case 75570 :
                graphicviewerdocumentchangededit.setNewValueInt(getAlignment());
                return;

            case 75571 :
                graphicviewerdocumentchangededit
                        .setNewValueInt(getFirstVisibleIndex());
                return;

            case 75572 :
                graphicviewerdocumentchangededit
                        .setNewValue(getItem(graphicviewerdocumentchangededit
                                .getOldValueInt()));
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

            case 75577 :
                graphicviewerdocumentchangededit
                        .setNewValue(getIcon(graphicviewerdocumentchangededit
                                .getOldValueInt()));
                return;

            case 75578 :
                graphicviewerdocumentchangededit
                        .setNewValueInt(getIconAlignment());
                return;

            case 75579 :
                graphicviewerdocumentchangededit
                        .setNewValueInt(getIconSpacing());
                return;
        }
        super.copyNewValueForRedo(graphicviewerdocumentchangededit);
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 75565 :
                myVertical = graphicviewerdocumentchangededit
                        .getValueBoolean(flag);
                return;

            case 75566 :
                myScrollBarOnRight = graphicviewerdocumentchangededit
                        .getValueBoolean(flag);
                return;

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

            case 75570 :
                myAlignment = graphicviewerdocumentchangededit
                        .getValueInt(flag);
                return;

            case 75571 :
                myFirstItem = graphicviewerdocumentchangededit
                        .getValueInt(flag);
                return;

            case 75572 :
                setItem(graphicviewerdocumentchangededit.getOldValueInt(),
                        (GraphicViewerObject) graphicviewerdocumentchangededit
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
                            agraphicviewerobject[2], agraphicviewerobject[3]);
                }
                return;

            case 75576 :
                if (flag) {
                    GraphicViewerObject agraphicviewerobject1[] = (GraphicViewerObject[]) graphicviewerdocumentchangededit
                            .getOldValue();
                    insertItem(
                            graphicviewerdocumentchangededit.getOldValueInt(),
                            agraphicviewerobject1[0], agraphicviewerobject1[1],
                            agraphicviewerobject1[2], agraphicviewerobject1[3]);
                } else {
                    removeItem(graphicviewerdocumentchangededit
                            .getOldValueInt());
                }
                return;

            case 75577 :
                setIcon(graphicviewerdocumentchangededit.getOldValueInt(),
                        (GraphicViewerObject) graphicviewerdocumentchangededit
                                .getValue(flag));
                return;

            case 75578 :
                myIconAlignment = graphicviewerdocumentchangededit
                        .getValueInt(flag);
                return;

            case 75579 :
                myIconSpacing = graphicviewerdocumentchangededit
                        .getValueInt(flag);
                return;
        }
        super.changeValue(graphicviewerdocumentchangededit, flag);
    }

    public void SVGUpdateReference(String s, Object obj) {
        super.SVGUpdateReference(s, obj);
        if (s.equals("bar"))
            myBar = (GraphicViewerScrollBar) obj;
        else if (s.equals("rect"))
            myRect = (GraphicViewerRectangle) obj;
        else if (s.equals("vectorelements"))
            myVector.add(obj);
        else if (s.equals("icons"))
            myIcons.add(obj);
        else if (s.equals("leftports"))
            myLeftPorts.add(obj);
        else if (s.equals("rightports"))
            myRightPorts.add(obj);
        else if (s.equals("linepen"))
            myLinePen = (GraphicViewerPen) obj;
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.ListArea",
                            domelement);
            domelement1
                    .setAttribute("alignment", Integer.toString(myAlignment));
            domelement1
                    .setAttribute("firstitem", Integer.toString(myFirstItem));
            domelement1.setAttribute("iconalignment",
                    Integer.toString(myIconAlignment));
            domelement1.setAttribute("iconspacing",
                    Integer.toString(myIconSpacing));
            domelement1.setAttribute("lastitem", Integer.toString(myLastItem));
            domelement1.setAttribute("scrollbaronright", myScrollBarOnRight
                    ? "true"
                    : "false");
            domelement1.setAttribute("spacing", Integer.toString(mySpacing));
            domelement1.setAttribute("vertical", myVertical ? "true" : "false");
            domelement1.setAttribute("insetleft",
                    Integer.toString(myInsets.left));
            domelement1.setAttribute("insetright",
                    Integer.toString(myInsets.right));
            domelement1
                    .setAttribute("insettop", Integer.toString(myInsets.top));
            domelement1.setAttribute("insetbottom",
                    Integer.toString(myInsets.bottom));
            if (myBar != null)
                domdoc.registerReferencingNode(domelement1, "bar", myBar);
            if (myRect != null)
                domdoc.registerReferencingNode(domelement1, "rect", myRect);
            for (int i = 0; i < myVector.size(); i++)
                domdoc.registerReferencingNode(domelement1, "vectorelements",
                        (GraphicViewerObject) myVector.get(i));

            for (int j = 0; j < myIcons.size(); j++)
                domdoc.registerReferencingNode(domelement1, "icons",
                        (GraphicViewerObject) myIcons.get(j));

            for (int k = 0; k < myLeftPorts.size(); k++)
                domdoc.registerReferencingNode(domelement1, "leftports",
                        (GraphicViewerObject) myLeftPorts.get(k));

            for (int l = 0; l < myRightPorts.size(); l++)
                domdoc.registerReferencingNode(domelement1, "rightports",
                        (GraphicViewerObject) myRightPorts.get(l));

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
            myAlignment = Integer.parseInt(domelement1
                    .getAttribute("alignment"));
            myFirstItem = Integer.parseInt(domelement1
                    .getAttribute("firstitem"));
            myIconAlignment = Integer.parseInt(domelement1
                    .getAttribute("iconalignment"));
            myIconSpacing = Integer.parseInt(domelement1
                    .getAttribute("iconspacing"));
            myLastItem = Integer.parseInt(domelement1.getAttribute("lastitem"));
            myScrollBarOnRight = domelement1.getAttribute("scrollbaronright")
                    .equals("true");
            mySpacing = Integer.parseInt(domelement1.getAttribute("spacing"));
            myVertical = domelement1.getAttribute("vertical").equals("true");
            myInsets.left = Integer.parseInt(domelement1
                    .getAttribute("insetleft"));
            myInsets.right = Integer.parseInt(domelement1
                    .getAttribute("insetright"));
            myInsets.top = Integer.parseInt(domelement1
                    .getAttribute("insettop"));
            myInsets.bottom = Integer.parseInt(domelement1
                    .getAttribute("insetbottom"));
            String s = domelement1.getAttribute("bar");
            domdoc.registerReferencingObject(this, "bar", s);
            String s1 = domelement1.getAttribute("rect");
            domdoc.registerReferencingObject(this, "rect", s1);
            String s4;
            for (String s2 = domelement1.getAttribute("vectorelements"); s2
                    .length() > 0; domdoc.registerReferencingObject(this,
                    "vectorelements", s4)) {
                int i = s2.indexOf(" ");
                if (i == -1)
                    i = s2.length();
                s4 = s2.substring(0, i);
                if (i >= s2.length())
                    s2 = "";
                else
                    s2 = s2.substring(i + 1);
            }

            String s6;
            for (String s3 = domelement1.getAttribute("icons"); s3.length() > 0; domdoc
                    .registerReferencingObject(this, "icons", s6)) {
                int j = s3.indexOf(" ");
                if (j == -1)
                    j = s3.length();
                s6 = s3.substring(0, j);
                if (j >= s3.length())
                    s3 = "";
                else
                    s3 = s3.substring(j + 1);
            }

            String s8;
            for (String s5 = domelement1.getAttribute("leftports"); s5.length() > 0; domdoc
                    .registerReferencingObject(this, "leftports", s8)) {
                int k = s5.indexOf(" ");
                if (k == -1)
                    k = s5.length();
                s8 = s5.substring(0, k);
                if (k >= s5.length())
                    s5 = "";
                else
                    s5 = s5.substring(k + 1);
            }

            String s10;
            for (String s7 = domelement1.getAttribute("rightports"); s7
                    .length() > 0; domdoc.registerReferencingObject(this,
                    "rightports", s10)) {
                int l = s7.indexOf(" ");
                if (l == -1)
                    l = s7.length();
                s10 = s7.substring(0, l);
                if (l >= s7.length())
                    s7 = "";
                else
                    s7 = s7.substring(l + 1);
            }

            if (domelement1.getAttribute("embeddedlinepen").equals("true"))
                domdoc.SVGTraverseChildren(graphicviewerdocument, domelement1,
                        null, false);
            String s9 = domelement1.getAttribute("linepen");
            domdoc.registerReferencingObject(this, "linepen", s9);
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        }
        return domelement.getNextSibling();
    }

    public static final int VerticalChanged = 0x1272d;
    public static final int ScrollBarOnRightChanged = 0x1272e;
    public static final int LinePenChanged = 0x1272f;
    public static final int InsetsChanged = 0x12730;
    public static final int SpacingChanged = 0x12731;
    public static final int AlignmentChanged = 0x12732;
    public static final int FirstVisibleIndexChanged = 0x12733;
    public static final int ItemChanged = 0x12734;
    public static final int LeftPortChanged = 0x12735;
    public static final int RightPortChanged = 0x12736;
    public static final int ItemInsertedChanged = 0x12737;
    public static final int ItemRemovedChanged = 0x12738;
    public static final int IconChanged = 0x12739;
    public static final int IconAlignmentChanged = 0x1273a;
    public static final int IconSpacingChanged = 0x1273b;
    public static final int ALIGN_LEFT = 0;
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_RIGHT = 2;
    private static int myBarSize = 14;
    private ArrayList<Object> myVector;
    private ArrayList<Object> myIcons;
    private ArrayList<Object> myLeftPorts;
    private ArrayList<Object> myRightPorts;
    private GraphicViewerRectangle myRect;
    private GraphicViewerScrollBar myBar;
    private boolean myVertical;
    private boolean myScrollBarOnRight;
    private int myAlignment;
    private int myIconAlignment;
    private int myFirstItem;
    private int myLastItem;
    private Insets myInsets;
    private int mySpacing;
    private GraphicViewerPen myLinePen;
    private int myIconSpacing;
    private transient Dimension myMaxItemSize;
}