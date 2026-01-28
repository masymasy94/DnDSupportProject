package com.dndplatform.chat.adapter.inbound.conversation.findbyid;

import com.dndplatform.chat.view.model.ConversationFindByIdResource;
import com.dndplatform.chat.view.model.vm.ConversationViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
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
public class ConversationFindByIdResourceImpl implements ConversationFindByIdResource {

    private final ConversationFindByIdResource delegate;

    @Inject
    public ConversationFindByIdResourceImpl(@Delegate ConversationFindByIdResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Path("/{id}")
    @Override
    @Operation(summary = "Get conversation", description = "Get a specific conversation by ID")
    @APIResponse(
            responseCode = "200",
            description = "Conversation retrieved successfully",
            content = @Content(schema = @Schema(implementation = ConversationViewModel.class))
    )
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @APIResponse(responseCode = "404", description = "Conversation not found")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public ConversationViewModel findById(
            @Parameter(description = "Conversation ID", required = true)
            @PathParam("id") Long id,
            @Parameter(description = "User ID of the requester", required = true)
            @QueryParam("userId") Long userId) {
        return delegate.findById(id, userId);
    }
}
