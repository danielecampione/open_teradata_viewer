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

import java.awt.Rectangle;
import java.util.Vector;

import javax.swing.undo.UndoableEdit;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class UndoMgr extends GraphicViewerUndoManager {

    private static final long serialVersionUID = 4723144372511071212L;

    public UndoMgr() {
    }

    // override these UndoManager methods to trap updates

    public boolean addEdit(UndoableEdit undoable) {
        setLogPrefix("");
        logChanges(undoable, true);
        return super.addEdit(undoable);
    }

    public void endTransaction(String pname) {
        boolean outer = (getTransactionLevel() == 1);
        super.endTransaction(pname);
        if (outer) {
            ApplicationFrame.getInstance().getConsole()
                    .println("  ended: " + pname);
        }
    }

    public void undo() {
        if (canUndo()) {
            setLogPrefix(" UNDO: ");
            logChanges(editToBeUndone(), false);
        }
        super.undo();
    }

    public void redo() {
        if (canRedo()) {
            setLogPrefix(" REDO: ");
            logChanges(editToBeRedone(), true);
        }
        super.redo();
    }

    // logging methods

    public String getLogPrefix() {
        return myLogPrefix;
    }
    public void setLogPrefix(String s) {
        myLogPrefix = s;
    }

    public void logChanges(UndoableEdit paramUndoableEdit, boolean paramBoolean) {
        if ((paramUndoableEdit instanceof GraphicViewerUndoManager.GraphicViewerCompoundEdit)) {
            GraphicViewerUndoManager.GraphicViewerCompoundEdit localGraphicViewerCompoundEdit = (GraphicViewerUndoManager.GraphicViewerCompoundEdit) paramUndoableEdit;
            Vector localVector = localGraphicViewerCompoundEdit.getAllEdits();
            int i;
            GraphicViewerDocumentChangedEdit localGraphicViewerDocumentChangedEdit;
            if (paramBoolean) {
                for (i = 0; i < localVector.size(); i++) {
                    localGraphicViewerDocumentChangedEdit = (GraphicViewerDocumentChangedEdit) localVector
                            .get(i);
                    logChange(localGraphicViewerDocumentChangedEdit);
                }
            } else {
                for (i = localVector.size() - 1; i >= 0; i--) {
                    localGraphicViewerDocumentChangedEdit = (GraphicViewerDocumentChangedEdit) localVector
                            .get(i);
                    logChange(localGraphicViewerDocumentChangedEdit);
                }
            }
        }
    }

    public void logChange(GraphicViewerDocumentChangedEdit edit) {
        if (edit.getHint() == GraphicViewerDocumentEvent.INSERTED) {
            ApplicationFrame
                    .getInstance()
                    .getConsole()
                    .println(
                            getLogPrefix() + "inserted " + ID(edit.getObject()));
        } else if (edit.getHint() == GraphicViewerDocumentEvent.REMOVED) {
            ApplicationFrame
                    .getInstance()
                    .getConsole()
                    .println(getLogPrefix() + "removed " + ID(edit.getObject()));
        } else if (edit.getHint() == GraphicViewerDocumentEvent.CHANGED) {
            if (edit.getFlags() == GraphicViewerLink.ChangedFromPort) {
                ApplicationFrame
                        .getInstance()
                        .getConsole()
                        .println(
                                getLogPrefix()
                                        + "changed link's From port from: "
                                        + ID(edit.getOldValue()) + "  to: "
                                        + ID(edit.getNewValue()));
            } else if (edit.getFlags() == GraphicViewerLink.ChangedToPort) {
                ApplicationFrame
                        .getInstance()
                        .getConsole()
                        .println(
                                getLogPrefix()
                                        + "changed link's To port from: "
                                        + ID(edit.getOldValue()) + "  to: "
                                        + ID(edit.getNewValue()));
            } else if (edit.getFlags() == GraphicViewerObject.ChangedGeometry
                    && edit.getObject() instanceof GraphicViewerNode) {
                ApplicationFrame
                        .getInstance()
                        .getConsole()
                        .println(
                                getLogPrefix()
                                        + "changed BoundingRect from: "
                                        + RECT((Rectangle) edit.getOldValue())
                                        + "  to: "
                                        + RECT(((GraphicViewerObject) edit
                                                .getObject()).getBoundingRect()));
            }
        }
    }

    public String ID(Object obj) {
        if (obj == null) {
            return "(null)";
        } else if (obj instanceof GraphicViewerPort) {
            GraphicViewerPort port = (GraphicViewerPort) obj;
            if (port.getParentNode() != port) {
                return ID(port.getParentNode());
            } else {
                return port.toString();
            }
        } else if (obj instanceof IGraphicViewerLabeledPart) {
            IGraphicViewerLabeledPart part = (IGraphicViewerLabeledPart) obj;
            return part.getText();
        } else if (obj instanceof GraphicViewerLink) {
            GraphicViewerLink link = (GraphicViewerLink) obj;
            return "link from " + ID(link.getFromPort().getParentNode())
                    + " to " + ID(link.getToPort().getParentNode());
        }
        return "";
    }

    public String RECT(Rectangle r) {
        return Integer.toString(r.x) + "," + Integer.toString(r.y) + ","
                + Integer.toString(r.width) + "," + Integer.toString(r.height);
    }

    private String myLogPrefix = "";
}