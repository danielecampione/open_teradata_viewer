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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerButton extends GraphicViewerControl {

    private static final long serialVersionUID = -4569987445267136858L;

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    class JBListener implements ActionListener {

        public void actionPerformed(ActionEvent actionevent) {
            a.fireUpdate(22, 0, _fldif);
            Point point = _fldif.getSpotLocation(0);
            Point point1 = a.docToViewCoords(point);
            _fldif.doMouseClick(0, point, point1, a);
        }

        GraphicViewerButton _fldif;
        GraphicViewerView a;

        JBListener(GraphicViewerButton graphicviewerbutton1,
                GraphicViewerView graphicviewerview) {
            _fldif = graphicviewerbutton1;
            a = graphicviewerview;
        }
    }

    public GraphicViewerButton() {
        dU = "";
        dV = 10;
    }

    public GraphicViewerButton(Rectangle rectangle, String s) {
        super(rectangle);
        dU = "";
        dV = 10;
        dU = s;
    }

    public GraphicViewerButton(Point point, Dimension dimension, String s) {
        super(point, dimension);
        dU = "";
        dV = 10;
        dU = s;
    }

    public GraphicViewerObject copyObject(
            IGraphicViewerCopyEnvironment graphicviewercopyenvironment) {
        GraphicViewerButton graphicviewerbutton = (GraphicViewerButton) super
                .copyObject(graphicviewercopyenvironment);
        if (graphicviewerbutton != null) {
            graphicviewerbutton.dU = dU;
            graphicviewerbutton.dV = dV;
        }
        return graphicviewerbutton;
    }

    public JComponent createComponent(GraphicViewerView graphicviewerview) {
        JButton jbutton = new JButton(getLabel());
        Font font = jbutton.getFont();
        dV = font.getSize();
        jbutton.addActionListener(new JBListener(this, graphicviewerview));
        return jbutton;
    }

    public void SVGWriteObject(IDomDoc domdoc, IDomElement domelement) {
        if (domdoc.GraphicViewerXMLOutputEnabled()) {
            IDomElement domelement1 = domdoc.createGraphicViewerClassElement(
                    "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerButton",
                    domelement);
            domelement1.setAttribute("fontsize", Integer.toString(dV));
            domelement1.setAttribute("label", dU);
        }
        if (domdoc.SVGOutputEnabled()) {
            IDomElement domelement2 = domdoc.createElement("rect");
            SVGWriteAttributes(domelement2);
            domelement.appendChild(domelement2);
        }
        super.SVGWriteObject(domdoc, domelement);
    }

    public IDomNode SVGReadObject(IDomDoc domdoc,
            GraphicViewerDocument graphicviewerdocument, IDomElement domelement,
            IDomElement domelement1) {
        if (domelement1 != null) {
            dV = Integer.parseInt(domelement1.getAttribute("fontsize"));
            dU = domelement1.getAttribute("label");
            super.SVGReadObject(domdoc, graphicviewerdocument, domelement,
                    domelement1.getNextSiblingGraphicViewerClassElement());
        }
        return domelement.getNextSibling();
    }

    public void SVGWriteAttributes(IDomElement domelement) {
        super.SVGWriteAttributes(domelement);
        domelement.setAttribute("x", Integer.toString(getTopLeft().x));
        domelement.setAttribute("y", Integer.toString(getTopLeft().y));
        domelement.setAttribute("width", Integer.toString(getWidth()));
        domelement.setAttribute("height", Integer.toString(getHeight()));
        domelement.setAttribute("style",
                "stroke:black;stroke-width:1;fill:none");
    }

    public void SVGReadAttributes(IDomElement domelement) {
        super.SVGReadAttributes(domelement);
        String s = domelement.getAttribute("x");
        String s1 = domelement.getAttribute("y");
        setTopLeft(new Point(Integer.parseInt(s), Integer.parseInt(s1)));
        String s2 = domelement.getAttribute("width");
        String s3 = domelement.getAttribute("height");
        setWidth(Integer.parseInt(s2));
        setHeight(Integer.parseInt(s3));
    }

    public void paint(Graphics2D graphics2d, GraphicViewerView graphicviewerview) {
        JComponent jcomponent = getComponent(graphicviewerview);
        if (jcomponent instanceof JButton) {
            JButton jbutton = (JButton) jcomponent;
            if (Math.abs(graphicviewerview.getScale() - 1.0D) >= 0.01D) {
                Font font = jbutton.getFont();
                Font font1 = new Font(font.getFontName(), font.getStyle(),
                        (int) ((double) dV * graphicviewerview.getScale()));
                jcomponent.setFont(font1);
            }
            Rectangle rectangle = graphicviewerview.c();
            Rectangle rectangle1 = getBoundingRect();
            rectangle.x = rectangle1.x;
            rectangle.y = rectangle1.y;
            rectangle.width = rectangle1.width;
            rectangle.height = rectangle1.height;
            graphicviewerview.convertDocToView(rectangle);
            jcomponent.setBounds(rectangle);
            jbutton.setText(getLabel());
            jcomponent.repaint();
        }
    }

    public String getLabel() {
        return dU;
    }

    public void setLabel(String s) {
        String s1 = dU;
        if (!s1.equals(s)) {
            dU = s == null ? "" : s;
            update(1001, 0, s1);
        }
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 1001 :
                graphicviewerdocumentchangededit.setNewValue(getLabel());
                return;
        }
        super.copyNewValueForRedo(graphicviewerdocumentchangededit);
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 1001 :
                setLabel((String) graphicviewerdocumentchangededit
                        .getValue(flag));
                return;
        }
        super.changeValue(graphicviewerdocumentchangededit, flag);
    }

    public static final int ChangedLabel = 1001;
    private String dU;
    private int dV;
}