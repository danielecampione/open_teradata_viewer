/*
 * Open Teradata Viewer ( editor language support java classreader )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader;

import java.util.Stack;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.attributes.Code;

/**
 * A <code>Frame</code> contains information on a method being decompiled,
 * similar to a Frame as defined in 3.6 of the JVM spec.
 *
 * @author D. Campione
 * 
 */
public class Frame {

    private Stack<String> operandStack;
    private LocalVarInfo[] localVars;

    /**
     * Ctor.
     *
     * @param code The {@link Code} attribute being decompiled.
     */
    public Frame(Code code) {
        operandStack = new Stack<String>();

        localVars = new LocalVarInfo[code.getMaxLocals()];
        int i = 0;
        MethodInfo mi = code.getMethodInfo();

        // Instance methods have an implicit first parameter of "this"
        if (!mi.isStatic()) {
            localVars[i++] = new LocalVarInfo("this", true);
        }

        // Name the passed-in local vars by their types. longs and doubles
        // take up two slots
        String[] paramTypes = mi.getParameterTypes();
        for (int param_i = 0; param_i < paramTypes.length; param_i++) {
            String type = paramTypes[param_i];
            if (type.indexOf('.') > -1) { // Class types
                type = type.substring(type.lastIndexOf('.') + 1);
            }
            String name = "localVar_" + type + "_" + param_i;
            localVars[i] = new LocalVarInfo(name, true);
            i++;
            if ("long".equals(type) || "double".equals(type)) {
                i++; // longs and doubles take up two slots
            }
        }

        // NOTE: Other local vars will still be "null" here. We need to infer
        // their types from their usage during disassembly/decompilation
        System.out.println("NOTE: " + (localVars.length - i)
                + " unknown localVars slots");
    }

    public LocalVarInfo getLocalVar(int index, String defaultType) {
        LocalVarInfo var = localVars[index];
        if (var == null) {
            String name = "localVar_" + defaultType + "_" + index;
            var = new LocalVarInfo(name, false);
            localVars[index] = var;
        } else {
            var.alreadyDeclared = true; // May be redundant
        }
        return var;
    }

    public String pop() {
        return operandStack.pop();
    }

    public void push(String value) {
        operandStack.push(value);
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    public static class LocalVarInfo {

        private String value;
        private boolean alreadyDeclared;

        public LocalVarInfo(String value, boolean alreadyDeclared) {
            this.value = value;
            this.alreadyDeclared = alreadyDeclared;
        }

        public String getValue() {
            return value;
        }

        public boolean isAlreadyDeclared() {
            return alreadyDeclared;
        }
    }
}