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

package net.sourceforge.open_teradata_viewer.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Config;
import net.sourceforge.open_teradata_viewer.Dialog;
import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;

import org.xml.sax.SAXException;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FavoritesAction extends CustomAction {

    private static final long serialVersionUID = -7717126935429649941L;

    protected FavoritesAction() {
        super(LanguageManager.getInstance().getString("menu.file.favorites"), "favorites.png", KeyStroke.getKeyStroke(
                KeyEvent.VK_F, KeyEvent.ALT_DOWN_MASK), null);
        setEnabled(true);
        
        // Add language change listener to update the action name when language changes
        LanguageManager.getInstance().addLanguageChangeListener((newLocale, newBundle) -> {
            putValue(NAME, newBundle.getString("menu.file.favorites"));
        });
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        favorites();
    }

    public void favorites() throws ParserConfigurationException, IOException,
    TransformerException, SAXException {
		Map<String, String> favorites = Config.getFavorites();
		
		final java.util.concurrent.atomic.AtomicReference<JList<?>> listRef = new java.util.concurrent.atomic.AtomicReference<>();
		final java.util.concurrent.atomic.AtomicReference<JScrollPane> scrollPaneRef = new java.util.concurrent.atomic.AtomicReference<>();
		
		try {
		    javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
		        @Override
		        public void run() {
		            final JList<?> tempList = new JList<Object>(favorites.keySet().toArray());
		            tempList.addMouseListener(FavoritesAction.this);
		            tempList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		            JScrollPane tempScrollPane = new JScrollPane(tempList);
		            listRef.set(tempList);
		            scrollPaneRef.set(tempScrollPane);
		        }
		    });
		} catch (Exception ex) {
		    throw new RuntimeException(ex);
		}
		
		final JList<?> list = listRef.get();
		final JScrollPane scrollPane = scrollPaneRef.get();
		
		LanguageManager langManager = LanguageManager.getInstance();
		Object value = Dialog.show(langManager.getString("dialog.favorites.title"), scrollPane,
		        Dialog.PLAIN_MESSAGE, new Object[]{"button.ok", "button.cancel", "button.add",
		                "button.delete"}, "button.ok");
		if (langManager.getString("button.ok").equals(value)) {
		    if (!list.isSelectionEmpty()) {
		        String name = (String) list.getSelectedValue();
		        String s = favorites.get(name);
		        ApplicationFrame.getInstance().setText(s);
		    }
		} else if (langManager.getString("button.delete").equals(value)) {
		    if (!list.isSelectionEmpty()) {
		        if (Dialog.YES_OPTION == Dialog.show(langManager.getString("dialog.delete_favorite.title"),
		                langManager.getString("message.are_you_sure"), Dialog.WARNING_MESSAGE,
		                Dialog.YES_NO_OPTION)) {
		            String name = (String) list.getSelectedValue();
		            favorites.remove(name);
		            Config.saveFavorites(favorites);
		        }
		    }
		    favorites();
		} else if (langManager.getString("button.add").equals(value)) {
		    final java.util.concurrent.atomic.AtomicReference<JComboBox<?>> comboBoxRef =
		            new java.util.concurrent.atomic.AtomicReference<>();
		    try {
		        javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
		            @Override
		            public void run() {
		                JComboBox<Object> cb = new JComboBox<Object>(favorites.keySet().toArray());
		                cb.setEditable(true);
		                cb.setSelectedIndex(-1);
		                comboBoxRef.set(cb);
		            }
		        });
		    } catch (Exception ex) {
		        throw new RuntimeException(ex);
		    }
		    JComboBox<?> comboBox = comboBoxRef.get();
		    if (Dialog.OK_OPTION == Dialog.show(langManager.getString("dialog.favorites.add.title"), comboBox,
		            Dialog.QUESTION_MESSAGE, Dialog.OK_CANCEL_OPTION)) {
		        String name = (String) comboBox.getSelectedItem();
		        if (name != null && !"".equals(name.trim())) {
		            favorites.put(name, ApplicationFrame.getInstance().getText());
		            Config.saveFavorites(favorites);
		        }
		    }
		    favorites();
		}
	}
}
