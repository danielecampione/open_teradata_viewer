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


import java.text.ParseException;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;

/**
 * Application arguments.
 *
 * @author D. Campione
 * 
 */
public class ApplicationArguments {

    /**
     * Option descriptions.
     *
     * <UL>
     * <LI>element 0 - short option
     * <LI>element 1 - long option (null if none)
     * <LI>element 2 - option description
     * </UL>
     */
    public interface IOptions {
    }

    /** Only instance of this class. */
    private static volatile ApplicationArguments s_instance;

    /** Project home directory. */
    private String _applicationFrameHome = null;

    /**
     * If not <TT>null</TT> then is an override for the users .OpenTeradataViewer
     * settings directory.
     */
    private String _userSettingsDir = null;

    /**
     * Ctor specifying arguments from command line.
     *
     * @param   args    Arguments passed on command line.
     *
     * @throws  ParseException
     *          Thrown if unable to parse arguments.
     */
    private ApplicationArguments(String[] args) throws ParseException {
        super();
    }

    /**
     * Initialize application arguments.
     *
     * @param   args    Arguments passed on command line.
     *
     * @return  <TT>true</TT> if arguments parsed successfully else
     *          <TT>false<.TT>. If parsing was unsuccessful an error was written
     *          to standard error.
     */
    public synchronized static boolean initialize(String[] args) {
        if (s_instance == null) {
            try {
                s_instance = new ApplicationArguments(args);
            } catch (ParseException ex) {
                return false;
            }
        } else {
            ApplicationFrame.getInstance().changeLog
                    .append("ApplicationArguments.initialize() called twice");
        }
        return true;
    }

    /**
     * Return the single instance of this class.
     *
     * @return the single instance of this class. If initialize() hasn't yet been called, then it is
     * assumed that there are no arguments to the application.
     */
    public static ApplicationArguments getInstance() {
        if (s_instance == null) {
            try {
                s_instance = new ApplicationArguments(new String[]{});
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return s_instance;
    }

    /**
     * @return  override for the user settings directory. Will be
     *              <TT>null</TT> if not overridden.
     */
    public String get_applicationFrameHomeHomeDirectory() {
        return _applicationFrameHome;
    }

    /**
     * @return The name of the directory where the program is installed into.
     */
    public String getUserSettingsDirectoryOverride() {
        return _userSettingsDir;
    }

    /**
     * Resets the internally stored instance so that the next call to initialize
     * will function as the first call.  Useful for unit tests, so it uses package
     * level access.
     */
    static final void reset() {
        s_instance = null;
    }
}