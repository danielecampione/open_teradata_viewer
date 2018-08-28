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
import java.awt.Point;
import java.awt.Rectangle;

/**
 * 
 * 
 * @author D. Campione
 *
 */
class GraphicViewerPositionArray {

    static final int OCCUPIED = 0;
    static final int START = 1;
    static final int MAXDIST = 2147483647;
    private boolean myInvalid = true;
    private int myMinX = 1;
    private int myMinY = 1;
    private int myMaxX = -1;
    private int myMaxY = -1;
    private Rectangle myBounds = new Rectangle(1, 1, -2, -2);
    private int myCellX = 10;
    private int myCellY = 10;
    private Dimension myCellSize = new Dimension(10, 10);
    private int[][] myArray = (int[][]) null;
    private int myUpperBoundX = 0;
    private int myUpperBoundY = 0;

    public final void initialize(Rectangle paramRectangle) {
        if ((paramRectangle.width <= 0) || (paramRectangle.height <= 0)) {
            return;
        }
        int i = paramRectangle.x;
        int j = paramRectangle.y;
        int k = paramRectangle.x + paramRectangle.width;
        int m = paramRectangle.y + paramRectangle.height;
        this.myMinX = ((int) Math.floor((i - this.myCellX) / this.myCellX) * this.myCellX);
        this.myMinY = ((int) Math.floor((j - this.myCellY) / this.myCellY) * this.myCellY);
        this.myMaxX = ((int) Math.ceil((k + 2 * this.myCellX) / this.myCellX) * this.myCellX);
        this.myMaxY = ((int) Math.ceil((m + 2 * this.myCellY) / this.myCellY) * this.myCellY);
        this.myBounds.x = this.myMinX;
        this.myBounds.y = this.myMinY;
        this.myBounds.width = (this.myMaxX - this.myMinX);
        this.myBounds.height = (this.myMaxY - this.myMinY);
        int n = 1 + (int) Math.ceil((this.myMaxX - this.myMinX) / this.myCellX);
        int i1 = 1 + (int) Math
                .ceil((this.myMaxY - this.myMinY) / this.myCellY);
        if ((this.myArray == null) || (this.myUpperBoundX < n - 1)
                || (this.myUpperBoundY < i1 - 1)) {
            this.myArray = new int[n][i1];
            this.myUpperBoundX = (n - 1);
            this.myUpperBoundY = (i1 - 1);
        }
        setAll(2147483647);
    }

    public final boolean isInvalid() {
        return myInvalid;
    }

    public final void setInvalid(boolean flag) {
        myInvalid = flag;
    }

    public final Rectangle getBounds() {
        return myBounds;
    }

    public final Dimension getCellSize() {
        return myCellSize;
    }

    public final void setCellSize(Dimension dimension) {
        if (dimension.width >= 1 && dimension.height >= 1
                && (dimension.width != myCellX || dimension.height != myCellY)) {
            myCellX = dimension.width;
            myCellY = dimension.height;
            myCellSize.width = myCellX;
            myCellSize.height = myCellY;
            initialize(new Rectangle(myMinX, myMinY, myMaxX - myMinX, myMaxY
                    - myMinY));
        }
    }

    public final boolean inBounds(int i, int j) {
        return myMinX <= i && i <= myMaxX && myMinY <= j && j <= myMaxY;
    }

    public final int getDist(Point point) {
        return getDist(point.x, point.y);
    }

    public final int getDist(int i, int j) {
        if (!inBounds(i, j)) {
            return 0;
        } else {
            i -= myMinX;
            i /= myCellX;
            j -= myMinY;
            j /= myCellY;
            int k = i;
            int l = j;
            return myArray[k][l];
        }
    }

    public final void setDist(Point point, int i) {
        setDist(point.x, point.y, i);
    }

    public final void setDist(int i, int j, int k) {
        if (!inBounds(i, j)) {
            return;
        } else {
            i -= myMinX;
            i /= myCellX;
            j -= myMinY;
            j /= myCellY;
            int l = i;
            int i1 = j;
            myArray[l][i1] = k;
            return;
        }
    }

