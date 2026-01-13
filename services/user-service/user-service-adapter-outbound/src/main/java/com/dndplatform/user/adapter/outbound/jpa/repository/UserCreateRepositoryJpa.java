package com.dndplatform.user.adapter.outbound.jpa.repository;

import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.adapter.outbound.jpa.mapper.UserEntityMapper;
import com.dndplatform.user.adapter.outbound.jpa.mapper.UserMapper;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.repository.UserCreateRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserCreateRepositoryJpa implements UserCreateRepository, PanacheRepository<UserEntity> {

    private final UserEntityMapper entityMapper;
    private final UserMapper domainMapper;

    @Inject
    public UserCreateRepositoryJpa(UserEntityMapper entityMapper, UserMapper domainMapper) {
        this.entityMapper = entityMapper;
        this.domainMapper = domainMapper;
    }

    @Override
    @Transactional
    public User create(User user) {
        var entity = entityMapper.apply(user);
        persist(entity);
        return domainMapper.apply(entity);
    }
}
