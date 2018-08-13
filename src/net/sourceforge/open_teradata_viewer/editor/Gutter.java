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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import net.sourceforge.open_teradata_viewer.editor.syntax.ActiveLineRangeEvent;
import net.sourceforge.open_teradata_viewer.editor.syntax.IActiveLineRangeListener;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;
import net.sourceforge.open_teradata_viewer.editor.syntax.folding.FoldManager;

/**
 * The gutter is the component on the left-hand side of the text area that
 * displays optional information such as line numbers, fold regions, and icons
 * (for bookmarks, debugging breakpoints, error markers, etc.).<p>
 *
 * Icons can be added on a per-line basis to visually mark syntax errors, lines
 * with breakpoints set on them, etc. To add icons to the gutter, you must first
 * call {@link TextScrollPane#setIconRowHeaderEnabled(boolean)} on the parent
 * scroll pane, to make the icon area visible. Then, you can add icons that
 * track either lines in the document, or offsets, via {@link
 * #addLineTrackingIcon(int, Icon)} and {@link
 * #addOffsetTrackingIcon(int, Icon)}, respectively. To remove an icon you've
 * added, use {@link #removeTrackingIcon(IGutterIconInfo)}.<p>
 *
 * In addition to support for arbitrary per-line icons, this component also has
 * built-in support for displaying icons representing "bookmarks;" that is,
 * lines a user can cycle through via F2 and Shift+F2. Bookmarked lines are
 * toggled via Ctrl+F2. In order to enable bookmarking, you must first assign an
 * icon to represent a bookmarked line, then actually enable the feature:
 * 
 * <pre>
 * Gutter gutter = scrollPane.getGutter();
 * gutter.setBookmarkIcon(new ImageIcon("bookmark.png"));
 * gutter.setBookmarkingEnabled(true);
 * </pre>
 *
 * @author D. Campione
 * @see IGutterIconInfo
 * 
 */
public class Gutter extends JPanel {

    private static final long serialVersionUID = -2743204121861608367L;

    /** The color used to highlight active line ranges if none is specified. */
    public static final Color DEFAULT_ACTIVE_LINE_RANGE_COLOR = new Color(51,
            153, 255);

    /** The text area. */
    private TextArea textArea;

    /** Renders line numbers. */
    private LineNumberList lineNumberList;

    /** The color used to render line numbers. */
    private Color lineNumberColor;

    /** The starting index for line numbers in the gutter. */
    private int lineNumberingStartIndex;

    /** The font used to render line numbers. */
    private Font lineNumberFont;

    /** Renders bookmark icons, breakpoints, error icons, etc. */
    private IconRowHeader iconArea;

    /** Shows lines that are code-foldable. */
    private FoldIndicator foldIndicator;

    /** Listens for events in our text area. */
    private TextAreaListener listener;

    /**
     * Ctor.
     *
     * @param textArea The parent text area.
     */
    public Gutter(TextArea textArea) {
        listener = new TextAreaListener();
        lineNumberColor = Color.gray;
        lineNumberFont = TextArea.getDefaultFont();
        lineNumberingStartIndex = 1;

        setTextArea(textArea);
        setLayout(new BorderLayout());
        if (this.textArea != null) {
            // Enable line numbers our first time through if they give us a text
            // area
            setLineNumbersEnabled(true);
            if (this.textArea instanceof SyntaxTextArea) {
                SyntaxTextArea sta = (SyntaxTextArea) this.textArea;
                setFoldIndicatorEnabled(sta.isCodeFoldingEnabled());
            }
        }

        setBorder(new GutterBorder(0, 0, 0, 1)); // Assume ltr

        Color bg = null;
        if (textArea != null) {
            bg = textArea.getBackground(); // May return null if image bg
        }
        setBackground(bg != null ? bg : Color.WHITE);
    }

