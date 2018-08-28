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

package net.sourceforge.open_teradata_viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import net.sourceforge.open_teradata_viewer.util.SwingUtil;

import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;

/**
 * Facade for common UI-related tasks
 * 
 * @author D. Campione
 */

public class UISupport {

    private static List<ClassLoader> secondaryResourceLoaders = new ArrayList<ClassLoader>();

    private static Component frame;
    public static Dimension TOOLBAR_BUTTON_DIMENSION;
    private static Boolean isWindows;
    private static Boolean isMac;

    private static IXDialogs dialogs;
    private static IXFileDialogs fileDialogs;
    private static Cursor hourglassCursor;
    private static Cursor defaultCursor;
    private static Boolean isHeadless;

    public static final String DEFAULT_EDITOR_FONT = "Courier plain";
    public static final int DEFAULT_EDITOR_FONT_SIZE = 11;

    static {
        frame = ApplicationFrame.getInstance();
        setDialogs(new SwingDialogs(frame));

        if (!isHeadless()) {
            TOOLBAR_BUTTON_DIMENSION = new Dimension(22, 21);
        }
    }

    /**
     * Add a classloader to find resources.
     * 
     * @param loader
     */
    public static void addClassLoader(ClassLoader loader) {
        secondaryResourceLoaders.add(loader);
    }

    /**
     * Set the main frame of this application. This is only used when running
     * under Swing.
     * 
     * @param frame
     */
    public static void setMainFrame(Component frame) {
        UISupport.frame = frame;
        setDialogs(new SwingDialogs(frame));
        setFileDialogs(new SwingFileDialogs(frame));
    }

    public static URL findSplash(String filename) {
        File file = new File(filename);
        URL url = null;

        try {
            if (!file.exists())
                url = UISupport.class.getResource("/icons/" + filename);
            else
                url = file.toURI().toURL();
        } catch (Exception e) {
            ExceptionDialog.ignoreException(e);
        }

        return url;
    }

    public static void setDialogs(IXDialogs iXDialogs) {
        dialogs = iXDialogs;
    }

    public static void setFileDialogs(IXFileDialogs iXFileDialogs) {
        fileDialogs = iXFileDialogs;
    }

    public static Frame getMainFrame() {
        return (Frame) (frame instanceof Frame ? frame : null);
    }

    public static Frame getParentFrame(Component component) {
        for (Container c = component.getParent(); c != null; c = c.getParent()) {
            if (c instanceof Frame) {
                return (Frame) c;
            }
        }
        return getMainFrame();
    }

    public static IXDialogs getDialogs() {
        return dialogs;
    }

    public static IXFileDialogs getFileDialogs() {
        return fileDialogs;
    }

    public static boolean confirm(String question, String title) {
        return dialogs.confirm(question, title);
    }

    public static int yesYesToAllOrNo(String question, String title) {
        return dialogs.yesYesToAllOrNo(question, title);
    }

    public static String prompt(String question, String title, String value) {
        return dialogs.prompt(question, title, value);
    }

    /**
     * @deprecated use prompt(String question, String title, String value)
     *             instead
     */

    @Deprecated
    public static String prompt(String question, String title) {
        return dialogs.prompt(question, title);
    }

    public static boolean stopCellEditing(JTable table) {
        try {
            int column = table.getEditingColumn();
            if (column > -1) {
                TableCellEditor cellEditor = table.getColumnModel()
                        .getColumn(column).getCellEditor();
                if (cellEditor == null) {
                    cellEditor = table.getDefaultEditor(table
                            .getColumnClass(column));
                }
                if (cellEditor != null) {
                    cellEditor.stopCellEditing();
                }
            }
        } catch (RuntimeException re) {
            return false;
        }
        return true;
    }

