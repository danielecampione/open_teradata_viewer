/*
 * Open Teradata Viewer ( editor )
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

package net.sourceforge.open_teradata_viewer.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JToolTip;
import javax.swing.ToolTipManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.View;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;
import net.sourceforge.open_teradata_viewer.editor.syntax.Token;
import net.sourceforge.open_teradata_viewer.editor.syntax.focusabletip.TipUtil;
import net.sourceforge.open_teradata_viewer.editor.syntax.folding.Fold;
import net.sourceforge.open_teradata_viewer.editor.syntax.folding.FoldManager;

/**
 * Component in the gutter that displays +/- icons to expand and collapse fold
 * regions in the editor.
 *
 * @author D. Campione
 * 
 */
public class FoldIndicator extends AbstractGutterComponent {

    private static final long serialVersionUID = 1798916457223016136L;

    /**
     * Used in {@link #paintComponent(Graphics)} to prevent reallocation on each
     * paint.
     */
    private Insets textAreaInsets;

    /**
     * Used in {@link #paintComponent(Graphics)} to prevent reallocation on each
     * paint.
     */
    private Rectangle visibleRect;

    /** The fold to show the outline line for. */
    private Fold foldWithOutlineShowing;

    /**
     * The color to use for fold icon backgrounds, if the default icons are
     * used.
     */
    private Color foldIconBackground;

    /** The icon used for collapsed folds. */
    private Icon collapsedFoldIcon;

    /** The icon used for expanded folds. */
    private Icon expandedFoldIcon;

    /**
     * Whether tool tips are displayed showing the contents of collapsed fold
     * regions.
     */
    private boolean showFoldRegionTips;

    /** The color used to paint fold outlines. */
    static final Color DEFAULT_FOREGROUND = Color.gray;

    /** The default color used to paint the "inside" of fold icons. */
    static final Color DEFAULT_FOLD_BACKGROUND = Color.white;

    /** Listens for events in this component. */
    private Listener listener;

    /** Width of this component. */
    private static final int WIDTH = 12;

    public FoldIndicator(TextArea textArea) {
        super(textArea);
        setForeground(DEFAULT_FOREGROUND);
        setFoldIconBackground(DEFAULT_FOLD_BACKGROUND);
        collapsedFoldIcon = new FoldIcon(true);
        expandedFoldIcon = new FoldIcon(false);
        listener = new Listener(this);
        visibleRect = new Rectangle();
        setShowCollapsedRegionToolTips(true);
    }

    /**
     * Overridden to use the editor's background if it's detected that the user
     * isn't using white as the editor bg, but the system's tool tip background
     * is yellow-ish.
     *
     * @return The tool tip.
     */
    public JToolTip createToolTip() {
        JToolTip tip = super.createToolTip();
        Color textAreaBG = textArea.getBackground();
        if (textAreaBG != null && !Color.white.equals(textAreaBG)) {
            Color bg = TipUtil.getToolTipBackground();
            // If current L&F's tool tip color is close enough to "yellow", and
            // we're not using the default text background of white, use the
            // editor background as the tool tip background
            if (bg.getRed() >= 240 && bg.getGreen() >= 240
                    && bg.getBlue() >= 200) {
                tip.setBackground(textAreaBG);
            }
        }
        return tip;
    }

    private Fold findOpenFoldClosestTo(Point p) {
        Fold fold = null;

        SyntaxTextArea sta = (SyntaxTextArea) textArea;
        if (sta.isCodeFoldingEnabled()) { // Should always be true
            int offs = sta.viewToModel(p);
            if (offs > -1) {
                try {
                    int line = sta.getLineOfOffset(offs);
                    FoldManager fm = sta.getFoldManager();
                    fold = fm.getFoldForLine(line);
                    if (fold == null) {
                        fold = fm.getDeepestOpenFoldContaining(offs);
                    }
                } catch (BadLocationException ble) {
                    ExceptionDialog.notifyException(ble); // Never happens
                }
            }
        }

        return fold;
    }

    /**
     * Returns the color to use for the "background" of fold icons. This is be
     * ignored if custom icons are used.
     *
     * @return The background color.
     * @see #setFoldIconBackground(Color)
     */
    public Color getFoldIconBackground() {
        return foldIconBackground;
    }

    public Dimension getPreferredSize() {
        int h = textArea != null ? textArea.getHeight() : 100; // Arbitrary
        return new Dimension(WIDTH, h);
    }

    /**
     * Returns whether tool tips are displayed showing the contents of
     * collapsed fold regions when the mouse hovers over a +/- icon.
     *
     * @return Whether these tool tips are displayed.
     * @see #setShowCollapsedRegionToolTips(boolean)
     */
    public boolean getShowCollapsedRegionToolTips() {
        return showFoldRegionTips;
    }

