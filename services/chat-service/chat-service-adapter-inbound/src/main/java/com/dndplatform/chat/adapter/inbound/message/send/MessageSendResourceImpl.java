package com.dndplatform.chat.adapter.inbound.message.send;

import com.dndplatform.chat.view.model.MessageSendResource;
import com.dndplatform.chat.view.model.vm.MessageViewModel;
import com.dndplatform.chat.view.model.vm.SendMessageViewModel;
import com.dndplatform.common.annotations.Delegate;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
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
public class MessageSendResourceImpl implements MessageSendResource {

    private final MessageSendResource delegate;

    @Inject
    public MessageSendResourceImpl(@Delegate MessageSendResource delegate) {
        this.delegate = delegate;
    }

    @POST
    @Path("/{conversationId}/messages")
    @Override
    @Operation(summary = "Send message", description = "Send a new message to a conversation")
    @APIResponse(
            responseCode = "200",
            description = "Message sent successfully",
            content = @Content(schema = @Schema(implementation = MessageViewModel.class))
    )
    @APIResponse(responseCode = "400", description = "Invalid request")
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @APIResponse(responseCode = "404", description = "Conversation not found")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public MessageViewModel send(
            @Parameter(description = "Conversation ID", required = true)
            @PathParam("conversationId") Long conversationId,
            @Parameter(description = "User ID of the sender", required = true)
            @QueryParam("userId") Long userId,
            @RequestBody(
                    description = "Message to send",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SendMessageViewModel.class))
            )
            SendMessageViewModel request) {
        return delegate.send(conversationId, userId, request);
    }
}
