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
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

import net.sourceforge.open_teradata_viewer.editor.syntax.parser.IParser;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.IParserNotice;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.TaskTagParser.TaskNotice;

/**
 * A component to sit alongside an {@link SyntaxTextArea} that displays colored
 * markers for locations of interest (parser errors, marked occurrences,
 * etc..).<p>
 *
 * <code>ErrorStrip</code>s display <code>IParserNotice</code>s from {@link
 * IParser}s. Currently, the only way to get lines flagged in this component is
 * to register a <code>IParser</code> on an SyntaxTextArea and return
 * <code>IParserNotice</code>s for each line to display an icon for. The
 * severity of each notice must be at least the threshold set by {@link
 * #setLevelThreshold(int)} to be displayed in this error strip. The default
 * threshold is {@link IParserNotice#WARNING}.<p>
 *
 * An <code>ErrorStrip</code> can be added to a UI like so:
 * <pre>
 * textArea = createTextArea();
 * textArea.addParser(new MyParser(textArea)); // Identifies lines to display
 * scrollPane = new TextScrollPane(textArea, true);
 * ErrorStrip es = new ErrorStrip(textArea);
 * JPanel temp = new JPanel(new BorderLayout());
 * temp.add(scrollPane);
 * temp.add(es, BorderLayout.LINE_END);
 * </pre>
 *
 * @author D. Campione
 * 
 */
public class ErrorStrip extends JComponent {

    private static final long serialVersionUID = 2920372562117101472L;

    /** The text area. */
    private SyntaxTextArea textArea;

    /** Listens for events in this component. */
    private Listener listener;

    /**
     * Whether "marked occurrences" in the text area should be shown in this
     * error strip.
     */
    private boolean showMarkedOccurrences;

    /**
     * Mapping of colors to brighter colors. This is kept to prevent unnecessary
     * creation of the same Colors over and over.
     */
    private Map<Color, Color> brighterColors;

    /**
     * Only notices of this severity (or worse) will be displayed in this error
     * strip.
     */
    private int levelThreshold;

    /** Whether the caret marker's location should be rendered. */
    private boolean followCaret;

    /** The color to use for the caret marker. */
    private Color caretMarkerColor;

    /** Where we paint the caret marker. */
    private int caretLineY;

    /** The last location of the caret marker. */
    private int lastLineY;

    /** The preferred width of this component. */
    private static final int PREFERRED_WIDTH = 14;

    /**
     * Ctor.
     *
     * @param textArea The text area we are examining.
     */
    public ErrorStrip(SyntaxTextArea textArea) {
        this.textArea = textArea;
        listener = new Listener();
        ToolTipManager.sharedInstance().registerComponent(this);
        setLayout(null); // Manually layout Markers as they can overlap
        addMouseListener(listener);
        setShowMarkedOccurrences(true);
        setLevelThreshold(IParserNotice.WARNING);
        setFollowCaret(true);
        setCaretMarkerColor(Color.BLACK);
    }

    /**
     * Overridden so we only start listening for parser notices when this
     * component (and presumably the text area) are visible.
     */
    public void addNotify() {
        super.addNotify();
        textArea.addCaretListener(listener);
        textArea.addPropertyChangeListener(
                SyntaxTextArea.PARSER_NOTICES_PROPERTY, listener);
        textArea.addPropertyChangeListener(
                SyntaxTextArea.MARK_OCCURRENCES_PROPERTY, listener);
        textArea.addPropertyChangeListener(
                SyntaxTextArea.MARKED_OCCURRENCES_CHANGED_PROPERTY, listener);
        refreshMarkers();
    }

    /** Manually manages layout since this component uses no layout manager. */
    public void doLayout() {
        for (int i = 0; i < getComponentCount(); i++) {
            Marker m = (Marker) getComponent(i);
            m.updateLocation();
        }
        listener.caretUpdate(null); // Force recalculation of caret line pos
    }

    /**
     * Returns a "brighter" color.
     *
     * @param c The color.
     * @return A brighter color.
     */
    private Color getBrighterColor(Color c) {
        if (brighterColors == null) {
            brighterColors = new HashMap<Color, Color>(5); // Usually small
        }
        Color brighter = (Color) brighterColors.get(c);
        if (brighter == null) {
            // Don't use c.brighter() as it doesn't work well for blue, and also
            // doesn't return something brighter "enough"
            int r = possiblyBrighter(c.getRed());
            int g = possiblyBrighter(c.getGreen());
            int b = possiblyBrighter(c.getBlue());
            brighter = new Color(r, g, b);
            brighterColors.put(c, brighter);
        }
        return brighter;
    }

