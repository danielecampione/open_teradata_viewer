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

package net.sourceforge.open_teradata_viewer.i18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.SwingUtilities;

import net.sourceforge.open_teradata_viewer.Config;

/**
 * Manages internationalization for the application.
 * Handles language switching and resource bundle management.
 * 
 * @author D. Campione
 */
public class LanguageManager {
    
    private static final String BUNDLE_NAME = "messages";
    private static final String DEFAULT_LANGUAGE = "en";
    
    private static LanguageManager instance;
    private ResourceBundle currentBundle;
    private Locale currentLocale;
    private List<LanguageChangeListener> listeners;
    
    // Supported languages
    public static final Language ENGLISH = new Language("en", "English", "English");
    public static final Language ITALIAN = new Language("it", "Italiano", "Italian");
    public static final Language GERMAN = new Language("de", "Deutsch", "German");
    public static final Language DUTCH = new Language("nl", "Nederlands", "Dutch");
    public static final Language UKRAINIAN = new Language("uk", "\u0423\u043a\u0440\u0430\u0457\u043d\u0441\u044c\u043a\u0430", "Ukrainian");
    public static final Language DANISH = new Language("da", "Dansk", "Danish");
    
    private static final Language[] SUPPORTED_LANGUAGES = {
        ENGLISH, ITALIAN, GERMAN, DUTCH, UKRAINIAN, DANISH
    };

    private LanguageManager() {
        listeners = new ArrayList<>();
        initializeLanguage();
    }
    
    /**
     * Initialize the language based on configuration or system defaults.
     */
    private void initializeLanguage() {
        String languageCode = null;
        
        try {
            // First try to load the saved language from XML configuration
            languageCode = Config.getSetting("language");
            
            // Debug logging
            System.out.println("[LanguageManager] Language from config: " + languageCode);
            
            if (languageCode != null && !languageCode.trim().isEmpty()) {
                // Validate that the language is supported
                if (isLanguageSupported(languageCode.trim())) {
                    setLanguageInternal(languageCode.trim());
                    return;
                } else {
                    System.out.println("[LanguageManager] Unsupported language in config: " + languageCode);
                }
            }
        } catch (Exception e) {
            System.err.println("[LanguageManager] Error reading language from config: " + e.getMessage());
        }
        
        // If no valid language from config, try system locale
        try {
            String systemLang = Locale.getDefault().getLanguage();
            System.out.println("[LanguageManager] System language: " + systemLang);
            
            if (isLanguageSupported(systemLang)) {
                setLanguageInternal(systemLang);
                return;
            }
        } catch (Exception e) {
            System.err.println("[LanguageManager] Error with system locale: " + e.getMessage());
        }
        
        // Fallback to default language
        System.out.println("[LanguageManager] Using default language: " + DEFAULT_LANGUAGE);
        setLanguageInternal(DEFAULT_LANGUAGE);
    }
    
    /**
     * Check if a language code is supported.
     */
    private boolean isLanguageSupported(String languageCode) {
        if (languageCode == null) return false;
        
        for (Language lang : SUPPORTED_LANGUAGES) {
            if (lang.getCode().equals(languageCode)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Internal method to set language without saving to config.
     */
    private void setLanguageInternal(String languageCode) {
        try {
            // Force explicit locale creation
            currentLocale = new Locale(languageCode);
            
            // Set the default locale to ensure consistent behavior
            Locale.setDefault(currentLocale);
            
            // Load the resource bundle with explicit locale and fallback handling
            try {
                currentBundle = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);
                System.out.println("[LanguageManager] Successfully loaded bundle for: " + languageCode);
                System.out.println("[LanguageManager] Bundle locale: " + currentBundle.getLocale());
            } catch (MissingResourceException e) {
                System.err.println("[LanguageManager] Failed to load bundle for " + languageCode + ": " + e.getMessage());
                
                // Try to load default bundle
                if (!DEFAULT_LANGUAGE.equals(languageCode)) {
                    currentLocale = new Locale(DEFAULT_LANGUAGE);
                    currentBundle = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale);
                    System.out.println("[LanguageManager] Loaded default bundle instead");
                } else {
                    throw new RuntimeException("Could not load default language resources", e);
                }
            }
            
        } catch (Exception e) {
            System.err.println("[LanguageManager] Critical error setting language: " + e.getMessage());
            throw new RuntimeException("Could not initialize language system", e);
        }
    }

    public static synchronized LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }
    
