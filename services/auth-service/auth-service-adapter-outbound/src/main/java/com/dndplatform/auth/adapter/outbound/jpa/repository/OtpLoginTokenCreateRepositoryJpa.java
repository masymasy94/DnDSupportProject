package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.adapter.outbound.jpa.entity.OtpLoginEntity;
import com.dndplatform.auth.adapter.outbound.jpa.entity.OtpLoginEntityBuilder;
import com.dndplatform.auth.domain.model.OtpLoginToken;
import com.dndplatform.auth.domain.model.OtpLoginTokenBuilder;
import com.dndplatform.auth.domain.repository.OtpLoginTokenCreateRepository;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import static com.dndplatform.auth.adapter.outbound.jpa.util.TokenUtil.generateOtpCode;
import static com.dndplatform.auth.adapter.outbound.jpa.util.TokenUtil.sha256;

@Transactional
@ApplicationScoped
public class OtpLoginTokenCreateRepositoryJpa implements OtpLoginTokenCreateRepository, PanacheRepository<OtpLoginEntity> {

    private final Logger log = Logger.getLogger(getClass().getName());

    @ConfigProperty(name = "otp-login.token-expiry-minutes", defaultValue = "5")
    long tokenExpiryMinutes;

    @Override
    public OtpLoginToken create(long userId) {
        log.info(() -> "Creating OTP login code for user id: %s".formatted(userId));

        var otpCode = generateOtpCode();
        var hashedCode = sha256(otpCode);
        var now = LocalDateTime.now();

        var entity = OtpLoginEntityBuilder.builder()
                .withToken(hashedCode)
                .withUserId(userId)
                .withExpiresAt(now.plusMinutes(tokenExpiryMinutes))
                .withUsed(false)
                .withCreatedAt(now)
                .build();
        persist(entity);

        return OtpLoginTokenBuilder.builder()
                .withId(entity.id)
                .withToken(otpCode)
                .withUserId(userId)
                .withExpiresAt(entity.expiresAt)
                .withUsed(false)
                .withCreatedAt(now)
                .build();
    }
}
