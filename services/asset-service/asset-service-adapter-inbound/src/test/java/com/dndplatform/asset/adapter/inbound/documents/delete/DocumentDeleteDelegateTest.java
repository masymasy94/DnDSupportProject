package com.dndplatform.asset.adapter.inbound.documents.delete;

import com.dndplatform.asset.domain.DocumentDeleteService;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class DocumentDeleteDelegateTest {

    @Mock
    private DocumentDeleteService service;

    private DocumentDeleteDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentDeleteDelegate(service);
    }

    @Test
    void shouldDelegateToServiceAndReturn204(@Random String documentId) {
        willDoNothing().given(service).delete(documentId);

        Response result = sut.delete(documentId);

        assertThat(result.getStatus()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());

        var inOrder = inOrder(service);
        then(service).should(inOrder).delete(documentId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldThrowWhenDocumentNotFound(@Random String documentId) {
        willThrow(new RuntimeException("Document not found")).given(service).delete(documentId);

        assertThatThrownBy(() -> sut.delete(documentId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Document not found");
    }
}
