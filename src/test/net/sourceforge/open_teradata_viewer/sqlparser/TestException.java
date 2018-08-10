/*
 * Open Teradata Viewer ( sql parser )
 * Copyright (C) 2012, D. Campione
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

package test.net.sourceforge.open_teradata_viewer.sqlparser;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 * An exception class with stack trace information.
 * 
 * @author D. Campione
 * 
 */
public class TestException extends Exception {

    private static final long serialVersionUID = 1838023724397340866L;

    private Throwable cause = null;

    public TestException() {
        super();
    }

    public TestException(String arg0) {
        super(arg0);
    }

    public TestException(Throwable arg0) {
        this.cause = arg0;
    }

    public TestException(String arg0, Throwable arg1) {
        super(arg0);
        this.cause = arg1;
    }

    public Throwable getCause() {
        return cause;
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(PrintWriter pw) {
        super.printStackTrace(pw);
        if (cause != null) {
            pw.println("Caused by:");
            cause.printStackTrace(pw);
        }
    }

    public void printStackTrace(PrintStream ps) {
        super.printStackTrace(ps);
        if (cause != null) {
            ps.println("Caused by:");
            cause.printStackTrace(ps);
        }
    }
}