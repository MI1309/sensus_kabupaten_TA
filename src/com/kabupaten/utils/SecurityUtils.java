package com.kabupaten.utils;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

/**
 * Utility class for security related functions
 */
public class SecurityUtils {

    /**
     * Hashes a password using SHA-256
     * @param password Plain text password
     * @return Hashed password string in hexadecimal
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return password; // Fallback to plain if error
        }
    }
}
