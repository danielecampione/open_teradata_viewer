/*
 * Open Teradata Viewer ( editor )
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

package net.sourceforge.open_teradata_viewer.editor;

import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.InputMap;
import javax.swing.KeyStroke;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;
import org.fife.ui.rsyntaxtextarea.Token;

import net.sourceforge.open_teradata_viewer.actions.Actions;
import net.sourceforge.open_teradata_viewer.editor.search.OTVSyntaxSearchEngine;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class OTVSyntaxTextArea extends RSyntaxTextArea {

    private static final long serialVersionUID = -4500559841244366663L;

    private OTVSyntaxSearchEngine _OTVSyntaxSearchEngine;

    int defaultModifier = getDefaultModifier();

    public OTVSyntaxTextArea() {
        this(0, 0);
    }

    public OTVSyntaxTextArea(int rows, int cols) {
        super(rows, cols);
        _OTVSyntaxSearchEngine = new OTVSyntaxSearchEngine(this);
        changeStyleProgrammatically();

        InputMap inputMap = getInputMap();
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, defaultModifier), Actions.RUN);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), Actions.RUN);
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

    /**
     * Returns the default modifier key for a system. For example, on Windows
     * this would be the CTRL key (<code>InputEvent.CTRL_MASK</code>).
     *
     * @return The default modifier key.
     */
    protected static final int getDefaultModifier() {
        return Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    }

    /** Changes the styles used in the editor programmatically. */
    private void changeStyleProgrammatically() {
        // Set the font for all token types
        setFont(this, new Font("Courier New", Font.PLAIN, 16));

        // Change a few things here and there
        SyntaxScheme scheme = getSyntaxScheme();
        scheme.getStyle(Token.RESERVED_WORD).background = Color.yellow;
        scheme.getStyle(Token.DATA_TYPE).foreground = Color.blue;
        scheme.getStyle(Token.LITERAL_STRING_DOUBLE_QUOTE).underline = true;
        scheme.getStyle(Token.COMMENT_EOL).font = new Font("Courier New", Font.PLAIN, 16);

        revalidate();
    }

    /**
     * Set the font for all token types.
     *
     * @param textArea The text area to modify.
     * @param font The font to use.
     */
    public static void setFont(RSyntaxTextArea textArea, Font font) {
        if (font != null) {
            SyntaxScheme ss = textArea.getSyntaxScheme();
            ss = (SyntaxScheme) ss.clone();
            for (int i = 0; i < ss.getStyleCount(); i++) {
                if (ss.getStyle(i) != null) {
                    ss.getStyle(i).font = font;
                }
            }
            textArea.setSyntaxScheme(ss);
            textArea.setFont(font);
        }
    }
}