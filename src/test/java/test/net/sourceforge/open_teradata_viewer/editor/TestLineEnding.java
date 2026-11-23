/*
 * Open Teradata Viewer ( editor )
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.nio.charset.StandardCharsets;
import java.util.Random;

import org.junit.jupiter.api.Test;

import net.sourceforge.open_teradata_viewer.FileIO;
import net.sourceforge.open_teradata_viewer.editor.LineEnding;

/**
 *
 *
 * @author D. Campione
 *
 */
public class TestLineEnding {

    @Test
    public void theSequencesAreTheOnesOfTheFiles() {
        assertEquals("\r\n", LineEnding.WINDOWS.getSequence());
        assertEquals("\n", LineEnding.UNIX.getSequence());
        assertEquals("\r", LineEnding.MAC.getSequence());
    }

    @Test
    public void detectTellsTheLineEndingOfTheFirstLine() {
        assertEquals(LineEnding.WINDOWS, LineEnding.detect("select 1\r\nfrom dual\r\n"));
        assertEquals(LineEnding.UNIX, LineEnding.detect("select 1\nfrom dual\n"));
        assertEquals(LineEnding.MAC, LineEnding.detect("select 1\rfrom dual\r"));
        // Only the first one counts: the others are taken for the same
        assertEquals(LineEnding.UNIX, LineEnding.detect("a\nb\r\nc\r"));
        assertEquals(LineEnding.WINDOWS, LineEnding.detect("a\r\nb\nc\r"));
        assertEquals(LineEnding.MAC, LineEnding.detect("a\rb\r\nc\n"));
    }

    @Test
    public void detectSeesALineEndingAtTheEdgesOfTheText() {
        assertEquals(LineEnding.UNIX, LineEnding.detect("\n"));
        assertEquals(LineEnding.WINDOWS, LineEnding.detect("\r\n"));
        // A carriage return that ends the text has nothing after it
        assertEquals(LineEnding.MAC, LineEnding.detect("a\r"));
        assertEquals(LineEnding.WINDOWS, LineEnding.detect("a\r\n"));
        // Two carriage returns in a row are two line endings of the old
        // Macintosh, not a carriage return followed by a line feed
        assertEquals(LineEnding.MAC, LineEnding.detect("\r\r\n"));
    }

    @Test
    public void detectGivesNullWhenThereIsNoLineEnding() {
        assertNull(LineEnding.detect(""));
        assertNull(LineEnding.detect("select 1"));
        assertNull(LineEnding.detect("\u00e8\u20ac\uD83D\uDE00"));
    }

    @Test
    public void normalizeEndsEveryLineWithALineFeed() {
        assertEquals("a\nb\nc\nd", LineEnding.normalize("a\r\nb\rc\nd"));
        assertEquals("\n\n", LineEnding.normalize("\r\n\r\n"));
        assertEquals("\n\n", LineEnding.normalize("\r\r"));
        assertEquals("\n\n", LineEnding.normalize("\r\r\n"));
        assertEquals("a\n", LineEnding.normalize("a\r"));
        assertEquals("a\n", LineEnding.normalize("a\r\n"));
        assertEquals("", LineEnding.normalize(""));
    }

    @Test
    public void normalizeLeavesTheOtherCharactersAsTheyAre() {
        assertEquals("\u00e8\u20ac\uD83D\uDE00\n\ta ", LineEnding.normalize("\u00e8\u20ac\uD83D\uDE00\r\n\ta "));
    }

    @Test
    public void normalizeDoesNotCopyATextThatHasNoCarriageReturn() {
        String text = "select 1\nfrom dual\n";
        assertSame(text, LineEnding.normalize(text));
    }

