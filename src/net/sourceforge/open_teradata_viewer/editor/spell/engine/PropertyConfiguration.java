/*
 * Open Teradata Viewer ( editor spell engine )
 * Copyright (C) 2015, D. Campione
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * Implementation class to read the properties controlling the spell engine.
 * The properties are read form the <code>configuration.properties</code> file.
 *
 * @author D. Campione
 * 
 */
public class PropertyConfiguration extends Configuration {

    /** The persistent set of properties supported by the spell engine. */
    public Properties prop;

    /** The name of the file containing spell engine properties. */
    public URL filename;

    /** Constructs and loads spell engine properties configuration. */
    public PropertyConfiguration() {
        prop = new Properties();
        try {
            filename = getClass()
                    .getClassLoader()
                    .getResource(
                            "net/sourceforge/open_teradata_viewer/editor/spell/engine/configuration.properties");
            InputStream in = filename.openStream();
            prop.load(in);
        } catch (Exception e) {
            System.err.println("Could not load Properties file :");
            ExceptionDialog.hideException(e);
        }
    }

    /**
     * @see net.sourceforge.open_teradata_viewer.editor.spell.engine.Configuration#getBoolean(String)
     */
    @Override
    public boolean getBoolean(String key) {
        return new Boolean(prop.getProperty(key)).booleanValue();
    }

    /**
     * @see net.sourceforge.open_teradata_viewer.editor.spell.engine.Configuration#getInteger(String)
     */
    @Override
    public int getInteger(String key) {
        return new Integer(prop.getProperty(key)).intValue();
    }

    /**
     * @see net.sourceforge.open_teradata_viewer.editor.spell.engine.Configuration#setBoolean(String, boolean)
     */
    @Override
    public void setBoolean(String key, boolean value) {
        String string = null;
        if (value) {
            string = "true";
        } else {
            string = "false";
        }

        prop.setProperty(key, string);
        save();
    }

    /**
     * @see net.sourceforge.open_teradata_viewer.editor.spell.engine.Configuration#setInteger(String, int)
     */
    @Override
    public void setInteger(String key, int value) {
        prop.setProperty(key, Integer.toString(value));
        save();
    }

    /**
     * Writes the property list (key and element pairs) in the
     * PropertyConfiguration file.
     */
    public void save() {
        try {
            File file = new File(filename.getFile());
            FileOutputStream fout = new FileOutputStream(file);
            prop.store(fout, "HEADER");
            fout.close();
        } catch (IOException e) {
            ExceptionDialog.ignoreException(e);
        }
    }
}