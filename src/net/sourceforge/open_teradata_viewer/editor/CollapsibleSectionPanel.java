/*
 * Open Teradata Viewer ( editor )
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

package net.sourceforge.open_teradata_viewer.editor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * A panel that can show or hide contents anchored to its bottom via a shortcut.
 * Those contents "slide" in, since today's applications are all about fancy
 * smancy animations.
 *
 * @author D. Campione
 * 
 */
public class CollapsibleSectionPanel extends JPanel {

    private static final long serialVersionUID = -5631642288716736010L;

    private List<BottomComponentInfo> bottomComponentInfos;
    private BottomComponentInfo currentBci;

    private Timer timer;
    private int tick;
    private int totalTicks = 10;
    private boolean down;

    private static final int FRAME_MILLIS = 10;

    /** Ctor. */
    public CollapsibleSectionPanel() {
        super(new BorderLayout());
        bottomComponentInfos = new ArrayList<BottomComponentInfo>();
        installKeystrokes();
    }

    /**
     * Adds a "bottom component". To show this component, you must call {@link
     * #showBottomComponent(JComponent)} directly. Any previously displayed
     * bottom component will be hidden.
     *
     * @param comp The component to add.
     * @see #addBottomComponent(KeyStroke, JComponent)
     */
    public void addBottomComponent(JComponent comp) {
        addBottomComponent(null, comp);
    }

    /**
     * Adds a "bottom component" and binds its display to a key stroke.
     * Whenever that key stroke is typed in a descendant of this panel, this
     * component will be displayed. You can also display it programmatically by
     * calling {@link #showBottomComponent(JComponent)}.
     *
     * @param ks The key stroke to bind to the display of the component.
     *        If this parameter is <code>null</code>, this method behaves
     *        exactly like the {@link #addBottomComponent(JComponent)} overload.
     * @param comp The component to add.
     * @return An action that displays this component. You can add this action
     *         to a <code>JMenu</code>, for example, to alert the user of a way
     *         to display the component.
     * @see #addBottomComponent(JComponent)
     */
    public Action addBottomComponent(KeyStroke ks, JComponent comp) {
        BottomComponentInfo bci = new BottomComponentInfo(comp);
        bottomComponentInfos.add(bci);

        Action action = null;
        if (ks != null) {
            InputMap im = getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
            im.put(ks, ks);
            action = new ShowBottomComponentAction(ks, bci);
            getActionMap().put(ks, action);
        }
        return action;
    }

