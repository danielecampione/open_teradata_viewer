/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class Dialog extends JOptionPane {

    private static final long serialVersionUID = -1579318866748692286L;

    public Dialog(Object message, int messageType, int optionType,
            Object[] options, Object initialValue) {
        super(message, messageType, optionType, null, options, initialValue);
    }

    public static int show(String title, Object message, int messageType,
            int optionType) {
        Dialog dialog = new Dialog(message, messageType, optionType, null, null);
        dialog.createDialog(ApplicationFrame.getInstance(), title).setVisible(
                true);
        if (dialog.getValue() == null) {
            return CLOSED_OPTION;
        } else {
            try {
                return ((Number) dialog.getValue()).intValue();
            } catch (ClassCastException cce) {
                return CLOSED_OPTION;
            }
        }
    }

    public static int show(String title, JScrollPane scrollPane,
            int messageType, int optionType) {
        determineSize(scrollPane);
        return show(title, (Object) scrollPane, messageType, optionType);
    }

    public static Object show(String title, Object message, int messageType,
            Object[] options, Object initialValue) {
        Dialog dialog = new Dialog(message, messageType, DEFAULT_OPTION,
                options, initialValue);
        try {
            UISupport.showDialog(dialog.createDialog(
                    ApplicationFrame.getInstance(), title));
        } catch (NoClassDefFoundError e) {
            UISupport.showDialog(dialog.createDialog(null, title));
        }
        return dialog.getValue();
    }

    public static Object show(String title, JScrollPane scrollPane,
            int messageType, Object[] options, Object initialValue) {
        determineSize(scrollPane);
        return show(title, (Object) scrollPane, messageType, options,
                initialValue);
    }

    private static void determineSize(JScrollPane scrollPane) {
        int minWidth = 600;
        int minHeight = 400;
        double scrollBarWidth = new JScrollBar().getPreferredSize().getWidth();
        if (scrollPane.getViewport().getComponent(0) instanceof JList) {
            minWidth = 0;
            minHeight = 0;
            scrollBarWidth = 0;
            JList<?> jList = (JList<?>) scrollPane.getViewport()
                    .getComponent(0);
            jList.setVisibleRowCount(Math.max(15, jList.getModel().getSize()));
        }
        double maxWidth = Toolkit.getDefaultToolkit().getScreenSize()
                .getWidth() * .8;
        double maxHeight = (Toolkit.getDefaultToolkit().getScreenSize()
                .getHeight() - 100) * .8;
        double preferedWidth = scrollPane.getPreferredSize().getWidth()
                + scrollBarWidth;
        double preferedHeight = scrollPane.getPreferredSize().getHeight()
                + scrollBarWidth;
        int width = (int) Math.min(maxWidth, Math.max(minWidth, preferedWidth));
        int height = (int) Math.min(maxHeight,
                Math.max(minHeight, preferedHeight));
        scrollPane.setPreferredSize(new Dimension(width, height));
    }

    @Override
    public void selectInitialValue() {
    }
}