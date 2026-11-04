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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import net.sourceforge.open_teradata_viewer.editor.FileSnapshot;

/**
 *
 *
 * @author D. Campione
 *
 */
public class TestFileSnapshot {

    @TempDir
    File directory;

    private File write(String name, byte[] content, long lastModifiedMillis) throws IOException {
        File file = new File(directory, name);
        Files.write(file.toPath(), content);
        Files.setLastModifiedTime(file.toPath(), FileTime.fromMillis(lastModifiedMillis));
        return file;
    }

    private static byte[] bytes(String text) {
        return text.getBytes(StandardCharsets.UTF_8);
    }

    @Test
    public void test_of_keepsTheGivenDateAndTheSizeOfTheContent() {
        FileTime date = FileTime.fromMillis(1_700_000_000_000L);

        FileSnapshot snapshot = FileSnapshot.of(date, bytes("select 1;"));

        assertEquals(date, snapshot.getLastModified());
        assertEquals(9, snapshot.getSize());
    }

    @Test
    public void test_read_isTheSameAsOfWhenTheContentIsTheSame() throws IOException {
        File file = write("a.sql", bytes("select * from t;"), 1_700_000_000_000L);

        FileSnapshot read = FileSnapshot.read(file);
        FileSnapshot of = FileSnapshot.of(FileSnapshot.lastModifiedOf(file), Files.readAllBytes(file.toPath()));

        assertEquals(of, read);
        assertEquals(of.hashCode(), read.hashCode());
    }

    @Test
    public void test_read_hasTheDateOfTheFile() throws IOException {
        File file = write("a.sql", bytes("select 1;"), 1_600_000_000_000L);

        assertEquals(FileTime.fromMillis(1_600_000_000_000L), FileSnapshot.read(file).getLastModified());
        assertEquals(FileTime.fromMillis(1_600_000_000_000L), FileSnapshot.lastModifiedOf(file));
    }

    @Test
    public void test_read_filesLongerThanTheBuffer() throws IOException {
        // Several times the size of what is read at once, and not a multiple of it
        byte[] content = new byte[8192 * 5 + 123];
        for (int i = 0; i < content.length; i++) {
            content[i] = (byte) (i * 31);
        }
        File file = write("big.bin", content, 1_700_000_000_000L);

        FileSnapshot read = FileSnapshot.read(file);

        assertEquals(content.length, read.getSize());
        assertEquals(FileSnapshot.of(FileTime.fromMillis(1_700_000_000_000L), content), read);
    }

    @Test
    public void test_read_emptyFile() throws IOException {
        File file = write("empty.sql", new byte[0], 1_700_000_000_000L);

        FileSnapshot read = FileSnapshot.read(file);

        assertEquals(0, read.getSize());
        assertEquals(FileSnapshot.of(FileTime.fromMillis(1_700_000_000_000L), new byte[0]), read);
    }

    @Test
    public void test_hasSameContentAs_ignoresTheDate() {
        FileSnapshot first = FileSnapshot.of(FileTime.fromMillis(1_000L), bytes("select 1;"));
        FileSnapshot touched = FileSnapshot.of(FileTime.fromMillis(2_000L), bytes("select 1;"));

        assertTrue(first.hasSameContentAs(touched));
        // The date is part of what a snapshot is
        assertNotEquals(first, touched);
    }

    @Test
    public void test_hasSameContentAs_tellsADifferentContentOfTheSameSize() {
        FileSnapshot first = FileSnapshot.of(FileTime.fromMillis(1_000L), bytes("select 1;"));
        FileSnapshot other = FileSnapshot.of(FileTime.fromMillis(1_000L), bytes("select 2;"));

        assertFalse(first.hasSameContentAs(other));
        assertNotEquals(first, other);
    }

    @Test
    public void test_hasSameContentAs_tellsADifferentSize() {
        FileSnapshot first = FileSnapshot.of(FileTime.fromMillis(1_000L), bytes("select 1;"));
        FileSnapshot longer = FileSnapshot.of(FileTime.fromMillis(1_000L), bytes("select 1; "));

        assertFalse(first.hasSameContentAs(longer));
    }

    @Test
    public void test_hasSameContentAs_null() {
        assertFalse(FileSnapshot.of(FileTime.fromMillis(1_000L), bytes("a")).hasSameContentAs(null));
    }

    @Test
    public void test_equals_sameValuesAreEqual() {
        FileSnapshot first = FileSnapshot.of(FileTime.fromMillis(1_000L), bytes("select 1;"));
        FileSnapshot same = FileSnapshot.of(FileTime.fromMillis(1_000L), bytes("select 1;"));

        assertEquals(first, first);
        assertEquals(first, same);
        assertEquals(first.hashCode(), same.hashCode());
        assertNotEquals(first, null);
        assertNotEquals(first, "select 1;");
    }

    @Test
    public void test_snapshotDoesNotShareTheArrayOfTheContent() {
        byte[] content = bytes("select 1;");
        FileSnapshot snapshot = FileSnapshot.of(FileTime.fromMillis(1_000L), content);

        Arrays.fill(content, (byte) 0);

        // Only the digest is kept: changing the array afterwards changes nothing
        assertTrue(snapshot.hasSameContentAs(FileSnapshot.of(FileTime.fromMillis(1_000L), bytes("select 1;"))));
    }

    @Test
    public void test_read_missingFileFails() {
        File missing = new File(directory, "missing.sql");

        assertThrows(IOException.class, () -> FileSnapshot.read(missing));
        assertThrows(IOException.class, () -> FileSnapshot.lastModifiedOf(missing));
    }

    @Test
    public void test_tryRead_missingFileIsNull() {
        assertNull(FileSnapshot.tryRead(new File(directory, "missing.sql")));
    }

    @Test
    public void test_tryRead_directoryIsNull() {
        assertNull(FileSnapshot.tryRead(directory));
    }

    @Test
    public void test_tryRead_existingFile() throws IOException {
        File file = write("a.sql", bytes("select 1;"), 1_700_000_000_000L);

        assertEquals(FileSnapshot.read(file), FileSnapshot.tryRead(file));
    }
}
