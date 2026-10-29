/*
 * Open Teradata Viewer ( test editor )
 * Copyright (C), D. Campione
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

package test.net.sourceforge.open_teradata_viewer.editor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.junit.jupiter.api.Test;

import net.sourceforge.open_teradata_viewer.editor.FileSyntaxStyle;

/**
 *
 *
 * @author D. Campione
 *
 */
public class TestFileSyntaxStyle {

    private static String styleOf(String fileName) {
        return FileSyntaxStyle.forFile(new File(fileName));
    }

    @Test
    public void test_forFile_defaultStyleIsSql() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_SQL, FileSyntaxStyle.DEFAULT_STYLE);
    }

    @Test
    public void test_forFile_sql() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_SQL, styleOf("query.sql"));
    }

    @Test
    public void test_forFile_unknownExtensionKeepsSql() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_SQL, styleOf("script.bteq"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_SQL, styleOf("create_tables.ddl"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_SQL, styleOf("notes.txt"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_SQL, styleOf("output.log"));
    }

    @Test
    public void test_forFile_noExtensionKeepsSql() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_SQL, styleOf("script"));
    }

    @Test
    public void test_forFile_nullFileKeepsSql() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_SQL, FileSyntaxStyle.forFile(null));
    }

    @Test
    public void test_forFile_onlyTheNameOfTheFileCounts() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_SQL, styleOf("project.java" + File.separator + "script"));
    }

    @Test
    public void test_forFile_java() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_JAVA, styleOf("Main.java"));
    }

    @Test
    public void test_forFile_javaScript() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT, styleOf("app.js"));
    }

    @Test
    public void test_forFile_xml() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_XML, styleOf("pom.xml"));
    }

    @Test
    public void test_forFile_html() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_HTML, styleOf("index.html"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_HTML, styleOf("index.htm"));
    }

    @Test
    public void test_forFile_commonTypes() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_JSON, styleOf("data.json"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_PYTHON, styleOf("tool.py"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL, styleOf("run.sh"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_WINDOWS_BATCH, styleOf("run.bat"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_PROPERTIES_FILE, styleOf("messages.properties"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_YAML, styleOf("config.yml"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_MARKDOWN, styleOf("README.md"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_CSS, styleOf("style.css"));
    }

    @Test
    public void test_forFile_extensionCaseIsIgnored() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_JAVA, styleOf("MAIN.JAVA"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_SQL, styleOf("QUERY.SQL"));
    }

    @Test
    public void test_forFile_backupExtensionIsIgnored() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_JAVA, styleOf("Main.java.bak"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_XML, styleOf("pom.xml.old"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_PYTHON, styleOf("tool.py.orig"));
    }

    @Test
    public void test_forFile_powerShell() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_POWERSHELL, styleOf("deploy.ps1"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_POWERSHELL, styleOf("tools.psm1"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_POWERSHELL, styleOf("tools.psd1"));
    }

    @Test
    public void test_forFile_vhdl() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_VHDL, styleOf("counter.vhd"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_VHDL, styleOf("counter.vhdl"));
    }

    @Test
    public void test_forFile_fileNamesWithoutExtension() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_MAKEFILE, styleOf("Makefile"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_DOCKERFILE, styleOf("Dockerfile"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_RUBY, styleOf("Vagrantfile"));
    }
}
