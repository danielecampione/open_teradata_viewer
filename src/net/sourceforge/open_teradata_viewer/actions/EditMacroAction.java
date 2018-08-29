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
import java.io.File;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Dialog;
import net.sourceforge.open_teradata_viewer.editor.macros.Macro;
import net.sourceforge.open_teradata_viewer.editor.macros.MacroManager;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class EditMacroAction extends CustomAction {

    private static final long serialVersionUID = -5849819778035858039L;

    protected EditMacroAction() {
        super("Edit macros...");
        setEnabled(true);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        Iterator<Macro> macros = MacroManager.get().getMacroIterator();
        Vector<Macro> vectorMacro = new Vector<Macro>(1, 1);
        while (macros.hasNext()) {
            vectorMacro.add(macros.next());
        }

        final JList<?> list = new JList(vectorMacro);
        list.addMouseListener(this);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Object value = Dialog.show("Macros", new JScrollPane(list), Dialog.PLAIN_MESSAGE,
                new Object[] { "Edit the script path..." }, "Edit");
        if ("Edit the script path...".equals(value)) {
            if (!list.isSelectionEmpty()) {
                Macro macro = vectorMacro.get(list.getSelectedIndex());
                setTheScriptPath(macro);
            }
        }
    }

    /** Called when the user clicks the "Edit the script path..." button. */
    private void setTheScriptPath(Macro macro) {
        String text;
        int messageType = JOptionPane.INFORMATION_MESSAGE;

        String currentFile = macro.getFile();
        File file = new File(currentFile);
        if (file.isFile()) { // Should always be true
            String scriptFullPath = JOptionPane.showInputDialog("Enter the absolute path of the macro script file:",
                    currentFile);
            if (scriptFullPath == null || scriptFullPath.trim().length() == 0) {
                return;
            }
            file = new File(scriptFullPath);
            if (file.isFile()) {
                macro.setFile(scriptFullPath);

                text = "The path of the script of the macro {0} has been modified.";
                text = MessageFormat.format(text, macro.getName());
            } else {
                return;
            }
        } else { // Macro script was deleted outside of Open Teradata Viewer
            text = "Error: macro script file does not exist:\n{0}";
            text = MessageFormat.format(text, file.getAbsolutePath());
            messageType = JOptionPane.ERROR_MESSAGE;
        }

        String title = "Information";
        JOptionPane.showMessageDialog(ApplicationFrame.getInstance(), text, title, messageType);
    }
}