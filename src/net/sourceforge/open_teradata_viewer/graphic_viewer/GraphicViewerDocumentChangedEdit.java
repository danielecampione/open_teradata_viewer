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

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class GraphicViewerDocumentChangedEdit extends AbstractUndoableEdit {

    private static final long serialVersionUID = 3488644327123099485L;

    private GraphicViewerUndoManager myUndoManager;
    private boolean myIsBeforeChanging;
    private GraphicViewerDocument myDoc;
    private int myHint;
    private int myFlags;
    private Object myObject;
    private int myOldValueInt;
    private Object myOldValue;
    private int myNewValueInt;
    private Object myNewValue;

    public GraphicViewerDocumentChangedEdit(
            GraphicViewerDocumentEvent graphicviewerdocumentevent,
            GraphicViewerUndoManager graphicviewerundomanager, boolean flag) {
        myUndoManager = graphicviewerundomanager;
        myIsBeforeChanging = flag;
        myDoc = (GraphicViewerDocument) graphicviewerdocumentevent.getSource();
        myHint = graphicviewerdocumentevent.getHint();
        myFlags = graphicviewerdocumentevent.getFlags();
        myObject = graphicviewerdocumentevent.getObject();
        myOldValueInt = graphicviewerdocumentevent.getPreviousValueInt();
        myOldValue = graphicviewerdocumentevent.getPreviousValue();
        myDoc.copyOldValueForUndo(this);
        myDoc.copyNewValueForRedo(this);
    }

    public void die() {
        super.die();
        myUndoManager = null;
        myDoc = null;
        myObject = null;
        myOldValue = null;
        myNewValue = null;
    }

    public String toString() {
        String str;
        if (getObject() != null) {
            str = getObject().getClass().getName();
            str = str.substring(str.lastIndexOf('.') + 1);
        } else {
            str = "(null)";
        }
        return new StringBuilder()
                .append(isBeforeChanging() ? "B " : "")
                .append(Integer.toString(getHint()))
                .append(": ")
                .append(Integer.toString(getFlags()))
                .append(" ")
                .append(str)
                .append(" ")
                .append(Integer.toString(getOldValueInt()))
                .append("/")
                .append(getOldValue() != null
                        ? getOldValue().toString()
                        : "null")
                .append(" --> ")
                .append(Integer.toString(getNewValueInt()))
                .append("/")
                .append(getNewValue() != null
                        ? getNewValue().toString()
                        : "null").toString();
    }

    public String getPresentationName() {
        return Integer.toString(getHint());
    }

    public boolean canUndo() {
        return super.canUndo() && myDoc != null;
    }

    public boolean canRedo() {
        return super.canRedo() && myDoc != null;
    }

    public void undo() throws CannotUndoException {
        super.undo();
        if (!isBeforeChanging()) {
            myDoc.changeValue(this, true);
        }
    }

    public void redo() throws CannotRedoException {
        super.redo();
        if (!isBeforeChanging()) {
            myDoc.changeValue(this, false);
        }
    }

    public boolean isBeforeChanging() {
        return myIsBeforeChanging;
    }

    public GraphicViewerDocumentChangedEdit findBeforeChangingEdit() {
        if (isBeforeChanging()) {
            return null;
        }
        Vector vector = getUndoManager().getCurrentEditVector();
        for (int i = vector.size() - 1; i >= 0; i--) {
            UndoableEdit undoableedit = (UndoableEdit) vector.elementAt(i);
            if (!(undoableedit instanceof GraphicViewerDocumentChangedEdit)) {
                continue;
            }
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit = (GraphicViewerDocumentChangedEdit) undoableedit;
            if (graphicviewerdocumentchangededit.isBeforeChanging()
                    && graphicviewerdocumentchangededit.getDoc() == getDoc()
                    && graphicviewerdocumentchangededit.getHint() == getHint()
                    && graphicviewerdocumentchangededit.getFlags() == getFlags()
                    && graphicviewerdocumentchangededit.getObject() == getObject()) {
                return graphicviewerdocumentchangededit;
            }
        }

        return null;
    }

    public GraphicViewerUndoManager getUndoManager() {
        return myUndoManager;
    }

    public GraphicViewerDocument getDoc() {
        return myDoc;
    }

    public int getHint() {
        return myHint;
    }

    public int getFlags() {
        return myFlags;
    }

    public Object getObject() {
        return myObject;
    }

    public int getValueInt(boolean flag) {
        if (flag) {
            return getOldValueInt();
        } else {
            return getNewValueInt();
        }
    }

    public boolean getValueBoolean(boolean flag) {
        if (flag) {
            return getOldValueBoolean();
        } else {
            return getNewValueBoolean();
        }
    }

    public Object getValue(boolean flag) {
        if (flag) {
            return getOldValue();
        } else {
            return getNewValue();
        }
    }

    public double getValueDouble(boolean flag) {
        if (flag) {
            return getOldValueDouble();
        } else {
            return getNewValueDouble();
        }
    }

    public int getOldValueInt() {
        return myOldValueInt;
    }

    public void setOldValueInt(int i) {
        myOldValueInt = i;
    }

    public boolean getOldValueBoolean() {
        return myOldValueInt != 0;
    }

    public void setOldValueBoolean(boolean flag) {
        myOldValueInt = flag ? 1 : 0;
    }

    public Object getOldValue() {
        return myOldValue;
    }

    public void setOldValue(Object obj) {
        myOldValue = obj;
    }

    public double getOldValueDouble() {
        return ((Double) myOldValue).doubleValue();
    }

    public void setOldValueDouble(double d) {
        myOldValue = new Double(d);
    }

    public int getNewValueInt() {
        return myNewValueInt;
    }

    public void setNewValueInt(int i) {
        myNewValueInt = i;
    }

    public boolean getNewValueBoolean() {
        return myNewValueInt != 0;
    }

    public void setNewValueBoolean(boolean flag) {
        myNewValueInt = flag ? 1 : 0;
    }

    public Object getNewValue() {
        return myNewValue;
    }

    public void setNewValue(Object obj) {
        myNewValue = obj;
    }

    public double getNewValueDouble() {
        return ((Double) myNewValue).doubleValue();
    }

    public void setNewValueDouble(double d) {
        myNewValue = new Double(d);
    }
}