    public static JPanel createProgressBarPanel(JProgressBar progressBar,
            int space, boolean indeterimate) {
        JPanel panel = new JPanel(new BorderLayout());

        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setString("");
        progressBar.setIndeterminate(indeterimate);

        progressBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1,
                Color.LIGHT_GRAY));

        panel.setBorder(BorderFactory.createEmptyBorder(space, space, space,
                space));
        panel.add(progressBar, BorderLayout.CENTER);

        return panel;
    }

    public static JSplitPane createHorizontalSplit() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerSize(10);
        splitPane.setOneTouchExpandable(true);
        return splitPane;
    }

    public static JSplitPane createHorizontalSplit(Component leftComponent,
            Component rightComponent) {
        JSplitPane splitPane = createHorizontalSplit();

        splitPane.setLeftComponent(leftComponent);
        splitPane.setRightComponent(rightComponent);
        return splitPane;
    }

    public static JSplitPane createVerticalSplit() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerSize(10);
        splitPane.setOneTouchExpandable(true);
        splitPane.setBorder(null);
        return splitPane;
    }

    public static JSplitPane createVerticalSplit(Component topComponent,
            Component bottomComponent) {
        JSplitPane splitPane = createVerticalSplit();

        splitPane.setLeftComponent(topComponent);
        splitPane.setRightComponent(bottomComponent);
        return splitPane;
    }

    public static void centerDialog(Window dialog) {
        centerDialog(dialog, dialog.getOwner());
    }

    public static void centerDialog(Window dialog, Window owner) {
        Dimension sz = dialog.getSize();
        Rectangle b = frame == null ? null : frame.getBounds();

        if ((owner != null) && SwingUtil.isVisible(owner)) {
            b = owner.getBounds();
        } else if (b == null) {
            GraphicsEnvironment ge = GraphicsEnvironment
                    .getLocalGraphicsEnvironment();
            b = ge.getDefaultScreenDevice().getDefaultConfiguration()
                    .getBounds();
        }

        dialog.setLocation(
                (int) ((b.getWidth() - sz.getWidth()) / 2) + (int) b.getX(),
                (int) ((b.getHeight() - sz.getHeight()) / 2) + (int) b.getY());
    }

    public static void showDialog(JDialog dialog) {
        centerDialog(dialog);
        dialog.setVisible(true);
    }

    public static boolean isHeadless() {
        if (isHeadless == null) {
            isHeadless = GraphicsEnvironment.isHeadless();
        }

        return isHeadless.booleanValue();
    }

    public static void showInfoMessage(String message) {
        dialogs.showInfoMessage(message);
    }

    public static void showInfoMessage(String message, String title) {
        dialogs.showInfoMessage(message, title);
    }

    public static <T extends Object> T prompt(String question, String title,
            T[] objects) {
        return (T) dialogs.prompt(question, title, objects);
    }

    public static <T extends Object> T prompt(String question, String title,
            T[] objects, String value) {
        return (T) dialogs.prompt(question, title, objects, value);
    }

    public static JButton createToolbarButton(Action action) {
        JButton result = new JButton(action);
        result.setPreferredSize(TOOLBAR_BUTTON_DIMENSION);
        result.setText("");
        return result;
    }

    public static JButton createToolbarButton(Action action, boolean enabled) {
        JButton result = createToolbarButton(action);
        result.setEnabled(enabled);
        return result;
    }

    public static JPanel createTabPanel(JTabbedPane tabs, boolean addBorder) {
        GradientPanel panel = new GradientPanel(new BorderLayout());

        Color color = UIManager.getDefaults().getColor("Panel.background");
        Color darker = color.darker();
        panel.setForeground(new Color((color.getRed() + darker.getRed()) / 2,
                (color.getGreen() + darker.getGreen()) / 2,
                (color.getBlue() + darker.getBlue()) / 2));

        if ((tabs.getTabPlacement() == JTabbedPane.LEFT)
                || (tabs.getTabPlacement() == JTabbedPane.RIGHT)) {
            panel.setDirection(GradientPanel.VERTICAL);
        }

        panel.add(tabs, BorderLayout.CENTER);

        if (addBorder) {
            if (tabs.getTabPlacement() == JTabbedPane.TOP) {
                panel.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0,
                        Color.GRAY));
            } else {
                panel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0,
                        Color.GRAY));
            }
        }

        tabs.setBorder(null);

        return panel;
    }

    public static void showPopup(JPopupMenu popup, JComponent invoker, Point p) {
        popup.setInvoker(invoker);

        popup.setLocation(
                (int) (invoker.getLocationOnScreen().getX() + p.getX()),
                (int) (invoker.getLocationOnScreen().getY() + p.getY()));
        popup.setVisible(true);
    }

    public static Boolean confirmOrCancel(String question, String title) {
        return dialogs.confirmOrCancel(question, title);
    }

    public static JPanel buildPanelWithToolbar(JComponent top,
            JComponent content) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(top, BorderLayout.NORTH);
        p.add(content, BorderLayout.CENTER);

        return p;
    }

    public static JPanel buildPanelWithToolbarAndStatusBar(JComponent top,
            JComponent content, JComponent bottom) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(top, BorderLayout.NORTH);
        p.add(content, BorderLayout.CENTER);
        p.add(bottom, BorderLayout.SOUTH);

        return p;
    }

    public static Dimension getPreferredButtonSize() {
        return TOOLBAR_BUTTON_DIMENSION;
    }

    public static void showErrorMessage(Throwable ex) {
        if (ex.toString().length() > 100) {
            dialogs.showExtendedInfo("Error", "An error of type "
                    + ex.getClass().getSimpleName() + " occured.",
                    ex.toString(), null);
        } else {
            dialogs.showErrorMessage(ex.toString());
        }
    }

    public static Component wrapInEmptyPanel(JComponent component, Border border) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(component, BorderLayout.CENTER);
        panel.setBorder(border);

        return panel;
    }

    public static void setHourglassCursor() {
        if (frame == null) {
            return;
        }

        if (hourglassCursor == null) {
            hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
        }

        frame.setCursor(hourglassCursor);
    }

    public static void resetCursor() {
        if (frame == null) {
            return;
        }

        if (defaultCursor == null) {
            defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        }

        frame.setCursor(defaultCursor);
    }

    public static JXToolBar createToolbar() {
        JXToolBar toolbar = new JXToolBar();
        toolbar.addSpace(1);
        toolbar.setRollover(true);
        toolbar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.SINGLE);
        toolbar.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
        toolbar.setMinimumSize(new Dimension(20, 20));
        return toolbar;
    }

    public static JXToolBar createSmallToolbar() {
        JXToolBar toolbar = new JXToolBar();
        toolbar.addSpace(1);
        toolbar.setRollover(true);
        toolbar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.SINGLE);
        toolbar.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        toolbar.setMinimumSize(new Dimension(20, 20));
        return toolbar;
    }

    public static DescriptionPanel buildDescription(String title,
            String string, ImageIcon icon) {
        return new DescriptionPanel(title, string, icon);
    }

    public static void setPreferredHeight(Component component, int heigth) {
        component.setPreferredSize(new Dimension((int) component
                .getPreferredSize().getWidth(), heigth));
    }

    public static void initDialogActions(final JDialog dialog,
            Action helpAction, JButton defaultButton) {
        dialog.getRootPane()
                .getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ESCAPE");
        dialog.getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {

            private static final long serialVersionUID = -8231069748876641051L;

            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });

        if (defaultButton != null) {
            dialog.getRootPane().setDefaultButton(defaultButton);
        }

        if (helpAction != null) {
            dialog.getRootPane()
                    .getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                    .put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "HELP");
            dialog.getRootPane().getActionMap().put("HELP", helpAction);
        }
    }

    public static <T extends JComponent> T addTitledBorder(T component,
            String title) {
        component.setBorder(BorderFactory.createCompoundBorder(BorderFactory
                .createEmptyBorder(3, 0, 0, 0), BorderFactory
                .createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                BorderFactory.createEmptyBorder(), title),
                        component.getBorder())));

        return component;
    }

    public static void beep() {
        Toolkit.getDefaultToolkit().beep();
    }

    public static <T extends Object> T prompt(String question, String title,
            List<T> objects) {
        return (T) dialogs.prompt(question, title, objects.toArray());
    }

    public static <T extends Object> T prompt(String question, String title,
            List<T> objects, String value) {
        return (T) dialogs.prompt(question, title, objects.toArray(), value);
    }

    public static void showExtendedInfo(String title, String description,
            String content, Dimension size) {
        dialogs.showExtendedInfo(title, description, content, size);
    }

    public static boolean confirmExtendedInfo(String title, String description,
            String content, Dimension size) {
        return dialogs.confirmExtendedInfo(title, description, content, size);
    }

    public static Boolean confirmOrCancelExtendedInfo(String title,
            String description, String content, Dimension size) {
        return dialogs.confirmOrCancleExtendedInfo(title, description, content,
                size);
    }

    public static JButton createActionButton(Action action, boolean enabled) {
        JButton button = createToolbarButton(action, enabled);
        action.putValue(Action.NAME, null);
        return button;
    }

    public static String selectXPath(String title, String info, String xml,
            String xpath) {
        return dialogs.selectXPath(title, info, xml, xpath);
    }

    public static <T extends JComponent> T setFixedSize(T component,
            Dimension size) {
        component.setMinimumSize(size);
        component.setMaximumSize(size);
        component.setPreferredSize(size);
        component.setSize(size);

        return component;
    }

    public static <T extends JComponent> T setFixedSize(T component, int i,
            int j) {
        return setFixedSize(component, new Dimension(i, j));
    }

    public static void setFixedColumnSize(TableColumn column, int width) {
        column.setWidth(width);
        column.setPreferredWidth(width);
        column.setMaxWidth(width);
        column.setMinWidth(width);
    }

    public static JButton createToolbarButton(ImageIcon icon) {
        JButton result = new JButton(icon);
        result.setPreferredSize(TOOLBAR_BUTTON_DIMENSION);
        return result;
    }

    public static char[] promptPassword(String question, String title) {
        return dialogs.promptPassword(question, title);
    }

    public static JPanel createEmptyPanel(int top, int left, int bottom,
            int right) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(top, left, bottom,
                right));
        return panel;
    }
}