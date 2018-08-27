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

import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * Stand-alone version of the demo.
 *
 * @author D. Campione
 *
 */
public class DemoApp extends JFrame {

    private static final long serialVersionUID = 7419588621991890397L;

    public DemoApp() {
        setRootPane(new DemoRootPane());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("STA Language Support Demo Application");
        pack();
    }

    /**
     * Called when we are made visible. Here we request that the {@link
     * SyntaxTextArea} is given focus.
     *
     * @param visible Whether this frame should be visible.
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            ((DemoRootPane) getRootPane()).focusTextArea();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager
                            .getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    ExceptionDialog.hideException(e); // Never happens
                }
                Toolkit.getDefaultToolkit().setDynamicLayout(true);
                new DemoApp().setVisible(true);
            }
        });
    }
}