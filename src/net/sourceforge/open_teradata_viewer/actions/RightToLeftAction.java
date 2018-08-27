/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer.actions;

import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ThreadedAction;
import net.sourceforge.open_teradata_viewer.editor.TextScrollPane;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class RightToLeftAction extends CustomAction {

    private static final long serialVersionUID = 5144421032473177175L;

    public RightToLeftAction() {
        super("Right-to-Left orientation");
        setEnabled(true);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // The "right-to-left orientation" process can be performed altough
        // other processes are running
        new ThreadedAction() {
            @Override
            protected void execute() throws Exception {
                performThreaded(e);
            }
        };
    }

    /* (non-Javadoc)
     * @see net.sourceforge.open_teradata_viewer.actions.CustomAction#performThreaded(java.awt.event.ActionEvent)
     */
    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        TextScrollPane scrollPane = ApplicationFrame.getInstance()
                .getTextScrollPane();
        if (scrollPane.getComponentOrientation().isLeftToRight()) {
            scrollPane
                    .applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        } else {
            scrollPane
                    .applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        }
    }
}