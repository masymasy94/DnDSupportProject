package com.dndplatform.chat.adapter.outbound.jpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@DisplayName("ParticipantUpdateLastReadRepositoryImpl")
class ParticipantUpdateLastReadRepositoryImplTest {

    @Mock
    private ParticipantPanacheRepository participantPanacheRepository;

    private ParticipantUpdateLastReadRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new ParticipantUpdateLastReadRepositoryImpl(participantPanacheRepository);
    }

    @Test
    @DisplayName("should execute update query with conversationId and userId")
    void shouldExecuteUpdateQueryWithConversationIdAndUserId() {
        Long conversationId = 42L;
        Long userId = 7L;

        given(participantPanacheRepository.update(
                eq("lastReadAt = ?1 where conversation.id = ?2 and userId = ?3"),
                any(LocalDateTime.class), eq(conversationId), eq(userId)))
                .willReturn(1);

        sut.updateLastReadAt(conversationId, userId);

        then(participantPanacheRepository).should().update(
                eq("lastReadAt = ?1 where conversation.id = ?2 and userId = ?3"),
                any(LocalDateTime.class), eq(conversationId), eq(userId));
    }
}
