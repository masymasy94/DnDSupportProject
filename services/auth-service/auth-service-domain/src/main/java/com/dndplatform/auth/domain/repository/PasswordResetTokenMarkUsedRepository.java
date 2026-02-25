package com.dndplatform.auth.domain.repository;

public interface PasswordResetTokenMarkUsedRepository {
    void markUsed(String token);
}
