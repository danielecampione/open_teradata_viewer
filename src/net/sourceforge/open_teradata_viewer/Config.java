/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.PrivateKey;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;

import net.sourceforge.open_teradata_viewer.security.CredentialManager;
import net.sourceforge.open_teradata_viewer.util.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import net.sourceforge.open_teradata_viewer.util.Utilities;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public final class Config {

    private static String version;
    private static final String PrivateKey = "$GeHeiM^"; // Legacy key for migration only
    private static final Logger logger = Logger.getInstance();
    private static final CredentialManager credentialManager = CredentialManager.getInstance();

    public static final String HOME_PAGE = "https://openteradata.sourceforge.net/";
    public static final String SOURCEFORGE_MIRROR = "https://sourceforge.net/projects/openteradata/";

    /** Home page of the project's official GitHub repository. */
    public static final String GITHUB_REPO_URL = "https://github.com/danielecampione/open_teradata_viewer/";

    /** GitHub REST API endpoint returning the most recent published release. */
    public static final String GITHUB_LATEST_RELEASE_API_URL = "https://api.github.com/repos/danielecampione/open_teradata_viewer/releases/latest";

    private Config() {
    }

    private static final Key KEY = new PrivateKey() {

        private static final long serialVersionUID = 3588352945486799581L;

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

    public static String getVersion() throws IOException {
        if (version == null) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(Config.class.getResourceAsStream("/changes.txt")))) {
                version = reader.readLine();
            }
        }
        return version;
    }

    /**
     * Used by plug-ins.
     */
    public static String getSetting(String name) throws Exception {
        Element config = getConfig();
        NodeList list = config.getElementsByTagName("settings");
        if (list.getLength() > 0) {
            Element settings = (Element) list.item(0);
            String value = settings.getAttribute(name);
            return value;
        }
        return null;
    }

    public static String getDrivers() throws Exception {
        return getSetting("drivers");
    }

    public static void saveDrivers(String drivers) throws Exception {
        saveSetting("drivers", drivers);
    }

    public static Vector<ConnectionData> getDatabases() throws Exception {
        Element config = getConfig();
        return getDatabases(config);
    }

    public static Vector<ConnectionData> getDatabases(Element config) throws Exception {
        NodeList nodeList = config.getElementsByTagName("database");
        return java.util.stream.IntStream.range(0, nodeList.getLength())
                .mapToObj(i -> (Element) nodeList.item(i))
                .map(element -> {
                    try {
                        ConnectionData connectionData = new ConnectionData(element.getAttribute("name"),
                                element.getAttribute("connection"), element.getAttribute("user"),
                                Config.decrypt(element.getAttribute("password")),
                                element.getAttribute("defaultOwner"));
                        String databaseTypeAttribute = element.getAttribute("databaseType");
                        ConnectionData.DatabaseType databaseType;
                        if (databaseTypeAttribute != null && databaseTypeAttribute.trim().length() > 0) {
                            databaseType = ConnectionData.DatabaseType.valueOf(databaseTypeAttribute);
                        } else {
                            // Connection saved before the databaseType attribute existed:
                            // fall back to guessing it from the JDBC URL
                            databaseType = ConnectionData.inferDatabaseTypeFromUrl(connectionData.getUrl());
                        }
                        connectionData.setDatabaseType(databaseType);
                        return connectionData;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(java.util.stream.Collectors.toCollection(Vector::new));
    }

    public static void saveDatabases(List<ConnectionData> connectionDatas) throws Exception {
        Collections.sort(connectionDatas);
        Element config = getConfig();
        NodeList nodeList = config.getElementsByTagName("database");
        for (int i = nodeList.getLength() - 1; i > -1; i--) {
            config.removeChild(nodeList.item(i));
        }
        connectionDatas.stream().forEach(connectionData -> {
            try {
                Element element = config.getOwnerDocument().createElement("database");
                element.setAttribute("name", connectionData.getName());
                element.setAttribute("user", connectionData.getUser());
                element.setAttribute("password", Config.encrypt(connectionData.getPassword()));
                element.setAttribute("connection", connectionData.getUrl());
                element.setAttribute("defaultOwner", connectionData.getDefaultOwner());
                element.setAttribute("databaseType", connectionData.getDatabaseType().name());
                config.appendChild(element);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        Config.saveConfig(config);
    }

    public static Map<String, String> getFavorites() throws ParserConfigurationException, IOException, SAXException {
        Element config = getConfig();
        NodeList favorites = config.getElementsByTagName("favorite");
        Map<String, String> map = new TreeMap<>();
        for (int i = 0; i < favorites.getLength(); i++) {
            Element favorite = (Element) favorites.item(i);
            map.put(favorite.getAttribute("name"), favorite.getAttribute("query"));
        }
        return map;
    }

    public static void saveFavorites(Map<?, ?> favorites)
            throws ParserConfigurationException, IOException, TransformerException, SAXException {
        Element config = getConfig();
        NodeList nodeList = config.getElementsByTagName("favorite");
        for (int i = nodeList.getLength() - 1; i > -1; i--) {
            config.removeChild(nodeList.item(i));
        }
        for (Object o : favorites.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            Element favorite = config.getOwnerDocument().createElement("favorite");
            favorite.setAttribute("name", (String) entry.getKey());
            favorite.setAttribute("query", (String) entry.getValue());
            config.appendChild(favorite);
        }
        Config.saveConfig(config);
    }

    /**
     * Used by plug-ins.
     */
    public static void saveSetting(String name, String value) throws Exception {
        Element config = getConfig();
        NodeList list = config.getElementsByTagName("settings");
        if (list.getLength() > 0) {
            Element settings = (Element) list.item(0);
            String currentValue = settings.getAttribute(name);
            if (currentValue.equals(value)) {
                return;
            }
            settings.setAttribute(name, value);
        } else {
            Element settings = config.getOwnerDocument().createElement("settings");
            settings.setAttribute(name, value);
            config.appendChild(settings);
        }
        Config.saveConfig(config);
    }

    protected static Element getConfig() throws ParserConfigurationException, IOException, SAXException {
        Element config;
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        try {
            try (InputStream inputStream = new FileInputStream(
                    new File(Utilities.normalizePath(System.getProperty("user.home")), "open_teradata_viewer.xml"))) {
                config = documentBuilder.parse(inputStream).getDocumentElement();
            }
        } catch (Exception e) {
            try (InputStream inputStream = new ByteArrayInputStream("<config/>".getBytes())) {
                config = documentBuilder.parse(inputStream).getDocumentElement();
            }
        }
        return config;
    }

    protected static void saveConfig(Element config) throws TransformerException {
        // remove whitespace
        NodeList childNodes = config.getChildNodes();
        for (int i = childNodes.getLength() - 1; i > -1; i--) {
            if (childNodes.item(i).getNodeType() == Node.TEXT_NODE) {
                config.removeChild(childNodes.item(i));
            }
        }
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(config), new StreamResult(new ByteArrayOutputStream())); // test first
        transformer.transform(new DOMSource(config), new StreamResult(
                new File(Utilities.normalizePath(System.getProperty("user.home")), "open_teradata_viewer.xml")));
    }

    protected static String decrypt(String encrypted) throws GeneralSecurityException {
        if (encrypted == null || "".equals(encrypted)) {
            return encrypted;
        }
        try {
            // First try new AES decryption
            return credentialManager.decrypt(encrypted);
        } catch (Exception e) {
            logger.debug("AES decryption failed, attempting legacy DES migration", e);
            try {
                // Try legacy DES decryption and migrate
                String migrated = credentialManager.migrateFromLegacyEncryption(encrypted);
                logger.info("Successfully migrated legacy encrypted password to AES");
                return credentialManager.decrypt(migrated);
            } catch (Exception legacyException) {
                logger.error("Failed to decrypt string with both AES and legacy DES", legacyException);
                throw new GeneralSecurityException("Decryption failed", legacyException);
            }
        }
    }

    protected static String encrypt(String decrypted) throws GeneralSecurityException {
        if (decrypted == null || "".equals(decrypted)) {
            return decrypted;
        }
        try {
            return credentialManager.encrypt(decrypted);
        } catch (Exception e) {
            logger.error("Failed to encrypt string", e);
            throw new GeneralSecurityException("Encryption failed", e);
        }
    }

    public static void saveLastUsedDir(String dir) throws Exception {
        saveSetting("dir", dir);
    }

    public static String getLastUsedDir() throws Exception {
        return getSetting("dir");
    }
}
