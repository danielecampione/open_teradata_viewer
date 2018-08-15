/*
 * Open Teradata Viewer ( editor syntax )
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

package net.sourceforge.open_teradata_viewer.editor.syntax;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;

/**
 * Base class for {@link TokenMakerFactory} implementations. A mapping from
 * language keys to the names of {@link ITokenMaker} classes is stored.
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
    private Map<String, Object> tokenMakerMap;

    /** Ctor. */
    protected AbstractTokenMakerFactory() {
        tokenMakerMap = new HashMap<String, Object>();
        initTokenMakerMap();
    }

    /**
     * Returns a {@link ITokenMaker} for the specified key.
     *
     * @param key The key.
     * @return The corresponding <code>ITokenMaker</code>, or <code>null</code>
     *         if none matches the specified key.
     */
    @Override
    protected ITokenMaker getTokenMakerImpl(String key) {
        TokenMakerCreator tmc = (TokenMakerCreator) tokenMakerMap.get(key);
        if (tmc != null) {
            try {
                return tmc.create();
            } catch (RuntimeException re) {
                throw re;
            } catch (Exception e) {
                ExceptionDialog.hideException(e);
            }
        }
        return null;
    }

    /**
     * Populates the mapping from keys to instances of
     * <code>TokenMakerCreator</code>s. Subclasses should override this method
     * and call one of the <code>putMapping</code> overloads to register {@link
     * ITokenMaker}s for syntax constants.
     *
     * @see #putMapping(String, String)
     * @see #putMapping(String, String, ClassLoader)
     */
    protected abstract void initTokenMakerMap();

    /** {@inheritDoc} */
    @Override
    public Set<String> keySet() {
        return tokenMakerMap.keySet();
    }

    /**
     * Adds a mapping from a key to a <code>ITokenMaker</code> implementation
     * class name.
     *
     * @param key The key.
     * @param className The <code>ITokenMaker</code> class name.
     * @see #putMapping(String, String, ClassLoader)
     */
    public void putMapping(String key, String className) {
        putMapping(key, className, null);
    }

    /**
     * Adds a mapping from a key to a <code>TokenMaker</code> implementation
     * class name.
     *
     * @param key The key.
     * @param className The <code>TokenMaker</code> class name.
     * @param cl The class loader to use when loading the class.
     * @see #putMapping(String, String)
     */
    public void putMapping(String key, String className, ClassLoader cl) {
        tokenMakerMap.put(key, new TokenMakerCreator(className, cl));
    }

    /** Wrapper that handles the creation of ITokenMaker instances. */
    private static class TokenMakerCreator {

        private String className;
        private ClassLoader cl;

        public TokenMakerCreator(String className, ClassLoader cl) {
            this.className = className;
            this.cl = cl != null ? cl : getClass().getClassLoader();
        }

        public ITokenMaker create() throws Exception {
            return (ITokenMaker) Class.forName(className, true, cl)
                    .newInstance();
        }
    }
}