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

import java.awt.event.ActionEvent;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import net.sourceforge.open_teradata_viewer.Context;
import net.sourceforge.open_teradata_viewer.Dialog;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class FetchLimitAction extends CustomAction {

    private static final long serialVersionUID = -4703116641594116984L;

    protected FetchLimitAction() {
        super(String
                .format("Fetch Limit = %s",
                        Context.getInstance().getFetchLimit() == 0
                                ? "Unlimited"
                                : String.valueOf(Context.getInstance()
                                        .getFetchLimit())), "fetchlimit.png",
                null, null);
        setEnabled(true);
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(Context
                .getInstance().getFetchLimit(), 0, 999999, 1));
        if (Dialog.OK_OPTION == Dialog.show("Fetch Limit", spinner,
                Dialog.QUESTION_MESSAGE, Dialog.OK_CANCEL_OPTION)) {
            Context.getInstance().setFetchLimit(
                    ((Number) spinner.getValue()).intValue());
        }
        putValue(NAME,
                String.format(
                        "Fetch Limit = %s",
                        Context.getInstance().getFetchLimit() == 0
                                ? "Unlimited"
                                : String.valueOf(Context.getInstance()
                                        .getFetchLimit())));
    }
}
