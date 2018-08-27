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

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.CollapsibleSectionPanel;
import net.sourceforge.open_teradata_viewer.editor.OTVSyntaxTextArea;
import net.sourceforge.open_teradata_viewer.editor.search.FindDialog;
import net.sourceforge.open_teradata_viewer.editor.search.ReplaceDialog;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class GoToLineAction extends CustomAction {

    private static final long serialVersionUID = 7902153669933142665L;

    protected GoToLineAction() {
        super("Go To..", null, KeyStroke.getKeyStroke(KeyEvent.VK_G, Toolkit
                .getDefaultToolkit().getMenuShortcutKeyMask()), null);
        setEnabled(true);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // The "goto line" process can be performed altough other processes are
        // running. No ThreadAction object must be instantiated because the
        // focus must still remains on the "goto line" frame
        try {
            performThreaded(e);
        } catch (Throwable t) {
            ExceptionDialog.showException(t);
        }
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        ApplicationFrame applicationFrame = ApplicationFrame.getInstance();
        FindDialog findDialog = applicationFrame.getFindDialog();
        ReplaceDialog replaceDialog = applicationFrame.getReplaceDialog();
        OTVSyntaxTextArea textArea = applicationFrame.getTextComponent();
        CollapsibleSectionPanel csp = applicationFrame
                .getCollapsibleSectionPanel();

        csp.hideBottomComponent();
        if (findDialog.isVisible()) {
            findDialog.setVisible(false);
        }
        if (replaceDialog.isVisible()) {
            replaceDialog.setVisible(false);
        }

        textArea.showGoToLineDialog(e);
    }
}