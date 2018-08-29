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

import java.awt.Component;
import java.awt.ComponentOrientation;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.UIManager;
import javax.swing.plaf.MenuItemUI;
import javax.swing.plaf.PopupMenuUI;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class JScrollMenu extends JMenu {

    private static final long serialVersionUID = 6905129453901406985L;

    // Covers the one in the JMenu because the method that creates it in
    // JMenu is private
    /** The popup menu portion of the menu. */
    private JPopupMenu popupMenu;

    /** Ctor. Constructs a new <code>JMenu</code> with no text. */
    public JScrollMenu() {
        this("");
    }

    /**
     * Ctor. Constructs a new <code>JMenu</code> with the supplied
     * string as its text.
     *
     * @param s The text for the menu label.
     */
    public JScrollMenu(String s) {
        super(s);
    }

    /**
     * Ctor. Constructs a menu whose properties are taken from the
     * <code>Action</code> supplied.
     *
     * @param a an <code>Action</code>
     */
    public JScrollMenu(Action a) {
        this();
        setAction(a);
    }

    /**
     * Lazily creates the popup menu. This method will create the
     * popup using the <code>JScrollPopupMenu</code> class. 
     */
    protected void ensurePopupMenuCreated() {
        if (popupMenu == null) {
            this.popupMenu = new JScrollPopupMenu();
            popupMenu.setInvoker(this);
            popupListener = createWinListener(popupMenu);
        }
    }

    //////////////////////////////
    //// All of these methods are necessary because ensurePopupMenuCreated() is private in JMenu
    //////////////////////////////
    @Override
    public void updateUI() {
        setUI((MenuItemUI) UIManager.getUI(this));

        if (popupMenu != null) {
            popupMenu.setUI((PopupMenuUI) UIManager.getUI(popupMenu));
        }
    }

    @Override
    public boolean isPopupMenuVisible() {
        ensurePopupMenuCreated();
        return popupMenu.isVisible();
    }

    @Override
    public void setMenuLocation(int x, int y) {
        super.setMenuLocation(x, y);
        if (popupMenu != null) {
            popupMenu.setLocation(x, y);
        }
    }

    @Override
    public JMenuItem add(JMenuItem menuItem) {
        ensurePopupMenuCreated();
        return popupMenu.add(menuItem);
    }

    @Override
    public Component add(Component c) {
        ensurePopupMenuCreated();
        popupMenu.add(c);
        return c;
    }

    @Override
    public Component add(Component c, int index) {
        ensurePopupMenuCreated();
        popupMenu.add(c, index);
        return c;
    }

    @Override
    public void addSeparator() {
        ensurePopupMenuCreated();
        popupMenu.addSeparator();
    }

    @Override
    public void insert(String s, int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }

        ensurePopupMenuCreated();
        popupMenu.insert(new JMenuItem(s), pos);
    }

    @Override
    public JMenuItem insert(JMenuItem mi, int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }
        ensurePopupMenuCreated();
        popupMenu.insert(mi, pos);
        return mi;
    }

    @Override
    public JMenuItem insert(Action a, int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }

        ensurePopupMenuCreated();
        JMenuItem mi = new JMenuItem(a);
        mi.setHorizontalTextPosition(JButton.TRAILING);
        mi.setVerticalTextPosition(JButton.CENTER);
        popupMenu.insert(mi, pos);
        return mi;
    }

    @Override
    public void insertSeparator(int index) {
        if (index < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }

        ensurePopupMenuCreated();
        popupMenu.insert(new JPopupMenu.Separator(), index);
    }

    @Override
    public void remove(JMenuItem item) {
        if (popupMenu != null) {
            popupMenu.remove(item);
        }
    }

    @Override
    public void remove(int pos) {
        if (pos < 0) {
            throw new IllegalArgumentException("index less than zero.");
        }
        if (pos > getItemCount()) {
            throw new IllegalArgumentException("index greater than the number of items.");
        }
        if (popupMenu != null) {
            popupMenu.remove(pos);
        }
    }

    @Override
    public void remove(Component c) {
        if (popupMenu != null) {
            popupMenu.remove(c);
        }
    }

    @Override
    public void removeAll() {
        if (popupMenu != null) {
            popupMenu.removeAll();
        }
    }

    @Override
    public int getMenuComponentCount() {
        return (popupMenu == null) ? 0 : popupMenu.getComponentCount();
    }

    @Override
    public Component getMenuComponent(int n) {
        return (popupMenu == null) ? null : popupMenu.getComponent(n);
    }

    @Override
    public Component[] getMenuComponents() {
        return (popupMenu == null) ? new Component[0] : popupMenu.getComponents();
    }

    @Override
    public JPopupMenu getPopupMenu() {
        ensurePopupMenuCreated();
        return popupMenu;
    }

    @Override
    public MenuElement[] getSubElements() {
        return popupMenu == null ? new MenuElement[0] : new MenuElement[] { popupMenu };
    }

    @Override
    public void applyComponentOrientation(ComponentOrientation o) {
        super.applyComponentOrientation(o);

        if (popupMenu != null) {
            int ncomponents = getMenuComponentCount();
            for (int i = 0; i < ncomponents; ++i) {
                getMenuComponent(i).applyComponentOrientation(o);
            }
            popupMenu.setComponentOrientation(o);
        }
    }

    @Override
    public void setComponentOrientation(ComponentOrientation o) {
        super.setComponentOrientation(o);
        if (popupMenu != null) {
            popupMenu.setComponentOrientation(o);
        }
    }
}