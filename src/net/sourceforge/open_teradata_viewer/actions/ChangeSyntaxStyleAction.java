/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.editor.OTVSyntaxTextArea;

/**
 * 
 * 
 * @author D. Campione
 *
 */
public class ChangeSyntaxStyleAction extends AbstractAction {

    private static final long serialVersionUID = -247870043878395367L;

    private String style;

    public ChangeSyntaxStyleAction(String name, String style) {
        putValue(NAME, name);
        this.style = style;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ApplicationFrame applicationFrame = ApplicationFrame.getInstance();
        OTVSyntaxTextArea textArea = applicationFrame.getTextComponent();

        textArea.setCaretPosition(0);
        textArea.setSyntaxEditingStyle(style);
        applicationFrame.refreshSourceTree();
    }
}