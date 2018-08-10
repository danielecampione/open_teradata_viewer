/*
 * Open Teradata Viewer ( sql parser )
 * Copyright (C) 2011, D. Campione
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

package net.sourceforge.open_teradata_viewer.sqlparser;

/**
 * An exception class with stack trace informations
 * 
 * @author D. Campione
 * 
 */
public class SQLParserException extends Exception {

    private static final long serialVersionUID = 3622093774920279664L;

    private Throwable cause = null;

    public SQLParserException() {
        super();
    }

    public SQLParserException(String arg0) {
        super(arg0);
    }

    public SQLParserException(Throwable arg0) {
        this.cause = arg0;
    }

    public SQLParserException(String arg0, Throwable arg1) {
        super(arg0);
        this.cause = arg1;
    }

    public Throwable getCause() {
        return cause;
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(java.io.PrintWriter pw) {
        super.printStackTrace(pw);
        if (cause != null) {
            pw.println("Caused by:");
            cause.printStackTrace(pw);
        }
    }

    public void printStackTrace(java.io.PrintStream ps) {
        super.printStackTrace(ps);
        if (cause != null) {
            ps.println("Caused by:");
            cause.printStackTrace(ps);
        }
    }

}