    /**
     * Adds an icon that tracks an offset in the document, and is displayed
     * adjacent to the line numbers. This is useful for marking things such as
     * source code errors.
     *
     * @param line The line to track (zero-based).
     * @param icon The icon to display. This should be small (say 16x16).
     * @return A tag for this icon. This can later be used in a call to {@link
     *         #removeTrackingIcon(IGutterIconInfo)} to remove this icon.
     * @throws BadLocationException If <code>offs</code> is an invalid offset
     *         into the text area.
     * @see #addLineTrackingIcon(int, Icon, String)
     * @see #addOffsetTrackingIcon(int, Icon)
     * @see #removeTrackingIcon(IGutterIconInfo)
     */
    public IGutterIconInfo addLineTrackingIcon(int line, Icon icon)
            throws BadLocationException {
        return addLineTrackingIcon(line, icon, null);
    }

    /**
    * Adds an icon that tracks an offset in the document, and is displayed
    * adjacent to the line numbers. This is useful for marking things such as
    * source code errors.
    *
    * @param line The line to track (zero-based).
    * @param icon The icon to display.  This should be small (say 16x16).
    * @param tip An optional tool tip for the icon.
    * @return A tag for this icon. This can later be used in a call to
    *         {@link #removeTrackingIcon(IGutterIconInfo)} to remove this icon.
    * @throws BadLocationException If <code>offs</code> is an invalid offset
    *         into the text area.
    * @see #addLineTrackingIcon(int, Icon)
    * @see #addOffsetTrackingIcon(int, Icon)
    * @see #removeTrackingIcon(IGutterIconInfo)
    */
    public IGutterIconInfo addLineTrackingIcon(int line, Icon icon, String tip)
            throws BadLocationException {
        int offs = textArea.getLineStartOffset(line);
        return addOffsetTrackingIcon(offs, icon, tip);
    }

    /**
     * Adds an icon that tracks an offset in the document, and is displayed
     * adjacent to the line numbers. This is useful for marking things such as
     * source code errors.
     *
     * @param offs The offset to track.
     * @param icon The icon to display. This should be small (say 16x16).
     * @return A tag for this icon.
     * @throws BadLocationException If <code>offs</code> is an invalid offset
     *         into the text area.
     * @see #addOffsetTrackingIcon(int, Icon, String)
     * @see #addLineTrackingIcon(int, Icon)
     * @see #removeTrackingIcon(IGutterIconInfo)
     */
    public IGutterIconInfo addOffsetTrackingIcon(int offs, Icon icon)
            throws BadLocationException {
        return addOffsetTrackingIcon(offs, icon, null);
    }

    /**
     * Adds an icon that tracks an offset in the document, and is displayed
     * adjacent to the line numbers. This is useful for marking things such as
     * source code errors.
     *
     * @param offs The offset to track.
     * @param icon The icon to display. This should be small (say 16x16).
     * @param tip An optional tool tip for the icon.
     * @return A tag for this icon.
     * @throws BadLocationException If <code>offs</code> is an invalid offset
     *         into the text area.
     * @see #addOffsetTrackingIcon(int, Icon)
     * @see #addLineTrackingIcon(int, Icon)
     * @see #removeTrackingIcon(IGutterIconInfo)
     */
    public IGutterIconInfo addOffsetTrackingIcon(int offs, Icon icon, String tip)
            throws BadLocationException {
        return iconArea.addOffsetTrackingIcon(offs, icon, tip);
    }

    /**
     * Clears the active line range.
     *
     * @see #setActiveLineRange(int, int)
     */
    private void clearActiveLineRange() {
        iconArea.clearActiveLineRange();
    }

    /**
     * Returns the color used to paint the active line range, if any.
     *
     * @return The color.
     * @see #setActiveLineRangeColor(Color)
     */
    public Color getActiveLineRangeColor() {
        return iconArea.getActiveLineRangeColor();
    }

    /**
     * Returns the icon to use for bookmarks.
     *
     * @return The icon to use for bookmarks.  If this is <code>null</code>,
     *         bookmarking is effectively disabled.
     * @see #setBookmarkIcon(Icon)
     * @see #isBookmarkingEnabled()
     */
    public Icon getBookmarkIcon() {
        return iconArea.getBookmarkIcon();
    }

    /**
     * Returns the bookmarks known to this gutter.
     *
     * @return The bookmarks. If there are no bookmarks, an empty array is
     *         returned.
     */
    public IGutterIconInfo[] getBookmarks() {
        return iconArea.getBookmarks();
    }

