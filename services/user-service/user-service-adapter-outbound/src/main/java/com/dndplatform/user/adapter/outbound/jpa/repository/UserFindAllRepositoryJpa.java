package com.dndplatform.user.adapter.outbound.jpa.repository;

import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.adapter.outbound.jpa.mapper.UserMapper;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.repository.UserFindAllRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class UserFindAllRepositoryJpa implements UserFindAllRepository {

    private final UserMapper mapper;

    @Inject
    public UserFindAllRepositoryJpa(UserMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<User> findAll(int page, int size) {
        return UserEntity.findAll(Sort.by("username"))
                .page(Page.of(page, size))
                .list()
                .stream()
                .map(e -> mapper.apply((UserEntity) e))
                .toList();
    }

    @Override
    public long count() {
        return UserEntity.count();
    }
}
