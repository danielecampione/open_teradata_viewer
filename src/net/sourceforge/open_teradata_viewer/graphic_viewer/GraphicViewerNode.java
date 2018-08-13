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

import java.util.ArrayList;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerNode extends GraphicViewerArea
        implements
            IGraphicViewerLabeledPart,
            IGraphicViewerIdentifiablePart {

    private static final long serialVersionUID = -8398199369467123389L;

    public static final int ChangedPartID = 2001;
    public static final int ChangedUserObject = 2002;
    public static final int ChangedToolTipText = 2003;
    public static final int PartsPorts = 1;
    public static final int PartsLinksIn = 2;
    public static final int PartsLinksOut = 4;
    public static final int PartsNodesIn = 8;
    public static final int PartsNodesOut = 16;
    public static final int PartsNotSelf = 32;
    public static final int PartsNodes = 24;
    public static final int PartsLinks = 6;
    private int myPartID = -1;
    private Object myUserObject = null;
    private String myToolTipText = null;

    public GraphicViewerNode() {
        setInternalFlags(getInternalFlags() & 0xfffffbff);
    }

    public GraphicViewerObject copyObject(
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerNode graphicviewernode = (GraphicViewerNode) super
                .copyObject(graphicviewercopyenvironment);
        if (graphicviewernode != null) {
            graphicviewernode.myPartID = myPartID;
            graphicviewernode.myUserObject = myUserObject;
            graphicviewernode.myToolTipText = myToolTipText;
        }
        return graphicviewernode;
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerNode",
                            domelement);
            domelement1.setAttribute("partid", Integer.toString(myPartID));
            if (myToolTipText != null) {
                domelement1.setAttribute("tooltiptext", myToolTipText);
            }
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument,
            IDomElement domelement, IDomElement domelement1) {
        if (domelement1 != null) {
            if (domdoc.getGraphicViewerSVGVersion() < 2D
                    && !domelement1.getLocalName().equals("GraphicViewerNode")) {
                return super.SVGReadObject(domdoc, graphicviewerdocument,
                        domelement, domelement1);
            }
            String s = domelement1.getAttribute("partid");
            if (s.length() > 0) {
                myPartID = Integer.parseInt(s);
            }
            String s1 = domelement1.getAttribute("tooltiptext");
            myToolTipText = s1;
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
            return domelement.getNextSibling();
        } else {
            return domelement.getNextSibling();
        }
    }

    public void SVGUpdateReference(String s, Object obj) {
        super.SVGUpdateReference(s, obj);
    }

    public String getText() {
        GraphicViewerText graphicviewertext = getLabel();
        if (graphicviewertext != null) {
            return graphicviewertext.getText();
        } else {
            return "";
        }
    }

    public void setText(String s) {
        GraphicViewerText graphicviewertext = getLabel();
        if (graphicviewertext != null) {
            graphicviewertext.setText(s);
        }
    }

    public GraphicViewerText getLabel() {
        return findLabel(this);
    }

    public void setLabel(GraphicViewerText graphicviewertext) {
    }

    static GraphicViewerText findLabel(GraphicViewerObject graphicviewerobject) {
        label0 : {
            if (graphicviewerobject instanceof GraphicViewerText) {
                return (GraphicViewerText) graphicviewerobject;
            }
            if (!(graphicviewerobject instanceof GraphicViewerArea)) {
                break label0;
            }
            GraphicViewerArea graphicviewerarea = (GraphicViewerArea) graphicviewerobject;
            GraphicViewerListPosition graphicviewerlistposition = graphicviewerarea
                    .getFirstObjectPos();
            GraphicViewerText graphicviewertext;
            do {
                if (graphicviewerlistposition == null) {
                    break label0;
                }
                GraphicViewerObject graphicviewerobject1 = graphicviewerarea
                        .getObjectAtPos(graphicviewerlistposition);
                graphicviewerlistposition = graphicviewerarea
                        .getNextObjectPos(graphicviewerlistposition);
                graphicviewertext = findLabel(graphicviewerobject1);
            } while (graphicviewertext == null);
            return graphicviewertext;
        }
        return null;
    }

    public int getPartID() {
        return myPartID;
    }

    public void setPartID(int i) {
        int j = myPartID;
        if (j != i) {
            myPartID = i;
            update(2001, j, null);
        }
    }

    public Object getUserObject() {
        return myUserObject;
    }

    public void setUserObject(Object obj) {
        Object obj1 = myUserObject;
        if (obj1 != obj) {
            myUserObject = obj;
            update(2002, 0, obj1);
        }
    }

    public String getToolTipText() {
        return myToolTipText;
    }

    public void setToolTipText(String s) {
        String s1 = myToolTipText;
        if (s1 != s || s1 != null && !s1.equals(s)) {
            myToolTipText = s;
            update(2003, 0, s1);
        }
    }

    static void copyProperties(GraphicViewerObject graphicviewerobject,
            GraphicViewerObject graphicviewerobject1) {
        if (graphicviewerobject != null && graphicviewerobject1 != null) {
            graphicviewerobject1.setSpotLocation(0,
                    graphicviewerobject.getSpotLocation(0));
            graphicviewerobject1.setSelectable(graphicviewerobject
                    .isSelectable());
            graphicviewerobject1
                    .setResizable(graphicviewerobject.isResizable());
        }
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 2001 :
                graphicviewerdocumentchangededit.setNewValueInt(getPartID());
                return;

            case 2002 :
                graphicviewerdocumentchangededit.setNewValue(getUserObject());
                return;

            case 2003 :
                graphicviewerdocumentchangededit.setNewValue(getToolTipText());
                return;
        }
        super.copyNewValueForRedo(graphicviewerdocumentchangededit);
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 2001 :
                setPartID(graphicviewerdocumentchangededit.getValueInt(flag));
                return;

            case 2002 :
                setUserObject(graphicviewerdocumentchangededit.getValue(flag));
                return;

            case 2003 :
                setToolTipText((String) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;
        }
        super.changeValue(graphicviewerdocumentchangededit, flag);
    }

    public ArrayList findAll(int i, ArrayList arraylist) {
        if (arraylist == null) {
            arraylist = new ArrayList();
        } else {
            arraylist.clear();
        }
        findAllAux(this, i, arraylist);
        return arraylist;
    }

    private void findAllAux(GraphicViewerObject graphicviewerobject, int i,
            ArrayList arraylist) {
        if (graphicviewerobject != null
                && (graphicviewerobject instanceof GraphicViewerPort)) {
            if ((i & 1) != 0) {
                addItem(arraylist, graphicviewerobject);
            }
            GraphicViewerPort graphicviewerport = (GraphicViewerPort) graphicviewerobject;
            for (GraphicViewerListPosition graphicviewerlistposition = graphicviewerport
                    .getFirstLinkPos(); graphicviewerlistposition != null;) {
                GraphicViewerLink graphicviewerlink = graphicviewerport
                        .getLinkAtPos(graphicviewerlistposition);
                graphicviewerlistposition = graphicviewerport
                        .getNextLinkPos(graphicviewerlistposition);
                considerLink(graphicviewerlink, graphicviewerport, i, arraylist);
            }

        }
        if (graphicviewerobject != null
                && (graphicviewerobject instanceof GraphicViewerArea)) {
            GraphicViewerArea graphicviewerarea = (GraphicViewerArea) graphicviewerobject;
            for (GraphicViewerListPosition graphicviewerlistposition1 = graphicviewerarea
                    .getFirstObjectPos(); graphicviewerlistposition1 != null;) {
                GraphicViewerObject graphicviewerobject1 = graphicviewerarea
                        .getObjectAtPos(graphicviewerlistposition1);
                graphicviewerlistposition1 = graphicviewerarea
                        .getNextObjectPos(graphicviewerlistposition1);
                findAllAux(graphicviewerobject1, i, arraylist);
            }

        }
    }

    private void addItem(ArrayList arraylist,
            GraphicViewerObject graphicviewerobject) {
        if (graphicviewerobject != null
                && !arraylist.contains(graphicviewerobject)) {
            arraylist.add(graphicviewerobject);
        }
    }

    private void considerLink(GraphicViewerLink graphicviewerlink,
            GraphicViewerPort graphicviewerport, int i, ArrayList arraylist) {
        boolean flag = (i & 0x20) == 0;
        if (graphicviewerlink.getFromPort() == graphicviewerport
                && (flag || graphicviewerlink.getToPort() == null || !graphicviewerlink
                        .getToPort().getPortObject().isChildOf(this))) {
            if ((i & 4) != 0) {
                addItem(arraylist, ((GraphicViewerObject) (graphicviewerlink)));
            }
            if ((i & 0x10) != 0) {
                addItem(arraylist,
                        ((GraphicViewerObject) (graphicviewerlink.getToPort()
                                .getParentGraphicViewerNode())));
            }
        }
        if (graphicviewerlink.getToPort() == graphicviewerport
                && (flag || graphicviewerlink.getFromPort() == null || !graphicviewerlink
                        .getFromPort().getPortObject().isChildOf(this))) {
            if ((i & 2) != 0) {
                addItem(arraylist, ((GraphicViewerObject) (graphicviewerlink)));
            }
            if ((i & 8) != 0) {
                addItem(arraylist,
                        ((GraphicViewerObject) (graphicviewerlink.getFromPort()
                                .getParentGraphicViewerNode())));
            }
        }
    }
}