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

import java.util.ArrayList;
import java.util.Collections;

import net.sourceforge.open_teradata_viewer.util.Order;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class StringList extends ArrayList<String> {

    private static final long serialVersionUID = -3619994613808880202L;

    private boolean ignoreCase = true;

    public StringList(boolean ignoreCase, int initialCapacity) {
        super(initialCapacity);
        setIgnoreCase(ignoreCase);
    }

    public StringList(boolean ignoreCase) {
        super();
        setIgnoreCase(ignoreCase);
    }

    public StringList() {
        super();
    }

    public Object clone() {
        Object result = super.clone();
        ((StringList) result).ignoreCase = this.ignoreCase;
        return result;
    }

    public int indexOf(Object elem) {
        if (elem == null) {
            for (int i = 0; i < size(); i++)
                if (get(i) == null)
                    return i;
        } else {
            for (int i = 0; i < size(); i++) {
                if (ignoreCase) {
                    if (get(i).equalsIgnoreCase((String) elem)) {
                        return i;
                    }
                } else {
                    if (get(i).equals(elem)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public boolean exists(String elem) {
        return indexOf(elem) != -1;
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    public String getText() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size(); i++) {
            sb.append(get(i));
            if (i != size() - 1) {
                sb.append('\n');
            }
        }
        return sb.toString();
    }

    public void setText(String text) {
        StringBuilder sb = new StringBuilder();

        clear();
        if (text == null) {
            return;
        }

        int i = 0;
        while (i < text.length()) {
            char ch = text.charAt(i);
            if (ch == '\n') {
                add(sb.toString());
                sb.setLength(0);
            } else if (ch != '\r') {
                sb.append(ch);
            }
            i++;
        }
        add(sb.toString());
    }

    public void sort(Order order) {
        Collections.sort(this, new AlphabeticComparator(ignoreCase, order));
    }

    public void sort() {
        sort(Order.ASCENDING);
    }

}
