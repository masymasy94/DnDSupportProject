package com.dndplatform.auth.domain.repository;

import com.dndplatform.auth.domain.model.OtpLoginToken;

import java.util.Optional;

public interface OtpLoginTokenFindByTokenRepository {
    Optional<OtpLoginToken> findByToken(String rawToken);
}
