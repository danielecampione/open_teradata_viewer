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

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Turns the bytes of a text file into text and back, in the way the editors of
 * text files usually do: a byte order mark (BOM) tells the encoding without
 * doubt; otherwise the encoding is a given one or, if none is given, it is
 * guessed. Nothing is ever replaced by a substitute character: a file that is
 * read and written back is the same file, and text that cannot be represented
 * in an encoding is reported instead of being written with question marks.
 *
 * @author D. Campione
 *
 */
public final class TextFileCodec {

    public static final String UTF_8 = "UTF-8";

    private static final String ISO_8859_1 = "ISO-8859-1";

    private static final byte[] NO_BYTES = new byte[0];

    private TextFileCodec() {
    }

    /** The text of a file, with how it has been encoded. */
    public static final class Decoded {

        private final String text;
        private final String encoding;
        private final boolean bom;

        private Decoded(String text, String encoding, boolean bom) {
            this.text = text;
            this.encoding = encoding;
            this.bom = bom;
        }

        /** @return The text, without the BOM. */
        public String getText() {
            return text;
        }

        /** @return The name of the encoding, as {@link Charset#name()} gives it. */
        public String getEncoding() {
            return encoding;
        }

        /** @return <code>true</code> if the file started with a BOM. */
        public boolean hasBom() {
            return bom;
        }
    }

    /**
     * @return The names of the encodings that can be used to read and to write
     *         a file, sorted.
     */
    public static List<String> getEncodings() {
        List<String> names = new ArrayList<>();
        for (Charset charset : Charset.availableCharsets().values()) {
            if (charset.canEncode()) {
                names.add(charset.name());
            }
        }
        Collections.sort(names, String.CASE_INSENSITIVE_ORDER);
        return names;
    }

    /**
     * @return The name of the encoding of the platform, which is the one of the
     *         files that are not Unicode and that other programs of the same
     *         computer write.
     */
    public static String getDefaultEncoding() {
        return Charset.defaultCharset().name();
    }

    /**
     * Reads the text of a file, guessing its encoding: the one of the BOM, if
     * there is one; otherwise UTF-8, if the bytes are valid UTF-8 (this is the
     * case of every file that holds only ASCII characters, too); otherwise the
     * encoding of the platform, if the bytes are valid in it; otherwise
     * ISO-8859-1, which accepts every byte. So the text is always read without
     * losing anything.
     *
     * @param bytes The content of the file.
     * @return The text, and how it was encoded.
     */
    public static Decoded decode(byte[] bytes) {
        return decode(bytes, Charset.defaultCharset());
    }

    /**
     * Like {@link #decode(byte[])}, but the encoding that is tried after UTF-8
     * is the given one instead of the one of the platform.
     *
     * @param bytes The content of the file.
     * @param platformCharset The encoding the non Unicode files are expected to
     *        be in.
     * @return The text, and how it was encoded.
     */
    public static Decoded decode(byte[] bytes, Charset platformCharset) {
        Decoded decoded = decodeWithBom(bytes);
        if (decoded != null) {
            return decoded;
        }
        for (String name : new String[] { UTF_8, platformCharset.name(), ISO_8859_1 }) {
            try {
                return decode(bytes, 0, Charset.forName(name), false);
            } catch (CharacterCodingException cce) {
                // The next one
            }
        }
        // ISO-8859-1 accepts every byte
        throw new IllegalStateException();
    }

    /**
     * Reads the text of a file in a given encoding, which is used only if the
     * file does not start with a BOM: the BOM tells the encoding without doubt.
     *
     * @param bytes The content of the file.
     * @param encoding The name of the encoding.
     * @return The text, and how it was encoded.
     * @throws CharacterCodingException If the file is not valid in the
     *         encoding. Nothing is replaced with a substitute character, as it
     *         would be lost writing the file back.
     */
    public static Decoded decode(byte[] bytes, String encoding) throws CharacterCodingException {
        Decoded decoded = decodeWithBom(bytes);
        if (decoded != null) {
            return decoded;
        }
        return decode(bytes, 0, Charset.forName(encoding), false);
    }

    /**
     * Reads again the text of a file that has already been read: the encoding
     * it had is kept, unless the file now starts with a BOM, or it is not valid
     * in that encoding anymore (then it is guessed, see
     * {@link #decode(byte[])}).
     *
     * @param bytes The content of the file.
     * @param encoding The encoding the file had.
     * @return The text, and how it was encoded.
     */
    public static Decoded decodeAgain(byte[] bytes, String encoding) {
        try {
            return decode(bytes, encoding);
        } catch (CharacterCodingException cce) {
            return decode(bytes);
        }
    }

