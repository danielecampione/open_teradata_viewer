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

package net.sourceforge.open_teradata_viewer.update;

import java.io.IOException;
import java.net.Proxy;

/**
 * Strategy interface abstracting the retrieval of the latest published
 * version of the application from a remote source.
 * <p>
 * Introducing this abstraction lets the actual data source (currently the
 * project's GitHub repository) be swapped in the future without touching
 * {@link net.sourceforge.open_teradata_viewer.UpdateChecker}, which is only
 * concerned with comparing the local and the remote version and reacting
 * accordingly.
 *
 * @author D. Campione
 * 
 */
public interface IUpdateVersionProvider {

    /**
     * Retrieves a label describing the latest version published on the
     * remote source, formatted exactly like the locally embedded version
     * (see <code>Config#getVersion()</code>), i.e. <code>"X.Y (dd/MM/yyyy)"
     * </code>. This keeps the version-comparison logic in
     * {@link net.sourceforge.open_teradata_viewer.UpdateChecker} completely
     * unaffected by where the label actually comes from.
     *
     * @param proxy the {@link Proxy} to use to reach the remote source (use
     *        {@link Proxy#NO_PROXY} for a direct connection).
     * @return the latest version label.
     * @throws IOException if the remote source could not be queried.
     */
    String getLatestVersionLabel(Proxy proxy) throws IOException;
    
}
