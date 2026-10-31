/*
 * Open Teradata Viewer ( kernel )
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

package test.net.sourceforge.open_teradata_viewer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import net.sourceforge.open_teradata_viewer.CommandLine;

/**
 *
 *
 * @author D. Campione
 *
 */
public class TestCommandLine {

    private static CommandLine parse(String... args) {
        return CommandLine.parse(args);
    }

    @Test
    public void test_parse_noArguments() {
        CommandLine commandLine = parse();
        assertTrue(commandLine.getFiles().isEmpty());
        assertFalse(commandLine.isHelpRequested());
        assertNull(commandLine.getInvalidOption());
    }

    @Test
    public void test_parse_oneFile() {
        CommandLine commandLine = parse("query.sql");
        assertEquals(Arrays.asList(new File("query.sql").getAbsoluteFile()), commandLine.getFiles());
        assertFalse(commandLine.isHelpRequested());
        assertNull(commandLine.getInvalidOption());
    }

    @Test
    public void test_parse_filesKeepTheOrderTheyWereGiven() {
        CommandLine commandLine = parse("c.sql", "a.sql", "b.sql");
        assertEquals(Arrays.asList(new File("c.sql").getAbsoluteFile(), new File("a.sql").getAbsoluteFile(),
                new File("b.sql").getAbsoluteFile()), commandLine.getFiles());
    }

    @Test
    public void test_parse_sameFileTwiceIsKeptTwice() {
        // Opening the same file twice shows its tab: the editor takes care of
        // it, the arguments are not changed
        assertEquals(2, parse("a.sql", "a.sql").getFiles().size());
    }

    @Test
    public void test_parse_relativeNamesBecomeAbsolute() {
        File file = parse("sub" + File.separator + "query.sql").getFiles().get(0);
        assertTrue(file.isAbsolute());
        assertEquals(new File("sub" + File.separator + "query.sql").getAbsoluteFile(), file);
    }

    @Test
    public void test_parse_absoluteNamesAreKept() {
        File absolute = new File("query.sql").getAbsoluteFile();
        assertEquals(Arrays.asList(absolute), parse(absolute.getPath()).getFiles());
    }

    @Test
    public void test_parse_namesWithSpacesAreOneFile() {
        CommandLine commandLine = parse("my queries" + File.separator + "daily report.sql");
        assertEquals(1, commandLine.getFiles().size());
        assertEquals("daily report.sql", commandLine.getFiles().get(0).getName());
    }

    @Test
    public void test_parse_nonexistentFilesAreNotChecked() {
        assertEquals(1, parse("surely_not_there_42.sql").getFiles().size());
    }

    @Test
    public void test_parse_emptyArgumentsAreIgnored() {
        CommandLine commandLine = parse("", "a.sql", "   ", "b.sql");
        assertEquals(Arrays.asList(new File("a.sql").getAbsoluteFile(), new File("b.sql").getAbsoluteFile()),
                commandLine.getFiles());
        assertNull(commandLine.getInvalidOption());
    }

    @Test
    public void test_parse_onlyEmptyArgumentsIsLikeNoArguments() {
        CommandLine commandLine = parse("");
        assertTrue(commandLine.getFiles().isEmpty());
        assertFalse(commandLine.isHelpRequested());
        assertNull(commandLine.getInvalidOption());
    }

    @Test
    public void test_parse_shortHelp() {
        CommandLine commandLine = parse("-h");
        assertTrue(commandLine.isHelpRequested());
        assertTrue(commandLine.getFiles().isEmpty());
        assertNull(commandLine.getInvalidOption());
    }

    @Test
    public void test_parse_longHelp() {
        CommandLine commandLine = parse("--help");
        assertTrue(commandLine.isHelpRequested());
        assertTrue(commandLine.getFiles().isEmpty());
        assertNull(commandLine.getInvalidOption());
    }

