/*
 * Open Teradata Viewer ( editor macros )
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

package net.sourceforge.open_teradata_viewer.editor.macros;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.text.MessageFormat;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.fife.rsta.ui.DecorativeIconPanel;
import org.fife.rsta.ui.EscapableDialog;
import org.fife.rsta.ui.ResizableFrameContentPane;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ApplicationMenuBar;
import net.sourceforge.open_teradata_viewer.KeyStrokeField;
import net.sourceforge.open_teradata_viewer.Main;
import net.sourceforge.open_teradata_viewer.SelectableLabel;
import net.sourceforge.open_teradata_viewer.util.OTVUtilities;
import net.sourceforge.open_teradata_viewer.util.UIUtil;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class NewMacroDialog extends EscapableDialog {

    private static final long serialVersionUID = 1266463108055817660L;

    private ApplicationFrame application;
    private Listener l;
    private DecorativeIconPanel nameDIP;
    private JTextField nameField;
    private JTextField descField;
    private KeyStrokeField shortcutField;
    private JComboBox typeCombo;
    private JButton okButton;
    private JButton cancelButton;
    private Macro macro;
    private boolean isNew;
    private static Icon ERROR_ICON;
    private static Icon WARN_ICON;

    private static final String[] EXTENSIONS = { ".js", ".groovy", };

    public NewMacroDialog(JDialog parent) {
        super(parent);
        createGUI();
    }

    public NewMacroDialog(JFrame parent) {
        super(parent);
        createGUI();
    }

    public void createGUI() {
        application = ApplicationFrame.getInstance();
        JPanel cp = new ResizableFrameContentPane(new BorderLayout());
        cp.setBorder(UIUtil.getEmpty5Border());
        l = new Listener();

        Box topPanel = Box.createVerticalBox();
        cp.add(topPanel, BorderLayout.NORTH);
        String descText = "<html>A macro is a script that can be assigned a shortcut, \r\n"
                + "and used like any other action in {0}. Support for different scripting \r\n"
                + "languages may require additional libraries.</html>";
        descText = MessageFormat.format(descText, Main.APPLICATION_NAME);
        SelectableLabel desc = new SelectableLabel(descText);
        topPanel.add(desc);
        topPanel.add(Box.createVerticalStrut(5));

        // Panel for defining the macro
        SpringLayout sl = new SpringLayout();
        JPanel formPanel = new JPanel(sl);
        JLabel nameLabel = UIUtil.newLabel("Name:");
        nameField = new JTextField(40);
        nameField.getDocument().addDocumentListener(l);
        nameLabel.setLabelFor(nameField);
        nameDIP = new DecorativeIconPanel();
        JPanel namePanel = OTVUtilities.createAssistancePanel(nameField, nameDIP);
        JLabel descLabel = UIUtil.newLabel("Description:");
        descField = new JTextField(40);
        descLabel.setLabelFor(descField);
        JPanel descPanel = OTVUtilities.createAssistancePanel(descField, null);
        JLabel shortcutLabel = UIUtil.newLabel("Shortcut:");
        shortcutField = new KeyStrokeField();
        shortcutLabel.setLabelFor(shortcutField);
        JPanel shortcutPanel = OTVUtilities.createAssistancePanel(shortcutField, null);
        JLabel typeLabel = UIUtil.newLabel("Type:");
        String[] items = { "Rhino (JavaScript)", "Groovy" };
        typeCombo = new JComboBox(items);
        typeCombo.addActionListener(l);
        typeCombo.setEditable(false);
        typeLabel.setLabelFor(typeCombo);
        JPanel typePanel = OTVUtilities.createAssistancePanel(typeCombo, null);
        if (application.getComponentOrientation().isLeftToRight()) {
            formPanel.add(nameLabel);
            formPanel.add(namePanel);
            formPanel.add(typeLabel);
            formPanel.add(typePanel);
            formPanel.add(descLabel);
            formPanel.add(descPanel);
            formPanel.add(shortcutLabel);
            formPanel.add(shortcutPanel);
        } else {
            formPanel.add(namePanel);
            formPanel.add(nameLabel);
            formPanel.add(typePanel);
            formPanel.add(typeLabel);
            formPanel.add(descPanel);
            formPanel.add(descLabel);
            formPanel.add(shortcutPanel);
            formPanel.add(shortcutLabel);
        }
        UIUtil.makeSpringCompactGrid(formPanel, 4, 2, 5, 5, 5, 5);
        topPanel.add(formPanel);
        topPanel.add(Box.createVerticalStrut(10));
        topPanel.add(Box.createVerticalGlue());

        // Panel for the buttons
        okButton = UIUtil.newButton("OK", "O");
        okButton.setEnabled(false);
        cancelButton = UIUtil.newButton("Cancel", "C");
        okButton.addActionListener(l);
        cancelButton.addActionListener(l);
        Container buttonPanel = UIUtil.createButtonFooter(okButton, cancelButton);
        cp.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(cp);
        setTitle("New Macro");
        getRootPane().setDefaultButton(okButton);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModal(true);
        packSpecial();
        setLocationRelativeTo(application);

        isNew = true;
    }

    /**
     * Returns the file to store a macro in.
     *
     * @return The file.
     */
    private File createMacroFile() {
        if (!isNew) { // Preserve old file
            return new File(macro.getFile());
        }
        return new File(ApplicationFrame.getInstance().getMacrosDirectory(),
                nameField.getText() + EXTENSIONS[typeCombo.getSelectedIndex()]);
    }

    /**
     * Returns the icon to use for fields with errors.
     *
     * @return The icon.
     */
    private Icon getErrorIcon() {
        if (ERROR_ICON == null) {
            URL res = getClass().getResource("error_co.gif");
            ERROR_ICON = new ImageIcon(res);
        }
        return ERROR_ICON;
    }

    /**
     * Returns the macro created by the user, or <code>null</code> if this
     * dialog was canceled.
     *
     * @return The macro created.
     */
    public Macro getMacro() {
        return macro;
    }

    /**
     * Returns the icon to use for fields with warnings.
     *
     * @return The icon.
     */
    private Icon getWarningIcon() {
        if (WARN_ICON == null) {
            URL res = getClass().getResource("warning_co.gif");
            WARN_ICON = new ImageIcon(res);
        }
        return WARN_ICON;
    }

    /** Called when the user clicks the OK button. */
    private void okPressed() {
        File file = createMacroFile();
        int rc = JOptionPane.YES_OPTION;

        if (isNew) {
            // Warn the user if they will be overwriting an existing macro.
            // Note that we check by macro name, so a JavaScript (Rhino) macro
            // named "test" would overwrite a Groovy macro named "test"
            String name = nameField.getText();
            if (MacroManager.get().containsMacroNamed(name)) {
                String text = "A macro with the name \"{0}\" already exists.\nDo you want to overwrite it?";
                text = MessageFormat.format(text, name);
                String title = "Configuration";
                rc = JOptionPane.showConfirmDialog(NewMacroDialog.this, text, title, JOptionPane.YES_NO_CANCEL_OPTION);
            }
            // Next, the file could already exist, though it is associated with
            // no macro (highly unlikely, but possible, say if they manually
            // mucked with their macros)
            else if (file.isFile()) {
                String text = "A file named \"{0}\" already exists, although it is not associated with a macro.\nDo you want to overwrite it?";
                text = MessageFormat.format(text, file.getName());
                String title = "Configuration";
                rc = JOptionPane.showConfirmDialog(NewMacroDialog.this, text, title, JOptionPane.YES_NO_CANCEL_OPTION);
            }
        }

        switch (rc) {
        case JOptionPane.YES_OPTION:
            if (isNew) { // Don't delete macro file if we're editing it
                file.delete();
                macro = new Macro();
            }
            macro.setName(nameField.getText());
            macro.setDesc(descField.getText());
            macro.setFile(file.getAbsolutePath());
            KeyStroke ks = shortcutField.getKeyStroke();
            if (ks != null) {
                macro.setAccelerator(ks.toString());
            }

            if (isNew) {
                MacroManager.get().addMacro(macro);
                ApplicationFrame app = ApplicationFrame.getInstance();
                ApplicationMenuBar menuBar = app.getApplicationMenuBar();
                menuBar.refreshMacrosMenu();
            }

            escapePressed();
            break;
        case JOptionPane.NO_OPTION:
            // Do nothing; let the user change the macro name
            nameField.selectAll();
            nameField.requestFocusInWindow();
            break;
        default: // case JOptionPane.CANCEL_OPTION:
            // Kill the whole dialog
            escapePressed();
            break;
        }
    }

    /**
     * Packs this dialog, taking special care to not be too wide due to our
     * <code>SelectableLabel</code>.
     */
    private void packSpecial() {
        pack();
        setSize(520, getHeight() + 80); // Enough for line wrapping
    }

    private void setBadMacroName(String reasonKey) {
        nameDIP.setShowIcon(true);
        String reason = null;
        if (reasonKey.equals("invalidChars")) {
            reason = "A macro name should only contain letters, numbers, spaces, '-' and '_'.";
        } else if (reasonKey.equals("empty")) {
            reason = "Macro name is required.";
        }
        nameDIP.setIcon(getErrorIcon());
        nameDIP.setToolTipText(reason);
        okButton.setEnabled(false);
    }

    private void setGoodMacroName() {
        nameDIP.setShowIcon(false);
        nameDIP.setToolTipText(null);
        okButton.setEnabled(true);
    }

    /**
     * Overridden to give focus to the appropriate text component when this
     * dialog is made visible.
     *
     * @param visible Whether this dialog is to be made visible.
     */
    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            JTextField field = isNew ? nameField : descField;
            field.requestFocusInWindow();
            field.selectAll();
        }
        super.setVisible(visible);
    }

    private void setWarnMacroName() {
        nameDIP.setShowIcon(true);
        String reason = "Another macro already exists with this name.";
        nameDIP.setIcon(getWarningIcon());
        nameDIP.setToolTipText(reason);
    }

    /**
     * Listens for events in this dialog.
     * 
     * @author D. Campione
     * 
     */
    private class Listener implements ActionListener, DocumentListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == okButton) {
                okPressed();
            } else if (source == cancelButton) {
                macro = null; // In case we're editing one via setMacro()
                escapePressed();
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            handleDocumentEvent(e);
        }

        public void handleDocumentEvent(DocumentEvent e) {
            if (nameField.getDocument().getLength() == 0) {
                setBadMacroName("empty");
                return;
            }

            String name = nameField.getText();
            for (int i = 0; i < name.length(); i++) {
                char ch = name.charAt(i);
                if (!(Character.isLetterOrDigit(ch) || ch == '_' || ch == '-' || ch == ' ')) {
                    setBadMacroName("invalidChars");
                    return;
                }
            }

            if (isNew && MacroManager.get().containsMacroNamed(name)) {
                setWarnMacroName();
            } else {
                setGoodMacroName();
            }

        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            handleDocumentEvent(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            handleDocumentEvent(e);
        }
    }
}