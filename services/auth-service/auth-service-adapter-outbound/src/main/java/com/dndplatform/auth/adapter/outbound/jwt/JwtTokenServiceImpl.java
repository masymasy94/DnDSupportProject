package com.dndplatform.auth.adapter.outbound.jwt;

import com.dndplatform.auth.domain.model.CreateLoginTokenResponse;
import com.dndplatform.auth.domain.model.RefreshToken;
import com.dndplatform.auth.domain.model.User;
import com.dndplatform.auth.domain.service.JwtTokenService;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.logging.Logger;

@ApplicationScoped
public class JwtTokenServiceImpl implements JwtTokenService {

    private final Logger log = Logger.getLogger(getClass().getName());
    @ConfigProperty(name = "jwt.issuer", defaultValue = "dnd-platform")
    String issuer;
    @ConfigProperty(name = "jwt.access-token-expiry-seconds", defaultValue = "3600")
    long accessTokenExpirySeconds;
    @ConfigProperty(name = "jwt.refresh-token-expiry-days", defaultValue = "30")
    long refreshTokenExpiryDays;


    @Override
    public CreateLoginTokenResponse generateTokenPair(User user, RefreshToken refreshToken) {

        log.info(() -> "Generating JWT tokens for user %s".formatted(user.username()));

        Instant now = Instant.now();
        Instant accessTokenExpiry = now.plusSeconds(accessTokenExpirySeconds);
        Instant refreshTokenExpiry = now.plus(Duration.ofDays(refreshTokenExpiryDays));

        String accessToken = createAccessToken(user, now, accessTokenExpiry);
        String refreshTokenJwt = createRefreshToken(user, refreshToken, now, refreshTokenExpiry);

        return createResponse(accessToken, refreshTokenJwt, accessTokenExpiry, refreshTokenExpiry);
    }







    private static CreateLoginTokenResponse createResponse(String accessToken, String refreshTokenJwt, Instant accessTokenExpiry, Instant refreshTokenExpiry) {
        return new CreateLoginTokenResponse(
                accessToken,
                refreshTokenJwt,
                accessTokenExpiry.toEpochMilli(),
                refreshTokenExpiry.toEpochMilli()
        );
    }

    private String createRefreshToken(User user, RefreshToken refreshToken, Instant now, Instant refreshTokenExpiry) {
        return Jwt.issuer(issuer)
                .subject(String.valueOf(user.id()))
                .claim("type", "refresh")
                .claim("jti", refreshToken.token())
                .issuedAt(now)
                .expiresAt(refreshTokenExpiry)
                .sign();
    }

    private String createAccessToken(User user, Instant now, Instant accessTokenExpiry) {
        return Jwt.issuer(issuer)
                .subject(String.valueOf(user.id()))
                .upn(user.email())
                .claim("username", user.username())
                .claim("role", user.role())
                .claim("type", "access")
                .groups(Set.of(user.role()))
                .issuedAt(now)
                .expiresAt(accessTokenExpiry)
                .sign();
    }
}
