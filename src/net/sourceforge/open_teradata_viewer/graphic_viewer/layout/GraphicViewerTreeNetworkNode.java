/*
 * Open Teradata Viewer ( graphic viewer layout )
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

package net.sourceforge.open_teradata_viewer.graphic_viewer.layout;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Comparator;

import net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerObject;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class GraphicViewerTreeNetworkNode {

    private GraphicViewerObject myGraphicViewerObject;
    private Rectangle myBounds;
    private Dimension myFocus = new Dimension(5, 5);
    private GraphicViewerTreeNetwork myNetwork;
    private GraphicViewerLinkArrayList mySourceLinks = new GraphicViewerLinkArrayList();
    private GraphicViewerLinkArrayList myDestinationLinks = new GraphicViewerLinkArrayList();
    public static final int SortingForwards = 1;
    public static final int SortingReverse = 2;
    public static final int SortingAscending = 3;
    public static final int SortingDescending = 4;
    private int mySorting;
    public static final int CompactionNone = 1;
    public static final int CompactionBlock = 2;
    private int myCompaction = 2;
    private int myBreadthLimit;
    private int myRowSpacing;
    private int myRowIndent;
    private int myCommentSpacing;
    private int myCommentMargin;
    private int myPortSpot = -1;
    private int myChildPortSpot = -1;
    public static final int AlignmentCenterSubtrees = 1;
    public static final int AlignmentCenterChildren = 2;
    public static final int AlignmentStart = 3;
    public static final int AlignmentEnd = 4;
    private int myAlignment = 2;
    private int myNodeIndent;
    private int myLayerSpacing = 50;
    static int myInitializedFlag = 65536;
    static int myNoSetsPortSpotFlag = 131072;
    static int myNoSetsChildPortSpotFlag = 262144;
    static int myRouteFirstRowFlag = 1;
    static int myRouteAroundCenteredFlag = 2;
    static int myRouteAroundLastParentFlag = 4;
    private int myInternalFlags;
    private GraphicViewerTreeNetworkNode myParent;
    private GraphicViewerTreeNetworkNode[] myChildren = getNoChildren();
    private int myLevel;
    private int myDescendentCount;
    private int myMaxChildrenCount;
    private int myMaxGenerationCount;
    private Comparator myComparer = new GraphicViewerAlphaComparer();
    private Point myRelativePosition;
    private Dimension mySubtreeSize;
    private Dimension mySubtreeOffset;
    private int myNodeSpacing = 20;
    private float myAngle;
    Point[] LeftFringe;
    Point[] RightFringe;

    public GraphicViewerObject getGraphicViewerObject() {
        return this.myGraphicViewerObject;
    }

    public void setGraphicViewerObject(
            GraphicViewerObject paramGraphicViewerObject) {
        this.myGraphicViewerObject = paramGraphicViewerObject;
        if (this.myGraphicViewerObject != null) {
            Point localPoint = this.myGraphicViewerObject.getSpotLocation(0);
            this.myBounds = new Rectangle(
                    this.myGraphicViewerObject.getBoundingRect().x,
                    this.myGraphicViewerObject.getBoundingRect().y,
                    this.myGraphicViewerObject.getBoundingRect().width,
                    this.myGraphicViewerObject.getBoundingRect().height);
            this.myFocus = new Dimension(localPoint.x - this.myBounds.x,
                    localPoint.y - this.myBounds.y);
        }
    }

    public GraphicViewerTreeNetwork getNetwork() {
        return this.myNetwork;
    }

    public void setNetwork(
            GraphicViewerTreeNetwork paramGraphicViewerTreeNetwork) {
        this.myNetwork = paramGraphicViewerTreeNetwork;
    }

    GraphicViewerLinkArrayList getSourceLinksList() {
        return this.mySourceLinks;
    }

    public void AddSourceLink(
            GraphicViewerTreeNetworkLink paramGraphicViewerTreeNetworkLink) {
        if (!this.mySourceLinks.Contains(paramGraphicViewerTreeNetworkLink)) {
            this.mySourceLinks.Add(paramGraphicViewerTreeNetworkLink);
        }
    }

    public void DeleteSourceLink(
            GraphicViewerTreeNetworkLink paramGraphicViewerTreeNetworkLink) {
        int i = this.mySourceLinks.IndexOf(paramGraphicViewerTreeNetworkLink);
        if (i != -1) {
            this.mySourceLinks.RemoveAt(i);
        }
    }

    GraphicViewerLinkArrayList getDestinationLinksList() {
        return this.myDestinationLinks;
    }

    public void AddDestinationLink(
            GraphicViewerTreeNetworkLink paramGraphicViewerTreeNetworkLink) {
        if (!this.myDestinationLinks
                .Contains(paramGraphicViewerTreeNetworkLink)) {
            this.myDestinationLinks.Add(paramGraphicViewerTreeNetworkLink);
        }
    }

    public void DeleteDestinationLink(
            GraphicViewerTreeNetworkLink paramGraphicViewerTreeNetworkLink) {
        int i = this.myDestinationLinks
                .IndexOf(paramGraphicViewerTreeNetworkLink);
        if (i != -1) {
            this.myDestinationLinks.RemoveAt(i);
        }
    }

    public Point getCenter() {
        return new Point(getBounds().x + getFocus().width, getBounds().y
                + getFocus().height);
    }

    public void setCenter(Point paramPoint) {
        this.myBounds.x = (paramPoint.x - getFocus().width);
        this.myBounds.y = (paramPoint.y - getFocus().height);
    }

    public Point getPosition() {
        return new Point(getBounds().x, getBounds().y);
    }

    public void setPosition(Point paramPoint) {
        this.myBounds.x = paramPoint.x;
        this.myBounds.y = paramPoint.y;
    }

    public Dimension getFocus() {
        return this.myFocus;
    }

    public void setFocus(Dimension paramDimension) {
        this.myFocus = paramDimension;
    }

    public Rectangle getBounds() {
        return this.myBounds;
    }

    public void setBounds(Rectangle paramRectangle) {
        this.myBounds = paramRectangle;
    }

    public Dimension getSize() {
        return new Dimension(this.myBounds.width, this.myBounds.height);
    }

    public int getWidth() {
        return getBounds().width;
    }

    public int getHeight() {
        return getBounds().height;
    }

    public void CommitPosition() {
        GraphicViewerObject localGraphicViewerObject = getGraphicViewerObject();
        if (localGraphicViewerObject != null) {
            Point localPoint1;
            if ((localGraphicViewerObject.redirectSelection() != null)
                    && (localGraphicViewerObject.redirectSelection() != localGraphicViewerObject)) {
                localPoint1 = localGraphicViewerObject.redirectSelection()
                        .getLocation();
                localPoint1.x += localGraphicViewerObject.redirectSelection()
                        .getWidth() / 2;
                localPoint1.y += localGraphicViewerObject.redirectSelection()
                        .getHeight() / 2;
                Point localPoint2 = localGraphicViewerObject.getLocation();
                localGraphicViewerObject.setLocation(new Point(getCenter().x
                        - (localPoint1.x - localPoint2.x), getCenter().y
                        - (localPoint1.y - localPoint2.y)));
            } else {
                localPoint1 = new Point(0, 0);
                localPoint1.x = (getCenter().x - getWidth() / 2);
                localPoint1.y = (getCenter().y - getHeight() / 2);
                localGraphicViewerObject.setLocation(localPoint1);
            }
        }
    }

    public GraphicViewerNodeArrayList getSources() {
        GraphicViewerNodeArrayList localGraphicViewerNodeArrayList = new GraphicViewerNodeArrayList();
        for (int i = 0; i < this.mySourceLinks.getCount(); i++) {
            GraphicViewerTreeNetworkLink localGraphicViewerTreeNetworkLink = this.mySourceLinks
                    .get(i);
            GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode = localGraphicViewerTreeNetworkLink
                    .getFromNode();
            if ((localGraphicViewerTreeNetworkNode != null)
                    && (!localGraphicViewerNodeArrayList
                            .Contains(localGraphicViewerTreeNetworkNode))) {
                localGraphicViewerNodeArrayList
                        .Add(localGraphicViewerTreeNetworkNode);
            }
        }
        return localGraphicViewerNodeArrayList;
    }

    public GraphicViewerNodeArrayList getDestinations() {
        GraphicViewerNodeArrayList localGraphicViewerNodeArrayList = new GraphicViewerNodeArrayList();
        for (int i = 0; i < this.myDestinationLinks.getCount(); i++) {
            GraphicViewerTreeNetworkLink localGraphicViewerTreeNetworkLink = this.myDestinationLinks
                    .get(i);
            GraphicViewerTreeNetworkNode localGraphicViewerTreeNetworkNode = localGraphicViewerTreeNetworkLink
                    .getToNode();
            if ((localGraphicViewerTreeNetworkNode != null)
                    && (!localGraphicViewerNodeArrayList
                            .Contains(localGraphicViewerTreeNetworkNode))) {
                localGraphicViewerNodeArrayList
                        .Add(localGraphicViewerTreeNetworkNode);
            }
        }
        return localGraphicViewerNodeArrayList;
    }

    public void CopyInheritedPropertiesFrom(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
        setInternalFlags(paramGraphicViewerTreeNetworkNode.getInternalFlags());
        setSorting(paramGraphicViewerTreeNetworkNode.getSorting());
        setComparer(paramGraphicViewerTreeNetworkNode.getComparer());
        setAngle(paramGraphicViewerTreeNetworkNode.getAngle());
        setAlignment(paramGraphicViewerTreeNetworkNode.getAlignment());
        setNodeIndent(paramGraphicViewerTreeNetworkNode.getNodeIndent());
        setNodeSpacing(paramGraphicViewerTreeNetworkNode.getNodeSpacing());
        setNodeIndent(paramGraphicViewerTreeNetworkNode.getNodeIndent());
        setLayerSpacing(paramGraphicViewerTreeNetworkNode.getLayerSpacing());
        setCompaction(paramGraphicViewerTreeNetworkNode.getCompaction());
        setBreadthLimit(paramGraphicViewerTreeNetworkNode.getBreadthLimit());
        setRowSpacing(paramGraphicViewerTreeNetworkNode.getRowSpacing());
        setRowIndent(paramGraphicViewerTreeNetworkNode.getRowIndent());
        setCommentSpacing(paramGraphicViewerTreeNetworkNode.getCommentSpacing());
        setCommentMargin(paramGraphicViewerTreeNetworkNode.getCommentMargin());
        setPortSpot(paramGraphicViewerTreeNetworkNode.getPortSpot());
        setChildPortSpot(paramGraphicViewerTreeNetworkNode.getChildPortSpot());
    }

    public void setInitialized(boolean paramBoolean) {
        if (paramBoolean) {
            this.myInternalFlags |= myInitializedFlag;
        } else {
            this.myInternalFlags &= (myInitializedFlag ^ 0xFFFFFFFF);
        }
    }

    public boolean getInitialized() {
        return (this.myInternalFlags & myInitializedFlag) != 0;
    }

    public GraphicViewerTreeNetworkNode getParent() {
        return this.myParent;
    }

    public void setParent(
            GraphicViewerTreeNetworkNode paramGraphicViewerTreeNetworkNode) {
        this.myParent = paramGraphicViewerTreeNetworkNode;
    }

    public GraphicViewerTreeNetworkNode[] getChildren() {
        return this.myChildren;
    }

    public void setChildren(
            GraphicViewerTreeNetworkNode[] paramArrayOfGraphicViewerTreeNetworkNode) {
        this.myChildren = paramArrayOfGraphicViewerTreeNetworkNode;
    }

    public int getChildrenCount() {
        return this.myChildren.length;
    }

    public int getLevel() {
        return this.myLevel;
    }

    public void setLevel(int paramInt) {
        this.myLevel = paramInt;
    }

    public int getDescendentCount() {
        return this.myDescendentCount;
    }

    public void setDescendentCount(int paramInt) {
        this.myDescendentCount = paramInt;
    }

    public int getMaxChildrenCount() {
        return this.myMaxChildrenCount;
    }

    public void setMaxChildrenCount(int paramInt) {
        this.myMaxChildrenCount = paramInt;
    }

    public int getMaxGenerationCount() {
        return this.myMaxGenerationCount;
    }

    public void setMaxGenerationCount(int paramInt) {
        this.myMaxGenerationCount = paramInt;
    }

    public static GraphicViewerTreeNetworkNode[] getNoChildren() {
        return new GraphicViewerTreeNetworkNode[0];
    }

    public int getSorting() {
        return this.mySorting;
    }

    public void setSorting(int paramInt) {
        this.mySorting = paramInt;
    }

    public int getInternalFlags() {
        return this.myInternalFlags;
    }

    public void setInternalFlags(int paramInt) {
        this.myInternalFlags = paramInt;
    }

    public Comparator getComparer() {
        return this.myComparer;
    }

    public void setComparer(Comparator paramComparator) {
        this.myComparer = paramComparator;
    }

    public float getAngle() {
        return this.myAngle;
    }

    public void setAngle(float paramFloat) {
        this.myAngle = paramFloat;
    }

    public int getAlignment() {
        return this.myAlignment;
    }

    public void setAlignment(int paramInt) {
        this.myAlignment = paramInt;
    }

    public int getNodeIndent() {
        return this.myNodeIndent;
    }

    public void setNodeIndent(int paramInt) {
        this.myNodeIndent = paramInt;
    }

    public int getLayerSpacing() {
        return this.myLayerSpacing;
    }

    public void setLayerSpacing(int paramInt) {
        this.myLayerSpacing = paramInt;
    }

    public int getCompaction() {
        return this.myCompaction;
    }

    public void setCompaction(int paramInt) {
        this.myCompaction = paramInt;
    }

    public int getBreadthLimit() {
        return this.myBreadthLimit;
    }

    public void setBreadthLimit(int paramInt) {
        this.myBreadthLimit = paramInt;
    }

    public int getRowSpacing() {
        return this.myRowSpacing;
    }

    public void setRowSpacing(int paramInt) {
        this.myRowSpacing = paramInt;
    }

    public int getRowIndent() {
        return this.myRowIndent;
    }

    public void setRowIndent(int paramInt) {
        this.myRowIndent = paramInt;
    }

    public int getCommentSpacing() {
        return this.myCommentSpacing;
    }

    public void setCommentSpacing(int paramInt) {
        this.myCommentSpacing = paramInt;
    }

    public int getCommentMargin() {
        return this.myCommentMargin;
    }

    public void setCommentMargin(int paramInt) {
        this.myCommentMargin = paramInt;
    }

    public int getPortSpot() {
        return this.myPortSpot;
    }

    public void setPortSpot(int paramInt) {
        this.myPortSpot = paramInt;
    }

    public int getChildPortSpot() {
        return this.myChildPortSpot;
    }

    public void setChildPortSpot(int paramInt) {
        this.myChildPortSpot = paramInt;
    }

    public Point getRelativePosition() {
        return this.myRelativePosition;
    }

    public void setRelativePosition(Point paramPoint) {
        this.myRelativePosition = paramPoint;
    }

    public Dimension getSubtreeSize() {
        return this.mySubtreeSize;
    }

    public void setSubtreeSize(Dimension paramDimension) {
        this.mySubtreeSize = paramDimension;
    }

    public Dimension getSubtreeOffset() {
        return this.mySubtreeOffset;
    }

    public void setSubtreeOffset(Dimension paramDimension) {
        this.mySubtreeOffset = paramDimension;
    }

    public int getNodeSpacing() {
        return this.myNodeSpacing;
    }

    public void setNodeSpacing(int paramInt) {
        this.myNodeSpacing = paramInt;
    }

    boolean getRouteAroundCentered() {
        return (this.myInternalFlags & myRouteAroundCenteredFlag) != 0;
    }

    void setRouteAroundCentered(boolean paramBoolean) {
        if (paramBoolean) {
            this.myInternalFlags |= myRouteAroundCenteredFlag;
        } else {
            this.myInternalFlags &= (myRouteAroundCenteredFlag ^ 0xFFFFFFFF);
        }
    }

    boolean getRouteAroundLastParent() {
        return (this.myInternalFlags & myRouteAroundLastParentFlag) != 0;
    }

    void setRouteAroundLastParent(boolean paramBoolean) {
        if (paramBoolean) {
            this.myInternalFlags |= myRouteAroundLastParentFlag;
        } else {
            this.myInternalFlags &= (myRouteAroundLastParentFlag ^ 0xFFFFFFFF);
        }
    }

    boolean getSetsPortSpot() {
        return (this.myInternalFlags & myNoSetsPortSpotFlag) != myNoSetsPortSpotFlag;
    }

    void setSetsPortSport(boolean paramBoolean) {
        if (paramBoolean) {
            this.myInternalFlags &= (myNoSetsPortSpotFlag ^ 0xFFFFFFFF);
        } else {
            this.myInternalFlags |= myNoSetsPortSpotFlag;
        }
    }

    boolean getSetsChildPortSpot() {
        return (this.myInternalFlags & myNoSetsChildPortSpotFlag) != myNoSetsChildPortSpotFlag;
    }

    void setSetsChildPortSpot(boolean paramBoolean) {
        if (paramBoolean) {
            this.myInternalFlags &= (myNoSetsChildPortSpotFlag ^ 0xFFFFFFFF);
        } else {
            this.myInternalFlags |= myNoSetsChildPortSpotFlag;
        }
    }

    boolean getRouteFirstRow() {
        return (this.myInternalFlags & myRouteFirstRowFlag) != 0;
    }

    void setRouteFirstRow(boolean paramBoolean) {
        if (paramBoolean) {
            this.myInternalFlags |= myRouteFirstRowFlag;
        } else {
            this.myInternalFlags &= (myRouteFirstRowFlag ^ 0xFFFFFFFF);
        }
    }
}