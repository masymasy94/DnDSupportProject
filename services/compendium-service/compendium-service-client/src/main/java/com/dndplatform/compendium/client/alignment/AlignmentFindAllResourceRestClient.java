package com.dndplatform.compendium.client.alignment;

import com.dndplatform.common.client.RestClientExceptionMapper;
import com.dndplatform.compendium.view.model.AlignmentFindAllResource;
import com.dndplatform.compendium.view.model.vm.AlignmentViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.annotation.RegisterProvider;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.dndplatform.compendium.client.ClientConfig.CLIENT_CONFIG_KEY;

@Path("/api/compendium/alignments")
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient(configKey = CLIENT_CONFIG_KEY)
@RegisterProvider(RestClientExceptionMapper.class)
@RequestScoped
public interface AlignmentFindAllResourceRestClient extends AlignmentFindAllResource {

    @Override
    @GET
    @Retry(delay = 100, delayUnit = ChronoUnit.MILLIS)
    List<AlignmentViewModel> findAll();
}
