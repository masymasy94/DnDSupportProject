package com.dndplatform.auth.domain;

import com.dndplatform.auth.domain.model.LoginResponse;
import com.dndplatform.auth.domain.model.UserLogin;

public interface UserLoginService {
    LoginResponse login(UserLogin loginResource);
}
