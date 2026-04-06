package com.dndplatform.documentqa.domain.impl;

import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.documentqa.domain.model.DocumentIngestion;
import com.dndplatform.documentqa.domain.model.IngestionStatus;
import com.dndplatform.documentqa.domain.repository.IngestionTrackingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@DisplayName("IngestionStatusServiceImpl")
class IngestionStatusServiceImplTest {

    @Mock
    private IngestionTrackingRepository repository;

    private IngestionStatusServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new IngestionStatusServiceImpl(repository);
    }

    @Nested
    @DisplayName("getStatus")
    class GetStatus {

        @Test
        @DisplayName("should return ingestion status when document exists")
        void shouldReturnIngestionStatusWhenDocumentExists() {
            String documentId = "doc-123";
            DocumentIngestion expected = new DocumentIngestion(
                    documentId,
                    "test.pdf",
                    "application/pdf",
                    IngestionStatus.COMPLETED,
                    5,
                    null,
                    "user-1",
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            given(repository.findByDocumentId(documentId)).willReturn(Optional.of(expected));

            DocumentIngestion result = sut.getStatus(documentId);

            assertThat(result).isEqualTo(expected);
            then(repository).should().findByDocumentId(documentId);
            then(repository).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("should throw NotFoundException when document does not exist")
        void shouldThrowNotFoundExceptionWhenDocumentDoesNotExist() {
            String documentId = "nonexistent";
            given(repository.findByDocumentId(documentId)).willReturn(Optional.empty());

            assertThatThrownBy(() -> sut.getStatus(documentId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Ingestion not found for document: " + documentId);

            then(repository).should().findByDocumentId(documentId);
            then(repository).shouldHaveNoMoreInteractions();
        }
    }
}
