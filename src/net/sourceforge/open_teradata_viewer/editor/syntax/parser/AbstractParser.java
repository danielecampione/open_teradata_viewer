/*
 * Open Teradata Viewer ( editor syntax parser )
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

package net.sourceforge.open_teradata_viewer.editor.syntax.parser;

import java.net.URL;

import net.sourceforge.open_teradata_viewer.editor.syntax.focusabletip.FocusableTip;

/**
 * A base class for {@link IParser} implementations. Most <code>IParser</code>s
 * should be able to extend this class.
 *
 * @author D. Campione
 * 
 */
public abstract class AbstractParser implements IParser {

    /**
     * Whether this parser is enabled. If this is <code>false</code>, then this
     * parser will not be run.
     */
    private boolean enabled;

    /**
     * Listens for events from {@link FocusableTip}s generated from this
     * parser's notices.
     */
    private IExtendedHyperlinkListener linkListener;

    /** Ctor. */
    protected AbstractParser() {
        setEnabled(true);
    }

    /** {@inheritDoc} */
    public IExtendedHyperlinkListener getHyperlinkListener() {
        return linkListener;
    }

    /**
     * Returns <code>null</code>. Parsers that wish to show images in their tool
     * tips should override this method to return the image base URL.
     *
     * @return <code>null</code> always.
     */
    public URL getImageBase() {
        return null;
    }

    /** {@inheritDoc} */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Toggles whether this parser is enabled.
     *
     * @param enabled Whether this parser is enabled.
     * @see #isEnabled()
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Returns the listener for this parser.
     *
     * @param listener The new listener.
     * @see #getHyperlinkListener()
     */
    public void setHyperlinkListener(IExtendedHyperlinkListener listener) {
        linkListener = listener;
    }
}