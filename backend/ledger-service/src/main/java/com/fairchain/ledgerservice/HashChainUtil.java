package com.fairchain.ledgerservice;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;

public class HashChainUtil {

    private HashChainUtil() {
    }

    /**
     * Computes SHA-256 over the event's own fields concatenated with the
     * previous event's hash. Changing any past event changes its hash,
     * which breaks every hash after it in the chain — that's what makes
     * this "tamper-evident" per Section 1.3.
     */
    public static String computeHash(String batchId, String eventType, String location,
                                      Instant timestamp, String metadataJson, String previousHash) {
        String input = String.join("|",
                nullSafe(batchId),
                nullSafe(eventType),
                nullSafe(location),
                timestamp != null ? timestamp.toString() : "",
                nullSafe(metadataJson),
                nullSafe(previousHash)
        );

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is guaranteed available on every standard JVM;
            // this should never actually happen.
            throw new IllegalStateException("SHA-256 algorithm not available", e);
        }
    }

    private static String nullSafe(String value) {
        return value != null ? value : "";
    }
}