    public final void setAll(int i) {
        if (myArray == null) {
            return;
        }
        for (int j = 0; j <= myUpperBoundX; j++) {
            for (int k = 0; k <= myUpperBoundY; k++) {
                myArray[j][k] = i;
            }
        }
    }

    public final void setAllUnoccupied(int i) {
        if (myArray == null) {
            return;
        }
        for (int j = 0; j <= myUpperBoundX; j++) {
            for (int k = 0; k <= myUpperBoundY; k++) {
                if (myArray[j][k] != 0) {
                    myArray[j][k] = i;
                }
            }
        }
    }

    public final boolean isOccupied(int i, int j) {
        return getDist(i, j) == 0;
    }

    public final boolean isUnoccupied(int i, int j, int k, int l) {
        int i1 = (i - myMinX) / myCellX;
        int j1 = (j - myMinY) / myCellY;
        int k1 = Math.max(0, k) / myCellX + 1;
        int l1 = Math.max(0, l) / myCellY + 1;
        int i2 = Math.min(i1 + k1, myUpperBoundX);
        int j2 = Math.min(j1 + l1, myUpperBoundY);
        for (int k2 = i1; k2 <= i2; k2++) {
            for (int l2 = j1; l2 <= j2; l2++) {
                if (myArray[k2][l2] == 0) {
                    return false;
                }
            }
        }

        return true;
    }

    private final int Ray(int paramInt1, int paramInt2, int paramInt3,
            boolean paramBoolean, int paramInt4, int paramInt5, int paramInt6,
            int paramInt7) {
        int i = myArray[paramInt1][paramInt2];
        if ((i != 0) && (i != 2147483647)) {
            if (paramBoolean) {
                paramInt2 += paramInt3;
            } else {
                paramInt1 += paramInt3;
            }
            while ((paramInt4 <= paramInt1) && (paramInt1 <= paramInt5)
                    && (paramInt6 <= paramInt2) && (paramInt2 <= paramInt7)) {
                i++;
                if (i >= myArray[paramInt1][paramInt2]) {
                    break;
                }
                myArray[paramInt1][paramInt2] = i;
                if (paramBoolean) {
                    paramInt2 += paramInt3;
                } else {
                    paramInt1 += paramInt3;
                }
            }
        }
        if (paramBoolean) {
            return paramInt2;
        }
        return paramInt1;
    }

    private final void Spread(int paramInt1, int paramInt2, int paramInt3,
            boolean paramBoolean, int paramInt4, int paramInt5, int paramInt6,
            int paramInt7) {
        if ((paramInt1 < paramInt4) || (paramInt1 > paramInt5)
                || (paramInt2 < paramInt6) || (paramInt2 > paramInt7)) {
            return;
        }
        int i = Ray(paramInt1, paramInt2, paramInt3, paramBoolean, paramInt4,
                paramInt5, paramInt6, paramInt7);
        int j;
        if (paramBoolean) {
            if (paramInt3 > 0) {
                j = paramInt2 + paramInt3;
                while (j < i) {
                    Spread(paramInt1, j, 1, !paramBoolean, paramInt4,
                            paramInt5, paramInt6, paramInt7);
                    Spread(paramInt1, j, -1, !paramBoolean, paramInt4,
                            paramInt5, paramInt6, paramInt7);
                    j += paramInt3;
                }
            } else {
                j = paramInt2 + paramInt3;
                while (j > i) {
                    Spread(paramInt1, j, 1, !paramBoolean, paramInt4,
                            paramInt5, paramInt6, paramInt7);
                    Spread(paramInt1, j, -1, !paramBoolean, paramInt4,
                            paramInt5, paramInt6, paramInt7);
                    j += paramInt3;
                }
            }
        } else if (paramInt3 > 0) {
            j = paramInt1 + paramInt3;
            while (j < i) {
                Spread(j, paramInt2, 1, !paramBoolean, paramInt4, paramInt5,
                        paramInt6, paramInt7);
                Spread(j, paramInt2, -1, !paramBoolean, paramInt4, paramInt5,
                        paramInt6, paramInt7);
                j += paramInt3;
            }
        } else {
            j = paramInt1 + paramInt3;
            while (j > i) {
                Spread(j, paramInt2, 1, !paramBoolean, paramInt4, paramInt5,
                        paramInt6, paramInt7);
                Spread(j, paramInt2, -1, !paramBoolean, paramInt4, paramInt5,
                        paramInt6, paramInt7);
                j += paramInt3;
            }
        }
    }

