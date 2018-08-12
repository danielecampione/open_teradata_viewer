/*
 * Open Teradata Viewer ( editor syntax )
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

package net.sourceforge.open_teradata_viewer.editor.syntax;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;

import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.InputMapUIResource;
import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;

import net.sourceforge.open_teradata_viewer.editor.TextArea;
import net.sourceforge.open_teradata_viewer.editor.TextAreaUI;

/**
 * UI used by <code>SyntaxTextArea</code>. This allows us to implement syntax
 * highlighting.
 *
 * @author D. Campione
 * 
 */
public class SyntaxTextAreaUI extends TextAreaUI {

    private static final String SHARED_ACTION_MAP_NAME = "SyntaxTextAreaUI.actionMap";
    private static final String SHARED_INPUT_MAP_NAME = "SyntaxTextAreaUI.inputMap";
    private static final EditorKit defaultKit = new SyntaxTextAreaEditorKit();

    public static ComponentUI createUI(JComponent ta) {
        return new SyntaxTextAreaUI(ta);
    }

    /** Ctor. */
    public SyntaxTextAreaUI(JComponent syntaxTextArea) {
        super(syntaxTextArea);
    }

    /**
     * Creates the view for an element.
     *
     * @param elem The element.
     * @return The view.
     */
    public View create(Element elem) {
        TextArea c = getTextArea();
        if (c instanceof SyntaxTextArea) {
            SyntaxTextArea area = (SyntaxTextArea) c;
            View v;
            if (area.getLineWrap())
                v = new WrappedSyntaxView(elem);
            else
                v = new SyntaxView(elem);
            return v;
        }
        return null;
    }

    /**
     * Creates the highlighter to use for syntax text areas.
     *
     * @return The highlighter.
     */
    protected Highlighter createHighlighter() {
        return new SyntaxTextAreaHighlighter();
    }

    /**
     * Returns the name to use to cache/fetch the shared action map. This should
     * be overridden by subclasses if the subclass has its own custom editor kit
     * to install, so its actions get picked up.
     *
     * @return The name of the cached action map.
     */
    protected String getActionMapName() {
        return SHARED_ACTION_MAP_NAME;
    }

    /**
     * Fetches the EditorKit for the UI.
     *
     * @param tc The text component for which this UI is installed.
     * @return The editor capabilities.
     * @see javax.swing.plaf.TextUI#getEditorKit
     */
    public EditorKit getEditorKit(JTextComponent tc) {
        return defaultKit;
    }

    /**
     * Get the InputMap to use for the UI.<p>
     *
     * This method is not named <code>getInputMap()</code> because there is a
     * package-private method in <code>BasicTextAreaUI</code> with that name.
     * Thus, creating a new method with that name causes certain compilers to
     * issue warnings that you are not actually overriding the original method
     * (since it is package-private).
     */
    protected InputMap getTextAreaInputMap() {
        InputMap map = new InputMapUIResource();
        InputMap shared = (InputMap) UIManager.get(SHARED_INPUT_MAP_NAME);
        if (shared == null) {
            shared = new SyntaxTextAreaDefaultInputMap();
            UIManager.put(SHARED_INPUT_MAP_NAME, shared);
        }
        map.setParent(shared);
        return map;
    }

    /**
     * Paints the text area's background.
     *
     * @param g The graphics component on which to paint.
     */
    protected void paintBackground(Graphics g) {
        super.paintBackground(g);
        paintMatchedBracket(g);
    }

    /**
     * Paints the "matched bracket", if any.
     *
     * @param g The graphics context.
     */
    protected void paintMatchedBracket(Graphics g) {
        SyntaxTextArea sta = (SyntaxTextArea) textArea;
        if (sta.isBracketMatchingEnabled()) {
            Rectangle match = sta.getMatchRectangle();
            if (match != null) {
                paintMatchedBracketImpl(g, sta, match);
            }
            if (sta.getPaintMatchedBracketPair()) {
                Rectangle dotRect = sta.getDotRectangle();
                if (dotRect != null) { // Should always be true
                    paintMatchedBracketImpl(g, sta, dotRect);
                }
            }
        }
    }

    private void paintMatchedBracketImpl(Graphics g, SyntaxTextArea sta,
            Rectangle r) {
        // We must add "-1" to the height because otherwise we'll paint below
        // the region that gets invalidated.
        if (sta.getAnimateBracketMatching()) {
            Color bg = sta.getMatchedBracketBGColor();
            if (bg != null) {
                g.setColor(bg);
                g.fillRoundRect(r.x, r.y, r.width, r.height - 1, 5, 5);
            }
            g.setColor(sta.getMatchedBracketBorderColor());
            g.drawRoundRect(r.x, r.y, r.width, r.height - 1, 5, 5);
        } else {
            Color bg = sta.getMatchedBracketBGColor();
            if (bg != null) {
                g.setColor(bg);
                g.fillRect(r.x, r.y, r.width, r.height - 1);
            }
            g.setColor(sta.getMatchedBracketBorderColor());
            g.drawRect(r.x, r.y, r.width, r.height - 1);
        }
    }

    /**
     * Gets called whenever a bound property is changed on this UI's
     * <code>SyntaxTextArea</code>.
     *
     * @param e The property change event.
     */
    protected void propertyChange(PropertyChangeEvent e) {

        String name = e.getPropertyName();

        // If they change the syntax scheme, we must do this so that
        // WrappedSyntaxView(_TEST) updates its child views properly
        if (name.equals(SyntaxTextArea.SYNTAX_SCHEME_PROPERTY)) {
            modelChanged();
        }

        // Everything else is general to all TextAreas
        else {
            super.propertyChange(e);
        }
    }

    /**
     * Updates the view. This should be called when the underlying
     * <code>SyntaxTextArea</code> changes its syntax editing style.
     */
    public void refreshSyntaxHighlighting() {
        modelChanged();
    }

    /**
     * Returns the y-coordinate of the specified line.<p>
     *
     * This method is quicker than using traditional
     * <code>modelToView(int)</code> calls, as the entire bounding box isn't
     * computed.
     */
    public int yForLine(int line) throws BadLocationException {
        Rectangle alloc = getVisibleEditorRect();
        if (alloc != null) {
            ISTAView view = (ISTAView) getRootView(textArea).getView(0);
            return view.yForLine(alloc, line);
        }
        return -1;
    }

    /**
     * Returns the y-coordinate of the line containing a specified offset.<p>
     *
     * This is faster than calling <code>modelToView(offs).y</code>, so it is
     * preferred if you do not need the actual bounding box.
     */
    public int yForLineContaining(int offs) throws BadLocationException {
        Rectangle alloc = getVisibleEditorRect();
        if (alloc != null) {
            ISTAView view = (ISTAView) getRootView(textArea).getView(0);
            return view.yForLineContaining(alloc, offs);
        }
        return -1;
    }
}