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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.sourceforge.open_teradata_viewer.DocumentState;
import net.sourceforge.open_teradata_viewer.editor.DefaultEncoding;
import net.sourceforge.open_teradata_viewer.editor.TextFileCodec;

/**
 *
 *
 * @author D. Campione
 *
 */
public class TestDefaultEncoding {

    @BeforeEach
    @AfterEach
    public void restoreTheDefault() {
        // The encoding in use is shared by the whole application
        DefaultEncoding.set(TextFileCodec.UTF_8);
    }

    @Test
    public void itIsUtf8UntilTheUserChoosesAnother() {
        assertEquals("UTF-8", DefaultEncoding.get());
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
        DefaultEncoding.set("windows-1252");
        assertEquals("windows-1252", DefaultEncoding.get());
        DefaultEncoding.set("utf8");
        assertEquals("UTF-8", DefaultEncoding.get());
        DefaultEncoding.set("windows-1252");
        DefaultEncoding.set("no-such-encoding");
        assertEquals("UTF-8", DefaultEncoding.get());
    }

    @Test
    public void aNewDocumentStateStartsWithTheEncodingInUse() {
        DocumentState before = new DocumentState();
        DefaultEncoding.set("ISO-8859-15");
        DocumentState after = new DocumentState();
        assertEquals("ISO-8859-15", after.getEncoding());
        // What has been created before does not change on its own
        assertEquals("UTF-8", before.getEncoding());
        DefaultEncoding.set("UTF-8");
        assertEquals("UTF-8", new DocumentState().getEncoding());
        assertEquals("ISO-8859-15", after.getEncoding());
    }
}
