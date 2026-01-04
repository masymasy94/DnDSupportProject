package com.dndplatform.auth.adapter.inbound.login;

import com.dndplatform.auth.adapter.inbound.login.mapper.UserLoginMapper;
import com.dndplatform.auth.domain.UserLoginService;
import com.dndplatform.auth.domain.model.UserLoginBuilder;
import com.dndplatform.auth.view.model.UserLoginResource;
import com.dndplatform.auth.view.model.vm.UserLoginViewModel;
import com.dndplatform.common.annotations.Delegate;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;


@Delegate
@RequestScoped
public class UserLoginDelegate implements UserLoginResource {

    private final UserLoginMapper mapper;
    private final UserLoginService service;

    @Inject
    public UserLoginDelegate(UserLoginMapper mapper, UserLoginService service){
        this.mapper = mapper;
        this.service = service;
    }

    @Override
    public int login(UserLoginViewModel userLoginViewModel) {
        var model = UserLoginBuilder.toBuilder(mapper.apply(userLoginViewModel)).build();
        return service.login(model);
    }
}
