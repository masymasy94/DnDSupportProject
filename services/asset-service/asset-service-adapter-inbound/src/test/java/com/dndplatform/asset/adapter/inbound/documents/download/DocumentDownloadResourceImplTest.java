package com.dndplatform.asset.adapter.inbound.documents.download;

import com.dndplatform.asset.view.model.DocumentDownloadResource;
import com.dndplatform.common.test.RandomExtension;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class DocumentDownloadResourceImplTest {

    @Mock
    private DocumentDownloadResource delegate;

    @Mock
    private Response expectedResponse;

    private DocumentDownloadResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentDownloadResourceImpl(delegate);
    }

    @Test
    void shouldDelegateDownload() {
        String documentId = "doc-123";
        given(delegate.download(documentId)).willReturn(expectedResponse);

        Response result = sut.download(documentId);

        assertThat(result).isEqualTo(expectedResponse);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).download(documentId);
        inOrder.verifyNoMoreInteractions();
    }
}
