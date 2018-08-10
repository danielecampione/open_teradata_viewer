/*
 * Open Teradata Viewer ( editor syntax parser )
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

package net.sourceforge.open_teradata_viewer.editor.syntax.parser;

import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sourceforge.open_teradata_viewer.DocumentReader;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxDocument;

import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A parser for XML documents. Adds squiggle underlines for any XML errors found
 * (though most XML parsers don't really have error recovery and so only can
 * find one error at a time).<p>
 *
 * This class isn't actually used by SyntaxTextArea anywhere, but you can
 * install and use it yourself. Doing so is as simple as:
 * 
 * <pre>
 * XmlParser xmlParser = new XmlParser();
 * textArea.addParser(xmlParser);
 * </pre>
 *
 * Also note that a single instance of this class can be installed on multiple
 * instances of <code>SyntaxTextArea</code>.
 * 
 * @author D. Campione
 *
 */
public class XmlParser extends AbstractParser {

    private SAXParserFactory spf;
    private DefaultParseResult result;

    public XmlParser() {
        result = new DefaultParseResult(this);
        try {
            spf = SAXParserFactory.newInstance();
        } catch (FactoryConfigurationError fce) {
            ExceptionDialog.hideException(fce);
        }
    }

    /** {@inheritDoc} */
    public IParseResult parse(SyntaxDocument doc, String style) {
        result.clearNotices();
        Element root = doc.getDefaultRootElement();
        result.setParsedLines(0, root.getElementCount() - 1);

        if (spf == null) {
            return result;
        }

        try {
            SAXParser sp = spf.newSAXParser();
            Handler handler = new Handler(doc);
            DocumentReader r = new DocumentReader(doc);
            InputSource input = new InputSource(r);
            sp.parse(input, handler);
            r.close();
        } catch (SAXParseException saxpe) {
            // A fatal parse error - ignore; a IParserNotice was already created
            ExceptionDialog.ignoreException(saxpe);
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
            result.addNotice(new DefaultParserNotice(this,
                    "Error parsing XML: " + e.getMessage(), 0, -1, -1));
        }

        return result;
    }

    /**
     * Callback notified when errors are found in the XML document. Adds a
     * notice to be squiggle-underlined.
     */
    private class Handler extends DefaultHandler {

        private Document doc;

        private Handler(Document doc) {
            this.doc = doc;
        }

        private void doError(SAXParseException e, int level) {
            int line = e.getLineNumber() - 1;
            Element root = doc.getDefaultRootElement();
            Element elem = root.getElement(line);
            int offs = elem.getStartOffset();
            int len = elem.getEndOffset() - offs;
            if (line == root.getElementCount() - 1) {
                len++;
            }
            DefaultParserNotice pn = new DefaultParserNotice(XmlParser.this,
                    e.getMessage(), line, offs, len);
            pn.setLevel(level);
            result.addNotice(pn);
        }

        public void error(SAXParseException e) {
            doError(e, IParserNotice.ERROR);
        }

        public void fatalError(SAXParseException e) {
            doError(e, IParserNotice.ERROR);
        }

        public void warning(SAXParseException e) {
            doError(e, IParserNotice.WARNING);
        }
    }
}