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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GeneralNode extends GraphicViewerNode {

    private static final long serialVersionUID = -5172230555329559218L;

    @SuppressWarnings("rawtypes")
    public GeneralNode() {
        myTopLabel = null;
        myBottomLabel = null;
        myIcon = null;
        myLeftPorts = new ArrayList();
        myRightPorts = new ArrayList();
    }

    public void initialize(Point point, Dimension dimension,
            GraphicViewerObject graphicviewerobject, String s, String s1,
            int i, int j) {
        setInitializing(true);
        setDraggable(true);
        setResizable(false);
        set4ResizeHandles(true);
        myIcon = graphicviewerobject;
        if (myIcon != null) {
            myIcon.setBoundingRect(point, dimension);
            myIcon.setSelectable(false);
            addObjectAtHead(myIcon);
        }
        if (s != null)
            myTopLabel = new GeneralNodeLabel(s, this);
        if (s1 != null)
            myBottomLabel = new GeneralNodeLabel(s1, this);
        for (int k = 0; k < i; k++) {
            String s2 = Integer.toString(k);
            GeneralNodePort generalnodeport = new GeneralNodePort(true, s2,
                    this);
            @SuppressWarnings("unused")
            GeneralNodePortLabel generalnodeportlabel = new GeneralNodePortLabel(
                    s2, generalnodeport);
            addLeftPort(generalnodeport);
        }

        for (int l = 0; l < j; l++) {
            String s3 = Integer.toString(l);
            GeneralNodePort generalnodeport1 = new GeneralNodePort(false, s3,
                    this);
            @SuppressWarnings("unused")
            GeneralNodePortLabel generalnodeportlabel1 = new GeneralNodePortLabel(
                    s3, generalnodeport1);
            addRightPort(generalnodeport1);
        }

        setInitializing(false);
        layoutChildren(null);
        setTopLeft(point);
    }

    @SuppressWarnings("unchecked")
    protected void copyChildren(GraphicViewerArea graphicviewerarea,
            GraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GeneralNode generalnode = (GeneralNode) graphicviewerarea;
        super.copyChildren(graphicviewerarea, graphicviewercopyenvironment);
        generalnode.myIcon = (GraphicViewerObject) graphicviewercopyenvironment
                .get(myIcon);
        generalnode.myTopLabel = (GraphicViewerText) graphicviewercopyenvironment
                .get(myTopLabel);
        generalnode.myBottomLabel = (GraphicViewerText) graphicviewercopyenvironment
                .get(myBottomLabel);
        for (int i = 0; i < myLeftPorts.size(); i++) {
            GeneralNodePort generalnodeport = (GeneralNodePort) myLeftPorts
                    .get(i);
            if (generalnodeport == null)
                continue;
            GeneralNodePort generalnodeport2 = (GeneralNodePort) graphicviewercopyenvironment
                    .get(generalnodeport);
            if (generalnodeport2 == null)
                continue;
            generalnode.myLeftPorts.add(generalnodeport2);
            generalnodeport2.setSideIndex(true,
                    generalnode.myLeftPorts.size() - 1);
            GeneralNodePortLabel generalnodeportlabel = generalnodeport
                    .getLabel();
            if (generalnodeportlabel == null)
                continue;
            GeneralNodePortLabel generalnodeportlabel2 = (GeneralNodePortLabel) graphicviewercopyenvironment
                    .get(generalnodeportlabel);
            if (generalnodeportlabel2 != null)
                generalnodeport2.setLabel(generalnodeportlabel2);
        }

        for (int j = 0; j < myRightPorts.size(); j++) {
            GeneralNodePort generalnodeport1 = (GeneralNodePort) myRightPorts
                    .get(j);
            if (generalnodeport1 == null)
                continue;
            GeneralNodePort generalnodeport3 = (GeneralNodePort) graphicviewercopyenvironment
                    .get(generalnodeport1);
            if (generalnodeport3 == null)
                continue;
            generalnode.myRightPorts.add(generalnodeport3);
            generalnodeport3.setSideIndex(false,
                    generalnode.myRightPorts.size() - 1);
            GeneralNodePortLabel generalnodeportlabel1 = generalnodeport1
                    .getLabel();
            if (generalnodeportlabel1 == null)
                continue;
            GeneralNodePortLabel generalnodeportlabel3 = (GeneralNodePortLabel) graphicviewercopyenvironment
                    .get(generalnodeportlabel1);
            if (generalnodeportlabel3 != null)
                generalnodeport3.setLabel(generalnodeportlabel3);
        }

    }

    public GraphicViewerObject removeObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        GraphicViewerObject graphicviewerobject = super
                .removeObjectAtPos(graphicviewerlistposition);
        if (graphicviewerobject == myTopLabel)
            myTopLabel = null;
        else if (graphicviewerobject == myBottomLabel)
            myBottomLabel = null;
        else if (graphicviewerobject == myIcon)
            myIcon = null;
        return graphicviewerobject;
    }

    public void layoutChildren(GraphicViewerObject graphicviewerobject) {
        if (isInitializing())
            return;
        setInitializing(true);
        GraphicViewerObject graphicviewerobject1 = getIcon();
        GraphicViewerText graphicviewertext = getTopLabel();
        GraphicViewerText graphicviewertext1 = getBottomLabel();
        int i = getNumLeftPorts();
        int j = 0;
        int k = 0;
        for (int l = 0; l < i; l++) {
            GeneralNodePort generalnodeport = getLeftPort(l);
            if (generalnodeport.isVisible()) {
                j += getPortAndLabelHeight(generalnodeport);
                k = Math.max(k, getPortAndLabelWidth(generalnodeport));
            }
        }

        if (graphicviewerobject1 != null) {
            Dimension dimension = getMinimumIconSize();
            int j1 = Math.max(dimension.width, graphicviewerobject1.getWidth());
            int l1 = Math.max(dimension.height,
                    graphicviewerobject1.getHeight());
            graphicviewerobject1.setBoundingRect(
                    graphicviewerobject1.getLeft()
                            - (j1 - graphicviewerobject1.getWidth()) / 2,
                    graphicviewerobject1.getTop()
                            - (l1 - graphicviewerobject1.getHeight()) / 2, j1,
                    l1);
        }
        int i1 = 0;
        int k1 = 0;
        if (graphicviewerobject1 != null)
            i1 = graphicviewerobject1.getLeft();
        else
            i1 = getLeft();
        if (graphicviewerobject1 != null)
            k1 = graphicviewerobject1.getTop();
        else
            k1 = getTop()
                    + (graphicviewertext == null ? 0 : graphicviewertext
                            .getHeight());
        if (graphicviewerobject1 != null
                && graphicviewerobject1.getHeight() > j)
            k1 += (graphicviewerobject1.getHeight() - j) / 2;
        int i2 = 0;
        for (int j2 = 0; j2 < i; j2++) {
            GeneralNodePort generalnodeport1 = getLeftPort(j2);
            if (generalnodeport1.isVisible()) {
                i2 += getPortAndLabelHeight(generalnodeport1) / 2;
                generalnodeport1.setSpotLocation(4, i1, k1 + i2);
                generalnodeport1.layoutLabel();
                i2 += getPortAndLabelHeight(generalnodeport1) / 2;
            }
        }

        i = getNumRightPorts();
        j = 0;
        for (int k2 = 0; k2 < i; k2++) {
            GeneralNodePort generalnodeport2 = getRightPort(k2);
            if (generalnodeport2.isVisible())
                j += getPortAndLabelHeight(generalnodeport2);
        }

        if (graphicviewerobject1 != null)
            i1 = graphicviewerobject1.getLeft()
                    + graphicviewerobject1.getWidth();
        else
            i1 = getLeft() + getWidth();
        if (graphicviewerobject1 != null)
            k1 = graphicviewerobject1.getTop();
        else
            k1 = getTop()
                    + (graphicviewertext == null ? 0 : graphicviewertext
                            .getHeight());
        if (graphicviewerobject1 != null
                && graphicviewerobject1.getHeight() > j)
            k1 += (graphicviewerobject1.getHeight() - j) / 2;
        i2 = 0;
        for (int l2 = 0; l2 < i; l2++) {
            GeneralNodePort generalnodeport3 = getRightPort(l2);
            if (generalnodeport3.isVisible()) {
                i2 += getPortAndLabelHeight(generalnodeport3) / 2;
                generalnodeport3.setSpotLocation(8, i1, k1 + i2);
                generalnodeport3.layoutLabel();
                i2 += getPortAndLabelHeight(generalnodeport3) / 2;
            }
        }

        if (graphicviewertext != null)
            if (graphicviewerobject1 != null)
                graphicviewertext.setSpotLocation(6, graphicviewerobject1, 2);
            else
                graphicviewertext.setSpotLocation(2, this, 2);
        if (graphicviewertext1 != null)
            if (graphicviewerobject1 != null)
                graphicviewertext1.setSpotLocation(2, graphicviewerobject1, 6);
            else
                graphicviewertext1.setSpotLocation(6, this, 6);
        setInitializing(false);
    }

    public void rescaleChildren(Rectangle rectangle) {
        if (getIcon() != null && getIcon().isVisible()) {
            int i = getIcon().getWidth();
            int j = getIcon().getHeight();
            if (i <= 0)
                i = 1;
            double d = (double) j / (double) i;
            Dimension dimension = getMinimumSizeWithIconSize(0, 0);
            int k = getWidth() - dimension.width;
            int l = getHeight();
            if (getTopLabel() != null && getTopLabel().isVisible())
                l -= getTopLabel().getHeight();
            if (getBottomLabel() != null && getBottomLabel().isVisible())
                l -= getBottomLabel().getHeight();
            Dimension dimension1 = getMinimumIconSize();
            k = Math.max(k, dimension1.width);
            l = Math.max(l, dimension1.height);
            double d1 = (double) l / (double) k;
            if (d < d1)
                l = (int) Math.rint(d * (double) k);
            else
                k = (int) Math.rint((double) l / d);
            getIcon().setSize(k, l);
        }
    }

    public int getPortAndLabelWidth(GeneralNodePort generalnodeport) {
        if (!generalnodeport.isVisible())
            return 0;
        GeneralNodePortLabel generalnodeportlabel = generalnodeport.getLabel();
        if (generalnodeportlabel != null && generalnodeportlabel.isVisible())
            return generalnodeport.getWidth()
                    + generalnodeport.getLabelSpacing()
                    + generalnodeportlabel.getWidth();
        else
            return generalnodeport.getWidth();
    }

    public int getPortAndLabelHeight(GeneralNodePort generalnodeport) {
        if (!generalnodeport.isVisible())
            return 0;
        GeneralNodePortLabel generalnodeportlabel = generalnodeport.getLabel();
        if (generalnodeportlabel != null && generalnodeportlabel.isVisible())
            return Math.max(generalnodeport.getHeight(),
                    generalnodeportlabel.getHeight());
        else
            return generalnodeport.getHeight();
    }

    public Dimension getMinimumSizeWithIconSize(int i, int j) {
        int k = getNumLeftPorts();
        int l = 0;
        int i1 = 0;
        for (int j1 = 0; j1 < k; j1++) {
            GeneralNodePort generalnodeport = getLeftPort(j1);
            l = Math.max(l, getPortAndLabelWidth(generalnodeport));
            i1 += getPortAndLabelHeight(generalnodeport);
        }

        i += l;
        j = Math.max(j, i1);
        k = getNumRightPorts();
        l = 0;
        i1 = 0;
        for (int k1 = 0; k1 < k; k1++) {
            GeneralNodePort generalnodeport1 = getRightPort(k1);
            l = Math.max(l, getPortAndLabelWidth(generalnodeport1));
            i1 += getPortAndLabelHeight(generalnodeport1);
        }

        i += l;
        j = Math.max(j, i1);
        @SuppressWarnings("unused")
        boolean flag = false;
        GraphicViewerText graphicviewertext = getTopLabel();
        if (graphicviewertext != null && graphicviewertext.isVisible()) {
            i = Math.max(i, graphicviewertext.getWidth());
            j += graphicviewertext.getHeight();
        }
        graphicviewertext = getBottomLabel();
        if (graphicviewertext != null && graphicviewertext.isVisible()) {
            i = Math.max(i, graphicviewertext.getWidth());
            j += graphicviewertext.getHeight();
        }
        return new Dimension(i, j);
    }

    public Dimension getMinimumIconSize() {
        return new Dimension(20, 20);
    }

    public Dimension getMinimumSize() {
        Dimension dimension = getMinimumIconSize();
        int i = dimension.width;
        int j = dimension.height;
        return getMinimumSizeWithIconSize(i, j);
    }

    public void setBoundingRect(int i, int j, int k, int l) {
        Dimension dimension = getMinimumSize();
        super.setBoundingRect(i, j, Math.max(k, dimension.width),
                Math.max(l, dimension.height));
    }

    protected Rectangle handleResize(Graphics2D graphics2d,
            GraphicViewerView graphicviewerview, Rectangle rectangle,
            Point point, int i, int j, int k, int l) {
        Dimension dimension = getMinimumSize();
        Rectangle rectangle1 = super.handleResize(graphics2d,
                graphicviewerview, rectangle, point, i, j,
                Math.max(k, dimension.width), Math.max(l, dimension.height));
        if (j == 2)
            setBoundingRect(rectangle1);
        return null;
    }

    @SuppressWarnings("unchecked")
    public void SVGUpdateReference(String s, Object obj) {
        super.SVGUpdateReference(s, obj);
        if (s.equals("toplabel"))
            myTopLabel = (GraphicViewerText) obj;
        else if (s.equals("bottomlabel"))
            myBottomLabel = (GraphicViewerText) obj;
        else if (s.equals("icon"))
            myIcon = (GraphicViewerObject) obj;
        else if (s.equals("leftports"))
            myLeftPorts.add(obj);
        else if (s.equals("rightports"))
            myRightPorts.add(obj);
    }

    public void SVGWriteObject(DomDoc domdoc, DomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            DomElement domelement1 = domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphicviewer.GeneralNode", domelement);
            if (myTopLabel != null)
                domdoc.registerReferencingNode(domelement1, "toplabel",
                        myTopLabel);
            if (myBottomLabel != null)
                domdoc.registerReferencingNode(domelement1, "bottomlabel",
                        myBottomLabel);
            if (myIcon != null)
                domdoc.registerReferencingNode(domelement1, "icon", myIcon);
            for (int i = 0; i < myLeftPorts.size(); i++)
                domdoc.registerReferencingNode(domelement1, "leftports",
                        (GeneralNodePort) myLeftPorts.get(i));

            for (int j = 0; j < myRightPorts.size(); j++)
                domdoc.registerReferencingNode(domelement1, "rightports",
                        (GeneralNodePort) myRightPorts.get(j));

        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public DomNode SVGReadObject(DomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, DomElement domelement,
            DomElement domelement1) {
        if (domelement1 != null) {
            String s = domelement1.getAttribute("toplabel");
            domdoc.registerReferencingObject(this, "toplabel", s);
            String s1 = domelement1.getAttribute("bottomlabel");
            domdoc.registerReferencingObject(this, "bottomlabel", s1);
            String s2 = domelement1.getAttribute("icon");
            domdoc.registerReferencingObject(this, "icon", s2);
            String s5;
            for (String s3 = domelement1.getAttribute("leftports"); s3.length() > 0; domdoc
                    .registerReferencingObject(this, "leftports", s5)) {
                int i = s3.indexOf(" ");
                if (i == -1)
                    i = s3.length();
                s5 = s3.substring(0, i);
                if (i >= s3.length())
                    s3 = "";
                else
                    s3 = s3.substring(i + 1);
            }

            String s6;
            for (String s4 = domelement1.getAttribute("rightports"); s4
                    .length() > 0; domdoc.registerReferencingObject(this,
                    "rightports", s6)) {
                int j = s4.indexOf(" ");
                if (j == -1)
                    j = s4.length();
                s6 = s4.substring(0, j);
                if (j >= s4.length())
                    s4 = "";
                else
                    s4 = s4.substring(j + 1);
            }

            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        }
        return domelement.getNextSibling();
    }

    public GraphicViewerText getTopLabel() {
        return myTopLabel;
    }

    public GraphicViewerText getBottomLabel() {
        return myBottomLabel;
    }

    public GraphicViewerObject getIcon() {
        return myIcon;
    }

    public int getNumLeftPorts() {
        return myLeftPorts.size();
    }

    public int getNumRightPorts() {
        return myRightPorts.size();
    }

    public GeneralNodePort getLeftPort(int i) {
        if (i < 0 || i >= myLeftPorts.size())
            return null;
        else
            return (GeneralNodePort) myLeftPorts.get(i);
    }

    public GeneralNodePort getRightPort(int i) {
        if (i < 0 || i >= myRightPorts.size())
            return null;
        else
            return (GeneralNodePort) myRightPorts.get(i);
    }

    void initializePort(GeneralNodePort generalnodeport) {
        if (generalnodeport == null)
            return;
        generalnodeport.setSelectable(false);
        generalnodeport.setDraggable(false);
        generalnodeport.setResizable(false);
        if (generalnodeport.getParent() == null) {
            addObjectAtTail(generalnodeport);
            if (generalnodeport.getLabel() != null)
                addObjectAtTail(generalnodeport.getLabel());
        }
    }

    public final void addLeftPort(GeneralNodePort generalnodeport) {
        insertLeftPort(getNumLeftPorts(), generalnodeport);
    }

    public final void addRightPort(GeneralNodePort generalnodeport) {
        insertRightPort(getNumRightPorts(), generalnodeport);
    }

    @SuppressWarnings("unchecked")
    public void insertLeftPort(int i, GeneralNodePort generalnodeport) {
        if (generalnodeport == null)
            return;
        if (i < 0)
            return;
        if (i < myLeftPorts.size()) {
            myLeftPorts.add(i, generalnodeport);
            generalnodeport.setSideIndex(true, i);
        } else {
            myLeftPorts.add(generalnodeport);
            generalnodeport.setSideIndex(true, myLeftPorts.size() - 1);
        }
        initializePort(generalnodeport);
        layoutChildren(generalnodeport);
        update(0x12719, -(i + 1), generalnodeport);
    }

    @SuppressWarnings("unchecked")
    public void insertRightPort(int i, GeneralNodePort generalnodeport) {
        if (generalnodeport == null)
            return;
        if (i < 0)
            return;
        if (i < myRightPorts.size()) {
            myRightPorts.add(i, generalnodeport);
            generalnodeport.setSideIndex(false, i);
        } else {
            myRightPorts.add(generalnodeport);
            generalnodeport.setSideIndex(false, myRightPorts.size() - 1);
        }
        initializePort(generalnodeport);
        layoutChildren(generalnodeport);
        update(0x12719, i, generalnodeport);
    }

    void deletePort(GeneralNodePort generalnodeport) {
        if (generalnodeport != null) {
            if (generalnodeport.getLabel() != null)
                removeObject(generalnodeport.getLabel());
            removeObject(generalnodeport);
            generalnodeport.setSideIndex(generalnodeport.isOnLeftSide(), -1);
        }
    }

    public void removeLeftPort(int i) {
        if (i < 0 || i >= myLeftPorts.size()) {
            return;
        } else {
            GeneralNodePort generalnodeport = (GeneralNodePort) myLeftPorts
                    .remove(i);
            deletePort(generalnodeport);
            layoutChildren(generalnodeport);
            update(0x1271a, -(i + 1), generalnodeport);
            return;
        }
    }

    public void removeRightPort(int i) {
        if (i < 0 || i >= myRightPorts.size()) {
            return;
        } else {
            GeneralNodePort generalnodeport = (GeneralNodePort) myRightPorts
                    .remove(i);
            deletePort(generalnodeport);
            layoutChildren(generalnodeport);
            update(0x1271a, i, generalnodeport);
            return;
        }
    }

    @SuppressWarnings("unchecked")
    public void setLeftPort(int i, GeneralNodePort generalnodeport) {
        GeneralNodePort generalnodeport1 = getLeftPort(i);
        if (generalnodeport1 != generalnodeport) {
            if (generalnodeport1 != null) {
                if (generalnodeport != null)
                    generalnodeport.setBoundingRect(generalnodeport1
                            .getBoundingRect());
                removeObject(generalnodeport1);
            }
            myLeftPorts.set(i, generalnodeport);
            generalnodeport.setSideIndex(true, i);
            initializePort(generalnodeport);
            update(0x1271b, -(i + 1), generalnodeport1);
        }
    }

    @SuppressWarnings("unchecked")
    public void setRightPort(int i, GeneralNodePort generalnodeport) {
        GeneralNodePort generalnodeport1 = getRightPort(i);
        if (generalnodeport1 != generalnodeport) {
            if (generalnodeport1 != null) {
                if (generalnodeport != null)
                    generalnodeport.setBoundingRect(generalnodeport1
                            .getBoundingRect());
                removeObject(generalnodeport1);
            }
            myRightPorts.set(i, generalnodeport);
            generalnodeport.setSideIndex(false, i);
            initializePort(generalnodeport);
            update(0x1271b, i, generalnodeport1);
        }
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 75545 :
                return;

            case 75546 :
                return;

            case 75547 :
                int i = graphicviewerdocumentchangededit.getOldValueInt();
                if (i < 0)
                    graphicviewerdocumentchangededit
                            .setNewValue(getLeftPort(-i - 1));
                else
                    graphicviewerdocumentchangededit
                            .setNewValue(getRightPort(i));
                return;
        }
        super.copyNewValueForRedo(graphicviewerdocumentchangededit);
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 75545 :
                int i = graphicviewerdocumentchangededit.getOldValueInt();
                if (i < 0) {
                    i = -i - 1;
                    if (flag)
                        removeLeftPort(i);
                    else
                        insertLeftPort(
                                i,
                                (GeneralNodePort) graphicviewerdocumentchangededit
                                        .getOldValue());
                } else if (flag)
                    removeRightPort(i);
                else
                    insertRightPort(i,
                            (GeneralNodePort) graphicviewerdocumentchangededit
                                    .getOldValue());
                return;

            case 75546 :
                int j = graphicviewerdocumentchangededit.getOldValueInt();
                if (j < 0) {
                    j = -j - 1;
                    if (flag)
                        insertLeftPort(
                                j,
                                (GeneralNodePort) graphicviewerdocumentchangededit
                                        .getOldValue());
                    else
                        removeLeftPort(j);
                } else if (flag)
                    insertRightPort(j,
                            (GeneralNodePort) graphicviewerdocumentchangededit
                                    .getOldValue());
                else
                    removeRightPort(j);
                return;

            case 75547 :
                int k = graphicviewerdocumentchangededit.getOldValueInt();
                if (k < 0) {
                    k = -k - 1;
                    setLeftPort(k,
                            (GeneralNodePort) graphicviewerdocumentchangededit
                                    .getValue(flag));
                } else {
                    setRightPort(k,
                            (GeneralNodePort) graphicviewerdocumentchangededit
                                    .getValue(flag));
                }
                return;
        }
        super.changeValue(graphicviewerdocumentchangededit, flag);
    }

    public static final int PortInsertedChanged = 0x12719;
    public static final int PortRemovedChanged = 0x1271a;
    public static final int PortSetChanged = 0x1271b;
    protected GraphicViewerText myTopLabel;
    protected GraphicViewerText myBottomLabel;
    protected GraphicViewerObject myIcon;
    @SuppressWarnings("rawtypes")
    protected ArrayList myLeftPorts;
    @SuppressWarnings("rawtypes")
    protected ArrayList myRightPorts;
}