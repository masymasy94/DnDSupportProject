package com.dndplatform.documentqa.adapter.inbound.rest.ingestion;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.documentqa.view.model.IngestionResource;
import com.dndplatform.documentqa.view.model.vm.IngestionStatusViewModel;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@RequestScoped
@RunOnVirtualThread
@Path("/api/document-qa/ingestion")
@Tag(name = "Document Q&A - Ingestion")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class IngestionResourceImpl implements IngestionResource {

    private final IngestionDelegate delegate;

    @Inject
    public IngestionResourceImpl(@Delegate IngestionResource delegate) {
        this.delegate = (IngestionDelegate) delegate;
    }

    @GET
    @Path("/{documentId}/status")
    @Operation(summary = "Get document ingestion status")
    @RolesAllowed("PLAYER")
    @SecurityRequirement(name = "bearer")
    @Override
    public IngestionStatusViewModel getStatus(@PathParam("documentId") String documentId) {
        return delegate.getStatus(documentId);
    }

    @POST
    @Path("/{documentId}")
    @Operation(summary = "Trigger manual document ingestion")
    @RolesAllowed("PLAYER")
    @SecurityRequirement(name = "bearer")
    @Override
    public void triggerIngestion(@PathParam("documentId") String documentId, @QueryParam("userId") Long userId) {
        delegate.triggerIngestion(documentId, userId);
    }
}
