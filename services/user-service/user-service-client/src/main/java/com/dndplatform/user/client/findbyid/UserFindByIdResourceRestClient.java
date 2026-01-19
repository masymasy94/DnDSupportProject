package com.dndplatform.user.client.findbyid;

import com.dndplatform.common.client.RestClientExceptionMapper;
import com.dndplatform.user.view.model.UserFindByIdResource;
import com.dndplatform.user.view.model.vm.UserViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.time.temporal.ChronoUnit;

import static com.dndplatform.user.client.ClientConfig.CLIENT_CONFIG_KEY;

@Path("/users/")
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient(configKey = CLIENT_CONFIG_KEY)
@RegisterProvider(RestClientExceptionMapper.class)
@RequestScoped
public interface UserFindByIdResourceRestClient extends UserFindByIdResource {

    @Override
    @GET
    @Path("/{id}")
    @Retry(delay = 100, delayUnit = ChronoUnit.MILLIS)
    UserViewModel findById(@PathParam("id") long id);
}
