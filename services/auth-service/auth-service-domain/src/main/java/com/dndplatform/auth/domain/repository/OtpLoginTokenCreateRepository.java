package com.dndplatform.auth.domain.repository;

import com.dndplatform.auth.domain.model.OtpLoginToken;

public interface OtpLoginTokenCreateRepository {
    OtpLoginToken create(long userId);
}
