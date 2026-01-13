package com.dndplatform.notificationservice.client.send;

import com.dndplatform.common.client.RestClientExceptionMapper;
import com.dndplatform.notificationservice.view.model.SendEmailResource;
import com.dndplatform.notificationservice.view.model.vm.SendEmailRequestViewModel;
import com.dndplatform.notificationservice.view.model.vm.SendEmailResponseViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.time.temporal.ChronoUnit;

import static com.dndplatform.notificationservice.client.ClientConfig.CLIENT_CONFIG_KEY;

@Path("/emails")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RegisterRestClient(configKey = CLIENT_CONFIG_KEY)
@RegisterProvider(RestClientExceptionMapper.class)
@RequestScoped
public interface SendEmailResourceRestClient extends SendEmailResource {

    @Override
    @POST
    @Retry(delay = 100, delayUnit = ChronoUnit.MILLIS)
    Response syncSend(SendEmailRequestViewModel request);
}
