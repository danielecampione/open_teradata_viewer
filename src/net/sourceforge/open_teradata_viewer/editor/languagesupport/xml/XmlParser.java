/*
 * Open Teradata Viewer ( editor language support xml )
 * Copyright (C) 2014, D. Campione
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.xml;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Segment;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sourceforge.open_teradata_viewer.DocumentReader;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.xml.tree.XmlTreeNode;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxDocument;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.AbstractParser;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.DefaultParseResult;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.DefaultParserNotice;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.IParseResult;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.IParserNotice;

import org.xml.sax.Attributes;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parses XML code in an <code>SyntaxTextArea</code>.<p>
 *
 * Like all STA <code>Parser</code>s, an <code>XmlParser</code> instance is
 * notified when the STA's text content changes. After a small delay, it will
 * parse the content as XML, building an AST and looking for any errors. When
 * parsing is complete, a property change event of type {@link #PROPERTY_AST} is
 * fired. Listeners can check the new value of the property for an {@link
 * XmlTreeNode} that represents the root of a tree structure modeling the XML
 * content in the text area. Note that the <code>XmlTreeNode</code> may be
 * incomplete if there were parsing/syntax errors (it will usually be complete
 * "up to" the error in the content).<p>
 *
 * This parser cannot be shared amongst multiple instances of
 * <code>SyntaxTextArea</code>.<p>
 *
 * @author D. Campione
 * 
 */
public class XmlParser extends AbstractParser {

    /**
     * The property change event that's fired when the document is re-parsed.
     * Applications can listen for this property change and update themselves
     * accordingly. The property's "new value" will be an {@link XmlTreeNode}
     * representing the root of a tree modeling the XML content. The "old value"
     * is always <code>null</code>.
     */
    public static final String PROPERTY_AST = "XmlAST";

    private XmlLanguageSupport xls;
    private PropertyChangeSupport support;
    private XmlTreeNode curElem;
    private XmlTreeNode root;
    private Locator locator;
    private SAXParserFactory spf;
    private SAXParser sp;
    private IValidationConfig validationConfig;

    public XmlParser(XmlLanguageSupport xls) {
        this.xls = xls;
        support = new PropertyChangeSupport(this);
        try {
            spf = SAXParserFactory.newInstance();
        } catch (FactoryConfigurationError fce) {
            ExceptionDialog.hideException(fce);
        }
    }

    /**
     * Adds a listener to this parser. Typically you'd want to register a
     * listener on the {@link #PROPERTY_AST} property.
     *
     * @param prop The property to listen for changes on.
     * @param l The listener itself.
     * @see #removePropertyChangeListener(String, PropertyChangeListener)
     */
    public void addPropertyChangeListener(String prop, PropertyChangeListener l) {
        support.addPropertyChangeListener(prop, l);
    }

    /**
     * Returns the XML model from the last time it was parsed.
     *
     * @return The root node of the XML model or <code>null</code> if it has not
     *         yet been parsed or an error occurred while parsing.
     */
    public XmlTreeNode getAst() {
        return root;
    }

    /**
     * Returns a string representing the "main" attribute for an element.
     *
     * @param attributes The attributes of an element. Calling code should have
     *        already verified this has length &gt; 0.
     * @return The "main" attribute.
     */
    private String getMainAttribute(Attributes attributes) {
        int nameIndex = -1;
        int idIndex = -1;

        for (int i = 0; i < attributes.getLength(); i++) {
            String name = attributes.getLocalName(i);
            if ("id".equals(name)) {
                idIndex = i;
                break;
            } else if ("name".equals(name)) {
                nameIndex = i;
            }
        }

        int i = idIndex;
        if (i == -1) {
            i = nameIndex;
            if (i == -1) {
                i = 0; // Default to first attribute
            }
        }

        return attributes.getLocalName(i) + "=" + attributes.getValue(i);
    }

    public SAXParserFactory getSaxParserFactory() {
        return spf;
    }

    /** {@inheritDoc} */
    @Override
    public IParseResult parse(SyntaxDocument doc, String style) {
        new ValidationConfigSniffer().sniff(doc);

        DefaultParseResult result = new DefaultParseResult(this);
        curElem = root = new XmlTreeNode("Root");

        if (spf == null || doc.getLength() == 0) {
            return result;
        }

        try {
            if (sp == null) { // New or reset for different validation
                sp = spf.newSAXParser();
            }
            Handler handler = new Handler(doc, result);
            if (validationConfig != null) {
                validationConfig.configureHandler(handler);
            }
            DocumentReader r = new DocumentReader(doc);
            InputSource input = new InputSource(r);
            sp.parse(input, handler);
            r.close();
        } catch (Exception e) {
            // Don't give an error; they likely just saved an incomplete XML
            // file
            // Fall through
            ExceptionDialog.ignoreException(e);
        }

        if (locator != null) {
            try {
                root.setStartOffset(doc.createPosition(0));
                root.setEndOffset(doc.createPosition(doc.getLength()));
            } catch (BadLocationException ble) {
                ExceptionDialog.hideException(ble);
            }
        }

        support.firePropertyChange(PROPERTY_AST, null, root);
        return result;
    }

