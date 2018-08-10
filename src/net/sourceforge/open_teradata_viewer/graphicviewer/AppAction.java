/*
 * Open Teradata Viewer ( graphic viewer )
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

package net.sourceforge.open_teradata_viewer.graphicviewer;

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

    public AppAction(String s, Container container) {
        super(s);
        init(container);
    }

    public AppAction(String s, Icon icon, Container container) {
        super(s, icon);
        init(container);
    }

    public GraphicViewer getApp() {
        return (GraphicViewer) myApp;
    }

    public OpenTeradataViewerView getView() {
        return getApp().getCurrentView();
    }

    @SuppressWarnings("unchecked")
    private final void init(Container container) {
        myApp = container;
        myAllActions.add(this);
    }

    public String toString() {
        return (String) getValue("Name");
    }

    public boolean canAct() {
        return getView() != null;
    }

    public void updateEnabled() {
        setEnabled(canAct());
    }

    public void free() {
        myAllActions.removeElement(this);
        myApp = null;
    }

    public static void updateAllActions() {
        for (int i = 0; i < myAllActions.size(); i++) {
            AppAction appaction = (AppAction) myAllActions.elementAt(i);
            appaction.updateEnabled();
        }

    }

    @SuppressWarnings("rawtypes")
    public static Vector allActions() {
        return myAllActions;
    }

    private Container myApp;
    @SuppressWarnings("rawtypes")
    private static Vector myAllActions = new Vector();

}