package com.dndplatform.user.domain.impl;

import com.dndplatform.common.exception.ConflictException;
import com.dndplatform.user.domain.UserRegisterService;
import com.dndplatform.user.domain.event.UserEventPublisher;
import com.dndplatform.user.domain.event.UserRegisteredEventMapper;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.model.UserBuilder;
import com.dndplatform.user.domain.model.UserRegister;
import com.dndplatform.user.domain.repository.UserCreateRepository;
import com.dndplatform.user.domain.repository.UserValidationRepository;
import com.dndplatform.user.domain.util.CryptUtil;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;

@ApplicationScoped
public class UserRegisterServiceImpl implements UserRegisterService {

    private static final String DEFAULT_ROLE = "PLAYER";

    private final UserValidationRepository userValidationRepository;
    private final UserCreateRepository userCreateRepository;
    private final UserEventPublisher userEventPublisher;
    private final UserRegisteredEventMapper userRegisteredEventMapper;

    @Inject
    public UserRegisterServiceImpl(UserValidationRepository userValidationRepository,
                                   UserCreateRepository userCreateRepository,
                                   UserEventPublisher userEventPublisher,
                                   UserRegisteredEventMapper userRegisteredEventMapper) {
        this.userValidationRepository = userValidationRepository;
        this.userCreateRepository = userCreateRepository;
        this.userEventPublisher = userEventPublisher;
        this.userRegisteredEventMapper = userRegisteredEventMapper;
    }

    @Override
    public User register(UserRegister userRegister) {

        userValidationRepository.existsByUsernameOrEmail(userRegister.username(), userRegister.email());
        var user = userCreateRepository.create(getUser(userRegister, CryptUtil.hashPassword(userRegister.password())));
        userEventPublisher.publishUserRegistered(userRegisteredEventMapper.apply(user));

        return user;
    }

    @Nonnull
    private static User getUser(UserRegister userRegister, String passwordHash) {
        return UserBuilder.builder()
                .withUsername(userRegister.username())
                .withEmail(userRegister.email())
                .withPasswordHash(passwordHash)
                .withRole(DEFAULT_ROLE)
                .withActive(true)
                .withCreatedAt(LocalDateTime.now())
                .build();
    }

}
