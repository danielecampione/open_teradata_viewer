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

import java.awt.Font;
import java.io.File;
import java.io.IOException;

import javax.swing.UIManager;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.FunctionCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.OutputCollector;

/**
 * Completion for Perl functions.
 *
 * @author D. Campione
 * 
 */
public class PerlFunctionCompletion extends FunctionCompletion {

    /** Ctor. */
    public PerlFunctionCompletion(ICompletionProvider provider, String name,
            String returnType) {
        super(provider, name, returnType);
    }

    /** {@inheritDoc} */
    @Override
    public String getSummary() {
        String summary = null;
        File installLoc = PerlLanguageSupport.getPerlInstallLocation();
        if (installLoc != null && PerlLanguageSupport.getUseSystemPerldoc()) {
            summary = getSummaryFromPerldoc(installLoc);
        }
        //else { // Don't use else - fallback for if perldoc isn't found
        if (summary == null) {
            summary = super.getSummary();
        }

        return summary;
    }

    /**
     * Gets a summary of this function from perldoc.
     *
     * @param installLoc The Perl install location.
     * @return The summary.
     */
    private String getSummaryFromPerldoc(File installLoc) {
        Process p = null;

        String fileName = "bin/perldoc";
        if (File.separatorChar == '\\') {
            fileName += ".bat";
        }
        File perldoc = new File(installLoc, fileName);
        if (!perldoc.isFile()) {
            return null;
        }

        String[] cmd = { perldoc.getAbsolutePath(), "-f", getName() };
        try {
            p = Runtime.getRuntime().exec(cmd);
        } catch (IOException ioe) {
            ExceptionDialog.hideException(ioe);
            return null;
        }

        OutputCollector oc = new OutputCollector(p.getInputStream());
        Thread t = new Thread(oc);
        t.start();
        int rc = 0;
        try {
            rc = p.waitFor();
            t.join();
        } catch (InterruptedException ie) {
            ExceptionDialog.hideException(ie);
        }

        CharSequence output = null;
        if (rc == 0) {
            output = oc.getOutput();
            if (output != null && output.length() > 0) {
                output = perldocToHtml(output);
            }
        }

        return output == null ? null : output.toString();
    }

    private static final StringBuilder perldocToHtml(CharSequence text) {
        StringBuilder sb = null;

        Font font = UIManager.getFont("Label.font");
        // Even Nimbus sets Label.font but just to be safe..
        if (font != null) {
            sb = new StringBuilder("<html><style>pre { font-family: ").append(
                    font.getFamily()).append("; }</style><pre>");
        } else { // Just use monospaced font
            sb = new StringBuilder("<html><pre>");
        }

        sb.append(text);
        return sb;
    }
}