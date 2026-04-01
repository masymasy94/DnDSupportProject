package com.dndplatform.user.adapter.inbound.search;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.user.adapter.inbound.register.mapper.UserViewModelMapper;
import com.dndplatform.user.domain.UserSearchService;
import com.dndplatform.user.view.model.UserSearchResource;
import com.dndplatform.user.view.model.vm.PagedUserViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

@Delegate
@RequestScoped
public class UserSearchDelegate implements UserSearchResource {

    private final UserSearchService service;
    private final UserViewModelMapper mapper;

    @Inject
    public UserSearchDelegate(UserSearchService service, UserViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public PagedUserViewModel search(String query, int page, int size) {
        var users = service.search(query, page, size).stream()
                .map(mapper)
                .toList();
        long total = service.countByQuery(query);
        int totalPages = (int) Math.ceil((double) total / size);
        return new PagedUserViewModel(users, page, size, total, totalPages);
    }
}
