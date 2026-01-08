package com.dndplatform.auth.adapter.inbound.login;

import com.dndplatform.auth.adapter.inbound.login.mapper.LoginResponseViewModelMapper;
import com.dndplatform.auth.adapter.inbound.login.mapper.UserLoginMapper;
import com.dndplatform.auth.domain.UserLoginService;
import com.dndplatform.auth.domain.model.UserLoginBuilder;
import com.dndplatform.auth.view.model.UserLoginResource;
import com.dndplatform.auth.view.model.vm.LoginResponseViewModel;
import com.dndplatform.auth.view.model.vm.UserLoginViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;


@Delegate
@RequestScoped
public class UserLoginDelegate implements UserLoginResource {

    private final UserLoginMapper userLoginMapper;
    private final LoginResponseViewModelMapper loginResponseViewModelMapper;
    private final UserLoginService service;

    @Inject
    public UserLoginDelegate(UserLoginMapper userLoginMapper, LoginResponseViewModelMapper loginResponseViewModelMapper, UserLoginService service){
        this.userLoginMapper = userLoginMapper;
        this.loginResponseViewModelMapper = loginResponseViewModelMapper;
        this.service = service;
    }

    @Override
    public Response login(UserLoginViewModel userLoginViewModel) {

        var model = UserLoginBuilder.toBuilder(userLoginMapper.apply(userLoginViewModel)).build();
        var response = service.login(model);
        var responseBody = loginResponseViewModelMapper.apply(response);
        return Response.status(Response.Status.CREATED).entity(responseBody).build();

    }
}