    private void createTimer() {
        timer = new Timer(FRAME_MILLIS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tick++;
                if (tick == totalTicks) {
                    timer.stop();
                    timer = null;
                    tick = 0;
                    Dimension finalSize = down ? new Dimension(0, 0)
                            : currentBci.getRealPreferredSize();
                    currentBci.component.setPreferredSize(finalSize);
                    if (down) {
                        remove(currentBci.component);
                        currentBci = null;
                    }
                } else {
                    if (tick == 1) {
                        if (down) {
                            focusMainComponent();
                        } else {
                            // We assume here that the component has some
                            // focusable child we want to play with
                            currentBci.component.requestFocusInWindow();
                        }
                    }
                    float proportion = !down ? (((float) tick) / totalTicks)
                            : (1f - (((float) tick) / totalTicks));
                    Dimension size = new Dimension(
                            currentBci.getRealPreferredSize());
                    size.height = (int) (size.height * proportion);
                    currentBci.component.setPreferredSize(size);
                }
                revalidate();
                repaint();
            }
        });
        timer.setRepeats(true);
    }

    /** Attempt to focus the "center" component of this panel. */
    private void focusMainComponent() {
        Component center = ((BorderLayout) getLayout())
                .getLayoutComponent(BorderLayout.CENTER);
        if (center instanceof JScrollPane) {
            center = ((JScrollPane) center).getViewport().getView();
        }
        center.requestFocusInWindow();
    }

    /**
     * @return The currently displayed bottom component. This will be
     *         <code>null</code> if no bottom component is displayed.
     */
    public JComponent getDisplayedBottomComponent() {
        // If a component is animating in or out, we consider it to be "not
        // displayed"
        if (currentBci != null && (timer == null || !timer.isRunning())) {
            return currentBci.component;
        }
        return null;
    }

    /**
     * Hides the currently displayed "bottom" component with a slide-out
     * animation.
     *
     * @see #showBottomComponent(JComponent)
     */
    public void hideBottomComponent() {
        if (currentBci == null) {
            return;
        }

        if (timer != null) {
            if (down) {
                return; // Already animating away
            }
            timer.stop();
            tick = totalTicks - tick;
        }
        down = true;

        createTimer();
        timer.start();
    }

    /** Installs standard keystrokes for this component. */
    private void installKeystrokes() {
        InputMap im = getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "onEscape");
        am.put("onEscape", new HideBottomComponentAction());
    }

    /**
     * Sets the amount of time, in milliseconds, it should take for a
     * "collapsible panel" to show or hide. The default is <code>120</code>.
     *
     * @param millis The amount of time, in milliseconds.
     */
    public void setAnimationTime(int millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("millis must be > 0");
        }
        totalTicks = Math.max(millis / FRAME_MILLIS, 1);
    }

    /**
     * Displays a new "bottom" component. If a component is currently displayed
     * at the "bottom," it is hidden.
     *
     * @param bci The new bottom component.
     * @see #hideBottomComponent()
     */
    private void showBottomComponent(BottomComponentInfo bci) {
        if (bci.equals(currentBci)) {
            currentBci.component.requestFocusInWindow();
            return;
        }

        // Remove currently displayed bottom component
        if (currentBci != null) {
            remove(currentBci.component);
        }
        currentBci = bci;
        add(currentBci.component, BorderLayout.SOUTH);

        if (timer != null) {
            timer.stop();
        }
        tick = 0;
        down = false;

        // Animate display of new bottom component
        createTimer();
        timer.start();
    }

    /**
     * Displays a previously-registered "bottom component".
     *
     * @param comp A previously registered component.
     * @see #addBottomComponent(JComponent)
     * @see #addBottomComponent(KeyStroke, JComponent)
     * @see #hideBottomComponent()
     */
    public void showBottomComponent(JComponent comp) {
        BottomComponentInfo info = null;
        for (BottomComponentInfo bci : bottomComponentInfos) {
            if (bci.component == comp) {
                info = bci;
                break;
            }
        }

        if (info != null) {
            showBottomComponent(info);
        }
    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (bottomComponentInfos != null) { // First time through
            for (BottomComponentInfo info : bottomComponentInfos) {
                if (!info.component.isDisplayable()) {
                    SwingUtilities.updateComponentTreeUI(info.component);
                }
                info.uiUpdated();
            }
        }
    }

    /**
     * Information about a "bottom component".
     * 
     * @author D. Campione
     * 
     */
    private static class BottomComponentInfo {

        private JComponent component;
        private Dimension _preferredSize;

        public BottomComponentInfo(JComponent component) {
            this.component = component;
        }

        public Dimension getRealPreferredSize() {
            if (_preferredSize == null) {
                _preferredSize = component.getPreferredSize();
            }
            return _preferredSize;
        }

        private void uiUpdated() {
            // Remove explicit size previously set
            component.setPreferredSize(null);
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    private class HideBottomComponentAction extends AbstractAction {

        private static final long serialVersionUID = 8553498001346220887L;

        @Override
        public void actionPerformed(ActionEvent e) {
            hideBottomComponent();
        }
    }

    /**
     * 
     * 
     * @author D. Campione
     *
     */
    private class ShowBottomComponentAction extends AbstractAction {

        private static final long serialVersionUID = -4843026225719293861L;

        private BottomComponentInfo bci;

        public ShowBottomComponentAction(KeyStroke ks, BottomComponentInfo bci) {
            putValue(ACCELERATOR_KEY, ks);
            this.bci = bci;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            showBottomComponent(bci);
        }
    }
}