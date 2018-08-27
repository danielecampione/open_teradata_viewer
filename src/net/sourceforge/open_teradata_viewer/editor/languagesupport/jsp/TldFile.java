/*
 * Open Teradata Viewer ( editor language support jsp )
 * Copyright (C) 2015, D. Campione
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.jsp;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.IParameterizedCompletion.Parameter;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.jsp.TldAttribute.TldAttributeParam;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * A TLD.
 *
 * @author D. Campione
 * 
 */
public class TldFile {

    private JspCompletionProvider provider;
    private File jar;
    private List<TldElement> tldElems;

    public TldFile(JspCompletionProvider provider, File jar) throws IOException {
        this.provider = provider;
        this.jar = jar;
        tldElems = loadTldElems();
    }

    public List<Parameter> getAttributesForTag(String tagName) {
        for (TldElement elem : tldElems) {
            if (elem.getName().equals(tagName)) {
                return elem.getAttributes();
            }
        }
        return null;
    }

    private String getChildText(Node elem) {
        StringBuilder sb = new StringBuilder();
        NodeList children = elem.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof Text) {
                sb.append(child.getNodeValue());
            }
        }
        return sb.toString();
    }

    public TldElement getElement(int index) {
        return tldElems.get(index);
    }

    public int getElementCount() {
        return tldElems.size();
    }

    private List<TldElement> loadTldElems() throws IOException {
        JarFile jar = new JarFile(this.jar);
        List<TldElement> elems = null;

        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().endsWith("tld")) {
                InputStream in = jar.getInputStream(entry);
                elems = parseTld(in);
                in.close();
            }
        }

        jar.close();
        return elems;
    }

    private List<TldElement> parseTld(InputStream in) throws IOException {
        List<TldElement> tldElems = new ArrayList<TldElement>();

        BufferedInputStream bin = new BufferedInputStream(in);

        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(bin);
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }
        Element root = doc.getDocumentElement(); // taglib

        NodeList nl = root.getElementsByTagName("uri");
        if (nl.getLength() != 1) {
            throw new IOException("Expected 1 'uri' tag; found: "
                    + nl.getLength());
        }

        nl = root.getElementsByTagName("tag");
        for (int i = 0; i < nl.getLength(); i++) {
            Element elem = (Element) nl.item(i);
            String name = getChildText(elem.getElementsByTagName("name")
                    .item(0));
            String desc = getChildText(elem.getElementsByTagName("description")
                    .item(0));
            TldElement tldElem = new TldElement(provider, name, desc);
            tldElems.add(tldElem);
            NodeList attrNl = elem.getElementsByTagName("attribute");
            List<TldAttributeParam> attrs = new ArrayList<TldAttributeParam>(
                    attrNl.getLength());
            for (int j = 0; j < attrNl.getLength(); j++) {
                Element attrElem = (Element) attrNl.item(j);
                name = getChildText(attrElem.getElementsByTagName("name").item(
                        0));
                desc = getChildText(attrElem
                        .getElementsByTagName("description").item(0));
                boolean required = Boolean.valueOf(
                        getChildText(attrElem.getElementsByTagName("required")
                                .item(0))).booleanValue();
                boolean rtexprValue = false;
                TldAttributeParam param = new TldAttributeParam(null, name,
                        required, rtexprValue);
                param.setDescription(desc);
                attrs.add(param);
            }
            tldElem.setAttributes(attrs);
        }

        return tldElems;
    }
}