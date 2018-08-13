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

    public static final int ChangedStartColor = LastChangedHint + 512;
    public static final int ChangedEndColor = LastChangedHint + 513;

    private Color myStartColor = Color.white;
    private Color myEndColor = Color.gray;

    public LinearGradientEllipse() {
        super();
    }

    // Make sure there's a color gradient going from the top-middle to the
    // bottom middle of the ellipse, no matter where the ellipse is or how big
    // it is
    public void geometryChange(Rectangle prevRect) {
        super.geometryChange(prevRect);
        setBrush(null);
    }

    public GraphicViewerBrush getBrush() {
        GraphicViewerBrush b = super.getBrush();
        if (b == null) {
            Rectangle rect = getBoundingRect();
            b = new GraphicViewerBrush(new GradientPaint(rect.x + rect.width
                    / 2, rect.y, getStartColor(), rect.x + rect.width / 2,
                    rect.y + rect.height, getEndColor()));
            setBrush(b);
        }
        return b;
    }

    public Color getStartColor() {
        return myStartColor;
    }
    public void setStartColor(Color value) {
        Color old = myStartColor;
        if (old != value) {
            myStartColor = value;
            setBrush(null);
            update(ChangedStartColor, 0, old);
        }
    }

    public Color getEndColor() {
        return myEndColor;
    }
    public void setEndColor(Color value) {
        Color old = myEndColor;
        if (old != value) {
            myEndColor = value;
            setBrush(null);
            update(ChangedEndColor, 0, old);
        }
    }

    public void copyNewValueForRedo(GraphicViewerDocumentChangedEdit e) {
        switch (e.getFlags()) {
            case ChangedStartColor :
                e.setNewValue(getStartColor());
                return;
            case ChangedEndColor :
                e.setNewValue(getEndColor());
                return;
            default :
                super.copyNewValueForRedo(e);
                return;
        }
    }

    public void changeValue(GraphicViewerDocumentChangedEdit e, boolean undo) {
        switch (e.getFlags()) {
            case ChangedGeometry :
                super.changeValue(e, undo);
                setBrush(null);
                break;
            case ChangedStartColor :
                setStartColor((Color) e.getValue(undo));
                break;
            case ChangedEndColor :
                setEndColor((Color) e.getValue(undo));
                break;
            default :
                super.changeValue(e, undo);
                break;
        }
    }
}