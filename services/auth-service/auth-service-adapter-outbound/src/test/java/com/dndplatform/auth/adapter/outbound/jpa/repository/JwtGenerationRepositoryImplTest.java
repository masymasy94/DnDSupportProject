package com.dndplatform.auth.adapter.outbound.jpa.repository;

import com.dndplatform.auth.domain.model.RefreshToken;
import com.dndplatform.auth.domain.model.User;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith({MockitoExtension.class, RandomExtension.class})
class JwtGenerationRepositoryImplTest {

    private static final String TEST_ISSUER = "test-issuer";
    private static final long ACCESS_TOKEN_EXPIRY_SECONDS = 3600L;

    private JwtGenerationRepositoryImpl sut;

    @BeforeEach
    void setUp() {
        sut = new JwtGenerationRepositoryImpl(TEST_ISSUER, ACCESS_TOKEN_EXPIRY_SECONDS);
    }

    @Test
    void shouldReturnTokenPairWithCorrectUserIdAndTokenValues(@Random User user, @Random RefreshToken refreshToken) {
        String fakeAccessToken = "fake.access.token";

        try (MockedStatic<Jwt> jwtStatic = Mockito.mockStatic(Jwt.class)) {
            JwtClaimsBuilder builder = mock(JwtClaimsBuilder.class, Mockito.RETURNS_SELF);
            given(builder.sign()).willReturn(fakeAccessToken);
            jwtStatic.when(() -> Jwt.issuer(anyString())).thenReturn(builder);

            var result = sut.generateTokenPair(user, refreshToken);

            assertThat(result.accessToken()).isEqualTo(fakeAccessToken);
            assertThat(result.refreshToken()).isEqualTo(refreshToken.token());
            assertThat(result.userId()).isEqualTo(user.id());
        }
    }

    @Test
    void shouldSetAccessTokenExpiryBasedOnConfiguredSeconds(@Random User user, @Random RefreshToken refreshToken) {
        String fakeAccessToken = "fake.access.token";

        try (MockedStatic<Jwt> jwtStatic = Mockito.mockStatic(Jwt.class)) {
            JwtClaimsBuilder builder = mock(JwtClaimsBuilder.class, Mockito.RETURNS_SELF);
            given(builder.sign()).willReturn(fakeAccessToken);
            jwtStatic.when(() -> Jwt.issuer(anyString())).thenReturn(builder);

            long beforeMs = System.currentTimeMillis();
            var result = sut.generateTokenPair(user, refreshToken);
            long afterMs = System.currentTimeMillis();

            long expectedExpiryMs = beforeMs + (ACCESS_TOKEN_EXPIRY_SECONDS * 1000);
            assertThat(result.accessTokenExpiresAt())
                    .isBetween(expectedExpiryMs, afterMs + (ACCESS_TOKEN_EXPIRY_SECONDS * 1000));
        }
    }

    @Test
    void shouldSetRefreshTokenExpiryFromRefreshTokenExpiresAt(@Random User user) {
        LocalDateTime expiresAt = LocalDateTime.of(2027, 6, 15, 12, 0, 0);
        RefreshToken refreshToken = new RefreshToken("token123", user.id(), expiresAt, false, LocalDateTime.now());
        String fakeAccessToken = "fake.access.token";

        try (MockedStatic<Jwt> jwtStatic = Mockito.mockStatic(Jwt.class)) {
            JwtClaimsBuilder builder = mock(JwtClaimsBuilder.class, Mockito.RETURNS_SELF);
            given(builder.sign()).willReturn(fakeAccessToken);
            jwtStatic.when(() -> Jwt.issuer(anyString())).thenReturn(builder);

            var result = sut.generateTokenPair(user, refreshToken);

            long expectedRefreshExpiry = expiresAt.toInstant(ZoneOffset.UTC).toEpochMilli();
            assertThat(result.refreshTokenExpiresAt()).isEqualTo(expectedRefreshExpiry);
        }
    }

    @Test
    void shouldUseConfiguredIssuerWhenBuildingAccessToken(@Random User user, @Random RefreshToken refreshToken) {
        String fakeAccessToken = "fake.access.token";

        try (MockedStatic<Jwt> jwtStatic = Mockito.mockStatic(Jwt.class)) {
            JwtClaimsBuilder builder = mock(JwtClaimsBuilder.class, Mockito.RETURNS_SELF);
            given(builder.sign()).willReturn(fakeAccessToken);
            jwtStatic.when(() -> Jwt.issuer(anyString())).thenReturn(builder);

            sut.generateTokenPair(user, refreshToken);

            jwtStatic.verify(() -> Jwt.issuer(TEST_ISSUER));
        }
    }

    @Test
    void shouldPassUserIdAsSubjectWhenBuildingAccessToken(@Random User user, @Random RefreshToken refreshToken) {
        String fakeAccessToken = "signed.jwt";

        try (MockedStatic<Jwt> jwtStatic = Mockito.mockStatic(Jwt.class)) {
            JwtClaimsBuilder builder = mock(JwtClaimsBuilder.class, Mockito.RETURNS_SELF);
            given(builder.sign()).willReturn(fakeAccessToken);
            jwtStatic.when(() -> Jwt.issuer(anyString())).thenReturn(builder);

            sut.generateTokenPair(user, refreshToken);

            org.mockito.BDDMockito.then(builder).should().subject(String.valueOf(user.id()));
        }
    }
}
