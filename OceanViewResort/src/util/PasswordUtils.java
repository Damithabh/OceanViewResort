package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility for SHA-256 Password Hashing
 * // Learned from https://howtodoinjava.com/security/sha-256-hashing-in-java/
 */
public class PasswordUtils {

    /**
     * Hashes a plain text string using the SHA-256 algorithm.
     * @param plainText The plain text password
     * @return The Hex representation of the hashed string
     */
    public static String hashPassword(String plainText) {
        if (plainText == null) return null;
        
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(plainText.getBytes());
            
            // Convert byte array to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.err.println("❌ Error: SHA-256 algorithm not found.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Verifies if a given plain text matches a stored hash.
     */
    public static boolean verifyPassword(String plainText, String storedHash) {
        if (plainText == null || storedHash == null) return false;
        String hashedPassword = hashPassword(plainText);
        return storedHash.equals(hashedPassword);
    }
}
