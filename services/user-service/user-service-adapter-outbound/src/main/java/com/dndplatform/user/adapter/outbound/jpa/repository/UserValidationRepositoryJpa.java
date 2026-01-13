package com.dndplatform.user.adapter.outbound.jpa.repository;

import com.dndplatform.common.exception.ConflictException;
import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.domain.repository.UserValidationRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.logging.Logger;

@ApplicationScoped
public class UserValidationRepositoryJpa implements UserValidationRepository, PanacheRepository<UserEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());

    @Override
    public void existsByUsernameOrEmail(String username, String email) {
        log.info(() -> "Checking if exists by username or email address %s".formatted(username));

        if (count("username = ?1 or email = ?2", username, email) > 0)
            throw new ConflictException("Username or email already exists");
    }
}
