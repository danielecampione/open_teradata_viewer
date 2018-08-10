/*
 * Open Teradata Viewer ( graphic viewer )
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

package net.sourceforge.open_teradata_viewer.graphicviewer;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class IntegerVerifier extends InputVerifier {

    public IntegerVerifier() {
    }

    public boolean verify(JComponent jcomponent) {
        String s;
        JTextField jtextfield = (JTextField) jcomponent;
        s = jtextfield.getText();
        @SuppressWarnings("unused")
        int i = Integer.parseInt(s);
        return true;
    }

    public boolean shouldYieldFocus(JComponent jcomponent) {
        if (super.shouldYieldFocus(jcomponent)) {
            return true;
        } else {
            jcomponent.setInputVerifier(null);
            JOptionPane.showMessageDialog(null,
                    "An illegal integer value was entered", "Input Error", 0);
            jcomponent.setInputVerifier(this);
            return false;
        }
    }
}