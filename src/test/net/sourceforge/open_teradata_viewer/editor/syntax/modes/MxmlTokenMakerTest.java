/*
 * Open Teradata Viewer ( editor syntax modes )
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

package test.net.sourceforge.open_teradata_viewer.editor.syntax.modes;

import javax.swing.text.Segment;

import net.sourceforge.open_teradata_viewer.editor.syntax.IToken;
import net.sourceforge.open_teradata_viewer.editor.syntax.ITokenTypes;
import net.sourceforge.open_teradata_viewer.editor.syntax.modes.MxmlTokenMaker;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the {@link MxmlTokenMaker} class.
 *
 * @author D. Campione
 *
 */
public class MxmlTokenMakerTest {

    @Test
    public void testMxml_comment() {
        String[] commentLiterals = { "<!-- Hello world -->", };

        for (String code : commentLiterals) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            MxmlTokenMaker tm = new MxmlTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.MARKUP_COMMENT, token.getType());
        }
    }

    @Test
    public void testMxml_comment_URL() {
        String code = "<!-- Hello world http://www.google.com -->";
        Segment segment = new Segment(code.toCharArray(), 0, code.length());
        MxmlTokenMaker tm = new MxmlTokenMaker();
        IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);

        Assert.assertFalse(token.isHyperlink());
        Assert.assertTrue("Token is not type MARKUP_COMMENT: " + token,
                token.is(ITokenTypes.MARKUP_COMMENT, "<!-- Hello world "));
        token = token.getNextToken();
        Assert.assertTrue(token.isHyperlink());
        Assert.assertTrue(token.is(ITokenTypes.MARKUP_COMMENT,
                "http://www.google.com"));
        token = token.getNextToken();
        Assert.assertFalse(token.isHyperlink());
        Assert.assertTrue(token.is(ITokenTypes.MARKUP_COMMENT, " -->"));
    }

    @Test
    public void testMxml_doctype() {
        String[] doctypes = {
                "<!doctype html>",
                "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">",
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">", };

        for (String code : doctypes) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            MxmlTokenMaker tm = new MxmlTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.MARKUP_DTD, token.getType());
        }
    }

    @Test
    public void testMxml_entityReferences() {
        String[] entityReferences = { "&nbsp;", "&lt;", "&gt;", "&#4012", };

        for (String code : entityReferences) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            MxmlTokenMaker tm = new MxmlTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.MARKUP_ENTITY_REFERENCE,
                    token.getType());
        }
    }

    @Test
    public void testMxml_happyPath_tagWithAttributes() {
        String code = "<body onload=\"doSomething()\" data-extra='true'>";
        Segment segment = new Segment(code.toCharArray(), 0, code.length());
        MxmlTokenMaker tm = new MxmlTokenMaker();
        IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);

        Assert.assertTrue(token.isSingleChar(ITokenTypes.MARKUP_TAG_DELIMITER,
                '<'));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.MARKUP_TAG_NAME, "body"));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.WHITESPACE, " "));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.MARKUP_TAG_ATTRIBUTE, "onload"));
        token = token.getNextToken();
        Assert.assertTrue(token.isSingleChar(ITokenTypes.OPERATOR, '='));
        token = token.getNextToken();
        Assert.assertTrue("Unexpected token: " + token, token.is(
                ITokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE, "\"doSomething()\""));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.WHITESPACE, " "));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.MARKUP_TAG_ATTRIBUTE,
                "data-extra"));
        token = token.getNextToken();
        Assert.assertTrue(token.isSingleChar(ITokenTypes.OPERATOR, '='));
        token = token.getNextToken();
        Assert.assertTrue(token.is(ITokenTypes.MARKUP_TAG_ATTRIBUTE_VALUE,
                "'true'"));
        token = token.getNextToken();
        Assert.assertTrue(token.isSingleChar(ITokenTypes.MARKUP_TAG_DELIMITER,
                '>'));
    }

    @Test
    public void testMxml_processingInstructions() {
        String[] doctypes = { "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>",
                "<?xml version='1.0' encoding='UTF-8' ?>",
                "<?xml-stylesheet type=\"text/css\" href=\"style.css\"?>", };

        for (String code : doctypes) {
            Segment segment = new Segment(code.toCharArray(), 0, code.length());
            MxmlTokenMaker tm = new MxmlTokenMaker();
            IToken token = tm.getTokenList(segment, ITokenTypes.NULL, 0);
            Assert.assertEquals(ITokenTypes.MARKUP_PROCESSING_INSTRUCTION,
                    token.getType());
        }
    }
}