package com.dndplatform.documentqa.adapter.outbound.messaging;

import com.dndplatform.documentqa.domain.model.IngestionStatus;
import com.dndplatform.documentqa.view.model.vm.DocumentIngestedEventVm;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
@DisplayName("DocumentIngestedEventPublisherRabbitMq")
class DocumentIngestedEventPublisherRabbitMqTest {

    @Mock
    private Emitter<DocumentIngestedEventVm> emitter;

    private DocumentIngestedEventPublisherRabbitMq sut;

    @BeforeEach
    void setUp() throws Exception {
        sut = new DocumentIngestedEventPublisherRabbitMq();
        Field emitterField = DocumentIngestedEventPublisherRabbitMq.class.getDeclaredField("emitter");
        emitterField.setAccessible(true);
        emitterField.set(sut, emitter);
    }

    @Test
    @DisplayName("should publish COMPLETED event with chunk count")
    void shouldPublishCompletedEvent() {
        String documentId = "doc-123";
        Integer chunkCount = 42;

        sut.publish(documentId, IngestionStatus.COMPLETED, null, chunkCount);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<DocumentIngestedEventVm> captor = ArgumentCaptor.forClass(DocumentIngestedEventVm.class);
        then(emitter).should().send(captor.capture());

        DocumentIngestedEventVm event = captor.getValue();
        assertThat(event.documentId()).isEqualTo(documentId);
        assertThat(event.status()).isEqualTo("COMPLETED");
        assertThat(event.errorMessage()).isNull();
        assertThat(event.chunkCount()).isEqualTo(chunkCount);
    }

    @Test
    @DisplayName("should publish FAILED event with error message")
    void shouldPublishFailedEvent() {
        String documentId = "doc-456";
        String errorMessage = "PDF parsing failed";

        sut.publish(documentId, IngestionStatus.FAILED, errorMessage, 0);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<DocumentIngestedEventVm> captor = ArgumentCaptor.forClass(DocumentIngestedEventVm.class);
        then(emitter).should().send(captor.capture());

        DocumentIngestedEventVm event = captor.getValue();
        assertThat(event.documentId()).isEqualTo(documentId);
        assertThat(event.status()).isEqualTo("FAILED");
        assertThat(event.errorMessage()).isEqualTo(errorMessage);
    }

    @Test
    @DisplayName("should publish PENDING event using status name")
    void shouldPublishPendingEventUsingStatusName() {
        String documentId = "doc-789";

        sut.publish(documentId, IngestionStatus.PENDING, null, null);

        @SuppressWarnings("unchecked")
        ArgumentCaptor<DocumentIngestedEventVm> captor = ArgumentCaptor.forClass(DocumentIngestedEventVm.class);
        then(emitter).should().send(captor.capture());

        DocumentIngestedEventVm event = captor.getValue();
        assertThat(event.status()).isEqualTo("PENDING");
    }
}
