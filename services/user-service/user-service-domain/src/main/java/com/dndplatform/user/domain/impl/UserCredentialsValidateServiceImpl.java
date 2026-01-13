package com.dndplatform.user.domain.impl;

import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.UnauthorizedException;
import com.dndplatform.user.domain.UserCredentialsValidateService;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.model.UserCredentialsValidate;
import com.dndplatform.user.domain.repository.UserFindByUsernameRepository;
import com.dndplatform.user.domain.util.CryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserCredentialsValidateServiceImpl implements UserCredentialsValidateService {

    private final UserFindByUsernameRepository userFindByUsernameRepository;

    @Inject
    public UserCredentialsValidateServiceImpl(UserFindByUsernameRepository userFindByUsernameRepository) {
        this.userFindByUsernameRepository = userFindByUsernameRepository;
    }

    @Override
    public User validateCredentials(UserCredentialsValidate credentials) {

        User user = userFindByUsernameRepository.findByUsername(credentials.username())
                .filter(u -> CryptUtil.verifyPassword(credentials.password(), u.passwordHash()))
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials for username %s".formatted(credentials.username())));

        if (!user.active()) {
            throw new ForbiddenException("User is not active");
        }

        return user;
    }
}
