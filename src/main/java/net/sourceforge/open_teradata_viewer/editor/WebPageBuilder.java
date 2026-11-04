/*
 * Open Teradata Viewer ( editor )
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

package net.sourceforge.open_teradata_viewer.editor;

import java.awt.Color;
import java.awt.Font;

import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.Token;

import net.sourceforge.open_teradata_viewer.Main;
import net.sourceforge.open_teradata_viewer.util.StringUtil;
import net.sourceforge.open_teradata_viewer.util.UIUtil;

/**
 * Builds the web page that shows the text of a text area, or a part of it,
 * with the colors and the font styles of its syntax highlighting.
 *
 * @author D. Campione
 */
public final class WebPageBuilder {

    private WebPageBuilder() {
    }

    /**
     * Builds the web page of a range of the text of a text area. Only the
     * characters of the range are in the page, whatever the tokens they belong
     * to look like, and each line of the text has its own line in the page. The
     * text area must not be changed by another thread while the page is built:
     * it is read through its token lists, so this has to be called from the
     * Swing event thread.
     *
     * @param title The title of the page.
     * @param textArea The text area whose text is shown in the page.
     * @param start The offset of the first character of the range. It is
     *        brought into the text if it is not.
     * @param end The offset after the last character of the range. It is
     *        brought into the text if it is not, and after <code>start</code>.
     * @return The page, with the line separator of the platform between the
     *         lines of the markup.
     * @throws BadLocationException If the text area cannot tell the lines of
     *         the range.
     */
    public static String build(String title, RSyntaxTextArea textArea, int start, int end)
            throws BadLocationException {
        int length = textArea.getDocument().getLength();
        int from = Math.max(0, Math.min(start, length));
        int to = Math.max(from, Math.min(end, length));

        // The style of each type of token that is in the range, as a CSS rule
        String[] styles = new String[Token.DEFAULT_NUM_TOKEN_TYPES];
        StringBuilder rule = new StringBuilder();
        StringBuilder body = new StringBuilder();

        int lastLine = textArea.getLineOfOffset(to);
        for (int i = textArea.getLineOfOffset(from); i <= lastLine; i++) {
            Token token = textArea.getTokenListForLine(i);
            while (token != null && token.isPaintable()) {
                // The characters of the token that are in the range: all of
                // them, except at the ends of the range
                int tokenStart = token.getOffset();
                int partStart = Math.max(tokenStart, from);
                int partEnd = Math.min(tokenStart + token.length(), to);
                if (partStart < partEnd) {
                    int type = token.getType();
                    if (styles[type] == null) {
                        rule.setLength(0);
                        rule.append(".s").append(type).append(" {\n");
                        Font font = textArea.getFontForTokenType(type);
                        if (font.isBold()) {
                            rule.append("font-weight: bold;\n");
                        }
                        if (font.isItalic()) {
                            rule.append("font-style: italic;\n");
                        }
                        Color c = textArea.getForegroundForToken(token);
                        rule.append("color: ").append(UIUtil.getHTMLFormatForColor(c)).append(";\n");
                        rule.append("}");
                        styles[type] = rule.toString();
                    }
                    String lexeme = token.getLexeme();
                    body.append("<span class=\"s").append(type).append("\">");
                    body.append(StringUtil.escapeForHTML(
                            lexeme.substring(partStart - tokenStart, partEnd - tokenStart), "\n", true));
                    body.append("</span>");
                }
                token = token.getNextToken();
            }
            body.append('\n');
        }

        String newLine = System.lineSeparator();
        StringBuilder page = new StringBuilder();
        page.append(
                        "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">")
                .append(newLine);
        page.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">").append(newLine);
        page.append("<head>").append(newLine);
        page.append("<!-- Generated by ").append(Main.APPLICATION_NAME).append(" -->").append(newLine);
        page.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />").append(newLine);
        // The title is a file name: it can hold characters that are special in HTML
        page.append("<title>").append(StringUtil.escapeForHTML(title, null, true)).append("</title>")
                .append(newLine);

        page.append("<style type=\"text/css\">").append(newLine);
        for (String style : styles) {
            if (style != null) {
                page.append(style).append(newLine);
            }
        }
        page.append("</style>").append(newLine);

        page.append("</head>").append(newLine);
        page.append("<body>\n<pre>").append(newLine);
        page.append(body).append(newLine);
        page.append("</pre>\n</body>\n</html>").append(newLine);
        return page.toString();
    }
}
