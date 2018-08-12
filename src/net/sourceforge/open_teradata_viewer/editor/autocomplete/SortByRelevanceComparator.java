/*
 * Open Teradata Viewer ( editor autocomplete )
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

package net.sourceforge.open_teradata_viewer.editor.autocomplete;

import java.util.Comparator;

/**
 * Compares two <code>ICompletion</code>s by their relevance before sorting them
 * lexicographically.
 *
 * @author D. Campione
 * 
 */
public class SortByRelevanceComparator implements Comparator {

    public int compare(Object o1, Object o2) {
        ICompletion c1 = (ICompletion) o1;
        ICompletion c2 = (ICompletion) o2;
        int rel1 = c1.getRelevance();
        int rel2 = c2.getRelevance();
        int diff = rel2 - rel1;//rel1 - rel2;
        return diff == 0 ? c1.compareTo(c2) : diff;
    }

}