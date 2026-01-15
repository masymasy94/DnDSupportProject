package com.dndplatform.auth.adapter.inbound.refresh;

import com.dndplatform.auth.adapter.inbound.login.mapper.LoginResponseViewModelMapper;
import com.dndplatform.auth.adapter.inbound.refresh.mapper.RefreshLoginTokenResponseViewModelMapper;
import com.dndplatform.auth.domain.RefreshLoginTokensService;
import com.dndplatform.auth.view.model.RefreshLoginTokensResource;
import com.dndplatform.auth.view.model.vm.RefreshTokenViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class RefreshLoginTokensDelegate implements RefreshLoginTokensResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final RefreshLoginTokensService service;
    private final RefreshLoginTokenResponseViewModelMapper mapper;

    @Inject
    public RefreshLoginTokensDelegate(RefreshLoginTokensService service,
                                      RefreshLoginTokenResponseViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public Response refreshLoginTokens(RefreshTokenViewModel refreshTokenViewModel) {
        log.info(() -> "Refresh token request received for user id %s".formatted(refreshTokenViewModel.userId()));

        var response = service.refreshLoginTokens(refreshTokenViewModel.token(), refreshTokenViewModel.userId());
        var responseBody = mapper.apply(response);
        return Response.status(Response.Status.CREATED).entity(responseBody).build();
    }

}
