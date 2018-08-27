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
 * Constants that define the different programming languages understood by
 * <code>SyntaxTextArea</code>. These constants are the values you can pass to
 * {@link SyntaxTextArea#setSyntaxEditingStyle(String)} to get syntax
 * highlighting.<p>
 *
 * By default, all <code>SyntaxTextArea</code>s can render all of these
 * languages, but this can be changed (the list can be augmented or completely
 * overwritten) on a per-text area basis. What languages can be rendered is
 * actually managed by the {@link TokenMakerFactory} installed on the text
 * area's {@link SyntaxDocument}. By default, all <code>SyntaxDocument</code>s
 * have a factory installed capable of handling all of these languages.
 *
 * @author D. Campione
 *
 */
public interface ISyntaxConstants {

    /** Style meaning don't syntax highlight anything. */
    public static final String SYNTAX_STYLE_NONE = "text/plain";

    /** Style for highlighting x86 assembly. */
    public static final String SYNTAX_STYLE_ASSEMBLY_X86 = "text/asm";

    /** Style for highlighting C. */
    public static final String SYNTAX_STYLE_C = "text/c";

    /** Style for highlighting Clojure. */
    public static final String SYNTAX_STYLE_CLOJURE = "text/clojure";

    /** Style for highlighting C++. */
    public static final String SYNTAX_STYLE_CPLUSPLUS = "text/cpp";

    /** Style for highlighting C#. */
    public static final String SYNTAX_STYLE_CSHARP = "text/cs";

    /** Style for highlighting CSS. */
    public static final String SYNTAX_STYLE_CSS = "text/css";

    /** Syntax style for highlighting D. */
    public static final String SYNTAX_STYLE_D = "text/d";

    /** Style for highlighting Dart. */
    public static final String SYNTAX_STYLE_DART = "text/dart";

    /** Style for highlighting Groovy. */
    public static final String SYNTAX_STYLE_GROOVY = "text/groovy";

    /** Style for highlighting HTML. */
    public static final String SYNTAX_STYLE_HTML = "text/html";

    /** Style for highlighting Java. */
    public static final String SYNTAX_STYLE_JAVA = "text/java";

    /** Style for highlighting JavaScript. */
    public static final String SYNTAX_STYLE_JAVASCRIPT = "text/javascript";

    /** Style for highlighting JSON. */
    public static final String SYNTAX_STYLE_JSON = "text/json";

    /** Style for highlighting JSP. */
    public static final String SYNTAX_STYLE_JSP = "text/jsp";

    /** Style for highlighting Lisp. */
    public static final String SYNTAX_STYLE_LISP = "text/lisp";

    /** Style for highlighting NSIS install scripts. */
    public static final String SYNTAX_STYLE_NSIS = "text/nsis";

    /** Style for highlighting Perl. */
    public static final String SYNTAX_STYLE_PERL = "text/perl";

    /** Style for highlighting PHP. */
    public static final String SYNTAX_STYLE_PHP = "text/php";

    /** Style for highlighting Python. */
    public static final String SYNTAX_STYLE_PYTHON = "text/python";

    /** Style for highlighting SQL. */
    public static final String SYNTAX_STYLE_SQL = "text/sql";

    /** Style for highlighting UNIX shell keywords. */
    public static final String SYNTAX_STYLE_UNIX_SHELL = "text/unix";

    /** Style for highlighting Visual Basic. */
    public static final String SYNTAX_STYLE_VISUAL_BASIC = "text/vb";

    /** Style for highlighting Windows batch files. */
    public static final String SYNTAX_STYLE_WINDOWS_BATCH = "text/bat";

    /** Style for highlighting XML. */
    public static final String SYNTAX_STYLE_XML = "text/xml";
}