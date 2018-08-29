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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.fife.ui.rsyntaxtextarea.FileLocation;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.macros.Macro;
import net.sourceforge.open_teradata_viewer.editor.macros.NewMacroDialog;
import net.sourceforge.open_teradata_viewer.util.IOUtil;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class NewMacroAction extends CustomAction {

    private static final long serialVersionUID = 1785032375160581778L;

    public NewMacroAction() {
        super("New macro...", null, null, null);
        setEnabled(true);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        ApplicationFrame owner = ApplicationFrame.getInstance();
        NewMacroDialog nmd = new NewMacroDialog(owner);
        nmd.setVisible(true);

        Macro macro = nmd.getMacro();
        if (macro != null) {
            File file = new File(macro.getFile());
            if (!file.isFile()) { // Should always be true
                createInitialContentByExtension(file);
            }

            if (file != null) {
                ApplicationFrame app = ApplicationFrame.getInstance();
                FileLocation loc = FileLocation.create(file);
                app.openFile(loc);
            }
        }
    }

    /**
     * Creates a new file with initial content for a macro, based on the
     * file's extension.
     *
     * @param file The file's extension.
     */
    private void createInitialContentByExtension(File file) {
        try {
            PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            String fileName = file.getName();
            String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
            String content = getInitialContentImpl(ext);
            w.println(content);
            w.close();
        } catch (IOException ioe) {
            ExceptionDialog.hideException(ioe);
        }
    }

    private String getInitialContentImpl(String ext) throws IOException {
        InputStream in = Macro.class.getResourceAsStream(ext + ".template.txt");
        return IOUtil.readFully(in);
    }
}