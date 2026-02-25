package com.dndplatform.auth.adapter.inbound.otplogin.validate;

import com.dndplatform.auth.view.model.ValidateOtpLoginResource;
import com.dndplatform.auth.view.model.vm.ValidateOtpLoginViewModel;
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
@Path("/auth/otp-login-tokens")
@Tag(name = "OTP Login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
public class ValidateOtpLoginResourceImpl implements ValidateOtpLoginResource {

    private final ValidateOtpLoginResource delegate;

    @Inject
    public ValidateOtpLoginResourceImpl(@Delegate ValidateOtpLoginResource delegate) {
        this.delegate = delegate;
    }

    @POST
    @Override
    @Operation(summary = "Exchange OTP code for JWT tokens")
    @APIResponse(responseCode = "201", description = "Login successful, tokens returned")
    public Response validateOtpLogin(ValidateOtpLoginViewModel vm) {
        return delegate.validateOtpLogin(vm);
    }
}
