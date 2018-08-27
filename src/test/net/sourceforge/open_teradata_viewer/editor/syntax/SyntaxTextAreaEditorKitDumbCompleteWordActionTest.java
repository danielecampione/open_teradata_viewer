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

import java.awt.event.ActionEvent;

import javax.swing.ActionMap;

import net.sourceforge.open_teradata_viewer.editor.TextArea;
import net.sourceforge.open_teradata_viewer.editor.TextAreaEditorKit;
import net.sourceforge.open_teradata_viewer.editor.syntax.ISyntaxConstants;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxDocument;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextAreaEditorKit.DumbCompleteWordAction;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import test.net.sourceforge.open_teradata_viewer.editor.SwingRunner;

/**
 * Unit tests for the {@link DumbCompleteWordAction} class.
 *
 * @author D. Campione
 *
 */
@RunWith(SwingRunner.class)
public class SyntaxTextAreaEditorKitDumbCompleteWordActionTest {

    /**
     * Returns a fake action event for test purposes.
     *
     * @param textArea The source text area.
     * @return The fake action event.
     */
    private static final ActionEvent createActionEvent(TextArea textArea) {
        return new ActionEvent(textArea, 0,
                TextAreaEditorKit.taDumbCompleteWordAction);
    }

    /**
     * Returns the "dumb complete word action" to test associated with a text
     * area.
     *
     * @param textArea The text area.
     * @return The associated action.
     */
    private static final DumbCompleteWordAction getDumbCompleteWordAction(
            SyntaxTextArea textArea) {
        ActionMap am = textArea.getActionMap();
        return (DumbCompleteWordAction) am
                .get(TextAreaEditorKit.taDumbCompleteWordAction);
    }

    @Test
    public void testActionPerformed_manyLinesInBetween() throws Exception {
        DumbCompleteWordAction action = new DumbCompleteWordAction();
        SyntaxDocument doc = new SyntaxDocument(
                ISyntaxConstants.SYNTAX_STYLE_JAVA);
        SyntaxTextArea textArea = new SyntaxTextArea(doc);

        textArea.setText("aaron arthur aardvark\nfoo bar\n// bad code\namazing\n   a");
        action.actionPerformed(createActionEvent(textArea));
        Assert.assertEquals(
                "aaron arthur aardvark\nfoo bar\n// bad code\namazing\n   amazing",
                textArea.getText());
        action.actionPerformed(createActionEvent(textArea));
        Assert.assertEquals(
                "aaron arthur aardvark\nfoo bar\n// bad code\namazing\n   aardvark",
                textArea.getText());
        action.actionPerformed(createActionEvent(textArea));
        Assert.assertEquals(
                "aaron arthur aardvark\nfoo bar\n// bad code\namazing\n   arthur",
                textArea.getText());
        action.actionPerformed(createActionEvent(textArea));
        Assert.assertEquals(
                "aaron arthur aardvark\nfoo bar\n// bad code\namazing\n   aaron",
                textArea.getText());
        action.actionPerformed(createActionEvent(textArea));
        Assert.assertEquals(
                "aaron arthur aardvark\nfoo bar\n// bad code\namazing\n   aaron",
                textArea.getText());
    }

