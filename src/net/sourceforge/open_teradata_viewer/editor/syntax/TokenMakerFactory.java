/*
 * Open Teradata Viewer ( editor syntax )
 * Copyright (C) 2013, D. Campione
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

import java.security.AccessControlException;
import java.util.Set;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.syntax.modes.PlainTextTokenMaker;

/**
 * A factory that maps syntax styles to {@link ITokenMaker}s capable of
 * splitting text into tokens for those syntax styles.
 *
 * @author D. Campione
 * 
 */
public abstract class TokenMakerFactory {

    /**
     * If this system property is set, a custom <code>TokenMakerFactory</code>
     * of the specified class will be used as the default token maker factory.
     */
    public static final String PROPERTY_DEFAULT_TOKEN_MAKER_FACTORY = "TokenMakerFactory";

    /** The singleton default <code>TokenMakerFactory</code> instance. */
    private static TokenMakerFactory DEFAULT_INSTANCE;

    /**
     * Returns the default <code>TokenMakerFactory</code> instance. This is the
     * factory used by all {@link SyntaxDocument}s by default.
     *
     * @return The factory.
     * @see #setDefaultInstance(TokenMakerFactory)
     */
    public static synchronized TokenMakerFactory getDefaultInstance() {
        if (DEFAULT_INSTANCE == null) {
            String clazz = null;
            try {
                clazz = System
                        .getProperty(PROPERTY_DEFAULT_TOKEN_MAKER_FACTORY);
            } catch (AccessControlException ace) {
                clazz = null; // We're in an applet; take default
            }
            if (clazz == null) {
                clazz = "net.sourceforge.open_teradata_viewer.editor.syntax.DefaultTokenMakerFactory";
            }
            try {
                DEFAULT_INSTANCE = (TokenMakerFactory) Class.forName(clazz)
                        .newInstance();
            } catch (RuntimeException re) {
                throw re;
            } catch (Exception e) {
                ExceptionDialog.notifyException(e);
                throw new InternalError("Cannot find TokenMakerFactory: "
                        + clazz);
            }
        }
        return DEFAULT_INSTANCE;
    }

    /**
     * Returns a {@link ITokenMaker} for the specified key.
     *
     * @param key The key.
     * @return The corresponding <code>ITokenMaker</code>, or {@link
     *         PlainTextTokenMaker} if none matches the specified key.
     */
    public final ITokenMaker getTokenMaker(String key) {
        ITokenMaker tm = getTokenMakerImpl(key);
        if (tm == null) {
            tm = new PlainTextTokenMaker();
        }
        return tm;
    }

    /**
     * Returns a {@link ITokenMaker} for the specified key.
     *
     * @param key The key.
     * @return The corresponding <code>ITokenMaker</code>, or <code>null</code>
     *         if none matches the specified key.
     */
    protected abstract ITokenMaker getTokenMakerImpl(String key);

    /**
     * Returns the set of keys that this factory maps to token makers.
     *
     * @return The set of keys.
     */
    public abstract Set<?> keySet();

    /**
     * Sets the default <code>TokenMakerFactory</code> instance. This is the
     * factory used by all future {@link SyntaxDocument}s by default.
     * <code>SyntaxDocument</code>s that have already been created are not
     * affected.
     *
     * @param tmf The factory.
     * @throws IllegalArgumentException If <code>tmf</code> is
     *         <code>null</code>.
     * @see #getDefaultInstance()
     */
    public static synchronized void setDefaultInstance(TokenMakerFactory tmf) {
        if (tmf == null) {
            throw new IllegalArgumentException("tmf cannot be null");
        }
        DEFAULT_INSTANCE = tmf;
    }
}