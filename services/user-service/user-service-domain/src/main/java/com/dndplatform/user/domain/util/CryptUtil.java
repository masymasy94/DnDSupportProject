package com.dndplatform.user.domain.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class CryptUtil {

    private static final int BCRYPT_COST = 12;

    private CryptUtil() {
    }

    public static String hashPassword(String plainPassword) {
        return BCrypt.withDefaults().hashToString(BCRYPT_COST, plainPassword.toCharArray());
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword).verified;
    }
}
