/*
 * Open Teradata Viewer ( editor )
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

package net.sourceforge.open_teradata_viewer.editor;

import java.awt.event.ActionEvent;

import net.sourceforge.open_teradata_viewer.editor.search.OTVSyntaxSearchEngine;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class OTVSyntaxTextArea extends SyntaxTextArea {

    private static final long serialVersionUID = -4500559841244366663L;

    private OTVSyntaxSearchEngine _OTVSyntaxSearchEngine;

    public OTVSyntaxTextArea() {
        this(0, 0);
    }

    public OTVSyntaxTextArea(int rows, int cols) {
        super(rows, cols);
        _OTVSyntaxSearchEngine = new OTVSyntaxSearchEngine(this);
    }

    public void showGoToLineDialog(ActionEvent evt) {
        _OTVSyntaxSearchEngine.goToLine();
    }

    public int[] getBoundsOfSQLToBeExecuted() {
        int[] bounds = new int[2];
        bounds[0] = getSelectionStart();
        bounds[1] = getSelectionEnd();

        if (bounds[0] == bounds[1]) {
            bounds = getSqlBoundsBySeparatorRule();
        }

        return bounds;
    }

    /**
     * The non selection separator is two new lines. Two new lines with white
     * spaces in between is counted as separator too.
     */
    private int[] getSqlBoundsBySeparatorRule() {
        int iCaretPos = getCaretPosition();
        int[] bounds = new int[2];

        String sql = getText();

        bounds[0] = lastIndexOfStateSep(sql, iCaretPos);
        bounds[1] = indexOfStateSep(sql, iCaretPos);

        return bounds;
    }

    private static int lastIndexOfStateSep(String sql, int pos) {
        int ix = pos;

        int newLinteCount = 0;
        for (;;) {
            if (ix == sql.length()) {
                if (ix == 0) {
                    return ix;
                } else {
                    ix--;
                }
            }

            if (false == Character.isWhitespace(sql.charAt(ix))) {
                newLinteCount = 0;
            }

            if ('\n' == sql.charAt(ix)) {
                ++newLinteCount;
                if (2 == newLinteCount) {
                    return ix + newLinteCount;
                }
            }

            if (0 == ix) {
                return 0 + newLinteCount;
            }

            --ix;
        }
    }

    private static int indexOfStateSep(String sql, int pos) {
        int ix = pos;

        int newLinteCount = 0;
        for (;;) {
            if (sql.length() == ix) {
                return sql.length();
            }

            if (false == Character.isWhitespace(sql.charAt(ix))) {
                newLinteCount = 0;
            }

            if ('\n' == sql.charAt(ix)) {
                ++newLinteCount;
                if (2 == newLinteCount) {
                    return ix - 1;
                }
            }

            ++ix;
        }
    }
}