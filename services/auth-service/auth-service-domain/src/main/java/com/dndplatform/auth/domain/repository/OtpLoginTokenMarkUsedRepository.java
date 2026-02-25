package com.dndplatform.auth.domain.repository;

public interface OtpLoginTokenMarkUsedRepository {
    void markUsed(String rawToken);
}
