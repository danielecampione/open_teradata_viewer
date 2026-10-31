/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The arguments the application is started with: the files to open in the
 * editor, or the request to print the usage statement.
 * <p>
 * An argument that starts with a dash is an option. The arguments that follow
 * a <code>--</code> argument are all file names, even those that look like
 * options. Empty arguments are ignored, as a launcher may pass one when there
 * is no file to open.
 *
 * @author D. Campione
 */
public final class CommandLine {

    private static final String HELP_SHORT = "-h";

    private static final String HELP_LONG = "--help";

    private static final String END_OF_OPTIONS = "--";

    private final List<File> files;

    private final boolean helpRequested;

    private final String invalidOption;

    private CommandLine(List<File> files, boolean helpRequested, String invalidOption) {
        this.files = Collections.unmodifiableList(files);
        this.helpRequested = helpRequested;
        this.invalidOption = invalidOption;
    }

    /**
     * Interprets the arguments of the application.
     *
     * @param args The arguments given to the <code>main</code> method.
     * @return What the arguments ask for. It is never <code>null</code>.
     */
    public static CommandLine parse(String[] args) {
        List<File> files = new ArrayList<>();
        boolean helpRequested = false;
        String invalidOption = null;
        boolean optionsEnded = false;
        for (String arg : args) {
            if (arg == null || arg.trim().isEmpty()) {
                continue;
            }
            if (!optionsEnded && arg.startsWith("-")) {
                if (END_OF_OPTIONS.equals(arg)) {
                    optionsEnded = true;
                } else if (HELP_SHORT.equals(arg) || HELP_LONG.equals(arg)) {
                    helpRequested = true;
                } else if (invalidOption == null) {
                    invalidOption = arg;
                }
            } else {
                // The name is made absolute: it is the one the document of the
                // file keeps
                files.add(new File(arg).getAbsoluteFile());
            }
        }
        return new CommandLine(files, helpRequested, invalidOption);
    }

    /**
     * @return The files to open, in the order they were given. The list cannot
     *         be modified. The files are not checked: they may not exist.
     */
    public List<File> getFiles() {
        return files;
    }

    /**
     * @return <code>true</code> if the usage statement was asked for.
     */
    public boolean isHelpRequested() {
        return helpRequested;
    }

    /**
     * @return The first option that is not known, or <code>null</code> if
     *         there is none.
     */
    public String getInvalidOption() {
        return invalidOption;
    }

    /**
     * @return The usage statement.
     */
    public static String getUsage() {
        String newLine = System.lineSeparator();
        return "Usage: java " + Main.class.getName() + " [options] [file ...]" + newLine + newLine
                + "Starts " + Main.APPLICATION_NAME + ". Each file is opened in a tab of the editor," + newLine
                + "and the first one is shown." + newLine
                + newLine + "Options:" + newLine
                + "  " + HELP_SHORT + ", " + HELP_LONG + "  Print this usage statement and exit." + newLine
                + "  " + END_OF_OPTIONS + "          End of the options: the arguments that follow are file names," + newLine
                + "              even if they start with a dash.";
    }
}
