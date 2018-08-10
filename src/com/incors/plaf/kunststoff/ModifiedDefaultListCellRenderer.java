package com.incors.plaf.kunststoff;

/*
 * This code was developed by INCORS GmbH (www.incors.com).
 * It is published under the terms of the GNU Lesser General Public License.
 */

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * The only difference between this class and the DefaultListCellRenderer is that
 * objects of this class are not opaque by default.
 */
public class ModifiedDefaultListCellRenderer extends DefaultListCellRenderer {

    private static final long serialVersionUID = -3319669560194338863L;

    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected,
                cellHasFocus);
        setOpaque(isSelected);
        return this;
    }

}