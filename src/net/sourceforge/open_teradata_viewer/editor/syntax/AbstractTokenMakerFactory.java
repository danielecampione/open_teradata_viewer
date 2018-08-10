/*
 * Open Teradata Viewer ( editor syntax )
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

package net.sourceforge.open_teradata_viewer.editor.syntax;

import java.util.Map;
import java.util.Set;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * Base class for {@link TokenMakerFactory} implementations. A
 * <code>java.util.Map</code> maps keys to the names of {@link ITokenMaker}
 * classes.
 *
 * @author D. Campione
 * 
 */
public abstract class AbstractTokenMakerFactory extends TokenMakerFactory {

    /**
     * A mapping from keys to the names of {@link ITokenMaker} implementation
     * class names.  When {@link #getTokenMaker(String)} is called with a key
     * defined in this map, a <code>ITokenMaker</code> of the corresponding type
     * is returned.
     */
    private Map<String, String> tokenMakerMap;

    /** Ctor. */
    protected AbstractTokenMakerFactory() {
        tokenMakerMap = createTokenMakerKeyToClassNameMap();
    }

    /**
     * Creates and returns a mapping from keys to the names of
     * {@link ITokenMaker} implementation classes. When
     * {@link #getTokenMaker(String)} is called with a key defined in this map,
     * a <code>ITokenMaker</code> of the corresponding type is returned.
     *
     * @return The map.
     */
    protected abstract Map createTokenMakerKeyToClassNameMap();

    /**
     * Returns a {@link ITokenMaker} for the specified key.
     *
     * @param key The key.
     * @return The corresponding <code>ITokenMaker</code>, or <code>null</code>
     *         if none matches the specified key.
     */
    protected ITokenMaker getTokenMakerImpl(String key) {
        String clazz = (String) tokenMakerMap.get(key);
        if (clazz != null) {
            try {
                return (ITokenMaker) Class.forName(clazz).newInstance();
            } catch (RuntimeException re) {
                throw re;
            } catch (Exception e) {
                ExceptionDialog.notifyException(e);
            }
        }
        return null;
    }

    /** {@inheritDoc} */
    public Set<String> keySet() {
        return tokenMakerMap.keySet();
    }

    /**
     * Adds a mapping from a key to a <code>ITokenMaker</code> implementation
     * class name.
     *
     * @param key The key.
     * @param className The <code>ITokenMaker</code> class name.
     * @return The previous value for the specified key, or <code>null</code>
     *         if there was none.
     */
    public String putMapping(String key, String className) {
        return (String) tokenMakerMap.put(key, className);
    }
}