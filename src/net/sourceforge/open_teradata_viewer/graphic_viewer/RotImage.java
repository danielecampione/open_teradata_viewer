/*
 * Open Teradata Viewer ( graphic viewer )
 * Copyright (C), D. Campione
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
import java.awt.geom.AffineTransform;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class RotImage extends GraphicViewerImage {

    private static final long serialVersionUID = 7031214743629079644L;

    public static final int ChangedAngle = LastChangedHint + 1;
    public static final int ChangedRotatable = LastChangedHint + 2;
    public static final int ChangedFlipped = LastChangedHint + 3;

    private double myAngle = 0;
    private boolean myFlipped = false;
    private boolean myRotatable = true;

    public RotImage() {
        super();
    }

    public RotImage(Point loc, Dimension size) {
        super(loc, size);
    }

    public RotImage(Rectangle r) {
        super(r);
    }

    public GraphicViewerObject copyObject(IGraphicViewerCopyEnvironment env) {
        RotImage newobj = (RotImage) super.copyObject(env);
        if (newobj != null) {
            newobj.myAngle = myAngle;
            newobj.myFlipped = myFlipped;
        }
        return newobj;
    }

    protected void gainedSelection(GraphicViewerSelection selection) {
        GraphicViewerHandle h = selection.createBoundingHandle(this);
        h.setBoundingRect(getBoundingRect());
        if (isRotatable()) {
            Rectangle r = getBoundingRect();
            int x = r.x + r.width / 2;
            int y = r.y + r.height / 2;
            double a = getAngle();
            if (a == 0) {
                x += 50;
            } else if (a == 90) {
                y += 50;
            } else if (a == 180) {
                x -= 50;
            } else if (a == 270) {
                y -= 50;
            } else {
                double t = a * Math.PI / 180;
                x += (int) Math.rint(50 * Math.cos(t));
                y += (int) Math.rint(50 * Math.sin(t));
            }
            GraphicViewerHandle goh = selection.createResizeHandle(this, x - 2,
                    y - 2, NumReservedHandles + 1, true);
            if (goh != null) {
                goh.setBrush(GraphicViewerBrush.yellow);
            }
        }
    }

    public Rectangle handleResize(Graphics2D g, GraphicViewerView view,
            Rectangle origRect, Point newPoint, int whichHandle, int event,
            int minWidth, int minHeight) {
        if (whichHandle == NumReservedHandles + 1) {
            float cx = getLeft() + getWidth() / 2.0f;
            float cy = getTop() + getHeight() / 2.0f;
            double a = GetAngle(newPoint.x - cx, newPoint.y - cy);
            double b = computeAngle(a);
            setAngle(b);
            return null;
        } else {
            return super.handleResize(g, view, origRect, newPoint, whichHandle,
                    event, minWidth, minHeight);
        }
    }

    // Calculate the permissible new angle given an angle specified by
    // interactive dragging of the special resize handle
    public double computeAngle(double a) {
        if (a >= 315 || (a >= -45 && a <= 45)) {
            return 0;
        }
        if (a > 45 && a < 135) {
            return 90;
        }
        if (a >= 135 && a <= 225) {
            return 180;
        }
        return 270;
    }

    static float GetAngle(float x, float y) {
        float A;
        if (x == 0) {
            if (y > 0) {
                A = 90;
            } else {
                A = 270;
            }
        } else if (y == 0) {
            if (x > 0) {
                A = 0;
            } else {
                A = 180;
            }
        } else {
            A = (float) (Math.atan(Math.abs(y / x)) * 180 / Math.PI);
            if (x < 0) {
                if (y < 0) {
                    A += 180;
                } else {
                    A = 180 - A;
                }
            } else if (y < 0) {
                A = 360 - A;
            }
        }
        return A;
    }

    public double getAngle() {
        return myAngle;
    }

    public void setAngle(double angle) {
        double old = myAngle;
        if (old != angle && validAngle(angle)) {
            myAngle = angle;
            update(ChangedAngle, 0, new Double(old));
            if (((old == 0 || old == 180) && (angle == 90 || angle == 270))
                    || ((old == 90 || old == 270) && (angle == 0 || angle == 180))) {
                Rectangle r = getBoundingRect();
                setBoundingRect(r.x + r.width / 2 - r.height / 2, r.y
                        + r.height / 2 - r.width / 2, r.height, r.width);
            }
        }
    }

    public boolean validAngle(double a) {
        return a == 0 || a == 90 || a == 180 || a == 270;
    }

    public boolean isFlipped() {
        return myFlipped;
    }

    public void setFlipped(boolean b) {
        boolean old = myFlipped;
        if (old != b) {
            myFlipped = b;
            update(ChangedFlipped, (old ? 1 : 0), null);
        }
    }

    public boolean isRotatable() {
        return myRotatable;
    }

    public void setRotatable(boolean r) {
        boolean old = myRotatable;
        if (old != r) {
            myRotatable = r;
            update(ChangedRotatable, (old ? 1 : 0), null);
        }
    }

    public void paint(Graphics2D g, GraphicViewerView view) {
        if (getImage() == null) {
            if (getURL() != null) {
                loadImage(getURL(), false);
            } else if (getFilename() != null) {
                loadImage(getFilename(), false);
            }
        }

        // Only draw if we've loaded an image..
        if (getImage() != null) {
            Rectangle rect = getBoundingRect();
            Dimension natsize = getNaturalSize();
            AffineTransform xform = new AffineTransform();
            double a = getAngle() * Math.PI / 180;
            xform.translate(rect.x + rect.width / 2, rect.y + rect.height / 2);
            xform.rotate(a);
            if (isFlipped()) {
                xform.scale(1, -1);
            }
            if (a == 0 || a == 180) {
                xform.scale(rect.width / (double) natsize.width, rect.height
                        / (double) natsize.height);
            } else {
                xform.scale(rect.height / (double) natsize.width, rect.width
                        / (double) natsize.height);
            }
            xform.translate(-natsize.width / 2, -natsize.height / 2);
            g.drawImage(getImage(), xform, this);
        }
    }

    public void copyNewValueForRedo(GraphicViewerDocumentChangedEdit e) {
        switch (e.getFlags()) {
            case ChangedAngle :
                e.setNewValue(new Double(getAngle()));
                return;
            case ChangedRotatable :
                e.setNewValueBoolean(isRotatable());
                return;
            case ChangedFlipped :
                e.setNewValueBoolean(isFlipped());
                return;
            default :
                super.copyNewValueForRedo(e);
                return;
        }
    }

    public void changeValue(GraphicViewerDocumentChangedEdit e, boolean undo) {
        switch (e.getFlags()) {
            case ChangedAngle :
                setAngle(((Double) e.getValue(undo)).doubleValue());
                return;
            case ChangedRotatable :
                setRotatable(e.getValueBoolean(undo));
                return;
            case ChangedFlipped :
                setFlipped(e.getValueBoolean(undo));
                return;
            default :
                super.changeValue(e, undo);
                return;
        }
    }
}