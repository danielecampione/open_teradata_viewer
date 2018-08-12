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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.EscapableDialog;
import net.sourceforge.open_teradata_viewer.ImageManager;
import net.sourceforge.open_teradata_viewer.UISupport;
import net.sourceforge.open_teradata_viewer.editor.SearchContext;
import net.sourceforge.open_teradata_viewer.editor.SearchEngine;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class OTVFindDialog extends EscapableDialog implements ActionListener {

    private static final long serialVersionUID = -6628863532019481270L;

    private SyntaxTextArea textArea;
    private JTextField findField;
    private JCheckBox regexCB;
    private JCheckBox matchCaseCB;
    private JCheckBox wholeWordCB;

    public OTVFindDialog(ApplicationFrame mainFrame) {
        super(mainFrame);
        JPanel cp = new JPanel(new BorderLayout());

        textArea = ApplicationFrame.getInstance().getTextComponent();

        // Create a toolbar with searching options
        JToolBar toolBar = new JToolBar();
        findField = new JTextField(30);
        toolBar.add(findField);
        final JButton nextButton = new JButton("Find next");
        nextButton.setIcon(ImageManager.getImage("/icons/go-down.png"));
        nextButton.setActionCommand("FindNext");
        nextButton.addActionListener(this);
        toolBar.add(nextButton);
        findField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nextButton.doClick(0);
            }
        });
        JButton prevButton = new JButton("Find previous");
        prevButton.setIcon(ImageManager.getImage("/icons/go-up.png"));
        prevButton.setActionCommand("FindPrev");
        prevButton.addActionListener(this);
        toolBar.add(prevButton);
        regexCB = new JCheckBox("Regex");
        toolBar.add(regexCB);
        matchCaseCB = new JCheckBox("Match case");
        toolBar.add(matchCaseCB);
        wholeWordCB = new JCheckBox("Whole word");
        toolBar.add(wholeWordCB);
        cp.add(toolBar, BorderLayout.NORTH);

        setContentPane(cp);
        setTitle("Find");
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public void actionPerformed(ActionEvent e) {
        // "FindNext" => search forward, "FindPrev" => search backward
        String command = e.getActionCommand();
        boolean forward = "FindNext".equals(command);

        // Create an object defining our search parameters
        SearchContext context = new SearchContext();
        String text = findField.getText();
        if (text.length() == 0) {
            return;
        }
        context.setSearchFor(text);
        context.setMatchCase(matchCaseCB.isSelected());
        context.setRegularExpression(regexCB.isSelected());
        context.setWholeWord(wholeWordCB.isSelected());
        context.setSearchForward(forward);

        boolean found = SearchEngine.search(textArea, context);
        if (!found) {
            UISupport.getDialogs().showInfoMessage("Text not found.",
                    "Search results");
        }
    }
}