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

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JTextArea;
import javax.swing.event.CaretEvent;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.TextUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.StyleContext;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxUtilities;

/**
 * This is the base class for <code>TextArea</code>; basically it's just an
 * extension of <code>javax.swing.JTextArea</code> adding a bunch of properties.
 * <p>
 *
 * This class is only supposed to be overridden by <code>TextArea</code>.
 *
 * @author D. Campione
 *
 */
public abstract class TextAreaBase extends JTextArea {

    private static final long serialVersionUID = 4696859672360311137L;

    public static final String BACKGROUND_IMAGE_PROPERTY = "background.image";
    public static final String CURRENT_LINE_HIGHLIGHT_COLOR_PROPERTY = "TA.currentLineHighlightColor";
    public static final String CURRENT_LINE_HIGHLIGHT_FADE_PROPERTY = "TA.currentLineHighlightFade";
    public static final String HIGHLIGHT_CURRENT_LINE_PROPERTY = "TA.currentLineHighlight";
    public static final String ROUNDED_SELECTION_PROPERTY = "TA.roundedSelection";

    private boolean tabsEmulatedWithSpaces; // If true, tabs will be expanded to spaces

    private boolean highlightCurrentLine; // If true, the current line is highlighted
    private Color currentLineColor; // The color used to highlight the current line
    private boolean marginLineEnabled; // If true, paint a "margin line"
    private Color marginLineColor; // The color used to paint the margin line
    private int marginLineX; // The x-location of the margin line
    private int marginSizeInChars; // How many 'm' widths the margin line is over
    private boolean fadeCurrentLineHighlight; // "Fade effect" for current line highlight
    private boolean roundedSelectionEdges;
    private int previousCaretY;
    int currentCaretY; // Used to know when to rehighlight current line

    private IBackgroundPainterStrategy backgroundPainter; // Paints the background

    private TAMouseListener mouseListener;

    private static final Color DEFAULT_CARET_COLOR = new ColorUIResource(255,
            51, 51);
    private static final Color DEFAULT_CURRENT_LINE_HIGHLIGHT_COLOR = new Color(
            255, 255, 170);
    private static final Color DEFAULT_MARGIN_LINE_COLOR = new Color(255, 224,
            224);
    private static final int DEFAULT_TAB_SIZE = 4;
    private static final int DEFAULT_MARGIN_LINE_POSITION = 80;

    /** Ctor. */
    public TextAreaBase() {
        init();
    }

    /**
     * Ctor.
     *
     * @param doc The document for the editor.
     */
    public TextAreaBase(AbstractDocument doc) {
        super(doc);
        init();
    }

    /**
     * Ctor.
     *
     * @param text The initial text to display.
     */
    public TextAreaBase(String text) {
        init();
        setText(text);
    }

    /**
     * Ctor.
     *
     * @param rows The number of rows to display.
     * @param cols The number of columns to display.
     * @throws IllegalArgumentException If either <code>rows</code> or
     *         <code>cols</code> is negative.
     */
    public TextAreaBase(int rows, int cols) {
        super(rows, cols);
        init();
    }

    /**
     * Ctor.
     *
     * @param text The initial text to display.
     * @param rows The number of rows to display.
     * @param cols The number of columns to display.
     * @throws IllegalArgumentException If either <code>rows</code> or
     *         <code>cols</code> is negative.
     */
    public TextAreaBase(String text, int rows, int cols) {
        super(rows, cols);
        init();
        setText(text);
    }

    /**
     * Ctor.
     *
     * @param doc The document for the editor.
     * @param text The initial text to display.
     * @param rows The number of rows to display.
     * @param cols The number of columns to display.
     * @throws IllegalArgumentException If either <code>rows</code> or
     *         <code>cols</code> is negative.
     */
    public TextAreaBase(AbstractDocument doc, String text, int rows, int cols) {
        super(doc, null, rows, cols);
        init();
        setText(text);
    }