    /**
     * Returns the color to use when painting the caret marker.
     *
     * @return The caret marker color.
     * @see #setCaretMarkerColor(Color)
     */
    public Color getCaretMarkerColor() {
        return caretMarkerColor;
    }

    /**
     * @return Whether the caret's position should be drawn.
     * @see #setFollowCaret(boolean)
     */
    public boolean getFollowCaret() {
        return followCaret;
    }

    /** {@inheritDoc} */
    public Dimension getPreferredSize() {
        int height = textArea.getPreferredScrollableViewportSize().height;
        return new Dimension(PREFERRED_WIDTH, height);
    }

    /**
     * Returns the minimum severity a parser notice must be for it to be
     * displayed in this error strip. This will be one of the constants
     * defined in the <code>IParserNotice</code> class.
     *
     * @return The minimum severity.
     * @see #setLevelThreshold(int)
     */
    public int getLevelThreshold() {
        return levelThreshold;
    }

    /**
     * @return Whether marked occurrences are shown in this error strip.
     * @see #setShowMarkedOccurrences(boolean)
     */
    public boolean getShowMarkedOccurrences() {
        return showMarkedOccurrences;
    }

    /** {@inheritDoc} */
    public String getToolTipText(MouseEvent e) {
        String text = null;
        int line = yToLine(e.getY());
        if (line > -1) {
            text = "Line: {0}";
            text = MessageFormat.format(text, new Object[]{new Integer(line)});
        }
        return text;
    }

    /**
     * Returns the y-offset in this component corresponding to a line in the
     * text component.
     *
     * @param line The line.
     * @return The y-offset.
     * @see #yToLine(int)
     */
    private int lineToY(int line) {
        int h = textArea.getVisibleRect().height;
        float lineCount = textArea.getLineCount();
        return (int) ((line / lineCount) * h) - 2;
    }

    /**
     * Overridden to (possibly) draw the caret's position.
     *
     * @param g The graphics context.
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (caretLineY > -1) {
            g.setColor(getCaretMarkerColor());
            g.fillRect(0, caretLineY, getWidth(), 2);
        }
    }

    /**
     * Returns a possibly brighter component for a color.
     *
     * @param i An RGB component for a color (0-255).
     * @return A possibly brighter value for the component.
     */
    private static final int possiblyBrighter(int i) {
        if (i < 255) {
            i += (int) ((255 - i) * 0.8f);
        }
        return i;
    }

    /** Refreshes the markers displayed in this error strip. */
    private void refreshMarkers() {
        removeAll(); // Listener is removed in Marker.removeNotify()
        Map<Integer, Marker> markerMap = new HashMap<Integer, Marker>();

        List<?> notices = textArea.getParserNotices();
        for (Iterator<?> i = notices.iterator(); i.hasNext();) {
            IParserNotice notice = (IParserNotice) i.next();
            if (notice.getLevel() <= levelThreshold
                    || (notice instanceof TaskNotice)) {
                Integer key = new Integer(notice.getLine());
                Marker m = (Marker) markerMap.get(key);
                if (m == null) {
                    m = new Marker(notice);
                    m.addMouseListener(listener);
                    markerMap.put(key, m);
                    add(m);
                } else {
                    m.addNotice(notice);
                }
            }
        }

        if (getShowMarkedOccurrences() && textArea.getMarkOccurrences()) {
            List<?> occurrences = textArea.getMarkedOccurrences();
            for (Iterator<?> i = occurrences.iterator(); i.hasNext();) {
                IDocumentRange range = (IDocumentRange) i.next();
                int line = 0;
                try {
                    line = textArea.getLineOfOffset(range.getStartOffset());
                } catch (BadLocationException ble) { // Never happens
                    continue;
                }
                IParserNotice notice = new MarkedOccurrenceNotice(range);
                Integer key = new Integer(line);
                Marker m = (Marker) markerMap.get(key);
                if (m == null) {
                    m = new Marker(notice);
                    m.addMouseListener(listener);
                    markerMap.put(key, m);
                    add(m);
                } else {
                    if (!m.containsMarkedOccurence()) {
                        m.addNotice(notice);
                    }
                }
            }
        }

        revalidate();
        repaint();
    }

    /** {@inheritDoc} */
    public void removeNotify() {
        super.removeNotify();
        textArea.removeCaretListener(listener);
        textArea.removePropertyChangeListener(
                SyntaxTextArea.PARSER_NOTICES_PROPERTY, listener);
        textArea.removePropertyChangeListener(
                SyntaxTextArea.MARK_OCCURRENCES_PROPERTY, listener);
        textArea.removePropertyChangeListener(
                SyntaxTextArea.MARKED_OCCURRENCES_CHANGED_PROPERTY, listener);
    }

