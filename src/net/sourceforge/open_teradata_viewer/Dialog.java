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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import net.sourceforge.open_teradata_viewer.i18n.LanguageManager;

/**
 * 
 * 
 * @author D. Campione
 * 
 */
public class Dialog extends JOptionPane implements LanguageManager.LanguageChangeListener {

    private static final long serialVersionUID = -1579318866748692286L;
    
    private Object message;
    private Object[] options;
    private Object initialValue;
    
    public Dialog(Object message, int messageType, int optionType,
            Object[] options, Object initialValue) {
        super(message, messageType, optionType, null, options, initialValue);
        this.message = message;
        this.options = options;
        this.initialValue = initialValue;
        
        // Register for language changes
        LanguageManager.getInstance().addLanguageChangeListener(this);
    }
    
    @Override
    public void onLanguageChanged(java.util.Locale newLocale, java.util.ResourceBundle newBundle) {
        // Update any localizable components
        if (message instanceof String) {
            String key = (String) message;
            try {
                message = newBundle.getString(key);
                setMessage(message);
            } catch (Exception e) {
                // Key not found, keep original message
            }
        }
        
        // Update options if they are localizable strings
        if (options != null) {
            for (int i = 0; i < options.length; i++) {
                if (options[i] instanceof String) {
                    String key = (String) options[i];
                    try {
                        options[i] = newBundle.getString(key);
                    } catch (Exception e) {
                        // Key not found, keep original text
                    }
                }
            }
            setOptions(options);
        }
        
        // Update initial value if it's a localizable string
        if (initialValue instanceof String) {
            String key = (String) initialValue;
            try {
                initialValue = newBundle.getString(key);
                setInitialValue(initialValue);
            } catch (Exception e) {
                // Key not found, keep original text
            }
        }
    }

    private static final List<Dialog> activeDialogs = new ArrayList<>();
    
    public static int show(String title, Object message, int messageType,
            int optionType) {
        // If we're already on EDT, execute directly
        if (SwingUtilities.isEventDispatchThread()) {
            return showOnEDT(title, message, messageType, optionType);
        }
        
        // Otherwise, execute on EDT and wait for result
        final AtomicReference<Integer> result = new AtomicReference<Integer>();
        final AtomicReference<Exception> exception = new AtomicReference<Exception>();
        
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    try {
                        result.set(showOnEDT(title, message, messageType, optionType));
                    } catch (Exception e) {
                        exception.set(e);
                    }
                }
            });
        } catch (Exception e) {
            exception.set(e);
        }
        
        if (exception.get() != null) {
            throw new RuntimeException(exception.get());
        }
        
        return result.get();
    }
    
    private static int showOnEDT(String title, Object message, int messageType,
            int optionType) {
        Dialog dialog = new Dialog(message, messageType, optionType, null, null);
        activeDialogs.add(dialog);
        javax.swing.JDialog jd = dialog.createDialog(ApplicationFrame.getInstance(), title);
        jd.setVisible(true);
        activeDialogs.remove(dialog);
        LanguageManager.getInstance().removeLanguageChangeListener(dialog);
        if (dialog.getValue() == null) {
            return CLOSED_OPTION;
        } else {
            try {
                return ((Number) dialog.getValue()).intValue();
            } catch (ClassCastException cce) {
                return CLOSED_OPTION;
            }
        }
    }

    public static int show(String title, JScrollPane scrollPane,
            int messageType, int optionType) {
        determineSize(scrollPane);
        // Delegate to show method that handles dialog tracking
        return show(title, (Object) scrollPane, messageType, optionType);
    }

    public static Object show(String title, Object message, int messageType,
            Object[] options, Object initialValue) {
        // If we're already on EDT, execute directly
        if (SwingUtilities.isEventDispatchThread()) {
            return showOnEDT(title, message, messageType, options, initialValue);
        }
        
        // Otherwise, execute on EDT and wait for result
        final AtomicReference<Object> result = new AtomicReference<Object>();
        final AtomicReference<Exception> exception = new AtomicReference<Exception>();
        
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    try {
                        result.set(showOnEDT(title, message, messageType, options, initialValue));
                    } catch (Exception e) {
                        exception.set(e);
                    }
                }
            });
        } catch (Exception e) {
            exception.set(e);
        }
        
        if (exception.get() != null) {
            throw new RuntimeException(exception.get());
        }
        
        return result.get();
    }
    
    private static Object showOnEDT(String title, Object message, int messageType,
            Object[] options, Object initialValue) {
        // Translate options before creating dialog if they are localization keys
        Object[] translatedOptions = null;
        if (options != null) {
            translatedOptions = new Object[options.length];
            LanguageManager langManager = LanguageManager.getInstance();
            for (int i = 0; i < options.length; i++) {
                if (options[i] instanceof String) {
                    String key = (String) options[i];
                    try {
                        translatedOptions[i] = langManager.getString(key);
                    } catch (Exception e) {
                        translatedOptions[i] = options[i]; // Keep original if not a key
                    }
                } else {
                    translatedOptions[i] = options[i];
                }
            }
        }
        
        Dialog dialog = new Dialog(message, messageType, DEFAULT_OPTION,
                translatedOptions, initialValue);
        activeDialogs.add(dialog);
        try {
            UISupport.showDialog(dialog.createDialog(
                    ApplicationFrame.getInstance(), title));
        } catch (NoClassDefFoundError e) {
            UISupport.showDialog(dialog.createDialog(null, title));
        }
        activeDialogs.remove(dialog);
        LanguageManager.getInstance().removeLanguageChangeListener(dialog);
        return dialog.getValue();
    }

    public static Object show(String title, JScrollPane scrollPane,
            int messageType, Object[] options, Object initialValue) {
        determineSize(scrollPane);
        // Delegate to show method that handles dialog tracking
        return show(title, (Object) scrollPane, messageType, options,
                initialValue);
    }

    private static void determineSize(JScrollPane scrollPane) {
        // If we're already on EDT, execute directly
        if (SwingUtilities.isEventDispatchThread()) {
            determineSizeOnEDT(scrollPane);
        } else {
            // Otherwise, execute on EDT and wait for completion
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    @Override
                    public void run() {
                        determineSizeOnEDT(scrollPane);
                    }
                });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    private static void determineSizeOnEDT(JScrollPane scrollPane) {
        int minWidth = 600;
        int minHeight = 400;
        double scrollBarWidth = new JScrollBar().getPreferredSize().getWidth();
        if (scrollPane.getViewport().getComponent(0) instanceof JList) {
            minWidth = 0;
            minHeight = 0;
            scrollBarWidth = 0;
            JList<?> jList = (JList<?>) scrollPane.getViewport()
                    .getComponent(0);
            jList.setVisibleRowCount(Math.max(15, jList.getModel().getSize()));
        }
        double maxWidth = Toolkit.getDefaultToolkit().getScreenSize()
                .getWidth() * .8;
        double maxHeight = (Toolkit.getDefaultToolkit().getScreenSize()
                .getHeight() - 100) * .8;
        double preferedWidth = scrollPane.getPreferredSize().getWidth()
                + scrollBarWidth;
        double preferedHeight = scrollPane.getPreferredSize().getHeight()
                + scrollBarWidth;
        int width = (int) Math.min(maxWidth, Math.max(minWidth, preferedWidth));
        int height = (int) Math.min(maxHeight,
                Math.max(minHeight, preferedHeight));
        scrollPane.setPreferredSize(new Dimension(width, height));
    }

    @Override
    public void selectInitialValue() {
    }
}