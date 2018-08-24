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

/**
 * Configuration information to get an {@link XmlParser} validating against
 * either a DTD or a schema.
 *
 * @author D. Campione
 * 
 */
public interface IValidationConfig {

    /**
     * Configures the parser itself. Called when this config is first set on an
     * <code>XmlParser</code>.
     */
    void configureParser(XmlParser parser);

    /**
     * Configures the actual handler instance. Called before each parsing of the
     * document.
     */
    void configureHandler(XmlParser.Handler handler);
}