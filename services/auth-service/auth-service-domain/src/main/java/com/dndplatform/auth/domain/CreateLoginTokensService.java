package com.dndplatform.auth.domain;

import com.dndplatform.auth.domain.model.CreateLoginTokenResponse;
import com.dndplatform.auth.domain.model.CreateLoginTokens;

public interface CreateLoginTokensService {
    CreateLoginTokenResponse createLoginTokens(CreateLoginTokens createLoginTokens);
}
