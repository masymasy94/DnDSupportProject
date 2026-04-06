package com.dndplatform.character.adapter.inbound.importsheet;

import com.dndplatform.character.view.model.vm.CharacterViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import jakarta.ws.rs.BadRequestException;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class CharacterImportSheetResourceImplTest {

    @Mock
    private CharacterImportSheetDelegate delegate;

    @Mock
    private FileUpload fileUpload;

    private CharacterImportSheetResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new CharacterImportSheetResourceImpl(delegate);
    }

    @Test
    void shouldThrowBadRequestWhenFileIsNull() {
        assertThatThrownBy(() -> sut.importSheet(null, 1L))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void shouldThrowBadRequestWhenContentTypeIsNotPdf(@Random Long userId) {
        given(fileUpload.contentType()).willReturn("text/plain");

        assertThatThrownBy(() -> sut.importSheet(fileUpload, userId))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void shouldDelegateImportSheetWithPdfContentType(@Random Long userId, @Random CharacterViewModel expected, @TempDir Path tempDir) throws IOException {
        var pdfFile = tempDir.resolve("sheet.pdf");
        var pdfBytes = new byte[]{37, 80, 68, 70, 45}; // %PDF-
        Files.write(pdfFile, pdfBytes);

        given(fileUpload.contentType()).willReturn("application/pdf");
        given(fileUpload.uploadedFile()).willReturn(pdfFile);
        given(fileUpload.fileName()).willReturn("sheet.pdf");
        given(delegate.importSheetWithUserId(pdfBytes, "sheet.pdf", "application/pdf", userId)).willReturn(expected);

        var result = sut.importSheet(fileUpload, userId);

        assertThat(result).isEqualTo(expected);
        then(delegate).should().importSheetWithUserId(pdfBytes, "sheet.pdf", "application/pdf", userId);
        then(delegate).shouldHaveNoMoreInteractions();
    }

    @Test
    void shouldThrowBadRequestWhenFileIsEmpty(@Random Long userId, @TempDir Path tempDir) throws IOException {
        var emptyFile = tempDir.resolve("empty.pdf");
        Files.write(emptyFile, new byte[0]);

        given(fileUpload.contentType()).willReturn("application/pdf");
        given(fileUpload.uploadedFile()).willReturn(emptyFile);

        assertThatThrownBy(() -> sut.importSheet(fileUpload, userId))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void shouldAcceptNullContentType(@Random Long userId, @Random CharacterViewModel expected, @TempDir Path tempDir) throws IOException {
        var pdfFile = tempDir.resolve("sheet.pdf");
        var pdfBytes = new byte[]{37, 80, 68, 70, 45}; // %PDF-
        Files.write(pdfFile, pdfBytes);

        given(fileUpload.contentType()).willReturn(null);
        given(fileUpload.uploadedFile()).willReturn(pdfFile);
        given(fileUpload.fileName()).willReturn("sheet.pdf");
        given(delegate.importSheetWithUserId(pdfBytes, "sheet.pdf", "application/pdf", userId)).willReturn(expected);

        var result = sut.importSheet(fileUpload, userId);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldUseDefaultFileNameWhenNull(@Random Long userId, @Random CharacterViewModel expected, @TempDir Path tempDir) throws IOException {
        var pdfFile = tempDir.resolve("sheet.pdf");
        var pdfBytes = new byte[]{37, 80, 68, 70, 45};
        Files.write(pdfFile, pdfBytes);

        given(fileUpload.contentType()).willReturn("application/pdf");
        given(fileUpload.uploadedFile()).willReturn(pdfFile);
        given(fileUpload.fileName()).willReturn(null);
        given(delegate.importSheetWithUserId(pdfBytes, "character-sheet.pdf", "application/pdf", userId)).willReturn(expected);

        var result = sut.importSheet(fileUpload, userId);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    void shouldThrowUnsupportedOperationForStreamEndpoint() {
        assertThatThrownBy(() -> sut.importSheet(null, "file.pdf"))
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
