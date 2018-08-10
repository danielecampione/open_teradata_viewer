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

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerTextNode extends GraphicViewerNode {

    private static final long serialVersionUID = -329622213870441858L;

    public GraphicViewerTextNode() {
        dn = null;
        dm = null;
        dp = null;
        dq = null;
        dj = null;
        di = null;
        dl = new Insets(3, 3, 2, 2);
        s();
    }

    public GraphicViewerTextNode(String s1) {
        dn = null;
        dm = null;
        dp = null;
        dq = null;
        dj = null;
        di = null;
        dl = new Insets(3, 3, 2, 2);
        s();
        _mthfor(s1);
    }

    private void s() {
        _mthfor(g() & 0xffffffef | 0x10000);
    }

    private void _mthfor(String s1) {
        setInitializing(true);
        dm = createBackground();
        dn = createLabel(s1);
        dp = createPort(2);
        dq = createPort(4);
        dj = createPort(6);
        di = createPort(8);
        addObjectAtHead(dm);
        addObjectAtTail(dn);
        addObjectAtTail(dp);
        addObjectAtTail(dq);
        addObjectAtTail(dj);
        addObjectAtTail(di);
        setInitializing(false);
        layoutChildren(null);
    }

    public GraphicViewerObject createBackground() {
        GraphicViewer3DRect graphicviewer3drect = new GraphicViewer3DRect();
        graphicviewer3drect.setSelectable(false);
        return graphicviewer3drect;
    }

    public GraphicViewerText createLabel(String s1) {
        GraphicViewerText graphicviewertext = new GraphicViewerText();
        graphicviewertext.setSelectable(false);
        graphicviewertext.setResizable(false);
        graphicviewertext.setDraggable(false);
        graphicviewertext.setMultiline(true);
        graphicviewertext.setEditable(false);
        graphicviewertext.setEditOnSingleClick(true);
        graphicviewertext.setTransparent(true);
        graphicviewertext.setText(s1);
        return graphicviewertext;
    }

    public GraphicViewerPort createPort(int i) {
        GraphicViewerPort graphicviewerport = new GraphicViewerPort();
        graphicviewerport.setStyle(0);
        graphicviewerport.setSize(3, 3);
        graphicviewerport.setFromSpot(i);
        graphicviewerport.setToSpot(i);
        graphicviewerport.setAutoRescale(false);
        return graphicviewerport;
    }

    protected void copyChildren(GraphicViewerArea graphicviewerarea,
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerTextNode graphicviewertextnode = (GraphicViewerTextNode) graphicviewerarea;
        graphicviewertextnode.dl.top = dl.top;
        graphicviewertextnode.dl.left = dl.left;
        graphicviewertextnode.dl.bottom = dl.bottom;
        graphicviewertextnode.dl.right = dl.right;
        super.copyChildren(graphicviewerarea, graphicviewercopyenvironment);
        graphicviewertextnode.dm = (GraphicViewerObject) graphicviewercopyenvironment
                .get(dm);
        graphicviewertextnode.dn = (GraphicViewerText) graphicviewercopyenvironment
                .get(dn);
        graphicviewertextnode.dp = (GraphicViewerPort) graphicviewercopyenvironment
                .get(dp);
        graphicviewertextnode.dq = (GraphicViewerPort) graphicviewercopyenvironment
                .get(dq);
        graphicviewertextnode.dj = (GraphicViewerPort) graphicviewercopyenvironment
                .get(dj);
        graphicviewertextnode.di = (GraphicViewerPort) graphicviewercopyenvironment
                .get(di);
    }

    public GraphicViewerObject removeObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition) {
        GraphicViewerObject graphicviewerobject = super
                .removeObjectAtPos(graphicviewerlistposition);
        if (graphicviewerobject == dm)
            dm = null;
        else if (graphicviewerobject == dn)
            dn = null;
        else if (graphicviewerobject == dp)
            dp = null;
        else if (graphicviewerobject == dq)
            dq = null;
        else if (graphicviewerobject == dj)
            dj = null;
        else if (graphicviewerobject == di)
            di = null;
        return graphicviewerobject;
    }

    public void layoutChildren(GraphicViewerObject graphicviewerobject) {
        if (isInitializing())
            return;
        GraphicViewerText graphicviewertext = getLabel();
        if (graphicviewertext == null)
            return;
        GraphicViewerObject graphicviewerobject1 = getBackground();
        if (graphicviewerobject1 != null) {
            Insets insets = getInsets();
            if (isAutoResize()) {
                graphicviewerobject1.setBoundingRect(
                        graphicviewertext.getLeft() - insets.left,
                        graphicviewertext.getTop() - insets.top,
                        graphicviewertext.getWidth() + insets.left
                                + insets.right, graphicviewertext.getHeight()
                                + insets.top + insets.bottom);
            } else {
                int i = Math.max(graphicviewerobject1.getWidth()
                        - (insets.left + insets.right), 0);
                int j = Math.max(graphicviewerobject1.getHeight()
                        - (insets.top + insets.bottom), 0);
                graphicviewertext.setWidth(i);
                graphicviewertext.setWrappingWidth(i);
                graphicviewertext.C();
                int k = Math.min(graphicviewertext.getHeight(), j);
                int l = graphicviewerobject1.getLeft() + insets.left;
                int i1 = graphicviewerobject1.getTop() + insets.top + (j - k)
                        / 2;
                graphicviewertext.setBoundingRect(l, i1, i, k);
            }
            if (getTopPort() != null)
                getTopPort().setSpotLocation(6, graphicviewerobject1, 2);
            if (getRightPort() != null)
                getRightPort().setSpotLocation(8, graphicviewerobject1, 4);
            if (getBottomPort() != null)
                getBottomPort().setSpotLocation(2, graphicviewerobject1, 6);
            if (getLeftPort() != null)
                getLeftPort().setSpotLocation(4, graphicviewerobject1, 8);
        }
    }

    public GraphicViewerText getLabel() {
        return dn;
    }

    public void setLabel(GraphicViewerText graphicviewertext) {
        GraphicViewerText graphicviewertext1 = dn;
        if (graphicviewertext1 != graphicviewertext) {
            a(graphicviewertext1, graphicviewertext);
            if (graphicviewertext1 != null)
                removeObject(graphicviewertext1);
            dn = graphicviewertext;
            if (graphicviewertext != null)
                addObjectAtTail(graphicviewertext);
            update(2301, 0, graphicviewertext1);
        }
    }

    public GraphicViewerObject getBackground() {
        return dm;
    }

    public void setBackground(GraphicViewerObject graphicviewerobject) {
        GraphicViewerObject graphicviewerobject1 = dm;
        if (graphicviewerobject1 != graphicviewerobject) {
            if (graphicviewerobject1 != null)
                removeObject(graphicviewerobject1);
            dm = graphicviewerobject;
            if (graphicviewerobject != null)
                addObjectAtHead(graphicviewerobject);
            update(2302, 0, graphicviewerobject1);
        }
    }

    public GraphicViewerPort getTopPort() {
        return dp;
    }

    public void setTopPort(GraphicViewerPort graphicviewerport) {
        GraphicViewerPort graphicviewerport1 = dp;
        if (graphicviewerport1 != graphicviewerport) {
            if (graphicviewerport1 != null)
                removeObject(graphicviewerport1);
            dp = graphicviewerport;
            if (graphicviewerport != null)
                addObjectAtTail(graphicviewerport);
            update(2303, 0, graphicviewerport1);
        }
    }

    public GraphicViewerPort getRightPort() {
        return dq;
    }

    public void setRightPort(GraphicViewerPort graphicviewerport) {
        GraphicViewerPort graphicviewerport1 = dq;
        if (graphicviewerport1 != graphicviewerport) {
            if (graphicviewerport1 != null)
                removeObject(graphicviewerport1);
            dq = graphicviewerport;
            if (graphicviewerport != null)
                addObjectAtTail(graphicviewerport);
            update(2304, 0, graphicviewerport1);
        }
    }

    public GraphicViewerPort getBottomPort() {
        return dj;
    }

    public void setBottomPort(GraphicViewerPort graphicviewerport) {
        GraphicViewerPort graphicviewerport1 = dj;
        if (graphicviewerport1 != graphicviewerport) {
            if (graphicviewerport1 != null)
                removeObject(graphicviewerport1);
            dj = graphicviewerport;
            if (graphicviewerport != null)
                addObjectAtTail(graphicviewerport);
            update(2305, 0, graphicviewerport1);
        }
    }

    public GraphicViewerPort getLeftPort() {
        return di;
    }

    public void setLeftPort(GraphicViewerPort graphicviewerport) {
        GraphicViewerPort graphicviewerport1 = di;
        if (graphicviewerport1 != graphicviewerport) {
            if (graphicviewerport1 != null)
                removeObject(graphicviewerport1);
            di = graphicviewerport;
            if (graphicviewerport != null)
                addObjectAtTail(graphicviewerport);
            update(2306, 0, graphicviewerport1);
        }
    }

    public Insets getInsets() {
        return dl;
    }

    public void setInsets(Insets insets) {
        _mthif(insets, false);
    }

    private void _mthif(Insets insets, boolean flag) {
        Insets insets1 = dl;
        if (!insets1.equals(insets)) {
            Insets insets2 = new Insets(insets1.top, insets1.left,
                    insets1.bottom, insets1.right);
            dl.top = insets.top;
            dl.left = insets.left;
            dl.bottom = insets.bottom;
            dl.right = insets.right;
            update(2307, 0, insets2);
            if (!flag)
                layoutChildren(null);
        }
    }

    public void setAutoResize(boolean flag) {
        _mthfor(flag, false);
    }

    private void _mthfor(boolean flag, boolean flag1) {
        boolean flag2 = (g() & 0x10000) != 0;
        if (flag2 != flag) {
            if (flag)
                _mthfor(g() | 0x10000);
            else
                _mthfor(g() & 0xfffeffff);
            update(2308, flag2 ? 1 : 0, null);
            if (!flag1)
                onAutoResizeChanged(flag2);
        }
    }

    public boolean isAutoResize() {
        return (g() & 0x10000) != 0;
    }

    public void onAutoResizeChanged(boolean flag) {
        GraphicViewerText graphicviewertext = getLabel();
        if (graphicviewertext != null) {
            graphicviewertext.setMultiline(flag);
            graphicviewertext.setWrapping(flag);
            graphicviewertext.setClipping(flag);
        }
    }

    public GraphicViewerPen getPen() {
        GraphicViewerObject graphicviewerobject = getBackground();
        if (graphicviewerobject != null
                && (graphicviewerobject instanceof GraphicViewerDrawable))
            return ((GraphicViewerDrawable) graphicviewerobject).getPen();
        else
            return null;
    }

    public void setPen(GraphicViewerPen graphicviewerpen) {
        GraphicViewerObject graphicviewerobject = getBackground();
        if (graphicviewerobject != null
                && (graphicviewerobject instanceof GraphicViewerDrawable))
            ((GraphicViewerDrawable) graphicviewerobject)
                    .setPen(graphicviewerpen);
    }

    public GraphicViewerBrush getBrush() {
        GraphicViewerObject graphicviewerobject = getBackground();
        if (graphicviewerobject != null
                && (graphicviewerobject instanceof GraphicViewerDrawable))
            return ((GraphicViewerDrawable) graphicviewerobject).getBrush();
        else
            return null;
    }

    public void setBrush(GraphicViewerBrush graphicviewerbrush) {
        GraphicViewerObject graphicviewerobject = getBackground();
        if (graphicviewerobject != null
                && (graphicviewerobject instanceof GraphicViewerDrawable))
            ((GraphicViewerDrawable) graphicviewerobject)
                    .setBrush(graphicviewerbrush);
    }

    public String getText() {
        if (dn != null)
            return dn.getText();
        else
            return "";
    }

    public void setText(String s1) {
        if (s1 == null)
            return;
        GraphicViewerText graphicviewertext = getLabel();
        if (graphicviewertext == null) {
            dn = createLabel(s1);
            addObjectAtTail(dn);
        } else {
            graphicviewertext.setText(s1);
        }
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 2301 :
                graphicviewerdocumentchangededit.setNewValue(getLabel());
                return;

            case 2302 :
                graphicviewerdocumentchangededit.setNewValue(getBackground());
                return;

            case 2303 :
                graphicviewerdocumentchangededit.setNewValue(getTopPort());
                return;

            case 2304 :
                graphicviewerdocumentchangededit.setNewValue(getRightPort());
                return;

            case 2305 :
                graphicviewerdocumentchangededit.setNewValue(getBottomPort());
                return;

            case 2306 :
                graphicviewerdocumentchangededit.setNewValue(getLeftPort());
                return;

            case 2307 :
                Insets insets = getInsets();
                graphicviewerdocumentchangededit.setNewValue(new Insets(
                        insets.top, insets.left, insets.bottom, insets.right));
                return;

            case 2308 :
                graphicviewerdocumentchangededit
                        .setNewValueBoolean(isAutoResize());
                return;
        }
        super.copyNewValueForRedo(graphicviewerdocumentchangededit);
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 2301 :
                setLabel((GraphicViewerText) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2302 :
                setBackground((GraphicViewerObject) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2303 :
                setTopPort((GraphicViewerPort) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2304 :
                setRightPort((GraphicViewerPort) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2305 :
                setBottomPort((GraphicViewerPort) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2306 :
                setLeftPort((GraphicViewerPort) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;

            case 2307 :
                _mthif((Insets) graphicviewerdocumentchangededit.getValue(flag),
                        true);
                return;

            case 2308 :
                _mthfor(graphicviewerdocumentchangededit.getValueBoolean(flag),
                        true);
                return;
        }
        super.changeValue(graphicviewerdocumentchangededit, flag);
    }

    public void SVGUpdateReference(String s1, Object obj) {
        super.SVGUpdateReference(s1, obj);
        if (s1.equals("textlabel"))
            dn = (GraphicViewerText) obj;
        else if (s1.equals("backdraw"))
            dm = (GraphicViewerObject) obj;
        else if (s1.equals("topport"))
            dp = (GraphicViewerPort) obj;
        else if (s1.equals("rightport"))
            dq = (GraphicViewerPort) obj;
        else if (s1.equals("bottomport"))
            dj = (GraphicViewerPort) obj;
        else if (s1.equals("leftport"))
            di = (GraphicViewerPort) obj;
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerTextNode",
                            domelement);
            domelement1.setAttribute("insetleft", Integer.toString(dl.left));
            domelement1.setAttribute("insetright", Integer.toString(dl.right));
            domelement1.setAttribute("insettop", Integer.toString(dl.top));
            domelement1
                    .setAttribute("insetbottom", Integer.toString(dl.bottom));
            if (dn != null)
                domdoc.registerReferencingNode(domelement1, "textlabel", dn);
            if (dm != null)
                domdoc.registerReferencingNode(domelement1, "backdraw", dm);
            if (dp != null)
                domdoc.registerReferencingNode(domelement1, "topport", dp);
            if (dq != null)
                domdoc.registerReferencingNode(domelement1, "rightport", dq);
            if (dj != null)
                domdoc.registerReferencingNode(domelement1, "bottomport", dj);
            if (di != null)
                domdoc.registerReferencingNode(domelement1, "leftport", di);
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, IDomElement domelement,
            IDomElement domelement1) {
        if (domelement1 != null) {
            dl.left = Integer.parseInt(domelement1.getAttribute("insetleft"));
            dl.right = Integer.parseInt(domelement1.getAttribute("insetright"));
            dl.top = Integer.parseInt(domelement1.getAttribute("insettop"));
            dl.bottom = Integer.parseInt(domelement1
                    .getAttribute("insetbottom"));
            String s1 = domelement1.getAttribute("textlabel");
            domdoc.registerReferencingObject(this, "textlabel", s1);
            String s2 = domelement1.getAttribute("backdraw");
            domdoc.registerReferencingObject(this, "backdraw", s2);
            String s3 = domelement1.getAttribute("topport");
            domdoc.registerReferencingObject(this, "topport", s3);
            String s4 = domelement1.getAttribute("rightport");
            domdoc.registerReferencingObject(this, "rightport", s4);
            String s5 = domelement1.getAttribute("bottomport");
            domdoc.registerReferencingObject(this, "bottomport", s5);
            String s6 = domelement1.getAttribute("leftport");
            domdoc.registerReferencingObject(this, "leftport", s6);
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        }
        return domelement.getNextSibling();
    }

    public static final int ChangedLabel = 2301;
    public static final int ChangedBackground = 2302;
    public static final int ChangedTopPort = 2303;
    public static final int ChangedRightPort = 2304;
    public static final int ChangedBottomPort = 2305;
    public static final int ChangedLeftPort = 2306;
    public static final int ChangedInsets = 2307;
    public static final int ChangedAutoResize = 2308;
    private GraphicViewerText dn;
    private GraphicViewerObject dm;
    private GraphicViewerPort dp;
    private GraphicViewerPort dq;
    private GraphicViewerPort dj;
    private GraphicViewerPort di;
    private Insets dl;
}