/*
 * Open Teradata Viewer ( editor search )
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

package net.sourceforge.open_teradata_viewer.editor.search;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.text.JTextComponent;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxUtilities;
import net.sourceforge.open_teradata_viewer.util.UIUtil;

/**
 * A toolbar for replace operations in a text editor application. This provides
 * a more seamless experience than using a Find or Replace dialog.
 *
 * @author D. Campione
 * @see FindToolBar
 * @see ReplaceDialog
 */
public class ReplaceToolBar extends FindToolBar {

    private static final long serialVersionUID = -6561544738295634512L;

    private JButton replaceButton;
    private JButton replaceAllButton;

    /** Our search listener, cached so we can grab its selected text easily. */
    protected ISearchListener searchListener;

    /**
     * Creates the tool bar.
     *
     * @param listener An entity listening for search events.
     */
    public ReplaceToolBar(ISearchListener listener) {
        super(listener);
        this.searchListener = listener;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        handleToggleButtons();
    }

    @Override
    protected Container createButtonPanel() {
        Box panel = new Box(BoxLayout.LINE_AXIS);

        JPanel bp = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(bp);

        createFindButtons();

        Component filler = Box.createRigidArea(new Dimension(5, 5));

        bp.add(findButton);
        bp.add(replaceButton);
        bp.add(replaceAllButton);
        bp.add(filler);
        panel.add(bp);

        JPanel optionPanel = new JPanel(new SpringLayout());
        matchCaseCheckBox = createCB("MatchCase");
        regexCheckBox = createCB("RegEx");
        wholeWordCheckBox = createCB("WholeWord");
        markAllCheckBox = createCB("MarkAll");
        // We use a "spacing" middle row, instead of spacing in the call to
        // UIUtil.makeSpringCompactGrid(), as the latter adds trailing spacing
        // after the final "row", which screws up our alignment
        Dimension spacing = new Dimension(1, 5);
        Component space1 = Box.createRigidArea(spacing);
        Component space2 = Box.createRigidArea(spacing);

        ComponentOrientation orientation = ComponentOrientation
                .getOrientation(getLocale());

        if (orientation.isLeftToRight()) {
            optionPanel.add(matchCaseCheckBox);
            optionPanel.add(wholeWordCheckBox);
            optionPanel.add(space1);
            optionPanel.add(space2);
            optionPanel.add(regexCheckBox);
            optionPanel.add(markAllCheckBox);
        } else {
            optionPanel.add(wholeWordCheckBox);
            optionPanel.add(matchCaseCheckBox);
            optionPanel.add(space2);
            optionPanel.add(space1);
            optionPanel.add(markAllCheckBox);
            optionPanel.add(regexCheckBox);
        }
        UIUtil.makeSpringCompactGrid(optionPanel, 3, 2, 0, 0, 0, 0);
        panel.add(optionPanel);

        return panel;
    }

    @Override
    protected Container createFieldPanel() {
        findFieldListener = new ReplaceFindFieldListener();

        JPanel temp = new JPanel(new SpringLayout());

        JLabel findLabel = new JLabel("Find What");
        JLabel replaceLabel = new JLabel("Replace With");

        findCombo = new SearchComboBox(this, false);
        JTextComponent findField = UIUtil.getTextComponent(findCombo);
        findFieldListener.install(findField);
        Container fcp = createContentAssistablePanel(findCombo);

        replaceCombo = new SearchComboBox(this, true);
        JTextComponent replaceField = UIUtil.getTextComponent(replaceCombo);
        findFieldListener.install(replaceField);
        Container rcp = createContentAssistablePanel(replaceCombo);

        // We use a "spacing" middle row, instead of spacing in the call to
        // UIUtil.makeSpringCompactGrid(), as the latter adds trailing spacing
        // after the final "row", which screws up our alignment
        Dimension spacing = new Dimension(1, 5);
        Component space1 = Box.createRigidArea(spacing);
        Component space2 = Box.createRigidArea(spacing);

        if (getComponentOrientation().isLeftToRight()) {
            temp.add(findLabel);
            temp.add(fcp);
            temp.add(space1);
            temp.add(space2);
            temp.add(replaceLabel);
            temp.add(rcp);
        } else {
            temp.add(fcp);
            temp.add(findLabel);
            temp.add(space2);
            temp.add(space1);
            temp.add(rcp);
            temp.add(replaceLabel);
        }
        UIUtil.makeSpringCompactGrid(temp, 3, 2, 0, 0, 0, 0);

        return temp;
    }

