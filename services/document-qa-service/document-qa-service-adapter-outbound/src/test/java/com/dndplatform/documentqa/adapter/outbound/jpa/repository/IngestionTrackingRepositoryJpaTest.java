package com.dndplatform.documentqa.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.documentqa.adapter.outbound.jpa.entity.DocumentIngestionEntity;
import com.dndplatform.documentqa.domain.model.DocumentIngestion;
import com.dndplatform.documentqa.domain.model.IngestionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@DisplayName("IngestionTrackingRepositoryJpa")
class IngestionTrackingRepositoryJpaTest {

    @Mock
    private DocumentIngestionPanacheRepository panacheRepository;

    private IngestionTrackingRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new IngestionTrackingRepositoryJpa(panacheRepository);
    }

    private DocumentIngestionEntity buildEntity(String documentId, String status) {
        DocumentIngestionEntity e = new DocumentIngestionEntity();
        e.documentId = documentId;
        e.fileName = "spellbook.pdf";
        e.contentType = "application/pdf";
        e.status = status;
        e.uploadedBy = "gandalf";
        e.startedAt = LocalDateTime.now();
        e.createdAt = LocalDateTime.now();
        return e;
    }

    @Nested
    @DisplayName("create")
    class Create {

        @Test
        @DisplayName("should persist ingestion tracking entity")
        void shouldPersistIngestionTrackingEntity() {
            String documentId = "doc-001";
            String fileName = "scroll.pdf";
            String contentType = "application/pdf";
            String uploadedBy = "frodo";

            sut.create(documentId, fileName, contentType, uploadedBy);

            InOrder order = inOrder(panacheRepository);
            order.verify(panacheRepository).persist(org.mockito.ArgumentMatchers.<DocumentIngestionEntity>any());
            order.verifyNoMoreInteractions();
        }

        @Test
        @DisplayName("should persist entity with PENDING status")
        void shouldPersistEntityWithPendingStatus() {
            String documentId = "doc-002";

            willAnswer(invocation -> {
                DocumentIngestionEntity entity = invocation.getArgument(0);
                assertThat(entity.status).isEqualTo(IngestionStatus.PENDING.name());
                return null;
            }).given(panacheRepository).persist(org.mockito.ArgumentMatchers.<DocumentIngestionEntity>any());

            sut.create(documentId, "tome.pdf", "application/pdf", "gandalf");

            then(panacheRepository).should().persist(org.mockito.ArgumentMatchers.<DocumentIngestionEntity>any());
        }
    }

    @Nested
    @DisplayName("updateStatus")
    class UpdateStatus {

        @Test
        @DisplayName("should update status to COMPLETED and set completedAt")
        void shouldUpdateStatusToCompletedAndSetCompletedAt() {
            String documentId = "doc-003";
            DocumentIngestionEntity entity = buildEntity(documentId, "PENDING");
            given(panacheRepository.findByIdOptional(documentId)).willReturn(Optional.of(entity));

            sut.updateStatus(documentId, IngestionStatus.COMPLETED, 10, null);

            assertThat(entity.status).isEqualTo("COMPLETED");
            assertThat(entity.chunkCount).isEqualTo(10);
            assertThat(entity.completedAt).isNotNull();
        }

        @Test
        @DisplayName("should update status to FAILED and set completedAt with error message")
        void shouldUpdateStatusToFailedAndSetCompletedAt() {
            String documentId = "doc-004";
            DocumentIngestionEntity entity = buildEntity(documentId, "PENDING");
            given(panacheRepository.findByIdOptional(documentId)).willReturn(Optional.of(entity));

            sut.updateStatus(documentId, IngestionStatus.FAILED, 0, "Parsing error");

            assertThat(entity.status).isEqualTo("FAILED");
            assertThat(entity.errorMessage).isEqualTo("Parsing error");
            assertThat(entity.completedAt).isNotNull();
        }

        @Test
        @DisplayName("should update status to non-terminal status without setting completedAt")
        void shouldUpdateStatusWithoutSettingCompletedAtForNonTerminalStatus() {
            String documentId = "doc-005";
            DocumentIngestionEntity entity = buildEntity(documentId, "PENDING");
            given(panacheRepository.findByIdOptional(documentId)).willReturn(Optional.of(entity));

            sut.updateStatus(documentId, IngestionStatus.CHUNKING, null, null);

            assertThat(entity.status).isEqualTo("CHUNKING");
            assertThat(entity.completedAt).isNull();
        }

        @Test
        @DisplayName("should do nothing and log warning when entity not found")
        void shouldDoNothingWhenEntityNotFound() {
            String documentId = "doc-missing";
            given(panacheRepository.findByIdOptional(documentId)).willReturn(Optional.empty());

            // Should not throw any exception
            sut.updateStatus(documentId, IngestionStatus.COMPLETED, 5, null);

            then(panacheRepository).should().findByIdOptional(documentId);
            then(panacheRepository).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("delete")
    class Delete {

        @Test
        @DisplayName("should call deleteByDocumentId on panache repository")
        void shouldCallDeleteByDocumentId() {
            String documentId = "doc-006";
            given(panacheRepository.deleteByDocumentId(documentId)).willReturn(1L);

            sut.delete(documentId);

            InOrder order = inOrder(panacheRepository);
            order.verify(panacheRepository).deleteByDocumentId(documentId);
            order.verifyNoMoreInteractions();
        }

        @Test
        @DisplayName("should not throw when document not found during delete")
        void shouldNotThrowWhenDocumentNotFound() {
            String documentId = "doc-not-exist";
            given(panacheRepository.deleteByDocumentId(documentId)).willReturn(0L);

            sut.delete(documentId);

            then(panacheRepository).should().deleteByDocumentId(documentId);
        }
    }

    @Nested
    @DisplayName("findByDocumentId")
    class FindByDocumentId {

        @Test
        @DisplayName("should return domain model when entity found")
        void shouldReturnDomainModelWhenEntityFound() {
            String documentId = "doc-007";
            DocumentIngestionEntity entity = buildEntity(documentId, "COMPLETED");
            entity.chunkCount = 5;
            given(panacheRepository.findByIdOptional(documentId)).willReturn(Optional.of(entity));

            Optional<DocumentIngestion> result = sut.findByDocumentId(documentId);

            assertThat(result).isPresent();
            assertThat(result.get().documentId()).isEqualTo(documentId);
            assertThat(result.get().status()).isEqualTo(IngestionStatus.COMPLETED);
            assertThat(result.get().chunkCount()).isEqualTo(5);

            then(panacheRepository).should().findByIdOptional(documentId);
            then(panacheRepository).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("should return empty when entity not found")
        void shouldReturnEmptyWhenEntityNotFound() {
            String documentId = "doc-000";
            given(panacheRepository.findByIdOptional(documentId)).willReturn(Optional.empty());

            Optional<DocumentIngestion> result = sut.findByDocumentId(documentId);

            assertThat(result).isEmpty();

            then(panacheRepository).should().findByIdOptional(documentId);
            then(panacheRepository).shouldHaveNoMoreInteractions();
        }
    }
}
