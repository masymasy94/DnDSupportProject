package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.adapter.outbound.jpa.entity.PasswordResetEntityBuilder;
import com.dndplatform.auth.domain.model.PasswordResetToken;
import com.dndplatform.auth.domain.model.PasswordResetTokenBuilder;
import com.dndplatform.auth.domain.repository.PasswordResetTokenCreateRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.LocalDateTime;
import java.util.logging.Logger;

import static com.dndplatform.auth.adapter.outbound.jpa.util.TokenUtil.generateOtpCode;
import static com.dndplatform.auth.adapter.outbound.jpa.util.TokenUtil.sha256;

@Transactional
@ApplicationScoped
public class PasswordResetTokenCreateRepositoryJpa implements PasswordResetTokenCreateRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final PasswordResetPanacheRepository panacheRepository;
    private final long tokenExpiryMinutes;

    @Inject
    public PasswordResetTokenCreateRepositoryJpa(PasswordResetPanacheRepository panacheRepository,
                                                 @ConfigProperty(name = "password-reset.token-expiry-minutes", defaultValue = "15") long tokenExpiryMinutes) {
        this.panacheRepository = panacheRepository;
        this.tokenExpiryMinutes = tokenExpiryMinutes;
    }

    @Override
    public PasswordResetToken create(long userId) {
        log.info(() -> "Creating password reset OTP for user id: %s".formatted(userId));

        var otpCode = generateOtpCode();
        var hashedCode = sha256(otpCode);
        var now = LocalDateTime.now();

        var entity = PasswordResetEntityBuilder.builder()
                .withToken(hashedCode)
                .withUserId(userId)
                .withExpiresAt(now.plusMinutes(tokenExpiryMinutes))
                .withUsed(false)
                .withCreatedAt(now)
                .build();
        panacheRepository.persist(entity);

        return PasswordResetTokenBuilder.builder()
                .withId(entity.id)
                .withToken(otpCode)
                .withUserId(userId)
                .withExpiresAt(entity.expiresAt)
                .withUsed(false)
                .withCreatedAt(now)
                .build();
    }
}
