package com.dndplatform.user.adapter.inbound.findbyemail;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.user.adapter.inbound.register.mapper.UserViewModelMapper;
import com.dndplatform.user.domain.UserFindByEmailService;
import com.dndplatform.user.view.model.UserFindByEmailResource;
import com.dndplatform.user.view.model.vm.UserFindByEmailViewModel;
import com.dndplatform.user.view.model.vm.UserViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class UserFindByEmailDelegate implements UserFindByEmailResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final UserViewModelMapper viewModelMapper;
    private final UserFindByEmailService service;

    @Inject
    public UserFindByEmailDelegate(UserViewModelMapper viewModelMapper,
                                   UserFindByEmailService service) {
        this.viewModelMapper = viewModelMapper;
        this.service = service;
    }

    @Override
    public UserViewModel findByEmail(UserFindByEmailViewModel vm) {
        log.info(() -> "Finding user by email: %s".formatted(vm.email()));
        var user = service.findByEmail(vm.email())
                .orElseThrow(() -> new NotFoundException("User not found with email: %s".formatted(vm.email())));
        return viewModelMapper.apply(user);
    }
}
