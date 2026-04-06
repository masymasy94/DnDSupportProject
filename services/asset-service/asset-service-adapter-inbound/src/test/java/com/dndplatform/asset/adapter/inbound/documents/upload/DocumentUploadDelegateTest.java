package com.dndplatform.asset.adapter.inbound.documents.upload;

import com.dndplatform.asset.domain.DocumentUploadService;
import com.dndplatform.asset.domain.model.Document;
import com.dndplatform.asset.domain.model.RagStatus;
import com.dndplatform.asset.view.model.vm.DocumentViewModel;
import com.dndplatform.common.test.RandomExtension;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class DocumentUploadDelegateTest {

    @Mock
    private DocumentUploadService service;

    @Mock
    private FileUpload fileUpload;

    private DocumentUploadDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentUploadDelegate(service);
    }

    @Test
    void shouldDelegateToServiceAndReturnViewModel() throws IOException {
        var tempFile = Files.createTempFile("test-upload", ".pdf");
        Files.write(tempFile, new byte[]{1, 2, 3});
        var document = new Document("doc-1", "sheet.pdf", "application/pdf", 3L, "gandalf", Instant.now(), RagStatus.PENDING);
        var uploadedBy = "gandalf";

        given(fileUpload.fileName()).willReturn("sheet.pdf");
        given(fileUpload.contentType()).willReturn("application/pdf");
        given(fileUpload.uploadedFile()).willReturn(tempFile);
        given(fileUpload.size()).willReturn(3L);
        given(service.upload(eq("sheet.pdf"), eq("application/pdf"), any(InputStream.class), eq(3L), eq(uploadedBy)))
                .willReturn(document);

        DocumentViewModel result = sut.upload(fileUpload, uploadedBy);

        assertThat(result.id()).isEqualTo("doc-1");
        assertThat(result.fileName()).isEqualTo("sheet.pdf");
        assertThat(result.contentType()).isEqualTo("application/pdf");
        assertThat(result.ragStatus()).isEqualTo("PENDING");

        var inOrder = inOrder(service);
        then(service).should(inOrder).upload(eq("sheet.pdf"), eq("application/pdf"), any(InputStream.class), eq(3L), eq(uploadedBy));
        inOrder.verifyNoMoreInteractions();

        Files.deleteIfExists(tempFile);
    }

    @Test
    void shouldThrowRuntimeExceptionWhenFileCannotBeRead() throws IOException {
        var nonExistentPath = Path.of("/tmp/nonexistent-file-that-does-not-exist.pdf");
        given(fileUpload.uploadedFile()).willReturn(nonExistentPath);

        assertThatThrownBy(() -> sut.upload(fileUpload, "gandalf"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to read uploaded file");
    }
}
