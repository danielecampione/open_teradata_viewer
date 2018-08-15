/*
 * Open Teradata Viewer ( graphic viewer )
 * Copyright (C) 2014, D. Campione
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

package net.sourceforge.open_teradata_viewer.graphic_viewer;

import java.util.Vector;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerUndoManager extends UndoManager
        implements
            IGraphicViewerDocumentListener {

    private static final long serialVersionUID = 794457760759349097L;

    public String[] myViewEditPresentationNames = {"", "Move Selection",
            "Copy Selection", "Resize", "New Link", "ReLink", "Drop",
            "Edit Text", "Copy", "Cut", "Paste", "Delete Selection",
            "Scrollbar", "No ReLink", "Undo", "Redo"};
    private boolean myUndoing = false;
    private boolean myRedoing = false;
    private GraphicViewerCompoundEdit myCompoundEdit = null;
    private int myLevel = 0;

    public void undo() throws CannotUndoException {
        try {
            myUndoing = true;
            endTransaction(true, null);
            super.undo();
            myUndoing = false;
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
            myUndoing = false;
        }
    }

    public void redo() throws CannotRedoException {
        try {
            myRedoing = true;
            endTransaction(false, null);
            super.redo();
            myRedoing = false;
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
            myRedoing = false;
        }
    }

    public void discardAllEdits() {
        endTransaction(false, null);
        super.discardAllEdits();
    }

    public void documentChanged(
            GraphicViewerDocumentEvent graphicviewerdocumentevent) {
        if (isUndoing() || isRedoing()) {
            return;
        }
        if (!skipEvent(graphicviewerdocumentevent)) {
            GraphicViewerCompoundEdit graphicviewercompoundedit = getCurrentEdit();
            if (graphicviewercompoundedit == null) {
                graphicviewercompoundedit = new GraphicViewerCompoundEdit();
                setCurrentEdit(graphicviewercompoundedit);
            }
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit = new GraphicViewerDocumentChangedEdit(
                    graphicviewerdocumentevent, this,
                    graphicviewerdocumentevent.isBeforeChanging());
            graphicviewercompoundedit.addEdit(graphicviewerdocumentchangededit);
        }
    }

    public boolean skipEvent(
            GraphicViewerDocumentEvent graphicviewerdocumentevent) {
        if (graphicviewerdocumentevent.getHint() < 200) {
            return true;
        }
        Object obj = graphicviewerdocumentevent.getSource();
        if (obj == null || !(obj instanceof GraphicViewerDocument)) {
            return true;
        }
        GraphicViewerDocument graphicviewerdocument = (GraphicViewerDocument) obj;
        if (graphicviewerdocument.isSkipsUndoManager()) {
            return true;
        }
        if (graphicviewerdocumentevent.getHint() == 203) {
            if (graphicviewerdocumentevent.getFlags() == 0) {
                return true;
            }
            GraphicViewerObject graphicviewerobject = graphicviewerdocumentevent
                    .getGraphicViewerObject();
            if (graphicviewerobject == null
                    || graphicviewerobject.isSkipsUndoManager()) {
                return true;
            }
        }
        return false;
    }

    public int getTransactionLevel() {
        return myLevel;
    }

    public void startTransaction() {
        myLevel++;
    }

    public void endTransaction(boolean flag) {
        endTransaction(flag, null);
    }

    public void endTransaction(String s) {
        endTransaction(true, s);
    }

    void endTransaction(boolean flag, String s) {
        if (myLevel > 0) {
            myLevel--;
        }
        if (myLevel == 0 && myCompoundEdit != null) {
            if (flag) {
                myCompoundEdit.end();
                if (myCompoundEdit.getAllEdits().size() > 0) {
                    if (s != null) {
                        myCompoundEdit.setPresentationName(s);
                    }
                    addEdit(myCompoundEdit);
                }
            } else {
                myCompoundEdit.die();
            }
            myCompoundEdit = null;
        }
    }

    public Vector getAllEdits() {
        return edits;
    }

    public Vector getCurrentEditVector() {
        if (getCurrentEdit() != null) {
            return getCurrentEdit().getAllEdits();
        } else {
            return null;
        }
    }

    public GraphicViewerCompoundEdit getCurrentEdit() {
        return myCompoundEdit;
    }

    public void setCurrentEdit(
            GraphicViewerCompoundEdit graphicviewercompoundedit) {
        myCompoundEdit = graphicviewercompoundedit;
    }

    UndoableEdit getEditToUndo() {
        return editToBeUndone();
    }

    UndoableEdit getEditToRedo() {
        return editToBeRedone();
    }

    public boolean isUndoing() {
        return myUndoing;
    }

    public boolean isRedoing() {
        return myRedoing;
    }

    public String getViewEditPresentationName(int i) {
        if (i >= 0 && i < myViewEditPresentationNames.length) {
            return myViewEditPresentationNames[i];
        } else {
            return null;
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    public class GraphicViewerCompoundEdit extends CompoundEdit {

        private static final long serialVersionUID = -897048645868128627L;

        private String myName = "";

        public GraphicViewerCompoundEdit() {
        }

        public Vector getAllEdits() {
            return edits;
        }

        public String getPresentationName() {
            return myName;
        }

        public void setPresentationName(String s) {
            myName = s;
        }

        public String getUndoPresentationName() {
            return getViewEditPresentationName(14) + " "
                    + getPresentationName();
        }

        public String getRedoPresentationName() {
            return getViewEditPresentationName(15) + " "
                    + getPresentationName();
        }
    }
}