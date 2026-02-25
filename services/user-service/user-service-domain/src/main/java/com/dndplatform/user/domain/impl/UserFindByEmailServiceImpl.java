package com.dndplatform.user.domain.impl;

import com.dndplatform.user.domain.UserFindByEmailService;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.repository.UserFindByEmailRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class UserFindByEmailServiceImpl implements UserFindByEmailService {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final UserFindByEmailRepository userFindByEmailRepository;

    @Inject
    public UserFindByEmailServiceImpl(UserFindByEmailRepository userFindByEmailRepository) {
        this.userFindByEmailRepository = userFindByEmailRepository;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log.info(() -> "Finding user by email: %s".formatted(email));
        return userFindByEmailRepository.findByEmail(email);
    }
}
