package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.adapter.outbound.jpa.mapper.PasswordResetTokenMapper;
import com.dndplatform.auth.domain.model.PasswordResetToken;
import com.dndplatform.auth.domain.repository.PasswordResetTokenFindByTokenRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

import static com.dndplatform.auth.adapter.outbound.jpa.util.TokenUtil.sha256;

@ApplicationScoped
public class PasswordResetTokenFindByTokenRepositoryJpa implements PasswordResetTokenFindByTokenRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final PasswordResetTokenMapper mapper;
    private final PasswordResetPanacheRepository panacheRepository;

    @Inject
    public PasswordResetTokenFindByTokenRepositoryJpa(PasswordResetTokenMapper mapper,
                                                       PasswordResetPanacheRepository panacheRepository) {
        this.mapper = mapper;
        this.panacheRepository = panacheRepository;
    }

    @Override
    public Optional<PasswordResetToken> findByToken(String rawToken) {
        log.info("Looking up password reset token");
        var hashedToken = sha256(rawToken);
        return panacheRepository.find("token", hashedToken)
                .firstResultOptional()
                .map(mapper);
    }
}
