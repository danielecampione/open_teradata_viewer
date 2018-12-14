/*
 * Open Teradata Viewer ( test editor sha1 sum tools )
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

package test.net.sourceforge.open_teradata_viewer.editor.sha1_sum_tools;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Test;
import org.xml.sax.SAXException;

import junit.framework.TestCase;
import net.sourceforge.open_teradata_viewer.editor.sha1_sum_tools.SHA1SumCalculator;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class TestSHA1SumCalculator extends TestCase {

    @Test
    public void test_indentXML_someText()
            throws SAXException, IOException, ParserConfigurationException, TransformerException {
        SHA1SumCalculator sha1SumCalculator = new SHA1SumCalculator();
        String input = "Some text";
        String sha1Sum = null;
        try {
            sha1Sum = sha1SumCalculator.calculateSHA1ChecksumOfAText(input);
        } catch (NoSuchAlgorithmException e) {
            TestCase.assertTrue(false);
        }
        String expectedSHA1Sum = "02d92c580d4ede6c80a878bdd9f3142d8f757be8";
        TestCase.assertTrue(expectedSHA1Sum.equals(sha1Sum));
    }

    @Test
    public void test_indentXML_nullText()
            throws SAXException, IOException, ParserConfigurationException, TransformerException {
        SHA1SumCalculator sha1SumCalculator = new SHA1SumCalculator();
        String input = null;
        String sha1Sum = null;
        try {
            sha1Sum = sha1SumCalculator.calculateSHA1ChecksumOfAText(input);
        } catch (NoSuchAlgorithmException e) {
            TestCase.assertTrue(false);
        }
        TestCase.assertNull(sha1Sum);
    }

    @Test
    public void test_indentXML_blankText()
            throws SAXException, IOException, ParserConfigurationException, TransformerException {
        SHA1SumCalculator sha1SumCalculator = new SHA1SumCalculator();
        String input = "";
        String sha1Sum = null;
        try {
            sha1Sum = sha1SumCalculator.calculateSHA1ChecksumOfAText(input);
        } catch (NoSuchAlgorithmException e) {
            TestCase.assertTrue(false);
        }
        String expectedSHA1Sum = "da39a3ee5e6b4b0d3255bfef95601890afd80709";
        TestCase.assertTrue(expectedSHA1Sum.equals(sha1Sum));
    }
}