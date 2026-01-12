package com.dndplatform.auth.adapter.outbound.rest.repository;

import com.dndplatform.auth.adapter.outbound.rest.mapper.UserMapper;
import com.dndplatform.auth.domain.model.User;
import com.dndplatform.auth.domain.repository.UserFindByIdRepository;
import com.dndplatform.user.client.findbyid.UserFindByIdResourceRestClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class UserFindByIdRepositoryRest implements UserFindByIdRepository {

    private final UserFindByIdResourceRestClient userFindByIdResourceRestClient;
    private final UserMapper userMapper;

    @Inject
    public UserFindByIdRepositoryRest(@RestClient UserFindByIdResourceRestClient userFindByIdResourceRestClient, UserMapper userMapper) {
        this.userFindByIdResourceRestClient = userFindByIdResourceRestClient;
        this.userMapper = userMapper;
    }


    @Override
    public User findById(long userId) {
        var viewModel = userFindByIdResourceRestClient.findById(userId);
        return userMapper.apply(viewModel);
    }
}
