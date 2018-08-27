/*
 * Open Teradata Viewer ( editor language support java tree )
 * Copyright (C) 2015, D. Campione
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.tree;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.IconFactory;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.LocalVariable;
import net.sourceforge.open_teradata_viewer.util.Utilities;

/**
 * Tree node for a local variable.
 *
 * @author D. Campione
 * 
 */
class LocalVarTreeNode extends JavaTreeNode {

    private static final long serialVersionUID = -7858117222997078460L;

    private String text;

    public LocalVarTreeNode(LocalVariable var) {
        super(var);
        setIcon(IconFactory.get().getIcon(IconFactory.LOCAL_VARIABLE_ICON));
        setSortPriority(PRIORITY_LOCAL_VAR);

        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append(var.getName());
        sb.append(" : ");
        sb.append("<font color='#888888'>");
        MemberTreeNode.appendType(var.getType(), sb);
        text = sb.toString();
    }

    @Override
    public String getText(boolean selected) {
        // Strip out HTML tags
        return selected ? Utilities.stripHtml(text).replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">") : text;
    }
}