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

import java.awt.Point;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerLabeledLink extends GraphicViewerLink {

    private static final long serialVersionUID = 5721221043857929353L;

    public GraphicViewerLabeledLink() {
        cB = null;
        cD = null;
        cC = null;
        m();
    }

    public GraphicViewerLabeledLink(GraphicViewerPort graphicviewerport,
            GraphicViewerPort graphicviewerport1) {
        super(graphicviewerport, graphicviewerport1);
        cB = null;
        cD = null;
        cC = null;
        m();
    }

    private final void m() {
        _mthfor(g() | 0x200);
    }

    public GraphicViewerObject copyObject(
            GraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerLabeledLink graphicviewerlabeledlink = (GraphicViewerLabeledLink) super
                .copyObject(graphicviewercopyenvironment);
        if (graphicviewerlabeledlink != null) {
            graphicviewerlabeledlink.setFromLabel(graphicviewercopyenvironment
                    .copy(cB));
            graphicviewerlabeledlink.setMidLabel(graphicviewercopyenvironment
                    .copy(cD));
            graphicviewerlabeledlink.setToLabel(graphicviewercopyenvironment
                    .copy(cC));
        }
        return graphicviewerlabeledlink;
    }

    public void setGrabChildSelection(boolean flag) {
        boolean flag1 = (g() & 0x200) != 0;
        if (flag1 != flag) {
            if (flag)
                _mthfor(g() | 0x200);
            else
                _mthfor(g() & 0xfffffdff);
            update(254, flag1 ? 1 : 0, null);
        }
    }

    public boolean isGrabChildSelection() {
        return (g() & 0x200) != 0;
    }

    protected void ownerChange(
            GraphicViewerObjectCollection graphicviewerobjectcollection,
            GraphicViewerObjectCollection graphicviewerobjectcollection1,
            GraphicViewerObject graphicviewerobject) {
        super.ownerChange(graphicviewerobjectcollection,
                graphicviewerobjectcollection1, graphicviewerobject);
        if (graphicviewerobjectcollection == null
                && graphicviewerobjectcollection1 != null) {
            Object obj = graphicviewerobjectcollection1;
            if (getParent() != null)
                obj = getParent();
            GraphicViewerListPosition graphicviewerlistposition = ((GraphicViewerObjectCollection) (obj))
                    .findObject(this);
            if (graphicviewerlistposition != null) {
                GraphicViewerObject graphicviewerobject1 = getFromLabel();
                if (graphicviewerobject1 != null
                        && ((GraphicViewerObjectCollection) (obj))
                                .findObject(graphicviewerobject1) == null) {
                    if (graphicviewerobject1.getParent() != null)
                        graphicviewerobject1.getParent().removeObject(
                                graphicviewerobject1);
                    graphicviewerlistposition = ((GraphicViewerObjectCollection) (obj))
                            .insertObjectAfter(graphicviewerlistposition,
                                    graphicviewerobject1);
                }
                GraphicViewerObject graphicviewerobject2 = getMidLabel();
                if (graphicviewerobject2 != null
                        && ((GraphicViewerObjectCollection) (obj))
                                .findObject(graphicviewerobject2) == null) {
                    if (graphicviewerobject2.getParent() != null)
                        graphicviewerobject2.getParent().removeObject(
                                graphicviewerobject2);
                    graphicviewerlistposition = ((GraphicViewerObjectCollection) (obj))
                            .insertObjectAfter(graphicviewerlistposition,
                                    graphicviewerobject2);
                }
                GraphicViewerObject graphicviewerobject3 = getToLabel();
                if (graphicviewerobject3 != null
                        && ((GraphicViewerObjectCollection) (obj))
                                .findObject(graphicviewerobject3) == null) {
                    if (graphicviewerobject3.getParent() != null)
                        graphicviewerobject3.getParent().removeObject(
                                graphicviewerobject3);
                    graphicviewerlistposition = ((GraphicViewerObjectCollection) (obj))
                            .insertObjectAfter(graphicviewerlistposition,
                                    graphicviewerobject3);
                }
            }
        } else if (graphicviewerobjectcollection != null
                && graphicviewerobjectcollection1 == null && !k()
                && !isChildOf(graphicviewerobject)) {
            graphicviewerobjectcollection.removeObject(getFromLabel());
            graphicviewerobjectcollection.removeObject(getMidLabel());
            graphicviewerobjectcollection.removeObject(getToLabel());
        }
    }

    public void calculateStroke() {
        super.calculateStroke();
        positionLabels();
    }

    public void positionLabels() {
        int i = getNumPoints();
        if (i >= 2) {
            GraphicViewerObject graphicviewerobject = getFromLabel();
            if (graphicviewerobject != null) {
                int j = getPointX(0);
                int l = getPointY(0);
                int j1 = getPointX(1);
                int l1 = getPointY(1);
                if (i == 2)
                    positionEndLabel(graphicviewerobject, j, l, j, l, j1, l1);
                else
                    positionEndLabel(graphicviewerobject, j, l, j1, l1,
                            getPointX(2), getPointY(2));
            }
            layoutMidLabel();
            graphicviewerobject = getToLabel();
            if (graphicviewerobject != null) {
                int k = getPointX(i - 1);
                int i1 = getPointY(i - 1);
                int k1 = getPointX(i - 2);
                int i2 = getPointY(i - 2);
                if (i == 2)
                    positionEndLabel(graphicviewerobject, k, i1, k, i1, k1, i2);
                else
                    positionEndLabel(graphicviewerobject, k, i1, k1, i2,
                            getPointX(i - 3), getPointY(i - 3));
            }
        }
    }

    protected void positionEndLabel(GraphicViewerObject graphicviewerobject,
            int i, int j, int k, int l, int i1, int j1) {
        byte byte0 = 0;
        if (i < k) {
            if (l <= j1)
                byte0 = 7;
            else
                byte0 = 1;
        } else if (i > k) {
            if (l <= j1)
                byte0 = 5;
            else
                byte0 = 3;
        } else if (j < l) {
            if (k <= i1)
                byte0 = 3;
            else
                byte0 = 1;
        } else if (j > l) {
            if (k <= i1)
                byte0 = 5;
            else
                byte0 = 7;
        } else if (k <= i1) {
            if (l <= j1)
                byte0 = 3;
            else
                byte0 = 5;
        } else if (l <= j1)
            byte0 = 1;
        else
            byte0 = 7;
        graphicviewerobject.setSpotLocation(byte0, i, j);
    }

    public void layoutMidLabel() {
        if (isInitializing())
            return;
        GraphicViewerObject graphicviewerobject = getMidLabel();
        if (graphicviewerobject != null) {
            int i = getNumPoints();
            if (i < 2)
                return;
            if (isCubic() && i >= 4 && i < 7) {
                Point point = getPoint(0);
                Point point1 = getPoint(1);
                Point point2 = getPoint(i - 2);
                Point point3 = getPoint(i - 1);
                Point point4 = new Point(0, 0);
                Point point5 = new Point(0, 0);
                GraphicViewerStroke.a(point.x, point.y, point1.x, point1.y,
                        point2.x, point2.y, point3.x, point3.y, point4, point5);
                positionMidLabel(graphicviewerobject, point4.x, point4.y,
                        point5.x, point5.y);
                return;
            }
            int j = i / 2;
            if (i % 2 == 0) {
                int k = getPointX(j - 1);
                int i1 = getPointY(j - 1);
                int k1 = getPointX(j);
                int i2 = getPointY(j);
                positionMidLabel(graphicviewerobject, k, i1, k1, i2);
            } else {
                int l = getPointX(j - 1);
                int j1 = getPointY(j - 1);
                int l1 = getPointX(j);
                int j2 = getPointY(j);
                int k2 = getPointX(j + 1);
                int l2 = getPointY(j + 1);
                float f = l1 - l;
                float f1 = j2 - j1;
                float f2 = k2 - l1;
                float f3 = l2 - j2;
                if (f * f + f1 * f1 >= f2 * f2 + f3 * f3)
                    positionMidLabel(graphicviewerobject, l, j1, l1, j2);
                else
                    positionMidLabel(graphicviewerobject, l1, j2, k2, l2);
            }
        }
    }

    protected void positionMidLabel(GraphicViewerObject graphicviewerobject,
            int i, int j, int k, int l) {
        if ((graphicviewerobject instanceof GraphicViewerText)
                && Math.abs(j - l) < 2 && !isCubic())
            graphicviewerobject.setSpotLocation(6, (i + k) / 2, (j + l) / 2);
        else
            graphicviewerobject.setSpotLocation(0, (i + k) / 2, (j + l) / 2);
    }

    public void SVGWriteObject(DomDoc domdoc, DomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            DomElement domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphicviewer.GraphicViewerLabeledLink",
                            domelement);
            domdoc.registerReferencingNode(domelement1, "fromlabel",
                    getFromLabel());
            domdoc.registerReferencingNode(domelement1, "midlabel",
                    getMidLabel());
            domdoc.registerReferencingNode(domelement1, "tolabel", getToLabel());
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public DomNode SVGReadObject(DomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, DomElement domelement,
            DomElement domelement1) {
        if (domelement1 != null) {
            String s = domelement1.getAttribute("fromlabel");
            String s1 = domelement1.getAttribute("midlabel");
            String s2 = domelement1.getAttribute("tolabel");
            domdoc.registerReferencingObject(this, "fromlabel", s);
            domdoc.registerReferencingObject(this, "midlabel", s1);
            domdoc.registerReferencingObject(this, "tolabel", s2);
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
            return domelement.getNextSibling();
        } else {
            return domelement.getNextSibling();
        }
    }

    public void SVGUpdateReference(String s, Object obj) {
        super.SVGUpdateReference(s, obj);
        if (s.equals("fromlabel"))
            setFromLabel((GraphicViewerObject) obj);
        else if (s.equals("midlabel"))
            setMidLabel((GraphicViewerObject) obj);
        else if (s.equals("tolabel"))
            setToLabel((GraphicViewerObject) obj);
    }

    public GraphicViewerObject getFromLabel() {
        return cB;
    }

    public void setFromLabel(GraphicViewerObject graphicviewerobject) {
        GraphicViewerObject graphicviewerobject1 = cB;
        if (graphicviewerobject1 != graphicviewerobject) {
            Object obj = getParent();
            if (obj == null)
                obj = getLayer();
            if (obj == null)
                obj = getView();
            if (graphicviewerobject1 != null) {
                if (obj != null)
                    ((GraphicViewerObjectCollection) (obj))
                            .removeObject(graphicviewerobject1);
                graphicviewerobject1.setPartner(null);
            }
            cB = graphicviewerobject;
            update(251, 0, graphicviewerobject1);
            if (graphicviewerobject != null)
                if (graphicviewerobject == getMidLabel()) {
                    cD = null;
                    update(252, 0, graphicviewerobject);
                } else if (graphicviewerobject == getToLabel()) {
                    cC = null;
                    update(253, 0, graphicviewerobject);
                } else {
                    graphicviewerobject.setPartner(this);
                    if (obj != null)
                        ((GraphicViewerObjectCollection) (obj))
                                .insertObjectAfter(
                                        ((GraphicViewerObjectCollection) (obj))
                                                .findObject(this),
                                        graphicviewerobject);
                }
            positionLabels();
        }
    }

    public GraphicViewerObject getMidLabel() {
        return cD;
    }

    public void setMidLabel(GraphicViewerObject graphicviewerobject) {
        GraphicViewerObject graphicviewerobject1 = cD;
        if (graphicviewerobject1 != graphicviewerobject) {
            Object obj = getParent();
            if (obj == null)
                obj = getLayer();
            if (obj == null)
                obj = getView();
            if (graphicviewerobject1 != null) {
                if (obj != null)
                    ((GraphicViewerObjectCollection) (obj))
                            .removeObject(graphicviewerobject1);
                graphicviewerobject1.setPartner(null);
            }
            cD = graphicviewerobject;
            update(252, 0, graphicviewerobject1);
            if (graphicviewerobject != null)
                if (graphicviewerobject == getFromLabel()) {
                    cB = null;
                    update(251, 0, graphicviewerobject);
                } else if (graphicviewerobject == getToLabel()) {
                    cC = null;
                    update(253, 0, graphicviewerobject);
                } else {
                    graphicviewerobject.setPartner(this);
                    if (obj != null)
                        ((GraphicViewerObjectCollection) (obj))
                                .insertObjectAfter(
                                        ((GraphicViewerObjectCollection) (obj))
                                                .findObject(this),
                                        graphicviewerobject);
                }
            positionLabels();
        }
    }

    public GraphicViewerObject getToLabel() {
        return cC;
    }

    public void setToLabel(GraphicViewerObject graphicviewerobject) {
        GraphicViewerObject graphicviewerobject1 = cC;
        if (graphicviewerobject1 != graphicviewerobject) {
            Object obj = getParent();
            if (obj == null)
                obj = getLayer();
            if (obj == null)
                obj = getView();
            if (graphicviewerobject1 != null) {
                if (obj != null)
                    ((GraphicViewerObjectCollection) (obj))
                            .removeObject(graphicviewerobject1);
                graphicviewerobject1.setPartner(null);
            }
            cC = graphicviewerobject;
            update(253, 0, graphicviewerobject1);
            if (graphicviewerobject != null)
                if (graphicviewerobject == getMidLabel()) {
                    cD = null;
                    update(252, 0, graphicviewerobject);
                } else if (graphicviewerobject == getFromLabel()) {
                    cB = null;
                    update(251, 0, graphicviewerobject);
                } else {
                    graphicviewerobject.setPartner(this);
                    if (obj != null)
                        ((GraphicViewerObjectCollection) (obj))
                                .insertObjectAfter(
                                        ((GraphicViewerObjectCollection) (obj))
                                                .findObject(this),
                                        graphicviewerobject);
                }
            positionLabels();
        }
    }

    public void update(int i, int j, Object obj) {
        if (isSuspendUpdates())
            return;
        if (i == 10) {
            Object obj1 = getParent();
            if (obj1 == null)
                obj1 = getLayer();
            if (obj1 == null)
                obj1 = getView();
            if (obj1 == null)
                return;
            GraphicViewerListPosition graphicviewerlistposition = ((GraphicViewerObjectCollection) (obj1))
                    .findObject(this);
            if (graphicviewerlistposition == null)
                return;
            GraphicViewerObject graphicviewerobject = getFromLabel();
            if (graphicviewerobject != null)
                graphicviewerlistposition = ((GraphicViewerObjectCollection) (obj1))
                        .insertObjectAfter(graphicviewerlistposition,
                                graphicviewerobject);
            graphicviewerobject = getMidLabel();
            if (graphicviewerobject != null)
                graphicviewerlistposition = ((GraphicViewerObjectCollection) (obj1))
                        .insertObjectAfter(graphicviewerlistposition,
                                graphicviewerobject);
            graphicviewerobject = getToLabel();
            if (graphicviewerobject != null)
                graphicviewerlistposition = ((GraphicViewerObjectCollection) (obj1))
                        .insertObjectAfter(graphicviewerlistposition,
                                graphicviewerobject);
        } else if (i == 110 || i == 103 || i == 101 || i == 102 || i == 109
                || i == 203 || i == 206)
            positionLabels();
        super.update(i, j, obj);
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 251 :
                graphicviewerdocumentchangededit.setNewValue(getFromLabel());
                return;

            case 252 :
                graphicviewerdocumentchangededit.setNewValue(getMidLabel());
                return;

            case 253 :
                graphicviewerdocumentchangededit.setNewValue(getToLabel());
                return;

            case 254 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isGrabChildSelection());
                return;
        }
        super.copyNewValueForRedo(graphicviewerdocumentchangededit);
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 251 :
                setFromLabel((GraphicViewerObject) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 252 :
                setMidLabel((GraphicViewerObject) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 253 :
                setToLabel((GraphicViewerObject) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 254 :
                setGrabChildSelection(graphicviewerdocumentchangededit
                        .getValueBoolean(flag));
                return;
        }
        super.changeValue(graphicviewerdocumentchangededit, flag);
    }

    public static final int ChangedFromLabel = 251;
    public static final int ChangedMidLabel = 252;
    public static final int ChangedToLabel = 253;
    public static final int ChangedGrabChildSelection = 254;
    private GraphicViewerObject cB;
    private GraphicViewerObject cD;
    private GraphicViewerObject cC;
}