    /**
     * Adds listeners that listen for changes to the current line, so we can
     * update our "current line highlight".
     */
    private void addCurrentLineHighlightListeners() {
        boolean add = true;
        MouseMotionListener[] mouseMotionListeners = getMouseMotionListeners();
        for (int i = 0; i < mouseMotionListeners.length; i++) {
            if (mouseMotionListeners[i] == mouseListener) {
                add = false;
                break;
            }
        }
        if (add == true) {
            addMouseMotionListener(mouseListener);
        }
        MouseListener[] mouseListeners = getMouseListeners();
        for (int i = 0; i < mouseListeners.length; i++) {
            if (mouseListeners[i] == mouseListener) {
                add = false;
                break;
            }
        }
        if (add == true) {
            addMouseListener(mouseListener);
        }
    }

    /**
     * Converts all instances of a number of spaces equal to a tab size into a
     * tab in this text area.
     *
     * @see #convertTabsToSpaces
     * @see #getTabsEmulated
     * @see #setTabsEmulated
     */
    public void convertSpacesToTabs() {
        int caretPosition = getCaretPosition();
        int tabSize = getTabSize();
        String tabInSpaces = "";
        for (int i = 0; i < tabSize; i++) {
            tabInSpaces += " ";
        }
        String text = getText();
        setText(text.replaceAll(tabInSpaces, "\t"));
        int newDocumentLength = getDocument().getLength();

        // Place the caret back in its proper position
        if (caretPosition < newDocumentLength) {
            setCaretPosition(caretPosition);
        } else {
            setCaretPosition(newDocumentLength - 1);
        }
    }

    /**
     * Converts all instances of a tab into a number of spaces equivalent to a
     * tab in this text area.
     *
     * @see #convertSpacesToTabs
     * @see #getTabsEmulated
     * @see #setTabsEmulated
     */
    public void convertTabsToSpaces() {
        int caretPosition = getCaretPosition();
        int tabSize = getTabSize();
        StringBuilder tabInSpaces = new StringBuilder();
        for (int i = 0; i < tabSize; i++) {
            tabInSpaces.append(' ');
        }
        String text = getText();
        setText(text.replaceAll("\t", tabInSpaces.toString()));

        // Put caret back at same place in document
        setCaretPosition(caretPosition);
    }

    /**
     * Returns the caret event/mouse listener for <code>TextArea</code>s.
     *
     * @return The caret event/mouse listener.
     */
    protected abstract TAMouseListener createMouseListener();

    /**
     * Returns the a real UI to install on this text component. Subclasses can
     * override this method to return an extended version of
     * <code>TextAreaUI</code>.
     *
     * @return The UI.
     */
    protected abstract TextAreaUI createTextAreaUI();

    /**
     * Forces the current line highlight to be repainted. This hack is necessary
     * for those situations when the view (appearance) changes but the caret's
     * location hasn't (and thus the current line highlight coordinates won't
     * get changed). Examples of this are when word wrap is toggled and when
     * syntax styles are changed in an <code>SyntaxTextArea</code>.
     */
    protected void forceCurrentLineHighlightRepaint() {
        // Check isShowing() to prevent BadLocationException in constructor if
        // linewrap is set to true
        if (isShowing()) {
            // Changing previousCaretY makes us sure to get a repaint
            previousCaretY = -1;
            // Trick it into checking for the need to repaint by firing a false
            // caret event
            fireCaretUpdate(mouseListener);
        }
    }

    /**
     * Returns the <code>java.awt.Color</code> used as the background color for
     * this text area. If a <code>java.awt.Image</code> image is currently being
     * used instead, <code>null</code> is returned.
     *
     * @return The current background color, or <code>null</code> if an image is
     *         currently the background.
     */
    @Override
    public final Color getBackground() {
        Object bg = getBackgroundObject();
        return (bg instanceof Color) ? (Color) bg : null;
    }

    /**
     * Returns the image currently used for the background. If the current
     * background is currently a <code>java.awt.Color</code> and not a
     * <code>java.awt.Image</code>, then <code>null</code> is returned.
     *
     * @return A <code>java.awt.Image</code> used for the background, or
     *         <code>null</code> if the background is not an image.
     * @see #setBackgroundImage
     */
    public final Image getBackgroundImage() {
        Object bg = getBackgroundObject();
        return (bg instanceof Image) ? (Image) bg : null;
    }

