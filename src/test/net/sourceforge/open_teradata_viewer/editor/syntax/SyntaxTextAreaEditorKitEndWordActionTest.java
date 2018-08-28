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

import javax.swing.text.DefaultEditorKit;

import org.junit.Assert;
import org.junit.Test;

import net.sourceforge.open_teradata_viewer.editor.TextArea;
import net.sourceforge.open_teradata_viewer.editor.syntax.ISyntaxConstants;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxDocument;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextAreaEditorKit.EndWordAction;

/**
 * Unit tests for the {@link EndWordAction} class.
 *
 * @author D. Campione
 *
 */
public class SyntaxTextAreaEditorKitEndWordActionTest {

    /**
     * Returns a fake action event for test purposes.
     *
     * @param textArea The source text area.
     * @return The fake action event.
     */
    private static final ActionEvent createActionEvent(TextArea textArea) {
        return new ActionEvent(textArea, 0, DefaultEditorKit.endWordAction);
    }

    @Test
    public void testGetWordEnd_noSelection_happyPath() {
        EndWordAction action = new EndWordAction("endWordAction", false);
        SyntaxDocument doc = new SyntaxDocument(
                ISyntaxConstants.SYNTAX_STYLE_JAVA);
        SyntaxTextArea textArea = new SyntaxTextArea(doc);

        final String TEXT = "This is the best";
        textArea.setText(TEXT);
        for (int i = 0; i < "This".length(); i++) {
            textArea.setCaretPosition(i);
            action.actionPerformed(createActionEvent(textArea));
            Assert.assertEquals("This".length(), textArea.getCaretPosition());
        }

        textArea.setCaretPosition("This".length());
        action.actionPerformed(createActionEvent(textArea));
        Assert.assertEquals("This ".length(), textArea.getCaretPosition());

        textArea.setCaretPosition("This ".length());
        action.actionPerformed(createActionEvent(textArea));
        Assert.assertEquals("This is".length(), textArea.getCaretPosition());

        textArea.setCaretPosition(TEXT.length());
        action.actionPerformed(createActionEvent(textArea));
        Assert.assertEquals(TEXT.length(), textArea.getCaretPosition());
    }
}