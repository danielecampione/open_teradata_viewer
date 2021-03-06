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

import org.fife.ui.rsyntaxtextarea.FileLocation;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.FileIO;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FileOpenAction extends CustomAction implements SyntaxConstants {

    private static final long serialVersionUID = -2948843917732757209L;

    protected FileOpenAction() {
        super("Open File", "fileopen.png", null, null);
        setEnabled(true);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        File file = FileIO.chooseFile();
        if (file != null) {
            ApplicationFrame app = ApplicationFrame.getInstance();
            FileLocation loc = FileLocation.create(file);
            app.openFile(loc);
        }
    }
}