package com.dndplatform.documentqa.domain.impl;

import com.dndplatform.documentqa.domain.DocumentIngestionService;
import com.dndplatform.documentqa.domain.event.DocumentIngestedEventPublisher;
import com.dndplatform.documentqa.domain.model.DocumentChunk;
import com.dndplatform.documentqa.domain.model.DocumentChunkBuilder;
import com.dndplatform.documentqa.domain.model.DocumentIngestionEvent;
import com.dndplatform.documentqa.domain.model.IngestionStatus;
import com.dndplatform.documentqa.domain.repository.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class DocumentIngestionServiceImpl implements DocumentIngestionService {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final DocumentFetchRepository documentFetchRepository;
    private final DocumentChunkStoreRepository chunkStoreRepository;
    private final EmbeddingRepository embeddingRepository;
    private final IngestionTrackingRepository trackingRepository;
    private final DocumentIngestedEventPublisher eventPublisher;
    private final TextExtractorRouter textExtractorRouter;
    private final TextChunker textChunker;

    @Inject
    public DocumentIngestionServiceImpl(DocumentFetchRepository documentFetchRepository,
                                        DocumentChunkStoreRepository chunkStoreRepository,
                                        EmbeddingRepository embeddingRepository,
                                        IngestionTrackingRepository trackingRepository,
                                        DocumentIngestedEventPublisher eventPublisher,
                                        TextExtractorRouter textExtractorRouter,
                                        TextChunker textChunker) {
        this.documentFetchRepository = documentFetchRepository;
        this.chunkStoreRepository = chunkStoreRepository;
        this.embeddingRepository = embeddingRepository;
        this.trackingRepository = trackingRepository;
        this.eventPublisher = eventPublisher;
        this.textExtractorRouter = textExtractorRouter;
        this.textChunker = textChunker;
    }

    @Override
    public void ingest(DocumentIngestionEvent event) {
        String documentId = event.documentId();
        try {
            // Clean up previous ingestion data (for re-ingestion)
            chunkStoreRepository.deleteByDocumentId(documentId);
            trackingRepository.delete(documentId);

            trackingRepository.create(documentId, event.fileName(), event.contentType(), event.uploadedBy());

            // Step 1: Fetch document
            trackingRepository.updateStatus(documentId, IngestionStatus.FETCHING_DOCUMENT, null, null);
            InputStream inputStream = documentFetchRepository.fetch(documentId, event.fileName());

            // Step 2: Extract text
            trackingRepository.updateStatus(documentId, IngestionStatus.EXTRACTING_TEXT, null, null);
            String text = textExtractorRouter.extract(inputStream, event.contentType(), event.fileName());

            // Step 3: Chunk text
            trackingRepository.updateStatus(documentId, IngestionStatus.CHUNKING, null, null);
            List<String> chunks = textChunker.chunk(text);

            // Step 4: Generate embeddings
            trackingRepository.updateStatus(documentId, IngestionStatus.GENERATING_EMBEDDINGS, null, null);
            List<float[]> embeddings = embeddingRepository.embedAll(chunks);

            // Step 5: Store vectors
            trackingRepository.updateStatus(documentId, IngestionStatus.STORING_VECTORS, null, null);
            List<DocumentChunk> documentChunks = new java.util.ArrayList<>();
            for (int i = 0; i < chunks.size(); i++) {
                documentChunks.add(DocumentChunkBuilder.builder()
                        .withDocumentId(documentId)
                        .withChunkIndex(i)
                        .withContent(chunks.get(i))
                        .build());
            }
            chunkStoreRepository.storeChunks(documentChunks, embeddings);

            // Step 6: Mark completed
            trackingRepository.updateStatus(documentId, IngestionStatus.COMPLETED, chunks.size(), null);
            eventPublisher.publish(documentId, IngestionStatus.COMPLETED, null, chunks.size());

            log.info(() -> "Successfully ingested document %s with %d chunks".formatted(documentId, chunks.size()));

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to ingest document: " + documentId, e);
            trackingRepository.updateStatus(documentId, IngestionStatus.FAILED, null, e.getMessage());
            eventPublisher.publish(documentId, IngestionStatus.FAILED, e.getMessage(), null);
        }
    }
}
