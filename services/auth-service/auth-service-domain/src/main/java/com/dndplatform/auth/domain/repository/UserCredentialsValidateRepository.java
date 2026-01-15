package com.dndplatform.auth.domain.repository;

import com.dndplatform.auth.domain.model.User;

public interface UserCredentialsValidateRepository {
    User validateCredentials(String username, String password);
}
