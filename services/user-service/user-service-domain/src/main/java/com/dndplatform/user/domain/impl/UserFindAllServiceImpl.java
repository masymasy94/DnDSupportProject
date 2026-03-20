package com.dndplatform.user.domain.impl;

import com.dndplatform.user.domain.UserFindAllService;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.repository.UserFindAllRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class UserFindAllServiceImpl implements UserFindAllService {

    private final UserFindAllRepository repository;

    @Inject
    public UserFindAllServiceImpl(UserFindAllRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<User> findAll(int page, int size) {
        return repository.findAll(page, size);
    }

    @Override
    public long count() {
        return repository.count();
    }
}
