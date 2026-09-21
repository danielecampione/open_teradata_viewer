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
public class UncommentAction extends CustomAction {

    private static final long serialVersionUID = -7121742231003418327L;

    public UncommentAction() {
        super(LanguageManager.getInstance().getString("action.uncomment_sql_code"), "uncomment.png",
                KeyStroke.getKeyStroke(KeyEvent.VK_7,
                        KeyEvent.SHIFT_DOWN_MASK + Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
                LanguageManager.getInstance().getString("action.uncomment_sql_code.short_description"));
        setEnabled(true);
                
        // Add language change listener to update the action name when language changes
        LanguageManager.getInstance().addLanguageChangeListener((newLocale, newBundle) -> {
            putValue(NAME, newBundle.getString("action.uncomment_sql_code"));
            putValue(SHORT_DESCRIPTION, newBundle.getString("action.uncomment_sql_code.short_description"));
        });
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
        OTVSyntaxTextArea textArea = ApplicationFrame.getInstance().getTextComponent();
        int[] bounds = textArea.getBoundsOfSQLToBeExecuted();

        if (bounds[0] == bounds[1]) {
            return;
        }

        int caretPosition = textArea.getCaretPosition();

        String textToComment = textArea.getText().substring(bounds[0], bounds[1]);

        String[] lines = textToComment.split("\n");

        StringBuilder uncommentedLines = new StringBuilder();
        // See CommentAction for why this tracks original-text offsets
        // instead of comparing against the output buffer's length.
        int origOffset = bounds[0];

        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            // BUGFIX: the previous check (line.startsWith(START_OF_LINE_COMMENT))
            // only matched when the marker was exactly at column 0. A line that
            // had been indented AFTER being commented (e.g. via "Increase
            // Indentation"), or SQL pasted in already commented-and-indented,
            // would silently fail to be uncommented, with no error and no
            // visible sign anything was skipped. Now the marker is looked up
            // right after any leading whitespace instead, and that whitespace
            // is preserved on removal. Same class of bug RSTA fixed for its
            // own ToggleCommentAction in 3.5.4.
            int contentStart = 0;
            while (contentStart < line.length() && Character.isWhitespace(line.charAt(contentStart))) {
                contentStart++;
            }
            boolean hasMarker = line.regionMatches(contentStart, Utilities.START_OF_LINE_COMMENT, 0,
                    Utilities.START_OF_LINE_COMMENT.length());

            if (hasMarker && origOffset + contentStart < caretPosition) {
                caretPosition -= Utilities.START_OF_LINE_COMMENT.length();
            }

            if (hasMarker) {
                uncommentedLines.append(line, 0, contentStart)
                        .append(line.substring(contentStart + Utilities.START_OF_LINE_COMMENT.length()));
            } else {
                // Not a commented line (e.g. a mixed selection) - leave it untouched.
                uncommentedLines.append(line);
            }

            origOffset += line.length() + 1; // +1 for the '\n' consumed by split()

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