    /**
     * Returns the <code>Object</code> representing the background for all
     * documents in this tabbed pane; either a <code>java.awt.Color</code> or a
     * <code>java.lang.Image</code> containing the image used as the background
     * for this text area.
     *
     * @return The <code>Object</code> used for the background.
     * @see #setBackgroundObject(Object newBackground)
     */
    public final Object getBackgroundObject() {
        if (backgroundPainter == null) {
            return null;
        }
        return (backgroundPainter instanceof ImageBackgroundPainterStrategy) ? (Object) ((ImageBackgroundPainterStrategy) backgroundPainter)
                .getMasterImage()
                : (Object) ((ColorBackgroundPainterStrategy) backgroundPainter)
                        .getColor();
    }

    /**
     * Gets the line number that the caret is on.
     *
     * @return The zero-based line number that the caret is on.
     */
    public final int getCaretLineNumber() {
        try {
            return getLineOfOffset(getCaretPosition());
        } catch (BadLocationException ble) {
            return 0; // Never happens
        }
    }

    /**
     * Gets the position from the beginning of the current line that the caret
     * is on.
     *
     * @return The zero-based position from the beginning of the current line
     *         that the caret is on.
     */
    public final int getCaretOffsetFromLineStart() {
        try {
            int pos = getCaretPosition();
            return pos - getLineStartOffset(getLineOfOffset(pos));
        } catch (BadLocationException ble) {
            return 0; // Never happens
        }
    }

    /**
     * Returns the color being used to highlight the current line. Note that if
     * highlighting the current line is turned off, you will not be seeing this
     * highlight.
     *
     * @return The color being used to highlight the current line.
     * @see #getHighlightCurrentLine()
     * @see #setHighlightCurrentLine(boolean)
     * @see #setCurrentLineHighlightColor
     */
    public Color getCurrentLineHighlightColor() {
        return currentLineColor;
    }

    /** @return The default caret color. */
    public static final Color getDefaultCaretColor() {
        return DEFAULT_CARET_COLOR;
    }

    /**
     * Returns the "default" color for highlighting the current line. Note that
     * this color was chosen only because it looks nice (to me) against a white
     * background.
     *
     * @return The default color for highlighting the current line.
     */
    public static final Color getDefaultCurrentLineHighlightColor() {
        return DEFAULT_CURRENT_LINE_HIGHLIGHT_COLOR;
    }

    /** @return The default font for text areas. */
    public static final Font getDefaultFont() {
        // Use StyleContext to get a composite font for better Asian language
        // support; see Sun bug S282887
        StyleContext sc = StyleContext.getDefaultStyleContext();
        Font font = null;
        int os = SyntaxUtilities.getOS();

        if (os == SyntaxUtilities.OS_MAC_OSX) {
            // Snow Leopard (1.6) uses Menlo as default monospaced font,
            // pre-Snow Leopard used Monaco
            font = sc.getFont("Menlo", Font.PLAIN, 12);
            if (!"Menlo".equals(font.getFamily())) {
                font = sc.getFont("Monaco", Font.PLAIN, 12);
                if (!"Monaco".equals(font.getFamily())) { // Shouldn't happen
                    font = sc.getFont("Monospaced", Font.PLAIN, 13);
                }
            }
        } else {
            // Consolas added in Vista, used by VS2010+
            font = sc.getFont("Consolas", Font.PLAIN, 13);
            if (!"Consolas".equals(font.getFamily())) {
                font = sc.getFont("Monospaced", Font.PLAIN, 13);
            }
        }

        return font;
    }

    /** @return The default foreground color for text in this text area. */
    public static final Color getDefaultForeground() {
        return Color.BLACK;
    }

