package com.dndplatform.documentqa.adapter.outbound.text;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TextChunkerImplTest {

    private TextChunkerImpl sut;

    @BeforeEach
    void setUp() {
        sut = new TextChunkerImpl(100, 20);
    }

    // ========================
    // Null / blank / empty
    // ========================

    @Test
    void chunk_shouldReturnEmptyListForNullInput() {
        List<String> result = sut.chunk(null);

        assertThat(result).isEmpty();
    }

    @Test
    void chunk_shouldReturnEmptyListForBlankInput() {
        List<String> result = sut.chunk("   ");

        assertThat(result).isEmpty();
    }

    @Test
    void chunk_shouldReturnEmptyListForEmptyString() {
        List<String> result = sut.chunk("");

        assertThat(result).isEmpty();
    }

    // ========================
    // Short text (fits in one chunk)
    // ========================

    @Test
    void chunk_shouldReturnSingleChunkForShortText() {
        String text = "Short sentence.";

        List<String> result = sut.chunk(text);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).contains("Short sentence");
    }

    @Test
    void chunk_shouldReturnNonEmptyChunks() {
        String text = "The wizard cast a fireball. The dragon roared. The party fled.";

        List<String> result = sut.chunk(text);

        assertThat(result).isNotEmpty();
        result.forEach(chunk -> assertThat(chunk).isNotBlank());
    }

    // ========================
    // Chunking behaviour
    // ========================

    @Test
    void chunk_shouldSplitTextExceedingChunkSize() {
        // Use a small chunk size to force splits
        sut = new TextChunkerImpl(50, 10);

        // Build text that clearly requires multiple chunks
        String sentence1 = "The ancient tome contained forbidden knowledge.";
        String sentence2 = "Dragons fear the silver blade.";
        String sentence3 = "Wizards study for many long years.";
        String text = sentence1 + " " + sentence2 + " " + sentence3;

        List<String> result = sut.chunk(text);

        assertThat(result).hasSizeGreaterThanOrEqualTo(2);
    }

    @Test
    void chunk_shouldHandleSingleSentenceExceedingChunkSize() {
        // A single sentence larger than chunkSize should be emitted as a standalone chunk
        sut = new TextChunkerImpl(20, 5);
        String longSentence = "This sentence is much longer than the configured chunk size limit.";

        List<String> result = sut.chunk(longSentence);

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(longSentence.strip());
    }

    @Test
    void chunk_shouldPreserveTextContent() {
        String text = "Magic is real. Dragons exist. Adventures await.";

        List<String> result = sut.chunk(text);

        String combined = String.join(" ", result);
        assertThat(combined).contains("Magic is real");
        assertThat(combined).contains("Dragons exist");
        assertThat(combined).contains("Adventures await");
    }

    // ========================
    // Multiple sentences -> overlap behavior
    // ========================

    @Test
    void chunk_shouldProduceChunksWithSentenceBoundaries() {
        sut = new TextChunkerImpl(60, 15);
        String text = "First sentence here. Second sentence follows. Third sentence ends.";

        List<String> result = sut.chunk(text);

        assertThat(result).isNotEmpty();
        // All content should be represented across all chunks
        String allChunks = String.join(" ", result);
        assertThat(allChunks).contains("First sentence");
        assertThat(allChunks).contains("Second sentence");
        assertThat(allChunks).contains("Third sentence");
    }

    @Test
    void chunk_shouldHandleTextWithOnlyOneSentence() {
        String text = "A single complete sentence that fits within the chunk size.";

        List<String> result = sut.chunk(text);

        assertThat(result).hasSize(1);
    }
}
