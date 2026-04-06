package com.dndplatform.asset.adapter.outbound.messaging;

import com.dndplatform.asset.domain.model.Document;
import com.dndplatform.asset.view.model.vm.DocumentUploadedEvent;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;

@SuppressWarnings("unchecked")
@ExtendWith({MockitoExtension.class, RandomExtension.class})
class DocumentUploadedEventPublisherRabbitMqTest {

    @Mock
    private Emitter<DocumentUploadedEvent> emitter;

    private DocumentUploadedEventPublisherRabbitMq sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentUploadedEventPublisherRabbitMq(emitter);
    }

    @Test
    void shouldPublishEventWithDocumentFields(@Random Document document) {
        sut.publish(document);

        var captor = ArgumentCaptor.forClass(DocumentUploadedEvent.class);
        then(emitter).should().send(captor.capture());

        var event = captor.getValue();
        assertThat(event.documentId()).isEqualTo(document.id());
        assertThat(event.fileName()).isEqualTo(document.fileName());
        assertThat(event.contentType()).isEqualTo(document.contentType());
        assertThat(event.uploadedBy()).isEqualTo(document.uploadedBy());
    }
}
