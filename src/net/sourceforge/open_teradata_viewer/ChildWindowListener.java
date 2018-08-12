/*
 * Open Teradata Viewer ( kernel )
 * Copyright (C) 2013, D. Campione
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

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import net.sourceforge.open_teradata_viewer.editor.search.OTVFindDialog;
import net.sourceforge.open_teradata_viewer.util.TranslucencyUtil;

/**
 * Listens for events in child windows of the application, and toggles the
 * opacity of those child windows if desired. This is done via reflection, since
 * the features we're using were added in Java 6.
 *
 * @author D. Campione
 * 
 */
class ChildWindowListener extends ComponentAdapter
        implements
            WindowFocusListener {

    /**
     * Constant indicating that the child windows should never be translucent.
     */
    public static final int TRANSLUCENT_NEVER = 0;

    /**
     * Indicates that the child windows should be translucent when they are not
     * focused.
     */
    public static final int TRANSLUCENT_WHEN_NOT_FOCUSED = 1;

    /**
     * Indicates that the child windows should be translucent when they are
     * overlapping the main application window.
     */
    public static final int TRANSLUCENT_WHEN_OVERLAPPING_APP = 2;

    /** Indicates that the child windows should always be translucent. */
    public static final int TRANSLUCENT_ALWAYS = 3;

    /** The parent application. */
    private ApplicationFrame app;

    /** When the child windows should be made translucent. */
    private int translucencyRule;

    /**
     * Ctor.
     *
     * @param app The parent application.
     */
    public ChildWindowListener(ApplicationFrame app) {
        this.app = app;
    }

    /** Called when one of the child windows is moved. */
    public void componentMoved(ComponentEvent e) {
        if (translucencyRule == TRANSLUCENT_WHEN_OVERLAPPING_APP) {
            Window w = (Window) e.getComponent();
            if (!w.isShowing()) {
                // Resized evidently called (as a result of
                // setLocationRelativeTo() ?) before window is shown
                return;
            } else if (w == app) {
                // Main application window - might change overlapping with
                // children
                refreshTranslucencies();
            } else { // The Find dialog
                refreshTranslucency(w);
            }
        }
    }

    /** Called when one of the child windows is resized. */
    public void componentShown(ComponentEvent e) {
        // Need to do this always, in case the translucency rule changed since
        // the window was last visible
        Window w = (Window) e.getComponent();
        if (w != app) {
            refreshTranslucency(w);
        }
    }

    /** Called when one of the child windows is resized. */
    public void componentResized(ComponentEvent e) {
        if (translucencyRule == TRANSLUCENT_WHEN_OVERLAPPING_APP) {
            Window w = (Window) e.getComponent();
            if (!w.isShowing()) {
                // Resized evidently called (as a result of pack() ?) before
                // window is shown
                return;
            } else if (w == app) {
                // Main application window - might change overlapping with
                // children
                refreshTranslucencies();
            } else { // The Find dialog
                refreshTranslucency(w);
            }
        }
    }

    /**
     * Refreshes the opacity of a window based on the current properties
     * selected by the user.
     *
     * @param window The window to refresh.
     */
    private void refreshTranslucency(Window window) {
        // The user has turned off this feature
        if (!app.isFindWindowOpacityEnabled()) {
            setTranslucent(window, false);
            return;
        }

        switch (translucencyRule) {
            case TRANSLUCENT_ALWAYS :
                setTranslucent(window, true);
                break;

            case TRANSLUCENT_NEVER :
                setTranslucent(window, false);
                break;

            case TRANSLUCENT_WHEN_NOT_FOCUSED :
                setTranslucent(window, !window.isFocused());
                break;

            case TRANSLUCENT_WHEN_OVERLAPPING_APP :
                Point p1 = window.getLocationOnScreen();
                Rectangle bounds1 = window.getBounds();
                bounds1.setLocation(p1);
                Point p2 = app.getLocationOnScreen();
                Rectangle bounds2 = app.getBounds();
                bounds2.setLocation(p2);
                setTranslucent(window, bounds2.intersects(bounds1));
                break;
        }
    }

    /** Refreshes the opacity of all windows being listened to. */
    public void refreshTranslucencies() {
        ApplicationFrame applicationFrame = ApplicationFrame.getInstance();
        OTVFindDialog _OTVFindDialog = applicationFrame.getFindDialog();
        // A window must be showing for its bounds to be queried
        if (_OTVFindDialog != null && _OTVFindDialog.isShowing()) {
            refreshTranslucency(_OTVFindDialog);
        }
    }

    public void setTranslucencyRule(int rule) {
        if (rule >= 0 && rule <= 3 && rule != translucencyRule) {
            this.translucencyRule = rule;
            if (rule == TRANSLUCENT_WHEN_OVERLAPPING_APP) {
                app.addComponentListener(this);
            } else { // OK if not actually added
                app.removeComponentListener(this);
            }
            refreshTranslucencies();
        }
    }

    /**
     * Sets whether a specific window is translucent.
     *
     * @param w The window.
     * @param translucent Whether that window is translucent.
     */
    private void setTranslucent(Window w, boolean translucent) {
        float curOpacity = TranslucencyUtil.get().getOpacity(w);
        float newOpacity = translucent ? app.getFindWindowOpacity() : 1;

        if (curOpacity != newOpacity) {
            TranslucencyUtil.get().setOpacity(w, newOpacity);
        }
    }

    /**
     * Called when a window gains focus.
     *
     * @param e The event.
     */
    public void windowGainedFocus(WindowEvent e) {
        if (translucencyRule == TRANSLUCENT_WHEN_NOT_FOCUSED) {
            refreshTranslucency(e.getWindow());
        }
    }

    /**
     * Called when a window loses focus.
     *
     * @param e The event.
     */
    public void windowLostFocus(WindowEvent e) {
        if (translucencyRule == TRANSLUCENT_WHEN_NOT_FOCUSED) {
            refreshTranslucency(e.getWindow());
        }
    }
}