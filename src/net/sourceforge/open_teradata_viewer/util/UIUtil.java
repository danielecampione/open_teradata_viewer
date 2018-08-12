/*
 * Open Teradata Viewer ( util )
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

package net.sourceforge.open_teradata_viewer.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.UIResource;
import javax.swing.table.TableCellRenderer;

/**
 * Utility methods for <code>net.sourceforge.open_teradata_viewer</code> GUI
 * components.
 *
 * @author D. Campione
 * 
 */
public class UIUtil {

    /**
     * Fixes the orientation of the default JTable renderers (for Object, Number
     * and Boolean) to match that of the current <code>Locale</code> (e.g.
     * <code>ComponentOrientation.getOrientation(table.getLocale())</code>).
     * This is needed because <code>DefaultTableCellRenderer</code> does not do
     * this, even though <code>DefaultListCellRenderer</code> and
     * <code>DefaultTreeCellRenderer</code> do.<p>
     *
     * See Sun bug http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6333197
     * for more information.
     *
     * @param table The table to update.
     * 
     */
    public static void fixJTableRendererOrientations(JTable table) {
        ComponentOrientation o = ComponentOrientation.getOrientation(table
                .getLocale());
        TableCellRenderer r = table.getDefaultRenderer(Object.class);
        if (r instanceof Component) { // Never null for JTable
            Component c = (Component) r;
            c.setComponentOrientation(o);
        }
        r = table.getDefaultRenderer(Number.class);
        if (r instanceof Component) { // Never null for JTable
            Component c = (Component) r;
            c.setComponentOrientation(o);
        }
        r = table.getDefaultRenderer(Boolean.class);
        if (r instanceof Component) { // Never null for JTable
            Component c = (Component) r;
            c.setComponentOrientation(o);
        }
        if (table.getTableHeader() != null) {
            r = table.getTableHeader().getDefaultRenderer();
            if (r instanceof Component) {
                ((Component) r).applyComponentOrientation(o);
            }
        }
    }

    /**
     * Returns a <code>String</code> of the form "#xxxxxx" good for use in HTML,
     * representing the given color.
     *
     * @param color The color to get a string for.
     * @return The HTML form of the color. If <code>color</code> is
     *         <code>null</code>, <code>#000000</code> is returned.
     */
    public static final String getHTMLFormatForColor(Color color) {
        if (color == null) {
            return "#000000";
        }

        StringBuffer sb = new StringBuffer("#");
        int r = color.getRed();
        if (r < 16) {
            sb.append('0');
        }
        sb.append(Integer.toHexString(r));

        int g = color.getGreen();
        if (g < 16) {
            sb.append('0');
        }
        sb.append(Integer.toHexString(g));

        int b = color.getBlue();
        if (b < 16) {
            sb.append('0');
        }
        sb.append(Integer.toHexString(b));

        return sb.toString();
    }

    /**
     * Tweaks certain LookAndFeels (i.e., Windows XP) to look just a tad more
     * like the native Look.
     */
    public static void installOsSpecificLafTweaks() {
        String lafName = UIManager.getLookAndFeel().getName()
                .toLowerCase(Locale.ENGLISH);
        String os = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);

        // XP has insets between the edge of popup menus and the selection
        if ("windows xp".equals(os) && "windows".equals(lafName)) {
            Border insetsBorder = BorderFactory.createEmptyBorder(2, 3, 2, 3);

            String key = "PopupMenu.border";
            Border origBorder = UIManager.getBorder(key);
            UIResource res = new BorderUIResource.CompoundBorderUIResource(
                    origBorder, insetsBorder);
            UIManager.getLookAndFeelDefaults().put(key, res);
        }
    }

    /**
     * Returns whether the specified color is "light" to use as a foreground.
     * Colors that return <code>true</code> indicate that the current Look and
     * Feel probably uses light text colors on a dark background.
     *
     * @param fg The foreground color.
     * @return Whether it is a "light" foreground color.
     */
    public static final boolean isLightForeground(Color fg) {
        return fg.getRed() > 0xa0 && fg.getGreen() > 0xa0
                && fg.getBlue() > 0xa0;
    }

    /**
     * Make a table use the right grid color on Windows XP / Vista, when using
     * the Windows Look and Feel.
     *
     * @param table The table to update.
     */
    public static void possiblyFixGridColor(JTable table) {
        String laf = UIManager.getLookAndFeel().getClass().getName();
        if (laf.endsWith("WindowsLookAndFeel")) {
            if (Color.white.equals(table.getBackground())) {
                Color gridColor = table.getGridColor();
                if (gridColor != null && gridColor.getRGB() <= 0x808080) {
                    table.setGridColor(new Color(0xe3e3e3));
                }
            }
        }
    }

    /**
     * Sets the rendering hints on a graphics object to those closest to the
     * system's desktop values.<p>
     * 
     * See <a
     * href="http://download.oracle.com/javase/6/docs/api/java/awt/doc-files/DesktopProperties.html">AWT
     * Desktop Properties</a> for more information.
     *
     * @param g2d The graphics context.
     * @return The old rendering hints.
     */
    public static Map setNativeRenderingHints(Graphics2D g2d) {
        Map old = g2d.getRenderingHints();

        // Try to use the rendering hint set that is "native"
        Map hints = (Map) Toolkit.getDefaultToolkit().getDesktopProperty(
                "awt.font.desktophints");
        if (hints != null) {
            g2d.addRenderingHints(hints);
        }

        return old;
    }
}