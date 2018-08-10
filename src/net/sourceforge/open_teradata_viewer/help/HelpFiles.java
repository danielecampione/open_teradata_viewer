/*
 * Open Teradata Viewer ( help )
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

package net.sourceforge.open_teradata_viewer.help;

import java.io.File;

import net.sourceforge.open_teradata_viewer.Tools;

/**
 * This class contains information about files and directories used by the
 * application.
 *
 * @author D. Campione
 */
public class HelpFiles {

    public static String helpFolder = "help";

    public HelpFiles() {
        super();
    }

    /**
     * @return the quickstart guide.
     */
    public File getQuickStartGuideFile() {
        return new File(Tools.conformizePath(System
                .getProperty("java.io.tmpdir"))
                + HelpFiles.helpFolder
                + File.separator + "manual.html");
    }

    /**
     * @return the FAQ.
     */
    public File getFAQFile() {
        return new File(Tools.conformizePath(System
                .getProperty("java.io.tmpdir"))
                + HelpFiles.helpFolder
                + File.separator + "FAQ.html");
    }

    /**
     * @return the changelog.
     */
    public File getChangeLogFile() {
        return new File(Tools.conformizePath(System
                .getProperty("java.io.tmpdir"))
                + HelpFiles.helpFolder
                + File.separator + "changes.html");
    }
    /**
     * @return the licence file.
     */
    public File getLicenceFile() {
        return new File(Tools.conformizePath(System
                .getProperty("java.io.tmpdir"))
                + HelpFiles.helpFolder
                + File.separator + "license.html");
    }
}
