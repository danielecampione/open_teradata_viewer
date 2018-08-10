/*
 * Open Teradata Viewer ( editor syntax iParser )
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

import java.util.ArrayList;
import java.util.List;

/**
 * A basic implementation of {@link IParseResult}. Most, if not all,
 * <code>IParser</code>s can return instances of this class.
 *
 * @author D. Campione
 * @see IParser
 * 
 */
public class DefaultParseResult implements IParseResult {

    private IParser iParser;
    private int firstLineParsed;
    private int lastLineParsed;
    private List<IParserNotice> notices;
    private long parseTime;
    private Exception error;

    public DefaultParseResult(IParser iParser) {
        this.iParser = iParser;
        notices = new ArrayList<IParserNotice>();
    }

    /**
     * Adds a iParser notice.
     *
     * @param notice The new notice.
     * @see #clearNotices()
     */
    public void addNotice(IParserNotice notice) {
        notices.add(notice);
    }

    /**
     * Clears any iParser notices in this result.
     *
     * @see #addNotice(IParserNotice)
     */
    public void clearNotices() {
        notices.clear();
    }

    /** {@inheritDoc} */
    public Exception getError() {
        return error;
    }

    /** {@inheritDoc} */
    public int getFirstLineParsed() {
        return firstLineParsed;
    }

    /** {@inheritDoc} */
    public int getLastLineParsed() {
        return lastLineParsed;
    }

    /** {@inheritDoc} */
    public List<IParserNotice> getNotices() {
        return notices;
    }

    /** {@inheritDoc} */
    public long getParseTime() {
        return parseTime;
    }

    /** {@inheritDoc} */
    public IParser getParser() {
        return iParser;
    }

    /**
     * Sets the error that occurred when last parsing the document, if any.
     *
     * @param e The error that occurred, or <code>null</code> if no error
     *         occurred.
     */
    public void setError(Exception e) {
        this.error = e;
    }

    /**
     * Sets the amount of time it took for this iParser to parse the document.
     *
     * @param time The amount of time, in milliseconds.
     * @see #getParseTime()
     */
    public void setParseTime(long time) {
        parseTime = time;
    }

    /**
     * Sets the line range parsed.
     *
     * @param first The first line parsed, inclusive.
     * @param last The last line parsed, inclusive.
     * @see #getFirstLineParsed()
     * @see #getLastLineParsed()
     */
    public void setParsedLines(int first, int last) {
        firstLineParsed = first;
        lastLineParsed = last;
    }
}