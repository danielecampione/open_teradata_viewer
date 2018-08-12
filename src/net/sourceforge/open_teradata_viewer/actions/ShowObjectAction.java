/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer.actions;

import net.sourceforge.open_teradata_viewer.IShowObjectValidationStrategy;

/**
 *
 * 
 * @author D. Campione
 * 
 */
public abstract class ShowObjectAction extends CustomAction {

    private static final long serialVersionUID = -457375182807555390L;

    /** Ctor. */
    protected ShowObjectAction(String name) {
        super(name);
    }

    public String getSQLQueryToShowObject(
            IShowObjectValidationStrategy showObjectValidationStrategy,
            String objectName) {
        return showObjectValidationStrategy.getSQLQueryToShowObject(objectName);
    }
}