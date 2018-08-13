/*
 * Open Teradata Viewer ( editor syntax parser )
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

package net.sourceforge.open_teradata_viewer.editor.syntax.parser;

import java.util.List;

/**
 * The result from a {@link IParser}. This contains the section of lines parsed
 * and any notices for that section.
 *
 * @author D. Campione
 * @see DefaultParseResult
 * @see IParserNotice
 * 
 */
public interface IParseResult {

    /**
     * Returns an error that occurred while parsing the document, if any.
     *
     * @return The error, or <code>null</code> if the document was
     *         successfully parsed.
     */
    public Exception getError();

    /**
     * Returns the first line parsed. All parser implementations should
     * currently set this to <code>0</code> and parse the entire document.
     *
     * @return The first line parsed.
     * @see #getLastLineParsed()
     */
    public int getFirstLineParsed();

    /**
     * Returns the first line parsed. All parser implementations should
     * currently set this to the document's line count and parse the entire
     * document.
     *
     * @return The last line parsed.
     * @see #getFirstLineParsed()
     */
    public int getLastLineParsed();

    /**
     * Returns the notices for the parsed section.
     *
     * @return A list of {@link IParserNotice}s.
     */
    public List<IParserNotice> getNotices();

    /**
     * Returns the parser that generated these notices.
     *
     * @return The parser.
     */
    public IParser getParser();

    /**
     * Returns the amount of time this parser took to parse the specified range
     * of text. This is an optional operation; parsers are permitted to return
     * <code>0</code> for this value.
     *
     * @return The parse time, in milliseconds, or <code>0</code> if the parse
     *         time was not recorded.
     */
    public long getParseTime();
}