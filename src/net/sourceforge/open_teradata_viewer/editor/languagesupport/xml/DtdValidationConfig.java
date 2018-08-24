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

import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.EntityResolver;

/**
 * Configuration information for validating XML against a DTD.
 *
 * @author D. Campione
 * 
 */
public class DtdValidationConfig implements IValidationConfig {

    private EntityResolver entityResolver;

    public DtdValidationConfig(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }

    @Override
    public void configureParser(XmlParser parser) {
        SAXParserFactory spf = parser.getSaxParserFactory();
        spf.setValidating(true);
        spf.setSchema(null);
    }

    @Override
    public void configureHandler(XmlParser.Handler handler) {
        handler.setEntityResolver(entityResolver);
    }
}