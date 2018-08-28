/*
 * Open Teradata Viewer ( test editor xml tools )
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

package test.net.sourceforge.open_teradata_viewer.editor.xml_tools;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.xml.sax.SAXException;

import junit.framework.TestCase;
import net.sourceforge.open_teradata_viewer.editor.xml_tools.XMLBeautifier;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class TestXMLBeautifier extends TestCase {

    @Test
    public void test_indentXML_defaultConstructor()
            throws SAXException, IOException, ParserConfigurationException, TransformerException {
        XMLBeautifier xmlBeautifier = new XMLBeautifier();
        String unformattedXML = "<note><body></body></note>";
        unformattedXML = xmlBeautifier.validateXML(unformattedXML);
        String formattedXML = xmlBeautifier.indentXML(unformattedXML);
        String expectedPrefix = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><note>";
        TestCase.assertTrue(formattedXML.startsWith(expectedPrefix));
    }

    @Test
    public void test_indentXML_returnCarriageAndSpaces()
            throws SAXException, IOException, ParserConfigurationException, TransformerException {
        XMLBeautifier xmlBeautifier = new XMLBeautifier(XMLBeautifier.DEFAULT_TAB_SIZE);
        String unformattedXML = "    <note>\r\n"
                + "<to>Tove</to><from>Jani</from><heading>Reminder</heading>            <body>Don't forget me this weekend!\r\n"
                + "</body></note>";
        String formattedXML = xmlBeautifier.indentXML(unformattedXML);
        String expectedPrefix = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><note>";
        TestCase.assertTrue(formattedXML.startsWith(expectedPrefix));

        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><note>\r\n" + "<to>Tove</to>\r\n"
                + "    <from>Jani</from>\r\n"
                + "    <heading>Reminder</heading>            <body>Don't forget me this weekend!\r\n" + "</body>\r\n"
                + "</note>\r\n";
        TestCase.assertTrue(formattedXML.equals(expected));

        formattedXML = xmlBeautifier.validateXML(formattedXML);
        formattedXML = xmlBeautifier.indentXML(formattedXML);
        expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><note>\r\n" + "    <to>Tove</to>\r\n"
                + "    <from>Jani</from>\r\n" + "    <heading>Reminder</heading>\r\n"
                + "    <body>Don't forget me this weekend!</body>\r\n" + "</note>\r\n";
        TestCase.assertTrue(formattedXML.equals(expected));
    }

    @Test
    public void test_indentXML_negativeTabSize()
            throws SAXException, IOException, ParserConfigurationException, TransformerException {
        XMLBeautifier xmlBeautifier = new XMLBeautifier(-1);
        String unformattedXML = "    <note>\r\n"
                + "<to>Tove</to><from>Jani</from><heading>Reminder</heading>            <body>Don't forget me this weekend!\r\n"
                + "</body></note>";
        unformattedXML = xmlBeautifier.validateXML(unformattedXML);
        String formattedXML = xmlBeautifier.indentXML(unformattedXML);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><note>\r\n" + "    <to>Tove</to>\r\n"
                + "    <from>Jani</from>\r\n" + "    <heading>Reminder</heading>\r\n"
                + "    <body>Don't forget me this weekend!</body>\r\n" + "</note>\r\n";
        TestCase.assertTrue(formattedXML.equals(expected));
    }
}