package com.dndplatform.documentqa.domain.impl;

import com.dndplatform.documentqa.domain.event.DocumentIngestedEventPublisher;
import com.dndplatform.documentqa.domain.model.*;
import com.dndplatform.documentqa.domain.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DocumentIngestionServiceImpl")
class DocumentIngestionServiceImplTest {

    @Mock
    private DocumentFetchRepository documentFetchRepository;

    @Mock
    private DocumentChunkStoreRepository chunkStoreRepository;

    @Mock
    private EmbeddingRepository embeddingRepository;

    @Mock
    private IngestionTrackingRepository trackingRepository;

    @Mock
    private DocumentIngestedEventPublisher eventPublisher;

    @Mock
    private TextExtractorRouter textExtractorRouter;

    @Mock
    private TextChunker textChunker;

    private DocumentIngestionServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentIngestionServiceImpl(
                documentFetchRepository,
                chunkStoreRepository,
                embeddingRepository,
                trackingRepository,
                eventPublisher,
                textExtractorRouter,
                textChunker
        );
    }

    @Nested
    @DisplayName("ingest")
    class Ingest {

        @Test
        @DisplayName("should successfully ingest document through all stages")
        void shouldSuccessfullyIngestDocument() {
            String documentId = "doc-123";
            String fileName = "test.pdf";
            String contentType = "application/pdf";
            String uploadedBy = "user-1";

            DocumentIngestionEvent event = new DocumentIngestionEvent(documentId, fileName, contentType, uploadedBy);

            InputStream inputStream = new ByteArrayInputStream("PDF content".getBytes());
            given(documentFetchRepository.fetch(documentId, fileName)).willReturn(inputStream);

            String extractedText = "This is the extracted text content.";
            given(textExtractorRouter.extract(inputStream, contentType, fileName)).willReturn(extractedText);

            List<String> chunks = List.of("chunk1", "chunk2", "chunk3");
            given(textChunker.chunk(extractedText)).willReturn(chunks);

            List<float[]> embeddings = List.of(new float[]{0.1f}, new float[]{0.2f}, new float[]{0.3f});
            given(embeddingRepository.embedAll(chunks)).willReturn(embeddings);

            sut.ingest(event);

            then(chunkStoreRepository).should().deleteByDocumentId(documentId);
            then(trackingRepository).should().delete(documentId);
            then(trackingRepository).should().create(documentId, fileName, contentType, uploadedBy);
            then(trackingRepository).should().updateStatus(documentId, IngestionStatus.FETCHING_DOCUMENT, null, null);
            then(documentFetchRepository).should().fetch(documentId, fileName);
            then(trackingRepository).should().updateStatus(documentId, IngestionStatus.EXTRACTING_TEXT, null, null);
            then(textExtractorRouter).should().extract(inputStream, contentType, fileName);
            then(trackingRepository).should().updateStatus(documentId, IngestionStatus.CHUNKING, null, null);
            then(textChunker).should().chunk(extractedText);
            then(trackingRepository).should().updateStatus(documentId, IngestionStatus.GENERATING_EMBEDDINGS, null, null);
            then(embeddingRepository).should().embedAll(chunks);
            then(trackingRepository).should().updateStatus(documentId, IngestionStatus.STORING_VECTORS, null, null);
            then(chunkStoreRepository).should().storeChunks(anyList(), eq(embeddings));
            then(trackingRepository).should().updateStatus(documentId, IngestionStatus.COMPLETED, 3, null);
            then(eventPublisher).should().publish(documentId, IngestionStatus.COMPLETED, null, 3);
        }

        @Test
        @DisplayName("should handle failure and update status to FAILED")
        void shouldHandleFailure() {
            String documentId = "doc-123";
            String fileName = "test.pdf";
            String contentType = "application/pdf";
            String uploadedBy = "user-1";

            DocumentIngestionEvent event = new DocumentIngestionEvent(documentId, fileName, contentType, uploadedBy);

            RuntimeException fetchException = new RuntimeException("Failed to fetch document");
            given(documentFetchRepository.fetch(documentId, fileName)).willThrow(fetchException);

            sut.ingest(event);

            then(chunkStoreRepository).should().deleteByDocumentId(documentId);
            then(trackingRepository).should().updateStatus(documentId, IngestionStatus.FAILED, null, fetchException.getMessage());
            then(eventPublisher).should().publish(documentId, IngestionStatus.FAILED, fetchException.getMessage(), null);
            then(chunkStoreRepository).shouldHaveNoMoreInteractions();
            then(embeddingRepository).shouldHaveNoMoreInteractions();
            then(textExtractorRouter).shouldHaveNoMoreInteractions();
            then(textChunker).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("should handle failure during text extraction")
        void shouldHandleFailureDuringTextExtraction() {
            String documentId = "doc-123";
            String fileName = "test.pdf";
            String contentType = "application/pdf";
            String uploadedBy = "user-1";

            DocumentIngestionEvent event = new DocumentIngestionEvent(documentId, fileName, contentType, uploadedBy);

            InputStream inputStream = new ByteArrayInputStream("content".getBytes());
            given(documentFetchRepository.fetch(documentId, fileName)).willReturn(inputStream);

            RuntimeException extractionException = new RuntimeException("Unsupported format");
            given(textExtractorRouter.extract(inputStream, contentType, fileName)).willThrow(extractionException);

            sut.ingest(event);

            then(trackingRepository).should().updateStatus(documentId, IngestionStatus.FAILED, null, extractionException.getMessage());
            then(eventPublisher).should().publish(documentId, IngestionStatus.FAILED, extractionException.getMessage(), null);
        }

        @Test
        @DisplayName("should handle failure during chunking")
        void shouldHandleFailureDuringChunking() {
            String documentId = "doc-123";
            String fileName = "test.pdf";
            String contentType = "application/pdf";
            String uploadedBy = "user-1";

            DocumentIngestionEvent event = new DocumentIngestionEvent(documentId, fileName, contentType, uploadedBy);

            InputStream inputStream = new ByteArrayInputStream("content".getBytes());
            given(documentFetchRepository.fetch(documentId, fileName)).willReturn(inputStream);

            String extractedText = "text";
            given(textExtractorRouter.extract(inputStream, contentType, fileName)).willReturn(extractedText);

            RuntimeException chunkingException = new RuntimeException("Text too short");
            given(textChunker.chunk(extractedText)).willThrow(chunkingException);

            sut.ingest(event);

            then(trackingRepository).should().updateStatus(documentId, IngestionStatus.FAILED, null, chunkingException.getMessage());
            then(eventPublisher).should().publish(documentId, IngestionStatus.FAILED, chunkingException.getMessage(), null);
        }
    }
}
