/*
 * Open Teradata Viewer ( editor language support java )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport.java;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.Icon;

import net.sourceforge.open_teradata_viewer.editor.autocomplete.ICompletionProvider;
import net.sourceforge.open_teradata_viewer.editor.autocomplete.ShorthandCompletion;

/**
 * A completion for shorthand items that mimics the style seen in Eclipse.
 *
 * @author D. Campione
 *
 */
class JavaShorthandCompletion extends ShorthandCompletion implements
        IJavaSourceCompletion {

    private static final Color SHORTHAND_COLOR = new Color(0, 127, 174);

    /** Ctor. */
    public JavaShorthandCompletion(ICompletionProvider provider,
            String inputText, String replacementText) {
        super(provider, inputText, replacementText);
    }

    /** Ctor. */
    public JavaShorthandCompletion(ICompletionProvider provider,
            String inputText, String replacementText, String shortDesc) {
        super(provider, inputText, replacementText, shortDesc);
    }

    /** {@inheritDoc} */
    @Override
    public Icon getIcon() {
        return IconFactory.get().getIcon(IconFactory.TEMPLATE_ICON);
    }

    /** {@inheritDoc} */
    @Override
    public void rendererText(Graphics g, int x, int y, boolean selected) {
        renderText(g, getInputText(), getReplacementText(), x, y, selected);
    }

    /**
     * Renders a completion in the style of a short-hand completion.
     * 
     * @param g The graphics context.
     * @param input The text the user enters to display this completion.
     * @param shortDesc An optional short description of the completion.
     * @param x The x-offset at which to paint.
     * @param y The y-offset at which to paint.
     * @param selected Whether this completion choice is selected.
     */
    public static void renderText(Graphics g, String input, String shortDesc,
            int x, int y, boolean selected) {
        Color orig = g.getColor();
        if (!selected && shortDesc != null) {
            g.setColor(SHORTHAND_COLOR);
        }
        g.drawString(input, x, y);
        if (shortDesc != null) {
            x += g.getFontMetrics().stringWidth(input);
            if (!selected) {
                g.setColor(orig);
            }
            String temp = " - ";
            g.drawString(temp, x, y);
            x += g.getFontMetrics().stringWidth(temp);
            if (!selected) {
                g.setColor(Color.GRAY);
            }
            g.drawString(shortDesc, x, y);
        }
    }
}