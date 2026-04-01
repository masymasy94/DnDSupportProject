package com.dndplatform.auth.adapter.outbound.jpa.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;

public final class TokenUtil {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final int OTP_BOUND = 1_000_000;

    private TokenUtil() {
    }

    public static String generateOtpCode() {
        return String.format("%06d", SECURE_RANDOM.nextInt(OTP_BOUND));
    }

    public static String sha256(String input) {
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            var hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}
