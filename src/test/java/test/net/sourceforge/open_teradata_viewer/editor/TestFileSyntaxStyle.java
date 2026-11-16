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
import static org.junit.jupiter.api.Assertions.assertNull;

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
    public void test_forFile_sql() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_SQL, styleOf("query.sql"));
    }

    @Test
    public void test_forFile_unknownExtensionIsNotHighlighted() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, styleOf("script.bteq"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, styleOf("create_tables.ddl"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, styleOf("notes.txt"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, styleOf("output.log"));
    }

    @Test
    public void test_forFile_noExtensionIsNotHighlighted() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, styleOf("script"));
    }

    @Test
    public void test_forFile_nullFileIsNotHighlighted() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, FileSyntaxStyle.forFile(null));
    }

    @Test
    public void test_forFile_onlyTheNameOfTheFileCounts() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, styleOf("project.java" + File.separator + "script"));
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

    private static String savedAs(String previous, String file) {
        return FileSyntaxStyle.forSavedAs(previous == null ? null : new File(previous), new File(file));
    }

    @Test
    public void test_forSavedAs_documentWithoutFileGetsTheStyleOfTheName() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_JAVA, savedAs(null, "Main.java"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_PYTHON, savedAs(null, "tool.py"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_SQL, savedAs(null, "query.sql"));
    }

    @Test
    public void test_forSavedAs_documentWithoutFileAndUnknownNameIsNotHighlighted() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, savedAs(null, "notes.txt"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, savedAs(null, "script"));
    }

    @Test
    public void test_forSavedAs_anotherKindOfFileChangesTheStyle() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_JAVA, savedAs("query.sql", "Main.java"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_SQL, savedAs("Main.java", "query.sql"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_XML, savedAs("data.json", "data.xml"));
    }

    @Test
    public void test_forSavedAs_fromAKnownKindToAnUnknownOneIsNotHighlighted() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, savedAs("Main.java", "notes.txt"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, savedAs("query.sql", "notes.txt"));
    }

    @Test
    public void test_forSavedAs_fromAnUnknownKindToAKnownOneChangesTheStyle() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_SQL, savedAs("notes.txt", "query.sql"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_JAVA, savedAs("script", "Main.java"));
    }

    @Test
    public void test_forSavedAs_theSameNameKeepsTheStyle() {
        assertNull(savedAs("Main.java", "Main.java"));
        assertNull(savedAs("notes.txt", "notes.txt"));
    }

    @Test
    public void test_forSavedAs_anotherFileOfTheSameKindKeepsTheStyle() {
        assertNull(savedAs("Main.java", "Other.java"));
        assertNull(savedAs("a" + File.separator + "tool.py", "b" + File.separator + "tool2.py"));
    }

    @Test
    public void test_forSavedAs_unknownNamesTellTheSameKind() {
        assertNull(savedAs("notes.txt", "output.log"));
        assertNull(savedAs("script", "notes.txt"));
    }

    @Test
    public void test_forSavedAs_caseAndBackupExtensionsDoNotChangeTheKind() {
        assertNull(savedAs("MAIN.JAVA", "Main.java"));
        assertNull(savedAs("Main.java", "Main.java.bak"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_XML, savedAs("Main.java", "pom.xml.old"));
    }

    private static String contentStyle(String text) {
        return FileSyntaxStyle.forContent(text);
    }

    @Test
    public void test_forContent_shebang() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL, contentStyle("#!/bin/bash\necho hi\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL, contentStyle("#!/bin/sh\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL, contentStyle("#!/usr/bin/env bash\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL, contentStyle("#!/bin/bash -e\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_PERL, contentStyle("#!/usr/bin/perl -w\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_PHP, contentStyle("#!/usr/bin/env php\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_PYTHON, contentStyle("#!/usr/bin/env python\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_RUBY, contentStyle("#!/usr/bin/ruby\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_LUA, contentStyle("#!/usr/bin/env lua\n"));
    }

    @Test
    public void test_forContent_shebangWithTheVersionOfTheInterpreter() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_PYTHON, contentStyle("#!/usr/bin/env python3\nprint(1)\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_PYTHON, contentStyle("#!/usr/bin/python3\nprint(1)\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_PYTHON, contentStyle("#!/usr/bin/python3.11 -u\nprint(1)\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_PYTHON, contentStyle("#!/usr/bin/env python3.11\nprint(1)\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_PHP, contentStyle("#!/usr/bin/env php8.1\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_PERL, contentStyle("#!/usr/bin/perl5.34.0\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_LUA, contentStyle("#!/usr/bin/env lua5.4\n"));
    }

    @Test
    public void test_forContent_shebangOfAnUnknownInterpreter() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, contentStyle("#!/usr/bin/env node\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, contentStyle("#!/usr/bin/awk -f\n"));
    }

    @Test
    public void test_forContent_xmlDeclaration() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_XML, contentStyle("<?xml version=\"1.0\"?>\n<a/>\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_XML,
                contentStyle("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<a/>\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_XML, contentStyle("<?xml version=\"1.0\"?>"));
    }

    @Test
    public void test_forContent_htmlDocumentType() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_HTML, contentStyle("<!doctype html>\n<html></html>\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_HTML, contentStyle("<!DOCTYPE html>\n<html></html>\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_HTML,
                contentStyle("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\">\n<html></html>\n"));
    }

    @Test
    public void test_forContent_lineBreakOfWindowsAndOfOldMacs() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL, contentStyle("#!/bin/bash\r\necho hi\r\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL, contentStyle("#!/bin/bash\recho hi\r"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_XML, contentStyle("<?xml version=\"1.0\"?>\r\n<a/>\r\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_HTML, contentStyle("<!DOCTYPE html>\r\n<html></html>\r\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_PYTHON, contentStyle("#!/usr/bin/env python3\r\nprint(1)\r\n"));
    }

    @Test
    public void test_forContent_spacesAtTheEndOfTheFirstLine() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_XML, contentStyle("<?xml version=\"1.0\"?> \t \n<a/>\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL, contentStyle("#!/bin/bash  \necho hi\n"));
    }

    @Test
    public void test_forContent_onlyTheFirstLineCounts() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, contentStyle("\n#!/bin/bash\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, contentStyle("some text\n#!/bin/bash\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, contentStyle("<!-- a comment -->\n<!DOCTYPE html>\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, contentStyle(" <?xml version=\"1.0\"?>\n"));
    }

    @Test
    public void test_forContent_textThatTellsNothing() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, contentStyle("hello\nworld\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, contentStyle("select 1 from dual;\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, contentStyle("<html></html>\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, contentStyle("<?xml version=\"1.0\"\n?>\n"));
    }

    @Test
    public void test_forContent_noTextOrAnOddFirstLine() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, contentStyle(null));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, contentStyle(""));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, contentStyle("\r\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, contentStyle("#!"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, contentStyle("#!/usr/bin/env"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, contentStyle("#!/usr/bin/env "));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, contentStyle("#!/usr/bin/env   \n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, contentStyle("#! /bin/bash\n"));
    }

    @Test
    public void test_forContent_veryLongFirstLine() {
        StringBuilder sb = new StringBuilder("{\"data\":[");
        for (int i = 0; i < 20000; i++) {
            sb.append(i).append(',');
        }
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, contentStyle(sb.toString()));
        // What follows the interpreter in a #! line does not matter, however long
        assertEquals(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL,
                contentStyle("#!/bin/bash " + "x".repeat(2000) + "\necho hi\n"));
    }

    private static String openedStyle(String fileName, String text) {
        return FileSyntaxStyle.forFileAndContent(fileName == null ? null : new File(fileName), text);
    }

    @Test
    public void test_forFileAndContent_nameWithoutExtensionTellsNothingSoTheContentDoes() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL, openedStyle("deploy", "#!/bin/bash\necho hi\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_XML, openedStyle("config", "<?xml version=\"1.0\"?>\r\n<a/>\r\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_HTML, openedStyle("index", "<!DOCTYPE html>\n<html></html>\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_PYTHON, openedStyle("tool", "#!/usr/bin/env python3\nprint(1)\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL,
                openedStyle("SCRIPT", "#!/bin/bash\necho hi\n"));
    }

    @Test
    public void test_forFileAndContent_noContentEitherIsNotHighlighted() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, openedStyle("deploy", "just some text\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, openedStyle("deploy", ""));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, openedStyle("deploy", null));
    }

    @Test
    public void test_forFileAndContent_theNameWinsOverTheContent() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_PYTHON, openedStyle("tool.py", "#!/bin/bash\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_SQL, openedStyle("query.sql", "<?xml version=\"1.0\"?>\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_DOCKERFILE, openedStyle("Dockerfile", "<?xml version=\"1.0\"?>\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_MAKEFILE, openedStyle("Makefile", "#!/bin/bash\n"));
    }

    @Test
    public void test_forFileAndContent_aNameWithAnExtensionIsTrusted() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, openedStyle("notes.txt", "#!/bin/bash\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, openedStyle("script.bteq", "<?xml version=\"1.0\"?>\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, openedStyle(".profile", "#!/bin/sh\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, openedStyle("archive.tar.gz", "<!DOCTYPE html>\n"));
    }

    @Test
    public void test_forFileAndContent_theExtensionOfABackupCopyDoesNotCount() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL, openedStyle("deploy.bak", "#!/bin/bash\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL, openedStyle("DEPLOY.OLD", "#!/bin/bash\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL, openedStyle("deploy.orig", "#!/bin/bash\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_JAVA, openedStyle("Main.java.bak", "#!/bin/bash\n"));
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, openedStyle("notes.txt.bak", "#!/bin/bash\n"));
    }

    @Test
    public void test_forFileAndContent_onlyTheNameOfTheFileCounts() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL,
                openedStyle("project.d" + File.separator + "deploy", "#!/bin/bash\n"));
    }

    @Test
    public void test_forFileAndContent_noFile() {
        assertEquals(SyntaxConstants.SYNTAX_STYLE_NONE, openedStyle(null, "#!/bin/bash\n"));
    }
}
