/*
 * Open Teradata Viewer ( animated assistant )
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

package net.sourceforge.open_teradata_viewer.animated_assistant;

import net.sourceforge.open_teradata_viewer.util.UtilException;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class GeneratorException extends UtilException {

    private static final long serialVersionUID = 7511052718694458088L;

    public GeneratorException() {
    }

    public GeneratorException(int code) {
        super(code);
    }

    public GeneratorException(int code, Throwable cause) {
        super(code, cause);
    }

    public GeneratorException(int code, Object[] argCode) {
        super(code, argCode);
    }

    public GeneratorException(String message) {
        super(message);
    }

    public GeneratorException(String code, String message) {
        super(code, message);
    }

    public GeneratorException(String message, Object[] pars) {
        super(message, pars);
    }

    public GeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeneratorException(Throwable cause) {
        super(cause);
    }

    static {
        registerErrorMessages(GeneratorException.class,
                new GeneratorErrorMessages());
    }

}