    /**
     * Returns the color of the "border" line.
     *
     * @return The color.
     * @see #setBorderColor(Color)
     */
    public Color getBorderColor() {
        return ((GutterBorder) getBorder()).getColor();
    }

    /**
     * @return The background color used by the (default) fold icons.
     * @see #setFoldBackground(Color)
     */
    public Color getFoldBackground() {
        return foldIndicator.getFoldIconBackground();
    }

    /**
     * @return The foreground color of the fold indicator.
     * @see #setFoldIndicatorForeground(Color)
     */
    public Color getFoldIndicatorForeground() {
        return foldIndicator.getForeground();
    }

    /**
     * Returns the color to use to paint line numbers.
     *
     * @return The color used when painting line numbers.
     * @see #setLineNumberColor(Color)
     */
    public Color getLineNumberColor() {
        return lineNumberColor;
    }

    /**
     * @return The font used for line numbers.
     * @see #setLineNumberFont(Font)
     */
    public Font getLineNumberFont() {
        return lineNumberFont;
    }

    /**
     * Returns the starting line's line number. The default value is
     * <code>1</code>.
     *
     * @return The index
     * @see #setLineNumberingStartIndex(int)
     */
    public int getLineNumberingStartIndex() {
        return lineNumberingStartIndex;
    }

