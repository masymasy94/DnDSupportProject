package com.dndplatform.chat.adapter.inbound.message.findbyconversation;

import com.dndplatform.chat.view.model.MessageFindByConversationResource;
import com.dndplatform.chat.view.model.vm.PagedMessageViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
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
@Tag(name = "Messages", description = "Message management")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class MessageFindByConversationResourceImpl implements MessageFindByConversationResource {

    private final MessageFindByConversationResource delegate;

    @Inject
    public MessageFindByConversationResourceImpl(@Delegate MessageFindByConversationResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Path("/{conversationId}/messages")
    @Override
    @Operation(summary = "Get messages", description = "Get paginated messages from a conversation")
    @APIResponse(
            responseCode = "200",
            description = "Messages retrieved successfully",
            content = @Content(schema = @Schema(implementation = PagedMessageViewModel.class))
    )
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @APIResponse(responseCode = "404", description = "Conversation not found")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public PagedMessageViewModel findByConversation(
            @Parameter(description = "Conversation ID", required = true)
            @PathParam("conversationId") Long conversationId,
            @Parameter(description = "User ID of the requester", required = true)
            @QueryParam("userId") Long userId,
            @Parameter(description = "Page number (0-indexed)")
            @QueryParam("page") @DefaultValue("0") Integer page,
            @Parameter(description = "Page size (default 50)")
            @QueryParam("pageSize") @DefaultValue("50") Integer pageSize) {
        return delegate.findByConversation(conversationId, userId, page, pageSize);
    }
}
