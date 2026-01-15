package com.dndplatform.user.adapter.inbound.validate;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.user.adapter.inbound.register.mapper.UserViewModelMapper;
import com.dndplatform.user.adapter.inbound.validate.mapper.UserCredentialsValidateMapper;
import com.dndplatform.user.domain.UserCredentialsValidateService;
import com.dndplatform.user.view.model.UserCredentialsValidateResource;
import com.dndplatform.user.view.model.vm.UserCredentialsValidateViewModel;
import com.dndplatform.user.view.model.vm.UserViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class UserCredentialsValidateDelegate implements UserCredentialsValidateResource {

    private final UserCredentialsValidateMapper credentialsMapper;
    private final UserViewModelMapper viewModelMapper;
    private final UserCredentialsValidateService service;

    @Inject
    public UserCredentialsValidateDelegate(UserCredentialsValidateMapper credentialsMapper,
                                           UserViewModelMapper viewModelMapper,
                                           UserCredentialsValidateService service) {
        this.credentialsMapper = credentialsMapper;
        this.viewModelMapper = viewModelMapper;
        this.service = service;
    }

    @Override
    public UserViewModel validateUserCredentials(UserCredentialsValidateViewModel userCredentialsValidateViewModel) {
        var model = credentialsMapper.apply(userCredentialsValidateViewModel);
        var user = service.validateCredentials(model);
        return viewModelMapper.apply(user);
    }
}
