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

import javax.swing.text.JTextComponent;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.BasicCompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletion;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;

/**
 * Base class for Java source completions.
 *
 * @author D. Campione
 * 
 */
public abstract class AbstractJavaSourceCompletion extends BasicCompletion
        implements IJavaSourceCompletion {

    public AbstractJavaSourceCompletion(ICompletionProvider provider,
            String replacementText) {
        super(provider, replacementText);
    }

    /**
     * Overridden to ensure that two completions don't just have the same text
     * value (ignoring case) but that they're of the same "type" of
     * <code>ICompletion</code> as well, so, for example, a completion for the
     * "String" class won't clash with a completion for a "string" LocalVar.
     *
     * @param c2 Another completion instance.
     * @return How this completion compares to the other one.
     */
    @Override
    public int compareTo(ICompletion c2) {
        int rc = -1;

        if (c2 == this) {
            rc = 0;
        } else if (c2 != null) {
            rc = toString().compareToIgnoreCase(c2.toString());
            if (rc == 0) { // Same text value
                String clazz1 = getClass().getName();
                clazz1 = clazz1.substring(clazz1.lastIndexOf('.'));
                String clazz2 = c2.getClass().getName();
                clazz2 = clazz2.substring(clazz2.lastIndexOf('.'));
                rc = clazz1.compareTo(clazz2);
            }
        }

        return rc;
    }

    @Override
    public String getAlreadyEntered(JTextComponent comp) {
        String temp = getProvider().getAlreadyEnteredText(comp);
        int lastDot = temp.lastIndexOf('.');
        if (lastDot > -1) {
            temp = temp.substring(lastDot + 1);
        }
        return temp;
    }
}