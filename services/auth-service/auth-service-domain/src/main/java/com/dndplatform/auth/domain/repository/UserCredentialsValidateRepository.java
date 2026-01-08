package com.dndplatform.auth.domain.repository;

import com.dndplatform.auth.domain.model.User;
import com.dndplatform.auth.domain.model.UserLogin;

public interface UserCredentialsValidateRepository {
    User validateCredentials(UserLogin userLogin);
}
