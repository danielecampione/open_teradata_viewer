/*
 * Open Teradata Viewer ( plugin )
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

package net.sourceforge.open_teradata_viewer.plugin;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * The plugin factory.
 * 
 * @author D. Campione
 * 
 */
public class PluginFactory {

    @SuppressWarnings("rawtypes")
    private Hashtable plugins = null;

    private URLClassLoader classLoader = null;

    @SuppressWarnings("rawtypes")
    private Vector idNotAvailable = new Vector();

    public PluginFactory(String pluginsPath) {
        this.loadPlugins(pluginsPath);
    }

    /**
     * The method takes an ID representing a pluginEntry ID and returns an
     * instance of that PluginEntry.
     * 
     * @return an instance of that PluginEntry.
     */
    public PluginEntry getPluginEntry(Integer id) {
        if (plugins == null)
            return null;
        EntryDescriptor entryDescriptor = (EntryDescriptor) plugins.get(id);
        Object pluginEntry = null;
        try {
            Object object = classLoader.loadClass(
                    entryDescriptor.getMain().trim()).newInstance();
            pluginEntry = (PluginEntry) object;
        } catch (Exception e) {
            ApplicationFrame.getInstance().printStackTraceOnGUI(e);
        }
        return (PluginEntry) pluginEntry;
    }

    /**
     * Returns a Collection of all EntryDescriptor objects, one for each
     * detected entry point.
     */
    @SuppressWarnings("rawtypes")
    public Collection getAllEntryDescriptor() {
        if (plugins == null)
            return null;
        return plugins.values();
    }

    /**
     * Returns a Vector containing EntryDescriptor objects of entry points that
     * have the specified name.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Vector getEntryDescriptorsByName(String name) {
        Iterator pluginsIterator = plugins.values().iterator();
        Vector pluginsByName = new Vector();
        while (pluginsIterator.hasNext()) {
            EntryDescriptor pd = (EntryDescriptor) pluginsIterator.next();
            if (pd.getName().equals(name)) {
                pluginsByName.add(pd);
            }
        }
        return pluginsByName;
    }

    /**
     * The method loads an EntryDescriptor object for each entry point detected
     * in plugins path end puts it in plugins HashMap using a pluginID.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void loadPlugins(String pluginsPath) {
        try {
            String[] jar = (new File(pluginsPath)).list(new PluginFileFilter());
            if (jar.length == 0)
                return;
            plugins = new Hashtable();

            URL[] url = new URL[jar.length];
            for (int i = 0; i < jar.length; i++) {
                String pi = "file:/" + pluginsPath + jar[i];
                url[i] = new URL(pi);
            }
            classLoader = new URLClassLoader(url);
            for (int i = 0; i < jar.length; i++) {
                JarFile jarFile = new JarFile(pluginsPath + jar[i]);
                ZipEntry xmlDescriptor = this
                        .getPluginPathFileDescriptor(pluginsPath + jar[i]);
                if (xmlDescriptor != null) {
                    ApplicationFrame.getInstance().changeLog.append("Loading "
                            + jar[i] + "..\n");
                    InputStream inputStream = jarFile
                            .getInputStream(xmlDescriptor);
                    DocumentBuilderFactory dbf = DocumentBuilderFactory
                            .newInstance();
                    DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
                    Document doc = documentBuilder.parse(inputStream);
                    NodeList entryTags = null;
                    try {
                        entryTags = doc.getElementsByTagName("entry");
                    } catch (Exception e) {
                        ApplicationFrame.getInstance().changeLog
                                .append("    Warning: There are not entry tags in descriptor.xml\n",
                                        ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);

                    }
                    if (entryTags.getLength() == 0) {
                        ApplicationFrame.getInstance().changeLog
                                .append("    Warning: There are not entry tags in descriptor.xml\n",
                                        ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                    }
                    for (int j = 0; j < entryTags.getLength(); j++) {
                        Element entryPoint = (Element) entryTags.item(j);
                        EntryDescriptor entryDescriptor = new EntryDescriptor();
                        Integer key = getNewEntryID();
                        entryDescriptor.setId(key);
                        try {
                            entryDescriptor.setName(entryPoint
                                    .getElementsByTagName("name").item(0)
                                    .getFirstChild().getNodeValue());
                        } catch (Exception e) {
                            ApplicationFrame.getInstance().changeLog
                                    .append("    Warning: name tag not present.\n",
                                            ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                            ApplicationFrame.getInstance().changeLog
                                    .append("    It is not possibile to load this Entry Point.\n",
                                            ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                            break;
                        }
                        try {
                            entryDescriptor.setMain(entryPoint
                                    .getElementsByTagName("main").item(0)
                                    .getFirstChild().getNodeValue());
                        } catch (Exception e) {
                            ApplicationFrame.getInstance().changeLog
                                    .append("    Warning: main tag not present.\n",
                                            ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                            ApplicationFrame.getInstance().changeLog
                                    .append("    It is not possibile to load this Entry Point.\n",
                                            ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                            break;
                        }
                        try {
                            entryDescriptor.setAuthor(entryPoint
                                    .getElementsByTagName("author").item(0)
                                    .getFirstChild().getNodeValue());
                        } catch (Exception e) {
                            ApplicationFrame.getInstance().changeLog
                                    .append("    author tag not present.\n",
                                            ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                        }
                        try {
                            entryDescriptor.setDescription(entryPoint
                                    .getElementsByTagName("description")
                                    .item(0).getFirstChild().getNodeValue());
                        } catch (Exception e) {
                            ApplicationFrame.getInstance().changeLog
                                    .append("    description tag not present.\n",
                                            ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
                        }
                        plugins.put(key, entryDescriptor);
                    }
                }
            }
        } catch (IOException e) {
            ApplicationFrame.getInstance().printStackTraceOnGUI(e);
        } catch (Exception e) {
            ApplicationFrame.getInstance().printStackTraceOnGUI(e);
        }
    }

    /**
     * @return the ZipEntry object of descriptor.xml or null if it doesn't
     * exist.
     */
    @SuppressWarnings("rawtypes")
    private ZipEntry getPluginPathFileDescriptor(String fileName) {
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(fileName);

            for (Enumeration e = jarFile.entries(); e.hasMoreElements();) {
                JarEntry jarEntry = (JarEntry) e.nextElement();
                String file = jarEntry.getName();
                if (file.endsWith(("descriptor.xml")))
                    return jarFile.getEntry(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (jarFile != null) {
                try {
                    jarFile.close();
                } catch (IOException ioe) {
                }
            }
        }
        return null;
    }

    /** Generates a unique Plugin Entry ID */
    @SuppressWarnings("unchecked")
    private Integer getNewEntryID() {
        int id = idNotAvailable.size() + 1;
        Integer ris = new Integer(id);
        idNotAvailable.add(ris);
        return ris;
    }

    /**
     * This class implements a FileFilter that includes every JAR file.
     * 
     * @author D. Campione
     * 
     */
    class PluginFileFilter implements FilenameFilter {
        public boolean accept(File dir, String filename) {
            return filename.toUpperCase().endsWith(".JAR");
        }
    }
}
