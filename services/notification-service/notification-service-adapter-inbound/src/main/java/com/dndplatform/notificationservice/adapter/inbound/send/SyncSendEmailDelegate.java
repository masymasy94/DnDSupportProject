package com.dndplatform.notificationservice.adapter.inbound.send;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.notificationservice.adapter.inbound.send.mapper.SendEmailRequestMapper;
import com.dndplatform.notificationservice.adapter.inbound.send.mapper.SendEmailResponseMapper;
import com.dndplatform.notificationservice.domain.SendEmailService;
import com.dndplatform.notificationservice.view.model.SendEmailResource;
import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class SyncSendEmailDelegate implements SendEmailResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final SendEmailRequestMapper requestMapper;
    private final SendEmailResponseMapper responseMapper;
    private final SendEmailService sendEmailService;

    @Inject
    public SyncSendEmailDelegate(SendEmailRequestMapper requestMapper,
                                 SendEmailResponseMapper responseMapper,
                                 SendEmailService sendEmailService) {
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
        this.sendEmailService = sendEmailService;
    }

    @Override
    public Response syncSend(EmailSendRequestViewModel request) {
        log.info(() -> "Sending email to: " + request.to());

        var email = requestMapper.apply(request);
        var result = sendEmailService.send(email);

        return Response
                .status(Response.Status.CREATED)
                .entity(responseMapper.apply(result))
                .build();
    }
}
