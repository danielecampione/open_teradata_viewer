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

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

import net.sourceforge.open_teradata_viewer.EscapableDialog;
import net.sourceforge.open_teradata_viewer.ImageManager;
import net.sourceforge.open_teradata_viewer.editor.SearchContext;
import net.sourceforge.open_teradata_viewer.util.UIUtil;

/**
 * Base class for all search dialogs (find, replace, find in files, etc..).
 * This class is not useful on its own; you should use either FindDialog or
 * ReplaceDialog, or extend this class to create your own search dialog.
 *
 * @author D. Campione
 * 
 */
public class AbstractSearchDialog extends EscapableDialog implements
        ActionListener {

    private static final long serialVersionUID = -4965591257546947576L;

    protected SearchContext context;
    private SearchContextListener contextListener;

    // Conditions check boxes and the panel they go in.
    // This should be added in the actual layout of the search dialog
    protected JCheckBox caseCheckBox;
    protected JCheckBox wholeWordCheckBox;
    protected JCheckBox regexCheckBox;
    protected JPanel searchConditionsPanel;

    /**
     * The image to use beside a text component when content assist is
     * available.
     */
    private static Image contentAssistImage;

    /** The combo box where the user enters the text for which to search. */
    protected SearchComboBox findTextCombo;

    // Miscellaneous other stuff
    protected JButton cancelButton;

    /**
     * Ctor. Does initializing for parts common to all search dialogs.
     *
     * @param owner The dialog that owns this search dialog.
     */
    public AbstractSearchDialog(Dialog owner) {
        super(owner);
        init();
    }

    /**
     * Ctor. Does initializing for parts common to all search dialogs.
     *
     * @param owner The window that owns this search dialog.
     */
    public AbstractSearchDialog(Frame owner) {
        super(owner);
        init();
    }

    /** Listens for actions in this search dialog. */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // They check/uncheck the "Match Case" checkbox on the Find dialog
        if (command.equals("Match Case")) {
            boolean matchCase = caseCheckBox.isSelected();
            context.setMatchCase(matchCase);
        }
        // They check/uncheck the "Whole word" checkbox on the Find dialog
        else if (command.equals("Whole Word")) {
            boolean wholeWord = wholeWordCheckBox.isSelected();
            context.setWholeWord(wholeWord);
        }
        // They check/uncheck the "Regular expression" checkbox
        else if (command.equals("RegEx")) {
            boolean useRegEx = regexCheckBox.isSelected();
            context.setRegularExpression(useRegEx);
        }
        // If they press the "Cancel" button
        else if (command.equals("Cancel")) {
            setVisible(false);
        }
    }

    /**
     * Returns the default search context to use for this dialog. Applications
     * that create new subclasses of this class can provide customized search
     * contexts here.
     *
     * @return The default search context.
     */
    protected SearchContext createDefaultSearchContext() {
        return new SearchContext();
    }

    /**
     * Returns a titled border for panels on search dialogs.
     *
     * @param title The title for the border.
     * @return The border.
     */
    protected Border createTitledBorder(String title) {
        if (title != null && title.charAt(title.length() - 1) != ':') {
            title += ":";
        }
        return BorderFactory.createTitledBorder(title);
    }

    @Override
    protected void escapePressed() {
        // Workaround for the strange behavior (Java bug?) that sometimes the
        // Escape keypress "gets through" from the AutoComplete's registered key
        // Actions, and gets to this EscapableDialog, which hides the entire
        // dialog. Reproduce by doing the following:
        //   1. In an empty find field, press Ctrl+Space
        //   2. Type "\\".
        //   3. Press Escape.
        // The entire dialog will hide, instead of the completion popup.
        // Further, bringing the Find dialog back up, the completion popup will
        // still be visible
        if (findTextCombo.hideAutoCompletePopups()) {
            return;
        }
        super.escapePressed();
    }

    /** Makes the "Find text" field active. */
    protected void focusFindTextField() {
        JTextComponent textField = UIUtil.getTextComponent(findTextCombo);
        textField.requestFocusInWindow();
        textField.selectAll();
    }

    /**
     * @return The text on the Cancel button.
     * @see #setCancelButtonText
     */
    public final String getCancelButtonText() {
        return cancelButton.getText();
    }

    /**
     * Returns the image to display beside text components when content assist
     * is available.
     *
     * @return The image to use.
     */
    public static Image getContentAssistImage() {
        if (contentAssistImage == null) {
            contentAssistImage = ImageManager.getImage("/icons/lightbulb.png")
                    .getImage();
        }
        return contentAssistImage;
    }

    /**
     * @return The text for the "Match Case" check box.
     * @see #setMatchCaseCheckboxText
     */
    public final String getMatchCaseCheckboxText() {
        return caseCheckBox.getText();
    }

    /**
     * @return The text for the "Regular Expression" check box.
     * @see #setRegularExpressionCheckboxText
     */
    public final String getRegularExpressionCheckboxText() {
        return regexCheckBox.getText();
    }

    /**
     * Returns the search context used by this dialog.
     *
     * @return The search context.
     * @see #setSearchContext(SearchContext)
     */
    public SearchContext getSearchContext() {
        return context;
    }

    /**
     * Returns the text to search for.
     *
     * @return The text the user wants to search for.
     */
    public String getSearchString() {
        return findTextCombo.getSelectedString();
    }

    /**
     * @return The text for the "Whole Word" check box.
     * @see #setWholeWordCheckboxText
     */
    public final String getWholeWordCheckboxText() {
        return wholeWordCheckBox.getText();
    }

    /**
     * Called when the regex checkbox is clicked (or its value is modified via a
     * change to the search context). Subclasses can override to add custom
     * behavior, but should call the super implementation.
     */
    protected void handleRegExCheckBoxClicked() {
        handleToggleButtons();
        // "Content assist" support
        boolean b = regexCheckBox.isSelected();
        findTextCombo.setAutoCompleteEnabled(b);
    }

    /**
     * Called whenever a property in the search context is modified.
     * Subclasses should override if they listen for additional properties.
     *
     * @param e The property change event fired.
     */
    protected void handleSearchContextPropertyChanged(PropertyChangeEvent e) {
        // A property changed on the context itself
        String prop = e.getPropertyName();

        if (SearchContext.PROPERTY_MATCH_CASE.equals(prop)) {
            boolean newValue = ((Boolean) e.getNewValue()).booleanValue();
            caseCheckBox.setSelected(newValue);
        } else if (SearchContext.PROPERTY_MATCH_WHOLE_WORD.equals(prop)) {
            boolean newValue = ((Boolean) e.getNewValue()).booleanValue();
            wholeWordCheckBox.setSelected(newValue);
        } else if (SearchContext.PROPERTY_USE_REGEX.equals(prop)) {
            boolean newValue = ((Boolean) e.getNewValue()).booleanValue();
            regexCheckBox.setSelected(newValue);
            handleRegExCheckBoxClicked();
        } else if (SearchContext.PROPERTY_SEARCH_FOR.equals(prop)) {
            String newValue = (String) e.getNewValue();
            String oldValue = getSearchString();
            // Prevents IllegalStateExceptions
            if (!newValue.equals(oldValue)) {
                setSearchString(newValue);
            }
        }
    }

    /**
     * Returns whether any action-related buttons (Find Next, Replace, etc..)
     * should be enabled. Subclasses can call this method when the "Find What"
     * or "Replace With" text fields are modified. They can then enable/disable
     * any components as appropriate.
     *
     * @return Whether the buttons should be enabled.
     */
    protected FindReplaceButtonsEnableResult handleToggleButtons() {
        JTextComponent tc = UIUtil.getTextComponent(findTextCombo);
        String text = tc.getText();
        if (text.length() == 0) {
            return new FindReplaceButtonsEnableResult(false, null);
        }
        if (regexCheckBox.isSelected()) {
            try {
                Pattern.compile(text);
            } catch (PatternSyntaxException pse) {
                return new FindReplaceButtonsEnableResult(false,
                        pse.getMessage());
            }
        }
        return new FindReplaceButtonsEnableResult(true, null);
    }

    private void init() {
        // The user should set a shared instance between all subclass instances,
        // but to be safe we set individual ones
        contextListener = new SearchContextListener();
        setSearchContext(createDefaultSearchContext());

        // Make a panel containing the option check boxes
        searchConditionsPanel = new JPanel();
        searchConditionsPanel.setLayout(new BoxLayout(searchConditionsPanel,
                BoxLayout.Y_AXIS));
        caseCheckBox = new JCheckBox("Match Case");
        caseCheckBox.addActionListener(this);
        searchConditionsPanel.add(caseCheckBox);
        wholeWordCheckBox = new JCheckBox("Whole Word");
        wholeWordCheckBox.addActionListener(this);
        searchConditionsPanel.add(wholeWordCheckBox);
        regexCheckBox = new JCheckBox("RegEx");
        regexCheckBox.addActionListener(this);
        searchConditionsPanel.add(regexCheckBox);

        // Initialize any text fields
        findTextCombo = new SearchComboBox(null, false);

        // Initialize other stuff
        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);
    }

    /**
     * Returns whether the characters on either side of
     * <code>substr(searchIn,startPos,startPos+searchStringLength)</code> are
     * whitespace. While this isn't the best definition of "whole word", it's
     * the one we're going to use for now.
     */
    public static final boolean isWholeWord(CharSequence searchIn, int offset,
            int len) {
        boolean wsBefore, wsAfter;

        try {
            wsBefore = Character.isWhitespace(searchIn.charAt(offset - 1));
        } catch (IndexOutOfBoundsException e) {
            wsBefore = true;
        }
        try {
            wsAfter = Character.isWhitespace(searchIn.charAt(offset + len));
        } catch (IndexOutOfBoundsException e) {
            wsAfter = true;
        }

        return wsBefore && wsAfter;
    }

    /**
     * Initializes the UI in this tool bar from a search context. This is called
     * whenever a new search context is installed on this tool bar (which should
     * practically be never).
     */
    protected void refreshUIFromContext() {
        if (this.caseCheckBox == null) {
            return; // First time through, UI not realized yet
        }
        this.caseCheckBox.setSelected(context.getMatchCase());
        this.regexCheckBox.setSelected(context.isRegularExpression());
        this.wholeWordCheckBox.setSelected(context.getWholeWord());
    }

    /** Overridden to ensure the "Find text" field gets focused. */
    @Override
    public void requestFocus() {
        super.requestFocus();
        focusFindTextField();
    }

    /**
     * Sets the text on the Cancel button.
     *
     * @param text The text for the Cancel button.
     * @see #getCancelButtonText
     */
    public final void setCancelButtonText(String text) {
        cancelButton.setText(text);
    }

    /**
     * Sets the text for the "Match Case" check box.
     *
     * @param text The text for the "Match Case" check box.
     * @see #getMatchCaseCheckboxText
     */
    public final void setMatchCaseCheckboxText(String text) {
        caseCheckBox.setText(text);
    }

    /**
     * Sets the text for the "Regular Expression" check box.
     *
     * @param text The text for the "Regular Expression" check box.
     * @see #getRegularExpressionCheckboxText
     */
    public final void setRegularExpressionCheckboxText(String text) {
        regexCheckBox.setText(text);
    }

    /**
     * Sets the search context for this dialog. You'll usually want to call this
     * method for all search dialogs and give them the same search context, so
     * that their options (match case, etc..) stay in sync with one another.
     *
     * @param context The new search context. This cannot be <code>null</code>.
     * @see #getSearchContext()
     */
    public void setSearchContext(SearchContext context) {
        if (this.context != null) {
            this.context.removePropertyChangeListener(contextListener);
        }
        this.context = context;
        this.context.addPropertyChangeListener(contextListener);
        refreshUIFromContext();
    }

    /**
     * Sets the <code>java.lang.String</code> to search for.
     *
     * @param newSearchString The <code>tring</code> to put into the search
     *        field.
     */
    public void setSearchString(String newSearchString) {
        findTextCombo.addItem(newSearchString);
    }

    /**
     * Sets the text for the "Whole Word" check box.
     *
     * @param text The text for the "Whole Word" check box.
     * @see #getWholeWordCheckboxText
     */
    public final void setWholeWordCheckboxText(String text) {
        wholeWordCheckBox.setText(text);
    }

    /**
     * Listens for properties changing in the search context.
     * 
     * @author D. Campione
     * 
     */
    private class SearchContextListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent e) {
            handleSearchContextPropertyChanged(e);
        }

    }
}