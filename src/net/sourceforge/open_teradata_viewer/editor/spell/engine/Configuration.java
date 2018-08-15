/*
 * Open Teradata Viewer ( editor spell engine )
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

package net.sourceforge.open_teradata_viewer.editor.spell.engine;

import java.security.AccessControlException;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * The various settings used to control how a spell checker works are read from
 * here.
 * Includes the COST_* constants that decide how to figure the cost of
 * converting one word to another in the EditDistance class.<p/>
 *
 * Also includes SPELL_* constants that control how misspellings are detected,
 * for example, how to handle mixed-case words, etc..
 *
 * @author D. Campione
 * @see EditDistance
 * 
 */
public abstract class Configuration {

    /**
     * Used by EditDistance: the cost of having to remove a character.<br/>
     * (Integer greater than 0).
     */
    public static final String COST_REMOVE_CHAR = "EDIT_DEL1";

    /**
     * Used by EditDistance: the cost of having to insert a character.<br/>
     * (Integer greater than 0).
     */
    public static final String COST_INSERT_CHAR = "EDIT_DEL2";

    /**
     * Used by EditDistance: the cost of having to swap two adjoining
     * characters.
     * For the swap value to ever be used, it should be smaller than the
     * COST_REMOVE_CHAR or COST_INSERT_CHAR values.<br/>
     * (Integer greater than 0).
     */
    public static final String COST_SWAP_CHARS = "EDIT_SWAP";

    /**
     * Used by EditDistance: the cost of having to change case, for example,
     * from i to I.<br/>
     * (Integer greater than 0).
     */
    public static final String COST_CHANGE_CASE = "EDIT_CASE";

    /**
     * Used by EditDistance: the cost of having to substitute one character for
     * another.
     * For the sub value to ever be used, it should be smaller than the
     * COST_REMOVE_CHAR or COST_INSERT_CHAR values.<br/>
     * (Integer greater than 0).
     */
    public static final String COST_SUBST_CHARS = "EDIT_SUB";

    /**
     * The maximum cost of suggested spelling. Any suggestions that cost more
     * are thrown away.<br/>
     * (Integer greater than 1).
     */
    public static final String SPELL_THRESHOLD = "SPELL_THRESHOLD";

    /**
     * Words that are all upper case are not spell checked, example:
     * "CIA".<br/>
     * (boolean).
     */
    public static final String SPELL_IGNOREUPPERCASE = "SPELL_IGNOREUPPERCASE";

    /**
     * Words that have mixed case are not spell checked, example:
     * "SpellChecker".<br/>
     * (boolean).
     */
    public static final String SPELL_IGNOREMIXEDCASE = "SPELL_IGNOREMIXEDCASE";

    /**
     * Words that look like an Internet address are not spell checked, example:
     * "http://openteradata.sourceforge.net".<br/>
     * (boolean).
     */
    public static final String SPELL_IGNOREINTERNETADDRESSES = "SPELL_IGNOREINTERNETADDRESS";

    /**
     * Words that have digits in them are not spell checked, example:
     * "mach5".<br/>
     * (boolean).
     */
    public static final String SPELL_IGNOREDIGITWORDS = "SPELL_IGNOREDIGITWORDS";

    /** (boolean). */
    public static final String SPELL_IGNOREMULTIPLEWORDS = "SPELL_IGNOREMULTIPLEWORDS";

    /**
     * The first word of a sentence is expected to start with an upper case
     * letter.<br/>
     * (boolean).
     */
    public static final String SPELL_IGNORESENTENCECAPITALIZATION = "SPELL_IGNORESENTENCECAPTILIZATION";

    /**
     * Whether to ignore words that are a single letter (common in programming).
     */
    public static final String SPELL_IGNORESINGLELETTERS = "SPELL_IGNORESINGLELETTERS";

    /**
     * Gets one of the integer constants.
     * 
     * @param key One of the integer constants defined in this class.
     * @return int Value of the setting.
     */
    public abstract int getInteger(String key);

    /**
     * Gets one of the boolean constants.
     * 
     * @param key One of the boolean constants defined in this class.
     * @return boolean Value of the setting.
     */
    public abstract boolean getBoolean(String key);

    /**
     * Sets one of the integer constants.
     * 
     * @param key One of the integer constants defined in this class.
     * @param value New integer value of the constant.
     */
    public abstract void setInteger(String key, int value);

    /**
     * Sets one of the boolean constants.
     * 
     * @param key One of the boolean constants defined in this class.
     * @param value New boolean value of this setting.
     */
    public abstract void setBoolean(String key, boolean value);

    /** Gets a new default Configuration. */
    public static final Configuration getConfiguration() {
        try {
            String config = System.getProperty("jazzy.config");
            if (config != null && config.length() > 0) {
                return getConfiguration(config);
            }
        } catch (AccessControlException e) {
            ExceptionDialog.hideException(e);
        }
        return getConfiguration(null);
    }

    /**
     * Returns a new instance of a Configuration class.
     * 
     * @param className The class to return, must be based on Configuration.
     */
    public static final Configuration getConfiguration(String className) {
        Configuration result;

        if (className != null && className.length() > 0) {
            try {
                result = (Configuration) Class.forName(className).newInstance();
            } catch (InstantiationException e) {
                result = new PropertyConfiguration();
            } catch (IllegalAccessException e) {
                result = new PropertyConfiguration();
            } catch (ClassNotFoundException e) {
                result = new PropertyConfiguration();
            }
        } else {
            result = new PropertyConfiguration();
        }
        return result;
    }
}