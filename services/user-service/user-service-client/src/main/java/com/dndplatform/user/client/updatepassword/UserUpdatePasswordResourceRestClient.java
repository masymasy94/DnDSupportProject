package com.dndplatform.user.client.updatepassword;

import com.dndplatform.common.client.RestClientExceptionMapper;
import com.dndplatform.user.client.authorization.AuthorizationFilter;
import com.dndplatform.user.view.model.UserUpdatePasswordResource;
import com.dndplatform.user.view.model.vm.UserUpdatePasswordViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.time.temporal.ChronoUnit;

import static com.dndplatform.user.client.ClientConfig.CLIENT_CONFIG_KEY;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@RegisterProvider(AuthorizationFilter.class)
@RegisterRestClient(configKey = CLIENT_CONFIG_KEY)
@RegisterProvider(RestClientExceptionMapper.class)
@RequestScoped
public interface UserUpdatePasswordResourceRestClient extends UserUpdatePasswordResource {

    @Override
    @PUT
    @Retry(delay = 100, delayUnit = ChronoUnit.MILLIS)
    @Path("/{id}/password")
    void updatePassword(@PathParam("id") long id, UserUpdatePasswordViewModel vm);
}
