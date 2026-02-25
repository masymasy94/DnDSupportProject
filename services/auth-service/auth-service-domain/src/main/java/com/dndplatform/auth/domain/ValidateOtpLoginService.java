package com.dndplatform.auth.domain;

import com.dndplatform.auth.domain.model.CreateLoginTokenResponse;
import com.dndplatform.auth.domain.model.ValidateOtpLogin;

public interface ValidateOtpLoginService {
    CreateLoginTokenResponse validateOtpLogin(ValidateOtpLogin request);
}