    /**
     * Returns the default color for the margin line.
     *
     * @return The default margin line color.
     * @see #getMarginLineColor()
     * @see #setMarginLineColor(Color)
     */
    public static final Color getDefaultMarginLineColor() {
        return DEFAULT_MARGIN_LINE_COLOR;
    }

    /**
     * @return The default margin line position.
     * @see #getMarginLinePosition
     * @see #setMarginLinePosition
     */
    public static final int getDefaultMarginLinePosition() {
        return DEFAULT_MARGIN_LINE_POSITION;
    }

    /**
     * Returns the default tab size, in spaces.
     *
     * @return The default tab size.
     */
    public static final int getDefaultTabSize() {
        return DEFAULT_TAB_SIZE;
    }

    /**
     * @return Whether the current line highlight is faded.
     * @see #setFadeCurrentLineHighlight
     */
    public boolean getFadeCurrentLineHighlight() {
        return fadeCurrentLineHighlight;
    }

    /**
     * @return Whether or not the current line is highlighted.
     * @see #setHighlightCurrentLine(boolean)
     * @see #getCurrentLineHighlightColor
     * @see #setCurrentLineHighlightColor
     */
    public boolean getHighlightCurrentLine() {
        return highlightCurrentLine;
    }

    /** @return The last offset of the line that the caret is currently on. */
    public final int getLineEndOffsetOfCurrentLine() {
        try {
            return getLineEndOffset(getCaretLineNumber());
        } catch (BadLocationException ble) {
            return 0; // Never happens
        }
    }

    /** @return The height of a line of text in this text area. */
    public int getLineHeight() {
        return getRowHeight();
    }

    /**
     * Returns the offset of the first character of the line that the caret is
     * on.
     *
     * @return The first offset of the line that the caret is currently on.
     */
    public final int getLineStartOffsetOfCurrentLine() {
        try {
            return getLineStartOffset(getCaretLineNumber());
        } catch (BadLocationException ble) {
            return 0; // Never happens
        }
    }

    /**
     * Returns the color used to paint the margin line.
     *
     * @return The margin line color.
     * @see #setMarginLineColor(Color)
     */
    public Color getMarginLineColor() {
        return marginLineColor;
    }

    /**
     * Returns the margin line position (in pixels) from the left-hand side of
     * the text area.
     *
     * @return The margin line position.
     * @see #getDefaultMarginLinePosition
     * @see #setMarginLinePosition
     */
    public int getMarginLinePixelLocation() {
        return marginLineX;
    }

    /**
     * Returns the margin line position (which is the number of 'm' widths in
     * the current font the margin line is over).
     *
     * @return The margin line position.
     * @see #getDefaultMarginLinePosition
     * @see #setMarginLinePosition
     */
    public int getMarginLinePosition() {
        return marginSizeInChars;
    }

    /**
     * Returns whether selection edges are rounded in this text area.
     *
     * @return Whether selection edges are rounded.
     * @see #setRoundedSelectionEdges(boolean)
     */
    public boolean getRoundedSelectionEdges() {
        return roundedSelectionEdges;
    }

    /**
     * Returns whether or not tabs are emulated with spaces (i.e., "soft" tabs).
     *
     * @return <code>true</code> if tabs are emulated with spaces;
     *         <code>false</code> if they aren't.
     * @see #setTabsEmulated
     */
    public boolean getTabsEmulated() {
        return tabsEmulatedWithSpaces;
    }

    /** Initializes this text area. */
    protected void init() {
        // Sets the UI. Note that setUI() is overridden in TextArea to only
        // update the popup menu; this method must be called to set the real UI.
        // This is done because the look and feel of an TextArea is independent
        // of the installed Java look and feels
        setTextAreaUI(createTextAreaUI());

        // So we get notified when the component is resized
        enableEvents(AWTEvent.COMPONENT_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);

        // Defaults for various properties
        setHighlightCurrentLine(true);
        setCurrentLineHighlightColor(getDefaultCurrentLineHighlightColor());
        setMarginLineEnabled(true);
        setMarginLineColor(getDefaultMarginLineColor());
        setMarginLinePosition(getDefaultMarginLinePosition());
        setBackgroundObject(Color.WHITE);
        setWrapStyleWord(true); // We only support wrapping at word boundaries
        setTabSize(4);
        setForeground(Color.BLACK);
        setTabsEmulated(true);

        // Stuff needed by the caret listener below
        previousCaretY = currentCaretY = getInsets().top;

        // Stuff to highlight the current line
        mouseListener = createMouseListener();
        // Also acts as a focus listener so we can update our shared actions
        // (cut, copy, etc.. on the popup menu)
        addFocusListener(mouseListener);
        addCurrentLineHighlightListeners();
    }

