package com.dndplatform.documentqa.adapter.inbound.rest.ingestion;

import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.documentqa.domain.DocumentIngestionService;
import com.dndplatform.documentqa.domain.IngestionStatusService;
import com.dndplatform.documentqa.domain.model.DocumentIngestion;
import com.dndplatform.documentqa.domain.model.IngestionStatus;
import com.dndplatform.documentqa.view.model.vm.IngestionStatusViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.inOrder;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class IngestionDelegateTest {

    @Mock
    private IngestionStatusService statusService;

    @Mock
    private DocumentIngestionService ingestionService;

    private IngestionDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new IngestionDelegate(statusService, ingestionService);
    }

    @Test
    void shouldReturnIngestionStatus() {
        String documentId = "doc-1";
        DocumentIngestion ingestion = new DocumentIngestion(
                documentId, "rulebook.pdf", "application/pdf",
                IngestionStatus.COMPLETED, 42, null, "1",
                LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now()
        );
        given(statusService.getStatus(documentId)).willReturn(ingestion);

        IngestionStatusViewModel result = sut.getStatus(documentId);

        assertThat(result.documentId()).isEqualTo(documentId);
        assertThat(result.fileName()).isEqualTo("rulebook.pdf");
        assertThat(result.status()).isEqualTo("COMPLETED");
        assertThat(result.chunkCount()).isEqualTo(42);

        then(statusService).should().getStatus(documentId);
    }

    @Test
    void shouldTriggerIngestion(@Random Long userId) {
        String documentId = "doc-2";
        DocumentIngestion ingestion = new DocumentIngestion(
                documentId, "monster-manual.pdf", "application/pdf",
                IngestionStatus.PENDING, 0, null, null,
                null, null, LocalDateTime.now()
        );
        given(statusService.getStatus(documentId)).willReturn(ingestion);
        willDoNothing().given(ingestionService).ingest(any());

        sut.triggerIngestion(documentId, userId);

        var inOrder = inOrder(statusService, ingestionService);
        then(statusService).should(inOrder).getStatus(documentId);
        then(ingestionService).should(inOrder).ingest(any());
    }
}
