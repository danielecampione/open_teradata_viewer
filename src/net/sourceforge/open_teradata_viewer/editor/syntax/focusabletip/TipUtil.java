/*
 * Open Teradata Viewer ( editor syntax focusabletip )
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

package net.sourceforge.open_teradata_viewer.editor.syntax.focusabletip;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.html.HTMLDocument;

import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxUtilities;
import net.sourceforge.open_teradata_viewer.util.Utilities;

/**
 * Static utility methods for focusable tips.
 *
 * @author D. Campione
 * 
 */
public class TipUtil {

    private TipUtil() {
    }

    /**
     * Returns the default background color to use for tool tip windows.
     *
     * @return The default background color.
     */
    public static Color getToolTipBackground() {
        Color c = UIManager.getColor("ToolTip.background");

        // Tooltip.background is wrong color on Nimbus
        boolean isNimbus = isNimbusLookAndFeel();
        if (c == null || isNimbus) {
            c = UIManager.getColor("info"); // Used by Nimbus (and others)
            if (c == null || (isNimbus && isDerivedColor(c))) {
                c = SystemColor.info; // System default
            }
        }

        if (c instanceof ColorUIResource) {
            c = new Color(c.getRGB());
        }
        return c;
    }

    /**
     * Returns the border used by tool tips in this look and feel.
     *
     * @return The border.
     */
    public static Border getToolTipBorder() {
        Border border = UIManager.getBorder("ToolTip.border");

        if (border == null || isNimbusLookAndFeel()) {
            border = UIManager.getBorder("nimbusBorder");
            if (border == null) {
                border = BorderFactory
                        .createLineBorder(SystemColor.controlDkShadow);
            }
        }

        return border;
    }

    /**
     * Returns whether a color is a Nimbus DerivedColor, which is troublesome
     * in that it doesn't use its RGB values (uses HSB instead?) and so
     * querying them is useless.
     *
     * @param c The color to check.
     * @return Whether it is a DerivedColor.
     */
    private static final boolean isDerivedColor(Color c) {
        return c != null && c.getClass().getName().endsWith(".DerivedColor");
    }

    /**
     * Returns whether the Nimbus Look and Feel is installed.
     *
     * @return Whether the current LAF is Nimbus.
     */
    private static final boolean isNimbusLookAndFeel() {
        return UIManager.getLookAndFeel().getName().equals("Nimbus");
    }

    /**
     * Tweaks a <code>JEditorPane</code> so it can be used to render the
     * content in a focusable pseudo-tool tip. It is assumed that the editor
     * pane is using an <code>HTMLDocument</code>.
     *
     * @param textArea The editor pane to tweak.
     */
    public static void tweakTipEditorPane(JEditorPane textArea) {
        // Jump through a few hoops to get things looking nice in Nimbus
        boolean isNimbus = isNimbusLookAndFeel();
        if (isNimbus) {
            Color selBG = textArea.getSelectionColor();
            Color selFG = textArea.getSelectedTextColor();
            textArea.setUI(new javax.swing.plaf.basic.BasicEditorPaneUI());
            textArea.setSelectedTextColor(selFG);
            textArea.setSelectionColor(selBG);
        }

        textArea.setEditable(false); // Required for links to work
        textArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Make selection visible even though we are not (initially) focusable
        textArea.getCaret().setSelectionVisible(true);

        // Set the foreground color. Important because when rendering HTML,
        // default foreground becomes black, which may not match all LAF's (e.g.
        // Substance)
        Color fg = UIManager.getColor("Label.foreground");
        if (fg == null || (isNimbus && isDerivedColor(fg))) {
            fg = SystemColor.textText;
        }
        textArea.setForeground(fg);

        // Make it use the "tool tip" background color
        textArea.setBackground(TipUtil.getToolTipBackground());

        // Force JEditorPane to use a certain font even in HTML.
        // All standard LookAndFeels, even Nimbus, define Label.font
        Font font = UIManager.getFont("Label.font");
        if (font == null) { // Try to make a sensible default
            font = new Font("SansSerif", Font.PLAIN, 12);
        }
        HTMLDocument doc = (HTMLDocument) textArea.getDocument();
        doc.getStyleSheet().addRule(
                "body { font-family: " + font.getFamily() + "; font-size: "
                        + font.getSize() + "pt" + "; color: "
                        + Utilities.getHexString(fg) + "; }");

        // Always add link foreground rule. Unfortunately these CSS rules stack
        // each time the LaF is changed
        Color linkFG = SyntaxUtilities.getHyperlinkForeground();
        doc.getStyleSheet().addRule(
                "a { color: " + Utilities.getHexString(linkFG) + "; }");

        URL url;
        String sURL = "/icons/bullet_black.png";
        Class<?> rootClass = Toolkit.getDefaultToolkit().getClass();
        File file = new File(sURL);
        try {
            if (file.exists()) {
                url = file.toURI().toURL();
            } else {
                url = rootClass.getResource(sURL);
            }
        } catch (MalformedURLException murle) {
            url = null;
        }
        if (url != null) {
            doc.getStyleSheet().addRule(
                    "ul { list-style-image: '" + url.toString() + "'; }");
        }
    }
}