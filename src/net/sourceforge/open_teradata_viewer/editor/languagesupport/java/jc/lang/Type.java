/*
 * Open Teradata Viewer ( editor language support java jc lang )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang;

import java.util.ArrayList;
import java.util.List;

/**
 * A type.
 *
 * <pre>
 * Type:
 *    Identifier [TypeArguments] { . Identifier [TypeArguments] } {[]}
 *    BasicType
 * </pre>
 *
 * @author D. Campione
 * 
 */
public class Type {

    private List<String> identifiers;
    private List<List<TypeArgument>> typeArguments;
    private int bracketPairCount;

    public Type() {
        identifiers = new ArrayList<String>(1);
        typeArguments = new ArrayList<List<TypeArgument>>(1);
    }

    public Type(String identifier) {
        this();
        addIdentifier(identifier, null);
    }

    public Type(String identifier, int bracketPairCount) {
        this();
        addIdentifier(identifier, null);
        setBracketPairCount(bracketPairCount);
    }

    /**
     * Adds an identifier to this type.
     *
     * @param identifier The identifier.
     * @param typeArgs The type arguments for the identifier. This may be
     *        <code>null</code> or an empty list if there are none.
     */
    public void addIdentifier(String identifier, List<TypeArgument> typeArgs) {
        identifiers.add(identifier);
        typeArguments.add(typeArgs);
    }

    public int getIdentifierCount() {
        return identifiers.size();
    }

    /**
     * Returns the name of this type.
     *
     * @param fullyQualified Whether the returned value should be fully
     *        qualified.
     * @return The name of this type. This will include type arguments, if any.
     * @see #getName(boolean, boolean)
     */
    public String getName(boolean fullyQualified) {
        return getName(fullyQualified, true);
    }

    /**
     * Returns the name of this type.
     *
     * @param fullyQualified Whether the returned value should be fully
     *        qualified.
     * @param addTypeArgs Whether type arguments should be at the end of the
     *        returned string, if any.
     * @return The name of this type.
     * @see #getName(boolean)
     */
    public String getName(boolean fullyQualified, boolean addTypeArgs) {
        StringBuilder sb = new StringBuilder();

        int count = identifiers.size();
        int start = fullyQualified ? 0 : count - 1;
        for (int i = start; i < count; i++) {
            sb.append(identifiers.get(i).toString());
            if (addTypeArgs && typeArguments.get(i) != null) {
                List<TypeArgument> typeArgs = typeArguments.get(i);
                int typeArgCount = typeArgs.size();
                if (typeArgCount > 0) {
                    sb.append('<');
                    for (int j = 0; j < typeArgCount; j++) {
                        TypeArgument typeArg = typeArgs.get(j);
                        sb.append(typeArg.toString());
                        if (j < typeArgCount - 1) {
                            sb.append(", ");
                        }
                    }
                    sb.append('>');
                }
            }
            if (i < count - 1) {
                sb.append('.');
            }
        }

        for (int i = 0; i < bracketPairCount; i++) {
            sb.append("[]");
        }

        return sb.toString();
    }

    public List<TypeArgument> getTypeArguments(int index) {
        return typeArguments.get(index);
    }

    /*
     * MethodDeclaratorRest allows bracket pairs after its FormalParameters,
     * which increment the array depth of the return type.
     */
    public void incrementBracketPairCount(int count) {
        bracketPairCount += count;
    }

    /** @return Whether this type is an array. */
    public boolean isArray() {
        return bracketPairCount > 0;
    }

    public boolean isBasicType() {
        boolean basicType = false;
        if (!isArray() && identifiers.size() == 1
                && typeArguments.get(0) == null) {
            String str = identifiers.get(0);
            basicType = "byte".equals(str) || "float".equals(str)
                    || "double".equals(str) || "int".equals(str)
                    || "short".equals(str) || "long".equals(str)
                    || "boolean".equals(str);
        }
        return basicType;
    }

    public void setBracketPairCount(int count) {
        bracketPairCount = count;
    }

    /**
     * Returns a string representation of this type. The type name will be fully
     * qualified.
     *
     * @return A string representation of this type.
     * @see #getName(boolean)
     */
    @Override
    public String toString() {
        return getName(true);
    }
}