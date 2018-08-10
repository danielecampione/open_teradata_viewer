/*
 * Open Teradata Viewer ( editor syntax )
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

package net.sourceforge.open_teradata_viewer.editor.syntax;

import java.util.HashMap;
import java.util.Map;

/**
 * The default implementation of <code>TokenMakerFactory</code>. This factory
 * can create {@link ITokenMaker}s for all languages known to {@link
 * SyntaxTextArea}.
 *
 * @author D. Campione
 * 
 */
class DefaultTokenMakerFactory extends AbstractTokenMakerFactory
        implements
            ISyntaxConstants {

    /**
     * Creates and returns a mapping from keys to the names of {@link
     * ITokenMaker} implementation classes. When {@link #getTokenMaker(String)}
     * is called with a key defined in this map, a <code>ITokenMaker</code> of
     * the corresponding type is returned.
     *
     * @return The map.
     */
    protected Map<String, ?> createTokenMakerKeyToClassNameMap() {

        HashMap map = new HashMap();

        String pkg = "net.sourceforge.open_teradata_viewer.editor.syntax.modes.";

        map.put(SYNTAX_STYLE_NONE, pkg + "PlainTextTokenMaker");
        map.put(SYNTAX_STYLE_SQL, pkg + "SQLTokenMaker");

        return map;
    }
}