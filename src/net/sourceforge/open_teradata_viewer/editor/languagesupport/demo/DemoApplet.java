/*
 * Open Teradata Viewer ( editor language support demo )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.demo;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * An applet version of the language support demo.
 *
 * @author D. Campione
 *
 */
public class DemoApplet extends JApplet {

    private static final long serialVersionUID = -955785363829260182L;

    /** Initializes this applet. */
    @Override
    public void init() {
        super.init();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String laf = UIManager.getSystemLookAndFeelClassName();
                try {
                    UIManager.setLookAndFeel(laf);
                } catch (Exception e) {
                    ExceptionDialog.hideException(e);
                }
                setRootPane(new DemoRootPane());
            }
        });
    }

    /**
     * Called when this applet is made visible. Here we request that the {@link
     * SyntaxTextArea} is given focus.
     *
     * @param visible Whether this applet should be visible.
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            ((DemoRootPane) getRootPane()).focusTextArea();
        }
    }
}