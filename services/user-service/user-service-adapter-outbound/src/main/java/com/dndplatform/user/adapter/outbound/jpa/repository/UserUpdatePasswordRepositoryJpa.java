package com.dndplatform.user.adapter.outbound.jpa.repository;

import com.dndplatform.user.domain.repository.UserUpdatePasswordRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class UserUpdatePasswordRepositoryJpa implements UserUpdatePasswordRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final UserPanacheRepository panacheRepository;

    @Inject
    public UserUpdatePasswordRepositoryJpa(UserPanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    @Transactional
    public void updatePassword(long id, String passwordHash) {
        log.info(() -> "Updating password hash for user id: %s".formatted(id));
        panacheRepository.updatePassword(id, passwordHash, LocalDateTime.now());
    }
}
