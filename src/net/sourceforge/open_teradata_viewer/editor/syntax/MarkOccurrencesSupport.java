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
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * Marks all occurrences of the token at the current caret position, if it is an
 * identifier.
 *
 * @author D. Campione
 * 
 */
class MarkOccurrencesSupport implements CaretListener, ActionListener {

    private SyntaxTextArea textArea;
    private Timer timer;
    private MarkOccurrencesHighlightPainter p;
    private List<Object> tags;

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
        p = new MarkOccurrencesHighlightPainter();
        setColor(color);
        tags = new ArrayList<Object>();
    }

    /**
     * Called after the caret has been moved and a fixed time delay has elapsed.
     * This locates and highlights all occurrences of the identifier at the
     * caret position, if any.
     *
     * @param e The event.
     */
    public void actionPerformed(ActionEvent e) {
        // Don't do anything if they are selecting text
        Caret c = textArea.getCaret();
        if (c.getDot() != c.getMark()) {
            return;
        }

        SyntaxDocument doc = (SyntaxDocument) textArea.getDocument();
        doc.readLock();
        try {

            // Remove old highlights
            removeHighlights();

            // Get the token at the caret position
            int line = textArea.getCaretLineNumber();
            Token tokenList = textArea.getTokenListForLine(line);
            int dot = c.getDot();
            Token t = SyntaxUtilities.getTokenAtOffset(tokenList, dot);
            if (t == null /* EOL */|| !isValidType(t) || isNonWordChar(t)) {
                // Try to the "left" of the caret
                dot--;
                try {
                    if (dot >= textArea.getLineStartOffset(line)) {
                        t = SyntaxUtilities.getTokenAtOffset(tokenList, dot);
                    }
                } catch (BadLocationException ble) {
                    ExceptionDialog.notifyException(ble); // Never happens
                }
            }

            // Add new highlights if an identifier is selected
            if (t != null && isValidType(t) && !isNonWordChar(t)) {
                SyntaxTextAreaHighlighter h = (SyntaxTextAreaHighlighter) textArea
                        .getHighlighter();
                char[] lexeme = t.getLexeme().toCharArray();
                int type = t.type;
                for (int i = 0; i < textArea.getLineCount(); i++) {
                    Token temp = textArea.getTokenListForLine(i);
                    while (temp != null && temp.isPaintable()) {
                        if (temp.is(type, lexeme)) {
                            try {
                                int end = temp.offset + temp.textCount;
                                Object tag = h.addMarkedOccurrenceHighlight(
                                        temp.offset, end, p);
                                tags.add(tag);
                            } catch (BadLocationException ble) {
                                ExceptionDialog.notifyException(ble); // Never happens
                            }
                        }
                        temp = temp.getNextToken();
                    }
                }
            }

        } finally {
            doc.readUnlock();
        }

        textArea.fireMarkedOccurrencesChanged();

    }

    /**
     * Called when the caret moves in the text area.
     *
     * @param e The event.
     */
    public void caretUpdate(CaretEvent e) {
        timer.restart();
    }

    /**
     * Returns the color being used to mark occurrences.
     *
     * @return The color being used.
     * @see #setColor(Paint)
     */
    public Color getColor() {
        return p.getColor();
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

    /**
     * Returns whether the specified token is a single non-word char (e.g. not
     * in <tt>[A-Za-z]</tt>. This is a HACK to work around the fact that many
     * standard token makers return things like semicolons and periods as {@link
     * Token#IDENTIFIER}s just to make the syntax highlighting coloring look a
     * little better.
     * 
     * @param t The token to check. This cannot be <tt>null</tt>.
     * @return Whether the token is a single non-word char.
     */
    private static final boolean isNonWordChar(Token t) {
        return t.textCount == 1
                && !SyntaxUtilities.isLetter(t.text[t.textOffset]);
    }

    /**
     * Returns whether the specified token is a type that we can do a "mark
     * occurrences" on.
     *
     * @param t The token.
     * @return Whether we should mark all occurrences of this token.
     */
    private boolean isValidType(Token t) {
        return textArea.getMarkOccurrencesOfTokenType(t.type);
    }

    /** Removes all highlights added to the text area by this listener. */
    private void removeHighlights() {
        if (textArea != null) {
            SyntaxTextAreaHighlighter h = (SyntaxTextAreaHighlighter) textArea
                    .getHighlighter();
            for (int i = 0; i < tags.size(); i++) {
                h.removeMarkOccurrencesHighlight(tags.get(i));
            }
        }
        tags.clear();
    }

    /**
     * Sets the color to use when marking occurrences.
     *
     * @param color The color to use.
     * @see #getColor()
     * @see #setPaintBorder(boolean)
     */
    public void setColor(Color color) {
        p.setColor(color);
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