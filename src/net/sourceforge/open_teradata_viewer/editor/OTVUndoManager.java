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

import javax.swing.Action;
import javax.swing.UIManager;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

/**
 * This class manages undos/redos for a particular editor pane. It groups all
 * undos that occur one character position apart together, to avoid Java's
 * horrible "one character at a time" undo behavior. It also recognizes
 * "replace" actions (i.e., text is selected, then the user types), and treats
 * it as a single action, instead of a remove/insert action pair.
 *
 * @author D. Campione
 * 
 */
public class OTVUndoManager extends UndoManager {

    private static final long serialVersionUID = 5982536274869582901L;

    private OTVCompoundEdit compoundEdit;
    private TextArea textArea;
    private int lastOffset;
    private String cantUndoText;
    private String cantRedoText;

    private int internalAtomicEditDepth;

    /**
     * Ctor.
     *
     * @param textArea The parent text area.
     */
    public OTVUndoManager(TextArea textArea) {
        this.textArea = textArea;
        cantUndoText = "Can't Undo";
        cantRedoText = "Can't Redo";
    }

    /**
     * Begins an "atomic" edit. This method is called when TextArea KNOWS that
     * some edits should be compound automatically, such as when the user is
     * typing in overwrite mode (the deletion of the current char + insertion of
     * the new one) or the playing back of a macro.
     *
     * @see #endInternalAtomicEdit()
     */
    public void beginInternalAtomicEdit() {
        if (++internalAtomicEditDepth == 1) {
            if (compoundEdit != null) {
                compoundEdit.end();
            }
            compoundEdit = new OTVCompoundEdit();
        }
    }

    /**
     * Ends an "atomic" edit.
     *
     * @see #beginInternalAtomicEdit()
     */
    public void endInternalAtomicEdit() {
        if (internalAtomicEditDepth > 0 && --internalAtomicEditDepth == 0) {
            addEdit(compoundEdit);
            compoundEdit.end();
            compoundEdit = null;
            updateActions(); // Needed to show the new display name
        }
    }

    /**
     * @return The "Can't Redo" string.
     * @see #getCantUndoText()
     */
    public String getCantRedoText() {
        return cantRedoText;
    }

    /**
     * @return The "Can't Undo" string.
     * @see #getCantRedoText()
     */
    public String getCantUndoText() {
        return cantUndoText;
    }

    /** {@inheritDoc} */
    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        updateActions();
    }

    private OTVCompoundEdit startCompoundEdit(UndoableEdit edit) {
        lastOffset = textArea.getCaretPosition();
        compoundEdit = new OTVCompoundEdit();
        compoundEdit.addEdit(edit);
        addEdit(compoundEdit);
        return compoundEdit;
    }

    /** {@inheritDoc} */
    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        updateActions();
    }

    @Override
    public void undoableEditHappened(UndoableEditEvent e) {
        // This happens when the first undoable edit occurs, and just after an
        // undo. So, we need to update our actions
        if (compoundEdit == null) {
            compoundEdit = startCompoundEdit(e.getEdit());
            updateActions();
            return;
        }

        else if (internalAtomicEditDepth > 0) {
            compoundEdit.addEdit(e.getEdit());
            return;
        }

        // This happens when there's already an undo that has occurred. Test to
        // see if these undos are on back-to-back characters, and if they are,
        // group them as a single edit. Since an undo has already occurred,
        // there is no need to update our actions here
        int diff = textArea.getCaretPosition() - lastOffset;
        // "<=1" allows contiguous "overwrite mode" key presses to be grouped
        // together
        if (Math.abs(diff) <= 1) {
            compoundEdit.addEdit(e.getEdit());
            lastOffset += diff;
            return;
        }

        // This happens when this UndoableEdit didn't occur at the character
        // just after the previous undlabeledit. Since an undo has already
        // occurred, there is no need to update our actions here either
        compoundEdit.end();
        compoundEdit = startCompoundEdit(e.getEdit());
    }

    /**
     * Ensures that undo/redo actions are enabled appropriately and have
     * descriptive text at all times.
     */
    public void updateActions() {
        String text;

        Action a = TextArea.getAction(TextArea.UNDO_ACTION);
        if (canUndo()) {
            a.setEnabled(true);
            text = getUndoPresentationName();
            a.putValue(Action.NAME, text);
            a.putValue(Action.SHORT_DESCRIPTION, text);
        } else {
            if (a.isEnabled()) {
                a.setEnabled(false);
                text = cantUndoText;
                a.putValue(Action.NAME, text);
                a.putValue(Action.SHORT_DESCRIPTION, text);
            }
        }

        a = TextArea.getAction(TextArea.REDO_ACTION);
        if (canRedo()) {
            a.setEnabled(true);
            text = getRedoPresentationName();
            a.putValue(Action.NAME, text);
            a.putValue(Action.SHORT_DESCRIPTION, text);
        } else {
            if (a.isEnabled()) {
                a.setEnabled(false);
                text = cantRedoText;
                a.putValue(Action.NAME, text);
                a.putValue(Action.SHORT_DESCRIPTION, text);
            }
        }
    }

    /**
     * The edit used by {@link OTVUndoManager}.
     *  
     * @author D. Campione
     * 
     */
    class OTVCompoundEdit extends CompoundEdit {

        private static final long serialVersionUID = -3457203341829617731L;

        @Override
        public String getUndoPresentationName() {
            return UIManager.getString("AbstractUndoableEdit.undoText");
        }

        @Override
        public String getRedoPresentationName() {
            return UIManager.getString("AbstractUndoableEdit.redoText");
        }

        @Override
        public boolean isInProgress() {
            return false;
        }

        @Override
        public void undo() throws CannotUndoException {
            if (compoundEdit != null) {
                compoundEdit.end();
            }
            super.undo();
            compoundEdit = null;
        }
    }
}