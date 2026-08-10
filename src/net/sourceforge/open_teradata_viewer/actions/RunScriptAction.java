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
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Context;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.History;
import net.sourceforge.open_teradata_viewer.ResultSetTable;
import net.sourceforge.open_teradata_viewer.WaitingDialog;
import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class RunScriptAction extends CustomAction {

    private static final long serialVersionUID = -2332087371109375191L;

    protected RunScriptAction() {
        super(LanguageManager.getInstance().getString("action.run_script"), "script.png", null, null);
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        setEnabled(isConnected);
        
        // Add language change listener to update the action name when language changes
        LanguageManager.getInstance().addLanguageChangeListener((newLocale, newBundle) -> {
            putValue(NAME, newBundle.getString("action.run_script"));
        });
    }

    @Override
    protected void performThreaded(ActionEvent ae) throws Exception {
        String text = ApplicationFrame.getInstance().getTextComponent().getText();
        boolean isConnected = Context.getInstance().getConnectionData() != null;
        if (!isConnected) {
            ApplicationFrame.getInstance().getConsole().println("NOT connected.",
                    ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            return;
        }
        if (text.trim().length() == 0) {
            return;
        }
        History.getInstance().add(text);
        Actions.getInstance().validateTextActions();

        List<int[]> statementBounds = splitStatements(text);
        int total = statementBounds.size();

        final Vector<Vector> dataVector = new Vector<Vector>();
        int count = 0;
        final Statement statement = Context.getInstance().getConnectionData()
                .getConnection()
                .createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        Runnable onCancel = new Runnable() {
            @Override
            public void run() {
                try {
                    statement.cancel();
                } catch (Throwable t) {
                    ExceptionDialog.hideException(t);
                }
            }
        };
        WaitingDialog waitingDialog = new WaitingDialog(onCancel);
        waitingDialog.setText(String.format("0/%d", total));
        int[] currentBounds = null;
        try {
            for (int[] bounds : statementBounds) {
                if (!waitingDialog.isVisible()) {
                    break;
                }
                currentBounds = bounds;
                String sql = text.substring(bounds[0], bounds[1]);
                Vector<String> row = new Vector<String>(1);
                int i = statement.executeUpdate(sql);
                row.add(Integer.toString(i));
                dataVector.add(row);
                waitingDialog.setText(String.format("%d/%d", ++count, total));
            }
        } catch (Exception e) {
            if (currentBounds != null) {
                ApplicationFrame.getInstance().getTextComponent()
                        .setSelectionStart(currentBounds[0]);
                ApplicationFrame.getInstance().getTextComponent()
                        .setSelectionEnd(currentBounds[1]);
            }
            ApplicationFrame.getInstance().focusTextArea();
            throw e;
        } finally {
            waitingDialog.hide();
            statement.close();
            Context.getInstance().setResultSet(null);
            final Vector<String> columnIdentifiers = new Vector<String>(1);
            columnIdentifiers.add(LanguageManager.getInstance().getString("label.rows_updated"));
            Context.getInstance().setColumnTypes(new int[]{Types.INTEGER});
            Context.getInstance().setColumnTypeNames(new String[1]);
            ResultSetTable.getInstance().setDataVector(dataVector,
                    columnIdentifiers, waitingDialog.getExecutionTime());
            Actions.getInstance().validateActions();
        }
    }

    /**
     * A PL/SQL block (CREATE PROCEDURE/FUNCTION/PACKAGE/PACKAGE BODY/
     * TRIGGER/TYPE/TYPE BODY, or a bare DECLARE/BEGIN block) is
     * conventionally terminated by a "/" on its own line in SQL*Plus/
     * SQLcl-style scripts, rather than by a semicolon - a block routinely
     * contains several semicolons of its own (e.g. inside BEGIN ... END;),
     * none of which are statement separators.
     */
    private static final java.util.regex.Pattern PLSQL_BLOCK_START = java.util.regex.Pattern.compile(
            "\\A\\s*(CREATE\\s+(OR\\s+REPLACE\\s+)?(PACKAGE\\s+BODY|PACKAGE|PROCEDURE|FUNCTION|TRIGGER|TYPE\\s+BODY|TYPE)\\b"
                    + "|DECLARE\\b|BEGIN\\b)",
            java.util.regex.Pattern.CASE_INSENSITIVE);

    /**
     * Splits a multi-statement SQL script into the [start, end) character
     * offsets of each individual statement it contains, the terminating
     * semicolon (or, for a PL/SQL block, the terminating "/" line)
     * excluded.
     * <p>
     * A semicolon is treated as a statement terminator only when it is the
     * last non-whitespace character on its line, and one located inside a
     * single-quoted string literal is never treated as a terminator (the
     * standard SQL <code>''</code> escape for a literal quote inside a
     * string is handled correctly as an emergent property of the simple
     * in/out-of-string toggle below). A statement that starts with a
     * PL/SQL block opener is the one exception to the semicolon rule: it
     * is instead read in full up to its terminating "/" line, so that the
     * semicolons inside it are preserved rather than splitting it into
     * broken fragments.
     *
     * @param text the full script text.
     * @return the ordered list of [start, end) offsets, one per statement.
     */
    private static List<int[]> splitStatements(String text) {
        List<int[]> statements = new ArrayList<int[]>();
        int length = text.length();
        int start = 0;
        while (start < length) {
            while (start < length && Character.isWhitespace(text.charAt(start))) {
                start++;
            }
            if (start >= length) {
                break;
            }
            if (PLSQL_BLOCK_START.matcher(text).region(start, length).lookingAt()) {
                int end = findStandaloneSlashLine(text, start, length);
                if (end != -1) {
                    statements.add(new int[] { start, end });
                    int nextLine = text.indexOf('\n', end);
                    start = nextLine == -1 ? length : nextLine + 1;
                    continue;
                }
                // No terminating "/" found for this block (script doesn't
                // use the convention, or it's simply missing) - fall back
                // to the ordinary semicolon rule below, same as any other
                // statement.
            }
            int end = findSemicolonTerminator(text, start, length);
            if (end == -1) {
                break;
            }
            statements.add(new int[] { start, end });
            start = advancePastSemicolonLine(text, end, length);
        }
        return statements;
    }

    private static int findStandaloneSlashLine(String text, int from, int length) {
        int pos = from;
        while (pos < length) {
            int lineEnd = text.indexOf('\n', pos);
            int lineEndExclusive = lineEnd == -1 ? length : lineEnd;
            if (text.substring(pos, lineEndExclusive).trim().equals("/")) {
                return pos;
            }
            pos = lineEnd == -1 ? length : lineEnd + 1;
        }
        return -1;
    }

    private static int findSemicolonTerminator(String text, int from, int length) {
        boolean inString = false;
        for (int i = from; i < length; i++) {
            char c = text.charAt(i);
            if (c == '\'') {
                inString = !inString;
            } else if (c == ';' && !inString) {
                int j = i + 1;
                while (j < length && (text.charAt(j) == ' '
                        || text.charAt(j) == '\t' || text.charAt(j) == '\r')) {
                    j++;
                }
                if (j == length || text.charAt(j) == '\n') {
                    return i;
                }
            }
        }
        return -1;
    }

    private static int advancePastSemicolonLine(String text, int semicolonPos, int length) {
        int j = semicolonPos + 1;
        while (j < length && (text.charAt(j) == ' ' || text.charAt(j) == '\t' || text.charAt(j) == '\r')) {
            j++;
        }
        return j < length ? j + 1 : j;
    }
}
