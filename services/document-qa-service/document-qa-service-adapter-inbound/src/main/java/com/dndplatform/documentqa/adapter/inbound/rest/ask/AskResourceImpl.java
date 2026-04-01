package com.dndplatform.documentqa.adapter.inbound.rest.ask;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.documentqa.view.model.AskResource;
import com.dndplatform.documentqa.view.model.vm.AskRequest;
import com.dndplatform.documentqa.view.model.vm.QuestionAnswerViewModel;
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
@Path("/api/document-qa/ask")
@Tag(name = "Document Q&A")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AskResourceImpl implements AskResource {

    private final AskDelegate delegate;

    @Inject
    public AskResourceImpl(@Delegate AskResource delegate) {
        this.delegate = (AskDelegate) delegate;
    }

    @POST
    @Operation(summary = "Ask a question about uploaded documents")
    @RolesAllowed("PLAYER")
    @SecurityRequirement(name = "bearer")
    @Override
    public QuestionAnswerViewModel ask(AskRequest request) {
        return delegate.ask(request);
    }
}
