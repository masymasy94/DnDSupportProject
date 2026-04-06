package com.dndplatform.asset.adapter.inbound.documents.uploadbatch;

import com.dndplatform.asset.domain.DocumentUploadBatchService;
import com.dndplatform.asset.domain.model.Document;
import com.dndplatform.asset.domain.model.DocumentContent;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class DocumentUploadBatchDelegateTest {

    @Mock
    private DocumentUploadBatchService service;

    @Mock
    private FileUpload fileUpload1;

    @Mock
    private FileUpload fileUpload2;

    private DocumentUploadBatchDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentUploadBatchDelegate(service);
    }

    @Test
    void shouldDelegateToServiceAndReturnMappedViewModels() throws IOException {
        var tempFile1 = Files.createTempFile("test-upload-1", ".pdf");
        var tempFile2 = Files.createTempFile("test-upload-2", ".pdf");
        Files.write(tempFile1, new byte[]{1, 2, 3});
        Files.write(tempFile2, new byte[]{4, 5, 6});

        var uploadedBy = "gandalf";
        var doc1 = new Document("id-1", "file1.pdf", "application/pdf", 3L, uploadedBy, Instant.now(), RagStatus.PENDING);
        var doc2 = new Document("id-2", "file2.pdf", "application/pdf", 3L, uploadedBy, Instant.now(), null);

        given(fileUpload1.fileName()).willReturn("file1.pdf");
        given(fileUpload1.contentType()).willReturn("application/pdf");
        given(fileUpload1.uploadedFile()).willReturn(tempFile1);
        given(fileUpload1.size()).willReturn(3L);

        given(fileUpload2.fileName()).willReturn("file2.pdf");
        given(fileUpload2.contentType()).willReturn("application/pdf");
        given(fileUpload2.uploadedFile()).willReturn(tempFile2);
        given(fileUpload2.size()).willReturn(3L);

        given(service.uploadMultiple(any(List.class), eq(uploadedBy))).willReturn(List.of(doc1, doc2));

        List<DocumentViewModel> result = sut.uploadMultiple(List.of(fileUpload1, fileUpload2), uploadedBy);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).id()).isEqualTo("id-1");
        assertThat(result.get(0).ragStatus()).isEqualTo("PENDING");
        assertThat(result.get(1).id()).isEqualTo("id-2");
        assertThat(result.get(1).ragStatus()).isNull();

        var inOrder = inOrder(service);
        then(service).should(inOrder).uploadMultiple(any(List.class), eq(uploadedBy));
        inOrder.verifyNoMoreInteractions();

        Files.deleteIfExists(tempFile1);
        Files.deleteIfExists(tempFile2);
    }

    @Test
    void shouldThrowRuntimeExceptionWhenFileCannotBeRead() {
        var nonExistentPath = Path.of("/tmp/nonexistent-batch-file.pdf");
        given(fileUpload1.uploadedFile()).willReturn(nonExistentPath);

        assertThatThrownBy(() -> sut.uploadMultiple(List.of(fileUpload1), "gandalf"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to read uploaded files");
    }
}
