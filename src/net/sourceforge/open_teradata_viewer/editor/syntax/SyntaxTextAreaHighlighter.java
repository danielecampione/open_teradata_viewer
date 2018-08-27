/*
 * Open Teradata Viewer ( editor syntax )
 * Copyright (C) 2015, D. Campione
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
import java.awt.Shape;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.plaf.TextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;

import net.sourceforge.open_teradata_viewer.editor.SmartHighlightPainter;
import net.sourceforge.open_teradata_viewer.editor.TextAreaHighlighter;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.IParser;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.IParserNotice;

/**
 * The highlighter implementation used by {@link SyntaxTextArea}s. It knows to
 * always paint "marked occurrences" highlights below selection highlights, and
 * squiggle underline highlights above all other highlights.<p>
 *
 * Most of this code is copied from javax.swing.text.DefaultHighlighter;
 * unfortunately, we cannot re-use much of it since it is package private.
 *
 * @author D. Campione
 * 
 */
public class SyntaxTextAreaHighlighter extends TextAreaHighlighter {

    /**
     * Marked occurrences in the document (to be painted separately from other
     * highlights).
     */
    private List<SyntaxLayeredHighlightInfoImpl> markedOccurrences;

    /**
     * Highlights from document parsers. These should be painted "on top of" all
     * other highlights to ensure they are always above the selection.
     */
    private List<SyntaxLayeredHighlightInfoImpl> parserHighlights;

    /** The default color used for parser notices when none is specified. */
    private static final Color DEFAULT_PARSER_NOTICE_COLOR = Color.RED;

    /** Ctor. */
    public SyntaxTextAreaHighlighter() {
        markedOccurrences = new ArrayList<SyntaxLayeredHighlightInfoImpl>();
        parserHighlights = new ArrayList<SyntaxLayeredHighlightInfoImpl>(0); // Often unused
    }

    /**
     * Adds a special "marked occurrence" highlight.
     *
     * @return A tag to reference the highlight later.
     * @throws BadLocationException
     * @see #clearMarkOccurrencesHighlights()
     */
    Object addMarkedOccurrenceHighlight(int start, int end,
            SmartHighlightPainter p) throws BadLocationException {
        Document doc = textArea.getDocument();
        TextUI mapper = textArea.getUI();
        // Always layered highlights for marked occurrences.
        SyntaxLayeredHighlightInfoImpl i = new SyntaxLayeredHighlightInfoImpl();
        i.setPainter(p);
        i.setStartOffset(doc.createPosition(start));
        // HACK: Use "end-1" to prevent chars the user types at the "end" of the
        // highlight to be absorbed into the highlight (default Highlight
        // behavior)
        i.setEndOffset(doc.createPosition(end - 1));
        markedOccurrences.add(i);
        mapper.damageRange(textArea, start, end);
        return i;
    }

    /**
     * Adds a highlight from a parser.
     *
     * @param notice The notice from a {@link IParser}.
     * @return A tag with which to reference the highlight.
     * @throws BadLocationException
     * @see #clearParserHighlights()
     * @see #clearParserHighlights(Parser)
     */
    IHighlightInfo addParserHighlight(IParserNotice notice, HighlightPainter p)
            throws BadLocationException {
        Document doc = textArea.getDocument();
        TextUI mapper = textArea.getUI();

        int start = notice.getOffset();
        int end = 0;
        if (start == -1) { // Could just define an invalid line number
            int line = notice.getLine();
            Element root = doc.getDefaultRootElement();
            if (line >= 0 && line < root.getElementCount()) {
                Element elem = root.getElement(line);
                start = elem.getStartOffset();
                end = elem.getEndOffset();
            }
        } else {
            end = start + notice.getLength();
        }

        // Always layered highlights for parser highlights
        SyntaxLayeredHighlightInfoImpl i = new SyntaxLayeredHighlightInfoImpl();
        i.setPainter(p);
        i.setStartOffset(doc.createPosition(start));
        // HACK: Use "end-1" to prevent chars the user types at the "end" of the
        // highlight to be absorbed into the highlight (default Highlight
        // behavior)
        i.setEndOffset(doc.createPosition(end - 1));
        i.notice = notice;//i.color = notice.getColor();

        parserHighlights.add(i);
        mapper.damageRange(textArea, start, end);
        return i;
    }

