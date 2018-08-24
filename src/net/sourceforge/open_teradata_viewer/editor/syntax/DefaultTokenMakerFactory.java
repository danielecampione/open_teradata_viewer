/*
 * Open Teradata Viewer ( editor syntax )
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

package net.sourceforge.open_teradata_viewer.editor.syntax;

/**
 * The default implementation of <code>TokenMakerFactory</code>. This factory
 * can create {@link ITokenMaker}s for all languages known to {@link
 * SyntaxTextArea}.
 *
 * @author D. Campione
 * 
 */
class DefaultTokenMakerFactory extends AbstractTokenMakerFactory implements
        ISyntaxConstants {

    /** {@inheritDoc} */
    @Override
    protected void initTokenMakerMap() {
        String pkg = "net.sourceforge.open_teradata_viewer.editor.syntax.modes.";

        putMapping(SYNTAX_STYLE_NONE, pkg + "PlainTextTokenMaker");
        putMapping(SYNTAX_STYLE_C, pkg + "CTokenMaker");
        putMapping(SYNTAX_STYLE_CLOJURE, pkg + "ClojureTokenMaker");
        putMapping(SYNTAX_STYLE_CSS, pkg + "CSSTokenMaker");
        putMapping(SYNTAX_STYLE_HTML, pkg + "HTMLTokenMaker");
        putMapping(SYNTAX_STYLE_JAVA, pkg + "JavaTokenMaker");
        putMapping(SYNTAX_STYLE_JAVASCRIPT, pkg + "JavaScriptTokenMaker");
        putMapping(SYNTAX_STYLE_JSP, pkg + "JSPTokenMaker");
        putMapping(SYNTAX_STYLE_PHP, pkg + "PHPTokenMaker");
        putMapping(SYNTAX_STYLE_SQL, pkg + "SQLTokenMaker");
        putMapping(SYNTAX_STYLE_XML, pkg + "XMLTokenMaker");
    }

}