package com.dndplatform.user.domain.impl;

import com.dndplatform.user.domain.UserUpdatePasswordService;
import com.dndplatform.user.domain.repository.UserUpdatePasswordRepository;
import com.dndplatform.user.domain.util.CryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class UserUpdatePasswordServiceImpl implements UserUpdatePasswordService {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final UserUpdatePasswordRepository userUpdatePasswordRepository;

    @Inject
    public UserUpdatePasswordServiceImpl(UserUpdatePasswordRepository userUpdatePasswordRepository) {
        this.userUpdatePasswordRepository = userUpdatePasswordRepository;
    }

    @Override
    public void updatePassword(long id, String newPassword) {
        log.info(() -> "Updating password for user id: %s".formatted(id));
        var passwordHash = CryptUtil.hashPassword(newPassword);
        userUpdatePasswordRepository.updatePassword(id, passwordHash);
    }
}
