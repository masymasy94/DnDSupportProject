package com.dndplatform.documentqa.adapter.outbound.text;

import com.dndplatform.documentqa.domain.impl.TextChunker;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.ArrayList;
import java.util.List;
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
        if (text == null || text.isEmpty()) {
            return List.of();
        }

        List<String> chunks = new ArrayList<>();
        int step = chunkSize - overlap;
        int length = text.length();

        for (int pos = 0; pos < length; pos += step) {
            int end = Math.min(pos + chunkSize, length);
            chunks.add(text.substring(pos, end));
        }

        log.info(() -> "Chunked text of %d characters into %d chunks (size=%d, overlap=%d)"
                .formatted(text.length(), chunks.size(), chunkSize, overlap));
        return chunks;
    }
}
