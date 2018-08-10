/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2012, D. Campione
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

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import net.sourceforge.open_teradata_viewer.util.Utilities;

import org.joda.time.DateTime;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class Console extends JTextPane {

    private static final long serialVersionUID = -6912021687768592375L;

    int maxCharacters;
    File logFile;
    BufferedWriter bw;
    int fileIndex;

    public Console(int width, int height, int maxChars) {
        super();
        setSize(width, height);
        maxCharacters = maxChars;

        fileIndex = getGreatestFileIndex();
        DateTime dateTime = new DateTime(new java.util.Date());
        logFile = new File(Utilities.conformizePath(System
                .getProperty("user.home"))
                + "open_teradata_viewer_"
                + String.format("%04d", dateTime.getYear())
                + "-"
                + String.format("%02d", dateTime.getMonthOfYear())
                + "-"
                + String.format("%02d", dateTime.getDayOfMonth()) + ".log");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException ioe) {
                ExceptionDialog.notifyException(ioe);
                UISupport.getDialogs().showErrorMessage(
                        "Unable to create the log file.");
            }
        }
        try {
            bw = new BufferedWriter(new FileWriter(logFile, true));
        } catch (IOException ioe) {
            ExceptionDialog.showException(ioe);
        }
    }

    public void print(String text, Color foregroundColor) {
        if (!((getDocument().getLength() + text.length()) <= maxCharacters)) {
            setText("");
        }

        // StyleContext
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
                StyleConstants.Foreground, foregroundColor);

        int len = getDocument().getLength(); // same value as getText().length();
        setCaretPosition(len); // place caret at the end (with no selection)
        setCharacterAttributes(aset, false);
        replaceSelection(text); // there is no selection, so inserts at caret

        if (logFile.exists()) {
            if (logFile.length() > 10000000) { // 10000000 equals to 10
                // MegaBytes
                try {
                    bw.close();
                } catch (IOException ioe) {
                    ExceptionDialog.ignoreException(ioe);
                }
                logFile.renameTo(new File(logFile.getAbsolutePath() + "-"
                        + fileIndex++));
                DateTime dateTime = new DateTime(new java.util.Date());
                logFile = new File(Utilities.conformizePath(System
                        .getProperty("user.home"))
                        + "open_teradata_viewer_"
                        + String.format("%04d", dateTime.getYear())
                        + "-"
                        + String.format("%02d", dateTime.getMonthOfYear())
                        + "-"
                        + String.format("%02d", dateTime.getDayOfMonth())
                        + ".log");
                try {
                    logFile.createNewFile();
                } catch (IOException ioe) {
                    ExceptionDialog.notifyException(ioe);
                    UISupport.getDialogs().showErrorMessage(
                            "Unable to create the log file.");
                }

                try {
                    bw = new BufferedWriter(new FileWriter(logFile));
                } catch (IOException ioe) {
                    ExceptionDialog.showException(ioe);
                }
            }
            try {
                bw.append(text);
                bw.flush();
            } catch (IOException ioe) {
                ExceptionDialog.showException(ioe);
            }
        }
    }

    public void print(String text) {
        print(text, ApplicationFrame.DEFAULT_FOREGROUND_COLOR_LOG);
    }

    public void println(String text) {
        print(text + System.getProperty("line.separator"),
                ApplicationFrame.DEFAULT_FOREGROUND_COLOR_LOG);
    }

    public void println(String text, Color foregroundColor) {
        print(text + System.getProperty("line.separator"), foregroundColor);
    }

    public void print(Object obj) {
        print(String.valueOf(obj));
    }

    public void println(Object obj) {
        println(String.valueOf(obj));
    }

    public void print(Object obj, Color foregroundColor) {
        print(String.valueOf(obj), foregroundColor);
    }

    public void println(Object obj, Color foregroundColor) {
        println(String.valueOf(obj), foregroundColor);
    }

    private int getGreatestFileIndex() {
        DateTime dateTime = new DateTime(new java.util.Date());
        String fileName = "open_teradata_viewer_"
                + String.format("%04d", dateTime.getYear()) + "-"
                + String.format("%02d", dateTime.getMonthOfYear()) + "-"
                + String.format("%02d", dateTime.getDayOfMonth()) + ".log-";
        File userHome = new File(Utilities.conformizePath(System
                .getProperty("user.home")));
        File[] listedFiles = Utilities.listFiles(userHome);
        Vector<Integer> listedFilesIndexVector = new Vector<Integer>(1, 1);
        for (int i = 0; i < listedFiles.length; i++) {
            if (!listedFiles[i].isDirectory()) {
                if (listedFiles[i].getName().startsWith(fileName)
                        && listedFiles[i].getName().length() > (fileName)
                                .length()) {
                    listedFilesIndexVector.add(new Integer(listedFiles[i]
                            .getName().substring((fileName).length(),
                                    listedFiles[i].getName().length())));
                }
            }
        }
        if (listedFilesIndexVector.size() == 0) {
            return 1;
        }

        Integer[] listedFilesIndex = new Integer[listedFilesIndexVector.size()];
        int i = 0;
        for (Integer integer : listedFilesIndexVector) {
            listedFilesIndex[i++] = integer;
        }
        Arrays.sort(listedFilesIndex);
        return ++listedFilesIndex[listedFilesIndex.length - 1];
    }
}