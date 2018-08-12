/*
 * Open Teradata Viewer ( util variant )
 * Copyright (C) 2013, D. Campione
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

package net.sourceforge.open_teradata_viewer.util.variant;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class VariantType {

    public static final int varUnassigned = 0;
    public static final int varNull = 1;
    public static final int varShort = 2;
    public static final int varInteger = 3;
    public static final int varFloat = 4;
    public static final int varDouble = 5;
    public static final int varLong = 6;
    public static final int varDate = 7;
    public static final int varBoolean = 8;
    public static final int varVariant = 9;
    public static final int varByte = 10;
    public static final int varString = 11;
    public static final int varList = 12;
    public static final int varBinary = 13;
    public static final int varBigDecimal = 14;
    public static final int varBigInteger = 15;
    public static final int varJavaObject = 16;
    public static final int varInputStream = 17;
    public static final int varOutputStream = 18;
    public static final int varTime = 19;
    public static final int varTimestamp = 20;

    public static final int varSubConnectable = 1;
    public static final int varSubSerializable = 2;
    public static final int varSubVariant = 3;

}
