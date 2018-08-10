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
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.Icon;
import javax.swing.JPanel;

import net.sourceforge.open_teradata_viewer.util.SwingUtil;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class StatusBar extends JPanel {

    private static final long serialVersionUID = 1915099907714734676L;

    public StatusBar() {
        super(new FlowLayout(FlowLayout.LEFT, 2, 2));
        init();
    }

    private void init() {

    }

    public StatusPanel addPanel(String name, String text, Icon icon, int width,
            StatusPanel panel) {
        panel.setName(name);
        add(panel);
        if (width > 0) {
            panel.setMinimumSize(new Dimension(width, panel.getHeight()));
            panel.setPreferredSize(panel.getMinimumSize());
        }
        return panel;
    }

    public StatusPanel addPanel(String name, String text, Icon icon, int width) {
        return addPanel(name, text, icon, width, new StatusPanel(name, text,
                icon));
    }

    public StatusPanel addPanel(String name, String text) {
        return addPanel(name, text, null, -1);
    }

    public StatusPanel addPanel(String name) {
        return addPanel(name, "", null, -1);
    }

    public StatusPanel addPanel(String name, StatusPanel panel) {
        return addPanel(name, "", null, -1, panel);
    }

    public void hidePanel(String name) {
        getPanel(name).setVisible(false);
    }

    public void hidePanels(String[] names) {
        for (int i = 0; i < names.length; i++) {
            hidePanel(names[i]);
        }
    }

    public StatusPanel removePanel(String name) {
        for (int i = 0; i < getComponentCount(); i++) {
            if (getComponent(i).getName().equals(name)
                    && getComponent(i) instanceof StatusPanel) {
                StatusPanel panel = (StatusPanel) getComponent(i);
                remove(i);
                return panel;
            }
        }
        return null;
    }

    public StatusPanel getPanel(String name) {
        for (int i = 0; i < getComponentCount(); i++) {
            if (getComponent(i) instanceof StatusPanel
                    && name.equalsIgnoreCase(getComponent(i).getName())) {
                return (StatusPanel) getComponent(i);
            }
        }
        return null;
    }

    public int getIndexOf(Component item) {
        return SwingUtil.getIndexOf(this, item);
    }

    public void addBefore(Component comp, Component newItem) {
        SwingUtil.addBefore(this, comp, newItem);
        refresh();
    }

    public void addAfter(Component comp, Component newItem) {
        SwingUtil.addAfter(this, comp, newItem);
        refresh();
    }

    public void refresh() {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                revalidate();
                repaint();
            }
        });
    }
}
