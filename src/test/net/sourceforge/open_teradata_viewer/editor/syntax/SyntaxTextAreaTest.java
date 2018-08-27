/*
 * Open Teradata Viewer ( editor syntax )
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

package test.net.sourceforge.open_teradata_viewer.editor.syntax;

import java.awt.Color;

import net.sourceforge.open_teradata_viewer.editor.syntax.ISyntaxConstants;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.net.sourceforge.open_teradata_viewer.editor.SwingRunner;

/**
 * Unit tests for the {@link SyntaxTextArea} class.
 *
 * @author D. Campione
 *
 */
@RunWith(SwingRunner.class)
public class SyntaxTextAreaTest {

    @Test
    public void testGetAnimateBracketMatching() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertTrue(textArea.getAnimateBracketMatching());
        textArea.setAnimateBracketMatching(false);
        Assert.assertFalse(textArea.getAnimateBracketMatching());
    }

    @Test
    public void testGetAntiAliasingEnabled() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertTrue(textArea.getAntiAliasingEnabled());
        textArea.setAntiAliasingEnabled(false);
        Assert.assertFalse(textArea.getAntiAliasingEnabled());
    }

    @Test
    public void testGetCloseCurlyBraces() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertTrue(textArea.getCloseCurlyBraces());
        textArea.setCloseCurlyBraces(false);
        Assert.assertFalse(textArea.getCloseCurlyBraces());
    }

    @Test
    public void testGetCloseMarkupTags() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertTrue(textArea.getCloseMarkupTags());
        textArea.setCloseMarkupTags(false);
        Assert.assertFalse(textArea.getCloseMarkupTags());
    }

    @Test
    public void testGetEOLMarkersVisible() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertFalse(textArea.getEOLMarkersVisible());
        textArea.setEOLMarkersVisible(true);
        Assert.assertTrue(textArea.getEOLMarkersVisible());
    }

    @Test
    public void testGetFractionalFontMetricsEnabled() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertFalse(textArea.getFractionalFontMetricsEnabled());
        textArea.setFractionalFontMetricsEnabled(true);
        Assert.assertTrue(textArea.getFractionalFontMetricsEnabled());
    }

    @Test
    public void testGetHighlightSecondaryLanguages() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertTrue(textArea.getHighlightSecondaryLanguages());
        textArea.setHighlightSecondaryLanguages(false);
        Assert.assertFalse(textArea.getHighlightSecondaryLanguages());
    }

    @Test
    public void testGetHyperlinkForeground() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        textArea.setHyperlinkForeground(Color.pink);
        Assert.assertEquals(Color.pink, textArea.getHyperlinkForeground());
    }

    @Test
    public void testGetHyperlinksEnabled() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertTrue(textArea.getHyperlinksEnabled());
        textArea.setHyperlinksEnabled(false);
        Assert.assertFalse(textArea.getHyperlinksEnabled());
    }

    @Test
    public void testGetMarkOccurrences() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertFalse(textArea.getMarkOccurrences());
        textArea.setMarkOccurrences(true);
        Assert.assertTrue(textArea.getMarkOccurrences());
    }

    @Test
    public void testGetMarkOccurrencesColor() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        textArea.setMarkOccurrencesColor(Color.pink);
        Assert.assertEquals(Color.pink, textArea.getMarkOccurrencesColor());
    }

    @Test
    public void testGetMatchedBracketBGColor() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        textArea.setMatchedBracketBGColor(Color.pink);
        Assert.assertEquals(Color.pink, textArea.getMatchedBracketBGColor());
    }

    @Test
    public void testGetMatchedBracketBorderColor() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        textArea.setMatchedBracketBorderColor(Color.pink);
        Assert.assertEquals(Color.pink, textArea.getMatchedBracketBorderColor());
    }

    @Test
    public void testGetPaintMatchedBracketPair() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertFalse(textArea.getPaintMatchedBracketPair());
        textArea.setPaintMatchedBracketPair(true);
        Assert.assertTrue(textArea.getPaintMatchedBracketPair());
    }

    @Test
    public void testGetPaintTabLines() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertFalse(textArea.getPaintTabLines());
        textArea.setPaintTabLines(true);
        Assert.assertTrue(textArea.getPaintTabLines());
    }

    @Test
    public void testGetSyntaxEditingStyle() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertEquals(ISyntaxConstants.SYNTAX_STYLE_NONE,
                textArea.getSyntaxEditingStyle());
        textArea.setSyntaxEditingStyle(ISyntaxConstants.SYNTAX_STYLE_JAVA);
        Assert.assertEquals(ISyntaxConstants.SYNTAX_STYLE_JAVA,
                textArea.getSyntaxEditingStyle());
    }

    @Test
    public void testGetTabLineColor() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        textArea.setTabLineColor(Color.blue);
        Assert.assertEquals(Color.blue, textArea.getTabLineColor());
    }

    @Test
    public void testGetPaintMarkOccurrencesBorder() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertFalse(textArea.getPaintMarkOccurrencesBorder());
        textArea.setPaintMarkOccurrencesBorder(true);
        Assert.assertTrue(textArea.getPaintMarkOccurrencesBorder());
    }

    @Test
    public void testGetParserDelay() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        textArea.setParserDelay(6789);
        Assert.assertEquals(6789, textArea.getParserDelay());
    }

    @Test
    public void testGetUseFocusableTips() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertTrue(textArea.getUseFocusableTips());
        textArea.setUseFocusableTips(false);
        Assert.assertFalse(textArea.getUseFocusableTips());
    }

    @Test
    public void testGetUseSelectedTextColor() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertFalse(textArea.getUseSelectedTextColor());
        textArea.setUseSelectedTextColor(true);
        Assert.assertTrue(textArea.getUseSelectedTextColor());
    }

    @Test
    public void testIsAutoIndentEnabled() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertTrue(textArea.isAutoIndentEnabled());
        textArea.setAutoIndentEnabled(false);
        Assert.assertFalse(textArea.isAutoIndentEnabled());
    }

    @Test
    public void testIsBracketMatchingEnabled() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertTrue(textArea.isBracketMatchingEnabled());
        textArea.setBracketMatchingEnabled(false);
        Assert.assertFalse(textArea.isBracketMatchingEnabled());
    }

    @Test
    public void testIsClearWhitespaceLinesEnabled() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertTrue(textArea.isClearWhitespaceLinesEnabled());
        textArea.setClearWhitespaceLinesEnabled(false);
        Assert.assertFalse(textArea.isClearWhitespaceLinesEnabled());
    }

    @Test
    public void testIsCodeFoldingEnabled() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertFalse(textArea.isCodeFoldingEnabled());
        textArea.setCodeFoldingEnabled(true);
        Assert.assertTrue(textArea.isCodeFoldingEnabled());
    }

    @Test
    public void testIsWhitespaceVisible() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertFalse(textArea.isWhitespaceVisible());
        textArea.setWhitespaceVisible(true);
        Assert.assertTrue(textArea.isWhitespaceVisible());
    }

    @Test
    public void testSetAnimateBracketMatching() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertTrue(textArea.getAnimateBracketMatching());
        textArea.setAnimateBracketMatching(false);
        Assert.assertFalse(textArea.getAnimateBracketMatching());
    }

    @Test
    public void testSetAntiAliasingEnabled() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertTrue(textArea.getAntiAliasingEnabled());
        textArea.setAntiAliasingEnabled(false);
        Assert.assertFalse(textArea.getAntiAliasingEnabled());
    }

    @Test
    public void testSetAutoIndentEnabled() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertTrue(textArea.isAutoIndentEnabled());
        textArea.setAutoIndentEnabled(false);
        Assert.assertFalse(textArea.isAutoIndentEnabled());
    }

    @Test
    public void testSetBracketMatchingEnabled() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertTrue(textArea.isBracketMatchingEnabled());
        textArea.setBracketMatchingEnabled(false);
        Assert.assertFalse(textArea.isBracketMatchingEnabled());
    }

    @Test
    public void testSetClearWhitespaceLinesEnabled() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertTrue(textArea.isClearWhitespaceLinesEnabled());
        textArea.setClearWhitespaceLinesEnabled(false);
        Assert.assertFalse(textArea.isClearWhitespaceLinesEnabled());
    }

    @Test
    public void testSetCloseCurlyBraces() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertTrue(textArea.getCloseCurlyBraces());
        textArea.setCloseCurlyBraces(false);
        Assert.assertFalse(textArea.getCloseCurlyBraces());
    }

    @Test
    public void testSetCloseMarkupTags() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertTrue(textArea.getCloseMarkupTags());
        textArea.setCloseMarkupTags(false);
        Assert.assertFalse(textArea.getCloseMarkupTags());
    }

    @Test
    public void testSetCodeFoldingEnabled() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertFalse(textArea.isCodeFoldingEnabled());
        textArea.setCodeFoldingEnabled(true);
        Assert.assertTrue(textArea.isCodeFoldingEnabled());
    }

    @Test
    public void testSetEOLMarkersVisible() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertFalse(textArea.getEOLMarkersVisible());
        textArea.setEOLMarkersVisible(true);
        Assert.assertTrue(textArea.getEOLMarkersVisible());
    }

    @Test
    public void testSetFractionalFontMetricsEnabled() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertFalse(textArea.getFractionalFontMetricsEnabled());
        textArea.setFractionalFontMetricsEnabled(true);
        Assert.assertTrue(textArea.getFractionalFontMetricsEnabled());
    }

    @Test
    public void testSetHighlightSecondaryLanguages() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertTrue(textArea.getHighlightSecondaryLanguages());
        textArea.setHighlightSecondaryLanguages(false);
        Assert.assertFalse(textArea.getHighlightSecondaryLanguages());
    }

    @Test
    public void testSetHyperlinkForeground() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        textArea.setHyperlinkForeground(Color.pink);
        Assert.assertEquals(Color.pink, textArea.getHyperlinkForeground());
    }

    @Test
    public void testSetHyperlinksEnabled() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertTrue(textArea.getHyperlinksEnabled());
        textArea.setHyperlinksEnabled(false);
        Assert.assertFalse(textArea.getHyperlinksEnabled());
    }

    @Test
    public void testSetMarkOccurrences() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertFalse(textArea.getMarkOccurrences());
        textArea.setMarkOccurrences(true);
        Assert.assertTrue(textArea.getMarkOccurrences());
    }

    @Test
    public void testSetMarkOccurrencesColor() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        textArea.setMarkOccurrencesColor(Color.pink);
        Assert.assertEquals(Color.pink, textArea.getMarkOccurrencesColor());
    }

    @Test
    public void testSetMatchedBracketBGColor() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        textArea.setMatchedBracketBGColor(Color.pink);
        Assert.assertEquals(Color.pink, textArea.getMatchedBracketBGColor());
    }

    @Test
    public void testSetMatchedBracketBorderColor() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        textArea.setMatchedBracketBorderColor(Color.pink);
        Assert.assertEquals(Color.pink, textArea.getMatchedBracketBorderColor());
    }

    @Test
    public void testSetPaintMarkOccurrencesBorder() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertFalse(textArea.getPaintMarkOccurrencesBorder());
        textArea.setPaintMarkOccurrencesBorder(true);
        Assert.assertTrue(textArea.getPaintMarkOccurrencesBorder());
    }

    @Test
    public void testSetPaintMatchedBracketPair() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertFalse(textArea.getPaintMatchedBracketPair());
        textArea.setPaintMatchedBracketPair(true);
        Assert.assertTrue(textArea.getPaintMatchedBracketPair());
    }

    @Test
    public void testSetPaintTabLines() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertFalse(textArea.getPaintTabLines());
        textArea.setPaintTabLines(true);
        Assert.assertTrue(textArea.getPaintTabLines());
    }

    @Test
    public void testSetParserDelay() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        textArea.setParserDelay(6789);
        Assert.assertEquals(6789, textArea.getParserDelay());
    }

    @Test
    public void testSetSyntaxEditingStyle() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertEquals(ISyntaxConstants.SYNTAX_STYLE_NONE,
                textArea.getSyntaxEditingStyle());
        textArea.setSyntaxEditingStyle(ISyntaxConstants.SYNTAX_STYLE_JAVA);
        Assert.assertEquals(ISyntaxConstants.SYNTAX_STYLE_JAVA,
                textArea.getSyntaxEditingStyle());
    }

    @Test
    public void testSetTabLineColor() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        textArea.setTabLineColor(Color.blue);
        Assert.assertEquals(Color.blue, textArea.getTabLineColor());
    }

    @Test
    public void testSetUseFocusableTips() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertTrue(textArea.getUseFocusableTips());
        textArea.setUseFocusableTips(false);
        Assert.assertFalse(textArea.getUseFocusableTips());
    }

    @Test
    public void testSetUseSelectedTextColor() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertFalse(textArea.getUseSelectedTextColor());
        textArea.setUseSelectedTextColor(true);
        Assert.assertTrue(textArea.getUseSelectedTextColor());
    }

    @Test
    public void testSetWhitespaceVisible() {
        SyntaxTextArea textArea = new SyntaxTextArea();
        Assert.assertFalse(textArea.isWhitespaceVisible());
        textArea.setWhitespaceVisible(true);
        Assert.assertTrue(textArea.isWhitespaceVisible());
    }
}