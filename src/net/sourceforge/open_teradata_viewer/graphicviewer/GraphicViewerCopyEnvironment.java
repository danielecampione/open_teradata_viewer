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

import java.util.Map;
import java.util.Vector;

/**
 * 
 * 
 * @author D. Campione
 *
 */
@SuppressWarnings("rawtypes")
public interface GraphicViewerCopyEnvironment extends Map {

    public abstract GraphicViewerObject copy(
            GraphicViewerObject graphicviewerobject);

    public abstract GraphicViewerObject copyComplete(
            GraphicViewerObject graphicviewerobject);

    public abstract void finishDelayedCopies();

    public abstract void clearDelayeds();

    public abstract boolean isEmptyDelayeds();

    public abstract int sizeDelayeds();

    public abstract boolean isDelayed(Object obj);

    public abstract void delay(Object obj);

    public abstract void removeDelayed(Object obj);

    public abstract Vector getDelayeds();
}