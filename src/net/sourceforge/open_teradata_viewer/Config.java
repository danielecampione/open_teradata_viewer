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

import javax.crypto.Cipher;
import javax.xml.parsers.DocumentBuilder;
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

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public final class Config {

    private static String version;

    public static final String HOME_PAGE = "http://openteradata.sourceforge.net";

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
            version = new BufferedReader(new InputStreamReader(
                    Config.class.getResourceAsStream("/changes.txt")))
                    .readLine();
        }
        return version;
    }

    private static String getSetting(String name) throws Exception {
        Element config = getConfig();
        NodeList list = config.getElementsByTagName("settings");
        if (list.getLength() > 0) {
            Element settings = (Element) list.item(0);
            return settings.getAttribute(name);
        }
        return null;
    }

    public static String getDrivers() throws Exception {
        return getSetting("drivers");
    }

    public static void saveDrivers(String driver) throws Exception {
        saveSetting("drivers", driver);
    }

    public static Vector<ConnectionData> getDatabases() throws Exception {
        Element config = getConfig();
        return getDatabases(config);
    }

    public static Vector<ConnectionData> getDatabases(Element config)
            throws Exception {
        Vector<ConnectionData> connectionDatas = new Vector<ConnectionData>();
        NodeList nodeList = config.getElementsByTagName("database");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            connectionDatas.add(new ConnectionData(
                    element.getAttribute("name"), element
                            .getAttribute("connection")));
        }
        return connectionDatas;
    }

    @SuppressWarnings("unchecked")
    public static void saveDatabases(List<ConnectionData> connectionDatas)
            throws Exception {
        Collections.sort(connectionDatas);
        Element config = getConfig();
        NodeList nodeList = config.getElementsByTagName("database");
        for (int i = nodeList.getLength() - 1; i > -1; i--) {
            config.removeChild(nodeList.item(i));
        }
        for (ConnectionData connectionData : connectionDatas) {
            Element element = config.getOwnerDocument().createElement(
                    "database");
            element.setAttribute("name", connectionData.getName());
            element.setAttribute("connection", connectionData.getUrl());
            config.appendChild(element);
        }
        Config.saveConfig(config);
    }

    public static Map<String, String> getFavorites()
            throws ParserConfigurationException, IOException, SAXException {
        Element config = getConfig();
        NodeList favorites = config.getElementsByTagName("favorite");
        Map<String, String> map = new TreeMap<String, String>();
        for (int i = 0; i < favorites.getLength(); i++) {
            Element favorite = (Element) favorites.item(i);
            map.put(favorite.getAttribute("name"),
                    favorite.getAttribute("query"));
        }
        return map;
    }

    @SuppressWarnings("rawtypes")
    public static void saveFavorites(Map favorites)
            throws ParserConfigurationException, IOException,
            TransformerException, SAXException {
        Element config = getConfig();
        NodeList nodeList = config.getElementsByTagName("favorite");
        for (int i = nodeList.getLength() - 1; i > -1; i--) {
            config.removeChild(nodeList.item(i));
        }
        for (Object o : favorites.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            Element favorite = config.getOwnerDocument().createElement(
                    "favorite");
            favorite.setAttribute("name", (String) entry.getKey());
            favorite.setAttribute("query", (String) entry.getValue());
            config.appendChild(favorite);
        }
        Config.saveConfig(config);
    }

    private static void saveSetting(String name, String value) throws Exception {
        Element config = getConfig();
        NodeList list = config.getElementsByTagName("settings");
        if (list.getLength() > 0) {
            Element settings = (Element) list.item(0);
            if (value.equals(settings.getAttribute(name))) {
                return;
            }
            settings.setAttribute(name, value);
        } else {
            Element settings = config.getOwnerDocument().createElement(
                    "settings");
            settings.setAttribute(name, value);
            config.appendChild(settings);
        }
        Config.saveConfig(config);
    }

    protected static Element getConfig() throws ParserConfigurationException,
            IOException, SAXException {
        Element config;
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder();
        try {
            InputStream inputStream = new FileInputStream(new File(
                    System.getProperty("user.home"), "OpenTeradataViewer.xml"));
            config = documentBuilder.parse(inputStream).getDocumentElement();
            inputStream.close();
        } catch (Exception e) {
            InputStream inputStream = new ByteArrayInputStream(
                    "<config/>".getBytes());
            config = documentBuilder.parse(inputStream).getDocumentElement();
            inputStream.close();
        }
        return config;
    }

    protected static void saveConfig(Element config)
            throws TransformerException {
        // remove whitespace
        NodeList childNodes = config.getChildNodes();
        for (int i = childNodes.getLength() - 1; i > -1; i--) {
            if (childNodes.item(i).getNodeType() == Node.TEXT_NODE) {
                config.removeChild(childNodes.item(i));
            }
        }
        Transformer transformer = TransformerFactory.newInstance()
                .newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(
                "{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(config), new StreamResult(
                new ByteArrayOutputStream())); // test first
        transformer.transform(new DOMSource(config), new StreamResult(new File(
                System.getProperty("user.home"), "OpenTeradataViewer.xml")));
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

    public static void saveLastUsedDir(String dir) throws Exception {
        saveSetting("dir", dir);
    }

    public static String getLastUsedDir() throws Exception {
        return getSetting("dir");
    }

    /**
     * Used by plug-ins.
     */
    public static void saveExtraProperty(String propertyName,
            String propertyValue) throws Exception {
        saveSetting(propertyName, propertyValue);
    }

    /**
     * Used by plug-ins.
     */
    public static String getExtraProperty(String propertyName) throws Exception {
        return getSetting(propertyName);
    }
}
