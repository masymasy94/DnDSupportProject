package com.dndplatform.documentqa.adapter.inbound.rest.ingestion;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.documentqa.view.model.vm.IngestionStatusViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.inOrder;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class IngestionResourceImplTest {

    @Mock
    private IngestionDelegate delegate;

    private IngestionResourceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new IngestionResourceImpl(delegate);
    }

    @Test
    void shouldDelegateGetStatus(@Random IngestionStatusViewModel expected) {
        String documentId = "doc-abc-123";
        given(delegate.getStatus(documentId)).willReturn(expected);

        IngestionStatusViewModel result = sut.getStatus(documentId);

        assertThat(result).isEqualTo(expected);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).getStatus(documentId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    void shouldDelegateTriggerIngestion() {
        String documentId = "doc-abc-123";
        Long userId = 1L;
        willDoNothing().given(delegate).triggerIngestion(documentId, userId);

        sut.triggerIngestion(documentId, userId);

        var inOrder = inOrder(delegate);
        then(delegate).should(inOrder).triggerIngestion(documentId, userId);
        inOrder.verifyNoMoreInteractions();
    }
}
