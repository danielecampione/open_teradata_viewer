/*
 * Open Teradata Viewer ( editor language support java classreader constantpool )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.constantpool;

/**
 * Constant types used by {@link ConstantPoolInfo}s.
 *
 * @author D. Campione
 * 
 */
interface IConstantTypes {

    public static final int CONSTANT_Class = 7;

    public static final int CONSTANT_Fieldref = 9;

    public static final int CONSTANT_Methodref = 10;

    public static final int CONSTANT_InterfaceMethodref = 11;

    public static final int CONSTANT_String = 8;

    public static final int CONSTANT_Integer = 3;

    public static final int CONSTANT_Float = 4;

    public static final int CONSTANT_Long = 5;

    public static final int CONSTANT_Double = 6;

    public static final int CONSTANT_NameAndType = 12;

    public static final int CONSTANT_Utf8 = 1;
}