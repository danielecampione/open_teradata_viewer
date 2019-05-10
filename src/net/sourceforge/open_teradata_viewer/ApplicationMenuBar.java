/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Iterator;
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

import org.fife.rsta.ui.CollapsibleSectionPanel;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaEditorKit;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import net.sourceforge.open_teradata_viewer.actions.Actions;
import net.sourceforge.open_teradata_viewer.actions.ChangeSyntaxStyleAction;
import net.sourceforge.open_teradata_viewer.actions.FancyCellRenderingAction;
import net.sourceforge.open_teradata_viewer.actions.LookAndFeelAction;
import net.sourceforge.open_teradata_viewer.actions.MatchedBracketPopupAction;
import net.sourceforge.open_teradata_viewer.actions.ParameterAssistanceAction;
import net.sourceforge.open_teradata_viewer.actions.RunMacroAction;
import net.sourceforge.open_teradata_viewer.actions.ShowDescriptionWindowAction;
import net.sourceforge.open_teradata_viewer.actions.ThemeAction;
import net.sourceforge.open_teradata_viewer.editor.macros.Macro;
import net.sourceforge.open_teradata_viewer.editor.macros.MacroManager;
import net.sourceforge.open_teradata_viewer.util.array.StringList;

/**
 *
 *
 * @author D. Campione
 *
 */
public class ApplicationMenuBar extends JMenuBar implements PropertyChangeListener {

    private static final long serialVersionUID = -3435078396857591267L;

    protected JCheckBoxMenuItem cbViewLineHighlight = new JCheckBoxMenuItem(Actions.VIEW_LINE_HIGHLIGHT);
    protected JCheckBoxMenuItem cbFadeCurrentLineHighlight = new JCheckBoxMenuItem(Actions.FADE_CURRENT_LINE_HIGHLIGHT);
    protected JCheckBoxMenuItem cbViewLineNumbers = new JCheckBoxMenuItem(Actions.VIEW_LINE_NUMBERS);
    protected JCheckBoxMenuItem cbBookmarks = new JCheckBoxMenuItem(Actions.BOOKMARKS);
    protected JCheckBoxMenuItem cbWordWrap = new JCheckBoxMenuItem(Actions.WORD_WRAP);
    protected JCheckBoxMenuItem cbAntialiasing = new JCheckBoxMenuItem(Actions.ANTIALIASING);
    protected JCheckBoxMenuItem cbMarkOccurrences = new JCheckBoxMenuItem(Actions.MARK_OCCURRENCES);
    protected JCheckBoxMenuItem cbRightToLeft = new JCheckBoxMenuItem(Actions.RIGHT_TO_LEFT);
    protected JCheckBoxMenuItem cbTabLines = new JCheckBoxMenuItem(Actions.TAB_LINES);
    protected JCheckBoxMenuItem cbAnimateBracketMatching = new JCheckBoxMenuItem(Actions.ANIMATE_BRACKET_MATCHING);
    protected JCheckBoxMenuItem cbPaintMatchedBracketPair = new JCheckBoxMenuItem(Actions.PAINT_MATCHED_BRACKET_PAIR);
    protected JCheckBoxMenuItem cbTabsEmulatedBySpaces = new JCheckBoxMenuItem(Actions.TABS_EMULATED_BY_SPACES);
    private JCheckBoxMenuItem cbCellRendering = new JCheckBoxMenuItem(Actions.FANCY_CELL_RENDERING);
    private JCheckBoxMenuItem cbShowDescriptionWindow = new JCheckBoxMenuItem(Actions.SHOW_DESCRIPTION_WINDOW);
    private JCheckBoxMenuItem cbParamAssistanceItem = new JCheckBoxMenuItem(Actions.PARAMETER_ASSISTANCE);
    private JCheckBoxMenuItem cbMatchedBracketPopupItem = new JCheckBoxMenuItem(Actions.MATCHED_BRACKET_POPUP);
    private JScrollMenu macrosMenu;

