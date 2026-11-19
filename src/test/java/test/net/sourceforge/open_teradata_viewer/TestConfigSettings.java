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

package test.net.sourceforge.open_teradata_viewer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import net.sourceforge.open_teradata_viewer.Config;

/**
 * The settings of the configuration file, which is the one of the user.
 *
 * @author D. Campione
 *
 */
public class TestConfigSettings {

    @TempDir
    Path tempDir;

    private String savedUserHome;
    private Path home;

    @BeforeEach
    public void setUp() throws IOException {
        savedUserHome = System.getProperty("user.home");
        home = Files.createDirectory(tempDir.resolve("home"));
        System.setProperty("user.home", home.toString());
    }

    @AfterEach
    public void tearDown() {
        System.setProperty("user.home", savedUserHome);
    }

    private Path configFile() {
        return home.resolve("open_teradata_viewer.xml");
    }

    private String configText() throws IOException {
        return new String(Files.readAllBytes(configFile()), StandardCharsets.UTF_8);
    }

    @Test
    public void saveSettingsKeepsAllTheValues() throws Exception {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("first", "1");
        values.put("second", "two");
        Config.saveSettings(values);
        assertEquals("1", Config.getSetting("first"));
        assertEquals("two", Config.getSetting("second"));
        String text = configText();
        assertTrue(text.contains("first=\"1\""), text);
        assertTrue(text.contains("second=\"two\""), text);
    }

    @Test
    public void saveSettingsKeepsTheOtherSettingsAndChangesTheOnesThatAreThere() throws Exception {
        Files.write(configFile(), "<config><settings other=\"x\" first=\"old\"/></config>".getBytes(StandardCharsets.UTF_8));
        Map<String, String> values = new LinkedHashMap<>();
        values.put("first", "new");
        values.put("second", "2");
        Config.saveSettings(values);
        assertEquals("x", Config.getSetting("other"));
        assertEquals("new", Config.getSetting("first"));
        assertEquals("2", Config.getSetting("second"));
    }

    @Test
    public void saveSettingKeepsOneValue() throws Exception {
        Config.saveSetting("single", "value");
        assertEquals("value", Config.getSetting("single"));
        Config.saveSetting("single", "another");
        assertEquals("another", Config.getSetting("single"));
    }

    @Test
    public void nothingIsWrittenIfEveryValueIsAlreadyThere() throws Exception {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("first", "1");
        values.put("second", "2");
        Config.saveSettings(values);
        FileTime old = FileTime.fromMillis(946684800000L);
        Files.setLastModifiedTime(configFile(), old);

        Config.saveSettings(values);
        assertEquals(old, Files.getLastModifiedTime(configFile()));
        Config.saveSetting("first", "1");
        assertEquals(old, Files.getLastModifiedTime(configFile()));

        // One of them differs: the file is written
        values.put("second", "3");
        Config.saveSettings(values);
        assertTrue(!old.equals(Files.getLastModifiedTime(configFile())));
        assertEquals("3", Config.getSetting("second"));
    }

    @Test
    public void anEmptyValueIsKeptWhenTheFileHasNoSettings() throws Exception {
        Config.saveSettings(Collections.singletonMap("empty", ""));
        assertTrue(Files.exists(configFile()));
        assertTrue(configText().contains("<settings"), configText());
    }

    @Test
    public void nothingIsChangedIfTheFileCannotBeWritten() throws Exception {
        Map<String, String> values = new LinkedHashMap<>();
        values.put("first", "1");
        values.put("second", "2");
        Config.saveSettings(values);
        String before = configText();

        // The file cannot be written in a folder that does not exist, even by
        // a user who can write everywhere
        System.setProperty("user.home", home.resolve("no-such-folder").toString());
        values.put("first", "changed");
        values.put("second", "changed");
        assertThrows(Exception.class, () -> Config.saveSettings(values));
        System.setProperty("user.home", home.toString());

        assertEquals(before, configText());
        assertEquals("1", Config.getSetting("first"));
        assertEquals("2", Config.getSetting("second"));
    }
}
