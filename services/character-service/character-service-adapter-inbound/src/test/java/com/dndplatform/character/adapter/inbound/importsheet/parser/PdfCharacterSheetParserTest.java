package com.dndplatform.character.adapter.inbound.importsheet.parser;

import jakarta.ws.rs.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PdfCharacterSheetParserTest {

    private PdfCharacterSheetParser sut;

    @BeforeEach
    void setUp() {
        sut = new PdfCharacterSheetParser();
    }

    @Test
    void shouldExtractFormFieldsFromValidPdf() throws IOException {
        byte[] pdfBytes = loadTemplate();

        Map<String, String> fields = sut.extractFormFields(pdfBytes);

        assertThat(fields).isNotEmpty();
        assertThat(fields).containsKey("CharacterName");
        assertThat(fields).containsKey("STR");
        assertThat(fields).containsKey("ClassLevel");
    }

    @Test
    void shouldThrowBadRequestForInvalidPdfBytes() {
        byte[] garbage = "not a pdf".getBytes();

        assertThatThrownBy(() -> sut.extractFormFields(garbage))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Failed to parse PDF");
    }

    @Test
    void shouldThrowBadRequestForTruncatedPdf() {
        byte[] truncated = new byte[]{0x25, 0x50, 0x44, 0x46, 0x2D};

        assertThatThrownBy(() -> sut.extractFormFields(truncated))
                .isInstanceOf(BadRequestException.class);
    }

    private byte[] loadTemplate() throws IOException {
        try (InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("META-INF/resources/wotc-5e-sheet.pdf")) {
            if (is == null) {
                throw new RuntimeException("PDF template not found");
            }
            return is.readAllBytes();
        }
    }
}
