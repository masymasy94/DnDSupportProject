package com.dndplatform.chat.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class ParticipantExistsRepositoryImplTest {

    @Mock
    private ParticipantPanacheRepository panacheRepository;

    private ParticipantExistsRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ParticipantExistsRepositoryImpl(panacheRepository);
    }

    @Test
    void shouldReturnTrueWhenParticipantExists(@Random Long conversationId, @Random Long userId) {
        given(panacheRepository.existsByConversationIdAndUserId(conversationId, userId)).willReturn(true);

        var result = sut.existsByConversationIdAndUserId(conversationId, userId);

        assertThat(result).isTrue();
        then(panacheRepository).should().existsByConversationIdAndUserId(conversationId, userId);
    }

    @Test
    void shouldReturnFalseWhenParticipantDoesNotExist(@Random Long conversationId, @Random Long userId) {
        given(panacheRepository.existsByConversationIdAndUserId(conversationId, userId)).willReturn(false);

        var result = sut.existsByConversationIdAndUserId(conversationId, userId);

        assertThat(result).isFalse();
        then(panacheRepository).should().existsByConversationIdAndUserId(conversationId, userId);
    }
}
