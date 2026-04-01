package com.dndplatform.user.domain.impl;

import com.dndplatform.user.domain.UserSearchService;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.repository.UserSearchRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class UserSearchServiceImpl implements UserSearchService {

    private final UserSearchRepository repository;

    @Inject
    public UserSearchServiceImpl(UserSearchRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> search(String query, int page, int size) {
        return repository.search(query, page, size);
    }

    @Override
    public long countByQuery(String query) {
        return repository.countByQuery(query);
    }
}
