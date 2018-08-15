/*
 * Open Teradata Viewer ( editor language support )
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

package net.sourceforge.open_teradata_viewer.editor.languagesupport;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.tree.DefaultTreeCellRenderer;

import net.sourceforge.open_teradata_viewer.actions.GoToMemberAction;
import net.sourceforge.open_teradata_viewer.editor.syntax.PopupWindowDecorator;
import net.sourceforge.open_teradata_viewer.editor.syntax.SyntaxTextArea;
import net.sourceforge.open_teradata_viewer.editor.syntax.focusabletip.TipUtil;

/**
 * A popup window that displays a text field and tree, allowing the user to jump
 * to a specific part of code in the current source file.
 *
 * @author D. Campione
 * @see GoToMemberAction
 */
public class GoToMemberWindow extends JWindow {

    private static final long serialVersionUID = 1437197199723853767L;

    private SyntaxTextArea textArea;
    private JTextField field;
    private AbstractSourceTree tree;
    private Listener listener;

    /**
     * Ctor.
     *
     * @param parent The parent window (hosting the text component).
     * @param textArea The text area.
     * @param tree The source tree appropriate for the current language.
     */
    public GoToMemberWindow(Window parent, SyntaxTextArea textArea,
            AbstractSourceTree tree) {
        super(parent);
        this.textArea = textArea;
        ComponentOrientation o = parent.getComponentOrientation();
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.setBorder(TipUtil.getToolTipBorder());

        listener = new Listener();
        addWindowFocusListener(listener);
        parent.addComponentListener(listener);

        field = createTextField();
        contentPane.add(field, BorderLayout.NORTH);

        this.tree = tree;
        tree.setSorted(true);
        tree.setShowMajorElementsOnly(true);
        tree.setGotoSelectedElementOnClick(false);
        tree.setFocusable(false);
        tree.listenTo(textArea);
        tree.addMouseListener(listener);
        JScrollPane sp = new JScrollPane(tree);
        sp.setBorder(null);
        sp.setViewportBorder(BorderFactory.createEmptyBorder());
        contentPane.add(sp);

        Color bg = TipUtil.getToolTipBackground();
        setBackground(bg);
        field.setBackground(bg);
        tree.setBackground(bg);
        ((DefaultTreeCellRenderer) tree.getCellRenderer())
                .setBackgroundNonSelectionColor(bg);

        // Give apps a chance to decorate us with drop shadows, etc..
        setContentPane(contentPane);
        PopupWindowDecorator decorator = PopupWindowDecorator.get();
        if (decorator != null) {
            decorator.decorate(this);
        }

        applyComponentOrientation(o);
        pack();
        JRootPane pane = getRootPane();
        InputMap im = pane
                .getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "EscapePressed");
        ActionMap am = pane.getActionMap();
        am.put("EscapePressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    /**
     * Creates the text field allowing the user to enter filter text.
     *
     * @return The text field.
     */
    private JTextField createTextField() {
        JTextField field = new JTextField(30);
        field.setUI(new BasicTextFieldUI());
        field.setBorder(new TextFieldBorder());
        field.addActionListener(listener);
        field.addKeyListener(listener);
        field.getDocument().addDocumentListener(listener);
        return field;
    }

    @Override
    public void dispose() {
        listener.uninstall();
        super.dispose();
        // Force refocus of text area to prevent NPE in GoToMemberAction's
        // (TextAction's) getTextComponent(ActinEvent) when a different
        // component is focused and this action is executed twice
        textArea.requestFocusInWindow();
    }

    /**
     * Listens for events in this window.
     * 
     * @author D. Campione
     * 
     */
    private class Listener extends MouseAdapter implements WindowFocusListener,
            ComponentListener, DocumentListener, ActionListener, KeyListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (tree.gotoSelectedElement()) {
                dispose();
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            handleDocumentEvent(e);
        }

        @Override
        public void componentHidden(ComponentEvent e) {
            dispose();
        }

        @Override
        public void componentMoved(ComponentEvent e) {
            dispose();
        }

        @Override
        public void componentResized(ComponentEvent e) {
            dispose();
        }

        @Override
        public void componentShown(ComponentEvent e) {
        }

        private void handleDocumentEvent(DocumentEvent e) {
            tree.filter(field.getText());
            tree.selectFirstNodeMatchingFilter();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            handleDocumentEvent(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
            case KeyEvent.VK_DOWN:
                tree.selectNextVisibleRow();
                break;
            case KeyEvent.VK_UP:
                tree.selectPreviousVisibleRow();
                break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                tree.gotoSelectedElement();
                dispose();
            }
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            handleDocumentEvent(e);
        }

        public void uninstall() {
            field.removeActionListener(this);
            field.getDocument().removeDocumentListener(this);
            tree.removeMouseListener(this);
            removeWindowFocusListener(this);
        }

        @Override
        public void windowGainedFocus(WindowEvent e) {
        }

        @Override
        public void windowLostFocus(WindowEvent e) {
            dispose();
        }
    }

    /**
     * The border for the filtering text field.
     * 
     * @author D. Campione
     * 
     */
    private static class TextFieldBorder implements Border {

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(2, 5, 3, 5);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w,
                int h) {
            g.setColor(UIManager.getColor("controlDkShadow"));
            g.drawLine(x, y + h - 1, x + w - 1, y + h - 1);
        }
    }
}