package com.dndplatform.common.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class RandomExtensionFieldInjectionTest {

    @InjectRandom
    private String randomString;

    @InjectRandom
    private Integer randomInteger;

    @InjectRandom
    private UUID randomUuid;

    @InjectRandom
    private SamplePayload randomPayload;

    @Test
    void shouldInjectRandomStringField() {
        assertThat(randomString).isNotNull().isNotBlank();
    }

    @Test
    void shouldInjectRandomIntegerField() {
        assertThat(randomInteger).isNotNull();
    }

    @Test
    void shouldInjectRandomUuidField() {
        assertThat(randomUuid).isNotNull();
    }

    @Test
    void shouldInjectRandomComplexPayloadField() {
        assertThat(randomPayload).isNotNull();
        assertThat(randomPayload.username()).isNotNull();
        assertThat(randomPayload.email()).isNotNull();
        assertThat(randomPayload.age()).isNotNull();
    }

    record SamplePayload(String username, String email, Integer age) {}
}
