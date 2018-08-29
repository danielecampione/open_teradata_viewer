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

/**
 * One of the partecipants belonging to the factory method that has been adopted
 * to implement the initialization of the columns name discoverer statement; the
 * class represents the ConcreteProduct of the specified design pattern.<p/>
 * 
 * It implements the concrete columns name discoverer appropriate to the
 * Teradata syntax.
 * 
 * @author D. Campione
 *
 */
public class TeradataColumnsNameDiscoverer implements IColumnsNameDiscovererElement {

    private String relationName;

    @Override
    public void setSQLQuery(String relationName) {
        this.relationName = relationName;
    }

    @Override
    public String getSQLQuery() {
        return "HELP TABLE " + relationName;
    }
}