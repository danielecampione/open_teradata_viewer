/*
 * Open Teradata Viewer ( editor search )
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

package net.sourceforge.open_teradata_viewer.editor.search;

import javax.swing.text.BadLocationException;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.UISupport;
import net.sourceforge.open_teradata_viewer.editor.OTVSyntaxTextArea;

/**
 *
 *
 * @author D. Campione
 *
 */
public class OTVSyntaxSearchEngine {

    private OTVSyntaxTextArea _OTVSyntaxTextArea;

    public OTVSyntaxSearchEngine(OTVSyntaxTextArea _OTVSyntaxTextArea) {
        this._OTVSyntaxTextArea = _OTVSyntaxTextArea;
    }

    public void goToLine() {
        OTVGoToDialog _OTVGoToDialog = ApplicationFrame.getInstance().getGoToDialog();

        _OTVGoToDialog.setMaxLineNumberAllowed(_OTVSyntaxTextArea
                .getLineCount());
        UISupport.showDialog(_OTVGoToDialog);

        int lineNumber = _OTVGoToDialog.getLineNumber();
        if (lineNumber > 0) {
            try {
                _OTVSyntaxTextArea.setCaretPosition(_OTVSyntaxTextArea
                        .getLineStartOffset(lineNumber - 1));
            } catch (BadLocationException ble) {
                UISupport.getDialogs().showInfoMessage("Invalid line number.",
                        "Go To");
            }
        }
    }
}