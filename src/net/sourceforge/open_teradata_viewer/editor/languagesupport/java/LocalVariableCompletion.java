/*
 * Open Teradata Viewer ( editor language support java )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java;

import java.awt.Graphics;

import javax.swing.Icon;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast.LocalVariable;

/**
 * 
 * 
 * @author D. Campione
 *
 */
class LocalVariableCompletion extends AbstractJavaSourceCompletion {

    private LocalVariable localVar;

    /**
     * The relevance of local variables. This allows local variables to be
     * "higher" in the completion list than other types.
     */
    private static final int RELEVANCE = 4;

    public LocalVariableCompletion(ICompletionProvider provider,
            LocalVariable localVar) {
        super(provider, localVar.getName());
        this.localVar = localVar;
        setRelevance(RELEVANCE);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof LocalVariableCompletion)
                && ((LocalVariableCompletion) obj).getReplacementText().equals(
                        getReplacementText());
    }

    @Override
    public Icon getIcon() {
        return IconFactory.get().getIcon(IconFactory.LOCAL_VARIABLE_ICON);
    }

    @Override
    public String getToolTipText() {
        return localVar.getType() + " " + localVar.getName();
    }

    @Override
    public int hashCode() {
        return getReplacementText().hashCode(); // Match equals()
    }

    @Override
    public void rendererText(Graphics g, int x, int y, boolean selected) {
        StringBuilder sb = new StringBuilder();
        sb.append(localVar.getName());
        sb.append(" : ");
        sb.append(localVar.getType());
        g.drawString(sb.toString(), x, y);
    }
}