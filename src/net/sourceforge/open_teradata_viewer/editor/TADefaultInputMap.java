/*
 * Open Teradata Viewer ( editor )
 * Copyright (C) 2012, D. Campione
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

import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;

import net.sourceforge.open_teradata_viewer.actions.Actions;

/**
 * The default input map for an <code>TextArea</code>. For the most part it is
 * exactly that the one for a <code>JTextArea</code>, but it adds a few things.
 * Currently, the new key bindings include:
 * <ul>
 *   <li>HOME key toggles between first character on line and first non-
 *       whitespace character on line.
 *   <li>INSERT key toggles between insert and overwrite modes.
 *   <li>Ctrl+DELETE key deletes all text between the caret and the end of the
 *       current line.
 *   <li>Ctrl+Shift+Up and Ctrl+Shift+Down move the current line up and down,
 *       respectively.
 *   <li>Ctrl+J joins lines.
 *   <li>Ctrl+Z is undo and Ctrl+Y is redo.
 *   <li>Ctrl+Up and Ctrl+Down shift the visible area of the text area up and
 *       down one line, respectively.
 *   <li>F2 and Shift+F2 moves to the next and previous bookmarks, respectively.
 *   <li>Ctrl+F2 toggles whether a bookmark is on the current line.
 *   <li>etc..
 * </ul>
 * 
 * @author D. Campione
 * 
 */
public class TADefaultInputMap extends InputMap {

    private static final long serialVersionUID = -2883282204711226260L;

    /** Constructs the default input map for an <code>TextArea</code>. */
    public TADefaultInputMap() {
        super();

        int defaultModifier = getDefaultModifier();
        int alt = InputEvent.ALT_MASK;
        int shift = InputEvent.SHIFT_MASK;

        put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0),
                DefaultEditorKit.beginLineAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, shift),
                DefaultEditorKit.selectionBeginLineAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, defaultModifier),
                DefaultEditorKit.beginAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, defaultModifier | shift),
                DefaultEditorKit.selectionBeginAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_END, 0),
                DefaultEditorKit.endLineAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_END, shift),
                DefaultEditorKit.selectionEndLineAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_END, defaultModifier),
                DefaultEditorKit.endAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_END, defaultModifier | shift),
                DefaultEditorKit.selectionEndAction);

        put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),
                DefaultEditorKit.backwardAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, shift),
                DefaultEditorKit.selectionBackwardAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, defaultModifier),
                DefaultEditorKit.previousWordAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, defaultModifier | shift),
                DefaultEditorKit.selectionPreviousWordAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),
                DefaultEditorKit.downAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, shift),
                DefaultEditorKit.selectionDownAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, defaultModifier),
                TextAreaEditorKit.taScrollDownAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, alt),
                TextAreaEditorKit.taLineDownAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),
                DefaultEditorKit.forwardAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, shift),
                DefaultEditorKit.selectionForwardAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, defaultModifier),
                DefaultEditorKit.nextWordAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, defaultModifier | shift),
                DefaultEditorKit.selectionNextWordAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),
                DefaultEditorKit.upAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, shift),
                DefaultEditorKit.selectionUpAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, defaultModifier),
                TextAreaEditorKit.taScrollUpAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, alt),
                TextAreaEditorKit.taLineUpAction);

        put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, 0),
                DefaultEditorKit.pageUpAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_UP, shift),
                TextAreaEditorKit.taSelectionPageUpAction);
        put(KeyStroke
                .getKeyStroke(KeyEvent.VK_PAGE_UP, defaultModifier | shift),
                TextAreaEditorKit.taSelectionPageLeftAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, 0),
                DefaultEditorKit.pageDownAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, shift),
                TextAreaEditorKit.taSelectionPageDownAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_PAGE_DOWN, defaultModifier
                | shift), TextAreaEditorKit.taSelectionPageRightAction);

        put(KeyStroke.getKeyStroke(KeyEvent.VK_CUT, 0),
                DefaultEditorKit.cutAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_COPY, 0),
                DefaultEditorKit.copyAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_PASTE, 0),
                DefaultEditorKit.pasteAction);

        put(KeyStroke.getKeyStroke(KeyEvent.VK_X, defaultModifier),
                DefaultEditorKit.cutAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_C, defaultModifier),
                DefaultEditorKit.copyAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_V, defaultModifier),
                DefaultEditorKit.pasteAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
                DefaultEditorKit.deleteNextCharAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, shift),
                DefaultEditorKit.cutAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, defaultModifier),
                TextAreaEditorKit.taDeleteRestOfLineAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, 0),
                TextAreaEditorKit.taToggleTextModeAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, shift),
                DefaultEditorKit.pasteAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_INSERT, defaultModifier),
                DefaultEditorKit.copyAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_A, defaultModifier),
                DefaultEditorKit.selectAllAction);

        put(KeyStroke.getKeyStroke(KeyEvent.VK_D, defaultModifier),
                TextAreaEditorKit.taDeleteLineAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_J, defaultModifier),
                TextAreaEditorKit.taJoinLinesAction);

        put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, shift),
                DefaultEditorKit.deletePrevCharAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, defaultModifier),
                TextAreaEditorKit.taDeletePrevWordAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0),
                DefaultEditorKit.insertTabAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
                DefaultEditorKit.insertBreakAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, shift),
                DefaultEditorKit.insertBreakAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, defaultModifier),
                TextAreaEditorKit.taDumbCompleteWordAction);

        put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, defaultModifier),
                TextAreaEditorKit.taUndoAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_Y, defaultModifier),
                TextAreaEditorKit.taRedoAction);

        put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0),
                TextAreaEditorKit.taNextBookmarkAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, shift),
                TextAreaEditorKit.taPrevBookmarkAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, defaultModifier),
                TextAreaEditorKit.taToggleBookmarkAction);

        put(KeyStroke.getKeyStroke(KeyEvent.VK_K, defaultModifier | shift),
                TextAreaEditorKit.taPrevOccurrenceAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_K, defaultModifier),
                TextAreaEditorKit.taNextOccurrenceAction);

        // NOTE: Currently, macros aren't part of the default input map for
        // TextArea, as they display their own popup windows, etc.. which may or
        // may not clash with the application in which the TextArea resides. You
        // can add the macro actions yourself into an application if you want.
        // They may become standard in the future if I can find a way to
        // implement them that I like

        put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, defaultModifier),
                Actions.RUN);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0), Actions.RUN);
    }

    /**
     * Returns the default modifier key for a system. For example, on Windows
     * this would be the CTRL key (<code>InputEvent.CTRL_MASK</code>).
     *
     * @return The default modifier key.
     */
    protected static final int getDefaultModifier() {
        return Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    }
}