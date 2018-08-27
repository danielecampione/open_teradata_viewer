/*
 * Open Teradata Viewer ( editor language support js ecma api e4x )
 * Copyright (C) 2015, D. Campione
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.functions.IE4XXMLFunctions;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSBoolean;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSFunction;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSObject;

/**
 * Object XML
 *
 * @author D. Campione
 * @since Standard ECMA-357 2nd. Edition.
 *
 */
public abstract class E4XXML implements IE4XXMLFunctions {

    /**
     * Object XML(xml).
     *
     * @constructor
     * @param xml The XML definition.
     * @extends Object
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    public E4XXML(JSObject xml) {
    }

    /**
     * <b>property prototype</b>
     *
     * @type XML
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    public E4XXML protype;

    /**
     * <b>property constructor</b>
     *
     * @type Function
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    protected JSFunction constructor;

    /**
     * <b>property ignoringComments</b> - The initial value of the
     * ignoreComments property is <b>true</b>. If ignoreComments is <b>true</b>,
     * XML comments are ignored when constructing new XML objects.
     *
     * @type Boolean
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    public static JSBoolean ignoringComments;

    /**
     * <b>property ignoreProcessingInstructions</b> - The initial value of the
     * ignoreProcessingInstructions property is <b>true</b>. If
     * ignoreProcessingInstructions is <b>true</b>, XML processing instructions
     * are ignored when constructing new XML objects.
     *
     * @type Boolean
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    public static JSBoolean ignoreProcessingInstructions;

    /**
     * <b>property ignoreWhitespace</b> - The initial value of the
     * ignoreWhitespace property is <b>true</b>. If ignoreWhitespace is
     * <b>true</b>, insignificant whitespace characters are ignored when
     * processing constructing new XML objects.
     *
     * @type Boolean
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    public static JSBoolean ignoreWhitespace;

    /**
     * <b>property prettyPrinting</b> - The initial value of the prettyPrinting
     * property is <b>true</b>. If prettyPrinting is <b>true</b>, the ToString
     * and ToXMLString operators will normalize whitespace characters between
     * certain tags to achieve a uniform and aesthetic appearance.
     *
     * @type Boolean
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    public static JSBoolean prettyPrinting;

    /**
     * <b>property prettyIndent</b> - The initial value of the prettyIndent
     * property is <b>2</b>. If the prettyPrinting property of the XML
     * constructor is <b>true</b>, the ToString and ToXMLString operators will
     * normalize whitespace characters between certain tags to achieve a uniform
     * and aesthetic appearance. Certain child nodes will be indented relative
     * to their parent node by the number of spaces specified by prettyIndent.
     *
     * @type Boolean
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    public static JSNumber prettyIndent;

    /**
     * <b>function settings()</b> - The settings method is a convenience method
     * for managing the collection of global XML settings stored as properties
     * of the XML constructor.<p>
     *
     * <strong>Example</strong>
     * <pre>
     * // Create a general purpose function that may need to save and restore XML settings
     * function getXMLCommentsFromString(xmlString) {
     *   // save previous XML settings and make sure comments are not ignored
     *   var settings = XML.settings();
     *   XML.ignoreComments = false;
     *   var comments = XML(xmlString).comment();
     *   // restore settings and return result
     *   XML.setSettings(settings);
     *   return comments;
     * }
     * </pre>
     *
     * @return An object containing the properties of the XML constructor used
     *         for storing XML settings.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    public static JSObject settings() {
        return null;
    }

    /**
     * <b>function setSetting(settings)</b> - The setSettings method is a
     * convenience method for managing the collection of global XML settings
     * stored as properties of the XML constructor.
     *
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    public static void setSettings(JSObject settings) {
    }

    /**
     * <b>function defaultSettings()</b> - The defaultSettings method is a
     * convenience method for managing the collection of global XML settings
     * stored as properties of the XML constructor.
     *
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     * @since Level 3 Document Object Model Core Definition.
     */
    public static JSObject defaultSettings() {
        return null;
    }
}