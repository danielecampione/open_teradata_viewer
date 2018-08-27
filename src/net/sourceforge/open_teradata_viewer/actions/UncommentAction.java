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

import java.awt.event.ActionEvent;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.ThreadedAction;
import net.sourceforge.open_teradata_viewer.editor.OTVSyntaxTextArea;
import net.sourceforge.open_teradata_viewer.util.Utilities;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class UncommentAction extends CustomAction {

    private static final long serialVersionUID = -7121742231003418327L;

    public UncommentAction() {
        super("Uncomment SQL code", "uncomment.png", null, null);
        setEnabled(true);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // The "uncomment" process can be performed altough other processes are
        // running
        new ThreadedAction() {
            @Override
            protected void execute() {
                try {
                    performThreaded(e);
                } catch (Throwable t) {
                    ExceptionDialog.ignoreException(t);
                }
            }
        };
    }

    /* (non-Javadoc)
     * @see net.sourceforge.open_teradata_viewer.actions.CustomAction#performThreaded(java.awt.event.ActionEvent)
     */
    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        OTVSyntaxTextArea textArea = ApplicationFrame.getInstance()
                .getTextComponent();
        int[] bounds = textArea.getBoundsOfSQLToBeExecuted();

        if (bounds[0] == bounds[1]) {
            return;
        }

        int caretPosition = textArea.getCaretPosition();

        String textToComment = textArea.getText().substring(bounds[0],
                bounds[1]);

        String[] lines = textToComment.split("\n");

        StringBuilder uncommentedLines = new StringBuilder();

        for (int i = 0; i < lines.length; i++) {
            if (bounds[0] + uncommentedLines.length() < caretPosition) {
                if (lines[i].startsWith(Utilities.START_OF_LINE_COMMENT)) {
                    caretPosition -= Utilities.START_OF_LINE_COMMENT.length();
                }
            }

            if (lines[i].startsWith(Utilities.START_OF_LINE_COMMENT)) {
                uncommentedLines.append(lines[i]
                        .substring(Utilities.START_OF_LINE_COMMENT.length()));
            } else {
                uncommentedLines.append(lines[i]);
            }

            if (i < lines.length - 1 || textToComment.endsWith("\n")) {
                uncommentedLines.append("\n");
            }
        }
        textArea.setSelectionStart(bounds[0]);
        textArea.setSelectionEnd(bounds[1]);

        textArea.replaceSelection(uncommentedLines.toString());

        textArea.setCaretPosition(caretPosition);
    }
}