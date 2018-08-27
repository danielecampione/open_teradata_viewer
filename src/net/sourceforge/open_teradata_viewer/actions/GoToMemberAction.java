/*
 * Open Teradata Viewer ( kernel )
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

package net.sourceforge.open_teradata_viewer.actions;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

import net.sourceforge.open_teradata_viewer.ExceptionDialog;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.AbstractSourceTree;
import net.sourceforge.open_teradata_viewer.editor.languagesupport.GoToMemberWindow;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;

/**
 * Displays a popup dialog with the "Go to member" tree. Language support
 * implementations that can do in-depth parsing of the source code in an editor
 * can create an {@link AbstractSourceTree} representing that source and add
 * this action to <code>SyntaxTextArea</code>'s input/action maps, so users can
 * easily navigate to functions, methods, etc..<p>
 *
 * The preferred keystroke to bind this action to is Ctrl+Shift+O (Cmd+Shift+O
 * on Mac). Language supports should also be sure to uninstall this shortcut
 * when they are uninstalled themselves.
 *
 * @author D. Campione
 * @see GoToMemberWindow
 */
public class GoToMemberAction extends TextAction {

    private static final long serialVersionUID = -4368637022297532430L;

    /** The outline tree class appropriate for the current language. */
    private Class<?> outlineTreeClass;

    /**
     * Ctor.
     *
     * @param outlineTreeClass A class extending {@link AbstractSourceTree}.
     *        This class must have a no-argument constructor.
     */
    public GoToMemberAction(Class<?> outlineTreeClass) {
        super("GoToType");
        this.outlineTreeClass = outlineTreeClass;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AbstractSourceTree tree = createTree();
        if (tree == null) {
            UIManager.getLookAndFeel().provideErrorFeedback(null);
            return;
        }
        JTextComponent tc = getTextComponent(e);
        if (tc instanceof SyntaxTextArea) {
            SyntaxTextArea textArea = (SyntaxTextArea) tc;
            Window parent = SwingUtilities.getWindowAncestor(textArea);
            GoToMemberWindow gtmw = new GoToMemberWindow(parent, textArea, tree);
            setLocationBasedOn(gtmw, textArea);
            gtmw.setVisible(true);
        } else {
            UIManager.getLookAndFeel().provideErrorFeedback(null);
        }
    }

    /**
     * Creates the outline tree.
     *
     * @return An instance of the outline tree.
     */
    private AbstractSourceTree createTree() {
        AbstractSourceTree tree = null;
        try {
            tree = (AbstractSourceTree) outlineTreeClass.newInstance();
            tree.setSorted(true);
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            ExceptionDialog.hideException(e);
        }
        return tree;
    }

    /**
     * Centers the window in the text area.
     *
     * @param gtmw The window to center.
     * @param textArea The parent text area to center it in.
     */
    private void setLocationBasedOn(GoToMemberWindow gtmw,
            SyntaxTextArea textArea) {
        Rectangle visibleRect = textArea.getVisibleRect();
        Dimension gtmwPS = gtmw.getPreferredSize();
        int x = visibleRect.x + (visibleRect.width - gtmwPS.width) / 2;
        int y = visibleRect.y + (visibleRect.height - gtmwPS.height) / 2;
        Point p = new Point(x, y);
        SwingUtilities.convertPointToScreen(p, textArea);
        gtmw.setLocation(p);
    }
}