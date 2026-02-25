package com.dndplatform.auth.domain;

import com.dndplatform.auth.domain.model.ResetPassword;

public interface ResetPasswordService {
    void resetPassword(ResetPassword request);
}
