/*
 * Open Teradata Viewer ( editor syntax parser )
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

package net.sourceforge.open_teradata_viewer.editor.syntax.parser;

import java.net.URL;

import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxDocument;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;
import net.sourceforge.open_teradata_viewer.editor.syntax.focusabletip.FocusableTip;

/**
 * An interface for a parser for content in an {@link SyntaxTextArea}. A
 * <code>IParser</code> returns a list of issues it finds in the text area's
 * content, which the text area can flag (e.g. squiggle underline). It can also
 * return descriptions of the issues, to be used in tool tips.<p>
 * 
 * To install a <code>IParser</code>, simply call {@link
 * SyntaxTextArea#addParser(IParser)}.
 *
 * @author D. Campione
 * @see AbstractParser
 * 
 */
public interface IParser {

    /**
     * Returns the listener for hyperlink events from {@link FocusableTip}s, or
     * <code>null</code> if none.
     *
     * @return The listener.
     */
    public IExtendedHyperlinkListener getHyperlinkListener();

    /**
     * Returns the base URL for any images displayed in returned
     * {@link IParserNotice} HTML text. Note that if a parser notice's text is
     * not HTML, this URL is not used.
     *
     * @return The URL.  This may be <code>null</code>.
     */
    public URL getImageBase();

    /**
     * Returns whether this parser is enabled. If this returns
     * <code>false</code>, it will not be run.
     *
     * @return Whether this parser is enabled.
     */
    public boolean isEnabled();

    /**
     * Parses input from the specified document.
     *
     * @param doc The document to parse. This document is in a read lock, so it
     *            cannot be modified while parsing is occurring.
     * @param style The language being rendered.
     * @return An object describing the section of the document parsed and the
     *         results. This is guaranteed to be non-<code>null</code>.
     */
    public IParseResult parse(SyntaxDocument doc, String style);
}