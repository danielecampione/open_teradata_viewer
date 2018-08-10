/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JToolBar;

/**
 *
 * 
 * @author D. Campione
 * 
 */

public class JXToolBar extends JToolBar {
    private static final long serialVersionUID = 962637193253696613L;

    public <T extends JComponent> T addFixed(T component) {
        if (!(component instanceof JButton)) {
            UISupport.setPreferredHeight(component, 18);
        }

        Dimension preferredSize = component.getPreferredSize();
        component.setMinimumSize(preferredSize);
        component.setMaximumSize(preferredSize);

        add(component);

        return component;
    }

    public Component add(Component component) {
        if (!(component instanceof AbstractButton)) {
            UISupport.setPreferredHeight(component, 18);
        }

        return super.add(component);
    }

    public void addGlue() {
        add(Box.createHorizontalGlue());
    }

    public void addRelatedGap() {
        addSpace(3);
    }

    public void addUnrelatedGap() {
        addSeparator();
    }

    public void addLabeledFixed(String string, JComponent component) {
        addFixed(new JLabel(string));
        addSeparator(new Dimension(3, 3));
        addFixed(component);
    }

    public void addSpace(int i) {
        addSeparator(new Dimension(i, 1));
    }
}
