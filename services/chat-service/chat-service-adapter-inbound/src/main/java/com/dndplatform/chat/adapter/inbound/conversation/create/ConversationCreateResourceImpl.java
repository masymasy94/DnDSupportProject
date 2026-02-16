package com.dndplatform.chat.adapter.inbound.conversation.create;

import com.dndplatform.chat.view.model.ConversationCreateResource;
import com.dndplatform.chat.view.model.vm.ConversationViewModel;
import com.dndplatform.chat.view.model.vm.CreateConversationViewModel;
import com.dndplatform.common.annotations.Delegate;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

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
public class ConversationCreateResourceImpl implements ConversationCreateResource {

    private final ConversationCreateResource delegate;

    @Inject
    public ConversationCreateResourceImpl(@Delegate ConversationCreateResource delegate) {
        this.delegate = delegate;
    }

    @POST
    @Override
    @Operation(summary = "Create conversation", description = "Create a new direct or group conversation")
    @APIResponse(
            responseCode = "200",
            description = "Conversation created successfully",
            content = @Content(schema = @Schema(implementation = ConversationViewModel.class))
    )
    @APIResponse(responseCode = "400", description = "Invalid request")
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public ConversationViewModel create(
            @Parameter(description = "User ID of the creator", required = true)
            @QueryParam("userId") Long userId,
            @RequestBody(
                    description = "Conversation creation request",
                    content = @Content(schema = @Schema(implementation = CreateConversationViewModel.class))
            )
            @Valid CreateConversationViewModel request) {
        return delegate.create(userId, request);
    }
}
