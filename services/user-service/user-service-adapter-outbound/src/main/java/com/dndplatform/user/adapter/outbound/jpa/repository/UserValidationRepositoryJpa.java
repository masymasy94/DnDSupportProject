package com.dndplatform.user.adapter.outbound.jpa.repository;

import com.dndplatform.common.exception.ConflictException;
import com.dndplatform.user.domain.repository.UserValidationRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class UserValidationRepositoryJpa implements UserValidationRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final UserPanacheRepository panacheRepository;

    @Inject
    public UserValidationRepositoryJpa(UserPanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    public void existsByUsernameOrEmail(String username, String email) {
        log.info(() -> "Checking if exists by username or email address %s".formatted(username));

        if (panacheRepository.countByUsernameOrEmail(username, email) > 0)
            throw new ConflictException("Username or email already exists");
    }
}
