/*
 * Open Teradata Viewer ( graphic viewer )
 * Copyright (C) 2014, D. Campione
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

package net.sourceforge.open_teradata_viewer.graphic_viewer;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

import net.sourceforge.open_teradata_viewer.UISupport;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class DoubleVerifier extends InputVerifier {

    public DoubleVerifier() {
    }

    public boolean verify(JComponent input) {
        JTextField tf = (JTextField) input;
        String entered = tf.getText();
        try {
            double dVal = Double.parseDouble(entered);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean shouldYieldFocus(JComponent input) {
        if (super.shouldYieldFocus(input)) {
            return true;
        } else {
            input.setInputVerifier(null);
            UISupport.getDialogs().showErrorMessage(
                    "An illegal double value was entered");
            input.setInputVerifier(this);
            return false;
        }
    }
}