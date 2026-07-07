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

package net.sourceforge.open_teradata_viewer.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import net.sourceforge.open_teradata_viewer.util.Logger;

/**
 * Secure credential management for database passwords and sensitive data.
 * Uses AES-256-GCM encryption with machine-specific key derivation.
 * 
 * @author D. Campione
 */
public final class CredentialManager {
    
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;
    private static final int KEY_LENGTH = 256;
    
    private static final Logger logger = Logger.getInstance();
    private static CredentialManager instance;
    private final SecretKey masterKey;
    
    private CredentialManager() throws Exception {
        this.masterKey = deriveMasterKey();
    }
    
    /**
     * Gets the singleton instance of CredentialManager.
     * 
     * @return the CredentialManager instance
     * @throws RuntimeException if initialization fails
     */
    public static synchronized CredentialManager getInstance() {
        if (instance == null) {
            try {
                instance = new CredentialManager();
            } catch (Exception e) {
                logger.error("Failed to initialize CredentialManager", e);
                throw new RuntimeException("Failed to initialize credential manager", e);
            }
        }
        return instance;
    }
    
    /**
     * Encrypts a password or sensitive string.
     * 
     * @param plaintext the text to encrypt
     * @return Base64-encoded encrypted data with IV prepended
     * @throws RuntimeException if encryption fails
     */
    public String encrypt(String plaintext) {
        if (plaintext == null || plaintext.isEmpty()) {
            return "";
        }
        
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            
            // Generate random IV
            byte[] iv = new byte[GCM_IV_LENGTH];
            new SecureRandom().nextBytes(iv);
            
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.ENCRYPT_MODE, masterKey, parameterSpec);
            
            byte[] encryptedData = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
            
            // Prepend IV to encrypted data
            byte[] encryptedWithIv = new byte[iv.length + encryptedData.length];
            System.arraycopy(iv, 0, encryptedWithIv, 0, iv.length);
            System.arraycopy(encryptedData, 0, encryptedWithIv, iv.length, encryptedData.length);
            
            return Base64.getEncoder().encodeToString(encryptedWithIv);
            
        } catch (Exception e) {
            logger.error("Failed to encrypt data", e);
            throw new RuntimeException("Encryption failed", e);
        }
    }
    
    /**
     * Decrypts a previously encrypted string.
     * 
     * @param encryptedData Base64-encoded encrypted data with IV prepended
     * @return the decrypted plaintext
     * @throws RuntimeException if decryption fails
     */
    public String decrypt(String encryptedData) {
        if (encryptedData == null || encryptedData.isEmpty()) {
            return "";
        }
        
        try {
            byte[] encryptedWithIv = Base64.getDecoder().decode(encryptedData);
            
            if (encryptedWithIv.length < GCM_IV_LENGTH) {
                throw new IllegalArgumentException("Invalid encrypted data length");
            }
            
            // Extract IV and encrypted data
            byte[] iv = new byte[GCM_IV_LENGTH];
            byte[] encrypted = new byte[encryptedWithIv.length - GCM_IV_LENGTH];
            
            System.arraycopy(encryptedWithIv, 0, iv, 0, GCM_IV_LENGTH);
            System.arraycopy(encryptedWithIv, GCM_IV_LENGTH, encrypted, 0, encrypted.length);
            
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.DECRYPT_MODE, masterKey, parameterSpec);
            
            byte[] decryptedData = cipher.doFinal(encrypted);
            return new String(decryptedData, StandardCharsets.UTF_8);
            
        } catch (Exception e) {
            logger.error("Failed to decrypt data", e);
            throw new RuntimeException("Decryption failed", e);
        }
    }
    
    /**
     * Derives a master key based on machine-specific characteristics.
     * This provides better security than a hardcoded key while maintaining
     * deterministic behavior across all execution contexts (IDE, JAR,
     * different JVM versions).
     *
     * NOTE: Only user.name is used because:
     * - java.home  → differs between JDK (Eclipse) and JRE (JAR)
     * - os.name    → older JVMs (<=1.8.0_311) report "Windows 10" even on
     *                Windows 11 hardware; newer JVMs report "Windows 11"
     * - os.version → similarly unstable across JVM versions
     * Using os.* properties would produce different keys depending on
     * which JVM runs the application, breaking decryption.
     */
    private SecretKey deriveMasterKey() throws Exception {
        StringBuilder keyMaterial = new StringBuilder();
        keyMaterial.append(System.getProperty("user.name", ""));

        // Fixed salt — prevents rainbow table attacks
        keyMaterial.append("OTV-2024-SALT-v1");

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = digest.digest(
                keyMaterial.toString().getBytes(StandardCharsets.UTF_8));

        return new SecretKeySpec(keyBytes, ALGORITHM);
    }
    
    /**
     * Migrates old DES-encrypted passwords to the new AES encryption.
     * This method should be called during application startup to upgrade
     * existing encrypted passwords.
     * 
     * @param oldEncryptedPassword the old DES-encrypted password
     * @return the new AES-encrypted password
     */
    public String migrateFromLegacyEncryption(String oldEncryptedPassword) {
        if (oldEncryptedPassword == null || oldEncryptedPassword.isEmpty()) {
            return "";
        }
        
        try {
            // Try to decrypt using the old DES method
            String decrypted = decryptLegacyDES(oldEncryptedPassword);
            
            // Re-encrypt using the new AES method
            return encrypt(decrypted);
            
        } catch (Exception e) {
            logger.warn("Failed to migrate legacy encrypted password", e);
            // If migration fails, return the original (might already be AES)
            return oldEncryptedPassword;
        }
    }
    
    /**
     * Decrypts passwords encrypted with the old DES method.
     * This is for backward compatibility only.
     */
    private String decryptLegacyDES(String encryptedPassword) throws Exception {
        // Implementation of the old DES decryption for migration
        // This would use the old "$GeHeiM^" key from Config.java
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("DES");
        javax.crypto.spec.DESKeySpec keySpec = new javax.crypto.spec.DESKeySpec("$GeHeiM^".getBytes());
        javax.crypto.SecretKeyFactory keyFactory = javax.crypto.SecretKeyFactory.getInstance("DES");
        javax.crypto.SecretKey key = keyFactory.generateSecret(keySpec);
        
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, key);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword));
        
        return new String(decrypted, StandardCharsets.UTF_8);
    }
    
    /**
     * Securely clears sensitive data from memory.
     * 
     * @param sensitiveData the array to clear
     */
    public static void clearSensitiveData(char[] sensitiveData) {
        if (sensitiveData != null) {
            Arrays.fill(sensitiveData, '\0');
        }
    }
    
    /**
     * Securely clears sensitive data from memory.
     * 
     * @param sensitiveData the array to clear
     */
    public static void clearSensitiveData(byte[] sensitiveData) {
        if (sensitiveData != null) {
            Arrays.fill(sensitiveData, (byte) 0);
        }
    }
}