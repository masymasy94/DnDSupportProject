package com.dndplatform.chat.adapter.inbound.conversation.updatereadbyid;

import com.dndplatform.chat.view.model.ConversationUpdateReadByIdResource;
import com.dndplatform.common.annotations.Delegate;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import static org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType.HTTP;

@RequestScoped
@Path("/api/chat/conversations")
@Tag(name = "Conversations", description = "Conversation management")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class ConversationUpdateReadByIdResourceImpl implements ConversationUpdateReadByIdResource {

    private final ConversationUpdateReadByIdResource delegate;

    @Inject
    public ConversationUpdateReadByIdResourceImpl(@Delegate ConversationUpdateReadByIdResource delegate) {
        this.delegate = delegate;
    }

    @PUT
    @Path("/{id}/read")
    @Override
    @Operation(summary = "Mark as read", description = "Mark a conversation as read for the current user")
    @APIResponse(responseCode = "204", description = "Conversation marked as read")
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @APIResponse(responseCode = "404", description = "Conversation not found")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public void updateReadById(
            @Parameter(description = "Conversation ID", required = true)
            @PathParam("id") Long conversationId,
            @Parameter(description = "User ID of the requester", required = true)
            @QueryParam("userId") Long userId) {
        delegate.updateReadById(conversationId, userId);
    }
}
