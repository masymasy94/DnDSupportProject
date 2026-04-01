package com.dndplatform.user.adapter.outbound.jpa.repository;

import com.dndplatform.user.adapter.outbound.jpa.entity.UserEntity;
import com.dndplatform.user.domain.repository.UserUpdatePasswordRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.logging.Logger;

@ApplicationScoped
public class UserUpdatePasswordRepositoryJpa implements UserUpdatePasswordRepository, PanacheRepository<UserEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());

    @Override
    @Transactional
    public void updatePassword(long id, String passwordHash) {
        log.info(() -> "Updating password hash for user id: %s".formatted(id));
        update("passwordHash = ?1, updatedAt = ?2 where id = ?3", passwordHash, LocalDateTime.now(), id);
    }
}
