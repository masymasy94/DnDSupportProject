package com.dndplatform.notificationservice.adapter.inbound.messaging.impl;

import com.dndplatform.notificationservice.adapter.inbound.messaging.EmailSendRequestConsumer;
import com.dndplatform.notificationservice.adapter.inbound.messaging.MessageAcknowledger;
import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModel;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willReturn;

@SuppressWarnings("unchecked")
@ExtendWith(MockitoExtension.class)
class EmailSendRequestConsumerImplTest {

    @Mock
    private EmailSendRequestConsumer delegate;

    @Mock
    private MessageAcknowledger acknowledger;

    @Mock
    private Message<EmailSendRequestViewModel> message;

    private EmailSendRequestConsumerImpl sut;

    @BeforeEach
    void setUp() {
        sut = new EmailSendRequestConsumerImpl(delegate, acknowledger);
    }

    @Test
    void shouldAckMessageWhenDelegateSucceeds() throws Exception {
        var viewModel = new EmailSendRequestViewModel("test@test.com", null, null, 1L, null, null, null);
        given(message.getPayload()).willReturn(viewModel);
        given(delegate.consumeEmailRequest(message)).willReturn(CompletableFuture.completedFuture(null));
        given(acknowledger.ack(message)).willReturn(CompletableFuture.completedFuture(null));

        sut.consumeEmailRequest(message).toCompletableFuture().get();

        then(delegate).should().consumeEmailRequest(message);
        then(acknowledger).should().ack(message);
        then(acknowledger).shouldHaveNoMoreInteractions();
    }

    @Test
    void shouldNackMessageWhenDelegateFails() throws Exception {
        var viewModel = new EmailSendRequestViewModel("test@test.com", null, null, 1L, null, null, null);
        var cause = new RuntimeException("send failed");
        given(message.getPayload()).willReturn(viewModel);
        given(delegate.consumeEmailRequest(message)).willReturn(CompletableFuture.failedFuture(cause));
        given(acknowledger.nack(eq(message), any(Throwable.class))).willReturn(CompletableFuture.completedFuture(null));

        sut.consumeEmailRequest(message).toCompletableFuture().get();

        then(delegate).should().consumeEmailRequest(message);
        then(acknowledger).should().nack(eq(message), any(Throwable.class));
        then(acknowledger).shouldHaveNoMoreInteractions();
    }
}
