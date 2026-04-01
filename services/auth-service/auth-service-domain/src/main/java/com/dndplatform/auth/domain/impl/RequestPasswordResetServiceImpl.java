package com.dndplatform.auth.domain.impl;

import com.dndplatform.auth.domain.RequestPasswordResetService;
import com.dndplatform.auth.domain.model.RequestPasswordReset;
import com.dndplatform.auth.domain.repository.PasswordResetEmailSendRepository;
import com.dndplatform.auth.domain.repository.PasswordResetTokenCreateRepository;
import com.dndplatform.auth.domain.repository.UserFindByEmailRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class RequestPasswordResetServiceImpl implements RequestPasswordResetService {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final UserFindByEmailRepository userFindByEmailRepository;
    private final PasswordResetTokenCreateRepository passwordResetTokenCreateRepository;
    private final PasswordResetEmailSendRepository passwordResetEmailSendRepository;

    @Inject
    public RequestPasswordResetServiceImpl(UserFindByEmailRepository userFindByEmailRepository,
                                           PasswordResetTokenCreateRepository passwordResetTokenCreateRepository,
                                           PasswordResetEmailSendRepository passwordResetEmailSendRepository) {
        this.userFindByEmailRepository = userFindByEmailRepository;
        this.passwordResetTokenCreateRepository = passwordResetTokenCreateRepository;
        this.passwordResetEmailSendRepository = passwordResetEmailSendRepository;
    }

    @Override
    public void requestPasswordReset(RequestPasswordReset request) {
        log.info(() -> "Processing password reset request for email: %s".formatted(request.email()));

        var userOpt = userFindByEmailRepository.findByEmail(request.email());
        if (userOpt.isEmpty()) {
            log.info(() -> "No user found for email: %s — returning silently to prevent enumeration".formatted(request.email()));
            return;
        }

        var user = userOpt.get();
        var token = passwordResetTokenCreateRepository.create(user.id());
        passwordResetEmailSendRepository.sendResetEmail(user.email(), token.token());

        log.info(() -> "Password reset email queued for user id: %s".formatted(user.id()));
    }
}
