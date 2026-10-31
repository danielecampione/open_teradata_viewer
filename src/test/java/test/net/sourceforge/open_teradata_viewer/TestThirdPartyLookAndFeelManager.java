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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import net.sourceforge.open_teradata_viewer.ExtendedLookAndFeelInfo;
import net.sourceforge.open_teradata_viewer.ThirdPartyLookAndFeelManager;

/**
 * Tests how the Look and Feels that are no longer shipped (the Substance skins
 * and Kunststoff) are dealt with in the XML file of the 3rd party Look and
 * Feels. The file is looked for in the user's home directory, which is pointed
 * to a temporary directory for the duration of each test.
 *
 * @author D. Campione
 *
 */
public class TestThirdPartyLookAndFeelManager {

    private static final String SUBSTANCE_PACKAGE = "org.pushingpixels.substance.";
    private static final String SUBSTANCE_RAVEN = "org.pushingpixels.substance.api.skin.SubstanceRavenLookAndFeel";
    private static final String SUBSTANCE_BUSINESS = "org.pushingpixels.substance.api.skin.SubstanceBusinessLookAndFeel";
    private static final String RADIANCE_RAVEN = "org.pushingpixels.radiance.theming.api.skin.RadianceRavenLookAndFeel";
    private static final String RADIANCE_BUSINESS = "org.pushingpixels.radiance.theming.api.skin.RadianceBusinessLookAndFeel";
    private static final String KUNSTSTOFF = "com.incors.plaf.kunststoff.KunststoffLookAndFeel";
    private static final String CUSTOM = "net.sourceforge.napkinlaf.NapkinLookAndFeel";

    /**
     * A revision that is always newer than the one of the built-in list: the
     * file is considered up to date and nothing is added to it.
     */
    private static final String UP_TO_DATE_REVISION = "1000";

    @TempDir
    Path tempDir;

    private String savedUserHome;
    private Path rootDir;
    private Path xmlFile;

    @BeforeEach
    public void setUp() throws IOException {
        savedUserHome = System.getProperty("user.home");
        Path home = Files.createDirectory(tempDir.resolve("home"));
        rootDir = Files.createDirectory(tempDir.resolve("root"));
        xmlFile = home.resolve("open_teradata_viewer_lookandfeels.xml");
        System.setProperty("user.home", home.toString());
    }

    @AfterEach
    public void tearDown() {
        System.setProperty("user.home", savedUserHome);
    }

    private static String entry(String name, String className, String minJavaVersion) {
        return "    <LookAndFeel class=\"" + className + "\" minJavaVersion=\"" + minJavaVersion + "\" name=\"" + name
                + "\"/>\n";
    }

    /**
     * @param revision The value of the revision attribute, or
     *                 <code>null</code> for a file that has none.
     */
    private void writeXml(String revision, String... entries) throws IOException {
        StringBuilder xml = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
        xml.append("<ThirdPartyLookAndFeels");
        if (revision != null) {
            xml.append(" defaultsRevision=\"").append(revision).append("\"");
        }
        xml.append(">\n");
        for (String entry : entries) {
            xml.append(entry);
        }
        xml.append("</ThirdPartyLookAndFeels>\n");
        Files.write(xmlFile, xml.toString().getBytes(StandardCharsets.UTF_8));
    }

    private String readXml() throws IOException {
        return new String(Files.readAllBytes(xmlFile), StandardCharsets.UTF_8);
    }

    private ThirdPartyLookAndFeelManager startManager() {
        return new ThirdPartyLookAndFeelManager(rootDir.toString() + File.separator);
    }

