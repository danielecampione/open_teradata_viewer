/*
 * Open Teradata Viewer ( editor syntax )
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

package net.sourceforge.open_teradata_viewer.editor.syntax;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.Icon;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.IGutterIconInfo;
import net.sourceforge.open_teradata_viewer.editor.IconRowHeader;
import net.sourceforge.open_teradata_viewer.editor.syntax.folding.FoldManager;

/**
 * A row header component that takes code folding into account when painting
 * itself.
 *
 * @author D. Campione
 * 
 */
public class FoldingAwareIconRowHeader extends IconRowHeader {

    private static final long serialVersionUID = -7224956820746703224L;

    /**
     * Ctor.
     *
     * @param textArea The parent text area.
     */
    public FoldingAwareIconRowHeader(SyntaxTextArea textArea) {
        super(textArea);
    }

    /**
     * {@inheritDoc}
     */
    protected void paintComponent(Graphics g) {
        // When line wrap is not enabled, take the faster code path
        if (textArea == null) {
            return;
        }
        SyntaxTextArea sta = (SyntaxTextArea) textArea;
        FoldManager fm = sta.getFoldManager();
        if (!fm.isCodeFoldingSupportedAndEnabled()) {
            super.paintComponent(g);
            return;
        }

        visibleRect = g.getClipBounds(visibleRect);
        if (visibleRect == null) {
            visibleRect = getVisibleRect();
        }
        if (visibleRect == null) {
            return;
        }

        g.setColor(getBackground());
        g.fillRect(0, visibleRect.y, width, visibleRect.height);

        if (textArea.getLineWrap()) {
            paintComponentWrapped(g);
            return;
        }

        Document doc = textArea.getDocument();
        Element root = doc.getDefaultRootElement();
        textAreaInsets = textArea.getInsets(textAreaInsets);

        // Get the first line to paint
        int cellHeight = textArea.getLineHeight();
        int topLine = (visibleRect.y - textAreaInsets.top) / cellHeight;

        // Get where to start painting (top of the row). We need to be "scrolled
        // up" up just enough for the missing part of the first line
        int y = topLine * cellHeight + textAreaInsets.top;

        // AFTER calculating visual offset to paint at, account for folding.
        topLine += fm.getHiddenLineCountAbove(topLine, true);

        // Paint the active line range
        if (activeLineRangeStart > -1 && activeLineRangeEnd > -1) {
            Color activeLineRangeColor = getActiveLineRangeColor();
            g.setColor(activeLineRangeColor);
            try {
                int y1 = sta.yForLine(activeLineRangeStart);
                if (y1 > -1) { // Not in a collapsed fold..
                    int y2 = sta.yForLine(activeLineRangeEnd);
                    if (y2 == -1) { // In a collapsed fold
                        y2 = y1;
                    }
                    y2 += cellHeight - 1;

                    int j = y1;
                    while (j <= y2) {
                        int yEnd = Math.min(y2, j + getWidth());
                        int xEnd = yEnd - j;
                        g.drawLine(0, j, xEnd, yEnd);
                        j += 2;
                    }

                    int i = 2;
                    while (i < getWidth()) {
                        int yEnd = y1 + getWidth() - i;
                        g.drawLine(i, y1, getWidth(), yEnd);
                        i += 2;
                    }

                    if (y1 >= y && y1 < y + visibleRect.height) {
                        g.drawLine(0, y1, getWidth(), y1);
                    }
                    if (y2 >= y && y2 < y + visibleRect.height) {
                        g.drawLine(0, y2, getWidth(), y2);
                    }
                }
            } catch (BadLocationException ble) {
                ExceptionDialog.notifyException(ble); // Never happens
            }
        }

        // Paint icons
        if (trackingIcons != null) {
            int lastLine = textArea.getLineCount() - 1;
            for (int i = trackingIcons.size() - 1; i >= 0; i--) { // Last to first
                IGutterIconInfo ti = getTrackingIcon(i);
                int offs = ti.getMarkedOffset();
                if (offs >= 0 && offs <= doc.getLength()) {
                    int line = root.getElementIndex(offs);
                    if (line <= lastLine && line >= topLine) {
                        try {
                            Icon icon = ti.getIcon();
                            if (icon != null) {
                                int lineY = sta.yForLine(line);
                                if (lineY >= y
                                        && lineY <= visibleRect.y
                                                + visibleRect.height) {
                                    int y2 = lineY
                                            + (cellHeight - icon
                                                    .getIconHeight()) / 2;
                                    icon.paintIcon(this, g, 0, y2);
                                    lastLine = line - 1; // Paint only 1 icon per line
                                }
                            }
                        } catch (BadLocationException ble) {
                            ExceptionDialog.hideException(ble); // Never happens
                        }
                    } else if (line < topLine) {
                        break; // All other lines are above us, so quit now
                    }
                }
            }
        }
    }

    /**
     * Paints icons when line wrapping is enabled. Note that this does not
     * override the parent class's implementation to avoid this version being
     * called when line wrapping is disabled.
     */
    private void paintComponentWrapped(Graphics g) {
        // The variables we use are as follows:
        // - visibleRect is the "visible" area of the text area; e.g. [0,100,
        // 300,100+(lineCount*cellHeight)-1].
        // actualTop.y is the topmost-pixel in the first logical line we paint.
        // Note that we may well not paint this part of the logical line, as it
        // may be broken into many physical lines, with the first few physical
        // lines scrolled past. Note also that this is NOT the visible rect of
        // this line number list; this line number list has visible rect ==
        // [0,0, insets.left-1,visibleRect.height-1]

        // We avoid using modelToView/viewToModel where possible, as these
        // methods trigger a parsing of the line into syntax tokens, which is
        // costly. It's cheaper to just grab the child views' bounds

        SyntaxTextArea sta = (SyntaxTextArea) textArea;
        Document doc = textArea.getDocument();
        Element root = doc.getDefaultRootElement();
        int topPosition = textArea.viewToModel(new Point(visibleRect.x,
                visibleRect.y));
        int topLine = root.getElementIndex(topPosition);

        int topY = visibleRect.y;
        int bottomY = visibleRect.y + visibleRect.height;
        int cellHeight = textArea.getLineHeight();

        // Paint icons
        if (trackingIcons != null) {
            int lastLine = textArea.getLineCount() - 1;
            for (int i = trackingIcons.size() - 1; i >= 0; i--) { // Last to first
                IGutterIconInfo ti = getTrackingIcon(i);
                int offs = ti.getMarkedOffset();
                if (offs >= 0 && offs <= doc.getLength()) {
                    int line = root.getElementIndex(offs);
                    if (line <= lastLine && line >= topLine) {
                        try {
                            int lineY = sta.yForLine(line);
                            if (lineY >= topY && lineY < bottomY) {
                                Icon icon = ti.getIcon();
                                if (icon != null) {
                                    int y2 = lineY
                                            + (cellHeight - icon
                                                    .getIconHeight()) / 2;
                                    ti.getIcon().paintIcon(this, g, 0, y2);
                                    lastLine = line - 1; // Paint only 1 icon per line
                                }
                            }
                        } catch (BadLocationException ble) {
                            ExceptionDialog.notifyException(ble); // Never happens
                        }
                    } else if (line < topLine) {
                        break; // All other lines are above us, so quit now
                    }
                }
            }
        }
    }
}