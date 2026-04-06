package com.dndplatform.user.domain.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CryptUtilTest {

    @Test
    void shouldHashPasswordToNonBlankString() {
        var hash = CryptUtil.hashPassword("myPassword123");

        assertThat(hash).isNotBlank();
        assertThat(hash).isNotEqualTo("myPassword123");
    }

    @Test
    void shouldProduceDifferentHashesForSamePassword() {
        var hash1 = CryptUtil.hashPassword("samePassword");
        var hash2 = CryptUtil.hashPassword("samePassword");

        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    void shouldVerifyCorrectPassword() {
        var password = "correctPassword";
        var hash = CryptUtil.hashPassword(password);

        assertThat(CryptUtil.verifyPassword(password, hash)).isTrue();
    }

    @Test
    void shouldNotVerifyWrongPassword() {
        var hash = CryptUtil.hashPassword("correctPassword");

        assertThat(CryptUtil.verifyPassword("wrongPassword", hash)).isFalse();
    }
}
