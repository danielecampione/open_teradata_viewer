/*
 * Open Teradata Viewer ( editor language support java classreader )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Utility methods for this package.
 *
 * @author D. Campione
 * 
 */
public class Util implements IAccessFlags {

    /** Private constructor to prevent instantiation. */
    private Util() {
    }

    /**
     * @return Whether an object has default scope.
     * @see #isDefault(int)
     * @see #isPrivate(int)
     * @see #isPublic(int)
     */
    public static boolean isDefault(int accessFlags) {
        int access = ACC_PUBLIC | ACC_PROTECTED | ACC_PRIVATE;
        return (accessFlags & access) == 0;
    }

    /**
     * @return Whether an object has private scope.
     * @see #isDefault(int)
     * @see #isPrivate(int)
     * @see #isPublic(int)
     */
    public static boolean isPrivate(int accessFlags) {
        return (accessFlags & ACC_PRIVATE) > 0;
    }

    /**
     * @return Whether an object has protected scope.
     * @see #isDefault(int)
     * @see #isPrivate(int)
     * @see #isPublic(int)
     */
    public static boolean isProtected(int accessFlags) {
        return (accessFlags & ACC_PROTECTED) > 0;
    }

    /**
     * @return Whether an object has public scope.
     * @see #isDefault(int)
     * @see #isPrivate(int)
     * @see #isPublic(int)
     */
    public static boolean isPublic(int accessFlags) {
        return (accessFlags & ACC_PUBLIC) > 0;
    }

    /**
     * Fully skips a given number of bytes in an input stream.
     *
     * @param in The input stream.
     * @param count The number of bytes to skip.
     * @throws IOException If an IO error occurs.
     */
    public static void skipBytes(DataInputStream in, int count)
            throws IOException {
        int skipped = 0;
        while (skipped < count) {
            skipped += in.skipBytes(count - skipped);
        }
    }
}