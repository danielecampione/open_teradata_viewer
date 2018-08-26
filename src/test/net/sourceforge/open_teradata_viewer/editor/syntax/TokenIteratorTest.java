/*
 * Open Teradata Viewer ( editor )
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

package test.net.sourceforge.open_teradata_viewer.editor.syntax;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.Element;

import net.sourceforge.open_teradata_viewer.editor.syntax.ISyntaxConstants;
import net.sourceforge.open_teradata_viewer.editor.syntax.IToken;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxDocument;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextAreaEditorKit;
import net.sourceforge.open_teradata_viewer.editor.syntax.TokenImpl;

import org.junit.Test;

/**
 * Unit tests for the iterator returned from a {@link SyntaxDocument}.
 *
 * @author D. Campione
 * 
 */
public class TokenIteratorTest {

    private static final SyntaxTextAreaEditorKit kit = new SyntaxTextAreaEditorKit();

    /**
     * Verifies that using an {@link SyntaxDocument}'s iterator returns the same
     * set of tokens as manually getting the token list for each line.
     */
    @Test
    public void testBasicIteration() throws Exception {
        SyntaxDocument doc = null;

        // A well-formed Java document
        doc = loadResource(
                "/res/testfiles/editor/syntax/TokenIteratorTest_JavaBasic.txt",
                ISyntaxConstants.SYNTAX_STYLE_JAVA);
        assertIteratorMatchesList(doc);

        // An unterminated Javadoc comment
        doc = loadResource(
                "/res/testfiles/editor/syntax/TokenIteratorTest_UnterminatedJavadoc.txt",
                ISyntaxConstants.SYNTAX_STYLE_JAVA);
        assertIteratorMatchesList(doc);

        // A single line
        doc.replace(0, doc.getLength(), "one two three", null);
        assertIteratorMatchesList(doc);

        // A single-line unterminated MLC
        doc.replace(0, doc.getLength(), "/* Unterminated MLC", null);
        assertIteratorMatchesList(doc);
    }

    /** Tests empty documents, documents with lots of blank lines, etc... */
    @Test
    public void testEmptyLines() throws Exception {
        SyntaxDocument doc = new SyntaxDocument(
                ISyntaxConstants.SYNTAX_STYLE_JAVA);

        // An empty document
        doc.remove(0, doc.getLength());
        assertIteratorMatchesList(doc);

        // A document with nothing but empty lines
        doc.insertString(0, "\n\n\n\n", null);
        assertIteratorMatchesList(doc);

        // A document with nothing lots of empty lines before text
        doc.insertString(0, "\n\n\n\nfor if while\n\n\n\n", null);
        assertIteratorMatchesList(doc);
    }

    /**
     * Loads a text resource from the classpath into an instance of {@link
     * SyntaxDocument}.
     *
     * @param res The resource.
     * @param syntax The syntax style to load with.
     * @return The document.
     * @throws Exception If anything goes wrong.
     */
    private SyntaxDocument loadResource(String res, String syntax)
            throws Exception {
        SyntaxDocument doc = new SyntaxDocument(syntax);
        BufferedReader r = new BufferedReader(new InputStreamReader(getClass()
                .getResourceAsStream(res)));
        kit.read(r, doc, 0);
        r.close();
        return doc;
    }

    /**
     * Compares the document's iterator's returned tokens against the expected
     * token list for the document. This method will cause a calling test to
     * fail if the iterator doesn't return the current token list.
     *
     * @param doc The document.
     */
    private static final void assertIteratorMatchesList(SyntaxDocument doc) {
        List<IToken> expected = getTokens(doc);
        int index = 0;
        for (IToken t : doc) {
            assertEquals(expected.get(index), t);
            index++;
        }
        assertEquals(expected.size(), index);
    }

    /**
     * Returns the set of expected paintable tokens from a document.
     *
     * @param doc The document.
     * @return The list of tokens, in the order in which they appear.
     */
    private static final List<IToken> getTokens(SyntaxDocument doc) {
        Element root = doc.getDefaultRootElement();
        int lineCount = root.getElementCount();
        List<IToken> list = new ArrayList<IToken>();

        for (int i = 0; i < lineCount; i++) {
            IToken t = doc.getTokenListForLine(i);
            while (t != null && t.isPaintable()) {
                list.add(new TokenImpl(t)); // Copy since ITokens are pooled
                t = t.getNextToken();
            }
        }

        return list;
    }
}