    /**
     * Positions tool tips to be aligned in the text component, so that the
     * displayed content is shown (almost) exactly where it would be in the
     * editor.
     *
     * @param e The mouse location.
     */
    public Point getToolTipLocation(MouseEvent e) {
        Point p = e.getPoint();
        p.y = (p.y / textArea.getLineHeight()) * textArea.getLineHeight();
        p.x = getWidth();
        return p;
    }

    /**
     * Overridden to show the content of a collapsed fold on mouse-overs.
     *
     * @param e The mouse location.
     */
    public String getToolTipText(MouseEvent e) {
        String text = null;

        SyntaxTextArea sta = (SyntaxTextArea) textArea;
        if (sta.isCodeFoldingEnabled()) {
            FoldManager fm = sta.getFoldManager();
            int pos = sta.viewToModel(new Point(0, e.getY()));
            if (pos >= 0) { // Not -1
                int line = 0;
                try {
                    line = sta.getLineOfOffset(pos);
                } catch (BadLocationException ble) {
                    ExceptionDialog.notifyException(ble); // Never happens
                    return null;
                }
                Fold fold = fm.getFoldForLine(line);
                if (fold != null && fold.isCollapsed()) {

                    int endLine = fold.getEndLine();
                    if (fold.getLineCount() > 25) { // Not too big
                        endLine = fold.getStartLine() + 25;
                    }

                    StringBuffer sb = new StringBuffer("<html><nobr>");
                    while (line <= endLine && line < sta.getLineCount()) { // Sanity
                        Token t = sta.getTokenListForLine(line);
                        while (t != null && t.isPaintable()) {
                            t.appendHTMLRepresentation(sb, sta, true, true);
                            t = t.getNextToken();
                        }
                        sb.append("<br>");
                        line++;
                    }

                    text = sb.toString();
                }
            }
        }

        return text;
    }

    void handleDocumentEvent(DocumentEvent e) {
        int newLineCount = textArea.getLineCount();
        if (newLineCount != currentLineCount) {
            currentLineCount = newLineCount;
            repaint();
        }
    }

    void lineHeightsChanged() {
    }

    protected void paintComponent(Graphics g) {
        if (textArea == null) {
            return;
        }

        visibleRect = g.getClipBounds(visibleRect);
        if (visibleRect == null) {
            visibleRect = getVisibleRect();
        }
        if (visibleRect == null) {
            return;
        }

        Color bg = getBackground();
        if (getGutter() != null) { // Should always be true
            bg = getGutter().getBackground();
        }
        g.setColor(bg);
        g.fillRect(0, visibleRect.y, getWidth(), visibleRect.height);

        SyntaxTextArea sta = (SyntaxTextArea) textArea;
        if (!sta.isCodeFoldingEnabled()) {
            return;
        }

        if (textArea.getLineWrap()) {
            paintComponentWrapped(g);
            return;
        }

        // Get where to start painting (top of the row). We need to be "scrolled
        // up" up just enough for the missing part of the first line
        int cellHeight = textArea.getLineHeight();
        int topLine = visibleRect.y / cellHeight;
        int y = topLine * cellHeight
                + (cellHeight - collapsedFoldIcon.getIconHeight()) / 2;
        textAreaInsets = textArea.getInsets(textAreaInsets);
        if (textAreaInsets != null) {
            y += textAreaInsets.top;
        }

        // Get the first and last lines to paint
        FoldManager fm = sta.getFoldManager();
        topLine += fm.getHiddenLineCountAbove(topLine, true);

        int width = getWidth();
        int x = width - 10;
        int line = topLine;
        boolean paintingOutlineLine = foldWithOutlineShowing != null
                && foldWithOutlineShowing.containsLine(line);

        while (y < visibleRect.y + visibleRect.height) {
            if (paintingOutlineLine) {
                g.setColor(getForeground());
                int w2 = width / 2;
                if (line == foldWithOutlineShowing.getEndLine()) {
                    int y2 = y + cellHeight / 2;
                    g.drawLine(w2, y, w2, y2);
                    g.drawLine(w2, y2, width - 2, y2);
                    paintingOutlineLine = false;
                } else {
                    g.drawLine(w2, y, w2, y + cellHeight);
                }
            }
            Fold fold = fm.getFoldForLine(line);
            if (fold != null) {
                if (fold == foldWithOutlineShowing && !fold.isCollapsed()) {
                    g.setColor(getForeground());
                    int w2 = width / 2;
                    g.drawLine(w2, y + cellHeight / 2, w2, y + cellHeight);
                    paintingOutlineLine = true;
                }
                if (fold.isCollapsed()) {
                    collapsedFoldIcon.paintIcon(this, g, x, y);
                    // Skip to next line to paint, taking extra care for lines
                    // with block ends and begins together, e.g. "} else {"
                    do {
                        int hiddenLineCount = fold.getLineCount();
                        if (hiddenLineCount == 0) {
                            // Fold parser identified a zero-line fold region.
                            // This is really a bug, but we'll be graceful here
                            // and avoid an infinite loop
                            break;
                        }
                        line += hiddenLineCount;
                        fold = fm.getFoldForLine(line);
                    } while (fold != null && fold.isCollapsed());
                } else {
                    expandedFoldIcon.paintIcon(this, g, x, y);
                }
            }
            line++;
            y += cellHeight;
        }
    }