    private static boolean isOffered(ThirdPartyLookAndFeelManager manager, String className) {
        for (ExtendedLookAndFeelInfo info : manager.get3rdPartyLookAndFeelInfo()) {
            if (className.equals(info.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void test_isRetired_substanceSkinsAndKunststoff() {
        assertTrue(ThirdPartyLookAndFeelManager.isRetired(SUBSTANCE_RAVEN));
        assertTrue(ThirdPartyLookAndFeelManager.isRetired("  " + SUBSTANCE_BUSINESS + " "));
        assertTrue(ThirdPartyLookAndFeelManager.isRetired(KUNSTSTOFF));
    }

    @Test
    public void test_isRetired_otherLookAndFeelsAreNot() {
        assertFalse(ThirdPartyLookAndFeelManager.isRetired(RADIANCE_RAVEN));
        assertFalse(ThirdPartyLookAndFeelManager.isRetired(CUSTOM));
        assertFalse(ThirdPartyLookAndFeelManager.isRetired("javax.swing.plaf.metal.MetalLookAndFeel"));
        // The prefix is a whole package, not just a piece of a name
        assertFalse(ThirdPartyLookAndFeelManager.isRetired("org.pushingpixels.substance"));
        assertFalse(ThirdPartyLookAndFeelManager.isRetired("org.pushingpixels.substances.Other"));
        assertFalse(ThirdPartyLookAndFeelManager.isRetired(""));
        assertFalse(ThirdPartyLookAndFeelManager.isRetired(null));
    }

    @Test
    public void test_fileAtTheCurrentRevision_substanceEntriesAreRemoved() throws IOException {
        writeXml(UP_TO_DATE_REVISION, entry("Substance Raven", SUBSTANCE_RAVEN, "1.7"),
                entry("Napkin", CUSTOM, "1.6"), entry("Substance Business", SUBSTANCE_BUSINESS, "1.7"),
                entry("Radiance Raven", RADIANCE_RAVEN, "9"));

        ThirdPartyLookAndFeelManager manager = startManager();

        String xml = readXml();
        assertFalse(xml.contains(SUBSTANCE_PACKAGE), xml);
        assertTrue(xml.contains(CUSTOM), xml);
        assertTrue(xml.contains(RADIANCE_RAVEN), xml);
        // Nothing but the retired entries has been touched
        assertTrue(xml.contains("defaultsRevision=\"" + UP_TO_DATE_REVISION + "\""), xml);
        assertFalse(xml.contains(RADIANCE_BUSINESS), xml);

        assertFalse(isOffered(manager, SUBSTANCE_RAVEN));
        assertFalse(isOffered(manager, SUBSTANCE_BUSINESS));
        assertTrue(isOffered(manager, CUSTOM));
        assertTrue(isOffered(manager, RADIANCE_RAVEN));
    }

    @Test
    public void test_outdatedFile_substanceEntriesAreRemovedAndRadianceOnesAdded() throws IOException {
        writeXml("3", entry("Substance Raven", SUBSTANCE_RAVEN, "1.7"), entry("Napkin", CUSTOM, "1.6"));

        ThirdPartyLookAndFeelManager manager = startManager();

        String xml = readXml();
        assertFalse(xml.contains(SUBSTANCE_PACKAGE), xml);
        assertTrue(xml.contains(CUSTOM), xml);
        assertTrue(xml.contains(RADIANCE_RAVEN), xml);
        assertTrue(xml.contains(RADIANCE_BUSINESS), xml);
        assertFalse(xml.contains("defaultsRevision=\"3\""), xml);

        assertFalse(isOffered(manager, SUBSTANCE_RAVEN));
        assertTrue(isOffered(manager, RADIANCE_RAVEN));
    }

    @Test
    public void test_fileWithoutRevision_substanceEntriesAreRemoved() throws IOException {
        writeXml(null, entry("Substance Raven", SUBSTANCE_RAVEN, "1.7"), entry("Napkin", CUSTOM, "1.6"));

        startManager();

        String xml = readXml();
        assertFalse(xml.contains(SUBSTANCE_PACKAGE), xml);
        assertTrue(xml.contains(CUSTOM), xml);
        assertTrue(xml.contains("defaultsRevision="), xml);
    }

    @Test
    public void test_kunststoffEntryIsRemovedEvenAtTheCurrentRevision() throws IOException {
        writeXml(UP_TO_DATE_REVISION, entry("Kunststoff", KUNSTSTOFF, "1.6"), entry("Napkin", CUSTOM, "1.6"));

        ThirdPartyLookAndFeelManager manager = startManager();

        String xml = readXml();
        assertFalse(xml.contains(KUNSTSTOFF), xml);
        assertTrue(xml.contains(CUSTOM), xml);
        assertFalse(isOffered(manager, KUNSTSTOFF));
    }

    @Test
    public void test_aRetiredEntryThatComesBackIsRemovedAgain() throws IOException {
        writeXml(UP_TO_DATE_REVISION, entry("Napkin", CUSTOM, "1.6"));
        startManager();

        // For example, the file is saved again by an older version of the
        // application
        writeXml(UP_TO_DATE_REVISION, entry("Napkin", CUSTOM, "1.6"), entry("Substance Raven", SUBSTANCE_RAVEN, "1.7"));
        ThirdPartyLookAndFeelManager manager = startManager();

        assertFalse(readXml().contains(SUBSTANCE_PACKAGE));
        assertFalse(isOffered(manager, SUBSTANCE_RAVEN));
    }

    @Test
    public void test_fileWithoutRetiredEntriesIsNotRewritten() throws IOException {
        writeXml(UP_TO_DATE_REVISION, entry("Napkin", CUSTOM, "1.6"), entry("Radiance Raven", RADIANCE_RAVEN, "9"));
        byte[] before = Files.readAllBytes(xmlFile);

        startManager();

        assertArrayEquals(before, Files.readAllBytes(xmlFile));
    }

    @Test
    public void test_secondStartupLeavesTheFileUnchanged() throws IOException {
        writeXml(UP_TO_DATE_REVISION, entry("Substance Raven", SUBSTANCE_RAVEN, "1.7"), entry("Napkin", CUSTOM, "1.6"));
        startManager();
        byte[] afterFirstStartup = Files.readAllBytes(xmlFile);

        startManager();

        assertArrayEquals(afterFirstStartup, Files.readAllBytes(xmlFile));
    }

    @Test
    public void test_entriesRemovedByTheUserAreNotRestored() throws IOException {
        // The user removed the Radiance Raven entry: the file is at the
        // current revision, so the entries of the built-in list are not added
        // again, and removing the retired ones does not change that
        writeXml(UP_TO_DATE_REVISION, entry("Substance Raven", SUBSTANCE_RAVEN, "1.7"),
                entry("Radiance Business", RADIANCE_BUSINESS, "9"));

        startManager();

        String xml = readXml();
        assertFalse(xml.contains(SUBSTANCE_PACKAGE), xml);
        assertFalse(xml.contains(RADIANCE_RAVEN), xml);
        assertTrue(xml.contains(RADIANCE_BUSINESS), xml);
    }

    @Test
    public void test_newFileHasNoRetiredEntries() throws IOException {
        assertFalse(Files.exists(xmlFile));

        ThirdPartyLookAndFeelManager manager = startManager();

        String xml = readXml();
        assertFalse(xml.contains(SUBSTANCE_PACKAGE), xml);
        assertFalse(xml.contains(KUNSTSTOFF), xml);
        assertTrue(xml.contains(RADIANCE_RAVEN), xml);
        assertTrue(isOffered(manager, RADIANCE_RAVEN));
    }

    @Test
    public void test_retiredEntriesAreNotOfferedEvenIfTheFileCannotBeRewritten() throws IOException {
        writeXml(UP_TO_DATE_REVISION, entry("Substance Raven", SUBSTANCE_RAVEN, "1.7"),
                entry("Napkin", CUSTOM, "1.6"));
        xmlFile.toFile().setWritable(false);
        // Where the file stays writable anyway (e.g. when running as root) the
        // situation cannot be reproduced
        assumeFalse(Files.isWritable(xmlFile));

        ThirdPartyLookAndFeelManager manager = startManager();

        assertTrue(readXml().contains(SUBSTANCE_RAVEN));
        assertFalse(isOffered(manager, SUBSTANCE_RAVEN));
        assertTrue(isOffered(manager, CUSTOM));
        assertEquals(1, manager.get3rdPartyLookAndFeelInfo().length);
    }
}
