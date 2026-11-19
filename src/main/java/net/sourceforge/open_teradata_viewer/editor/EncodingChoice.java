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

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * What the user chooses when a text is saved in a file: an encoding and, for
 * UTF-8, whether the file starts with a byte order mark (BOM). UTF-8 with a BOM
 * and UTF-8 without it are two different choices, as the programs that read the
 * file may need one or the other. The BOM is not a choice for the other
 * encodings: the flavors of UTF-16 and UTF-32 that need one always get it, and
 * the others have none, see {@link TextFileCodec#writesBom(String, boolean)}.
 * <p>
 * Objects of this class are immutable.
 *
 * @author D. Campione
 *
 */
public final class EncodingChoice {

    /** UTF-8, without a BOM. */
    public static final EncodingChoice UTF_8 = new EncodingChoice(TextFileCodec.UTF_8, false);

    /** UTF-8, with a BOM. */
    public static final EncodingChoice UTF_8_WITH_BOM = new EncodingChoice(TextFileCodec.UTF_8, true);

    private final String encoding;
    private final boolean bom;

    private EncodingChoice(String encoding, boolean bom) {
        this.encoding = encoding;
        this.bom = bom;
    }

    /**
     * @param encoding The name of an encoding, or one of its aliases.
     * @param bom <code>true</code> to write the BOM of UTF-8. It does not count
     *        for the other encodings, see the description of this class.
     * @return The choice of that encoding, under the name that
     *         {@link Charset#name()} gives it.
     * @throws java.nio.charset.IllegalCharsetNameException If the name is not
     *         the one of an encoding.
     * @throws java.nio.charset.UnsupportedCharsetException If this Java
     *         platform does not know the encoding.
     */
    public static EncodingChoice of(String encoding, boolean bom) {
        String name = Charset.forName(encoding).name();
        if (TextFileCodec.UTF_8.equals(name)) {
            return bom ? UTF_8_WITH_BOM : UTF_8;
        }
        return new EncodingChoice(name, false);
    }

    /**
     * @return The choices that can be offered to the user, as many as the
     *         encodings that can be used to write a text (see
     *         {@link TextFileCodec#getEncodings()}) and one more: UTF-8 with a
     *         BOM, which comes right after UTF-8 without it.
     */
    public static List<EncodingChoice> getChoices() {
        List<EncodingChoice> choices = new ArrayList<>();
        for (String name : TextFileCodec.getEncodings()) {
            choices.add(of(name, false));
            if (TextFileCodec.UTF_8.equals(name)) {
                choices.add(UTF_8_WITH_BOM);
            }
        }
        return choices;
    }

    /** @return The name of the encoding, as {@link Charset#name()} gives it. */
    public String getEncoding() {
        return encoding;
    }

    /**
     * @return <code>true</code> for UTF-8 with a BOM. It is always
     *         <code>false</code> for the other encodings, also for the ones
     *         whose files have a BOM, see {@link #writesBom()}.
     */
    public boolean hasBom() {
        return bom;
    }

    /**
     * @return <code>true</code> if {@link TextFileCodec#encode(String, String, boolean)}
     *         must be told to write a BOM when it writes a text in this
     *         encoding.
     */
    public boolean writesBom() {
        return TextFileCodec.writesBom(encoding, bom);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EncodingChoice)) {
            return false;
        }
        EncodingChoice other = (EncodingChoice) obj;
        return bom == other.bom && encoding.equals(other.encoding);
    }

    @Override
    public int hashCode() {
        return Objects.hash(encoding, bom);
    }

    /** @return The name of the encoding, followed by " with BOM" if it is the choice with a BOM. */
    @Override
    public String toString() {
        return bom ? encoding + " with BOM" : encoding;
    }
}
