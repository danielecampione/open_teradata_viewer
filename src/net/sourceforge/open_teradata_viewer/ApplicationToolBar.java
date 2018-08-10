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

import java.awt.Insets;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import net.sourceforge.open_teradata_viewer.actions.Actions;
import net.sourceforge.open_teradata_viewer.actions.GroupAction;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ApplicationToolBar extends JToolBar {

    private static final long serialVersionUID = 4087667816607539762L;

    public ApplicationToolBar(JToggleButton schemaBrowserToggleButton) {
        setFloatable(false);
        add(Actions.RUN);
        add(Actions.RUN_SCRIPT);
        add(Actions.COMMIT);
        add(Actions.ROLLBACK);
        addSeparator();
        if (Actions.CUT_COPY_PASTE_ENABLED) {
            add(Actions.CUT);
            add(Actions.COPY);
            add(Actions.PASTE);
            addSeparator();
        }
        add(Actions.FILE_OPEN);
        add(Actions.FILE_SAVE);
        add(Actions.FAVORITES);
        addSeparator();
        add(Actions.HISTORY_PREVIOUS);
        add(Actions.HISTORY_NEXT);
        addSeparator();
        add(Actions.LOB_GROUP);
        addSeparator();
        add(Actions.EXPORT_GROUP);
        add(Box.createHorizontalGlue());
        schemaBrowserToggleButton.setAction(Actions.SCHEMA_BROWSER);
        schemaBrowserToggleButton.setText(null);
        add(setup(schemaBrowserToggleButton));
    }

    @Override
    public JButton add(Action action) {
        return (JButton) setup(super.add(action));
    }

    private AbstractButton setup(AbstractButton button) {
        button.setMargin(new Insets(1, 1, 1, 1));
        button.setFocusable(false);
        String toolTipText = (String) button.getAction().getValue(Action.NAME);
        KeyStroke accelerator = (KeyStroke) button.getAction().getValue(
                AbstractAction.ACCELERATOR_KEY);
        if (accelerator != null) {
            toolTipText += String.format(" (%s+%s)",
                    KeyEvent.getModifiersExText(accelerator.getModifiers()),
                    KeyEvent.getKeyText(accelerator.getKeyCode()));
        }
        button.setToolTipText(toolTipText);
        return button;
    }

    public JToggleButton add(GroupAction action) {
        JToggleButton toggleButton = (JToggleButton) add(new JToggleButton(
                action));
        toggleButton.setMargin(new Insets(1, 1, 1, 1));
        toggleButton.setHorizontalTextPosition(JButton.LEFT);
        toggleButton.setVerticalTextPosition(JButton.BOTTOM);
        toggleButton.setFocusable(false);
        return toggleButton;
    }
}
