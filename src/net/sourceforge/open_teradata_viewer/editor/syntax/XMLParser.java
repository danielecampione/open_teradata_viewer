/*
 * Open Teradata Viewer ( editor syntax )
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

package net.sourceforge.open_teradata_viewer.editor.syntax;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.DocumentReader;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.AbstractParser;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.DefaultParseResult;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.DefaultParserNotice;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.IParseResult;
import net.sourceforge.open_teradata_viewer.editor.syntax.parser.IParserNotice;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A parser for XML documents.
 *
 * @author D. Campione
 * 
 */
public class XMLParser extends AbstractParser {

    private SAXParserFactory spf;
    private SyntaxTextArea textArea;
    private DefaultParseResult result;

    public XMLParser(SyntaxTextArea textArea) {
        this.textArea = textArea;
        result = new DefaultParseResult(this);
        try {
            spf = SAXParserFactory.newInstance();
        } catch (FactoryConfigurationError fce) {
            ExceptionDialog.notifyException(fce);
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
            Handler handler = new Handler();
            DocumentReader r = new DocumentReader(doc);
            InputSource input = new InputSource(r);
            sp.parse(input, handler);
            r.close();
        } catch (SAXParseException saxpe) {
            // A fatal parse error - ignore; a IParserNotice was already created
            ExceptionDialog.ignoreException(saxpe);
        } catch (Exception e) {
            ExceptionDialog.notifyException(e);
            result.addNotice(new DefaultParserNotice(this,
                    "Error parsing XML: " + e.getMessage(), 0, -1, -1));
        }

        return result;
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    private class Handler extends DefaultHandler {

        private void doError(SAXParseException saxpe) {
            int line = saxpe.getLineNumber() - 1;
            try {
                int offs = textArea.getLineStartOffset(line);
                int len = textArea.getLineEndOffset(line) - offs + 1;
                IParserNotice pn = new DefaultParserNotice(XMLParser.this,
                        saxpe.getMessage(), line, offs, len);
                result.addNotice(pn);
                ApplicationFrame
                        .getInstance()
                        .getConsole()
                        .println(">>> " + offs + "-" + len + " -> " + pn + ".",
                                ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
            } catch (BadLocationException ble) {
                ExceptionDialog.notifyException(ble);
            }
        }

        public void error(SAXParseException e) throws SAXException {
            doError(e);
        }

        public void fatalError(SAXParseException e) throws SAXException {
            doError(e);
        }

        public void warning(SAXParseException e) throws SAXException {
            doError(e);
        }
    }
}