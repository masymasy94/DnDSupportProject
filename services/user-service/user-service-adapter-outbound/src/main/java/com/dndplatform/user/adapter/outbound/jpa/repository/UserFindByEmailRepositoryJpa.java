package com.dndplatform.user.adapter.outbound.jpa.repository;

import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.adapter.outbound.jpa.mapper.UserMapper;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.repository.UserFindByEmailRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class UserFindByEmailRepositoryJpa implements UserFindByEmailRepository, PanacheRepository<UserEntity> {

    private final UserMapper mapper;

    @Inject
    public UserFindByEmailRepositoryJpa(UserMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return find("email", email)
                .firstResultOptional()
                .map(mapper);
    }
}
