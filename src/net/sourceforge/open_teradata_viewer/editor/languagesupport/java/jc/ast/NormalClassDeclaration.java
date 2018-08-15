/*
 * Open Teradata Viewer ( editor language support java jc ast )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.ast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.Type;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang.TypeParameter;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.Scanner;

/**
 * A class declaration:
 * 
 * <pre>
 * NormalClassDeclaration:
 *    'class' Identifier [TypeParameters] ['extends' Type] ['implements' TypeList] ClassBody
 * </pre>
 *
 * @author D. Campione
 * 
 */
public class NormalClassDeclaration extends AbstractTypeDeclarationNode {

    // --- "NormalClassDeclaration" fields ---
    private List<TypeParameter> typeParams;
    private Type extendedType;
    private List<Type> implementedList;

    public NormalClassDeclaration(Scanner s, int offs, String className) {
        super(className, s.createOffset(offs), s.createOffset(offs
                + className.length()));
        implementedList = new ArrayList<Type>(0); // Usually not many
        // If parsing java.lang.Object.java, setExtendedType(null) should be
        // called. This is here for all other classes without an explicit super
        // class declared
        extendedType = new Type("java.lang.Object");
    }

    public void addImplemented(Type implemented) {
        implementedList.add(implemented);
    }

    public Type getExtendedType() {
        return extendedType;
    }

    public int getImplementedCount() {
        return implementedList.size();
    }

    public Iterator<Type> getImplementedIterator() {
        return implementedList.iterator();
    }

    /**
     * Gets the method in this class that contains a given offset.
     *
     * @param offs The offset.
     * @return The method containing the offset or <code>null</code> if no
     *         method in this class contains the offset.
     */
    public Method getMethodContainingOffset(int offs) {
        for (Iterator<Method> i = getMethodIterator(); i.hasNext();) {
            Method method = i.next();
            if (method.getBodyContainsOffset(offs)) {
                return method;
            }
        }
        return null;
    }

    public List<TypeParameter> getTypeParameters() {
        return typeParams;
    }

    @Override
    public String getTypeString() {
        return "class";
    }

    /**
     * Returns whether a <code>Type</code> and a type name are type compatible.
     */
    private boolean isTypeCompatible(Type type, String typeName) {
        String typeName2 = type.getName(false);

        int lt = typeName2.indexOf('<');
        if (lt > -1) {
            String arrayDepth = null;
            int brackets = typeName2.indexOf('[', lt);
            if (brackets > -1) {
                arrayDepth = typeName2.substring(brackets);
            }
            typeName2 = typeName2.substring(lt);
            if (arrayDepth != null) {
                typeName2 += arrayDepth;
            }
        }

        return typeName2.equalsIgnoreCase(typeName);
    }

    public void setExtendedType(Type type) {
        extendedType = type;
    }

    public void setTypeParameters(List<TypeParameter> typeParams) {
        this.typeParams = typeParams;
    }
}