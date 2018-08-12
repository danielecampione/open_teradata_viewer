/*
 * Open Teradata Viewer ( animated assistant )
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

package net.sourceforge.open_teradata_viewer.animated_assistant;

import net.sourceforge.open_teradata_viewer.util.ErrorMessages;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class GeneratorErrorMessages extends ErrorMessages {

    public static final int OTVGEN_01001_INIT = 1001;
    public static final int OTVGEN_01002_NEXT_VALUE = 1002;
    public static final int OTVGEN_01003_NO_CURR_VALUE = 1003;
    public static final int OTVGEN_01004_MIN_MAX_VALUE = 1004;

    public GeneratorErrorMessages() {
        this("OTVGEN");
    }

    public GeneratorErrorMessages(String prefix) {
        super(GeneratorErrorMessages.class, prefix);
    }

}