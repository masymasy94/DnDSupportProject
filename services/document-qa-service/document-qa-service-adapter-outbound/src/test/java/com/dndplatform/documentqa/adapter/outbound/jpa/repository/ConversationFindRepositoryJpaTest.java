package com.dndplatform.documentqa.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.documentqa.adapter.outbound.jpa.entity.ConversationEntity;
import com.dndplatform.documentqa.domain.model.Conversation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@DisplayName("ConversationFindRepositoryJpa")
class ConversationFindRepositoryJpaTest {

    @Mock
    private ConversationPanacheRepository panacheRepository;

    private ConversationFindRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationFindRepositoryJpa(panacheRepository);
    }

    private ConversationEntity buildEntity(Long id, Long userId, Long campaignId, String title) {
        ConversationEntity e = new ConversationEntity();
        e.id = id;
        e.userId = userId;
        e.campaignId = campaignId;
        e.title = title;
        e.createdAt = LocalDateTime.now();
        return e;
    }

    @Nested
    @DisplayName("findByUserId")
    class FindByUserId {

        @Test
        @DisplayName("should return list of conversations for user")
        void shouldReturnListOfConversationsForUser() {
            Long userId = 10L;
            ConversationEntity e1 = buildEntity(1L, userId, null, "Quest 1");
            ConversationEntity e2 = buildEntity(2L, userId, 5L, "Quest 2");
            given(panacheRepository.findByUserId(userId)).willReturn(List.of(e1, e2));

            List<Conversation> result = sut.findByUserId(userId);

            assertThat(result).hasSize(2);
            assertThat(result.get(0).id()).isEqualTo(1L);
            assertThat(result.get(1).id()).isEqualTo(2L);
            assertThat(result.get(0).userId()).isEqualTo(userId);

            then(panacheRepository).should().findByUserId(userId);
            then(panacheRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("should return empty list when user has no conversations")
        void shouldReturnEmptyListWhenUserHasNoConversations() {
            Long userId = 999L;
            given(panacheRepository.findByUserId(userId)).willReturn(List.of());

            List<Conversation> result = sut.findByUserId(userId);

            assertThat(result).isEmpty();

            then(panacheRepository).should().findByUserId(userId);
            then(panacheRepository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("findById")
    class FindById {

        @Test
        @DisplayName("should return conversation when found")
        void shouldReturnConversationWhenFound() {
            Long id = 42L;
            ConversationEntity entity = buildEntity(id, 7L, 3L, "Dark Forest");
            given(panacheRepository.findByIdOptional(id)).willReturn(Optional.of(entity));

            Optional<Conversation> result = sut.findById(id);

            assertThat(result).isPresent();
            assertThat(result.get().id()).isEqualTo(id);
            assertThat(result.get().title()).isEqualTo("Dark Forest");

            then(panacheRepository).should().findByIdOptional(id);
            then(panacheRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("should return empty when not found")
        void shouldReturnEmptyWhenNotFound() {
            Long id = 999L;
            given(panacheRepository.findByIdOptional(id)).willReturn(Optional.empty());

            Optional<Conversation> result = sut.findById(id);

            assertThat(result).isEmpty();

            then(panacheRepository).should().findByIdOptional(id);
            then(panacheRepository).shouldHaveNoMoreInteractions();
        }
    }
}
