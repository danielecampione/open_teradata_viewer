/*
 * Open Teradata Viewer ( graphic viewer )
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

package net.sourceforge.open_teradata_viewer.graphicviewer;

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

    public GraphicViewerDocumentChangedEdit(
            GraphicViewerDocumentEvent graphicviewerdocumentevent,
            GraphicViewerUndoManager graphicviewerundomanager, boolean flag) {
        _fldtry = graphicviewerundomanager;
        _fldif = flag;
        _fldbyte = (GraphicViewerDocument) graphicviewerdocumentevent
                .getSource();
        _fldint = graphicviewerdocumentevent.getHint();
        a = graphicviewerdocumentevent.getFlags();
        _fldfor = graphicviewerdocumentevent.getObject();
        _fldnew = graphicviewerdocumentevent.getPreviousValueInt();
        _fldcase = graphicviewerdocumentevent.getPreviousValue();
        _fldbyte.copyOldValueForUndo(this);
        _fldbyte.copyNewValueForRedo(this);
    }

    public void die() {
        super.die();
        _fldtry = null;
        _fldbyte = null;
        _fldfor = null;
        _fldcase = null;
        _fldchar = null;
    }

    public String toString() {
        String s;
        if (getObject() != null) {
            s = getObject().getClass().getName();
            s = s.substring(s.lastIndexOf('.') + 1);
        } else {
            s = "(null)";
        }
        return (isBeforeChanging() ? "B " : "") + Integer.toString(getHint())
                + ": " + Integer.toString(getFlags()) + " " + s + " "
                + Integer.toString(getOldValueInt()) + "/"
                + (getOldValue() == null ? "null" : getOldValue().toString())
                + " --> " + Integer.toString(getNewValueInt()) + "/"
                + (getNewValue() == null ? "null" : getNewValue().toString());
    }

    public String getPresentationName() {
        return Integer.toString(getHint());
    }

    public boolean canUndo() {
        return super.canUndo() && _fldbyte != null;
    }

    public boolean canRedo() {
        return super.canRedo() && _fldbyte != null;
    }

    public void undo() throws CannotUndoException {
        super.undo();
        if (!isBeforeChanging())
            _fldbyte.changeValue(this, true);
    }

    public void redo() throws CannotRedoException {
        super.redo();
        if (!isBeforeChanging())
            _fldbyte.changeValue(this, false);
    }

    public boolean isBeforeChanging() {
        return _fldif;
    }

    @SuppressWarnings("rawtypes")
    public GraphicViewerDocumentChangedEdit findBeforeChangingEdit() {
        if (isBeforeChanging())
            return null;
        Vector vector = getUndoManager().getCurrentEditVector();
        for (int i = vector.size() - 1; i >= 0; i--) {
            UndoableEdit undoableedit = (UndoableEdit) vector.elementAt(i);
            if (!(undoableedit instanceof GraphicViewerDocumentChangedEdit))
                continue;
            GraphicViewerDocumentChangedEdit graphicviewerdocumentchangededit = (GraphicViewerDocumentChangedEdit) undoableedit;
            if (graphicviewerdocumentchangededit.isBeforeChanging()
                    && graphicviewerdocumentchangededit.getDoc() == getDoc()
                    && graphicviewerdocumentchangededit.getHint() == getHint()
                    && graphicviewerdocumentchangededit.getFlags() == getFlags()
                    && graphicviewerdocumentchangededit.getObject() == getObject())
                return graphicviewerdocumentchangededit;
        }

        return null;
    }

    public GraphicViewerUndoManager getUndoManager() {
        return _fldtry;
    }

    public GraphicViewerDocument getDoc() {
        return _fldbyte;
    }

    public int getHint() {
        return _fldint;
    }

    public int getFlags() {
        return a;
    }

    public Object getObject() {
        return _fldfor;
    }

    public int getValueInt(boolean flag) {
        if (flag)
            return getOldValueInt();
        else
            return getNewValueInt();
    }

    public boolean getValueBoolean(boolean flag) {
        if (flag)
            return getOldValueBoolean();
        else
            return getNewValueBoolean();
    }

    public Object getValue(boolean flag) {
        if (flag)
            return getOldValue();
        else
            return getNewValue();
    }

    public double getValueDouble(boolean flag) {
        if (flag)
            return getOldValueDouble();
        else
            return getNewValueDouble();
    }

    public int getOldValueInt() {
        return _fldnew;
    }

    public void setOldValueInt(int i) {
        _fldnew = i;
    }

    public boolean getOldValueBoolean() {
        return _fldnew != 0;
    }

    public void setOldValueBoolean(boolean flag) {
        _fldnew = flag ? 1 : 0;
    }

    public Object getOldValue() {
        return _fldcase;
    }

    public void setOldValue(Object obj) {
        _fldcase = obj;
    }

    public double getOldValueDouble() {
        return ((Double) _fldcase).doubleValue();
    }

    public void setOldValueDouble(double d) {
        _fldcase = new Double(d);
    }

    public int getNewValueInt() {
        return _flddo;
    }

    public void setNewValueInt(int i) {
        _flddo = i;
    }

    public boolean getNewValueBoolean() {
        return _flddo != 0;
    }

    public void setNewValueBoolean(boolean flag) {
        _flddo = flag ? 1 : 0;
    }

    public Object getNewValue() {
        return _fldchar;
    }

    public void setNewValue(Object obj) {
        _fldchar = obj;
    }

    public double getNewValueDouble() {
        return ((Double) _fldchar).doubleValue();
    }

    public void setNewValueDouble(double d) {
        _fldchar = new Double(d);
    }

    private GraphicViewerUndoManager _fldtry;
    private boolean _fldif;
    private GraphicViewerDocument _fldbyte;
    private int _fldint;
    private int a;
    private Object _fldfor;
    private int _fldnew;
    private Object _fldcase;
    private int _flddo;
    private Object _fldchar;
}