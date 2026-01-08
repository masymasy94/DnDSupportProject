package com.dndplatform.auth.adapter.outbound.jwt;

import com.dndplatform.auth.domain.model.RefreshToken;
import com.dndplatform.auth.domain.model.TokenPair;
import com.dndplatform.auth.domain.model.User;
import com.dndplatform.auth.domain.service.JwtTokenService;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

@ApplicationScoped
public class JwtTokenServiceImpl implements JwtTokenService {

    @ConfigProperty(name = "jwt.issuer", defaultValue = "dnd-platform")
    String issuer;

    @ConfigProperty(name = "jwt.access-token-expiry-seconds", defaultValue = "3600")
    long accessTokenExpirySeconds;

    @ConfigProperty(name = "jwt.refresh-token-expiry-days", defaultValue = "30")
    long refreshTokenExpiryDays;

    @Override
    public TokenPair generateTokenPair(User user, RefreshToken refreshToken) {
        Instant now = Instant.now();

        Instant accessTokenExpiry = now.plusSeconds(accessTokenExpirySeconds);

        String accessToken = Jwt.issuer(issuer)
                .subject(String.valueOf(user.id()))
                .upn(user.email())
                .claim("username", user.username())
                .claim("role", user.role())
                .claim("type", "access")
                .groups(Set.of(user.role()))
                .issuedAt(now)
                .expiresAt(accessTokenExpiry)
                .sign();

        Instant refreshTokenExpiry = now.plus(Duration.ofDays(refreshTokenExpiryDays));

        String refreshTokenJwt = Jwt.issuer(issuer)
                .subject(String.valueOf(user.id()))
                .claim("type", "refresh")
                .claim("jti", refreshToken.token())
                .issuedAt(now)
                .expiresAt(refreshTokenExpiry)
                .sign();

        return new TokenPair(
                accessToken,
                refreshTokenJwt,
                accessTokenExpiry.toEpochMilli(),
                refreshTokenExpiry.toEpochMilli()
        );
    }
}
