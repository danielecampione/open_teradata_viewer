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

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XNamespace;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XQName;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXMLList;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSArray;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSBoolean;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSNumber;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSObject;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.JSString;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions.IJSObjectFunctions;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public interface IE4XXMLFunctions extends IJSObjectFunctions {

    /**
     * <b>function addNamespace(namespace)</b> - Adds a namespace declaration to
     * the in scope namespaces for this XML object and returns this XML object.
     * 
     * @param name The namespace to be added. 
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public void addNamespace(E4XNamespace namespace);

    /**
     * <b>function appendChild(xml)</b> - Appends the given child to the end of
     * this XML object's properties and returns this XML object.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * {@code var e = <employees>
     * 	  <employee id="0" ><name>Jim</name><age>25</age></employee>
     * 	  <employee id="1" ><name>Joe</name><age>20</age></employee>
     * 	</employees>;
     * // Add a new child element to the end of Jim's employee element
     * e.employee.(name == "Jim").appendChild(<hobby>snorkeling</hobby>);}
     * </pre>
     * 
     * @param xml XML to be appended. 
     * @returns <b>this</b> XML Object 
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXML appendChild(E4XXML xml);

    /**
     * <b>function attribute(attributeName)</b> - Finds list of XML attributes
     * associated with the attribute name.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * // get the id of the employee named Jim
     * e.employee.(name == "Jim").attribute("id");
     * </pre>
     * 
     * @param attributeName Name of attribute to find. 
     * @returns An XMLList containing zero or one XML attributes associated with
     *          this XML object that have the given <b><i>attributeName</i></b>.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXMLList attribute(JSString attributeName);

    /**
     * <b>function attributes()</b> - List of XML attributes associated with an
     * XML object.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * // print the attributes of an XML object
     *  function printAttributes(x) {
     *   for each (var a in x.attributes()) {
     *     print("The attribute named " + a.name() + " has the value " + a);
     *   }
     *  }
     * </pre>
     * 
     * @returns An XMLList containing the XML attributes of this object.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXMLList attributes();

    /**
     * <b>function child(propertyName)</b> - Finds list of XML object matching a
     * given <b><i>propertyName</i></b>.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * var name = customer.child("name"); // equivalent to: var name = customer.name;
     * var secondChild = customer.child(1); // equivalent to: var secondChild = customer.*[1]
     * </pre>
     * 
     * @param propertyName Name of XML element to find. 
     * @returns The list of children in this XML object matching the given
     *          propertyName. If propertyName is a numeric index, the child
     *          method returns a list containing the child at the ordinal
     *          position identified by <b><i>propertyName</i></b>.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXMLList child(JSString propertyName);

    /**
     * <b>function child(propertyName)</b> - Finds list of XML object matching a
     * given <b><i>propertyName</i></b>.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * var name = customer.child("name"); // equivalent to: var name = customer.name;
     * var secondChild = customer.child(1); // equivalent to: var secondChild = customer.*[1]
     * </pre>
     * 
     * @param propertyName Name of XML element to find. 
     * @returns The list of children in this XML object matching the given
     *          propertyName. If propertyName is a numeric index, the child
     *          method returns a list containing the child at the ordinal
     *          position identified by <b><i>propertyName</i></b>.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXMLList child(JSNumber propertyName);

    /**
     * <b>function childIndex()</b> - Returns the index position of the XML
     * element.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * // Get the ordinal index of the employee named Joe.
     * var joeindex = e.employee.(name == "Joe").childIndex();
     * </pre> 
     * 
     * @returns A Number representing the ordinal position of this XML object
     *          within the context of its parent.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public JSNumber childIndex();

    /**
     * <b>function children()</b> - Returns list of children for the XML
     * element.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * // Get child elements of first employee: returns an XMLList containing:
     * // <name>Jim</name>, <age>25</age> and <hobby>Snorkeling</hobby>
     * var emps = e.employee[0].children();
     * </pre> 
     * 
     * @returns An XMLList containing all the properties of this XML object in
     *          order.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXMLList children();

    /**
     * <b>function comments()</b> - Returns list of comments for the XML
     * element.
     * 
     * @returns An XMLList containing the properties of this XML object that
     *          represent XML comments.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXMLList comments();

    /**
     * <b>function contains(value)</b> - Returns the result of comparing this
     * XML object with the given value.
     * 
     * @returns the result of comparing this XML object with the given value.
     *          This treatment intentionally blurs the distinction between a
     *          single XML object and an XMLList containing only one value.
     * @param value XML object to test.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public JSBoolean contains(E4XXML value);

    /**
     * <b>function contains(value)</b> - Returns the result of comparing this
     * XML object with the given value.
     * 
     * @returns The result of comparing this XML object with the given value.
     *          This treatment intentionally blurs the distinction between a
     *          single XML object and an XMLList containing only one value.
     * @param value XMLList object to test.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public JSBoolean contains(E4XXMLList value);

    /**
     * <b>function copy()</b> - Returns a deep copy of the XML object.
     * 
     * @returns the copy method returns a deep copy of this XML object with the
     *          internal [[Parent]] property set to <b>null</b>.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public JSBoolean copy();

    /**
     * <b>function descendants(name)</b> - Returns all the XML valued
     * descendants (children, grandchildren, great-grandchildren, etc..).
     * 
     * @returns All the XML valued descendants (children, grandchildren,
     *          great-grandchildren, etc..) of this XML object with the given
     *          name. If the name parameter is omitted, it returns all
     *          descendants of this XML object.
     * @param <i>name</i> optional parameter to identity the decendants. If
     *        omitted all decendants are returned.  
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXMLList descendants(JSString name);

    /**
     * <b>function elements(name)</b> - Returns the child elements.
     * 
     * @returns An XMLList containing all the children of this XML object that
     *          are XML elements with the given name. When the elements method
     *          is called with no parameters, it returns an XMLList containing
     *          all the children of this XML object that are XML elements
     *          regardless of their name.
     * @param <i>name</i> Optional parameter to identity the element. If omitted
     *        all children are returned.  
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXMLList elements(JSString name);

    /**
     * <b>function hasComplexContent()</b> - A Boolean value indicating whether
     * this XML object contains complex content.
     * 
     * @returns A Boolean value indicating whether this XML object contains
     *          complex content. An XML object is considered to contain complex
     *          content if it represents an XML element that has child elements.
     *          XML objects representing attributes, comments, processing
     *          instructions and text nodes do not have complex content.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public JSBoolean hasComplexContent();

    /**
     * <b>function hasSimpleContent()</b> - A Boolean value indicating whether
     * this XML object contains simple content.
     * 
     * @returns A Boolean value indicating whether this XML object contains
     *          simple content. An XML object is considered to contain simple
     *          content if it represents a text node, represents an attribute
     *          node or if it represents an XML element that has no child
     *          elements. XML objects representing comments and processing
     *          instructions do not have simple content.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public JSBoolean hasSimpleContent();

    /**
     * <b>function inScopeNamespaces()</b> - Returns an array of Namespace
     * objects representing the namespaces in scope for this object.
     * 
     * @returns An array of Namespace objects representing the namespaces in
     *          scope for this object.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public JSArray inScopeNamespaces();

    /**
     * <b>function insertChildAfter(child1 , child2)</b> - Inserts the given
     * <b></i>child2</b></i> after the given </b></i>child1</b></i> in this XML
     * object and returns this XML object. If </b></i>child1</b></i> is
     * <b><i>null</i></b>, the insertChildAfter method inserts
     * <b></i>child2</b></i> before all children of this XML object (i.e., after
     * none of them). If </b></i>child1</b></i> does not exist in this XML
     * object, it returns without modifying this XML object.
     * 
     * @returns XML object representing <b><i>x</i></b>.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXML insertChildAfter(E4XXML child1, E4XXML child2);

    /**
     * <b>function insertChildBefore(child1 , child2)</b> - Inserts the given
     * <b></i>child2</b></i> before the given </b></i>child1</b></i> in this XML
     * object and returns this XML object. If </b></i>child1</b></i> is null,
     * the insertChildBefore method inserts <b></i>child2</b></i> after all
     * children in this XML object (i.e., before none of them). If
     * </b></i>child1</b></i> does not exist in this XML object, it returns
     * without modifying this XML object.
     * 
     * @returns XML object representing <b><i>x</i></b>.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition
     */
    public E4XXML insertChildBefore(E4XXML child1, E4XXML child2);

    /**
     * <b>function length()</b> - The length method always returns the integer 1
     * for XML objects.
     * 
     * @returns 1 for XML objects (allowing an XML object to be treated like an
     *          XML List with a single item).
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public JSNumber length();

    /**
     * <b>function localName()</b> - Returns the localName part of the XML
     * object.
     * 
     * @returns The local name of this object.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public JSNumber localName();

    /**
     * <b>function name()</b> - Returns qualified name for the XML object.
     * 
     * @returns The qualified name associated with this XML object.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XQName name();

    /**
     * <b>function namespace(prefix)</b> - Returns the namespace associated with
     * this object.
     * 
     * @param <i>prefix</i> Optional prefix identifier.
     * @returns The namespace associated with this object or if a prefix is
     *          specified, an in-scope namespace with that prefix.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XNamespace namespace(JSString prefix);

    /**
     * <b>function namespaceDeclarations()</b> - Returns a string representing
     * the kind of object this is (e.g. "element").
     * 
     * @returns An Array of Namespace objects representing the namespace
     *          declarations associated with this XML object in the context of
     *          its parent.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public JSArray namespaceDeclarations();

    /**
     * <b>function nodeKind()</b> - Return an array of Namespace objects
     * representing the namespace declarations associated with this object.
     * 
     * @returns A string representing the <b><i>Class</i></b> of this XML
     *          object.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public JSString nodeKind();

    /**
     * <b>function normalize()</b> - Puts all text nodes in this and all
     * descendant XML objects into a normal form by merging adjacent text nodes
     * and eliminating empty text nodes.
     * 
     * @returns This XML object.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXML normalize();

    /**
     * <b>function parent()</b> - Returns the parent of this XML object.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * // Get the parent element of the second name in "e". Returns {@code <employee id="1" ...}
     * var firstNameParent = e..name[1].parent();
     * </pre>
     * 
     * @returns The parent of this XML object.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXML parent();

    /**
     * <b>function processingInstructions(name)</b> - A list of all processing
     * instructions that are children of this element.
     * 
     * @param <i>name</i> Optional node name filter. 
     * @returns An XMLList containing all the children of this XML object that
     *          are processing-instructions with the given <b><i>name</i></b>.
     *          When the processingInstructions method is called with no
     *          parameters, it returns an XMLList containing all the children of
     *          this XML object that are processing-instructions regardless of
     *          their name.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXMLList processingInstructions(JSString name);

    /**
     * <b>function prependChild(value)</b> - Adds a new child to an element,
     * prior to all other children.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * // Add a new child element to the front of John's employee element
     * e.employee.(name == "John").prependChild({@code <prefix>Mr.</prefix>});
     * </pre>
     * 
     * @returns This XML object
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXML prependChild(E4XXML value);

    /**
     * <b>function removeNamespace(namespace)</b> - Removes a namespace from the
     * in-scope namespaces of the element.
     * 
     * @param namespace Namespace to remove.
     * @returns A copy of this XML object.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXML removeNamespace(E4XNamespace namespace);

    /**
     * <b>function replace(propertyName, value)</b> - Replaces the XML
     * properties of this XML object specified by propertyName with value and
     * returns this XML object. If this XML object contains no properties that
     * match propertyName, the replace method returns without modifying this XML
     * object. The propertyName parameter may be a numeric property name, an
     * unqualified name for a set of XML elements, a qualified name for a set of
     * XML elements or the properties wildcard "*". When the propertyName
     * parameter is an unqualified name, it identifies XML elements in the
     * default namespace. The value parameter may be an XML object, XMLList
     * object or any value that may be converted to a String with ToString().<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * {@code // Replace the first employee record with an open staff requisition
     * employees.replace(0, <requisition status="open"/>);
     * // Replace all item elements in the order with a single empty item
     * order.replace("item", <item/>);}
     * </pre> 
     * 
     * @param propertyName Name of property to replace.
     * @param value XML, XMLList or any other object (that can be converted
     *        using ToString()) to insert.
     * @returns This XML object.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXML replace(JSString propertyName, JSObject value);

    /**
     * <b>function replace(propertyName, value)</b> - Replaces the XML
     * properties of this XML object specified by propertyName with value and
     * returns this XML object. If this XML object contains no properties that
     * match propertyName, the replace method returns without modifying this XML
     * object. The propertyName parameter may be a numeric property name, an
     * unqualified name for a set of XML elements, a qualified name for a set of
     * XML elements or the properties wildcard "*". When the propertyName
     * parameter is an unqualified name, it identifies XML elements in the
     * default namespace. The value parameter may be an XML object, XMLList
     * object or any value that may be converted to a String with ToString().<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * {@code // Replace the first employee record with an open staff requisition
     * employees.replace(0, <requisition status="open"/>);
     * // Replace all item elements in the order with a single empty item
     * order.replace("item", <item/>);}
     * </pre> 
     * 
     * @param propertyName Number index to replace.
     * @param value XML, XMLList or any other object (that can be converted
     *        using ToString()) to insert.
     * @returns This XML object
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXML replace(JSNumber propertyName, JSObject value);

    /**
     * <b>function setChildren(value)</b> - Replaces the XML properties of this
     * XML object with a new set of XML properties from value. value may be a
     * single XML object or an XMLList. setChildren returns this XML object.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * {@code // Replace the entire contents of Jim's employee element
     * employees.replace(0, <requisition status="open"/>);
     * e.employee.(name == "Jim").setChildren(<name>John</name> + <age>35</age>);}
     * </pre> 
     * 
     * @param value New XML to set.
     * @returns This XML object.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXML setChildren(E4XXML value);

    /**
     * <b>function setChildren(value)</b> - Replaces the XML properties of this
     * XML object with a new set of XML properties from value. value may be a
     * single XML object or an XMLList. setChildren returns this XML object.<p>
     * 
     * <strong>Example</strong>
     * <pre>
     * {@code // Replace the entire contents of Jim's employee element
     * employees.replace(0, <requisition status="open"/>);
     * e.employee.(name == "Jim").setChildren(<name>John</name> + <age>35</age>);}
     * </pre> 
     * 
     * @param value New XMLList to set.
     * @returns This XML object.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public E4XXML setChildren(E4XXMLList value);

    /**
     * <b>function setLocalName(name)</b> - Replaces the local name of this XML
     * object with a string constructed from the given <b><i>name</i></b>.
     * 
     * @param name Name to set for the XML element.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public void setLocalName(JSString name);

    /**
     * <b>function setName(name)</b> - Sets the name of the XML object to the
     * requested value (possibly qualified).
     * 
     * @param name Qualified name to set.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public void setName(E4XQName name);

    /**
     * <b>function setNamespace(namespace)</b> - Sets the namespace of the XML
     * object to the requested value.
     * 
     * @param namespace Namespace to set.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public void setNamespace(E4XNamespace namespace);

    /**
     * <b>function text()</b> - Returns an XMLList containing all XML properties
     * of this XML object that represent XML text nodes.
     * 
     * @return Concatenation of all text node children as a list.
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
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
     * @memberOf XML
     * @see net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.E4XXML
     * @since Standard ECMA-357 2nd. Edition.
     */
    public JSString toXMLString();
}