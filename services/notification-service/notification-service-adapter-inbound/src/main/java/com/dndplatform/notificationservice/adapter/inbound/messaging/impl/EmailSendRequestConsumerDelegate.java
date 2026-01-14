package com.dndplatform.notificationservice.adapter.inbound.messaging.impl;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.notificationservice.adapter.inbound.messaging.EmailSendRequestConsumer;
import com.dndplatform.notificationservice.adapter.inbound.send.mapper.SendEmailRequestMapper;
import com.dndplatform.notificationservice.domain.SendEmailService;
import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class EmailSendRequestConsumerDelegate implements EmailSendRequestConsumer {

    private final SendEmailRequestMapper mapper;
    private final SendEmailService sendEmailService;

    @Inject
    public EmailSendRequestConsumerDelegate(SendEmailRequestMapper mapper, SendEmailService sendEmailService) {
        this.mapper = mapper;
        this.sendEmailService = sendEmailService;
    }

    @Override
    public void consumeEmailRequest(EmailSendRequestViewModel emailSendRequestViewModel) {
        var model = mapper.apply(emailSendRequestViewModel);
        sendEmailService.send(model);
    }
}
