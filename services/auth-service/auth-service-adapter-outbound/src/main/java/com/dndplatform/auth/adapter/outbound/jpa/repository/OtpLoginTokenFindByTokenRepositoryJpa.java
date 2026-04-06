package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.adapter.outbound.jpa.mapper.OtpLoginTokenMapper;
import com.dndplatform.auth.domain.model.OtpLoginToken;
import com.dndplatform.auth.domain.repository.OtpLoginTokenFindByTokenRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

import static com.dndplatform.auth.adapter.outbound.jpa.util.TokenUtil.sha256;

@ApplicationScoped
public class OtpLoginTokenFindByTokenRepositoryJpa implements OtpLoginTokenFindByTokenRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final OtpLoginTokenMapper mapper;
    private final OtpLoginPanacheRepository panacheRepository;

    @Inject
    public OtpLoginTokenFindByTokenRepositoryJpa(OtpLoginTokenMapper mapper,
                                                  OtpLoginPanacheRepository panacheRepository) {
        this.mapper = mapper;
        this.panacheRepository = panacheRepository;
    }

    @Override
    public Optional<OtpLoginToken> findByToken(String rawToken) {
        log.info("Looking up OTP login token");
        var hashedToken = sha256(rawToken);
        return panacheRepository.find("token", hashedToken)
                .firstResultOptional()
                .map(mapper);
    }
}
