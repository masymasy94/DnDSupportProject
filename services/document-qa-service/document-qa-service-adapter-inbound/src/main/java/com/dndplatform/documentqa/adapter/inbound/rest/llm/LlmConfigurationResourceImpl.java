package com.dndplatform.documentqa.adapter.inbound.rest.llm;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.documentqa.view.model.LlmConfigurationResource;
import com.dndplatform.documentqa.view.model.vm.CreateLlmConfigurationRequest;
import com.dndplatform.documentqa.view.model.vm.LlmConfigurationViewModel;
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
@Path("/api/document-qa/llm")
@Tag(name = "Document Q&A - LLM Configuration")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LlmConfigurationResourceImpl implements LlmConfigurationResource {

    private final LlmConfigurationDelegate delegate;

    @Inject
    public LlmConfigurationResourceImpl(@Delegate LlmConfigurationResource delegate) {
        this.delegate = (LlmConfigurationDelegate) delegate;
    }

    @GET
    @Path("/configurations")
    @Operation(summary = "List system LLM configurations")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearer")
    @Override
    public List<LlmConfigurationViewModel> listSystemConfigurations() {
        return delegate.listSystemConfigurations();
    }

    @POST
    @Path("/configurations")
    @Operation(summary = "Create system LLM configuration")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearer")
    @Override
    public LlmConfigurationViewModel createSystemConfiguration(CreateLlmConfigurationRequest request) {
        return delegate.createSystemConfiguration(request);
    }

    @PUT
    @Path("/configurations/{id}/activate")
    @Operation(summary = "Activate system LLM configuration")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearer")
    @Override
    public void activateSystemConfiguration(@PathParam("id") Long id) {
        delegate.activateSystemConfiguration(id);
    }

    @DELETE
    @Path("/configurations/{id}")
    @Operation(summary = "Delete system LLM configuration")
    @RolesAllowed("ADMIN")
    @SecurityRequirement(name = "bearer")
    @Override
    public void deleteSystemConfiguration(@PathParam("id") Long id) {
        delegate.deleteSystemConfiguration(id);
    }

    @GET
    @Path("/user-configurations")
    @Operation(summary = "List user LLM configurations")
    @RolesAllowed("PLAYER")
    @SecurityRequirement(name = "bearer")
    @Override
    public List<LlmConfigurationViewModel> listUserConfigurations(@QueryParam("userId") Long userId) {
        return delegate.listUserConfigurations(userId);
    }

    @POST
    @Path("/user-configurations")
    @Operation(summary = "Create user LLM configuration")
    @RolesAllowed("PLAYER")
    @SecurityRequirement(name = "bearer")
    @Override
    public LlmConfigurationViewModel createUserConfiguration(CreateLlmConfigurationRequest request) {
        return delegate.createUserConfiguration(request);
    }

    @PUT
    @Path("/user-configurations/{id}/activate")
    @Operation(summary = "Activate user LLM configuration")
    @RolesAllowed("PLAYER")
    @SecurityRequirement(name = "bearer")
    @Override
    public void activateUserConfiguration(@PathParam("id") Long id, @QueryParam("userId") Long userId) {
        delegate.activateUserConfiguration(id, userId);
    }

    @DELETE
    @Path("/user-configurations/{id}")
    @Operation(summary = "Delete user LLM configuration")
    @RolesAllowed("PLAYER")
    @SecurityRequirement(name = "bearer")
    @Override
    public void deleteUserConfiguration(@PathParam("id") Long id, @QueryParam("userId") Long userId) {
        delegate.deleteUserConfiguration(id, userId);
    }
}
