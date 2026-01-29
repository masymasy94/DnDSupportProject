package com.dndplatform.notificationservice.adapter.inbound.template;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.notificationservice.view.model.GetAllEmailTemplatesResource;
import io.smallrye.common.annotation.RunOnVirtualThread;
import com.dndplatform.notificationservice.view.model.vm.GetAllEmailTemplatesResponseViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@RequestScoped
@RunOnVirtualThread
@Path("/email-templates")
@Tag(name = "Email Templates", description = "Email template management operations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class GetAllEmailTemplatesResourceImpl implements GetAllEmailTemplatesResource {

    private final GetAllEmailTemplatesResource delegate;

    @Inject
    public GetAllEmailTemplatesResourceImpl(@Delegate GetAllEmailTemplatesResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Override
    @Operation(summary = "Get all email templates", description = "Retrieves all email templates")
    @APIResponse(responseCode = "200", description = "Templates retrieved successfully")
    public GetAllEmailTemplatesResponseViewModel getAll() {
        return delegate.getAll();
    }
}