    private static Decoded decodeWithBom(byte[] b) {
        String name = null;
        int length = 0;
        if (b.length >= 4 && b[0] == 0 && b[1] == 0 && b[2] == (byte) 0xFE && b[3] == (byte) 0xFF) {
            name = "UTF-32BE";
            length = 4;
        } else if (b.length >= 4 && b[0] == (byte) 0xFF && b[1] == (byte) 0xFE && b[2] == 0 && b[3] == 0) {
            name = "UTF-32LE";
            length = 4;
        } else if (b.length >= 3 && b[0] == (byte) 0xEF && b[1] == (byte) 0xBB && b[2] == (byte) 0xBF) {
            name = UTF_8;
            length = 3;
        } else if (b.length >= 2 && b[0] == (byte) 0xFE && b[1] == (byte) 0xFF) {
            name = "UTF-16BE";
            length = 2;
        } else if (b.length >= 2 && b[0] == (byte) 0xFF && b[1] == (byte) 0xFE) {
            name = "UTF-16LE";
            length = 2;
        }
        if (name == null) {
            return null;
        }
        try {
            return decode(b, length, Charset.forName(name), true);
        } catch (CharacterCodingException cce) {
            // A BOM followed by something that is not valid: it was not a BOM
            return null;
        }
    }

    private static Decoded decode(byte[] bytes, int offset, Charset charset, boolean bom)
            throws CharacterCodingException {
        CharsetDecoder decoder = charset.newDecoder().onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT);
        String text = decoder.decode(ByteBuffer.wrap(bytes, offset, bytes.length - offset)).toString();
        return new Decoded(text, charset.name(), bom);
    }

    /**
     * @param encoding The name of an encoding.
     * @param hadBom <code>true</code> if the file the text comes from started
     *        with a BOM.
     * @return <code>true</code> if {@link #encode(String, String, boolean)}
     *         must be told to write a BOM: the one the file had, if it is
     *         UTF-8; always, for the other flavors of Unicode that have one,
     *         as an editor that does not write it makes the file unreadable
     *         to the programs that expect it.
     */
    public static boolean writesBom(String encoding, boolean hadBom) {
        switch (Charset.forName(encoding).name()) {
            case UTF_8:
                return hadBom;
            case "UTF-16LE":
            case "UTF-16BE":
            case "UTF-32":
            case "UTF-32LE":
            case "UTF-32BE":
                return true;
            default:
                // "UTF-16" is written with its BOM by the encoder itself
                return false;
        }
    }

    /**
     * Turns some text into the bytes of a file.
     *
     * @param text The text.
     * @param encoding The name of the encoding.
     * @param bom <code>true</code> to write the BOM of the encoding first, if
     *        it has one.
     * @return The bytes.
     * @throws CharacterCodingException If the text has characters that cannot
     *         be represented in the encoding: no <code>?</code> is written in
     *         their place.
     */
    public static byte[] encode(String text, String encoding, boolean bom) throws CharacterCodingException {
        Charset charset = Charset.forName(encoding);
        CharsetEncoder encoder = charset.newEncoder().onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT);
        ByteBuffer buffer = encoder.encode(CharBuffer.wrap(text));
        byte[] body = new byte[buffer.remaining()];
        buffer.get(body);
        byte[] prefix = bom ? bomOf(charset.name()) : NO_BYTES;
        if (prefix.length == 0) {
            return body;
        }
        byte[] bytes = new byte[prefix.length + body.length];
        System.arraycopy(prefix, 0, bytes, 0, prefix.length);
        System.arraycopy(body, 0, bytes, prefix.length, body.length);
        return bytes;
    }

    private static byte[] bomOf(String name) {
        switch (name) {
            case UTF_8:
                return new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };
            case "UTF-16LE":
                return new byte[] { (byte) 0xFF, (byte) 0xFE };
            case "UTF-16BE":
                return new byte[] { (byte) 0xFE, (byte) 0xFF };
            case "UTF-32LE":
                return new byte[] { (byte) 0xFF, (byte) 0xFE, 0, 0 };
            case "UTF-32":
            case "UTF-32BE":
                return new byte[] { 0, 0, (byte) 0xFE, (byte) 0xFF };
            default:
                return NO_BYTES;
        }
    }
}
