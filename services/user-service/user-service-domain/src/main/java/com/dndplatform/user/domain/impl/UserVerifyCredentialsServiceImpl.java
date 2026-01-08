package com.dndplatform.user.domain.impl;

import com.dndplatform.common.exception.ForbiddenException;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.common.exception.UnauthorizedException;
import com.dndplatform.user.domain.UserVerifyCredentialsService;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.model.UserCredentialsValidate;
import com.dndplatform.user.domain.repository.UserFindByUsernameRepository;
import com.dndplatform.user.domain.util.CryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserVerifyCredentialsServiceImpl implements UserVerifyCredentialsService {

    private final UserFindByUsernameRepository userFindByUsernameRepository;

    @Inject
    public UserVerifyCredentialsServiceImpl(UserFindByUsernameRepository userFindByUsernameRepository) {
        this.userFindByUsernameRepository = userFindByUsernameRepository;
    }

    @Override
    public User validateCredentials(UserCredentialsValidate credentials) {

        var user = userFindByUsernameRepository.findByUsername(credentials.username())
                .orElseThrow(() -> new NotFoundException("No user found with the given username"));

        if (!CryptUtil.verifyPassword(credentials.password(), user.passwordHash())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        if (!user.active()) {
            throw new ForbiddenException("User account is not active");
        }

        return user;
    }
}
