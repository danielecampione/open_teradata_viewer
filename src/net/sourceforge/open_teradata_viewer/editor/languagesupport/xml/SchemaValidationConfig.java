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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

import org.xml.sax.SAXException;

/**
 * Configuration information for validating an XML file against an XML schema.
 *
 * @author D. Campione
 * 
 */
public class SchemaValidationConfig implements IValidationConfig {

    private Schema schema;

    public SchemaValidationConfig(String language, InputStream in)
            throws IOException {
        SchemaFactory sf = SchemaFactory.newInstance(language);

        BufferedInputStream bis = new BufferedInputStream(in);
        try {
            schema = sf.newSchema(new StreamSource(bis));
        } catch (SAXException se) {
            ExceptionDialog.hideException(se);
            throw new IOException(se.toString());
        } finally {
            bis.close();
        }
    }

    @Override
    public void configureParser(XmlParser parser) {
        SAXParserFactory spf = parser.getSaxParserFactory();
        spf.setValidating(false); // Not using a DTD
        if (schema != null) {
            spf.setSchema(schema);
        }
    }

    @Override
    public void configureHandler(XmlParser.Handler handler) {
        handler.setEntityResolver(null); // Not used in schema validation
    }
}