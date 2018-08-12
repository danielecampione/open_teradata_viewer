/*
 * Open Teradata Viewer ( graphic viewer )
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

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    public class GraphicViewerCompoundEdit extends CompoundEdit {

        private static final long serialVersionUID = -897048645868128627L;

        public Vector<UndoableEdit> getAllEdits() {
            return edits;
        }

        public String getPresentationName() {
            return a;
        }

        public void setPresentationName(String s) {
            a = s;
        }

        public String getUndoPresentationName() {
            return getViewEditPresentationName(14) + " "
                    + getPresentationName();
        }

        public String getRedoPresentationName() {
            return getViewEditPresentationName(15) + " "
                    + getPresentationName();
        }

        private String a;

        public GraphicViewerCompoundEdit() {
            a = "";
        }
    }

    public GraphicViewerUndoManager() {
        q = false;
        p = false;
        n = null;
        o = 0;
    }

    public void undo() throws CannotUndoException {
        try {
            q = true;
            a(true, null);
            super.undo();
            q = false;
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
            q = false;
        }
    }

    public void redo() throws CannotRedoException {
        try {
            p = true;
            a(false, null);
            super.redo();
            p = false;
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
            p = false;
        }
    }

    public void discardAllEdits() {
        a(false, null);
        super.discardAllEdits();
    }

    public void documentChanged(
            GraphicViewerDocumentEvent graphicviewerdocumentevent) {
        if (isUndoing() || isRedoing())
            return;
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
        if (graphicviewerdocumentevent.getHint() < 200)
            return true;
        Object obj = graphicviewerdocumentevent.getSource();
        if (obj == null || !(obj instanceof GraphicViewerDocument))
            return true;
        GraphicViewerDocument graphicviewerdocument = (GraphicViewerDocument) obj;
        if (graphicviewerdocument.isSkipsUndoManager())
            return true;
        if (graphicviewerdocumentevent.getHint() == 203) {
            if (graphicviewerdocumentevent.getFlags() == 0)
                return true;
            GraphicViewerObject graphicviewerobject = graphicviewerdocumentevent
                    .getGraphicViewerObject();
            if (graphicviewerobject == null
                    || graphicviewerobject.isSkipsUndoManager())
                return true;
        }
        return false;
    }

    public int getTransactionLevel() {
        return o;
    }

    public void startTransaction() {
        o++;
    }

    public void endTransaction(boolean flag) {
        a(flag, null);
    }

    public void endTransaction(String s) {
        a(true, s);
    }

    void a(boolean flag, String s) {
        if (o > 0)
            o--;
        if (o == 0 && n != null) {
            if (flag) {
                n.end();
                if (n.getAllEdits().size() > 0) {
                    if (s != null)
                        n.setPresentationName(s);
                    addEdit(n);
                }
            } else {
                n.die();
            }
            n = null;
        }
    }

    public Vector<UndoableEdit> getAllEdits() {
        return edits;
    }

    public Vector<UndoableEdit> getCurrentEditVector() {
        if (getCurrentEdit() != null)
            return getCurrentEdit().getAllEdits();
        else
            return null;
    }

    public GraphicViewerCompoundEdit getCurrentEdit() {
        return n;
    }

    public void setCurrentEdit(
            GraphicViewerCompoundEdit graphicviewercompoundedit) {
        n = graphicviewercompoundedit;
    }

    UndoableEdit _mthnew() {
        return editToBeUndone();
    }

    UndoableEdit _mthint() {
        return editToBeRedone();
    }

    public boolean isUndoing() {
        return q;
    }

    public boolean isRedoing() {
        return p;
    }

    public String getViewEditPresentationName(int i) {
        if (i >= 0 && i < myViewEditPresentationNames.length)
            return myViewEditPresentationNames[i];
        else
            return null;
    }

    public String myViewEditPresentationNames[] = {"", "Move Selection",
            "Copy Selection", "Resize", "New Link", "ReLink", "Drop",
            "Edit Text", "Copy", "Cut", "Paste", "Delete Selection",
            "Scrollbar", "No ReLink", "Undo", "Redo"};
    private boolean q;
    private boolean p;
    private GraphicViewerCompoundEdit n;
    private int o;
}