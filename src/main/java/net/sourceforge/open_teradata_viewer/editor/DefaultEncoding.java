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

import net.sourceforge.open_teradata_viewer.Config;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * The encoding that the text of a new file is written in: the one a tab starts
 * with, and the one the "Save file as..." dialog suggests for it, until the user
 * chooses another one there. It is a setting of the user, which is kept in the
 * configuration file, and it is UTF-8 until the user changes it: it does not
 * follow the platform, so that the files written on different computers are in
 * the same encoding. A file that is opened keeps the encoding it is in, see
 * {@link TextFileCodec}, whatever this setting is.
 *
 * @author D. Campione
 *
 */
public final class DefaultEncoding {

    /** The name of the setting in the configuration file. */
    public static final String SETTING = "default_encoding";

    private static volatile String encoding = TextFileCodec.UTF_8;

    private DefaultEncoding() {
    }

    /**
     * @return The name of the encoding, as {@link Charset#name()} gives it.
     */
    public static String get() {
        return encoding;
    }

    /**
     * Changes the encoding that is in use, without keeping it for the next time
     * the application is started, see {@link #save(String)}.
     *
     * @param name The name of an encoding, which is replaced by UTF-8 if it
     *        cannot be used, see {@link #normalize(String)}.
     */
    public static void set(String name) {
        encoding = normalize(name);
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
     * Takes the encoding from the configuration file. It stays UTF-8 if the
     * user has not chosen another one, or if the file cannot be read.
     */
    public static void load() {
        try {
            set(Config.getSetting(SETTING));
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
        }
    }

    /**
     * Changes the encoding that is in use and keeps it in the configuration
     * file. Nothing is changed if the file cannot be written.
     *
     * @param name The name of an encoding, which is replaced by UTF-8 if it
     *        cannot be used, see {@link #normalize(String)}.
     * @throws Exception If the configuration file cannot be written.
     */
    public static void save(String name) throws Exception {
        String normalized = normalize(name);
        Config.saveSetting(SETTING, normalized);
        encoding = normalized;
    }
}
