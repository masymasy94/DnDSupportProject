package com.dndplatform.notificationservice.adapter.inbound.template;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.notificationservice.adapter.inbound.template.mapper.CreateEmailTemplateRequestMapper;
import com.dndplatform.notificationservice.adapter.inbound.template.mapper.CreateEmailTemplateResponseMapper;
import com.dndplatform.notificationservice.domain.CreateEmailTemplateService;
import com.dndplatform.notificationservice.view.model.CreateEmailTemplateResource;
import com.dndplatform.notificationservice.view.model.vm.CreateEmailTemplateRequestViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class CreateEmailTemplateDelegate implements CreateEmailTemplateResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CreateEmailTemplateRequestMapper requestMapper;
    private final CreateEmailTemplateResponseMapper responseMapper;
    private final CreateEmailTemplateService createEmailTemplateService;

    @Inject
    public CreateEmailTemplateDelegate(CreateEmailTemplateRequestMapper requestMapper,
                                       CreateEmailTemplateResponseMapper responseMapper,
                                       CreateEmailTemplateService createEmailTemplateService) {
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
        this.createEmailTemplateService = createEmailTemplateService;
    }

    @Override
    public Response create(CreateEmailTemplateRequestViewModel request) {
        log.info(() -> "Creating email template: " + request.name());

        var emailTemplate = requestMapper.apply(request);
        var result = createEmailTemplateService.create(emailTemplate);
        var response = responseMapper.apply(result);

        return Response.status(Response.Status.CREATED).entity(response).build();
    }
}
