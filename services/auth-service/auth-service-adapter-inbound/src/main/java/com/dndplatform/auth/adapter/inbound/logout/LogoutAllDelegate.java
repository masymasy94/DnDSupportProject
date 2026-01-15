package com.dndplatform.auth.adapter.inbound.logout;

import com.dndplatform.auth.domain.LogoutAllService;
import com.dndplatform.auth.view.model.LogoutAllResource;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class LogoutAllDelegate implements LogoutAllResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final LogoutAllService logoutAllService;

    @Inject
    public LogoutAllDelegate(LogoutAllService logoutAllService) {
        this.logoutAllService = logoutAllService;
    }

    @Override
    public Response logoutAll(long userId) {
        log.info(() -> "Logout all request received for user id %s".formatted(userId));

        logoutAllService.logoutAll(userId);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
