/*
 * Open Teradata Viewer ( editor language support js ast type ecma e4x )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.ecma.e4x;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.IECMAAdditions;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.TypeDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.ecma.TypeDeclarations;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ECMAvE4xAdditions implements IECMAAdditions {

    @Override
    public void addAdditionalTypes(TypeDeclarations typeDecs) {
        typeDecs.addTypeDeclaration(
                TypeDeclarations.ECMA_GLOBAL,
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x",
                        "E4XGlobal", "Global", false, false));

        typeDecs.addTypeDeclaration(
                TypeDeclarations.ECMA_NAMESPACE,
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x",
                        "E4XNamespace", "Namespace", false, false));

        typeDecs.addTypeDeclaration(
                TypeDeclarations.ECMA_QNAME,
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x",
                        "E4XQName", "QName", false, false));

        typeDecs.addTypeDeclaration(
                TypeDeclarations.ECMA_XML,
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x",
                        "E4XXML", "XML", false, false));

        typeDecs.addTypeDeclaration(
                TypeDeclarations.ECMA_XMLLIST,
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x",
                        "E4XXMLList", "XMLList", false, false));

        typeDecs.addTypeDeclaration(
                "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.functions.E4XGlobalFunctions",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.functions",
                        "IE4XGlobalFunctions", "Global", false, false));

        typeDecs.addTypeDeclaration(
                "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.functions.E4XXMLFunctions",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.functions",
                        "IE4XXMLFunctions", "XML", false, false));

        typeDecs.addTypeDeclaration(
                "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.functions.IE4XXMLListFunctions",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.e4x.functions",
                        "IE4XXMLListFunctions", "XMList", false, false));

        // XML lookups for constructors and class completions
        typeDecs.addECMAObject(TypeDeclarations.ECMA_NAMESPACE, true);
        typeDecs.addECMAObject(TypeDeclarations.ECMA_QNAME, true);
        typeDecs.addECMAObject(TypeDeclarations.ECMA_XML, true);
        typeDecs.addECMAObject(TypeDeclarations.ECMA_XMLLIST, true);
    }

}