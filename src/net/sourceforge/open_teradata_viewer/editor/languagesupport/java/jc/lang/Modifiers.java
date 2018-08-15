/*
 * Open Teradata Viewer ( editor language support java jc lang )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lang;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.jc.lexer.ITokenTypes;

/**
 * Wrapper around modifiers to a member.
 *
 * @author D. Campione
 * 
 */
public class Modifiers {

    public static final Integer ABSTRACT = new Integer(1024);
    public static final Integer FINAL = new Integer(16);
    public static final Integer INTERFACE = new Integer(512);
    public static final Integer NATIVE = new Integer(256);
    public static final Integer PRIVATE = new Integer(2);
    public static final Integer PROTECTED = new Integer(4);
    public static final Integer PUBLIC = new Integer(1);
    public static final Integer STATIC = new Integer(8);
    public static final Integer STRICTFP = new Integer(2048);
    public static final Integer SYNCHRONIZED = new Integer(32);
    public static final Integer TRANSIENT = new Integer(128);
    public static final Integer VOLATILE = new Integer(64);

    private List<Integer> modifiers;
    private List<Annotation> annotations;

    private static final Map<Integer, String> MODIFIER_TEXT = new HashMap<Integer, String>() {

        private static final long serialVersionUID = 5085951604670422846L;

        {
            put(ABSTRACT, "abstract");
            put(FINAL, "final");
            put(INTERFACE, "interface");
            put(NATIVE, "native");
            put(PRIVATE, "private");
            put(PROTECTED, "protected");
            put(PUBLIC, "public");
            put(STATIC, "static");
            put(STRICTFP, "strictfp");
            put(SYNCHRONIZED, "synchronized");
            put(TRANSIENT, "transient");
            put(VOLATILE, "volatile");
        }
    };

    public Modifiers() {
        modifiers = new ArrayList<Integer>(1); // Usually not many
        annotations = new ArrayList<Annotation>(0); // Often 0 or 1 (@Deprecated)
    }

    public void addAnnotation(Annotation annotation) {
        annotations.add(annotation);
    }

    public boolean addModifier(int tokenType) {
        Integer key = null;

        switch (tokenType) {
        case ITokenTypes.KEYWORD_ABSTRACT:
            key = ABSTRACT;
            break;
        case ITokenTypes.KEYWORD_FINAL:
            key = FINAL;
            break;
        case ITokenTypes.KEYWORD_INTERFACE:
            key = INTERFACE;
            break;
        case ITokenTypes.KEYWORD_NATIVE:
            key = NATIVE;
            break;
        case ITokenTypes.KEYWORD_PRIVATE:
            key = PRIVATE;
            break;
        case ITokenTypes.KEYWORD_PROTECTED:
            key = PROTECTED;
            break;
        case ITokenTypes.KEYWORD_PUBLIC:
            key = PUBLIC;
            break;
        case ITokenTypes.KEYWORD_STATIC:
            key = STATIC;
            break;
        case ITokenTypes.KEYWORD_STRICTFP:
            key = STRICTFP;
            break;
        case ITokenTypes.KEYWORD_SYNCHRONIZED:
            key = SYNCHRONIZED;
            break;
        case ITokenTypes.KEYWORD_TRANSIENT:
            key = TRANSIENT;
            break;
        case ITokenTypes.KEYWORD_VOLATILE:
            key = VOLATILE;
            break;
        default:
            throw new IllegalArgumentException("Invalid tokenType: "
                    + tokenType);
        }

        int pos = Collections.binarySearch(modifiers, key);
        if (pos < 0) {
            int insertionPoint = -(pos + 1);
            modifiers.add(insertionPoint, key);
        }

        return pos < 0;
    }

    private boolean containsModifier(Integer modifierKey) {
        return Collections.binarySearch(modifiers, modifierKey) >= 0;
    }

    public boolean isAbstract() {
        return containsModifier(ABSTRACT);
    }

    public boolean isFinal() {
        return containsModifier(FINAL);
    }

    public boolean isPrivate() {
        return containsModifier(PRIVATE);
    }

    public boolean isProtected() {
        return containsModifier(PROTECTED);
    }

    public boolean isPublic() {
        return containsModifier(PUBLIC);
    }

    public boolean isStatic() {
        return containsModifier(STATIC);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < annotations.size(); i++) {
            sb.append(annotations.get(i).toString());
            if (i < annotations.size() - 1 || modifiers.size() > 0) {
                sb.append(' ');
            }
        }
        for (int i = 0; i < modifiers.size(); i++) {
            Integer modifier = modifiers.get(i);
            sb.append(MODIFIER_TEXT.get(modifier));
            if (i < modifiers.size() - 1) {
                sb.append(' ');
            }
        }
        return sb.toString();
    }
}