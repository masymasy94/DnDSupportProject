package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.auth.adapter.outbound.jpa.mapper.UserMapper;
import com.dndplatform.auth.domain.model.User;
import com.dndplatform.auth.domain.repository.UserFindByCredentialsRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class UserFindByCredentialsRepositoryJpa implements UserFindByCredentialsRepository, PanacheRepository<UserEntity> {

    private final UserMapper mapper;

    @Inject
    public UserFindByCredentialsRepositoryJpa(UserMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<User> findUserByCredentials(String username, String passwordHash) {

        var usr = find("username = ?1 and passwordHash = ?2", username, passwordHash).firstResult();
        return Optional.ofNullable(mapper.apply(usr));
    }

}
