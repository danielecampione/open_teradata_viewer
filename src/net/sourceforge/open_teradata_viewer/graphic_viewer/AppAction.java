/*
 * Open Teradata Viewer ( graphic viewer )
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

package net.sourceforge.open_teradata_viewer.graphic_viewer;

import java.awt.Container;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public abstract class AppAction extends AbstractAction {

    private static final long serialVersionUID = -4788463998005168537L;

    private static Vector myAllActions = new Vector();

    public GraphicViewer getApp() {
        return (GraphicViewer) myApp;
    }

    public GraphicViewerView getView() {
        return getApp().getCurrentView();
    }

    public AppAction(String name, Container app) {
        super(name);
        init(app);
    }

    public AppAction(String name, Icon icon, Container app) {
        super(name, icon);
        init(app);
    }

    private final void init(Container app) {
        myApp = app;
        myAllActions.add(this);
    }

    public String toString() {
        return (String) getValue(NAME);
    }

    public boolean canAct() {
        return (getView() != null);
    }

    public void updateEnabled() {
        setEnabled(canAct());
    }

    public void free() {
        myAllActions.removeElement(this);
        myApp = null;
    }

    private Container myApp;

    // Keep track of all instances of AppAction

    public static void updateAllActions() {
        for (int i = 0; i < myAllActions.size(); i++) {
            AppAction act = (AppAction) myAllActions.elementAt(i);
            act.updateEnabled();
        }
    }

    public static Vector allActions() {
        return myAllActions;
    }
}