/*
 * Open Teradata Viewer ( editor search )
 * Copyright (C) 2012, D. Campione
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

package net.sourceforge.open_teradata_viewer.editor.search;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import net.sourceforge.open_teradata_viewer.EscapableDialog;
import net.sourceforge.open_teradata_viewer.UISupport;

/**
 * A "Go To" dialog allowing you to go to a specific line number in a document
 * in Text.
 *
 * @author D. Campione
 *
 */
public class OTVGoToDialog extends EscapableDialog {

    private static final long serialVersionUID = -5708580445109367953L;

    private JButton okButton;
    private JButton cancelButton;
    private JTextField lineNumberField;
    private int maxLineNumberAllowed; // Number of lines in the document
    private int lineNumber; // The line to go to, or -1 for Cancel

    /**
     * Creates a new <code>GoToDialog</code>.
     *
     * @param owner          The main window that owns this dialog.
     */
    public OTVGoToDialog(JFrame parent) {
        // Let it be known who the owner of this dialog is
        super(parent);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                escapePressed();
            }
        });

        ComponentOrientation orientation = ComponentOrientation
                .getOrientation(getLocale());

        lineNumber = 1;
        maxLineNumberAllowed = 1; // Empty document has 1 line
        GoToListener listener = new GoToListener();

        // Set the main content pane for the "GoTo" dialog
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        // Make a panel containing the "Line number" edit box
        JPanel enterLineNumberPane = new JPanel();
        BoxLayout box = new BoxLayout(enterLineNumberPane, BoxLayout.LINE_AXIS);
        enterLineNumberPane.setLayout(box);
        lineNumberField = new JTextField(16);
        lineNumberField.setText("" + lineNumber);
        AbstractDocument doc = (AbstractDocument) lineNumberField.getDocument();
        doc.addDocumentListener(listener);
        doc.setDocumentFilter(new GoToDocumentFilter());
        enterLineNumberPane.add(new JLabel("Line number"));
        enterLineNumberPane.add(lineNumberField);

        // Make a panel containing the OK and Cancel buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        okButton = createJButton("OK", "O");
        okButton.addActionListener(listener);
        cancelButton = createJButton("Cancel", "C");
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(listener);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // Put everything into a neat little package
        contentPane.add(enterLineNumberPane, BorderLayout.NORTH);
        JPanel temp = new JPanel();
        temp.add(buttonPanel);
        contentPane.add(temp, BorderLayout.SOUTH);
        JRootPane rootPane = getRootPane();
        rootPane.setDefaultButton(okButton);
        setTitle("Go To");
        setModal(true);
        applyComponentOrientation(orientation);
        pack();
        setLocationRelativeTo(parent);
    }

    public static final JButton createJButton(String textKey, String mnemonicKey) {
        JButton b = new JButton(textKey);
        b.setActionCommand(textKey);
        b.setMnemonic((int) mnemonicKey.charAt(0));
        return b;
    }

    /**
     * Called when they've clicked OK or pressed Enter; check the line number
     * they entered for validity and if it's okay, close this dialog. If it
     * isn't okay, display an error message.
     */
    private void attemptToGetGoToLine() {
        try {
            lineNumber = Integer.parseInt(lineNumberField.getText());

            if (lineNumber < 1 || lineNumber > maxLineNumberAllowed)
                throw new NumberFormatException();

            // If we have a valid line number, close the dialog
            setVisible(false);
        } catch (NumberFormatException nfe) {
            UISupport.getDialogs().showInfoMessage(
                    "Maximum line number allowed is " + maxLineNumberAllowed
                            + ".", "Go To");
            return;
        }
    }

    /**
     * Called when the user clicks Cancel or hits the Escape key. This hides the
     * dialog.
     */
    protected void escapePressed() {
        lineNumber = -1;
        super.escapePressed();
    }

    /**
     * Gets the line number the user entered to go to.
     *
     * @return The line number the user decided to go to, or <code>-1</code> if
     *         the dialog was canceled.
     */
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * Sets the maximum line number for them to enter.
     *
     * @param max The new maximum line number value.
     */
    public void setMaxLineNumberAllowed(int max) {
        this.maxLineNumberAllowed = max;
    }

    /**
     * Overrides <code>JDialog</code>'s <code>setVisible</code> method; decides
     * whether or not buttons are enabled if the user is enabling the dialog.
     */
    public void setVisible(boolean visible) {
        if (visible) {
            lineNumber = -1;
            okButton.setEnabled(lineNumberField.getDocument().getLength() > 0);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    lineNumberField.requestFocusInWindow();
                    lineNumberField.selectAll();
                }
            });
        }
        super.setVisible(visible);
    }

    /**
     * A document filter that only allows numbers.
     * 
     * @author D. Campione
     * 
     */
    static class GoToDocumentFilter extends DocumentFilter {

        private final String cleanse(String text) {
            boolean beep = false;
            if (text != null) {
                int length = text.length();
                for (int i = 0; i < length; i++) {
                    if (!Character.isDigit(text.charAt(i))) {
                        text = text.substring(0, i) + text.substring(i + 1);
                        i--;
                        length--;
                        beep = true;
                    }
                }
            }
            if (beep) {
                UIManager.getLookAndFeel().provideErrorFeedback(null);
            }
            return text;
        }

        public void insertString(DocumentFilter.FilterBypass fb, int offset,
                String text, AttributeSet attr) throws BadLocationException {
            fb.insertString(offset, cleanse(text), attr);
        }

        public void remove(DocumentFilter.FilterBypass fb, int offset,
                int length) throws BadLocationException {
            fb.remove(offset, length);
        }

        public void replace(DocumentFilter.FilterBypass fb, int offset,
                int length, String text, AttributeSet attr)
                throws BadLocationException {
            fb.replace(offset, length, cleanse(text), attr);
        }
    }

    /**
     * Listens for events in this dialog.
     * 
     * @author D. Campione
     * 
     */
    private class GoToListener implements ActionListener, DocumentListener {

        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("OK"))
                attemptToGetGoToLine();
            else if (command.equals("Cancel"))
                escapePressed();
        }

        public void changedUpdate(DocumentEvent e) {
        }

        public void insertUpdate(DocumentEvent e) {
            okButton.setEnabled(lineNumberField.getDocument().getLength() > 0);
        }

        public void removeUpdate(DocumentEvent e) {
            okButton.setEnabled(lineNumberField.getDocument().getLength() > 0);
        }
    }
}