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

import org.fife.ui.autocomplete.AutoCompletion;
import org.fife.ui.rtextarea.RTextScrollPane;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.SQLCellRenderer;

/**
 * The settings shared by all the documents of the editor.
 * <p>
 * They are kept by the components of each document, not in one place: the
 * commands that change them are applied to every document, and this class
 * brings a document that has just been created to the same settings of one that
 * already exists.
 *
 * @author D. Campione
 */
public final class EditorSettings {

    private EditorSettings() {
    }

    /**
     * Copies the settings shared by the documents of the editor, but the theme
     * and the spell checking, from a document to another one: the font, the
     * options of the text area, of its scroll pane and of the auto-completion.
     * <p>
     * The theme changes some of these options (and resets the font), so it has
     * to be applied to the target before this method is called.
     *
     * @param from The document to copy the settings from.
     * @param to The document to copy the settings to.
     */
    public static void copy(EditorDocument from, EditorDocument to) {
        OTVSyntaxTextArea source = from.getTextArea();
        OTVSyntaxTextArea target = to.getTextArea();

        OTVSyntaxTextArea.setFont(target, source.getFont());
        target.setHighlightCurrentLine(source.getHighlightCurrentLine());
        target.setFadeCurrentLineHighlight(source.getFadeCurrentLineHighlight());
        target.setMarkOccurrences(source.getMarkOccurrences());
        target.setLineWrap(source.getLineWrap());
        target.setAntiAliasingEnabled(source.getAntiAliasingEnabled());
        target.setAnimateBracketMatching(source.getAnimateBracketMatching());
        target.setShowMatchedBracketPopup(source.getShowMatchedBracketPopup());
        target.setPaintMatchedBracketPair(source.getPaintMatchedBracketPair());
        target.setPaintTabLines(source.getPaintTabLines());
        target.setTabsEmulated(source.getTabsEmulated());

        RTextScrollPane sourcePane = from.getScrollPane();
        RTextScrollPane targetPane = to.getScrollPane();
        targetPane.setLineNumbersEnabled(sourcePane.getLineNumbersEnabled());
        targetPane.setIconRowHeaderEnabled(sourcePane.isIconRowHeaderEnabled());
        targetPane.applyComponentOrientation(sourcePane.getComponentOrientation());

        AutoCompletion sourceCompletion = from.getAutoCompletion();
        AutoCompletion targetCompletion = to.getAutoCompletion();
        if (sourceCompletion != null && targetCompletion != null) {
            targetCompletion.setParameterAssistanceEnabled(sourceCompletion.isParameterAssistanceEnabled());
            targetCompletion.setShowDescWindow(sourceCompletion.getShowDescWindow());
            targetCompletion
                    .setListCellRenderer(sourceCompletion.getListCellRenderer() == null ? null : new SQLCellRenderer());
        }
    }
}
