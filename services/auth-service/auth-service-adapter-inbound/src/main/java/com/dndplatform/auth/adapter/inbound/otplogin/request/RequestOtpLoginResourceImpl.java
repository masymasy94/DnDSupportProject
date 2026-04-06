package com.dndplatform.auth.adapter.inbound.otplogin.request;

import com.dndplatform.auth.view.model.RequestOtpLoginResource;
import com.dndplatform.auth.view.model.vm.RequestOtpLoginViewModel;
import com.dndplatform.common.annotations.Delegate;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@RequestScoped
@RunOnVirtualThread
@Path("/auth/otp-login-requests")
@Tag(name = "OTP Login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
public class RequestOtpLoginResourceImpl implements RequestOtpLoginResource {

    private final RequestOtpLoginResource delegate;

    @Inject
    public RequestOtpLoginResourceImpl(@Delegate RequestOtpLoginResource delegate) {
        this.delegate = delegate;
    }

    @POST
    @Override
    @Operation(summary = "Request an OTP login code via email")
    @APIResponse(responseCode = "202", description = "Request accepted")
    public Response requestOtpLogin(RequestOtpLoginViewModel vm) {
        return delegate.requestOtpLogin(vm);
    }
}
