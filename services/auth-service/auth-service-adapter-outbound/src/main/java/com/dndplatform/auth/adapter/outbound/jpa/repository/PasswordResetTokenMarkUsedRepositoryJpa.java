package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.domain.repository.PasswordResetTokenMarkUsedRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

import static com.dndplatform.auth.adapter.outbound.jpa.util.TokenUtil.sha256;

@ApplicationScoped
public class PasswordResetTokenMarkUsedRepositoryJpa implements PasswordResetTokenMarkUsedRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final PasswordResetPanacheRepository panacheRepository;

    @Inject
    public PasswordResetTokenMarkUsedRepositoryJpa(PasswordResetPanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    @Transactional
    public void markUsed(String rawToken) {
        log.info("Marking password reset token as used");
        var hashedToken = sha256(rawToken);
        panacheRepository.update("used = true where token = ?1", hashedToken);
    }
}
