/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2011, D. Campione
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


import java.awt.Container;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Dialog;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.Settings;
import net.sourceforge.open_teradata_viewer.SettingsException;
import net.sourceforge.open_teradata_viewer.SettingsKeys;
import net.sourceforge.open_teradata_viewer.ThreadedAction;
import net.sourceforge.open_teradata_viewer.Tools;

import org.xml.sax.SAXException;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public abstract class CustomAction extends AbstractAction
        implements
            MouseListener {

    private static final long serialVersionUID = 1753928583474033071L;

    private static JFileChooser fileChooser;

    private static byte[][] savedLobs;

    public static boolean inProgress;

    /**
     * An object name cannot be a Teradata reserved word.
     */
    private String[] teradataReservedWords = {"CUBE", "GROUPING", "ROLLUP",
            "SETS", "ABORT", "ABORTSESSION", "ABS", "ABSOLUTE", "ACCESS_LOCK",
            "ACCOUNT", "ACOS", "ACOSH", "ACTION", "ADD", "ADD_MONTHS", "ADMIN",
            "AFTER", "AGGREGATE", "ALIAS", "ALL", "ALLOCATE", "ALTER", "AMP",
            "AND", "ANSIDATE", "ANY", "ARE", "ARRAY", "AS", "ASC", "ASIN",
            "ASINH", "ASSERTION", "AT", "ATAN", "ATAN2", "ATANH", "ATOMIC",
            "AUTHORIZATION", "AVE", "AVERAGE", "AVG", "BEFORE", "BEGIN",
            "BETWEEN", "BINARY", "BIT", "BLOB", "BOOLEAN", "BOTH", "BREADTH",
            "BT", "BUT", "BY", "BYTE", "BYTEINT", "BYTES", "CALL", "CASCADE",
            "CASCADED", "CASE", "CASE_N", "CASESPECIFIC", "CAST", "CATALOG",
            "CD", "CHAR", "CHAR_LENGTH", "CHAR2HEXINT", "CHARACTER",
            "CHARACTER_LENGTH", "CHARACTERS", "CHARS", "CHECK", "CHECKPOINT",
            "CLASS", "CLOB", "CLOSE", "CLUSTER", "CM", "COALESCE", "COLLATE",
            "COLLATION", "COLLECT", "COLUMN", "COMMENT", "COMMIT",
            "COMPLETION", "COMPRESS", "CONNECT", "CONNECTION", "CONSTRAINT",
            "CONSTRAINTS", "CONSTRUCTOR", "CONTINUE", "CONVERT_TABLE_HEADER",
            "CORR", "CORRESPONDING", "COS", "COSH", "COUNT", "COVAR_POP",
            "COVAR_SAMP", "CREATE", "CROSS", "CS", "CSUM", "CT", "CUBE",
            "CURRENT", "CURRENT_DATE", "CURRENT_PATH", "CURRENT_ROLE",
            "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR",
            "CV", "CYCLE", "DATA", "DATABASE", "DATABLOCKSIZE", "DATE",
            "DATEFORM", "DAY", "DEALLOCATE", "DEC", "DECIMAL", "DECLARE",
            "DEFAULT", "DEFERRABLE", "DEFERRED", "DEGREES", "DEL", "DELETE",
            "DEPTH", "DEREF", "DESC", "DESCRIBE", "DESCRIPTOR", "DESTROY",
            "DESTRUCTOR", "DETERMINISTIC", "DIAGNOSTIC", "DIAGNOSTICS",
            "DICTIONARY", "DISABLED", "DISCONNECT", "DISTINCT", "DO", "DOMAIN",
            "DOUBLE", "DROP", "DUAL", "DUMP", "DYNAMIC", "EACH", "ECHO",
            "ELSE", "ELSEIF", "ENABLED", "END", "END-EXEC", "EQ", "EQUALS",
            "ERROR", "ERRORFILES", "ERRORTABLES", "ESCAPE", "ET", "EVERY",
            "EXCEPT", "EXCEPTION", "EXEC", "EXECUTE", "EXISTS", "EXIT", "EXP",
            "EXPLAIN", "EXTERNAL", "EXTRACT", "FALLBACK", "FALSE",
            "FASTEXPORT", "FETCH", "FIRST", "FLOAT", "FOR", "FOREIGN",
            "FORMAT", "FOUND", "FREE", "FREESPACE", "FROM", "FULL", "FUNCTION",
            "GE", "GENERAL", "GENERATED", "GET", "GIVE", "GLOBAL", "GO",
            "GOTO", "GRANT", "GRAPHIC", "GROUP", "GROUPING", "GT", "HANDLER",
            "HASH", "HASHAMP", "HASHBAKAMP", "HASHBUCKET", "HASHROW", "HAVING",
            "HELP", "HOST", "HOUR", "IDENTITY", "IF", "IGNORE", "IMMEDIATE",
            "IN", "INCONSISTENT", "INDEX", "INDICATOR", "INITIALIZE",
            "INITIALLY", "INITIATE", "INNER", "INOUT", "INPUT", "INS",
            "INSERT", "INSTEAD", "INT", "INTEGER", "INTEGERDATE", "INTERSECT",
            "INTERVAL", "INTO", "IS", "ISOLATION", "ITERATE", "JOIN",
            "JOURNAL", "KEY", "KURTOSIS", "LANGUAGE", "LARGE", "LAST",
            "LATERAL", "LE", "LEADING", "LEAVE", "LEFT", "LESS", "LEVEL",
            "LIKE", "LIMIT", "LN", "LOADING", "LOCAL", "LOCALTIME",
            "LOCALTIMESTAMP", "LOCATOR", "LOCK", "LOCKING", "LOG", "LOGGING",
            "LOGON", "LONG", "LOOP", "LOWER", "LT", "MACRO", "MAP", "MATCH",
            "MAVG", "MAX", "MAXIMUM", "MCHARACTERS", "MDIFF", "MERGE", "MIN",
            "MINDEX", "MINIMUM", "MINUS", "MINUTE", "MLINREG", "MLOAD", "MOD",
            "MODE", "MODIFIES", "MODIFY", "MODULE", "MONITOR", "MONRESOURCE",
            "MONSESSION", "MONTH", "MSUBSTR", "MSUM", "MULTISET", "NAMED",
            "NAMES", "NATIONAL", "NATURAL", "NCHAR", "NCLOB", "NE", "NEW",
            "NEW_TABLE", "NEXT", "NO", "NONE", "NOT", "NOWAIT", "NULL",
            "NULLIF", "NULLIFZERO", "NUMERIC", "OBJECT", "OBJECTS",
            "OCTET_LENGTH", "OF", "OFF", "OLD", "OLD_TABLE", "ON", "ONLY",
            "OPEN", "OPERATION", "OPTION", "OR", "ORDER", "ORDINALITY", "OUT",
            "OUTER", "OUTPUT", "OVER", "OVERLAPS", "OVERRIDE", "PAD",
            "PARAMETER", "PARAMETERS", "PARTIAL", "PASSWORD", "PATH",
            "PERCENT", "PERCENT_RANK", "PERM", "PERMANENT", "POSITION",
            "POSTFIX", "PRECISION", "PREFIX", "PREORDER", "PREPARE",
            "PRESERVE", "PRIMARY", "PRIOR", "PRIVATE", "PRIVILEGES",
            "PROCEDURE", "PROFILE", "PROPORTIONAL", "PROTECTION", "PUBLIC",
            "QUALIFIED", "QUALIFY", "QUANTILE", "RADIANS", "RANDOM", "RANGE_N",
            "RANK", "READ", "READS", "REAL", "RECURSIVE", "REF", "REFERENCES",
            "REFERENCING", "REGR_AVGX", "REGR_AVGY", "REGR_COUNT",
            "REGR_INTERCEPT", "REGR_R2", "REGR_SLOPE", "REGR_SXX", "REGR_SXY",
            "REGR_SYY", "RELATIVE", "RELEASE", "RENAME", "REPEAT", "REPLACE",
            "REPLICATION", "REPOVERRIDE", "REQUEST", "RESTART", "RESTORE",
            "RESTRICT", "RESULT", "RESUME", "RET", "RETRIEVE", "RETURN",
            "RETURNS", "REVALIDATE", "REVOKE", "RIGHT", "RIGHTS", "ROLE",
            "ROLLBACK", "ROLLFORWARD", "ROLLUP", "ROUTINE", "ROW",
            "ROW_NUMBER", "ROWID", "ROWS", "SAMPLE", "SAMPLEID", "SAVEPOINT",
            "SCHEMA", "SCOPE", "SCROLL", "SEARCH", "SECOND", "SECTION", "SEL",
            "SELECT", "SEQUENCE", "SESSION", "SESSION_USER", "SET",
            "SETRESRATE", "SETS", "SETSESSRATE", "SHOW", "SIN", "SINH", "SIZE",
            "SKEW", "SMALLINT", "SOME", "SOUNDEX", "SPACE", "SPECIFIC",
            "SPECIFICTYPE", "SPOOL", "SQL", "SQLEXCEPTION", "SQLSTATE",
            "SQLTEXT", "SQLWARNING", "SQRT", "SS", "START", "STARTUP", "STATE",
            "STATEMENT", "STATIC", "STATISTICS", "STDDEV_POP", "STDDEV_SAMP",
            "STEPINFO", "STRING_CS", "STRUCTURE", "SUBSCRIBER", "SUBSTR",
            "SUBSTRING", "SUM", "SUMMARY", "SUSPEND", "SYSTEM_USER", "TABLE",
            "TAN", "TANH", "TBL_CS", "TEMPORARY", "TERMINATE", "THAN", "THEN",
            "THRESHOLD", "TIME", "TIMESTAMP", "TIMEZONE_HOUR",
            "TIMEZONE_MINUTE", "TITLE", "TO", "TRACE", "TRAILING",
            "TRANSACTION", "TRANSLATE", "TRANSLATE_CHK", "TRANSLATION",
            "TREAT", "TRIGGER", "TRIM", "TRUE", "TYPE", "UC", "UNDEFINED",
            "UNDER", "UNDO", "UNION", "UNIQUE", "UNKNOWN", "UNNEST", "UNTIL",
            "UPD", "UPDATE", "UPPER", "UPPERCASE", "USAGE", "USER", "USING",
            "VALUE", "VALUES", "VAR_POP", "VAR_SAMP", "VARBYTE", "VARCHAR",
            "VARGRAPHIC", "VARIABLE", "VARYING", "VIEW", "VOLATILE", "WAIT",
            "WHEN", "WHENEVER", "WHERE", "WHILE", "WIDTH_BUCKET", "WITH",
            "WITHOUT", "WORK", "WRITE", "YEAR", "ZEROIFNULL", "ZONE", "ADMIN",
            "AGGREGATE", "BLOB", "CASE_N", "CLASS", "CLOB", "CLOSE", "CYCLE",
            "DEGREES", "DETERMINISTIC", "EXTERNAL", "FETCH", "FIRST",
            "FUNCTION", "GENERATED", "IDENTITY", "INPUT", "LANGUAGE", "LIMIT",
            "LOCATOR", "MERGE", "NEXT", "NONE", "OBJECTS", "ONLY", "OPEN",
            "PARAMETER", "PREPARE", "PROFILE", "PROPORTIONAL", "RADIANS",
            "RANGE_N", "RETURNS", "ROW_NUMBER", "SCROLL", "SPECIFIC", "SQL",
            "SQLTEXT", "START", "STEPINFO", "SUMMARY", "THRESHOLD", "TRACE",
            "WITHOUT"};

    /**
     * An object name can contain the letters A through Z, the digits 0 through
     * 9, the underscore (_), $, and #. A name in double quotation marks can
     * contain any characters except double quotation marks.
     */
    private String[] teradataLegalChars = {"A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3", "4", "5", "6",
            "7", "8", "9", "_", "$", "#", "."};

    protected CustomAction(String name) {
        this(name, null, null, null);
    }

    protected CustomAction(String name, String icon, KeyStroke accelerator,
            String shortDescription) {
        super(name);
        if (icon != null) {
            putValue(
                    SMALL_ICON,
                    new ImageIcon(CustomAction.class.getResource("/icons/"
                            + icon)));
        }
        if (accelerator != null) {
            putValue(ACCELERATOR_KEY, accelerator);
        }
        if (shortDescription != null) {
            putValue(SHORT_DESCRIPTION, shortDescription);
        }
        setEnabled(true);
    }

    public void actionPerformed(final ActionEvent e) {
        if (!inProgress) {
            inProgress = true;
            new ThreadedAction() {
                @Override
                protected void execute() throws Exception {
                    performThreaded(e);
                }
            };
        } else {
            ApplicationFrame.getInstance().changeLog.append(
                    "Another process is already running..\n",
                    ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
        }
    }

    protected abstract void performThreaded(ActionEvent e) throws Exception;

    public void saveAndOpenFile(String fileName, byte[] bytes) throws Exception {
        File file = saveFile(fileName, bytes);
        if (file != null
                && Dialog.YES_OPTION == Dialog.show("Open file",
                        "Open the file with the associated application?",
                        Dialog.QUESTION_MESSAGE, Dialog.YES_NO_OPTION)) {
            openFile(file);
        }
    }

    public File saveFile(String fileName, byte[] bytes) throws Exception {
        JFileChooser fileChooser = getFileChooser();
        fileChooser.setSelectedFile(new File(fileName));
        if (JFileChooser.APPROVE_OPTION == fileChooser
                .showSaveDialog(ApplicationFrame.getInstance())) {
            try {
                Settings.write(SettingsKeys.LAST_USED_DIR_KEY, fileChooser
                        .getCurrentDirectory().getCanonicalPath());
            } catch (SettingsException e1) {
                // ignore
            }
            File selectedFile = fileChooser.getSelectedFile();
            if (!selectedFile.exists()
                    || Dialog.YES_OPTION == Dialog.show("File exists",
                            "Overwrite existing file?",
                            Dialog.QUESTION_MESSAGE, Dialog.YES_NO_OPTION)) {
                FileOutputStream out = new FileOutputStream(selectedFile);
                out.write(bytes);
                out.close();
                return selectedFile;
            }
        }
        return null;
    }

    private void openFile(File file) throws IOException {
        Desktop.getDesktop().open(file);
    }

    protected static JFileChooser getFileChooser() throws IOException,
            SAXException, ParserConfigurationException {
        if (fileChooser == null) {
            fileChooser = new JFileChooser();
        }
        String dir = Settings.load(SettingsKeys.LAST_USED_DIR_KEY,
                SettingsKeys.LAST_USED_DIR_DEFAULT_VALUE);
        if (dir != null) {
            fileChooser.setCurrentDirectory(new File(dir));
        }
        return fileChooser;
    }

    public void showFile(String text, byte[] bytes) throws Exception {
        JTextArea textArea = new JTextArea(text);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        boolean isXml = text.startsWith("<?xml");
        Object[] options = isXml ? new Object[]{"Save to file",
                "Save to file and open", "Copy to clipboard",
                "Pretty print XML", "Cancel"} : new Object[]{"Save to file",
                "Save to file and open", "Copy to clipboard", "Cancel"};
        Object value = Dialog.show("Preview", scrollPane, Dialog.PLAIN_MESSAGE,
                options, "Save to file");
        if ("Save to file".equals(value)) {
            String fileName = isXml ? "export.xml" : "export.txt";
            saveFile(fileName, bytes != null ? bytes : text.getBytes());
        } else if ("Save to file and open".equals(value)) {
            String fileName = isXml ? "export.xml" : "export.txt";
            File file = saveFile(fileName,
                    bytes != null ? bytes : text.getBytes());
            if (file != null) {
                openFile(file);
            }
        } else if ("Copy to clipboard".equals(value)) {
            try {
                Toolkit.getDefaultToolkit().getSystemClipboard()
                        .setContents(new StringSelection(text), null);
            } catch (Throwable t2) {
                ExceptionDialog.hideException(t2);
            }
        } else if ("Pretty print XML".equals(value)) {
            try {
                Transformer transformer = TransformerFactory.newInstance()
                        .newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(
                        "{http://xml.apache.org/xslt}indent-amount", "4");
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                transformer.transform(new StreamSource(new StringReader(text)),
                        new StreamResult(outputStream));
                text = outputStream.toString();
            } catch (Throwable t) {
                ExceptionDialog.showException(t);
            }
            showFile(text, bytes);
        }
    }

    protected static byte[][] getSavedLobs() {
        return savedLobs;
    }

    protected static void setSavedLobs(byte[][] newSavedLobs) {
        savedLobs = newSavedLobs;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && e.getSource() instanceof JList) {
            Container container = (Container) e.getSource();
            while (!(container instanceof JOptionPane)) {
                container = container.getParent();
            }
            JOptionPane optionPane = (JOptionPane) container;
            Object value = optionPane.getInitialValue();
            if (value == null) {
                value = JOptionPane.OK_OPTION;
            }
            optionPane.setValue(value);
            while (!(container instanceof JDialog)) {
                container = container.getParent();
            }
            container.setVisible(false);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    protected boolean canBeAnObjectName(String text) {
        if (!Tools.isEmpty(text) && text.trim().length() > 0) {
            text = text.trim();
            // A Teradata object name must be from 1 to 30 characters long.
            int lastTokenIndex = text.lastIndexOf(".");
            if (text.substring(lastTokenIndex == -1 ? 0 : lastTokenIndex,
                    text.length()).length() >= 30) {
                ApplicationFrame.getInstance().changeLog
                        .append("A Teradata object name must be from 1 to 30 characters long.\n",
                                ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                return false;
            }
            int numRetries;
            for (int i = 0; i < text.length(); i++) {
                numRetries = 0;
                for (int j = 0; j < teradataLegalChars.length; j++) {
                    if (text.toUpperCase().charAt(i) != teradataLegalChars[j]
                            .charAt(0)) {
                        numRetries++;
                    } else {
                        break;
                    }
                }
                if (numRetries == teradataLegalChars.length) {
                    ApplicationFrame.getInstance().changeLog
                            .append("You're trying to perform a SQL command using an illegal character.\n",
                                    ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                    return false;
                }
            }
            for (int i = 0; i < teradataReservedWords.length; i++) {
                if (text.equalsIgnoreCase(teradataReservedWords[i])) {
                    ApplicationFrame.getInstance().changeLog.append(
                            "You're trying to perform a SQL command using a Teradata reserved word: \""
                                    + teradataReservedWords[i] + "\".\n",
                            ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                    return false;
                }
            }
            // A Teradata object name cannot be a number
            try {
                Double.parseDouble(text);
                ApplicationFrame.getInstance().changeLog.append(
                        "A Teradata object name cannot be a number.\n",
                        ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                return false;
            } catch (NumberFormatException nfe) {
                return true;
            }
        }
        return false;
    }
}