    @Test
    public void toFileGivesTheLinesTheEndingOfTheFile() {
        assertEquals("a\r\nb\r\n\r\nc", LineEnding.WINDOWS.toFile("a\nb\n\nc"));
        assertEquals("a\nb\n\nc", LineEnding.UNIX.toFile("a\nb\n\nc"));
        assertEquals("a\rb\r\rc", LineEnding.MAC.toFile("a\nb\n\nc"));
    }

    @Test
    public void toFileDoesNotDoubleTheLineEndingsThatAreAlreadyThere() {
        assertEquals("a\r\nb", LineEnding.WINDOWS.toFile("a\r\nb"));
        assertEquals("a\r\nb", LineEnding.WINDOWS.toFile("a\rb"));
        assertEquals("a\nb", LineEnding.UNIX.toFile("a\r\nb"));
        assertEquals("a\rb", LineEnding.MAC.toFile("a\r\nb"));
    }

    @Test
    public void aFileWithTheSameLineEndingEverywhereIsWrittenAsItWasRead() {
        Random random = new Random(20261123L);
        String alphabet = "ab \t;\u00e8\u20ac";
        for (LineEnding lineEnding : LineEnding.values()) {
            for (int run = 0; run < 500; run++) {
                StringBuilder file = new StringBuilder();
                int pieces = random.nextInt(12);
                for (int i = 0; i < pieces; i++) {
                    int length = random.nextInt(6);
                    for (int j = 0; j < length; j++) {
                        file.append(alphabet.charAt(random.nextInt(alphabet.length())));
                    }
                    if (random.nextInt(3) > 0) {
                        file.append(lineEnding.getSequence());
                    }
                }
                String original = file.toString();
                LineEnding detected = LineEnding.detect(original);
                if (original.contains(lineEnding.getSequence())) {
                    assertEquals(lineEnding, detected, original);
                } else {
                    assertNull(detected, original);
                }
                LineEnding ending = detected != null ? detected : lineEnding;
                String text = LineEnding.normalize(original);
                assertEquals(original, ending.toFile(text), original);
            }
        }
    }

    @Test
    public void aFileWithMixedLineEndingsIsWrittenWithTheFirstOne() {
        String original = "a\r\nb\nc\rd";
        LineEnding detected = LineEnding.detect(original);
        assertEquals(LineEnding.WINDOWS, detected);
        assertEquals("a\r\nb\r\nc\r\nd", detected.toFile(LineEnding.normalize(original)));
    }

    @Test
    public void theDefaultIsTheLineEndingOfTheFilesOfThePlatform() {
        assertEquals(LineEnding.WINDOWS, LineEnding.getDefault("\r\n"));
        assertEquals(LineEnding.UNIX, LineEnding.getDefault("\n"));
        assertEquals(LineEnding.UNIX, LineEnding.getDefault("\r"));
        assertEquals(LineEnding.getDefault(System.lineSeparator()), LineEnding.getDefault());
    }

    @Test
    public void encodeTextGivesTheLinesTheEndingOfTheFileBeforeEncoding() throws Exception {
        assertArrayEquals("a\r\n\u00e8".getBytes(StandardCharsets.UTF_8),
                FileIO.encodeText("a\n\u00e8", LineEnding.WINDOWS, "UTF-8", false));
        assertArrayEquals("a\n\u00e8".getBytes(StandardCharsets.UTF_8),
                FileIO.encodeText("a\n\u00e8", LineEnding.UNIX, "UTF-8", false));
        assertArrayEquals(new byte[] { 'a', '\r', (byte) 0xE8 },
                FileIO.encodeText("a\n\u00e8", LineEnding.MAC, "ISO-8859-1", false));
        assertArrayEquals(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 'a', '\r', '\n' },
                FileIO.encodeText("a\n", LineEnding.WINDOWS, "UTF-8", true));
        assertArrayEquals(new byte[] { (byte) 0xFF, (byte) 0xFE, 'a', 0, '\r', 0, '\n', 0 },
                FileIO.encodeText("a\n", LineEnding.WINDOWS, "UTF-16LE", true));
    }
}
