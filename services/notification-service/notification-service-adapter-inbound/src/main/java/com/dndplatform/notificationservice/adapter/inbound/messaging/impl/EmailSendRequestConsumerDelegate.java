package com.dndplatform.notificationservice.adapter.inbound.messaging.impl;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.notificationservice.adapter.inbound.messaging.EmailSendRequestConsumer;
import com.dndplatform.notificationservice.adapter.inbound.send.mapper.SendEmailRequestMapper;
import com.dndplatform.notificationservice.domain.SendEmailService;
import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.Objects.requireNonNull;

@Delegate
@RequestScoped
public class EmailSendRequestConsumerDelegate implements EmailSendRequestConsumer {

    private final SendEmailRequestMapper mapper;
    private final SendEmailService sendEmailService;

    @Inject
    public EmailSendRequestConsumerDelegate(SendEmailRequestMapper mapper,
                                            SendEmailService sendEmailService) {
        this.mapper = mapper;
        this.sendEmailService = sendEmailService;
    }

    @Override
    public CompletionStage<Void> consumeEmailRequest(Message<EmailSendRequestViewModel> message) {
        var payload = message.getPayload();
        requireNonNull(payload, "payload of EmailSendRequestViewModel is null");

        sendEmailService.send(mapper.apply(payload));
        return CompletableFuture.completedFuture(null);
    }
}
