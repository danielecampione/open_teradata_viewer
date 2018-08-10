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
import java.io.FilenameFilter;
import java.util.Arrays;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.Tools;

/**
 * This class contains information about files and directories used by the
 * application.
 *
 * @author D. Campione
 */
public class ApplicationFiles {

    /** Flag for cleaning up execution log files on app entry. **/
    private static boolean needExecutionLogCleanup = true;

    /** Name of directory to contain users settings. */
    private String _userSettingsDir;

    /** Name of folder that contains Open Teradata Viewer app. */
    private final File _applicationPanelHomeDir;

    /** Name of folder that contains library jars */
    private String _libraryDir;

    /** Name of folder that contains update files */
    private String _updateDir;

    public static String helpFolder = "help";

    /**
     * Ctor.
     */
    public ApplicationFiles() {
        super();
        ApplicationArguments args = ApplicationArguments.getInstance();

        final String homeDir = args.get_applicationPanelHomeHomeDirectory();
        _applicationPanelHomeDir = new File(homeDir != null
                ? homeDir
                : Tools.conformizePath(System.getProperty("user.dir")));
        String homeDirPath = _applicationPanelHomeDir.getPath()
                + File.separator;
        _libraryDir = homeDirPath + "lib";
        _updateDir = homeDirPath + "update";

        _userSettingsDir = args.getUserSettingsDirectoryOverride();
        if (_userSettingsDir == null) {
            _userSettingsDir = Tools.conformizePath(System
                    .getProperty("user.home")) + ".OpenTeradataViewer";
        }
        try {
            new File(_userSettingsDir).mkdirs();
        } catch (Exception ex) {
            ApplicationFrame.getInstance().changeLog.append(
                    "Error creating user settings directory: "
                            + _userSettingsDir + "\n",
                    ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
        }
        try {
            final File logsDir = getExecutionLogFile().getParentFile();
            logsDir.mkdirs();
        } catch (Exception ex) {
            ApplicationFrame.getInstance().changeLog.append(
                    "Error creating logs directory\n",
                    ApplicationFrame.WARNING_FOREGROUND_COLOR_LOG);
        }
    }
    /**
     * @return file to log execution information to.
     */
    public File getExecutionLogFile() {
        final String dirPath = _userSettingsDir + File.separator + "logs";
        final String logBaseName = "squirrel-sql.log";

        if (needExecutionLogCleanup) {
            // first time through this method in program, so go cleanup
            // old log files
            deleteOldFiles(dirPath, logBaseName);
            needExecutionLogCleanup = false;
        }
        return new File(dirPath + File.separator + logBaseName);
    }

    /**
     * Internal method to remove old files such as log files.
     * The dirPath is the path name of the directory containing the files.
     * The fileBase is the base name of all files in the set to be culled,
     * i.e. this method removes old versions of files named <fileBase>*,
     * but not the file named <fileBase> or recent versions of that file.
     * It is assumed that files are named with dates such that the names of
     * older files are alphabetically before newer files.
     */
    private void deleteOldFiles(String dirPath, String fileBase) {

        // the number of files to keep is arbitrarilly set here
        final int numberToKeep = 3;

        /**
         * Define filter to select only names using the fileBase
         * 
         * @author D. Campione
         *
         */
        class OldFileNameFilter implements FilenameFilter {

            String fBase;
            OldFileNameFilter(String fileBase) {
                fBase = fileBase;
            }
            public boolean accept(File dir, String name) {
                if (name.startsWith(fBase))
                    return true;
                return false;
            }
        }

        // get the directory
        File dir = new File(dirPath);

        // create filename filter and attach to directory
        OldFileNameFilter fileFilter = new OldFileNameFilter(fileBase);

        // get list of files using that base name
        String fileNames[] = dir.list(fileFilter);
        if (fileNames == null || fileNames.length <= numberToKeep)
            return; // not too many old files

        // we do not expect a lot of files in this directory,
        // so just do things linearly

        // sort the list
        Arrays.sort(fileNames);

        // If the file using the base name with no extention exists,
        // it is first.  The other files are in order from oldest to newest.
        // The set of files to delete is slightly different depending on
        // whether the base name file exists or not.
        int startIndex = 0;
        int endIndex = fileNames.length - numberToKeep;
        if (fileNames[0].equals(fileBase)) {
            // since the base name file exists, we need to skip it
            // and bump up the endIndex
            startIndex = 1;
            endIndex++;
        }

        for (int i = startIndex; i < endIndex; i++) {
            // delete the old file
            File oldFile = new File(dirPath + File.separator + fileNames[i]);
            oldFile.delete();
        }
    }

    public File getUserSettingsDirectory() {
        return new File(_userSettingsDir);
    }

    public File getLibraryDirectory() {
        return new File(_libraryDir);
    }

    public File getUpdateDirectory() {
        return new File(_updateDir);
    }

    public File get_applicationPanelHomeDir() {
        return _applicationPanelHomeDir;
    }

    /**
     * @return directory that contains plugin specific user settings
     */
    public File getPluginsUserSettingsDirectory() {
        return new File(_userSettingsDir + File.separator + "plugins");
    }

    /**
     * @return the quickstart guide.
     */
    public File getQuickStartGuideFile() {
        return new File(Tools.conformizePath(System
                .getProperty("java.io.tmpdir"))
                + ApplicationFiles.helpFolder
                + File.separator + "manual.html");
    }

    /**
     * @return the FAQ.
     */
    public File getFAQFile() {
        return new File(Tools.conformizePath(System
                .getProperty("java.io.tmpdir"))
                + ApplicationFiles.helpFolder
                + File.separator + "FAQ.html");
    }

    /**
     * @return the changelog.
     */
    public File getChangeLogFile() {
        return new File(Tools.conformizePath(System
                .getProperty("java.io.tmpdir")) + "changes.txt");
    }
    /**
     * @return the licence file.
     */
    public File getLicenceFile() {
        return new File(Tools.conformizePath(System
                .getProperty("java.io.tmpdir")) + "license.txt");
    }
    /**
     * @return the Welcome document..
     */
    public File getWelcomeFile() {
        return new File(Tools.conformizePath(System
                .getProperty("java.io.tmpdir"))
                + ApplicationFiles.helpFolder
                + File.separator + "welcome.html");
    }
}
