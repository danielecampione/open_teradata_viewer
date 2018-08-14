/*
 * Open Teradata Viewer ( editor search )
 * Copyright (C) 2013, D. Campione
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
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.EventListenerList;
import javax.swing.text.JTextComponent;

import net.sourceforge.open_teradata_viewer.editor.SearchContext;
import net.sourceforge.open_teradata_viewer.util.UIUtil;

/**
 * This is the base class for {@link FindDialog} and {@link ReplaceDialog}. It
 * is basically all of the features common to the two dialogs that weren't taken
 * care of in {@link AbstractSearchDialog}.
 *
 * @author D. Campione
 * 
 */
public abstract class AbstractFindReplaceDialog extends AbstractSearchDialog {

    private static final long serialVersionUID = 6066711198294930960L;

    /**
     * Property fired when the user toggles the search direction radio buttons.
     */
    public static final String SEARCH_DOWNWARD_PROPERTY = "SearchDialog.SearchDownward";

    // The radio buttons for changing the search direction
    protected JRadioButton upButton;
    protected JRadioButton downButton;
    protected JPanel dirPanel;
    private String dirPanelTitle;
    protected JLabel findFieldLabel;
    protected JButton findNextButton;

    /** The "mark all" check box. */
    protected JCheckBox markAllCheckBox;

    /** Folks listening for events in this dialog. */
    private EventListenerList listenerList;

    /**
     * Ctor.
     *
     * @param owner The dialog that owns this search dialog.
     */
    public AbstractFindReplaceDialog(Dialog owner) {
        super(owner);
        init();
    }

    /**
     * Ctor. Does initializing for parts common to <code>FindDialog</code> and
     * <code>ReplaceDialog</code> that isn't taken care of in
     * <code>AbstractSearchDialog</code>'s constructor.
     *
     * @param owner The window that owns this search dialog.
     */
    public AbstractFindReplaceDialog(Frame owner) {
        super(owner);
        init();
    }

    /**
     * Listens for action events in this dialog.
     *
     * @param e The event that occurred.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if ("UpRadioButtonClicked".equals(command)) {
            context.setSearchForward(false);
        } else if ("DownRadioButtonClicked".equals(command)) {
            context.setSearchForward(true);
        } else if ("MarkAll".equals(command)) {
            boolean checked = markAllCheckBox.isSelected();
            context.setMarkAll(checked);
        } else if (SearchEvent.Type.FIND.name().equals(command)) {
            // Add the item to the combo box's list, if it isn't already there
            JTextComponent tc = UIUtil.getTextComponent(findTextCombo);
            findTextCombo.addItem(tc.getText());
            context.setSearchFor(getSearchString());

            fireSearchEvent(e); // Let parent application know
        } else {
            super.actionPerformed(e);
        }
    }

    /**
     * Adds a {@link ISearchListener} to this dialog. This listener will be
     * notified when find or replace operations are triggered. For example, for
     * a Replace dialog, a listener will receive notification when the user
     * clicks "Find", "Replace" or "Replace All".
     *
     * @param l The listener to add.
     * @see #removeSearchListener(ISearchListener)
     */
    public void addSearchListener(ISearchListener l) {
        listenerList.add(ISearchListener.class, l);
    }

