package com.dndplatform.user.domain;

import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.model.UserCredentialsValidate;

public interface UserVerifyCredentialsService {

    User validateCredentials(UserCredentialsValidate credentials);
}
