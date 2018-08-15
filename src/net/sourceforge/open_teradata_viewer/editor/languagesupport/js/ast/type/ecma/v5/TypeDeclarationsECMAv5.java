/*
 * Open Teradata Viewer ( editor language support js ast type ecma v5 )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.ecma.v5;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.TypeDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.ecma.v3.TypeDeclarationsECMAv3;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class TypeDeclarationsECMAv5 extends TypeDeclarationsECMAv3 {

    @Override
    protected void loadTypes() {
        // Load all v3 types because all these extend them
        super.loadTypes();
        // Override main types with v5
        addTypeDeclaration(
                ECMA_ARRAY,
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5",
                        "JS5Array", "Array", false, false));
        addTypeDeclaration(
                ECMA_DATE,
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5",
                        "JS5Date", "Date", false, false));
        addTypeDeclaration(
                ECMA_FUNCTION,
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5",
                        "JS5Function", "Function", false, false));
        addTypeDeclaration(
                ECMA_OBJECT,
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5",
                        "JS5Object", "Object", false, false));
        addTypeDeclaration(
                ECMA_STRING,
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5",
                        "JS5String", "String", false, false));
        addTypeDeclaration(
                ECMA_JSON,
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5",
                        "JS5JSON", "JSON", false, false));

        addTypeDeclaration(
                "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.functions.IJS5ArrayFunctions",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.functions",
                        "IJS5ArrayFunctions", "Array", false, false));
        addTypeDeclaration(
                "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.functions.IJS5DateFunctions",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.functions",
                        "IJS5DateFunctions", "Date", false, false));
        addTypeDeclaration(
                "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.functions.IJS5FunctionFunctions",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.functions",
                        "IJS5FunctionFunctions", "Function", false, false));
        addTypeDeclaration(
                "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.functions.IJS5ObjectFunctions",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.functions",
                        "IJS5ObjectFunctions", "Object", false, false));
        addTypeDeclaration(
                "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.functions.IJS5StringFunctions",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma5.functions",
                        "IJS5StringFunctions", "String", false, false));

    }

}