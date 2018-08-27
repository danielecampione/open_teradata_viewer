/*
 * Open Teradata Viewer ( help )
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

package net.sourceforge.open_teradata_viewer.help;

import java.util.EventObject;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class HtmlViewerPanelListenerEvent extends EventObject {

    private static final long serialVersionUID = 3702970918400459309L;

    /** The <CODE>HtmlViewerPanel</CODE> involved. */
    private HtmlViewerPanel _pnl;
    /**
     * Ctor.
     *
     * @param   source  The <CODE>HtmlViewerPanel</CODE> that change has
     *                  happened to.
     * 
     * @throws  IllegalArgumentException
     *          Thrown if <TT>null</TT>HtmlViewerPanel/TT> passed.
     */
    HtmlViewerPanelListenerEvent(HtmlViewerPanel source) {
        super(checkParams(source));
        _pnl = source;
    }
    /**
     * Return the <CODE>HtmlViewerPanel</CODE>.
     */
    public HtmlViewerPanel getHtmlViewerPanel() {
        return _pnl;
    }
    private static HtmlViewerPanel checkParams(HtmlViewerPanel source) {
        if (source == null) {
            throw new IllegalArgumentException("HtmlViewerPanel == null");
        }
        return source;
    }
}