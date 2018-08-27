/*
 * Open Teradata Viewer ( editor )
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

package test.net.sourceforge.open_teradata_viewer.editor;

import net.sourceforge.open_teradata_viewer.editor.OTVDocument;
import net.sourceforge.open_teradata_viewer.editor.TextArea;
import net.sourceforge.open_teradata_viewer.editor.TextAreaEditorKit.DumbCompleteWordAction;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Unit tests for the {@link DumbCompleteWordAction} class.
 *
 * @author D. Campione
 *
 */
@RunWith(SwingRunner.class)
public class TextAreaEditorKitDumbCompleteWordActionTest {

    @Test
    public void testGetWordStart() throws Exception {
        DumbCompleteWordAction action = new DumbCompleteWordAction();
        OTVDocument doc = new OTVDocument();
        TextArea textArea = new TextArea(doc);

        textArea.setText("for if  while");
        Assert.assertEquals(0, action.getWordStart(textArea, 0));
        Assert.assertEquals(0, action.getWordStart(textArea, 1));
        Assert.assertEquals(0, action.getWordStart(textArea, 2));
        Assert.assertEquals(3, action.getWordStart(textArea, 3));
        Assert.assertEquals(4, action.getWordStart(textArea, 4));
        Assert.assertEquals(4, action.getWordStart(textArea, 5));
        Assert.assertEquals(6, action.getWordStart(textArea, 6));
        // Multiple adjacent spaces treated as a word
        Assert.assertEquals(6, action.getWordStart(textArea, 7));
        Assert.assertEquals(8, action.getWordStart(textArea, 8));
        Assert.assertEquals(8, action.getWordStart(textArea, 9));
        Assert.assertEquals(8, action.getWordStart(textArea, 10));
        Assert.assertEquals(8, action.getWordStart(textArea, 11));
        Assert.assertEquals(8, action.getWordStart(textArea, 12));
        Assert.assertEquals(8, action.getWordStart(textArea, 13));

        textArea.setText("  for  ");
        Assert.assertEquals(0, action.getWordStart(textArea, 0));
        Assert.assertEquals(0, action.getWordStart(textArea, 1));
        Assert.assertEquals(2, action.getWordStart(textArea, 2));
        Assert.assertEquals(2, action.getWordStart(textArea, 3));
        Assert.assertEquals(2, action.getWordStart(textArea, 4));
        Assert.assertEquals(5, action.getWordStart(textArea, 5));
        Assert.assertEquals(5, action.getWordStart(textArea, 6));
        Assert.assertEquals(5, action.getWordStart(textArea, 7));

        doc.replace(0, doc.getLength(), "", null);
        Assert.assertEquals(0, action.getWordStart(textArea, 0));
    }

    @Test
    public void testGetWordEnd() throws Exception {
        DumbCompleteWordAction action = new DumbCompleteWordAction();
        OTVDocument doc = new OTVDocument();
        TextArea textArea = new TextArea(doc);

        textArea.setText("for if  while");
        Assert.assertEquals(3, action.getWordEnd(textArea, 0));
        Assert.assertEquals(3, action.getWordEnd(textArea, 1));
        Assert.assertEquals(3, action.getWordEnd(textArea, 2));
        Assert.assertEquals(4, action.getWordEnd(textArea, 3));
        Assert.assertEquals(6, action.getWordEnd(textArea, 4));
        Assert.assertEquals(6, action.getWordEnd(textArea, 5));
        Assert.assertEquals(8, action.getWordEnd(textArea, 6));
        Assert.assertEquals(8, action.getWordEnd(textArea, 7));
        Assert.assertEquals(13, action.getWordEnd(textArea, 8));
        Assert.assertEquals(13, action.getWordEnd(textArea, 9));
        Assert.assertEquals(13, action.getWordEnd(textArea, 10));
        Assert.assertEquals(13, action.getWordEnd(textArea, 11));
        Assert.assertEquals(13, action.getWordEnd(textArea, 12));
        Assert.assertEquals(13, action.getWordEnd(textArea, 13));

        textArea.setText("  for  ");
        Assert.assertEquals(2, action.getWordEnd(textArea, 0));
        Assert.assertEquals(2, action.getWordEnd(textArea, 1));
        Assert.assertEquals(5, action.getWordEnd(textArea, 2));
        Assert.assertEquals(5, action.getWordEnd(textArea, 3));
        Assert.assertEquals(5, action.getWordEnd(textArea, 4));
        Assert.assertEquals(7, action.getWordEnd(textArea, 5));
        Assert.assertEquals(7, action.getWordEnd(textArea, 6));
        Assert.assertEquals(7, action.getWordEnd(textArea, 7));

        doc.replace(0, doc.getLength(), "", null);
        Assert.assertEquals(0, action.getWordEnd(textArea, 0));
    }

    @Test
    public void testGetPreviousWord() throws Exception {
        DumbCompleteWordAction action = new DumbCompleteWordAction();
        OTVDocument doc = new OTVDocument();
        TextArea textArea = new TextArea(doc);

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
}