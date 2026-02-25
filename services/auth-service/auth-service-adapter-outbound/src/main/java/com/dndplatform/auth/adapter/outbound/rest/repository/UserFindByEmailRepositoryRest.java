package com.dndplatform.auth.adapter.outbound.rest.repository;

import com.dndplatform.auth.adapter.outbound.rest.mapper.UserMapper;
import com.dndplatform.auth.domain.model.User;
import com.dndplatform.auth.domain.repository.UserFindByEmailRepository;
import com.dndplatform.common.exception.NotFoundException;
import com.dndplatform.user.client.findbyemail.UserFindByEmailResourceRestClient;
import com.dndplatform.user.view.model.vm.UserFindByEmailViewModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Optional;
import java.util.logging.Logger;

@ApplicationScoped
public class UserFindByEmailRepositoryRest implements UserFindByEmailRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final UserFindByEmailResourceRestClient userFindByEmailClient;
    private final UserMapper userMapper;

    @Inject
    public UserFindByEmailRepositoryRest(@RestClient UserFindByEmailResourceRestClient userFindByEmailClient,
                                         UserMapper userMapper) {
        this.userFindByEmailClient = userFindByEmailClient;
        this.userMapper = userMapper;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log.info(() -> "Calling user-service to find user by email: %s".formatted(email));
        try {
            var vm = new UserFindByEmailViewModel(email);
            return Optional.ofNullable(userFindByEmailClient.findByEmail(vm))
                    .map(userMapper);
        } catch (NotFoundException e) {
            return Optional.empty();
        }
    }
}
