package com.dndplatform.user.adapter.inbound.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.user.adapter.inbound.register.mapper.UserViewModelMapper;
import com.dndplatform.user.domain.UserFindAllService;
import com.dndplatform.user.view.model.UserFindAllResource;
import com.dndplatform.user.view.model.vm.PagedUserViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class UserFindAllDelegate implements UserFindAllResource {

    private final UserFindAllService service;
    private final UserViewModelMapper mapper;

    @Inject
    public UserFindAllDelegate(UserFindAllService service, UserViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public PagedUserViewModel findAll(int page, int size) {
        var users = service.findAll(page, size).stream()
                .map(mapper)
                .toList();
        long total = service.count();
        int totalPages = (int) Math.ceil((double) total / size);
        return new PagedUserViewModel(users, page, size, total, totalPages);
    }
}