    /**
     * Removes all "marked occurrences" highlights from the view.
     *
     * @see #addMarkedOccurrenceHighlight(int, int, SmartHighlightPainter)
     */
    void clearMarkOccurrencesHighlights() {
        // Don't remove via an iterator; since our List is an ArrayList, this
        // implies tons of System.arrayCopy()s
        for (IHighlightInfo info : markedOccurrences) {
            repaintListHighlight(info);
        }
        markedOccurrences.clear();
    }

    /**
     * Removes all parser highlights.
     *
     * @see #addParserHighlight(IParserNotice, javax.swing.text.Highlighter.HighlightPainter)
     */
    void clearParserHighlights() {
        // Don't remove via an iterator; since our List is an ArrayList, this
        // implies tons of System.arrayCopy()s 
        for (int i = 0; i < parserHighlights.size(); i++) {
            repaintListHighlight(parserHighlights.get(i));
        }
        parserHighlights.clear();
    }

    /**
     * Removes all of the highlights for a specific parser.
     *
     * @param parser The parser.
     */
    public void clearParserHighlights(IParser parser) {
        Iterator<SyntaxLayeredHighlightInfoImpl> i = parserHighlights
                .iterator();
        for (; i.hasNext();) {
            SyntaxLayeredHighlightInfoImpl info = i.next();

            if (info.notice.getParser() == parser) {
                if (info.width > 0 && info.height > 0) {
                    textArea.repaint(info.x, info.y, info.width, info.height);
                }
                i.remove();
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void deinstall(JTextComponent c) {
        super.deinstall(c);
        markedOccurrences.clear();
        parserHighlights.clear();
    }

    /**
     * Returns a list of "marked occurrences" in the text area. If there are no
     * marked occurrences, this will be an empty list.
     *
     * @return The list of marked occurrences, or an empty list if none. The
     *         contents of this list will be of type {@link DocumentRange}.
     */
    public List<DocumentRange> getMarkedOccurrences() {
        List<DocumentRange> list = new ArrayList<DocumentRange>(
                markedOccurrences.size());
        for (IHighlightInfo info : markedOccurrences) {
            int start = info.getStartOffset();
            int end = info.getEndOffset() + 1; // HACK
            DocumentRange range = new DocumentRange(start, end);
            list.add(range);
        }
        return list;
    }

    @Override
    public void paintLayeredHighlights(Graphics g, int lineStart, int lineEnd,
            Shape viewBounds, JTextComponent editor, View view) {
        paintListLayered(g, lineStart, lineEnd, viewBounds, editor, view,
                markedOccurrences);
        super.paintLayeredHighlights(g, lineStart, lineEnd, viewBounds, editor,
                view);
        paintListLayered(g, lineStart, lineEnd, viewBounds, editor, view,
                parserHighlights);
    }

    /**
     * Removes a parser highlight from this view.
     *
     * @param tag The reference to the highlight.
     * @see #addParserHighlight(IParserNotice, javax.swing.text.Highlighter.HighlightPainter)
     */
    void removeParserHighlight(IHighlightInfo tag) {
        repaintListHighlight(tag);
        parserHighlights.remove(tag);
    }

    /**
     * Highlight info implementation used for parser notices and marked
     * occurrences.
     * 
     * @author D. Campione
     * 
     */
    private static class SyntaxLayeredHighlightInfoImpl extends
            LayeredHighlightInfoImpl {
        IParserNotice notice;//Color color; // Used only by Parser highlights

        @Override
        public Color getColor() {
            Color color = null;
            if (notice != null) {
                color = notice.getColor();
                if (color == null) {
                    color = DEFAULT_PARSER_NOTICE_COLOR;
                }
            }
            return color;
        }
    }
}