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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import net.sourceforge.open_teradata_viewer.editor.TextFileCodec;
import net.sourceforge.open_teradata_viewer.editor.TextFileCodec.Decoded;

/**
 *
 *
 * @author D. Campione
 *
 */
public class TestTextFileCodec {

    private static final Charset WINDOWS_1252 = Charset.forName("windows-1252");

    private static byte[] bytes(int... values) {
        byte[] bytes = new byte[values.length];
        for (int i = 0; i < values.length; i++) {
            bytes[i] = (byte) values[i];
        }
        return bytes;
    }

    @Test
    public void asciiAndEmptyFilesAreUtf8() {
        Decoded ascii = TextFileCodec.decode("select 1;".getBytes(StandardCharsets.US_ASCII), WINDOWS_1252);
        assertEquals("UTF-8", ascii.getEncoding());
        assertFalse(ascii.hasBom());
        assertEquals("select 1;", ascii.getText());

        Decoded empty = TextFileCodec.decode(new byte[0], WINDOWS_1252);
        assertEquals("UTF-8", empty.getEncoding());
        assertEquals("", empty.getText());
    }

    @Test
    public void validUtf8IsPreferredToThePlatformEncoding() {
        Decoded decoded = TextFileCodec.decode("\u00e8\u20ac\uD83D\uDE00".getBytes(StandardCharsets.UTF_8), WINDOWS_1252);
        assertEquals("UTF-8", decoded.getEncoding());
        assertEquals("\u00e8\u20ac\uD83D\uDE00", decoded.getText());
    }

    @Test
    public void bomTellsTheEncodingAndIsNotPartOfTheText() {
        Object[][] cases = {
                { bytes(0xEF, 0xBB, 0xBF, 'a', 0xC3, 0xA8), "UTF-8", "a\u00e8" },
                { bytes(0xFF, 0xFE, 'a', 0, 0xAC, 0x20), "UTF-16LE", "a\u20ac" },
                { bytes(0xFE, 0xFF, 0, 'a', 0x20, 0xAC), "UTF-16BE", "a\u20ac" },
                { bytes(0xFF, 0xFE, 0, 0, 'a', 0, 0, 0), "UTF-32LE", "a" },
                { bytes(0, 0, 0xFE, 0xFF, 0, 0, 0, 'a'), "UTF-32BE", "a" } };
        for (Object[] c : cases) {
            Decoded decoded = TextFileCodec.decode((byte[]) c[0], WINDOWS_1252);
            assertEquals(c[1], decoded.getEncoding());
            assertTrue(decoded.hasBom());
            assertEquals(c[2], decoded.getText());
        }
    }

    @Test
    public void whatIsNotUtf8IsReadInThePlatformEncodingOrInLatin1() {
        Decoded decoded = TextFileCodec.decode(bytes('a', 0xE8, 0x80), WINDOWS_1252);
        assertEquals("windows-1252", decoded.getEncoding());
        assertEquals("a\u00e8\u20ac", decoded.getText());

        // 0x81 has no character in windows-1252: nothing is replaced, so the
        // encoding that has all of them is used
        Decoded undefined = TextFileCodec.decode(bytes('a', 0x81), WINDOWS_1252);
        assertEquals("ISO-8859-1", undefined.getEncoding());
        assertEquals("a\u0081", undefined.getText());

        Decoded utf8Platform = TextFileCodec.decode(bytes('a', 0xE8), StandardCharsets.UTF_8);
        assertEquals("ISO-8859-1", utf8Platform.getEncoding());
        assertEquals("a\u00e8", utf8Platform.getText());
    }

    @Test
    public void readingAndWritingBackGivesTheSameBytes() throws Exception {
        Random random = new Random(20261108L);
        for (int i = 0; i < 2000; i++) {
            byte[] original = new byte[random.nextInt(40)];
            random.nextBytes(original);
            // Some files start with a BOM, as real ones do
            if (i % 5 == 0 && original.length >= 4) {
                byte[][] boms = { bytes(0xEF, 0xBB, 0xBF), bytes(0xFF, 0xFE), bytes(0xFE, 0xFF) };
                byte[] bom = boms[i % 3];
                System.arraycopy(bom, 0, original, 0, bom.length);
            }
            for (Charset platform : new Charset[] { WINDOWS_1252, StandardCharsets.UTF_8 }) {
                Decoded decoded = TextFileCodec.decode(original, platform);
                byte[] written = TextFileCodec.encode(decoded.getText(), decoded.getEncoding(),
                        TextFileCodec.writesBom(decoded.getEncoding(), decoded.hasBom()));
                assertArrayEquals(original, written, "iteration " + i + ", " + platform);
            }
        }
    }

