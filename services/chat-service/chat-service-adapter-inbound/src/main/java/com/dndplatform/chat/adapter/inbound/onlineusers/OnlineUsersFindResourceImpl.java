package com.dndplatform.chat.adapter.inbound.onlineusers;

import com.dndplatform.chat.view.model.OnlineUsersFindResource;
import com.dndplatform.common.annotations.Delegate;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import java.util.Set;

import static org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType.HTTP;

@RequestScoped
@RunOnVirtualThread
@Path("/api/chat/online-users")
@Tag(name = "Online Users", description = "Online user presence")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@SecurityScheme(
        description = "JWT Bearer token authorization",
        securitySchemeName = "bearer",
        type = HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OnlineUsersFindResourceImpl implements OnlineUsersFindResource {

    private final OnlineUsersFindResource delegate;

    @Inject
    public OnlineUsersFindResourceImpl(@Delegate OnlineUsersFindResource delegate) {
        this.delegate = delegate;
    }

    @GET
    @Override
    @Operation(summary = "Get online users", description = "Get the set of user IDs currently connected via WebSocket")
    @APIResponse(
            responseCode = "200",
            description = "Online user IDs retrieved successfully",
            content = @Content(schema = @Schema(implementation = Long[].class))
    )
    @APIResponse(responseCode = "401", description = "Unauthorized")
    @SecurityRequirement(name = "bearer")
    @RolesAllowed("PLAYER")
    public Set<Long> findOnlineUsers() {
        return delegate.findOnlineUsers();
    }
}
