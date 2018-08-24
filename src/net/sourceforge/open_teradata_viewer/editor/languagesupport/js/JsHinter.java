/*
 * Open Teradata Viewer ( editor language support js )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.BadLocationException;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxDocument;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.DefaultParseResult;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.DefaultParserNotice;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.IParserNotice;

/**
 * Launches jshint as an external process to parse JavaScript in an {@link
 * SyntaxTextArea}. Note that this is pretty inefficient and was mainly done as
 * a test of jshint integration. In the future, the external process should be
 * launched in a separate thread.
 *
 * @author D. Campione
 * 
 */
class JsHinter {

    private JavaScriptParser parser;
    private DefaultParseResult result;

    private static final Map<String, MarkStrategy> MARK_STRATEGIES;
    static {
        MARK_STRATEGIES = new HashMap<String, MarkStrategy>();
        MARK_STRATEGIES.put("E015", MarkStrategy.MARK_CUR_TOKEN); // Unclosed regular expression
        MARK_STRATEGIES.put("E019", MarkStrategy.MARK_CUR_TOKEN); // Unmatched '{a}'
        MARK_STRATEGIES.put("E030", MarkStrategy.MARK_CUR_TOKEN); // Expected an identifier and instead saw '{a}'
        MARK_STRATEGIES.put("E041", MarkStrategy.STOP_PARSING); // Unrecoverable syntax error
        MARK_STRATEGIES.put("E042", MarkStrategy.STOP_PARSING); // Stopping
        MARK_STRATEGIES.put("E043", MarkStrategy.STOP_PARSING); // Too many errors
        MARK_STRATEGIES.put("W004", MarkStrategy.MARK_PREV_NON_WS_TOKEN); // '{a}' is already defined
        MARK_STRATEGIES.put("W015", MarkStrategy.MARK_CUR_TOKEN); // Expected {a} to have an indentation of {b} instead at {c}
        MARK_STRATEGIES.put("W032", MarkStrategy.MARK_PREV_TOKEN); // Unnecessary semicolon
        MARK_STRATEGIES.put("W033", MarkStrategy.MARK_PREV_TOKEN); // Missing semicolon
        MARK_STRATEGIES.put("W060", MarkStrategy.MARK_CUR_TOKEN); // document.write can be a form of eval
        MARK_STRATEGIES.put("W098", MarkStrategy.MARK_PREV_TOKEN); // '{a}' is defined but never used
        MARK_STRATEGIES.put("W116", MarkStrategy.MARK_PREV_TOKEN); // Expected '{a}' and instead saw '{b}'
        MARK_STRATEGIES.put("W117", MarkStrategy.MARK_CUR_TOKEN); // '{a}' is not defined
    }

    private JsHinter(JavaScriptParser parser, SyntaxDocument doc,
            DefaultParseResult result) {
        this.parser = parser;
        this.result = result;
    }

    public static void parse(JavaScriptParser parser, SyntaxDocument doc,
            DefaultParseResult result) throws IOException {
        String stdout = null;

        List<String> command = new ArrayList<String>();
        if (File.separatorChar == '\\') {
            command.add("cmd.exe");
            command.add("/c");
        } else {
            command.add("/bin/sh");
            command.add("-c");
        }
        command.add("jshint");
        if (parser.getJsHintRCFile() != null) {
            command.add("--config");
            command.add(parser.getJsHintRCFile().getAbsolutePath());
        }
        command.add("--verbose"); // Allows to decide error vs. warning
        command.add("-");

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream();

        Process p = pb.start();
        PrintWriter w = new PrintWriter(p.getOutputStream());

        // Create threads to read the stdout and stderr of the external process.
        // If we do not do it this way, the process may deadlock
        InputStream outStream = p.getInputStream();
        InputStream errStream = p.getErrorStream();
        StreamReaderThread stdoutThread = new StreamReaderThread(outStream);
        StreamReaderThread stderrThread = new StreamReaderThread(errStream);
        stdoutThread.start();

        try {
            String text = doc.getText(0, doc.getLength());
            w.print(text);
            w.flush();
            w.close();

            /*rc = */p.waitFor();
            p = null;

            // Save the stdout and stderr. Don't interrupt reader threads; just
            // wait for them to terminate normally
            //stdoutThread.interrupt();
            //stderrThread.interrupt();
            stdoutThread.join();
            stderrThread.join();
            stdout = stdoutThread.getStreamOutput();
        } catch (InterruptedException ie) {
            stdoutThread.interrupt();
        } catch (BadLocationException ble) {
            ExceptionDialog.hideException(ble); // Never happens
        } finally {
            if (outStream != null) {
                outStream.close();
            }
            w.close();
            if (p != null) {
                p.destroy();
            }
        }

        JsHinter hinter = new JsHinter(parser, doc, result);
        hinter.parseOutput(doc, stdout);
    }

