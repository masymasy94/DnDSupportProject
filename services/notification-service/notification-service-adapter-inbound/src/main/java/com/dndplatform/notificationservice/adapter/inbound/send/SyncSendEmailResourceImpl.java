package com.dndplatform.notificationservice.adapter.inbound.send;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.notificationservice.view.model.SendEmailResource;
import com.dndplatform.notificationservice.view.model.vm.SendEmailRequestViewModel;
import com.dndplatform.notificationservice.view.model.vm.SendEmailResponseViewModel;
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
@Path("/emails")
@Tag(name = "Email", description = "Email sending operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SyncSendEmailResourceImpl implements SendEmailResource {

    private final SendEmailResource delegate;

    @Inject
    public SyncSendEmailResourceImpl(@Delegate SendEmailResource delegate) {
        this.delegate = delegate;
    }

    @POST
    @Override
    @Operation(summary = "Send an email", description = "Send an email synchronously")
    @APIResponse(responseCode = "201", description = "Email sent successfully")
    @APIResponse(responseCode = "400", description = "Invalid request")
    @APIResponse(responseCode = "500", description = "Email sending failed")
    public Response syncSend(@Valid SendEmailRequestViewModel request) {
        return delegate.syncSend(request);
    }
}
