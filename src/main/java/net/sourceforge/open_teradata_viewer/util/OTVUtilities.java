/*
 * Open Teradata Viewer ( util )
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

package net.sourceforge.open_teradata_viewer.util;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.fife.rsta.ui.DecorativeIconPanel;

/**
 * Collection of tools for use by any of the Open Teradata Viewer components.
 *
 * @author D. Campione
 * 
 */
public class OTVUtilities {

    /**
     * Creates a panel containing the specified component and an (optional)
     * decorative (or assistance) icon panel.
     *
     * @param comp The component.
     * @param iconPanel The icon panel. If this is <code>null</code>, then a
     *        spacer is used.
     * @return The panel.
     */
    public static JPanel createAssistancePanel(JComponent comp, DecorativeIconPanel iconPanel) {
        if (iconPanel == null) {
            iconPanel = new DecorativeIconPanel();
        }
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(iconPanel, BorderLayout.LINE_START);
        panel.add(comp);
        return panel;
    }

    /**
     * Returns the directory in which to load and save user preferences
     * (beyond those saved via the Java preferences API).
     *
     * @return The directory.
     */
    public static File getPreferencesDirectory() {
        return new File(System.getProperty("user.home"));
    }
}