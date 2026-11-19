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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.sourceforge.open_teradata_viewer.editor.EncodingChoice;
import net.sourceforge.open_teradata_viewer.editor.TextFileCodec;

/**
 *
 *
 * @author D. Campione
 *
 */
public class TestEncodingChoice {

    @Test
    public void utf8WithAndWithoutBomAreTwoChoices() {
        assertEquals("UTF-8", EncodingChoice.UTF_8.getEncoding());
        assertEquals("UTF-8", EncodingChoice.UTF_8_WITH_BOM.getEncoding());
        assertFalse(EncodingChoice.UTF_8.hasBom());
        assertTrue(EncodingChoice.UTF_8_WITH_BOM.hasBom());
        assertNotEquals(EncodingChoice.UTF_8, EncodingChoice.UTF_8_WITH_BOM);
    }

    @Test
    public void ofGivesTheNameOfTheEncodingAndKeepsTheBomOfUtf8() {
        assertSame(EncodingChoice.UTF_8, EncodingChoice.of("UTF-8", false));
        assertSame(EncodingChoice.UTF_8_WITH_BOM, EncodingChoice.of("UTF-8", true));
        assertSame(EncodingChoice.UTF_8, EncodingChoice.of("utf8", false));
        assertSame(EncodingChoice.UTF_8_WITH_BOM, EncodingChoice.of("utf8", true));
        assertEquals("windows-1252", EncodingChoice.of("cp1252", false).getEncoding());
        assertEquals("ISO-8859-1", EncodingChoice.of("latin1", false).getEncoding());
    }

    @Test
    public void theBomIsNotAChoiceForTheOtherEncodings() {
        for (String name : TextFileCodec.getEncodings()) {
            if (name.equals("UTF-8")) {
                continue;
            }
            EncodingChoice choice = EncodingChoice.of(name, true);
            assertFalse(choice.hasBom(), name);
            // The same choice, with or without the flag
            assertEquals(EncodingChoice.of(name, false), choice, name);
            assertEquals(EncodingChoice.of(name, false).hashCode(), choice.hashCode(), name);
        }
    }

    @Test
    public void aNameThatIsNotAnEncodingIsRefused() {
        assertThrows(IllegalCharsetNameException.class, () -> EncodingChoice.of("not a name!", false));
        assertThrows(UnsupportedCharsetException.class, () -> EncodingChoice.of("no-such-encoding", false));
    }

    @Test
    public void whetherABomIsWrittenFollowsTheRulesOfTheCodec() {
        assertFalse(EncodingChoice.UTF_8.writesBom());
        assertTrue(EncodingChoice.UTF_8_WITH_BOM.writesBom());
        // The BOM of these cannot be left out
        for (String name : new String[] { "UTF-16LE", "UTF-16BE", "UTF-32LE", "UTF-32BE", "UTF-32" }) {
            assertTrue(EncodingChoice.of(name, false).writesBom(), name);
            assertTrue(EncodingChoice.of(name, true).writesBom(), name);
        }
        // The encoder writes its BOM itself
        assertFalse(EncodingChoice.of("UTF-16", false).writesBom());
        assertFalse(EncodingChoice.of("windows-1252", false).writesBom());
        assertFalse(EncodingChoice.of("ISO-8859-1", true).writesBom());
    }

    @Test
    public void theBytesThatAreWrittenFollowTheChoice() throws Exception {
        byte[] without = TextFileCodec.encode("select 1", EncodingChoice.UTF_8.getEncoding(),
                EncodingChoice.UTF_8.writesBom());
        byte[] with = TextFileCodec.encode("select 1", EncodingChoice.UTF_8_WITH_BOM.getEncoding(),
                EncodingChoice.UTF_8_WITH_BOM.writesBom());
        assertEquals(8, without.length);
        assertEquals(11, with.length);
        assertEquals((byte) 0xEF, with[0]);
        assertEquals((byte) 0xBB, with[1]);
        assertEquals((byte) 0xBF, with[2]);
    }

    @Test
    public void theChoicesAreTheEncodingsAndUtf8WithABom() {
        List<String> encodings = TextFileCodec.getEncodings();
        List<EncodingChoice> choices = EncodingChoice.getChoices();
        assertEquals(encodings.size() + 1, choices.size());
        Set<EncodingChoice> distinct = new HashSet<>(choices);
        assertEquals(choices.size(), distinct.size());

        // UTF-8 with a BOM comes right after UTF-8 without it
        int utf8 = choices.indexOf(EncodingChoice.UTF_8);
        assertTrue(utf8 >= 0);
        assertEquals(EncodingChoice.UTF_8_WITH_BOM, choices.get(utf8 + 1));

        // Without that one, they are the encodings, in the same order
        int next = 0;
        for (EncodingChoice choice : choices) {
            if (choice.equals(EncodingChoice.UTF_8_WITH_BOM)) {
                continue;
            }
            assertEquals(encodings.get(next++), choice.getEncoding());
            assertFalse(choice.hasBom());
        }
        assertEquals(encodings.size(), next);
    }

    @Test
    public void theTextOfAChoiceIsItsEncodingAndTheBom() {
        assertEquals("UTF-8", EncodingChoice.UTF_8.toString());
        assertEquals("UTF-8 with BOM", EncodingChoice.UTF_8_WITH_BOM.toString());
        assertEquals("windows-1252", EncodingChoice.of("windows-1252", false).toString());
    }
}