    @Test
    public void testActionPerformed_dollarSignImportant() throws Exception {
        SyntaxDocument doc = new SyntaxDocument(
                ISyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        SyntaxTextArea textArea = new SyntaxTextArea(doc);
        DumbCompleteWordAction action = getDumbCompleteWordAction(textArea);

        textArea.setText("$routeProvider routeSkip $ro");
        action.actionPerformed(createActionEvent(textArea));
        String actual = textArea.getText();
        Assert.assertEquals("$routeProvider routeSkip $routeProvider", actual);
    }

    @Test
    public void testActionPerformed_underscoresImportant() throws Exception {
        SyntaxDocument doc = new SyntaxDocument(
                ISyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        SyntaxTextArea textArea = new SyntaxTextArea(doc);
        DumbCompleteWordAction action = getDumbCompleteWordAction(textArea);

        textArea.setText("__foo\n__bar   __bas\n_");

        action.actionPerformed(createActionEvent(textArea));
        String actual = textArea.getText();
        Assert.assertEquals("__foo\n__bar   __bas\n__bas", actual);

        action.actionPerformed(createActionEvent(textArea));
        actual = textArea.getText();
        Assert.assertEquals("__foo\n__bar   __bas\n__bar", actual);

        action.actionPerformed(createActionEvent(textArea));
        actual = textArea.getText();
        Assert.assertEquals("__foo\n__bar   __bas\n__foo", actual);

        // No change when run again
        action.actionPerformed(createActionEvent(textArea));
        actual = textArea.getText();
        Assert.assertEquals("__foo\n__bar   __bas\n__foo", actual);
    }

    @Test
    public void testGetWordStart() throws Exception {
        DumbCompleteWordAction action = new DumbCompleteWordAction();
        SyntaxDocument doc = new SyntaxDocument(
                ISyntaxConstants.SYNTAX_STYLE_JAVA);
        SyntaxTextArea textArea = new SyntaxTextArea(doc);

        textArea.setText("for if  while");
        Assert.assertEquals(0, action.getWordStart(textArea, 0));
        Assert.assertEquals(0, action.getWordStart(textArea, 1));
        Assert.assertEquals(0, action.getWordStart(textArea, 2));
        Assert.assertEquals(3, action.getWordStart(textArea, 3));
        Assert.assertEquals(4, action.getWordStart(textArea, 4));
        Assert.assertEquals(4, action.getWordStart(textArea, 5));
        Assert.assertEquals(6, action.getWordStart(textArea, 6));
        Assert.assertEquals(7, action.getWordStart(textArea, 7));
        Assert.assertEquals(8, action.getWordStart(textArea, 8));
        Assert.assertEquals(8, action.getWordStart(textArea, 9));
        Assert.assertEquals(8, action.getWordStart(textArea, 10));
        Assert.assertEquals(8, action.getWordStart(textArea, 11));
        Assert.assertEquals(8, action.getWordStart(textArea, 12));
        Assert.assertEquals(8, action.getWordStart(textArea, 13));

        textArea.setText("  for  ");
        Assert.assertEquals(0, action.getWordStart(textArea, 0));
        Assert.assertEquals(1, action.getWordStart(textArea, 1));
        Assert.assertEquals(2, action.getWordStart(textArea, 2));
        Assert.assertEquals(2, action.getWordStart(textArea, 3));
        Assert.assertEquals(2, action.getWordStart(textArea, 4));
        Assert.assertEquals(5, action.getWordStart(textArea, 5));
        Assert.assertEquals(6, action.getWordStart(textArea, 6));
        Assert.assertEquals(7, action.getWordStart(textArea, 7));

        doc.replace(0, doc.getLength(), "", null);
        Assert.assertEquals(0, action.getWordStart(textArea, 0));
    }

    @Test
    public void testGetWordEnd() throws Exception {
        DumbCompleteWordAction action = new DumbCompleteWordAction();
        SyntaxDocument doc = new SyntaxDocument(
                ISyntaxConstants.SYNTAX_STYLE_JAVA);
        SyntaxTextArea textArea = new SyntaxTextArea(doc);

        textArea.setText("for if  while");
        Assert.assertEquals(3, action.getWordEnd(textArea, 0));
        Assert.assertEquals(3, action.getWordEnd(textArea, 1));
        Assert.assertEquals(3, action.getWordEnd(textArea, 2));
        Assert.assertEquals(3, action.getWordEnd(textArea, 3));
        Assert.assertEquals(6, action.getWordEnd(textArea, 4));
        Assert.assertEquals(6, action.getWordEnd(textArea, 5));
        Assert.assertEquals(6, action.getWordEnd(textArea, 6));
        Assert.assertEquals(7, action.getWordEnd(textArea, 7));
        Assert.assertEquals(13, action.getWordEnd(textArea, 8));
        Assert.assertEquals(13, action.getWordEnd(textArea, 9));
        Assert.assertEquals(13, action.getWordEnd(textArea, 10));
        Assert.assertEquals(13, action.getWordEnd(textArea, 11));
        Assert.assertEquals(13, action.getWordEnd(textArea, 12));
        Assert.assertEquals(13, action.getWordEnd(textArea, 13));

        textArea.setText("  for  ");
        Assert.assertEquals(0, action.getWordEnd(textArea, 0));
        Assert.assertEquals(1, action.getWordEnd(textArea, 1));
        Assert.assertEquals(5, action.getWordEnd(textArea, 2));
        Assert.assertEquals(5, action.getWordEnd(textArea, 3));
        Assert.assertEquals(5, action.getWordEnd(textArea, 4));
        Assert.assertEquals(5, action.getWordEnd(textArea, 5));
        Assert.assertEquals(6, action.getWordEnd(textArea, 6));
        Assert.assertEquals(7, action.getWordEnd(textArea, 7));

        doc.replace(0, doc.getLength(), "", null);
        Assert.assertEquals(0, action.getWordEnd(textArea, 0));
    }

    @Test
    public void testGetPreviousWord() throws Exception {
        DumbCompleteWordAction action = new DumbCompleteWordAction();
        SyntaxDocument doc = new SyntaxDocument(
                ISyntaxConstants.SYNTAX_STYLE_JAVA);
        SyntaxTextArea textArea = new SyntaxTextArea(doc);

        textArea.setText("for if  while");
        // Offset 0 throws an exception
        //Assert.assertEquals(0, action.getPreviousWord(textArea, 0));
        Assert.assertEquals(0, action.getPreviousWord(textArea, 1));
        Assert.assertEquals(0, action.getPreviousWord(textArea, 2));
        Assert.assertEquals(0, action.getPreviousWord(textArea, 3));
        Assert.assertEquals(0, action.getPreviousWord(textArea, 4));
        Assert.assertEquals(4, action.getPreviousWord(textArea, 5));
        Assert.assertEquals(4, action.getPreviousWord(textArea, 6));
        // Spaces - find word before spaces
        Assert.assertEquals(4, action.getPreviousWord(textArea, 7));
        Assert.assertEquals(4, action.getPreviousWord(textArea, 8));
        Assert.assertEquals(8, action.getPreviousWord(textArea, 9));
        Assert.assertEquals(8, action.getPreviousWord(textArea, 10));
        Assert.assertEquals(8, action.getPreviousWord(textArea, 11));
        Assert.assertEquals(8, action.getPreviousWord(textArea, 12));
        Assert.assertEquals(8, action.getPreviousWord(textArea, 13));

        textArea.setText("  for  ");
        // Offset 0 throws an exception
        //Assert.assertEquals(0, action.getPreviousWord(textArea, 0));
        //Assert.assertEquals(0, action.getPreviousWord(textArea, 1));
        //Assert.assertEquals(2, action.getPreviousWord(textArea, 2));
        Assert.assertEquals(2, action.getPreviousWord(textArea, 3));
        Assert.assertEquals(2, action.getPreviousWord(textArea, 4));
        Assert.assertEquals(2, action.getPreviousWord(textArea, 5));
        Assert.assertEquals(2, action.getPreviousWord(textArea, 6));
        Assert.assertEquals(2, action.getPreviousWord(textArea, 7));
    }

    @Test
    public void testGetPreviousWord_manyLinesInBetween() throws Exception {
        DumbCompleteWordAction action = new DumbCompleteWordAction();
        SyntaxDocument doc = new SyntaxDocument(
                ISyntaxConstants.SYNTAX_STYLE_JAVA);
        SyntaxTextArea textArea = new SyntaxTextArea(doc);

        textArea.setText("aaron arthur aardvark\nfoo bar\n// bad code\namazing\n   a");
        Assert.assertEquals(textArea.getDocument().getLength() - 1, action
                .getPreviousWord(textArea, textArea.getDocument().getLength()));
        Assert.assertEquals("aaron arthur aardvark\nfoo bar\n// bad code\n"
                .length(), action.getPreviousWord(textArea, textArea
                .getDocument().getLength() - 2));
        Assert.assertEquals("aaron arthur ".length(),
                action.getPreviousWord(textArea, 22));
        Assert.assertEquals("aaron ".length(),
                action.getPreviousWord(textArea, 8));
    }
}