package com.dndplatform.asset.adapter.inbound.documents.download;

import com.dndplatform.asset.domain.DocumentDownloadService;
import com.dndplatform.asset.domain.model.DocumentContent;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class DocumentDownloadDelegateTest {

    @Mock
    private DocumentDownloadService service;

    private DocumentDownloadDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentDownloadDelegate(service);
    }

    @Test
    void shouldReturnDocumentContentWithCorrectHeaders(@Random String documentId) {
        var content = new DocumentContent(
                "character-sheet.pdf",
                "application/pdf",
                1024L,
                new ByteArrayInputStream(new byte[]{1, 2, 3})
        );
        given(service.download(documentId)).willReturn(content);

        Response result = sut.download(documentId);

        assertThat(result.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(result.getHeaderString("Content-Type")).isEqualTo("application/pdf");
        assertThat(result.getHeaderString("Content-Disposition"))
                .isEqualTo("attachment; filename=\"character-sheet.pdf\"");

        var inOrder = inOrder(service);
        then(service).should(inOrder).download(documentId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenDocumentNotFound(@Random String documentId) {
        willThrow(new RuntimeException("Document not found")).given(service).download(documentId);

        assertThatThrownBy(() -> sut.download(documentId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Document not found");
    }
}
