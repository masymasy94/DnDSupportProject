package com.dndplatform.user.adapter.inbound.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.user.adapter.inbound.register.mapper.UserViewModelMapper;
import com.dndplatform.user.domain.UserFindByIdService;
import com.dndplatform.user.view.model.UserFindByIdResource;
import com.dndplatform.user.view.model.vm.UserViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class UserFindByIdDelegate implements UserFindByIdResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final UserViewModelMapper viewModelMapper;
    private final UserFindByIdService service;

    @Inject
    public UserFindByIdDelegate(UserViewModelMapper viewModelMapper,
                                UserFindByIdService service) {
        this.viewModelMapper = viewModelMapper;
        this.service = service;
    }

    @Override
    public UserViewModel findById(long id) {
        log.info(() -> "Finding user by id: %s".formatted(id));

        var user = service.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: %s".formatted(id)));

        return viewModelMapper.apply(user);
    }
}
