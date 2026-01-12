package com.dndplatform.auth.adapter.outbound.rest.repository;

import com.dndplatform.auth.adapter.outbound.rest.mapper.UserMapper;
import com.dndplatform.auth.domain.model.User;
import com.dndplatform.auth.domain.repository.UserFindByIdRepository;
import com.dndplatform.user.client.findbyid.UserFindByIdResourceRestClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class UserFindByIdRepositoryRest implements UserFindByIdRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final UserFindByIdResourceRestClient userFindByIdResourceRestClient;
    private final UserMapper userMapper;

    @Inject
    public UserFindByIdRepositoryRest(@RestClient UserFindByIdResourceRestClient userFindByIdResourceRestClient, UserMapper userMapper) {
        this.userFindByIdResourceRestClient = userFindByIdResourceRestClient;
        this.userMapper = userMapper;
    }


    @Override
    public Optional<User> findById(long userId) {
        log.info(() -> "Calling user service for user with id %s".formatted(userId));

        return Optional.ofNullable(userFindByIdResourceRestClient.findById(userId))
                .map(userMapper);
    }
}
