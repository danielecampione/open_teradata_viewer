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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import net.sourceforge.open_teradata_viewer.DocumentState;
import net.sourceforge.open_teradata_viewer.editor.DefaultEncoding;
import net.sourceforge.open_teradata_viewer.editor.EncodingChoice;
import net.sourceforge.open_teradata_viewer.editor.TextFileCodec;

/**
 *
 *
 * @author D. Campione
 *
 */
public class TestDefaultEncoding {

    @TempDir
    Path tempDir;

    private String savedUserHome;
    private Path home;

    @BeforeEach
    public void setUp() throws IOException {
        // The encoding in use is shared by the whole application, and so is
        // the configuration file of the user
        DefaultEncoding.set(EncodingChoice.UTF_8);
        savedUserHome = System.getProperty("user.home");
        home = Files.createDirectory(tempDir.resolve("home"));
        System.setProperty("user.home", home.toString());
    }

    @AfterEach
    public void tearDown() {
        System.setProperty("user.home", savedUserHome);
        DefaultEncoding.set(EncodingChoice.UTF_8);
    }

    private Path configFile() {
        return home.resolve("open_teradata_viewer.xml");
    }

    private String configText() throws IOException {
        return new String(Files.readAllBytes(configFile()), StandardCharsets.UTF_8);
    }