    /**
     * @return Whether or not the margin line is being painted.
     * @see #setMarginLineEnabled
     */
    public boolean isMarginLineEnabled() {
        return marginLineEnabled;
    }

    /**
     * Paints the text area.
     *
     * @param g The graphics context with which to paint.
     */
    @Override
    protected void paintComponent(Graphics g) {
        backgroundPainter.paint(g, getVisibleRect());

        // Paint the main part of the text area
        TextUI ui = getUI();
        if (ui != null) {
            // Not allowed to modify g, so make a copy
            Graphics scratchGraphics = g.create();
            try {
                ui.update(scratchGraphics, this);
            } finally {
                scratchGraphics.dispose();
            }
        }
    }

    /** Updates the current line highlight location. */
    protected void possiblyUpdateCurrentLineHighlightLocation() {
        int width = getWidth();
        int lineHeight = getLineHeight();
        int dot = getCaretPosition();

        // If we're wrapping lines we need to check the actual y-coordinate of
        // the caret, not just the line number, since a single logical line can
        // span multiple physical lines
        if (getLineWrap()) {
            try {
                Rectangle temp = modelToView(dot);
                if (temp != null) {
                    currentCaretY = temp.y;
                }
            } catch (BadLocationException ble) {
                ExceptionDialog.notifyException(ble); // Should never happen
            }
        } else { // No line wrap - we can simply check the line number (quicker)
                 // Modified for code folding requirements
            try {
                Rectangle temp = modelToView(dot);
                if (temp != null) {
                    currentCaretY = temp.y;
                }
            } catch (BadLocationException ble) {
                ExceptionDialog.notifyException(ble); // Should never happen
            }
        }

        // Repaint current line (to fill in entire highlight), and old line (to
        // erase entire old highlight) if necessary. Always repaint current line
        // in case selection is added or removed
        repaint(0, currentCaretY, width, lineHeight);
        if (previousCaretY != currentCaretY) {
            repaint(0, previousCaretY, width, lineHeight);
        }

        previousCaretY = currentCaretY;
    }

    /**
     * Overridden so we can tell when the text area is resized and update the
     * current-line highlight, if necessary (i.e., if it is enabled and if
     * lineWrap is enabled.
     *
     * @param e The component event about to be sent to all registered
     *          <code>ComponentListener</code>s.
     */
    @Override
    protected void processComponentEvent(ComponentEvent e) {
        // In line wrap mode, resizing the text area means that the caret's
        // "line" could change - not to a different logical line, but a
        // different physical one. So, here we force a repaint of the current
        // line's highlight if necessary
        if (e.getID() == ComponentEvent.COMPONENT_RESIZED
                && getLineWrap() == true && getHighlightCurrentLine()) {
            previousCaretY = -1; // So we are sure to repaint
            fireCaretUpdate(mouseListener);
        }

        super.processComponentEvent(e);
    }

    /**
     * Sets the background color of this text editor. Note that this is
     * equivalent to calling <code>setBackgroundObject(bg)</code>.
     *
     * NOTE: the opaque property is set to <code>true</code> when the background
     * is set to a color (by this method). When an image is used for the
     * background, opaque is set to false. This is because we perform better
     * when setOpaque is true, but if we use an image for the background when
     * opaque is true, we get on-screen garbage when the user scrolls via the
     * arrow keys. Thus we need setOpaque to be false in that case.<p> You never
     * have to change the opaque property yourself; it is always done for you.
     *
     * @param bg The color to use as the background color.
     */
    @Override
    public void setBackground(Color bg) {
        Object oldBG = getBackgroundObject();
        if (oldBG instanceof Color) { // Just change color of strategy
            ((ColorBackgroundPainterStrategy) backgroundPainter).setColor(bg);
        } else { // Was an image painter..
            backgroundPainter = new ColorBackgroundPainterStrategy(bg);
        }
        setOpaque(true);
        firePropertyChange("background", oldBG, bg);
        repaint();
    }

