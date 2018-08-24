/*
 * Open Teradata Viewer ( editor autocomplete )
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

package net.sourceforge.open_teradata_viewer.editor.autocomplete;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JList;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * The cell renderer used for the SQL language.
 *
 * @author D. Campione
 * 
 */
public class SQLCellRenderer extends CompletionCellRenderer {

    private static final long serialVersionUID = 4733294644861327363L;

    private Icon variableIcon;
    private Icon functionIcon;
    private Icon emptyIcon;

    /** Ctor. */
    public SQLCellRenderer() {
        variableIcon = getIcon("/icons/var.png");
        functionIcon = getIcon("/icons/function.png");
        emptyIcon = new EmptyIcon(16);
    }

    /**
     * Returns an icon.
     *
     * @param resource The icon to retrieve. This should either be a file, or a
     *        resource loadable by the current ClassLoader.
     * @return The icon.
     */
    @Override
    protected Icon getIcon(String resource) {
        ClassLoader cl = getClass().getClassLoader();
        URL url = cl.getResource(resource);
        if (url == null) {
            File file = new File(resource);
            try {
                url = file.toURI().toURL();
            } catch (MalformedURLException mue) {
                ExceptionDialog.hideException(mue); // Never happens
            }
        }
        return url != null ? new ImageIcon(url) : null;
    }

    /** {@inheritDoc} */
    @Override
    protected void prepareForOtherCompletion(JList list, ICompletion c,
            int index, boolean selected, boolean hasFocus) {
        super.prepareForOtherCompletion(list, c, index, selected, hasFocus);
        setIcon(emptyIcon);
    }

    /** {@inheritDoc} */
    @Override
    protected void prepareForVariableCompletion(JList list,
            VariableCompletion vc, int index, boolean selected, boolean hasFocus) {
        super.prepareForVariableCompletion(list, vc, index, selected, hasFocus);
        setIcon(variableIcon);
    }

    /** {@inheritDoc} */
    @Override
    protected void prepareForFunctionCompletion(JList list,
            FunctionCompletion fc, int index, boolean selected, boolean hasFocus) {
        super.prepareForFunctionCompletion(list, fc, index, selected, hasFocus);
        setIcon(functionIcon);
    }
}