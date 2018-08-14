/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.MenuElement;
import javax.swing.UIManager;

import net.sourceforge.open_teradata_viewer.actions.Actions;
import net.sourceforge.open_teradata_viewer.actions.AnimatedAssistantAction;
import net.sourceforge.open_teradata_viewer.actions.FancyCellRenderingAction;
import net.sourceforge.open_teradata_viewer.actions.LookAndFeelAction;
import net.sourceforge.open_teradata_viewer.actions.ParameterAssistanceAction;
import net.sourceforge.open_teradata_viewer.actions.ShowDescriptionWindowAction;
import net.sourceforge.open_teradata_viewer.actions.ThemeAction;
import net.sourceforge.open_teradata_viewer.editor.CollapsibleSectionPanel;
import net.sourceforge.open_teradata_viewer.editor.OTVSyntaxTextArea;
import net.sourceforge.open_teradata_viewer.editor.TextArea;
import net.sourceforge.open_teradata_viewer.editor.TextScrollPane;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextAreaEditorKit;
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
    JCheckBoxMenuItem cbPaintMatchedBracketPair = new JCheckBoxMenuItem(
            Actions.PAINT_MATCHED_BRACKET_PAIR);
    private JCheckBoxMenuItem cbCellRendering = new JCheckBoxMenuItem(
            Actions.FANCY_CELL_RENDERING);
    private JCheckBoxMenuItem cbShowDescriptionWindow = new JCheckBoxMenuItem(
            Actions.SHOW_DESCRIPTION_WINDOW);
    private JCheckBoxMenuItem cbParamAssistanceItem = new JCheckBoxMenuItem(
            Actions.PARAMETER_ASSISTANCE);

    public ApplicationMenuBar() {
        JMenu menu;
        JMenu subMenu;

        ApplicationFrame applicationFrame = ApplicationFrame.getInstance();
        CollapsibleSectionPanel csp = applicationFrame
                .getCollapsibleSectionPanel();

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
        menu.add(Actions.PRINT_PREVIEW);
        menu.addSeparator();
        menu.add(Actions.FAVORITES);

        menu = new JMenu("Edit");
        add(menu);
        menu.add(Actions.CUT);
        menu.add(Actions.COPY);
        menu.add(Actions.COPY_AS_RTF);
        menu.add(Actions.PASTE);
        menu.addSeparator();
        menu.add(TextArea.getAction(TextArea.DELETE_ACTION));
        menu.add(TextArea.getAction(TextArea.SELECT_ALL_ACTION));
        menu.addSeparator();
        menu.add(Actions.DATE_TIME);
        menu.addSeparator();
        subMenu = new JMenu("Folding");
        menu.add(subMenu);
        subMenu.add(new SyntaxTextAreaEditorKit.ToggleCurrentFoldAction());
        subMenu.add(new SyntaxTextAreaEditorKit.CollapseAllCommentFoldsAction());
        subMenu.add(new SyntaxTextAreaEditorKit.CollapseAllFoldsAction());
        subMenu.add(new SyntaxTextAreaEditorKit.ExpandAllFoldsAction());
        menu.addSeparator();
        menu.add(Actions.FORMAT_SQL);
        menu.addSeparator();
        subMenu = new JMenu("View");
        menu.add(subMenu);
        subMenu.add(cbViewLineHighlight);
        subMenu.add(cbFadeCurrentLineHighlight);
        subMenu.add(cbViewLineNumbers);
        subMenu.add(cbBookmarks);
        subMenu.add(cbWordWrap);
        subMenu.add(cbAntialiasing);
        subMenu.add(cbMarkOccurrences);
        subMenu.add(cbRightToLeft);
        subMenu.add(cbTabLines);
        subMenu.add(cbAnimateBracketMatching);
        subMenu.add(cbPaintMatchedBracketPair);
        menu.addSeparator();
        subMenu = new JMenu("Text");
        menu.add(subMenu);
        subMenu.add(Actions.COMMENT);
        subMenu.add(Actions.UNCOMMENT);
        subMenu.addSeparator();
        subMenu.add(Actions.INVERT_SELECTION_CASE);
        subMenu.add(Actions.UPPER_SELECTION_CASE);
        subMenu.add(Actions.LOWER_SELECTION_CASE);
        subMenu = new JMenu("Indent");
        menu.add(subMenu);
        subMenu.add(Actions.INCREASE_INDENT);
        subMenu.add(Actions.DECREASE_INDENT);
        menu.add(Actions.INCREASE_FONT_SIZES);
        menu.add(Actions.DECREASE_FONT_SIZES);
        menu.addSeparator();
        menu.add(Actions.GO_TO_LINE);
        int default_modifier = getToolkit().getMenuShortcutKeyMask();
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_F, default_modifier);
        Action a = csp
                .addBottomComponent(ks, applicationFrame.getFindToolBar());
        a.putValue(Action.NAME, "Show Find Search Bar");
        menu.add(new JMenuItem(a));
        ks = KeyStroke.getKeyStroke(KeyEvent.VK_R, default_modifier);
        a = csp.addBottomComponent(ks, applicationFrame.getReplaceToolBar());
        a.putValue(Action.NAME, "Show Replace Search Bar");
        menu.add(new JMenuItem(a));
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
        ButtonGroup buttonGroupEditorTheme = new ButtonGroup();
        subMenu = new JMenu("Editor Theme");
        addThemeItem("Default", "/res/themes/default.xml",
                buttonGroupEditorTheme, subMenu, true);
        addThemeItem("Default (Alternative Version)",
                "/res/themes/default-alt.xml", buttonGroupEditorTheme, subMenu);
        addThemeItem("Dark", "/res/themes/dark.xml", buttonGroupEditorTheme,
                subMenu);
        addThemeItem("Eclipse", "/res/themes/eclipse.xml",
                buttonGroupEditorTheme, subMenu);
        addThemeItem("IDEA", "/res/themes/idea.xml", buttonGroupEditorTheme,
                subMenu);
        addThemeItem("Visual Studio", "/res/themes/vs.xml",
                buttonGroupEditorTheme, subMenu);
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

        // Add any 3rd party Look and Feels in the installation directory
        ExtendedLookAndFeelInfo[] info = applicationFrame
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
        cbCellRendering
                .setSelected(((FancyCellRenderingAction) Actions.FANCY_CELL_RENDERING)
                        .isFancyCellRenderingActivated());
        menu.add(cbCellRendering);
        cbShowDescriptionWindow
                .setSelected(((ShowDescriptionWindowAction) Actions.SHOW_DESCRIPTION_WINDOW)
                        .isVisible());
        menu.add(cbShowDescriptionWindow);
        cbParamAssistanceItem
                .setSelected(((ParameterAssistanceAction) Actions.PARAMETER_ASSISTANCE)
                        .isParameterAssistanceEnabled());
        menu.add(cbParamAssistanceItem);
        menu.addSeparator();
        JCheckBoxMenuItem mniAnimatedAssistant = new JCheckBoxMenuItem(
                Actions.ANIMATED_ASSISTANT);
        mniAnimatedAssistant
                .setSelected(((AnimatedAssistantAction) Actions.ANIMATED_ASSISTANT)
                        .isAnimatedAssistantActivated());
        menu.add(mniAnimatedAssistant);
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
        ApplicationFrame applicationFrame = ApplicationFrame.getInstance();
        OTVSyntaxTextArea _OTVSyntaxTextArea = applicationFrame
                .getTextComponent();
        TextScrollPane textScrollPane = applicationFrame.getTextScrollPane();

        cbViewLineHighlight.setSelected(_OTVSyntaxTextArea
                .getHighlightCurrentLine());
        cbFadeCurrentLineHighlight.setSelected(_OTVSyntaxTextArea
                .getFadeCurrentLineHighlight());
        cbViewLineNumbers.setSelected(textScrollPane.getLineNumbersEnabled());
        cbBookmarks.setSelected(textScrollPane.isIconRowHeaderEnabled());
        cbWordWrap.setSelected(_OTVSyntaxTextArea.getLineWrap());
        cbAntialiasing.setSelected(_OTVSyntaxTextArea.getAntiAliasingEnabled());
        cbMarkOccurrences.setSelected(_OTVSyntaxTextArea.getMarkOccurrences());
        cbRightToLeft.setSelected(!textScrollPane.getComponentOrientation()
                .isLeftToRight());
        cbTabLines.setSelected(_OTVSyntaxTextArea.getPaintTabLines());
        cbAnimateBracketMatching.setSelected(_OTVSyntaxTextArea
                .getAnimateBracketMatching());
        cbPaintMatchedBracketPair.setSelected(_OTVSyntaxTextArea
                .getPaintMatchedBracketPair());
    }

    private void addThemeItem(String name, String themeXml, ButtonGroup bg,
            JMenu menu, boolean isSelected) {
        JRadioButtonMenuItem item = new JRadioButtonMenuItem(new ThemeAction(
                name, themeXml));
        bg.add(item);
        item.setSelected(isSelected);
        menu.add(item);
    }

    private void addThemeItem(String name, String themeXml, ButtonGroup bg,
            JMenu menu) {
        addThemeItem(name, themeXml, bg, menu, false);
    }
}