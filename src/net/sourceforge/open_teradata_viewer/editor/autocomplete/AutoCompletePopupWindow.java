/*
 * Open Teradata Viewer ( editor autocomplete )
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

package net.sourceforge.open_teradata_viewer.editor.autocomplete;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ListUI;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;

import net.sourceforge.open_teradata_viewer.ApplicationFrame;
import net.sourceforge.open_teradata_viewer.editor.syntax.PopupWindowDecorator;
import net.sourceforge.open_teradata_viewer.editor.syntax.focusabletip.SizeGrip;
import net.sourceforge.open_teradata_viewer.util.Utilities;

/**
 * The actual popup window of choices. When visible, this window intercepts
 * certain keystrokes in the parent text component and uses them to navigate the
 * completion choices instead. If Enter or Escape is pressed, the window hides
 * itself and notifies the {@link AutoCompletion} to insert the selected text.
 *
 * @author D. Campione
 * 
 */
class AutoCompletePopupWindow extends JWindow implements CaretListener,
        ListSelectionListener, MouseListener {

    private static final long serialVersionUID = -5837857638318646330L;

    /** The parent AutoCompletion instance. */
    private AutoCompletion ac;

    /** The list of completion choices. */
    private JList list;

    /** The contents of {@link #list()}. */
    private CompletionListModel model;

    /**
     * A hack to work around the fact that we clear our completion model (and
     * our selection) when hiding the completion window. This allows us to still
     * know what the user selected after the popup is hidden.
     */
    private ICompletion lastSelection;

    /**
     * Optional popup window containing a description of the currently selected
     * completion.
     */
    private AutoCompleteDescWindow descWindow;

    /**
     * The preferred size of the optional description window. This field only
     * exists because the user may (and usually will) set the size of the
     * description window before it exists (it must be parented to a
     * Window).
     */
    private Dimension preferredDescWindowSize;

    /**
     * Whether the completion window and the optional description window
     * should be displayed above the current caret position (as opposed to
     * underneath it, which is preferred unless there is not enough space).
     */
    private boolean aboveCaret;

    private int lastLine;

    private boolean keyBindingsInstalled;

    private KeyActionPair escapeKap;
    private KeyActionPair upKap;
    private KeyActionPair downKap;
    private KeyActionPair leftKap;
    private KeyActionPair rightKap;
    private KeyActionPair enterKap;
    private KeyActionPair tabKap;
    private KeyActionPair homeKap;
    private KeyActionPair endKap;
    private KeyActionPair pageUpKap;
    private KeyActionPair pageDownKap;
    private KeyActionPair ctrlCKap;

    private KeyActionPair oldEscape, oldUp, oldDown, oldLeft, oldRight,
            oldEnter, oldTab, oldHome, oldEnd, oldPageUp, oldPageDown,
            oldCtrlC;

    /** The space between the caret and the completion popup. */
    private static final int VERTICAL_SPACE = 1;

    /** The class name of the Substance List UI. */
    private static final String SUBSTANCE_LIST_UI = "org.pushingpixels.substance.internal.ui.SubstanceListUI";

    /**
     * Ctor.
     *
     * @param parent The parent window (hosting the text component).
     * @param ac The auto-completion instance.
     */
    public AutoCompletePopupWindow(Window parent, final AutoCompletion ac) {
        super(parent);
        ComponentOrientation o = ac.getTextComponentOrientation();

        this.ac = ac;
        model = new CompletionListModel();
        list = new PopupList(model);

        list.setCellRenderer(new DelegatingCellRenderer());
        list.addListSelectionListener(this);
        list.addMouseListener(this);

        JPanel contentPane = new JPanel(new BorderLayout());
        JScrollPane sp = new JScrollPane(list,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        JPanel corner = new SizeGrip();
        boolean isLeftToRight = o.isLeftToRight();
        String str = isLeftToRight ? JScrollPane.LOWER_RIGHT_CORNER
                : JScrollPane.LOWER_LEFT_CORNER;
        sp.setCorner(str, corner);

        contentPane.add(sp);
        setContentPane(contentPane);
        applyComponentOrientation(o);

        // Give apps a chance to decorate us with drop shadows, etc..
        if (Util.getShouldAllowDecoratingMainAutoCompleteWindows()) {
            PopupWindowDecorator decorator = PopupWindowDecorator.get();
            if (decorator != null) {
                decorator.decorate(this);
            }
        }

        pack();

        setFocusableWindowState(false);

        lastLine = -1;
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        if (isVisible()) { // Should always be true
            int line = ac.getLineOfCaret();
            if (line != lastLine) {
                lastLine = -1;
                setVisible(false);
            } else {
                doAutocomplete();
            }
        } else if (AutoCompletion.getDebug()) {
            Thread.dumpStack();
        }
    }

    /**
     * Creates the description window.
     *
     * @return The description window.
     */
    private AutoCompleteDescWindow createDescriptionWindow() {
        AutoCompleteDescWindow dw = new AutoCompleteDescWindow(this, ac);
        dw.applyComponentOrientation(ac.getTextComponentOrientation());
        Dimension size = preferredDescWindowSize;
        if (size == null) {
            size = getSize();
        }
        dw.setSize(size);
        return dw;
    }

    /**
     * Creates the mappings from keys to Actions we'll be putting into the text
     * component's ActionMap and InputMap.
     */
    private void createKeyActionPairs() {
        // Actions we'll install
        EnterAction enterAction = new EnterAction();
        escapeKap = new KeyActionPair("Escape", new EscapeAction());
        upKap = new KeyActionPair("Up", new UpAction());
        downKap = new KeyActionPair("Down", new DownAction());
        leftKap = new KeyActionPair("Left", new LeftAction());
        rightKap = new KeyActionPair("Right", new RightAction());
        enterKap = new KeyActionPair("Enter", enterAction);
        tabKap = new KeyActionPair("Tab", enterAction);
        homeKap = new KeyActionPair("Home", new HomeAction());
        endKap = new KeyActionPair("End", new EndAction());
        pageUpKap = new KeyActionPair("PageUp", new PageUpAction());
        pageDownKap = new KeyActionPair("PageDown", new PageDownAction());
        ctrlCKap = new KeyActionPair("CtrlC", new CopyAction());

        // Buffers for the actions we replace
        oldEscape = new KeyActionPair();
        oldUp = new KeyActionPair();
        oldDown = new KeyActionPair();
        oldLeft = new KeyActionPair();
        oldRight = new KeyActionPair();
        oldEnter = new KeyActionPair();
        oldTab = new KeyActionPair();
        oldHome = new KeyActionPair();
        oldEnd = new KeyActionPair();
        oldPageUp = new KeyActionPair();
        oldPageDown = new KeyActionPair();
        oldCtrlC = new KeyActionPair();
    }

    protected void doAutocomplete() {
        lastLine = ac.refreshPopupWindow();
    }

    /** @return The copy keystroke to use for this platform. */
    private static final KeyStroke getCopyKeyStroke() {
        int key = KeyEvent.VK_C;
        int mask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        return KeyStroke.getKeyStroke(key, mask);
    }

    /**
     * Returns the default list cell renderer used when a completion provider
     * does not supply its own.
     *
     * @return The default list cell renderer.
     * @see #setListCellRenderer(ListCellRenderer)
     */
    public ListCellRenderer getListCellRenderer() {
        DelegatingCellRenderer dcr = (DelegatingCellRenderer) list
                .getCellRenderer();
        return dcr.getFallbackCellRenderer();
    }

    /**
     * Returns the selected value, or <code>null</code> if nothing is selected.
     *
     * @return The selected value.
     */
    public ICompletion getSelection() {
        return isShowing() ? (ICompletion) list.getSelectedValue()
                : lastSelection;
    }

    /**
     * Inserts the currently selected completion.
     *
     * @see #getSelection()
     */
    private void insertSelectedCompletion() {
        ICompletion comp = getSelection();
        ac.insertCompletion(comp);
    }

    /**
     * Registers keyboard actions to listen for in the text component and
     * intercept.
     *
     * @see #uninstallKeyBindings()
     */
    private void installKeyBindings() {
        if (AutoCompletion.getDebug()) {
            ApplicationFrame.getInstance().getConsole()
                    .println("PopupWindow: Installing keybindings");
        }
        if (keyBindingsInstalled) {
            System.err.println("Error: key bindings were already installed");
            Thread.dumpStack();
            return;
        }

        if (escapeKap == null) { // Lazily create actions
            createKeyActionPairs();
        }

        JTextComponent comp = ac.getTextComponent();
        InputMap im = comp.getInputMap();
        ActionMap am = comp.getActionMap();

        replaceAction(im, am, KeyEvent.VK_ESCAPE, escapeKap, oldEscape);
        if (AutoCompletion.getDebug() && oldEscape.action == escapeKap.action) {
            Thread.dumpStack();
        }
        replaceAction(im, am, KeyEvent.VK_UP, upKap, oldUp);
        replaceAction(im, am, KeyEvent.VK_LEFT, leftKap, oldLeft);
        replaceAction(im, am, KeyEvent.VK_DOWN, downKap, oldDown);
        replaceAction(im, am, KeyEvent.VK_RIGHT, rightKap, oldRight);
        replaceAction(im, am, KeyEvent.VK_ENTER, enterKap, oldEnter);
        replaceAction(im, am, KeyEvent.VK_TAB, tabKap, oldTab);
        replaceAction(im, am, KeyEvent.VK_HOME, homeKap, oldHome);
        replaceAction(im, am, KeyEvent.VK_END, endKap, oldEnd);
        replaceAction(im, am, KeyEvent.VK_PAGE_UP, pageUpKap, oldPageUp);
        replaceAction(im, am, KeyEvent.VK_PAGE_DOWN, pageDownKap, oldPageDown);

        // Make Ctrl+C copy from description window. This isn't done
        // automagically because the description window is not focusable, and
        // copying from text components can only be done from focused components
        KeyStroke ks = getCopyKeyStroke();
        oldCtrlC.key = im.get(ks);
        im.put(ks, ctrlCKap.key);
        oldCtrlC.action = am.get(ctrlCKap.key);
        am.put(ctrlCKap.key, ctrlCKap.action);

        comp.addCaretListener(this);

        keyBindingsInstalled = true;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            insertSelectedCompletion();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * Positions the description window relative to the completion choices
     * window. We assume there is room on one side of the other for this entire
     * window to fit.
     */
    private void positionDescWindow() {
        boolean showDescWindow = descWindow != null && ac.getShowDescWindow();
        if (!showDescWindow) {
            return;
        }

        // Don't use getLocationOnScreen() as this throws an exception if window
        // isn't visible yet, but getLocation() doesn't, and is in screen
        // coordinates
        Point p = getLocation();
        Rectangle screenBounds = Util.getScreenBoundsForPoint(p.x, p.y);

        // Try to position to the right first (LTR)
        int x;
        if (ac.getTextComponentOrientation().isLeftToRight()) {
            x = getX() + getWidth() + 5;
            if (x + descWindow.getWidth() > screenBounds.x + screenBounds.width) { // Doesn't fit
                x = getX() - 5 - descWindow.getWidth();
            }
        } else { // RTL
            x = getX() - 5 - descWindow.getWidth();
            if (x < screenBounds.x) { // Doesn't fit
                x = getX() + getWidth() + 5;
            }
        }

        int y = getY();
        if (aboveCaret) {
            y = y + getHeight() - descWindow.getHeight();
        }

        if (x != descWindow.getX() || y != descWindow.getY()) {
            descWindow.setLocation(x, y);
        }
    }

    /**
     * "Puts back" the original key/Action pair for a keystroke. This is used
     * when this popup is hidden.
     *
     * @param im The input map.
     * @param am The action map.
     * @param key The keystroke whose key/Action pair to change.
     * @param kap The (original) key/Action pair.
     * @see #replaceAction(InputMap, ActionMap, int, KeyActionPair, KeyActionPair)
     */
    private void putBackAction(InputMap im, ActionMap am, int key,
            KeyActionPair kap) {
        KeyStroke ks = KeyStroke.getKeyStroke(key, 0);
        am.put(im.get(ks), kap.action); // Original action for the "new" key
        im.put(ks, kap.key); // Original key for the keystroke
    }

    /**
     * Replaces a key/Action pair in an InputMap and ActionMap with a new pair.
     *
     * @param im The input map.
     * @param am The action map.
     * @param key The keystroke whose information to replace.
     * @param kap The new key/Action pair for <code>key</code>.
     * @param old A buffer in which to place the old key/Action pair.
     * @see #putBackAction(InputMap, ActionMap, int, KeyActionPair)
     */
    private void replaceAction(InputMap im, ActionMap am, int key,
            KeyActionPair kap, KeyActionPair old) {
        KeyStroke ks = KeyStroke.getKeyStroke(key, 0);
        old.key = im.get(ks);
        im.put(ks, kap.key);
        old.action = am.get(kap.key);
        am.put(kap.key, kap.action);
    }

    /**
     * Selects the first item in the completion list.
     *
     * @see #selectLastItem()
     */
    private void selectFirstItem() {
        if (model.getSize() > 0) {
            list.setSelectedIndex(0);
            list.ensureIndexIsVisible(0);
        }
    }

    /**
     * Selects the last item in the completion list.
     *
     * @see #selectFirstItem()
     */
    private void selectLastItem() {
        int index = model.getSize() - 1;
        if (index > -1) {
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }
    }

    /**
     * Selects the next item in the completion list.
     *
     * @see #selectPreviousItem()
     */
    private void selectNextItem() {
        int index = list.getSelectedIndex();
        if (index > -1) {
            index = (index + 1) % model.getSize();
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        }
    }

    /**
     * Selects the completion item one "page down" from the currently selected
     * one.
     *
     * @see #selectPageUpItem()
     */
    private void selectPageDownItem() {
        int visibleRowCount = list.getVisibleRowCount();
        int i = Math.min(list.getModel().getSize() - 1, list.getSelectedIndex()
                + visibleRowCount);
        list.setSelectedIndex(i);
        list.ensureIndexIsVisible(i);
    }

    /**
     * Selects the completion item one "page up" from the currently selected
     * one.
     *
     * @see #selectPageDownItem()
     */
    private void selectPageUpItem() {
        int visibleRowCount = list.getVisibleRowCount();
        int i = Math.max(0, list.getSelectedIndex() - visibleRowCount);
        list.setSelectedIndex(i);
        list.ensureIndexIsVisible(i);
    }

    /**
     * Selects the previous item in the completion list.
     *
     * @see #selectNextItem()
     */
    private void selectPreviousItem() {
        int index = list.getSelectedIndex();
        switch (index) {
        case 0:
            index = list.getModel().getSize() - 1;
            break;
        case -1: // Check for an empty list (would be an error)
            index = list.getModel().getSize() - 1;
            if (index == -1) {
                return;
            }
            break;
        default:
            index = index - 1;
            break;
        }
        list.setSelectedIndex(index);
        list.ensureIndexIsVisible(index);
    }

    /**
     * Sets the completions to display in the choices list. The first completion
     * is selected.
     *
     * @param completions The completions to display.
     */
    public void setCompletions(List<ICompletion> completions) {
        model.setContents(completions);
        selectFirstItem();
    }

    /**
     * Sets the size of the description window.
     *
     * @param size The new size. This cannot be <code>null</code>.
     */
    public void setDescriptionWindowSize(Dimension size) {
        if (descWindow != null) {
            descWindow.setSize(size);
        } else {
            preferredDescWindowSize = size;
        }
    }

    /**
     * Sets the default list cell renderer to use when a completion provider
     * does not supply its own.
     *
     * @param renderer The renderer to use. If this is <code>null</code>, a
     *        default renderer is used.
     * @see #getListCellRenderer()
     */
    public void setListCellRenderer(ListCellRenderer renderer) {
        DelegatingCellRenderer dcr = (DelegatingCellRenderer) list
                .getCellRenderer();
        dcr.setFallbackCellRenderer(renderer);
    }

    /**
     * Sets the location of this window to be "good" relative to the specified
     * rectangle. That rectangle should be the location of the text component's
     * caret, in screen coordinates.
     *
     * @param r The text component's caret position, in screen coordinates.
     */
    public void setLocationRelativeTo(Rectangle r) {
        // Multi-monitor support - make sure the completion window (and
        // description window, if applicable) both fit in the same window in a
        // multi-monitor environment. To do this, we decide which monitor the
        // rectangle "r" is in, and use that one (just pick top-left corner as
        // the defining point)
        Rectangle screenBounds = Util.getScreenBoundsForPoint(r.x, r.y);

        boolean showDescWindow = descWindow != null && ac.getShowDescWindow();
        int totalH = getHeight();
        if (showDescWindow) {
            totalH = Math.max(totalH, descWindow.getHeight());
        }

        // Try putting our stuff "below" the caret first. We assume that the
        // entire height of our stuff fits on the screen one way or the other
        aboveCaret = false;
        int y = r.y + r.height + VERTICAL_SPACE;
        if (y + totalH > screenBounds.height) {
            y = r.y - VERTICAL_SPACE - getHeight();
            aboveCaret = true;
        }

        // Get x-coordinate of completions. Try to align left edge with the
        // caret first
        int x = r.x;
        if (!ac.getTextComponentOrientation().isLeftToRight()) {
            x -= getWidth(); // RTL => align right edge
        }
        if (x < screenBounds.x) {
            x = screenBounds.x;
        } else if (x + getWidth() > screenBounds.x + screenBounds.width) { // Completions don't fit
            x = screenBounds.x + screenBounds.width - getWidth();
        }

        setLocation(x, y);

        // Position the description window, if necessary
        if (showDescWindow) {
            positionDescWindow();
        }
    }

    /**
     * Toggles the visibility of this popup window.
     *
     * @param visible Whether this window should be visible.
     */
    @Override
    public void setVisible(boolean visible) {
        if (visible != isVisible()) {
            if (visible) {
                installKeyBindings();
                lastLine = ac.getLineOfCaret();
                selectFirstItem();
                if (descWindow == null && ac.getShowDescWindow()) {
                    descWindow = createDescriptionWindow();
                    positionDescWindow();
                }
                // descWindow needs a kick-start the first time it's displayed.
                // Also, the newly-selected item in the choices list is probably
                // different from the previous one anyway
                if (descWindow != null) {
                    ICompletion c = (ICompletion) list.getSelectedValue();
                    if (c != null) {
                        descWindow.setDescriptionFor(c);
                    }
                }
            } else {
                uninstallKeyBindings();
            }

            super.setVisible(visible);

            // Some languages, such as Java, can use quite a lot of memory when
            // displaying hundreds of completion choices. We pro-actively clear
            // our list model here to make them available for GC. Otherwise,
            // they stick around, and consider the following: a user starts
            // code-completion for Java 5 SDK classes, then hides the dialog,
            // then changes the "class path" to use a Java 6 SDK instead. On
            // pressing Ctrl+space, a new array of completions is created. If
            // this window holds on to the previous completions, you're getting
            // roughly 2x the necessary completions in memory until the
            // completions are actually passed to this window
            if (!visible) { // Do after super.setVisible(false)
                lastSelection = (ICompletion) list.getSelectedValue();
                model.clear();
            }

            // Must set descWindow's visibility one way or the other each time,
            // because of the way child JWindows' visibility is handled - in
            // some ways it's dependent on the parent, in other ways it's not
            if (descWindow != null) {
                descWindow.setVisible(visible && ac.getShowDescWindow());
            }
        }
    }

    /**
     * Stops intercepting certain keystrokes from the text component.
     *
     * @see #installKeyBindings()
     */
    private void uninstallKeyBindings() {
        if (AutoCompletion.getDebug()) {
            ApplicationFrame.getInstance().getConsole()
                    .println("PopupWindow: Removing keybindings");
        }
        if (!keyBindingsInstalled) {
            return;
        }

        JTextComponent comp = ac.getTextComponent();
        InputMap im = comp.getInputMap();
        ActionMap am = comp.getActionMap();

        putBackAction(im, am, KeyEvent.VK_ESCAPE, oldEscape);
        putBackAction(im, am, KeyEvent.VK_UP, oldUp);
        putBackAction(im, am, KeyEvent.VK_DOWN, oldDown);
        putBackAction(im, am, KeyEvent.VK_LEFT, oldLeft);
        putBackAction(im, am, KeyEvent.VK_RIGHT, oldRight);
        putBackAction(im, am, KeyEvent.VK_ENTER, oldEnter);
        putBackAction(im, am, KeyEvent.VK_TAB, oldTab);
        putBackAction(im, am, KeyEvent.VK_HOME, oldHome);
        putBackAction(im, am, KeyEvent.VK_END, oldEnd);
        putBackAction(im, am, KeyEvent.VK_PAGE_UP, oldPageUp);
        putBackAction(im, am, KeyEvent.VK_PAGE_DOWN, oldPageDown);

        // Ctrl+C
        KeyStroke ks = getCopyKeyStroke();
        am.put(im.get(ks), oldCtrlC.action); // Original action
        im.put(ks, oldCtrlC.key); // Original key

        comp.removeCaretListener(this);

        keyBindingsInstalled = false;
    }

    /**
     * Updates the <tt>LookAndFeel</tt> of this window and the description
     * window.
     */
    public void updateUI() {
        SwingUtilities.updateComponentTreeUI(this);
        if (descWindow != null) {
            descWindow.updateUI();
        }
    }

    /**
     * Called when a new item is selected in the popup list.
     *
     * @param e The event.
     */
    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            Object value = list.getSelectedValue();
            if (value != null && descWindow != null) {
                descWindow.setDescriptionFor((ICompletion) value);
                positionDescWindow();
            }
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    class CopyAction extends AbstractAction {

        private static final long serialVersionUID = 2336112833039588035L;

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean doNormalCopy = false;
            if (descWindow != null && descWindow.isVisible()) {
                doNormalCopy = !descWindow.copy();
            }
            if (doNormalCopy) {
                ac.getTextComponent().copy();
            }
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    class DownAction extends AbstractAction {

        private static final long serialVersionUID = -9133118458164297182L;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isVisible()) {
                selectNextItem();
            }
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    class EndAction extends AbstractAction {

        private static final long serialVersionUID = 8750415837852868313L;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isVisible()) {
                selectLastItem();
            }
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    class EnterAction extends AbstractAction {

        private static final long serialVersionUID = -4238065115131914595L;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isVisible()) {
                insertSelectedCompletion();
            }
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    class EscapeAction extends AbstractAction {

        private static final long serialVersionUID = 7758725740424041354L;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isVisible()) {
                setVisible(false);
            }
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    class HomeAction extends AbstractAction {

        private static final long serialVersionUID = 1515439613863880718L;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isVisible()) {
                selectFirstItem();
            }
        }
    }

    /**
     * A mapping from a key (an Object) to an Action.
     * 
     * @author D. Campione
     * 
     */
    private static class KeyActionPair {

        public Object key;
        public Action action;

        public KeyActionPair() {
        }

        public KeyActionPair(Object key, Action a) {
            this.key = key;
            this.action = a;
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    class LeftAction extends AbstractAction {

        private static final long serialVersionUID = -9179740981295239317L;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isVisible()) {
                JTextComponent comp = ac.getTextComponent();
                Caret c = comp.getCaret();
                int dot = c.getDot();
                if (dot > 0) {
                    c.setDot(--dot);
                    // Ensure moving left hasn't moved us up a line, thus hiding
                    // the popup window
                    if (comp.isVisible()) {
                        if (lastLine != -1) {
                            doAutocomplete();
                        }
                    }
                }
            }
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    class PageDownAction extends AbstractAction {

        private static final long serialVersionUID = -8912740349382113626L;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isVisible()) {
                selectPageDownItem();
            }
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    class PageUpAction extends AbstractAction {

        private static final long serialVersionUID = -211861503498236284L;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isVisible()) {
                selectPageUpItem();
            }
        }
    }

    /**
     * The actual list of completion choices in this popup window.
     * 
     * @author D. Campione
     * 
     */
    private class PopupList extends JList {

        private static final long serialVersionUID = 5699713196106767012L;

        public PopupList(CompletionListModel model) {
            super(model);
        }

        @Override
        public void setUI(ListUI ui) {
            if (Utilities.getUseSubstanceRenderers()
                    && SUBSTANCE_LIST_UI.equals(ui.getClass().getName())) {
                // Substance requires its special ListUI be installed for its
                // renderers to actually render, but long completion lists will
                // simply populate too slowly on initial display (when
                // calculating preferred size of all items), so in this case we
                // give a prototype cell value
                ICompletionProvider p = ac.getCompletionProvider();
                BasicCompletion bc = new BasicCompletion(p, "Hello world");
                setPrototypeCellValue(bc);
            } else {
                // Our custom UI that is faster for long HTML completion lists
                ui = new FastListUI();
                setPrototypeCellValue(null);
            }
            super.setUI(ui);
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    class RightAction extends AbstractAction {

        private static final long serialVersionUID = 867069533272682694L;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isVisible()) {
                JTextComponent comp = ac.getTextComponent();
                Caret c = comp.getCaret();
                int dot = c.getDot();
                if (dot < comp.getDocument().getLength()) {
                    c.setDot(++dot);
                    // Ensure moving right hasn't moved us up a line, thus
                    // hiding the popup window
                    if (comp.isVisible()) {
                        if (lastLine != -1) {
                            doAutocomplete();
                        }
                    }
                }
            }
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     * 
     */
    class UpAction extends AbstractAction {

        private static final long serialVersionUID = 3402215720137484155L;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isVisible()) {
                selectPreviousItem();
            }
        }
    }
}