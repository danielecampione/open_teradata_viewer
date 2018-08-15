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

import java.io.DataInputStream;
import java.io.IOException;

import net.sourceforge.open_teradata_viewer.editor.languagesupport.java.classreader.constantpool.ConstantClassInfo;

/**
 * An entry in the exception table of a {@link Code} attribute. This denotes
 * either a <tt>catch</tt> or <tt>finally</tt> block (the section of code it
 * covers, the type of <tt>Throwable</tt> it handles and the location of the
 * exception handler code).
 *
 * @author D. Campione
 * 
 */
public class ExceptionTableEntry {

    /** The parent class file. */
    private ClassFile cf;

    /**
     * Start of the range in the code array at which the exception handler is
     * active.
     */
    private int startPC;

    /**
     * End of the range in the code array at which the exception handler is
     * active. This value must be either a valid index into the code array of
     * the opcode of an instruction or be equal to {@link Code#getCodeLength()}.
     */
    private int endPC;

    /**
     * The start of the exception handler. This value must be a valid index into
     * the code array and must be the index of the opcode of an instruction.
     */
    private int handlerPC;

    /**
     * If the value of {@link #catchType} is nonzero, it must be a valid index
     * into the constant pool. The constant pool entry at that index must be a
     * {@link ConstantClassInfo} structure representing a class of exceptions
     * that this exception handler is designated to catch. This class must be
     * the class <code>Throwable</code> or one of its subclasses.
     * The exception handler will be called only if the thrown exception is an
     * instance of the given class or one of its subclasses.<p>
     *
     * If the value of {@link #catchType} is zero, this exception handler is for
     * all exceptions. This is used to implement <code>finally</code>.
     */
    private int catchType;

    /**
     * Ctor.
     *
     * @param cf The parent class file.
     */
    public ExceptionTableEntry(ClassFile cf) {
        this.cf = cf;
    }

    /**
     * Returns the name of the <tt>Throwable</tt> type caught and handled by
     * this exception handler.
     *
     * @param fullyQualified Whether the name should be fully qualified.
     * @return The name of the <tt>Throwable</tt> type or <code>null</code> if
     *         this entry denotes a <tt>finally</tt> block.
     */
    public String getCaughtThrowableType(boolean fullyQualified) {
        return catchType == 0 ? null : cf.getClassNameFromConstantPool(
                catchType, fullyQualified);
    }

    public int getEndPC() {
        return endPC;
    }

    public int getHandlerPC() {
        return handlerPC;
    }

    public int getStartPC() {
        return startPC;
    }

    /**
     * Reads an exception table entry from an input stream.
     *
     * @param cf The class file.
     * @param in The input stream to read from.
     * @return The exception table entry.
     * @throws IOException If an IO error occurs.
     */
    public static ExceptionTableEntry read(ClassFile cf, DataInputStream in)
            throws IOException {
        ExceptionTableEntry entry = new ExceptionTableEntry(cf);
        entry.startPC = in.readUnsignedShort();
        entry.endPC = in.readUnsignedShort();
        entry.handlerPC = in.readUnsignedShort();
        entry.catchType = in.readUnsignedShort();
        return entry;
    }
}