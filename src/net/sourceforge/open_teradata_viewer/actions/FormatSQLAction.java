/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2014, D. Campione
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

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ThreadedAction;

import org.hibernate.jdbc.util.FormatStyle;
import org.hibernate.jdbc.util.IFormatter;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FormatSQLAction extends CustomAction {

    private static final long serialVersionUID = 860629017867347294L;

    protected FormatSQLAction() {
        super(
                "Format SQL code",
                "format.png",
                KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.SHIFT_DOWN_MASK
                        + Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()),
                null);
        setEnabled(true);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // The "formatting" process can be performed altough other processes are
        // running
        new ThreadedAction() {
            @Override
            protected void execute() throws Exception {
                performThreaded(e);
            }
        };
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        IFormatter iFormatter = FormatStyle.BASIC.getFormatter();
        String original = ApplicationFrame.getInstance().getText();
        String formatted = iFormatter.format(original);
        ApplicationFrame.getInstance().setText(formatted);
    }
}