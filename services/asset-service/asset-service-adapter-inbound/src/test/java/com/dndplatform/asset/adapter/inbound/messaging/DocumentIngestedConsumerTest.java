package com.dndplatform.asset.adapter.inbound.messaging;

import com.dndplatform.asset.domain.model.RagStatus;
import com.dndplatform.asset.domain.repository.DocumentMetadataUpdateRagStatusRepository;
import com.dndplatform.asset.view.model.vm.DocumentIngestedEvent;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class DocumentIngestedConsumerTest {

    @Mock
    private DocumentMetadataUpdateRagStatusRepository repository;

    @Mock
    private Message<DocumentIngestedEvent> message;

    private DocumentIngestedConsumer sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentIngestedConsumer(repository);
    }

    @Test
    void shouldUpdateRagStatusAndAckMessage() throws Exception {
        var event = new DocumentIngestedEvent("doc-123", "COMPLETED", null, 5);
        given(message.getPayload()).willReturn(event);
        given(message.ack()).willReturn(CompletableFuture.completedFuture(null));
        willDoNothing().given(repository).updateRagStatus("doc-123", RagStatus.COMPLETED, null);

        sut.consume(message).toCompletableFuture().get();

        then(repository).should().updateRagStatus("doc-123", RagStatus.COMPLETED, null);
        then(message).should().ack();
    }

    @Test
    void shouldNackMessageWhenRepositoryThrows() throws Exception {
        var event = new DocumentIngestedEvent("doc-456", "FAILED", "error msg", 0);
        var cause = new RuntimeException("DB error");
        given(message.getPayload()).willReturn(event);
        org.mockito.BDDMockito.willThrow(cause).given(repository)
                .updateRagStatus("doc-456", RagStatus.FAILED, "error msg");
        given(message.nack(cause)).willReturn(CompletableFuture.completedFuture(null));

        sut.consume(message).toCompletableFuture().get();

        then(message).should().nack(cause);
    }
}
