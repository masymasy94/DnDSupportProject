package com.dndplatform.notificationservice.adapter.inbound.template;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.notificationservice.view.model.CreateEmailTemplateResource;
import com.dndplatform.notificationservice.view.model.vm.CreateEmailTemplateRequestViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@RequestScoped
@Path("/email-templates")
@Tag(name = "Email Templates", description = "Email template management operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CreateEmailTemplateResourceImpl implements CreateEmailTemplateResource {

    private final CreateEmailTemplateResource delegate;

    @Inject
    public CreateEmailTemplateResourceImpl(@Delegate CreateEmailTemplateResource delegate) {
        this.delegate = delegate;
    }

    @POST
    @Override
    @Operation(summary = "Create an email template", description = "Create a new email template with Qute syntax validation")
    @APIResponse(responseCode = "201", description = "Template created successfully")
    @APIResponse(responseCode = "400", description = "Invalid request or template syntax")
    @APIResponse(responseCode = "409", description = "Template with this name already exists")
    public Response create(@Valid CreateEmailTemplateRequestViewModel request) {
        return delegate.create(request);
    }
}
