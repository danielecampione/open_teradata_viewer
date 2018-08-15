/*
 * Open Teradata Viewer ( editor language support js ecma api e4x functions )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.functions;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXMLList;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSBoolean;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions.IJSObjectFunctions;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public interface IE4XXMLListFunctions extends IJSObjectFunctions {

    /**
     * <b>function attribute(attributeName)</b> - Calls the attribute method of
     * each XML object in this XMLList object passing attributeName as a
     * parameter and returns an XMLList containing the results in order.
     * 
     * @param attributeName Name of attribute to find.
     * @returns An XMLList containing the results in order.
     * @memberOf XMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.functions.IE4XXMLFunctions#attribute(JSString)
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXMLList attribute(JSString attributeName);

    /**
     * <b>function attributes()</b> - Calls the attributes() method of each XML
     * object in this XMLList object and returns an XMLList containing the
     * results in order.
     * 
     * @returns An XMLList containing the results in order.
     * @memberOf XMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.functions.IE4XXMLFunctions#attributes()
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXMLList attributes();

    /**
     * <b>function child(propertyName)</b> - Calls the child() method of each
     * XML object in this XMLList object and returns an XMLList containing the
     * results in order.
     * 
     * @param propertyName Name of XML element to find. 
     * @returns An XMLList containing the results in order.
     * @memberOf XMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.functions.IE4XXMLFunctions#child(JSString)
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXMLList child(JSString propertyName);

    /**
     * <b>function children()</b> - Calls the children() method of each XML
     * object in this XMLList object and returns an XMLList containing the
     * results concatenated in order.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * // Get all the children of all the items in the order
     * var allitemchildren = order.item.children();
     * 
     * // Get all grandchildren of the order that have the name price
     * var grandChildren = order.children().price;
     * </pre> 
     * 
     * @returns An XMLList containing the results concatenated in order.
     * @memberOf XMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.functions.IE4XXMLFunctions#children()
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXMLList children();

    /**
     * <b>function comments()</b> - Calls the comments method of each XML object
     * in this XMLList object and returns an XMLList containing the results
     * concatenated in order.
     * 
     * @returns An XMLList containing the results concatenated in order.
     * @memberOf XMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.functions.IE4XXMLFunctions#comments()
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXMLList comments();

    /**
     * <b>function contains(value)</b> - Returns a boolean value indicating
     * whether this XMLList object contains an XML object that compares equal to
     * the given <b><i>value</i></b>.
     * 
     * @returns <b><i>true</i></b> if XMLList contains XML <b><i>value</i></b>,
     *          otherwise <b><i>false</i></b>.
     * @param value XML object to test.
     * @memberOf XMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXMLList
     * @since Standard ECMA-357 2nd. Edition.
     */
    public JSBoolean contains(E4XXML value);

    /**
     * <b>function copy()</b> - Returns a deep copy of the XMLList object.
     * 
     * @returns The copy method returns a deep copy of this XMLList object.
     * @memberOf XMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXMLList
     * @since Standard ECMA-357 2nd. Edition.
     */
    public JSBoolean copy();

    /**
     * <b>function descendants(name)</b> - Calls the descendants method of each
     * XML object in this XMLList object with the optional parameter name (or
     * the string "*" if name is omitted) and returns an XMLList containing the
     * results concatenated in order.
     * 
     * @returns All the XML valued descendants (children, grandchildren,
     *          great-grandchildren, etc..) of this XMLList object with the
     *          given name. If the name parameter is omitted, it returns all
     *          descendants of this XMLList object.
     * @param <i>name</i> Optional parameter to identity the decendants. If
     *        omitted all decendants are returned.  
     * @memberOf XMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.functions.IE4XXMLFunctions#descendants(JSString)
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXMLList descendants(JSString name);

    /**
     * <b>function elements(name)</b> - Calls the elements method of each XML
     * object in this XMLList object passing the optional parameter name (or "*"
     * if it is omitted) and returns an XMList containing the results in order.
     * 
     * @returns An XMLList containing all the children of this XMLList object
     *          that are XML elements with the given name. When the elements
     *          method is called with no parameters, it returns an XMLList
     *          containing all the children of this XML object that are XML
     *          elements regardless of their name.
     * @param <i>name</i> Optional parameter to identity the element. If omitted
     *        all children are returned.
     * @memberOf XMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.functions.IE4XXMLFunctions#elements(JSString)
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXMLList elements(JSString name);

    /**
     * <b>function hasComplexContent()</b> - Returns a Boolean value indicating
     * whether this XMLList object contains complex content. An XMLList object
     * is considered to contain complex content if it is not empty, contains a
     * single XML item with complex content or contains elements.
     * 
     * @returns <b><i>true</i></b> if XMLList contains complex content,
     *          otherwise <b><i>false</i></b>.
     * @memberOf XMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXMLList
     * @since Standard ECMA-357 2nd. Edition.
     */
    public JSBoolean hasComplexContent();

    /**
     * <b>function hasSimpleContent()</b> - Returns a Boolean value indicating
     * whether this XMLList contains simple content. An XMLList object is
     * considered to contain simple content if it is empty, contains a single
     * XML item with simple content or contains no elements.
     * 
     * @returns <b><i>true</i></b> if XMLList contains simple content, otherwise
     *          <b><i>false</i></b>.
     * @memberOf XMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXMLList
     * @since Standard ECMA-357 2nd. Edition.
     */
    public JSBoolean hasSimpleContent();

    /**
     * <b>function length()</b> - Returns the number of properties in this
     * XMLList object.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * for (var i = 0; i < e..name.length(); i++) {
     *   print("Employee name:" + e..name[i]);
     * }
     * </pre>
     * 
     * @returns The number of properties in this XMLList object.
     * @memberOf XMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXMLList
     * @since Standard ECMA-357 2nd. Edition.
     */
    public JSNumber length();

    /**
     * <b>function normalize()</b> - Puts all text nodes in this XMLList, all
     * the XML objects it contains and the descendents of all the XML objects it
     * contains into a normal form by merging adjacent text nodes and
     * eliminating empty text nodes.
     * 
     * @returns This XMLList object.
     * @memberOf XMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXMLList
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXMLList normalize();

    /**
     * <b>function parent()</b> - If all items in this XMLList object have the
     * same parent, it is returned. Otherwise, the parent method returns
     * <b><i>undefined</i></b>.
     * 
     * @returns The parent of this XMLList object.
     * @memberOf XMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXMLList
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXML parent();

    /**
     * <b>function processingInstructions(name)</b> - Calls the
     * processingInstructions method of each XML object in this XMLList object
     * passing the optional parameter name (or "*" if it is omitted) and returns
     * an XMList containing the results in order.
     * 
     * @param <i>name</i> Optional node name filter. 
     * @returns An XMList containing the results in order.
     * @memberOf XMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.functions.IE4XXMLFunctions#processingInstructions(JSString)
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXMLList processingInstructions(JSString name);

    /**
     * <b>function text()</b> - Calls the text method of each XML object
     * contained in this XMLList object and returns an XMLList containing the
     * results concatenated in order.
     * 
     * @return An XMLList containing the results concatenated in order.
     * @memberOf XMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.functions.IE4XXMLFunctions#text()
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXMLList text();

    /**
     * <b>function toXMLString()</b> - Returns the toXMLString() method returns
     * an XML encoded string representation of this XML object. Unlike the
     * toString method, toXMLString provides no special treatment for XML
     * objects that contain only XML text nodes (i.e., primitive values). The
     * toXMLString method always includes the start tag, attributes and end tag
     * of the XML object regardless of its content. It is provided for cases
     * when the default XML to string conversion rules are not desired.
     * 
     * @return Serializes this XML object as parseable XML.
     * @memberOf XMLList
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXMLList
     * @since Standard ECMA-357 2nd. Edition.
     */
    public JSString toXMLString();
}