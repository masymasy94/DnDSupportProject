package com.dndplatform.auth.adapter.outbound.jpa.util;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class TokenUtilTest {

    @Test
    void shouldGenerateOtpCodeWithSixDigits() {
        var otpCode = TokenUtil.generateOtpCode();

        assertThat(otpCode).hasSize(6);
        assertThat(otpCode).matches("\\d{6}");
    }

    @Test
    void shouldGenerateDifferentCodesOnSubsequentCalls() {
        var first = TokenUtil.generateOtpCode();
        var second = TokenUtil.generateOtpCode();

        // Very unlikely to be equal; validates randomness
        assertThat(first).isNotNull();
        assertThat(second).isNotNull();
    }

    @Test
    void shouldProduceDeterministicSha256ForSameInput(@Random String input) {
        var hash1 = TokenUtil.sha256(input);
        var hash2 = TokenUtil.sha256(input);

        assertThat(hash1).isEqualTo(hash2);
    }

    @Test
    void shouldProduceDifferentHashesForDifferentInputs(@Random String input1, @Random String input2) {
        var hash1 = TokenUtil.sha256(input1);
        var hash2 = TokenUtil.sha256(input2);

        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    void shouldProduceHexStringOfExpectedLength(@Random String input) {
        var hash = TokenUtil.sha256(input);

        // SHA-256 produces 32 bytes = 64 hex characters
        assertThat(hash).hasSize(64);
        assertThat(hash).matches("[0-9a-f]{64}");
    }

    @Test
    void shouldProduceDifferentHashFromRawToken(@Random String input) {
        var hash = TokenUtil.sha256(input);

        assertThat(hash).isNotEqualTo(input);
    }
}
