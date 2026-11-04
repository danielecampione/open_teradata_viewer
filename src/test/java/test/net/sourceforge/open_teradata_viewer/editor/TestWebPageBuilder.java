/*
 * Open Teradata Viewer ( test editor )
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

package test.net.sourceforge.open_teradata_viewer.editor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.BadLocationException;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.junit.jupiter.api.Test;

import net.sourceforge.open_teradata_viewer.editor.WebPageBuilder;

/**
 *
 *
 * @author D. Campione
 *
 */
public class TestWebPageBuilder {

    private static final String SQL = "-- a <comment> & more\nSELECT 'caf\u00e9' AS \"q\", 12.5\n\tFROM t WHERE x >= 3;\n";

    private static RSyntaxTextArea sqlArea(String text) {
        RSyntaxTextArea textArea = new RSyntaxTextArea();
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_SQL);
        textArea.setText(text);
        return textArea;
    }

    private static String build(RSyntaxTextArea textArea, int start, int end) throws BadLocationException {
        return WebPageBuilder.build("page", textArea, start, end);
    }

    /**
     * The text that a page shows: the lines of its body, without the markup.
     * The tabs are shown as three spaces.
     */
    private static String shownText(String page) {
        String newLine = System.lineSeparator();
        int start = page.indexOf("<pre>") + "<pre>".length() + newLine.length();
        int end = page.lastIndexOf("</pre>") - newLine.length();
        return page.substring(start, end).replaceAll("<span class=\"s\\d+\">", "").replace("</span>", "")
                .replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&");
    }

    private static Set<String> find(String page, String regex) {
        Set<String> found = new HashSet<>();
        Matcher matcher = Pattern.compile(regex, Pattern.MULTILINE).matcher(page);
        while (matcher.find()) {
            found.add(matcher.group(1));
        }
        return found;
    }

    @Test
    public void test_build_wholeText() throws BadLocationException {
        RSyntaxTextArea textArea = sqlArea(SQL);
        String shown = shownText(build(textArea, 0, textArea.getDocument().getLength()));
        // Each line of the text is a line of the page
        assertEquals(SQL.replace("\t", "   ") + "\n", shown);
    }

    @Test
    public void test_build_range() throws BadLocationException {
        RSyntaxTextArea textArea = sqlArea(SQL);
        // From the middle of a keyword to the middle of a string, over two lines
        int start = SQL.indexOf("comment") + 3;
        int end = SQL.indexOf("caf\u00e9") + 2;
        String selected = SQL.substring(start, end);
        assertEquals(selected + "\n", shownText(build(textArea, start, end)));
    }

    @Test
    public void test_build_rangeInsideOneToken() throws BadLocationException {
        RSyntaxTextArea textArea = sqlArea("SELECT 1;");
        assertEquals("LEC\n", shownText(build(textArea, 2, 5)));
    }

    @Test
    public void test_build_rangeInALaterLine() throws BadLocationException {
        RSyntaxTextArea textArea = sqlArea("first\nsecond\nthird\nfourth");
        // Only the lines of the range are in the page, not the ones before or after
        assertEquals("con\n", shownText(build(textArea, 8, 11)));
        assertEquals("ond\nthi\n", shownText(build(textArea, 9, 16)));
    }

    @Test
    public void test_build_rangeEndingAfterALineBreak() throws BadLocationException {
        RSyntaxTextArea textArea = sqlArea("first\nsecond\nthird");
        // As the text "first\n" has two lines, the second one being empty
        assertEquals("first\n\n", shownText(build(textArea, 0, 6)));
    }

    @Test
    public void test_build_emptyRange() throws BadLocationException {
        RSyntaxTextArea textArea = sqlArea(SQL);
        assertEquals("\n", shownText(build(textArea, 7, 7)));
        assertEquals("\n", shownText(build(sqlArea(""), 0, 0)));
    }

    @Test
    public void test_build_rangeOutsideTheTextIsBroughtInside() throws BadLocationException {
        RSyntaxTextArea textArea = sqlArea("SELECT 1;");
        String whole = build(textArea, 0, 9);
        assertEquals(whole, build(textArea, -4, 1000));
        // Reversed ends give an empty range
        assertEquals("\n", shownText(build(textArea, 6, 2)));
    }

    @Test
    public void test_build_onlyTheStylesOfTheRange() throws BadLocationException {
        RSyntaxTextArea textArea = sqlArea("SELECT 12;");
        String wholePage = build(textArea, 0, 10);
        String numberPage = build(textArea, 7, 9);
        assertEquals("12\n", shownText(numberPage));
        Set<String> used = find(numberPage, "class=\"(s\\d+)\"");
        assertEquals(1, used.size());
        // Each class used has its rule, and nothing else has one
        assertEquals(used, find(numberPage, "^\\.(s\\d+) \\{"));
        assertTrue(find(wholePage, "^\\.(s\\d+) \\{").size() > used.size());
    }

    @Test
    public void test_build_everyClassUsedHasItsRule() throws BadLocationException {
        RSyntaxTextArea textArea = sqlArea(SQL);
        String page = build(textArea, 0, textArea.getDocument().getLength());
        assertEquals(find(page, "class=\"(s\\d+)\""), find(page, "^\\.(s\\d+) \\{"));
    }

    @Test
    public void test_build_markupOfTheTextIsEscaped() throws BadLocationException {
        RSyntaxTextArea textArea = sqlArea("SELECT '<b>&</b>';");
        String page = build(textArea, 0, textArea.getDocument().getLength());
        assertTrue(page.contains("&lt;b&gt;&amp;&lt;/b&gt;"));
        assertFalse(page.contains("<b>"));
    }

    @Test
    public void test_build_titleIsEscaped() throws BadLocationException {
        RSyntaxTextArea textArea = sqlArea("SELECT 1;");
        assertTrue(WebPageBuilder.build("C:\\R&D\\a<b>.html", textArea, 0, 9)
                .contains("<title>C:\\R&amp;D\\a&lt;b&gt;.html</title>"));
        assertTrue(WebPageBuilder.build("export.html", textArea, 0, 9).contains("<title>export.html</title>"));
    }
}
