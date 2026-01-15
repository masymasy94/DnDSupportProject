package com.dndplatform.user.domain;

import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.model.UserRegister;

public interface UserRegisterService {
    User register(UserRegister userRegister);
}
