package com.dndplatform.auth.domain;

import com.dndplatform.auth.domain.model.RequestPasswordReset;

public interface RequestPasswordResetService {
    void requestPasswordReset(RequestPasswordReset request);
}
