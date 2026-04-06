package com.dndplatform.documentqa.adapter.outbound.text;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PdfTextExtractorTest {

    private PdfTextExtractor sut;

    @BeforeEach
    void setUp() {
        sut = new PdfTextExtractor();
    }

    private InputStream pdfWithText(String text) throws Exception {
        PDDocument doc = new PDDocument();
        PDPage page = new PDPage();
        doc.addPage(page);
        try (PDPageContentStream content = new PDPageContentStream(doc, page)) {
            content.beginText();
            content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
            content.showText(text);
            content.endText();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        doc.save(baos);
        doc.close();
        return new ByteArrayInputStream(baos.toByteArray());
    }

    private InputStream emptyPdf() throws Exception {
        PDDocument doc = new PDDocument();
        doc.addPage(new PDPage());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        doc.save(baos);
        doc.close();
        return new ByteArrayInputStream(baos.toByteArray());
    }

    @Test
    void shouldExtractTextFromPdf() throws Exception {
        InputStream input = pdfWithText("Hello adventurer");

        String result = sut.extract(input);

        assertThat(result).contains("Hello adventurer");
    }

    @Test
    void shouldReturnBlankForEmptyPage() throws Exception {
        InputStream input = emptyPdf();

        String result = sut.extract(input);

        assertThat(result).isBlank();
    }

    @Test
    void shouldThrowRuntimeExceptionWhenInputStreamFails() {
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
                .hasMessageContaining("Failed to extract text from PDF");
    }
}
