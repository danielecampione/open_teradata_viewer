/*
 * Open Teradata Viewer ( editor language support js ast type ecma client )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.ecma.client;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.IECMAAdditions;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.TypeDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.ecma.TypeDeclarations;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class DOMAddtions implements IECMAAdditions {

    @Override
    public void addAdditionalTypes(TypeDeclarations typeDecs) {
        // Add all client DOM objects
        typeDecs.addTypeDeclaration(
                "Attr",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "JSAttr", "Attr", false, false));
        typeDecs.addTypeDeclaration(
                "CDATASection",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "JSCDATASection", "CDATASection", false, false));
        typeDecs.addTypeDeclaration(
                "CharacterData",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "JSCharacterData", "CharacterData", false, false));
        typeDecs.addTypeDeclaration(
                "Comment",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "JSComment", "Comment", false, false));
        typeDecs.addTypeDeclaration(
                "Document",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "JSDocument", "Document", false, false));
        typeDecs.addTypeDeclaration(
                "DocumentFragment",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "JSDocumentFragment", "DocumentFragment", false, false));
        typeDecs.addTypeDeclaration(
                "DocumentType",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "JSDocumentType", "DocumentType", false, false));
        typeDecs.addTypeDeclaration(
                "DOMConfiguration",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "JSDOMConfiguration", "DOMConfiguration", false, false));
        typeDecs.addTypeDeclaration(
                "DOMImplementation",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "JSDOMImplementation", "DOMImplementation", false,
                        false));
        typeDecs.addTypeDeclaration(
                "DOMImplementationList",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "JSDOMImplementationList", "DOMImplementationList",
                        false, false));
        typeDecs.addTypeDeclaration(
                "DOMImplementationSource",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "DOMImplementationSource", "DOMImplementationSource",
                        false, false));
        typeDecs.addTypeDeclaration(
                "DOMLocator",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "JSDOMLocator", "DOMLocator", false, false));
        typeDecs.addTypeDeclaration(
                "DOMStringList",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "JSDOMStringList", "DOMStringList", false, false));
        typeDecs.addTypeDeclaration(
                "Element",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "JSElement", "Element", false, false));
        typeDecs.addTypeDeclaration(
                "Entity",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "JSEntity", "Entity", false, false));
        typeDecs.addTypeDeclaration(
                "EntityReference",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "JSEntityReference", "EntityReference", false, false));
        typeDecs.addTypeDeclaration(
                "NamedNodeMap",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "JSNamedNodeMap", "NamedNodeMap", false, false));
        typeDecs.addTypeDeclaration(
                "NameList",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "JSNameList", "NameList", false, false));
        typeDecs.addTypeDeclaration(
                "Node",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "JSNode", "Node", false, false));
        typeDecs.addTypeDeclaration(
                "NodeList",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "JSNodeList", "NodeList", false, false));
        typeDecs.addTypeDeclaration(
                "Notation",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "JSNotation", "Notation", false, false));
        typeDecs.addTypeDeclaration(
                "ProcessingInstruction",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "JSProcessingInstruction", "ProcessingInstruction",
                        false, false));
        typeDecs.addTypeDeclaration(
                "Text",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "JSText", "Text", false, false));
        typeDecs.addTypeDeclaration(
                "TypeInfo",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "JSTypeInfo", "TypeInfo", false, false));
        typeDecs.addTypeDeclaration(
                "UserDataHandler",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.dom",
                        "JSUserDataHandler", "UserDataHandler", false, false));

        // Add dom ecma objects
        typeDecs.addECMAObject("Attr", true);
        typeDecs.addECMAObject("CDATASection", true);
        typeDecs.addECMAObject("CharacterData", true);
        typeDecs.addECMAObject("Comment", true);
        typeDecs.addECMAObject("Document", true);
        typeDecs.addECMAObject("DocumentFragment", true);
        typeDecs.addECMAObject("DOMConfiguration", true);
        typeDecs.addECMAObject("DOMImplementation", true);
        typeDecs.addECMAObject("DOMImplementationList", true);
        typeDecs.addECMAObject("DOMLocator", true);
        typeDecs.addECMAObject("DOMStringList", true);
        typeDecs.addECMAObject("Element", true);
        typeDecs.addECMAObject("Entity", true);
        typeDecs.addECMAObject("EntityReference", true);
        typeDecs.addECMAObject("NamedNodeMap", true);
        typeDecs.addECMAObject("NameList", true);
        typeDecs.addECMAObject("Node", true);
        typeDecs.addECMAObject("NodeList", true);
        typeDecs.addECMAObject("Notation", true);
        typeDecs.addECMAObject("ProcessingInstruction", true);
        typeDecs.addECMAObject("Text", true);
        typeDecs.addECMAObject("TypeInfo", true);
        typeDecs.addECMAObject("UserDataHandler", true);
    }

}