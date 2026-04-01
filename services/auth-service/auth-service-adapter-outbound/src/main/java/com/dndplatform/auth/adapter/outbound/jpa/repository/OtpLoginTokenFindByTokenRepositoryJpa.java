package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.adapter.outbound.jpa.entity.OtpLoginEntity;
import com.dndplatform.auth.adapter.outbound.jpa.mapper.OtpLoginTokenMapper;
import com.dndplatform.auth.domain.model.OtpLoginToken;
import com.dndplatform.auth.domain.repository.OtpLoginTokenFindByTokenRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

import static com.dndplatform.auth.adapter.outbound.jpa.util.TokenUtil.sha256;

@ApplicationScoped
public class OtpLoginTokenFindByTokenRepositoryJpa implements OtpLoginTokenFindByTokenRepository, PanacheRepository<OtpLoginEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final OtpLoginTokenMapper mapper;

    @Inject
    public OtpLoginTokenFindByTokenRepositoryJpa(OtpLoginTokenMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Optional<OtpLoginToken> findByToken(String rawToken) {
        log.info("Looking up OTP login token");
        var hashedToken = sha256(rawToken);
        return find("token", hashedToken)
                .firstResultOptional()
                .map(mapper);
    }
}