    @Override
    protected void createFindButtons() {
        super.createFindButtons();

        replaceButton = new JButton("Replace");
        makeEnterActivateButton(replaceButton);
        replaceButton.setToolTipText("Replace.ToolTip");
        replaceButton.setActionCommand("Replace");
        replaceButton.addActionListener(listener);
        replaceButton.setEnabled(false);

        replaceAllButton = new JButton("Replace All");
        makeEnterActivateButton(replaceAllButton);
        replaceAllButton.setActionCommand("ReplaceAll");
        replaceAllButton.addActionListener(listener);
        replaceAllButton.setEnabled(false);
    }

    /**
     * Called when the regex checkbox is clicked (or its value is modified via a
     * change to the search context). Subclasses can override to add custom
     * behavior, but should call the super implementation.
     */
    @Override
    protected void handleRegExCheckBoxClicked() {
        super.handleRegExCheckBoxClicked();
        // "Content assist" support
        boolean b = regexCheckBox.isSelected();
        replaceCombo.setAutoCompleteEnabled(b);
    }

    @Override
    protected void handleSearchAction(ActionEvent e) {
        String command = e.getActionCommand();
        super.handleSearchAction(e);
        if ("FindNext".equals(command) || "FindPrevious".equals(command)) {
            handleToggleButtons(); // Replace button could toggle state
        }
    }

    @Override
    protected FindReplaceButtonsEnableResult handleToggleButtons() {
        FindReplaceButtonsEnableResult er = super.handleToggleButtons();
        boolean shouldReplace = er.getEnable();
        replaceAllButton.setEnabled(shouldReplace);

        // "Replace" is only enabled if text to search for is selected in the UI
        if (shouldReplace) {
            String text = searchListener.getSelectedText();
            shouldReplace = matchesSearchFor(text);
        }
        replaceButton.setEnabled(shouldReplace);

        return er;
    }

    private boolean matchesSearchFor(String text) {
        if (text == null || text.length() == 0) {
            return false;
        }
        String searchFor = findCombo.getSelectedString();
        if (searchFor != null && searchFor.length() > 0) {
            boolean matchCase = matchCaseCheckBox.isSelected();
            if (regexCheckBox.isSelected()) {
                int flags = Pattern.MULTILINE; // '^' and '$' are done per line
                flags = SyntaxUtilities.getPatternFlags(matchCase, flags);
                Pattern pattern = null;
                try {
                    pattern = Pattern.compile(searchFor, flags);
                } catch (PatternSyntaxException pse) {
                    ExceptionDialog.hideException(pse); // Never happens
                    return false;
                }
                return pattern.matcher(text).matches();
            } else {
                if (matchCase) {
                    return searchFor.equals(text);
                }
                return searchFor.equalsIgnoreCase(text);
            }
        }
        return false;
    }

    /**
     * Overridden to possibly toggle the enabled state of the replace button.
     */
    @Override
    public boolean requestFocusInWindow() {
        boolean result = super.requestFocusInWindow();
        handleToggleButtons(); // Replace button state may change
        return result;
    }

    /**
     * Listens for the user typing into the search field.
     * 
     * @author D. Campione
     * 
     */
    protected class ReplaceFindFieldListener extends FindFieldListener {

        @Override
        protected void handleDocumentEvent(DocumentEvent e) {
            super.handleDocumentEvent(e);
            JTextComponent findField = UIUtil.getTextComponent(findCombo);
            JTextComponent replaceField = UIUtil.getTextComponent(replaceCombo);
            if (e.getDocument().equals(findField.getDocument())) {
                handleToggleButtons();
            }
            if (e.getDocument() == replaceField.getDocument()) {
                getSearchContext().setReplaceWith(replaceField.getText());
            }
        }

    }
}