/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2013, D. Campione
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
public class CommentAction extends CustomAction {

    private static final long serialVersionUID = -2802751406188460908L;

    public CommentAction() {
        super("Comment", "comment.png", null, null);
        setEnabled(true);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // The "comment" process can be performed altough other processes are
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

        StringBuffer commentedLines = new StringBuffer();

        for (int i = 0; i < lines.length; i++) {
            if (bounds[0] + commentedLines.length() <= caretPosition) {
                caretPosition += Utilities.START_OF_LINE_COMMENT.length();
            }

            commentedLines.append(Utilities.START_OF_LINE_COMMENT).append(
                    lines[i]);
            if (i < lines.length - 1 || textToComment.endsWith("\n")) {
                commentedLines.append("\n");
            }
        }

        textArea.setSelectionStart(bounds[0]);
        textArea.setSelectionEnd(bounds[1]);

        textArea.replaceSelection(commentedLines.toString());

        textArea.setCaretPosition(caretPosition);
    }
}