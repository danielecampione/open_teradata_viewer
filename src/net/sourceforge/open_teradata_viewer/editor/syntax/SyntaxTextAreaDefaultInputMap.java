/*
 * Open Teradata Viewer ( editor syntax )
 * Copyright (C) 2013, D. Campione
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

package net.sourceforge.open_teradata_viewer.editor.syntax;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import net.sourceforge.open_teradata_viewer.editor.TADefaultInputMap;

/**
 * The default input map for an <code>SyntaxTextArea</code>.
 * Currently, the new key bindings include:
 * <ul>
 *   <li>Shift+Tab indents the current line or currently selected lines to the
 *       left.
 * </ul>
 *
 * @author D. Campione
 * 
 */
public class SyntaxTextAreaDefaultInputMap extends TADefaultInputMap {

    private static final long serialVersionUID = -8887284768510591809L;

    /** Constructs the default input map for an <code>SyntaxTextArea</code>. */
    public SyntaxTextAreaDefaultInputMap() {
        int defaultMod = getDefaultModifier();
        int shift = InputEvent.SHIFT_MASK;

        put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, shift),
                SyntaxTextAreaEditorKit.staDecreaseIndentAction);
        put(KeyStroke.getKeyStroke('}'),
                SyntaxTextAreaEditorKit.staCloseCurlyBraceAction);
        put(KeyStroke.getKeyStroke('/'),
                SyntaxTextAreaEditorKit.staCloseMarkupTagAction);
        int os = SyntaxUtilities.getOS();
        if (os == SyntaxUtilities.OS_WINDOWS
                || os == SyntaxUtilities.OS_MAC_OSX) {
            // *nix causes trouble with CloseMarkupTagAction and
            // ToggleCommentAction. It triggers both KEY_PRESSED ctrl+'/' and
            // KEY_TYPED '/' events when the user presses ctrl+'/', but Windows
            // and OS X do not. If we try to "move" the KEY_TYPED event for '/'
            // to KEY_PRESSED, it'll work for Linux boxes with QWERTY keyboard
            // layouts, but non-QUERTY users won't be able to type a '/'
            // character at all then. Rather than try to hack together a
            // solution by trying to detect the IM locale and do different
            // things for different OSes & keyboard layouts, we do the simplest
            // thing and (unfortunately) don't have a ToggleCommentAction for
            // *nix out-of-the-box. Applications can add one easily enough if
            // they want one
            put(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, defaultMod),
                    SyntaxTextAreaEditorKit.staToggleCommentAction);
        }

        put(KeyStroke.getKeyStroke(KeyEvent.VK_OPEN_BRACKET, defaultMod),
                SyntaxTextAreaEditorKit.staGoToMatchingBracketAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, defaultMod),
                SyntaxTextAreaEditorKit.staCollapseFoldAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_ADD, defaultMod),
                SyntaxTextAreaEditorKit.staExpandFoldAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_DIVIDE, defaultMod),
                SyntaxTextAreaEditorKit.staCollapseAllFoldsAction);
        put(KeyStroke.getKeyStroke(KeyEvent.VK_MULTIPLY, defaultMod),
                SyntaxTextAreaEditorKit.staExpandAllFoldsAction);

        // NOTE: no modifiers => mapped to keyTyped. If we had "0" as a second
        // parameter, we'd get the template action (keyPressed) AND the default
        // space action (keyTyped)
        put(CodeTemplateManager.TEMPLATE_KEYSTROKE,
                SyntaxTextAreaEditorKit.staPossiblyInsertTemplateAction);
    }
}