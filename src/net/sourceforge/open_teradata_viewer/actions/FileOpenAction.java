/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2014, D. Campione
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

import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.TransferHandler;
import javax.swing.TransferHandler.TransferSupport;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Context;
import net.sourceforge.open_teradata_viewer.FileIO;
import net.sourceforge.open_teradata_viewer.editor.syntax.ISyntaxConstants;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FileOpenAction extends CustomAction implements ISyntaxConstants {

    private static final long serialVersionUID = -2948843917732757209L;

    protected FileOpenAction() {
        super("Open File", "fileopen.png", null, null);
        setEnabled(true);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        ApplicationFrame applicationFrame = ApplicationFrame.getInstance();
        File file = FileIO.openFile();
        if (file != null) {
            applicationFrame.setText("");
            Context.getInstance().setOpenedFile(file);
            SyntaxTextArea textArea = ApplicationFrame.getInstance()
                    .getTextComponent();
            TransferHandler transferHandler = textArea.getTransferHandler();
            StringSelection stringSelection = new StringSelection(new String(
                    FileIO.readFile(file)));
            transferHandler.importData(new TransferSupport(textArea,
                    stringSelection));
            textArea.setCaretPosition(0);
        }
    }
}