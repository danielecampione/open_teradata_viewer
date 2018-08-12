/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFileChooser;

import com.l2fprod.common.swing.JDirectoryChooser;

/**
 * 
 * @author D. Campione
 */
public class SwingFileDialogs implements IXFileDialogs {
    private static Component parent;
    private static Map<Object, JFileChooser> choosers = new HashMap<Object, JFileChooser>();

    public SwingFileDialogs(Component parent) {
        SwingFileDialogs.parent = parent;
    }

    public static synchronized JFileChooser getChooser(Object action) {
        action = null;
        JFileChooser chooser = choosers.get(action);
        if (chooser == null) {
            chooser = new JFileChooser();
            choosers.put(action, chooser);
        }

        chooser.resetChoosableFileFilters();

        return chooser;
    }

    public static Component getParent() {
        return parent;
    }

    public File saveAs(Object action, String title) {
        return saveAs(action, title, null, null, null);
    }

    public File saveAs(Object action, String title, String extension,
            String fileType, File defaultFile) {
        JFileChooser chooser = getChooser(action);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle(title);
        chooser.setAcceptAllFileFilterUsed(true);

        if ((extension != null) && (fileType != null)) {
            chooser.setFileFilter(new ExtensionFileFilter(extension, fileType));
        } else {
            chooser.setFileFilter(null);
        }

        if (defaultFile != null) {
            chooser.setSelectedFile(defaultFile);
        } else {
            chooser.setSelectedFile(null);
        }

        if (chooser.showSaveDialog(getParent()) != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        return chooser.getSelectedFile();
    }

    public File open(Object action, String title, String extension,
            String fileType, String current) {
        return openFile(action, title, extension, fileType, current);
    }

    public static File openFile(Object action, String title, String extension,
            String fileType, String current) {
        JFileChooser chooser = getChooser(action);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle(title);
        chooser.setAcceptAllFileFilterUsed(true);
        if (current != null) {
            File file = new File(current);
            if (file.isDirectory()) {
                chooser.setCurrentDirectory(file);
            } else {
                chooser.setSelectedFile(file);
            }
        } else {
            chooser.setSelectedFile(null);
        }

        if ((extension != null) && (fileType != null)) {
            chooser.setFileFilter(new ExtensionFileFilter(extension, fileType));
        } else {
            chooser.setFileFilter(null);
        }

        if (chooser.showOpenDialog(getParent()) != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        return chooser.getSelectedFile();
    }

    public File openXML(Object action, String title) {
        return open(action, title, ".xml", "XML Files (*.xml)", null);
    }

    public File openDirectory(Object action, String title, File defaultDirectory) {
        JDirectoryChooser chooser = new JDirectoryChooser(defaultDirectory);
        chooser.setDialogTitle(title);
        chooser.setShowingCreateDirectory(true);
        chooser.setPreferredSize(new Dimension(400, 400));
        if (chooser.showSaveDialog(getParent()) != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        return chooser.getSelectedFile();

        // JFileChooser chooser = getChooser( action );
        // chooser.setDialogTitle( title );
        // chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
        //
        // if( defaultDirectory != null )
        // chooser.setCurrentDirectory( defaultDirectory );
        //
        // if( chooser.showOpenDialog( getParent() ) !=
        // JFileChooser.APPROVE_OPTION )
        // return null;
        //
        // return chooser.getSelectedFile();
    }

    public File openFileOrDirectory(Object action, String title,
            File defaultDirectory) {
        JFileChooser chooser = getChooser(action);
        chooser.setDialogTitle(title);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        if (defaultDirectory != null) {
            chooser.setCurrentDirectory(defaultDirectory);
        }

        if (chooser.showOpenDialog(getParent()) != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        return chooser.getSelectedFile();
    }

    public File saveAsDirectory(Object action, String title,
            File defaultDirectory) {
        JDirectoryChooser chooser = new JDirectoryChooser(defaultDirectory);
        chooser.setDialogTitle(title);
        chooser.setShowingCreateDirectory(true);
        chooser.setAcceptAllFileFilterUsed(true);
        if (chooser.showSaveDialog(getParent()) != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        return chooser.getSelectedFile();

        // JFileChooser chooser = getChooser( action );
        // chooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
        // chooser.setDialogTitle( title );
        // chooser.setAcceptAllFileFilterUsed( true );
        //
        // if( defaultDirectory != null )
        // chooser.setSelectedFile( defaultDirectory );
        // else
        // chooser.setSelectedFile( null );
        //
        // if( chooser.showSaveDialog( getParent() ) !=
        // JFileChooser.APPROVE_OPTION )
        // return null;
        //
        // return chooser.getSelectedFile();
    }
}