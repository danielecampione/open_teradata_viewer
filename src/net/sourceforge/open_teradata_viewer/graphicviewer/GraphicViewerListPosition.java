/*
 * Open Teradata Viewer ( graphic viewer )
 * Copyright (C) 2011, D. Campione
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

import java.io.Serializable;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerListPosition implements Serializable {

    private static final long serialVersionUID = 2652042136068981624L;

    GraphicViewerListPosition(GraphicViewerObject graphicviewerobject,
            GraphicViewerListPosition graphicviewerlistposition,
            GraphicViewerListPosition graphicviewerlistposition1) {
        _flddo = graphicviewerobject;
        a = graphicviewerlistposition1;
        _fldif = graphicviewerlistposition;
    }

    GraphicViewerObject _flddo;
    GraphicViewerListPosition a;
    GraphicViewerListPosition _fldif;
}