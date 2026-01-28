package com.dndplatform.chat.adapter.inbound.conversation.findbyuser;

import com.dndplatform.chat.view.model.ConversationFindByUserResource;
import com.dndplatform.chat.view.model.vm.ConversationViewModel;
import com.dndplatform.common.annotations.Delegate;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
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

import java.util.List;

import static org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType.HTTP;

@RequestScoped
@RunOnVirtualThread
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
public class ConversationFindByUserResourceImpl implements ConversationFindByUserResource {

    private final ConversationFindByUserResource delegate;

    @Inject
    public ConversationFindByUserResourceImpl(@Delegate ConversationFindByUserResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Override
    @Operation(summary = "List conversations", description = "Get all conversations for the current user")
    @APIResponse(
            responseCode = "200",
            description = "Conversations retrieved successfully",
            content = @Content(schema = @Schema(implementation = ConversationViewModel[].class))
    )
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public List<ConversationViewModel> findByUser(
            @Parameter(description = "User ID", required = true)
            @QueryParam("userId") long userId) {
        return delegate.findByUser(userId);
    }
}