    public ApplicationMenuBar() {
        JMenu menu;
        JMenu subMenu;

        ApplicationFrame applicationFrame = ApplicationFrame.getInstance();
        CollapsibleSectionPanel csp = applicationFrame.getCollapsibleSectionPanel();

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
        menu.add(Actions.CALCULATE_MD5_CHECKSUM);
        menu.add(Actions.CALCULATE_SHA1_CHECKSUM_OF_A_FILE);
        menu.add(Actions.CALCULATE_SHA1_CHECKSUM_OF_A_TEXT);
        menu.addSeparator();
        menu.add(Actions.PRINT_PREVIEW);
        menu.addSeparator();
        menu.add(Actions.FAVORITES);

        menu = new JMenu("Edit");
        add(menu);
        menu.add(Actions.CUT);
        menu.add(Actions.COPY);
        menu.add(Actions.COPY_AS_STYLED_TEXT);
        menu.add(Actions.PASTE);
        menu.addSeparator();
        menu.add(RTextArea.getAction(RTextArea.DELETE_ACTION));
        menu.add(RTextArea.getAction(RTextArea.SELECT_ALL_ACTION));
        menu.addSeparator();
        menu.add(Actions.DATE_TIME);
        menu.addSeparator();
        subMenu = new JMenu("Folding");
        menu.add(subMenu);
        subMenu.add(new RSyntaxTextAreaEditorKit.ToggleCurrentFoldAction());
        subMenu.add(new RSyntaxTextAreaEditorKit.CollapseAllCommentFoldsAction());
        subMenu.add(new RSyntaxTextAreaEditorKit.CollapseAllFoldsAction());
        subMenu.add(new RSyntaxTextAreaEditorKit.ExpandAllFoldsAction());
        menu.addSeparator();
        menu.add(Actions.FORMAT_SQL);
        subMenu = new JMenu("XML Tools");
        menu.add(subMenu);
        subMenu.add(Actions.INDENT_XML);
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
        subMenu.add(cbTabsEmulatedBySpaces);
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
        JCheckBoxMenuItem cbItemToggleSpellingParser = new JCheckBoxMenuItem(Actions.TOGGLE_SPELLING_PARSER);
        cbItemToggleSpellingParser.setSelected(true);
        menu.add(cbItemToggleSpellingParser);
        menu.addSeparator();
        menu.add(Actions.HISTORY_PREVIOUS);
        menu.add(Actions.HISTORY_NEXT);

        refreshEditOptions();

        menu = new JMenu("Search");
        add(menu);
        menu.add(Actions.SHOW_FIND_DIALOG);
        menu.add(Actions.SHOW_REPLACE_DIALOG);
        menu.addSeparator();
        int default_modifier = getToolkit().getMenuShortcutKeyMask();
        KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_F, default_modifier);
        Action a = csp.addBottomComponent(ks, applicationFrame.getFindToolBar());
        a.putValue(Action.NAME, "Show Find Search Bar");
        menu.add(new JMenuItem(a));
        ks = KeyStroke.getKeyStroke(KeyEvent.VK_R, default_modifier);
        a = csp.addBottomComponent(ks, applicationFrame.getReplaceToolBar());
        a.putValue(Action.NAME, "Show Replace Search Bar");
        menu.add(new JMenuItem(a));
        menu.addSeparator();
        menu.add(Actions.GO_TO_LINE);

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