    private void parseOutput(SyntaxDocument doc, String output) {
        // Line format:
        // stdin: line xx, col yy, error-or-warning-text. (Ennn)

        // Element root = doc.getDefaultRootElement();
        // int indent = parser.getJsHintIndent();

        String[] lines = output.split("\r?\n");
        // OUTER:
        for (String line : lines) {
            String origLine = line;
            if (line.startsWith("stdin: line ")) {
                line = line.substring("stdin: line ".length());
                int end = 0;
                while (Character.isDigit(line.charAt(end))) {
                    end++;
                }
                int lineNum = Integer.parseInt(line.substring(0, end)) - 1;
                if (lineNum == -1) {
                    // Probably bad option to jshint, e.g.
                    // stdin: line 0, col 0, Bad option: 'ender'. (E001)
                    // Just give them the entire error
                    DefaultParserNotice dpn = new DefaultParserNotice(parser,
                            origLine, 0);
                    result.addNotice(dpn);
                    continue;
                }
                line = line.substring(end);
                if (line.startsWith(", col ")) {
                    line = line.substring(", col ".length());
                    end = 0;
                    while (Character.isDigit(line.charAt(end))) {
                        end++;
                    }
                    line = line.substring(end);
                    if (line.startsWith(", ")) {
                        String msg = line.substring(", ".length());
                        String errorCode = null;

                        // Ends in "(E0xxx)" or "(W0xxx)"
                        int noticeType = IParserNotice.ERROR;
                        if (msg.charAt(msg.length() - 1) == ')') {
                            int openParen = msg.lastIndexOf('(');
                            errorCode = msg.substring(openParen + 1,
                                    msg.length() - 1);
                            if (msg.charAt(openParen + 1) == 'W') {
                                noticeType = IParserNotice.WARNING;
                            }
                            msg = msg.substring(0, openParen - 1);
                        }

                        DefaultParserNotice dpn = null;
                        MarkStrategy markStrategy = getMarkStrategy(errorCode);
                        switch (markStrategy) {
                        default:
                        case MARK_LINE:
                            // Just mark the whole line, as the offset returned
                            // by JSHint can vary
                            dpn = new DefaultParserNotice(parser, msg, lineNum);
                            break;
                        }
                        dpn.setLevel(noticeType);
                        result.addNotice(dpn);
                    }
                }
            }
        }
    }

    private static final MarkStrategy getMarkStrategy(String msgCode) {
        MarkStrategy strategy = MARK_STRATEGIES.get(msgCode);
        return strategy != null ? strategy : MarkStrategy.MARK_LINE;
    }

    /**
     * A thread dedicated to reading either the stdout or stderr stream of an
     * external process. These streams are read in a dedicated thread to ensure
     * they are consumed appropriately to prevent deadlock. This idea was taken
     * from <a
     * href="http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps_p.html">
     * this JavaWorld article</a>.
     * 
     * @author D. Campione
     * 
     */
    static class StreamReaderThread extends Thread {

        private BufferedReader r;
        private StringBuilder buffer;

        /**
         * Ctor.
         * 
         * @param in The stream (stdout or stderr) to read from.
         */
        public StreamReaderThread(InputStream in) {
            r = new BufferedReader(new InputStreamReader(in));
            this.buffer = new StringBuilder();
        }

        /**
         * Returns the output read from the stream.
         * 
         * @return The stream's output, as a <code>String</code>.
         */
        public String getStreamOutput() {
            return buffer.toString();
        }

        /**
         * Continually reads from the output stream until this thread is
         * interrupted.
         */
        @Override
        public void run() {
            String line;
            try {
                while ((line = r.readLine()) != null) {
                    buffer.append(line).append('\n');
                }
            } catch (IOException ioe) {
                buffer.append("IOException occurred: " + ioe.getMessage());
            }
        }
    }

    /**
     * What exactly to mark as the error in the document, based on an error code
     * from JSHint.
     * 
     * @author D. Campione
     * 
     */
    private enum MarkStrategy {
        MARK_LINE, MARK_CUR_TOKEN, MARK_PREV_TOKEN, MARK_PREV_NON_WS_TOKEN, IGNORE, STOP_PARSING;
    }
}