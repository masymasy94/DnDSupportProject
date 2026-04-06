package com.dndplatform.documentqa.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.documentqa.adapter.outbound.jpa.entity.DocumentChunkEntity;
import com.dndplatform.documentqa.domain.model.DocumentChunk;
import com.dndplatform.documentqa.domain.model.DocumentChunkBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@DisplayName("DocumentChunkStoreRepositoryJpa")
class DocumentChunkStoreRepositoryJpaTest {

    @Mock
    private DocumentChunkPanacheRepository panacheRepository;

    private DocumentChunkStoreRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentChunkStoreRepositoryJpa(panacheRepository);
    }

    @Nested
    @DisplayName("storeChunks")
    class StoreChunks {

        @Test
        @DisplayName("should persist each chunk and update embedding via panache repository")
        void shouldPersistEachChunkAndUpdateEmbedding() {
            DocumentChunk chunk1 = DocumentChunkBuilder.builder()
                    .withId(null)
                    .withDocumentId("doc-a")
                    .withChunkIndex(0)
                    .withContent("The hobbit set out from Bag End.")
                    .build();
            DocumentChunk chunk2 = DocumentChunkBuilder.builder()
                    .withId(null)
                    .withDocumentId("doc-a")
                    .withChunkIndex(1)
                    .withContent("He wore his traveling cloak.")
                    .build();

            float[] emb1 = {0.1f, 0.2f, 0.3f};
            float[] emb2 = {0.4f, 0.5f, 0.6f};

            willAnswer(invocation -> {
                DocumentChunkEntity entity = invocation.getArgument(0);
                entity.id = 100L;
                return null;
            }).given(panacheRepository).persist(org.mockito.ArgumentMatchers.<DocumentChunkEntity>any());

            sut.storeChunks(List.of(chunk1, chunk2), List.of(emb1, emb2));

            then(panacheRepository).should(org.mockito.Mockito.times(2))
                    .persist(org.mockito.ArgumentMatchers.<DocumentChunkEntity>any());
            then(panacheRepository).should(org.mockito.Mockito.times(2))
                    .updateEmbedding(org.mockito.ArgumentMatchers.anyLong(), anyString());
        }

        @Test
        @DisplayName("should handle empty chunk list without interaction")
        void shouldHandleEmptyChunkList() {
            sut.storeChunks(List.of(), List.of());

            then(panacheRepository).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("deleteByDocumentId")
    class DeleteByDocumentId {

        @Test
        @DisplayName("should delegate deletion to panache repository")
        void shouldDelegateDeletionToPanacheRepository() {
            String documentId = "doc-b";
            given(panacheRepository.deleteByDocumentId(documentId)).willReturn(3L);

            sut.deleteByDocumentId(documentId);

            InOrder order = inOrder(panacheRepository);
            order.verify(panacheRepository).deleteByDocumentId(documentId);
            order.verifyNoMoreInteractions();
        }
    }
}
