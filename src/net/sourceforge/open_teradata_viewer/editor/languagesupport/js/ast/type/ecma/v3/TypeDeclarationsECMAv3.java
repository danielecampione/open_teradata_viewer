/*
 * Open Teradata Viewer ( editor language support js ast type ecma v3 )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.ecma.v3;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.TypeDeclaration;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type.ecma.TypeDeclarations;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class TypeDeclarationsECMAv3 extends TypeDeclarations {

    @Override
    protected void loadTypes() {
        addTypeDeclaration(
                ECMA_ARRAY,
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3",
                        "JSArray", "Array", false, false));
        addTypeDeclaration(
                ECMA_BOOLEAN,
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3",
                        "JSBoolean", "Boolean", false, false));
        addTypeDeclaration(
                ECMA_DATE,
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3",
                        "JSDate", "Date", false, false));
        addTypeDeclaration(
                ECMA_ERROR,
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3",
                        "JSError", "Error", false, false));
        addTypeDeclaration(
                ECMA_FUNCTION,
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3",
                        "JSFunction", "Function", false, false));
        addTypeDeclaration(
                ECMA_MATH,
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3",
                        "JSMath", "Math", false, false));
        addTypeDeclaration(
                ECMA_NUMBER,
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3",
                        "JSNumber", "Number", false, false));
        addTypeDeclaration(
                ECMA_OBJECT,
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3",
                        "JSObject", "Object", false, false));
        addTypeDeclaration(
                ECMA_REGEXP,
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3",
                        "JSRegExp", "RegExp", false, false));
        addTypeDeclaration(
                ECMA_STRING,
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3",
                        "JSString", "String", false, false));
        addTypeDeclaration(
                ECMA_GLOBAL,
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3",
                        "JSGlobal", "Global", false, false));
        addTypeDeclaration(
                ANY,
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3",
                        "JSUndefined", "undefined", false, false));

        addTypeDeclaration(
                "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions.IJSObjectFunctions",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions",
                        "IJSObjectFunctions", "Object", false, false));
        addTypeDeclaration(
                "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions.IJSArrayFunctions",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions",
                        "IJSArrayFunctions", "Array", false, false));
        addTypeDeclaration(
                "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions.IJSDateFunctions",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions",
                        "IJSDateFunctions", "Date", false, false));
        addTypeDeclaration(
                "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions.IJSFunctionFunctions",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions",
                        "IJSFunctionFunctions", "Function", false, false));
        addTypeDeclaration(
                "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions.IJSNumberFunctions",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions",
                        "IJSNumberFunctions", "Number", false, false));
        addTypeDeclaration(
                "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions.IJSRegExpFunctions",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions",
                        "IJSRegExpFunctions", "RegExp", false, false));
        addTypeDeclaration(
                "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions.IJSStringFunctions",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions",
                        "IJSStringFunctions", "String", false, false));
        addTypeDeclaration(
                "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions.IJSGlobalFunctions",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.ecma3.functions",
                        "IJSGlobalFunctions", "Global", false, false));
    }

}