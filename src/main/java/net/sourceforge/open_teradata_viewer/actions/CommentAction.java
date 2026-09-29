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

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.ThreadedAction;
import net.sourceforge.open_teradata_viewer.editor.OTVSyntaxTextArea;
import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;
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
        super(LanguageManager.getInstance().getString("action.comment_sql_code"), "comment.png",
                KeyStroke.getKeyStroke(KeyEvent.VK_7, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
                LanguageManager.getInstance().getString("action.comment_sql_code.short_description"));
        setEnabled(true);
                
        // Add language change listener to update the action name when language changes
        LanguageManager.getInstance().addLanguageChangeListener((newLocale, newBundle) -> {
            putValue(NAME, newBundle.getString("action.comment_sql_code"));
            putValue(SHORT_DESCRIPTION, newBundle.getString("action.comment_sql_code.short_description"));
        });
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
        OTVSyntaxTextArea textArea = ApplicationFrame.getInstance().getTextComponent();
        int[] bounds = textArea.getBoundsOfSQLToBeExecuted();

        if (bounds[0] == bounds[1]) {
            return;
        }

        int caretPosition = textArea.getCaretPosition();

        String textToComment = textArea.getText().substring(bounds[0], bounds[1]);

        String[] lines = textToComment.split("\n");

        StringBuilder commentedLines = new StringBuilder();
        // Tracks the offset (in the ORIGINAL, un-commented text) of the start
        // of the line currently being processed, so the caret shift can be
        // computed against real document positions instead of against the
        // length of the (partially built) output, which is fragile once the
        // comment marker is no longer always inserted at column 0 below.
        int origOffset = bounds[0];

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            // Insert the comment marker right after the line's leading
            // whitespace instead of always at column 0, so existing
            // indentation is preserved. This also keeps Comment/Uncomment
            // symmetric: Uncomment now looks for the marker after any
            // leading whitespace too (see UncommentAction), mirroring the
            // fix RSTA itself applied to its own ToggleCommentAction in
            // 3.5.4 for the identical class of bug.
            int contentStart = 0;
            while (contentStart < line.length() && Character.isWhitespace(line.charAt(contentStart))) {
                contentStart++;
            }
            boolean blankLine = contentStart == line.length();

            if (origOffset + contentStart <= caretPosition) {
                caretPosition += Utilities.START_OF_LINE_COMMENT.length();
            }

            if (blankLine) {
                // Don't litter blank lines inside the selection with a bare
                // comment marker.
                commentedLines.append(line);
            } else {
                commentedLines.append(line, 0, contentStart).append(Utilities.START_OF_LINE_COMMENT)
                        .append(line.substring(contentStart));
            }

            origOffset += line.length() + 1; // +1 for the '\n' consumed by split()

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