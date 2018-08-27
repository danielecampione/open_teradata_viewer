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

import java.awt.Point;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public interface IGraphicViewerObjectCollection
        extends
            IGraphicViewerObjectSimpleCollection {

    public abstract GraphicViewerListPosition addObjectAtHead(
            GraphicViewerObject graphicviewerobject);

    public abstract GraphicViewerListPosition addObjectAtTail(
            GraphicViewerObject graphicviewerobject);

    public abstract GraphicViewerListPosition insertObjectBefore(
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerObject graphicviewerobject);

    public abstract GraphicViewerListPosition insertObjectAfter(
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerObject graphicviewerobject);

    public abstract void bringObjectToFront(
            GraphicViewerObject graphicviewerobject);

    public abstract void sendObjectToBack(
            GraphicViewerObject graphicviewerobject);

    public abstract void removeObject(GraphicViewerObject graphicviewerobject);

    public abstract GraphicViewerObject removeObjectAtPos(
            GraphicViewerListPosition graphicviewerlistposition);

    public abstract GraphicViewerObject pickObject(Point point, boolean flag);

    public abstract GraphicViewerListPosition getLastObjectPos();

    public abstract GraphicViewerListPosition getPrevObjectPos(
            GraphicViewerListPosition graphicviewerlistposition);

    public abstract GraphicViewerListPosition findObject(
            GraphicViewerObject graphicviewerobject);
}