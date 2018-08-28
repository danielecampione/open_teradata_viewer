/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer.actions;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import net.sourceforge.open_teradata_viewer.ImageManager;
import net.sourceforge.open_teradata_viewer.ThreadedAction;

/**
 *
 *
 * @author D. Campione
 *
 */
public abstract class CustomAction extends AbstractAction
        implements MouseListener {

    private static final long serialVersionUID = 1753928583474033071L;

    private KeyStroke altKey;

    protected CustomAction(String name) {
        this(name, null, null, null);
    }

    protected CustomAction(String name, String icon, KeyStroke accelerator,
            String shortDescription) {
        super(name);
        if (icon != null) {
            putValue(SMALL_ICON, ImageManager.getImage("/icons/" + icon));
        }
        if (accelerator != null) {
            putValue(ACCELERATOR_KEY, accelerator);
        }
        if (shortDescription != null) {
            putValue(SHORT_DESCRIPTION, shortDescription);
        }
        setEnabled(false);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        new ThreadedAction() {
            @Override
            protected void execute() throws Exception {
                performThreaded(e);
            }
        };
    }

    protected abstract void performThreaded(ActionEvent e) throws Exception;

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && e.getSource() instanceof JList) {
            Container container = (Container) e.getSource();
            while (!(container instanceof JOptionPane)) {
                container = container.getParent();
            }
            JOptionPane optionPane = (JOptionPane) container;
            Object value = optionPane.getInitialValue();
            if (value == null) {
                value = JOptionPane.OK_OPTION;
            }
            optionPane.setValue(value);
            while (!(container instanceof JDialog)) {
                container = container.getParent();
            }
            container.setVisible(false);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /** @see SwingUtil.addAction() */
    public KeyStroke getAltShortCut() {
        return altKey;
    }

    /** @see SwingUtil.addAction() */
    public void setAltShortCut(int keyCode, int modifiers) {
        altKey = KeyStroke.getKeyStroke(keyCode, modifiers);
    }
}