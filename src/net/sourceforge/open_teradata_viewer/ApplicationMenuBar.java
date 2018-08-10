/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.MenuElement;
import javax.swing.UIManager;

import net.sourceforge.open_teradata_viewer.actions.Actions;
import net.sourceforge.open_teradata_viewer.actions.AnimatedAssistantAction;
import net.sourceforge.open_teradata_viewer.actions.LookAndFeelAction;
import net.sourceforge.open_teradata_viewer.util.array.StringList;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ApplicationMenuBar extends JMenuBar {

    private static final long serialVersionUID = -3435078396857591267L;

    JCheckBoxMenuItem cbViewLineHighlight = new JCheckBoxMenuItem(
            Actions.VIEW_LINE_HIGHLIGHT);
    JCheckBoxMenuItem cbFadeCurrentLineHighlight = new JCheckBoxMenuItem(
            Actions.FADE_CURRENT_LINE_HIGHLIGHT);
    JCheckBoxMenuItem cbViewLineNumbers = new JCheckBoxMenuItem(
            Actions.VIEW_LINE_NUMBERS);
    JCheckBoxMenuItem cbBookmarks = new JCheckBoxMenuItem(Actions.BOOKMARKS);
    JCheckBoxMenuItem cbWordWrap = new JCheckBoxMenuItem(Actions.WORD_WRAP);
    JCheckBoxMenuItem cbAntialiasing = new JCheckBoxMenuItem(
            Actions.ANTIALIASING);
    JCheckBoxMenuItem cbMarkOccurrences = new JCheckBoxMenuItem(
            Actions.MARK_OCCURRENCES);
    JCheckBoxMenuItem cbRightToLeft = new JCheckBoxMenuItem(
            Actions.RIGHT_TO_LEFT);
    JCheckBoxMenuItem cbTabLines = new JCheckBoxMenuItem(Actions.TAB_LINES);
    JCheckBoxMenuItem cbAnimateBracketMatching = new JCheckBoxMenuItem(
            Actions.ANIMATE_BRACKET_MATCHING);

    public ApplicationMenuBar() {
        JMenu menu;
        JMenu subMenu;

        menu = new JMenu("Connection");
        add(menu);
        menu.add(Actions.CONNECT);
        menu.add(Actions.DISCONNECT);
        menu.addSeparator();
        menu.add(Actions.COMMIT);
        menu.add(Actions.ROLLBACK);

        menu = new JMenu("File");
        add(menu);
        menu.add(Actions.FILE_OPEN);
        menu.add(Actions.FILE_SAVE);
        menu.addSeparator();
        menu.add(Actions.FAVORITES);

        menu = new JMenu("Edit");
        add(menu);
        menu.add(Actions.CUT);
        menu.add(Actions.COPY);
        menu.add(Actions.COPY_AS_RTF);
        menu.add(Actions.PASTE);
        menu.addSeparator();
        menu.add(Actions.FORMAT_SQL);
        menu.addSeparator();
        menu.add(cbViewLineHighlight);
        menu.add(cbFadeCurrentLineHighlight);
        menu.add(cbViewLineNumbers);
        menu.add(cbBookmarks);
        menu.add(cbWordWrap);
        menu.add(cbAntialiasing);
        menu.add(cbMarkOccurrences);
        menu.add(cbRightToLeft);
        menu.add(cbTabLines);
        menu.add(cbAnimateBracketMatching);
        menu.addSeparator();
        menu.add(Actions.COMMENT);
        menu.add(Actions.UNCOMMENT);
        menu.add(Actions.DATE_TIME);
        subMenu = new JMenu("Indent");
        menu.add(subMenu);
        subMenu.add(Actions.INCREASE_INDENT);
        subMenu.add(Actions.DECREASE_INDENT);
        menu.add(Actions.INCREASE_FONT_SIZES);
        menu.add(Actions.DECREASE_FONT_SIZES);
        menu.addSeparator();
        menu.add(Actions.GO_TO_LINE);
        menu.add(Actions.FIND);
        menu.addSeparator();
        menu.add(Actions.HISTORY_PREVIOUS);
        menu.add(Actions.HISTORY_NEXT);

        refreshEditOptions();

        menu = new JMenu("Schema Browser");
        add(menu);
        menu.add(Actions.SCHEMA_BROWSER);
        menu.add(Actions.FETCH_LIMIT);
        menu.add(Actions.SELECT_FROM);
        menu.addSeparator();
        menu.add(Actions.DRIVERS);
        menu.addSeparator();
        menu.add(Actions.INSERT);
        menu.add(Actions.DELETE);
        menu.add(Actions.EDIT);
        menu.add(Actions.DUPLICATE);
        menu.addSeparator();
        subMenu = new JMenu("Lob");
        menu.add(subMenu);
        subMenu.add(Actions.LOB_EXPORT);
        subMenu.add(Actions.LOB_IMPORT);
        subMenu.add(Actions.LOB_COPY);
        subMenu.add(Actions.LOB_PASTE);
        subMenu = new JMenu("Export");
        menu.add(subMenu);
        subMenu.add(Actions.EXPORT_EXCEL);
        subMenu.add(Actions.EXPORT_PDF);
        subMenu.add(Actions.EXPORT_FLAT_FILE);
        subMenu.add(Actions.EXPORT_INSERTS);
        subMenu = new JMenu("Show");
        menu.add(subMenu);
        subMenu.add(Actions.SHOW_TABLE);
        subMenu.add(Actions.SHOW_VIEW);
        subMenu.add(Actions.SHOW_PROCEDURE);
        subMenu.add(Actions.SHOW_MACRO);
        menu.addSeparator();
        menu.add(Actions.EXPLAIN_REQUEST);
        menu.add(Actions.ANALYZE_QUERY);
        menu.addSeparator();
        menu.add(Actions.RUN);
        menu.add(Actions.RUN_SCRIPT);

        menu = new JMenu(ApplicationFrame.LAF_MENU_LABEL);
        add(menu);
        subMenu = new JMenu("Editor Theme");
        JRadioButtonMenuItem radioButtonDefaultTheme = new JRadioButtonMenuItem(
                Actions.DEFAULT_THEME);
        radioButtonDefaultTheme.setSelected(true);
        JRadioButtonMenuItem radioButtonDarkTheme = new JRadioButtonMenuItem(
                Actions.DARK_THEME);
        JRadioButtonMenuItem radioButtonEclipseTheme = new JRadioButtonMenuItem(
                Actions.ECLIPSE_THEME);
        JRadioButtonMenuItem radioButtonVisualStudioTheme = new JRadioButtonMenuItem(
                Actions.VISUAL_STUDIO_THEME);
        ButtonGroup buttonGroupEditorTheme = new ButtonGroup();
        buttonGroupEditorTheme.add(radioButtonDefaultTheme);
        buttonGroupEditorTheme.add(radioButtonDarkTheme);
        buttonGroupEditorTheme.add(radioButtonEclipseTheme);
        buttonGroupEditorTheme.add(radioButtonVisualStudioTheme);
        subMenu.add(radioButtonDefaultTheme);
        subMenu.add(radioButtonDarkTheme);
        subMenu.add(radioButtonEclipseTheme);
        subMenu.add(radioButtonVisualStudioTheme);
        menu.add(subMenu);
        menu.addSeparator();
        UIManager.LookAndFeelInfo[] lafInfo = UIManager
                .getInstalledLookAndFeels();
        StringList completePathOfLafClasses = new StringList(true,
                lafInfo.length);
        ButtonGroup buttonGroupLookAndFeel = new ButtonGroup();
        Vector<JRadioButtonMenuItem> _mnuAvailableLookAndFeel = new Vector<JRadioButtonMenuItem>(
                lafInfo.length, 1);
        for (int i = 0; i < lafInfo.length; i++) {
            String name = lafInfo[i].getName();
            _mnuAvailableLookAndFeel.add(new JRadioButtonMenuItem(name));
            completePathOfLafClasses.add(lafInfo[i].getClassName());
            buttonGroupLookAndFeel.add(_mnuAvailableLookAndFeel.elementAt(i));
            menu.add(_mnuAvailableLookAndFeel.elementAt(i));
            _mnuAvailableLookAndFeel.elementAt(i).addActionListener(
                    new LookAndFeelAction(completePathOfLafClasses.get(i)));
            if (completePathOfLafClasses.get(i).equals(
                    UIManager.getLookAndFeel().getClass().toString()
                            .substring("class ".length()))) {
                _mnuAvailableLookAndFeel.elementAt(i).setSelected(true);
            }
        }

        // Add any 3rd party Look and Feels in the lookandfeels subdirectory
        ExtendedLookAndFeelInfo[] info = ApplicationFrame.getInstance()
                .get3rdPartyLookAndFeelInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (!completePathOfLafClasses.exists(info[i].getClassName())) {
                    _mnuAvailableLookAndFeel.add(new JRadioButtonMenuItem(
                            info[i].getName()));
                    completePathOfLafClasses.add(info[i].getClassName());
                    buttonGroupLookAndFeel.add(_mnuAvailableLookAndFeel
                            .elementAt(completePathOfLafClasses.size() - 1));
                    menu.add(_mnuAvailableLookAndFeel
                            .elementAt(completePathOfLafClasses.size() - 1));
                    _mnuAvailableLookAndFeel
                            .elementAt(completePathOfLafClasses.size() - 1)
                            .addActionListener(
                                    new LookAndFeelAction(
                                            completePathOfLafClasses
                                                    .get(completePathOfLafClasses
                                                            .size() - 1)));
                    if (completePathOfLafClasses.get(
                            completePathOfLafClasses.size() - 1).equals(
                            UIManager.getLookAndFeel().getClass().toString()
                                    .substring("class ".length()))) {
                        _mnuAvailableLookAndFeel.elementAt(
                                completePathOfLafClasses.size() - 1)
                                .setSelected(true);
                    }
                }
            }
        }

        menu = new JMenu("View");
        add(menu);
        JCheckBoxMenuItem mniAnimatedAssistant = new JCheckBoxMenuItem(
                Actions.ANIMATED_ASSISTANT);
        mniAnimatedAssistant
                .setSelected(((AnimatedAssistantAction) Actions.ANIMATED_ASSISTANT)
                        .isAnimatedAssistantActived());
        menu.add(mniAnimatedAssistant);
        menu.addSeparator();
        JCheckBoxMenuItem fullScreenMenuItem = new JCheckBoxMenuItem(
                Actions.FULL_SCREEN);
        menu.add(fullScreenMenuItem);

        menu = new JMenu("?");
        add(menu);
        menu.add(Actions.HELP);
        menu.addSeparator();
        menu.add(Actions.ABOUT);

        new UpdateChecker(this).check();

        setMnemonics(this);
    }

    private void setMnemonics(MenuElement menuElement) {
        Set<Character> used = new HashSet<Character>();
        MenuElement[] subElements = menuElement.getSubElements();
        for (MenuElement subElement : subElements) {
            AbstractButton item = (AbstractButton) subElement;
            char[] chars = item.getText().toCharArray();
            for (char aChar : chars) {
                if (used.add(aChar)) {
                    item.setMnemonic(aChar);
                    break;
                }
            }
            if (item instanceof JMenu) {
                setMnemonics(((JMenu) item).getPopupMenu());
            }
        }
    }

    public void refreshEditOptions() {
        cbViewLineHighlight.setSelected(ApplicationFrame.getInstance()
                .getTextComponent().getHighlightCurrentLine());
        cbFadeCurrentLineHighlight.setSelected(ApplicationFrame.getInstance()
                .getTextComponent().getFadeCurrentLineHighlight());
        cbViewLineNumbers.setSelected(ApplicationFrame.getInstance()
                .getTextScrollPane().getLineNumbersEnabled());
        cbBookmarks.setSelected(ApplicationFrame.getInstance()
                .getTextScrollPane().isIconRowHeaderEnabled());
        cbWordWrap.setSelected(ApplicationFrame.getInstance()
                .getTextComponent().getLineWrap());
        cbAntialiasing.setSelected(ApplicationFrame.getInstance()
                .getTextComponent().getAntiAliasingEnabled());
        cbMarkOccurrences.setSelected(ApplicationFrame.getInstance()
                .getTextComponent().getMarkOccurrences());
        cbRightToLeft.setSelected(!ApplicationFrame.getInstance()
                .getTextScrollPane().getComponentOrientation().isLeftToRight());
        cbTabLines.setSelected(ApplicationFrame.getInstance()
                .getTextComponent().getPaintTabLines());
        cbAnimateBracketMatching.setSelected(ApplicationFrame.getInstance()
                .getTextComponent().getAnimateBracketMatching());
    }
}