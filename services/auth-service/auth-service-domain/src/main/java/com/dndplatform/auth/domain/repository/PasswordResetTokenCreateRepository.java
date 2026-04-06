package com.dndplatform.auth.domain.repository;

import com.dndplatform.auth.domain.model.PasswordResetToken;

public interface PasswordResetTokenCreateRepository {
    PasswordResetToken create(long userId);
}
