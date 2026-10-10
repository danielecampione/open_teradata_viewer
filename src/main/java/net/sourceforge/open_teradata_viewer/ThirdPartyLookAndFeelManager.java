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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import net.sourceforge.open_teradata_viewer.util.Utilities;

/**
 * NOTE: Specifying LookAndFeels in this XML file is done at your own risk. If a
 * LookAndFeel throws an Exception on the EDT, for any reason (needs special
 * configuration not handled by this LAF manager, requires a newer JRE version
 * than is specified in the XML, etc..), it can hose the GUI application and
 * keep it from running.<p>
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
    private static final String ROOT = "ThirdPartyLookAndFeels";

    /**
     * Root attribute of the XML file holding the revision of the built-in list
     * (default_3rd_party_lnfs.list) the file was last synchronized with. Older
     * releases ignore it.
     */
    private static final String DEFAULTS_REVISION_ATTRIBUTE = "defaultsRevision";

    /**
     * The current revision of the built-in list. Raise it whenever an entry
     * with a new "since" revision is added to default_3rd_party_lnfs.list.
     */
    private static final int DEFAULTS_REVISION = 4;

    /**
     * Built-in Look and Feels that are no longer shipped. Their entries are
     * dropped once from an existing file that is older than the revision that
     * retired them, so that the menu does not offer a Look and Feel that can
     * no longer be loaded.
     */
    private static final String[] RETIRED_CLASSES = { "com.incors.plaf.kunststoff.KunststoffLookAndFeel" };

    /** The Office Look and Feels are built on top of the Windows one. */
    private static final String WINDOWS_ONLY_CLASS_PREFIX = "org.fife.plaf.";

    /** The Look and Feel used by default on Windows, when it is available. */
    private static final String WINDOWS_DEFAULT_LOOK_AND_FEEL = "org.fife.plaf.VisualStudio2005.VisualStudio2005LookAndFeel";

    private static final boolean IS_WINDOWS = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH)
            .startsWith("windows");

    /** Ctor. */
    public ThirdPartyLookAndFeelManager(String path) {
        URL[] urls = null;

        try {
            urls = Drivers.retrieveAllJars(path);
            Drivers.addAllJarsToClasspath(path);
        } catch (MalformedURLException murle) {
            ExceptionDialog.ignoreException(murle);
        } catch (Exception e) {
            urls = new URL[0];
        }

        lnfInfo = load3rdPartyLookAndFeelInfo("open_teradata_viewer_lookandfeels.xml");

        try {
            int count = lnfInfo == null ? 0 : lnfInfo.size();
            // 3rd party Look and Feel jars. Add them to classpath. NOTE: The
            // lines of code below must be in the order they're in
            if (count > 0) {
                List lnfJarUrlList = new ArrayList();
                for (Iterator i = lnfInfo.iterator(); i.hasNext();) {
                    ExtendedLookAndFeelInfo info = (ExtendedLookAndFeelInfo) i.next();
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

        // Specifying a parent ClassLoader other than the default hoses some
        // LAFs, such as Substance
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
        ExtendedLookAndFeelInfo[] array = new ExtendedLookAndFeelInfo[lnfInfo.size()];
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
        File file = new File(Utilities.normalizePath(System.getProperty("user.home")), xmlFile);
        if (!file.isFile()) {
            try {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.newDocument();
                Element rootElement = doc.createElement(ROOT);
                rootElement.setAttribute(DEFAULTS_REVISION_ATTRIBUTE, String.valueOf(DEFAULTS_REVISION));
                doc.appendChild(rootElement);

                for (String[] defaultLookAndFeel : readDefaultLookAndFeels()) {
                    rootElement.appendChild(createLookAndFeelElement(doc, defaultLookAndFeel));
                }

                writeDocument(doc, file);
            } catch (ParserConfigurationException pce) {
                ExceptionDialog.ignoreException(pce);
            } catch (TransformerConfigurationException tce) {
                ExceptionDialog.ignoreException(tce);
            } catch (TransformerException te) {
                ExceptionDialog.ignoreException(te);
            } catch (IOException ioe) {
                ExceptionDialog.ignoreException(ioe);
            }
        } else {
            synchronizeWithDefaults(file);
        }

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        Document doc = null;
        try {
            db = dbf.newDocumentBuilder();
            try (InputStream inputStream = new FileInputStream(file);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                InputSource is = new InputSource(reader);
                is.setEncoding("UTF-8");
                doc = db.parse(is);
            }
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
     * @param lafInfo An array list of <code>ExtendedLookAndFeelInfo</code>s.
     * @throws IOException If an error occurs while parsing the XML.
     */
    private static void loadFromXML(Element root, ArrayList lafInfo) throws IOException {
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
                        throw new IOException("XML error: LookAndFeel " + "tags shouldn't have children.");
                    }
                    NamedNodeMap attributes = child.getAttributes();
                    if (attributes == null || attributes.getLength() < 2) {
                        throw new IOException("XML error: LookAndFeel " + "tags should have at least two attributes.");
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
                                minVersion = Double.parseDouble(node2.getNodeValue());
                            } catch (NumberFormatException nfe) {
                                ExceptionDialog.hideException(nfe);
                            }
                        } else {
                            throw new IOException("XML error: unknown " + "attribute: '" + attr + "'.");
                        }
                    }
                    if (name == null) {
                        throw new IOException("open_teradata_viewer_lookandfeels.xml: At least one "
                                + "LookAndFeel had no '" + NAME + "' attribute.");
                    }
                    if (className == null) {
                        throw new IOException("open_teradata_viewer_lookandfeels.xml: LookAndFeel " + name
                                + " is missing required '" + CLASS + "' attribute.");
                    }
                    if (minVersion <= 0) {
                        throw new IOException("open_teradata_viewer_lookandfeels.xml: LookAndFeel " + name
                                + " is missing required '" + MIN_JAVA_VERSION + "' attribute or the value of this "
                                + "property isn't strictly greater than 0.");
                    }
                    boolean add = true;
                    String javaSpecVersion = System.getProperty("java.specification.version");
                    try {
                        double javaSpecVersionVal = Double.parseDouble(javaSpecVersion);
                        add = javaSpecVersionVal >= minVersion;
                    } catch (NumberFormatException nfe) {
                        ExceptionDialog.hideException(nfe);
                    }
                    // The Office Look and Feels extend the Windows one: they cannot
                    // work anywhere else, so they are not offered there
                    if (add && !IS_WINDOWS && className.startsWith(WINDOWS_ONLY_CLASS_PREFIX)) {
                        add = false;
                    }
                    if (add) {
                        lafInfo.add(new ExtendedLookAndFeelInfo(name, className));
                    }
                } else { // Anything else is an error
                    throw new IOException("XML error:  Unknown element " + "node: " + elemName);
                }
            }
        }
    }

    /**
     * Reads the built-in list of 3rd party Look and Feels. Every non-empty line
     * that is not a comment has the form
     * <code>name;class;minJavaVersion[;since]</code>, where the optional
     * <code>since</code> is the revision of the list that introduced the entry
     * (1 when omitted).
     *
     * @return The entries as arrays of four strings: name, class, minimum Java
     *         version and since revision.
     * @throws IOException If the list cannot be read.
     */
    private static List<String[]> readDefaultLookAndFeels() throws IOException {
        List<String[]> entries = new ArrayList<>();
        try (InputStream is = ThirdPartyLookAndFeelManager.class.getResourceAsStream("/res/default_3rd_party_lnfs.list")) {
            if (is == null) {
                throw new IOException("Resource not found: /res/default_3rd_party_lnfs.list");
            }
            // readLine() copes with any line terminator, whatever the platform
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] fields = line.split(";");
                if (fields.length < 3) {
                    continue;
                }
                String[] entry = { fields[0].trim(), fields[1].trim(), fields[2].trim(),
                        fields.length > 3 ? fields[3].trim() : "1" };
                entries.add(entry);
            }
        }
        return entries;
    }

    private static Element createLookAndFeelElement(Document doc, String[] defaultLookAndFeel) {
        Element element = doc.createElement(LOOK_AND_FEEL);
        element.setAttribute(NAME, defaultLookAndFeel[0]);
        element.setAttribute(CLASS, defaultLookAndFeel[1]);
        element.setAttribute(MIN_JAVA_VERSION, defaultLookAndFeel[2]);
        return element;
    }

    private static void writeDocument(Document doc, File file) throws TransformerException {
        DOMSource source = new DOMSource(doc);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(source, new StreamResult(new ByteArrayOutputStream())); // Test first
        transformer.transform(source, new StreamResult(file));
    }

    /**
     * Brings an already existing XML file up to date with the built-in list:
     * the Look and Feels that were introduced after the revision the file was
     * last synchronized with are added, and the ones that have been retired
     * since are removed. This is done once per revision, so that an entry the
     * user removes later on is never put back.
     *
     * @param file The XML file of the 3rd party Look and Feels.
     */
    private static void synchronizeWithDefaults(File file) {
        try {
            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc;
            try (InputStream inputStream = new FileInputStream(file)) {
                doc = docBuilder.parse(inputStream);
            }
            Element rootElement = doc.getDocumentElement();

            int revision = 1;
            String storedRevision = rootElement.getAttribute(DEFAULTS_REVISION_ATTRIBUTE).trim();
            if (storedRevision.length() > 0) {
                try {
                    revision = Integer.parseInt(storedRevision);
                } catch (NumberFormatException nfe) {
                    ExceptionDialog.hideException(nfe);
                }
            }
            if (revision >= DEFAULTS_REVISION) {
                return;
            }

            Set<String> presentClasses = new HashSet<>();
            List<Element> retired = new ArrayList<>();
            List<String> retiredClasses = Arrays.asList(RETIRED_CLASSES);
            NodeList children = rootElement.getElementsByTagName(LOOK_AND_FEEL);
            for (int i = 0; i < children.getLength(); i++) {
                Element lookAndFeel = (Element) children.item(i);
                String className = lookAndFeel.getAttribute(CLASS).trim();
                if (retiredClasses.contains(className)) {
                    retired.add(lookAndFeel);
                } else {
                    presentClasses.add(className);
                }
            }
            for (Element lookAndFeel : retired) {
                rootElement.removeChild(lookAndFeel);
            }

            for (String[] defaultLookAndFeel : readDefaultLookAndFeels()) {
                int since = 1;
                try {
                    since = Integer.parseInt(defaultLookAndFeel[3]);
                } catch (NumberFormatException nfe) {
                    ExceptionDialog.hideException(nfe);
                }
                if (since > revision && !presentClasses.contains(defaultLookAndFeel[1])) {
                    rootElement.appendChild(createLookAndFeelElement(doc, defaultLookAndFeel));
                }
            }
            rootElement.setAttribute(DEFAULTS_REVISION_ATTRIBUTE, String.valueOf(DEFAULTS_REVISION));

            // Drop the blank text nodes of the old formatting, the transformer
            // indents the whole file again
            NodeList rootChildren = rootElement.getChildNodes();
            for (int i = rootChildren.getLength() - 1; i >= 0; i--) {
                Node child = rootChildren.item(i);
                if (child.getNodeType() == Node.TEXT_NODE && child.getTextContent().trim().isEmpty()) {
                    rootElement.removeChild(child);
                }
            }
            writeDocument(doc, file);
        } catch (Exception e) {
            ExceptionDialog.ignoreException(e);
        }
    }

    /**
     * Returns the Look and Feel to use when the user has never chosen one: on
     * Windows it is the Visual Studio 2005 one, provided that it is listed and
     * can be loaded, everywhere else (or otherwise) the given one.
     *
     * @param systemLookAndFeel The class name of the Look and Feel to fall back
     *                          to, usually the system one.
     * @return The class name of the Look and Feel to use.
     */
    public String getDefaultLookAndFeelClassName(String systemLookAndFeel) {
        if (IS_WINDOWS && isListed(WINDOWS_DEFAULT_LOOK_AND_FEEL)) {
            try {
                Class.forName(WINDOWS_DEFAULT_LOOK_AND_FEEL, false, lafLoader);
                return WINDOWS_DEFAULT_LOOK_AND_FEEL;
            } catch (ClassNotFoundException | LinkageError e) {
                ExceptionDialog.hideException(e);
            }
        }
        return systemLookAndFeel;
    }

    private boolean isListed(String className) {
        if (lnfInfo != null) {
            for (Iterator i = lnfInfo.iterator(); i.hasNext();) {
                if (className.equals(((ExtendedLookAndFeelInfo) i.next()).getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void restoreSystemLookAndFeel() {
        try {
            String defaultLAF = UIManager.getSystemLookAndFeelClassName();
            String startupLookAndFeelProperty = "startup_lookandfeel_class";
            Config.saveSetting(startupLookAndFeelProperty, defaultLAF);
            System.err.println("Default Look And Feel has been restored.");
        } catch (Exception e) {
            ExceptionDialog.ignoreException(e);
        }
    }
}