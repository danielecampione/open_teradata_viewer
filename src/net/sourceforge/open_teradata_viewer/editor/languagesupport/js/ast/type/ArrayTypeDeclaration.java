/*
 * Open Teradata Viewer ( editor language support js ast type )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.js.ast.type;

/**
 * Extended TypeDeclaration that stores the TypeDeclaration for the Array.
 * e.g
 * var a = [1, 2, 3]; // Array Type - Number
 * var b = ["","",""]; // Array Type - String
 * var c = [1, "", true] // Array Type - any (Default)
 *
 * This is used to determine the type of object in the array when setting
 * variables:
 * e.g
 * var a = [1, 2, 3]; // Array Type - Number
 * var d = a[1]; // var d is resolved as a Number
 * 
 * @author D. Campione
 * 
 */
public class ArrayTypeDeclaration extends TypeDeclaration {

    private TypeDeclaration arrayType;

    public ArrayTypeDeclaration(String pkg, String apiName, String jsName,
            boolean staticsOnly) {
        super(pkg, apiName, jsName, staticsOnly);
    }

    public ArrayTypeDeclaration(String pkg, String apiName, String jsName) {
        super(pkg, apiName, jsName);
    }

    public TypeDeclaration getArrayType() {
        return arrayType;
    }

    public void setArrayType(TypeDeclaration containerType) {
        this.arrayType = containerType;
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals = super.equals(obj);

        if (equals) {
            // Check the container types
            ArrayTypeDeclaration objArrayType = (ArrayTypeDeclaration) obj;

            if (getArrayType() == null && objArrayType.getArrayType() == null) {
                return false;
            }

            if (getArrayType() == null && objArrayType.getArrayType() != null) {
                return false;
            }

            if (getArrayType() != null && objArrayType.getArrayType() == null) {
                return false;
            }
            // Else
            return getArrayType().equals(
                    ((ArrayTypeDeclaration) obj).getArrayType());

        }
        return equals;
    }
}