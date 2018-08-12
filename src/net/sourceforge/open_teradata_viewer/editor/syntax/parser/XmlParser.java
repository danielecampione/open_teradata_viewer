/*
 * Open Teradata Viewer ( editor syntax parser )
 * Copyright (C) 2013, D. Campione
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

import java.io.IOException;

import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sourceforge.open_teradata_viewer.DocumentReader;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxDocument;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
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
 * To support DTD validation, specify an entity resolver when creating the
 * parser, and enable validation like so:
 * 
 * <pre>
 * XmlParser xmlParser = new XmlParser(new MyEntityResolver());
 * xmlParser.setValidating(true);
 * textArea.addParser(xmlParser);
 * </pre>
 *
 * Also note that a single instance of this class can be installed on multiple
 * instances of <code>SyntaxTextArea</code>.<p>
 * 
 * @author D. Campione
 *
 */
public class XmlParser extends AbstractParser {

    private SAXParserFactory spf;
    private DefaultParseResult result;
    private EntityResolver entityResolver;

    public XmlParser() {
        this(null);
    }

    /**
     * Constructor allowing DTD validation of documents.
     * 
     * @param resolver An entity resolver to use if validation is enabled.
     * @see #setValidating(boolean)
     */
    public XmlParser(EntityResolver resolver) {
        this.entityResolver = resolver;
        result = new DefaultParseResult(this);
        try {
            spf = SAXParserFactory.newInstance();
        } catch (FactoryConfigurationError fce) {
            ExceptionDialog.hideException(fce);
        }
    }

    /**
     * @return Whether this parser does DTD validation.
     * @see #setValidating(boolean)
     */
    public boolean isValidating() {
        return spf.isValidating();
    }

    /** {@inheritDoc} */
    public IParseResult parse(SyntaxDocument doc, String style) {
        result.clearNotices();
        Element root = doc.getDefaultRootElement();
        result.setParsedLines(0, root.getElementCount() - 1);

        if (spf == null || doc.getLength() == 0) {
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
            ExceptionDialog.hideException(e); // Will print if the specified DTD can't be found
            result.addNotice(new DefaultParserNotice(this,
                    "Error parsing XML: " + e.getMessage(), 0, -1, -1));
        }

        return result;
    }

    /**
     * Sets whether this parser will use DTD validation if required.
     *
     * @param validating Whether DTD validation should be enabled. If this is
     *        <code>true</code>, documents must specify a DOCTYPE, and you
     *        should have used the constructor specifying an entity resolver.
     * @see #isValidating()
     */
    public void setValidating(boolean validating) {
        spf.setValidating(validating);
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

        /*
         * NOTE: If you compile with Java 4-, you must remove IOException to the
         * throws clause of this method. The "official" release is built with
         * Java 7.
         */
        public InputSource resolveEntity(String publicId, String systemId)
                throws SAXException {
            if (entityResolver != null) {
                try {
                    return entityResolver.resolveEntity(publicId, systemId);
                } catch (IOException ioe) {
                    ExceptionDialog.hideException(ioe);
                }
            }
            try {
                return super.resolveEntity(publicId, systemId);
            } catch (IOException ioe) {
                ExceptionDialog.hideException(ioe);
                return null;
            }
        }

        public void warning(SAXParseException e) {
            doError(e, IParserNotice.WARNING);
        }
    }
}