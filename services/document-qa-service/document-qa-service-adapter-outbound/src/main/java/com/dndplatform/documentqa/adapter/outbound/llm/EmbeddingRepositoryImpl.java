package com.dndplatform.documentqa.adapter.outbound.llm;

import com.dndplatform.documentqa.domain.repository.EmbeddingRepository;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class EmbeddingRepositoryImpl implements EmbeddingRepository {

    private static final int BATCH_SIZE = 20;

    private final Logger log = Logger.getLogger(getClass().getName());

    private final EmbeddingModel embeddingModel;

    @Inject
    public EmbeddingRepositoryImpl(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @Override
    public float[] embed(String text) {
        log.info(() -> "Generating embedding for text of %d characters".formatted(text.length()));
        return embeddingModel.embed(text).content().vector();
    }

    @Override
    public List<float[]> embedAll(List<String> texts) {
        log.info(() -> "Generating embeddings for %d texts in batches of %d".formatted(texts.size(), BATCH_SIZE));
        List<float[]> allEmbeddings = new ArrayList<>();

        for (int i = 0; i < texts.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, texts.size());
            int batchStart = i;
            log.info(() -> "Embedding batch %d-%d of %d".formatted(batchStart + 1, end, texts.size()));

            List<TextSegment> batch = texts.subList(i, end).stream()
                    .map(TextSegment::from)
                    .toList();

            Response<List<Embedding>> response = embeddingModel.embedAll(batch);
            response.content().stream()
                    .map(Embedding::vector)
                    .forEach(allEmbeddings::add);
        }

        log.info(() -> "Generated %d embeddings total".formatted(allEmbeddings.size()));
        return allEmbeddings;
    }
}
