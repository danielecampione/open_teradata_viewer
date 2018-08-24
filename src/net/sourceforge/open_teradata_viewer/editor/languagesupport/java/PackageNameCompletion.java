/*
 * Open Teradata Viewer ( editor language support java )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java;

import java.awt.Graphics;

import javax.swing.Icon;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;

/**
 * A completion that represents a package name.
 *
 * @author D. Campione
 * 
 */
class PackageNameCompletion extends AbstractJavaSourceCompletion {

    public PackageNameCompletion(ICompletionProvider provider, String text,
            String alreadyEntered) {
        super(provider, text.substring(text.lastIndexOf('.') + 1));
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof PackageNameCompletion)
                && ((PackageNameCompletion) obj).getReplacementText().equals(
                        getReplacementText());
    }

    @Override
    public Icon getIcon() {
        return IconFactory.get().getIcon(IconFactory.PACKAGE_ICON);
    }

    @Override
    public int hashCode() {
        return getReplacementText().hashCode();
    }

    @Override
    public void rendererText(Graphics g, int x, int y, boolean selected) {
        g.drawString(getInputText(), x, y);
    }
}