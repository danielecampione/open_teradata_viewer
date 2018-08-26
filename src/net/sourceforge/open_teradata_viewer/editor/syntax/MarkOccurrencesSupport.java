/*
 * Open Teradata Viewer ( editor syntax )
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

package net.sourceforge.open_teradata_viewer.editor.syntax;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Caret;

import net.sourceforge.open_teradata_viewer.editor.SmartHighlightPainter;

/**
 * Marks all occurrences of the token at the current caret position, if it is an
 * identifier.
 *
 * @author D. Campione
 * @see IOccurrenceMarker
 * 
 */
class MarkOccurrencesSupport implements CaretListener, ActionListener {

    private SyntaxTextArea textArea;
    private Timer timer;
    private SmartHighlightPainter p;

    /** The default color used to mark occurrences. */
    public static final Color DEFAULT_COLOR = new Color(224, 224, 224);

    /** The default delay. */
    private static final int DEFAULT_DELAY_MS = 1000;

    /** Ctor. Creates a listener with a 1 second delay. */
    public MarkOccurrencesSupport() {
        this(DEFAULT_DELAY_MS);
    }

    /**
     * Ctor.
     *
     * @param delay The delay between when the caret last moves and when the
     *              text should be scanned for matching occurrences. This should
     *              be in milliseconds.
     */
    public MarkOccurrencesSupport(int delay) {
        this(delay, DEFAULT_COLOR);
    }

    /**
     * Ctor.
     *
     * @param delay The delay between when the caret last moves and when the
     *              text should be scanned for matching occurrences. This should
     *              be in milliseconds.
     * @param color The color to use to mark the occurrences. This cannot be
     *              <code>null</code>.
     */
    public MarkOccurrencesSupport(int delay, Color color) {
        timer = new Timer(delay, this);
        timer.setRepeats(false);
        p = new SmartHighlightPainter();
        setColor(color);
    }

    /**
     * Called after the caret has been moved and a fixed time delay has elapsed.
     * This locates and highlights all occurrences of the identifier at the
     * caret position, if any.
     *
     * @param e The event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Don't do anything if they are selecting text
        Caret c = textArea.getCaret();
        if (c.getDot() != c.getMark()) {
            return;
        }

        SyntaxDocument doc = (SyntaxDocument) textArea.getDocument();
        IOccurrenceMarker occurrenceMarker = doc.getOccurrenceMarker();
        boolean occurrencesChanged = false;

        if (occurrenceMarker != null) {
            doc.readLock();
            try {
                IToken t = occurrenceMarker.getTokenToMark(textArea);

                if (t != null && occurrenceMarker.isValidType(textArea, t)
                        && !SyntaxUtilities.isNonWordChar(t)) {
                    removeHighlights();
                    SyntaxTextAreaHighlighter h = (SyntaxTextAreaHighlighter) textArea
                            .getHighlighter();
                    occurrenceMarker.markOccurrences(doc, t, h, p);
                    occurrencesChanged = true;
                }
            } finally {
                doc.readUnlock();
            }
        }

        if (occurrencesChanged) {
            textArea.fireMarkedOccurrencesChanged();
        }
    }

    /**
     * Called when the caret moves in the text area.
     *
     * @param e The event.
     */
    @Override
    public void caretUpdate(CaretEvent e) {
        timer.restart();
    }

    /**
     * Returns the color being used to mark occurrences.
     *
     * @return The color being used.
     * @see #setColor(Color)
     */
    public Color getColor() {
        return (Color) p.getPaint();
    }

    /**
     * Returns the delay, in milliseconds.
     *
     * @return The delay.
     * @see #setDelay(int)
     */
    public int getDelay() {
        return timer.getDelay();
    }

    /**
     * Returns whether a border is painted around marked occurrences.
     *
     * @return Whether a border is painted.
     * @see #setPaintBorder(boolean)
     * @see #getColor()
     */
    public boolean getPaintBorder() {
        return p.getPaintBorder();
    }

    /**
     * Installs this listener on a text area. If it is already installed on
     * another text area, it is uninstalled first.
     *
     * @param textArea The text area to install on.
     */
    public void install(SyntaxTextArea textArea) {
        if (this.textArea != null) {
            uninstall();
        }
        this.textArea = textArea;
        textArea.addCaretListener(this);
        if (textArea.getMarkOccurrencesColor() != null) {
            setColor(textArea.getMarkOccurrencesColor());
        }
    }

    /** Removes all highlights added to the text area by this listener. */
    private void removeHighlights() {
        if (textArea != null) {
            SyntaxTextAreaHighlighter h = (SyntaxTextAreaHighlighter) textArea
                    .getHighlighter();
            h.clearMarkOccurrencesHighlights();
        }
    }

    /**
     * Sets the color to use when marking occurrences.
     *
     * @param color The color to use.
     * @see #getColor()
     * @see #setPaintBorder(boolean)
     */
    public void setColor(Color color) {
        p.setPaint(color);
        if (textArea != null) {
            removeHighlights();
            caretUpdate(null); // Force a highlight repaint
        }
    }

    /**
     * Sets the delay between the last caret position change and when the
     * text is scanned for matching identifiers. A delay is needed to prevent
     * repeated scanning while the user is typing.
     *
     * @param delay The new delay.
     * @see #getDelay()
     */
    public void setDelay(int delay) {
        timer.setDelay(delay);
    }

    /**
     * Toggles whether a border is painted around marked highlights.
     *
     * @param paint Whether to paint a border.
     * @see #getPaintBorder()
     * @see #setColor(Color)
     */
    public void setPaintBorder(boolean paint) {
        if (paint != p.getPaintBorder()) {
            p.setPaintBorder(paint);
            if (textArea != null) {
                textArea.repaint();
            }
        }
    }

    /**
     * Uninstalls this listener from the current text area. Does nothing if it
     * not currently installed on any text area.
     *
     * @see #install(SyntaxTextArea)
     */
    public void uninstall() {
        if (textArea != null) {
            removeHighlights();
            textArea.removeCaretListener(this);
        }
    }
}