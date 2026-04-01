package com.dndplatform.documentqa.adapter.outbound.text;

import com.dndplatform.documentqa.domain.impl.TextChunker;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

@ApplicationScoped
public class TextChunkerImpl implements TextChunker {

    private final Logger log = Logger.getLogger(getClass().getName());

    @ConfigProperty(name = "rag.chunk.size", defaultValue = "1000")
    int chunkSize;

    @ConfigProperty(name = "rag.chunk.overlap", defaultValue = "200")
    int overlap;

    @Override
    public List<String> chunk(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }

        List<String> sentences = splitIntoSentences(text);
        List<String> chunks = new ArrayList<>();
        List<String> window = new ArrayList<>();
        int windowLength = 0;

        for (String sentence : sentences) {
            // Sentence exceeds chunkSize on its own — flush window then emit as standalone chunk
            if (sentence.length() >= chunkSize) {
                if (!window.isEmpty()) {
                    chunks.add(String.join(" ", window));
                    window.clear();
                    windowLength = 0;
                }
                chunks.add(sentence);
                continue;
            }

            // Adding this sentence would overflow the chunk — flush and slide the window
            if (windowLength + sentence.length() > chunkSize && !window.isEmpty()) {
                chunks.add(String.join(" ", window));

                // Drop sentences from the front until the retained tail fits within overlap budget
                while (windowLength > overlap && !window.isEmpty()) {
                    String dropped = window.remove(0);
                    windowLength = Math.max(0, windowLength - dropped.length() - 1);
                }
            }

            window.add(sentence);
            windowLength += sentence.length() + 1; // +1 accounts for the joining space
        }

        if (!window.isEmpty()) {
            chunks.add(String.join(" ", window));
        }

        log.info(() -> "Chunked text of %d characters into %d sentence-aware chunks (size=%d, overlap=%d)"
                .formatted(text.length(), chunks.size(), chunkSize, overlap));
        return chunks;
    }

    private List<String> splitIntoSentences(String text) {
        List<String> sentences = new ArrayList<>();
        BreakIterator boundary = BreakIterator.getSentenceInstance(Locale.ENGLISH);
        boundary.setText(text);

        int start = boundary.first();
        for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
            String sentence = text.substring(start, end).strip();
            if (!sentence.isEmpty()) {
                sentences.add(sentence);
            }
        }
        return sentences;
    }
}