    /**
     * Sets the current language and updates the resource bundle.
     * 
     * @param languageCode the language code (e.g., "en", "it")
     */
    public void setLanguage(String languageCode) {
        if (languageCode == null || languageCode.trim().isEmpty()) {
            System.err.println("[LanguageManager] Invalid language code: " + languageCode);
            return;
        }
        
        languageCode = languageCode.trim();
        
        // Check if language is supported
        if (!isLanguageSupported(languageCode)) {
            System.err.println("[LanguageManager] Unsupported language: " + languageCode);
            return;
        }
        
        // Check if it's already the current language
        if (currentLocale != null && languageCode.equals(currentLocale.getLanguage())) {
            System.out.println("[LanguageManager] Language already set to: " + languageCode);
            return;
        }
        
        try {
            // Set the language internally
            setLanguageInternal(languageCode);
            
            // Save to configuration
            Config.saveSetting("language", languageCode);
            System.out.println("[LanguageManager] Language saved to config: " + languageCode);
            
            // Notify listeners on EDT
            SwingUtilities.invokeLater(() -> {
                System.out.println("[LanguageManager] Notifying " + listeners.size() + " listeners");
                for (LanguageChangeListener listener : listeners) {
                    try {
                        listener.onLanguageChanged(currentLocale, currentBundle);
                    } catch (Exception e) {
                        System.err.println("[LanguageManager] Error notifying listener: " + e.getMessage());
                    }
                }
            });
            
        } catch (Exception e) {
            System.err.println("[LanguageManager] Error setting language to " + languageCode + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gets a localized string for the given key.
     * 
     * @param key the resource key
     * @return the localized string, or the key itself if not found
     */
    public String getString(String key) {
        if (key == null) {
            return "";
        }
        
        try {
            if (currentBundle != null) {
                String value = currentBundle.getString(key);
                return value != null ? value : key;
            }
        } catch (MissingResourceException e) {
            System.err.println("[LanguageManager] Missing resource key: " + key);
        } catch (Exception e) {
            System.err.println("[LanguageManager] Error getting string for key " + key + ": " + e.getMessage());
        }
        
        return key; // Return key as fallback
    }
    
    /**
     * Gets a localized string with parameters.
     * 
     * @param key the resource key
     * @param params parameters to substitute
     * @return the formatted localized string
     */
    public String getString(String key, Object... params) {
        if (key == null) {
            return "";
        }
        
        try {
            if (currentBundle != null) {
                String pattern = currentBundle.getString(key);
                if (pattern != null && params != null && params.length > 0) {
                    return String.format(pattern, params);
                }
                return pattern != null ? pattern : key;
            }
        } catch (MissingResourceException e) {
            System.err.println("[LanguageManager] Missing resource key: " + key);
        } catch (Exception e) {
            System.err.println("[LanguageManager] Error getting string for key " + key + ": " + e.getMessage());
        }
        
        return key; // Return key as fallback
    }

    /**
     * Gets the current locale.
     * 
     * @return the current locale
     */
    public Locale getCurrentLocale() {
        return currentLocale;
    }
    
    /**
     * Gets the current language code.
     * 
     * @return the current language code
     */
    public String getCurrentLanguageCode() {
        return currentLocale != null ? currentLocale.getLanguage() : DEFAULT_LANGUAGE;
    }

    /**
     * Gets all supported languages.
     * 
     * @return array of supported languages
     */
    public Language[] getSupportedLanguages() {
        return SUPPORTED_LANGUAGES.clone();
    }
    
    /**
     * Gets the current language object.
     * 
     * @return the current language
     */
    public Language getCurrentLanguage() {
        String currentCode = getCurrentLanguageCode();
        for (Language lang : SUPPORTED_LANGUAGES) {
            if (lang.getCode().equals(currentCode)) {
                return lang;
            }
        }
        return ENGLISH; // fallback
    }

    /**
     * Adds a language change listener.
     * 
     * @param listener the listener to add
     */
    public void addLanguageChangeListener(LanguageChangeListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
            System.out.println("[LanguageManager] Added language change listener. Total: " + listeners.size());
        }
    }
    
    /**
     * Removes a language change listener.
     * 
     * @param listener the listener to remove
     */
    public void removeLanguageChangeListener(LanguageChangeListener listener) {
        if (listeners.remove(listener)) {
            System.out.println("[LanguageManager] Removed language change listener. Total: " + listeners.size());
        }
    }

    /**
     * Interface for language change notifications.
     */
    public interface LanguageChangeListener {
        void onLanguageChanged(Locale newLocale, ResourceBundle newBundle);
    }
    
    /**
     * Represents a supported language.
     */
    public static class Language {
        private final String code;
        private final String nativeName;
        private final String englishName;
        
        public Language(String code, String nativeName, String englishName) {
            this.code = code;
            this.nativeName = nativeName;
            this.englishName = englishName;
        }
        
        public String getCode() {
            return code;
        }
        
        public String getNativeName() {
            return nativeName;
        }
        
        public String getEnglishName() {
            return englishName;
        }
        
        @Override
        public String toString() {
            return nativeName;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Language language = (Language) obj;
            return code.equals(language.code);
        }
        
        @Override
        public int hashCode() {
            return code.hashCode();
        }
    }
}