    /**
     * Sets the color to use when painting the caret marker.
     *
     * @param color The new caret marker color.
     * @see #getCaretMarkerColor()
     */
    public void setCaretMarkerColor(Color color) {
        if (color != null) {
            caretMarkerColor = color;
            listener.caretUpdate(null); // Force repaint
        }
    }

    /**
     * Toggles whether the caret's current location should be drawn.
     *
     * @param follow Whether the caret's current location should be followed.
     * @see #getFollowCaret()
     */
    public void setFollowCaret(boolean follow) {
        if (followCaret != follow) {
            if (followCaret) {
                repaint(0, caretLineY, getWidth(), 2); // Erase
            }
            caretLineY = -1;
            lastLineY = -1;
            followCaret = follow;
            listener.caretUpdate(null); // Possibly repaint
        }
    }

    /**
     * Sets the minimum severity a parser notice must be for it to be displayed
     * in this error strip. This should be one of the constants defined in the
     * <code>IParserNotice</code> class. The default value is {@link
     * IParserNotice#WARNING}.
     *
     * @param level The new severity threshold.
     * @see #getLevelThreshold()
     * @see IParserNotice
     */
    public void setLevelThreshold(int level) {
        levelThreshold = level;
        if (isDisplayable()) {
            refreshMarkers();
        }
    }

    /**
     * Sets whether marked occurrences are shown in this error strip.
     *
     * @param show Whether to show marked occurrences.
     * @see #getShowMarkedOccurrences()
     */
    public void setShowMarkedOccurrences(boolean show) {
        if (show != showMarkedOccurrences) {
            showMarkedOccurrences = show;
            if (isDisplayable()) { // Skip this when we're first created
                refreshMarkers();
            }
        }
    }

    /**
     * Returns the line in the text area corresponding to a y-offset in this
     * component.
     *
     * @param y The y-offset.
     * @return The line.
     * @see #lineToY(int)
     */
    private final int yToLine(int y) {
        int line = -1;
        int h = textArea.getVisibleRect().height;
        if (y < h) {
            float at = y / (float) h;
            line = (int) (textArea.getLineCount() * at);
        }
        return line;
    }

    /**
     * Listens for events in the error strip and its markers.
     * 
     * @author D. Campione
     * 
     */
    private class Listener extends MouseAdapter
            implements
                PropertyChangeListener,
                CaretListener {

        private Rectangle visibleRect = new Rectangle();

        public void caretUpdate(CaretEvent e) {
            if (getFollowCaret()) {
                int line = textArea.getCaretLineNumber();
                float percent = line / ((float) textArea.getLineCount());
                textArea.computeVisibleRect(visibleRect);
                caretLineY = (int) (visibleRect.height * percent);
                if (caretLineY != lastLineY) {
                    repaint(0, lastLineY, getWidth(), 2); // Erase old position
                    repaint(0, caretLineY, getWidth(), 2);
                    lastLineY = caretLineY;
                }
            }
        }

        public void mouseClicked(MouseEvent e) {
            Component source = (Component) e.getSource();
            if (source instanceof Marker) {
                ((Marker) source).mouseClicked(e);
                return;
            }

            int line = yToLine(e.getY());
            if (line > -1) {
                try {
                    int offs = textArea.getLineStartOffset(line);
                    textArea.setCaretPosition(offs);
                } catch (BadLocationException ble) { // Never happens
                    UIManager.getLookAndFeel().provideErrorFeedback(textArea);
                }
            }

        }

        public void propertyChange(PropertyChangeEvent e) {
            String propName = e.getPropertyName();

            // If they change whether marked occurrences are visible in editor
            if (SyntaxTextArea.MARK_OCCURRENCES_PROPERTY.equals(propName)) {
                if (getShowMarkedOccurrences()) {
                    refreshMarkers();
                }
            } else if (SyntaxTextArea.PARSER_NOTICES_PROPERTY.equals(propName)) { // If parser notices changed
                refreshMarkers();
            } else if (SyntaxTextArea.MARKED_OCCURRENCES_CHANGED_PROPERTY // If marked occurrences changed
                    .equals(propName)) {
                if (getShowMarkedOccurrences()) {
                    refreshMarkers();
                }
            }
        }
    }

    private static final Color COLOR = new Color(220, 220, 220);

    /**
     * A notice that wraps a "marked occurrence".
     * 
     * @author D. Campione
     * 
     */
    private class MarkedOccurrenceNotice implements IParserNotice {

        private IDocumentRange range;

        public MarkedOccurrenceNotice(IDocumentRange range) {
            this.range = range;
        }

        public int compareTo(Object o) {
            return 0;
        }

