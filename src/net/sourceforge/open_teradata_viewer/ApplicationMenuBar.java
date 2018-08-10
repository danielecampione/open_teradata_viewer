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
import net.sourceforge.open_teradata_viewer.actions.ChangeLookAndFeelAction;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ApplicationMenuBar extends JMenuBar {

    private static final long serialVersionUID = -3435078396857591267L;

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
        if (Actions.CUT_COPY_PASTE_ENABLED) {
            menu.add(Actions.CUT);
            menu.add(Actions.COPY);
            menu.add(Actions.PASTE);
            menu.addSeparator();
        }
        menu.add(Actions.FORMAT_SQL);
        menu.addSeparator();
        menu.add(Actions.HISTORY_PREVIOUS);
        menu.add(Actions.HISTORY_NEXT);

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
        menu.addSeparator();
        subMenu = new JMenu("Export");
        menu.add(subMenu);
        subMenu.add(Actions.EXPORT_EXCEL);
        subMenu.add(Actions.EXPORT_PDF);
        subMenu.add(Actions.EXPORT_FLAT_FILE);
        subMenu.add(Actions.EXPORT_INSERTS);
        menu.addSeparator();
        subMenu = new JMenu("Show");
        menu.add(subMenu);
        subMenu.add(Actions.SHOW_TABLE);
        subMenu.add(Actions.SHOW_VIEW);
        subMenu.add(Actions.SHOW_PROCEDURE);
        subMenu.add(Actions.SHOW_MACRO);
        menu.addSeparator();
        menu.add(Actions.EXPLAIN_REQUEST);
        menu.addSeparator();
        menu.add(Actions.ANALYZE_QUERY);
        menu.addSeparator();
        menu.add(Actions.RUN);
        menu.add(Actions.RUN_SCRIPT);

        menu = new JMenu(ApplicationFrame.LAF_MENU_LABEL);
        add(menu);
        UIManager.LookAndFeelInfo[] lafInfo = UIManager
                .getInstalledLookAndFeels();
        String[] completePathOfLafClasses = new String[lafInfo.length];
        ButtonGroup buttonGroupLookAndFeel = new ButtonGroup();
        JRadioButtonMenuItem[] _mnuAvailableLookAndFeel = new JRadioButtonMenuItem[lafInfo.length];
        for (int i = 0; i < lafInfo.length; i++) {
            completePathOfLafClasses[i] = lafInfo[i].getClassName();
            _mnuAvailableLookAndFeel[i] = new JRadioButtonMenuItem(
                    completePathOfLafClasses[i]
                            .substring(completePathOfLafClasses[i]
                                    .lastIndexOf(".") + 1));
            buttonGroupLookAndFeel.add(_mnuAvailableLookAndFeel[i]);
            menu.add(_mnuAvailableLookAndFeel[i]);
            _mnuAvailableLookAndFeel[i]
                    .addActionListener(new ChangeLookAndFeelAction(
                            completePathOfLafClasses[i]));
            if (completePathOfLafClasses[i].equals(UIManager.getLookAndFeel()
                    .getClass().toString().substring("class ".length())))
                _mnuAvailableLookAndFeel[i].setSelected(true);
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
}
