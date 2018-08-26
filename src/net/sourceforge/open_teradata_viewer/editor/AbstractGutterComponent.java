/*
 * Open Teradata Viewer ( editor )
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

package net.sourceforge.open_teradata_viewer.editor;

import java.awt.Container;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.text.View;

/**
 * A component that can be displayed in a {@link Gutter}.
 *
 * @author D. Campione
 *
 */
abstract class AbstractGutterComponent extends JPanel {

    private static final long serialVersionUID = 856428045496761886L;

    /** The text area whose lines we are marking with icons. */
    protected TextArea textArea;

    /** The number of lines in the text area. */
    protected int currentLineCount;

    /**
     * Ctor.
     *
     * @param textArea The text area.
     */
    public AbstractGutterComponent(TextArea textArea) {
        init(); // Called before setTextArea()
        setTextArea(textArea);
    }

    /**
     * Returns the bounds of a child view as a rectangle, since
     * <code>View</code>s tend to use <code>Shape</code>.
     *
     * @param parent The parent view of the child whose bounds we're getting.
     * @param line The index of the child view.
     * @param editorRect Returned from the text area's
     *                   <code>getVisibleEditorRect</code> method.
     * @return The child view's bounds.
     */
    protected static final Rectangle getChildViewBounds(View parent, int line,
            Rectangle editorRect) {
        Shape alloc = parent.getChildAllocation(line, editorRect);
        if (alloc == null) {
            // WrappedSyntaxView can have this when made so small it's no longer
            // visible
            return new Rectangle();
        }
        return alloc instanceof Rectangle ? (Rectangle) alloc : alloc
                .getBounds();
    }

    /**
     * Returns the parent <code>Gutter</code> component.
     *
     * @return The parent <code>Gutter</code>.
     */
    protected Gutter getGutter() {
        Container parent = getParent();
        return (parent instanceof Gutter) ? (Gutter) parent : null;
    }

    /**
     * Called when text is inserted to or removed from the text area.
     * Implementations can take this opportunity to repaint, revalidate, etc..
     *
     * @param e The document event.
     */
    abstract void handleDocumentEvent(DocumentEvent e);

    /**
     * Called by the constructor before the text area is set. This is a hook to
     * allow subclasses to do any needed initialization. The default
     * implementation does nothing.
     */
    protected void init() {
    }

    /**
     * Called when the line heights of the text area change. This is usually the
     * result of one or more of the fonts in the editor changing.
     */
    abstract void lineHeightsChanged();

    /**
     * Sets the text area being displayed. Subclasses can override, but should
     * call the super implementation.
     *
     * @param textArea The text area.
     */
    public void setTextArea(TextArea textArea) {
        this.textArea = textArea;
        int lineCount = textArea == null ? 0 : textArea.getLineCount();
        if (currentLineCount != lineCount) {
            currentLineCount = lineCount;
            repaint();
        }
    }
}