    private final int BreakOut(int i, int j, int k, boolean flag, int l,
            int i1, int j1, int k1) {
        int l1 = 1;
        int i2 = myArray[i][j];
        for (myArray[i][j] = l1; i2 == 0 && i > l && i < i1 && j > j1 && j < k1; myArray[i][j] = l1++) {
            if (flag) {
                j += k;
            } else {
                i += k;
            }
            i2 = myArray[i][j];
        }

        if (flag) {
            return j;
        } else {
            return i;
        }
    }

    private final void BreakIn(int paramInt1, int paramInt2, int paramInt3,
            boolean paramBoolean, int paramInt4, int paramInt5, int paramInt6,
            int paramInt7) {
        int i = myArray[paramInt1][paramInt2];
        myArray[paramInt1][paramInt2] = 2147483647;
        while ((i == 0) && (paramInt1 > paramInt4) && (paramInt1 < paramInt5)
                && (paramInt2 > paramInt6) && (paramInt2 < paramInt7)) {
            if (paramBoolean) {
                paramInt2 += paramInt3;
            } else {
                paramInt1 += paramInt3;
            }
            i = myArray[paramInt1][paramInt2];
            myArray[paramInt1][paramInt2] = 2147483647;
        }
    }

    public void propagate(int i, int j, double d, int k, int l, double d1,
            int i1, int j1, int k1, int l1) {
        if (myArray == null) {
            return;
        }
        if (!inBounds(i, j)) {
            return;
        }
        i -= myMinX;
        i /= myCellX;
        j -= myMinY;
        j /= myCellY;
        if (!inBounds(k, l)) {
            return;
        }
        k -= myMinX;
        k /= myCellX;
        l -= myMinY;
        l /= myCellY;
        if (Math.abs(i - k) <= 1 && Math.abs(j - l) <= 1) {
            myArray[i][j] = 0;
            return;
        }
        i1 -= myMinX;
        i1 /= myCellX;
        j1 -= myMinY;
        j1 /= myCellY;
        k1 -= myMinX;
        k1 /= myCellX;
        l1 -= myMinY;
        l1 /= myCellY;
        int i2 = Math.max(0, Math.min(myUpperBoundX, i1));
        int j2 = Math.min(myUpperBoundX, Math.max(0, k1));
        int k2 = Math.max(0, Math.min(myUpperBoundY, j1));
        int l2 = Math.min(myUpperBoundY, Math.max(0, l1));
        int i3 = i;
        int j3 = j;
        byte byte0 = ((byte) (d != 0.0D && d != 1.5707963267948966D ? -1 : 1));
        boolean flag = d == 1.5707963267948966D || d == 4.7123889803846897D;
        if (flag) {
            j3 = BreakOut(i3, j3, byte0, flag, i2, j2, k2, l2);
        } else {
            i3 = BreakOut(i3, j3, byte0, flag, i2, j2, k2, l2);
        }
        BreakIn(k, l, d1 != 0.0D && d1 != 1.5707963267948966D ? -1 : 1,
                d1 == 1.5707963267948966D || d1 == 4.7123889803846897D, i2, j2,
                k2, l2);
        Spread(i3, j3, 1, false, i2, j2, k2, l2);
        Spread(i3, j3, -1, false, i2, j2, k2, l2);
        Spread(i3, j3, 1, true, i2, j2, k2, l2);
        Spread(i3, j3, -1, true, i2, j2, k2, l2);
    }
}