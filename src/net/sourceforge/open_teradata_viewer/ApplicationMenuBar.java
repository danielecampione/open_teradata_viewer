/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2011, D. Campione
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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.MenuElement;
import javax.swing.UIManager;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;

import net.sourceforge.open_teradata_viewer.actions.Actions;
import net.sourceforge.open_teradata_viewer.actions.AnimatedLoadingAction;
import net.sourceforge.open_teradata_viewer.actions.ChangeLookAndFeelAction;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ApplicationMenuBar extends JMenuBar {

    private static final long serialVersionUID = -3435078396857591267L;

    private HashMap<Object, Action> actions;

    public ApplicationMenuBar() {
        JMenu menu;
        
        menu = new JMenu("Connection");
        menu = addMouseSensitivityToJMenu(menu);
        add(menu);
        menu.add(Actions.CONNECT);
        menu.addSeparator();
        menu.add(Actions.DISCONNECT);

        menu = new JMenu("File");
        menu = addMouseSensitivityToJMenu(menu);
        add(menu);
        menu.add(Actions.FILE_OPEN);
        menu.add(Actions.FILE_SAVE);
        menu.addSeparator();
        menu.add(Actions.FAVORITES);

        menu = new JMenu("Edit");
        menu = addMouseSensitivityToJMenu(menu);
        add(menu);
        // Set up the edit actions
        actions = createActionTable(ApplicationFrame.getInstance()
                .getTextComponent());
        //These actions come from the default editor kit.
        //Get the ones we want and stick them in the menu.
        menu.add(getActionByName(DefaultEditorKit.cutAction));
        menu.add(getActionByName(DefaultEditorKit.copyAction));
        menu.add(getActionByName(DefaultEditorKit.pasteAction));
        menu.addSeparator();
        menu.add(getActionByName(DefaultEditorKit.selectAllAction));
        menu.addSeparator();
        menu.add(Actions.FORMAT_SQL);
        menu.addSeparator();
        menu.add(Actions.HISTORY_PREVIOUS);
        menu.add(Actions.HISTORY_NEXT);

        menu = new JMenu("Schema Browser");
        menu = addMouseSensitivityToJMenu(menu);
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
        menu.add(Actions.SHOW_PROCEDURE);
        menu.add(Actions.SHOW_VIEW);
        menu.addSeparator();
        menu.add(Actions.ANALYZE_QUERY);
        menu.addSeparator();
        menu.add(Actions.EXPORT_EXCEL);
        menu.add(Actions.EXPORT_INSERTS);
        menu.add(Actions.EXPORT_FLAT_FILE);
        menu.add(Actions.EXPORT_PDF);
        menu.addSeparator();
        menu.add(Actions.LOB_COPY);
        menu.add(Actions.LOB_PASTE);
        menu.add(Actions.LOB_EXPORT);
        menu.add(Actions.LOB_IMPORT);
        menu.addSeparator();
        menu.add(Actions.RUN);
        menu.add(Actions.RUN_SCRIPT);

        menu = new JMenu(ApplicationFrame.LAF_MENU_LABEL);
        menu = addMouseSensitivityToJMenu(menu);
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
        menu = addMouseSensitivityToJMenu(menu);
        add(menu);
        JCheckBoxMenuItem loadingAssistantMenuItem = new JCheckBoxMenuItem(
                Actions.ANIMATED_LOADING);
        loadingAssistantMenuItem
                .setSelected(((AnimatedLoadingAction) Actions.ANIMATED_LOADING)
                        .isLoadingAssistantActived());
        menu.add(loadingAssistantMenuItem);
        menu.addSeparator();
        JCheckBoxMenuItem fullScreenMenuItem = new JCheckBoxMenuItem(
                Actions.FULL_SCREEN);
        menu.add(fullScreenMenuItem);

        menu = new JMenu("?");
        menu = addMouseSensitivityToJMenu(menu);
        add(menu);
        menu.add(Actions.HELP);
        menu.addSeparator();
        menu.add(Actions.ABOUT);

        setMnemonics(this);
    }
    
    private JMenu addMouseSensitivityToJMenu(JMenu menu) {
    	menu.addMouseListener(new MouseAdapter() {
        	public void forcesTheRepaint() {
        		if (((AnimatedLoadingAction) Actions.ANIMATED_LOADING)
                        .isLoadingAssistantActived()) {
                    ApplicationFrame.getInstance().setRepainted(false);
                }
        	}
        	@Override
			public void mouseEntered(MouseEvent e) {
        		forcesTheRepaint();
        	}
        	@Override
			public void mouseExited(MouseEvent e) {
        		forcesTheRepaint();
        	}
        	@Override
			public void mouseReleased(MouseEvent e) {
        		forcesTheRepaint();
			}
			@Override
			public void mousePressed(MouseEvent e) {
				forcesTheRepaint();
			}
		});
    	return menu;
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

    private Action getActionByName(String name) {
        return actions.get(name);
    }

    //The following two methods allow us to find an
    //action provided by the editor kit by its name.
    private HashMap<Object, Action> createActionTable(
            JTextComponent textComponent) {
        HashMap<Object, Action> actions = new HashMap<Object, Action>();
        Action[] actionsArray = textComponent.getActions();
        for (int i = 0; i < actionsArray.length; i++) {
            Action a = actionsArray[i];
            actions.put(a.getValue(Action.NAME), a);
        }
        return actions;
    }
}
