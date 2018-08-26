/*
 * Open Teradata Viewer ( editor language support sh )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.sh;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.FunctionCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.OutputCollector;

/**
 * Completion for Unix shell "functions" (command line utilities).
 *
 * @author D. Campione
 * 
 */
public class ShellFunctionCompletion extends FunctionCompletion {

    /** Ctor. */
    public ShellFunctionCompletion(ICompletionProvider provider, String name,
            String returnType) {
        super(provider, name, returnType);
    }

    /** {@inheritDoc} */
    @Override
    public String getSummary() {
        String summary = null;
        if (ShellCompletionProvider.getUseLocalManPages()) {
            summary = getSummaryFromManPage();
        }
        //else { // Don't use else - fallback for if man isn't found
        if (summary == null) {
            summary = super.getSummary();
        }

        return summary;
    }

    /**
     * Gets a summary of this function from the local system's man pages.
     *
     * @return The summary.
     */
    private String getSummaryFromManPage() {
        Process p = null;

        String[] cmd = { "/usr/bin/man", getName() };
        try {
            p = Runtime.getRuntime().exec(cmd);
        } catch (IOException ioe) {
            ExceptionDialog.hideException(ioe);
            return null;
        }

        OutputCollector stdout = new OutputCollector(p.getInputStream());
        Thread t = new Thread(stdout);
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
            output = stdout.getOutput();
            if (output != null && output.length() > 0) {
                output = manToHtml(output);
            }
        }

        return output == null ? null : output.toString();
    }

    private static final StringBuffer manToHtml(CharSequence text) {
        Pattern p = Pattern.compile("(?:_\\010.)+|(?:(.)\\010\\1)+");//"(?:.\\010.)+");
        Matcher m = p.matcher(text);
        StringBuffer sb = new StringBuffer("<html><pre>");
        while ((m.find())) {
            System.out.println("... found '" + m.group() + "'");
            String group = m.group();
            if (group.startsWith("_")) {
                sb.append("<u>");
                String replacement = group.replaceAll("_\\010", "");
                replacement = quoteReplacement(replacement);
                m.appendReplacement(sb, replacement);
                System.out.println("--- '" + replacement);
                sb.append("</u>");
            } else {
                String replacement = group.replaceAll(".\\010.", "");
                replacement = quoteReplacement(replacement);
                m.appendReplacement(sb, replacement);
                System.out.println("--- '" + replacement);
            }
        }
        m.appendTail(sb);
        return sb;
    }

    private static String quoteReplacement(String text) {
        if (text.indexOf('$') > -1 || text.indexOf('\\') > -1) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < text.length(); i++) {
                char ch = text.charAt(i);
                if (ch == '$' || ch == '\\') {
                    sb.append('\\');
                }
                sb.append(ch);
            }
            text = sb.toString();
        }
        return text;
    }
}