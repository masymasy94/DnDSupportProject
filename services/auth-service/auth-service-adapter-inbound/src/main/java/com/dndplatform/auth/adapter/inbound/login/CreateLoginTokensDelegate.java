package com.dndplatform.auth.adapter.inbound.login;

import com.dndplatform.auth.adapter.inbound.login.mapper.CreateLoginTokensMapper;
import com.dndplatform.auth.adapter.inbound.login.mapper.LoginResponseViewModelMapper;
import com.dndplatform.auth.domain.CreateLoginTokensService;
import com.dndplatform.auth.domain.model.CreateLoginTokensBuilder;
import com.dndplatform.auth.view.model.CreateLoginTokensResource;
import com.dndplatform.auth.view.model.vm.CreateLoginTokensViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.logging.Logger;


@Delegate
@RequestScoped
public class CreateLoginTokensDelegate implements CreateLoginTokensResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final CreateLoginTokensMapper createLoginTokensMapper;
    private final LoginResponseViewModelMapper loginResponseViewModelMapper;
    private final CreateLoginTokensService service;

    @Inject
    public CreateLoginTokensDelegate(CreateLoginTokensMapper createLoginTokensMapper, LoginResponseViewModelMapper loginResponseViewModelMapper, CreateLoginTokensService service){
        this.createLoginTokensMapper = createLoginTokensMapper;
        this.loginResponseViewModelMapper = loginResponseViewModelMapper;
        this.service = service;
    }

    @Override
    public Response createLoginTokens(CreateLoginTokensViewModel createLoginTokensViewModel) {

        log.info(() -> "Login attempt for user: " + createLoginTokensViewModel.username());

        var model = CreateLoginTokensBuilder.toBuilder(createLoginTokensMapper.apply(createLoginTokensViewModel)).build();
        var response = service.createLoginTokens(model);
        var responseBody = loginResponseViewModelMapper.apply(response);

        log.info(() -> "Login response: %s".formatted(responseBody));
        return Response.status(Response.Status.CREATED).entity(responseBody).build();

    }
}
