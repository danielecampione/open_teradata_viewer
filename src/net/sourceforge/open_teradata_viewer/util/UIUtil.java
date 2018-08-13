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
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.Spring;
import javax.swing.SpringLayout;
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

    /** A very common border that can be shared across many components. */
    private static final Border EMPTY_5_BORDER = BorderFactory
            .createEmptyBorder(5, 5, 5, 5);

    /**
     * Fixes the orientation of the renderer of a combo box. Swing standard LaFs
     * don't handle this on their own.
     *
     * @param combo The combo box.
     */
    public static void fixComboOrientation(JComboBox combo) {
        ListCellRenderer r = combo.getRenderer();
        if (r instanceof Component) {
            ComponentOrientation o = ComponentOrientation.getOrientation(Locale
                    .getDefault());
            ((Component) r).setComponentOrientation(o);
        }
    }

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
     * Returns an empty border of width 5 on all sides. Since this is a very
     * common border in GUI's, the border returned is a singleton.
     *
     * @return The border.
     */
    public static Border getEmpty5Border() {
        return EMPTY_5_BORDER;
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
     * Used by makeSpringCompactGrid. This is ripped off directly from
     * <code>SpringUtilities.java</code> in the Sun Java Tutorial.
     *
     * @param parent The container whose layout must be an instance of
     *        <code>SpringLayout</code>.
     * @return The spring constraints for the specified component contained in
     *         <code>parent</code>.
     */
    private static final SpringLayout.Constraints getConstraintsForCell(
            int row, int col, Container parent, int cols) {
        SpringLayout layout = (SpringLayout) parent.getLayout();
        Component c = parent.getComponent(row * cols + col);
        return layout.getConstraints(c);
    }

    /**
     * This method is ripped off from <code>SpringUtilities.java</code> found on
     * Sun's Java Tutorial pages. It takes a component whose layout is
     * <code>SpringLayout</code> and organizes the components it contains into
     * a nice grid.
     * Aligns the first <code>rows</code> * <code>cols</code> components of
     * <code>parent</code> in a grid. Each component in a column is as wide as
     * the maximum preferred width of the components in that column; height is
     * similarly determined for each row. The parent is made just big enough to
     * fit them all.
     *
     * @param parent The container whose layout is <code>SpringLayout</code>.
     * @param rows The number of rows of components to make in the container.
     * @param cols The number of columns of components to make.
     * @param initialX The x-location to start the grid at.
     * @param initialY The y-location to start the grid at.
     * @param xPad The x-padding between cells.
     * @param yPad The y-padding between cells.
     */
    public static final void makeSpringCompactGrid(Container parent, int rows,
            int cols, int initialX, int initialY, int xPad, int yPad) {
        SpringLayout layout;
        try {
            layout = (SpringLayout) parent.getLayout();
        } catch (ClassCastException cce) {
            System.err.println("The first argument to makeCompactGrid "
                    + "must use SpringLayout.");
            return;
        }

        // Align all cells in each column and make them the same width
        Spring x = Spring.constant(initialX);
        for (int c = 0; c < cols; c++) {
            Spring width = Spring.constant(0);
            for (int r = 0; r < rows; r++) {
                width = Spring.max(width,
                        getConstraintsForCell(r, c, parent, cols).getWidth());
            }
            for (int r = 0; r < rows; r++) {
                SpringLayout.Constraints constraints = getConstraintsForCell(r,
                        c, parent, cols);
                constraints.setX(x);
                constraints.setWidth(width);
            }
            x = Spring.sum(x, Spring.sum(width, Spring.constant(xPad)));
        }

        // Align all cells in each row and make them the same height
        Spring y = Spring.constant(initialY);
        for (int r = 0; r < rows; r++) {
            Spring height = Spring.constant(0);
            for (int c = 0; c < cols; c++) {
                height = Spring.max(height,
                        getConstraintsForCell(r, c, parent, cols).getHeight());
            }
            for (int c = 0; c < cols; c++) {
                SpringLayout.Constraints constraints = getConstraintsForCell(r,
                        c, parent, cols);
                constraints.setY(y);
                constraints.setHeight(height);
            }
            y = Spring.sum(y, Spring.sum(height, Spring.constant(yPad)));
        }

        // Set the parent's size
        SpringLayout.Constraints pCons = layout.getConstraints(parent);
        pCons.setConstraint(SpringLayout.SOUTH, y);
        pCons.setConstraint(SpringLayout.EAST, x);
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

    /**
     * Returns a button with the specified text.
     *
     * @param text The label for the component.
     * @return The button.
     */
    public static final JButton newButton(String text) {
        JButton b = new JButton(text);
        return b;
    }

    /**
     * Returns an <code>JLabel</code> with the specified text.
     *
     * @param text The label for the component.
     * @param labelFor The component the label is labeling.
     * @return The <code>JLabel</code>.
     */
    public static final JLabel newLabel(String text, Component labelFor) {
        JLabel label = new JLabel(text);
        if (labelFor != null) {
            label.setLabelFor(labelFor);
        }
        return label;
    }
}