/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2012, D. Campione
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.UIManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sourceforge.open_teradata_viewer.util.StreamUtil;
import net.sourceforge.open_teradata_viewer.util.StringUtil;
import net.sourceforge.open_teradata_viewer.util.Utilities;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * NOTE: Specifying LookAndFeels in this XML file is done at your own risk.
 *
 * A class capable of reading an XML file specifying 3rd party Look and Feel JAR
 * files, and returning an array of information about the Look and Feels, so
 * your application can use them.<p>
 *
 * The XML file read should have the following format:<p>
 * <pre>
 *   &lt;?xml version="1.0" encoding="UTF-8" ?&gt;
 *   &lt;ThirdPartyLookAndFeels&gt;
 *      &lt;LookAndFeel name="name" class="class" minJavaVersion="1.6"/&gt;
 *      &lt;LookAndFeel name="otherName" class="otherClass" minJavaVersion="1.6"/&gt;
 *      ... other LookAndFeel tags if desired ...
 *   &lt;/ThirdPartyLookAndFeels&gt;
 * </pre>
 *
 * where <code>name</code> is the name of the Look and Feel (as appears in
 * program's menu), <code>class</code> is the main Look and Feel class, such as
 * <code>net.sourceforge.napkinlaf.NapkinLookAndFeel</code>, relative to the
 * install location of the specified application. The
 * <code>minJavaVersion</code> attribute specifies the minimum Java version the
 * JRE must be for the application to offer this LookAndFeel as a choice. This
 * should be a double value, such as "1.6", etc..
 *
 * @author D. Campione
 * 
 */
public class ThirdPartyLookAndFeelManager {

    private List lnfInfo;
    private URLClassLoader lafLoader;

    private static final String CLASS = "class";
    private static final String LOOK_AND_FEEL = "LookAndFeel";
    private static final String MIN_JAVA_VERSION = "minJavaVersion";
    private static final String NAME = "name";

    public static URL[] ALL_LNFS_JARS;

    static {
        try {
            String path = Utilities.conformizePath(System
                    .getProperty("user.dir"))
                    + "lookandfeels"
                    + System.getProperty("file.separator");
            ALL_LNFS_JARS = Drivers.retrieveAllJars(path);
            Drivers.addAllJarsToClasspath(path);
        } catch (MalformedURLException murle) {
            ExceptionDialog.ignoreException(murle);
        } catch (Exception e) {
            ALL_LNFS_JARS = new URL[0];
        }
    }

    /** Ctor. */
    public ThirdPartyLookAndFeelManager() {
        URL[] urls = null;

        lnfInfo = load3rdPartyLookAndFeelInfo("open_teradata_viewer_lookandfeels.xml");

        try {
            int count = lnfInfo == null ? 0 : lnfInfo.size();
            // 3rd party Look and Feel jars. Add them to classpath. NOTE: The
            // lines of code below must be in the order they're in
            if (count > 0) {
                List lnfJarUrlList = new ArrayList();
                for (Iterator i = lnfInfo.iterator(); i.hasNext();) {
                    ExtendedLookAndFeelInfo info = (ExtendedLookAndFeelInfo) i
                            .next();
                    urls = ALL_LNFS_JARS;
                    for (int j = 0; j < urls.length; j++) {
                        if (!lnfJarUrlList.contains(urls[j])) {
                            lnfJarUrlList.add(urls[j]);
                        }
                    }
                }
                urls = (URL[]) lnfJarUrlList.toArray(new URL[0]);
            }
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
        }

        if (urls == null) {
            urls = new URL[0];
        }

        lafLoader = new URLClassLoader(urls);
    }

    /**
     * Returns an array of information on the JAR files containing 3rd party
     * Look and Feels. These jars were dynamically loaded from an XML file
     * relative to the root directory you gave this manager instance.
     *
     * @return An array of URLs for JAR files containing Look and Feels.
     */
    public ExtendedLookAndFeelInfo[] get3rdPartyLookAndFeelInfo() {
        if (lnfInfo == null) {
            return new ExtendedLookAndFeelInfo[0];
        }
        ExtendedLookAndFeelInfo[] array = new ExtendedLookAndFeelInfo[lnfInfo
                .size()];
        return (ExtendedLookAndFeelInfo[]) lnfInfo.toArray(array);
    }

    public ClassLoader getLAFClassLoader() {
        return lafLoader;
    }

    /**
     * Returns an array with each element representing a 3rd party Look and Feel
     * available to your GUI application.
     *
     * @param xmlFile The XML file specifying the 3rd party Look and Feels.
     * @return A list of {@link ExtendedLookAndFeelInfo}s.
     */
    private List load3rdPartyLookAndFeelInfo(String xmlFile) {
        File file = new File(Utilities.conformizePath(System
                .getProperty("user.home")), xmlFile);
        if (!file.isFile()) {
            try {
                String defaultThirdPartyLookAndFeelsFileContent = StreamUtil
                        .stream2String(ThirdPartyLookAndFeelManager.class
                                .getResourceAsStream("/res/default_3rd_party_lnfs.list"));
                String[] defaultThirdPartyLookAndFeels = StringUtil.split(
                        defaultThirdPartyLookAndFeelsFileContent, System
                                .getProperty("line.separator").charAt(0));

                DocumentBuilderFactory docFactory = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.newDocument();
                Element rootElement = doc
                        .createElement("ThirdPartyLookAndFeels");
                doc.appendChild(rootElement);

                String[] attributeNames = {NAME, CLASS, MIN_JAVA_VERSION};
                for (String line : defaultThirdPartyLookAndFeels) {
                    String[] attributes = line.split(";");
                    Element element = doc.createElement("LookAndFeel");
                    rootElement.appendChild(element);
                    for (int i = 0; i < attributeNames.length; i++) {
                        String attributeName = attributeNames[i];
                        Attr attr = doc.createAttribute(attributeName);
                        initAttribute(doc, attr, element, attributeName,
                                attributes[i].trim());
                    }
                }

                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(file);

                // Write the content into XML file
                TransformerFactory transformerFactory = TransformerFactory
                        .newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(
                        "{http://xml.apache.org/xslt}indent-amount", "4");
                transformer.transform(source, new StreamResult(
                        new ByteArrayOutputStream())); // Test first
                transformer.transform(source, result);
            } catch (ParserConfigurationException pce) {
                ExceptionDialog.ignoreException(pce);
            } catch (TransformerConfigurationException tce) {
                ExceptionDialog.ignoreException(tce);
            } catch (TransformerException te) {
                ExceptionDialog.ignoreException(te);
            } catch (IOException ioe) {
                ExceptionDialog.ignoreException(ioe);
            }
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        Document doc = null;
        try {
            db = dbf.newDocumentBuilder();
            InputStream inputStream = new FileInputStream(file);
            InputSource is = new InputSource(new BufferedReader(
                    new InputStreamReader(inputStream)));
            is.setEncoding("UTF-8");
            doc = db.parse(is);
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
            return null;
        }

        // Traverse the XML tree
        ArrayList lafInfo = new ArrayList(1);
        try {
            loadFromXML(doc.getDocumentElement(), lafInfo);
        } catch (IOException ioe) {
            ExceptionDialog.hideException(ioe);
        }

        return lafInfo;
    }

    /**
     * Used in parsing the XML file containing the 3rd party look and feels.
     *
     * @param node The root node of the parsed XML document.
     * @param lafInfo An array list of <code>ExtendedLookAndFeelInfo</code>s.
     * @throws IOException If an error occurs while parsing the XML.
     */
    private static void loadFromXML(Element root, ArrayList lafInfo)
            throws IOException {
        if (root == null) {
            throw new IOException("XML error: node==null.");
        }

        NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                String elemName = child.getNodeName();

                // Might be a Look And Feel declaration
                if (LOOK_AND_FEEL.equals(elemName)) {
                    // Shouldn't have any children
                    NodeList childNodes = child.getChildNodes();
                    if (childNodes != null && childNodes.getLength() > 0) {
                        throw new IOException("XML error: LookAndFeel "
                                + "tags shouldn't have children.");
                    }
                    NamedNodeMap attributes = child.getAttributes();
                    if (attributes == null || attributes.getLength() < 2) {
                        throw new IOException("XML error: LookAndFeel "
                                + "tags should have at least two attributes.");
                    }
                    String name = null;
                    String className = null;
                    double minVersion = 0;
                    for (int j = 0; j < attributes.getLength(); j++) {
                        Node node2 = attributes.item(j);
                        String attr = node2.getNodeName();
                        if (NAME.equals(attr)) {
                            name = node2.getNodeValue();
                        } else if (CLASS.equals(attr)) {
                            className = node2.getNodeValue();
                        } else if (MIN_JAVA_VERSION.equals(attr)) {
                            try {
                                minVersion = Double.parseDouble(node2
                                        .getNodeValue());
                            } catch (NumberFormatException nfe) {
                                ExceptionDialog.hideException(nfe);
                            }
                        } else {
                            throw new IOException("XML error: unknown "
                                    + "attribute: '" + attr + "'.");
                        }
                    }
                    if (name == null || className == null || minVersion <= 0) {
                        throw new IOException(
                                "XML error: LookAndFeel "
                                        + "must have attributes 'name', 'class' and 'minJavaVersion'.");
                    }
                    boolean add = true;
                    if (minVersion > 0) {
                        String javaSpecVersion = System
                                .getProperty("java.specification.version");
                        try {
                            double javaSpecVersionVal = Double
                                    .parseDouble(javaSpecVersion);
                            add = javaSpecVersionVal >= minVersion;
                        } catch (NumberFormatException nfe) {
                            ExceptionDialog.hideException(nfe);
                        }
                    }
                    if (add) {
                        lafInfo.add(new ExtendedLookAndFeelInfo(name, className));
                    }
                } else { // Anything else is an error
                    throw new IOException("XML error:  Unknown element "
                            + "node: " + elemName);
                }
            }
        }
    }

    private void initAttribute(Document doc, Attr attr, Element element,
            String attributeName, String attributeValue) {
        attr = doc.createAttribute(attributeName);
        attr.setValue(attributeValue);
        element.setAttributeNode(attr);
    }

    public static void restoreSystemLookAndFeel() {
        try {
            String defaultLAF = UIManager.getSystemLookAndFeelClassName();
            String startupLookAndFeelProperty = "startup_lookandfeel_class";
            Config.saveSetting(startupLookAndFeelProperty, defaultLAF);
            UISupport.getDialogs().showInfoMessage(
                    "Default Look And Feel has been restored.",
                    Main.APPLICATION_NAME);
        } catch (Exception e) {
            ExceptionDialog.ignoreException(e);
        }
    }
}