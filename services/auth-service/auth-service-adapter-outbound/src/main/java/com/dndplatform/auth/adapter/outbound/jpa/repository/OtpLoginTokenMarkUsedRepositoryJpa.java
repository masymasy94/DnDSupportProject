package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.domain.repository.OtpLoginTokenMarkUsedRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

import static com.dndplatform.auth.adapter.outbound.jpa.util.TokenUtil.sha256;

@ApplicationScoped
public class OtpLoginTokenMarkUsedRepositoryJpa implements OtpLoginTokenMarkUsedRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final OtpLoginPanacheRepository panacheRepository;

    @Inject
    public OtpLoginTokenMarkUsedRepositoryJpa(OtpLoginPanacheRepository panacheRepository) {
        this.panacheRepository = panacheRepository;
    }

    @Override
    @Transactional
    public void markUsed(String rawToken) {
        log.info("Marking OTP login token as used");
        var hashedToken = sha256(rawToken);
        panacheRepository.update("used = true where token = ?1", hashedToken);
    }
}
