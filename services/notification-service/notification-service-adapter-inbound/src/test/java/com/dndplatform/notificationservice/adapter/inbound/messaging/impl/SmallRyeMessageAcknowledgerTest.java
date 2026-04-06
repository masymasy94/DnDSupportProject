package com.dndplatform.notificationservice.adapter.inbound.messaging.impl;

import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class SmallRyeMessageAcknowledgerTest {

    @Mock
    private Message<?> message;

    private SmallRyeMessageAcknowledger sut;

    @BeforeEach
    void setUp() {
        sut = new SmallRyeMessageAcknowledger();
    }

    @Test
    void shouldDelegateAckToMessage() {
        given(message.ack()).willReturn(CompletableFuture.completedFuture(null));

        var result = sut.ack(message);

        assertThat(result).isNotNull();
        then(message).should().ack();
    }

    @Test
    void shouldDelegateNackToMessage() {
        var cause = new RuntimeException("send error");
        given(message.nack(cause)).willReturn(CompletableFuture.completedFuture(null));

        var result = sut.nack(message, cause);

        assertThat(result).isNotNull();
        then(message).should().nack(cause);
    }

    @Test
    void shouldThrowIllegalStateWhenAckCalledWithNullMessage() {
        assertThatThrownBy(() -> sut.ack(null))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldThrowIllegalStateWhenNackCalledWithNullMessage() {
        assertThatThrownBy(() -> sut.nack(null, new RuntimeException()))
                .isInstanceOf(IllegalStateException.class);
    }
}
