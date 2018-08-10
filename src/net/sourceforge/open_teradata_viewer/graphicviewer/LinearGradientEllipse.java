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

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Rectangle;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class LinearGradientEllipse extends GraphicViewerEllipse {

    private static final long serialVersionUID = -6150951656987679358L;

    public LinearGradientEllipse() {
        myStartColor = Color.white;
        myEndColor = Color.gray;
    }

    public void geometryChange(Rectangle rectangle) {
        super.geometryChange(rectangle);
        setBrush(null);
    }

    public GraphicViewerBrush getBrush() {
        GraphicViewerBrush graphicviewerbrush = super.getBrush();
        if (graphicviewerbrush == null) {
            Rectangle rectangle = getBoundingRect();
            graphicviewerbrush = new GraphicViewerBrush(new GradientPaint(
                    rectangle.x + rectangle.width / 2, rectangle.y,
                    getStartColor(), rectangle.x + rectangle.width / 2,
                    rectangle.y + rectangle.height, getEndColor()));
            setBrush(graphicviewerbrush);
        }
        return graphicviewerbrush;
    }

    public Color getStartColor() {
        return myStartColor;
    }

    public void setStartColor(Color color) {
        Color color1 = myStartColor;
        if (color1 != color) {
            myStartColor = color;
            setBrush(null);
            update(0x101ff, 0, color1);
        }
    }

    public Color getEndColor() {
        return myEndColor;
    }

    public void setEndColor(Color color) {
        Color color1 = myEndColor;
        if (color1 != color) {
            myEndColor = color;
            setBrush(null);
            update(0x10200, 0, color1);
        }
    }

    public void copyNewValueForRedo(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 66047 :
                graphicviewerdocumentchangededit.setNewValue(getStartColor());
                return;

            case 66048 :
                graphicviewerdocumentchangededit.setNewValue(getEndColor());
                return;
        }
        super.copyNewValueForRedo(graphicviewerdocumentchangededit);
    }

    public void changeValue(
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit,
            boolean flag) {
        switch (graphicviewerdocumentchangededit.getFlags()) {
            case 1 : // '\001'
                super.changeValue(graphicviewerdocumentchangededit, flag);
                setBrush(null);
                break;

            case 66047 :
                setStartColor((Color) graphicviewerdocumentchangededit
                        .getValue(flag));
                break;

            case 66048 :
                setEndColor((Color) graphicviewerdocumentchangededit
                        .getValue(flag));
                break;

            default :
                super.changeValue(graphicviewerdocumentchangededit, flag);
                break;
        }
    }

    public static final int ChangedStartColor = 0x101ff;
    public static final int ChangedEndColor = 0x10200;
    private Color myStartColor;
    private Color myEndColor;
}