    /**
     * Returns <code>true</code> if the line numbers are enabled and visible.
     *
     * @return Whether or not line numbers are visible.
     */
    public boolean getLineNumbersEnabled() {
        for (int i = 0; i < getComponentCount(); i++) {
            if (getComponent(i) == lineNumberList) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns whether tool tips are displayed showing the contents of collapsed
     * fold regions when the mouse hovers over a +/- icon.
     *
     * @return Whether these tool tips are displayed.
     * @see #setShowCollapsedRegionToolTips(boolean)
     */
    public boolean getShowCollapsedRegionToolTips() {
        return foldIndicator.getShowCollapsedRegionToolTips();
    }

    /**
     * Returns the tracking icons at the specified view position.
     *
     * @param p The view position.
     * @return The tracking icons at that position. If there are no tracking
     *         icons there, this will be an empty array.
     * @throws BadLocationException If <code>p</code> is invalid.
     */
    public IGutterIconInfo[] getTrackingIcons(Point p)
            throws BadLocationException {
        int offs = textArea.viewToModel(new Point(0, p.y));
        int line = textArea.getLineOfOffset(offs);
        return iconArea.getTrackingIcons(line);
    }

    /**
     * @return Whether the fold indicator is enabled.
     * @see #setFoldIndicatorEnabled(boolean)
     */
    public boolean isFoldIndicatorEnabled() {
        for (int i = 0; i < getComponentCount(); i++) {
            if (getComponent(i) == foldIndicator) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return Whether bookmarking is enabled.
     * @see #setBookmarkingEnabled(boolean)
     */
    public boolean isBookmarkingEnabled() {
        return iconArea.isBookmarkingEnabled();
    }

    /** @return Whether the icon row header is enabled. */
    public boolean isIconRowHeaderEnabled() {
        for (int i = 0; i < getComponentCount(); i++) {
            if (getComponent(i) == iconArea) {
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the specified tracking icon.
     *
     * @param tag A tag for an icon in the gutter, as returned from either
     *            {@link #addLineTrackingIcon(int, Icon)} or {@link
     *            #addOffsetTrackingIcon(int, Icon)}.
     * @see #removeAllTrackingIcons()
     * @see #addLineTrackingIcon(int, Icon)
     * @see #addOffsetTrackingIcon(int, Icon)
     */
    public void removeTrackingIcon(IGutterIconInfo tag) {
        iconArea.removeTrackingIcon(tag);
    }

    /**
     * Removes all tracking icons.
     *
     * @see #removeTrackingIcon(IGutterIconInfo)
     * @see #addOffsetTrackingIcon(int, Icon)
     */
    public void removeAllTrackingIcons() {
        iconArea.removeAllTrackingIcons();
    }

    /**
     * Sets the color to use to render active line ranges.
     *
     * @param color The color to use. If this is null, then the default color is
     *              used.
     * @see #getActiveLineRangeColor()
     * @see #DEFAULT_ACTIVE_LINE_RANGE_COLOR
     */
    public void setActiveLineRangeColor(Color color) {
        iconArea.setActiveLineRangeColor(color);
    }

    /**
     * Highlights a range of lines in the icon area. This, of course, will only
     * be visible if the icon area is visible.
     *
     * @param startLine The start of the line range.
     * @param endLine The end of the line range.
     * @see #clearActiveLineRange()
     */
    private void setActiveLineRange(int startLine, int endLine) {
        iconArea.setActiveLineRange(startLine, endLine);
    }

    /**
     * Sets the icon to use for bookmarks.
     *
     * @param icon The new bookmark icon. If this is <code>null</code>,
     *             bookmarking is effectively disabled.
     * @see #getBookmarkIcon()
     * @see #isBookmarkingEnabled()
     */
    public void setBookmarkIcon(Icon icon) {
        iconArea.setBookmarkIcon(icon);
    }

    /**
     * Sets whether bookmarking is enabled. Note that a bookmarking icon must be
     * set via {@link #setBookmarkIcon(Icon)} before bookmarks are truly
     * enabled.
     *
     * @param enabled Whether bookmarking is enabled.
     * @see #isBookmarkingEnabled()
     * @see #setBookmarkIcon(Icon)
     */
    public void setBookmarkingEnabled(boolean enabled) {
        iconArea.setBookmarkingEnabled(enabled);
        if (enabled && !isIconRowHeaderEnabled()) {
            setIconRowHeaderEnabled(true);
        }
    }

    /**
     * Sets the color for the "border" line.
     *
     * @param color The new color.
     * @see #getBorderColor()
     */
    public void setBorderColor(Color color) {
        ((GutterBorder) getBorder()).setColor(color);
        repaint();
    }

    /** {@inheritDoc} */
    public void setComponentOrientation(ComponentOrientation o) {
        // Reuse the border to preserve its color
        if (o.isLeftToRight()) {
            ((GutterBorder) getBorder()).setEdges(0, 0, 0, 1);
        } else {
            ((GutterBorder) getBorder()).setEdges(0, 1, 0, 0);
        }
        super.setComponentOrientation(o);
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
        if (foldIndicator != null) {
            foldIndicator.setFoldIcons(collapsedIcon, expandedIcon);
        }
    }

    /**
     * Toggles whether the fold indicator is enabled.
     *
     * @param enabled Whether the fold indicator should be enabled.
     * @see #isFoldIndicatorEnabled()
     */
    public void setFoldIndicatorEnabled(boolean enabled) {
        if (foldIndicator != null) {
            if (enabled) {
                add(foldIndicator, BorderLayout.LINE_END);
            } else {
                remove(foldIndicator);
            }
            revalidate();
        }
    }

    /**
     * Sets the background color used by the (default) fold icons.
     *
     * @param bg The new background color.
     * @see #getFoldBackground()
     */
    public void setFoldBackground(Color bg) {
        if (bg == null) {
            bg = FoldIndicator.DEFAULT_FOLD_BACKGROUND;
        }
        foldIndicator.setFoldIconBackground(bg);
    }

    /**
     * Sets the foreground color used by the fold indicator.
     *
     * @param fg The new fold indicator foreground.
     * @see #getFoldIndicatorForeground()
     */
    public void setFoldIndicatorForeground(Color fg) {
        if (fg == null) {
            fg = FoldIndicator.DEFAULT_FOREGROUND;
        }
        foldIndicator.setForeground(fg);
    }

    /**
     * Toggles whether the icon row header (used for breakpoints, bookmarks,
     * etc.) is enabled.
     *
     * @param enabled Whether the icon row header is enabled.
     * @see #isIconRowHeaderEnabled()
     */
    void setIconRowHeaderEnabled(boolean enabled) {
        if (iconArea != null) {
            if (enabled) {
                add(iconArea, BorderLayout.LINE_START);
            } else {
                remove(iconArea);
            }
            revalidate();
        }
    }

    /**
     * Sets the color to use to paint line numbers.
     *
     * @param color The color to use when painting line numbers.
     * @see #getLineNumberColor()
     */
    public void setLineNumberColor(Color color) {
        if (color != null && !color.equals(lineNumberColor)) {
            lineNumberColor = color;
            if (lineNumberList != null) {
                lineNumberList.setForeground(color);
            }
        }
    }

    /**
     * Sets the font used for line numbers.
     *
     * @param font The font to use.  This cannot be <code>null</code>.
     * @see #getLineNumberFont()
     */
    public void setLineNumberFont(Font font) {
        if (font == null) {
            throw new IllegalArgumentException("font cannot be null");
        }
        if (!font.equals(lineNumberFont)) {
            lineNumberFont = font;
            if (lineNumberList != null) {
                lineNumberList.setFont(font);
            }
        }
    }

    /**
     * Sets the starting line's line number.  The default value is
     * <code>1</code>. Applications can call this method to change this value
     * if they are displaying a subset of lines in a file, for example.
     *
     * @param index The new index.
     * @see #getLineNumberingStartIndex()
     */
    public void setLineNumberingStartIndex(int index) {
        if (index != lineNumberingStartIndex) {
            lineNumberingStartIndex = index;
            lineNumberList.setLineNumberingStartIndex(index);
        }
    }

    /**
     * Toggles whether or not line numbers are visible.
     *
     * @param enabled Whether or not line numbers should be visible.
     * @see #getLineNumbersEnabled()
     */
    void setLineNumbersEnabled(boolean enabled) {
        if (lineNumberList != null) {
            if (enabled) {
                add(lineNumberList);
            } else {
                remove(lineNumberList);
            }
            revalidate();
        }
    }

    /**
     * Toggles whether tool tips should be displayed showing the contents of
     * collapsed fold regions when the mouse hovers over a +/- icon.
     *
     * @param show Whether to show these tool tips.
     * @see #getShowCollapsedRegionToolTips()
     */
    public void setShowCollapsedRegionToolTips(boolean show) {
        if (foldIndicator != null) {
            foldIndicator.setShowCollapsedRegionToolTips(show);
        }
    }

    /**
     * Sets the text area being displayed. This will clear any tracking icons
     * currently displayed.
     *
     * @param textArea The text area.
     */
    void setTextArea(TextArea textArea) {
        if (this.textArea != null) {
            listener.uninstall();
        }

        if (textArea != null) {
            TextAreaEditorKit kit = (TextAreaEditorKit) textArea.getUI()
                    .getEditorKit(textArea);

            if (lineNumberList == null) {
                lineNumberList = kit.createLineNumberList(textArea);
                lineNumberList.setFont(getLineNumberFont());
                lineNumberList.setForeground(getLineNumberColor());
                lineNumberList
                        .setLineNumberingStartIndex(getLineNumberingStartIndex());
            } else {
                lineNumberList.setTextArea(textArea);
            }
            if (iconArea == null) {
                iconArea = kit.createIconRowHeader(textArea);
            } else {
                iconArea.setTextArea(textArea);
            }
            if (foldIndicator == null) {
                foldIndicator = new FoldIndicator(textArea);
            } else {
                foldIndicator.setTextArea(textArea);
            }

            listener.install(textArea);
        }

        this.textArea = textArea;
    }

    /**
     * Programatically toggles whether there is a bookmark for the specified
     * line. If bookmarking is not enabled, this method does nothing.
     *
     * @param line The line.
     * @return Whether a bookmark is now at the specified line.
     * @throws BadLocationException If <code>line</code> is an invalid line
     *         number in the text area.
     */
    public boolean toggleBookmark(int line) throws BadLocationException {
        return iconArea.toggleBookmark(line);
    }

    public void setBorder(Border border) {
        if (border instanceof GutterBorder) {
            super.setBorder(border);
        }
    }

    /**
     * The border used by the gutter.
     * 
     * @author D. Campione
     * 
     */
    private static class GutterBorder extends EmptyBorder {

        private static final long serialVersionUID = 4508773552231820960L;

        private Color color;
        private Rectangle visibleRect;

        public GutterBorder(int top, int left, int bottom, int right) {
            super(top, left, bottom, right);
            color = new Color(221, 221, 221);
            visibleRect = new Rectangle();
        }

        public Color getColor() {
            return color;
        }

        public void paintBorder(Component c, Graphics g, int x, int y,
                int width, int height) {
            visibleRect = g.getClipBounds(visibleRect);
            if (visibleRect == null) {
                visibleRect = ((JComponent) c).getVisibleRect();
            }
            g.setColor(color);
            if (left == 1) {
                g.drawLine(0, visibleRect.y, 0, visibleRect.y
                        + visibleRect.height);
            } else {
                g.drawLine(width - 1, visibleRect.y, width - 1, visibleRect.y
                        + visibleRect.height);
            }
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public void setEdges(int top, int left, int bottom, int right) {
            this.top = top;
            this.left = left;
            this.bottom = bottom;
            this.right = right;
        }
    }

    /**
     * Listens for the text area resizing.
     * 
     * @author D. Campione
     * 
     */
    /*
     * This is necessary to keep child components the same height as the text
     * area. The worse case is when the user toggles word-wrap and it changes
     * the height of the text area. In that case, if we listen for the
     * "lineWrap" property change, we get notified BEFORE the text area decides
     * on its new size, thus we cannot resize properly. We listen instead for
     * ComponentEvents so we change size after the text area has resized
     */
    private class TextAreaListener extends ComponentAdapter
            implements
                DocumentListener,
                PropertyChangeListener,
                IActiveLineRangeListener {

        private boolean installed;

        /**
         * Modifies the "active line range" that is painted in this component.
         *
         * @param e Information about the new "active line range".
         */
        public void activeLineRangeChanged(ActiveLineRangeEvent e) {
            if (e.getMin() == -1) {
                clearActiveLineRange();
            } else {
                setActiveLineRange(e.getMin(), e.getMax());
            }
        }

        public void changedUpdate(DocumentEvent e) {
        }

        public void componentResized(ComponentEvent e) {
            revalidate();
        }

        protected void handleDocumentEvent(DocumentEvent e) {
            for (int i = 0; i < getComponentCount(); i++) {
                AbstractGutterComponent agc = (AbstractGutterComponent) getComponent(i);
                agc.handleDocumentEvent(e);
            }
        }

        public void insertUpdate(DocumentEvent e) {
            handleDocumentEvent(e);
        }

        public void install(TextArea textArea) {
            if (installed) {
                uninstall();
            }
            textArea.addComponentListener(this);
            textArea.getDocument().addDocumentListener(this);
            textArea.addPropertyChangeListener(this);
            if (textArea instanceof SyntaxTextArea) {
                SyntaxTextArea sta = (SyntaxTextArea) textArea;
                sta.addActiveLineRangeListener(this);
                sta.getFoldManager().addPropertyChangeListener(this);
            }
            installed = true;
        }

        public void propertyChange(PropertyChangeEvent e) {
            String name = e.getPropertyName();

            // If they change the text area's font, we need to update cell
            // heights to match the font's height
            if ("font".equals(name)
                    || SyntaxTextArea.SYNTAX_SCHEME_PROPERTY.equals(name)) {
                for (int i = 0; i < getComponentCount(); i++) {
                    AbstractGutterComponent agc = (AbstractGutterComponent) getComponent(i);
                    agc.lineHeightsChanged();
                }
            }

            // If they toggle whether code folding is enabled..
            else if (SyntaxTextArea.CODE_FOLDING_PROPERTY.equals(name)) {
                boolean foldingEnabled = ((Boolean) e.getNewValue())
                        .booleanValue();
                if (lineNumberList != null) { // Its size depends on folding
                    lineNumberList.updateCellWidths();
                }
                setFoldIndicatorEnabled(foldingEnabled);
            }

            // If code folds are updated..
            else if (FoldManager.PROPERTY_FOLDS_UPDATED.equals(name)) {
                repaint();
            }
        }

        public void removeUpdate(DocumentEvent e) {
            handleDocumentEvent(e);
        }

        public void uninstall() {
            if (installed) {
                textArea.removeComponentListener(this);
                textArea.getDocument().removeDocumentListener(this);
                if (textArea instanceof SyntaxTextArea) {
                    SyntaxTextArea sta = (SyntaxTextArea) textArea;
                    sta.removeActiveLineRangeListener(this);
                    sta.getFoldManager().removePropertyChangeListener(this);
                }
                installed = false;
            }
        }
    }
}