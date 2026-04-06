package com.dndplatform.asset.adapter.inbound.documents.delete;

import com.dndplatform.asset.view.model.DocumentDeleteResource;
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
class DocumentDeleteResourceImplTest {

    @Mock
    private DocumentDeleteResource delegate;

    @Mock
    private Response expectedResponse;

    private DocumentDeleteResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentDeleteResourceImpl(delegate);
    }

    @Test
    void shouldDelegateDelete() {
        String documentId = "doc-456";
        given(delegate.delete(documentId)).willReturn(expectedResponse);

        Response result = sut.delete(documentId);

        assertThat(result).isEqualTo(expectedResponse);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).delete(documentId);
        inOrder.verifyNoMoreInteractions();
    }
}
