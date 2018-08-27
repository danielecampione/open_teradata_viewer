/*
 * Open Teradata Viewer ( editor language support demo )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.demo;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.text.DefaultHighlighter;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.ExtensionFileFilter;

/**
 * Container for all actions used by the demo.
 *
 * @author D. Campione
 *
 */
interface IActions {

    /**
     * Displays an "About" dialog.
     *
     * @author D. Campione
     *
     */
    static class AboutAction extends AbstractAction {

        private static final long serialVersionUID = 3409774296685465559L;

        private DemoRootPane demo;

        public AboutAction(DemoRootPane demo) {
            this.demo = demo;
            putValue(NAME, "About STALanguageSupport..");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            AboutDialog ad = new AboutDialog(
                    (DemoApp) SwingUtilities.getWindowAncestor(demo));
            ad.setLocationRelativeTo(demo);
            ad.setVisible(true);
        }
    }

    /**
     * Exits the application.
     *
     * @author D. Campione
     *
     */
    static class ExitAction extends AbstractAction {

        private static final long serialVersionUID = -8819520623216398938L;

        public ExitAction() {
            putValue(NAME, "Exit");
            putValue(MNEMONIC_KEY, new Integer('x'));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    /**
     * Lets the user open a file.
     *
     * @author D. Campione
     *
     */
    static class OpenAction extends AbstractAction {

        private static final long serialVersionUID = -7528698036444761174L;

        private DemoRootPane demo;
        private JFileChooser chooser;

        public OpenAction(DemoRootPane demo) {
            this.demo = demo;
            putValue(NAME, "Open..");
            putValue(MNEMONIC_KEY, new Integer('O'));
            int mods = demo.getToolkit().getMenuShortcutKeyMask();
            KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_O, mods);
            putValue(ACCELERATOR_KEY, ks);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (chooser == null) {
                chooser = new JFileChooser();
                chooser.setFileFilter(new ExtensionFileFilter(
                        "Java Source Files", "java"));
            }
            int rc = chooser.showOpenDialog(demo);
            if (rc == JFileChooser.APPROVE_OPTION) {
                demo.openFile(chooser.getSelectedFile());
            }
        }
    }

    /**
     * Changes the look and feel of the demo application.
     *
     * @author D. Campione
     *
     */
    static class LookAndFeelAction extends AbstractAction {

        private static final long serialVersionUID = 8236954929510295972L;

        private LookAndFeelInfo info;
        private DemoRootPane demo;

        public LookAndFeelAction(DemoRootPane demo, LookAndFeelInfo info) {
            putValue(NAME, info.getName());
            this.demo = demo;
            this.info = info;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                UIManager.setLookAndFeel(info.getClassName());
                SwingUtilities.updateComponentTreeUI(demo);
            } catch (RuntimeException re) {
                throw re;
            } catch (Exception ex) {
                ExceptionDialog.hideException(ex);
            }
        }
    }

    /**
     * Changes the language being edited and installs appropriate language
     * support.
     *
     * @author D. Campione
     *
     */
    static class StyleAction extends AbstractAction {

        private static final long serialVersionUID = -9082001656202191454L;

        private DemoRootPane demo;
        private String res;
        private String style;

        public StyleAction(DemoRootPane demo, String name, String res,
                String style) {
            putValue(NAME, name);
            this.demo = demo;
            this.res = res;
            this.style = style;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            demo.setText(res, style);
        }
    }

    /**
     *
     *
     * @author D. Campione
     *
     */
    static class ToggleLayeredHighlightsAction extends AbstractAction {

        private static final long serialVersionUID = 5354835324452526123L;

        private DemoRootPane demo;

        public ToggleLayeredHighlightsAction(DemoRootPane demo) {
            this.demo = demo;
            putValue(NAME, "Layered Selection Highlights");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            DefaultHighlighter h = (DefaultHighlighter) demo.getTextArea()
                    .getHighlighter();
            h.setDrawsLayeredHighlights(!h.getDrawsLayeredHighlights());
        }
    }
}