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

import java.awt.Graphics2D;

import javax.swing.text.TabExpander;

/**
 * Renders tokens in an instance of {@link SyntaxTextArea}. One instance may
 * render tokens "regularly," another may render visible whitespace, for
 * example.
 *
 * @author D. Campione
 * 
 */
interface ITokenPainter {

    /**
     * Paints this token.
     *
     * @param token The token to render.
     * @param g The graphics context in which to paint.
     * @param x The x-coordinate at which to paint.
     * @param y The y-coordinate at which to paint.
     * @param host The text area this token is in.
     * @param e How to expand tabs.
     * @return The x-coordinate representing the end of the painted text.
     */
    public float paint(IToken token, Graphics2D g, float x, float y,
            SyntaxTextArea host, TabExpander e);

    /**
     * Paints this token.
     *
     * @param token The token to render.
     * @param g The graphics context in which to paint.
     * @param x The x-coordinate at which to paint.
     * @param y The y-coordinate at which to paint.
     * @param host The text area this token is in.
     * @param e How to expand tabs.
     * @param clipStart The left boundary of the clip rectangle in which we're
     *        painting. This optimizes painting by allowing us to not paint
     *        paint when this token is "to the left" of the clip rectangle.
     * @return The x-coordinate representing the end of the painted text.
     */
    public float paint(IToken token, Graphics2D g, float x, float y,
            SyntaxTextArea host, TabExpander e, float clipStart);

    /**
     * Paints this token as it should appear in a selected region of text
     * (assuming painting with a selection-foreground color is enabled in the
     * parent <code>SyntaxTextArea</code>).
     *
     * @param token The token to render.
     * @param g The graphics context in which to paint.
     * @param x The x-coordinate at which to paint.
     * @param y The y-coordinate at which to paint.
     * @param host The text area this token is in.
     * @param e How to expand tabs.
     * @return The x-coordinate representing the end of the painted text.
     */
    public float paintSelected(IToken token, Graphics2D g, float x, float y,
            SyntaxTextArea host, TabExpander e);

    /**
     * Paints this token as it should appear in a selected region of text
     * (assuming painting with a selection-foreground color is enabled in the
     * parent <code>SyntaxTextArea</code>).
     *
     * @param token The token to render.
     * @param g The graphics context in which to paint.
     * @param x The x-coordinate at which to paint.
     * @param y The y-coordinate at which to paint.
     * @param host The text area this token is in.
     * @param e How to expand tabs.
     * @param clipStart The left boundary of the clip rectangle in which we're
     *        painting. This optimizes painting by allowing us to not paint when
     *        this token is "to the left" of the clip rectangle.
     * @return The x-coordinate representing the end of the painted text.
     */
    public float paintSelected(IToken token, Graphics2D g, float x, float y,
            SyntaxTextArea host, TabExpander e, float clipStart);
}