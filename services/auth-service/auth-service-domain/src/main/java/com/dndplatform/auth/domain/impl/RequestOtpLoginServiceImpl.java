package com.dndplatform.auth.domain.impl;

import com.dndplatform.auth.domain.RequestOtpLoginService;
import com.dndplatform.auth.domain.model.RequestOtpLogin;
import com.dndplatform.auth.domain.repository.OtpLoginEmailSendRepository;
import com.dndplatform.auth.domain.repository.OtpLoginTokenCreateRepository;
import com.dndplatform.auth.domain.repository.UserFindByEmailRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class RequestOtpLoginServiceImpl implements RequestOtpLoginService {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final UserFindByEmailRepository userFindByEmailRepository;
    private final OtpLoginTokenCreateRepository otpLoginTokenCreateRepository;
    private final OtpLoginEmailSendRepository otpLoginEmailSendRepository;

    @Inject
    public RequestOtpLoginServiceImpl(UserFindByEmailRepository userFindByEmailRepository,
                                      OtpLoginTokenCreateRepository otpLoginTokenCreateRepository,
                                      OtpLoginEmailSendRepository otpLoginEmailSendRepository) {
        this.userFindByEmailRepository = userFindByEmailRepository;
        this.otpLoginTokenCreateRepository = otpLoginTokenCreateRepository;
        this.otpLoginEmailSendRepository = otpLoginEmailSendRepository;
    }

    @Override
    public void requestOtpLogin(RequestOtpLogin request) {
        log.info(() -> "Processing OTP login request for email: %s".formatted(request.email()));

        var userOpt = userFindByEmailRepository.findByEmail(request.email());
        if (userOpt.isEmpty()) {
            log.info(() -> "No user found for email: %s — returning silently to prevent enumeration".formatted(request.email()));
            return;
        }

        var user = userOpt.get();
        var token = otpLoginTokenCreateRepository.create(user.id());
        otpLoginEmailSendRepository.sendOtpLoginEmail(user.email(), token.token());

        log.info(() -> "OTP login email queued for user id: %s".formatted(user.id()));
    }
}
