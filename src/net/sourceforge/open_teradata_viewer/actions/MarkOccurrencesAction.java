/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer.actions;

import java.awt.event.ActionEvent;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ThreadedAction;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class MarkOccurrencesAction extends CustomAction {

    private static final long serialVersionUID = 2234725763494469028L;

    public MarkOccurrencesAction() {
        super("Mark occurrences");
        setEnabled(true);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // The "mark occurrences" process can be performed altough other
        // processes are running
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
        RSyntaxTextArea textArea = ApplicationFrame.getInstance()
                .getTextComponent();
        textArea.setMarkOccurrences(!textArea.getMarkOccurrences());
    }
}