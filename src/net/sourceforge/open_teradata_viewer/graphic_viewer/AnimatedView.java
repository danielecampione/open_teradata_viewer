/*
 * Open Teradata Viewer ( graphic viewer )
 * Copyright (C) 2014, D. Campione
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class AnimatedView extends OTVView {

    private static final long serialVersionUID = -2006218480848329886L;

    public AnimatedView() {
        new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                GraphicViewerDocument doc = getDocument();
                doc.setSkipsUndoManager(true);
                GraphicViewerListPosition pos = doc.getFirstObjectPos();
                while (pos != null) {
                    GraphicViewerObject obj = doc.getObjectAtPos(pos);
                    pos = doc.getNextObjectPosAtTop(pos);
                    if (obj instanceof AnimatedLink) {
                        AnimatedLink link = (AnimatedLink) obj;
                        link.step();
                    }
                }
                doc.setSkipsUndoManager(false);
            }
        }).start();
    }

    public void newLink(GraphicViewerPort from, GraphicViewerPort to) {
        GraphicViewerDocument doc = getDocument();
        if (doc == null) {
            return;
        }

        GraphicViewerLink link = new AnimatedLink(from, to);

        // Now smarter about automatically adding links to subgraphs
        //  [instead of doc.getLinksLayer().addObjectAtTail(link);]
        GraphicViewerSubGraphBase.reparentToCommonSubGraph(link, from, to,
                true, doc.getLinksLayer());

        fireUpdate(GraphicViewerViewEvent.LINK_CREATED, 0, link);
        doc.endTransaction(getEditPresentationName(4));
    }
}