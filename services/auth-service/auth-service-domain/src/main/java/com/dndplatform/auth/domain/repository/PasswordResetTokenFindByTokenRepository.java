package com.dndplatform.auth.domain.repository;

import com.dndplatform.auth.domain.model.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenFindByTokenRepository {
    Optional<PasswordResetToken> findByToken(String token);
}
