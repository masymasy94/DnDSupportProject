package com.dndplatform.auth.domain.impl;

import com.dndplatform.auth.domain.model.RefreshToken;
import com.dndplatform.auth.domain.repository.RefreshTokenCreateRepository;
import com.dndplatform.auth.domain.UserLoginService;
import com.dndplatform.auth.domain.repository.UserUpdateLastLoginByIdRepository;
import com.dndplatform.auth.domain.model.User;
import com.dndplatform.auth.domain.model.UserLogin;
import com.dndplatform.auth.domain.repository.UserFindByCredentialsRepository;
import com.dndplatform.auth.domain.util.CryptUtil;
import jakarta.annotation.Nonnull;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.UUID;

@ApplicationScoped
public class UserLoginServiceImpl implements UserLoginService {

    private final UserFindByCredentialsRepository userFindByCredentialsRepository;
    private final UserUpdateLastLoginByIdRepository userUpdateLastLoginByIdRepository;
    private final RefreshTokenCreateRepository refreshTokenCreateRepository;

    @Inject
    public UserLoginServiceImpl(UserFindByCredentialsRepository userFindByCredentialsRepository,
                                UserUpdateLastLoginByIdRepository userUpdateLastLoginByIdRepository,
                                RefreshTokenCreateRepository refreshTokenCreateRepository) {
        this.userFindByCredentialsRepository = userFindByCredentialsRepository;
        this.userUpdateLastLoginByIdRepository = userUpdateLastLoginByIdRepository;
        this.refreshTokenCreateRepository = refreshTokenCreateRepository;
    }

    @Override
    public String login(UserLogin userLogin) {

        // optional - check rate limiting 429 on redis

        var user = getUserByCredentials(userLogin);
        userUpdateLastLoginByIdRepository.updateLastLoginById(user.id());
        var refreshToken = refreshTokenCreateRepository.createRefreshToken(user.id());

        return generateJwtToken(user, refreshToken);

        // (OPZIONALE) REVOCA VECCHI TOKEN (keep only last N)

        //  PUBBLICA EVENTO
        //Send "New login detected" email (security)
        //Notification in-app "Welcome back!"
        //If you login from new device/location → Security alert
        //Count daily/weekly/monthly logins
        //Track peak hours
        //Identify more active users
        //Generate reports for admin
        //Permanent log of all logins (compliance, GDPR)
        //Track suspicious attempts
        //Forensics in case of breach
        //Reports for audits
        //Brand the user as "online" in his campaigns
        //Notify other players that someone is connected
        //Update UI in real-time


        // (OPZIONALE) RESET RATE LIMIT --   Redis: clear failed attempts
    }

    @Nonnull
    private User getUserByCredentials(UserLogin userLogin) {

        var passwordHash = CryptUtil.hashPassword(userLogin.password());
        var user = userFindByCredentialsRepository.findUserByCredentials(userLogin.username(), passwordHash)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials")); // TODO - custom exception 401

        if (!user.active())
            throw new IllegalArgumentException("User not active");// TODO - custom exception 403

        return user;
    }

    private String generateJwtToken(User user, RefreshToken refreshToken){
        return UUID.randomUUID().toString(); // todo - implement JWT generation
    }
}
