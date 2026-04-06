package com.dndplatform.documentqa.adapter.outbound.text;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class TextExtractorRouterImplTest {

    @Mock
    private PdfTextExtractor pdfTextExtractor;

    @Mock
    private PlainTextExtractor plainTextExtractor;

    @InjectMocks
    private TextExtractorRouterImpl sut;

    private InputStream stream(String content) {
        return new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void extract_shouldRouteToPdfExtractorForPdfContentType() {
        InputStream inputStream = stream("pdf bytes");
        given(pdfTextExtractor.extract(same(inputStream))).willReturn("Extracted PDF text");

        String result = sut.extract(inputStream, "application/pdf", "rulebook.pdf");

        assertThat(result).isEqualTo("Extracted PDF text");
        then(pdfTextExtractor).should().extract(inputStream);
        then(plainTextExtractor).shouldHaveNoInteractions();
    }

    @Test
    void extract_shouldRouteToPlainExtractorForTextContentType() {
        InputStream inputStream = stream("plain text");
        given(plainTextExtractor.extract(same(inputStream))).willReturn("Extracted plain text");

        String result = sut.extract(inputStream, "text/plain", "notes.txt");

        assertThat(result).isEqualTo("Extracted plain text");
        then(plainTextExtractor).should().extract(inputStream);
        then(pdfTextExtractor).shouldHaveNoInteractions();
    }

    @Test
    void extract_shouldRouteToPlainExtractorForUnknownContentType() {
        InputStream inputStream = stream("some data");
        given(plainTextExtractor.extract(same(inputStream))).willReturn("Some extracted text");

        String result = sut.extract(inputStream, "application/octet-stream", "data.bin");

        assertThat(result).isEqualTo("Some extracted text");
        then(plainTextExtractor).should().extract(inputStream);
        then(pdfTextExtractor).shouldHaveNoInteractions();
    }

    @Test
    void extract_shouldRouteToPlainExtractorForNullContentType() {
        InputStream inputStream = stream("data");
        given(plainTextExtractor.extract(same(inputStream))).willReturn("Fallback text");

        String result = sut.extract(inputStream, null, "unknown.file");

        assertThat(result).isEqualTo("Fallback text");
        then(pdfTextExtractor).shouldHaveNoInteractions();
    }

    @Test
    void extract_shouldRouteToPlainExtractorForTextHtmlContentType() {
        InputStream inputStream = stream("<html>test</html>");
        given(plainTextExtractor.extract(same(inputStream))).willReturn("<html>test</html>");

        String result = sut.extract(inputStream, "text/html", "page.html");

        assertThat(result).isEqualTo("<html>test</html>");
        then(plainTextExtractor).should().extract(inputStream);
    }
}
