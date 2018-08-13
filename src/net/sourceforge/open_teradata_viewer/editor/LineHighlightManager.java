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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.text.BadLocationException;
import javax.swing.text.Position;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * Manages line highlights in an <code>TextArea</code>.
 *
 * @author D. Campione
 * 
 */
class LineHighlightManager {

    private TextArea textArea;
    private List<LineHighlightInfo> lineHighlights;

    /**
     * Ctor.
     *
     * @param textArea The parent text area.
     */
    public LineHighlightManager(TextArea textArea) {
        this.textArea = textArea;
    }

    /**
     * Highlights the specified line.
     *
     * @param line The line to highlight.
     * @param color The color to highlight with.
     * @return A tag for the highlight.
     * @throws BadLocationException If <code>line</code> is not a valid line
     *         number.
     * @see #removeLineHighlight(Object)
     */
    public Object addLineHighlight(int line, Color color)
            throws BadLocationException {
        int offs = textArea.getLineStartOffset(line);
        LineHighlightInfo lhi = new LineHighlightInfo(textArea.getDocument()
                .createPosition(offs), color);
        if (lineHighlights == null) {
            lineHighlights = new ArrayList<LineHighlightInfo>(1);
        }
        int index = Collections.binarySearch(lineHighlights, lhi);
        if (index < 0) { // Common case
            index = -(index + 1);
        }
        lineHighlights.add(index, lhi);
        repaintLine(lhi);
        return lhi;
    }

    /**
     * Paints any highlighted lines in the specified line range.
     *
     * @param g The graphics context.
     */
    public void paintLineHighlights(Graphics g) {
        int count = lineHighlights == null ? 0 : lineHighlights.size();
        if (count > 0) {
            int docLen = textArea.getDocument().getLength();
            Rectangle vr = textArea.getVisibleRect();
            int lineHeight = textArea.getLineHeight();

            try {
                for (int i = 0; i < count; i++) {
                    LineHighlightInfo lhi = lineHighlights.get(i);
                    int offs = lhi.getOffset();
                    if (offs >= 0 && offs <= docLen) {
                        int y = textArea.yForLineContaining(offs);
                        if (y > vr.y - lineHeight) {
                            if (y < vr.y + vr.height) {
                                g.setColor(lhi.getColor());
                                g.fillRect(0, y, textArea.getWidth(),
                                        lineHeight);
                            } else {
                                break; // Out of visible rect
                            }
                        }
                    }
                }
            } catch (BadLocationException ble) {
                ExceptionDialog.notifyException(ble); // Never happens
            }
        }
    }

    /**
     * Removes all line highlights.
     *
     * @see #removeLineHighlight(Object)
     */
    public void removeAllLineHighlights() {
        if (lineHighlights != null) {
            lineHighlights.clear();
            textArea.repaint();
        }
    }

    /**
     * Removes a line highlight.
     *
     * @param tag The tag of the line highlight to remove.
     * @see #addLineHighlight(int, Color)
     */
    public void removeLineHighlight(Object tag) {
        if (tag instanceof LineHighlightInfo) {
            lineHighlights.remove(tag);
            repaintLine((LineHighlightInfo) tag);
        }
    }

    /**
     * Repaints the line pointed to by the specified highlight information.
     *
     * @param lhi The highlight information.
     */
    private void repaintLine(LineHighlightInfo lhi) {
        int offs = lhi.getOffset();
        // May be > length if they deleted text including the highlight
        if (offs >= 0 && offs <= textArea.getDocument().getLength()) {
            try {
                int y = textArea.yForLineContaining(offs);
                if (y > -1) {
                    textArea.repaint(0, y, textArea.getWidth(),
                            textArea.getLineHeight());
                }
            } catch (BadLocationException ble) {
                ExceptionDialog.notifyException(ble); // Never happens
            }
        }
    }

    /**
     * Information about a line highlight.
     *
     * @author D. Campione
     * 
     */
    private static class LineHighlightInfo implements
            Comparable<LineHighlightInfo> {
        private Position offs;
        private Color color;

        public LineHighlightInfo(Position offs, Color c) {
            this.offs = offs;
            this.color = c;
        }

        @Override
        public int compareTo(LineHighlightInfo o) {
            if (o != null) {
                return offs.getOffset() - o.getOffset();
            }
            return -1;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof LineHighlightInfo) {
                return offs.getOffset() == ((LineHighlightInfo) o).getOffset();
            }
            return false;
        }

        public Color getColor() {
            return color;
        }

        public int getOffset() {
            return offs.getOffset();
        }

        @Override
        public int hashCode() {
            return getOffset();
        }
    }
}