    @Test
    public void aGivenEncodingIsUsedWhenThereIsNoBom() throws Exception {
        Decoded decoded = TextFileCodec.decode(bytes('a', 0xE8), "windows-1252");
        assertEquals("windows-1252", decoded.getEncoding());
        assertEquals("a\u00e8", decoded.getText());

        // The BOM wins
        Decoded withBom = TextFileCodec.decode(bytes(0xEF, 0xBB, 0xBF, 'a'), "windows-1252");
        assertEquals("UTF-8", withBom.getEncoding());
        assertTrue(withBom.hasBom());
    }

    @Test
    public void aFileThatIsNotValidInTheGivenEncodingIsReportedNotReplaced() {
        assertThrows(CharacterCodingException.class, () -> TextFileCodec.decode(bytes('a', 0xE8), "UTF-8"));
        assertThrows(CharacterCodingException.class, () -> TextFileCodec.decode(bytes('a', 0x81), "windows-1252"));
    }

    @Test
    public void readingAgainKeepsTheEncodingWhileItIsValid() {
        Decoded kept = TextFileCodec.decodeAgain(bytes('a', 0xE8), "windows-1252");
        assertEquals("windows-1252", kept.getEncoding());

        Decoded guessed = TextFileCodec.decodeAgain(bytes('a', 0xE8), "UTF-8");
        assertFalse("UTF-8".equals(guessed.getEncoding()));
        assertEquals("a\u00e8", guessed.getText());

        Decoded bom = TextFileCodec.decodeAgain(bytes(0xFF, 0xFE, 'a', 0), "windows-1252");
        assertEquals("UTF-16LE", bom.getEncoding());
    }

    @Test
    public void textThatDoesNotFitIsReportedNotWrittenAsQuestionMarks() {
        assertThrows(CharacterCodingException.class, () -> TextFileCodec.encode("a\u20ac", "ISO-8859-1", false));
        assertThrows(CharacterCodingException.class, () -> TextFileCodec.encode("\uD83D", "UTF-8", false));
        assertThrows(CharacterCodingException.class, () -> TextFileCodec.encode("\u00e8", "US-ASCII", false));
    }

    @Test
    public void bomIsWrittenWhenAskedAndOnlyOnce() throws Exception {
        assertArrayEquals(bytes(0xEF, 0xBB, 0xBF, 'a'), TextFileCodec.encode("a", "UTF-8", true));
        assertArrayEquals(bytes('a'), TextFileCodec.encode("a", "UTF-8", false));
        assertArrayEquals(bytes(0xFF, 0xFE, 'a', 0), TextFileCodec.encode("a", "UTF-16LE", true));
        assertArrayEquals(bytes(0xFE, 0xFF, 0, 'a'), TextFileCodec.encode("a", "UTF-16BE", true));
        assertArrayEquals(bytes(0xFF, 0xFE, 0, 0, 'a', 0, 0, 0), TextFileCodec.encode("a", "UTF-32LE", true));
        assertArrayEquals(bytes(0, 0, 0xFE, 0xFF, 0, 0, 0, 'a'), TextFileCodec.encode("a", "UTF-32BE", true));
        assertArrayEquals(bytes(0, 0, 0xFE, 0xFF, 0, 0, 0, 'a'), TextFileCodec.encode("a", "UTF-32", true));
        // The encoder of UTF-16 writes it by itself
        assertArrayEquals(bytes(0xFE, 0xFF, 0, 'a'), TextFileCodec.encode("a", "UTF-16", false));
        assertArrayEquals(bytes(0xE8), TextFileCodec.encode("\u00e8", "ISO-8859-1", true));
    }

    @Test
    public void bomRules() {
        assertTrue(TextFileCodec.writesBom("UTF-8", true));
        assertFalse(TextFileCodec.writesBom("UTF-8", false));
        for (String name : new String[] { "UTF-16LE", "UTF-16BE", "UTF-32", "UTF-32LE", "UTF-32BE" }) {
            assertTrue(TextFileCodec.writesBom(name, false), name);
        }
        assertFalse(TextFileCodec.writesBom("UTF-16", false));
        assertFalse(TextFileCodec.writesBom("windows-1252", true));
    }

    @Test
    public void encodingsToChooseFrom() {
        List<String> names = TextFileCodec.getEncodings();
        assertTrue(names.contains("UTF-8"));
        assertTrue(names.contains("ISO-8859-1"));
        assertTrue(names.contains("windows-1252"));
        for (int i = 1; i < names.size(); i++) {
            assertTrue(names.get(i - 1).compareToIgnoreCase(names.get(i)) <= 0, names.get(i));
        }
        for (String name : names) {
            assertTrue(Charset.forName(name).canEncode(), name);
        }
        assertTrue(Charset.isSupported(TextFileCodec.getDefaultEncoding()));
    }

