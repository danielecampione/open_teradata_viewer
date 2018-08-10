/*
 * Open Teradata Viewer ( look and feel )
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

package com.incors.plaf;

import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.WeakHashMap;

/**
 * 
 * 
 * @author Christoph Wilhelms
 * 
 */
class FastGradientPaintContext implements PaintContext {

    /**
     * 
     * 
     * @author Christoph Wilhelms
     * 
     */
    private class GradientInfo {
        ColorModel model;
        int parallelLength, startColor, endColor;
        boolean isVertical;
        public boolean equals(Object o) {
            if (!(o instanceof GradientInfo))
                return false;
            GradientInfo gi = (GradientInfo) o;
            if (gi.model.equals(model) && gi.parallelLength == parallelLength
                    && gi.startColor == startColor && gi.endColor == endColor
                    && gi.isVertical == isVertical)
                return true;
            return false;
        }
        public int hashCode() {
            return parallelLength;
        }
        public String toString() {
            return "Direction:" + (isVertical ? "ver" : "hor") + ", Length: "
                    + Integer.toString(parallelLength) + ", Color1: "
                    + Integer.toString(startColor, 16) + ", Color2: "
                    + Integer.toString(endColor, 16);
        }
    }

    /**
     * 
     * 
     * @author Christoph Wilhelms
     * 
     */
    private class Gradient {
        GradientInfo info;
        int perpendicularLength = 0;
        WritableRaster raster;
        HashMap<Integer, Raster> childRasterCache;

        Gradient(GradientInfo i) {
            info = i;
        }

        private Raster getRaster(int parallelPos, int perpendicularLength,
                int parallelLength) {
            if (raster == null
                    || (this.perpendicularLength < perpendicularLength))
                createRaster(perpendicularLength);

            Integer key = new Integer(parallelPos);
            Object o = childRasterCache.get(key);
            if (o != null)
                return (Raster) o;
            else {
                Raster r;
                if (info.isVertical)
                    r = raster.createChild(0, parallelPos,
                            this.perpendicularLength, info.parallelLength
                                    - parallelPos, 0, 0, null);
                else
                    r = raster
                            .createChild(parallelPos, 0, info.parallelLength
                                    - parallelPos, this.perpendicularLength, 0,
                                    0, null);
                childRasterCache.put(key, r);
                return r;
            }

        }

        public void dispose() {
            //			raster = null;
        }

        private void createRaster(int perpendicularLength) {
            int gradientWidth, gradientHeight;
            if (info.isVertical) {
                gradientHeight = info.parallelLength;
                gradientWidth = this.perpendicularLength = perpendicularLength;
            } else {
                gradientWidth = info.parallelLength;
                gradientHeight = this.perpendicularLength = perpendicularLength;
            }

            int sa = (info.startColor >> 24) & 0xFF;
            int sr = (info.startColor >> 16) & 0xFF;
            int sg = (info.startColor >> 8) & 0xFF;
            int sb = info.startColor & 0xFF;
            int da = ((info.endColor >> 24) & 0xFF) - sa;
            int dr = ((info.endColor >> 16) & 0xFF) - sr;
            int dg = ((info.endColor >> 8) & 0xFF) - sg;
            int db = (info.endColor & 0xFF) - sb;

            raster = info.model.createCompatibleWritableRaster(gradientWidth,
                    gradientHeight);

            Object c = null;
            int pl = info.parallelLength;
            for (int i = 0; i < pl; i++) {
                c = info.model.getDataElements((sa + (i * da) / pl) << 24
                        | (sr + (i * dr) / pl) << 16
                        | (sg + (i * dg) / pl) << 8 | (sb + (i * db) / pl), c);
                for (int j = 0; j < perpendicularLength; j++) {
                    if (info.isVertical) {
                        raster.setDataElements(j, i, c);
                    } else {
                        raster.setDataElements(i, j, c);
                    }
                }
            }
            childRasterCache = new HashMap<Integer, Raster>();
        }
    }

    private static WeakHashMap<GradientInfo, WeakReference<Gradient>> gradientCache = new WeakHashMap<GradientInfo, WeakReference<Gradient>>();

    private static LinkedList<GradientInfo> recentInfos = new LinkedList<GradientInfo>();

    GradientInfo info;
    int parallelDevicePos;
    Gradient gradient;

    FastGradientPaintContext(ColorModel cm, Rectangle r, int sc, int ec,
            boolean ver) {
        info = new GradientInfo();
        if ((((sc & ec) >> 24) & 0xFF) != 0xFF) {
            info.model = ColorModel.getRGBdefault();
        } else {
            info.model = cm;
        }
        info.startColor = sc;
        info.endColor = ec;
        if (info.isVertical = ver) {
            parallelDevicePos = r.y;
            info.parallelLength = r.height;
        } else {
            parallelDevicePos = r.x;
            info.parallelLength = r.width;
        }
        recentInfos.remove(info);
        recentInfos.add(0, info);
        if (recentInfos.size() > 16)
            recentInfos.removeLast();
        Object o = gradientCache.get(info);
        if (o != null)
            o = ((WeakReference<?>) o).get();
        if (o != null) {
            gradient = (Gradient) o;
        } else {
            gradientCache.put(info, new WeakReference<Gradient>(
                    gradient = new Gradient(info)));
        }
    }

    public void dispose() {
        gradient.dispose();
    }

    public ColorModel getColorModel() {
        return info.model;
    }

    public synchronized Raster getRaster(int x, int y, int w, int h) {
        if (info.isVertical) {
            return gradient.getRaster(y - parallelDevicePos, w, h);
        } else {
            return gradient.getRaster(x - parallelDevicePos, h, w);
        }
    }
}