package com.dndplatform.auth.domain;

import com.dndplatform.auth.domain.model.TokenPair;
import com.dndplatform.auth.domain.model.UserLogin;

public interface UserLoginService {
    TokenPair login(UserLogin loginResource);
}
