/*
 * Open Teradata Viewer ( editor language support js ast type ecma client )
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
public class ClientBrowserAdditions implements IECMAAdditions {

    @Override
    public void addAdditionalTypes(TypeDeclarations typeDecs) {
        // Add browser objects
        typeDecs.addTypeDeclaration(
                "Window",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client",
                        "Window", "Window", false, false));
        typeDecs.addTypeDeclaration(
                "History",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client",
                        "History", "History", false, false));
        typeDecs.addTypeDeclaration(
                "Location",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client",
                        "Location", "Location", false, false));
        typeDecs.addTypeDeclaration(
                "Navigator",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client",
                        "Navigator", "Navigator", false, false));
        typeDecs.addTypeDeclaration(
                "Screen",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client",
                        "Screen", "Screen", false, false));
        typeDecs.addTypeDeclaration(
                "BarProp",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client",
                        "BarProp", "BarProp", false, false));

        typeDecs.addTypeDeclaration(
                "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client.funtions.IHistoryFunctions",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client.funtions",
                        "IHistoryFunctions", "History", false, false));
        typeDecs.addTypeDeclaration(
                "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client.funtions.ILocationFunctions",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client.funtions",
                        "ILocationFunctions", "Location", false, false));
        typeDecs.addTypeDeclaration(
                "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client.funtions.INavigatorFunctions",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client.funtions",
                        "INavigatorFunctions", "Navigator", false, false));
        typeDecs.addTypeDeclaration(
                "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client.funtions.IWindowFunctions",
                new TypeDeclaration(
                        "net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ecma.api.client.funtions",
                        "IWindowFunctions", "Window", false, false));

        typeDecs.addECMAObject("Window", true);
        typeDecs.addECMAObject("History", true);
        typeDecs.addECMAObject("Location", true);
        typeDecs.addECMAObject("Navigator", true);
        typeDecs.addECMAObject("Screen", true);
        typeDecs.addECMAObject("BarProp", true);
    }

}