        JScrollMenu menu2 = new JScrollMenu(ApplicationFrame.LAF_MENU_LABEL);
        add(menu2);
        ButtonGroup buttonGroupEditorTheme = new ButtonGroup();
        subMenu = new JMenu("Editor Theme");
        addThemeItem("Default", "/res/themes/default.xml", buttonGroupEditorTheme, subMenu, true);
        addThemeItem("Default (Alternative Version)", "/res/themes/default-alt.xml", buttonGroupEditorTheme, subMenu);
        addThemeItem("Dark", "/res/themes/dark.xml", buttonGroupEditorTheme, subMenu);
        addThemeItem("Eclipse", "/res/themes/eclipse.xml", buttonGroupEditorTheme, subMenu);
        addThemeItem("IDEA", "/res/themes/idea.xml", buttonGroupEditorTheme, subMenu);
        addThemeItem("Monokai", "/res/themes/monokai.xml", buttonGroupEditorTheme, subMenu);
        addThemeItem("Visual Studio", "/res/themes/vs.xml", buttonGroupEditorTheme, subMenu);
        menu2.add(subMenu);
        menu2.addSeparator();
        UIManager.LookAndFeelInfo[] lafInfo = UIManager.getInstalledLookAndFeels();
        StringList completePathOfLafClasses = new StringList(true, lafInfo.length);
        ButtonGroup buttonGroupLookAndFeel = new ButtonGroup();
        Vector<JRadioButtonMenuItem> _mnuAvailableLookAndFeel = new Vector<JRadioButtonMenuItem>(lafInfo.length, 1);
        for (int i = 0; i < lafInfo.length; i++) {
            String name = lafInfo[i].getName();
            _mnuAvailableLookAndFeel.add(new JRadioButtonMenuItem(name));
            completePathOfLafClasses.add(lafInfo[i].getClassName());
            buttonGroupLookAndFeel.add(_mnuAvailableLookAndFeel.elementAt(i));
            menu2.add(_mnuAvailableLookAndFeel.elementAt(i));
            _mnuAvailableLookAndFeel.elementAt(i)
                    .addActionListener(new LookAndFeelAction(completePathOfLafClasses.get(i)));
            if (completePathOfLafClasses.get(i)
                    .equals(UIManager.getLookAndFeel().getClass().toString().substring("class ".length()))) {
                _mnuAvailableLookAndFeel.elementAt(i).setSelected(true);
            }
        }

