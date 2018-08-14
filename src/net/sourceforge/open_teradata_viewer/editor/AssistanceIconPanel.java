/*
 * Open Teradata Viewer ( editor )
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

package net.sourceforge.open_teradata_viewer.editor;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

/**
 * A panel meant to be displayed alongside a text component, that can display
 * assistance icons for that text component.
 *
 * @author D. Campione
 * 
 */
public class AssistanceIconPanel extends DecorativeIconPanel implements
        PropertyChangeListener {

    private static final long serialVersionUID = -5085972105075779750L;

    /**
     * The tool tip text for the light bulb icon. It is assumed that access to
     * this field is single-threaded (on the EDT).
     */
    private static String ASSISTANCE_AVAILABLE;

    /**
     * Ctor.
     *
     * @param comp The component to listen to. This can be <code>null</code> to
     *        create a "filler" icon panel for alignment purposes.  
     */
    public AssistanceIconPanel(JComponent comp) {
        // null can be passed to make a "filler" icon panel for alignment
        // purposes
        if (comp != null) {
            ComponentListener listener = new ComponentListener();

            if (comp instanceof JComboBox) {
                JComboBox combo = (JComboBox) comp;
                Component c = combo.getEditor().getEditorComponent();
                if (c instanceof JTextComponent) { // Always true
                    JTextComponent tc = (JTextComponent) c;
                    tc.addFocusListener(listener);
                }
            } else { // Usually a JTextComponent
                comp.addFocusListener(listener);
            }

            comp.addPropertyChangeListener(IContentAssistable.ASSISTANCE_IMAGE,
                    this);
        }
    }

    /**
     * Returns the "Content Assist Available" tool tip text for the light bulb
     * icon. It is assumed that this method is only called on the EDT.
     *
     * @return The text.
     */
    static String getAssistanceAvailableText() {
        if (ASSISTANCE_AVAILABLE == null) {
            ASSISTANCE_AVAILABLE = "Content Assist Available";
        }
        return ASSISTANCE_AVAILABLE;
    }

    /**
     * Called when the property {@link
     * IContentAssistable#ASSISTANCE_IMAGE} is fired by the component we are
     * listening to.
     *
     * @param e The change event.
     */
    @Override
    public void propertyChange(PropertyChangeEvent e) {
        Image img = (Image) e.getNewValue();
        setAssistanceEnabled(img);
    }

    /**
     * A hook for applications to initialize this panel, if the component we're
     * listening to already has content assist enabled.
     *
     * @param img The image to display, or <code>null</code> if content assist
     *        is not currently available.
     */
    public void setAssistanceEnabled(Image img) {
        if (img == null && getIcon() != EMPTY_ICON) {
            setIcon(EMPTY_ICON);
            setToolTipText(null);
        } else {
            setIcon(new ImageIcon(img));
            setToolTipText(getAssistanceAvailableText());
        }
    }

    /**
     * Listens for events in the text component we're annotating.
     * 
     * @author D. Campione
     * 
     */
    private class ComponentListener implements FocusListener {

        /**
         * Called when the combo box or text component gains focus.
         *
         * @param e The focus event.
         */
        @Override
        public void focusGained(FocusEvent e) {
            setShowIcon(true);
        }

        /**
         * Called when the combo box or text component loses focus.
         *
         * @param e The focus event.
         */
        @Override
        public void focusLost(FocusEvent e) {
            setShowIcon(false);
        }
    }
}