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

package net.sourceforge.open_teradata_viewer.editor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * What a file was like when a document read it or wrote it: when it had been
 * last modified, how many bytes it had and a digest of its content.
 * <p>
 * It is what a document remembers of its file, so that a later look at the file
 * can tell whether it has been changed by someone else since. The date alone
 * would not be reliable enough: a backup tool, an antivirus or a network share
 * can change it without touching the content, and the digest tells these cases
 * apart from a real change.
 * <p>
 * A snapshot is immutable.
 *
 * @author D. Campione
 */
public final class FileSnapshot {

    private static final String DIGEST_ALGORITHM = "SHA-256";

    private final FileTime lastModified;
    private final long size;
    private final byte[] digest;

    private FileSnapshot(FileTime lastModified, long size, byte[] digest) {
        this.lastModified = lastModified;
        this.size = size;
        this.digest = digest;
    }

    /**
     * Gets the date a file was last modified. When the content of a file is
     * going to be read, ask for it <em>before</em> reading, and give it to
     * {@link #of(FileTime, byte[])} with the content: if the file is changed
     * while it is being read, the date is older than the content, so the
     * change is noticed by the next look at the file, instead of never.
     *
     * @param file The file.
     * @return The date the file was last modified.
     * @throws IOException If the file does not exist or cannot be accessed.
     */
    public static FileTime lastModifiedOf(File file) throws IOException {
        return Files.getLastModifiedTime(file.toPath());
    }

    /**
     * Describes a file whose content has been read already.
     *
     * @param lastModifiedBeforeReading The date the file was last modified, got
     *        before its content was read, see {@link #lastModifiedOf(File)}.
     * @param content The content of the file, as read.
     * @return The snapshot of the file.
     */
    public static FileSnapshot of(FileTime lastModifiedBeforeReading, byte[] content) {
        MessageDigest messageDigest = newDigest();
        return new FileSnapshot(lastModifiedBeforeReading, content.length, messageDigest.digest(content));
    }

    /**
     * Reads a file to describe it. The content is not kept, only its digest.
     *
     * @param file The file.
     * @return The snapshot of the file.
     * @throws IOException If the file does not exist or cannot be read.
     */
    public static FileSnapshot read(File file) throws IOException {
        Path path = file.toPath();
        // The date comes first, see lastModifiedOf(File)
        FileTime lastModified = Files.getLastModifiedTime(path);
        MessageDigest messageDigest = newDigest();
        long length = 0;
        try (InputStream in = Files.newInputStream(path)) {
            byte[] buffer = new byte[8192];
            int count;
            while ((count = in.read(buffer)) != -1) {
                messageDigest.update(buffer, 0, count);
                length += count;
            }
        }
        return new FileSnapshot(lastModified, length, messageDigest.digest());
    }

    /**
     * Like {@link #read(File)}, but does not report a failure: whoever calls it
     * can live with not knowing what the file is like.
     *
     * @param file The file.
     * @return The snapshot of the file, or <code>null</code> if the file cannot
     *         be read.
     */
    public static FileSnapshot tryRead(File file) {
        try {
            return read(file);
        } catch (IOException | RuntimeException e) {
            return null;
        }
    }

    private static MessageDigest newDigest() {
        try {
            return MessageDigest.getInstance(DIGEST_ALGORITHM);
        } catch (NoSuchAlgorithmException nsae) {
            // Every Java platform has to provide it
            throw new IllegalStateException(nsae);
        }
    }

    /** @return The date the file was last modified. */
    public FileTime getLastModified() {
        return lastModified;
    }

    /** @return The number of bytes of the file. */
    public long getSize() {
        return size;
    }

    /**
     * @param other Another snapshot.
     * @return <code>true</code> if the other snapshot has the same content as
     *         this one, whatever the date.
     */
    public boolean hasSameContentAs(FileSnapshot other) {
        return other != null && size == other.size && Arrays.equals(digest, other.digest);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FileSnapshot)) {
            return false;
        }
        FileSnapshot other = (FileSnapshot) obj;
        return lastModified.equals(other.lastModified) && hasSameContentAs(other);
    }

    @Override
    public int hashCode() {
        return 31 * (31 * lastModified.hashCode() + Long.hashCode(size)) + Arrays.hashCode(digest);
    }

    @Override
    public String toString() {
        return "FileSnapshot[lastModified=" + lastModified + ", size=" + size + "]";
    }
}
