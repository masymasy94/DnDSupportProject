package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.domain.JwtGenerationRepository;
import com.dndplatform.auth.domain.model.CreateLoginTokenResponse;
import com.dndplatform.auth.domain.model.RefreshToken;
import com.dndplatform.auth.domain.model.User;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.logging.Logger;

@ApplicationScoped
public class JwtGenerationRepositoryImpl implements JwtGenerationRepository {

    private final Logger log = Logger.getLogger(getClass().getName());
    @ConfigProperty(name = "jwt.issuer", defaultValue = "dnd-platform")
    String issuer;
    @ConfigProperty(name = "jwt.access-token-expiry-seconds", defaultValue = "3600")
    long accessTokenExpirySeconds;



    @Override
    public CreateLoginTokenResponse generateTokenPair(User user, RefreshToken refreshToken) {

        log.info(() -> "Generating JWT tokens for user %s".formatted(user.username()));

        Instant now = Instant.now();
        Instant accessTokenExpiry = now.plusSeconds(accessTokenExpirySeconds);
        Instant refreshTokenExpiry = refreshToken.expiresAt().toInstant(ZoneOffset.UTC);
        String accessToken = createAccessToken(user, now, accessTokenExpiry);
        String refreshTokenJwt = refreshToken.token();

        return createResponse(accessToken, refreshTokenJwt, accessTokenExpiry, refreshTokenExpiry, user.id());
    }




    private static CreateLoginTokenResponse createResponse(String accessToken, String refreshTokenJwt, Instant accessTokenExpiry, Instant refreshTokenExpiry, Long id) {
        return new CreateLoginTokenResponse(
                accessToken,
                refreshTokenJwt,
                accessTokenExpiry.toEpochMilli(),
                refreshTokenExpiry.toEpochMilli(),
                id
        );
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