        // Add any 3rd party Look and Feels in the installation directory
        ExtendedLookAndFeelInfo[] info = applicationFrame.get3rdPartyLookAndFeelInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (!completePathOfLafClasses.exists(info[i].getClassName())) {
                    _mnuAvailableLookAndFeel.add(new JRadioButtonMenuItem(info[i].getName()));
                    completePathOfLafClasses.add(info[i].getClassName());
                    buttonGroupLookAndFeel.add(_mnuAvailableLookAndFeel.elementAt(completePathOfLafClasses.size() - 1));
                    menu2.add(_mnuAvailableLookAndFeel.elementAt(completePathOfLafClasses.size() - 1));
                    _mnuAvailableLookAndFeel.elementAt(completePathOfLafClasses.size() - 1).addActionListener(
                            new LookAndFeelAction(completePathOfLafClasses.get(completePathOfLafClasses.size() - 1)));
                    if (completePathOfLafClasses.get(completePathOfLafClasses.size() - 1)
                            .equals(UIManager.getLookAndFeel().getClass().toString().substring("class ".length()))) {
                        _mnuAvailableLookAndFeel.elementAt(completePathOfLafClasses.size() - 1).setSelected(true);
                    }
                }
            }
        }

        menu = new JMenu("View");
        add(menu);
        JScrollMenu subMenu2 = new JScrollMenu("View As (Highlighting File Type)");
        ButtonGroup bg = new ButtonGroup();
        addSyntaxItem("SQL", SyntaxConstants.SYNTAX_STYLE_SQL, bg, subMenu2);
        addSyntaxItem("Assembler (x86)", SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86, bg, subMenu2);
        addSyntaxItem("C", SyntaxConstants.SYNTAX_STYLE_C, bg, subMenu2);
        addSyntaxItem("C++", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS, bg, subMenu2);
        addSyntaxItem("CSS", SyntaxConstants.SYNTAX_STYLE_CSS, bg, subMenu2);
        addSyntaxItem("CSV", SyntaxConstants.SYNTAX_STYLE_CSV, bg, subMenu2);
        addSyntaxItem("C#", SyntaxConstants.SYNTAX_STYLE_CSHARP, bg, subMenu2);
        addSyntaxItem("Clojure", SyntaxConstants.SYNTAX_STYLE_CLOJURE, bg, subMenu2);
        addSyntaxItem("D", SyntaxConstants.SYNTAX_STYLE_D, bg, subMenu2);
        addSyntaxItem("Dart", SyntaxConstants.SYNTAX_STYLE_DART, bg, subMenu2);
        addSyntaxItem("Docker", SyntaxConstants.SYNTAX_STYLE_DOCKERFILE, bg, subMenu2);
        addSyntaxItem("Groovy", SyntaxConstants.SYNTAX_STYLE_GROOVY, bg, subMenu2);
        addSyntaxItem("Hosts", SyntaxConstants.SYNTAX_STYLE_HOSTS, bg, subMenu2);
        addSyntaxItem("HTML", SyntaxConstants.SYNTAX_STYLE_HTML, bg, subMenu2);
        addSyntaxItem("INI", SyntaxConstants.SYNTAX_STYLE_INI, bg, subMenu2);
        addSyntaxItem("Java", SyntaxConstants.SYNTAX_STYLE_JAVA, bg, subMenu2);
        addSyntaxItem("JavaScript", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT, bg, subMenu2);
        addSyntaxItem("JSON", SyntaxConstants.SYNTAX_STYLE_JSON, bg, subMenu2);
        addSyntaxItem("JSP", SyntaxConstants.SYNTAX_STYLE_JSP, bg, subMenu2);
        addSyntaxItem("Less", SyntaxConstants.SYNTAX_STYLE_LESS, bg, subMenu2);
        addSyntaxItem("Lisp", SyntaxConstants.SYNTAX_STYLE_LISP, bg, subMenu2);
        addSyntaxItem("Lua", SyntaxConstants.SYNTAX_STYLE_LUA, bg, subMenu2);
        addSyntaxItem("MXML", SyntaxConstants.SYNTAX_STYLE_MXML, bg, subMenu2);
        addSyntaxItem("NSIS", SyntaxConstants.SYNTAX_STYLE_NSIS, bg, subMenu2);
        addSyntaxItem("Perl", SyntaxConstants.SYNTAX_STYLE_PERL, bg, subMenu2);
        addSyntaxItem("PHP", SyntaxConstants.SYNTAX_STYLE_PHP, bg, subMenu2);
        addSyntaxItem("Python", SyntaxConstants.SYNTAX_STYLE_PYTHON, bg, subMenu2);
        addSyntaxItem("Ruby", SyntaxConstants.SYNTAX_STYLE_RUBY, bg, subMenu2);
        addSyntaxItem("Scala", SyntaxConstants.SYNTAX_STYLE_SCALA, bg, subMenu2);
        addSyntaxItem("TypeScript", SyntaxConstants.SYNTAX_STYLE_TYPESCRIPT, bg, subMenu2);
        addSyntaxItem("Unix Shell", SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL, bg, subMenu2);
        addSyntaxItem("Visual Basic", SyntaxConstants.SYNTAX_STYLE_VISUAL_BASIC, bg, subMenu2);
        addSyntaxItem("Windows batch", SyntaxConstants.SYNTAX_STYLE_WINDOWS_BATCH, bg, subMenu2);
        addSyntaxItem("XML", SyntaxConstants.SYNTAX_STYLE_XML, bg, subMenu2);
        addSyntaxItem("YAML", SyntaxConstants.SYNTAX_STYLE_YAML, bg, subMenu2);
        addSyntaxItem("No Highlighting", SyntaxConstants.SYNTAX_STYLE_NONE, bg, subMenu2);
        JRadioButtonMenuItem radioButtonMenu = (JRadioButtonMenuItem) subMenu2.getSubElements()[0].getSubElements()[0];
        radioButtonMenu.setSelected(true);
        menu.add(subMenu2);
        menu.addSeparator();
        cbCellRendering
                .setSelected(((FancyCellRenderingAction) Actions.FANCY_CELL_RENDERING).isFancyCellRenderingActivated());
        menu.add(cbCellRendering);
        cbShowDescriptionWindow
                .setSelected(((ShowDescriptionWindowAction) Actions.SHOW_DESCRIPTION_WINDOW).isVisible());
        menu.add(cbShowDescriptionWindow);
        cbParamAssistanceItem
                .setSelected(((ParameterAssistanceAction) Actions.PARAMETER_ASSISTANCE).isParameterAssistanceEnabled());
        menu.add(cbParamAssistanceItem);
        cbMatchedBracketPopupItem.setSelected(
                ((MatchedBracketPopupAction) Actions.MATCHED_BRACKET_POPUP).isMatchedBracketPopupEnabled());
        menu.add(cbMatchedBracketPopupItem);
        menu.addSeparator();
        JCheckBoxMenuItem fullScreenMenuItem = new JCheckBoxMenuItem(Actions.FULL_SCREEN);
        menu.add(fullScreenMenuItem);

        menu = new JMenu("?");
        add(menu);
        menu.add(Actions.HELP);
        menu.addSeparator();
        menu.add(Actions.ABOUT);

        macrosMenu = new JScrollMenu("Macros");
        add(macrosMenu);
        macrosMenu.add(Actions.NEW_MACRO);
        macrosMenu.add(Actions.EDIT_MACRO);
        applicationFrame.loadMacros();
        refreshMacrosMenu();

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
        RSyntaxTextArea _OTVSyntaxTextArea = applicationFrame.getTextComponent();
        RTextScrollPane textScrollPane = applicationFrame.getTextScrollPane();

        cbViewLineHighlight.setSelected(_OTVSyntaxTextArea.getHighlightCurrentLine());
        cbFadeCurrentLineHighlight.setSelected(_OTVSyntaxTextArea.getFadeCurrentLineHighlight());
        cbViewLineNumbers.setSelected(textScrollPane.getLineNumbersEnabled());
        cbBookmarks.setSelected(textScrollPane.isIconRowHeaderEnabled());
        cbWordWrap.setSelected(_OTVSyntaxTextArea.getLineWrap());
        cbAntialiasing.setSelected(_OTVSyntaxTextArea.getAntiAliasingEnabled());
        cbMarkOccurrences.setSelected(_OTVSyntaxTextArea.getMarkOccurrences());
        cbRightToLeft.setSelected(!textScrollPane.getComponentOrientation().isLeftToRight());
        cbTabLines.setSelected(_OTVSyntaxTextArea.getPaintTabLines());
        cbAnimateBracketMatching.setSelected(_OTVSyntaxTextArea.getAnimateBracketMatching());
        cbPaintMatchedBracketPair.setSelected(_OTVSyntaxTextArea.getPaintMatchedBracketPair());
        cbTabsEmulatedBySpaces.setSelected(_OTVSyntaxTextArea.getTabsEmulated());
    }

    private void addThemeItem(String name, String themeXml, ButtonGroup bg, JMenu menu, boolean isSelected) {
        JRadioButtonMenuItem item = new JRadioButtonMenuItem(new ThemeAction(name, themeXml));
        bg.add(item);
        item.setSelected(isSelected);
        menu.add(item);
    }

    private void addThemeItem(String name, String themeXml, ButtonGroup bg, JMenu menu) {
        addThemeItem(name, themeXml, bg, menu, false);
    }

    private void addSyntaxItem(String name, String style, ButtonGroup bg, JScrollMenu menu) {
        JRadioButtonMenuItem item = new JRadioButtonMenuItem(new ChangeSyntaxStyleAction(name, style));
        bg.add(item);
        menu.add(item);
    }

    /** {@inheritDoc} */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();

        if (MacroManager.PROPERTY_MACROS.equals(prop)) {
            refreshMacrosMenu();
        }
    }

    /**
     * Refreshes the elements in the Macros menu to be in sync with the macros
     * the user has defined.
     */
    public void refreshMacrosMenu() {
        JScrollPopupMenu menu = (JScrollPopupMenu) macrosMenu.getSubElements()[0];
        while (menu.getSubElements().length > 2) {
            menu.remove(2);
        }

        macrosMenu.addSeparator();
        if (MacroManager.get().getMacroCount() > 0) {
            Iterator<Macro> i = MacroManager.get().getMacroIterator();
            while (i.hasNext()) {
                Macro macro = i.next();
                RunMacroAction a = new RunMacroAction(macro);
                macrosMenu.add(createMenuItem(a));
            }
        } else {
            String text = "No Macros Defined";
            JMenuItem item = new JMenuItem(text);
            item.setEnabled(false);
            macrosMenu.add(item);
        }

        remove(macrosMenu);
        add(macrosMenu);
    }

    public JMenu getMacrosMenu() {
        return macrosMenu;
    }

    /**
     * Creates a menu item from an action, with no tool tip.
     *
     * @param a The action.
     * @return The menu item.
     */
    private static final JMenuItem createMenuItem(Action a) {
        JMenuItem item = new JMenuItem(a);
        item.setToolTipText(null);
        return item;
    }
}