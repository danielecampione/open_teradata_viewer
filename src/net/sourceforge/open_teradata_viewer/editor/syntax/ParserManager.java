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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Timer;
import javax.swing.ToolTipManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Position;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.syntax.focusabletip.FocusableTip;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.IParseResult;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.IParser;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.IParserNotice;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.ToolTipInfo;

/**
 * Manages running a parser object for an <code>SyntaxTextArea</code>.
 *
 * @author D. Campione
 * 
 */
class ParserManager
        implements
            DocumentListener,
            ActionListener,
            HyperlinkListener {

    private SyntaxTextArea textArea;
    private List<IParser> iParsers;
    private Timer timer;
    private boolean running;
    private IParser parserForTip;
    private Position firstOffsetModded;
    private Position lastOffsetModded;

    /**
     * Mapping of notices to their highlights in the editor. Can't use a Map
     * since iParsers could return two <code>IParserNotice</code>s that compare
     * equally via <code>equals()</code>. Real-world example: The Perl compiler
     * will return 2+ identical error messages if the same error is committed in
     * a single line more than once.
     */
    private List<NoticeHighlightPair> noticeHighlightPairs;

    /** Painter used to underline errors. */
    private SquiggleUnderlineHighlightPainter parserErrorHighlightPainter = new SquiggleUnderlineHighlightPainter(
            Color.RED);

    /**
     * If this system property is set to <code>true</code>, debug messages will
     * be printed to stdout to help diagnose parsing issues.
     */
    private static final String PROPERTY_DEBUG_PARSING = "rsta.debugParsing";

    /** Whether to print debug messages while running iParsers. */
    private static final boolean DEBUG_PARSING;

    /**
     * The default delay between the last key press and when the document is
     * parsed, in milliseconds.
     */
    private static final int DEFAULT_DELAY_MS = 1250;

    /**
     * Ctor.
     *
     * @param textArea The text area whose document the parser will be parsing.
     */
    public ParserManager(SyntaxTextArea textArea) {
        this(DEFAULT_DELAY_MS, textArea);
    }

    /**
     * Ctor.
     *
     * @param delay The delay between the last key press and when the document
     *              is parsed.
     * @param textArea The text area whose document the parser will be parsing.
     */
    public ParserManager(int delay, SyntaxTextArea textArea) {
        this.textArea = textArea;
        textArea.getDocument().addDocumentListener(this);
        iParsers = new ArrayList<IParser>(1); // Usually small
        timer = new Timer(delay, this);
        timer.setRepeats(false);
        running = true;
    }

    /**
     * Called when the timer fires (e.g. it's time to parse the document).
     * 
     * @param e The event.
     */
    public void actionPerformed(ActionEvent e) {
        // Sanity check - should have >1 parser if event is fired
        int parserCount = getParserCount();
        if (parserCount == 0) {
            return;
        }

        long begin = 0;
        if (DEBUG_PARSING) {
            begin = System.currentTimeMillis();
        }

        SyntaxDocument doc = (SyntaxDocument) textArea.getDocument();

        Element root = doc.getDefaultRootElement();
        int firstLine = firstOffsetModded == null ? 0 : root
                .getElementIndex(firstOffsetModded.getOffset());
        int lastLine = lastOffsetModded == null
                ? root.getElementCount() - 1
                : root.getElementIndex(lastOffsetModded.getOffset());
        firstOffsetModded = lastOffsetModded = null;
        if (DEBUG_PARSING) {
            ApplicationFrame
                    .getInstance()
                    .getConsole()
                    .println(
                            "[DEBUG]: Minimum lines to parse: " + firstLine
                                    + "-" + lastLine + ".");
        }

        String style = textArea.getSyntaxEditingStyle();
        doc.readLock();
        try {
            for (int i = 0; i < parserCount; i++) {
                IParser iParser = getParser(i);
                if (iParser.isEnabled()) {
                    IParseResult res = iParser.parse(doc, style);
                    addParserNoticeHighlights(res);
                } else {
                    clearParserNoticeHighlights(iParser);
                }
            }
            textArea.fireParserNoticesChange();
        } finally {
            doc.readUnlock();
        }

        if (DEBUG_PARSING) {
            float time = (System.currentTimeMillis() - begin) / 1000f;
            ApplicationFrame.getInstance().getConsole()
                    .println("Total parsing time: " + time + " seconds.");
        }

    }

    /**
     * Adds a parser for the text area.
     *
     * @param iParser The new parser. If this is <code>null</code>, nothing
     *                happens.
     * @see #getParser(int)
     * @see #removeParser(IParser)
     */
    public void addParser(IParser iParser) {
        if (iParser != null && !iParsers.contains(iParser)) {
            if (running) {
                timer.stop();
            }
            iParsers.add(iParser);
            if (iParsers.size() == 1) {
                // Okay to call more than once
                ToolTipManager.sharedInstance().registerComponent(textArea);
            }
            if (running) {
                timer.restart();
            }
        }
    }

    /**
     * Adds highlights for a list of parser notices. Any current notices from
     * the same IParser, in the same parsed range, are removed.
     *
     * @param res The result of a parsing.
     * @see #clearParserNoticeHighlights()
     */
    private void addParserNoticeHighlights(IParseResult res) {
        if (DEBUG_PARSING) {
            ApplicationFrame
                    .getInstance()
                    .getConsole()
                    .println(
                            "[DEBUG]: Adding parser notices from "
                                    + res.getParser() + ".");
        }

        if (noticeHighlightPairs == null) {
            noticeHighlightPairs = new ArrayList<NoticeHighlightPair>();
        }

        removeParserNotices(res);

        List<?> notices = res.getNotices();
        if (notices.size() > 0) { // Guaranteed non-null
            SyntaxTextAreaHighlighter h = (SyntaxTextAreaHighlighter) textArea
                    .getHighlighter();

            for (Iterator<?> i = notices.iterator(); i.hasNext();) {
                IParserNotice notice = (IParserNotice) i.next();
                if (DEBUG_PARSING) {
                    ApplicationFrame.getInstance().getConsole()
                            .println("[DEBUG]: ... adding: " + notice + ".");
                }
                try {
                    Object highlight = null;
                    if (notice.getShowInEditor()) {
                        highlight = h.addParserHighlight(notice,
                                parserErrorHighlightPainter);
                    }
                    noticeHighlightPairs.add(new NoticeHighlightPair(notice,
                            highlight));
                } catch (BadLocationException ble) { // Never happens
                    ExceptionDialog.notifyException(ble);
                }
            }
        }

        if (DEBUG_PARSING) {
            ApplicationFrame
                    .getInstance()
                    .getConsole()
                    .println(
                            "[DEBUG]: Done adding parser notices from "
                                    + res.getParser() + ".");
        }
    }

    /**
     * Called when the document is modified.
     *
     * @param e The document event.
     */
    public void changedUpdate(DocumentEvent e) {
    }

    private void clearParserNoticeHighlights() {
        SyntaxTextAreaHighlighter h = (SyntaxTextAreaHighlighter) textArea
                .getHighlighter();
        if (h != null) {
            h.clearParserHighlights();
        }
        if (noticeHighlightPairs != null) {
            noticeHighlightPairs.clear();
        }
    }

    /**
     * Removes all parser notice highlights for a specific parser.
     *
     * @param iParser The parser whose highlights to remove.
     */
    private void clearParserNoticeHighlights(IParser iParser) {
        SyntaxTextAreaHighlighter h = (SyntaxTextAreaHighlighter) textArea
                .getHighlighter();
        if (h != null) {
            h.clearParserHighlights(iParser);
        }
        if (noticeHighlightPairs != null) {
            for (Iterator<NoticeHighlightPair> i = noticeHighlightPairs
                    .iterator(); i.hasNext();) {
                NoticeHighlightPair pair = (NoticeHighlightPair) i.next();
                if (pair.notice.getParser() == iParser) {
                    i.remove();
                }
            }
        }
    }

    /**
     * Removes all iParsers and any highlights they have created.
     *
     * @see #addParser(IParser)
     */
    public void clearParsers() {
        timer.stop();
        clearParserNoticeHighlights();
        iParsers.clear();
        textArea.fireParserNoticesChange();
    }

    /**
     * Forces the given {@link IParser} to re-parse the content of this text
     * area.<p>
     * 
     * This method can be useful when a <code>IParser</code> can be configured
     * as to what notices it returns. For example, if a Java language parser can
     * be configured to set whether no serialVersionUID is a warning, error, or
     * ignored, this method can be called after changing the expected notice
     * type to have the document re-parsed.
     *
     * @param parser The index of the <code>IParser</code> to re-run.
     * @see #getParser(int)
     */
    public void forceReparsing(int parser) {
        IParser p = getParser(parser);
        SyntaxDocument doc = (SyntaxDocument) textArea.getDocument();
        String style = textArea.getSyntaxEditingStyle();
        doc.readLock();
        try {
            if (p.isEnabled()) {
                IParseResult res = p.parse(doc, style);
                addParserNoticeHighlights(res);
            } else {
                clearParserNoticeHighlights(p);
            }
            textArea.fireParserNoticesChange();
        } finally {
            doc.readUnlock();
        }
    }

    /**
     * Returns the delay between the last "concurrent" edit and when the
     * document is re-parsed.
     *
     * @return The delay, in milliseconds.
     * @see #setDelay(int)
     */
    public int getDelay() {
        return timer.getDelay();
    }

    /**
     * Returns the specified parser.
     *
     * @param index The index of the parser.
     * @return The parser.
     * @see #getParserCount()
     * @see #addParser(IParser)
     * @see #removeParser(IParser)
     */
    public IParser getParser(int index) {
        return (IParser) iParsers.get(index);
    }

    /** @return The number of registered iParsers. */
    public int getParserCount() {
        return iParsers.size();
    }

    /**
     * Returns a list of the current parser notices for this text area. This
     * method (like most Swing methods) should only be called on the EDT.
     *
     * @return The list of notices. This will be an empty list if there are
     *         none.
     */
    public List<IParserNotice> getParserNotices() {
        List<IParserNotice> notices = new ArrayList<IParserNotice>();
        if (noticeHighlightPairs != null) {
            for (Iterator<NoticeHighlightPair> i = noticeHighlightPairs
                    .iterator(); i.hasNext();) {
                NoticeHighlightPair pair = (NoticeHighlightPair) i.next();
                notices.add(pair.notice);
            }
        }
        return notices;
    }

    /**
     * Returns the tool tip to display for a mouse event at the given location.
     * This method is overridden to give a registered parser a chance to display
     * a tool tip (such as an error description when the mouse is over an error
     * highlight).
     *
     * @param e The mouse event.
     * @return The tool tip to display, and possibly a hyperlink event handler. 
     */
    public ToolTipInfo getToolTipText(MouseEvent e) {

        String tip = null;
        HyperlinkListener listener = null;
        parserForTip = null;

        int pos = textArea.viewToModel(e.getPoint());
        if (noticeHighlightPairs != null) {
            for (int j = 0; j < noticeHighlightPairs.size(); j++) {
                NoticeHighlightPair pair = (NoticeHighlightPair) noticeHighlightPairs
                        .get(j);
                IParserNotice notice = pair.notice;
                if (notice.containsPosition(pos)) {
                    tip = notice.getToolTipText();
                    parserForTip = notice.getParser();
                    if (parserForTip instanceof HyperlinkListener) {
                        listener = (HyperlinkListener) parserForTip;
                    }
                    break;
                }
            }
        }

        URL imageBase = parserForTip == null ? null : parserForTip
                .getImageBase();
        return new ToolTipInfo(tip, listener, imageBase);
    }

    /**
     * Called when the document is modified.
     *
     * @param e The document event.
     */
    public void handleDocumentEvent(DocumentEvent e) {
        if (running && iParsers.size() > 0) {
            timer.restart();
        }
    }

    /**
     * Called when the user clicks a hyperlink in a {@link FocusableTip}.
     *
     * @param e The event.
     */
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (parserForTip != null && parserForTip.getHyperlinkListener() != null) {
            parserForTip.getHyperlinkListener().linkClicked(textArea, e);
        }
    }

    /**
     * Called when the document is modified.
     *
     * @param e The document event.
     */
    public void insertUpdate(DocumentEvent e) {
        // Keep track of the first and last offset modified. Some iParsers are
        // smart and will only re-parse this section of the file
        try {
            int offs = e.getOffset();
            if (firstOffsetModded == null
                    || offs < firstOffsetModded.getOffset()) {
                firstOffsetModded = e.getDocument().createPosition(offs);
            }
            offs = e.getOffset() + e.getLength();
            if (lastOffsetModded == null || offs > lastOffsetModded.getOffset()) {
                lastOffsetModded = e.getDocument().createPosition(offs);
            }
        } catch (BadLocationException ble) {
            ExceptionDialog.notifyException(ble); // Shouldn't happen
        }

        handleDocumentEvent(e);
    }

    /**
     * Removes a parser.
     *
     * @param iParser The parser to remove.
     * @return Whether the parser was found.
     * @see #addParser(IParser)
     * @see #getParser(int)
     */
    public boolean removeParser(IParser iParser) {
        removeParserNotices(iParser);
        boolean removed = iParsers.remove(iParser);
        if (removed) {
            textArea.fireParserNoticesChange();
        }
        return removed;
    }

    /**
     * Removes all parser notices (and clears highlights in the editor) from a
     * particular parser.
     *
     * @param iParser The parser.
     */
    private void removeParserNotices(IParser iParser) {
        if (noticeHighlightPairs != null) {
            SyntaxTextAreaHighlighter h = (SyntaxTextAreaHighlighter) textArea
                    .getHighlighter();
            for (Iterator<NoticeHighlightPair> i = noticeHighlightPairs
                    .iterator(); i.hasNext();) {
                NoticeHighlightPair pair = (NoticeHighlightPair) i.next();
                if (pair.notice.getParser() == iParser
                        && pair.highlight != null) {
                    h.removeParserHighlight(pair.highlight);
                    i.remove();
                }
            }
        }
    }

    /**
     * Removes any currently stored notices (and the corresponding highlights
     * from the editor) from the same IParser, and in the given line range, as
     * in the results.
     *
     * @param res The results.
     */
    private void removeParserNotices(IParseResult res) {
        if (noticeHighlightPairs != null) {
            SyntaxTextAreaHighlighter h = (SyntaxTextAreaHighlighter) textArea
                    .getHighlighter();
            for (Iterator<NoticeHighlightPair> i = noticeHighlightPairs
                    .iterator(); i.hasNext();) {
                NoticeHighlightPair pair = (NoticeHighlightPair) i.next();
                boolean removed = false;
                if (shouldRemoveNotice(pair.notice, res)) {
                    if (pair.highlight != null) {
                        h.removeParserHighlight(pair.highlight);
                    }
                    i.remove();
                    removed = true;
                }
                if (DEBUG_PARSING) {
                    String text = removed
                            ? "[DEBUG]: ... notice removed: "
                            : "[DEBUG]: ... notice not removed: ";
                    ApplicationFrame.getInstance().getConsole()
                            .println(text + pair.notice + ".");
                }
            }
        }
    }

    /**
     * Called when the document is modified.
     *
     * @param e The document event.
     */
    public void removeUpdate(DocumentEvent e) {
        // Keep track of the first and last offset modified. Some iParsers are
        // smart and will only re-parse this section of the file. Note that for
        // removals, only the line at the removal start needs to be re-parsed.
        try {
            int offs = e.getOffset();
            if (firstOffsetModded == null
                    || offs < firstOffsetModded.getOffset()) {
                firstOffsetModded = e.getDocument().createPosition(offs);
            }
            if (lastOffsetModded == null || offs > lastOffsetModded.getOffset()) {
                lastOffsetModded = e.getDocument().createPosition(offs);
            }
        } catch (BadLocationException ble) { // Never happens
            ExceptionDialog.notifyException(ble);
        }

        handleDocumentEvent(e);
    }

    /**
     * Restarts parsing the document.
     *
     * @see #stopParsing()
     */
    public void restartParsing() {
        timer.restart();
        running = true;
    }

    /**
     * Sets the delay between the last "concurrent" edit and when the document
     * is re-parsed.
     *
     * @param millis The new delay, in milliseconds. This must be greater than
     *               <code>0</code>.
     * @see #getDelay()
     */
    public void setDelay(int millis) {
        if (running) {
            timer.stop();
        }
        timer.setDelay(millis);
        if (running) {
            timer.start();
        }
    }

    /**
     * Returns whether a parser notice should be removed, based on a parse
     * result.
     *
     * @param notice The notice in question.
     * @param res The result.
     * @return Whether the notice should be removed.
     */
    private final boolean shouldRemoveNotice(IParserNotice notice,
            IParseResult res) {
        if (DEBUG_PARSING) {
            ApplicationFrame
                    .getInstance()
                    .getConsole()
                    .println(
                            "[DEBUG]: ... ... shouldRemoveNotice " + notice
                                    + ": "
                                    + (notice.getParser() == res.getParser())
                                    + ".");
        }

        // NOTE: We must currently remove all notices for the parser. IParser
        // implementors are required to parse the entire document each parsing
        // request, as STA is not yet sophisticated enough to determine the
        // minimum range of text to parse (and ParserNotices' locations aren't
        // updated when the Document is mutated, which would be a requirement
        // for this as well).
        return notice.getParser() == res.getParser();

    }

    /**
     * Stops parsing the document.
     *
     * @see #restartParsing()
     */
    public void stopParsing() {
        timer.stop();
        running = false;
    }

    /**
     * Mapping of a parser notice to its highlight in the editor.
     * 
     * @author D. Campione
     * 
     */
    private static class NoticeHighlightPair {
        public IParserNotice notice;
        public Object highlight;

        public NoticeHighlightPair(IParserNotice notice, Object highlight) {
            this.notice = notice;
            this.highlight = highlight;
        }
    }

    static {
        boolean debugParsing = false;
        try {
            debugParsing = Boolean.getBoolean(PROPERTY_DEBUG_PARSING);
        } catch (AccessControlException ace) {
            // Likely an applet's security manager
            debugParsing = false;
        }
        DEBUG_PARSING = debugParsing;
    }
}