    private void writeConfig(String settingsAttributes) throws IOException {
        Files.write(configFile(),
                ("<config><settings " + settingsAttributes + "/></config>").getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void itIsUtf8WithoutABomUntilTheUserChoosesAnother() {
        assertEquals("UTF-8", DefaultEncoding.get());
        assertFalse(DefaultEncoding.hasBom());
        assertSame(EncodingChoice.UTF_8, DefaultEncoding.getChoice());
    }

    @Test
    public void namesThatCannotBeUsedStandForUtf8() {
        assertEquals("UTF-8", DefaultEncoding.normalize(null));
        assertEquals("UTF-8", DefaultEncoding.normalize(""));
        assertEquals("UTF-8", DefaultEncoding.normalize("   "));
        assertEquals("UTF-8", DefaultEncoding.normalize("no-such-encoding"));
        assertEquals("UTF-8", DefaultEncoding.normalize("not a name!"));
    }

    @Test
    public void anEncodingThatCannotWriteATextStandsForUtf8() {
        List<String> decodeOnly = Charset.availableCharsets().values().stream().filter(charset -> !charset.canEncode())
                .map(Charset::name).collect(Collectors.toList());
        assumeFalse(decodeOnly.isEmpty());
        for (String name : decodeOnly) {
            assertEquals("UTF-8", DefaultEncoding.normalize(name), name);
        }
    }

    @Test
    public void aliasesAndSpacesGiveTheNameOfTheEncoding() {
        assertEquals("UTF-8", DefaultEncoding.normalize("utf8"));
        assertEquals("windows-1252", DefaultEncoding.normalize("  Windows-1252 "));
        assertEquals("ISO-8859-1", DefaultEncoding.normalize("latin1"));
    }

    @Test
    public void everyEncodingOfTheListsIsKeptAsItIs() {
        List<String> encodings = TextFileCodec.getEncodings();
        assertNotNull(encodings);
        for (String name : encodings) {
            assertEquals(name, DefaultEncoding.normalize(name), name);
        }
    }

    @Test
    public void setChangesTheEncodingInUse() {
        DefaultEncoding.set(EncodingChoice.of("windows-1252", false));
        assertEquals("windows-1252", DefaultEncoding.get());
        DefaultEncoding.set(EncodingChoice.of("utf8", false));
        assertEquals("UTF-8", DefaultEncoding.get());
        DefaultEncoding.set(EncodingChoice.of("windows-1252", false));
        DefaultEncoding.set(null);
        assertEquals("UTF-8", DefaultEncoding.get());
        assertFalse(DefaultEncoding.hasBom());
    }

    @Test
    public void setChangesTheBomInUseToo() {
        DefaultEncoding.set(EncodingChoice.UTF_8_WITH_BOM);
        assertEquals("UTF-8", DefaultEncoding.get());
        assertTrue(DefaultEncoding.hasBom());
        assertSame(EncodingChoice.UTF_8_WITH_BOM, DefaultEncoding.getChoice());
        DefaultEncoding.set(EncodingChoice.UTF_8);
        assertFalse(DefaultEncoding.hasBom());

        // The BOM is a choice only for UTF-8: it is gone with another encoding
        DefaultEncoding.set(EncodingChoice.UTF_8_WITH_BOM);
        DefaultEncoding.set(EncodingChoice.of("windows-1252", true));
        assertEquals("windows-1252", DefaultEncoding.get());
        assertFalse(DefaultEncoding.hasBom());
        DefaultEncoding.set(EncodingChoice.UTF_8);
        assertFalse(DefaultEncoding.hasBom());
    }

    @Test
    public void anEncodingThatCannotWriteATextIsReplacedWithUtf8WithoutABom() {
        List<String> decodeOnly = Charset.availableCharsets().values().stream().filter(charset -> !charset.canEncode())
                .map(Charset::name).collect(Collectors.toList());
        assumeFalse(decodeOnly.isEmpty());
        DefaultEncoding.set(EncodingChoice.UTF_8_WITH_BOM);
        for (String name : decodeOnly) {
            assertSame(EncodingChoice.UTF_8, DefaultEncoding.normalizeChoice(EncodingChoice.of(name, true)), name);
        }
        assertSame(EncodingChoice.UTF_8, DefaultEncoding.normalizeChoice(null));
        // What can be used is left as it is
        assertSame(EncodingChoice.UTF_8_WITH_BOM, DefaultEncoding.normalizeChoice(EncodingChoice.UTF_8_WITH_BOM));
        EncodingChoice latin = EncodingChoice.of("ISO-8859-15", false);
        assertSame(latin, DefaultEncoding.normalizeChoice(latin));
    }

    @Test
    public void aNewDocumentStateStartsWithTheEncodingAndTheBomInUse() {
        DocumentState before = new DocumentState();
        DefaultEncoding.set(EncodingChoice.of("ISO-8859-15", false));
        DocumentState after = new DocumentState();
        assertEquals("ISO-8859-15", after.getEncoding());
        assertFalse(after.hasBom());
        // What has been created before does not change on its own
        assertEquals("UTF-8", before.getEncoding());
        DefaultEncoding.set(EncodingChoice.UTF_8_WITH_BOM);
        DocumentState withBom = new DocumentState();
        assertEquals("UTF-8", withBom.getEncoding());
        assertTrue(withBom.hasBom());
        DefaultEncoding.set(EncodingChoice.UTF_8);
        DocumentState plain = new DocumentState();
        assertEquals("UTF-8", plain.getEncoding());
        assertFalse(plain.hasBom());
        assertEquals("ISO-8859-15", after.getEncoding());
        assertTrue(withBom.hasBom());
        assertFalse(before.hasBom());
    }

    @Test
    public void loadTakesTheEncodingAndTheBomFromTheConfigurationFile() throws IOException {
        writeConfig("default_encoding=\"UTF-8\" default_encoding_bom=\"true\"");
        DefaultEncoding.load();
        assertSame(EncodingChoice.UTF_8_WITH_BOM, DefaultEncoding.getChoice());

        writeConfig("default_encoding=\"windows-1252\" default_encoding_bom=\"false\"");
        DefaultEncoding.load();
        assertEquals("windows-1252", DefaultEncoding.get());
        assertFalse(DefaultEncoding.hasBom());
    }

    @Test
    public void aFileWrittenBeforeTheBomCouldBeChosenGivesNoBom() throws IOException {
        DefaultEncoding.set(EncodingChoice.UTF_8_WITH_BOM);
        writeConfig("default_encoding=\"UTF-8\"");
        DefaultEncoding.load();
        assertSame(EncodingChoice.UTF_8, DefaultEncoding.getChoice());

        writeConfig("default_encoding=\"ISO-8859-15\"");
        DefaultEncoding.load();
        assertEquals("ISO-8859-15", DefaultEncoding.get());
        assertFalse(DefaultEncoding.hasBom());
    }

    @Test
    public void loadIgnoresWhatCannotBeUsed() throws IOException {
        // The BOM does not count for another encoding than UTF-8
        writeConfig("default_encoding=\"windows-1252\" default_encoding_bom=\"true\"");
        DefaultEncoding.load();
        assertEquals("windows-1252", DefaultEncoding.get());
        assertFalse(DefaultEncoding.hasBom());

        writeConfig("default_encoding=\"no-such-encoding\" default_encoding_bom=\"true\"");
        DefaultEncoding.load();
        assertEquals("UTF-8", DefaultEncoding.get());
        assertTrue(DefaultEncoding.hasBom());

        writeConfig("default_encoding=\"UTF-8\" default_encoding_bom=\"perhaps\"");
        DefaultEncoding.load();
        assertSame(EncodingChoice.UTF_8, DefaultEncoding.getChoice());
    }

    @Test
    public void loadWithoutAConfigurationFileLeavesUtf8WithoutABom() {
        DefaultEncoding.set(EncodingChoice.of("windows-1252", false));
        DefaultEncoding.load();
        assertSame(EncodingChoice.UTF_8, DefaultEncoding.getChoice());
    }

    @Test
    public void saveKeepsTheEncodingAndTheBomInTheConfigurationFile() throws Exception {
        DefaultEncoding.save(EncodingChoice.UTF_8_WITH_BOM);
        assertSame(EncodingChoice.UTF_8_WITH_BOM, DefaultEncoding.getChoice());
        String text = configText();
        assertTrue(text.contains("default_encoding=\"UTF-8\""), text);
        assertTrue(text.contains("default_encoding_bom=\"true\""), text);

        // What a following run of the application finds
        DefaultEncoding.set(EncodingChoice.of("ISO-8859-1", false));
        DefaultEncoding.load();
        assertSame(EncodingChoice.UTF_8_WITH_BOM, DefaultEncoding.getChoice());

        DefaultEncoding.save(EncodingChoice.of("windows-1252", false));
        text = configText();
        assertTrue(text.contains("default_encoding=\"windows-1252\""), text);
        assertTrue(text.contains("default_encoding_bom=\"false\""), text);
        DefaultEncoding.set(EncodingChoice.UTF_8_WITH_BOM);
        DefaultEncoding.load();
        assertEquals("windows-1252", DefaultEncoding.get());
        assertFalse(DefaultEncoding.hasBom());
    }

    @Test
    public void saveReplacesAnUnusableChoiceWithUtf8() throws Exception {
        DefaultEncoding.save(null);
        assertSame(EncodingChoice.UTF_8, DefaultEncoding.getChoice());
        String text = configText();
        assertTrue(text.contains("default_encoding=\"UTF-8\""), text);
        assertTrue(text.contains("default_encoding_bom=\"false\""), text);
    }

    @Test
    public void saveKeepsTheOtherSettingsOfTheConfigurationFile() throws Exception {
        writeConfig("dir=\"/some/dir\" default_encoding=\"UTF-8\"");
        DefaultEncoding.save(EncodingChoice.UTF_8_WITH_BOM);
        String text = configText();
        assertTrue(text.contains("dir=\"/some/dir\""), text);
        assertTrue(text.contains("default_encoding_bom=\"true\""), text);
    }

    @Test
    public void saveChangesNothingIfTheConfigurationFileCannotBeWritten() throws Exception {
        DefaultEncoding.save(EncodingChoice.of("windows-1252", false));
        String before = configText();
        // The file cannot be written in a folder that does not exist, even by
        // a user who can write everywhere
        System.setProperty("user.home", home.resolve("no-such-folder").toString());
        assertThrows(Exception.class, () -> DefaultEncoding.save(EncodingChoice.UTF_8_WITH_BOM));
        assertEquals("windows-1252", DefaultEncoding.get());
        assertFalse(DefaultEncoding.hasBom());
        System.setProperty("user.home", home.toString());
        assertEquals(before, configText());
    }
}
