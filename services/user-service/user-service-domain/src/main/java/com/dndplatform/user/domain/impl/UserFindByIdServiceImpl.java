package com.dndplatform.user.domain.impl;

import com.dndplatform.user.domain.UserFindByIdService;
import com.dndplatform.user.domain.model.User;
import com.dndplatform.user.domain.repository.UserFindByIdRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class UserFindByIdServiceImpl implements UserFindByIdService {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final UserFindByIdRepository userFindByIdRepository;

    @Inject
    public UserFindByIdServiceImpl(UserFindByIdRepository userFindByIdRepository) {
        this.userFindByIdRepository = userFindByIdRepository;
    }

    @Override
    public Optional<User> findById(long id) {
        log.info(() -> "Finding user by id: %s".formatted(id));
        return userFindByIdRepository.findById(id);
    }
}