    /**
     * Paints folding icons when line wrapping is enabled.
     *
     * @param g The graphics context.
     */
    private void paintComponentWrapped(Graphics g) {
        // The variables we use are as follows:
        // - visibleRect is the "visible" area of the text area; e.g. [0,100,
        //   300,100+(lineCount*cellHeight)-1].
        // - actualTop.y is the topmost-pixel in the first logical line we
        //   paint. Note that we may well not paint this part of the logical
        //   line, as it may be broken into many physical lines, with the first
        //   few physical lines scrolled past. Note also that this is NOT the
        //   visible rect of this line number list; this line number list has
        //   visible rect == [0,0, insets.left-1,visibleRect.height-1].
        // - offset (<=0) is the y-coordinate at which we begin painting when we
        //   begin painting with the first logical line. This can be negative,
        //   signifying that we've scrolled past the actual topmost part of this
        //   line

        // The algorithm is as follows:
        // - Get the starting y-coordinate at which to paint. This may be above
        //   the first visible y-coordinate as we're in line-wrapping mode, but
        //   we always paint entire logical lines.
        // - Paint that line's indicator, if appropriate. Increment y to be just
        //   below the are we just painted (i.e., the beginning of the next
        //   logical line's view area).
        // - Get the ending visual position for that line. We can now loop back,
        //   paint this line, and continue until our y-coordinate is past the
        //   last visible y-value

        // We avoid using modelToView/viewToModel where possible, as these
        // methods trigger a parsing of the line into syntax tokens, which is
        // costly. It's cheaper to just grab the child views' bounds

        // Some variables we'll be using
        int width = getWidth();

        TextAreaUI ui = (TextAreaUI) textArea.getUI();
        View v = ui.getRootView(textArea).getView(0);
        Document doc = textArea.getDocument();
        Element root = doc.getDefaultRootElement();
        int topPosition = textArea.viewToModel(new Point(visibleRect.x,
                visibleRect.y));
        int topLine = root.getElementIndex(topPosition);
        int cellHeight = textArea.getLineHeight();
        FoldManager fm = ((SyntaxTextArea) textArea).getFoldManager();

        // Compute the y at which to begin painting text, taking into account
        // that 1 logical line => at least 1 physical line, so it may be that
        // y<0. The computed y-value is the y-value of the top of the first
        // (possibly) partially-visible view
        Rectangle visibleEditorRect = ui.getVisibleEditorRect();
        Rectangle r = LineNumberList.getChildViewBounds(v, topLine,
                visibleEditorRect);
        int y = r.y;
        y += (cellHeight - collapsedFoldIcon.getIconHeight()) / 2;

        int visibleBottom = visibleRect.y + visibleRect.height;
        int x = width - 10;
        int line = topLine;
        boolean paintingOutlineLine = foldWithOutlineShowing != null
                && foldWithOutlineShowing.containsLine(line);
        int lineCount = root.getElementCount();

        while (y < visibleBottom && line < lineCount) {
            int curLineH = LineNumberList.getChildViewBounds(v, line,
                    visibleEditorRect).height;

            if (paintingOutlineLine) {
                g.setColor(getForeground());
                int w2 = width / 2;
                if (line == foldWithOutlineShowing.getEndLine()) {
                    int y2 = y + curLineH - cellHeight / 2;
                    g.drawLine(w2, y, w2, y2);
                    g.drawLine(w2, y2, width - 2, y2);
                    paintingOutlineLine = false;
                } else {
                    g.drawLine(w2, y, w2, y + curLineH);
                }
            }
            Fold fold = fm.getFoldForLine(line);
            if (fold != null) {
                if (fold == foldWithOutlineShowing && !fold.isCollapsed()) {
                    g.setColor(getForeground());
                    int w2 = width / 2;
                    g.drawLine(w2, y + cellHeight / 2, w2, y + curLineH);
                    paintingOutlineLine = true;
                }
                if (fold.isCollapsed()) {
                    collapsedFoldIcon.paintIcon(this, g, x, y);
                    y += LineNumberList.getChildViewBounds(v, line,
                            visibleEditorRect).height;
                    line += fold.getLineCount() + 1;
                } else {
                    expandedFoldIcon.paintIcon(this, g, x, y);
                    y += curLineH;
                    line++;
                }
            } else {
                y += curLineH;
                line++;
            }
        }
    }

