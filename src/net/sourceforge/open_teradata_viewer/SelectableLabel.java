/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C), D. Campione
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

package net.sourceforge.open_teradata_viewer;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.plaf.TextUI;
import javax.swing.text.html.HTMLDocument;

import net.sourceforge.open_teradata_viewer.util.UIUtil;

/**
 * A "label" component that can have its text selected.
 *
 * @author D. Campione
 * 
 */
public class SelectableLabel extends JTextPane {

    private static final long serialVersionUID = -2287203345430218639L;

    /**
     * The color to use when rendering hyperlinks. If non-<code>null</code>,
     * this overrides Swing's default.
     */
    private Color hyperlinkForeground;

    /** Ctor. */
    public SelectableLabel() {
        this(null);
    }

    /**
     * Ctor.
     *
     * @param text The text to display. This can be HTML.
     */
    public SelectableLabel(String text) {
        setText(text);
        labelize();
    }

    /** Makes this editor pane look like a label. */
    private void labelize() {

        setBorder(null);
        setEditable(false);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0)); // Needed for Nimbus

        // In case text component fonts are different from label fonts
        Font font = UIManager.getFont("Label.font");
        if (font == null) { // Not required to be defined
            font = new JLabel().getFont();
        }
        setFont(font);

        // In case text component fg is different from label fg.
        Color fg = UIManager.getColor("Label.foreground");
        if (fg != null) {
            setForeground(fg);
        }

        // Some LaFs have different selection colors for editable vs.
        // non-editable text components; we want to use the editable color
        Color sel = UIManager.getColor("TextField.selectionBackground");
        if (sel != null) {
            setSelectionColor(sel);
        }
    }

    /**
     * Sets the color to use for hyperlinks, when HTML is displayed in this
     * component. This can be used to override the default color used by
     * Swing, if you think a custom color looks better in your LookAndFeel.
     *
     * @param fg The new foreground. Setting this to <code>null</code> will
     *        cause the default color to be used.
     */
    public void setHyperlinkForeground(Color fg) {
        this.hyperlinkForeground = fg;
    }

    /**
     * Sets the text displayed by this label.
     *
     * @param text The text to display. If this begins with
     *        "<code>&lt;html&gt;</code>", the text will be rendered as
     *        HTML.
     */
    @Override
    public void setText(String text) {
        if (text != null && text.startsWith("<html>")) {
            setContentType("text/html");
            updateDefaultHtmlFont();
        } else {
            setContentType("text/plain");
        }
        super.setText(text);
    }

    /**
     * Overridden to keep our special properties, to keep us looking like
     * a label across <code>LookAndFeels</code>.
     */
    @Override
    public void setUI(TextUI ui) {
        super.setUI(ui);
        labelize();
        if (getDocument() instanceof HTMLDocument) {
            updateDefaultHtmlFont();
        }
    }

    /**
     * Add a CSS rule to force body tags to use the default label font
     * instead of the value in javax.swing.text.html.default.css. This was
     * adapted from:
     * http://explodingpixels.wordpress.com/2008/10/28/make-jeditorpane-use-the-system-font/
     */
    private void updateDefaultHtmlFont() {
        Color fg = UIManager.getColor("Label.foreground");
        Font font = UIManager.getFont("Label.font");
        // These properties are defined by all standard LaFs, even Nimbus (!),
        // but you never know what crazy LaFs there are..
        if (font == null) {
            font = new JLabel().getFont();
        }
        if (fg == null) {
            fg = new JLabel().getForeground();
        }

        // When rendering HTML, the JEditorPane foreground color is ignored;
        // we have to set it in the CSS as well. This is only needed for LAF's
        // whose label color isn't (extremely close to) black
        HTMLDocument doc = (HTMLDocument) getDocument();
        String bodyRule = "body { font-family: " + font.getFamily() + "; font-size: " + font.getSize() + "pt"
                + "; color: " + UIUtil.getHTMLFormatForColor(fg) + "; }";
        doc.getStyleSheet().addRule(bodyRule);

        // If this LaF looks to be light-text-on-dark-background,
        // use a light color for hyperlinks
        Color linkFG = hyperlinkForeground;
        if (linkFG == null) {
            linkFG = UIUtil.getHyperlinkForeground();
        }
        if (linkFG != null) {
            String aRule = "a { color: " + UIUtil.getHTMLFormatForColor(linkFG) + "; }";
            doc.getStyleSheet().addRule(aRule);
        }
    }
}