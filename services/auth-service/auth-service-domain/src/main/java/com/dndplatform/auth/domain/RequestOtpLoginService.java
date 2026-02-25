package com.dndplatform.auth.domain;

import com.dndplatform.auth.domain.model.RequestOtpLogin;

public interface RequestOtpLoginService {
    void requestOtpLogin(RequestOtpLogin request);
}
