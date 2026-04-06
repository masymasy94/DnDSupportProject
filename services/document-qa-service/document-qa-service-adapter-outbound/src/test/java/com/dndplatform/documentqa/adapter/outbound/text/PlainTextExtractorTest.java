package com.dndplatform.documentqa.adapter.outbound.text;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class PlainTextExtractorTest {

    private PlainTextExtractor sut;

    @BeforeEach
    void setUp() {
        sut = new PlainTextExtractor();
    }

    private InputStream stream(String text) {
        return new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void extract_shouldReturnTextContent() {
        String content = "Hello, adventurer!";

        String result = sut.extract(stream(content));

        assertThat(result).isEqualTo(content);
    }

    @Test
    void extract_shouldReturnEmptyStringForEmptyStream() {
        String result = sut.extract(stream(""));

        assertThat(result).isEmpty();
    }

    @Test
    void extract_shouldHandleMultilineContent() {
        String content = "Line 1\nLine 2\nLine 3";

        String result = sut.extract(stream(content));

        assertThat(result).isEqualTo(content);
    }

    @Test
    void extract_shouldHandleUnicodeContent() {
        String content = "Gandalf \u00e9l\u0151 \u00fczenet";

        String result = sut.extract(stream(content));

        assertThat(result).isEqualTo(content);
    }

    @Test
    void extract_shouldWrapIOExceptionInRuntimeException() {
        InputStream broken = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException("disk error");
            }

            @Override
            public byte[] readAllBytes() throws IOException {
                throw new IOException("disk error");
            }
        };

        assertThatThrownBy(() -> sut.extract(broken))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to extract plain text");
    }
}
