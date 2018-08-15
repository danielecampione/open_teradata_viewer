/*
 * Open Teradata Viewer ( util variant )
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

package net.sourceforge.open_teradata_viewer.util.variant;

import net.sourceforge.open_teradata_viewer.util.UtilException;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class VariantException extends UtilException {

    private static final long serialVersionUID = 2850431901466224603L;

    public static final int ERR_02001_CANT_WRITE = 2001;
    public static final int ERR_02002_CANT_READ = 2002;
    public static final int ERR_02003_CANT_CONVERT = 2003;
    public static final int ERR_02004_DIVIDE_BY_ZERO = 2004;
    public static final int ERR_02005_READ_ARRAY_PROBLEM = 2005;

    public VariantException() {
        super();
    }

    public VariantException(int code) {
        super(code);
    }

    public VariantException(int code, Throwable cause) {
        super(code, cause);
    }

    public VariantException(int code, Object[] argCode) {
        super(code, argCode);
    }

    public VariantException(String message) {
        super(message);
    }

    public VariantException(String message, Object[] pars) {
        super(message, pars);
    }

    public VariantException(String message, Throwable cause) {
        super(message, cause);
    }

    public VariantException(Throwable cause) {
        super(cause);
    }

}
