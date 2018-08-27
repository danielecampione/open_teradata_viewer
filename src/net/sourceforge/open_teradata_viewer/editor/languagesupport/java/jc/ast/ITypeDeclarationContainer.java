/*
 * Open Teradata Viewer ( editor language support java jc ast )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast;

/**
 * Interface for tree nodes that can hold type declarations (e.g. {@link
 * CompilationUnit}s and {@link ITypeDeclaration}s.
 *
 * @author D. Campione
 * 
 */
public interface ITypeDeclarationContainer {

    public void addTypeDeclaration(ITypeDeclaration typeDec);

}