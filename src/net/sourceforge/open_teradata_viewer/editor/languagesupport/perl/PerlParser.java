/*
 * Open Teradata Viewer ( editor language support perl )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.perl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Element;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.IOUtil;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.OutputCollector;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxDocument;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.AbstractParser;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.DefaultParseResult;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.IParseResult;

/**
 * Parses Perl code in an <tt>SyntaxTextArea</tt>.<p>
 * 
 * @author D. Campione
 * 
 */
public class PerlParser extends AbstractParser {

    private DefaultParseResult result;

    private boolean taintModeEnabled;
    private boolean warningsEnabled;

    /**
     * The user's requested value for <code>PERL5LIB</code> when parsing Perl
     * code, or <code>null</code> to use the default.
     */
    private String perl5LibOverride;

    /**
     * The environment to use when launching Perl to parse code, in the format
     * expected by <code>Runtime.exec()</code>. This may be <code>null</code>.
     */
    private String[] perlEnvironment;

    /**
     * The maximum amount of time to wait for Perl to finish compiling a source
     * file.
     */
    private static final int MAX_COMPILE_MILLIS = 10000;

    /** Ctor. */
    public PerlParser() {
        result = new DefaultParseResult(this);
    }

    /**
     * Creates the array of environment variables to use when running Perl to
     * syntax check code, based on the user's requested <code>PERL5LIB</code>.
     */
    private void createPerlEnvironment() {
        // Default to using the same environment as parent process
        perlEnvironment = null;

        // See if they specified a special value for PERL5LIB
        String perl5Lib = getPerl5LibOverride();
        if (perl5Lib != null) {
            String[] toAdd = { "PERL5LIB", perl5Lib };
            perlEnvironment = IOUtil.getEnvironmentSafely(toAdd);
        }
    }

    /**
     * Returns the value to use for <code>PERL5LIB</code> when parsing Perl
     * code.
     *
     * @return The value, or <code>null</code> to use the system default.
     * @see #setPerl5LibOverride(String)
     */
    public String getPerl5LibOverride() {
        return perl5LibOverride;
    }

    /**
     * Returns whether warnings are enabled when checking syntax.
     *
     * @return Whether warnings are enabled.
     * @see #setWarningsEnabled(boolean)
     */
    public boolean getWarningsEnabled() {
        return warningsEnabled;
    }

    /**
     * Returns whether taint mode is enabled when checking syntax.
     *
     * @return Whether taint mode is enabled.
     * @see #setTaintModeEnabled(boolean)
     */
    public boolean isTaintModeEnabled() {
        return taintModeEnabled;
    }

    /** {@inheritDoc} */
    @Override
    public IParseResult parse(SyntaxDocument doc, String style) {
        result.clearNotices();

        int lineCount = doc.getDefaultRootElement().getElementCount();
        result.setParsedLines(0, lineCount - 1);

        long start = System.currentTimeMillis();
        try {
            // Bail early if install location is misconfigured
            File dir = PerlLanguageSupport.getPerlInstallLocation();
            if (dir == null) {
                return result;
            }
            String exe = File.separatorChar == '\\' ? "bin/perl.exe"
                    : "bin/perl";
            File perl = new File(dir, exe);
            if (!perl.isFile()) {
                return result;
            }

            File tempFile = File.createTempFile("perlParser", ".tmp");
            BufferedOutputStream out = new BufferedOutputStream(
                    new FileOutputStream(tempFile));
            try {
                new DefaultEditorKit().write(out, doc, 0, doc.getLength());
            } catch (BadLocationException ble) {
                ExceptionDialog.hideException(ble);
                throw new IOException(ble.getMessage());
            }
            out.close();

            String opts = "-c";
            if (getWarningsEnabled()) {
                opts += "w";
            }
            if (isTaintModeEnabled()) {
                opts += "t";
            }

            String[] envp = perlEnvironment;
            String[] cmd = { perl.getAbsolutePath(), opts,
                    tempFile.getAbsolutePath() };
            Process p = Runtime.getRuntime().exec(cmd, envp);
            Element root = doc.getDefaultRootElement();
            OutputCollector stdout = new OutputCollector(p.getInputStream(),
                    false);
            Thread t = new Thread(stdout);
            t.start();
            PerlOutputCollector stderr = new PerlOutputCollector(
                    p.getErrorStream(), this, result, root);
            Thread t2 = new Thread(stderr);
            t2.start();
            try {
                t2.join(MAX_COMPILE_MILLIS);
                t.join(MAX_COMPILE_MILLIS);
                if (t.isAlive()) {
                    t.interrupt();
                } else {
                    p.waitFor();
                }
            } catch (InterruptedException ie) {
                ExceptionDialog.hideException(ie);
            }

            long time = System.currentTimeMillis() - start;
            result.setParseTime(time);
        } catch (IOException ioe) {
            result.setError(ioe);
            ExceptionDialog.hideException(ioe);
        }

        return result;
    }

    /**
     * Sets the value to use for <code>PERL5LIB</code> when parsing Perl code.
     *
     * @param override The value, or <code>null</code> to use the system
     *        default.
     * @see #getPerl5LibOverride()
     */
    public void setPerl5LibOverride(String override) {
        perl5LibOverride = override;
        createPerlEnvironment(); // Refresh our cached environment map
    }

    /**
     * Toggles whether taint mode is enabled when checking syntax.
     *
     * @param enabled Whether taint mode should be enabled.
     * @see #isTaintModeEnabled()
     */
    public void setTaintModeEnabled(boolean enabled) {
        taintModeEnabled = enabled;
    }

    /**
     * Toggles whether warnings are returned when checking syntax.
     *
     * @param enabled Whether warnings are enabled.
     * @see #getWarningsEnabled()
     */
    public void setWarningsEnabled(boolean enabled) {
        warningsEnabled = enabled;
    }
}