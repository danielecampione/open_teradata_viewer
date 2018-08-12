/*
 * Open Teradata Viewer ( editor )
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

package net.sourceforge.open_teradata_viewer.editor;

import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

/**
 * The base action used by the actions defined in {@link TextAreaEditorKit}.
 * This action is what allows instances of <code>TextArea</code> to record
 * keystrokes into "macros;" if an action is recordable and occurs while the
 * user is recording a macro, it adds itself to the currently-being-recorded
 * macro.
 *
 * @author D. Campione
 * 
 */
public abstract class RecordableTextAction extends TextAction {

    private static final long serialVersionUID = 3169945064084218559L;

    /** Whether or not this text action should be recorded in a macro. */
    private boolean isRecordable;

    /**
     * Ctor.
     *
     * @param text The text (name) associated with the action.
     */
    public RecordableTextAction(String text) {
        this(text, null, null, null, null);
    }

    /**
     * Ctor.
     *
     * @param text The text (name) associated with the action.
     * @param icon The icon associated with the action.
     * @param desc The description of the action.
     * @param mnemonic The mnemonic for the action.
     * @param accelerator The accelerator key for the action.
     */
    public RecordableTextAction(String text, Icon icon, String desc,
            Integer mnemonic, KeyStroke accelerator) {
        super(text);
        putValue(SMALL_ICON, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(ACCELERATOR_KEY, accelerator);
        putValue(MNEMONIC_KEY, mnemonic);
        setRecordable(true);
    }

    /**
     * This method is final so that you cannot override it and mess up the
     * macro-recording part of it. The actual meat of the action is in
     * <code>actionPerformedImpl</code>.
     *
     * @param e The action being performed.
     * @see #actionPerformedImpl
     */
    public final void actionPerformed(ActionEvent e) {
        JTextComponent textComponent = getTextComponent(e);
        if (textComponent instanceof TextArea) {
            TextArea textArea = (TextArea) textComponent;
            if (TextArea.isRecordingMacro() && isRecordable()) {
                int mod = e.getModifiers();
                // We ignore keypresses involving ctrl/alt/meta if they are
                // "default" keypresses - i.e., they aren't some special action
                // like paste (e.g., paste would return Ctrl+V, but its action
                // would be "paste-action")
                String macroID = getMacroID();
                if (!DefaultEditorKit.defaultKeyTypedAction.equals(macroID)
                        || ((mod & ActionEvent.ALT_MASK) == 0
                                && (mod & ActionEvent.CTRL_MASK) == 0 && (mod & ActionEvent.META_MASK) == 0)) {
                    String command = e.getActionCommand();
                    TextArea.addToCurrentMacro(macroID, command);
                }
            }
            actionPerformedImpl(e, textArea);
        }
    }

    /**
     * The actual meat of the action. If you wish to subclass this action and
     * modify its behavior, this is the method to override.
     *
     * @param e The action being performed.
     * @param textArea The text area "receiving" the action.
     * @see #actionPerformed
     */
    public abstract void actionPerformedImpl(ActionEvent e, TextArea textArea);

    /**
     * Returns the accelerator for this action.
     *
     * @return The accelerator.
     * @see #setAccelerator(KeyStroke)
     */
    public KeyStroke getAccelerator() {
        return (KeyStroke) getValue(ACCELERATOR_KEY);
    }

    /**
     * Returns the description for this action.
     *
     * @return The description.
     */
    public String getDescription() {
        return (String) getValue(SHORT_DESCRIPTION);
    }

    /**
     * Returns the icon for this action.
     *
     * @return The icon.
     */
    public Icon getIcon() {
        return (Icon) getValue(SMALL_ICON);
    }

    /**
     * Returns the identifier for this macro. This method makes it so that you
     * can create an instance of the <code>TextAreaEditorKit.CutAction</code>
     * action, for example, rename it to "Remove", and it will still be recorded
     * as a "cut" action. Subclasses should return a unique string from this
     * method; preferably the name of the action.<p> If you subclass a
     * <code>RecordableTextAction</code>, you should NOT override this method;
     * if you do, the action may not be properly recorded in a macro.
     *
     * @return The internally-used macro ID.
     */
    public abstract String getMacroID();

    /**
     * Returns the mnemonic for this action.
     *
     * @return The mnemonic, or <code>-1</code> if not defined.
     * @see #setMnemonic(char)
     * @see #setMnemonic(Integer)
     */
    public int getMnemonic() {
        Integer i = (Integer) getValue(MNEMONIC_KEY);
        return i != null ? i.intValue() : -1;
    }

    /**
     * @return The name of this action.
     * @see #setName(String)
     */
    public String getName() {
        return (String) getValue(NAME);
    }

    /**
     * Returns whether or not this action will be recorded and replayed in a
     * macro.
     *
     * @return Whether or not this action will be recorded and replayed.
     * @see #setRecordable
     */
    public boolean isRecordable() {
        return isRecordable;
    }

    /**
     * Sets the accelerator for this action.
     *
     * @param accelerator The new accelerator.
     * @see #getAccelerator()
     */
    public void setAccelerator(KeyStroke accelerator) {
        putValue(ACCELERATOR_KEY, accelerator);
    }

    /**
     * Sets the mnemonic for this action.
     *
     * @param mnemonic The new mnemonic.
     * @see #setMnemonic(Integer)
     * @see #getMnemonic()
     */
    public void setMnemonic(char mnemonic) {
        setMnemonic(new Integer(mnemonic));
    }

    /**
     * Sets the mnemonic for this action.
     *
     * @param mnemonic The new mnemonic.
     * @see #setMnemonic(char)
     * @see #getMnemonic()
     */
    public void setMnemonic(Integer mnemonic) {
        putValue(MNEMONIC_KEY, mnemonic);
    }

    /**
     * Sets the name of this action.
     *
     * @param name The new name.
     * @see #getName()
     */
    public void setName(String name) {
        putValue(NAME, name);
    }

    /**
     * Sets whether or not this action will be recorded and replayed in a macro.
     *
     * @param recordable Whether or not this action should be recorded and
     *                   replayed.
     * @see #isRecordable
     */
    public void setRecordable(boolean recordable) {
        isRecordable = recordable;
    }

    /**
     * Sets the short description for this action.
     *
     * @param shortDesc The short description for this action.
     */
    public void setShortDescription(String shortDesc) {
        putValue(SHORT_DESCRIPTION, shortDesc);
    }
}