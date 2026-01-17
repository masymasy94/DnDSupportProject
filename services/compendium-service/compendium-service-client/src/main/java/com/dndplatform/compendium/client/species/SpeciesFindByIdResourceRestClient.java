package com.dndplatform.compendium.client.species;

import com.dndplatform.common.client.RestClientExceptionMapper;
import com.dndplatform.compendium.view.model.SpeciesFindByIdResource;
import com.dndplatform.compendium.view.model.vm.SpeciesViewModel;
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

import static com.dndplatform.compendium.client.ClientConfig.CLIENT_CONFIG_KEY;

@Path("/api/compendium/species")
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient(configKey = CLIENT_CONFIG_KEY)
@RegisterProvider(RestClientExceptionMapper.class)
@RequestScoped
public interface SpeciesFindByIdResourceRestClient extends SpeciesFindByIdResource {

    @Override
    @GET
    @Path("/{id}")
    @Retry(delay = 100, delayUnit = ChronoUnit.MILLIS)
    SpeciesViewModel findById(@PathParam("id") int id);
}
