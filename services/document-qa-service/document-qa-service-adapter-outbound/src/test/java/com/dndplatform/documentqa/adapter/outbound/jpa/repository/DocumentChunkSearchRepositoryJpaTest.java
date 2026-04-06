package com.dndplatform.documentqa.adapter.outbound.jpa.repository;

import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.documentqa.domain.model.SourceReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
@DisplayName("DocumentChunkSearchRepositoryJpa")
class DocumentChunkSearchRepositoryJpaTest {

    @Mock
    private DocumentChunkPanacheRepository panacheRepository;

    private DocumentChunkSearchRepositoryJpa sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentChunkSearchRepositoryJpa(panacheRepository);
    }

    private float[] buildEmbedding() {
        return new float[]{0.1f, 0.2f, 0.3f};
    }

    @Nested
    @DisplayName("searchSimilar")
    class SearchSimilar {

        @Test
        @DisplayName("should execute query with document filter and return results")
        void shouldExecuteQueryWithDocumentFilterAndReturnResults() {
            float[] queryEmbedding = buildEmbedding();
            List<String> documentIds = List.of("doc-1", "doc-2");
            int topK = 5;

            Object[] row1 = {"doc-1", "spellbook.pdf", "Dragons breathe fire.", 0.87};
            Object[] row2 = {"doc-2", "scroll.pdf", "The hobbit loves elevenses.", 0.75};

            given(panacheRepository.searchSimilar(anyString(), eq(true), eq(documentIds), eq(topK)))
                    .willReturn(List.of(row1, row2));

            List<SourceReference> result = sut.searchSimilar(queryEmbedding, documentIds, topK);

            assertThat(result).hasSize(2);
            assertThat(result.get(0).documentId()).isEqualTo("doc-1");
            assertThat(result.get(0).fileName()).isEqualTo("spellbook.pdf");
            assertThat(result.get(0).excerpt()).isEqualTo("Dragons breathe fire.");
            assertThat(result.get(0).score()).isEqualTo(0.87);
            assertThat(result.get(1).documentId()).isEqualTo("doc-2");

            then(panacheRepository).should().searchSimilar(anyString(), eq(true), eq(documentIds), eq(topK));
        }

        @Test
        @DisplayName("should execute query without document filter when documentIds is null")
        void shouldExecuteQueryWithoutDocumentFilterWhenDocumentIdsIsNull() {
            float[] queryEmbedding = buildEmbedding();
            int topK = 3;

            Object[] row = {"doc-x", "tome.pdf", "Ancient magic.", 0.95};
            List<Object[]> rows = new ArrayList<>();
            rows.add(row);

            given(panacheRepository.searchSimilar(anyString(), eq(false), isNull(), eq(topK)))
                    .willReturn(rows);

            List<SourceReference> result = sut.searchSimilar(queryEmbedding, null, topK);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).documentId()).isEqualTo("doc-x");
            assertThat(result.get(0).score()).isEqualTo(0.95);
        }

        @Test
        @DisplayName("should return empty list when no results found")
        void shouldReturnEmptyListWhenNoResultsFound() {
            float[] queryEmbedding = buildEmbedding();

            given(panacheRepository.searchSimilar(anyString(), eq(false), eq(List.of()), eq(10)))
                    .willReturn(List.of());

            List<SourceReference> result = sut.searchSimilar(queryEmbedding, List.of(), 10);

            assertThat(result).isEmpty();
        }
    }
}
