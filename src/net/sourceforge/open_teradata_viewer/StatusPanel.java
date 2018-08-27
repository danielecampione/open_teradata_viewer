/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.border.Border;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class StatusPanel extends JLabel {

    private static final long serialVersionUID = 6601034705971728099L;

    private Border oldBorder;
    private boolean displayActivation = false;

    public StatusPanel(String name, String text, Icon icon) {
        super(text, icon, LEADING);
        setName(name);
        init();
    }

    public StatusPanel(String name, String text) {
        super(text);
        setName(name);
        init();
    }

    public StatusPanel(String name) {
        super();
        setName(name);
        init();
    }

    public StatusPanel() {
        super();
        init();
    }

    private void init() {
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent arg0) {
            }
            public void mouseEntered(MouseEvent arg0) {
                if (displayActivation) {
                    oldBorder = getBorder();
                    setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                }
            }
            public void mouseExited(MouseEvent arg0) {
                if (displayActivation) {
                    setBorder(oldBorder);
                }
            }
            public void mousePressed(MouseEvent arg0) {
            }
            public void mouseReleased(MouseEvent arg0) {
            }
        });
    }

    public boolean isDisplayActivation() {
        return displayActivation;
    }

    public void setDisplayActivation(boolean displayActivation) {
        this.displayActivation = displayActivation;
    }

}
