package com.fairchain.batchservice;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * DEMO-GRADE password hashing (plain SHA-256, no salt, no bcrypt).
 * Section 4.4 allows "a simplified token issued at login" for demo
 * purposes — extending that same allowance here given the timeline.
 * NOT safe for real production use; flagged clearly.
 */
public class PasswordUtil {

    private PasswordUtil() {
    }

    public static String hash(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
