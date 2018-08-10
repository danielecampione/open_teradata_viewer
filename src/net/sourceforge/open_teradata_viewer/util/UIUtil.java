/*
 * Open Teradata Viewer ( util )
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

package net.sourceforge.open_teradata_viewer.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;

import javax.swing.JTable;
import javax.swing.UIManager;
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
}