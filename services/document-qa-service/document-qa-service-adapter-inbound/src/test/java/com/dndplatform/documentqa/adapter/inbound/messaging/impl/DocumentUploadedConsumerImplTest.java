package com.dndplatform.documentqa.adapter.inbound.messaging.impl;

import com.dndplatform.common.test.RandomExtension;
import com.dndplatform.documentqa.adapter.inbound.messaging.DocumentUploadedConsumer;
import com.dndplatform.documentqa.view.model.vm.DocumentUploadedEventVm;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class DocumentUploadedConsumerImplTest {

    @Mock
    private DocumentUploadedConsumer delegate;

    private DocumentUploadedConsumerImpl sut;

    @BeforeEach
    void setUp() {
        sut = new DocumentUploadedConsumerImpl(delegate);
    }

    @Test
    void consume_shouldAckMessageOnSuccess() throws Exception {
        DocumentUploadedEventVm payload = new DocumentUploadedEventVm("doc-1", "rulebook.pdf", "application/pdf", "42");
        AtomicBoolean acked = new AtomicBoolean(false);
        AtomicBoolean nacked = new AtomicBoolean(false);

        Message<DocumentUploadedEventVm> message = Message.of(payload,
                () -> {
                    acked.set(true);
                    return CompletableFuture.completedFuture(null);
                },
                (t) -> {
                    nacked.set(true);
                    return CompletableFuture.completedFuture(null);
                });

        given(delegate.consume(any())).willReturn(CompletableFuture.completedFuture(null));

        CompletionStage<Void> result = sut.consume(message);
        result.toCompletableFuture().get();

        assertThat(acked.get()).isTrue();
        assertThat(nacked.get()).isFalse();
    }

    @Test
    void consume_shouldNackMessageOnDelegateFailure() throws Exception {
        DocumentUploadedEventVm payload = new DocumentUploadedEventVm("doc-2", "monster.pdf", "application/pdf", "99");
        AtomicBoolean acked = new AtomicBoolean(false);
        AtomicReference<Throwable> nackReason = new AtomicReference<>();

        Message<DocumentUploadedEventVm> message = Message.of(payload,
                () -> {
                    acked.set(true);
                    return CompletableFuture.completedFuture(null);
                },
                (t) -> {
                    nackReason.set(t);
                    return CompletableFuture.completedFuture(null);
                });

        RuntimeException error = new RuntimeException("Ingestion failed");
        CompletableFuture<Void> failed = new CompletableFuture<>();
        failed.completeExceptionally(error);
        given(delegate.consume(any())).willReturn(failed);

        CompletionStage<Void> result = sut.consume(message);
        result.toCompletableFuture().get();

        assertThat(acked.get()).isFalse();
        assertThat(nackReason.get()).isEqualTo(error);
    }

    @Test
    void consume_shouldForwardMessageToDelegate() throws Exception {
        DocumentUploadedEventVm payload = new DocumentUploadedEventVm("doc-3", "spells.txt", "text/plain", "7");
        Message<DocumentUploadedEventVm> message = Message.of(payload,
                () -> CompletableFuture.completedFuture(null),
                (t) -> CompletableFuture.completedFuture(null));

        given(delegate.consume(any())).willReturn(CompletableFuture.completedFuture(null));

        sut.consume(message).toCompletableFuture().get();

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Message<DocumentUploadedEventVm>> captor =
                ArgumentCaptor.forClass((Class<Message<DocumentUploadedEventVm>>) (Class<?>) Message.class);
        then(delegate).should().consume(captor.capture());
        assertThat(captor.getValue().getPayload()).isEqualTo(payload);
    }

    @Test
    void consume_shouldReturnNonNullCompletionStage() throws Exception {
        DocumentUploadedEventVm payload = new DocumentUploadedEventVm("doc-4", "items.pdf", "application/pdf", "1");
        Message<DocumentUploadedEventVm> message = Message.of(payload,
                () -> CompletableFuture.completedFuture(null),
                (t) -> CompletableFuture.completedFuture(null));

        given(delegate.consume(any())).willReturn(CompletableFuture.completedFuture(null));

        CompletionStage<Void> result = sut.consume(message);

        assertThat(result).isNotNull();
    }
}
