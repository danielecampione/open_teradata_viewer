/*
 * Open Teradata Viewer ( editor syntax parser )
 * Copyright (C) 2015, D. Campione
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

import java.util.EventListener;

import javax.swing.event.HyperlinkEvent;

import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;
import net.sourceforge.open_teradata_viewer.editor.syntax.focusabletip.FocusableTip;

/**
 * Listens for hyperlink events from {@link FocusableTip}s. In addition to the
 * link event, the text area that the tip is for is also received, which allows
 * the listener to modify the displayed content, if desired.
 *
 * @author D. Campione
 * 
 */
public interface IExtendedHyperlinkListener extends EventListener {

    /**
     * Called when a link in a {@link FocusableTip} is clicked.
     *
     * @param textArea The text area displaying the tip.
     * @param e The event.
     */
    public void linkClicked(SyntaxTextArea textArea, HyperlinkEvent e);

}