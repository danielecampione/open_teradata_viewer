/*
 * Open Teradata Viewer ( editor )
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

package net.sourceforge.open_teradata_viewer.editor;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.ToolTipManager;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.EmptyIcon;

/**
 * A panel that displays an 8x8 decorative icon for a component, such as a
 * text field or combo box. This can be used to display error icons, warning
 * icons, etc..
 *
 * @author D. Campione
 * @see AssistanceIconPanel
 * 
 */
public class DecorativeIconPanel extends JPanel {

    private static final long serialVersionUID = -8549519183124443734L;

    /**
     * The width of this icon panel, to help align the components we're
     * listening to with other combo boxes or text fields without a
     * DecorativeIconPanel.
     */
    public static final int WIDTH = 8;

    private JLabel iconLabel;
    private boolean showIcon;
    private String tip;

    protected static final EmptyIcon EMPTY_ICON = new EmptyIcon(WIDTH);

    /** Ctor. */
    public DecorativeIconPanel() {
        setLayout(new BorderLayout());
        iconLabel = new JLabel(EMPTY_ICON) {
            @Override
            public String getToolTipText(MouseEvent e) {
                return showIcon ? tip : null;
            }
        };
        iconLabel.setVerticalAlignment(SwingConstants.TOP);
        ToolTipManager.sharedInstance().registerComponent(iconLabel);
        add(iconLabel, BorderLayout.NORTH);
    }

    /**
     * Returns the icon being displayed.
     *
     * @return The icon.
     * @see #setIcon(Icon)
     */
    public Icon getIcon() {
        return iconLabel.getIcon();
    }

    /**
     * Returns whether the icon (if any) is being rendered.
     *
     * @return Whether the icon is being rendered.
     * @see #setShowIcon(boolean)
     */
    public boolean getShowIcon() {
        return showIcon;
    }

    /**
     * Returns the tool tip displayed when the mouse hovers over the icon.
     * If the icon is not being displayed, this parameter is ignored.
     *
     * @return The tool tip text.
     * @see #setToolTipText(String)
     */
    @Override
    public String getToolTipText() {
        return tip;
    }

    /**
     * Paints any child components. Overridden so the user can explicitly hide
     * the icon.
     *
     * @param g The graphics context.
     * @see #setShowIcon(boolean)
     */
    @Override
    protected void paintChildren(Graphics g) {
        if (showIcon) {
            super.paintChildren(g);
        }
    }

    /**
     * Sets the icon to display.
     *
     * @param icon The new icon to display.
     * @see #getIcon()
     */
    public void setIcon(Icon icon) {
        if (icon == null) {
            icon = EMPTY_ICON;
        }
        iconLabel.setIcon(icon);
    }

    /**
     * Toggles whether the icon should be shown.
     *
     * @param show Whether to show the icon.
     * @see #getShowIcon()
     */
    public void setShowIcon(boolean show) {
        if (show != showIcon) {
            showIcon = show;
            repaint();
        }
    }

    /**
     * Sets the tool tip text to display when the mouse is over the icon.
     * This parameter is ignored if the icon is not being displayed.
     *
     * @param tip The tool tip text to display.
     * @see #getToolTipText()
     */
    @Override
    public void setToolTipText(String tip) {
        this.tip = tip;
    }
}