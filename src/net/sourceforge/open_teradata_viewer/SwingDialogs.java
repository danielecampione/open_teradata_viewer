/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.jgoodies.forms.builder.ButtonBarBuilder;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class SwingDialogs implements IXDialogs {
    private final Component parent;
    private JDialog extendedInfoDialog;
    private Boolean extendedInfoResult;

    public SwingDialogs(Component parent) {
        this.parent = parent;
    }

    @Override
    public void showExtendedInfo(String title, String description,
            String content, Dimension size) {
        // Build a right-aligned bar for: OK
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGlue();
        builder.addButton(new JButton(new OkAction("OK")));
        JPanel buttonBar = builder.getPanel();

        showExtendedInfo(title, description, content, buttonBar, size);
    }

    private void showExtendedInfo(String title, String description,
            String content, JPanel buttonBar, Dimension size) {
        extendedInfoDialog = new JDialog(UISupport.getMainFrame(), title);
        extendedInfoDialog.setModal(true);
        JPanel panel = new JPanel(new BorderLayout());

        if (description != null) {
            panel.add(UISupport.buildDescription(title, description, null),
                    BorderLayout.NORTH);
        }

        JEditorPane editorPane = new JEditorPane("text/html", content);
        editorPane.setCaretPosition(0);
        editorPane.setEditable(false);
        editorPane
                .addHyperlinkListener(new DefaultHyperlinkListener(editorPane));

        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                scrollPane.getBorder()));

        panel.add(scrollPane);
        buttonBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 5));
        panel.add(buttonBar, BorderLayout.SOUTH);

        extendedInfoDialog.getRootPane().setContentPane(panel);
        if (size == null) {
            extendedInfoDialog.setSize(400, 300);
        } else {
            extendedInfoDialog.setSize(size);
        }

        extendedInfoResult = null;
        UISupport.showDialog(extendedInfoDialog);
    }

    @Override
    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(parent, message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showInfoMessage(String message) {
        showInfoMessage(message, "Information");
    }

    @Override
    public void showInfoMessage(String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public boolean confirm(String question, String title) {
        return JOptionPane.showConfirmDialog(parent, question, title,
                JOptionPane.YES_NO_OPTION) == JOptionPane.OK_OPTION;
    }

    @Override
    public Boolean confirmOrCancel(String question, String title) {
        int result = JOptionPane.showConfirmDialog(parent, question, title,
                JOptionPane.YES_NO_CANCEL_OPTION);

        if (result == JOptionPane.CANCEL_OPTION) {
            return null;
        }

        return Boolean.valueOf(result == JOptionPane.YES_OPTION);
    }

    @Override
    public int yesYesToAllOrNo(String question, String title) {
        String[] buttons = { "Yes", "Yes to all", "No" };
        return JOptionPane.showOptionDialog(parent, question, title, 0,
                JOptionPane.QUESTION_MESSAGE, null, buttons, buttons[0]);
    }

    @Override
    public String prompt(String question, String title, String value) {
        return (String) JOptionPane.showInputDialog(parent, question, title,
                JOptionPane.QUESTION_MESSAGE, null, null, value);
    }

    @Override
    public String prompt(String question, String title) {
        return JOptionPane.showInputDialog(parent, question, title,
                JOptionPane.QUESTION_MESSAGE);
    }

    @Override
    public Object prompt(String question, String title, Object[] objects) {
        Object result = JOptionPane.showInputDialog(parent, question, title,
                JOptionPane.OK_CANCEL_OPTION, null, objects, null);
        return result;
    }

    @Override
    public Object prompt(String question, String title, Object[] objects,
            String value) {
        Object result = JOptionPane.showInputDialog(parent, question, title,
                JOptionPane.OK_CANCEL_OPTION, null, objects, value);
        return result;
    }

    @Override
    public char[] promptPassword(String question, String title) {
        JPasswordField passwordField = new JPasswordField();
        passwordField.addAncestorListener(new FocusAncestorListener(
                passwordField));
        JLabel qLabel = new JLabel(question);
        JOptionPane.showConfirmDialog(null, new Object[] { qLabel,
                passwordField }, title, JOptionPane.OK_CANCEL_OPTION);
        return passwordField.getPassword();
    }

    @Override
    public boolean confirmExtendedInfo(String title, String description,
            String content, Dimension size) {
        // Build a right-aligned bar for: OK and Cancel
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGlue();
        builder.addButton(new JButton(new OkAction("OK")));
        builder.addRelatedGap();
        builder.addButton(new JButton(new CancelAction("Cancel")));
        JPanel buttonBar = builder.getPanel();

        showExtendedInfo(title, description, content, buttonBar, size);

        return extendedInfoResult == null ? false : extendedInfoResult;
    }

    @Override
    public Boolean confirmOrCancleExtendedInfo(String title,
            String description, String content, Dimension size) {
        // Build a right-aligned bar for: Yes, No and Cancel
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addGlue();
        builder.addButton(new JButton(new OkAction("Yes")));
        builder.addRelatedGap();
        builder.addButton(new JButton(new NoAction("No")));
        builder.addRelatedGap();
        builder.addButton(new JButton(new CancelAction("Cancel")));
        JPanel buttonBar = builder.getPanel();

        showExtendedInfo(title, description, content, buttonBar, size);

        return extendedInfoResult;
    }

    @Override
    public String selectXPath(String title, String info, String xml,
            String xpath) {
        return prompt("Specify XPath expression", "Select XPath", xpath);
    }

    private final class FocusAncestorListener implements AncestorListener {
        private final JComponent component;

        public FocusAncestorListener(JComponent component) {
            this.component = component;
        }

        @Override
        public void ancestorAdded(AncestorEvent event) {
            component.requestFocusInWindow();
        }

        @Override
        public void ancestorMoved(AncestorEvent event) {
        }

        @Override
        public void ancestorRemoved(AncestorEvent event) {
        }
    }

    private final class CancelAction extends AbstractAction {

        private static final long serialVersionUID = -2054252907763258164L;

        public CancelAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            extendedInfoResult = null;
            extendedInfoDialog.setVisible(false);
        }
    }

    private final class NoAction extends AbstractAction {

        private static final long serialVersionUID = 639947814769798602L;

        public NoAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            extendedInfoResult = false;
            extendedInfoDialog.setVisible(false);
        }
    }

    private final class OkAction extends AbstractAction {

        private static final long serialVersionUID = 7819174404213017075L;

        public OkAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            extendedInfoResult = true;
            extendedInfoDialog.setVisible(false);
        }
    }
}