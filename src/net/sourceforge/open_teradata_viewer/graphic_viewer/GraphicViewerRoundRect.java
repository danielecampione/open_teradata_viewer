/*
 * Open Teradata Viewer ( graphic viewer )
 * Copyright (C) 2015, D. Campione
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
import java.awt.Point;
import java.awt.Rectangle;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerRoundRect extends GraphicViewerDrawable {

    private static final long serialVersionUID = 3411129146763651958L;

    public static final int ChangedArcDimension = 404;
    private Dimension myArcDimension = new Dimension();

    public GraphicViewerRoundRect() {
        init(5, 5);
    }

    public GraphicViewerRoundRect(Dimension paramDimension) {
        init(paramDimension.width, paramDimension.height);
    }

    public GraphicViewerRoundRect(Rectangle paramRectangle,
            Dimension paramDimension) {
        super(paramRectangle);
        init(paramDimension.width, paramDimension.height);
    }

    public GraphicViewerRoundRect(Point paramPoint, Dimension paramDimension1,
            Dimension paramDimension2) {
        super(paramPoint, paramDimension1);
        init(paramDimension2.width, paramDimension2.height);
    }

    private final void init(int paramInt1, int paramInt2) {
        myArcDimension.width = paramInt1;
        myArcDimension.height = paramInt2;
    }

    public GraphicViewerObject copyObject(
            IGraphicViewerCopyEnvironment paramGraphicViewerCopyEnvironment) {
        GraphicViewerRoundRect localGraphicViewerRoundRect = (GraphicViewerRoundRect) super
                .copyObject(paramGraphicViewerCopyEnvironment);
        if (localGraphicViewerRoundRect != null) {
            localGraphicViewerRoundRect.myArcDimension.width = myArcDimension.width;
            localGraphicViewerRoundRect.myArcDimension.height = myArcDimension.height;
        }
        return localGraphicViewerRoundRect;
    }

    public Dimension getArcDimension() {
        return myArcDimension;
    }

    public void setArcDimension(Dimension paramDimension) {
        Dimension localDimension1 = myArcDimension;
        if ((localDimension1.width != paramDimension.width)
                || (localDimension1.height != paramDimension.height)) {
            Dimension localDimension2 = new Dimension(localDimension1);
            myArcDimension.width = paramDimension.width;
            myArcDimension.height = paramDimension.height;
            update(404, 0, localDimension2);
        }
    }

    public void copyOldValueForUndo(
            GraphicViewerDocumentChangedEdit paramGraphicViewerDocumentChangedEdit) {
        switch (paramGraphicViewerDocumentChangedEdit.getFlags()) {
            case 404 :
                paramGraphicViewerDocumentChangedEdit
                        .setOldValue(new Dimension(
                                (Dimension) paramGraphicViewerDocumentChangedEdit
                                        .getOldValue()));
                return;
        }
        super.copyOldValueForUndo(paramGraphicViewerDocumentChangedEdit);
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit paramGraphicViewerDocumentChangedEdit) {
        switch (paramGraphicViewerDocumentChangedEdit.getFlags()) {
            case 404 :
                paramGraphicViewerDocumentChangedEdit
                        .setNewValue(new Dimension(getArcDimension()));
                return;
        }
        super.copyNewValueForRedo(paramGraphicViewerDocumentChangedEdit);
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit paramGraphicViewerDocumentChangedEdit,
            boolean paramBoolean) {
        switch (paramGraphicViewerDocumentChangedEdit.getFlags()) {
            case 404 :
                setArcDimension((Dimension) paramGraphicViewerDocumentChangedEdit
                        .getValue(paramBoolean));
                return;
        }
        super.changeValue(paramGraphicViewerDocumentChangedEdit, paramBoolean);
    }

    public void SVGWriteObject(IDomDoc paramDomDoc, IDomElement paramDomElement) {
        IDomElement localDomElement;
        if (paramDomDoc.GraphicViewerXMLOutputEnabled()) {
            localDomElement = paramDomDoc
                    .createGraphicViewerClassElement(
                            "net.sourceforge.open_teradata_viewer.graphic_viewer.GraphicViewerRoundRect",
                            paramDomElement);
            localDomElement.setAttribute("arcwidth",
                    Integer.toString(myArcDimension.width));
            localDomElement.setAttribute("archeight",
                    Integer.toString(myArcDimension.height));
        }
        if (paramDomDoc.SVGOutputEnabled()) {
            localDomElement = paramDomDoc.createElement("rect");
            SVGWriteAttributes(localDomElement);
            paramDomElement.appendChild(localDomElement);
        }
        super.SVGWriteObject(paramDomDoc, paramDomElement);
    }

    public IDomNode SVGReadObject(IDomDoc paramDomDoc,
            GraphicViewerDocument paramGraphicViewerDocument,
            IDomElement paramDomElement1, IDomElement paramDomElement2) {
        if (paramDomElement2 != null) {
            int i = Integer.parseInt(paramDomElement2.getAttribute("arcwidth"));
            int j = Integer
                    .parseInt(paramDomElement2.getAttribute("archeight"));
            setArcDimension(new Dimension(i, j));
            super.SVGReadObject(paramDomDoc, paramGraphicViewerDocument,
                    paramDomElement1,
                    paramDomElement2.getNextSiblingGraphicViewerClassElement());
        }
        return paramDomElement1.getNextSibling();
    }

    public void SVGWriteAttributes(IDomElement paramDomElement) {
        super.SVGWriteAttributes(paramDomElement);
        paramDomElement.setAttribute("x", Integer.toString(getTopLeft().x));
        paramDomElement.setAttribute("y", Integer.toString(getTopLeft().y));
        paramDomElement.setAttribute("width", Integer.toString(getWidth()));
        paramDomElement.setAttribute("height", Integer.toString(getHeight()));
        paramDomElement.setAttribute("rx",
                Double.toString(getArcDimension().width / 2));
        paramDomElement.setAttribute("ry",
                Double.toString(getArcDimension().height / 2));
    }

    public void SVGReadAttributes(IDomElement paramDomElement) {
        super.SVGReadAttributes(paramDomElement);
        String str1 = paramDomElement.getAttribute("x");
        String str2 = paramDomElement.getAttribute("y");
        setTopLeft(new Point(Integer.parseInt(str1), Integer.parseInt(str2)));
        String str3 = paramDomElement.getAttribute("width");
        String str4 = paramDomElement.getAttribute("height");
        setWidth(Integer.parseInt(str3));
        setHeight(Integer.parseInt(str4));
    }

    public void paint(Graphics2D paramGraphics2D,
            GraphicViewerView paramGraphicViewerView) {
        Rectangle localRectangle = getBoundingRect();
        drawRoundRect(paramGraphics2D, localRectangle.x, localRectangle.y,
                localRectangle.width, localRectangle.height,
                myArcDimension.width, myArcDimension.height);
    }
}