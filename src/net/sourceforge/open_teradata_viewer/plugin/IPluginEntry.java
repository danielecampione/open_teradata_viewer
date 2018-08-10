/*
 * Open Teradata Viewer ( plugin )
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

package net.sourceforge.open_teradata_viewer.plugin;

/**
 * This interface represents a Plugin entry point.
 *
 * @author D. Campione
 * 
 */
public interface IPluginEntry {

    /** 
     * This method must be used to configure a plugin entry point with some
     * parameters. These parameters are objects that the plugin will use.
     */
    public void initPluginEntry(Object param);

    /**
     * This method must be used to define the sequence of actions that starts
     * the plugin entry.
     */
    public void startPluginEntry();

    /**
     * This method must be used to define the sequence of actions that stops the
     * plugin entry.
     */
    public void stopPluginEntry();

    /**
     * This method must be used to define the sequence of actions that pauses
     * the plugin entry.
     */
    public void pausePluginEntry();
}