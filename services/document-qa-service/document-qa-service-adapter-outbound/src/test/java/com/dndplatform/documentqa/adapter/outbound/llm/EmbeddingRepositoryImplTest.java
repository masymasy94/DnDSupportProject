package com.dndplatform.documentqa.adapter.outbound.llm;

import com.dndplatform.common.test.RandomExtension;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.output.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@DisplayName("EmbeddingRepositoryImpl")
class EmbeddingRepositoryImplTest {

    @Mock
    private EmbeddingModel embeddingModel;

    private EmbeddingRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new EmbeddingRepositoryImpl(embeddingModel);
    }

    private Response<Embedding> embeddingResponse(float[] vector) {
        return Response.from(Embedding.from(vector));
    }

    @SuppressWarnings("unchecked")
    private Response<List<Embedding>> batchEmbeddingResponse(int size) {
        List<Embedding> embeddings = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            embeddings.add(Embedding.from(new float[]{(float) i * 0.1f, (float) i * 0.2f}));
        }
        return Response.from(embeddings);
    }

    @Nested
    @DisplayName("embed")
    class Embed {

        @Test
        @DisplayName("should return float vector from embedding model")
        void shouldReturnFloatVectorFromEmbeddingModel() {
            String text = "The dragon sleeps beneath the mountain.";
            float[] expectedVector = {0.1f, 0.2f, 0.3f, 0.4f};
            given(embeddingModel.embed(text)).willReturn(embeddingResponse(expectedVector));

            float[] result = sut.embed(text);

            assertThat(result).isEqualTo(expectedVector);

            then(embeddingModel).should().embed(text);
            then(embeddingModel).shouldHaveNoMoreInteractions();
        }
    }

    @Nested
    @DisplayName("embedAll")
    class EmbedAll {

        @Test
        @DisplayName("should embed single batch when texts size is within batch limit")
        void shouldEmbedSingleBatchWhenTextsWithinBatchLimit() {
            List<String> texts = List.of("Text A", "Text B", "Text C");
            given(embeddingModel.embedAll(any(List.class))).willReturn(batchEmbeddingResponse(3));

            List<float[]> result = sut.embedAll(texts);

            assertThat(result).hasSize(3);
            then(embeddingModel).should(times(1)).embedAll(any(List.class));
            then(embeddingModel).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("should split into multiple batches when texts exceed batch size of 20")
        void shouldSplitIntoMultipleBatchesWhenTextsExceedBatchSize() {
            // 25 texts = 2 batches (20 + 5)
            List<String> texts = IntStream.range(0, 25)
                    .mapToObj(i -> "Text number " + i)
                    .toList();

            given(embeddingModel.embedAll(any(List.class)))
                    .willReturn(batchEmbeddingResponse(20))
                    .willReturn(batchEmbeddingResponse(5));

            List<float[]> result = sut.embedAll(texts);

            assertThat(result).hasSize(25);
            then(embeddingModel).should(times(2)).embedAll(any(List.class));
            then(embeddingModel).shouldHaveNoMoreInteractions();
        }

        @Test
        @DisplayName("should handle exactly 20 texts in one batch")
        void shouldHandleExactly20TextsInOneBatch() {
            List<String> texts = IntStream.range(0, 20)
                    .mapToObj(i -> "Text " + i)
                    .toList();
            given(embeddingModel.embedAll(any(List.class))).willReturn(batchEmbeddingResponse(20));

            List<float[]> result = sut.embedAll(texts);

            assertThat(result).hasSize(20);
            then(embeddingModel).should(times(1)).embedAll(any(List.class));
        }

        @Test
        @DisplayName("should return empty list when input is empty")
        void shouldReturnEmptyListWhenInputIsEmpty() {
            List<float[]> result = sut.embedAll(List.of());

            assertThat(result).isEmpty();
            then(embeddingModel).shouldHaveNoInteractions();
        }
    }
}
