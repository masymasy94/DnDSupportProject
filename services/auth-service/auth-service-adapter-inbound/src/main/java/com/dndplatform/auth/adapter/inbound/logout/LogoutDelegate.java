package com.dndplatform.auth.adapter.inbound.logout;

import com.dndplatform.auth.domain.LogoutService;
import com.dndplatform.auth.view.model.LogoutResource;
import com.dndplatform.auth.view.model.vm.LogoutViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class LogoutDelegate implements LogoutResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final LogoutService service;

    @Inject
    public LogoutDelegate(LogoutService service) {
        this.service = service;
    }

    @Override
    public Response logout(LogoutViewModel logoutViewModel) {
        log.info(() -> "Logout request received for user id %s".formatted(logoutViewModel.userId()));

        service.logout(logoutViewModel.refreshToken(), logoutViewModel.userId());
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
