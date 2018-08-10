/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2011, D. Campione
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

package net.sourceforge.open_teradata_viewer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.PrivateKey;
import java.util.Properties;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * This class provides a utility for reading and writing settings.
 * 
 * @author D. Campione
 */
public class Settings {
    /**
     * The file where the settings are saved.
     */
    private static final String SETTINGS_FILE = Tools.conformizePath(System
            .getProperty("user.home")) + ".OpenTeradataViewer";

    public static String load(String key, String def) {
        return load(key, def, false);
    }

    /** Loads a setting from the file.
     * @param key The key for the setting.
     * @param def The default setting.
     * @return The setting, or the default.
     */
    public static String load(String key, String def, boolean isEncrypted) {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(SETTINGS_FILE));
            String value = prop.getProperty(key);

            if (value != null) {
                return isEncrypted ? decrypt(value) : value;
            }
        } catch (Exception e) {

        }

        return def;
    }

    public static void write(String key, String value) throws SettingsException {
        write(key, value, false);
    }

    /** Saves a setting to the file. 
     * @param key The setting key.
     * @param value The setting value.
     * @throws SettingsException If there is an error.
     */
    public static void write(String key, String value, boolean encrypt)
            throws SettingsException {
        value = value.replaceAll("\\\\", "\\\\");

        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream(SETTINGS_FILE));

            prop.setProperty(key, encrypt ? encrypt(value) : value);

            prop.store(new FileOutputStream(SETTINGS_FILE),
                    "Settings for \"Open Teradata Viewer\".");
        } catch (Exception e) {
            try {
                FileWriter out = new FileWriter(SETTINGS_FILE);
                out.write(key + "=" + value);
                out.close();
            } catch (IOException ioe) {
                throw new SettingsException("The setting could not be saved.",
                        ioe);
            }
        }
    }

    protected static String decrypt(String encrypted)
            throws GeneralSecurityException, IOException {
        if ((encrypted == null) || "".equals(encrypted)) {
            return encrypted;
        }
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, KEY);
        return new String(cipher.doFinal(new BASE64Decoder()
                .decodeBuffer(encrypted)));
    }

    protected static String encrypt(String decrypted)
            throws GeneralSecurityException {
        if ((decrypted == null) || "".equals(decrypted)) {
            return decrypted;
        }
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, KEY);
        return new BASE64Encoder().encode(cipher.doFinal(decrypted.getBytes()));
    }

    private static final Key KEY = new PrivateKey() {

        private static final long serialVersionUID = -5152973286706937113L;

        @Override
        public byte[] getEncoded() {
            return "$GeHeiM^".getBytes();
        }

        @Override
        public String getAlgorithm() {
            return "DES";
        }

        @Override
        public String getFormat() {
            return "RAW";
        }
    };
}
