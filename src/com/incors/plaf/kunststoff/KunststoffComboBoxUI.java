/*
 * Open Teradata Viewer ( look and feel )
 * Copyright (C) 2011, D. Campione
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

package com.incors.plaf.kunststoff;

import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.CellRendererPane;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalComboBoxIcon;
import javax.swing.plaf.metal.MetalComboBoxUI;

/**
 * The KunststoffComboBoxUI is a little bit tricky, but it should work fine in
 * most cases. It currently draws only correctly if the renderer of the combo
 * box is an instance of <code>JComponent</code> and the background color is an
 * instance of <code>ColorUIResource</code>. In a default <code>JComboBox</code>
 * with a default renderer this should be the case.
 * 
 * @author Aljoscha Rittner
 * @author C.J. Kent
 * @author Christian Peter
 * @author Christoph Wilhelms
 * @author Eric Georges
 * @author Gerald Bauer
 * @author Ingo Kegel
 * @author Jamie LaScolea
 * @author <A HREF="mailto:jens@jensn.de">Jens Niemeyer</A>
 * @author Jerason Banes
 * @author Jim Wissner
 * @author Johannes Ernst
 * @author Jonas Kilian
 * @author <A HREF="mailto:julien@izforge.com">Julien Ponge</A>
 * @author Karsten Lentzsch
 * @author Matthew Philips
 * @author Romain Guy
 * @author Sebastian Ferreyra
 * @author Steve Varghese
 * @author Taoufik Romdhane
 * @author Timo Haberkern
 * 
 */
public class KunststoffComboBoxUI extends MetalComboBoxUI {

    public static ComponentUI createUI(JComponent c) {
        return new KunststoffComboBoxUI();
    }

    /**
     * Installs MyMetalComboBoxButton
     */
    protected JButton createArrowButton() {
        JButton button = new MyMetalComboBoxButton(comboBox,
                new MetalComboBoxIcon(), comboBox.isEditable() ? true : false,
                currentValuePane, listBox);
        button.setMargin(new Insets(0, 1, 1, 3));
        return button;
    }

    /**
     * This inner class finally fixed a nasty bug with the combo box. Thanks to
     * Matthew Philips for providing the bugfix.
     * Thanks to Ingo Kegel for fixing two compiling issues for jikes.
     * 
     * @author Aljoscha Rittner
     * @author C.J. Kent
     * @author Christian Peter
     * @author Christoph Wilhelms
     * @author Eric Georges
     * @author Gerald Bauer
     * @author Ingo Kegel
     * @author Jamie LaScolea
     * @author <A HREF="mailto:jens@jensn.de">Jens Niemeyer</A>
     * @author Jerason Banes
     * @author Jim Wissner
     * @author Johannes Ernst
     * @author Jonas Kilian
     * @author <A HREF="mailto:julien@izforge.com">Julien Ponge</A>
     * @author Karsten Lentzsch
     * @author Matthew Philips
     * @author Romain Guy
     * @author Sebastian Ferreyra
     * @author Steve Varghese
     * @author Taoufik Romdhane
     * @author Timo Haberkern
     * 
     */
    private final class MyMetalComboBoxButton
            extends
                javax.swing.plaf.metal.MetalComboBoxButton {

        private static final long serialVersionUID = -467026017297807915L;

        public MyMetalComboBoxButton(JComboBox<?> cb, Icon i, boolean onlyIcon,
                CellRendererPane pane, JList<?> list) {
            super(cb, i, onlyIcon, pane, list);
        }

        public void paintComponent(Graphics g) {
            if (!iconOnly && MyMetalComboBoxButton.this.comboBox != null) {
                boolean isSetRendererOpaque = false;
                ListCellRenderer<?> renderer = MyMetalComboBoxButton.this.comboBox
                        .getRenderer();
                if (renderer instanceof JComponent) {
                    JComponent jRenderer = (JComponent) renderer;
                    if (jRenderer.isOpaque()
                            && jRenderer.getBackground() instanceof ColorUIResource) {
                        isSetRendererOpaque = true; // remember to set the renderer opaque again
                        jRenderer.setOpaque(false);
                    }
                }
                super.paintComponent(g);
                if (isSetRendererOpaque) {
                    ((JComponent) renderer).setOpaque(true);
                }
            } else {
                super.paintComponent(g);
            }
        }

    }
}
