/*
 * Open Teradata Viewer ( editor syntax folding )
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

package net.sourceforge.open_teradata_viewer.editor.syntax.folding;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.open_teradata_viewer.editor.syntax.ISyntaxConstants;

/**
 * Manages fold parsers. Instances of <code>SyntaxTextArea</code> call into this
 * class to retrieve fold parsers for whatever language they're editing. Folks
 * implementing custom languages can add a {@link IFoldParser} implementation for
 * their language to this manager and it will be used by STA.
 *
 * @author D. Campione
 * 
 */
public class FoldParserManager implements ISyntaxConstants {

    /** Map from syntax styles to fold parsers. */
    private Map<String, IFoldParser> foldParserMap;

    private static final FoldParserManager INSTANCE = new FoldParserManager();

    /** Private constructor to prevent instantiation. */
    private FoldParserManager() {
        foldParserMap = createFoldParserMap();
    }

    /**
     * Adds a mapping from a syntax style to a fold parser. The parser specified
     * will be shared among all STA instances editing that language, so it
     * should be stateless (which should not be difficult for a fold parser).
     * You can also override the fold parser for built-in languages, with your
     * own parser implementations.
     *
     * @param syntaxStyle The syntax style.
     * @param parser The parser.
     * @see ISyntaxConstants
     */
    public void addFoldParserMapping(String syntaxStyle, IFoldParser parser) {
        foldParserMap.put(syntaxStyle, parser);
    }

    /**
     * Creates the syntax style-to-fold parser mapping for built-in languages.
     * 
     * @return The style-to-fold parser mapping.
     */
    private Map<String, IFoldParser> createFoldParserMap() {
        Map<String, IFoldParser> map = new HashMap<String, IFoldParser>();

        map.put(SYNTAX_STYLE_SQL, new CurlyFoldParser(true, false));

        return map;
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return The singleton instance.
     */
    public static FoldParserManager get() {
        return INSTANCE;
    }

    /**
     * Returns a fold parser to use for an editor highlighting code of a
     * specific language.
     *
     * @param syntaxStyle A value from {@link ISyntaxConstants}
     * @return A fold parser to use, or <code>null</code> if none is registered
     *         for the language.
     */
    public IFoldParser getFoldParser(String syntaxStyle) {
        return foldParserMap.get(syntaxStyle);
    }
}