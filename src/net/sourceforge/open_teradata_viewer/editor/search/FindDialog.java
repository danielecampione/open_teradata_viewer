/*
 * Open Teradata Viewer ( editor search )
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

package net.sourceforge.open_teradata_viewer.editor.search;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import net.sourceforge.open_teradata_viewer.editor.AssistanceIconPanel;
import net.sourceforge.open_teradata_viewer.editor.ResizableFrameContentPane;
import net.sourceforge.open_teradata_viewer.editor.SearchContext;
import net.sourceforge.open_teradata_viewer.editor.SearchEngine;
import net.sourceforge.open_teradata_viewer.util.UIUtil;

/**
 * A "Find" dialog similar to those found in most Windows text editing
 * applications. Contains many search options, including:<br>
 * <ul>
 *   <li>Match Case
 *   <li>Match Whole Word
 *   <li>Use Regular Expressions
 *   <li>Search Forwards or Backwards
 *   <li>Mark all
 * </ul>
 * The dialog also remembers your previous several selections in a combo box.
 * <p>An application can use a <code>FindDialog</code> as follows. It is
 * suggested that you create an <code>Action</code> or something similar to
 * facilitate "bringing up" the Find dialog. Have the main application contain
 * an object that implements {@link ISearchListener}. This object will receive
 * {@link SearchEvent}s of the following types from the Find dialog:
 * <ul>
 *   <li>{@link SearchEvent.Type#FIND} action when the user clicks the "Find"
 *       button.
 * </ul>
 * The application can then call i.e.
 * {@link SearchEngine#find(javax.swing.JTextArea, net.sourceforge.open_teradata_viewer.editor.SearchContext) SearchEngine.find()}
 * to actually execute the search.
 *
 * @author D. Campione
 * @see FindToolBar
 * 
 */
public class FindDialog extends AbstractFindReplaceDialog {

    private static final long serialVersionUID = 4395409214307100638L;

    /**
     * Creates a new <code>FindDialog</code>.
     *
     * @param owner The parent dialog.
     * @param listener The component that listens for {@link SearchEvent}s.
     */
    public FindDialog(Dialog owner, ISearchListener listener) {
        super(owner);
        init(listener);
    }

    /**
     * Creates a new <code>FindDialog</code>.
     *
     * @param owner The main window that owns this dialog.
     * @param listener The component that listens for {@link SearchEvent}s.
     */
    public FindDialog(Frame owner, ISearchListener listener) {
        super(owner);
        init(listener);
    }

    /**
     * Initializes find dialog-specific initialization stuff.
     *
     * @param listener The component that listens for {@link SearchEvent}s.
     */
    private void init(ISearchListener listener) {
        ComponentOrientation orientation = ComponentOrientation
                .getOrientation(getLocale());

        // Make a panel containing the "Find" edit box
        JPanel enterTextPane = new JPanel(new SpringLayout());
        enterTextPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
        JTextComponent textField = UIUtil.getTextComponent(findTextCombo);
        textField.addFocusListener(new FindFocusAdapter());
        textField.addKeyListener(new FindKeyListener());
        textField.getDocument().addDocumentListener(new FindDocumentListener());
        JPanel temp = new JPanel(new BorderLayout());
        temp.add(findTextCombo);
        AssistanceIconPanel aip = new AssistanceIconPanel(findTextCombo);
        temp.add(aip, BorderLayout.LINE_START);
        if (orientation.isLeftToRight()) {
            enterTextPane.add(findFieldLabel);
            enterTextPane.add(temp);
        } else {
            enterTextPane.add(temp);
            enterTextPane.add(findFieldLabel);
        }

        UIUtil.makeSpringCompactGrid(enterTextPane, 1, 2, //rows, cols
                0, 0, //initX, initY
                6, 6); //xPad, yPad

        // Make a panel containing the inherited search direction radio buttons
        // and the inherited search options
        JPanel bottomPanel = new JPanel(new BorderLayout());
        temp = new JPanel(new BorderLayout());
        bottomPanel.setBorder(UIUtil.getEmpty5Border());
        temp.add(searchConditionsPanel, BorderLayout.LINE_START);
        temp.add(dirPanel);
        bottomPanel.add(temp, BorderLayout.LINE_START);

        // Now, make a panel containing all the above stuff
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(enterTextPane);
        leftPanel.add(bottomPanel);

        // Make a panel containing the action buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2, 1, 5, 5));
        buttonPanel.add(findNextButton);
        buttonPanel.add(cancelButton);
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(buttonPanel, BorderLayout.NORTH);

        // Put everything into a neat little package
        JPanel contentPane = new JPanel(new BorderLayout());
        if (orientation.isLeftToRight()) {
            contentPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 5));
        } else {
            contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 0));
        }
        contentPane.add(leftPanel);
        contentPane.add(rightPanel, BorderLayout.LINE_END);
        temp = new ResizableFrameContentPane(new BorderLayout());
        temp.add(contentPane, BorderLayout.NORTH);
        setContentPane(temp);
        getRootPane().setDefaultButton(findNextButton);
        setTitle("Find..");
        setResizable(true);
        pack();
        setLocationRelativeTo(getParent());

        setSearchContext(new SearchContext());
        addSearchListener(listener);

        applyComponentOrientation(orientation);
    }

    /**
     * Overrides <code>JDialog</code>'s <code>setVisible</code> method; decides
     * whether or not buttons are enabled.
     *
     * @param visible Whether or not the dialog should be visible.
     */
    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            String selectedItem = (String) findTextCombo.getSelectedItem();
            findNextButton.setEnabled(selectedItem != null);
            super.setVisible(true);
            focusFindTextField();
        } else {
            super.setVisible(false);
        }
    }

    /**
     * This method should be called whenever the <code>LookAndFeel</code> of the
     * application changes. This calls
     * <code>SwingUtilities.updateComponentTreeUI(this)</code> and does other
     * necessary things.<p>
     * Note that this is <em>not</em> an override, as JDialogs don't have an
     * <code>updateUI()</code> method.
     */
    public void updateUI() {
        SwingUtilities.updateComponentTreeUI(this);
        pack();
        JTextComponent textField = UIUtil.getTextComponent(findTextCombo);
        textField.addFocusListener(new FindFocusAdapter());
        textField.addKeyListener(new FindKeyListener());
        textField.getDocument().addDocumentListener(new FindDocumentListener());
    }

    /**
     * Listens for changes in the text field (find search field).
     * 
     * @author D. Campione
     * 
     */
    private class FindDocumentListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            handleToggleButtons();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            JTextComponent comp = UIUtil.getTextComponent(findTextCombo);
            if (comp.getDocument().getLength() == 0) {
                findNextButton.setEnabled(false);
            } else {
                handleToggleButtons();
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }
    }

    /**
     * Listens for the text field gaining focus. All it does is select all text
     * in the combo box's text area.
     * 
     * @author D. Campione
     * 
     */
    private class FindFocusAdapter extends FocusAdapter {

        @Override
        public void focusGained(FocusEvent e) {
            UIUtil.getTextComponent(findTextCombo).selectAll();
        }

    }

    /**
     * Listens for key presses in the find dialog.
     * 
     * @author D. Campione
     * 
     */
    private class FindKeyListener implements KeyListener {

        // Listens for the user pressing a key down
        @Override
        public void keyPressed(KeyEvent e) {
        }

        // Listens for a user releasing a key
        @Override
        public void keyReleased(KeyEvent e) {
        }

        // Listens for a key being typed
        @Override
        public void keyTyped(KeyEvent e) {
        }
    }
}