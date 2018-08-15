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

/**
 * Class/interface access flag masks.
 *
 * @author D. Campione
 * 
 */
public interface IAccessFlags {

    /** Declared public; may be accessed from outside its package. */
    public static final int ACC_PUBLIC = 0x0001;

    /** Declared private; usable only within the defining class. */
    public static final int ACC_PRIVATE = 0x0002;

    /** Declared protected; may be accessed within subclasses. */
    public static final int ACC_PROTECTED = 0x0004;

    /** Declared static. */
    public static final int ACC_STATIC = 0x0008;

    /** Declared final; no subclasses allowed. */
    public static final int ACC_FINAL = 0x0010;

    /**
     * Treat superclass methods specially when invoked by the
     * <em>invokespecial</em> instruction.
     */
    /* NOTE: This is the same value as ACC_SYNCHRONIZED */
    public static final int ACC_SUPER = 0x0020;

    /** Declared synchronized; invocation is wrapped in a monitor block. */
    /* NOTE: This is the same value as ACC_SUPER */
    public static final int ACC_SYNCHRONIZED = 0x0020;

    /** Declared volatile; cannot be cached. */
    public static final int ACC_VOLATILE = 0x0040;

    /**
     * Declared transient; not written or read by a persistent object manager.
     */
    public static final int ACC_TRANSIENT = 0x0080;

    /** Declared native; implemented in a language other than Java. */
    public static final int ACC_NATIVE = 0x0100;

    /** Is an interface, not a class. */
    public static final int ACC_INTERFACE = 0x0200;

    /** Declared abstract; may not be instantiated. */
    public static final int ACC_ABSTRACT = 0x0400;

    /** Declared strictfp; floating-point mode is FP-strict. */
    public static final int ACC_STRICT = 0x0800;

    /** Declared <code>synthetic</codeL; not present in the source code. */
    public static final int ACC_SYNTHETIC = 0x1000;

    /** Declared as an annotation type. */
    public static final int ACC_ANNOTATION = 0x2000;

    /** Declared as an enum type. */
    public static final int ACC_ENUM = 0x4000;
}