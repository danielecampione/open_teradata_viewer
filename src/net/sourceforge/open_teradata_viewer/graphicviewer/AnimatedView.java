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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class AnimatedView extends OpenTeradataViewerView {

    private static final long serialVersionUID = -2006218480848329886L;

    public AnimatedView() {
        new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent paramActionEvent) {
                GraphicViewerDocument localGraphicViewerDocument = AnimatedView.this
                        .getDocument();
                localGraphicViewerDocument.setSkipsUndoManager(true);
                GraphicViewerListPosition localGraphicViewerListPosition = localGraphicViewerDocument
                        .getFirstObjectPos();
                while (localGraphicViewerListPosition != null) {
                    GraphicViewerObject localGraphicViewerObject = localGraphicViewerDocument
                            .getObjectAtPos(localGraphicViewerListPosition);
                    localGraphicViewerListPosition = localGraphicViewerDocument
                            .getNextObjectPosAtTop(localGraphicViewerListPosition);
                    if ((localGraphicViewerObject instanceof AnimatedLink)) {
                        AnimatedLink localAnimatedLink = (AnimatedLink) localGraphicViewerObject;
                        localAnimatedLink.step();
                    }
                }
                localGraphicViewerDocument.setSkipsUndoManager(false);
            }
        }).start();
    }

    public void newLink(GraphicViewerPort paramGraphicViewerPort1,
            GraphicViewerPort paramGraphicViewerPort2) {
        GraphicViewerDocument localGraphicViewerDocument = getDocument();
        if (localGraphicViewerDocument == null)
            return;

        AnimatedLink localAnimatedLink = new AnimatedLink(
                paramGraphicViewerPort1, paramGraphicViewerPort2);

        GraphicViewerSubGraphBase.reparentToCommonSubGraph(localAnimatedLink,
                paramGraphicViewerPort1, paramGraphicViewerPort2, true,
                localGraphicViewerDocument.getLinksLayer());

        fireUpdate(31, 0, localAnimatedLink);
        localGraphicViewerDocument.endTransaction(getEditPresentationName(4));
    }
}
