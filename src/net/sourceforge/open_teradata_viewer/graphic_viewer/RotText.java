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

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class RotText extends GraphicViewerText {

    private static final long serialVersionUID = 5788926885477887209L;

    public static final int ChangedAngle = LastChangedHint + 1;
    public static final int ChangedRotatable = LastChangedHint + 2;

    // State  
    private double myAngle = 0;
    private boolean myRotatable = true;
    transient private Rectangle myRealBounds = null;
    transient private Shape myShape = null;

    public RotText() {
        setResizable(true);
    }

    public GraphicViewerObject copyObject(IGraphicViewerCopyEnvironment env) {
        RotText newobj = (RotText) super.copyObject(env);
        if (newobj != null) {
            newobj.myAngle = myAngle;
        }
        return newobj;
    }

    public void paint(Graphics2D g, GraphicViewerView view) {
        double angle = getAngle();
        if (angle != 0) {
            angle = angle * Math.PI / 180;
            Rectangle r = getBoundingRect();
            g.rotate(angle, r.x + r.width / 2, r.y + r.height / 2);
            super.paint(g, view);
            g.rotate(-angle, r.x + r.width / 2, r.y + r.height / 2);
        } else {
            super.paint(g, view);
        }
    }

    public void expandRectByPenWidth(Rectangle rect) {
        if (getAngle() != 0) {
            rect.setBounds(getRealBounds());
        }
        super.expandRectByPenWidth(rect);
    }

    public void setBoundingRect(int left, int top, int width, int height) {
        super.setBoundingRect(left, top, width, height);
        clearRealBounds();
    }

    public boolean isPointInObj(Point pnt) {
        if (getAngle() != 0) {
            getRealBounds();
            return myShape.contains(pnt);
        } else {
            return super.isPointInObj(pnt);
        }
    }

    protected void gainedSelection(GraphicViewerSelection selection) {
        if (isSelectBackground()) {
            setTransparent(false);
        } else {
            GraphicViewerHandle h = selection.createBoundingHandle(this);
            h.setBoundingRect(getRealBounds());
        }
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

    // Allow only multiples of 10 degrees for new angles specified by
    // interactive dragging of the special resize handle
    public double computeAngle(double a) {
        return (((int) a) / 10) * 10;
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

    public void setAngle(double a) {
        double olda = myAngle;
        if (olda != a) {
            myAngle = a;
            update(ChangedAngle, 0, new Double(olda));
            clearRealBounds();
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

    public Rectangle getRealBounds() {
        if (myRealBounds == null) {
            updateRealBounds(getBoundingRect());
        }
        return myRealBounds;
    }

    private void clearRealBounds() {
        myRealBounds = null;
        myShape = null;
    }

    private void updateRealBounds(Rectangle r) {
        double angle = getAngle() * Math.PI / 180;
        int cx = r.x + r.width / 2;
        int cy = r.y + r.height / 2;
        java.awt.geom.AffineTransform t = java.awt.geom.AffineTransform
                .getRotateInstance(angle, cx, cy);
        Rectangle xr = new Rectangle(r);
        myShape = t.createTransformedShape(xr);
        myRealBounds = myShape.getBounds();
    }

    public void copyNewValueForRedo(GraphicViewerDocumentChangedEdit e) {
        switch (e.getFlags()) {
            case ChangedAngle :
                e.setNewValue(new Double(getAngle()));
                return;
            case ChangedRotatable :
                e.setNewValueBoolean(isRotatable());
                return;
            default :
                super.copyNewValueForRedo(e);
                return;
        }
    }

    public void changeValue(GraphicViewerDocumentChangedEdit e, boolean undo) {
        switch (e.getFlags()) {
            case ChangedAngle :
                setAngle(e.getValueDouble(undo));
                return;
            case ChangedRotatable :
                setRotatable(e.getValueBoolean(undo));
                return;
            default :
                super.changeValue(e, undo);
                return;
        }
    }
}