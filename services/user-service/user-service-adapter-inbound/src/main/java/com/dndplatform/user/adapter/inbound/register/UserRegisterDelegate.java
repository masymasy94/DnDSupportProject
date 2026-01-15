package com.dndplatform.user.adapter.inbound.register;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.user.adapter.inbound.register.mapper.UserRegisterMapper;
import com.dndplatform.user.adapter.inbound.register.mapper.UserViewModelMapper;
import com.dndplatform.user.domain.UserRegisterService;
import com.dndplatform.user.view.model.UserRegisterResource;
import com.dndplatform.user.view.model.vm.UserRegisterViewModel;
import com.dndplatform.user.view.model.vm.UserViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class UserRegisterDelegate implements UserRegisterResource {

    private final UserRegisterMapper registerMapper;
    private final UserViewModelMapper viewModelMapper;
    private final UserRegisterService service;

    @Inject
    public UserRegisterDelegate(UserRegisterMapper registerMapper,
                                UserViewModelMapper viewModelMapper,
                                UserRegisterService service) {
        this.registerMapper = registerMapper;
        this.viewModelMapper = viewModelMapper;
        this.service = service;
    }

    @Override
    public UserViewModel register(UserRegisterViewModel userRegisterViewModel) {
        var userRegister = registerMapper.apply(userRegisterViewModel);
        var user = service.register(userRegister);
        return viewModelMapper.apply(user);
    }
}
