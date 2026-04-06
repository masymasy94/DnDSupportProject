package com.dndplatform.documentqa.adapter.outbound.jpa.repository;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.documentqa.adapter.outbound.jpa.entity.ConversationEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@DisplayName("ConversationDeleteRepositoryJpa")
class ConversationDeleteRepositoryJpaTest {

    @Mock
    private ConversationPanacheRepository panacheRepository;

    private ConversationDeleteRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new ConversationDeleteRepositoryJpa(panacheRepository);
    }

    @Nested
    @DisplayName("deleteById")
    class DeleteById {

        @Test
        @DisplayName("should find and delete conversation when it exists")
        void shouldFindAndDeleteConversationWhenItExists() {
            Long id = 5L;
            ConversationEntity entity = new ConversationEntity();
            entity.id = id;
            given(panacheRepository.findByIdOptional(id)).willReturn(Optional.of(entity));

            sut.deleteById(id);

            InOrder order = inOrder(panacheRepository);
            order.verify(panacheRepository).findByIdOptional(id);
            order.verify(panacheRepository).delete(entity);
            order.verifyNoMoreInteractions();
        }

        @Test
        @DisplayName("should throw NotFoundException when conversation does not exist")
        void shouldThrowNotFoundExceptionWhenConversationDoesNotExist() {
            Long id = 999L;
            given(panacheRepository.findByIdOptional(id)).willReturn(Optional.empty());

            assertThatThrownBy(() -> sut.deleteById(id))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Conversation not found with ID: " + id);

            then(panacheRepository).should().findByIdOptional(id);
            then(panacheRepository).shouldHaveNoMoreInteractions();
        }
    }
}
