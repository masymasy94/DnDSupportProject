package com.dndplatform.auth.domain.impl;

import com.dndplatform.auth.domain.UserLoginService;
import com.dndplatform.auth.domain.model.UserLogin;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserLoginServiceImpl implements UserLoginService {

    @Override
    public int login(UserLogin loginResource) {
        return 10000;
    }
}
