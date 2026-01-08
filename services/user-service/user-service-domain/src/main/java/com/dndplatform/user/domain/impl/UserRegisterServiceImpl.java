package com.dndplatform.user.domain.impl;

import com.dndplatform.common.exception.ConflictException;
import com.dndplatform.user.domain.UserRegisterService;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.model.UserBuilder;
import com.dndplatform.user.domain.model.UserRegister;
import com.dndplatform.user.domain.repository.UserCreateRepository;
import com.dndplatform.user.domain.repository.UserExistsByEmailRepository;
import com.dndplatform.user.domain.repository.UserExistsByUsernameRepository;
import com.dndplatform.user.domain.util.CryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;

@ApplicationScoped
public class UserRegisterServiceImpl implements UserRegisterService {

    private static final String DEFAULT_ROLE = "PLAYER";

    private final UserExistsByUsernameRepository userExistsByUsernameRepository;
    private final UserExistsByEmailRepository userExistsByEmailRepository;
    private final UserCreateRepository userCreateRepository;

    @Inject
    public UserRegisterServiceImpl(UserExistsByUsernameRepository userExistsByUsernameRepository,
                                   UserExistsByEmailRepository userExistsByEmailRepository,
                                   UserCreateRepository userCreateRepository) {
        this.userExistsByUsernameRepository = userExistsByUsernameRepository;
        this.userExistsByEmailRepository = userExistsByEmailRepository;
        this.userCreateRepository = userCreateRepository;
    }

    @Override
    public User register(UserRegister userRegister) {
        validateUniqueness(userRegister);

        var passwordHash = CryptUtil.hashPassword(userRegister.password());

        var user = UserBuilder.builder()
                .withUsername(userRegister.username())
                .withEmail(userRegister.email())
                .withPasswordHash(passwordHash)
                .withRole(DEFAULT_ROLE)
                .withActive(true)
                .withCreatedAt(LocalDateTime.now())
                .build();

        return userCreateRepository.create(user);
    }

    private void validateUniqueness(UserRegister userRegister) {
        if (userExistsByUsernameRepository.existsByUsername(userRegister.username())) {
            throw new ConflictException("Username already exists");
        }

        if (userExistsByEmailRepository.existsByEmail(userRegister.email())) {
            throw new ConflictException("Email already registered");
        }
    }
}