    @Test
    public void test_parse_helpIsAskedForAnywhere() {
        assertTrue(parse("a.sql", "--help", "b.sql").isHelpRequested());
    }

    @Test
    public void test_parse_helpIsCaseSensitive() {
        CommandLine commandLine = parse("-H");
        assertFalse(commandLine.isHelpRequested());
        assertEquals("-H", commandLine.getInvalidOption());
    }

    @Test
    public void test_parse_unknownOption() {
        CommandLine commandLine = parse("--verbose");
        assertEquals("--verbose", commandLine.getInvalidOption());
        assertFalse(commandLine.isHelpRequested());
        assertTrue(commandLine.getFiles().isEmpty());
    }

    @Test
    public void test_parse_aLoneDashIsAnUnknownOption() {
        assertEquals("-", parse("-").getInvalidOption());
    }

    @Test
    public void test_parse_firstUnknownOptionIsTheOneReported() {
        assertEquals("-x", parse("-x", "--y").getInvalidOption());
    }

    @Test
    public void test_parse_unknownOptionDoesNotHideTheFiles() {
        CommandLine commandLine = parse("a.sql", "-x");
        assertEquals("-x", commandLine.getInvalidOption());
        assertEquals(1, commandLine.getFiles().size());
    }

    @Test
    public void test_parse_unknownOptionAndHelp() {
        CommandLine commandLine = parse("-x", "-h");
        assertTrue(commandLine.isHelpRequested());
        assertEquals("-x", commandLine.getInvalidOption());
    }

    @Test
    public void test_parse_afterTheEndOfOptionsEverythingIsAFile() {
        CommandLine commandLine = parse("--", "-h", "--help", "-x");
        assertFalse(commandLine.isHelpRequested());
        assertNull(commandLine.getInvalidOption());
        assertEquals(Arrays.asList(new File("-h").getAbsoluteFile(), new File("--help").getAbsoluteFile(),
                new File("-x").getAbsoluteFile()), commandLine.getFiles());
    }

    @Test
    public void test_parse_optionsBeforeTheEndOfOptionsStillCount() {
        CommandLine commandLine = parse("-h", "--", "a.sql");
        assertTrue(commandLine.isHelpRequested());
        assertEquals(1, commandLine.getFiles().size());
    }

    @Test
    public void test_parse_theSecondEndOfOptionsIsAFile() {
        CommandLine commandLine = parse("--", "--");
        assertEquals(Arrays.asList(new File("--").getAbsoluteFile()), commandLine.getFiles());
        assertNull(commandLine.getInvalidOption());
    }

    @Test
    public void test_parse_aDashInsideANameIsNotAnOption() {
        CommandLine commandLine = parse("my-query.sql", "dir-1" + File.separator + "a-b.sql");
        assertNull(commandLine.getInvalidOption());
        assertEquals(2, commandLine.getFiles().size());
    }

    @Test
    public void test_getFiles_cannotBeModified() {
        CommandLine commandLine = parse("a.sql");
        assertThrows(UnsupportedOperationException.class, () -> commandLine.getFiles().add(new File("b.sql")));
        assertThrows(UnsupportedOperationException.class, () -> commandLine.getFiles().clear());
    }

    @Test
    public void test_parse_doesNotKeepTheArray() {
        String[] args = { "a.sql" };
        CommandLine commandLine = CommandLine.parse(args);
        args[0] = "other.sql";
        assertEquals(Arrays.asList(new File("a.sql").getAbsoluteFile()), commandLine.getFiles());
    }

    @Test
    public void test_getUsage_describesTheOptionsAndTheFiles() {
        String usage = CommandLine.getUsage();
        assertTrue(usage.startsWith("Usage: java net.sourceforge.open_teradata_viewer.Main"));
        assertTrue(usage.contains("[file ...]"));
        assertTrue(usage.contains("and the first one is shown"));
        assertTrue(usage.contains("-h, --help"));
        assertTrue(usage.contains("--  "));
    }
}
