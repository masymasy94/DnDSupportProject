package com.dndplatform.documentqa.adapter.inbound.rest.conversation;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.documentqa.view.model.ConversationResource;
import com.dndplatform.documentqa.view.model.vm.ConversationMessageViewModel;
import com.dndplatform.documentqa.view.model.vm.ConversationViewModel;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.List;

@RequestScoped
@RunOnVirtualThread
@Path("/api/document-qa/conversations")
@Tag(name = "Document Q&A - Conversations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ConversationResourceImpl implements ConversationResource {

    private final ConversationDelegate delegate;

    @Inject
    public ConversationResourceImpl(@Delegate ConversationResource delegate) {
        this.delegate = (ConversationDelegate) delegate;
    }

    @GET
    @Operation(summary = "List user conversations")
    @RolesAllowed("PLAYER")
    @SecurityRequirement(name = "bearer")
    @Override
    public List<ConversationViewModel> listConversations(@QueryParam("userId") Long userId) {
        return delegate.listConversations(userId);
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get conversation details")
    @RolesAllowed("PLAYER")
    @SecurityRequirement(name = "bearer")
    @Override
    public ConversationViewModel getConversation(@PathParam("id") Long id, @QueryParam("userId") Long userId) {
        return delegate.getConversation(id, userId);
    }

    @GET
    @Path("/{id}/messages")
    @Operation(summary = "Get conversation messages")
    @RolesAllowed("PLAYER")
    @SecurityRequirement(name = "bearer")
    @Override
    public List<ConversationMessageViewModel> getMessages(@PathParam("id") Long id, @QueryParam("userId") Long userId) {
        return delegate.getMessages(id, userId);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete conversation")
    @RolesAllowed("PLAYER")
    @SecurityRequirement(name = "bearer")
    @Override
    public void deleteConversation(@PathParam("id") Long id, @QueryParam("userId") Long userId) {
        delegate.deleteConversation(id, userId);
    }
}