    /**
     * Sets this image as the background image. This method fires a property
     * change event of type {@link #BACKGROUND_IMAGE_PROPERTY}.<p>
     *
     * NOTE: the opaque property is set to <code>true</code> when the background
     * is set to a color. When an image is used for the background (by this
     * method), opaque is set to false. This is because we perform better when
     * setOpaque is true, but if we use an image for the background when opaque
     * is true, we get on-screen garbage when the user scrolls via the arrow
     * keys. Thus we need setOpaque to be false in that case.<p> You never have
     * to change the opaque property yourself; it is always done for you.
     *
     * @param image The image to use as this text area's background.
     * @see #getBackgroundImage
     */
    public void setBackgroundImage(Image image) {
        Object oldBG = getBackgroundObject();
        if (oldBG instanceof Image) { // Just change image being displayed
            ((ImageBackgroundPainterStrategy) backgroundPainter)
                    .setImage(image);
        } else { // Was a color strategy..
            ImageBackgroundPainterStrategy strategy = new BufferedImageBackgroundPainterStrategy(
                    this);
            strategy.setImage(image);
            backgroundPainter = strategy;
        }
        setOpaque(false);
        firePropertyChange(BACKGROUND_IMAGE_PROPERTY, oldBG, image);
        repaint();
    }

    /**
     * Makes the background into this <code>Object</code>.
     *
     * @param newBackground The <code>java.awt.Color</code> or
     *                      <code>java.awt.Image</code> object. If
     *                      <code>newBackground</code> is not either of these,
     *                      the background is set to plain white.
     */
    public void setBackgroundObject(Object newBackground) {
        if (newBackground instanceof Color) {
            setBackground((Color) newBackground);
        } else if (newBackground instanceof Image) {
            setBackgroundImage((Image) newBackground);
        } else {
            setBackground(Color.WHITE);
        }
    }

    /**
     * Sets the color to use to highlight the current line. Note that if
     * highlighting the current line is turned off, you will not be able to see
     * this highlight. This method fires a property change of type {@link
     * #CURRENT_LINE_HIGHLIGHT_COLOR_PROPERTY}.
     *
     * @param color The color to use to highlight the current line.
     * @throws NullPointerException if <code>color</code> is <code>null</code>.
     * @see #getHighlightCurrentLine()
     * @see #setHighlightCurrentLine(boolean)
     * @see #getCurrentLineHighlightColor
     */
    public void setCurrentLineHighlightColor(Color color) {
        if (color == null) {
            throw new NullPointerException();
        }
        if (!color.equals(currentLineColor)) {
            Color old = currentLineColor;
            currentLineColor = color;
            firePropertyChange(CURRENT_LINE_HIGHLIGHT_COLOR_PROPERTY, old,
                    color);
        }
    }

    /**
     * Sets whether the current line highlight should have a "fade" effect. This
     * method fires a property change event of type
     * <code>CURRENT_LINE_HIGHLIGHT_FADE_PROPERTY</code>.
     *
     * @param fade Whether the fade effect should be enabled.
     * @see #getFadeCurrentLineHighlight
     */
    public void setFadeCurrentLineHighlight(boolean fade) {
        if (fade != fadeCurrentLineHighlight) {
            fadeCurrentLineHighlight = fade;
            if (getHighlightCurrentLine()) {
                forceCurrentLineHighlightRepaint();
            }
            firePropertyChange(CURRENT_LINE_HIGHLIGHT_FADE_PROPERTY, !fade,
                    fade);
        }
    }

