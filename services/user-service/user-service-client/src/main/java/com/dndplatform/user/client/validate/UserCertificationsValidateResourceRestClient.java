package com.dndplatform.user.client.validate;

import com.dndplatform.common.client.RestClientExceptionMapper;
import com.dndplatform.user.client.authorization.AuthorizationFilter;
import com.dndplatform.user.view.model.UserCredentialsValidateResource;
import com.dndplatform.user.view.model.vm.UserCredentialsValidateViewModel;
import com.dndplatform.user.view.model.vm.UserViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.time.temporal.ChronoUnit;

import static com.dndplatform.user.client.ClientConfig.CLIENT_CONFIG_KEY;


@Path("/internal/users/credentials-validation")
@Produces(MediaType.APPLICATION_JSON)
@RegisterProvider(AuthorizationFilter.class)
@RegisterRestClient(configKey = CLIENT_CONFIG_KEY)
@RegisterProvider(RestClientExceptionMapper.class)
@RequestScoped
public interface UserCertificationsValidateResourceRestClient extends UserCredentialsValidateResource {

    @Override
    @POST
    @Retry(delay = 100, delayUnit = ChronoUnit.MILLIS)
    @Path("")
    UserViewModel validateUserCredentials(UserCredentialsValidateViewModel userCredentialsValidateViewModel);
}
