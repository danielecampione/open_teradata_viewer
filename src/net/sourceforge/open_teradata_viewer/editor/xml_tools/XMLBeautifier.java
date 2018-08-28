/*
 * Open Teradata Viewer ( editor xml tools )
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

package net.sourceforge.open_teradata_viewer.editor.xml_tools;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.SAXException;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.editor.OTVSyntaxTextArea;
import net.sourceforge.open_teradata_viewer.util.StringUtil;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class XMLBeautifier {

    public static final int DEFAULT_TAB_SIZE = 4;

    private int tabSize;

    public XMLBeautifier() {
        initDefaultTabSize();
    }

    public XMLBeautifier(int tabSize) {
        if (tabSize < 0) {
            initDefaultTabSize();
        } else {
            this.tabSize = tabSize;
        }
    }

    public void initDefaultTabSize() {
        ApplicationFrame application = ApplicationFrame.getInstance();
        if (application == null) {
            tabSize = DEFAULT_TAB_SIZE;
        } else {
            OTVSyntaxTextArea textArea = application.getTextComponent();
            if (textArea == null) {
                tabSize = DEFAULT_TAB_SIZE;
            } else {
                tabSize = textArea.getTabSize();
            }
        }
    }

    public String indentXML(String xml)
            throws SAXException, IOException, ParserConfigurationException, TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "" + tabSize);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        transformer.transform(new StreamSource(new StringReader(xml)), new StreamResult(outputStream));
        String formattedXML = outputStream.toString();

        return formattedXML;
    }

    public String validateXML(String unformattedXML) {
        unformattedXML = StringUtil.recursiveReplaceAll(unformattedXML, "\r\n", "");
        unformattedXML = StringUtil.recursiveReplaceAll(unformattedXML, "\n", "");
        unformattedXML = StringUtil.recursiveReplaceAll(unformattedXML, ">\t", ">");
        unformattedXML = StringUtil.recursiveReplaceAll(unformattedXML, "> ", ">");
        unformattedXML = StringUtil.recursiveReplaceAll(unformattedXML, "\t<", "<");
        unformattedXML = StringUtil.recursiveReplaceAll(unformattedXML, " <", "<");

        return unformattedXML;
    }

    public int getTabSize() {
        return tabSize;
    }
}