    /**
     * Sets the font for this text area. This is overridden only so that we can
     * update the size of the "current line highlight" and the location of the
     * "margin line," if necessary.
     *
     * @param font The font to use for this text component.
     */
    @Override
    public void setFont(Font font) {
        super.setFont(font);
        if (font != null) {
            updateMarginLineX();
            if (highlightCurrentLine) {
                possiblyUpdateCurrentLineHighlightLocation();
            }
        }
    }

    /**
     * Sets whether or not the current line is highlighted. This method fires a
     * property change of type {@link #HIGHLIGHT_CURRENT_LINE_PROPERTY}.
     *
     * @param highlight Whether or not to highlight the current line.
     * @see #getHighlightCurrentLine()
     * @see #getCurrentLineHighlightColor
     * @see #setCurrentLineHighlightColor
     */
    public void setHighlightCurrentLine(boolean highlight) {
        if (highlight != highlightCurrentLine) {
            highlightCurrentLine = highlight;
            firePropertyChange(HIGHLIGHT_CURRENT_LINE_PROPERTY, !highlight,
                    highlight);
            repaint(); // Repaint entire width of line
        }
    }

    /**
     * Sets whether or not word wrap is enabled. This is overridden so that the
     * "current line highlight" gets updated if it needs to be.
     *
     * @param wrap Whether or not word wrap should be enabled.
     */
    @Override
    public void setLineWrap(boolean wrap) {
        super.setLineWrap(wrap);
        forceCurrentLineHighlightRepaint();
    }

    /**
     * Overridden to update the current line highlight location.
     *
     * @param insets The new insets.
     */
    @Override
    public void setMargin(Insets insets) {
        Insets old = getInsets();
        int oldTop = old != null ? old.top : 0;
        int newTop = insets != null ? insets.top : 0;
        if (oldTop != newTop) {
            // The entire editor will be automatically repainted if it is
            // visible, so no need to call repaint() or forceCurrentLine..()
            previousCaretY = currentCaretY = newTop;
        }
        super.setMargin(insets);
    }

    /**
     * Sets the color used to paint the margin line.
     *
     * @param color The new margin line color.
     * @see #getDefaultMarginLineColor()
     * @see #getMarginLineColor()
     */
    public void setMarginLineColor(Color color) {
        marginLineColor = color;
        if (marginLineEnabled) {
            Rectangle visibleRect = getVisibleRect();
            repaint(marginLineX, visibleRect.y, 1, visibleRect.height);
        }
    }

    /**
     * Enables or disables the margin line.
     *
     * @param enabled Whether or not the margin line should be enabled.
     * @see #isMarginLineEnabled
     */
    public void setMarginLineEnabled(boolean enabled) {
        if (enabled != marginLineEnabled) {
            marginLineEnabled = enabled;
            if (marginLineEnabled) {
                Rectangle visibleRect = getVisibleRect();
                repaint(marginLineX, visibleRect.y, 1, visibleRect.height);
            }
        }
    }

    /**
     * Sets the number of 'm' widths the margin line is over.
     *
     * @param size The margin size.
     * #see #getDefaultMarginLinePosition
     * @see #getMarginLinePosition
     */
    public void setMarginLinePosition(int size) {
        marginSizeInChars = size;
        if (marginLineEnabled) {
            Rectangle visibleRect = getVisibleRect();
            repaint(marginLineX, visibleRect.y, 1, visibleRect.height);
            updateMarginLineX();
            repaint(marginLineX, visibleRect.y, 1, visibleRect.height);
        }
    }

    /**
     * Sets whether the edges of selections are rounded in this text area. This
     * method fires a property change of type {@link
     * #ROUNDED_SELECTION_PROPERTY}.
     *
     * @param rounded Whether selection edges should be rounded.
     * @see #getRoundedSelectionEdges()
     */
    public void setRoundedSelectionEdges(boolean rounded) {
        if (roundedSelectionEdges != rounded) {
            roundedSelectionEdges = rounded;
            Caret c = getCaret();
            if (c instanceof ConfigurableCaret) {
                ((ConfigurableCaret) c).setRoundedSelectionEdges(rounded);
                if (c.getDot() != c.getMark()) { // e.g., there's is a selection
                    repaint();
                }
            }
            firePropertyChange(ROUNDED_SELECTION_PROPERTY, !rounded, rounded);
        }
    }

