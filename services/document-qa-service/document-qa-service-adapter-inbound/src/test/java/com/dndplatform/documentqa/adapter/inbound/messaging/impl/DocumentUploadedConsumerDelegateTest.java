package com.dndplatform.documentqa.adapter.inbound.messaging.impl;

import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.documentqa.domain.DocumentIngestionService;
import com.dndplatform.documentqa.domain.model.DocumentIngestionEvent;
import com.dndplatform.documentqa.view.model.vm.DocumentUploadedEventVm;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class DocumentUploadedConsumerDelegateTest {

    @Mock
    private DocumentIngestionService ingestionService;

    private DocumentUploadedConsumerDelegate sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentUploadedConsumerDelegate(ingestionService);
    }

    @Test
    void shouldDelegateToService() throws Exception {
        DocumentUploadedEventVm payload = new DocumentUploadedEventVm("doc-1", "rulebook.pdf", "application/pdf", "42");
        Message<DocumentUploadedEventVm> message = Message.of(payload);

        willDoNothing().given(ingestionService).ingest(org.mockito.ArgumentMatchers.any());

        var result = sut.consume(message);

        assertThat(result).isNotNull();

        ArgumentCaptor<DocumentIngestionEvent> captor = ArgumentCaptor.forClass(DocumentIngestionEvent.class);
        then(ingestionService).should().ingest(captor.capture());

        DocumentIngestionEvent event = captor.getValue();
        assertThat(event.documentId()).isEqualTo("doc-1");
        assertThat(event.fileName()).isEqualTo("rulebook.pdf");
        assertThat(event.contentType()).isEqualTo("application/pdf");
        assertThat(event.uploadedBy()).isEqualTo("42");
    }
}
