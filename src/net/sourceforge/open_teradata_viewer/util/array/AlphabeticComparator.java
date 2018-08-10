/*
 * Open Teradata Viewer ( util array )
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

package net.sourceforge.open_teradata_viewer.util.array;

import java.io.Serializable;
import java.util.Comparator;

import net.sourceforge.open_teradata_viewer.util.Order;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class AlphabeticComparator implements Comparator<String>, Serializable {

    private static final long serialVersionUID = 2748109548353153689L;

    private boolean ignoreCase = true;
    private Order order = Order.ASCENDING;

    public AlphabeticComparator() {
        super();
    }

    public AlphabeticComparator(boolean ignoreCase) {
        super();
        this.ignoreCase = ignoreCase;
    }

    public AlphabeticComparator(boolean ignoreCase, Order order) {
        super();
        this.ignoreCase = ignoreCase;
        this.order = order;
    }

    public AlphabeticComparator(Order order) {
        super();
        this.order = order;
    }

    public int compare(String o1, String o2) {
        if (order == Order.DESCENDING) {
            if (ignoreCase) {
                return o2.compareToIgnoreCase(o1);
            } else {
                return o2.compareTo(o1);
            }
        } else {
            if (ignoreCase) {
                return o1.compareToIgnoreCase(o2);
            } else {
                return o1.compareTo(o2);
            }
        }
    }

}