    /**
     * Sets the UI for this <code>TextArea</code>. Note that, for instances of
     * <code>TextArea</code>, <code>setUI</code> only updates the popup menu;
     * this is because <code>TextArea</code>s' look and feels are independent of
     * the Java Look and Feel. This method is here so subclasses can set a UI
     * (subclass of <code>TextAreaUI</code>) if they have to.
     *
     * @param ui The new UI.
     * @see #setUI
     */
    protected void setTextAreaUI(TextAreaUI ui) {
        super.setUI(ui);

        // Workaround as setUI makes the text area opaque, even if we don't want
        // it to be
        setOpaque(getBackgroundObject() instanceof Color);
    }

    /**
     * Changes whether or not tabs should be emulated with spaces (i.e., soft
     * tabs). Note that this affects all tabs inserted AFTER this call, not tabs
     * already in the document. For that, see {@link #convertTabsToSpaces} and
     * {@link #convertSpacesToTabs}.
     *
     * @param areEmulated Whether or not tabs should be emulated with spaces.
     * @see #convertSpacesToTabs
     * @see #convertTabsToSpaces
     * @see #getTabsEmulated
     */
    public void setTabsEmulated(boolean areEmulated) {
        tabsEmulatedWithSpaces = areEmulated;
    }

    /**
     * Workaround, sets the number of characters to expand tabs to. This will be
     * multiplied by the maximum advance for variable width fonts. A
     * PropertyChange event ("tabSize") is fired when the tab size changes.
     *
     * @param size Number of characters to expand to.
     */
    @Override
    public void setTabSize(int size) {
        super.setTabSize(size);
        boolean b = getLineWrap();
        setLineWrap(!b);
        setLineWrap(b);
    }

    /**
     * This is here so subclasses such as <code>SyntaxTextArea</code> that have
     * multiple fonts can define exactly what it means, for example, for the
     * margin line to be "80 characters" over.
     */
    protected void updateMarginLineX() {
        Font font = getFont();
        if (font == null) {
            marginLineX = 0;
            return;
        }
        marginLineX = getFontMetrics(font).charWidth('m') * marginSizeInChars;
    }

    /**
     * Returns the y-coordinate of the specified line.
     *
     * @param line The line number.
     * @return The y-coordinate of the top of the line, or <code>-1</code> if
     *         this text area doesn't yet have a positive size or the line is
     *         hidden (i.e. from folding).
     * @throws BadLocationException If <code>line</code> isn't a valid line
     *         number for this document.
     */
    public int yForLine(int line) throws BadLocationException {
        return ((TextAreaUI) getUI()).yForLine(line);
    }

    /**
     * Returns the y-coordinate of the line containing an offset.
     *
     * @param offs The offset info the document.
     * @return The y-coordinate of the top of the offset, or <code>-1</code> if
     *         this text area doesn't yet have a positive size or the line is
     *         hidden (i.e. from folding).
     * @throws BadLocationException If <code>offs</code> isn't a valid offset
     *         into the document.
     */
    public int yForLineContaining(int offs) throws BadLocationException {
        return ((TextAreaUI) getUI()).yForLineContaining(offs);
    }

    /**
     *
     *
     * @author D. Campione
     *
     */
    protected class TAMouseListener extends CaretEvent implements
            MouseListener, MouseMotionListener, FocusListener {

        private static final long serialVersionUID = -6346308896782464836L;

        TAMouseListener(TextAreaBase textArea) {
            super(textArea);
        }

        @Override
        public void focusGained(FocusEvent e) {
        }

        @Override
        public void focusLost(FocusEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public int getDot() {
            return dot;
        }

        @Override
        public int getMark() {
            return mark;
        }

        protected int dot;
        protected int mark;
    }
}