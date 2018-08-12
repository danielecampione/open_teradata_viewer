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

    public GraphicViewerPositionArray() {
        _fldvoid = true;
        _fldelse = 1;
        _fldbyte = 1;
        _fldnew = -1;
        _fldint = -1;
        _flddo = new Rectangle(1, 1, -2, -2);
        _fldif = 10;
        a = 10;
        _fldnull = new Dimension(10, 10);
        _fldgoto = (int[][]) null;
        _fldcase = 0;
        _fldtry = 0;
    }

    public final void initialize(Rectangle rectangle) {
        if (rectangle.width <= 0 || rectangle.height <= 0)
            return;
        int i = rectangle.x;
        int j = rectangle.y;
        int k = rectangle.x + rectangle.width;
        int l = rectangle.y + rectangle.height;
        _fldelse = (int) Math.floor((double) (i - _fldif) / (double) _fldif)
                * _fldif;
        _fldbyte = (int) Math.floor((double) (j - a) / (double) a) * a;
        _fldnew = (int) Math.ceil((double) (k + 2 * _fldif) / (double) _fldif)
                * _fldif;
        _fldint = (int) Math.ceil((double) (l + 2 * a) / (double) a) * a;
        _flddo.x = _fldelse;
        _flddo.y = _fldbyte;
        _flddo.width = _fldnew - _fldelse;
        _flddo.height = _fldint - _fldbyte;
        int i1 = 1 + (int) Math.ceil((double) (_fldnew - _fldelse)
                / (double) _fldif);
        int j1 = 1 + (int) Math
                .ceil((double) (_fldint - _fldbyte) / (double) a);
        if (_fldgoto == null || _fldcase < i1 - 1 || _fldtry < j1 - 1) {
            _fldgoto = new int[i1][j1];
            _fldcase = i1 - 1;
            _fldtry = j1 - 1;
        }
        setAll(0x7fffffff);
    }

    public final boolean isInvalid() {
        return _fldvoid;
    }

    public final void setInvalid(boolean flag) {
        _fldvoid = flag;
    }

    public final Rectangle getBounds() {
        return _flddo;
    }

    public final Dimension getCellSize() {
        return _fldnull;
    }

    public final void setCellSize(Dimension dimension) {
        if (dimension.width >= 1 && dimension.height >= 1
                && (dimension.width != _fldif || dimension.height != a)) {
            _fldif = dimension.width;
            a = dimension.height;
            _fldnull.width = _fldif;
            _fldnull.height = a;
            initialize(new Rectangle(_fldelse, _fldbyte, _fldnew - _fldelse,
                    _fldint - _fldbyte));
        }
    }

    public final boolean inBounds(int i, int j) {
        return _fldelse <= i && i <= _fldnew && _fldbyte <= j && j <= _fldint;
    }

    public final int getDist(Point point) {
        return getDist(point.x, point.y);
    }

    public final int getDist(int i, int j) {
        if (!inBounds(i, j)) {
            return 0;
        } else {
            i -= _fldelse;
            i /= _fldif;
            j -= _fldbyte;
            j /= a;
            int k = i;
            int l = j;
            return _fldgoto[k][l];
        }
    }

    public final void setDist(Point point, int i) {
        setDist(point.x, point.y, i);
    }

    public final void setDist(int i, int j, int k) {
        if (!inBounds(i, j)) {
            return;
        } else {
            i -= _fldelse;
            i /= _fldif;
            j -= _fldbyte;
            j /= a;
            int l = i;
            int i1 = j;
            _fldgoto[l][i1] = k;
            return;
        }
    }

    public final void setAll(int i) {
        if (_fldgoto == null)
            return;
        for (int j = 0; j <= _fldcase; j++) {
            for (int k = 0; k <= _fldtry; k++)
                _fldgoto[j][k] = i;

        }

    }

    public final void setAllUnoccupied(int i) {
        if (_fldgoto == null)
            return;
        for (int j = 0; j <= _fldcase; j++) {
            for (int k = 0; k <= _fldtry; k++)
                if (_fldgoto[j][k] != 0)
                    _fldgoto[j][k] = i;

        }

    }

    public final boolean isOccupied(int i, int j) {
        return getDist(i, j) == 0;
    }

    public final boolean isUnoccupied(int i, int j, int k, int l) {
        int i1 = (i - _fldelse) / _fldif;
        int j1 = (j - _fldbyte) / a;
        int k1 = Math.max(0, k) / _fldif + 1;
        int l1 = Math.max(0, l) / a + 1;
        int i2 = Math.min(i1 + k1, _fldcase);
        int j2 = Math.min(j1 + l1, _fldtry);
        for (int k2 = i1; k2 <= i2; k2++) {
            for (int l2 = j1; l2 <= j2; l2++)
                if (_fldgoto[k2][l2] == 0)
                    return false;

        }

        return true;
    }

    private final int _mthif(int i, int j, int k, boolean flag, int l, int i1,
            int j1, int k1) {
        int l1 = _fldgoto[i][j];
        if (l1 != 0 && l1 != 0x7fffffff) {
            if (flag)
                j += k;
            else
                i += k;
            while (l <= i && i <= i1 && j1 <= j && j <= k1
                    && ++l1 < _fldgoto[i][j]) {
                _fldgoto[i][j] = l1;
                if (flag)
                    j += k;
                else
                    i += k;
            }
        }
        if (flag)
            return j;
        else
            return i;
    }

    private final void _mthdo(int i, int j, int k, boolean flag, int l, int i1,
            int j1, int k1) {
        if (i < l || i > i1 || j < j1 || j > k1)
            return;
        int l1 = _mthif(i, j, k, flag, l, i1, j1, k1);
        if (flag) {
            if (k > 0) {
                for (int i2 = j + k; i2 < l1; i2 += k) {
                    _mthdo(i, i2, 1, !flag, l, i1, j1, k1);
                    _mthdo(i, i2, -1, !flag, l, i1, j1, k1);
                }

            } else {
                for (int j2 = j + k; j2 > l1; j2 += k) {
                    _mthdo(i, j2, 1, !flag, l, i1, j1, k1);
                    _mthdo(i, j2, -1, !flag, l, i1, j1, k1);
                }

            }
        } else if (k > 0) {
            for (int k2 = i + k; k2 < l1; k2 += k) {
                _mthdo(k2, j, 1, !flag, l, i1, j1, k1);
                _mthdo(k2, j, -1, !flag, l, i1, j1, k1);
            }

        } else {
            for (int l2 = i + k; l2 > l1; l2 += k) {
                _mthdo(l2, j, 1, !flag, l, i1, j1, k1);
                _mthdo(l2, j, -1, !flag, l, i1, j1, k1);
            }

        }
    }

    private final int _mthfor(int i, int j, int k, boolean flag, int l, int i1,
            int j1, int k1) {
        int l1 = 1;
        int i2 = _fldgoto[i][j];
        for (_fldgoto[i][j] = l1; i2 == 0 && i > l && i < i1 && j > j1
                && j < k1; _fldgoto[i][j] = l1++) {
            if (flag)
                j += k;
            else
                i += k;
            i2 = _fldgoto[i][j];
        }

        if (flag)
            return j;
        else
            return i;
    }

    private final void a(int i, int j, int k, boolean flag, int l, int i1,
            int j1, int k1) {
        int l1 = _fldgoto[i][j];
        for (_fldgoto[i][j] = 0x7fffffff; l1 == 0 && i > l && i < i1 && j > j1
                && j < k1; _fldgoto[i][j] = 0x7fffffff) {
            if (flag)
                j += k;
            else
                i += k;
            l1 = _fldgoto[i][j];
        }

    }

    public void propagate(int i, int j, double d, int k, int l, double d1,
            int i1, int j1, int k1, int l1) {
        if (_fldgoto == null)
            return;
        if (!inBounds(i, j))
            return;
        i -= _fldelse;
        i /= _fldif;
        j -= _fldbyte;
        j /= a;
        if (!inBounds(k, l))
            return;
        k -= _fldelse;
        k /= _fldif;
        l -= _fldbyte;
        l /= a;
        if (Math.abs(i - k) <= 1 && Math.abs(j - l) <= 1) {
            _fldgoto[i][j] = 0;
            return;
        }
        i1 -= _fldelse;
        i1 /= _fldif;
        j1 -= _fldbyte;
        j1 /= a;
        k1 -= _fldelse;
        k1 /= _fldif;
        l1 -= _fldbyte;
        l1 /= a;
        int i2 = Math.max(0, Math.min(_fldcase, i1));
        int j2 = Math.min(_fldcase, Math.max(0, k1));
        int k2 = Math.max(0, Math.min(_fldtry, j1));
        int l2 = Math.min(_fldtry, Math.max(0, l1));
        int i3 = i;
        int j3 = j;
        byte byte0 = ((byte) (d != 0.0D && d != 1.5707963267948966D ? -1 : 1));
        boolean flag = d == 1.5707963267948966D || d == 4.7123889803846897D;
        if (flag)
            j3 = _mthfor(i3, j3, byte0, flag, i2, j2, k2, l2);
        else
            i3 = _mthfor(i3, j3, byte0, flag, i2, j2, k2, l2);
        a(k, l, d1 != 0.0D && d1 != 1.5707963267948966D ? -1 : 1,
                d1 == 1.5707963267948966D || d1 == 4.7123889803846897D, i2, j2,
                k2, l2);
        _mthdo(i3, j3, 1, false, i2, j2, k2, l2);
        _mthdo(i3, j3, -1, false, i2, j2, k2, l2);
        _mthdo(i3, j3, 1, true, i2, j2, k2, l2);
        _mthdo(i3, j3, -1, true, i2, j2, k2, l2);
    }

    static final int _fldfor = 0;
    static final int _fldlong = 1;
    static final int _fldchar = 0x7fffffff;
    private boolean _fldvoid;
    private int _fldelse;
    private int _fldbyte;
    private int _fldnew;
    private int _fldint;
    private Rectangle _flddo;
    private int _fldif;
    private int a;
    private Dimension _fldnull;
    private int _fldgoto[][];
    private int _fldcase;
    private int _fldtry;
}