    /**
     * Notifies all listeners that have registered interest for notification on
     * this event type. The event instance is lazily created using the
     * <code>event</code> parameter.
     * 
     * @param event The <code>ActionEvent</code> object coming from a child
     *        component.
     */
    protected void fireSearchEvent(ActionEvent event) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        SearchEvent e = null;
        // Process the listeners last to first, notifying those that are
        // interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ISearchListener.class) {
                // Lazily create the event:
                if (e == null) {
                    String command = event.getActionCommand();
                    SearchEvent.Type type = SearchEvent.Type.valueOf(command);
                    e = new SearchEvent(this, type, context);
                }
                ((ISearchListener) listeners[i + 1]).searchEvent(e);
            }
        }
    }

    /**
     * @return The text for the "Down" radio button.
     * @see #setDownRadioButtonText
     */
    public final String getDownRadioButtonText() {
        return downButton.getText();
    }

    /**
     * @return The text on the Find button.
     * @see #setFindButtonText
     */
    public final String getFindButtonText() {
        return findNextButton.getText();
    }

    /**
     * @return The text on the "Find what" text field.
     * @see #setFindWhatLabelText
     */
    public final String getFindWhatLabelText() {
        return findFieldLabel.getText();
    }

    /**
     * Returns the text for the search direction's radio buttons' border.
     *
     * @return The text for the search radio buttons' border.
     * @see #setSearchButtonsBorderText
     */
    public final String getSearchButtonsBorderText() {
        return dirPanelTitle;
    }

    /**
     * @return The text for the "Up" radio button.
     * @see #setUpRadioButtonText
     */
    public final String getUpRadioButtonText() {
        return upButton.getText();
    }

    /**
     * Called whenever a property in the search context is modified.
     * Subclasses should override if they listen for additional properties.
     *
     * @param e The property change event fired.
     */
    @Override
    protected void handleSearchContextPropertyChanged(PropertyChangeEvent e) {
        String prop = e.getPropertyName();

        if (SearchContext.PROPERTY_SEARCH_FORWARD.equals(prop)) {
            boolean newValue = ((Boolean) e.getNewValue()).booleanValue();
            JRadioButton button = newValue ? downButton : upButton;
            button.setSelected(true);
        } else if (SearchContext.PROPERTY_MARK_ALL.equals(prop)) {
            boolean newValue = ((Boolean) e.getNewValue()).booleanValue();
            markAllCheckBox.setSelected(newValue);
        } else {
            super.handleSearchContextPropertyChanged(e);
        }
    }

    @Override
    protected FindReplaceButtonsEnableResult handleToggleButtons() {
        FindReplaceButtonsEnableResult er = super.handleToggleButtons();
        boolean enable = er.getEnable();

        findNextButton.setEnabled(enable);

        // setBackground doesn't show up with XP Look and Feel
        //findTextComboBox.setBackground(enable ?
        //		UIManager.getColor("ComboBox.background") : Color.PINK);
        JTextComponent tc = UIUtil.getTextComponent(findTextCombo);
        tc.setForeground(enable ? UIManager.getColor("TextField.foreground")
                : UIUtil.getErrorTextForeground());

        String tooltip = SearchUtil.getToolTip(er);
        tc.setToolTipText(tooltip); // Always set, even if null

        return er;
    }

    private void init() {
        listenerList = new EventListenerList();

        // Make a panel containing the "search up/down" radio buttons
        dirPanel = new JPanel();
        dirPanel.setLayout(new BoxLayout(dirPanel, BoxLayout.LINE_AXIS));
        setSearchButtonsBorderText("Direction");
        ButtonGroup bg = new ButtonGroup();
        upButton = new JRadioButton("Up", false);
        upButton.setMnemonic((int) "U".charAt(0));
        downButton = new JRadioButton("Down", true);
        downButton.setMnemonic((int) "D".charAt(0));
        upButton.setActionCommand("UpRadioButtonClicked");
        upButton.addActionListener(this);
        downButton.setActionCommand("DownRadioButtonClicked");
        downButton.addActionListener(this);
        bg.add(upButton);
        bg.add(downButton);
        dirPanel.add(upButton);
        dirPanel.add(downButton);

        // Initialize the "mark all" button
        markAllCheckBox = new JCheckBox("Mark All");
        markAllCheckBox.setMnemonic((int) "M".charAt(0));
        markAllCheckBox.setActionCommand("MarkAll");
        markAllCheckBox.addActionListener(this);

        // Rearrange the search conditions panel
        searchConditionsPanel.removeAll();
        searchConditionsPanel.setLayout(new BorderLayout());
        JPanel temp = new JPanel();
        temp.setLayout(new BoxLayout(temp, BoxLayout.PAGE_AXIS));
        temp.add(caseCheckBox);
        temp.add(wholeWordCheckBox);
        searchConditionsPanel.add(temp, BorderLayout.LINE_START);
        temp = new JPanel();
        temp.setLayout(new BoxLayout(temp, BoxLayout.PAGE_AXIS));
        temp.add(regexCheckBox);
        temp.add(markAllCheckBox);
        searchConditionsPanel.add(temp, BorderLayout.LINE_END);

        // Create the "Find what" label
        findFieldLabel = new JLabel("Find what:  ");

        // Create a "Find Next" button
        findNextButton = new JButton("Find");
        findNextButton.setActionCommand(SearchEvent.Type.FIND.name());
        findNextButton.addActionListener(this);
        findNextButton.setDefaultCapable(true);
        findNextButton.setEnabled(false); // Initially, nothing to look for

        installKeyboardActions();
    }

    /** Adds extra keyboard actions for Find and Replace dialogs. */
    private void installKeyboardActions() {
        JRootPane rootPane = getRootPane();
        InputMap im = rootPane
                .getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap am = rootPane.getActionMap();

        int modifier = getToolkit().getMenuShortcutKeyMask();
        KeyStroke ctrlF = KeyStroke.getKeyStroke(KeyEvent.VK_F, modifier);
        im.put(ctrlF, "focusSearchForField");
        am.put("focusSearchForField", new AbstractAction() {

            private static final long serialVersionUID = -8581713159383740586L;

            @Override
            public void actionPerformed(ActionEvent e) {
                AbstractFindReplaceDialog.this.requestFocus();
            }
        });
    }

    /** Overridden to initialize UI elements specific to this subclass. */
    @Override
    protected void refreshUIFromContext() {
        if (this.markAllCheckBox == null) {
            return; // First time through, UI not realized yet
        }
        super.refreshUIFromContext();
        markAllCheckBox.setSelected(context.getMarkAll());
        boolean searchForward = context.getSearchForward();
        upButton.setSelected(!searchForward);
        downButton.setSelected(searchForward);
    }

    /**
     * Removes a {@link ISearchListener} from this dialog.
     *
     * @param l The listener to remove.
     * @see #addSearchListener(ISearchListener)
     */
    public void removeSearchListener(ISearchListener l) {
        listenerList.remove(ISearchListener.class, l);
    }

    /**
     * Sets the text label for the "Down" radio button.
     *
     * @param text The new text label for the "Down" radio button.
     * @see #getDownRadioButtonText
     */
    public void setDownRadioButtonText(String text) {
        downButton.setText(text);
    }

    /**
     * Sets the text on the "Find" button.
     *
     * @param text The text for the Find button.
     * @see #getFindButtonText
     */
    public final void setFindButtonText(String text) {
        findNextButton.setText(text);
    }

    /**
     * Sets the label on the "Find what" text field.
     *
     * @param text The text for the "Find what" text field's label.
     * @see #getFindWhatLabelText
     */
    public void setFindWhatLabelText(String text) {
        findFieldLabel.setText(text);
    }

    /**
     * Sets the text for the search direction's radio buttons' border.
     *
     * @param text The text for the search radio buttons' border.
     * @see #getSearchButtonsBorderText
     */
    public final void setSearchButtonsBorderText(String text) {
        dirPanelTitle = text;
        dirPanel.setBorder(createTitledBorder(dirPanelTitle));
    }

    /**
     * Sets the text label for the "Up" radio button.
     *
     * @param text The new text label for the "Up" radio button.
     * @see #getUpRadioButtonText
     */
    public void setUpRadioButtonText(String text) {
        upButton.setText(text);
    }
}