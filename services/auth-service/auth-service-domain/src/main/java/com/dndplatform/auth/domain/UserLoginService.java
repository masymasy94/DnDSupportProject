package com.dndplatform.auth.domain;

import com.dndplatform.auth.domain.model.UserLogin;

public interface UserLoginService {
    String login(UserLogin loginResource);
}
