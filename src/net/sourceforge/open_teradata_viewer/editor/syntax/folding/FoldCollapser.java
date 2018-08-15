/*
 * Open Teradata Viewer ( editor syntax folding )
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

package net.sourceforge.open_teradata_viewer.editor.syntax.folding;

import java.util.ArrayList;
import java.util.List;

/**
 * Collapses folds based on their type.  You can create an instance of this
 * class to collapse all comment blocks when opening a new file, for example.
 *
 * @author D. Campione
 * 
 */
public class FoldCollapser {

    private List<Integer> typesToCollapse;

    /** Creates an instance that collapses all comment blocks. */
    public FoldCollapser() {
        this(IFoldType.COMMENT);
    }

    /**
     * Creates an instance that collapses all blocks of the specified type.
     *
     * @param typeToCollapse The type to collapse.
     * @see IFoldType
     */
    public FoldCollapser(int typeToCollapse) {
        typesToCollapse = new ArrayList<Integer>(3);
        addTypeToCollapse(typeToCollapse);
    }

    /**
     * Adds a type of fold to collapse.
     *
     * @param typeToCollapse The type of fold to collapse.
     */
    public void addTypeToCollapse(int typeToCollapse) {
        typesToCollapse.add(Integer.valueOf(typeToCollapse));
    }

    /**
     * Collapses any relevant folds known by the fold manager.
     *
     * @param fm The fold manager.
     */
    public void collapseFolds(FoldManager fm) {
        for (int i = 0; i < fm.getFoldCount(); i++) {
            Fold fold = fm.getFold(i);
            collapseImpl(fold);
        }
    }

    /**
     * Collapses the specified fold, and any of its child folds, as appropriate.
     *
     * @param fold The fold to examine.
     * @see #getShouldCollapse(Fold)
     */
    protected void collapseImpl(Fold fold) {
        if (getShouldCollapse(fold)) {
            fold.setCollapsed(true);
        }
        for (int i = 0; i < fold.getChildCount(); i++) {
            collapseImpl(fold.getChild(i));
        }
    }

    /**
     * Returns whether a specific fold should be collapsed.
     *
     * @param fold The fold to examine.
     * @return Whether the fold should be collapsed.
     */
    public boolean getShouldCollapse(Fold fold) {
        int type = fold.getFoldType();
        for (Integer typeToCollapse : typesToCollapse) {
            if (type == typeToCollapse.intValue()) {
                return true;
            }
        }
        return false;
    }
}