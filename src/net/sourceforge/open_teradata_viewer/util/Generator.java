/*
 * Open Teradata Viewer ( util )
 * Copyright (C), D. Campione
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

package net.sourceforge.open_teradata_viewer.util;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.GeneratorException;
import net.sourceforge.open_teradata_viewer.util.variant.IVariantConnectable;
import net.sourceforge.open_teradata_viewer.util.variant.Variant;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class Generator implements IVariantConnectable {

    private static final long serialVersionUID = 7886277492925405188L;

    public final static int UTL_01001_NOT_INITED = 1001;
    public final static int UTL_01002_CANT_READ = 1002;
    public final static int UTL_01003_CANT_WRITE = 1003;

    private long nextVal;
    private long currVal;
    private long maxVal;
    private long startVal;
    private long increment;
    private boolean inited = false;

    public Generator(long startVal) {
        super();
        nextVal = startVal;
        maxVal = Long.MAX_VALUE;
        this.startVal = startVal;
        increment = 1;
    }

    public Generator() {
        this(1);
    }

    public long getNextVal() {
        synchronized (this) {
            inited = true;
            currVal = nextVal;
            nextVal += increment;
            if (nextVal > maxVal) {
                nextVal = startVal;
            }
            return currVal;
        }
    }

    public void setNextVal(long nextVal) {
        synchronized (this) {
            inited = false;
            this.nextVal = nextVal;
            if (this.nextVal > maxVal) {
                this.nextVal = startVal;
            }
        }
    }

    public long getCurrVal() throws GeneratorException {
        synchronized (this) {
            if (!inited) {
                throw new GeneratorException(UTL_01001_NOT_INITED);
            }
            return currVal;
        }
    }

    public void setMaxVal(long maxVal) {
        synchronized (this) {
            this.maxVal = maxVal;
        }
    }

    public long getMaxVal() {
        return maxVal;
    }

    public void setStartVal(long startVal) {
        synchronized (this) {
            this.startVal = startVal;
        }
    }

    public long getStartVal() {
        return startVal;
    }

    public void setIncrement(long increment) {
        synchronized (this) {
            this.increment = increment;
        }
    }

    public long getIncrement() {
        return increment;
    }

    public void write(DataOutput raf) {
        try {
            raf.writeLong(nextVal);
            raf.writeLong(maxVal);
            raf.writeLong(startVal);
            raf.writeLong(increment);
        } catch (IOException ioe) {
            ExceptionDialog.notifyException((new GeneratorException(
                    UTL_01003_CANT_WRITE, ioe)));
        }
    }

    public void read(DataInput raf) {
        try {
            inited = false;
            nextVal = raf.readLong();
            maxVal = raf.readLong();
            startVal = raf.readLong();
            increment = raf.readLong();
        } catch (IOException ioe) {
            ExceptionDialog.notifyException((new GeneratorException(
                    new GeneratorException(UTL_01002_CANT_READ, ioe))));
        }
    }
    public int compareTo(Variant variant) {
        return 0;
    }

    public int getSize() {
        return 8 + 8 + 8 + 8;
    }

    public Object castTo(int valueType) {
        return null;
    }

    public String toString() {
        return "[nextVal:" + nextVal + ", " + "currVal:" + currVal + ", "
                + "maxVal:" + maxVal + ", " + "startVal:" + startVal + ", "
                + "increment:" + increment + "]";
    }
}