    /**
     * Removes a listener on a specific property.
     *
     * @param prop The property being listened to.
     * @param l The listener to remove.
     * @see #addPropertyChangeListener(String, PropertyChangeListener)
     */
    public void removePropertyChangeListener(String prop,
            PropertyChangeListener l) {
        support.removePropertyChangeListener(prop, l);
    }

    /**
     * Sets how validation will be done by this parser. This can be used to set
     * up either DTD or Schema validation.
     *
     * @param config Configuration information for validation. If this is
     *        <code>null</code>, no validation will be done.
     */
    public void setValidationConfig(IValidationConfig config) {
        this.validationConfig = config;
        if (validationConfig != null) {
            validationConfig.configureParser(this);
        }
        sp = null; // Force recreation for possible new validation params
    }

    /**
     * Callback for events when we're parsing the XML in the editor. Creates our
     * model and records any parsing errors for squiggle underlining.
     * 
     * @author D. Campione
     * 
     */
    public class Handler extends DefaultHandler {

        private DefaultParseResult result;
        private SyntaxDocument doc;
        private Segment s;
        private EntityResolver entityResolver;

        public Handler(SyntaxDocument doc, DefaultParseResult result) {
            this.doc = doc;
            this.result = result;
            s = new Segment();
        }

        private void doError(SAXParseException e, IParserNotice.Level level) {
            if (!xls.getShowSyntaxErrors()) {
                return;
            }
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

        @Override
        public void endElement(String uri, String localName, String qName) {
            curElem = (XmlTreeNode) curElem.getParent();
        }

        @Override
        public void error(SAXParseException e) throws SAXException {
            doError(e, IParserNotice.Level.ERROR);
        }

        @Override
        public void fatalError(SAXParseException e) throws SAXException {
            doError(e, IParserNotice.Level.ERROR);
        }

        private int getTagStart(int end) {
            Element root = doc.getDefaultRootElement();
            int line = root.getElementIndex(end);
            Element elem = root.getElement(line);
            int start = elem.getStartOffset();
            int lastCharOffs = -1;

            try {
                while (line >= 0) {
                    doc.getText(start, end - start, s);
                    for (int i = s.offset + s.count - 1; i >= s.offset; i--) {
                        char ch = s.array[i];
                        if (ch == '<') {
                            return lastCharOffs;
                        } else if (Character.isLetterOrDigit(ch)) {
                            lastCharOffs = start + i - s.offset;
                        }
                    }
                    if (--line >= 0) {
                        elem = root.getElement(line);
                        start = elem.getStartOffset();
                        end = elem.getEndOffset();
                    }
                }
            } catch (BadLocationException ble) {
                ExceptionDialog.hideException(ble);
            }

            return -1;
        }

        @Override
        public InputSource resolveEntity(String publicId, String systemId)
                throws IOException, SAXException {
            if (entityResolver != null) {
                return entityResolver.resolveEntity(publicId, systemId);
            }
            // Override default behavior and return empty DTD contents
            return new InputSource(new java.io.StringReader(" "));
        }

        @Override
        public void setDocumentLocator(Locator l) {
            locator = l;
        }

        public void setEntityResolver(EntityResolver resolver) {
            this.entityResolver = resolver;
        }

        @Override
        public void startElement(String uri, String localName, String qName,
                Attributes attributes) {
            XmlTreeNode newElem = new XmlTreeNode(qName);
            if (attributes.getLength() > 0) {
                newElem.setMainAttribute(getMainAttribute(attributes));
            }
            if (locator != null) {
                int line = locator.getLineNumber();
                if (line != -1) {
                    int offs = doc.getDefaultRootElement().getElement(line - 1)
                            .getStartOffset();
                    int col = locator.getColumnNumber();
                    if (col != -1) {
                        offs += col - 1;
                    }
                    // "offs" is now the end of the tag. Find the beginning of it
                    offs = getTagStart(offs);
                    try {
                        newElem.setStartOffset(doc.createPosition(offs));
                        int endOffs = offs + qName.length();
                        newElem.setEndOffset(doc.createPosition(endOffs));
                    } catch (BadLocationException ble) {
                        ExceptionDialog.hideException(ble);
                    }
                }
            }

            curElem.add(newElem);
            curElem = newElem;
        }

        @Override
        public void warning(SAXParseException e) throws SAXException {
            doError(e, IParserNotice.Level.WARNING);
        }
    }
}