    private int rowAtPoint(Point p) {
        int line = 0;

        try {
            int offs = textArea.viewToModel(p);
            if (offs > -1) {
                line = textArea.getLineOfOffset(offs);
            }
        } catch (BadLocationException ble) {
            ExceptionDialog.notifyException(ble); // Never happens
        }

        return line;
    }

    /**
     * Sets the color to use for the "background" of fold icons. This will be
     * ignored if custom icons are used.
     *
     * @param bg The new background color.
     * @see #getFoldIconBackground()
     */
    public void setFoldIconBackground(Color bg) {
        foldIconBackground = bg;
    }

    /**
     * Sets the icons to use to represent collapsed and expanded folds.
     *
     * @param collapsedIcon The collapsed fold icon. This cannot be
     *                      <code>null</code>.
     * @param expandedIcon The expanded fold icon. This cannot be
     *                     <code>null</code>.
     */
    public void setFoldIcons(Icon collapsedIcon, Icon expandedIcon) {
        this.collapsedFoldIcon = collapsedIcon;
        this.expandedFoldIcon = expandedIcon;
        revalidate(); // Icons may be different sizes
        repaint();
    }

    /**
     * Toggles whether tool tips should be displayed showing the contents of
     * collapsed fold regions when the mouse hovers over a +/- icon.
     *
     * @param show Whether to show these tool tips.
     * @see #getShowCollapsedRegionToolTips()
     */
    public void setShowCollapsedRegionToolTips(boolean show) {
        if (show != showFoldRegionTips) {
            if (show) {
                ToolTipManager.sharedInstance().registerComponent(this);
            } else {
                ToolTipManager.sharedInstance().unregisterComponent(this);
            }
            showFoldRegionTips = show;
        }
    }

    /** Overridden so we can track when code folding is enabled/disabled. */
    public void setTextArea(TextArea textArea) {
        if (this.textArea != null) {
            this.textArea.removePropertyChangeListener(
                    SyntaxTextArea.CODE_FOLDING_PROPERTY, listener);
        }
        super.setTextArea(textArea);
        if (this.textArea != null) {
            this.textArea.addPropertyChangeListener(
                    SyntaxTextArea.CODE_FOLDING_PROPERTY, listener);
        }
    }

    /**
     * The default +/- icon for expanding and collapsing folds.
     * 
     * @author D. Campione
     * 
     */
    private class FoldIcon implements Icon {
        private boolean collapsed;

        public FoldIcon(boolean collapsed) {
            this.collapsed = collapsed;
        }

        public int getIconHeight() {
            return 8;
        }

        public int getIconWidth() {
            return 8;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(foldIconBackground);
            g.fillRect(x, y, 8, 8);
            g.setColor(getForeground());
            g.drawRect(x, y, 8, 8);
            g.drawLine(x + 2, y + 4, x + 2 + 4, y + 4);
            if (collapsed) {
                g.drawLine(x + 4, y + 2, x + 4, y + 6);
            }
        }
    }

    /**
     * Listens for events in this component.
     * 
     * @author D. Campione
     * 
     */
    private class Listener extends MouseInputAdapter
            implements
                PropertyChangeListener {

        public Listener(FoldIndicator fgc) {
            fgc.addMouseListener(this);
            fgc.addMouseMotionListener(this);
        }

        public void mouseClicked(MouseEvent e) {
            Point p = e.getPoint();
            int line = rowAtPoint(p);

            SyntaxTextArea sta = (SyntaxTextArea) textArea;
            FoldManager fm = sta.getFoldManager();

            Fold fold = fm.getFoldForLine(line);
            if (fold != null) {
                fold.toggleCollapsedState();
                getGutter().repaint();
                textArea.repaint();
            }
        }

        public void mouseExited(MouseEvent e) {
            if (foldWithOutlineShowing != null) {
                foldWithOutlineShowing = null;
                repaint();
            }
        }

        public void mouseMoved(MouseEvent e) {
            Fold newSelectedFold = findOpenFoldClosestTo(e.getPoint());
            if (newSelectedFold != foldWithOutlineShowing
                    && newSelectedFold != null
                    && !newSelectedFold.isOnSingleLine()) {
                foldWithOutlineShowing = newSelectedFold;
                repaint();
            }
        }

        public void propertyChange(PropertyChangeEvent e) {
            // Whether folding is enabled in the editor has changed
            repaint();
        }
    }
}