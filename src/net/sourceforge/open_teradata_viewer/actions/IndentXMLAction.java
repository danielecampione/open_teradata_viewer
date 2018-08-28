/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C), D. Campione
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

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.ThreadedAction;
import net.sourceforge.open_teradata_viewer.editor.OTVSyntaxTextArea;
import net.sourceforge.open_teradata_viewer.editor.xml_tools.XMLBeautifier;

public class IndentXMLAction extends CustomAction {

    private static final long serialVersionUID = -7312181464359474079L;

    protected IndentXMLAction() {
        super("Pretty print (XML only - with line breaks)", null, null, null);
        setEnabled(true);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // The "indent XML" process can be performed altough other processes are
        // running
        new ThreadedAction() {
            @Override
            protected void execute() throws Exception {
                performThreaded(e);
            }
        };
    }

    @Override
    protected void performThreaded(ActionEvent e) throws Exception {
        setEnabled(false);
        OTVSyntaxTextArea textArea = ApplicationFrame.getInstance().getTextComponent();
        XMLBeautifier xmlBeautifier = new XMLBeautifier(textArea.getTabSize());
        String unformattedXML = textArea.getText();
        String formattedXML = unformattedXML;
        unformattedXML = xmlBeautifier.validateXML(unformattedXML);
        try {
            formattedXML = xmlBeautifier.indentXML(unformattedXML);
        } catch (Throwable t) {
            ExceptionDialog.notifyException(t);
        }

        textArea.setText(formattedXML);
        setEnabled(true);
    }
}