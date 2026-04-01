package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.adapter.outbound.jpa.entity.PasswordResetEntity;
import com.dndplatform.auth.domain.repository.PasswordResetTokenMarkUsedRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

import static com.dndplatform.auth.adapter.outbound.jpa.util.TokenUtil.sha256;

@ApplicationScoped
public class PasswordResetTokenMarkUsedRepositoryJpa implements PasswordResetTokenMarkUsedRepository, PanacheRepository<PasswordResetEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());

    @Override
    @Transactional
    public void markUsed(String rawToken) {
        log.info("Marking password reset token as used");
        var hashedToken = sha256(rawToken);
        update("used = true where token = ?1", hashedToken);
    }
}
