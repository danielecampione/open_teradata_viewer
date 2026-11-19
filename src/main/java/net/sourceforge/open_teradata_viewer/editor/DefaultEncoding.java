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
import java.util.LinkedHashMap;
import java.util.Map;

import net.sourceforge.open_teradata_viewer.Config;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * The encoding that the text of a new file is written in, and whether it starts
 * with a BOM (see {@link EncodingChoice}): the ones a tab starts with, and the
 * ones the "Save file as..." dialog suggests for it, until the user chooses
 * others there. It is a setting of the user, which is kept in the configuration
 * file, and it is UTF-8 without a BOM until the user changes it: it does not
 * follow the platform, so that the files written on different computers are in
 * the same encoding. A file that is opened keeps the encoding and the BOM it
 * has, see {@link TextFileCodec}, whatever this setting is.
 *
 * @author D. Campione
 *
 */
public final class DefaultEncoding {

    /** The name of the setting of the encoding in the configuration file. */
    public static final String SETTING = "default_encoding";

    /**
     * The name of the setting of the BOM in the configuration file: its value
     * is <code>true</code> or <code>false</code>, and it is missing in the
     * files written before the BOM could be chosen.
     */
    public static final String BOM_SETTING = "default_encoding_bom";

    private static volatile EncodingChoice choice = EncodingChoice.UTF_8;

    private DefaultEncoding() {
    }

    /**
     * @return The name of the encoding, as {@link Charset#name()} gives it.
     */
    public static String get() {
        return choice.getEncoding();
    }

    /**
     * @return <code>true</code> if a new file starts with a BOM (which can only
     *         be, if its encoding is UTF-8).
     */
    public static boolean hasBom() {
        return choice.hasBom();
    }

    /**
     * @return The encoding and the BOM, read together: they are always the
     *         ones that were chosen together, which two calls to
     *         {@link #get()} and {@link #hasBom()} do not tell if the choice is
     *         changed in between.
     */
    public static EncodingChoice getChoice() {
        return choice;
    }

    /**
     * Changes the encoding and the BOM that are in use, without keeping them
     * for the next time the application is started, see
     * {@link #save(EncodingChoice)}.
     *
     * @param value What new files are saved in, which is replaced by UTF-8
     *        without a BOM if its encoding cannot be used, see
     *        {@link #normalizeChoice(EncodingChoice)}.
     */
    public static void set(EncodingChoice value) {
        choice = normalizeChoice(value);
    }

    /**
     * Tells which encoding a name stands for.
     *
     * @param name The name of an encoding, or one of its aliases (for example
     *        <code>utf8</code>), or <code>null</code>.
     * @return The name of the encoding as {@link Charset#name()} gives it, or
     *         UTF-8 if the name is empty, is not known by this Java platform, or
     *         is the name of an encoding that cannot be used to write a text.
     */
    public static String normalize(String name) {
        if (name == null) {
            return TextFileCodec.UTF_8;
        }
        try {
            Charset charset = Charset.forName(name.trim());
            return charset.canEncode() ? charset.name() : TextFileCodec.UTF_8;
        } catch (IllegalArgumentException iae) {
            // An empty name, or one that no encoding has
            return TextFileCodec.UTF_8;
        }
    }

    /**
     * @param value What new files could be saved in, or <code>null</code>.
     * @return <code>value</code> itself, or UTF-8 without a BOM if it is
     *         <code>null</code> or if its encoding cannot be used to write a
     *         text, see {@link #normalize(String)}.
     */
    public static EncodingChoice normalizeChoice(EncodingChoice value) {
        if (value == null) {
            return EncodingChoice.UTF_8;
        }
        return value.getEncoding().equals(normalize(value.getEncoding())) ? value : EncodingChoice.UTF_8;
    }

    /**
     * Takes the encoding and the BOM from the configuration file. They stay
     * UTF-8 without a BOM if the user has not chosen others, or if the file
     * cannot be read. A file written before the BOM could be chosen has no BOM
     * setting, and gives no BOM.
     */
    public static void load() {
        try {
            String name = normalize(Config.getSetting(SETTING));
            set(EncodingChoice.of(name, Boolean.parseBoolean(Config.getSetting(BOM_SETTING))));
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
        }
    }

    /**
     * Changes the encoding and the BOM that are in use and keeps them in the
     * configuration file, both in one write. Nothing is changed if the file
     * cannot be written.
     *
     * @param value What new files are saved in, which is replaced by UTF-8
     *        without a BOM if its encoding cannot be used, see
     *        {@link #normalizeChoice(EncodingChoice)}.
     * @throws Exception If the configuration file cannot be written.
     */
    public static void save(EncodingChoice value) throws Exception {
        EncodingChoice normalized = normalizeChoice(value);
        Map<String, String> settings = new LinkedHashMap<>();
        settings.put(SETTING, normalized.getEncoding());
        settings.put(BOM_SETTING, String.valueOf(normalized.hasBom()));
        Config.saveSettings(settings);
        choice = normalized;
    }
}
