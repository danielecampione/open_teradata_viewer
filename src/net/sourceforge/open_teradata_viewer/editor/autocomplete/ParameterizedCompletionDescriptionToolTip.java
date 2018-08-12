/*
 * Open Teradata Viewer ( editor autocomplete )
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

package net.sourceforge.open_teradata_viewer.editor.autocomplete;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import net.sourceforge.open_teradata_viewer.editor.syntax.PopupWindowDecorator;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxUtilities;
import net.sourceforge.open_teradata_viewer.editor.syntax.focusabletip.TipUtil;

/**
 * A "tool tip" that displays information on the function or method currently
 * being entered.
 *
 * @author D. Campione
 * 
 */
class ParameterizedCompletionDescriptionToolTip {

    /** The actual tool tip. */
    private JWindow tooltip;

    /** The label that holds the description. */
    private JLabel descLabel;

    /** The completion being described. */
    private IParameterizedCompletion pc;

    /**
     * Ctor.
     *
     * @param owner The parent window.
     * @param ac The parent auto-completion.
     * @param pc The completion being described.
     */
    public ParameterizedCompletionDescriptionToolTip(Window owner,
            ParameterizedCompletionContext context, AutoCompletion ac,
            IParameterizedCompletion pc) {
        tooltip = new JWindow(owner);

        this.pc = pc;

        descLabel = new JLabel();
        descLabel.setBorder(BorderFactory.createCompoundBorder(
                TipUtil.getToolTipBorder(),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)));
        descLabel.setOpaque(true);
        descLabel.setBackground(TipUtil.getToolTipBackground());
        // It appears that if a JLabel is set as a content pane directly, when
        // using the JDK's opacity API's, it won't paint its background, even
        // if label.setOpaque(true) is called. You have to have a container
        // underneath it for it to paint its background. Thus, we embed our
        // label in a parent JPanel to handle this case
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(descLabel);
        tooltip.setContentPane(panel);

        // Give apps a chance to decorate us with drop shadows, etc..
        PopupWindowDecorator decorator = PopupWindowDecorator.get();
        if (decorator != null) {
            decorator.decorate(tooltip);
        }

        updateText(0);

        tooltip.setFocusableWindowState(false);
    }

    /**
     * Returns whether this tool tip is visible.
     *
     * @return Whether this tool tip is visible.
     * @see #setVisible(boolean)
     */
    public boolean isVisible() {
        return tooltip.isVisible();
    }

    /**
     * Sets the location of this tool tip relative to the given rectangle.
     *
     * @param r The visual position of the caret (in screen coordinates).
     */
    public void setLocationRelativeTo(Rectangle r) {
        // Multi-monitor support - make sure the completion window (and
        // description window, if applicable) both fit in the same window in a
        // multi-monitor environment. To do this, we decide which monitor the
        // rectangle "r" is in, and use that one (just pick top-left corner as
        // the defining point)
        Rectangle screenBounds = Util.getScreenBoundsForPoint(r.x, r.y);

        // Try putting our stuff "above" the caret first
        int y = r.y - 5 - tooltip.getHeight();
        if (y < 0) {
            y = r.y + r.height + 5;
        }

        // Get x-coordinate of completions. Try to align left edge with the
        // caret first
        int x = r.x;
        if (x < screenBounds.x) {
            x = screenBounds.x;
        } else if (x + tooltip.getWidth() > screenBounds.x + screenBounds.width) { // Completions don't fit
            x = screenBounds.x + screenBounds.width - tooltip.getWidth();
        }

        tooltip.setLocation(x, y);
    }

    /**
     * Toggles the visibility of this tool tip.
     *
     * @param visible Whether this tool tip should be visible.
     * @see #isVisible()
     */
    public void setVisible(boolean visible) {
        tooltip.setVisible(visible);
    }

    /**
     * Updates the text in the tool tip to have the current parameter displayed
     * in bold.
     *
     * @param selectedParam The index of the selected parameter.
     * @return Whether the text needed to be updated.
     */
    public boolean updateText(int selectedParam) {
        StringBuffer sb = new StringBuffer("<html>");
        int paramCount = pc.getParamCount();
        for (int i = 0; i < paramCount; i++) {
            if (i == selectedParam) {
                sb.append("<b>");
            }

            // Some parameter types may have chars in them unfriendly to HTML
            // (such as type parameters in Java). We need to take care to escape
            // these
            String temp = pc.getParam(i).toString();
            sb.append(SyntaxUtilities.escapeForHtml(temp, "<br>", false));

            if (i == selectedParam) {
                sb.append("</b>");
            }
            if (i < paramCount - 1) {
                sb.append(pc.getProvider().getParameterListSeparator());
            }
        }

        if (selectedParam >= 0 && selectedParam < paramCount) {
            IParameterizedCompletion.Parameter param = pc
                    .getParam(selectedParam);
            String desc = param.getDescription();
            if (desc != null) {
                sb.append("<br>");
                sb.append(desc);
            }
        }

        descLabel.setText(sb.toString());
        tooltip.pack();

        return true;
    }

    /**
     * Updates the <tt>LookAndFeel</tt> of this window and the description
     * window.
     */
    public void updateUI() {
        SwingUtilities.updateComponentTreeUI(tooltip);
    }
}