        public boolean containsPosition(int pos) {
            return pos >= range.getStartOffset() && pos < range.getEndOffset();
        }

        public boolean equals(Object o) {
            return compareTo(o) == 0;
        }

        public Color getColor() {
            return COLOR;
        }

        public int getLength() {
            return range.getEndOffset() - range.getStartOffset();
        }

        public int getLevel() {
            return INFO;
        }

        public int getLine() {
            try {
                return textArea.getLineOfOffset(range.getStartOffset());
            } catch (BadLocationException ble) {
                return 0;
            }
        }

        public String getMessage() {
            String text = null;
            try {
                String word = textArea.getText(range.getStartOffset(),
                        getLength());
                text = "Occurrence of \"{0}\"";
                text = MessageFormat.format(text, new Object[]{word});
            } catch (BadLocationException ble) {
                UIManager.getLookAndFeel().provideErrorFeedback(textArea);
            }
            return text;
        }

        public int getOffset() {
            return range.getStartOffset();
        }

        public IParser getParser() {
            return null;
        }

        public boolean getShowInEditor() {
            return false;
        }

        public String getToolTipText() {
            return null;
        }

        public int hashCode() {
            return 0;
        }

    }

    /**
     * A "marker" in this error strip, representing one or more notices.
     * 
     * @author D. Campione
     * 
     */
    private class Marker extends JComponent {

        private static final long serialVersionUID = 8877063424120739438L;

        private List<IParserNotice> notices;

        public Marker(IParserNotice notice) {
            notices = new ArrayList<IParserNotice>(1); // Usually just 1
            addNotice(notice);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setSize(getPreferredSize());
            ToolTipManager.sharedInstance().registerComponent(this);
        }

        public void addNotice(IParserNotice notice) {
            notices.add(notice);
        }

        public boolean containsMarkedOccurence() {
            boolean result = false;
            for (int i = 0; i < notices.size(); i++) {
                if (notices.get(i) instanceof MarkedOccurrenceNotice) {
                    result = true;
                    break;
                }
            }
            return result;
        }

        public Color getColor() {
            // Return the color for the highest-level parser
            Color c = null;
            int lowestLevel = Integer.MAX_VALUE; // ERROR is 0
            for (Iterator<IParserNotice> i = notices.iterator(); i.hasNext();) {
                IParserNotice notice = (IParserNotice) i.next();
                if (notice.getLevel() < lowestLevel) {
                    lowestLevel = notice.getLevel();
                    c = notice.getColor();
                }
            }
            return c;
        }

        public Dimension getPreferredSize() {
            int w = PREFERRED_WIDTH - 4; // 2-pixel empty border
            return new Dimension(w, 5);
        }

        public String getToolTipText() {
            String text = null;

            if (notices.size() == 1) {
                text = ((IParserNotice) notices.get(0)).getMessage();
            } else {
                StringBuffer sb = new StringBuffer("<html>");
                sb.append("Multiple markers at this line:");
                sb.append("<br>");
                for (int i = 0; i < notices.size(); i++) {
                    IParserNotice pn = (IParserNotice) notices.get(i);
                    sb.append("&nbsp;&nbsp;&nbsp;- ");
                    sb.append(pn.getMessage());
                    sb.append("<br>");
                }
                text = sb.toString();
            }

            return text;
        }

        protected void mouseClicked(MouseEvent e) {
            IParserNotice pn = (IParserNotice) notices.get(0);
            int offs = pn.getOffset();
            int len = pn.getLength();
            if (offs > -1 && len > -1) { // These values are optional
                textArea.setSelectionStart(offs);
                textArea.setSelectionEnd(offs + len);
            } else {
                int line = pn.getLine();
                try {
                    offs = textArea.getLineStartOffset(line);
                    textArea.setCaretPosition(offs);
                } catch (BadLocationException ble) { // Never happens
                    UIManager.getLookAndFeel().provideErrorFeedback(textArea);
                }
            }
        }

        protected void paintComponent(Graphics g) {
            Color borderColor = getColor();
            if (borderColor == null) {
                borderColor = Color.DARK_GRAY;
            }
            Color fillColor = getBrighterColor(borderColor);

            int w = getWidth();
            int h = getHeight();

            g.setColor(fillColor);
            g.fillRect(0, 0, w, h);

            g.setColor(borderColor);
            g.drawRect(0, 0, w - 1, h - 1);
        }

        public void removeNotify() {
            super.removeNotify();
            ToolTipManager.sharedInstance().unregisterComponent(this);
            removeMouseListener(listener);
        }

        public void updateLocation() {
            int line = ((IParserNotice) notices.get(0)).getLine();
            int y = lineToY(line);
            setLocation(2, y);
        }
    }
}