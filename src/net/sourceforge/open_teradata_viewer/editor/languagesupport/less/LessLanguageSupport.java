/*
 * Open Teradata Viewer ( editor language support less )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.less;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.css.CssCompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.css.CssLanguageSupport;

/**
 * Language support for Less.
 *
 * @author D. Campione
 *
 */
public class LessLanguageSupport extends CssLanguageSupport {

    /** Ctor. */
    public LessLanguageSupport() {
        setShowDescWindow(true);
    }

    /**
     * Overridden to return a completion provider that understands Less.
     *
     * @return A completion provider to use for this language.
     */
    @Override
    protected CssCompletionProvider createProvider() {
        return new LessCompletionProvider();
    }
}