    @Test
    public void onWindowsTheSystemEncodingIsTheAnsiCodePage() {
        Charset utf8 = StandardCharsets.UTF_8;
        // The default encoding of Java does not matter: with -Dfile.encoding=UTF-8
        // (or from Java 18 on) it is UTF-8, the code page is what is asked for
        assertEquals(WINDOWS_1252, TextFileCodec.findSystemCharset("Windows 10", "Cp1252", null, utf8));
        assertEquals(WINDOWS_1252, TextFileCodec.findSystemCharset("Windows 11", "Cp1252", "Cp1252", utf8));
        assertEquals(Charset.forName("windows-1250"),
                TextFileCodec.findSystemCharset("Windows 11", "Cp1250", "Cp1252", utf8));
        assertEquals(Charset.forName("MS932"), TextFileCodec.findSystemCharset("Windows 10", "MS932", null, utf8));
        assertEquals(WINDOWS_1252,
                TextFileCodec.findSystemCharset("Windows Server 2022", "Cp1252", null, StandardCharsets.ISO_8859_1));
    }

    @Test
    public void onWindowsSetToUtf8TheCodePageOfTheLanguageIsUsed() {
        Charset utf8 = StandardCharsets.UTF_8;
        // Java 17 and later tell it as native.encoding
        assertEquals(Charset.forName("windows-1250"),
                TextFileCodec.findSystemCharset("Windows 11", "UTF-8", "Cp1250", utf8));
        // Java 11 does not: Windows-1252
        assertEquals(WINDOWS_1252, TextFileCodec.findSystemCharset("Windows 11", "UTF-8", null, utf8));
        assertEquals(WINDOWS_1252, TextFileCodec.findSystemCharset("Windows 11", "UTF-8", "UTF-8", utf8));
        assertEquals(WINDOWS_1252, TextFileCodec.findSystemCharset("Windows 11", "UTF-8", "UTF-16LE", utf8));
    }

    @Test
    public void onWindowsAnUnknownCodePageIsSkipped() {
        Charset utf8 = StandardCharsets.UTF_8;
        assertEquals(WINDOWS_1252, TextFileCodec.findSystemCharset("Windows 10", null, null, utf8));
        assertEquals(WINDOWS_1252, TextFileCodec.findSystemCharset("Windows 10", "", "", utf8));
        assertEquals(WINDOWS_1252, TextFileCodec.findSystemCharset("Windows 10", "NoSuchCodePage", null, utf8));
        assertEquals(WINDOWS_1252, TextFileCodec.findSystemCharset("Windows 10", "not a name!", "?", utf8));
        assertEquals(Charset.forName("windows-1251"),
                TextFileCodec.findSystemCharset("Windows 10", "NoSuchCodePage", "Cp1251", utf8));
    }

    @Test
    public void elsewhereTheSystemEncodingIsTheDefaultOfJava() {
        // What Windows would say is not looked at
        for (String os : new String[] { "Linux", "Mac OS X", "FreeBSD", "SunOS", "" }) {
            assertEquals(StandardCharsets.UTF_8,
                    TextFileCodec.findSystemCharset(os, "Cp1252", "Cp1252", StandardCharsets.UTF_8), os);
            assertEquals(StandardCharsets.ISO_8859_1,
                    TextFileCodec.findSystemCharset(os, "Cp1252", "Cp1252", StandardCharsets.ISO_8859_1), os);
        }
        assertEquals(StandardCharsets.UTF_8,
                TextFileCodec.findSystemCharset(null, "Cp1252", "Cp1252", StandardCharsets.UTF_8));
    }

    @Test
    public void aWindows1252FileIsReadAsSuchEvenIfJavaDefaultsToUtf8() {
        // Euro sign, curly quotes and en dash: the bytes 0x80 to 0x9F, which
        // ISO-8859-1 reads as control characters
        byte[] file = bytes('a', ' ', 0x80, ' ', 0x93, 'b', 0x94, ' ', 0x96, ' ', 0xE8);
        Charset system = TextFileCodec.findSystemCharset("Windows 10", "Cp1252", null, StandardCharsets.UTF_8);
        Decoded decoded = TextFileCodec.decode(file, system);
        assertEquals("windows-1252", decoded.getEncoding());
        assertEquals("a \u20ac \u201cb\u201d \u2013 \u00e8", decoded.getText());

        Decoded before = TextFileCodec.decode(file, StandardCharsets.UTF_8);
        assertEquals("ISO-8859-1", before.getEncoding());
        assertEquals("a \u0080 \u0093b\u0094 \u0096 \u00e8", before.getText());
    }

    @Test
    public void theSystemEncodingOfThisJavaIsTheOneThatIsGuessed() {
        Charset system = TextFileCodec.findSystemCharset(System.getProperty("os.name"),
                System.getProperty("sun.jnu.encoding"), System.getProperty("native.encoding"), Charset.defaultCharset());
        assertEquals(system.name(), TextFileCodec.getDefaultEncoding());
        byte[] file = bytes('a', 0xE8, 0x80);
        Decoded guessed = TextFileCodec.decode(file);
        Decoded expected = TextFileCodec.decode(file, system);
        assertEquals(expected.getEncoding(), guessed.getEncoding());
        assertEquals(expected.getText(), guessed.getText());
    }
}
